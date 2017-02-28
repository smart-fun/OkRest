package fr.arnaudguyon.okrest;

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

            String versionName = BuildConfig.VERSION_NAME;
            int versionCode = BuildConfig.VERSION_CODE;
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

            String appVersion = BuildConfig.APPLICATION_ID + "/" + versionName;
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
