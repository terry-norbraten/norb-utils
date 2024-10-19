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

import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.Length;
import com.bbn.openmap.proj.coords.DMSLatLonPoint;
import com.bbn.openmap.proj.coords.MGRSPoint;
import com.bbn.openmap.proj.coords.UTMPoint;

/**
 * <p>Utility Class to convert WGS84 Geodetic coordinates to a Universal
 * Transverse Mercator (UTM) Coordinate System, i.e. X3D local coordinates to a
 * representation in Geodetic Decimal Degrees (DD) referenced to the WGS 84 datum</p>
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.GeodeticToUTMConverter">Terry Norbraten, NPS MOVES Institute</a>
 * @version $Id: GeodeticToUTMConverter.java 441 2012-08-30 17:38:06Z tdnorbra $
 */
public class GeodeticToUTMConverter {

    /** UTM Easting/Northing (x, y) coordinate pair */
    private UTMPoint utm;

    /** OpenMap version of a lat/long coordinate pair */
    private LatLonPoint llp;

    /** Reusable scratch float[] */
    private float[] latLon;

    /** Default constructor */
    public GeodeticToUTMConverter() {
        utm = new UTMPoint();
        llp = new LatLonPoint();
        latLon = new float[2];
    }

    /** Constructor initializing with a LatLongPoint
     *
     * @param llp the LatLongPoint to initialize with
     */
    public GeodeticToUTMConverter(LatLonPoint llp) {
        this();
        setUTM(llp);
    }

    /** Constructor initializing with raw latitude and longitude
     *
     * @param lat the latitude to initialize with
     * @param lon the longitude to initialize with
     */
    public GeodeticToUTMConverter(float lat, float lon) {
        this();
        setUTM(lat, lon);
    }

    /**
     * Sets the WGS84 GeoCenter coordinates and transforms them to UTM coordinates
     * @param lat LATITUDE
     * @param lon LONGITUDE
     */
    public final void setUTM(float lat, float lon) {
        setUTM(new LatLonPoint(lat, lon));
    }

    /**
     * Sets the WGS84 GeoCenter coordinates and transforms them to UTM coordinates
     * @param llp a LatLonPoint
     */
    public final void setUTM(LatLonPoint llp) {
        this.llp = llp;
        setUTM(new UTMPoint(llp));
    }

    /**
     * @return the utm
     */
    public UTMPoint getUTM() {
        return utm;
    }

    /**
     * @param utmPoint the utm to set
     */
    public void setUTM(UTMPoint utmPoint) {
        this.utm = utmPoint;
    }

    /**
     * <p>Add an offset to the GeoCenter which takes into account negative
     * values.  The x axis is easting. The y axis is northing. </p>
     *
     * @param x the easting in meters to add as an offset from the GeoCenter
     * @param y the northing in meters to add as an offset from the GeoCenter
     */
    public void addOffsetToUTM(float x, float y) {
        utm.easting += x;
        utm.northing += y;
    }

    /**
     * <p>Add an offset to the GeoCenter which takes into account negative
     * values.  The X axis is easting. The Y axis is northing. </p>
     *
     * @param offset the (easting, northing) utm to add as an offset from the GeoCenter
     */
    public void addOffsetToUTM(float[] offset) {
        addOffsetToUTM(offset[0], offset[1]);
    }

    /**
     * Returns an array offset from the GeoCenter in lat/long order
     * @return an array offset from the GeoCenter in lat/long order
     */
    public final float[] getArrayOffsetFromUTM() {

        LatLonPoint tempLLP = getLatLonPointOffsetFromUTM();

        // Set the new (lat, lon) point
        latLon[0] = tempLLP.getLatitude();
        latLon[1] = tempLLP.getLongitude();
        return latLon;
    }

    /**
     * Returns a LatLonPoint offset from the GeoCenter
     * @return a LatLonPoint offset from the GeoCenter
     */
    public final LatLonPoint getLatLonPointOffsetFromUTM() {

        LatLonPoint tempLLP = getUTM().toLatLonPoint();

        // Preserve the original GeoCenter here
        setUTM(new UTMPoint(llp));

        return tempLLP;
    }

    /**
     * Command line entry point for this class
     * @param args command line arguments (if any)
     */
    public static void main(String[] args) {

        if (args.length < 8) {
            System.err.println("Usage: latitude negative?true:false, "
                    + "degrees (int), minutes (int), seconds (float), "
                    + "longitude negative?true:false, degrees (int), "
                    + "minutes (int), seconds (float),");
            System.exit(-1);
        }

        float latSec, lonSec;
        int latMin, lonMin;
        LatLonPoint llp;
        DMSLatLonPoint dms;

        // We have only DM format (int, float)
        if (Float.parseFloat(args[3]) == 0.0f && Float.parseFloat(args[7]) == 0.0f) {

            latSec = Float.parseFloat(args[2]);

            // Isolate the min, sec pair
            latMin = (int) latSec;
            latSec -= latMin;
            latSec *= 60f;

            lonSec = Float.parseFloat(args[6]);

            // Isolate the min, sec pair
            lonMin = (int) lonSec;
            lonSec -= lonMin;
            lonSec *= 60f;

            dms = new DMSLatLonPoint(Boolean.parseBoolean(args[0]),
                Integer.parseInt(args[1]),
                latMin,
                latSec,
                Boolean.parseBoolean(args[4]),
                Integer.parseInt(args[5]),
                lonMin,
                lonSec);

        // We have DMS format (int, int, float)
        } else {

            dms = new DMSLatLonPoint(Boolean.parseBoolean(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Float.parseFloat(args[3]),
                Boolean.parseBoolean(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6]),
                Float.parseFloat(args[7]));
        }

        llp = dms.getLatLonPoint();
        GeodeticToUTMConverter g2utm = new GeodeticToUTMConverter(llp);

        final float LATITUDE = llp.getLatitude();
        final float LONGITUDE = llp.getLongitude();

        // 1 meter = 0.000621371192 miles

        float pointSWX = -2500;
        float pointSWY = -15f;

        float pointNWX = -2500f;
        float pointNWY = 45f;

        float pointNEX = 2500f;
        float pointNEY = 45f;

        float pointSEX = 2500f;
        float pointSEY = -15f;

        System.out.println("Central LAT/LONG is: (" + LATITUDE + ", " + LONGITUDE + ")");

//        g2utm.addOffsetToUTM(pointSWX, pointSWY);
//        float[] result = g2utm.getArrayOffsetFromUTM();
//        System.out.println("SW Offset from geoCenter: (" +
//                result[0] + ", " + result[1] + ")");
//
//        LatLonPoint swCorner = new LatLonPoint(result[0], result[1]);
//
//        g2utm.addOffsetToUTM(pointNWX, pointNWY);
//        result = g2utm.getArrayOffsetFromUTM();
//        System.out.println("NW Offset from geoCenter: (" +
//                result[0] + ", " + result[1] + ")");
//
//        g2utm.addOffsetToUTM(pointNEX, pointNEY);
//        result = g2utm.getArrayOffsetFromUTM();
//        System.out.println("NE Offset from geoCenter: (" +
//                result[0] + ", " + result[1] + ")");
//
//        g2utm.addOffsetToUTM(pointSEX, pointSEY);
//        result = g2utm.getArrayOffsetFromUTM();
//        System.out.println("SE Offset from geoCenter: (" +
//                result[0] + ", " + result[1] + ")");
//
//        LatLonPoint seCorner = new LatLonPoint(result[0], result[1]);

        Length mile = Length.MILE;
//        float radDist = swCorner.distance(seCorner);
//        float mileDist = mile.fromRadians(radDist);
//        System.out.println("Distance from swCorner of BB to seCorner is: " + mileDist + " miles");

        Length meter = Length.METER;
//        radDist = mile.toRadians(mileDist);
//        float meterDist = meter.fromRadians(radDist);
//        System.out.println(mileDist + " miles equates to " + meterDist + " meters");

        System.out.println("MGRS equivilant is: " + MGRSPoint.LLtoMGRS(llp));
    }

} // end class file GeodeticToUTMConverter.java