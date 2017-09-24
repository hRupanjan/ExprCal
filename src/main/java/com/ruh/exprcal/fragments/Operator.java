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
import java.util.regex.Pattern;

/**
 * A fragment of 'Operator' type
 */
public class Operator extends ExpressionFragment implements Symbol {
    /**
     * Stores the operator in character format
     */
    private char opt;
    /**
     * Array having a set of default operators
     */
    private static final char[] OPERATORS = {'+', '-', '*', '/', '^'};
    /**
     * <ul>
     * <li>DUAL - signifies if the operator is both Binary and Sign i.e '+' or '-'</li>
     * <li>BINARY - signifies if the opeartor is Binary i.e. '+','-','*','/' or '^'</li>
     * </ul>
     */
    public static final int BINARY = 0, DUAL = 1;
    /**
     * Stores the type of the operator
     */
    private int optType;

    /**
     * The constructor that makes a Operator fragment.
     * @param pos the position in the fragment map
     * @param val the value of the fragment
     * @throws BadExpressionFragmentException if character is not recognized as an operator
     */
    public Operator(int pos, char val) throws BadExpressionFragmentException {
        super(pos, val);
        if (exists(val)) {
            this.opt = val;
            if (isDual(val)) {
                this.optType = Operator.DUAL;
            }
            if (isBinary(val)) {
                this.optType = Operator.BINARY;
            }
        } else {
            throw new BadExpressionFragmentException("Not an Operator", "" + val);
        }
    }

    /**
     * Sets the operator of the object
     * @param val the value of the fragment
     * @return the reference to self
     * @throws BadExpressionFragmentException if character is not recognized as an operator
     */
    public Operator setOperator(char val) throws BadExpressionFragmentException {
        if (exists(val)) {
            this.opt = val;
            if (isDual(val)) {
                this.optType = Operator.DUAL;
            }
            if (isBinary(val)) {
                this.optType = Operator.BINARY;
            }
        } else {
            throw new BadExpressionFragmentException("Not an Operator", "" + val);
        }
        super.setValue(val);
        return this;
    }

    /**
     * Returns the Operator
     * @return the operator in {@code char} format
     */
    public char getOperator() {
        return opt;
    }

    /**
     * Checks whether the character exists in the operator array
     * @param val the operator that is to be checked
     * @return {@code true} if character is exists in the list, otherwise {@code false}
     */
    public static boolean exists(char val) {
        for (int i = 0; i < OPERATORS.length; i++) {
            if (OPERATORS[i] == val) {
                return true;
            }
        }
        return false;
    }

    /**
     * Operates on the numbers with the operator
     * @param x first number
     * @param opt operator
     * @param y second number
     * @return the result in com.ruh.exprcal.fragments.Number format
     * @throws BadExpressionFragmentException if result isn't supported in the number format
     */
    public static Number operate(Number x, Operator opt, Number y) throws BadExpressionFragmentException {
        double a = x.getNumber();
        double b = y.getNumber();
        double temp;
        switch (opt.getOperator()) {
            case '+':
                temp = a + b;
                break;
            case '-':
                temp = a - b;
                break;
            case '*':
                temp = a * b;
                break;
            case '/':
                temp = a / b;
                break;
            case '^':
                temp = Math.pow(a, b);
                break;
            default:
                temp = 0;
        }
        return new Number(ExpressionFragment.BASIC_POS, temp);
    }

    /**
     * If a character is binary
     * @param ch the character to be checked
     * @return {@code true} if the character is BINARY, otherwise {@code false}
     */
    private static boolean isBinary(char ch) {
        return Pattern.matches("[\\*]|[\\/]|[\\^]", "" + ch);
    }

    /**
     * If a character is dual
     * @param ch the character to be checked
     * @return {@code true} if the character is DUAL, otherwise {@code false}
     */
    private static boolean isDual(char ch) {
        return Pattern.matches("[\\+]|[\\-]", "" + ch);
    }

    /**
     * Returns the type of operator the current character could be deciding on on it's post and pre values
     * @param pre the fragment last inserted in the map
     * @param ch the current character
     * @param post the succeeding character
     * @return the type of the operator viz. BINARY or DUAL
     * @throws BadExpressionFragmentException if expression paradigms are violated
     */
    public static int returnOpt(ExpressionFragment pre, char ch, char post) throws BadExpressionFragmentException {
        switch (pre.getFragmentType()) {
            case ExpressionFragment.FRAG_BRACK: {
                if (((Bracket) pre).isClose()) {
                    if (Pattern.matches("[+-.0-9]", "" + post)) {
                        return BINARY;
                    } else if (Pattern.matches("[a-z]|[A-Z]", "" + post)) {
                        return BINARY;
                    } else if (Pattern.matches("[\\*\\/\\^]", "" + post)) {
                        throw new BadExpressionFragmentException("Two binary operators can't sit together");
                    } else if (post == '(') {
                        return BINARY;
                    } else if (post == ')') {
                        throw new BadExpressionFragmentException("Bracket is closing after a binary operator");
                    }
                } else {
                    if (isDual(ch)) {
                        return DUAL;
                    } else {
                        throw new BadExpressionFragmentException("A binary operator can't sit beside an Open Bracket");
                    }
                }
            }
            break;
            case ExpressionFragment.FRAG_NUM:
            case ExpressionFragment.FRAG_EXPR:
            case ExpressionFragment.FRAG_CONS:
            case ExpressionFragment.FRAG_FUNC: {
                if (Pattern.matches("[.0-9]", "" + post)) {
                    return BINARY;
                } else if (Pattern.matches("[a-z]|[A-Z]", "" + post)) {
                    return BINARY;
                } else if (Pattern.matches("[+-]", "" + post)) {
                    return BINARY;
                } else if (Pattern.matches("[\\*\\/\\^]", "" + post)) {
                    throw new BadExpressionFragmentException("Two binary operators can't sit together");
                } else if (post == '(') {
                    return BINARY;
                } else if (post == ')') {
                    throw new BadExpressionFragmentException("Bracket is closing after a binary operator");
                }
            }
            break;
            case ExpressionFragment.FRAG_OPT: {
                if (Pattern.matches("[.0-9]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[a-z]|[A-Z]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[+-]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[\\*\\/\\^]", "" + post)) {
                    throw new BadExpressionFragmentException("Two binary operators can't sit together");
                } else if (post == '(') {
                    return DUAL;
                } else if (post == ')') {
                    throw new BadExpressionFragmentException("Bracket is closing after a binary operator");
                }

            }
            break;
            case ExpressionFragment.FRAG_SIGN: {
                if (Pattern.matches("[.0-9]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[a-z]|[A-Z]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[+-]", "" + post)) {
                    return DUAL;
                } else if (Pattern.matches("[\\*\\/\\^]", "" + post)) {
                    throw new BadExpressionFragmentException("Two binary operators can't sit together");
                } else if (post == '(') {
                    return DUAL;
                } else if (post == ')') {
                    throw new BadExpressionFragmentException("Bracket is closing after a binary operator");
                }
            }
            break;

            default:
                return -999;
        }
        return -999;
    }

    @Override
    public int getType() {
        return optType;
    }

    @Override
    public char getChar() {
        return getOperator();
    }

    @Override
    public int getPriority() {
        switch (getChar()) {
            case '+':
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 3;
            case '^':
                return 4;
            default:
                return ExpressionFragment.HIGH_OPT_PRIORITY;
        }
    }

    @Override
    public boolean isOpt() {
        return true;
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

}
