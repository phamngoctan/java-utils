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
}
