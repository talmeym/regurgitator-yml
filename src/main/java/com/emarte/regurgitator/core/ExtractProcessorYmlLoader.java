package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.FORMAT;
import static com.emarte.regurgitator.core.CoreConfigConstants.INDEX;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadMandatoryStr;

public class ExtractProcessorYmlLoader implements YmlLoader<ExtractProcessor> {
    private static final Log log = getLog(ExtractProcessorYmlLoader.class);

    @Override
    public ExtractProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        log.debug("Loaded extract processor");
        return new ExtractProcessor(loadMandatoryStr(yaml, FORMAT), Integer.parseInt(loadMandatoryStr(yaml, INDEX)));
    }
}
