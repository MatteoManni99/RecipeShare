package com.example.demo1;

import com.thoughtworks.xstream.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {
    public static String databaseIp;
    public static int databasePort;
    public static String databaseUser;
    public static String databasePsw;
    public static int classificaMaxLines;
    public static int totalAliens;
    public static int totalLives;
    
    public static void load() { //1
        Configuration.validate();
        
        try {
            String configurationFileContent = new String(Files.readAllBytes(Paths.get("conf.xml"))); 
            XStream xstream = new XStream();
            xstream.alias("Configuration", SerializableConfiguration.class);
            SerializableConfiguration confObj = (SerializableConfiguration)xstream.fromXML(configurationFileContent);
            
            Configuration.databaseIp = confObj.databaseIp;
            Configuration.databasePort = confObj.databasePort;
            Configuration.databaseUser = confObj.databaseUser;
            Configuration.databasePsw = confObj.databasePsw;
            Configuration.classificaMaxLines = confObj.classificaMaxLines;
            Configuration.totalAliens = confObj.totalAliens;
            Configuration.totalLives = confObj.totalLives;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void validate() { //2
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document document = documentBuilder.parse(new File("conf.xml"));
            Schema schema = schemaFactory.newSchema(new StreamSource(new File("conf.xsd")));
            schema.newValidator().validate(new DOMSource(document));
        }
        catch (SAXException ex) {
            System.out.println("Validation error: " + ex.getMessage());
        }
        catch (ParserConfigurationException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

//1: Preleva le informazioni dal file di configurazione locale
//2: Valida il file di configurazione locale