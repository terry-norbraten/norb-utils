package com.norb.utils;

import static com.norb.utils.FileIO.copyFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.FileIOTest">Terry Norbraten, NPS MOVES</a>
 */
public class FileIOTest {

    File fIn, fOut;

    public FileIOTest() {
    }

    @Before
    public void setUp() {
        fIn = new File("ReadMe");
        fOut = new File("ReadMeCopy");
    }

    @After
    public void tearDown() {
        fIn = fOut = null;
    }

    /**
     * Test of copyFile method, of class FileIO.
     * @throws java.io.IOException
     */
    @Test
    public void testCopyFile_3args_1() throws IOException {
        System.out.println("copyFile 3 args");
        boolean close = true;
        FileIO.copyFile(fIn, fOut, close);
        assertTrue(fOut.exists());
    }

    /**
     * Test of copyFile method, of class FileIO.
     * @throws java.io.IOException
     */
    @Test
    public void testCopyFile_3args_2() throws IOException {
        System.out.println("copyFile 2 args");
        try (Reader r = new BufferedReader(new FileReader(fIn)); Writer w = new PrintWriter(new BufferedWriter(new FileWriter(fOut)))) {
            FileIO.copyFile(r, w);
        }
        assertTrue(fOut.exists());
    }

    /**
     * Test of moveFile method, of class FileIO.
     * @throws java.io.IOException
     */
    @Test
    public void testMoveFile() throws IOException {
        System.out.println("moveFile");
        FileIO.moveFile(fOut, fIn);
        assertTrue(fIn.exists());
    }

} // end class file FileIOTest.java