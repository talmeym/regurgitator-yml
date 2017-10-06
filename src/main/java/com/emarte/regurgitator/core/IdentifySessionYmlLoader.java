/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadId;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class IdentifySessionYmlLoader extends IdentifySessionLoader implements YmlLoader<Step> {
    private static final Log log = getLog(IdentifySessionYmlLoader.class);

    @Override
    public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(yaml, allIds);
        String source = loadOptionalStr(yaml, SOURCE);
        String value = loadOptionalStr(yaml, VALUE);
        return buildIdentifySession(id, source, value, log);
    }
}
