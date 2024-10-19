package com.norb.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;

/**
 * A utility class that caches XSLT stylesheets in memory and returns
 * transformers for each cached stylesheet.
 *
 * From XSLT Processing with Java, Related Reading, Java and XSLT By Eric M. Burke
 * Published on The O'Reilly Network (http://www.oreillynet.com/)
 * http://www.onjava.com/pub/a/onjava/excerpt/java_xslt_ch5/index.html?page=9#templates_api
 *
 * @version $Id: StylesheetCache.java 1180 2012-02-24 01:35:08Z tdnorbra $
 * <p>
 *   <dt><b>History:</b>
 *   <pre><b>
 *     Date:     06 JAN 03
 *     Time:     1617
 *     Author:   JD NEUSHUL
 *     Comments: Initial
 *
 *     Date:     08 DEC 2005
 *     Time:     2216
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.StylesheetCache">Terry Norbraten, NPS MOVES</a>
 *     Comments: Incorporated generic conventions for JDK 1.5
 *
 *     Date:     14 APR 2011
 *     Time:     2023
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.StylesheetCache">Terry Norbraten, NPS MOVES</a>
 *     Comments: Refactor to take File objects as inputs
 *   </b></pre>
 * </p>
 * @author JD NEUSHUL
 */
public class StylesheetCache {

    /** map xslt file names to MapEntry instances (MapEntry is defined below) */
    private static final Map<File, MapEntry> CACHE = new HashMap<>();

    // prevent instantiation of this class
    private StylesheetCache() {}

    /**
     * Flush all cached stylesheets from
     * memory, emptying the cache.
     */
    public static synchronized void flushAll() {
        CACHE.clear();
    }

    /**
     * Flush a specific cached stylesheet from memory.
     *
     * @param xsltFile the stylesheet file to remove.
     */
    public static synchronized void flush(File xsltFile) {
        CACHE.remove(xsltFile);
    }

    /**
     * <p>Obtain a new Transformer instance for the specified XSLT file name.  A
     * new entry will be added to the cache if this is the first request for the
     * specified file name.</p>
     *
     * @param xslFile the file object of an XSLT stylesheet
     * @return a transformation context for the given stylesheet
     * @throws TransformerConfigurationException
     */
    public static synchronized Transformer newTransformer(File xslFile)
            throws TransformerConfigurationException {

        // determine when the file was last modified on disk
        long xslLastModified = xslFile.lastModified();
        MapEntry entry = CACHE.get(xslFile);
        if (entry != null) {
            // if the file has been modified more recently than the
            // cached stylesheet, remove the entry reference
            if (xslLastModified > entry.lastModified) {
                entry = null;
            }
        }

        // create a new entry in the cache if necessary
        if (entry == null) {
            TransformerFactory transFact = TransformerFactory.newInstance();

            // If we place a stylesheet in a jar, then use:
//            Source stylesource = new StreamSource(StylesheetCache.class.getClassLoader().getResourceAsStream(xsltFile.getPath()));
            Source stylesource = new StreamSource(xslFile);
            Templates templates = transFact.newTemplates(stylesource);
            entry = new MapEntry(xslLastModified, templates);
            CACHE.put(xslFile, entry);
        }
        return entry.templates.newTransformer();
    }

    /**
     * This inner class represents a value in the cache Map.
     */
    static class MapEntry {

        long lastModified;
        Templates templates;

        MapEntry(long lastModified, Templates templates) {
            this.lastModified = lastModified;
            this.templates = templates;
        }
    }

} // end class file StylesheetCache.java
