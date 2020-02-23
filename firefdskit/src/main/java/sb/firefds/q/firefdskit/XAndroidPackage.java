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
package sb.firefds.q.firefdskit;

import android.annotation.SuppressLint;
import android.content.Context;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.q.firefdskit.utils.Preferences.PREF_DEFAULT_REBOOT_BEHAVIOR;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_HIDE_USB_NOTIFICATION;
import static sb.firefds.q.firefdskit.utils.Preferences.PREF_HIDE_VOLTE_ICON;

public class XAndroidPackage {

    private static final String STATUS_BAR_MANAGER_SERVICE = "com.android.server.statusbar.StatusBarManagerService";
    private static final String USB_HANDLER = "com.android.server.usb.UsbDeviceManager.UsbHandler";
    private static final String SHUTDOWN_THREAD = "com.android.server.power.ShutdownThread";

    @SuppressLint("StaticFieldLeak")

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
}
