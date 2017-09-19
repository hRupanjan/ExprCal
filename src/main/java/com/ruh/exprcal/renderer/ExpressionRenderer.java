package com.ruh.exprcal.renderer;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.fragments.Expression;
import com.ruh.exprcal.fragments.Function;

/**
 *
 * @author Rupanjan Hari
 */
public class ExpressionRenderer {

    private final Expression exp;
    private double result;
    public static final int DEGREE = Function.DEGREE, RADIAN = Function.RADIAN;
    private boolean solved=false;

    public ExpressionRenderer(String s, int trig_flag, int round_scale) throws BadExpressionFragmentException, BadExpressionException {
        this.exp = new Expression(ExpressionFragment.BASIC_POS, s, trig_flag, round_scale);
    }

    public Expression getExpression() {
        return exp;
    }

    public double getResult() {
        return result;
    }

    public ExpressionRenderer render() throws BadExpressionFragmentException, BadExpressionException {
        if (solved)
            return this;
        result = exp.solve().getResult().getNumber();
        solved=true;
        return this;
    }

}
