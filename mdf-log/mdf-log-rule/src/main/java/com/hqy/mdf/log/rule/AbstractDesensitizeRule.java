package com.hqy.mdf.log.rule;

import com.hqy.mdf.common.util.DesensitizationUtils;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author hqy
 */
@Data
public abstract class AbstractDesensitizeRule implements DesensitizeRule {

    /**
     * 正则匹配
     */
    protected Pattern pattern;

    /**
     * 值的正则表达式
     */
    protected String regex;

    /**
     * 前缀长度
     */
    protected int preLen;
    /**
     * 后缀长度
     */
    protected int sufLen;
    /**
     * 掩码字符
     */
    protected char mask = '*';

    public void setRegex(String regex) {
        if (regex != null && !regex.isEmpty()) {
            pattern = Pattern.compile(regex);
        }
        this.regex = regex;
    }

    @Override
    public String desensitize(String value) {
        if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value)) {
            return value;
        }
        return DesensitizationUtils.desensitization(value, preLen, sufLen, mask);

    }




}
