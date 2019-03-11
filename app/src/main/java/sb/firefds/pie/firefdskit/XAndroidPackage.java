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

import android.content.pm.Signature;


import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Preferences.*;

public class XAndroidPackage {

    private static final String WINDOW_STATE_CLASS = "android.server.wm.WindowState";
    private static final String WINDOW_MANAGER_SERVICE_CLASS =
            "android.server.wm.WindowManagerService";
    private static final String PACKAGE_MANAGER_SERVICE_UTILS_CLASS =
            "android.server.pm.PackageManagerServiceUtils";

    public static void doHook(final XSharedPreferences prefs, final ClassLoader classLoader) {


        try {
            if (prefs.getBoolean(PREF_DISABLE_SECURE_FLAG, false)) {
                Class<?> windowStateClass = XposedHelpers.findClass(WINDOW_STATE_CLASS, classLoader);

                XposedHelpers.findAndHookMethod(WINDOW_MANAGER_SERVICE_CLASS,
                        classLoader,
                        "isSecureLocked",
                        windowStateClass,
                        XC_MethodReplacement.returnConstant(Boolean.FALSE));
            }

            if (prefs.getBoolean(PREF_DISABLE_SIGNATURE_CHECK, false)) {
                XposedHelpers.findAndHookMethod(PACKAGE_MANAGER_SERVICE_UTILS_CLASS,
                        classLoader,
                        "compareSignatures",
                        Signature[].class,
                        Signature[].class,
                        XC_MethodReplacement.returnConstant(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
