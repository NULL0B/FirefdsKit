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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemProperties;

import com.samsung.android.feature.SemCscFeature;
import com.topjohnwu.superuser.Shell;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sb.firefds.pie.firefdskit.bean.FeatureDTO;
import sb.firefds.pie.firefdskit.utils.Constants;
import sb.firefds.pie.firefdskit.utils.Utils;

import static sb.firefds.pie.firefdskit.utils.Constants.DISABLE_PHONE_NUMBER_FORMATTING;
import static sb.firefds.pie.firefdskit.utils.Constants.DISABLE_SMS_TO_MMS_CONVERSION_BY_TEXT_INPUT;
import static sb.firefds.pie.firefdskit.utils.Constants.FALSE;
import static sb.firefds.pie.firefdskit.utils.Constants.FORCE_CONNECT_MMS;
import static sb.firefds.pie.firefdskit.utils.Constants.MAX_RECIPIENT_LENGTH_AS;
import static sb.firefds.pie.firefdskit.utils.Constants.SMS_MAX_BYTE;
import static sb.firefds.pie.firefdskit.utils.Constants.TRUE;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_LOW_BATTERY_SOUND;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_NUMBER_FORMATTING;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_SMS_TO_MMS;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_DISABLE_VOLUME_CONTROL_SOUND;
import static sb.firefds.pie.firefdskit.utils.Preferences.PREF_FORCE_MMS_CONNECT;

public class XCscFeaturesManager {

    private static final String RO_BUILD_PDA = "ro.build.PDA";
    private final static String SYSTEM_CSC_SALES_CODE_DAT =
            Utils.getCSCType().equals(Utils.CscType.OMC_OMC) ?
                    "/system/omc/sales_code.dat" : "/system/csc/sales_code.dat";
    private static final String SYSTEM_CSC_VERSION_TXT =
            Utils.getCSCType().equals(Utils.CscType.OMC_OMC) ?
                    "/system/omc/CSCVersion.txt" : "/system/CSCVersion.txt";
    private static ArrayList<FeatureDTO> defaultFeatureDTOs;
    private static String version = "";
    private static String country = "";
    private static String countryISO = "";
    private static String salesCode = "";
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static void applyCscFeatures(SharedPreferences prefs, Context context) {
        StringBuilder features = new StringBuilder();
        ArrayList<FeatureDTO> featuresDTOList;

        try {

            XCscFeaturesManager.context = context;

            setVolumeControlSounds(prefs);

            setLowBatterySounds(prefs);

            featuresDTOList = getCscFeaturesList(prefs);

            for (FeatureDTO featureDTO : featuresDTOList) {
                features.append("\t\t<")
                        .append(featureDTO.getfeatureCode())
                        .append(">")
                        .append(featureDTO.getFeatureValue())
                        .append("</")
                        .append(featureDTO.getfeatureCode())
                        .append(">\n");
            }

            if (defaultFeatureDTOs != null && defaultFeatureDTOs.size() > 0) {
                for (FeatureDTO defaultFeatureDTO : defaultFeatureDTOs) {
                    if (features.indexOf(defaultFeatureDTO.getfeatureCode()) < 0) {
                        features.append("\t\t<")
                                .append(defaultFeatureDTO.getfeatureCode())
                                .append(">")
                                .append(defaultFeatureDTO.getFeatureValue())
                                .append("</")
                                .append(defaultFeatureDTO.getfeatureCode())
                                .append(">\n");
                    }
                }
            }

            applyCSCFeatures(Constants.FEATURES_LIST_HEADER1
                    + (version.isEmpty() ? "ED0001" : version)
                    + Constants.FEATURES_LIST_HEADER2
                    + (country.isEmpty() ? "UNITED KINGDOM" : country)
                    + Constants.FEATURES_LIST_HEADER3
                    + (countryISO.isEmpty() ? "GB" : countryISO)
                    + Constants.FEATURES_LIST_HEADER4
                    + (salesCode.isEmpty() ? "BTU" : salesCode)
                    + Constants.FEATURES_LIST_HEADER5
                    + features + Constants.FEATURES_LIST_FOOTER);
            Shell.su("echo " + (salesCode.isEmpty() ? "BTU" : salesCode) + " > "
                    + SYSTEM_CSC_SALES_CODE_DAT).submit();

            String pda = SystemProperties.get(RO_BUILD_PDA);
            if (!pda.isEmpty() && !(new File(SYSTEM_CSC_VERSION_TXT).exists())) {
                Shell.su("echo " + pda + " > " + SYSTEM_CSC_VERSION_TXT).submit();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void setVolumeControlSounds(SharedPreferences prefs) {
        if (prefs.getBoolean(PREF_DISABLE_VOLUME_CONTROL_SOUND, false)) {
            Utils.disableVolumeControlSounds(context);
        } else {
            Utils.enableVolumeControlSounds(context);
        }
    }

    private static void setLowBatterySounds(SharedPreferences prefs) {
        if (prefs.getBoolean(PREF_DISABLE_LOW_BATTERY_SOUND, false)) {
            Utils.disableLowBatterySounds(context);
        } else {
            Utils.enableLowBatterySounds(context);
        }
    }

    /**
     * @param prefs CSC Preference
     */
    private static ArrayList<FeatureDTO> getCscFeaturesList(SharedPreferences prefs) {
        ArrayList<FeatureDTO> featuresDTOList = new ArrayList<>();

        // Messaging
        if (prefs.getBoolean(PREF_FORCE_MMS_CONNECT, false))
            featuresDTOList.add(new FeatureDTO(FORCE_CONNECT_MMS, TRUE));
        else
            featuresDTOList.add(new FeatureDTO(FORCE_CONNECT_MMS, FALSE));
        if (prefs.getBoolean(PREF_DISABLE_SMS_TO_MMS, false)) {
            featuresDTOList.add(new FeatureDTO(DISABLE_SMS_TO_MMS_CONVERSION_BY_TEXT_INPUT, TRUE));
            featuresDTOList.add(new FeatureDTO(SMS_MAX_BYTE, "999"));
        } else {
            featuresDTOList.add(new FeatureDTO(DISABLE_SMS_TO_MMS_CONVERSION_BY_TEXT_INPUT, FALSE));
            featuresDTOList.add(new FeatureDTO(SMS_MAX_BYTE, "140"));
        }
        featuresDTOList.add(new FeatureDTO(MAX_RECIPIENT_LENGTH_AS, "999"));

        // Contacts
        if (prefs.getBoolean(PREF_DISABLE_NUMBER_FORMATTING, false))
            featuresDTOList.add(new FeatureDTO(DISABLE_PHONE_NUMBER_FORMATTING, TRUE));
        else
            featuresDTOList.add(new FeatureDTO(DISABLE_PHONE_NUMBER_FORMATTING, FALSE));

        return featuresDTOList;
    }

    private static void applyCSCFeatures(String langList) {
        new CSCFeaturesTask().execute(langList);
    }

    private static class CSCFeaturesTask extends AsyncTask<String, Void, Void> {
        private OutputStream out = null;
        private File featureXML = new File(context.getCacheDir(),
                Constants.FEATURE_XML);

        protected Void doInBackground(String... params) {
            try {

                out = new FileOutputStream(featureXML);
                out.write(params[0].getBytes());

            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (featureXML.isFile()) {
                try {
                    Utils.applyCSCFeatues(context);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    static void getDefaultCSCFeaturesFromFiles() {

        defaultFeatureDTOs = new ArrayList<>();
        SemCscFeature mCscFeature;
        mCscFeature = SemCscFeature.getInstance();

        version = mCscFeature.getString("Version");
        country = mCscFeature.getString("Country");
        countryISO = mCscFeature.getString("CountryISO");
        salesCode = mCscFeature.getString("SalesCode");

        XmlPullParserFactory factory;
        XmlPullParser p;
        DefaultCSCCollector tc = new DefaultCSCCollector();
        InputStream is = null;

        try {
            String[] files = {Constants.SYSTEM_CSC_FEATURE_XML, Constants.SYSTEM_CSC_OTHER_XML,
                    Constants.SYSTEM_OTHER_FEATURE_BKP, Constants.SYSTEM_CSC_FEATURE_BKP};

            for (String cscFile : files) {
                if (new File(cscFile).isFile()) {
                    try {
                        factory = XmlPullParserFactory.newInstance();
                        p = factory.newPullParser();
                        is = new FileInputStream(cscFile);
                        p.setInput(is, "UTF-8");

                        skipToTag(p);
                        processTag(p, p.getName(), tc);

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

            defaultFeatureDTOs.addAll(tc.defaultFeatures());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
        }
    }

    private static void processTag(XmlPullParser p, String tag, TagProcessor tp) throws IOException,
            XmlPullParserException {
        tp.processTag(p, tag);

        // this check is necessary since getNextText() pushes the current event
        // to END_TAG!
        int nextEvent = (p.getEventType() == XmlPullParser.END_TAG) ? p.getEventType() : p.next();
        while (nextEvent != XmlPullParser.END_TAG) {
            if (nextEvent == XmlPullParser.START_TAG) {
                processTag(p, p.getName(), tp); // recursive call
            }
            nextEvent = p.next();
        }
        p.require(XmlPullParser.END_TAG, null, tag);
    }

    private static void skipToTag(XmlPullParser p) throws IOException, XmlPullParserException {
        int nextEvent = p.next();
        while (nextEvent != XmlPullParser.START_TAG && nextEvent != XmlPullParser.END_DOCUMENT) {
            nextEvent = p.next();
        }
    }
}

interface TagProcessor {
    void processTag(XmlPullParser p, String tag) throws IOException, XmlPullParserException;
}

class DefaultCSCCollector implements TagProcessor {
    private ArrayList<FeatureDTO> defaultFeatures;

    DefaultCSCCollector() {
        defaultFeatures = new ArrayList<>();
    }

    public void processTag(XmlPullParser p, String tag) throws IOException, XmlPullParserException {
        if (tag.startsWith("CscFeature")) {
            defaultFeatures.add(new FeatureDTO(tag, p.nextText()));
        }
    }

    ArrayList<FeatureDTO> defaultFeatures() {
        return defaultFeatures;
    }
}
