/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.BUILDER;
import static com.emarte.regurgitator.core.EntityLookup.valueBuilder;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadId;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalValueProcessors;

public class BuildParameterYmlLoader implements YmlLoader<Step> {
    private static final Log log = getLog(BuildParameterYmlLoader.class);
    private static final YmlLoaderUtil<YmlLoader<ValueBuilder>> builderLoaderUtil = new YmlLoaderUtil<YmlLoader<ValueBuilder>>();

    @Override
    public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        Object builderObj = YmlConfigUtil.loadMandatory(yaml, BUILDER);
        ValueBuilder builder;

        if(builderObj instanceof String) {
            builder = valueBuilder((String) builderObj);
        } else {
            Yaml builderYaml = new Yaml((Map) builderObj);
            builder = builderLoaderUtil.deriveLoader(builderYaml).load(builderYaml, allIds);
        }

        List<ValueProcessor> processors = loadOptionalValueProcessors(yaml, allIds);

        String id = loadId(yaml, allIds);
        log.debug("Loaded build parameter '{}'", id);
        return new BuildParameter(id, YmlConfigUtil.loadPrototype(yaml), YmlConfigUtil.loadContext(yaml), builder, processors);
    }
}
