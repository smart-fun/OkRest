package fr.arnaudguyon.okrest;

import android.support.annotation.NonNull;

/**
 * Headers for a Request
 */

public class RequestHeaders extends RequestPairs {

    public RequestHeaders(@NonNull String key, @NonNull String value) {
        super(key, value);
    }

    public RequestHeaders(RequestHeaders other) {
        super(other);
    }

    public RequestHeaders addAcceptJson() {
        add("Accept", "application/json");
        return this;
    }

}
