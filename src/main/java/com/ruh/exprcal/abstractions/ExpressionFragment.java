package com.ruh.exprcal.abstractions;

import com.ruh.exprcal.fragments.Expression;

/**
 *
 * @author Rupanjan Hari
 */
public abstract class ExpressionFragment implements java.io.Serializable {

    private String value;
    private int position;
    public static final int HIGH_OPT_PRIORITY = 999, LOW_OPT_PRIORITY = -999, BASIC_POS = 0;
    public static final String FRAG_NUM = "NUMBER", FRAG_OPT = "OPERATOR", FRAG_SIGN = "SIGN", FRAG_BRACK = "BRACKET", FRAG_EXPR = "EXPRESSION", FRAG_FUNC = "FUNCTION";

    public ExpressionFragment(int pos, String value) {
        this.value = value;
        this.position = pos;
    }

    public ExpressionFragment(int pos, char value) {
        this.value = "" + value;
        this.position = pos;
    }

    public ExpressionFragment(int pos, java.lang.Number value) {
        this.value = value.toString();
        this.position = pos;
    }

    public ExpressionFragment(int pos, Expression value) {
        this.value = value.toString();
        this.position = pos;
    }

    public ExpressionFragment setValue(String val) {
        this.value = val;
        return this;
    }

    public ExpressionFragment setValue(char val) {
        this.value = "" + val;
        return this;
    }

    public ExpressionFragment setValue(java.lang.Number val) {
        this.value = String.valueOf(val);
        return this;
    }

    public ExpressionFragment setValue(Expression val) {
        this.value = String.valueOf(val);
        return this;
    }

    public ExpressionFragment setPos(int pos) {
        this.position = pos;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public int getPos() {
        return position;
    }

    public String getFragmentType() {
        if (this.isBracket()) {
            return FRAG_BRACK;
        } else if (this.isNumber()) {
            return FRAG_NUM;
        } else if (this.isOpt()) {
            return FRAG_OPT;
        } else if (this.isSign()) {
            return FRAG_SIGN;
        } else if (this.isExpression()) {
            return FRAG_EXPR;
        } else if (this.isFunction()) {
            return FRAG_FUNC;
        }
        return "";
    }

    public abstract boolean isOpt();

    public abstract boolean isNumber();

    public abstract boolean isBracket();

    public abstract boolean isSign();

    public abstract boolean isExpression();

    public abstract boolean isFunction();

    @Override
    public String toString() {
        return value; //To change body of generated methods, choose Tools | Templates.
    }

}
