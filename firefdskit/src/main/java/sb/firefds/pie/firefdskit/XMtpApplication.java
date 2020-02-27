package sb.firefds.pie.firefdskit;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Packages.MTP_APPLICATION;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_HIDE_MTP_NOTIFICATION;

public class XMtpApplication {

    private static final String USB_CONNECTION = MTP_APPLICATION + ".USBConnection";

    public static void doHook(RemotePreferences prefs, ClassLoader classLoader) {

        if (prefs.getBoolean(PREF_HIDE_MTP_NOTIFICATION, false)) {
            try {
                XposedHelpers.findAndHookMethod(USB_CONNECTION,
                        classLoader,
                        "showDiaglog",
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                Object mReceiver = XposedHelpers.getObjectField(param.thisObject, "mReceiver");
                                XposedHelpers.callMethod(mReceiver, "changeMtpMode");
                                XposedHelpers.callMethod(param.thisObject, "finish");
                            }
                        });
            } catch (Throwable e) {
                XposedBridge.log(e);
            }
        }
    }
}