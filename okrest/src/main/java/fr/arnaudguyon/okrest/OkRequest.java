package fr.arnaudguyon.okrest;

import android.content.Context;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.ArrayList;

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

    private Builder mBuilder;

    private OkRequest(Builder builder) {
        mBuilder = builder;
    }

    public static class Builder {
        private String mUrl;
        private RequestParams mParams;
        private RequestHeaders mHeaders;
        private String mBodyString;
        private JSONObject mBodyJSON;

        public Builder() {
        }

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder params(RequestParams params) {
            mParams = params;
            return this;
        }

        public Builder params(String... params) {
            mParams = new RequestParams(params);
            return this;
        }

        public Builder headers(RequestHeaders params) {
            mHeaders = params;
            return this;
        }

        public Builder headers(String... params) {
            mHeaders = new RequestHeaders(params);
            return this;
        }

        public Builder body(String body) {
            mBodyString = body;
            return this;
        }

        public Builder body(JSONObject body) {
            mBodyJSON = body;
            return this;
        }

        public OkRequest build() {
            return new OkRequest(this);
        }

    }

    public void get(Context context, int requestCode, RequestListenerJSON listener) {
        OkClient client = OkClient.getInstance();
        client.get(context, requestCode, this, listener);
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

    Request createOkHttpRequest(Context context, ResponseType responseType) {

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
        if (responseType == ResponseType.JSON) {
            builder.addHeader("Accept", "application/json");
        }

        // BODY
        if ((mBuilder.mBodyJSON != null) || !TextUtils.isEmpty(mBuilder.mBodyString)) {
            if (responseType == ResponseType.TEXT) {
                RequestBody requestBody = RequestBody.create(TEXT, mBuilder.mBodyString);
                builder.post(requestBody);
            } else if (responseType == ResponseType.JSON) {
                String body = (mBuilder.mBodyJSON != null) ? mBuilder.mBodyJSON.toString() : mBuilder.mBodyString;
                RequestBody requestBody = RequestBody.create(JSON, body);
                builder.post(requestBody);
            } else if (responseType == ResponseType.XML) {
                RequestBody requestBody = RequestBody.create(XML, mBuilder.mBodyString);
                builder.post(requestBody);
            }
        }

        return builder.build();
    }
}
