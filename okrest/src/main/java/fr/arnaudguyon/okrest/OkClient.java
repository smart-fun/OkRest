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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Dispatcher;
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
    private OkHttpClient mClient = new OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIMOUT, TimeUnit.MILLISECONDS)
//            .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS))
            .build();

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

    public void cancelAllRequests() {
        Dispatcher dispatcher = mClient.dispatcher();
        if (dispatcher != null) {
            List<Call> queueCalls = dispatcher.queuedCalls();
            cancelAllRequest(queueCalls);
            List<Call> runningCalls = dispatcher.runningCalls();
            cancelAllRequest(runningCalls);
        }
    }

    private void cancelAllRequest(List<Call> calls) {
        if (calls != null) {
            for (Call call : calls) {
                call.cancel();
            }
        }
    }

    public void cancelRequest(@NonNull String tag) {
        Dispatcher dispatcher = mClient.dispatcher();
        if (dispatcher != null) {
            List<Call> queueCalls = dispatcher.queuedCalls();
            cancelRequest(tag, queueCalls);
            List<Call> runningCalls = dispatcher.runningCalls();
            cancelRequest(tag, runningCalls);
        }
    }

    private void cancelRequest(String tag, List<Call> calls) {
        if (calls != null) {
            for (Call call : calls) {
                Request request = call.request();
                if (request != null) {
                    if (TextUtils.equals(tag, request.tag().toString())) {
                        call.cancel();
                    }
                }
            }
        }
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
                boolean success = response.isSuccessful();
                OkResponse okResponse = new OkResponse(response);
                sendResponse(handler, success, requestCode, okResponse, listener);
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
        NetworkInfo netInfo = (cm != null) ? cm.getActiveNetworkInfo() : null;
        return ((netInfo != null) && (netInfo.isConnectedOrConnecting()));
    }

}
