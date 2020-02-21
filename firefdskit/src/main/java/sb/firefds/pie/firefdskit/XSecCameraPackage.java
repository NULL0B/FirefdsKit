/*
 * Copyright (C) 2016 Mohamed Karami for XTouchWiz Project (Wanam@xda)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sb.firefds.pie.firefdskit;

import android.content.SharedPreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Packages.CAMERA;
import static sb.firefds.pie.firefdskit.utils.Packages.SAMSUNG_CAMERA;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_TEMPERATURE_CHECKS;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_CAMERA_SHUTTER_MENU;

public class XSecCameraPackage {

    private static final String FEATURE = SAMSUNG_CAMERA + ".feature.Feature";
    private static final String CAMERA_TEMPERATURE_MANAGER = CAMERA + ".provider.CameraTemperatureManager";
    private static final String PREFERENCE_SETTING_FRAGMENT = CAMERA + ".setting.PreferenceSettingFragment";

    public static void doHook(final SharedPreferences prefs, ClassLoader classLoader) {

        final Class<?> cameraFeatureClass = XposedHelpers.findClass(FEATURE, classLoader);

        if (prefs.getBoolean(PREF_DISABLE_TEMPERATURE_CHECKS, false)) {
            try {
                XposedHelpers.findAndHookMethod(CAMERA_TEMPERATURE_MANAGER,
                        classLoader,
                        "start",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                XposedHelpers.setStaticBooleanField(cameraFeatureClass, "SUPPORT_THERMISTOR_TEMPERATURE", false);
                            }
                        });
            } catch (Throwable e) {
                XposedBridge.log(e);
            }
        }

        try {
            XposedHelpers.findAndHookMethod(PREFERENCE_SETTING_FRAGMENT,
                    classLoader,
                    "updateFeaturedPreference",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            XposedHelpers.setStaticBooleanField(cameraFeatureClass,
                                    "ENABLE_SHUTTER_SOUND_MENU",
                                    prefs.getBoolean(PREF_ENABLE_CAMERA_SHUTTER_MENU, false));
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }
}
