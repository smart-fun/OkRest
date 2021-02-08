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

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkResponse {

    private Response mResponse;
    private ResponseHeaders mHeaders;
    private String mString;
    private JSONObject mJSON;
    private Exception mError;

    OkResponse(@NonNull Response response) {
        mResponse = response;
        ResponseBody responseBody = mResponse.body();
        if (responseBody != null) {
            try {
                mString = responseBody.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public @NonNull Exception getError() {
        if (mError != null) {
            return mError;
        }
        if (mResponse != null) {
            String message = mResponse.message();
            return new NetworkErrorException(message);
        }
        return new NetworkErrorException();
    }

}
