package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author Rupanjan Hari
 */
public class Sign extends ExpressionFragment {

    private final char sign;
    private static final char[] SIGNS = {'+', '-'};

    public Sign(int pos, char value) throws BadExpressionFragmentException {
        super(pos, value);
        this.sign = value;
    }

    public static boolean exists(char val) {
        for (int i = 0; i < SIGNS.length; i++) {
            if (SIGNS[i] == val) {
                return true;
            }
        }
        return false;
    }

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

}
