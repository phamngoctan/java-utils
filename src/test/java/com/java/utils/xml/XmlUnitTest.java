package com.java.utils.xml;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.ValidationProblem;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

public class XmlUnitTest {

	@Test
	public void given2XMLS_whenIdentical_thenCorrect() {
		String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
		String testXml = "<struct><int>3</int><boolean>false</boolean></struct>";
		assertThat(testXml, isIdenticalTo(controlXml));
	}

	@Test
	public void given2XMLSWithSimilarNodesButDifferentSequence_whenNotIdentical_thenCorrect() {
		String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
		String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";
		assertThat(testXml, not(isIdenticalTo(controlXml)));
	}

	@Test
	public void given2XMLS_whenGeneratesDifferences_thenCorrect() {
		String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
		String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";
		Diff myDiff = DiffBuilder.compare(controlXml).withTest(testXml).build();

		Iterator<Difference> iter = myDiff.getDifferences().iterator();
		int size = 0;
		while (iter.hasNext()) {
			iter.next().toString();
			size++;
		}
		assertThat(size, greaterThan(1));
	}

	@Test
	public void given2XMLS_whenGeneratesOneDifference_thenCorrect() {
		String myControlXML = "<struct><int>3</int><boolean>false</boolean></struct>";
		String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";

		Diff myDiff = DiffBuilder.compare(myControlXML).withTest(myTestXML)
				.withComparisonController(ComparisonControllers.StopWhenDifferent).build();

		Iterator<Difference> iter = myDiff.getDifferences().iterator();
		int size = 0;
		while (iter.hasNext()) {
			iter.next().toString();
			size++;
		}
		assertThat(size, equalTo(1));
	}

	@Test
	public void given2XMLS_whenSimilar_thenCorrect() {
		String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
		String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";

		// assertThat(testXml, isSimilarTo(controlXml));
		// ==
		// assertThat(testXml, isSimilarTo(controlXml).withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.Default)));
		assertThat(testXml, isSimilarTo(controlXml).withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));
	}
	
	@Test
	public void give2Xmls_whenDiff_thenCorrect() {
		StringBuilder assertionResult = new StringBuilder();
		String controlXml = "<TaxSalaries>\r\n" + 
				"	<TaxSalary>\r\n" + 
				"		<Period>\r\n" + 
				"			<from>2013-01-01</from>\r\n" + 
				"			<until>2013-12-31</until>\r\n" + 
				"		</Period>\r\n" + 
				"		<CanteenLunchCheck/>\r\n" + 
				"		<Income>303350.00</Income>\r\n" + 
				"		<SporadicBenefits>\r\n" + 
				"			<Text>Sonderzulage Geburtszulage</Text>\r\n" + 
				"			<Sum>41000.00</Sum>\r\n" + 
				"		</SporadicBenefits>\r\n" + 
				"		<CapitalPayment>\r\n" + 
				"			<Text>Kapitalleistung mit Vorsorgecharakter</Text>\r\n" + 
				"			<Sum>45000.00</Sum>\r\n" + 
				"		</CapitalPayment>\r\n" + 
				"		<GrossIncome>389350.00</GrossIncome>\r\n" + 
				"		<AHV-ALV-NBUV-AVS-AC-AANP-Contribution>19944.00</AHV-ALV-NBUV-AVS-AC-AANP-Contribution>\r\n" + 
				"		<BVG-LPP-Contribution>\r\n" + 
				"			<Regular>21600.00</Regular>\r\n" + 
				"			<Purchase>10000.00</Purchase>\r\n" + 
				"		</BVG-LPP-Contribution>\r\n" + 
				"		<NetIncome>337806.00</NetIncome>\r\n" + 
				"		<ChargesRule>\r\n" + 
				"			<WithRegulation>\r\n" + 
				"				<Allowed>2011-04-15</Allowed>\r\n" + 
				"				<Canton>LU</Canton>\r\n" + 
				"			</WithRegulation>\r\n" + 
				"		</ChargesRule>\r\n" + 
				"		<OtherFringeBenefits>Vergünstigung Versicherungsprämien</OtherFringeBenefits>\r\n" + 
				"		<StandardRemark>\r\n" + 
				"			<RelocationCosts>2000.00</RelocationCosts>\r\n" + 
				"			<PrivatePartCompanyCar>\r\n" + 
				"				<Allowed>2010-10-12</Allowed>\r\n" + 
				"				<Canton>LU</Canton>\r\n" + 
				"			</PrivatePartCompanyCar>\r\n" + 
				"			<MinimalEmployeeCarPartPercentage/>\r\n" + 
				"		</StandardRemark>\r\n" + 
				"	</TaxSalary>\r\n" + 
				"</TaxSalaries>\r\n" + 
				"";
		String testXml = "<TaxSalaries>\r\n" + 
				"	<TaxSalary>\r\n" + 
				"		<Period>\r\n" + 
				"			<from>2013-01-01</from>\r\n" + 
				"			<until>2013-12-31</until>\r\n" + 
				"		</Period>\r\n" + 
				"		<Income>303350.00</Income>\r\n" + 
				"		<FringeBenefits/>\r\n" + 
				"		<SporadicBenefits>\r\n" + 
				"			<Text>BIRTH_ALLOWANCE, SPECIAL_ALLOWANCE</Text>\r\n" + 
				"			<Sum>41000.00</Sum>\r\n" + 
				"		</SporadicBenefits>\r\n" + 
				"		<CapitalPayment>\r\n" + 
				"			<Text>CAPITAL_PERFORMANCE_WITH_PREVENTIVE_CHARACTER</Text>\r\n" + 
				"			<Sum>45000.00</Sum>\r\n" + 
				"		</CapitalPayment>\r\n" + 
				"		<GrossIncome>389350.00</GrossIncome>\r\n" + 
				"		<AHV-ALV-NBUV-AVS-AC-AANP-Contribution>19944.00</AHV-ALV-NBUV-AVS-AC-AANP-Contribution>\r\n" + 
				"		<BVG-LPP-Contribution>\r\n" + 
				"			<Regular>21600.00</Regular>\r\n" + 
				"			<Purchase>10000.00</Purchase>\r\n" + 
				"		</BVG-LPP-Contribution>\r\n" + 
				"		<NetIncome>337806.00</NetIncome>\r\n" + 
				"	</TaxSalary>\r\n" + 
				"</TaxSalaries>\r\n" + 
				"";
		Diff myDiffSimilar = DiffBuilder.compare(controlXml).withTest(testXml)
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
				.checkForSimilar()
				.ignoreWhitespace()
				.build();
		Iterator<Difference> iter = myDiffSimilar.getDifferences().iterator();
		int assertionErrorCount = 0;
	    while (iter.hasNext()) {
	    	assertionErrorCount++;
	    	assertionResult.append("\nNo " + assertionErrorCount + ":" + iter.next().toString());
	    }
	    System.out.println(assertionResult.toString());
	    assertFalse(assertionResult.toString(), assertionResult.length() == 0);
	}
	
	@Test
	public void test_makeSureXmlFileFollowXsd() {
		Validator xmlUnitValidator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
		xmlUnitValidator.setSchemaSources(
				Input.fromFile(this.getClass().getClassLoader()
						.getResource("SalaryDeclarationServiceTypes.xsd").getFile()).build(),
				Input.fromFile(this.getClass().getClassLoader()
						.getResource("SalaryDeclaration.xsd").getFile()).build(),
				Input.fromFile(this.getClass().getClassLoader()
						.getResource("SalaryDeclarationContainer.xsd").getFile()).build());
		StringBuilder assertionResult = new StringBuilder();
		int assertionErrorCount = 0;
		 ValidationResult validationResults =
		 xmlUnitValidator.validateInstance(Input.fromFile(this.getClass().getClassLoader().getResource("correct_xml_file.xml").getFile()).build());
//		ValidationResult validationResults = xmlUnitValidator.validateInstance(Input.fromByteArray(xmlString.getBytes()).build());
	    Iterator<ValidationProblem> problems = validationResults.getProblems().iterator();
	    while (problems.hasNext()) {
	    	assertionErrorCount++;
	        assertionResult.append("\nNo ").append(assertionErrorCount).append(": ").append(problems.next().toString());
	    }
	    assertTrue(assertionResult.toString(), assertionResult.length() == 0);
	}
	
	@Test
	public void test_makeSureDataInReferenceAttribute_isCorrect()
			throws ParserConfigurationException, SAXException, IOException {
		String expression = "";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document fullXmlDocumentForReferenceFile = builder.parse(new File(this.getClass().getClassLoader().getResource("correct_xml_file.xml").getFile()));
		Document fullXmlDocumentForExportedFile = builder.parse(new File(this.getClass().getClassLoader().getResource("xmlWithReferenceAttributeInsideTestFile.xml").getFile()));

		String partialXmlDocumentForReference = " <AHV-AVS-Salaries>\r\n" + 
				"                        <AHV-AVS-Salary institutionIDRef=\"#AHV1\">\r\n" + 
				"                            <AccountingTime>\r\n" + 
				"                                <from>2013-01-01</from>\r\n" + 
				"                                <until>2013-12-31</until>\r\n" + 
				"                            </AccountingTime>\r\n" + 
				"                            <AHV-AVS-Income>342000.00</AHV-AVS-Income>\r\n" + 
				"                            <ALV-AC-Income>126000.00</ALV-AC-Income>\r\n" + 
				"                            <ALVZ-ACS-Income>189000.00</ALVZ-ACS-Income>\r\n" + 
				"                            <ALV-AC-Open>27000.00</ALV-AC-Open>\r\n" + 
				"                        </AHV-AVS-Salary>\r\n" + 
				"                    </AHV-AVS-Salaries>";
		Document partialXmlDocumentForReferenceFileDoc = builder.parse(new ByteArrayInputStream(partialXmlDocumentForReference.getBytes()));
		String partialXmlDocumentForExported = "<AHV-AVS-Salaries>\r\n" + 
				"                        <AHV-AVS-Salary institutionIDRef=\"#AHV-AVS\">\r\n" + 
				"                            <AccountingTime>\r\n" + 
				"                                <from>2013-01-01</from>\r\n" + 
				"                                <until>2013-12-31</until>\r\n" + 
				"                            </AccountingTime>\r\n" + 
				"                            <AHV-AVS-Income>342000.00</AHV-AVS-Income>\r\n" + 
				"                            <ALV-AC-Income>126000.00</ALV-AC-Income>\r\n" + 
				"                            <ALVZ-ACS-Income>189000.00</ALVZ-ACS-Income>\r\n" + 
				"                            <ALV-AC-Open>27000.00</ALV-AC-Open>\r\n" + 
				"                        </AHV-AVS-Salary>\r\n" + 
				"                    </AHV-AVS-Salaries>";
		Document partialXmlDocumentForExportedFileDoc = builder.parse(new ByteArrayInputStream(partialXmlDocumentForExported .getBytes()));
		
		Diff myDiff = DiffBuilder.compare(partialXmlDocumentForReferenceFileDoc).withTest(partialXmlDocumentForExportedFileDoc)
				.withDifferenceEvaluator(new ConsiderAttributeDifferenceEvaluator("institutionIDRef", fullXmlDocumentForReferenceFile, fullXmlDocumentForExportedFile))
				.checkForSimilar().ignoreWhitespace().build();

		StringBuilder assertionResult = new StringBuilder();
		int assertionErrorCount = 0;
		Iterator<Difference> iter = myDiff.getDifferences().iterator();
		while (iter.hasNext()) {
			assertionErrorCount++;
			assertionResult.append("\nNo ").append(assertionErrorCount).append(": ").append(expression).append(" ")
					.append(iter.next().toString());
		}
		assertTrue(assertionResult.toString(), assertionResult.length() == 0);
	}
	
}
