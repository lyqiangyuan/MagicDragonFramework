package com.hqy.mdf.log;

import com.hqy.mdf.log.rule.CustomizeDesensitizeRule;
import com.hqy.mdf.log.rule.GlobalDesensitizeRule;
import lombok.Data;

import java.util.List;

/**
 * @author hqy
 */
@Data
public class LogDesensitizeProperties {
    /**
     * 脱敏开关
     */
    private boolean enable;

    /**
     * 是否添加默认全局兜底规则
     */
    private boolean addDefaultGlobalRules = true;

    /**
     * 自定义规则
     */
    private List<CustomizeDesensitizeRule> customizeRules;

    /**
     * 全局兜底规则
     */
    private List<GlobalDesensitizeRule> globalRules;


}
