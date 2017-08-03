package usagitoneko.nekof.Activity;


import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import usagitoneko.nekof.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityToJoyStick {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, false){};

    @Test
    public void mainActivityToJoyStick() {
        MainActivity mainActivity = mActivityTestRule.getActivity();

        ViewInteraction pwdInputView = onView(
                allOf(withId(R.id.password), isDisplayed()));
        pwdInputView.perform(click());

        ViewInteraction pwdInputView2 = onView(
                allOf(withId(R.id.password), isDisplayed()));
        pwdInputView2.perform(replaceText("8888"), closeSoftKeyboard());
        onView(withId(R.id.password)).check(matches(withText("8882")));

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.showPwSwitch), withText("Show Password"), isDisplayed()));
        switch_.perform(click());
        View view = mainActivity.findViewById(R.id.testForIntent);
        TextView textView = (TextView)view;
        assertThat(textView.getText().toString(), is("success!"));

        onView(withId(R.id.submitPassword)).perform(click());

        //in joystick activity
        onView(withId(R.id.uTurnButton)).perform(click());
        onView(withId(R.id.forceStopButton)).perform(click());
    }



    @Test
    public void onNewIntentDiscovered(){
        MainActivity mainActivity = mActivityTestRule.getActivity();
        Intent intent = new Intent(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mActivityTestRule.launchActivity(intent);
        /*View viewById = mainActivity.findViewById(R.id.testForIntent);
        assertThat*/
        /*View view = mainActivity.findViewById(R.id.testForIntent);
        TextView textView = (TextView)view;
        assertThat(textView.getText().toString(), is("success!"));*/
//        onView(withId(R.id.testForIntent)).check(matches(withText("success!")));
    }

}
