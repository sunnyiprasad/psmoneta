package com.rsc.moneta.test.module;


import com.rsc.moneta.module.CheckResponse;
import com.rsc.moneta.module.outputhandler.MonetaOutputHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sulic
 */
public class TestMonetaOutputHandler {

    @Test
    public void testParseResponse() {
        try {
            MonetaOutputHandler handler = new MonetaOutputHandler();
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);            
            Document doc = fac.newDocumentBuilder().parse("testdata/testMonetaResponse.xml");
            CheckResponse res = handler.parseResponse(doc);
            Assert.assertNull(res);
        } catch (SAXException ex) {
            Logger.getLogger(TestMonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TestMonetaOutputHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
