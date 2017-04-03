package fr.arnaudguyon.okrest;

import android.support.annotation.NonNull;

import java.util.Set;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by aguyon on 03.04.17.
 */

public class ResponseHeaders extends RequestPairs {

    private ResponseHeaders() {
    }

    ResponseHeaders(Response response) {
        if (response != null) {
            Headers headers = response.headers();
            if (headers != null) {
                Set<String> keys = headers.names();
                for (String key : keys) {
                    String value = headers.get(key);
                    add(key, value);
                }
            }
        }
    }

}
