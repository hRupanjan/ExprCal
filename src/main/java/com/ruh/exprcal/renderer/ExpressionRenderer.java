/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT)
 * Licensed under MIT (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
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
 * This class can be used to render an expression and get the result.
 * This class has chained methods for greater work flow and better reading experience.
 */
public class ExpressionRenderer {

    /**
     * Stores the Expression
     */
    private Expression exp;
    /**
     * Stores the flag to set the DEGREE statics
     */
    private final int trigFlag;
    /**
     * Stores the rounding factor for the resultant
     */
    private final int roundScale;
    /**
     * Stores the result after calculation
     */
    private double result;
    /**
     * The DEGREE and RADIAN statics to control the result
     */
    public static final int DEGREE = Function.DEGREE, RADIAN = Function.RADIAN;

    /**
     * Constructor to initialize the object for operation. (Method chaining isn't possible with Function and Constant addition)
     * @param ex the expression in String format
     * @param trigFlag the DEGREE flag (set:- <code>ExpressionRenderer.DEGREE</code> or <code>ExpressionRenderer.RADIAN</code>)
     * @param roundScale the rounding up scale (set:- Any integer no. within 0-9)
     * @throws BadExpressionFragmentException if any fragment in the expression is not recognizable
     * @throws BadExpressionException if the expression is not supported
     */
    public ExpressionRenderer(String ex, int trigFlag, int roundScale) throws BadExpressionFragmentException, BadExpressionException {
        this.trigFlag = trigFlag;
        this.roundScale = roundScale;
        this.exp = new Expression(ExpressionFragment.BASIC_POS, ex, trigFlag, roundScale);
    }
    
    /**
     * Constructor to initialize the object for operation. Specifically useful for method chaining.
     * @param trigFlag the DEGREE flag (set:- {@code ExpressionRenderer.DEGREE} or {@code ExpressionRenderer.RADIAN})
     * @param roundScale the rounding up scale (set:- Any integer no. within 0-9)
     */
    public ExpressionRenderer(int trigFlag, int roundScale){
        this.trigFlag = trigFlag;
        this.roundScale = roundScale;
        this.result = 0.0;
        this.exp = null;
    }
    
    /**
     * Sets the Expression to be rendered
     * @param ex the expression in String format
     * @return the reference to self
     * @throws BadExpressionFragmentException if any fragment in the expression is not recognizable
     * @throws BadExpressionException if the expression is not supported
     */
    public ExpressionRenderer setExpression(String ex) throws BadExpressionFragmentException, BadExpressionException
    {
        this.exp = new Expression(ExpressionFragment.BASIC_POS, ex, trigFlag, roundScale);
        
        return this;
    }
    
    /**
     * Adds the declared constants to the Constant Pool
     * Declarations that are accepted:-
     * <ul>
     * <li>"X" means X=0</li>
     * <li>"X=" means X=0</li>
     * <li>"X=10" means X=10.0</li>
     * <li>"X=0.0" means X=0.0</li>
     * <li>"X=3*4+sqrt(9)" means X=15.0</li>
     * <li>"X=A" means X=A=some pre-declared value</li>
     * </ul>
     * @param consList the constant declarations
     * @return the reference to self
     * @throws IllegalInitialisationException if any declaration violates the paradigm
     * @throws BadExpressionFragmentException if the Constant that is to be initialized is not present in the pool
     */
    public ExpressionRenderer add(String... consList) throws IllegalInitialisationException, BadExpressionFragmentException
    {
        Constant.add(trigFlag, consList);
        return this;
    }
    
    /**
     * Adds the declared function to the Function Pool
     * <br>How to add the method:-
     * <pre>
     * if Math.abs(double a) is the method that is to be added to the pool then do it like
     * Eg:- {@code object.add("absolute", Math.class.getDeclaredMethod("abs", double.class));}
     * </pre>
     * @param funcName the name by which the method will be known in the expression
     * @param method the {@code static} method that will be invoked
     * @return the reference to self
     * @throws IllegalInitialisationException if declaration violates the paradigm
     */
    public ExpressionRenderer add(String funcName, Method method) throws IllegalInitialisationException
    {
        Function.add(funcName, method);
        return this;
    }
    
    /**
     * Returns the reference to the expression
     * @return the reference to the expression
     */
    public Expression getExpression() {
        return this.exp;
    }

    /**
     * Before rendering it will return 0.0
     * @return the result after rendering the expression
     */
    public double getResult() {
        return result;
    }

    /**
     * Renders the expression to find the value.
     * @return the reference to self
     * @throws BadExpressionFragmentException if any fragment in the expression is not recognizable
     * @throws BadExpressionException if the expression is not supported or <em>null</em>
     */
    public ExpressionRenderer render() throws BadExpressionFragmentException, BadExpressionException {
        if (exp!=null){
            result = this.exp.solve().getResult().getNumber();
        }
        else{
            throw new BadExpressionException("Empty Expression", "");
        }
        return this;
    }

}
