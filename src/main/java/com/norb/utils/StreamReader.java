/*
Copyright (c) 1995-2011 held by the author(s).  All rights reserved.

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
      (http://www.nps.edu and http://www.movesinstitute.org)
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.logging.log4j.Logger;

/** Primarily used to redirect streams spawned by remote systems
 * so that output and error messages can be monitored
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.StreamReader">Terry Norbraten, NPS MOVES</a>
 * @version $Id: StreamReader.java 520 2013-05-06 23:31:16Z tdnorbra $
 */
public class StreamReader implements Runnable {

    private static final Logger LOG = LogUtils.getLogger(StreamReader.class);

    private final InputStream inStr;
    private final boolean error;

    /** Creates a new instance of StreamReader
     *
     * @param in a InputStream to read from
     * @param err flag to denote error stream
     */
    public StreamReader(InputStream in, boolean err) {
        inStr = in;
        error = err;
    }

    @Override
    public void run() {
        String msg;

        try (Reader isr = new InputStreamReader(inStr); Reader br = new BufferedReader(isr)) {
            while ((msg = ((BufferedReader) br).readLine()) != null) {
                if (!error) {
                    System.out.println(msg);
                } else {
                    System.err.println(msg);
                }
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }

} // end class file StreamReader.java
