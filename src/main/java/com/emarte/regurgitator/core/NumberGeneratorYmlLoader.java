package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.MAX;
import static com.emarte.regurgitator.core.Log.getLog;

public class NumberGeneratorYmlLoader implements YmlLoader<ValueGenerator> {
	private static final Log log = getLog(NumberGeneratorYmlLoader.class);

	@Override
	public NumberGenerator load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		log.debug("Loaded number generator");
		return new NumberGenerator(YmlConfigUtil.loadOptionalInt(yaml, MAX));
	}
}
