/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.abstractions.Symbol;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;

/**
 * A fragment of 'Bracket' type
 */
public class Bracket extends ExpressionFragment implements Symbol {
    /**
     * The bracket character
     */
    private char brac;
    /**
     * The {@code static} array that contains the types of Brackets
     */
    private static final char[] BRACKETS = {'(', ')'};
    /**
     * The static variables that signifies the states of Bracket
     */
    public static final int OPEN = -1, CLOSE = 1;
    /**
     * Variable that stores the state
     */
    private int bracType;

    /**
     * The constructor that makes a Bracket fragment
     * @param pos the position of the fragment
     * @param val the value of the fragment
     * @throws BadExpressionFragmentException if the value is unsupported
     */
    public Bracket(int pos, char val) throws BadExpressionFragmentException {
        super(pos, val);
        if (exists(val)) {
            this.brac = val;
            if (this.brac == '(') {
                this.bracType = Bracket.OPEN;
            } else if (this.brac == ')') {
                this.bracType = Bracket.CLOSE;
            }
        } else {
            throw new BadExpressionFragmentException("Not a Bracket", "" + val);
        }
    }

    /**
     * Sets new value to the Bracket fragment
     * @param val the new value of the Bracket
     * @return a reference to self
     * @throws BadExpressionFragmentException if the character isn't supported
     */
    public Bracket setBracket(char val) throws BadExpressionFragmentException {
        if (exists(val)) {
            this.brac = val;
            if (this.brac == '(') {
                this.bracType = Bracket.OPEN;
            } else if (this.brac == ')') {
                this.bracType = Bracket.CLOSE;
            }
        } else {
            throw new BadExpressionFragmentException("Not a Bracket", "" + val);
        }
        super.setValue(val);
        return this;
    }

    /**
     * The value in {@code char} format
     * @return the character that represents the bracket
     */
    public char getBracket() {
        return brac;
    }

    /**
     * Returns the state of the bracket
     * @return {@code true} if the bracket is an open bracket otherwise {@code false}
     */
    public boolean isOpen() {
        switch (this.getType()) {
            case Bracket.OPEN:
                return true;
            default:
                return false;
        }

    }

    /**
     * Returns the state of the bracket
     * @return {@code true} if the bracket is an close bracket otherwise {@code false}
     */
    public boolean isClose() {
        return !isOpen();
    }

    /**
     * Checks whether the character is in the {@code BRACKETS} array.
     * @param val the 
     * @return {@code true} if the character exists in the BRACKETS array otherwise {@code false}
     */
    public static boolean exists(char val) {
        for (int i = 0; i < BRACKETS.length; i++) {
            if (BRACKETS[i] == val) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getType() {
        return bracType;
    }

    @Override
    public boolean isOpt() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isBracket() {
        return true;
    }

    @Override
    public boolean isSign() {
        return false;
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public char getChar() {
        return getBracket();
    }

    @Override
    public int getPriority() {
        switch (getChar()) {
            case '(':
            case ')':
                return 0;
            default:
                return ExpressionFragment.HIGH_OPT_PRIORITY;
        }
    }

}
