package com.example.villageplanner_teaminfiniteloop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class WhiteBoxTest_EmailFormat {
    Login_Registration tester = new Login_Registration();

    @Test
    public void testCheckEmailFormat() {
        assertTrue(tester.checkEmailFormat("test@gmail.com"));
        assertFalse(tester.checkEmailFormat("notvalidemail.com"));
        assertFalse(tester.checkEmailFormat("notvalidemailcom"));
    }
}
