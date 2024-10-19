package com.norb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author terry
 */
public class StreamCopierTest extends Assert {

    File fIn, fOut;
    InputStream in = null;
    OutputStream out = null;

    public StreamCopierTest() {
    }

    @Before
    public void setUp() throws FileNotFoundException {
        fIn = new File("ReadMe");
        in = new FileInputStream(fIn);
        out = new FileOutputStream(fOut = new File("ReadMeCopy"));
    }

    @After
    public void tearDown() throws IOException {
        fIn = fOut = null;
        in.close();
        in = null;
        out.close();
        out = null;
    }

    /**
     * Test of copy method, o
     * @throws IOException class StreamCopier.
     */
    @Test
    public void testCopy() throws IOException {
        System.out.println("copy");
        StreamCopier.copy(in, out);
        assertTrue(fOut.exists());
    }

}
