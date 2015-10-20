import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Class for HealthProfileReader
 * @author Toomas Kallioja
 *
 */
public class HealthProfileReader {
	Document doc;
    XPath xpath;

    /**
     * Load the xml from file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
	public void loadXML() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        doc = builder.parse("people.xml");

        //creating xpath object
        getXPathObj();
    }

    /**
     * Get xpath object
     * @return xpath
     */
    public XPath getXPathObj() {

        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        return xpath;
    }
    /**
     * Get weight of person
     * @param personId
     * @return weight node of person
     * @throws XPathExpressionException
     */
    public Node getWeight(int personId) throws XPathExpressionException {
    	XPathExpression expr = xpath.compile("//person[@id="+personId+"]/healthprofile/weight");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }

    /**
     * Get height of person
     * @param personId
     * @return height node of person
     * @throws XPathExpressionException
     */
    public Node getHeight(int personId) throws XPathExpressionException {
    	XPathExpression expr = xpath.compile("//person[@id="+personId+"]/healthprofile/height");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
    }

    /**
     * Print a given node
     * @param node
     */
    public void printNode(Node node) {
    	// Check that node is now text node
		if (node.getNodeType() != Node.TEXT_NODE) {
			// If node is person, print line that shows person's id
			if (node.getNodeName() == "person") {
                System.out.println("");
				System.out.print("Person with ID " + node.getAttributes().getNamedItem("id").getNodeValue() + ": ");
			} else {
			// Else print the node name	
				System.out.print(node.getNodeName() + ": ");
			}
			// Get node's child nodes and make a nodelist
			NodeList newNodeList = node.getChildNodes();
			/* If nodelist has multiple nodes (meaning that node has elements as children)
			 * print the nodelist
			 */
			if (newNodeList.getLength() > 1) {
				System.out.println("");
				printNodeList(newNodeList);
			// If node doesnt have elements, print the node value	
			} else {
				if (node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
	    			System.out.println(node.getFirstChild().getNodeValue());
	    		}
			}
		}
    }

   /**
    * Gets NodeList as parameter, loops it through and prints every node
    * @param nodeList
    */
    public void printNodeList(NodeList nodeList) {
	    for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			printNode(node);
		}
    }

    /**
     * Print all people from the xml file
     * @throws XPathExpressionException
     */
    public void printAllPeople() throws XPathExpressionException {
    	// Gets all the person nodes from the xml and makes a nodelist
    	XPathExpression expr = xpath.compile("/people/person");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        // Print the nodelist
        printNodeList(nodes);
    }

    /**
     * Print healthprofile of person with certain id
     * @param id
     * @throws XPathExpressionException
     */
    public void printHealthProfileByPersonId(int id) throws XPathExpressionException {
    	// Gets the healthprofile node of person with the given id
    	XPathExpression expr = xpath.compile("//person[@id="+id+"]/healthprofile/..");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODE);
        // Print the id of the person and the healthprofile node
       	System.out.println("Healthprofile of person with id " + id + ":");
        System.out.println("");
       	printNodeList(nodes);
    }

    /**
     * Prints people with certain weight conditions
     * @param weight
     * @param condition
     * @throws XPathExpressionException
     */
    public void printPeopleByWeight(int weight, String condition) throws XPathExpressionException {
    	// Finds all the persons whose weight fits to the given condition and makes a nodelist
    	XPathExpression expr = xpath.compile("/people/person/healthprofile[weight " + condition + "'" + weight + "']/..");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        // Prints the given weight condition and the nodelist
        System.out.println("Persons with weight " + condition + weight + ": ");
        printNodeList(nodes);
    }

	/**
	 * When the program is run, initialize new healthProfileReader, load xml and print information from xml
	 * 
	 * @param args
	 */
    public static void main(String[] args) throws ParserConfigurationException, SAXException,
        IOException, XPathExpressionException {   
        // Initialize new HealthProfileReader
        HealthProfileReader hpr = new HealthProfileReader();
        // Load XML file
        hpr.loadXML();
        // Print the wanted information from xml
        hpr.printAllPeople();
        System.out.println("");
        hpr.printHealthProfileByPersonId(5);
        System.out.println("");
        hpr.printPeopleByWeight(90, ">"); 
    }
}