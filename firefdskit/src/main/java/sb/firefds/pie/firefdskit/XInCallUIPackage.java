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

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static sb.firefds.pie.firefdskit.utils.Packages.INCALLUI;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_AUTO_CALL_RECORDING;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_CALL_ADD;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_ENABLE_CALL_RECORDING_MENU;

public class XInCallUIPackage {

    private static final String FEATURE_FUNCTION = INCALLUI + ".modelimpl.feature.function";
    private static final String VOICE_RECORDING_FEATURE_IMPL = FEATURE_FUNCTION + ".VoiceRecordingFeatureImpl";
    private static final String VOICE_RECORDING_BY_MENU_FEATURE_IMPL = FEATURE_FUNCTION + ".VoiceRecordingByMenuFeatureImpl";
    private static final String VOICE_RECORDING_BY_BUTTON_FEATURE_IMPL = FEATURE_FUNCTION + ".VoiceRecordingByButtonFeatureImpl";
    private static final String VOICE_RECORDING_CONTEXT_IMPL = INCALLUI + ".modelimpl.callcontext.VoiceRecordingContextImpl";

    public static void doHook(final SharedPreferences prefs, final ClassLoader classLoader) {

        try {
            XposedHelpers.findAndHookMethod(VOICE_RECORDING_FEATURE_IMPL,
                    classLoader,
                    "isSupportVoiceRecording",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_ENABLE_CALL_ADD, false) &&
                                    !prefs.getBoolean(PREF_ENABLE_AUTO_CALL_RECORDING, false)) {
                                return Boolean.FALSE;
                            } else {
                                return Boolean.TRUE;
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            XposedHelpers.findAndHookMethod(VOICE_RECORDING_BY_MENU_FEATURE_IMPL,
                    classLoader,
                    "isSupportVoiceRecording",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_ENABLE_CALL_RECORDING_MENU, false)) {
                                return Boolean.TRUE;
                            } else {
                                return Boolean.FALSE;
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            XposedHelpers.findAndHookMethod(VOICE_RECORDING_BY_BUTTON_FEATURE_IMPL,
                    classLoader,
                    "isSupportVoiceRecording",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_ENABLE_CALL_ADD, false)) {
                                return Boolean.FALSE;
                            } else {
                                return Boolean.TRUE;
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }

        try {
            XposedHelpers.findAndHookMethod(VOICE_RECORDING_CONTEXT_IMPL,
                    classLoader,
                    "isForcedToAutoRecord",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            if (prefs.getBoolean(PREF_ENABLE_AUTO_CALL_RECORDING, false)) {
                                return Boolean.TRUE;
                            } else {
                                return Boolean.FALSE;
                            }
                        }
                    });
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }
}
