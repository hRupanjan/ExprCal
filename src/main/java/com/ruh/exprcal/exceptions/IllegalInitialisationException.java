/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.exceptions;

public class IllegalInitialisationException extends Exception {

    /**
     * Creates a new instance of <code>IllegalInitialisationException</code>
     * without detail message.
     */
    public IllegalInitialisationException() {
        System.err.println("The data is unsupported or paradigm is violated.");
    }

    /**
     * Constructs an instance of <code>IllegalInitialisationException</code>
     * with the specified detail message.
     * @param msg the detail message.
     */
    public IllegalInitialisationException(String msg) {
        super(msg);
    }
}
