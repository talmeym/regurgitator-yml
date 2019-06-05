/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class AtIndexProcessorYmlLoader extends AtIndexProcessorLoader implements YmlLoader<AtIndexProcessor> {
    private static final Log log = getLog(AtIndexProcessorYmlLoader.class);

    @Override
    public AtIndexProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        return buildAtIndexProcessor(loadOptionalStr(yaml, SOURCE), loadOptionalStr(yaml, VALUE), log);
    }
}
