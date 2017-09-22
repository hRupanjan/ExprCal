package com.ruh.exprcal.fragments;

import com.ruh.exprcal.abstractions.ExpressionFragment;
import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.exceptions.IllegalInitialisationException;
import com.ruh.exprcal.renderer.ExpressionRenderer;
import java.util.HashMap;

/**
 * -----------------------------------------------------------------------------
 * ExprCal (v1.0-SNAPSHOT) Licensed under MIT
 * (https://github.com/hRupanjan/ExprCal/blob/master/LICENSE)
 * -----------------------------------------------------------------------------
 */
public class Constant extends ExpressionFragment {

    private static final String CONSTANT_PATTERN = "[A-Z]+";
    private static final String DEFAULT_CONS[] = {"PI", "E"};
    private static final double DEFAULT_CONS_VALUES[] = {Math.PI, Math.E};
    private static HashMap<String, Double> cons_pool = new HashMap<>();
    private final String cons_name;
    private static int trig_flag;

    static {
        populateConstantPool();
    }

    public Constant(int pos, String cons, double value, int trig_fl) {
        super(pos, cons.toUpperCase());
        this.cons_name = cons.toUpperCase();
        trig_flag = trig_fl;
        convert();
        //if the CONSTANT is not default you can change the value as many times you want, but the last change will persist
        if (isNotDefault(cons_name)) {
            cons_pool.put(cons.toUpperCase(), value);
        }
    }

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

    private void convert() {
        switch (trig_flag) {
            case Function.DEGREE:
                cons_pool.replace("PI", 180.0);
                break;
            case Function.RADIAN:
                break;
        }

    }

    public Number get() throws BadExpressionFragmentException {
        if (exists(cons_name)) {
            return new Number(BASIC_POS, cons_pool.get(cons_name));
        }
        return new Number(BASIC_POS, 0.0);
    }

    public static double get(String cons) throws BadExpressionFragmentException {
        if (exists(cons)) {
            return cons_pool.get(cons);
        }
        return 0.0;
    }

    public static void add(String cons, double value) {
        if (isNotDefault(cons)) {
            cons_pool.put(cons.toUpperCase(), value);
        }
    }

    public static void add(int trig_flag, String... cons_list) throws IllegalInitialisationException {
        for (String elem : cons_list) {
            if (elem.matches(CONSTANT_PATTERN)) {
                add(elem, 0.0);
            } else {
                String[] segments = elem.split("[=]");
                if (segments.length == 2) {
                    if (segments[0].matches(CONSTANT_PATTERN)) {
                        if (Number.isValid(segments[1])) {
                            add(segments[0], Double.parseDouble(segments[1]));
                        } else {
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

    private static void populateConstantPool() {
        for (int i = 0; i < DEFAULT_CONS.length; i++) {
            cons_pool.put(DEFAULT_CONS[i], DEFAULT_CONS_VALUES[i]);
        }
    }

    private static boolean isNotDefault(String cons) {
        for (String elem : DEFAULT_CONS) {
            if (elem.toUpperCase().equals(cons.toUpperCase())) {
                return false;
            }
        }
        return true;
    }

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
