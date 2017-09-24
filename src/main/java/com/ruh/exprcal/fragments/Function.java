/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import com.google.common.math.BigIntegerMath;
import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.exceptions.IllegalInitialisationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A fragment of 'Function' type
 */
public class Function extends ExpressionFragment {
    /**
     * The pattern to recognize a Function
     */
    private static final String FUNCTION_PATTERN = "[a-z]+";
    /**
     * Stores the name of the function
     */
    private String name;
    /**
     * Stores a queue of expressions accepted in the function
     */
    private Queue<Expression> exp = new LinkedList<>();
    /**
     * Stores the name of default functions in the array
     */
    private static final String[] DEFAULT_FUNCTIONS = {"sin", "cosec", "cos", "sec", "tan", "cot", "log", "ln", "pow", "sqrt", "cbrt", "fact"};
    /**
     * Holds all the Functions in the pool
     */
    private static HashMap<String,Method> functionPool = new HashMap<>();
    /**
     * Holds the final result after execution
     */
    private double result;
    /**
     * The degree multiplicand
     */
    private final double degree;
    /**
     * The trigonometric flag & round up scale
     */
    private static int trigFlag, roundScale;
    /**
     * The DEGREE and RADIAN statics to control the result
     */
    public static final int DEGREE = 0, RADIAN = 1;
    /**
     * The solution available flag for caching
     */
    private boolean processed = false;
    
    /**
     * The {@code static} block that populates the Function Pool
     */
    static {
        try {
            populateMethodPool();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The constructor that makes a Function fragment.
     * @param pos the position where the expression will exist
     * @param value the function in String format
     * @param trigFl the DEGREE flag (set:- <code>ExpressionRenderer.DEGREE</code> or <code>ExpressionRenderer.RADIAN</code>)
     * @param roundSc the rounding up scale (set:- Any integer no. within 0-9)
     * @throws BadExpressionFragmentException if expression fragments are unsupported
     * @throws BadExpressionException if function or expression can't be parsed
     */
    public Function(int pos, String value, int trigFl, int roundSc) throws BadExpressionFragmentException, BadExpressionException {
        super(pos, value);
        trigFlag = trigFl;
        roundScale = roundSc;
        formFunction(value);
        switch (trigFlag) {
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
    
    /**
     * Adds the declared function to the Function Pool
     * <br>How to add the method:-
     * <pre>
     * if Math.abs(double a) is the method that is to be added to the pool then do it like
     * Eg:- {@code object.add("absolute", Math.class.getDeclaredMethod("abs", double.class));}
     * </pre>
     * @param s the name by which the method will be known in the expression
     * @param m the {@code static} method that will be invoked
     * @throws IllegalInitialisationException if declaration violates the paradigm
     */
    public static void add(String s,Method m) throws IllegalInitialisationException
    {
        if (isNotDefault(s)){
            if (s.matches(FUNCTION_PATTERN))
                functionPool.put(s, m);
            else
                throw new IllegalInitialisationException();
        }
    }

    /**
     * Returns the result of the function in com.ruh.exprcal.fragments.Number format
     * @return the result in com.ruh.exprcal.fragments.Number format
     * @throws BadExpressionFragmentException if number format is not supported
     */
    public Number getResult() throws BadExpressionFragmentException {
        return new Number(BASIC_POS, result);
    }

    /**
     * Forms the function with name and method
     * @param s the entire function in {@code String} format
     * @throws BadExpressionFragmentException if function doesn't exist in the pool
     * @throws BadExpressionException if expression can't be parsed
     */
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
            this.exp.add(new Expression(BASIC_POS, elem, trigFlag, roundScale));
        }
    }
    
    /**
     * Processes the function and returns the reference to self
     * @return the the reference to self
     * @throws BadExpressionException if the function doesn't follow standard expression paradigms
     * @throws BadExpressionFragmentException if the fragments doesn't follow standard paradigms
     */
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
                        temp_res = (double)functionPool.get(name).invoke(null, temp.poll() * degree);
                        break;
                    case "log":
                    case "ln":
                    case "sqrt":
                    case "fact":
                        temp_res = (double)functionPool.get(name).invoke(null, temp.poll());
                        break;
                    default:
                        if (exists(name) && functionPool.get(name).getParameterCount()==temp.size())
                            temp_res = (double)functionPool.get(name).invoke(null, temp.poll());
                        else
                            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            case 2:
                switch (name) {
                    case "pow":
                        temp_res = (double)functionPool.get(name).invoke(null, temp.poll(), temp.poll());
                        break;
                    default:
                        if (exists(name) && functionPool.get(name).getParameterCount()==temp.size())
                            temp_res = (double)functionPool.get(name).invoke(null, temp.poll(), temp.poll());
                        else
                            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
                }
                break;
            default:
                throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
        }

        result = new BigDecimal(temp_res).setScale(roundScale, RoundingMode.CEILING).doubleValue();
        processed = true;
        }
        catch(IllegalAccessException| IllegalArgumentException| InvocationTargetException e)
        {
            throw new BadExpressionFragmentException("Function doesn't exist with these arguments", super.getValue());
        }
        return this;
    }
    
    /**
     * Checks if a function is default
     * @param cons the function to be checked
     * @return {@code false} if the function is DEFAUALT, otherwise {@code true}
     */
    private static boolean isNotDefault(String s)
    {
        for (String elem:DEFAULT_FUNCTIONS)
            return !(elem.equals(s));
        return true;
    }

    /**
     * Checks whether a function exists in the pool
     * @param s the function name to be checked
     * @return {@code true} if the function exists in the pool, otherwise {@code false}
     */
    public static boolean exists(String s) {
        return functionPool.containsKey(s);
    }
    
    /**
     * Populates function pool when the class loads
     * @throws NoSuchMethodException if Method passed isn't found
     */
    private static void populateMethodPool() throws NoSuchMethodException
    {
        for (String elem:DEFAULT_FUNCTIONS)
        {
            switch(elem)
            {
                case "sin":
                case "cos":
                case "tan":
                case "sqrt":
                case "cbrt":
                    functionPool.put(elem, Math.class.getDeclaredMethod(elem, double.class));
                    break;
                case "cosec":
                case "sec":
                case "cot":
                case "ln":
                case "log":
                case "fact":
                    functionPool.put(elem, FunctionWrapper.class.getDeclaredMethod(elem, double.class));
                    break;
                case "pow":
                    functionPool.put(elem, Math.class.getDeclaredMethod(elem, double.class, double.class));
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
    
    /**
     * The function wrapper class for functions with unsupported names
     */
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
        public static double ln(double a)
        {
            return Math.log(a);
        }
        public static double log(double a)
        {
            return Math.log10(a);
        }
        public static double fact(double a)
        {
            return BigIntegerMath.factorial((int)a).doubleValue();
        }
    }

}
