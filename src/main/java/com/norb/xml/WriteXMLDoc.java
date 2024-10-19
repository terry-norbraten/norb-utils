package com.norb.xml;

import com.norb.utils.LogUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** Utility to write XML Documents from either JDOM or DOM
 * @version $Id: WriteXMLDoc.java 1180 2012-02-24 01:35:08Z tdnorbra $
 * <p>
 *   <dt><b>History:</b>
 *   <pre><b>
 *     Date:     22 SEP 03
 *     Time:     1826
 *     Author:   JDNeushul
 *     Comments: Initial
 *
 *     Date:     25 NOV 05
 *     Time:     1617
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.WriteXMLDoc">Terry Norbraten, NPS MOVES</a>
 *     Comments: Updated to JDOM 1.1
 *
 *     Date:     30 OCT 08
 *     Time:     0449Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.WriteXMLDoc">Terry Norbraten, NPS MOVES</a>
 *     Comments: Addition of DOM to JDOM conversion
 *   </b></pre>
 * </p>
 * @since September 22, 2002, 6:26 PM
 * @author  JDN
 */
public class WriteXMLDoc {

    private static final Logger LOG = LogUtils.getLogger(WriteXMLDoc.class);

    private static Element node;

    public static void main(String[] args) {
        LOG.info("Usage: Call from program: WriteXMLDoc.out(Document c, String fileoutname)");
    }

    /** Serialize a single DOM Element to the underlying Writer
     *
     * @param c
     * @param writer
     */
    public static void serializeDOMElement(Element c, Writer writer) {
        Format format = Format.getRawFormat();
        format.setOmitDeclaration(true);
        format.setIndent("                ");
        XMLOutputter xmlOut = new XMLOutputter(format);
        try {
            xmlOut.output(getJdomElement(c), writer);
        } catch (IOException ioe) {
            LOG.error(ioe);
        }
    }

    /** Serialize a single DOM Element to String form
     *
     * @param xmlDoc the DOM fragment to serialize
     * @param fOmitXMLDeclare true if no XML declaration
     * @param fPrettyPrint true means use {@link Format#getPrettyFormat()}, otherwise use {@link Format#getRawFormat()}
     * @return {@link String} serialization as valid XML
     */
    public static String serializeDOMElement(Document xmlDoc,
            boolean fOmitXMLDeclare, boolean fPrettyPrint) {

        Format format;

        if (fPrettyPrint) {
            format = Format.getPrettyFormat();
        } else {
            format = Format.getRawFormat();
        }
        format.setOmitDeclaration(fOmitXMLDeclare);
        XMLOutputter xmlOut = new XMLOutputter(format);
        return xmlOut.outputString(getJdomElement(xmlDoc));
    }

    /** @param file the XML file to format after marshaling out */
    public static void formatResultsFile(File file) {

        // Lot of hoops to pretty-fy config xml files
        org.jdom2.Document doc = LoadXMLDoc.load(file);
        WriteXMLDoc.xmlOut(doc, file);
    }

    /** Write an entire XML document from DOM to the file specified
     *
     * @param root the DOM document root
     * @param xmlFile the file to write DOM out to
     */
    public static void nodeOut(Node root, File xmlFile) {
        xmlOut(new org.jdom2.Document().setRootElement(getJdomElement(root)), xmlFile);
    }

    /** Write an entire XML document from JDOM to the file specified
     *
     * @param xmlDoc the JDOM document root
     * @param newFile the file to write DOM out to
     */
    public static void xmlOut(org.jdom2.Document xmlDoc, File newFile) {
        LOG.info("newFileName: {}", newFile);
        LOG.info("Writing xml: {} to file: {}", xmlDoc.getRootElement().getName(), newFile);
        FileWriter writter = null;
        try {
            writter = new FileWriter(newFile);
            XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
            xmlOut.output(xmlDoc, writter);
        } catch (IOException ioe) {
            LOG.error(ioe.getLocalizedMessage());
        } finally {
            try {
                if (writter != null)
                    writter.close();
            } catch (IOException ioe) {}
        }
    }

    /** Convert an org.w3c.dom.Node to an org.jdom.Element
     *
     * @param node the DOM node to convert
     * @return a JDOM element from a DOM node
     */
    public static org.jdom2.Element getJdomElement(Node node) {
        WriteXMLDoc.node = (org.w3c.dom.Element) node;

        // Convert from org.w3c.dom to org.jdom for easier access
        return new DOMBuilder().build(WriteXMLDoc.node);
    }

    /** Covert an org.jdom.Document to an org.w3c.dom.Document
     *
     * @param jdomDoc the jdoc document to convert to dom
     * @return a converted jdom to dom document
     */
    public static org.w3c.dom.Document convertToDOM(org.jdom2.Document jdomDoc) {
        org.w3c.dom.Document doc = null;
        try {
            doc = new DOMOutputter().output(jdomDoc);
        } catch (JDOMException je) {
            LOG.error(je.getLocalizedMessage());
        }
        return doc;
    }

} // end class file WriteXMLDoc.java
