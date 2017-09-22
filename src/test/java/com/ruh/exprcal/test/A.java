package com.ruh.exprcal.test;

//import com.ruh.exprcal.fragments.Constant;
//import com.ruh.exprcal.fragments.Function;
import com.ruh.exprcal.fragments.Constant;
import com.ruh.exprcal.fragments.Function;
import com.ruh.exprcal.renderer.ExpressionRenderer;

public class A {

    public static void main(String[] args) {

        try {
            String s = "X^2+Y^2+abs(-30.3348)";
            String s1 = "si(0.0)";
//            Constant.add("X",9);
//            Constant.add(ExpressionRenderer.DEGREE,"X=-sqrt(X)","Y=4");
//            Function.add("abs", Math.class.getDeclaredMethod("abs", double.class));
            ExpressionRenderer e = new ExpressionRenderer(ExpressionRenderer.DEGREE, 4);
            e.add("X=9","X=-sqrt(X)","Y=4").add("abs", Math.class.getDeclaredMethod("abs", double.class))
                    .setExpression(s).render();
            System.out.println(e.getExpression());
            System.out.println(e.render().getResult());
//            for (String elem:"X=".split("[=]"))
//            System.out.println(elem+"5");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
