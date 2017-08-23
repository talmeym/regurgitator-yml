package com.emarte.regurgitator.core;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.ID;

public class YmlConfigUtil {
    public static Yaml getYaml(Map map) {
        String type = (String) map.keySet().iterator().next();
        return new Yaml(type, (Map) map.get(type));
    }

    public static String loadId(Yaml yaml, Set<Object> ids) throws RegurgitatorException {
        Map values = yaml.getValues();
        String id = values.containsKey(ID) ? (String) values.get(ID) : yaml.getType() + "-" + new Random().nextInt(100000);

        if (!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static String loadOptionalStr(Map values, String name) {
        return values.containsKey(name) ? (String) values.get(name) : null;
    }
}
