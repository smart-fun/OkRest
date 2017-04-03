package fr.arnaudguyon.okrest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Main class to do REST requests using OkHttp.
 * use getInstance().requestGet() or getInstance().requestPost()
 */

class OkClient {

    private static final String TAG = "OkClient";
    private static final int DEFAULT_TIMOUT = 30000;

    private static final int ERRORCODE_NO_INTERNET_CONNECTION = -1;

    private static OkClient sInstance;
    private OkHttpClient mClient = new OkHttpClient.Builder().readTimeout(DEFAULT_TIMOUT, TimeUnit.MILLISECONDS).build();

    private OkClient() {
    }

    public static OkClient getInstance() {
        if (sInstance == null) {
            synchronized (OkClient.class) {
                if (sInstance == null) {
                    sInstance = new OkClient();
                }
            }
        }
        return sInstance;
    }

    void execute(Context context, int requestCode, OkRequest okRequest, RequestListener listener) {

        if (!checkInternetConnection(context, requestCode, listener)) {
            return;
        }
        Request request = okRequest.createOkHttpRequest(context);
        doRequest(context, request, requestCode, listener);
    }

    private void doRequest(final Context context, Request request, final int requestCode, final RequestListener listener) {

        final Handler handler = new Handler(context.getMainLooper());

        mClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException exception) {
                if ((listener == null) || call.isCanceled()) {
                    return;
                }
                exception.printStackTrace();
                OkResponse okResponse = new OkResponse(exception);
                sendResponse(handler, false, requestCode, okResponse, listener);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if ((listener == null) || call.isCanceled()) {
                    return;
                }
                OkResponse okResponse = new OkResponse(response);
                sendResponse(handler, response.isSuccessful(), requestCode, okResponse, listener);
            }
        });
    }

    private void sendResponse(Handler handler, final boolean success, final int requestCode, final @NonNull OkResponse reponse, final RequestListener listener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onRequestResponse(success, requestCode, reponse);
                }
            }
        });
    }

    private boolean checkInternetConnection(Context context, int requestCode, RequestListener listener) {
        if (!isOnline(context)) {
            if (listener != null) {
                NoInternetException exception = new NoInternetException();
                OkResponse okResponse = new OkResponse(exception);
                listener.onRequestResponse(false, requestCode, okResponse);
            } else {
                Log.w(TAG, "No internet connection and no listener");
            }
            return false;
        }
        return true;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return ((netInfo != null) && (netInfo.isConnectedOrConnecting()));
    }

}
