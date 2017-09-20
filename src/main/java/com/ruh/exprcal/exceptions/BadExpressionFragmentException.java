package com.ruh.exprcal.exceptions;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class BadExpressionFragmentException extends Exception {

    /**
     * Creates a new instance of <code>BadExpressionFragmentException</code>
     * without detail message.
     */
    public BadExpressionFragmentException() {
        System.err.println("The expression fragment is bad resulting erronious output.");
    }

    /**
     * Constructs an instance of <code>BadExpressionFragmentException</code>
     * with the specified detail message with the fragment.
     *
     * @param msg the detail message.
     * @param fragment the Expression Fragment
     */
    public BadExpressionFragmentException(String msg, String fragment) {
        super(msg);
        System.err.println("The expression fragment is bad.\nFragment: " + fragment);
    }

    /**
     * Constructs an instance of <code>BadExpressionFragmentException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BadExpressionFragmentException(String msg) {
        super(msg);
    }
}
