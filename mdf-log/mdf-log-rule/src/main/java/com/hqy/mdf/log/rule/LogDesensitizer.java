package com.hqy.mdf.log.rule;

import com.hqy.mdf.common.enums.DesensitizationTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogDesensitizer {


    // 中国手机号正则表达式
    private static final String PHONE_REGEX = "1[3-9]\\d{9}";
    // 15位身份证号码正则表达式
    private static final String ID_CARD_15_REGEX = "[1-9]\\d{5}\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}";
    // 18位身份证号码正则表达式
    private static final String ID_CARD_18_REGEX = "[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]";
    // 组合正则表达式，同时匹配15位和18位身份证号码
    private static final String ID_CARD_REGEX = ID_CARD_15_REGEX + "|" + ID_CARD_18_REGEX;
    // 62中国银联卡、Visa卡、Mastercard卡、美国运通卡、JCB卡的正则表达式
    private static final String BANK_CARD_REGEX = "62[0-9]{14,17}" + "|" +"4[0-9]{15}" + "|" +"5[1-5][0-9]{14}" + "|" +"(34|37)[0-9]{13}" + "|" +"35[0-9]{14}";

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
            if (properties.isAddDefaultGlobalRules()) {
                globalPatterns.addAll(getDefaultGlobalRules());
            }
        }else {
            globalPatterns.addAll(getDefaultGlobalRules());
        }

    }

    private List<DesensitizeRule> getDefaultGlobalRules() {
        List<DesensitizeRule> defaultGlobalRules = new ArrayList<>();
        //手机号
        GlobalDesensitizeRule rule1 = new GlobalDesensitizeRule();
        rule1.setPattern(Pattern.compile(PHONE_REGEX));
        rule1.setPreLen(DesensitizationTypeEnum.MOBILE_NO.getPreLength());
        rule1.setSufLen(DesensitizationTypeEnum.MOBILE_NO.getSufLength());
        defaultGlobalRules.add(rule1);

        //身份证号
        GlobalDesensitizeRule rule2 = new GlobalDesensitizeRule();
        rule2.setPattern(Pattern.compile(ID_CARD_REGEX));
        rule2.setPreLen(DesensitizationTypeEnum.ID_CARD_NO.getPreLength());
        rule2.setSufLen(DesensitizationTypeEnum.ID_CARD_NO.getSufLength());
        defaultGlobalRules.add(rule2);

        //银行卡号
        GlobalDesensitizeRule rule3 = new GlobalDesensitizeRule();
        rule3.setPattern(Pattern.compile(BANK_CARD_REGEX));
        rule3.setPreLen(DesensitizationTypeEnum.BANK_CARD_NO.getPreLength());
        rule3.setSufLen(DesensitizationTypeEnum.BANK_CARD_NO.getSufLength());
        defaultGlobalRules.add(rule3);
        return defaultGlobalRules;
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

        private List<DesensitizeRule>  rules;

        AbstractProcessor(List<DesensitizeRule> rules) {
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

        JsonMsgProcessor(List<DesensitizeRule> rules) {
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

        ToStringMsgProcessor(List<DesensitizeRule> rules) {
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