package com.java.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NodeTools {
	/**
	 * Method sorts any NodeList by provided attribute.
	 * 
	 * @param nl
	 *            NodeList to sort
	 * @param attributeName
	 *            attribute name to use
	 * @param asc
	 *            true - ascending, false - descending
	 * @param B
	 *            class must implement Comparable and have Constructor(String) -
	 *            e.g. Integer.class , BigDecimal.class etc
	 * @return
	 */
	public static Node[] sortNodes(NodeList nl, boolean asc, Map<String, Class<? extends Comparable>> attrielementTagNameWithTypeMap) {
		class NodeComparator<T> implements Comparator<T> {
			@Override
			public int compare(T a, T b) {
				int ret = 0;
				Comparable bda = null, bdb = null;
				for (Entry<String, Class<? extends Comparable>> entry : attrielementTagNameWithTypeMap.entrySet())
				{
				    System.out.println(entry.getKey() + "/" + entry.getValue());
				    try {
						Constructor bc = entry.getValue().getDeclaredConstructor(String.class);
//						System.out.println(((Element) a).getChildNodes().item(1).getTextContent() + " vs " + ((Element) b).getChildNodes().item(1).getTextContent());
//						System.out.println(bc.newInstance(((Element) a).getChildNodes().item(0).getTextContent()));
//						bda = (Comparable) bc.newInstance(((Element) a).getChildNodes().item(1).getTextContent());
//						bdb = (Comparable) bc.newInstance(((Element) b).getChildNodes().item(1).getTextContent());
						
						bda = (Comparable) bc.newInstance(((Element) a).getElementsByTagName(entry.getKey()).item(0).getTextContent());
						bdb = (Comparable) bc.newInstance(((Element) b).getElementsByTagName(entry.getKey()).item(0).getTextContent());
						
					} catch (Exception e) {
						System.err.println("Node sorting function will not work properly when sorting with key " + entry.getKey());
						return 0; // yes, ugly, i know :)
					}
					ret = bda.compareTo(bdb);
					if (bda.compareTo(bdb) == 0) {
						continue;
					} else {
						break;
					}
				}
				return asc ? ret : -ret;
			}
		}

		List<Node> x = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			x.add(nl.item(i));
		}
		Node[] ret = new Node[x.size()];
		ret = x.toArray(ret);
		Arrays.sort(ret, new NodeComparator<Node>());
		return ret;
	}

	public static void main(String... args) {
		DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String s = "<xml><item id=\"1\" price=\"100.00\" /><item id=\"3\" price=\"29.99\" /><item id=\"2\" price=\"5.10\" /></xml>";
		Document doc = null;
		try {
			builder = factoryBuilder.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(s)));
		} catch (Exception e) {
			System.out.println("Alarm " + e);
			return;
		}

		System.out.println("*** Sort by id ***");
		LinkedHashMap<String, Class<? extends Comparable>> attrielementTagNameWithTypeMap = new LinkedHashMap<>();
		attrielementTagNameWithTypeMap.put("id", Integer.class);
		Node[] ret = NodeTools.sortNodes(doc.getElementsByTagName("item"), true, attrielementTagNameWithTypeMap);

		for (Node n : ret) {
			System.out.println(((Element) n).getAttribute("id") + " : " + ((Element) n).getAttribute("price"));
		}

		/*System.out.println("*** Sort by price ***");
		ret = NodeTools.sortNodes(doc.getElementsByTagName("item"), "price", true, BigDecimal.class);
		for (Node n : ret) {
			System.out.println(((Element) n).getAttribute("id") + " : " + ((Element) n).getAttribute("price"));
		}*/
		
		
		///////////////////////////////
		System.out.println("///////////////////////////////");
		String s1 = "<CompanyDescription>\r\n" + 
				"	<Name>\r\n" + 
				"		<HR-RC-Name>Muster AG</HR-RC-Name>\r\n" + 
				"	</Name>\r\n" + 
				"	<Address>\r\n" + 
				"		<Street>Bahnhofstrasse 1</Street>\r\n" + 
				"		<ZIP-Code>6002</ZIP-Code>\r\n" + 
				"		<City>Luzern</City>\r\n" + 
				"		<Country>SWITZERLAND</Country>\r\n" + 
				"	</Address>\r\n" + 
				"	<UID-EHRA>CH-100.3.032.254-9</UID-EHRA>\r\n" + 
				"	<UID-BFS>CHE-999.999.996</UID-BFS>\r\n" + 
				"	<Workplace workplaceID=\"#4\">\r\n" + 
				"		<BUR-REE-Number>12345677</BUR-REE-Number>\r\n" + 
				"		<AddressExtended>\r\n" + 
				"			<Street>Bahnhofstrasse 1</Street>\r\n" + 
				"			<ZIP-Code>6002</ZIP-Code>\r\n" + 
				"			<City>Luzern</City>\r\n" + 
				"			<Country>SWITZERLAND</Country>\r\n" + 
				"			<Canton>LU</Canton>\r\n" + 
				"			<MunicipalityID>1061</MunicipalityID>\r\n" + 
				"		</AddressExtended>\r\n" + 
				"		<CompanyWorkingTime>\r\n" + 
				"			<WeeklyHoursAndLessons>\r\n" + 
				"				<WeeklyHours>42.00</WeeklyHours>\r\n" + 
				"				<WeeklyLessons>21</WeeklyLessons>\r\n" + 
				"			</WeeklyHoursAndLessons>\r\n" + 
				"		</CompanyWorkingTime>\r\n" + 
				"	</Workplace>\r\n" + 
				"	<Workplace workplaceID=\"#5\">\r\n" + 
				"		<BUR-REE-Number>12345677</BUR-REE-Number>\r\n" + 
				"		<AddressExtended>\r\n" + 
				"			<Street>Neumattstrasse 45</Street>\r\n" + 
				"			<ZIP-Code>5000</ZIP-Code>\r\n" + 
				"			<City>Aarau</City>\r\n" + 
				"			<Country>SWITZERLAND</Country>\r\n" + 
				"			<Canton>AG</Canton>\r\n" + 
				"			<MunicipalityID>4001</MunicipalityID>\r\n" + 
				"		</AddressExtended>\r\n" + 
				"		<CompanyWorkingTime>\r\n" + 
				"			<WeeklyHoursAndLessons>\r\n" + 
				"				<WeeklyHours>40.00</WeeklyHours>\r\n" + 
				"				<WeeklyLessons>20</WeeklyLessons>\r\n" + 
				"			</WeeklyHoursAndLessons>\r\n" + 
				"		</CompanyWorkingTime>\r\n" + 
				"	</Workplace>\r\n" + 
				"	<Workplace workplaceID=\"#6\">\r\n" + 
				"		<BUR-REE-Number>23456788</BUR-REE-Number>\r\n" + 
				"		<AddressExtended>\r\n" + 
				"			<Street>Zelgstrasse 12</Street>\r\n" + 
				"			<ZIP-Code>3027</ZIP-Code>\r\n" + 
				"			<City>Bern</City>\r\n" + 
				"			<Country>SWITZERLAND</Country>\r\n" + 
				"			<Canton>BE</Canton>\r\n" + 
				"			<MunicipalityID>351</MunicipalityID>\r\n" + 
				"		</AddressExtended>\r\n" + 
				"		<CompanyWorkingTime>\r\n" + 
				"			<WeeklyHoursAndLessons>\r\n" + 
				"				<WeeklyHours>40.00</WeeklyHours>\r\n" + 
				"				<WeeklyLessons>20</WeeklyLessons>\r\n" + 
				"			</WeeklyHoursAndLessons>\r\n" + 
				"		</CompanyWorkingTime>\r\n" + 
				"	</Workplace>\r\n" + 
				"</CompanyDescription>";
		try {
			builder = factoryBuilder.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(s1)));
		} catch (Exception e) {
			System.out.println("Alarm " + e);
			return;
		}
		HashMap<String, Class<? extends Comparable>> attrielementTagNameWithTypeMapForPlace = new LinkedHashMap<>();
		attrielementTagNameWithTypeMapForPlace.put("BUR-REE-Number", Integer.class);
		attrielementTagNameWithTypeMapForPlace.put("ZIP-Code", Integer.class);
		Node[] ret1 = NodeTools.sortNodes(doc.getElementsByTagName("Workplace"), true, attrielementTagNameWithTypeMapForPlace);
		for (Node n : ret1) {
//			XMLUtils.printTags(n);
			System.out.println(n.getNodeName() + " - " + n.getChildNodes().item(1).getTextContent() + " name: " + n.getChildNodes().item(1).getTextContent());
		}
		while (doc.getElementsByTagName("Workplace").getLength() != 1) {
			Node node = doc.getElementsByTagName("Workplace").item(0);
			System.out.println(node.getAttributes().getNamedItem("workplaceID"));
			node.getParentNode().removeChild(node);
		}
		System.out.println("================================================= Remain length " + doc.getElementsByTagName("Workplace").getLength());
		
		NodeList elementsByTagName = doc.getElementsByTagName("Workplace");
		for (int i = 0; i < ret1.length; i++) {
			elementsByTagName.item(0).getParentNode().appendChild(ret1[i]);
		}
		
		/*System.out.println("================================================= Remain length " + doc.getElementsByTagName("Workplace").getLength());
		for (int i = 0; i < doc.getElementsByTagName("Workplace").getLength(); i++) {
			System.out.println("===================================");
			XMLUtils.printTags(doc.getElementsByTagName("Workplace").item(i));
		}*/
		
		System.out.println(docToString(doc));
	}

	private static String docToString(Document doc) throws TransformerFactoryConfigurationError {
		try {
			Source source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
