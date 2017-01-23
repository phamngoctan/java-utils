package com.java.utils.xml;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {
	
	public static String formatExpressionToSupportNameSpace(String strToFormat) {
		String[] splitedString = strToFormat.split("/");
		String formattedString = Arrays.asList(splitedString).stream()
				.filter(t -> t != null && t.length() != 0)
				.map(t -> {
						String original = t;
						if (t.indexOf('[') != -1) {
							String nodeName = t.substring(0, t.indexOf('['));
							String otherExpression = original.substring(original.indexOf('['), original.length());
							return "*[local-name()='" + nodeName + "']" + otherExpression + "/";
						}
						return "*[local-name()='" + t + "']/";
					})
				.reduce("", (x, y)-> x + y);
		
		return formatPrefix(formatSuffix(formattedString));
	}

    private static String formatPrefix(String strToFormat) {
		return "//" + strToFormat;
	}
	
	private static String formatSuffix(String strToFormat) {
		if (strToFormat == null || strToFormat.length() == 0) {
			return strToFormat;
		}
		String lastCharacter = Character.toString(strToFormat.charAt(strToFormat.length() - 1));
		if (lastCharacter.equals("/")) {
			return strToFormat.substring(0, strToFormat.length() - 1);
		}
		return "";
	}
    
    public static void printTags(Node nodes) {
		if ((nodes.hasChildNodes() || nodes.getNodeType() != 3)) {
			NodeList nl = nodes.getChildNodes();
			if (nl.getLength() == 1) {
				System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
			}
			for (int j = 0; j < nl.getLength(); j++) {
				printTags(nl.item(j));
			}
		}
	}
    
    public String getXPath(Node node) {
	    Node parent = node.getParentNode();
	    if (parent == null) {
	        return node.getNodeName();
	    }
	    return getXPath(parent) + "/" + node.getNodeName();
	}
	
    public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
  	  return xmlString.replaceAll("(<\\?[^<]*\\?>)?", "") /* remove preamble */
  			  .replaceAll("\n", "").replace("\r", "") /* remove new line */
  			  .replace("\t", " ") /* remove tab */
  			  .replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
  			  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
  			  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3") /* remove closing tags prefix */
  			  .replaceAll(" +", " ") /* remove double white spaces */
  			  ;
  	}
    
    public static String nodeToString(Node node) throws TransformerException {
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.setOutputProperty(OutputKeys.INDENT, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return buf.toString();
	}
    
}
