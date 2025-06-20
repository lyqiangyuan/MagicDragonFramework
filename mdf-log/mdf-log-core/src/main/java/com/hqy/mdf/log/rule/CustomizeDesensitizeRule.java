package com.hqy.mdf.log.rule;

import com.hqy.mdf.log.MdfLogConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * @author hqy
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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
            String[] parts = position.split(MdfLogConstant.COMMA);
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
            this.keyList = Arrays.asList(keys.split(MdfLogConstant.COMMA));
        }
        this.keys = keys;
    }


    @Override
    public boolean match(String key, String value, String msg) {
        if (keyList != null && keyList.contains(key)
                && (pattern == null || pattern.matcher(value).matches())) {
            return true;
        }
        return false;
    }

}
