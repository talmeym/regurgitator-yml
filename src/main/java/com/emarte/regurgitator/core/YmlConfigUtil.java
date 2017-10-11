/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.*;

import static com.emarte.regurgitator.core.ConflictPolicy.REPLACE;
import static com.emarte.regurgitator.core.CoreConfigConstants.*;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.EntityLookup.parameterType;
import static com.emarte.regurgitator.core.EntityLookup.valueProcessor;

public class YmlConfigUtil {
    private static final YmlLoaderUtil<YmlLoader<ValueProcessor>> processorLoaderUtil = new YmlLoaderUtil<YmlLoader<ValueProcessor>>();
    private static final Random RANDOM = new Random();

    public static String loadId(Yaml yaml, Set<Object> ids) throws RegurgitatorException {
        String id = yaml.contains(ID) ? (String) yaml.get(ID) : yaml.getType() + "-" + new Random().nextInt(100000);

        if (!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static String loadId(Yaml inner, String outerName, Set<Object> ids) throws RegurgitatorException {
        String id = inner.contains(ID) ? (String) inner.get(ID) : outerName + "-" + RANDOM.nextInt(100000);

        if (!ids.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static Object loadOptional(Yaml yaml, String key) {
        return yaml.contains(key) ? yaml.get(key) : null;
    }

    public static String loadOptionalStr(Yaml yaml, String name) {
        return yaml.contains(name) ? (String) yaml.get(name) : null;
    }

    public static boolean loadOptionalBoolean(Yaml yaml, String key) {
        return yaml.contains(key) && ((String)yaml.get(key)).toLowerCase().equals("true");
    }

    public static Object loadMandatory(Yaml yaml, String key) throws RegurgitatorException {
        if (yaml.contains(key)) {
            return yaml.get(key);
        }

        throw new RegurgitatorException("Yml missing mandatory element: " + key);    }

    public static ValueProcessor loadOptionalValueProcessor(Yaml yaml, Set<Object> ids) throws RegurgitatorException {
        Object processorObj = yaml.get(PROCESSOR);

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
        if(yaml.contains(key)) {
            return (String) yaml.get(key);
        }

        throw new RegurgitatorException("Yml missing mandatory element: " + key);
    }

    private static ParameterType loadType(Yaml yaml) throws RegurgitatorException {
        return yaml.contains(TYPE) ? parameterType((String) yaml.get(TYPE)) : STRING;
    }

    private static ConflictPolicy loadConflictPolicy(Yaml yaml) {
        return yaml.contains(MERGE) ? ConflictPolicy.valueOf((String)yaml.get(MERGE)) : REPLACE;
    }

    public static String loadContext(Yaml yaml) {
        return new ContextLocation((String) yaml.get(NAME)).getContext();
    }

    public static Integer loadOptionalInt(Yaml yaml, String name) {
        return yaml.contains(name) ? Integer.parseInt((String) yaml.get(name)) : null;
    }
}
