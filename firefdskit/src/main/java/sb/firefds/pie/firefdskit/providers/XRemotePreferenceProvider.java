package sb.firefds.pie.firefdskit.providers;

import com.crossbowffs.remotepreferences.RemotePreferenceFile;
import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import static sb.firefds.pie.firefdskit.utils.Constants.PREFS;
import static sb.firefds.pie.firefdskit.utils.Constants.PREFS_AUTHORITY;

public class XRemotePreferenceProvider extends RemotePreferenceProvider {
    public XRemotePreferenceProvider() {
        super(PREFS_AUTHORITY, new RemotePreferenceFile[]{
                new RemotePreferenceFile(PREFS, true)});
    }
}
