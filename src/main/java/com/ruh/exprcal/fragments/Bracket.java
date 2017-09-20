package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.abstractions.Symbol;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class Bracket extends ExpressionFragment implements Symbol {

    private char brac;
    private static final char[] BRACKETS = {'(', ')'};
    public static final int OPEN = -1, CLOSE = 1;
    private int brac_type;

    public Bracket(int pos, char val) throws BadExpressionFragmentException {
        super(pos, val);
        if (exists(val)) {
            this.brac = val;
            if (this.brac == '(') {
                this.brac_type = Bracket.OPEN;
            } else if (this.brac == ')') {
                this.brac_type = Bracket.CLOSE;
            }
        } else {
            throw new BadExpressionFragmentException("Not a Bracket", "" + val);
        }
    }

    public Bracket setBracket(char val) throws BadExpressionFragmentException {
        if (exists(val)) {
            this.brac = val;
            if (this.brac == '(') {
                this.brac_type = Bracket.OPEN;
            } else if (this.brac == ')') {
                this.brac_type = Bracket.CLOSE;
            }
        } else {
            throw new BadExpressionFragmentException("Not a Bracket", "" + val);
        }
        super.setValue(val);
        return this;
    }

    public char getBracket() {
        return brac;
    }

    public boolean isOpen() {
        switch (this.getType()) {
            case Bracket.OPEN:
                return true;
            default:
                return false;
        }

    }

    public boolean isClose() {
        return !isOpen();
    }

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
        return brac_type;
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
