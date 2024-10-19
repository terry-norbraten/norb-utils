package com.norb.utils;

import java.util.ResourceBundle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author terry
 */
public class ProjectResourcesTest extends Assert {

    public ProjectResourcesTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProjectProperties method, of class ProjectResources.
     */
    @Test
    public void testGetProjectProperties() {
        System.out.println("getProjectProperties");
        ResourceBundle expResult = ProjectResources.getProjectProperties();
        ResourceBundle result = ProjectResources.getProjectProperties();
        assertSame(expResult, result);
    }

    /**
     * Test of getProperty method, of class ProjectResources.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
        String prop = "hellow.world";
        String expResult = "Hello World!";
        String result = ProjectResources.getProperty(prop);
        assertEquals(expResult, result);
    }

}
