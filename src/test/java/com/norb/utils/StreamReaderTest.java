package com.norb.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.StreamReaderTest">Terry Norbraten, NPS MOVES</a>
 */
public class StreamReaderTest extends Assert {

    Path pIn;
    StreamReader instance = null;

    public StreamReaderTest() {
    }

    @Before
    public void setUp() throws IOException {
        pIn = Paths.get("src/main/resources/build.properties");
        instance = new StreamReader(Files.newInputStream(pIn), true); // <- stream to err
    }

    @After
    public void tearDown() {
        pIn = null;
        instance = null;
    }

    /**
     * Test of run method, of class StreamReader.
     */
    @Test
    public void testRun() {
        System.out.println("run");

        try {
            Thread t = new Thread(instance);
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotNull(instance);
    }

} // end class file StreamReaderTest.java