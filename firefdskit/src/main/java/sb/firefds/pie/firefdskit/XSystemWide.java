package sb.firefds.pie.firefdskit;

import android.os.PowerManager;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.crossbowffs.remotepreferences.RemotePreferenceAccessException;
import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DEFAULT_REBOOT_BEHAVIOR;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_SECURE_FLAG;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS;

public class XSystemWide {

    private final static String WIFI_AP_CUST_CLASS = "android.net.wifi.WifiApCust";

    public static void doHook(final RemotePreferences prefs) {
        try {
            XposedHelpers.findAndHookMethod(Window.class,
                    "setFlags",
                    int.class,
                    int.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DISABLE_SECURE_FLAG, false)) {
                                Integer flags = (Integer) param.args[0];
                                flags &= ~WindowManager.LayoutParams.FLAG_SECURE;
                                param.args[0] = flags;
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(SurfaceView.class,
                    "setSecure",
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DISABLE_SECURE_FLAG, false)) {
                                param.args[0] = false;
                            }
                        }
                    });


            XposedHelpers.findAndHookMethod(PowerManager.class,
                    "reboot",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DEFAULT_REBOOT_BEHAVIOR, false)) {
                                if (param.args[0] == null) {
                                    param.args[0] = "recovery";
                                }
                            }
                        }
                    });

            Class<?> WifiApCustClass = XposedHelpers.findClass(WIFI_AP_CUST_CLASS, null);
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupportMaxClientMenu",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupportMaxClientMenu"));
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5G",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupport5G"));
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5GBasedOnCountry",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupport5GBasedOnCountry"));
            XposedHelpers.setStaticObjectField(WifiApCustClass, "mRegion",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) ? "NA" : XposedHelpers.getStaticObjectField(WifiApCustClass, "mRegion"));
        } catch (Throwable e) {
            if (!(e instanceof RemotePreferenceAccessException)) {
                XposedBridge.log(e);
            }
        }
    }
}
