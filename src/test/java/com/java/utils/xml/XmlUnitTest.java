package com.java.utils.xml;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.xmlunit.matchers.CompareMatcher.*;

import java.util.Iterator;

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

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
}
