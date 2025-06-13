package com.hqy.mdf.log.rule;

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
    private boolean open;

    /**
     * 是否添加默认全局兜底规则
     */
    private boolean addDefaultGlobalRules = true;

    /**
     * 自定义规则
     */
    private List<DesensitizeRule> customizeRules;

    /**
     * 全局兜底规则
     */
    private List<DesensitizeRule> globalRules;


}
