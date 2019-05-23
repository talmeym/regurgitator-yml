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

    public static String loadId(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String id = yaml.contains(ID) ? (String) yaml.get(ID) : yaml.getType() + "-" + new Random().nextInt(100000);

        if (!allIds.add(id)) {
            throw new RegurgitatorException("Duplicate id: " + id);
        }

        return id;
    }

    public static String loadId(Yaml inner, String outerName, Set<Object> allIds) throws RegurgitatorException {
        String id = inner.contains(ID) ? (String) inner.get(ID) : outerName + "-" + RANDOM.nextInt(100000);

        if (!allIds.add(id)) {
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

    public static boolean loadOptionalBool(Yaml yaml, String key) {
        return yaml.contains(key) && ((String)yaml.get(key)).toLowerCase().equals("true");
    }

    public static Object loadMandatory(Yaml yaml, String key) throws RegurgitatorException {
        if (yaml.contains(key)) {
            return yaml.get(key);
        }

        throw new RegurgitatorException("Yml missing mandatory element: " + key);
    }

    public static List<ValueProcessor> loadMandatoryValueProcessors(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        List<ValueProcessor> processors = loadOptionalValueProcessors(yaml, allIds);

        if(processors != null && processors.size() > 0) {
            return processors;
        }

        throw new RegurgitatorException("element missing mandatory processor or processors");
    }

    public static List<ValueProcessor> loadOptionalValueProcessors(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        List<ValueProcessor> processors = new ArrayList<ValueProcessor>();
        Object processorObj = yaml.get(PROCESSOR);
        Object processorsObj = yaml.get(PROCESSORS);

        if(processorObj != null && processorsObj != null) {
            throw new RegurgitatorException("Only one of 'processor' or 'processors' is allowed");
        }

        if(processorObj != null) {
            if(processorObj instanceof String) {
                processors.add(valueProcessor((String) processorObj));
            } else if(processorObj instanceof Map) {
                Yaml processor = new Yaml((Map) processorObj);
                processors.add(processorLoaderUtil.deriveLoader(processor).load(processor, allIds));
            } else {
                throw new RegurgitatorException("'processor' should be a string or an object or, for an array, replaced with 'processors'");
            }
        }

        if(processorsObj != null) {
            if(processorsObj instanceof String) {
                for(String part: ((String)processorsObj).split(",")) {
                    processors.add(valueProcessor(part));
                }
            } else if (processorsObj instanceof List) {
                for (Object object : (List) processorsObj) {
                    Yaml processor = new Yaml((Map) object);
                    processors.add(processorLoaderUtil.deriveLoader(processor).load(processor, allIds));
                }
            } else {
                throw new RegurgitatorException("'processors' field should be an array, or replaced with 'processor'");
            }
        }

        return processors;
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

    public static String loadContext(Yaml yaml) throws RegurgitatorException {
        return new ContextLocation(loadMandatoryStr(yaml, NAME)).getContext();
    }

    public static Integer loadOptionalInt(Yaml yaml, String name) {
        return yaml.contains(name) ? Integer.parseInt((String) yaml.get(name)) : null;
    }

    public static Long loadOptionalLong(Yaml yaml, String name) {
        return yaml.contains(name) ? Long.parseLong((String) yaml.get(name)) : null;
    }
}
