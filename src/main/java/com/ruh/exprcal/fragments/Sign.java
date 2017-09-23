/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.util.Map;
import java.util.SortedMap;

/**
 * A fragment of 'Sign' type
 */
public class Sign extends ExpressionFragment {
    /**
     * Stores the sign in character format
     */
    private final char sign;
    /**
     * The array containing the signs
     */
    private static final char[] SIGNS = {'+', '-'};

    /**
     * The constructor that makes a Sign fragment.
     * @param pos the position in the fragment map
     * @param value the value of the fragment
     */
    public Sign(int pos, char value) {
        super(pos, value);
        this.sign = value;
    }

    /**
     * Checks whether the character exists in the sign array
     * @param val the sign that is to be checked
     * @return {@code true} if character is exists in the list, otherwise {@code false}
     */
    public static boolean exists(char val) {
        for (int i = 0; i < SIGNS.length; i++) {
            if (SIGNS[i] == val) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a sign block as an input and returns the multiplied number
     * @param list the sign block
     * @return the multiplied number in com.ruh.exprcal.fragments.Number format
     * @throws BadExpressionFragmentException if the String isn't supported in the number format
     */
    public static Number multiply(SortedMap<Integer, ExpressionFragment> list) throws BadExpressionFragmentException {
        char temp = '\0';
        for (Map.Entry<Integer, ExpressionFragment> elem : list.entrySet()) {
            if (temp == '\0') {
                temp = ((Sign) (elem.getValue())).sign;
                continue;
            }

            switch (((Sign) (elem.getValue())).sign) {
                case '-':
                    switch (temp) {
                        case '-':
                            temp = '+';
                            break;
                        case '+':
                            temp = '-';
                            break;
                    }
                    break;
                case '+':
                    switch (temp) {
                        case '-':
                            temp = '-';
                            break;
                        case '+':
                            temp = '+';
                            break;
                    }
                    break;
            }
        }
        return new Number(BASIC_POS, temp + "1");
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
        return false;
    }

    @Override
    public boolean isSign() {
        return true;
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

}
