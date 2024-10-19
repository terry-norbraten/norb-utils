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
package com.norb.xml;

import com.norb.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.Logger;

/**
 * Transformation utility
 * @author dtdavis
 * @since March 11, 2004, 4:55 PM
 * @version $Id: XsltUtility.java 1180 2012-02-24 01:35:08Z tdnorbra $
 */
public class XsltUtility implements URIResolver {

    private static final Logger LOG = LogUtils.getLogger(XsltUtility.class);

    static final String USAGE_MESSAGE = "Please provide paths to input file, " +
            "desired output filen and xslt file in this order";

    /** No outside instantiation */
    private XsltUtility() {}

    /**
     * Internal resolver utility
     * @param href the first arg of document() xsl function
     * @param base the base URI (node-set) of the xsl document() function
     * @return a Source to the relative file name in the xsl document() function
     * @throws javax.xml.transform.TransformerException
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        String baseRef = base.substring(0, base.lastIndexOf("/"));
        String resolvedRef = baseRef.replaceAll("\\\\", "/") + "/" + href;
        resolvedRef = resolvedRef.replaceAll("file:/", "");
        return new StreamSource(resolvedRef);
    }

    /**
     * Runs an XSL Transformation on an XML file and writes the result to another file
     *
     * @param inputFile the XML file to be transformed
     * @param outputFile the output file for transformation results
     * @param xsltFile the XSLT stylesheet to utilize for transformation
     *
     * @return the resulting transformed XML file
     */
    public static boolean runXslt(File inputFile, File outputFile, File xsltFile)
    {
         return runXslt(inputFile, outputFile, xsltFile, null);
    }

    /**
     * Runs an XSL Transformation on an XML file and writes the result to another file
     *
     * @param inputFile the XML file to be transformed
     * @param outputFile the output file for transformation results
     * @param xsltFile the XSLT stylesheet to utilize for transformation
     * @param parameterList array of String-array pairs holding stylesheet-input parameter name, value
     *
     * @return status flag resulting from a successfully transformed XML file
     */
    public static boolean runXslt(File inputFile, File outputFile, File xsltFile, String[][] parameterList)
    {
        boolean success = false;
        try {
            Transformer xFormer = getXslTransformer(xsltFile);
            xFormer.setURIResolver(new XsltUtility());

            if (parameterList != null)
            {
                for (int i=0; i < parameterList[0].length; i++)
                {
                    if ((parameterList[i][0] != null) && (parameterList[i][1] != null))
                            xFormer.setParameter(parameterList[i][0], parameterList[i][1]);
                }
            }

            Source source = new StreamSource(inputFile.toURI().toURL().toExternalForm());
            Result result = new StreamResult(new FileOutputStream(outputFile));
            xFormer.transform(source, result);
            success = true;

        } catch (MalformedURLException | FileNotFoundException | TransformerException ex) {
            LOG.error(ex.getLocalizedMessage());
        }
        return success;
    }

    /** Obtain a Transformer from the Stylesheet cacher
     * @param xslFile the stylesheet of interest
     * @return a transformer based on a specific stylesheet
     */
    private static Transformer getXslTransformer(File xslFile) {

        // Force Xalan for this TransformerFactory
        System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");

        Transformer t = null;
        try {
            t = StylesheetCache.newTransformer(xslFile);

            // See if we can indent the output results
            t.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
        } catch (javax.xml.transform.TransformerConfigurationException tce) {LOG.error(tce.getLocalizedMessage());}
        return t;
    }

    /** Command Line entry point for this class
     * @param args the command line arguments (if any)
     */
    public static void main(String[] args) {
        if (args == null || args.length < 3) {
            LOG.warn(USAGE_MESSAGE);
            return;
        }
        for (String arg : args) {
           LOG.info(arg);
        }
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        File xsltFile = new File(args[2]);
        XsltUtility.runXslt(inFile, outFile, xsltFile);
    }

} // end class file XsltUtility.java