package com.ruh.exprcal.test;

import com.ruh.exprcal.fragments.Constant;
import com.ruh.exprcal.fragments.Expression;
import com.ruh.exprcal.renderer.ExpressionRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rupanjan Hari
 */
public class A {

    public static void main(String[] args) {

        try {
            String s = "X^2+Y^2+fact(5)";
            String s1 = "fact(0)";
            Constant.add("X",3);
            Constant.add("Y",4);
//            System.err.println(Math.pow(2, Math.sin(Math.PI)));
            ExpressionRenderer e = new ExpressionRenderer(s, ExpressionRenderer.RADIAN, 4);
            System.out.println(e.getExpression());
            System.out.println(e.render().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
