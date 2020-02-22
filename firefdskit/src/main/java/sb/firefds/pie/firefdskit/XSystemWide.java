package sb.firefds.pie.firefdskit;

import android.os.PowerManager;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DEFAULT_REBOOT_BEHAVIOR;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_SECURE_FLAG;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS;

public class XSystemWide {

    //private final static String CSC_PARSER_CLASS = "android.net.wifi.CscParser";
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

            /*XposedHelpers.findAndHookMethod(SemCscFeature.class,
                    "getBoolean",
                    String.class,
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedBridge.log("FFK: before getBoolean");
                            if (prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false)) {
                                String tag = (String) param.args[0];
                                XposedBridge.log("FFK: changing getBoolean " + tag);
                                switch (tag) {
                                    case "CscFeature_Wifi_SupportMenuMobileApMaxClient":
                                    case "CscFeature_Wifi_SupportMobileAp5G":
                                    case "CscFeature_Wifi_SupportMobileAp5GBasedOnCountry":
                                        XposedBridge.log("FFK: Setting getBoolean for " + tag + " TRUE");
                                        param.setResult(true);
                                        break;
                                }
                            }
                        }
                    });*/


            /*XposedHelpers.findAndHookMethod(CSC_PARSER_CLASS,
                    null,
                    "get",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false)) {
                                String path = (String) param.args[0];
                                if (path.equals("GeneralInfo.Region")) {
                                    param.setResult("NA");
                                }
                            }
                        }
                    });*/

            Class<?> WifiApCustClass = XposedHelpers.findClass(WIFI_AP_CUST_CLASS, null);
            XposedHelpers.findAndHookMethod(WifiApCustClass,
                    "getInstance",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            XposedBridge.log("FFK: After getInstance");
                            if (prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false)) {
                                XposedBridge.log("FFK: Setting static fields NA");
                                XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupportMaxClientMenu", true);
                                XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5G", true);
                                XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5GBasedOnCountry", true);
                                XposedHelpers.setStaticObjectField(WifiApCustClass, "mRegion", "NA");
                            }
                        }
                    });

            /*Class<?> WifiApCustClass = XposedHelpers.findClass(WIFI_AP_CUST_CLASS, null);
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupportMaxClientMenu",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupportMaxClientMenu"));
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5G",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupport5G"));
            XposedHelpers.setStaticBooleanField(WifiApCustClass, "mSupport5GBasedOnCountry",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) || XposedHelpers.getStaticBooleanField(WifiApCustClass, "mSupport5GBasedOnCountry"));
            XposedHelpers.setStaticObjectField(WifiApCustClass, "mRegion",
                    prefs.getBoolean(PREF_ENABLE_ADVANCED_HOTSPOT_OPTIONS, false) ? "NA" : XposedHelpers.getStaticObjectField(WifiApCustClass, "mRegion"));*/
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }
}
