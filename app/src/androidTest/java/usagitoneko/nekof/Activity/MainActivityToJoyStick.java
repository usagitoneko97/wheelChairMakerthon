package usagitoneko.nekof.Activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityToJoyStick {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void mainActivityToJoyStick() {
        ViewInteraction pwdInputView = onView(
                allOf(withId(R.id.password), isDisplayed()));
        pwdInputView.perform(click());

        ViewInteraction pwdInputView2 = onView(
                allOf(withId(R.id.password), isDisplayed()));
        pwdInputView2.perform(replaceText("8888"), closeSoftKeyboard());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.showPwSwitch), withText("Show Password"), isDisplayed()));
        switch_.perform(click());

        ViewInteraction fancyButton = onView(
                allOf(withId(R.id.submitPassword), withText("Connect"), isDisplayed()));
        fancyButton.perform(click());

        Intent intent = new Intent();
        intent.putExtra("EXTRA", "Test");
        mActivityTestRule.launchActivity(intent);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Settings"), isDisplayed()));
        appCompatTextView.perform(click());

        pressBack();

    }

}
