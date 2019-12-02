package com.chromaclypse.api.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@TestMethodOrder(OrderAnnotation.class)
public class TestRomanNumeral {
	
	private void testConvert(int expected, String input) {
		assertEquals(expected, RomanNumeral.parseInt(input));
	}
	
	private void testConvert(String expected, int input) {
		assertEquals(expected, RomanNumeral.valueOf(input));
	}
	
	@DisplayName("Basic numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@Order(1)
	@CsvFileSource(resources = "/TestRomanNumeralSource/BasicNumerals.csv")
	public void basicNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Basic compound numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@Order(2)
	@CsvFileSource(resources = "/TestRomanNumeralSource/BasicCompoundNumerals.csv")
	public void basicCompoundNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Subtractive numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@Order(3)
	@CsvFileSource(resources = "/TestRomanNumeralSource/SubtractiveNumerals.csv")
	public void subtractiveNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Bute force (all numerals)")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@Order(4)
	@CsvFileSource(resources = "/TestRomanNumeralSource/AllNumerals.csv")
	public void allNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Bute force (all values)")
	@ParameterizedTest(name = "{0} converts to \"{1}\"")
	@Order(5)
	@CsvFileSource(resources = "/TestRomanNumeralSource/AllNumerals.csv")
	public void allValues(int input, String expected) {
		testConvert(expected, input);
	}
}
