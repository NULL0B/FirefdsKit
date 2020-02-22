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

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.UserManager;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DEFAULT_REBOOT_BEHAVIOR;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_HIDE_USB_NOTIFICATION;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_HIDE_VOLTE_ICON;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_MAX_SUPPORTED_USERS;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_SUPPORTS_MULTIPLE_USERS;

public class XAndroidPackage {

    /* private static final String PACKAGE_MANAGER_SERVICE_UTILS = "com.android.server.pm.PackageManagerServiceUtils";
     private static final String PACKAGE_MANAGER_SERVICE = "com.android.server.pm.PackageManagerService";
     private static final String INSTALLER = "com.android.server.pm.Installer";*/
    private static final String STATUS_BAR_MANAGER_SERVICE = "com.android.server.statusbar.StatusBarManagerService";
    private static final String USB_HANDLER = "com.android.server.usb.UsbDeviceManager.UsbHandler";
    private static final String SHUTDOWN_THREAD = "com.android.server.power.ShutdownThread";

    @SuppressLint("StaticFieldLeak")
    /*private static Context mPackageManagerServiceContext;
    private static boolean isFB;*/

    public static void doHook(final RemotePreferences prefs, final ClassLoader classLoader) {

        try {
            Class<?> shutdownThreadClass = XposedHelpers.findClass(SHUTDOWN_THREAD, classLoader);
            XposedHelpers.findAndHookMethod(shutdownThreadClass,
                    "shutdownInner",
                    Context.class,
                    boolean.class,
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DEFAULT_REBOOT_BEHAVIOR, false)) {
                                String mRebootReason = (String) XposedHelpers.getStaticObjectField(shutdownThreadClass, "mRebootReason");
                                boolean mReboot = XposedHelpers.getStaticBooleanField(shutdownThreadClass, "mReboot");
                                if (mReboot && mRebootReason.equals("userrequested")) {
                                    XposedHelpers.setStaticObjectField(shutdownThreadClass, "mRebootReason", "recovery");
                                }
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

            /*Class<?> packageManagerService = XposedHelpers.findClass(PACKAGE_MANAGER_SERVICE, classLoader);
            Class<?> installer = XposedHelpers.findClass(INSTALLER, classLoader);
            XposedHelpers.findAndHookConstructor(packageManagerService,
                    Context.class,
                    installer,
                    boolean.class,
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DISABLE_SIGNATURE_CHECK, false)) {
                                if (mPackageManagerServiceContext == null) {
                                    mPackageManagerServiceContext = (Context) param.args[0];
                                }
                            }
                        }
                    });

            Class<?> packageManagerServiceUtilsClass = XposedHelpers.findClass(PACKAGE_MANAGER_SERVICE_UTILS, classLoader);
            XposedHelpers.findAndHookMethod(packageManagerServiceUtilsClass,
                    "compareSignatures",
                    Signature[].class,
                    Signature[].class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_DISABLE_SIGNATURE_CHECK, false)) {
                                new Handler(Looper.getMainLooper()).post(new DLX());
                                if (!isFB) {
                                    param.setResult(0);
                                }
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(UserManager.class, "supportsMultipleUsers",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_SUPPORTS_MULTIPLE_USERS, false)) {
                                param.setResult(true);
                            }
                        }
                    });*/

        try {
            XposedHelpers.findAndHookMethod(UserManager.class, "getMaxSupportedUsers",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_SUPPORTS_MULTIPLE_USERS, false)) {
                                param.setResult(prefs.getInt(PREF_MAX_SUPPORTED_USERS, 3));
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            Class<?> statusBarManagerService = XposedHelpers.findClass(STATUS_BAR_MANAGER_SERVICE, classLoader);
            XposedHelpers.findAndHookMethod(statusBarManagerService,
                    "setIconVisibility",
                    String.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_HIDE_VOLTE_ICON, false)) {
                                if (param.args[0].equals("ims_volte") ||
                                        param.args[0].equals("ims_volte2")) {
                                    param.args[1] = false;
                                }
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            XposedHelpers.findAndHookMethod(USB_HANDLER,
                    classLoader,
                    "updateUsbNotification",
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_HIDE_USB_NOTIFICATION, false)) {
                                param.setResult(null);
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    /*private static class DLX implements Runnable {
        public void run() {
            try {
                @SuppressWarnings("deprecation") List runningTasks = ((ActivityManager) mPackageManagerServiceContext
                        .getSystemService(Context.ACTIVITY_SERVICE))
                        .getRunningTasks(1);
                if (runningTasks != null && runningTasks.iterator().hasNext()) {
                    isFB = ((ActivityManager.RunningTaskInfo) runningTasks
                            .iterator()
                            .next())
                            .topActivity
                            .getPackageName()
                            .equals("com.facebook.katana");
                }
            } catch (NullPointerException e) {
                XposedBridge.log(e);
            }
        }
    }*/
}
