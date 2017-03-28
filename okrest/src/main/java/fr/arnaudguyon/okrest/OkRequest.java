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
        private String mPostBodyString;
        private JSONObject mPostBodyJSON;

        private String mPutBodyString;
        private JSONObject mPutBodyJSON;

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

        public Builder postBody(String body) {
            mPostBodyString = body;
            return this;
        }

        public Builder postBody(JSONObject body) {
            mPostBodyJSON = body;
            return this;
        }

        public Builder putBody(String body) {
            mPutBodyString = body;
            return this;
        }

        public Builder putBody(JSONObject body) {
            mPutBodyJSON = body;
            return this;
        }

        public OkRequest build() {
            return new OkRequest(this);
        }

    }

    public void execute(Context context, int requestCode, RequestListenerJSON listener) {
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

        // POST BODY
        if ((mBuilder.mPostBodyJSON != null) || !TextUtils.isEmpty(mBuilder.mPostBodyString)) {
            if (responseType == ResponseType.TEXT) {
                RequestBody requestBody = RequestBody.create(TEXT, mBuilder.mPostBodyString);
                builder.post(requestBody);
            } else if (responseType == ResponseType.JSON) {
                String body = (mBuilder.mPostBodyJSON != null) ? mBuilder.mPostBodyJSON.toString() : mBuilder.mPostBodyString;
                RequestBody requestBody = RequestBody.create(JSON, body);
                builder.post(requestBody);
            } else if (responseType == ResponseType.XML) {
                RequestBody requestBody = RequestBody.create(XML, mBuilder.mPostBodyString);
                builder.post(requestBody);
            }
        }

        // PUT BODY
        if ((mBuilder.mPutBodyJSON != null) || !TextUtils.isEmpty(mBuilder.mPutBodyString)) {
            if (responseType == ResponseType.TEXT) {
                RequestBody requestBody = RequestBody.create(TEXT, mBuilder.mPutBodyString);
                builder.put(requestBody);
            } else if (responseType == ResponseType.JSON) {
                String body = (mBuilder.mPutBodyJSON != null) ? mBuilder.mPutBodyJSON.toString() : mBuilder.mPutBodyString;
                RequestBody requestBody = RequestBody.create(JSON, body);
                builder.put(requestBody);
            } else if (responseType == ResponseType.XML) {
                RequestBody requestBody = RequestBody.create(XML, mBuilder.mPutBodyString);
                builder.put(requestBody);
            }
        }

        return builder.build();
    }
}
