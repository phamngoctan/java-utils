package com.java.utils.xml;

import java.io.StringWriter;
import java.util.Arrays;

import javax.ws.rs.NotSupportedException;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.ElementSelectors;

public class ConsiderAttributeDifferenceEvaluator implements DifferenceEvaluator {
    private String attributeName;
    private Document fullXmlDocumentForReferenceFile;
    private Document fullXmlDocumentForExportedFile;
    private XPath xPath;
    public ConsiderAttributeDifferenceEvaluator(String attributeName, Document fullXmlDocumentForReferenceFile, Document fullXmlDocumentForExportedFile, XPath xPath) {
        this.attributeName = attributeName;
        this.fullXmlDocumentForReferenceFile = fullXmlDocumentForReferenceFile;
        this.fullXmlDocumentForExportedFile = fullXmlDocumentForExportedFile;
        this.xPath = xPath; 
    }
    
    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
        if (outcome == ComparisonResult.EQUAL)
            return outcome;
        final Node controlNode = comparison.getControlDetails().getTarget();
        if (controlNode instanceof Attr) {
            Attr attr = (Attr) controlNode;
            xPath = XPathFactory.newInstance().newXPath();
            System.out.println(comparison.getControlDetails().getValue());
            System.out.println(comparison.getControlDetails().getXPath());
            System.out.println(comparison.getControlDetails().getParentXPath());

            System.out.println("========================================");
            System.out.println(comparison.getTestDetails().getTarget().getNodeType());
            System.out.println(comparison.getTestDetails().getTarget().getParentNode());
            System.out.println(comparison.getTestDetails().getTarget().getTextContent());
            System.out.println(comparison.getTestDetails().getTarget().getNodeName());
            System.out.println("========================================");
            
            if (attr.getName().equals(attributeName)) {
    			if (attributeName.equalsIgnoreCase("institutionIDRef")) {
    				String currentComparingTag = getCurrentComparingTag(comparison.getTestDetails().getParentXPath());
    				// Check for tag of /Job/Addressees/
    				if (InstitutionSupportedTag.fromString(currentComparingTag) != null) {
    					switch (InstitutionSupportedTag.fromString(currentComparingTag)) {
    					case AHV_AVS:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					case BVG_LPP:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					case FAK_CAF:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					case KTG_AMC:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					case UVG_LAA:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					case UVGZ_LAAC:
    						throw new NotSupportedException("This tag is not supported for comparing");
    						//break;
    					default:
    						break;
    					}
    				}
    				
    				if (PersonInstitutionSupportedTag.fromString(currentComparingTag) != null) {
    					PersonInstitutionSupportedTag personInstitutionTag = null;
    					switch (PersonInstitutionSupportedTag.fromString(currentComparingTag)) {
    					case AHV_AVS_SALARY:
							personInstitutionTag = PersonInstitutionSupportedTag.AHV_AVS_SALARY;
    						break;
    					case FAK_CAF_SALARY:
    						personInstitutionTag = PersonInstitutionSupportedTag.FAK_CAF_SALARY;
    						break;
    					case KTG_AMC_SALARY:
    						personInstitutionTag = PersonInstitutionSupportedTag.KTG_AMC_SALARY;
    						break;
    					case UVG_LAA_SALARY:
    						personInstitutionTag = PersonInstitutionSupportedTag.UVG_LAA_SALARY;
    						break;
    					case UVGZ_LAAC_SALARY:
    						personInstitutionTag = PersonInstitutionSupportedTag.UVGZ_LAAC_SALARY;
    						break;
    					default:
    						break;
    					}
    					if (personInstitutionTag != null && isPersonInstitutionReferenceCorrectID(comparison, xPath, personInstitutionTag)) {
							return ComparisonResult.SIMILAR;
						}
    				}
    			}
            	// keep it as normal
                return outcome;
            }
        }
        return outcome;
    }

	private boolean isPersonInstitutionReferenceCorrectID(Comparison comparison, XPath xPath, PersonInstitutionSupportedTag referenceTag) {
		try {
			String referencePathInReferenceFileExpression = "//Institutions/" + referenceTag.toReferenceTag() + "[@institutionID='" + comparison.getControlDetails().getValue() + "']";
			referencePathInReferenceFileExpression = formatExpressionToSupportNameSpace(referencePathInReferenceFileExpression);
			System.out.println(referencePathInReferenceFileExpression);
			final NodeList nodeListForReferenceFile = (NodeList) xPath.compile(referencePathInReferenceFileExpression).evaluate(fullXmlDocumentForReferenceFile, XPathConstants.NODESET);
			for (int i = 0; i < nodeListForReferenceFile.getLength(); i++) {
				printTags(nodeListForReferenceFile.item(i));
			}
			final String referenceXml = removeXmlStringNamespaceAndPreamble(nodeToString(nodeListForReferenceFile.item(0)));
			
			System.out.println("==================");
			
			String referencePathInExportedFile = "//Institutions/" + referenceTag.toReferenceTag() + "[@institutionID='" + comparison.getTestDetails().getValue() + "']";
			referencePathInExportedFile = formatExpressionToSupportNameSpace(referencePathInExportedFile);
			System.out.println(referencePathInExportedFile);
			final NodeList nodeListForExportedFile = (NodeList) xPath.compile(referencePathInExportedFile).evaluate(fullXmlDocumentForExportedFile, XPathConstants.NODESET);
			for (int i = 0; i < nodeListForExportedFile.getLength(); i++) {
				printTags(nodeListForExportedFile.item(i));
			}
			final String actualXml = removeXmlStringNamespaceAndPreamble(nodeToString(nodeListForExportedFile.item(0)));
			final Diff myDiffSimilar = DiffBuilder.compare(referenceXml).withTest(actualXml)
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
					.withDifferenceEvaluator(new IgnoreAttributeDifferenceEvaluator("institutionID"))
					.ignoreWhitespace().checkForSimilar().build();
			if (!myDiffSimilar.hasDifferences()) {
				return true;
			}
			return false;
		} catch (XPathExpressionException | TransformerException ex) {
			throw new RuntimeException("Something wrong when do the ConsiderAttributeDifference Evaluator");
		}
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
    
    private String getCurrentComparingTag(String fullPath) {
    	if (fullPath == null || fullPath.isEmpty()) {
    		return fullPath;
    	}
    	return fullPath.substring(fullPath.lastIndexOf("/") + 1, fullPath.lastIndexOf("["));
    }
    
    public void printTags(Node nodes) {
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
	
    private String removeXmlStringNamespaceAndPreamble(String xmlString) {
  	  return xmlString.replaceAll("(<\\?[^<]*\\?>)?", "") /* remove preamble */
  			  .replaceAll("\n", "").replace("\r", "") /* remove new line */
  			  .replace("\t", " ") /* remove tab */
  			  .replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
  			  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
  			  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3") /* remove closing tags prefix */
  			  .replaceAll(" +", " ") /* remove double white spaces */
  			  ;
  	}
    
    private String nodeToString(Node node) throws TransformerException {
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.setOutputProperty(OutputKeys.INDENT, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return buf.toString();
	}
}