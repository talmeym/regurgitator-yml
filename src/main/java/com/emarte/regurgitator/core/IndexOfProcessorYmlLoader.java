package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalBoolean;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class IndexOfProcessorYmlLoader implements YmlLoader<IndexOfProcessor> {
	private static final Log log = getLog(IndexOfProcessorYmlLoader.class);

	@Override
	public IndexOfProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		String source = loadOptionalStr(yaml, SOURCE);
		String value = loadOptionalStr(yaml, VALUE);
		boolean backwards = loadOptionalBoolean(yaml, LAST);
		log.debug("Loaded index of processor");
		return new IndexOfProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value), backwards);
	}
}
