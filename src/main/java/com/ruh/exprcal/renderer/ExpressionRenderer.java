package com.ruh.exprcal.renderer;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.exceptions.IllegalInitialisationException;
import com.ruh.exprcal.fragments.Constant;
import com.ruh.exprcal.fragments.Expression;
import com.ruh.exprcal.fragments.Function;
import java.lang.reflect.Method;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class ExpressionRenderer {

    private Expression exp;
    private final int trig_flag;
    private final int round_scale;
    private double result;
    public static final int DEGREE = Function.DEGREE, RADIAN = Function.RADIAN;
    private boolean solved=false;

    public ExpressionRenderer(String s, int trig_flag, int round_scale) throws BadExpressionFragmentException, BadExpressionException {
        this.trig_flag = trig_flag;
        this.round_scale = round_scale;
        this.exp = new Expression(ExpressionFragment.BASIC_POS, s, trig_flag, round_scale);
    }
    
    public ExpressionRenderer(int trig_flag, int round_scale){
        this.trig_flag = trig_flag;
        this.round_scale = round_scale;
        this.solved = false;
        this.result = 0.0;
        this.exp = null;
    }
    
    public ExpressionRenderer setExpression(String ex) throws BadExpressionFragmentException, BadExpressionException
    {
        this.exp = new Expression(ExpressionFragment.BASIC_POS, ex, trig_flag, round_scale);
        
        return this;
    }
    
    public ExpressionRenderer add(String... cons_list) throws IllegalInitialisationException
    {
        Constant.add(trig_flag, cons_list);
        return this;
    }
    
    public ExpressionRenderer add(String func_name, Method meth)
    {
        Function.add(func_name, meth);
        return this;
    }
    
    public Expression getExpression() {
        return this.exp;
    }

    public double getResult() {
        return result;
    }

    public ExpressionRenderer render() throws BadExpressionFragmentException, BadExpressionException {
        if (solved)
            return this;
        if (exp!=null){
            result = this.exp.solve().getResult().getNumber();
            solved=true;
        }
        else{
            throw new BadExpressionException("Empty Expression", "");
        }
        return this;
    }

}
