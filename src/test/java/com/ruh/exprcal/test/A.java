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
            String s = "(2/2/X)";
            Constant.add("X",4.0);
            ExpressionRenderer e = new ExpressionRenderer(s, ExpressionRenderer.DEGREE, 4);
            System.out.println(e.getExpression());
            System.out.println(e.render().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
