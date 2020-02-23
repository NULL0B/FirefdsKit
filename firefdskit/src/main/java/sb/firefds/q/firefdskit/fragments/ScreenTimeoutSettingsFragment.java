package sb.firefds.q.firefdskit.fragments;

import android.os.Bundle;

import androidx.annotation.Keep;

import sb.firefds.q.firefdskit.R;

@Keep
public class ScreenTimeoutSettingsFragment extends FirefdsPreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setStorageDeviceProtected();
        setPreferencesFromResource(R.xml.screen_timeout_settings, rootKey);
    }
}
