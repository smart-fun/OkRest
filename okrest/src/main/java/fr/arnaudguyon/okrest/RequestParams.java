package fr.arnaudguyon.okrest;


import android.support.annotation.NonNull;

/**
 * Params for a Request
 */

public class RequestParams extends RequestPairs {

    public RequestParams(@NonNull String key, @NonNull String value) {
        super(key, value);
    }

    public RequestParams(RequestParams other) {
        super(other);
    }

}
