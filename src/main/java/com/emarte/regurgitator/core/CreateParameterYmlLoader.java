/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.List;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;

public class CreateParameterYmlLoader extends CreateParameterLoader implements YmlLoader<Step> {
    private static final Log log = getLog(CreateParameterYmlLoader.class);

    @Override
    public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String source = loadOptionalStr(yaml, SOURCE);
        String value = loadOptionalStr(yaml, VALUE);
        String file = loadOptionalStr(yaml, FILE);
        List<ValueProcessor> processors = loadOptionalValueProcessors(yaml, allIds);
        return buildCreateParameter(loadId(yaml, allIds), loadPrototype(yaml), loadContext(yaml), source, value, file, processors, log);
    }
}
