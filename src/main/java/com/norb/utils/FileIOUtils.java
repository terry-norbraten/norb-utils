/*
Copyright (c) 1995-2010 held by the author(s).  All rights reserved.

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

import edu.nps.moves.host.UI.GraphView;
import java.io.*;
import java.util.StringTokenizer;

/** Utility class to handle ONR Marksmanship Trainer File I/O for data capture
 * initiated from our GraphView UI
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.FileIOUtils">Terry Norbraten, NPS MOVES</a>
 * @version $Id: FileIOUtils.java 340 2012-02-17 23:30:40Z tdnorbra $
 */
public class FileIOUtils {

    /** Instance of our GraphView class */
    private GraphView graphView;

    /** Our logfile writer instance */
    private Writer logFile;

    /** Our efficient String concatenator */
    private StringBuilder sb;

    /** Comma literal */
    private final static String COMMA = ",";

    /** Comma and newline literal concatenation */
    private final static String COMMA_NEWLINE = COMMA + "\n";

    /** Our accelerometer file header */
    private final static String ACCEL_HEADER = "mac,deltaT,index,x,y,z,total,2G,ca,el,tiltZ,offsetX,offsetY,offsetZ,sampleFreq" + COMMA_NEWLINE;

    /** Our adc file header */
    private final static String ADC_HEADER = "mac,deltaT,index,bs,tp,trr,trl,sampleFreq" + COMMA_NEWLINE;

    /** Our temp file header */
    private final static String TEMP_HEADER = "mac,deltaT,index,tempCelsius" + COMMA_NEWLINE;

    /** Creates a new instance of FileIOUtils
     * @param gw an instance of our GraphView
     */
    public FileIOUtils(GraphView gw) {
        setGraphView(gw);
    }

    /* Routines to read & write telemetry data to a file */

    /** Write out only a single trigger event's Accel data
     *
     * @param file the csv file to write to
     * @param indexAccelBeginEvent beginning index of the event
     */
    public void writeAccelerometerEvent(File file, int indexAccelBeginEvent) {
        sb = new StringBuilder();

        try {
            logFile = new BufferedWriter(new FileWriter(file));

            // write a column label header
            logFile.write(ACCEL_HEADER);
            logFile.flush();

            // save the calibrated rest offsets in first sample written
            sb.append(graphView.getId());
            sb.append(COMMA);
            sb.append(graphView.getTimeMSAccelerometer()[indexAccelBeginEvent]);
            sb.append(COMMA);
            sb.append(indexAccelBeginEvent);
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelX()[indexAccelBeginEvent]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelY()[indexAccelBeginEvent]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelZ()[indexAccelBeginEvent]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelTotalG()[indexAccelBeginEvent]));
            sb.append(COMMA);
            sb.append(graphView.getScale());
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltX()[indexAccelBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltY()[indexAccelBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltZ()[indexAccelBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetX()));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetY()));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetZ()));
            sb.append(COMMA);
            sb.append(GraphView.MILLISECONDS_PER_SECOND/graphView.SAMPLE_PERIOD_IN_MS_ACCELEROMETER);
            sb.append(COMMA_NEWLINE);
            logFile.write(sb.toString());
            logFile.flush();

            sb.setLength(0);

            for (int i = (indexAccelBeginEvent + 1); i <= graphView.getIndexMaxAccelerometer(); i++) {
                sb.append(graphView.getId());
                sb.append(COMMA);
                sb.append(graphView.getTimeMSAccelerometer()[i]);
                sb.append(COMMA);
                sb.append(i);
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelX()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelY()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelZ()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelTotalG()[i]));
                sb.append(COMMA);
                sb.append(graphView.getScale());
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltX()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltY()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltZ()[i]);
                sb.append(COMMA_NEWLINE);
                logFile.write(sb.toString());
                logFile.flush();

                sb.setLength(0);
            }
        } catch (IOException ex) {
            System.err.println("Error writing out file: " + ex);
        } finally {
            try {
                logFile.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    /** Write out only a single trigger event's ADC data
     *
     * @param file the csv file to write to
     * @param indexADCBeginEvent beginning index of the event
     */
    public void writeADCEvent(File file, int indexADCBeginEvent) {
        sb = new StringBuilder();

        try {
            logFile = new BufferedWriter(new FileWriter(file));

            // write a column label header
            logFile.write(ADC_HEADER);
            logFile.flush();

            sb.append(graphView.getId());
            sb.append(COMMA);
            sb.append(graphView.getTimeMSADC()[indexADCBeginEvent]);
            sb.append(COMMA);
            sb.append(indexADCBeginEvent);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCbS()[indexADCBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtP()[indexADCBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtRR()[indexADCBeginEvent]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtRL()[indexADCBeginEvent]);
            sb.append(COMMA);
            sb.append(GraphView.MILLISECONDS_PER_SECOND/graphView.SAMPLE_PERIOD_IN_MS_ADC);
            sb.append(COMMA_NEWLINE);
            logFile.write(sb.toString());
            logFile.flush();

            sb.setLength(0);

            for (int i = (indexADCBeginEvent + 1); i <= graphView.getIndexMaxADC(); i++) {

                sb.append(graphView.getId());
                sb.append(COMMA);
                sb.append(graphView.getTimeMSADC()[i]);
                sb.append(COMMA);
                sb.append(i);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCbS()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtP()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtRR()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtRL()[i]);
                sb.append(COMMA_NEWLINE);
                logFile.write(sb.toString());
                logFile.flush();

                sb.setLength(0);
            }
        } catch (IOException ex) {
            System.err.println("Error writing out file: " + ex);
        } finally {
            try {
                logFile.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
    }

    /**
     * Write whole acceleration telemetry data capture out to a file.
     *
     * @param file the file to write the data into
     * @return true if successful, false otherwise
     */
    public boolean writeAccelerometerData(File file) {
        sb = new StringBuilder();
        boolean results = false;

        try {
            logFile = new BufferedWriter(new FileWriter(file));

            // write a column label header
            logFile.write(ACCEL_HEADER);
            logFile.flush();

            // save the calibrated rest offsets in first sample written
            sb.append(graphView.getId());
            sb.append(COMMA);
            sb.append(graphView.getTimeMSAccelerometer()[0]);
            sb.append(COMMA);
            sb.append(0);
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelX()[0]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelY()[0]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelZ()[0]));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getyDataAccelTotalG()[0]));
            sb.append(COMMA);
            sb.append(graphView.getScale());
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltX()[0]);
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltY()[0]);
            sb.append(COMMA);
            sb.append(graphView.getyDataTiltZ()[0]);
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetX()));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetY()));
            sb.append(COMMA);
            sb.append(graphView.getFormatter().format(graphView.getgOffsetZ()));
            sb.append(COMMA);
            sb.append(GraphView.MILLISECONDS_PER_SECOND/graphView.SAMPLE_PERIOD_IN_MS_ACCELEROMETER);
            sb.append(COMMA_NEWLINE);
            logFile.write(sb.toString());
            logFile.flush();

            sb.setLength(0);

            for (int i = 1; i <= graphView.getIndexMaxAccelerometer(); i++) {

                sb.append(graphView.getId());
                sb.append(COMMA);
                sb.append(graphView.getTimeMSAccelerometer()[i]);
                sb.append(COMMA);
                sb.append(i);
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelX()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelY()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelZ()[i]));
                sb.append(COMMA);
                sb.append(graphView.getFormatter().format(graphView.getyDataAccelTotalG()[i]));
                sb.append(COMMA);
                sb.append(graphView.getScale());
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltX()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltY()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataTiltZ()[i]);
                sb.append(COMMA_NEWLINE);
                logFile.write(sb.toString());
                logFile.flush();

                sb.setLength(0);
            }
            results = true;
        } catch (IOException ex) {
            System.err.println("Error writing out file: " + ex);
        } finally {
            try {
                logFile.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
        return results;
    }

    /**
     * Write whole ADC pin value data capture out to a file.
     *
     * @param file the file to write the data into
     * @return true if successful, false otherwise
     */
    public boolean writeADCData(File file) {
        sb = new StringBuilder();
        boolean results = false;

        try {
            logFile = new BufferedWriter(new FileWriter(file));

            // write a column label header
            logFile.write(ADC_HEADER);
            logFile.flush();

            sb.append(graphView.getId());
            sb.append(COMMA);
            sb.append(graphView.getTimeMSADC()[0]);
            sb.append(COMMA);
            sb.append(0);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCbS()[0]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtP()[0]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtRR()[0]);
            sb.append(COMMA);
            sb.append(graphView.getyDataADCtRL()[0]);
            sb.append(COMMA);
            sb.append(GraphView.MILLISECONDS_PER_SECOND/graphView.SAMPLE_PERIOD_IN_MS_ADC);
            sb.append(COMMA_NEWLINE);
            logFile.write(sb.toString());
            logFile.flush();

            sb.setLength(0);

            for (int i = 1; i <= graphView.getIndexMaxADC(); i++) {

                sb.append(graphView.getId());
                sb.append(COMMA);
                sb.append(graphView.getTimeMSADC()[i]);
                sb.append(COMMA);
                sb.append(i);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCbS()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtP()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtRR()[i]);
                sb.append(COMMA);
                sb.append(graphView.getyDataADCtRL()[i]);
                sb.append(COMMA_NEWLINE);
                logFile.write(sb.toString());
                logFile.flush();

                sb.setLength(0);
            }
            results = true;
        } catch (IOException ex) {
            System.err.println("Error writing out file: " + ex);
        } finally {
            try {
                logFile.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
        return results;
    }

    /**
     * Write whole temperature data capture out to a file.
     *
     * @param file the file to write the data into
     * @return true if successful, false otherwise
     */
    public boolean writeTemperatureData(File file) {
        sb = new StringBuilder();
        boolean results = false;

        try {
            logFile = new BufferedWriter(new FileWriter(file));

            // write a column label header
            logFile.write(TEMP_HEADER);
            logFile.flush();

            sb.append(graphView.getId());
            sb.append(COMMA);
            sb.append(graphView.getTimeMSTemperature()[0]);
            sb.append(COMMA);
            sb.append(0);
            sb.append(COMMA);
            sb.append(graphView.getyDataTemperature()[0]);
            sb.append(COMMA_NEWLINE);
            logFile.write(sb.toString());
            logFile.flush();

            sb.setLength(0);

            for (int i = 1; i <= graphView.getIndexMaxTemperature(); i++) {

                sb.append(graphView.getId());
                sb.append(COMMA);
                sb.append(graphView.getTimeMSTemperature()[i]);
                sb.append(COMMA);
                sb.append(i);
                sb.append(COMMA);
                sb.append(graphView.getyDataTemperature()[i]);
                sb.append(COMMA_NEWLINE);
                logFile.write(sb.toString());
                logFile.flush();

                sb.setLength(0);
            }
            results = true;
        } catch (IOException ex) {
            System.err.println("Error writing out file: " + ex);
        } finally {
            try {
                logFile.close();
            } catch (IOException ex) {
                // do nothing
            }
        }
        return results;
    }

    /**
     * Read in acceleration telemetry data from a file.
     *
     * @param file the file to read the data from
     * @return true if successful, false otherwise
     */
    public boolean readAccelerometerFile(File file) {
        boolean results = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            graphView.setFileAccelTelemetryData(true);
            String str;
            boolean firstLine = true;
            while ((str = in.readLine()) != null) {

                // Skip over the label header
                if (firstLine) {
                    firstLine = !firstLine;
                    continue;
                }

                StringTokenizer stk = new StringTokenizer(str, ";,");
                String address = stk.nextToken();
                long localTimeMS  = Long.parseLong(stk.nextToken());
                int index = Integer.parseInt(stk.nextToken());
                double x  = Double.parseDouble(stk.nextToken());
                double y  = Double.parseDouble(stk.nextToken());
                double z  = Double.parseDouble(stk.nextToken());
                double g  = Double.parseDouble(stk.nextToken());
                int scale = Integer.parseInt(stk.nextToken());
                int xTilt = Integer.parseInt(stk.nextToken());
                int yTilt = Integer.parseInt(stk.nextToken());
                int zTilt = Integer.parseInt(stk.nextToken());

                // Supply offset readings from the first line in CSV file
                if (index == 0 && stk.hasMoreTokens()) {
                    double gx = Double.parseDouble(stk.nextToken());
                    double gy = Double.parseDouble(stk.nextToken());
                    double gz = Double.parseDouble(stk.nextToken());

                    // account for one time sampling frequency data on first line
                    stk.nextToken();

                    // taken only from the first line of recorded accel data
                    graphView.setGOffsets(gx, gy, gz);
                }
                graphView.takeAccelerometerData(address, localTimeMS, index, x, y, z, g,
                        scale, xTilt, yTilt, zTilt);
            }
            results = true;

            graphView.setDisplaySize();
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file: " + file.getPath());
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + file.getName() + " : " + e);
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                // do nothing
            }
        }
        graphView.repaint();
        return results;
    }

    /**
     * Read in ADC value data from a file.
     *
     * @param file the file to read the data from
     * @return true if successful, false otherwise
     */
    public boolean readADCFile(File file) {

        boolean results = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            graphView.setFileADCValuesData(true);
            String str;
            boolean firstLine = true;
            while ((str = in.readLine()) != null) {

                // Skip over the label header
                if (firstLine) {
                    firstLine = !firstLine;
                    continue;
                }

                StringTokenizer stk = new StringTokenizer(str, ";,");
                String address = stk.nextToken();
                long localTimeMS  = Long.parseLong(stk.nextToken());
                int index = Integer.parseInt(stk.nextToken());
                int pin0  = Integer.parseInt(stk.nextToken());
                int pin1  = Integer.parseInt(stk.nextToken());
                int pin2  = Integer.parseInt(stk.nextToken());
                int pin3  = Integer.parseInt(stk.nextToken());

                // account for one time sampling frequency value on first data line
                if (index == 0 && stk.hasMoreTokens()) {
                    stk.nextToken();
                }

                // Could be ADC Pin 0, 1, 2 or 3 value
                graphView.takeADCData(address, localTimeMS, index, pin0, pin1, pin2, pin3);
            }
            results = true;

            graphView.setDisplaySize();
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file: " + file.getPath());
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + file.getName() + " : " + e);
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                // do nothing
            }
        }
        graphView.repaint();
        return results;
    }

    /**
     * @param graphView the graphView to set
     */
    public final void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }

} // end class file FileIOUtils.java
