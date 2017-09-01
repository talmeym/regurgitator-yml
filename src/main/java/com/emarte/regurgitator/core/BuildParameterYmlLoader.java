package com.emarte.regurgitator.core;

import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.BUILDER;
import static com.emarte.regurgitator.core.EntityLookup.valueBuilder;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadId;

public class BuildParameterYmlLoader implements YmlLoader<Step> {
    private static final Log log = getLog(BuildParameterYmlLoader.class);
    private static final YmlLoaderUtil<YmlLoader<ValueBuilder>> builderLoaderUtil = new YmlLoaderUtil<YmlLoader<ValueBuilder>>();

	@Override
	public Step load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		Object builderObj = YmlConfigUtil.loadMandatory(yaml, BUILDER);
        ValueBuilder valueBuilder;

        if(builderObj instanceof String) {
            valueBuilder = valueBuilder((String) builderObj);
        } else {
            Yaml builderYaml = YmlConfigUtil.getYaml((Map) builderObj);
            valueBuilder = builderLoaderUtil.deriveLoader(builderYaml).load(builderYaml, allIds);
        }

		ValueProcessor processor = YmlConfigUtil.loadOptionalValueProcessor(yaml, allIds);
		String id = loadId(yaml, allIds);
		log.debug("Loaded build parameter '" + id + '\'');
		return new BuildParameter(id, YmlConfigUtil.loadPrototype(yaml), YmlConfigUtil.loadContext(yaml), valueBuilder, processor);
	}
}
