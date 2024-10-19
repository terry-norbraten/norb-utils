/*
 * LoadXMLDoc.java
 *
 * Created on: September 22, 2002, 6:26 PM
 */
package com.norb.xml;

import com.norb.utils.LogUtils;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Loads an XML file and returns a JDOM Document object
 * @author  JD NEUSHUL
 * @version $Id: LoadXMLDoc.java 1180 2012-02-24 01:35:08Z tdnorbra $
 */
public class LoadXMLDoc {

    private static final Logger LOG = LogUtils.getLogger(LoadXMLDoc.class);

   public static void main(String[] args) {
      LOG.info("Usage: Call from program: LoadXMLDoc.load(String filename)");
   }

   /**
    * Loads an XML file and returns a JDOM Document object
    * @param xmlFile the XML file to load
    * @return a JDOM Document object from a given XML file
    */
   public static Document load(File xmlFile) {

      Document xmlDoc = null;
      SAXBuilder builder = new SAXBuilder();
      try {

         xmlDoc = builder.build(xmlFile);
         LOG.info("{} Loaded", xmlFile);

      } catch (JDOMException | IOException e) {
          LOG.error(e.getLocalizedMessage());
      }

      return xmlDoc;
   }

} // end class file LoadXMLDoc.java