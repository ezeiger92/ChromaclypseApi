package com.chromaclypse.api.config.visitor;

import com.chromaclypse.api.config.ConfigAction;

public class PrintableVisitor implements ConfigVisitor {
	private static String maxIndent = "                                ";
	private StringBuilder string = new StringBuilder();
	
	@Override
	public void branch(String propName, Object propVal, Object configVal, int depth) {
		string.append('\n');
		string.append(getIndent(depth));
		string.append(propName);
		string.append(':');
	}

	@Override
	public void leaf(String propName, Object propVal, Object configVal, int depth) {
		String indent = getIndent(depth);
		string.append('\n');
		string.append(indent);
		string.append(propName);
		string.append(": ");
		string.append(propVal.toString().replaceAll("\n", "\n" + indent));
	}
	
	@Override
	public ConfigAction getAction() {
		return ConfigAction.NONE;
	}
	
	private static String getIndent(int depth) {
		depth = Math.min(depth * 4, maxIndent.length());
		return maxIndent.substring(0, depth);
	}

	@Override
	public String toString() {
		return string.toString().replace("::", "");
	}
}
