package com.ruh.exprcal.test;

import com.ruh.exprcal.exceptions.BadExpressionException;
import com.ruh.exprcal.exceptions.BadExpressionFragmentException;
import com.ruh.exprcal.exceptions.IllegalInitialisationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.ruh.exprcal.renderer.ExpressionRenderer;

public class A {
    ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 6);
    @Test
    public void testCase1()
    {
        try {
            String s = "-+-+9";
            e.setExpression(s).render();
            assertEquals(9, e.getResult(), 0.001);
        } catch (BadExpressionException | BadExpressionFragmentException e) {
        }
    }
    @Test
    public void testCase2()
    {
        try {
            String s = "-+--+sin(0)";
            e.setExpression(s).render();
            assertEquals(0, e.getResult(), 0.001);
        } catch (BadExpressionException | BadExpressionFragmentException e) {
        }
    }
    @Test
    public void testCase3()
    {
        try {
            String s = "-+--+3*--5";
            e.setExpression(s).render();
            assertEquals(-15, e.getResult(), 0.001);
        } catch (BadExpressionException | BadExpressionFragmentException e) {
        }
    }
}
