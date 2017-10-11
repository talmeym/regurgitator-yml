/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.*;

import static java.util.Collections.unmodifiableMap;

public class Yaml {
    private final String type;
    private final Map values;

    public Yaml(Map map) {
        type = (String) map.keySet().iterator().next();
        values = map.get(type) instanceof Map ? unmodifiableMap((Map) map.get(type)) : new HashMap();
    }

    public Yaml(String type, Map values) {
        this.type = type;
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public Map getValueMap() {
        return values;
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Object get(String key) {
        return values.get(key);
    }
}
