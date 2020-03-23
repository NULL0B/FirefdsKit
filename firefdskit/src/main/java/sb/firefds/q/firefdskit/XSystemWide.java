package sb.firefds.q.firefdskit;

import android.os.PowerManager;
import android.os.UserManager;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.samsung.android.feature.SemCscFeature;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.q.firefdskit.utils.Constants.ENABLE_CALL_RECORDING;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_DEFAULT_REBOOT_BEHAVIOR;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_DISABLE_SECURE_FLAG;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_ENABLE_CALL_ADD;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_ENABLE_CALL_RECORDING;

public class XSystemWide {

    public static void doHook(RemotePreferences prefs) {

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
                        });
            }

            XposedHelpers.findAndHookMethod(SemCscFeature.class,
                    "getString",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (param.args[0].equals(ENABLE_CALL_RECORDING)) {
                                if (prefs.getBoolean(PREF_ENABLE_CALL_RECORDING, false)) {
                                    if (prefs.getBoolean(PREF_ENABLE_CALL_ADD, false)) {
                                        param.setResult("RecordingAllowedByMenu");
                                    } else {
                                        param.setResult("RecordingAllowed");
                                    }
                                } else {
                                    param.setResult("");
                                }
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(SemCscFeature.class,
                    "getString",
                    String.class,
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (param.args[0].equals(ENABLE_CALL_RECORDING)) {
                                if (prefs.getBoolean(PREF_ENABLE_CALL_RECORDING, false)) {
                                    if (prefs.getBoolean(PREF_ENABLE_CALL_ADD, false)) {
                                        param.setResult("RecordingAllowedByMenu");
                                    } else {
                                        param.setResult("RecordingAllowed");
                                    }
                                } else {
                                    param.setResult("");
                                }
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }
}
