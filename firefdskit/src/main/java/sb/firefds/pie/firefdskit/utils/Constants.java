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
package sb.firefds.pie.firefdskit.utils;

import sb.firefds.pie.firefdskit.BuildConfig;

import static sb.firefds.pie.firefdskit.utils.Packages.FIREFDSKIT;

public class Constants {

    public static final String BACKUP_DIR = "FirefdsKitBackup";
    public static final String PREFS = BuildConfig.APPLICATION_ID + "_preferences";
    public static final String PREFS_AUTHORITY = FIREFDSKIT + ".preferences";

    public static final String REBOOT_ACTION = "REBOOT_ACTION";
    public static final String REBOOT_DEVICE_ACTION = "REBOOT_DEVICE";
    public static final String QUICK_REBOOT_DEVICE_ACTION = "QUICK_REBOOT_DEVICE";

    public static final String FORCE_CONNECT_MMS = "CscFeature_RIL_ForceConnectMMS";
    public static final String DISABLE_SMS_TO_MMS_CONVERSION_BY_TEXT_INPUT =
            "CscFeature_Message_DisableSmsToMmsConversionByTextInput";
    public static final String DISABLE_PHONE_NUMBER_FORMATTING = "CscFeature_Common_DisablePhoneNumberFormatting";

    public static final String SHORTCUT_STATUSBAR = "STATUSBAR";
    public static final String SHORTCUT_SYSTEM = "SYSTEM";
    public static final String SHORTCUT_PHONE = "PHONE";
    public static final String SHORTCUT_SECURITY = "SECURITY";

    public static final String POWER_ACTION = "power";
    public static final String RESTART_ACATION = "restart";
    public static final String EMERGENCY_ACTION = "emergency";
    public static final String RECOVERY_ACTION = "recovery";
    public static final String DOWNLOAD_ACTION = "download";
    public static final String DATA_MODE_ACTION = "data_mode";
    public static final String SCREENSHOT_ACTION = "screenshot";
    public static final String MULTIUSER_ACTION = "multiuser";
    public static final String RESTART_UI_ACTION = "restart_ui";
    public static final String FLASHLIGHT_ACTION = "flashlight";
    public static final String SCREEN_RECORD_ACTION = "screen_record";

}
