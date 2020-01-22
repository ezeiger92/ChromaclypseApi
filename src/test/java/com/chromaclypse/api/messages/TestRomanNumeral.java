package com.chromaclypse.api.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestRomanNumeral {
	
	private void testConvert(int expected, String input) {
		assertEquals(expected, RomanNumeral.parseInt(input));
		assertEquals(expected, new RomanNumeral(input).intValue());
	}
	
	private void testConvert(String expected, int input) {
		assertEquals(expected, RomanNumeral.valueOf(input));
		assertEquals(expected, new RomanNumeral(input).toString());
	}

	@DisplayName("Invalid input throws")
	@Test
	public void invalidInputThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			RomanNumeral.parseInt("Java");
		});
	}

	@DisplayName("Invalid numeric values throws")
	@Test
	public void invalidNumbersThrow() {
		assertThrows(IllegalArgumentException.class, () -> {
			RomanNumeral.parseInt("MMMMM");
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			RomanNumeral.valueOf(-12);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			RomanNumeral.valueOf(9001);
		});
	}

	@DisplayName("Case insensitive")
	@Test
	public void caseInsensitive() {
		assertEquals(4, RomanNumeral.parseInt("iv"));
	}
	
	
	@DisplayName("Basic numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@CsvFileSource(resources = "/TestRomanNumeralSource/BasicNumerals.csv")
	public void basicNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Basic compound numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@CsvFileSource(resources = "/TestRomanNumeralSource/BasicCompoundNumerals.csv")
	public void basicCompoundNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Subtractive numerals")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@CsvFileSource(resources = "/TestRomanNumeralSource/SubtractiveNumerals.csv")
	public void subtractiveNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Bute force (all numerals)")
	@ParameterizedTest(name = "\"{1}\" converts to {0}")
	@CsvFileSource(resources = "/TestRomanNumeralSource/AllNumerals.csv")
	public void allNumerals(int expected, String input) {
		testConvert(expected, input);
	}
	
	@DisplayName("Bute force (all values)")
	@ParameterizedTest(name = "{0} converts to \"{1}\"")
	@CsvFileSource(resources = "/TestRomanNumeralSource/AllNumerals.csv")
	public void allValues(int input, String expected) {
		testConvert(expected, input);
	}
}
