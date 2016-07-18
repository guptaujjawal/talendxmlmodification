/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talendxmlmodificationdom;
/**
 *
 * @author ugupta
 */
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TalendXMLModificationDOM {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      try {
		
                String filepath = "src/inputfiles/xml1.xml";
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);

		// Get the root element
		NodeList talendfile = doc.getElementsByTagName("node");
                
                System.out.println("No. of nodes to be processed : "+talendfile.getLength());
                for(int k=0;k<talendfile.getLength();k++){
                    boolean bComment=false;
                    boolean bLabel=false;
                Node nodeElement = talendfile.item(k);
		// getting node attribute
		NamedNodeMap attr = nodeElement.getAttributes();
		Node nodeAttr = attr.getNamedItem("componentName");
		if(nodeAttr.getNodeValue().equals("tSetGlobalVar") || nodeAttr.getNodeValue().equals("tOracleOutput")
                        || nodeAttr.getNodeValue().equals("tOracleRow") || nodeAttr.getNodeValue().equals("tOracleInput") 
                        || nodeAttr.getNodeValue().equals("tFileOutputDelimited")|| nodeAttr.getNodeValue().equals("tFileInputDelimited")){
                    //loop the node list node
                    NodeList list = nodeElement.getChildNodes();
                    for (int i = 0; i < list.getLength(); i++) {
                        Node node = list.item(i);
                         if ("elementParameter".equals(node.getNodeName())) {
                                  NamedNodeMap elementAttr=node.getAttributes();
                                  Node elementAttrFeild = elementAttr.getNamedItem("field");
                                  if(elementAttrFeild.getNodeValue().equals("MEMO")){
                                      bComment=true;
                                      Node elementAttrCValue = elementAttr.getNamedItem("value");
                                      if(elementAttrCValue.getNodeValue().equals("")){
                                          elementAttrCValue.setNodeValue("Sample Comment");
                                      }
                                  }
                         }
                          if ("elementParameter".equals(node.getNodeName())) {
                                  NamedNodeMap elementAttr=node.getAttributes();
                                  Node elementAttrFeild = elementAttr.getNamedItem("field");
                                  if(elementAttrFeild.getNodeValue().equals("TEXT")){
                                      Node elementAttrFeildLabel = elementAttr.getNamedItem("name");
                                      if(elementAttrFeildLabel.getNodeValue().equals("LABEL")){
                                            bLabel=true;
                                            Node elementAttrLValue = elementAttr.getNamedItem("value");
                                            if(elementAttrLValue.getNodeValue().equals("")){
                                                elementAttrLValue.setNodeValue("Sample Label");
                                            }
                                      }
                                  }
                         }
                    }
                   if(!bComment){
                        //append a new node to node element
                        Element comment = doc.createElement("elementParameter");
                        comment.setAttribute("field", "MEMO"); 
                        comment.setAttribute("name", "COMMENT");
                        comment.setAttribute("value", "Sample Comment");
                        nodeElement.appendChild(comment);
                    }
                 if(!bLabel){
                       //append a new LABEL to node element
                       Element label = doc.createElement("elementParameter");
                        label.setAttribute("field", "TEXT"); 
                        label.setAttribute("name", "LABEL");
                        label.setAttribute("value", "Sample Label");
                        nodeElement.appendChild(label);
                   }
                }
             }	
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);

		System.out.println("Done");

	   } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	   } 
            catch (TransformerException tfe) {
		tfe.printStackTrace();
	   } 
            catch (IOException ioe) {
		ioe.printStackTrace();
	   } catch (SAXException sae) {
		sae.printStackTrace();
	   }  
    }
    
}
