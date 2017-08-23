package com.emarte.regurgitator.core;

public class CoreYmlEntityPack extends AbstractEntityPack {
    public CoreYmlEntityPack() {
        addConfigurationLoader("yml", new YmlConfigurationLoader());
    }
}
