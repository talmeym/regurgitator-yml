package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;

public class CreateResponseYmlLoader extends CreateResponseLoader implements YmlLoader<Step> {
    private static final Log log = getLog(CreateResponseYmlLoader.class);

	@Override
	public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		String source = loadOptionalStr(yaml, SOURCE);
		String value = loadOptionalStr(yaml, VALUE);
		String file = loadOptionalStr(yaml, FILE);
		ValueProcessor processor = loadOptionalValueProcessor(yaml, allIds);
		return buildCreateResponse(loadId(yaml, allIds), source, value, file, processor, log);
	}
}
