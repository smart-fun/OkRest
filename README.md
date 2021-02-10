# OkRest

Wrapper for OkHttp library to make it faster to use.

This is apha version of the library, do not use for production.

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

## Library License

Copyright 2017-2021 Arnaud Guyon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
