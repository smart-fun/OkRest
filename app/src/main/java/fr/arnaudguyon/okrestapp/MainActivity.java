package fr.arnaudguyon.okrestapp;

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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.arnaudguyon.okrest.OkRequest;
import fr.arnaudguyon.okrest.OkResponse;
import fr.arnaudguyon.okrest.RequestListener;
import fr.arnaudguyon.okrest.RequestParams;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_COMMENTS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final TextView resultView = findViewById(R.id.resultView);

        OkRequest request = new OkRequest.Builder()
                .url("https://world.openfoodfacts.org/api/v0/product/737628064502.json")
//                .url("https://jsonplaceholder.typicode.com/posts/1")
//                .addParam("postId", "1")
                .addHeader("Accept", "application/json")
                .build();

        request.execute(this, REQUEST_COMMENTS_ID, new RequestListener() {
            @Override
            public void onRequestResponse(boolean success, int requestCode, OkResponse response) {
                if (success) {
                    String result = response.getBodyJSON().toString();
                    resultView.setText(result);
                    Log.i(TAG, result);
                } else {
                    String result = "error " + response.getStatusCode();
                    Log.i(TAG, result);
                    resultView.setText(result);
                }
            }

        });

//        request.cancel();

        RequestParams first = new RequestParams("a", "1");
        first.add("b", "2");
        RequestParams second = new RequestParams(first);
        Log.i(TAG, "second: " + second.toString());

    }
}
