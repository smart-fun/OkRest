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

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

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

    public RequestPairs(@NonNull String key, @NonNull String value) {
        add(key, value);
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
