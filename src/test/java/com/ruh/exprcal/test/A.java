package com.ruh.exprcal.test;

//import com.ruh.exprcal.fragments.Constant;
//import com.ruh.exprcal.fragments.Function;
import com.ruh.exprcal.fragments.Constant;
import com.ruh.exprcal.fragments.Function;
import com.ruh.exprcal.renderer.ExpressionRenderer;

public class A {

    public static void main(String[] args) {

        try {
            String s = "----X^2+Y^2+-sin(PI)";
            ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
            e.add("U=9","X=U","X=-+-sqrt(X)","Y=4").setExpression(s).render();
            System.out.println(e.getExpression());
            System.out.println(e.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
