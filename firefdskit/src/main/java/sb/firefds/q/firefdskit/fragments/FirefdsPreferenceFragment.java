package sb.firefds.q.firefdskit.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;

import sb.firefds.q.firefdskit.R;
import sb.firefds.q.firefdskit.notifications.RebootNotification;
import sb.firefds.q.firefdskit.utils.Utils;

import static sb.firefds.q.firefdskit.FirefdsKitActivity.getSharedPreferences;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_SCREEN_TIMEOUT_HOURS;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_SCREEN_TIMEOUT_MINUTES;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_SCREEN_TIMEOUT_SECONDS;

public class FirefdsPreferenceFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static AppCompatActivity fragmentActivity;
    private static List<String> changesMade;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = (AppCompatActivity) getActivity();
        if (changesMade == null) {
            changesMade = new ArrayList<>();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        try {
            // No reboot notification required
            String[] litePrefs = fragmentActivity.getResources().getStringArray(R.array.lite_preferences);

            setTimeoutPrefs(sharedPreferences, key);

            for (String string : litePrefs) {
                if (key.equalsIgnoreCase(string)) {
                    return;
                }
            }

            // Add preference key to changed keys list
            if (!changesMade.contains(key)) {
                changesMade.add(key);
            }
            RebootNotification.notify(fragmentActivity, changesMade.size(), true);
        } catch (Throwable e) {
            Utils.log(e);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onResume() {
        super.onResume();
        registerPrefsReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterPrefsReceiver();
    }

    private void setTimeoutPrefs(SharedPreferences sharedPreferences, String key) {

        if (key.equals(PREF_SCREEN_TIMEOUT_SECONDS) ||
                key.equals(PREF_SCREEN_TIMEOUT_MINUTES) ||
                key.equals(PREF_SCREEN_TIMEOUT_HOURS)) {

            int hour = sharedPreferences.getInt(PREF_SCREEN_TIMEOUT_HOURS, 0) * 3600000;
            int min = sharedPreferences.getInt(PREF_SCREEN_TIMEOUT_MINUTES, 0) * 60000;
            int sec = sharedPreferences.getInt(PREF_SCREEN_TIMEOUT_SECONDS, 30) * 1000;
            int timeoutML = hour + min + sec;
            Settings.System.putInt(fragmentActivity.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeoutML);
        }
    }

    private void registerPrefsReceiver() {
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void unregisterPrefsReceiver() {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
