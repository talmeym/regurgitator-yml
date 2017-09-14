package com.emarte.regurgitator.core;

import java.util.*;

public class Yaml {
    private String type;
    private Map values;

    public Yaml(Map map) {
        type = (String) map.keySet().iterator().next();
        values = map.get(type) instanceof Map ? (Map) map.get(type) : new HashMap();
    }

    public Yaml(String type, Map values) {
        this.type = type;
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public Map getValues() {
        return values;
    }
}
