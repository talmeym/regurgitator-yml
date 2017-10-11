/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.*;

public class AbstractYmlPackageMap implements YmlPackageMap {
    private final Map<List<String>, String> PACKAGE_TYPES = new HashMap<List<String>, String>();

    protected void addPackageMapping(List<String> kinds, String pakkage) {
        PACKAGE_TYPES.put(kinds, pakkage);
    }

    @Override
    public final String getPackageForKind(String kind) {
        for(List<String> kinds: PACKAGE_TYPES.keySet()) {
            if(kinds.contains(kind)) {
                return PACKAGE_TYPES.get(kinds);
            }
        }

        return null;
    }
}
