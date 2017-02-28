package fr.arnaudguyon.okrest;

/**
 * Headers for a Request
 */

public class RequestHeaders extends RequestPairs {

    public RequestHeaders() {
    }

    public RequestHeaders(String... args) {
        for(int i=0; i< args.length; i+= 2) {
            add(args[i], args[i+1]);
        }
    }

}
