package com.emarte.regurgitator.core;

import java.util.Set;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadId;
import static com.emarte.regurgitator.core.YmlConfigUtil.loadOptionalStr;

public class RecordMessageYmlLoader implements YmlLoader<RecordMessage> {
    private static final Log log = getLog(RecordMessageYmlLoader.class);

    @Override
    public RecordMessage load(Yaml yaml, Set<Object> allIds) throws RegurgitatorException {
        String id = loadId(yaml, allIds);
        String folderPath = loadOptionalStr(yaml, CoreConfigConstants.FOLDER);
        log.debug("Loaded record message '{}'", id);
        return new RecordMessage(id, folderPath);
    }
}
