package fr.arnaudguyon.okrest;

/**
 * Created by aguyon on 17.01.17.
 */

public interface RequestListenerString extends RequestListener {
    void onRequestSuccess(int requestCode, String responseBody);
    void onRequestFailure(int requestCode, int statusCode, String responseBody);
}
