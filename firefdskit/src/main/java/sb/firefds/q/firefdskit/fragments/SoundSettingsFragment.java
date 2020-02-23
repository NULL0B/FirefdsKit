package sb.firefds.q.firefdskit.fragments;

import android.os.Bundle;

import sb.firefds.q.firefdskit.R;

public class SoundSettingsFragment extends FirefdsPreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setStorageDeviceProtected();
        setPreferencesFromResource(R.xml.sound_settings, rootKey);
    }
}
