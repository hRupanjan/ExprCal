package com.ruh.exprcal.abstractions;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public interface Symbol {

    public char getChar();

    public int getPriority();

    public int getType();
}
