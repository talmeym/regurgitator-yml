/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.*;

import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.*;

public class YmlConfigUtil {
    private static final YmlLoaderUtil<YmlLoader<ValueProcessor>> processorLoaderUtil = new YmlLoaderUtil<YmlLoader<ValueProcessor>>();
    private static final Random RANDOM = new Random();

    public static String loadId(Yaml yaml, Set<Object> ids) throws RegurgitatorException {
        Map values = yaml.getValues();
        String id = values.containsKey(ID) ? (String) values.get(ID) : yaml.getType() + "-" + new Random().nextInt(100000);

        if (!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static String loadId(Yaml inner, String outerName, Set<Object> ids) throws RegurgitatorException {
        Map values = inner.getValues();
        String id = values.containsKey(ID) ? (String) values.get(ID) : outerName + "-" + RANDOM.nextInt(100000);

        if (!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static Object loadOptional(Yaml yaml, String key) {
        return yaml.getValues().containsKey(key) ? yaml.getValues().get(key) : null;
    }

    public static String loadOptionalStr(Yaml yaml, String name) {
        Map values = yaml.getValues();
        return values.containsKey(name) ? (String) values.get(name) : null;
    }

    public static boolean loadOptionalBoolean(Yaml yaml, String key) {
        Map values = yaml.getValues();
        return values.containsKey(key) && ((String)values.get(key)).toLowerCase().equals("true");
    }

    public static Object loadMandatory(Yaml yaml, String key) throws RegurgitatorException {
        Map values = yaml.getValues();

        if (values.containsKey(key)) {
            return values.get(key);
        }

        throw new RegurgitatorException("Yml missing mandatory element: " + key);    }

    public static ValueProcessor loadOptionalValueProcessor(Yaml yaml, Set<Object> ids) throws RegurgitatorException {
        Map values = yaml.getValues();
        Object processorObj = values.get(PROCESSOR);

        if(processorObj instanceof String) {
            return valueProcessor((String) processorObj);
        } else if (processorObj != null){
            Yaml processorYaml = new Yaml((Map) processorObj);
            return processorLoaderUtil.deriveLoader(processorYaml).load(processorYaml, ids);
        }

        return null;
    }

    public static ParameterPrototype loadPrototype(Yaml yaml) throws RegurgitatorException {
        return new ParameterPrototype(loadName(yaml), loadType(yaml), loadConflictPolicy(yaml));
    }

    private static String loadName(Yaml yaml) throws RegurgitatorException {
        return new ContextLocation(loadMandatoryStr(yaml, NAME)).getName();
    }

    public static String loadMandatoryStr(Yaml yaml, String key) throws RegurgitatorException {
        Map values = yaml.getValues();

        if(values.containsKey(key)) {
            return (String) values.get(key);
        }

        throw new RegurgitatorException("Yml missing mandatory element: " + key);
    }

    private static ParameterType loadType(Yaml yaml) throws RegurgitatorException {
        Map values = yaml.getValues();
        return values.containsKey(TYPE) ? parameterType((String) values.get(TYPE)) : STRING;
    }

    private static ConflictPolicy loadConflictPolicy(Yaml yaml) {
        Map values = yaml.getValues();
        return values.containsKey(MERGE) ? ConflictPolicy.valueOf((String)values.get(MERGE)) : REPLACE;
    }

    public static String loadContext(Yaml yaml) {
        return new ContextLocation((String) yaml.getValues().get(NAME)).getContext();
    }

    public static Integer loadOptionalInt(Yaml yaml, String name) {
        Map values = yaml.getValues();
        return values.containsKey(name) ? Integer.parseInt((String) values.get(name)) : null;
    }
}
