package com.chromaclypse.api.messages;

import java.util.List;

public interface Formatter {
	String colorize(String input);
	String colorize(String... inputs);
	String[] colorizeList(String... inputs);
	
	String decolor(String input);
	String decolor(String... inputs);
	String[] decolorList(String... inputs);
	
	String niceName(String input);
	List<String> wrap(int max_length, String... input);
}
