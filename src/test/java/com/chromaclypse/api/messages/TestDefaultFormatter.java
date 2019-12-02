package com.chromaclypse.api.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TestDefaultFormatter {
	public static class DFormatter implements Formatter {

		@Override
		public boolean isColor(String input, int index) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String colorize(String input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String colorize(String... inputs) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] colorizeList(String... inputs) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String decolor(String input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String decolor(String... inputs) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] decolorList(String... inputs) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String niceName(String input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> wrap(int max_length, String... input) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Test
	public void test() {
		Formatter f = new DFormatter();

		assertEquals("100", f.commas(100));
		assertEquals("1,000", f.commas(1000));
		assertEquals("10,000", f.commas(10000));
		assertEquals("100,000", f.commas(100000));
		assertEquals("1,000,000", f.commas(1000000));
	}
}
