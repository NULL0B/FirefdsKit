package sb.firefds.pie.firefdskit.providers;

import com.crossbowffs.remotepreferences.RemotePreferenceFile;
import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import sb.firefds.pie.firefdskit.BuildConfig;

public class XRemotePreferenceProvider extends RemotePreferenceProvider {
    public XRemotePreferenceProvider() {
        super("sb.firefds.pie.firefdskit.preferences", new RemotePreferenceFile[]{
                new RemotePreferenceFile(BuildConfig.APPLICATION_ID + "_preferences", true)});
    }
}
