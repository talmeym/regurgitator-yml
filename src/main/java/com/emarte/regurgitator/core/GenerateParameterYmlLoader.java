/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.GENERATOR;
import static com.emarte.regurgitator.core.EntityLookup.valueGenerator;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;

public class GenerateParameterYmlLoader implements YmlLoader<Step> {
    private static final Log log = getLog(GenerateParameterYmlLoader.class);
    private static final YmlLoaderUtil<YmlLoader<ValueGenerator>> generatorLoaderUtil = new YmlLoaderUtil<YmlLoader<ValueGenerator>>();

    @Override
    public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        Object generatorObj = loadMandatory(yaml, GENERATOR);
        ValueGenerator generator;

        if(generatorObj instanceof String) {
            generator = valueGenerator(stringify(generatorObj));
        } else {
            Yaml generatorYaml = new Yaml((Map) generatorObj);
            generator = generatorLoaderUtil.deriveLoader(generatorYaml).load(generatorYaml, allIds);
        }

        List<ValueProcessor> processors = loadOptionalValueProcessors(yaml, allIds);

        String id = loadId(yaml, allIds);
        log.debug("Loaded generate parameter '{}'", id);
        return new GenerateParameter(id, loadPrototype(yaml), loadContext(yaml), generator, processors);
    }
}
