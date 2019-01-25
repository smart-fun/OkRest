# OkRest
do not use yet. Wrapper for OkHttp library to make it faster to use.

## Usage

### Prepare Request

Simple GET example with a parameter and a header:

```java

OkRequest request = new OkRequest.Builder()
        .url("https://jsonplaceholder.typicode.com/posts/1")
        .addParam("postId", "1")
        .addHeader("Accept", "application/json")
        .build();
        
```

### Send Request and handle result

```java

request.execute(context, REQUEST_CODE, new RequestListener() {
    @Override
    public void onRequestResponse(boolean success, int requestCode, OkResponse response) {
        if (success) {
            String result = response.getBodyJSON().toString();
            Log.i(TAG, result);
        } else {
            String result = "error " + response.getStatusCode();
            Log.i(TAG, result);
        }
    }

});

```