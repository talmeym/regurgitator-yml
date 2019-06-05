/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.INDEX_SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE_SOURCE;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadMandatoryStr;

public class SetAtIndexProcessorYmlLoader implements YmlLoader<SetAtIndexProcessor> {
    private static final Log log = Log.getLog(SetAtIndexProcessorYmlLoader.class);

    @Override
    public SetAtIndexProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String indexStr = loadMandatoryStr(yaml, INDEX_SOURCE);
        String valueStr = loadMandatoryStr(yaml, VALUE_SOURCE);
        log.debug("Loaded set at processor");
        return new SetAtIndexProcessor(new ValueSource(new ContextLocation(indexStr), null), new ValueSource(new ContextLocation(valueStr), null));
    }
}
