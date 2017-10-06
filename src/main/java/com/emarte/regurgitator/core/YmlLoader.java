/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

import java.util.Set;

public interface YmlLoader<TYPE> extends Loader<Yaml, TYPE> {
    TYPE load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException;
}
