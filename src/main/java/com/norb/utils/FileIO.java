/*
Copyright (c) 1995-2012 held by the author(s).  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer
      in the documentation and/or other materials provided with the
      distribution.
    * Neither the names of the Naval Postgraduate School (NPS)
      Modeling Virtual Environments and Simulation (MOVES) Institute
      (http://www.nps.edu and http://www.MovesInstitute.org)
      nor the names of its contributors may be used to endorse or
      promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package com.norb.utils;

import java.io.*;

/** <p>Taken from Java Cookbook, by Ian F. Darwin
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * www.nps.edu</p>
 *
 * @author Mike Bailey
 * @since Mar 15, 2006 1:10:37 PM
 * @version $Id: FileIO.java 502 2013-01-08 23:11:09Z tdnorbra $
 */
public class FileIO {

    /**
     * Copies a file from one location to another directory
     * @param infile file to copy
     * @param outfile file name/path to copy to
     * @param close flag to indicate closing the streams
     * @throws java.io.IOException if something goes wrong
     */
    public static void copyFile(File infile, File outfile, boolean close) throws IOException {
        if (close) {
            try (Reader r = new BufferedReader(new FileReader(infile)); Writer w = new PrintWriter(new BufferedWriter(new FileWriter(outfile)))) {
                copyFile(r, w);
            }
        } else
            copyFile(new BufferedReader(new FileReader(infile)), new PrintWriter(new BufferedWriter(new FileWriter(outfile))));
    }

    /**
     * Copies a file from one location to another directory
     * @param is the input stream reader of the inFile
     * @param os the output stream that will write to the copied file
     * @throws java.io.IOException if something goes wrong
     */
    public static void copyFile(final Reader is, final Writer os) throws IOException {
        try (is) {
            synchronized (is) {
                try (os) {
                    synchronized (os) {
                        String msg;
                        while ((msg = ((BufferedReader) is).readLine()) != null) {
                            os.write(msg + "\n");
                        }
                    }
                }
            }
        }
    }

    /**
     * Move a file from one location (directory) to another
     * @param from the file to move
     * @param to the location (path/name) to move to
     * @throws IOException if something goes wrong
     */
    public static void moveFile(File from, File to) throws IOException {
        copyFile(from, to, true);
        from.delete();
    }

} // end class file FileIO.java
