package fr.arnaudguyon.okrestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import fr.arnaudguyon.okrest.OkRequest;
import fr.arnaudguyon.okrest.RequestListenerJSON;
import fr.arnaudguyon.okrest.RequestParams;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_COMMENTS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        OkRequest request = new OkRequest.Builder()
                .url("https://jsonplaceholder.typicode.com/comments")
                .params("postId", "1")
                .build();

        request.get(this, REQUEST_COMMENTS_ID, new RequestListenerJSON() {
            @Override
            public void onRequestSuccess(int requestCode, JSONObject responseBody) {
                Log.i(TAG, responseBody.toString());
            }

            @Override
            public void onRequestFailure(int requestCode, int statusCode, JSONObject responseBody) {
                Log.i(TAG, "error " + statusCode);
            }
        });

        RequestParams first = new RequestParams("a", "1", "b", "2");
        RequestParams second = new RequestParams(first);
        Log.i(TAG, "second: " + second.toString());

    }
}
