package com.emarte.regurgitator.core;

import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;

import static com.emarte.regurgitator.core.YmlConfigUtil.getYaml;

public class YmlConfigurationLoader implements ConfigurationLoader {
    private static YmlLoaderUtil<YmlLoader<Step>> loaderUtil = new YmlLoaderUtil<YmlLoader<Step>>();

    @Override
    public Step load(InputStream input) throws RegurgitatorException {
        YamlReader reader = new YamlReader(new InputStreamReader(input));

        try {
            Yaml yaml = getYaml((Map) reader.read());
            return loaderUtil.deriveLoader(yaml).load(yaml, new HashSet<Object>());
        } catch (Exception e) {
            throw new RegurgitatorException("Error loading regurgitator configuration", e);
        }
    }
}
