/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT) Licensed under MIT
 * (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.exceptions.IllegalInitialisationException;
import com.ruh.exprcal.renderer.ExpressionRenderer;
import java.util.HashMap;

/**
 * A fragment of 'Constant' type
 */
public class Constant extends ExpressionFragment {
    /**
     * The pattern to recognize a Constant
     */
    private static final String CONSTANT_PATTERN = "[A-Z]+";
    /**
     * The default constant names
     */
    private static final String DEFAULT_CONS[] = {"PI", "E"};
    /**
     * The default constant values
     */
    private static final double DEFAULT_CONS_VALUES[] = {Math.PI, Math.E};
    /**
     * The Map that will contain the constants. (The constant pool)
     */
    private static HashMap<String, Double> cons_pool = new HashMap<>();
    /**
     * The variable that will store a specific constant when an object is made
     */
    private final String cons_name;
    /**
     * Trigonometric Flag
     */
    private static int trig_flag;

    /**
     * The {@code static} block that populates the Constant Pool
     */
    static {
        populateConstantPool();
    }

    /**
     * The constructor that makes a Constant fragment. If the CONSTANT is not default 
     * the value can be changed as many times needed, but the last change will persist.
     * @param pos the position in the fragment map
     * @param cons the name of the Constant
     * @param value the value of the fragment
     * @param trig_fl the trigonometric flag i.e. DEGREE or RADIAN
     */
    public Constant(int pos, String cons, double value, int trig_fl) {
        super(pos, cons.toUpperCase());
        this.cons_name = cons.toUpperCase();
        trig_flag = trig_fl;
        convert();
        add(cons,value);
    }

    /**
     * The constructor that makes a Constant fragment. If the CONSTANT is not default 
     * the value can be changed as many times needed, but the last change will persist.
     * @param pos the position
     * @param cons the name of the Constant
     * @param trig_fl the trigonometric flag i.e. DEGREE or RADIAN
     * @throws BadExpressionFragmentException if the Constant is not present in the pool
     */
    public Constant(int pos, String cons, int trig_fl) throws BadExpressionFragmentException {
        super(pos, cons);
        trig_flag = trig_fl;
        convert();
        if (exists(cons)) {
            this.cons_name = cons.toUpperCase();
        } else {
            throw new BadExpressionFragmentException("Constant doesn't exist. Declare First", cons);
        }
    }

    /**
     * Converts the degree multiplicand with respect to the trigonometric flag.
     */
    private void convert() {
        switch (trig_flag) {
            case Function.DEGREE:
                cons_pool.replace("PI", 180.0);
                break;
            case Function.RADIAN:
                break;
        }

    }

    /**
     * Returns the value of the Constant if present in the pool in com.ruh.exprcal.fragments.Number format
     * @return the value of the Constant in com.ruh.exprcal.fragments.Number format
     * @throws BadExpressionFragmentException if the number is Infinity or NaN
     */
    public Number get() throws BadExpressionFragmentException {
        if (exists(cons_name)) {
            return new Number(BASIC_POS, cons_pool.get(cons_name));
        }
        return new Number(BASIC_POS, 0.0);
    }

    /**
     * Returns the value of the Constant if present in the pool
     * @param cons the name of the constant whose value is desired
     * @return the value of the constant in {@code double} format
     * @throws IllegalInitialisationException if Constant doesn't exist in the pool
     */
    public static double get(String cons) throws IllegalInitialisationException{
        if (exists(cons)) {
            return cons_pool.get(cons);
        }
        throw new IllegalInitialisationException("The constant doesn't exist in the pool\nConstant:-"+cons);
    }

    /**
     * Adds a Constant to the pool
     * @param cons the name of the constant
     * @param value the value of the constant
     */
    public static void add(String cons, double value) {
        if (isNotDefault(cons)) {
            cons_pool.put(cons.toUpperCase(), value);
        }
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
     * @param trig_flag the trigonometric flag
     * @param cons_list the constant initialization list
     * @throws IllegalInitialisationException
     * @throws BadExpressionFragmentException 
     */
    public static void add(int trig_flag, String... cons_list) throws IllegalInitialisationException, BadExpressionFragmentException {
        for (String elem : cons_list) {
            if (elem.matches(CONSTANT_PATTERN)) {
                add(elem, 0.0);
            } else {
                String[] segments = elem.split("[=]");
                if (segments.length == 2) {
                    if (segments[0].matches(CONSTANT_PATTERN)) {
                        if (Number.isValid(segments[1])) {
                            add(segments[0], Double.parseDouble(segments[1]));
                        }
                        else if(segments[1].matches(CONSTANT_PATTERN))
                        {
                            if (exists(segments[1]))
                                add(segments[0],get(segments[1]));
                        }
                        else {
                            try {
                                add(segments[0], new ExpressionRenderer(segments[1], trig_flag, 7).render().getResult());
                            } catch (BadExpressionException | BadExpressionFragmentException e) {
                                throw new IllegalInitialisationException();
                            }
                        }
                    } else {
                        throw new IllegalInitialisationException();
                    }
                } else {
                    if (segments.length == 1) {
                        if (segments[0].matches(CONSTANT_PATTERN)) {
                            add(segments[0], 0.0);
                        } else {
                            throw new IllegalInitialisationException();
                        }
                    } else {
                        throw new IllegalInitialisationException();
                    }
                }
            }
        }
    }

    /**
     * Populates constant pool when the class loads
     */
    private static void populateConstantPool() {
        for (int i = 0; i < DEFAULT_CONS.length; i++) {
            cons_pool.put(DEFAULT_CONS[i], DEFAULT_CONS_VALUES[i]);
        }
    }

    /**
     * Checks if a constant is default
     * @param cons the constant to be checked
     * @return {@code false} if the constant is DEFAUALT, otherwise {@code true}
     */
    private static boolean isNotDefault(String cons) {
        for (String elem : DEFAULT_CONS) {
            if (elem.equals(cons)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a constant exists in the pool
     * @param s the constant name to be checked
     * @return {@code true} if the constant exists in the pool, otherwise {@code false}
     */
    public static boolean exists(String s) {
        return cons_pool.containsKey(s);
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
        return false;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

}
