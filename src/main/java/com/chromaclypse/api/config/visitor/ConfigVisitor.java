package com.chromaclypse.api.config.visitor;

import com.chromaclypse.api.config.ConfigAction;

public interface ConfigVisitor {
	void branch(String propName, Object propVal, Object configVal, int depth);
	void leaf(String propName, Object propVal, Object configVal, int depth);
	ConfigAction getAction();
}
