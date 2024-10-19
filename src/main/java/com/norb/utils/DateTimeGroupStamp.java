/*
 * Program:      Extensible Markup Language (XML) Schema-based Binary
 *               Compression (XSBC)
 *
 * Filename:     DateTimeGroupStamp.java
 *
 * Author(s):    Terry Norbraten
 *               http://www.nps.edu and http://www.movesinstitute.org
 *
 * Created on:   Created on April 29, 2007
 *
 * Compiler:     netBeans IDE 5.5 (External), JDK 6.0
 * O/S:          Windows XP Home Ed. (SP2)
 *
 * Description:  Class to produce military style Date Time Group (DTG) stamp
 *
 * References:
 *
 * URL:          http://www<URL>/DateTimeGroupStamp.java
 *
 * Requirements: 1) JDK 1.5 or greater to compile generics for collections,
 *                  direct invocation of static methods, use of the
 *                  StringBuilder class and usage of enhanced "for" loops
 *
 * Assumptions:  1)
 *
 * TODO:
 *
 * Copyright (c) 1995-2008 held by the author(s).  All rights reserved.
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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

// Application specific local imports
import org.apache.logging.log4j.Logger;

/**
 * Class to produce military style Date Time Group (DTG) stamp
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=org.web3d.xmsf.xsbc.util.DateTimeGroupStamp">Terry Norbraten, NPS MOVES</a>
 * @version $Id: DateTimeGroupStamp.java,v 1.4 2008/06/04 17:58:50 tnorbraten Exp $
 * <p>
 *   <b>History:</b>
 *   <pre><b>
 *     Date:     April 29, 2007
 *     Time:     4:20 PM
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.DateTimeGroupStamp">Terry Norbraten, NPS MOVES</a>
 *     Comments: Initial
 *
 *     Date:     15 MAY 2007
 *     Time:     0929
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.DateTimeGroupStamp">Terry Norbraten, NPS MOVES</a>
 *     Comments: Refactored to produce new DTGs upon each instantiation
 *
 *     Date:     26 JUN 2007
 *     Time:     2326Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.DateTimeGroupStamp">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Added one time static method to return current DTG
 *
 *     Date:     23 OCT 2009
 *     Time:     1626Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.DateTimeGroupStamp">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Added getter for seconds
 *   </b></pre>
 * </p>
 */
public class DateTimeGroupStamp {

    static final Logger LOG = LogUtils.getLogger(DateTimeGroupStamp.class);

    private final String BASEDIR = ".";
    private final String PATHSEP = "/";

    /** Name of the data file folder here */
    private final String DATA_FILE_FOLDER = "";
    private final String DATA_FILE_PATH = BASEDIR + PATHSEP + DATA_FILE_FOLDER + PATHSEP;

    /** Calendar instance to craft our DTG */
    private final Calendar calendar = Calendar.getInstance();

    /** Creates a new instance of DateTimeGroupStamp which immediately sets Zulu
     * time
     */
    public DateTimeGroupStamp() {
        setZulu();
    }

    /** @return a Calendar instance */
    public Calendar getCalendar() {return calendar;}

    /** @return the zone offset for the zone this system is reporting */
    public long getZoneOffset() {return getCalendar().get(Calendar.ZONE_OFFSET);}

    /** @return the current date */
    public int getDate() {return getCalendar().get(Calendar.DATE);}

    /** @return the hour of the day in 24 hour frame */
    public int getHourOfDay() {return getCalendar().get(Calendar.HOUR_OF_DAY);}

    /** @return minute of the hour */
    public int getMinute() {return getCalendar().get(Calendar.MINUTE);}

    /** @return seconds of the minute */
    public int getSeconds() {return getCalendar().get(Calendar.SECOND);}

    /** @return the current month */
    public int getMonth() {return getCalendar().get(Calendar.MONTH);}

    /** @return converted Calendar.MONTH_NAME into an abbreviation */
    public String getMonthAbreviation() {
        String ma = "";
        switch(getMonth()) {
            case(Calendar.JANUARY):
                ma = "JAN";
                break;

            case(Calendar.FEBRUARY):
                ma = "FEB";
                break;

            case(Calendar.MARCH):
                ma = "MAR";
                break;

            case(Calendar.APRIL):
                ma = "APR";
                break;

            case(Calendar.MAY):
                ma = "MAY";
                break;

            case(Calendar.JUNE):
                ma = "JUN";
                break;

            case(Calendar.JULY):
                ma = "JUL";
                break;

            case(Calendar.AUGUST):
                ma = "AUG";
                break;

            case(Calendar.SEPTEMBER):
                ma = "SEP";
                break;

            case(Calendar.OCTOBER):
                ma = "OCT";
                break;

            case(Calendar.NOVEMBER):
                ma = "NOV";
                break;

            case(Calendar.DECEMBER):
                ma = "DEC";
                break;
        }
        return ma;
    }

    /** @return the current year */
    public int getYear() {return getCalendar().get(Calendar.YEAR);}

    /** Append a "0" in front of hours and minutes that are less than 10
     * @return the full Date Time Group stamp
     */
    public String getDateTimeGroup() {
        return String.valueOf((getDate() < 10) ? "0" + getDate() : getDate()) +
                ((getHourOfDay() < 10) ? "0" + getHourOfDay() : getHourOfDay()) +
                ((getMinute() < 10) ? "0" + getMinute() : getMinute()) + "Z" +
                getMonthAbreviation() + getYear();
    }

    /** Set Calendar instance for Zulu time.  If we are west of the prime
     * meridian, then this will add to get Zulu
     */
    private void setZulu() {
        getCalendar().setTimeInMillis(getCalendar().getTimeInMillis() - getZoneOffset());
    }

    /** Intended for a one time static return of a current DTG
     * @return the current DTG in String form
     */
    public static String getCurrentDateTimeGroup() {
        DateTimeGroupStamp dtg = new DateTimeGroupStamp();
        return dtg.getDateTimeGroup();
    }

    /** @param args Command Line arguments (if any) */
    public static void main(String[] args) {
        DateTimeGroupStamp dtgs = new DateTimeGroupStamp();
        LOG.debug("Zone offset in hours: " + Math.round(dtgs.getZoneOffset() * (2.77777778 * Math.pow(10, -7))));
        LOG.debug("Zone offset in miliseconds: " + dtgs.getZoneOffset());
        LOG.info("Current DTG: " + dtgs.getDateTimeGroup());

        PrintWriter out;
        try {

            // the destination of this file should be the base directory
            out = new PrintWriter(new File("buildStamp.txt"));
            out.write("Current " + DateTimeGroupStamp.class.getName() + " build is: " + DateTimeGroupStamp.getCurrentDateTimeGroup());
            out.close();
        } catch (FileNotFoundException ex) {LOG.fatal(ex);}

    }

} // end class file DateTimeGroupStamp.java