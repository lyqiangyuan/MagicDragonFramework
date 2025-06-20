package com.hqy.mdf.log;

import com.hqy.mdf.log.rule.CustomizeDesensitizeRule;
import com.hqy.mdf.log.rule.DesensitizeRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogDesensitizer {

    private final List<MsgProcessor> processors = new ArrayList<>();
    private final List<DesensitizeRule> globalPatterns = new ArrayList<>();

    private LogDesensitizeProperties properties;
    
    public LogDesensitizer(LogDesensitizeProperties configProperties) {
        this.properties = configProperties;
        // 注册默认处理器（按优先级排序）
        if (properties != null && properties.getCustomizeRules() != null) {
            processors.add(new JsonMsgProcessor(properties.getCustomizeRules()));
            processors.add(new ToStringMsgProcessor(properties.getCustomizeRules()));
        }

        if (properties != null && properties.getGlobalRules() != null) {
            globalPatterns.addAll(properties.getGlobalRules());
        }
        if (properties != null && properties.isAddDefaultGlobalRules()) {
            globalPatterns.addAll(MdfLogContext.getDefaultGlobalRules());
        }

    }
    
    public String process(String logMessage) {
        if (logMessage == null || logMessage.isEmpty()) {
            return logMessage;
        }
        
        // 1. 尝试使用处理器处理
        for (int i = 0; i < processors.size(); i++) {
            MsgProcessor processor = processors.get(i);
            if (processor.canProcess(logMessage)) {
                logMessage = processor.process(logMessage);
            }
        }
        
        // 2. 使用全局正则匹配兜底
        return applyGlobalPatterns(logMessage);
    }


    
    private String applyGlobalPatterns(String message) {
        for (int i = 0; i < globalPatterns.size(); i++) {
            DesensitizeRule rule = globalPatterns.get(i);
            if (rule.match(null,null,message)) {
                message = rule.desensitize(message);
            }
        }
        return message;
    }

    // 格式处理器接口
    interface MsgProcessor {
        boolean canProcess(String message);
        String process(String message);
    }

    static abstract class AbstractProcessor implements MsgProcessor {

        private final List<CustomizeDesensitizeRule>  rules;

        AbstractProcessor(List<CustomizeDesensitizeRule> rules) {
            this.rules = rules;
        }

        public String desensitize(String fieldName, String value) {
            if (value == null || value.isEmpty()) {
                return value;
            }
            if (rules != null && !rules.isEmpty()) {
                for (int i = 0; i < rules.size(); i++) {
                    DesensitizeRule rule = rules.get(i);
                    if (rule.match(fieldName, value,null)) {
                        return rule.desensitize(value);
                    }
                }
            }
            return value;
        }

    }
    
    // JSON 格式处理器
    static class JsonMsgProcessor extends AbstractProcessor {
        // 处理"key":"value"格式 (JSON格式)
        private static final Pattern KEY_VALUE_PATTERN =
                Pattern.compile("\"(\\w+)\"(\\s*:\\s*)\"([^\"]*)\"");

        JsonMsgProcessor(List<CustomizeDesensitizeRule> rules) {
            super(rules);
        }

        @Override
        public boolean canProcess(String message) {
            if (message !=null && !message.isEmpty() && !"null".equalsIgnoreCase(message)) {
                Matcher matcher = KEY_VALUE_PATTERN.matcher(message);
                return matcher.find();
            }
            return false;
        }
        
        @Override
        public String process(String message) {
            Matcher matcher = KEY_VALUE_PATTERN.matcher(message);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String field = matcher.group(1);
                String linkChar = matcher.group(2);
                String value = matcher.group(3);
                String sensitiveValue = desensitize(field, value);
                // 替换文本
                matcher.appendReplacement(sb, "\"" + field + "\"" + linkChar + "\"" + sensitiveValue + "\"");
            }
            // 补全剩余文本
            matcher.appendTail(sb);

            return sb.toString();
        }
    }
    
    // ToString 格式处理器（如 User@123{name=xxx, phone=123}）
    static class ToStringMsgProcessor extends AbstractProcessor {
        private static final Pattern KEY_VALUE_PATTERN =
            Pattern.compile("(\\w+)(\\s*=\\s*)([^,)}]+)");

        ToStringMsgProcessor(List<CustomizeDesensitizeRule> rules) {
            super(rules);
        }

        @Override
        public boolean canProcess(String message) {
            if (message !=null && !message.isEmpty() && !"null".equalsIgnoreCase(message)) {
                Matcher matcher = KEY_VALUE_PATTERN.matcher(message);
                return matcher.find();
            }
            return false;
        }
        
        @Override
        public String process(String message) {
            Matcher matcher = KEY_VALUE_PATTERN.matcher(message);
            StringBuffer sb = new StringBuffer();
            
            while (matcher.find()) {
                String field = matcher.group(1);
                String linkChar = matcher.group(2);
                String value = matcher.group(3);
                String sensitiveValue = desensitize(field, value);
                matcher.appendReplacement(sb, field + linkChar + sensitiveValue);
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
    }
}