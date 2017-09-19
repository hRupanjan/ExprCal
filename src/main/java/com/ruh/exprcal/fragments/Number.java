package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.util.regex.Pattern;

/**
 *
 * @author Rupanjan Hari
 */
public class Number extends ExpressionFragment implements Comparable<Number> {

    private double num;
    private static final String FLOAT_PATTERN = "[+-]?([0-9]*[.])?[0-9]+";

    public Number() {
        super(ExpressionFragment.BASIC_POS, 0);
        this.num = 0.0;
    }

    public Number(int pos, String val) throws BadExpressionFragmentException {
        super(pos, val);
        if (Pattern.matches(FLOAT_PATTERN, val)) {
            this.num = Double.parseDouble(val);
        } else {
            throw new BadExpressionFragmentException("Not a Number", val);
        }

    }

    public Number(int pos, java.lang.Number val) throws BadExpressionFragmentException {
        super(pos, val);
        if (Pattern.matches(FLOAT_PATTERN, val.toString())) {
            this.num = val.doubleValue();
        } else {
            throw new BadExpressionFragmentException("Not a Number", val.toString());
        }
    }

    public Number setNumber(String num) throws BadExpressionFragmentException {
        if (Pattern.matches(FLOAT_PATTERN, num)) {
            this.num = Double.parseDouble(num);
        } else {
            throw new BadExpressionFragmentException("Not a Number", num);
        }
        super.setValue(num);
        return this;
    }

    public Number setNumber(java.lang.Number num) throws BadExpressionFragmentException {
        if (Pattern.matches(FLOAT_PATTERN, num.toString())) {
            this.num = num.doubleValue();
        } else {
            throw new BadExpressionFragmentException("Not a Number", num.toString());
        }
        super.setValue(num);
        return this;
    }

    public double getNumber() {
        return num;
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

    public static boolean isValid(String value) {

        return Pattern.matches(FLOAT_PATTERN, value);
    }

}
