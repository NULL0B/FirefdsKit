package sb.firefds.q.firefdskit.fragments;

import android.os.Bundle;

import sb.firefds.q.firefdskit.R;

public class LockscreenSettingsFragment extends FirefdsPreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setStorageDeviceProtected();
        setPreferencesFromResource(R.xml.lockscreen_settings, rootKey);
    }
}
