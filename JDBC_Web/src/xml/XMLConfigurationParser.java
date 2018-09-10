package xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class XMLConfigurationParser {
    public static String readXMLBasePackage(String configPath) throws DocumentException {
        SAXReader reader = new SAXReader();
        InputStream in = XMLConfigurationParser.class.getClassLoader().getResourceAsStream(configPath);
        Document doc = reader.read(in);

        Element rootElement = doc.getRootElement();
        Element scanElement = rootElement.element("component-scan");

        String path = scanElement.attributeValue("base-package");

        return path;
    }
}
