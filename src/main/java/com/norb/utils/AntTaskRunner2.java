/*
 * Program:      Savage Studio
 *
 * Filename:     AntTaskRunner2.java
 *
 * Author(s):    Terry Norbraten
 *               http://www.nps.edu and http://www.movesinstitute.org
 *
 * Created on:   09 MAY 2014
 *
 * Compiler:     JDK1.8
 *
 * Description:  Run external Ant tasks from within source code
 *
 * References:
 *
 * URL:          http://www.<URL>/AntTaskRunner2.java
 *
 * Requirements: 1) JDK 1.6 or greater to compile generics for collections,
 *                  direct invocation of static methods, use of the
 *                  StringBuilder class and usage of enhanced "for" loops
 *
 * Assumptions:  1)
 *
 * TODO:
 *
 * Copyright (c) 1995-2014 held by the author(s).  All rights reserved.
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
import java.io.File;

// Local imports
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/** Run external Ant tasks from within source code.  This is solely for the
 * purpose of being able to run Xj3D on Macs via JDK1.6 until the JOGL issue
 * is finally resolved for Macs.
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.AntTaskRunner2">Terry Norbraten, NPS MOVES</a>
 */
public class AntTaskRunner2 implements Runnable {

    private File buildF;
    private Project project;
    private String taskName;

    public AntTaskRunner2() {

        buildF = new File("build.xml");

        project = new Project();
        project.setUserProperty("ant.file", buildF.getAbsolutePath());

        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);

        project.addBuildListener(consoleLogger);

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        project.addReference("ant.projectHelper", helper);
        helper.parse(project, buildF);

    }

    /**
     * Add a property to the Ant task before we launch
     *
     * @param name the name of the property to set
     * @param value the value of the property to set
     */
    public void setUserProperty(String name, String value) {
        project.setUserProperty(name, value);
    }

    /**
     * Add the ant task name to run
     *
     * @param name the name of the ant task
     */
    public void setTaskname(String name) {
        taskName = name;
    }

    /**
     * Execute the ant task
     */
    @Override
    public void run() {

        try {
            project.fireBuildStarted();
            project.init();
            project.executeTarget(taskName);
            project.fireBuildFinished(null);
        } catch (BuildException e) {
            project.fireBuildFinished(e);
        }

    }


} // end class file AntTaskRunner.java
