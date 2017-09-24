/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.abstractions;

/**
 * An interface that signifies the Operators and Brackets
 */
public interface Symbol {

    /**
     * Returns the symbol in {@code character} format
     * @return the character under the symbol
     */
    public char getChar();

    /**
     * Returns the priority of the symbol in {@code int} format
     * @return the priority of the symbol
     */
    public int getPriority();

    /**
     * Returns the type of the Symbol.<br>
     * For Operators:-
     * <ul>
     * <li>DUAL - signifies if the operator is both Binary and Sign i.e '+' or '-'</li>
     * <li>BINARY - signifies if the operator is Binary i.e. '+','-','*','/' or '^'</li>
     * </ul><br>
     * For Brackets:-
     * <ul>
     * <li>OPEN - signifies if the Bracket is Open</li>
     * <li>CLOSE - signifies if the Bracket is Closed</li>
     * </ul>
     * @return the type of the symbol.
     */
    public int getType();
}
