package com.hqy.mdf.log.logback.rule;

/**
 * @author hqy
 */
public interface DesensitizeRule {

    boolean isMatch(String key, String value);

    boolean isMatch(String msg);

    String desensitize(String value);
}
