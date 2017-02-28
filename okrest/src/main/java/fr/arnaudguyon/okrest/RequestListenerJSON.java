package fr.arnaudguyon.okrest;

import org.json.JSONObject;

/**
 * Created by aguyon on 17.01.17.
 */

public interface RequestListenerJSON extends RequestListener {
    void onRequestSuccess(int requestCode, JSONObject responseBody);
    void onRequestFailure(int requestCode, int statusCode, JSONObject responseBody);
}
