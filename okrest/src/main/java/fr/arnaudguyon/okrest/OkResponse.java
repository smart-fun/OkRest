package fr.arnaudguyon.okrest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by aguyon on 03.04.17.
 */

public class OkResponse {

    private Response mResponse;
    private ResponseHeaders mHeaders;
    private String mString;
    private JSONObject mJSON;
    private Exception mError;

    OkResponse(@NonNull Response response) {
        mResponse = response;
    }

    OkResponse(@NonNull Exception error) {
        mError = error;
    }

    public Response getRawResponse() {
        return mResponse;
    }

    public ResponseHeaders getHeaders() {
        if (mHeaders == null) {
            mHeaders = new ResponseHeaders(mResponse);
        }
        return mHeaders;
    }

    public int getStatusCode() {
        if (mResponse == null) {
            return 0;
        }
        return mResponse.code();
    }

    public String getBodyString() {
        if ((mString == null) && (mResponse != null)) {
            ResponseBody responseBody = mResponse.body();
            if (responseBody != null) {
                try {
                    mString = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mString;
    }

    public JSONObject getBodyJSON() {
        if (mJSON != null) {
            return mJSON;
        }
        String body = getBodyString();
        if (!TextUtils.isEmpty(body)) {
            try {
                mJSON = new JSONObject(body);
                return mJSON;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray array = new JSONArray(body);
                mJSON = new JSONObject();
                mJSON.put("array", array);
                return mJSON;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Exception getError() {
        return mError;
    }

}
