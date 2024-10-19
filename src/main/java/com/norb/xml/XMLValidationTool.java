/*
 * File:        XMLValidationTool.java
 *
 * Created on:  02 SEP 08
 *
 * Refenences:  Code borrowed from Elliotte Rusty Harold's IBM article at:
 *              http://www-128.ibm.com/developerworks/xml/library/x-javaxmlvalidapi.html,
 *              and from Olaf Meyer's posting on Google Groups - comp.text.xml
 *              (ErrorPrinter).
 *
 * Assumptions: Connection to the internet now optional.  XML files will be
 *              resolved to the Schema in order to shorten internal parsing
 *              validation time.
 */
package com.norb.xml;

// Standard Library Imports
import com.norb.utils.LogUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility class to validate XML files against provided Schema and
 * report errors in &lt;(file: row, column): error&gt; format.
 * @version $Id: XMLValidationTool.java 315 2012-02-08 03:59:47Z tdnorbra $
 * <p>
 *   <b>History:</b>
 *   <pre><b>
 *     Date:     04 NOV 08
 *     Time:     1646Z
 *     Author:   <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.XMLValidationTool">Terry Norbraten, NPS MOVES</a>
 *     Comments: 1) Initial
 *   </b></pre>
 * </p>
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.XMLValidationTool">Terry Norbraten</a>
 */
public class XMLValidationTool {

    /** Permanent www location for AVCL Schema */
    public static final String ONLINE_AVCL_SCHEMA = "https://savage.nps.edu/sub/usmc/MobileRobotTargetGUI/Scripts/AVCL21.xsd";

    /** The locally resolved location for AVCL21.xsd */
//    public static final String LOCAL_AVCL_SCHEMA = AUVW.SCRIPTS + "AVCL21.xsd";

    /** The locally resolved location for MapSchema.xsd */
//    public static final String LOCAL_AVCL_MAP_SCHEMA = AUVW.MAPS + "MapSchema.xsd";

    private static final Logger LOG = LogUtils.getLogger(XMLValidationTool.class);

    private static XMLValidationTool me;
    private FileWriter fWriter;
    private File xmlFile, schema, errorsLog;
    private boolean valid;

    /**
     * Creates a singleton instance of XMLValidationTool
     * @param xmlFile the scene file to validate
     * @param schema the schema to validate the xmlFile against
     * @return a singleton instance of XMLValidationTool
     */
    public static XMLValidationTool getInstance(File xmlFile, File schema) {
        if (me == null) {
            me = new XMLValidationTool(xmlFile, schema);
        }
        return me;
    }

    /**
     * Creates a singleton instance of XMLValidationTool
     * @return a singleton instance of XMLValidationTool
     */
    public static XMLValidationTool getInstance() {
        if (me == null) {
            me = new XMLValidationTool();
        }
        return me;
    }

    /** Default instance of XMLValidationTool */
    private XMLValidationTool() {

        /* Through trial and error, found how to set this property by
         * deciphering the JAXP debug readout using the -Djaxp.debug=1 JVM arg.
         * Reading the API for SchemaFactory.getInstance(String) helps too.
         */
        System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema",
                "org.apache.xerces.jaxp.validation.XMLSchemaFactory");
        try
        {
            LOG.debug("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema = " +
                    System.getProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema"));
        }
        catch (NoClassDefFoundError ncd)
        {} // just here to catch the exception when it's called from a command-line run

        // Prepare ValidationErrors log with current DTG
        errorsLog = new File(System.getProperty("user.dir") + "/ValidationErrors.txt");

        // New ValidationErrors log each Workbench startup
        if (errorsLog.exists()) {
            errorsLog.delete();
        }
    }

    /**
     * Creates a new instance of XMLValidationTool
     * @param xmlFile the scene file to validate
     * @param schema the schema to validate the xmlFile against
     */
    private XMLValidationTool(File xmlFile, File schema) {
        this();
        setXmlFile(xmlFile); // these methods must stay public for validation to work
        setSchema(schema);
    }

    /** Will report any XML well-formedness or validation errors encountered
     * @return true if parsed XML file is well-formed and valid XML
     */
    public boolean isValidXML() {

        // start fresh
        valid = true;

        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // 2. Compile the schema.
        // Here the schema is loaded from a java.io.File, but you could use
        // a java.net.URL or a javax.xml.transform.Source instead.
        Schema schemaDoc = null;
        try {
            schemaDoc = factory.newSchema(getSchema());
        } catch (SAXException ex) {
            LOG.error("Unable to create Schema object: " + ex);
        }

        // 3. Get a validator from the schema object.
        Validator validator = schemaDoc.newValidator();

        // 4. Designate an error handler and an LSResourceResolver
        MyHandler mh = new MyHandler();
        validator.setErrorHandler(mh);

        // 5. Prepare to parse the document to be validated.
        InputSource src = new InputSource(getXmlFile().getAbsolutePath());
        SAXSource source = new SAXSource(src);

        // 6. Parse, validate and report any errors.
        try {
            LOG.info("XML schema validation results:  "); // source.getSystemId() +

            fWriter = new FileWriter(errorsLog, true);
            Calendar cal = Calendar.getInstance();
            fWriter.write("****************************\n");
            fWriter.write(cal.getTime().toString() + "\n");
            fWriter.write("****************************\n\n");

            validator.validate(source);

        } catch (SAXException ex) {
            LOG.error("{} is not well-formed XML", source.getSystemId());
            LOG.error(ex);
        } catch (IOException ex) {
            LOG.error(ex);
        } finally {
            try {

                // Space between file entries
                fWriter.write("\n");
                fWriter.close();
            } catch (IOException ex) {}
        }
        return valid;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    /** Mutator method to change the AVCL (or X3D etc.) file to validate
     * @param file the file to set for this validator to validate
     */
    public final void setXmlFile(File file) {
        xmlFile = file;
    }

    public File getSchema() {
        return schema;
    }

    public final void setSchema(File schema) {
        this.schema = schema;
    }

    /** Inner utility class to report errors in <(file: row, column): error>
     * format and to resolve X3D scenes to a local DTD
     */
    class MyHandler implements ErrorHandler {

        private MessageFormat message = new MessageFormat("({0}: row {1}, column {2}):\n{3}\n");
        private String validationMessage;

        /** Stores the particular message as the result of a SAXParseException
         * encountered.
         * @param ex the particular SAXParseException used to form a message
         */
        private void setMessage(SAXParseException ex) {
            validationMessage = message.format(new Object[]{ex.getSystemId(), ex.getLineNumber(), ex.getColumnNumber(), ex.getMessage()});
        }

        /** Needed to ensure that a batch of file errors get recorded
         * @param level WARNING, ERROR or FATAL error reporting levels
         */
        private void writeMessage(String level) {
            try {
                fWriter.write(level + validationMessage + "\n");
            } catch (IOException ex) {
                LOG.error(ex);
            }

            // if we got here, something went wrong
            valid = false;
        }

        @Override
        public void warning(SAXParseException ex) {
            setMessage(ex);
            writeMessage("Warning: ");
            LOG.warn(validationMessage);
            System.err.println ("  warning");
            System.err.println (validationMessage);
        }

        @Override
        public void error(SAXParseException ex) {
            setMessage(ex);
            writeMessage("Error: ");
            LOG.error(validationMessage);
            System.err.println ("  error");
            System.err.println (validationMessage);
        }

        @Override
        public void fatalError(SAXParseException ex) throws SAXParseException {
            setMessage(ex);
            writeMessage("Fatal error: ");
            LOG.fatal(validationMessage);
            System.err.println ("  fatal error");
            System.err.println (validationMessage);
            throw ex;
        }
    }

} // end class file XMLValidationTool.java
