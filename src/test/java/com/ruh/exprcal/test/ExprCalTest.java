/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruh.exprcal.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 *
 * @author RUH
 */
public class ExprCalTest {
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(A.class);
        result.getFailures().forEach((failure) -> {
            System.out.println(failure.getMessage());
        });
        System.out.println(result.wasSuccessful());
    }
}
