package com.java.utils.xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

public class XmlUtils {
	
	@Test
	public void xmlUtils_using_test() throws XPathExpressionException, SAXException, IOException, TransformerException, ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		XPath xPath = XPathFactory.newInstance().newXPath();
		Document xmlDocumentForExportedFile = builder.parse(new File(this.getClass().getClassLoader().getResource("referenceXml/exportedFile.xml").getFile()));
		Document xmlDocumentForReferenceFile = builder.parse(new File(this.getClass().getClassLoader().getResource("referenceXml/DeclareAll_FAK_Detail20141031_DE.xml").getFile()));
		
		int assertionErrorCount = 0;
		StringBuilder assertionResult = new StringBuilder();
		
		String expression = "/Staff/Person[1]/Particulars";
		String formattedExpression = formatExpressionToSupportNameSpace(expression);
		//String includeChildElements = "true";
		
		NodeList nodeListForExportedFile = (NodeList) xPath.compile(formattedExpression).evaluate(xmlDocumentForExportedFile, XPathConstants.NODESET);
		NodeList nodeListForReferenceFile = (NodeList) xPath.compile(formattedExpression).evaluate(xmlDocumentForReferenceFile, XPathConstants.NODESET);
		if (nodeListForReferenceFile != null && nodeListForExportedFile != null) {
			for (int nodeCount = 0; nodeCount < nodeListForExportedFile.getLength(); nodeCount++) {
				//printTags(nodeListForExportedFile.item(nodeCount), nodeListForReferenceFile.item(nodeCount));
				String referenceXml = removeXmlStringNamespaceAndPreamble(nodeToString(nodeListForReferenceFile.item(nodeCount)));
				String actualXml = removeXmlStringNamespaceAndPreamble(nodeToString(nodeListForExportedFile.item(nodeCount)));
				System.out.println(referenceXml);
				System.out.println(actualXml);
				Diff myDiffSimilar = DiffBuilder.compare(referenceXml).withTest(actualXml)
						.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
						.ignoreWhitespace()
						.checkForSimilar()
						.build();
				Iterator<Difference> iter = myDiffSimilar.getDifferences().iterator();
				while (iter.hasNext()) {
					assertionErrorCount++;
					assertionResult.append("\nNo ").append(assertionErrorCount).append(": ").append(expression)
							.append(" ").append(iter.next().toString());
				}
			}
		}
		assertTrue(assertionResult.toString(), assertionResult.length() != 0);
	}
	
	public void printTags(Node nodes, Node refNodes) {
		if ((nodes.hasChildNodes() || nodes.getNodeType() != 3) && (refNodes.hasChildNodes() || refNodes.getNodeType() != 3)) {
			NodeList nl = nodes.getChildNodes();
			NodeList nlRef = refNodes.getChildNodes();
			if (nl.getLength() == 1) {
				System.out.println("============================");
				System.out.println(getXPath(nodes));
				System.out.println(nodes.getNodeName() + " : " + nodes.getTextContent());
				System.out.println("Ref " + getXPath(refNodes));
				System.out.println("Ref " + refNodes.getNodeName() + " : " + refNodes.getTextContent());
			}
			for (int j = 0; j < nl.getLength(); j++) {
				printTags(nl.item(j), nlRef.item(j));
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
	
	public String nodeToString(Node node) throws TransformerException {
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.setOutputProperty(OutputKeys.INDENT, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return buf.toString();
	}
	
	public String removeXmlStringNamespaceAndPreamble(String xmlString) {
	  return xmlString.replaceAll("(<\\?[^<]*\\?>)?", "") /* remove preamble */
			  .replaceAll("\n", "").replace("\r", "") /* remove new line */
			  .replace("\t", " ") /* remove tab */
			  .replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
			  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
			  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3") /* remove closing tags prefix */
			  .replaceAll(" +", " ") /* remove double white spaces */
			  ;
	}
	
	private String formatExpressionToSupportNameSpace(String strToFormat) {
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
	
	private String formatPrefix(String strToFormat) {
		return "//" + strToFormat;
	}
	
	private String formatSuffix(String strToFormat) {
		if (strToFormat == null || strToFormat.length() == 0) {
			return strToFormat;
		}
		String lastCharacter = Character.toString(strToFormat.charAt(strToFormat.length() - 1));
		if (lastCharacter.equals("/")) {
			return strToFormat.substring(0, strToFormat.length() - 1);
		}
		return "";
	}
}
