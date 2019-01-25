package fr.arnaudguyon.okrestapp;

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
        .url("https://jsonplaceholder.typicode.com/posts/1")
        .addParam("postId", "1")
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
