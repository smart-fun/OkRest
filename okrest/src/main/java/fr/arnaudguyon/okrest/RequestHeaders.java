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

import androidx.annotation.NonNull;

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
