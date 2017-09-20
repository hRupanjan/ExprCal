package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.abstractions.Symbol;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.util.regex.Pattern;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class Operator extends ExpressionFragment implements Symbol {

    private char opt;
    private static final char[] OPERATORS = {'+', '-', '*', '/', '^'};
    public static final int BINARY = 0, DUAL = 1;
    private int opt_type;

    public Operator(int pos, char val) throws BadExpressionFragmentException {
        super(pos, val);
        if (exists(val)) {
            this.opt = val;
            if (isDual(val)) {
                this.opt_type = Operator.DUAL;
            }
            if (isBinary(val)) {
                this.opt_type = Operator.BINARY;
            }
        } else {
            throw new BadExpressionFragmentException("Not an Operator", "" + val);
        }
    }

    public Operator setOperator(char val) throws BadExpressionFragmentException {
        if (exists(val)) {
            this.opt = val;
            if (isDual(val)) {
                this.opt_type = Operator.DUAL;
            }
            if (isBinary(val)) {
                this.opt_type = Operator.BINARY;
            }
        } else {
            throw new BadExpressionFragmentException("Not an Operator", "" + val);
        }
        super.setValue(val);
        return this;
    }

    public char getOperator() {
        return opt;
    }

    public static boolean exists(char val) {
        for (int i = 0; i < OPERATORS.length; i++) {
            if (OPERATORS[i] == val) {
                return true;
            }
        }
        return false;
    }

    public static Number operate(Number x, Operator opt, Number y) throws BadExpressionFragmentException {
        double a = x.getNumber();
        double b = y.getNumber();
        
//        System.err.println(x+" "+opt+" "+y);//check it all
        
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

    private static boolean isBinary(char ch) {
        return Pattern.matches("[\\*]|[\\/]|[\\^]", "" + ch);
    }

    private static boolean isDual(char ch) {
        return Pattern.matches("[\\+]|[\\-]", "" + ch);
    }

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
        return opt_type;
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
