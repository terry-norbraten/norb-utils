/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package com.norb.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.DateTimeGroupStampTest">Terry Norbraten, NPS MOVES</a>
 */
public class DateTimeGroupStampTest {

    DateTimeGroupStamp instance;

    public DateTimeGroupStampTest() {
    }

    @Before
    public void setUp() {
        instance = DateTimeGroupStamp.getInstance();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of getDateTimeGroup method, of class DateTimeGroupStamp.
     */
    @Test
    public void testGetDateTimeGroup() {
        System.out.println("getDateTimeGroup");
        String expResult = "Z";
        String result = instance.getDateTimeGroup();
        assertTrue(result.contains(expResult));
    }

    /**
     * Test of getCurrentDateTimeGroup method, of class DateTimeGroupStamp.
     */
    @Test
    public void testGetCurrentDateTimeGroup() {
        System.out.println("getCurrentDateTimeGroup");
        String expResult = "Z";
        String result = DateTimeGroupStamp.getCurrentDateTimeGroup();
        assertTrue(result.contains(expResult));
    }

} // end class file DateTimeGroupStampTest.java