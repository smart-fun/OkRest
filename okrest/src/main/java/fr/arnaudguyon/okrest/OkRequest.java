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
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by aguyon on 28.02.17.
 */

public class OkRequest {

    private static final MediaType TEXT = MediaType.parse("text/xml; charset=utf-8");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    public enum BodyType {
        POST,
        PUT,
        PATCH,
        CONNECT
    }

    private Builder mBuilder;
    private String mTag;

    private OkRequest(Builder builder) {
        mBuilder = builder;
    }

    public static class Builder {
        private String mUrl;
        private RequestParams mParams;
        private RequestHeaders mHeaders;
        private ResponseType mResponseType = ResponseType.JSON;

        private String mBodyString;
        private JSONObject mBodyJSON;
        private BodyType mBodyType;

        public Builder() {
        }

        public Builder url(@NonNull String url) {
            mUrl = url;
            return this;
        }

        public Builder params(@NonNull RequestParams params) {
            mParams = params;
            return this;
        }

        public Builder addParam(@NonNull String key, @NonNull String value) {
            if (mParams == null) {
                mParams = new RequestParams(key, value);
            } else {
                mParams.add(key, value);
            }
            return this;
        }

        public Builder headers(RequestHeaders params) {
            mHeaders = params;
            return this;
        }

        public Builder addHeader(@NonNull String key, @NonNull String value) {
            if (mHeaders == null) {
                mHeaders = new RequestHeaders(key, value);
            } else {
                mHeaders.add(key, value);
            }
            return this;
        }

        public Builder body(BodyType bodyType, String body) {
            mBodyType = bodyType;
            mBodyString = body;
            return this;
        }

        public Builder body(BodyType bodyType, JSONObject body) {
            mBodyType = bodyType;
            mBodyJSON = body;
            return this;
        }

        public Builder setResponseType(ResponseType responseType) {
            mResponseType = responseType;
            return this;
        }

        public OkRequest build() {
            return new OkRequest(this);
        }

    }

    public void execute(Context context, int requestCode, RequestListener listener) {
        OkClient client = OkClient.getInstance();
        client.execute(context, requestCode, this, listener);
    }

    private static HttpUrl formatUrl(String url, RequestParams params) {
        if (params != null) {
            ArrayList<Pair<String, String>> pairs = params.getParams();
            boolean firstParam = true;
            for (Pair<String, String> pair : pairs) {
                if (firstParam) {
                    url += "?" + pair.first + "=" + pair.second;
                    firstParam = false;
                } else {
                    url += "&" + pair.first + "=" + pair.second;
                }
            }
        }
        return HttpUrl.parse(url);
    }

    Request createOkHttpRequest(Context context) {

        Request.Builder builder = new Request.Builder();

        // URL
        HttpUrl url = formatUrl(mBuilder.mUrl, mBuilder.mParams);
        builder.url(url);

        // HEADERS
        builder.header("User-Agent", UserAgent.get(context));
        if (mBuilder.mHeaders != null) {
            for (Pair<String, String> pair : mBuilder.mHeaders.getParams()) {
                builder.addHeader(pair.first, pair.second);
            }
        }
        if (mBuilder.mResponseType == ResponseType.JSON) {
            builder.addHeader("Accept", "application/json");
        }

        // BODY (POST / PUT...)
        if ((mBuilder.mBodyJSON != null) || !TextUtils.isEmpty(mBuilder.mBodyString)) {
            RequestBody requestBody = null;
            if (mBuilder.mResponseType == ResponseType.TEXT) {
                requestBody = RequestBody.create(TEXT, mBuilder.mBodyString);
            } else if (mBuilder.mResponseType == ResponseType.JSON) {
                String body = (mBuilder.mBodyJSON != null) ? mBuilder.mBodyJSON.toString() : mBuilder.mBodyString;
                requestBody = RequestBody.create(JSON, body);
            } else if (mBuilder.mResponseType == ResponseType.XML) {
                requestBody = RequestBody.create(XML, mBuilder.mBodyString);
            }
            builder.method(mBuilder.mBodyType.name(), requestBody);
        }

        // unique Tag in case of the user wants to cancel the call later
        mTag = UUID.randomUUID().toString();
        builder.tag(mTag);

        return builder.build();
    }

    public void cancel() {
        OkClient client = OkClient.getInstance();
        client.cancelRequest(mTag);
    }
}
