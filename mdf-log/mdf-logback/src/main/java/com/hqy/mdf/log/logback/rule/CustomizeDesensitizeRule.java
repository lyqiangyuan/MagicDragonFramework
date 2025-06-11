package com.hqy.mdf.log.logback.rule;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author hqy
 */
@Data
public class CustomizeDesensitizeRule extends AbstractDesensitizeRule {

    /**
     * 匹配的key
     */
    protected List<String> keyList;

    /**
     * 支持 "xxx,yyy" 格式的 keys 属性, ‘,’分割
     */
    private String keys;

    /**
     * 支持 "3,4" 格式的 position 属性, ‘,’分割
     */
    private String position;

    // 支持 "1,2" 格式的 position 属性
    public void setPosition(String position) {
        if (position != null) {
            String[] parts = position.split(",");
            if (parts.length == 2) {
                this.preLen = Integer.parseInt(parts[0].trim());
                this.sufLen = Integer.parseInt(parts[1].trim());
            }
        }
        this.position = position;
    }

    // 支持 "password,secret" 格式的 keys 属性
    public void setKeys(String keys) {
        if (keys != null) {
            this.keyList = Arrays.asList(keys.split(","));
        }
        this.keys = keys;
    }


    @Override
    public boolean isMatch(String key, String value) {
        if (keyList != null && keyList.contains(key)
                && (pattern == null || pattern.matcher(value).matches())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMatch(String msg) {
        throw  new UnsupportedOperationException("Not supported yet.");
    }

}
