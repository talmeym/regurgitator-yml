/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.Log.getLog;

public class UuidGeneratorYmlLoader implements YmlLoader<ValueGenerator> {
    private static final Log log = getLog(UuidGeneratorYmlLoader.class);

    @Override
    public UuidGenerator load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded uuid generator");
        return new UuidGenerator();
    }
}
