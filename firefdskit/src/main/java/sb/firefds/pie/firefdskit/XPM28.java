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

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import sb.firefds.pie.firefdskit.utils.Packages;

public class XPM28 {
    private static final String CLASS_PERMISSION_MANAGER_SERVICE =
            "com.android.server.pm.permission.PermissionManagerService";
    private static final String CLASS_PACKAGE_PARSER_PACKAGE =
            "android.content.pm.PackageParser.Package";
    private static final String CLASS_PERMISSION_CALLBACK =
            "com.android.server.pm.permission.PermissionManagerInternal.PermissionCallback";

    private static final String REBOOT = "android.permission.REBOOT";
    private static final String WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";
    private static final String STATUSBAR = "android.permission.EXPAND_STATUS_BAR";

    public static void doHook(final ClassLoader classLoader) {
        try {
            final Class<?> pmServiceClass =
                    XposedHelpers.findClass(CLASS_PERMISSION_MANAGER_SERVICE, classLoader);
            final Class<?> pmCallbackClass =
                    XposedHelpers.findClass(CLASS_PERMISSION_CALLBACK, classLoader);

            XposedHelpers.findAndHookMethod(pmServiceClass,
                    "grantPermissions",
                    CLASS_PACKAGE_PARSER_PACKAGE,
                    boolean.class,
                    String.class,
                    pmCallbackClass,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            final String pkgName = (String) XposedHelpers.getObjectField(param.args[0],
                                    "packageName");
                            if (Packages.FIREFDSKIT.equals(pkgName)) {
                                final Object extras = XposedHelpers.getObjectField(param.args[0], "mExtras");
                                final Object ps = XposedHelpers.callMethod(extras, "getPermissionsState");
                                final Object settings = XposedHelpers.getObjectField(param.thisObject, "mSettings");
                                final Object permissions = XposedHelpers.getObjectField(settings, "mPermissions");

                                if (!(Boolean) XposedHelpers.callMethod(ps, "hasInstallPermission", REBOOT)) {
                                    final Object pAccess = XposedHelpers.callMethod(permissions, "get", REBOOT);
                                    XposedHelpers.callMethod(ps, "grantInstallPermission", pAccess);
                                }

                                if (!(Boolean) XposedHelpers.callMethod(ps, "hasInstallPermission", STATUSBAR)) {
                                    final Object pAccess = XposedHelpers.callMethod(permissions, "get", STATUSBAR);
                                    XposedHelpers.callMethod(ps, "grantInstallPermission", pAccess);
                                }

                                if (!(Boolean) XposedHelpers.callMethod(ps, "hasInstallPermission", WRITE_SETTINGS)) {
                                    final Object pAccess = XposedHelpers.callMethod(permissions, "get", WRITE_SETTINGS);
                                    XposedHelpers.callMethod(ps, "grantInstallPermission", pAccess);
                                }
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }
}
