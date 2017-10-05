package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.CoreConfigConstants.SOURCE;
import static com.emarte.regurgitator.core.CoreConfigConstants.VALUE;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class IndexProcessorYmlLoader implements YmlLoader<IndexProcessor> {
    private static final Log log = getLog(IndexProcessorYmlLoader.class);

    @Override
    public IndexProcessor load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String source = loadOptionalStr(yaml, SOURCE);
        String value = loadOptionalStr(yaml, VALUE);
        log.debug("Loaded index processor");
        return new IndexProcessor(new ValueSource(source != null ? new ContextLocation(source) : null, value));
    }
}
