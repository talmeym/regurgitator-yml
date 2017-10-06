/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.*;

public class Yaml {
    private final String type;
    private final Map values;

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
