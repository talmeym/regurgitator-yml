package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.AS_INDEX;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalBoolean;

public class SizeProcessorYmlLoader implements YmlLoader<SizeProcessor> {
	private static final Log log = getLog(SizeProcessorYmlLoader.class);

	@Override
	public SizeProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		boolean lastIndex = loadOptionalBoolean(yaml, AS_INDEX);

		log.debug("Loaded size processor");
		return new SizeProcessor(lastIndex);
	}
}
