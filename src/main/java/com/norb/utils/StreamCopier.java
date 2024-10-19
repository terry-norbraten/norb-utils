/* Program:     Extensible Markup Language (XML) Schema-based Binary
 *              Compression (XSBC)
 *
 * Author(s):   Elliotte Rusty Harold, Terry Norbraten
 *
 * Created on:  Unknown, 1999
 * Revised:     June 23, 2005
 *
 * File:        StreamCopier.java
 *
 * Compiler:    JDK1.6
 * O/S:         Windows XP Home Ed. (SP2)
 *
 * Description: Class that copies data between two streams as quickly as
 *              possible.  The copy method reads from the input stream and
 *              writes onto the output stream until the input stream is
 *              exhausted.
 *
 * Information: Borrowed from Java I/O by Elliotte Rusty Harold, Copyright 1999
 *              O'Reilly & Associates, Inc.
 */
package com.norb.utils;

// Standard library imports
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

// Application specific local imports

/**
 * Class that copies data between two streams as quickly as possible.  The copy
 * method reads from the input stream and writes onto the output stream until
 * the input stream is exhausted.
 * @version $Id: StreamCopier.java,v 1.4 2008/05/03 19:06:37 tnorbraten Exp $
 *<p>
 *   <b>History:</b>
 *   <pre><b>
 *     Date:     June 23, 2005
 *     Time:     1055:27
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.StreamCopier">Terry D. Norbraten</a>
 *     Comments: Initial
 *   </b></pre>
 *</p>
 *
 * @author Elliotte Rusty Harold
 */
public class StreamCopier {

   /** A 1024 byte buffer is used to try to make the reads efficient
     * @param in an InputStream to read from
     * @param out an OutputStream to write to
     * @throws IOException
     */
   public static void copy(InputStream in, OutputStream out) throws IOException {

      /* Do not allow other threads to read from the input or write to the
       * output while copying is taking place.
       */
       synchronized(in) {
         synchronized(out) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
               out.write(buffer, 0, bytesRead);
            }
         }
      }
   }

} // end class file StreamCopier.java