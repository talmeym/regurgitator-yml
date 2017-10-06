/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.core;

public class CoreYmlEntityPack extends AbstractEntityPack {
    public CoreYmlEntityPack() {
        addConfigurationLoader("yml", new YmlConfigurationLoader());
    }
}
