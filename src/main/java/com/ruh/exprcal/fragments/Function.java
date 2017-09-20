package com.ruh.exprcal.fragments;

import com.google.common.math.BigIntegerMath;
import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class Function extends ExpressionFragment {

    private String name;
    private Queue<Expression> exp = new LinkedList<>();
    private static final String[] DEFAULT_FUNCTIONS = {"sin", "cosec", "cos", "sec", "tan", "cot", "log", "pow", "sqrt", "fact"};
    private static HashMap<String,Method> function_pool = new HashMap<>();
    
    private double result;
    private final double degree;
    private static int trig_flag, round_scale;
    public static final int DEGREE = 0, RADIAN = 1;
    private boolean processed = false;
    
    
    static {
        try {
            populateMethodPool();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

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
    
    public static void add(String s,Method m)
    {
        if (isNotDefault(s))
            function_pool.put(s, m);
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
        
        try{
        double temp_res = 0.0;
        Queue<Double> temp = new LinkedList<>();
        for (Expression elem : exp) {
            temp.add(elem.solve().getResult().getNumber());
        }
        switch (temp.size()) {
            case 1:
                switch (name) {
                    case "sin":
                    case "cosec":
                    case "cos":
                    case "sec":
                    case "tan":
                    case "cot":
                        temp_res = (double)function_pool.get(name).invoke(null, temp.poll() * degree);
                        break;
                    case "log":
                    case "sqrt":
                    case "fact":
                        temp_res = (double)function_pool.get(name).invoke(null, temp.poll());
                        break;
                    default:
                        if (exists(name) && function_pool.get(name).getParameterCount()==temp.size())
                            temp_res = (double)function_pool.get(name).invoke(null, temp.poll());
                        else
                            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            case 2:
                switch (name) {
                    case "pow":
                        temp_res = (double)function_pool.get(name).invoke(null, temp.poll(), temp.poll());
                        break;
                    default:
                        if (exists(name) && function_pool.get(name).getParameterCount()==temp.size())
                            temp_res = (double)function_pool.get(name).invoke(null, temp.poll(), temp.poll());
                        else
                            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            default:
                throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
        }

        result = new BigDecimal(temp_res).setScale(round_scale, RoundingMode.CEILING).doubleValue();
        processed = true;
        }
        catch(IllegalAccessException| IllegalArgumentException| InvocationTargetException e)
        {
            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
        }
        return this;
    }
    
    private static boolean isNotDefault(String s)
    {
        for (String elem:DEFAULT_FUNCTIONS)
            return !(elem.equals(s));
        return true;
    }

    public static boolean exists(String s) {
        return function_pool.containsKey(s);
    }
    
    private static void populateMethodPool() throws NoSuchMethodException
    {
        for (String elem:DEFAULT_FUNCTIONS)
        {
            switch(elem)
            {
                case "sin":
                case "cos":
                case "tan":
                case "log":
                case "sqrt":
                    function_pool.put(elem, Math.class.getDeclaredMethod(elem, double.class));
                    break;
                case "cosec":
                case "sec":
                case "cot":
                case "fact":
                    function_pool.put(elem, FunctionWrapper.class.getDeclaredMethod(elem, double.class));
                    break;
                case "pow":
                    function_pool.put(elem, Math.class.getDeclaredMethod(elem, double.class, double.class));
                    break;
            }
        }
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
    
    private static class FunctionWrapper
    {
        public static double cosec(double a)
        {
            return 1/Math.sin(a);
        }
        public static double sec(double a)
        {
            return 1/Math.cos(a);
        }
        public static double cot(double a)
        {
            return 1/Math.tan(a);
        }
        public static double fact(double a)
        {
            return BigIntegerMath.factorial((int)a).doubleValue();
        }
    }

}
