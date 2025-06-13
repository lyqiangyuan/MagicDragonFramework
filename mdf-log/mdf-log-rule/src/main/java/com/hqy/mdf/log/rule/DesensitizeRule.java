package com.hqy.mdf.log.rule;

/**
 * @author hqy
 */
public interface DesensitizeRule {

    boolean match(String key, String value, String msg);

    String desensitize(String value);
}
