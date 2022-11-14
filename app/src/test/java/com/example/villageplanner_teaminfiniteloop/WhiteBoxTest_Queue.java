package com.example.villageplanner_teaminfiniteloop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class WhiteBoxTest_Queue {
    Queue tester = new Queue();

    @Test
    public void testCalculateDistance() {
        // test from cava restaurant to cava1 user
        // cava: 34.02507542003946, -118.28452602789426
        // cava1: 34.025084271502244, -118.28453136955459
        // distance: 0.0011 km or 5.905330181121826E-5 mile
        assertEquals(0.0007, tester.calculateDistance(34.02507542003946, -118.28452602789426, 34.025084271502244, -118.28453136955459), 0.0002);

        // test from cava to dulce1
        // dulce 1: 34.02546561267407, -118.2855536702259
        // distance: 0.0648 mile
        assertEquals(0.0648, tester.calculateDistance(34.02507542003946, -118.28452602789426, 34.02546561267407, -118.2855536702259), 0.0002);
    }

    @Test
    public void testStringToDouble() {
        ArrayList<Double> test = new ArrayList<Double>();
        test.add(3.0);
        test.add(4.0);
        assertEquals(test, tester.stringToDouble("3.0, 4.0"));
    }

    @Test
    public void testConvertToRad() {
        assertEquals(3.14159, tester.convertToRad(180), 0.00001);
    }
//    @Test
//    public void testToLocation() {
//        assertEquals(0, tester.toLocation("0.345, 0.345"));
//    }
}
