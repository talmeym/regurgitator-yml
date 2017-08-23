package com.emarte.regurgitator.core;

import java.util.Set;

public interface YmlLoader<TYPE> extends Loader {
	TYPE load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException;
}
