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
package sb.firefds.pie.firefdskit.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import sb.firefds.pie.firefdskit.XCscFeaturesManager;
import sb.firefds.pie.firefdskit.core.R;
import sb.firefds.pie.firefdskit.utils.Utils;

import static sb.firefds.pie.firefdskit.utils.Constants.DOWNLOAD_REBOOT_DEVICE_ACTION;
import static sb.firefds.pie.firefdskit.utils.Constants.PREFS;
import static sb.firefds.pie.firefdskit.utils.Constants.QUICK_REBOOT_DEVICE_ACTION;
import static sb.firefds.pie.firefdskit.utils.Constants.REBOOT_ACTION;
import static sb.firefds.pie.firefdskit.utils.Constants.REBOOT_DEVICE_ACTION;
import static sb.firefds.pie.firefdskit.utils.Constants.RECOVERY_REBOOT_DEVICE_ACTION;

public class WanamRebootActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFS, 0);
        String reboot = Objects.requireNonNull(getIntent().getExtras()).getString(REBOOT_ACTION);
        try {
            switch (Objects.requireNonNull(reboot)) {
                case REBOOT_DEVICE_ACTION:
                    rebootDevice();
                    break;
                case QUICK_REBOOT_DEVICE_ACTION:
                    quickRebootDevice();
                    break;
                case RECOVERY_REBOOT_DEVICE_ACTION:
                    Utils.rebootEPM("recovery");
                    break;
                case DOWNLOAD_REBOOT_DEVICE_ACTION:
                    Utils.rebootEPM("download");
                    break;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void rebootDevice() throws Throwable {
        Utils.closeStatusBar(this);
        showRebootDialog();
        if (!Utils.isOmcEncryptedFlag()) {
            XCscFeaturesManager.applyCscFeatures(sharedPreferences, getApplicationContext());
        }
        Utils.reboot();
    }

    private void quickRebootDevice() throws Throwable {
        Utils.closeStatusBar(this);
        showRebootDialog();
        if (!Utils.isOmcEncryptedFlag()) {
            XCscFeaturesManager.applyCscFeatures(sharedPreferences, getApplicationContext());
        }
        Utils.performQuickReboot();
    }

    private void showRebootDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_dialog).create().show();
    }
}
