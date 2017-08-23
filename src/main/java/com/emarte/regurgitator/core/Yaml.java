package com.emarte.regurgitator.core;

import java.util.Map;

public class Yaml {
    private String type;
    private Map values;

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
