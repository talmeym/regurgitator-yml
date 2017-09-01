package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;
import static com.emarte.regurgitator.core.Log.getLog;

public class CreateParameterYmlLoader extends CreateParameterLoader implements YmlLoader<Step> {
	private static final Log log = getLog(CreateParameterYmlLoader.class);

	@Override
	public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		String source = loadOptionalStr(yaml, SOURCE);
		String value = loadOptionalStr(yaml, VALUE);
		String file = loadOptionalStr(yaml, FILE);
		ValueProcessor processor = loadOptionalValueProcessor(yaml, allIds);
		return buildCreateParameter(loadId(yaml, allIds), loadPrototype(yaml), loadContext(yaml), source, value, file, processor, log);
	}
}
