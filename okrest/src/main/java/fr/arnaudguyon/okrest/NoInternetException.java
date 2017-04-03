package fr.arnaudguyon.okrest;

/**
 * Created by aguyon on 03.04.17.
 */

public class NoInternetException extends Exception {

    public NoInternetException() {
        super("No Internet Connection");
    }

}
