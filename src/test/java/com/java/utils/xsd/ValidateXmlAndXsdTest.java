package com.java.utils.xsd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Assert;
import org.junit.Test;

public class ValidateXmlAndXsdTest {

	@Test
	public void test() throws IOException {
		ClassLoader classLoader = ValidateXmlAndXsdTest.class.getClassLoader();
		
//		InputStream isXml = new FileInputStream(new File(classLoader.getResource("test.xml").getFile()));
		InputStream isXml2 = new FileInputStream(new File(classLoader.getResource("test2.xml").getFile()));
//		InputStream test = new FileInputStream(new File(classLoader.getResource("file.xml").getFile()));
		
		
//		InputStream isXsd1 = new FileInputStream(new File(classLoader.getResource("SalaryDeclarationConsumerServiceTypes.xsd").getFile()));
		InputStream isXsd2 = new FileInputStream(new File(classLoader.getResource("SalaryDeclarationServiceTypes.xsd").getFile()));
//		InputStream isXsd3 = new FileInputStream(new File(classLoader.getResource("SalaryDeclaration.xsd").getFile()));
		
//		System.out.println(validateAgainstXSD(isXml, isXsd2));
		Assert.assertTrue(validateAgainstXSD(isXml2, isXsd2));
		isXsd2.close();
//		System.out.println(validateAgainstXSD(test, isXsd2));
	}

	static boolean validateAgainstXSD(InputStream xml, InputStream... xsd) {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			factory.setResourceResolver(new ResourceResolver("./"));
			Source[] sources = new Source[xsd.length];
			for(int i = 0; i < xsd.length; i++) {
				sources[i] = new StreamSource(xsd[i]);
			}
			
			Schema schema = factory.newSchema(sources);
			Validator validator = schema.newValidator();
			StreamSource streamSource = new StreamSource(xml);
			validator.validate(streamSource);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
