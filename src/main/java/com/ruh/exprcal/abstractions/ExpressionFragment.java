/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.abstractions;

import com.ruh.exprcal.fragments.Expression;

/**
 * An abstract class that unifies all the fragments. It can be serialized.
 */
public abstract class ExpressionFragment implements java.io.Serializable {
    /**
     * Value of the fragment
     */
    private String value;
    /**
     * Position of the fragment in the expression
     */
    private int position;
    /**
     * Static values to set Operator Priorities
     */
    public static final int HIGH_OPT_PRIORITY = 999, LOW_OPT_PRIORITY = -999;
    /**
     * Basic position of a fragment in the expression
     */
    public static final int BASIC_POS = 0;
    /**
     * The various types of fragments in {@code String} format
     */
    public static final String FRAG_NUM = "NUMBER", FRAG_OPT = "OPERATOR", FRAG_SIGN = "SIGN", FRAG_BRACK = "BRACKET", FRAG_EXPR = "EXPRESSION", FRAG_FUNC = "FUNCTION", FRAG_CONS="CONSTANT";

    /**
     * Constructor to set the character sequence as a fragment
     * @param pos the position where the fragment will be placed
     * @param value the value of the fragment
     */
    public ExpressionFragment(int pos, String value) {
        this.value = value;
        this.position = pos;
    }

    /**
     * Constructor to set the character as a fragment
     * @param pos the position where the fragment will be placed
     * @param value the value of the fragment
     */
    public ExpressionFragment(int pos, char value) {
        this.value = "" + value;
        this.position = pos;
    }

    /**
     * Constructor to set the number as a fragment
     * @param pos the position where the fragment will be placed
     * @param value the value of the fragment
     */
    public ExpressionFragment(int pos, java.lang.Number value) {
        this.value = value.toString();
        this.position = pos;
    }

    /**
     * Constructor to set the Expression as a fragment
     * @param pos the position where the fragment will be placed
     * @param value the value of the fragment
     */
    public ExpressionFragment(int pos, Expression value) {
        this.value = value.toString();
        this.position = pos;
    }

    /**
     * Sets the value of the fragment
     * @param val the character sequence that is to be set for the value
     * @return the reference to self
     */
    public ExpressionFragment setValue(String val) {
        this.value = val;
        return this;
    }

    /**
     * Sets the value of the fragment
     * @param val the character that is to be set for the value
     * @return the reference to self
     */
    public ExpressionFragment setValue(char val) {
        this.value = "" + val;
        return this;
    }

    /**
     * Sets the value of the fragment
     * @param val the number that is to be set for the value
     * @return the reference to self
     */
    public ExpressionFragment setValue(java.lang.Number val) {
        this.value = String.valueOf(val);
        return this;
    }

    /**
     * Sets the value of the fragment
     * @param val the Expression that is to be set for the value
     * @return the reference to self
     */
    public ExpressionFragment setValue(Expression val) {
        this.value = String.valueOf(val);
        return this;
    }

    /**
     * Sets the position of the fragment
     * @param pos the new position where the fragment needs to be placed
     * @return the reference to self
     */
    public ExpressionFragment setPos(int pos) {
        this.position = pos;
        return this;
    }

    /**
     * Returns the value
     * @return the value in {@code String} format
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the position of the fragment
     * @return the position in the map
     */
    public int getPos() {
        return position;
    }

    /**
     * Returns the type of fragment
     * @return the type of the fragment in {@code String} format
     */
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
        } else if (this.isConstant()) {
            return FRAG_CONS;
        }
        return "";
    }

    /**
     * @return {@code true} if Operator Fragment else {@code false}
     */
    public abstract boolean isOpt();

    /**
     * @return {@code true} if Number Fragment else {@code false}
     */
    public abstract boolean isNumber();

    /**
     * @return {@code true} if Bracket Fragment else {@code false}
     */
    public abstract boolean isBracket();

    /**
     * @return {@code true} if Sign Fragment else {@code false}
     */
    public abstract boolean isSign();

    /**
     * @return {@code true} if Expression Fragment else {@code false}
     */
    public abstract boolean isExpression();

    /**
     * @return {@code true} if Function Fragment else {@code false}
     */
    public abstract boolean isFunction();
    
    /**
     * @return {@code true} if Constant Fragment else {@code false}
     */
    public abstract boolean isConstant();

    @Override
    public String toString() {
        return value;
    }

}
