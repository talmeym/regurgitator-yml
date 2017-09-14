package com.emarte.regurgitator.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.EntityLookup.conditionBehaviour;
import static com.emarte.regurgitator.core.EntityLookup.hasConditionBehaviour;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;
import static java.util.Map.Entry;

public class ConditionYmlLoader {
	private static final Log log = getLog(ConditionYmlLoader.class);
	private static final YmlLoaderUtil<YmlLoader<ConditionBehaviour>> conditionBehaviourLoaderUtil = new YmlLoaderUtil<YmlLoader<ConditionBehaviour>>();

	public static Condition load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
		String source = loadMandatoryStr(yaml, SOURCE);
		String expectation = loadOptionalStr(yaml, EXPECTATION);

		Entry behaviourAttr = getBehaviourAttribute(yaml);
		ConditionBehaviour behaviour;
		String value;

		if(behaviourAttr != null) {
			behaviour = conditionBehaviour((String)behaviourAttr.getKey());
			value = (String) behaviourAttr.getValue();
		} else {
		    Object object = loadMandatory(yaml, BEHAVIOUR);
			value = loadMandatoryStr(yaml, VALUE);

			if(object instanceof String) {
				behaviour = conditionBehaviour((String) object);
			} else {
				Yaml behaviourYaml = new Yaml((Map) object);
				behaviour = conditionBehaviourLoaderUtil.deriveLoader(behaviourYaml).load(behaviourYaml, allIds);
			}
		}

		String id = loadId(yaml, CONDITION, allIds);
		log.debug("Loaded condition '" + id + "'");
		return new Condition(id, new ContextLocation(source), value, expectation != null ? Boolean.valueOf(expectation) : true, behaviour);
	}

	private static Entry getBehaviourAttribute(Yaml yaml) throws RegurgitatorException {
		boolean behaviourFieldFound = yaml.getValues().containsKey(BEHAVIOUR);
		Set<Entry> entries = yaml.getValues().entrySet();
		Set<Entry> behavioursFound = new HashSet<Entry>();

		for(Entry entry: entries) {
			if(entry.getValue() instanceof String) {
				if(hasConditionBehaviour((String)entry.getKey())) {
					behavioursFound.add(entry);
				}
			}
		}

		if(behavioursFound.size() == 0 && !behaviourFieldFound) {
			throw new RegurgitatorException("no valid condition behaviour is defined");
		}

		if(behavioursFound.size() > 0) {
			if(behavioursFound.size() > 1 || behaviourFieldFound) {
				throw new RegurgitatorException("more than one valid condition behaviour was found");
			}

			return behavioursFound.iterator().next();
		}

		return null;
	}
}
