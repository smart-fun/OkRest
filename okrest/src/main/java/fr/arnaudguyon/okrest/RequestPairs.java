package fr.arnaudguyon.okrest;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Base class for RequestHeaders and RequestParams.
 * Holds a list of Pairs(String, String)
 */

abstract class RequestPairs {

    private ArrayList<Pair<String, String>> mParams = new ArrayList<>();

    protected RequestPairs() {
    }

    public RequestPairs(RequestPairs other) {
        for(Pair<String, String> pair : other.mParams) {
            mParams.add(Pair.create(pair.first, pair.second));
        }
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

    public String getFirst(String key) {
        for(Pair<String, String> pair : mParams) {
            if (TextUtils.equals(pair.first, key)) {
                return pair.second;
            }
        }
        return null;
    }

    public ArrayList<String> getAll(String key) {
        ArrayList<String> result = new ArrayList<>();
        for(Pair<String, String> pair : mParams) {
            if (TextUtils.equals(pair.first, key)) {
                result.add(pair.second);
            }
        }
        return result;
    }
}
