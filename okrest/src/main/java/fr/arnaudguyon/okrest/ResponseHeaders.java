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
