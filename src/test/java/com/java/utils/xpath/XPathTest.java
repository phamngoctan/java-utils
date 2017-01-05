package com.java.utils.xpath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathTest {

	private XPath xPath;
	private String xml;
	private InputSource xmlDocument;

	@Before
	public void init() {
		xPath = XPathFactory.newInstance().newXPath();
		xml = "<?xml version=\"1.0\"?>\r\n" + "<Employees>\r\n" + "    <Employee emplid=\"1111\" type=\"admin\">\r\n"
				+ "        <firstname>John</firstname>\r\n" + "        <lastname>Watson</lastname>\r\n"
				+ "        <age>30</age>\r\n" + "        <email>johnwatson@sh.com</email>\r\n" + "    </Employee>\r\n"
				+ "    <Employee emplid=\"2222\" type=\"admin\">\r\n" + "        <firstname>Sherlock</firstname>\r\n"
				+ "        <lastname>Homes</lastname>\r\n" + "        <age>32</age>\r\n"
				+ "        <email>sherlock@sh.com</email>\r\n" + "    </Employee>\r\n"
				+ "    <Employee emplid=\"3333\" type=\"user\">\r\n" + "        <firstname>Jim</firstname>\r\n"
				+ "        <lastname>Moriarty</lastname>\r\n" + "        <age>52</age>\r\n"
				+ "        <email>jim@sh.com</email>\r\n" + "    </Employee>\r\n"
				+ "    <Employee emplid=\"4444\" type=\"user\">\r\n" + "        <firstname>Mycroft</firstname>\r\n"
				+ "        <lastname>Holmes</lastname>\r\n" + "        <age>41</age>\r\n"
				+ "        <email>mycroft@sh.com</email>\r\n" + "    </Employee>\r\n" + "</Employees>";
	}

	@Test
	public void xPathTest_readAllValue()
			throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		String expression = "/Employees/Employee/firstname";
		System.out.println(expression);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		// Document xmlDocument = builder.parse(new
		// FileInputStream("\\employees.xml"));

		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
		}
	}

	@Test
	public void xPathTest_parseAndEvaluate() throws XPathExpressionException {
	}

	public static void main(String[] args) {
		String xmlData = "<Employees>\r\n" + 
				"	<Employee id=\"1\">\r\n" + 
				"		<age>29</age>\r\n" + 
				"		<name>Pankaj</name>\r\n" + 
				"		<gender>Male</gender>\r\n" + 
				"		<role>Java Developer</role>\r\n" + 
				"	</Employee>\r\n" + 
				"	<Employee id=\"2\">\r\n" + 
				"		<age>35</age>\r\n" + 
				"		<name>Lisa</name>\r\n" + 
				"		<gender>Female</gender>\r\n" + 
				"		<role>CEO</role>\r\n" + 
				"	</Employee>\r\n" + 
				"	<Employee id=\"3\">\r\n" + 
				"		<age>40</age>\r\n" + 
				"		<name>Tom</name>\r\n" + 
				"		<gender>Male</gender>\r\n" + 
				"		<role>Manager</role>\r\n" + 
				"	</Employee>\r\n" + 
				"	<Employee id=\"4\">\r\n" + 
				"		<age>25</age>\r\n" + 
				"		<name>Meghna</name>\r\n" + 
				"		<gender>Female</gender>\r\n" + 
				"		<role>Manager</role>\r\n" + 
				"	</Employee>\r\n" + 
				"</Employees>";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

			// Create XPathFactory object
			XPathFactory xpathFactory = XPathFactory.newInstance();

			// Create XPath object
			XPath xpath = xpathFactory.newXPath();

			String name = getEmployeeNameById(doc, xpath, 4);
			System.out.println("Employee Name with ID 4: " + name);

			List<String> names = getEmployeeNameWithAge(doc, xpath, 30);
			System.out.println("Employees with 'age>30' are:" + Arrays.toString(names.toArray()));

			List<String> femaleEmps = getFemaleEmployeesName(doc, xpath);
			System.out.println("Female Employees names are:" + Arrays.toString(femaleEmps.toArray()));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private static List<String> getFemaleEmployeesName(Document doc, XPath xpath) {
		List<String> list = new ArrayList<>();
		try {
			// create XPathExpression object
			XPathExpression expr = xpath.compile("/Employees/Employee[gender='Female']/name/text()");
			// evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static List<String> getEmployeeNameWithAge(Document doc, XPath xpath, int age) {
		List<String> list = new ArrayList<>();
		try {
			XPathExpression expr = xpath.compile("/Employees/Employee[age>" + age + "]/name/text()");
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static String getEmployeeNameById(Document doc, XPath xpath, int id) {
		String name = null;
		try {
			XPathExpression expr = xpath.compile("/Employees/Employee[@id='" + id + "']/name/text()");
			name = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return name;
	}

}
