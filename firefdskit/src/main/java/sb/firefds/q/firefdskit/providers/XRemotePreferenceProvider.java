package sb.firefds.q.firefdskit.providers;

import com.crossbowffs.remotepreferences.RemotePreferenceFile;
import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import static sb.firefds.q.firefdskit.utils.Constants.PREFS;

public class XRemotePreferenceProvider extends RemotePreferenceProvider {
    public XRemotePreferenceProvider() {
        super("sb.firefds.q.firefdskit.preferences", new RemotePreferenceFile[]{
                new RemotePreferenceFile(PREFS, true)});
    }
}