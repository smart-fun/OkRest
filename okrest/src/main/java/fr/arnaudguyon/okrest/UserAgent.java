package fr.arnaudguyon.okrest;

/*
    Copyright 2017 Arnaud Guyon
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Locale;

/**
 * Default User Agent header for requests, using format like:
 * "Android/5.0.1 (samsung GT-I9505; fr-FR; API 21) com.mycompamy.myapp/1.1.0 (Build 44)"
 */
class UserAgent {

    private static String sUserAgent;

    static String get(Context context) {
        if (sUserAgent == null) {

            String versionName = "1.0";
            int versionCode = 1;
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                versionName = packageInfo.versionName;
                versionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Locale locale = Locale.getDefault();

            String androidVersion = "Android/" + Build.VERSION.RELEASE;
            String androidAPI = "API " + Build.VERSION.SDK_INT;

            String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
            String deviceLanguage = locale.getLanguage() + "-" + locale.getCountry();

            String appVersion = context.getPackageName() + "/" + versionName;
            String appBuild = "Build " + versionCode;

            sUserAgent =
                    androidVersion +
                    " (" + deviceName + "; " + deviceLanguage + "; " + androidAPI + ") " +
                    appVersion +
                    " (" + appBuild + ")";
        }
        return sUserAgent;
    }
}
