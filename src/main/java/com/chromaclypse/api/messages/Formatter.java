package com.chromaclypse.api.messages;

import java.util.List;

public interface Formatter {
	
	default boolean isColor(String input) {
		return isColor(input, 0);
	}

	default String round(double value, int places) {
		double factor = Math.pow(10, places);

		long full = (long)Math.round(value * factor);
		String string = String.valueOf(full);

		if(places < 0) {
			full *= (long)factor;
		}

		if(places > 0) {
			int length = string.length();

			String tail = "00";
			String head = "0";

			if(length >= places) {
				tail = string.substring(length - places);
				head = string.substring(0, length - places);
				
				if(head.isEmpty()) {
					head = "0";
				}

				if(tail.isEmpty()) {
					tail = "00";
				}
				else if(tail.length() == 1) {
					tail += "0";
				}
			}

			return head + '.' + tail;
		}
		else {
			return string;
		}
	}

	default String commas(double value, int places) {
		StringBuilder builder = new StringBuilder(round(value, places)).reverse();
		
		for(int i = Math.max(places, 0) + 4; i < builder.length(); i += 4) {
			builder.insert(i, ',');
		}
		
		return builder.reverse().toString();
	}
	
	default String commas(long value) {
		StringBuilder builder = new StringBuilder(String.valueOf(value)).reverse();

		for(int i = 3; i < builder.length(); i += 4) {
			builder.insert(i, ',');
		}

		return builder.reverse().toString();
	}
	
	boolean isColor(String input, int index);
	
	String colorize(String input);
	String colorize(String... inputs);
	String[] colorizeList(String... inputs);
	
	String decolor(String input);
	String decolor(String... inputs);
	String[] decolorList(String... inputs);
	
	String niceName(String input);
	List<String> wrap(int max_length, String... input);
}
