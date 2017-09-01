package com.emarte.regurgitator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.CONDITION;
import static com.emarte.regurgitator.core.CoreConfigConstants.RULE;
import static com.emarte.regurgitator.core.CoreConfigConstants.STEP;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigConstants.CONDITIONS;
import static com.emarte.regurgitator.core.YmlConfigUtil.*;

public class RuleYmlLoader {
    private static final Log log = getLog(RuleYmlLoader.class);

	public static Rule load(Yaml yaml, Set<Object> stepIds, Set<Object> allIds) throws RegurgitatorException {
		List<Condition> conditions = new ArrayList<Condition>();

		List conditionYamls = (List) yaml.getValues().get(CONDITIONS);

		if(conditionYamls != null) {
			for (Object obj : conditionYamls) {
				Yaml conditionYaml = new Yaml(CONDITION, (Map) obj);
				conditions.add(ConditionYmlLoader.load(conditionYaml, allIds));
			}
		}

		String stepId = loadMandatoryStr(yaml, STEP);

		if(!stepIds.contains(stepId)) {
			throw new RegurgitatorException("Error with configuration: rule step not found: " + stepId);
		}

		String id = loadId(yaml, RULE, allIds);
		log.debug("Loaded rule '" + id + "'");
		return new Rule(id, conditions, stepId);
	}
}
