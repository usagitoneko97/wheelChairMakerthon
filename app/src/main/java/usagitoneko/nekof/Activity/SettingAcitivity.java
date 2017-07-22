package usagitoneko.nekof.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import usagitoneko.nekof.R;
import usagitoneko.nekof.fragments.SettingsFragment;

public class SettingAcitivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
