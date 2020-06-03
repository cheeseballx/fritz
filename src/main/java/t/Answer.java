package t;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Answer {

    private Map<String, String> map;

    public Answer(String result) {

        this.map = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = builder.parse(new InputSource(new StringReader(result)));
        } catch (SAXException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element root = doc.getDocumentElement();

        NodeList childs = root.getChildNodes();

        for (int i=0; i<childs.getLength(); i++){
            Node child = childs.item(i);
            
            String tag = child.getNodeName();
            String content = child.getNodeValue();

            this.map.put(tag, content);
        }
    }

    public Map<String,String> getTagValues(){
        
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
            String key = it.next();
            System.out.println(key + " --- " + map.get(key));
        }
        
        return this.map;
    }
}