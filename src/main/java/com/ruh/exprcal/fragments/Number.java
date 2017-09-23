/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.util.regex.Pattern;

/**
 * A fragment of 'Number' type
 */
public class Number extends ExpressionFragment implements Comparable<Number> {
    /**
     * Stores the number in {@code double} format
     */
    private double num;
    /**
     * The pattern to recognize a Number
     */
    private static final String FLOAT_PATTERN = "([+-]?([0-9]*[.])?[0-9]+)|([+-]*[0-9]*[.][0-9]+[E][+-]*[0-9]+)";

    /**
     * The constructor to form a 0.0 valued number 
     */
    public Number() {
        super(ExpressionFragment.BASIC_POS, 0);
        this.num = 0.0;
    }

    /**
     * The constructor that makes a Number fragment.
     * @param pos the position where the number will exist
     * @param val the value in {@code String} format
     * @throws BadExpressionFragmentException if the Number is 'Infinity' or 'NaN'
     */
    public Number(int pos, String val) throws BadExpressionFragmentException {
        super(pos, val);
        if (Pattern.matches(FLOAT_PATTERN, val)) {
            this.num = Double.parseDouble(val);
        } else {
            if (val.equals("Infinity"))
                throw new BadExpressionFragmentException("Infinity", val);
            else
                throw new BadExpressionFragmentException("Not a Number", val);
        }

    }

    /**
     * The constructor that makes a Number fragment.
     * @param pos the position where the number will exist
     * @param val the value in {@code java.lang.Number} format
     * @throws BadExpressionFragmentException if the Number is 'Infinity' or 'NaN'
     */
    public Number(int pos, java.lang.Number val) throws BadExpressionFragmentException {
        super(pos, val);
        if (Pattern.matches(FLOAT_PATTERN, val.toString())) {
            this.num = val.doubleValue();
        } else {
            if (val.toString().equals("Infinity"))
                throw new BadExpressionFragmentException("Infinity", val.toString());
            else
                throw new BadExpressionFragmentException("Not a Number", val.toString());
        }
    }

    /**
     * Sets the value of the fragment
     * @param num the value in {@code String} format
     * @throws BadExpressionFragmentException if the Number is 'Infinity' or 'NaN'
     */
    public Number setNumber(String num) throws BadExpressionFragmentException {
        if (Pattern.matches(FLOAT_PATTERN, num)) {
            this.num = Double.parseDouble(num);
        } else {
            if (num.equals("Infinity"))
                throw new BadExpressionFragmentException("Infinity", num);
            else
                throw new BadExpressionFragmentException("Not a Number", num);
        }
        super.setValue(num);
        return this;
    }

    /**
     * Sets the value of the fragment
     * @param num the value in {@code java.lang.Number} format
     * @throws BadExpressionFragmentException if the Number is 'Infinity' or 'NaN'
     */
    public Number setNumber(java.lang.Number num) throws BadExpressionFragmentException {
        if (Pattern.matches(FLOAT_PATTERN, num.toString())) {
            this.num = num.doubleValue();
        } else {
            if (num.toString().equals("Infinity"))
                throw new BadExpressionFragmentException("Infinity", num.toString());
            else
                throw new BadExpressionFragmentException("Not a Number", num.toString());
        }
        super.setValue(num);
        return this;
    }

    /**
     * Returns the number in {@code double} format
     * @return the number in {@code double} format
     */
    public double getNumber() {
        return num;
    }
    
    /**
     * Checks whether a string is a valid number
     * @param value the string to be checked
     * @return {@code true} if the String is a valid number, otherwise {@code false}
     */
    public static boolean isValid(String value) {
        return Pattern.matches(FLOAT_PATTERN, value);
    }

    @Override
    public boolean isOpt() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isBracket() {
        return false;
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
    public int compareTo(Number o) {
        return Double.compare(this.num, o.getNumber());
    }
}
