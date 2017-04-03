package fr.arnaudguyon.okrestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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

        OkRequest request = new OkRequest.Builder()
                .url("https://jsonplaceholder.typicode.com/comments")
                .params("postId", "1")
                .build();

        request.execute(this, REQUEST_COMMENTS_ID, new RequestListener() {
            @Override
            public void onRequestResponse(boolean success, int requestCode, OkResponse response) {
                if (success) {
                    Log.i(TAG, response.getBodyJSON().toString());
                } else {
                    Log.i(TAG, "error " + response.getStatusCode());
                }
            }

        });

        RequestParams first = new RequestParams("a", "1", "b", "2");
        RequestParams second = new RequestParams(first);
        Log.i(TAG, "second: " + second.toString());

    }
}
