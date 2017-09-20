package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Rupanjan Hari
 */
public class Function extends ExpressionFragment {

    private String name;
    private Queue<Expression> exp = new LinkedList<>();
    private static final String[] DEFAULT_FUNCTIONS = {"sin", "cosec", "cos", "sec", "tan", "cot", "log", "pow"};
    private static final List<String> FUNCTIONS = Arrays.asList(DEFAULT_FUNCTIONS);
    private double result;
    private final double degree;
    private static int trig_flag, round_scale;
    public static final int DEGREE = 0, RADIAN = 1;
    private boolean processed = false;

    public Function(int pos, String value, int trig_fl, int round_sc) throws BadExpressionFragmentException, BadExpressionException {
        super(pos, value);
        trig_flag = trig_fl;
        round_scale = round_sc;
        formFunction(value);
        switch (trig_flag) {
            case DEGREE:
                degree = Math.PI / 180;
                break;
            case RADIAN:
                degree = 1.0;
                break;
            default:
                degree = 0.0;
        }
    }

    public Number getResult() throws BadExpressionFragmentException {
        return new Number(BASIC_POS, result);
    }

    private void formFunction(String s) throws BadExpressionFragmentException, BadExpressionException {
        String temp = "";
        int i;
        for (i = 0; i < s.length(); i++) {
            if (Character.isAlphabetic(s.charAt(i))) {
                temp += s.charAt(i);
            } else {
                if (exists(temp)) {
                    break;
                } else {
                    throw new BadExpressionFragmentException("Not a Function", temp);
                }
            }
        }
        this.name = temp;
        String args[] = (s.substring(i + 1, s.length() - 1)).split("[,]");
        for (String elem : args) {
            this.exp.add(new Expression(BASIC_POS, elem, trig_flag, round_scale));
        }
    }

    public Function process() throws BadExpressionException, BadExpressionFragmentException {
        if (processed) {
            return this;
        }
        Double temp_res = 0.0;
        Queue<Double> temp = new LinkedList<>();
        for (Expression elem : exp) {
            temp.add(elem.solve().getResult().getNumber());
        }
        switch (temp.size()) {
            case 1:
                switch (name) {
                    case "sin":
                        temp_res = Math.sin(temp.poll() * degree);
                        break;
                    case "cosec":
                        temp_res = 1 / Math.sin(temp.poll() * degree);
                        break;
                    case "cos":
                        temp_res = Math.cos(temp.poll() * degree);
                        break;
                    case "sec":
                        temp_res = 1 / Math.cos(temp.poll() * degree);
                        break;
                    case "tan":
                        temp_res = Math.tan(temp.poll() * degree);
                        break;
                    case "cot":
                        temp_res = 1 / Math.tan(temp.poll() * degree);
                        break;
                    case "log":
                        temp_res = Math.log(temp.poll());
                        break;
                    default:
                        throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            case 2:
                switch (name) {
                    case "pow":
                        temp_res = Math.pow(temp.poll(), temp.poll());
                        break;
                    default:
                        throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            default:
                throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
        }

        result = new BigDecimal(temp_res).setScale(round_scale, RoundingMode.CEILING).doubleValue();
        processed = true;
        return this;
    }

    public static boolean exists(String s) {

        for (String function : FUNCTIONS) {

            if (s.equalsIgnoreCase(function)) {
                return true;
            }
        }
        return false;
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
        return false;
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

}
