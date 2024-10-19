/*
 * Program:      Autonomous Unmanned Vehicle (AUV) Workbench
 *
 * Filename:     AntTaskRunner.java
 *
 * Author(s):    Terry Norbraten
 *               http://www.nps.edu and http://www.movesinstitute.org
 *
 * Created on:   June 19, 2007
 *
 * Compiler:     JDK1.6
 * O/S:          Windows XP Home Ed. (SP2)
 *
 * Description:  Run external Ant tasks from within source code
 *
 * References:
 *
 * URL:          http://www.<URL>/AntTaskRunner.java
 *
 * Requirements: 1) JDK 1.5 or greater to compile generics for collections,
 *                  direct invocation of static methods, use of the
 *                  StringBuilder class and usage of enhanced "for" loops
 *
 * Assumptions:  1)
 *
 * TODO:
 *
 * Copyright (c) 1995-2012 held by the author(s).  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer
 *       in the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the names of the Naval Postgraduate School (NPS)
 *       Modeling Virtual Environments and Simulation (MOVES) Institute
 *       (http://www.nps.edu and http://www.movesinstitute.org)
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific
 *       prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.norb.utils;

// Standard library imports
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.Logger;
//import workbench.main.AUVWorkbenchConfig2;

/** Run external Ant tasks from within source code
 * @version $Id: AntTaskRunner.java 8020 2012-07-03 01:37:06Z brutzman $
 * <p>
 *   <b>History:</b>
 *   <pre><b>
 *     Date:     June 19, 2007
 *     Time:     3:17 PM
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.AntTaskRunner">Terry Norbraten, NPS MOVES</a>
 *     Comments: Initial
 *
 *     Date:     April 10, 2008
 *     Time:     0000Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.AntTaskRunner">Terry Norbraten, NPS MOVES</a>
 *     Comments: Bug fix 1669
 *   </b></pre>
 * </p>
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.AntTaskRunner">Terry Norbraten, NPS MOVES</a>
 */
public class AntTaskRunner implements Runnable {

    private static final Logger LOG = LogUtils.getLogger(AntTaskRunner.class);

    /** Instance of AUVWorkbenchConfig2 */
//    private AUVWorkbenchConfig2 cfg2;

    private Container container;

    private File buildF, anthome, launcher, buildDir;

    private JTextArea statusTextArea;

    private Process process;

    private StringBuilder sb = new StringBuilder();

    private final String SYNCOBJ = "syncObj";

    private Thread sysoutThr, syserrThr;

    private Vector<String> fixedArgsV = new Vector<>(4);
    /** With initial capacity 15 and capacity increment 5 */
    private Vector<String> argsV = new Vector<>(15, 5);

    private boolean isCancelled = false;

    public AntTaskRunner() {

//        cfg2 = AUVWorkbenchConfig2.instance();
//        buildF = new File(cfg2.val("app.path.buildfile"));
//        anthome = new File(cfg2.val("app.path.anthome"));
//        launcher = new File(anthome, "lib/ant-launcher.jar");
//        buildDir = new File(cfg2.val("app.path.build"));
//
//        fixedArgsV.add(SystemUtilities.buildPathToRunningVM());
        fixedArgsV.add("-Dant.home=" + anthome.getAbsolutePath());
        fixedArgsV.add("-Djavax.net.ssl.trustStorePassword=" + System.getProperty("javax.net.ssl.trustStorePassword"));
        fixedArgsV.add("-Djavax.net.ssl.trustStore=" + System.getProperty("javax.net.ssl.trustStore"));
    }

    /** Default constructor
     * @param container the parent Frame of this runner
     * @param sta the JTextArea to output our Ant task results
     */
    public AntTaskRunner(Container container, JTextArea sta) {
        this();
        this.container = container;
        statusTextArea = sta;
    }

    /** Creates a new instance of AntTaskRunner
     * @param args the command line arguments (if any)
     */
    public AntTaskRunner(String[] args) {
        this();
        if (args.length <= 0) {
            throw new IllegalArgumentException("Unrecognized arguments");
        }
    }

    public String[] getUserPassword() {
        String username = JOptionPane.showInputDialog(container,
                "Please Enter User Name", "Auv Workbench Downloads",
                JOptionPane.QUESTION_MESSAGE);

//        String password = PasswordDialog.getPassword(container);

//        String[] contents = new String[] {username, password};

//        if ((contents[0] != null) && (contents[1] != null)) {
//            argsV.add("-Dauvw.username=" + username);
//            argsV.add("-Dauvw.password=" + password);
//        }

//        return contents;
        return null;
    }

  public void buildMainArguments()
  {
    argsV.clear();
    argsV.addAll(fixedArgsV);

    argsV.add("-cp");
    argsV.add(launcher.getAbsolutePath());
    argsV.add("org.apache.tools.ant.launch.Launcher");
    argsV.add("-buildfile");
    argsV.add(buildF.getAbsolutePath());
    argsV.add("-lib");
    argsV.add(buildDir.getAbsolutePath());
    argsV.add("-verbose");
    // enabling these causes the target NOT to be run
//        argsV.add("-diagnostics");
//        argsV.add("-debug");
  }

    /** Add an argument to the basic ant task command line
     * @param arg the name of the ant task
     */
    public void setTaskname(String arg)
    {
      argsV.add(arg);
    }

    /** Cancel task flag
     * @param cnx boolean flag
     */
    public void setIsCancelled(boolean cnx) {
        isCancelled = cnx;
        appendStatus("\nTask canceled.\n", true);
        if (process != null) {process.destroy();}
    }

    /** Execute the ant task */
    @Override
    public void run() {

        String[] commandline = new String[] {};
        commandline = argsV.toArray(commandline);

        Runtime runtime = Runtime.getRuntime();
        process = null;

        try {
            appendStatus("Task begun.\n", true);
            process = runtime.exec(commandline, null, new File(System.getProperty("user.dir")));
            sysoutThr = new Thread(new ReaderThread(new BufferedInputStream(process.getInputStream())), "ant task runner sysout");
            syserrThr = new Thread(new ReaderThread(new BufferedInputStream(process.getErrorStream())), "ant task runner syserr");
            sysoutThr.setPriority(Thread.NORM_PRIORITY);
            syserrThr.setPriority(Thread.NORM_PRIORITY);
            sysoutThr.start();
            syserrThr.start();

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            appendStatus("\nError: " + e.getMessage(), true);
        }

        if (!isCancelled) {
            appendStatus("\nTask complete.\n", true);
        }
    }

    /** Show status output of ant task
     * @param s the string to display in the JTextArea (status)
     * @param flush clear output buffer if true
     */
    public void appendStatus(String s, boolean flush) {
        if (statusTextArea == null) {
//            SystemUtilities.sysOutPrint(s);
            return;
        }
        synchronized (SYNCOBJ) {
            sb.append(s);

            if (flush || sb.length() > 1024) {
                final String st = sb.toString();
                sb.setLength(0);

                SwingUtilities.invokeLater(() -> {
                    statusTextArea.append(st);
                });
            }
        }
    }

    /* INNER CLASS(es) */

    class ReaderThread implements Runnable {
        InputStream inStr;
        private final byte[] buffr;

        ReaderThread(InputStream in) {
            inStr = in;
            buffr = new byte[1024];
        }

        @Override
        public void run() {

            try {
                while (true) {
                    final int len = inStr.read(buffr);
                    if (len > 0) {
                        appendStatus(new String(buffr, 0, len), false);

                    /* windows JVM needs this... it doesn't do the
                     * exception when the process is gone.
                     */
                    } else {break;}
                }
            } catch (IOException ioe) {LOG.error(ioe);}
        }
    }

    /**
     * @param args the command line arguments (if any)
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new AntTaskRunner(args);
    }

} // end class file AntTaskRunner.java
