package com.ruh.exprcal.test;

import com.ruh.exprcal.fragments.Constant;
import com.ruh.exprcal.fragments.Function;
import com.ruh.exprcal.renderer.ExpressionRenderer;

public class A {

    public static void main(String[] args) {

        try {
            String s = "X^2+Y^2+fact(5)";
            String s1 = "si(0.0)";
            Constant.add("X",3);
            Constant.add("Y",4);
            Function.add("si", Math.class.getDeclaredMethod("sin", double.class));
            ExpressionRenderer e = new ExpressionRenderer(s1, ExpressionRenderer.RADIAN, 4);
            System.out.println(e.getExpression());
            System.out.println(e.render().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
