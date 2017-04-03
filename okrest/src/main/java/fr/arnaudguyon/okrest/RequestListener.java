package fr.arnaudguyon.okrest;

/**
 * Listener interface for Request responses
 */

public interface RequestListener {
    void onRequestResponse(boolean success, int requestCode, OkResponse response);
}
