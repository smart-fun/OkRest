package fr.arnaudguyon.okrest;

/**
 * Headers for a Request
 */

public class RequestHeaders extends RequestPairs {

    public RequestHeaders() {
    }

    public RequestHeaders(String... args) {
        super(args);
    }

    public RequestHeaders(RequestHeaders other) {
        super(other);
    }

    public RequestHeaders addAcceptJson() {
        add("Accept", "application/json");
        return this;
    }

}
