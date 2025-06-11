package com.hqy.mdf.log.logback.rule;

import com.hqy.mdf.common.util.DesensitizationUtils;

import java.util.regex.Matcher;

/**
 * @author hqy
 */
public class GlobalDesensitizeRule extends AbstractDesensitizeRule {

    @Override
    public boolean isMatch(String key, String value) {
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isMatch(String msg) {
        if (pattern != null && pattern.matcher(msg).find()) {
            return true;
        }
        return false;
    }

    // 脱敏方法
    @Override
    public String desensitize(String msg) {
        if (msg == null || msg.isEmpty() || "null".equalsIgnoreCase(msg)) {
            return msg;
        }
        Matcher matcher = pattern.matcher(msg);
        // 使用StringBuffer存储替换后的结果
        StringBuffer sb = new StringBuffer();

        // 遍历所有匹配的内容并替换
        while (matcher.find()) {
            String group = matcher.group();
            // 进行脱敏
            String maskedGroup = DesensitizationUtils.desensitization(group, preLen, sufLen, mask);
            // 将匹配到的内容替换为脱敏后的内容
            matcher.appendReplacement(sb, maskedGroup);
        }
        // 追加剩余部分
        matcher.appendTail(sb);

        return sb.toString();

    }


}
