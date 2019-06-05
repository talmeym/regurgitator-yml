/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class RemoveAtIndexProcessorYmlLoader extends RemoveAtIndexProcessorBuilder implements YmlLoader<RemoveAtIndexProcessor> {
    private static final Log log = getLog(RemoveAtIndexProcessorYmlLoader.class);

    @Override
    public RemoveAtIndexProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        return buildRemoveAtIndexProcessor(loadOptionalStr(yaml, SOURCE), loadOptionalStr(yaml, VALUE), log);
    }
}
