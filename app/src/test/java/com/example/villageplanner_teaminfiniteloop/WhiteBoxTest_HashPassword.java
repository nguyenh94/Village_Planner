package com.example.villageplanner_teaminfiniteloop;

import org.junit.Test;
import static org.junit.Assert.*;

public class WhiteBoxTest_HashPassword {
    Login_Registration tester = new Login_Registration();

    @Test
    public void testHashPassword() {
        assertEquals("202cb962ac59075b964b07152d234b70", tester.hashPassword("123"));
        assertEquals("098f6bcd4621d373cade4e832627b4f6", tester.hashPassword("test"));
    }
}
