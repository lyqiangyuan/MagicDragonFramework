package com.hqy.mdf.log.rule;

import com.hqy.mdf.common.util.DesensitizationUtils;

import java.util.regex.Matcher;

/**
 * @author hqy
 */
public class MailDesensitizeRule extends GlobalDesensitizeRule {

    private static final String AT_STR = "@";

    @Override
    public boolean match(String key, String value, String msg) {
        if (super.match(key, value, msg) && msg.contains(AT_STR)) {
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
            if (group.contains(AT_STR)) {
                String[] parts = group.split(AT_STR);
                String username = parts[0];
                // 进行脱敏
                parts[0] = DesensitizationUtils.desensitization(username, preLen, sufLen, mask);
                // 将匹配到的内容替换为脱敏后的内容
                matcher.appendReplacement(sb, String.join(AT_STR, parts));
            }else  {
                matcher.appendReplacement(sb, group);
            }
        }
        // 追加剩余部分
        matcher.appendTail(sb);

        return sb.toString();

    }


}
