package com.chromaclypse.api.messages;

public class RomanNumeral {
	private final int value;
	
	public RomanNumeral(int numeralValue) {
		value = numeralValue;
	}
	
	public RomanNumeral(String numeralString) {
		value = parseInt(numeralString);
	}
	
	public int intValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return valueOf(value);
	}
	
	public static int parseInt(String numeralString) {
		final char[] characters = numeralString.toCharArray();
		int result = 0;
		int highestValue = 0;
		
		for(int i = characters.length - 1; i > -1; --i) {
			int numeralValue = lookupValue(characters[i]);
			
			if(numeralValue >= highestValue) {
				result += numeralValue;
				highestValue = numeralValue;
			}
			else {
				result -= numeralValue;
			}
		}
		
		rangeCheck(result, "Only numbers between 1 - 3999 can be expressed (input = '" + numeralString + "', output = " + result + ")");
		
		return result;
	}
	
	public static String valueOf(int numeralValue) {
		rangeCheck(numeralValue, "Only numbers between 1 - 3999 can be expressed (input = " + numeralValue + ")");
		
		int iOnes = numeralValue % 10;
		int iTens = (numeralValue / 10) % 10;
		int iHundreds = (numeralValue / 100) % 10;
		int iThousands = (numeralValue / 1000) % 10;
		
		return thousands[iThousands] + hundreds[iHundreds] + tens[iTens] + ones[iOnes];
	}
	
	private static final void rangeCheck(int value, String message) {
		if(value < 1 || value > 3999) {
			throw new IllegalArgumentException(message);
		}
	}
	
	private static final String[] ones = {
			"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"
	};
	
	private static final String[] tens = {
			"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"
	};
	
	private static final String[] hundreds = {
			"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"
	};
	
	private static final String[] thousands = {
			"", "M", "MM", "MMM"
	};
	
	private static int lookupValue(char numeral) {
		switch(numeral) {
		case 'I':
			return 1;
		case 'V':
			return 5;
		case 'X':
			return 10;
		case 'L':
			return 50;
		case 'C':
			return 100;
		case 'D':
			return 500;
		case 'M':
			return 1000;
		default:
			throw new IllegalArgumentException("Invalid numeral character: '" + numeral + "'");
		}
	}
}