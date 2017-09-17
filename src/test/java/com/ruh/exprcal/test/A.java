package com.ruh.exprcal.test;

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
            String s = "1.233---.999.00+sin(30)/cos(60)tan(45)";
            System.out.println(new ExpressionRenderer(s, ExpressionRenderer.DEGREE, 4).getExpression());
            System.out.println(new ExpressionRenderer(s, ExpressionRenderer.DEGREE, 4).render().getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
