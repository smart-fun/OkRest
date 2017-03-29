package fr.arnaudguyon.okrest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    void execute(Context context, int requestCode, OkRequest okRequest, RequestListenerJSON listener) {

        if (!checkInternetConnection(context, requestCode, listener)) {
            return;
        }
        Request request = okRequest.createOkHttpRequest(context, ResponseType.JSON);
        doRequest(context, request, requestCode, listener);
    }

    private void doRequest(final Context context, Request request, final int requestCode, final RequestListener listener) {

        final Handler handler = new Handler(context.getMainLooper());

        mClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (listener == null) {
                    return;
                }

                if (call.isCanceled()) {    // don't send error if the user/app cancelled the call
                    return;
                }

                e.printStackTrace();
                sendFailure(handler, requestCode, 0, null, listener);
            }

            @Override
            public void onResponse(Call call, Response response) {

                if (listener == null) {
                    return;
                }

                int statusCode = response.code();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String body = responseBody.string();
                            sendSuccess(handler, requestCode, statusCode, body, listener);
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                sendFailure(handler, requestCode, statusCode, null, listener);
            }
        });
    }

    private void sendSuccess(Handler handler, final int requestCode, final int statusCode, final @NonNull String body, final RequestListener listener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener instanceof RequestListenerString) {
                    RequestListenerString stringListener = (RequestListenerString) listener;
                    stringListener.onRequestSuccess(requestCode, body);
                } else if (listener instanceof RequestListenerJSON) {
                    RequestListenerJSON jsonListener = (RequestListenerJSON) listener;
                    try {
                        JSONObject jsonObject = TextUtils.isEmpty(body) ? new JSONObject() : new JSONObject(body);
                        jsonListener.onRequestSuccess(requestCode, jsonObject);
                    } catch (JSONException e) {
                        try {
                            JSONArray jsonArray = new JSONArray(body);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("array", jsonArray);
                            jsonListener.onRequestSuccess(requestCode, jsonObject);
                        } catch (JSONException e1) {
                            jsonListener.onRequestFailure(requestCode, statusCode, null);
                        }
                    }
                } else {
                    throw new RuntimeException("use RequestListenerString or RequestListenerJSON listener");
                }
            }
        });
    }

    private void sendFailure(Handler handler, final int requestCode, final int statusCode, final String body, final RequestListener listener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener instanceof RequestListenerString) {
                    RequestListenerString stringListener = (RequestListenerString) listener;
                    stringListener.onRequestFailure(requestCode, statusCode, body);
                } else if (listener instanceof RequestListenerJSON) {
                    RequestListenerJSON jsonListener = (RequestListenerJSON) listener;
                    JSONObject json = null;
                    try {
                        json = (body != null) ? new JSONObject(body) : null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonListener.onRequestFailure(requestCode, statusCode, json);
                }
            }
        });
    }

    private boolean checkInternetConnection(Context context, int requestCode, RequestListener listener) {
        if (!isOnline(context)) {
            if (listener != null) {
                if (listener instanceof RequestListenerJSON) {
                    RequestListenerJSON jsonListener = (RequestListenerJSON) listener;
                    jsonListener.onRequestFailure(requestCode, ERRORCODE_NO_INTERNET_CONNECTION, null);
                }
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
