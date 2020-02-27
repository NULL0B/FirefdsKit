package sb.firefds.pie.firefdskit;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_TRANSITION_EFFECT;

public class XTouchwizLauncherPackage {

    private static final String PAGE_TRANSITION_MANAGER =
            "com.android.launcher3.framework.view.features.pagetransition.PageTransitionManager";

    public static void doHook(RemotePreferences prefs, ClassLoader classLoader) {
        int transEffect = Integer.parseInt(prefs.getString(PREF_TRANSITION_EFFECT, "0"));
        if (transEffect != 0) {
            try {
                XposedHelpers.findAndHookMethod(PAGE_TRANSITION_MANAGER,
                        classLoader,
                        "setCurrentTransitionEffect",
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                param.args[0] = transEffect;
                            }
                        });

            } catch (Throwable e) {
                XposedBridge.log(e);
            }
        }
    }
}
