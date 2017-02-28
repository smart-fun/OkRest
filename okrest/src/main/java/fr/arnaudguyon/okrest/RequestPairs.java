package fr.arnaudguyon.okrest;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;

/**
 * Base class for RequestHeaders and RequestParams.
 * Holds a list of Pairs(String, String)
 */

abstract class RequestPairs {

    private ArrayList<Pair<String, String>> mParams = new ArrayList<>();

    protected RequestPairs() {
    }

    public RequestPairs(String... args) {
        for(int i=0; i< args.length; i+= 2) {
            add(args[i], args[i+1]);
        }
    }

    public void add(@NonNull String key, @NonNull String value) {
        mParams.add(Pair.create(key, value));
    }

    ArrayList<Pair<String, String>> getParams() {
        return mParams;
    }
}
