package com.hqy.mdf.log;

import com.hqy.mdf.common.enums.DesensitizationTypeEnum;
import com.hqy.mdf.log.rule.DesensitizeRule;
import com.hqy.mdf.log.rule.GlobalDesensitizeRule;
import com.hqy.mdf.log.rule.MailDesensitizeRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * @author hqy
 */
public class MdfLogContext {

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
    //邮箱地址正则表达式
    private static final String EMAIL_REGEX = "[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}";

    private static final ConcurrentMap<String, Object> PROPERTIES = new ConcurrentHashMap<>();

    private static final List<DesensitizeRule> DEFAULT_GLOBAL_RULES;

    static {
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

        //邮箱
        MailDesensitizeRule rule4 = new MailDesensitizeRule();
        rule4.setPattern(Pattern.compile(EMAIL_REGEX));
        rule4.setPreLen(DesensitizationTypeEnum.DEFAULT.getPreLength());
        rule4.setSufLen(DesensitizationTypeEnum.DEFAULT.getPreLength());
        defaultGlobalRules.add(rule4);
        DEFAULT_GLOBAL_RULES = defaultGlobalRules;

        // LogDesensitizeProperties defaultConfig = new LogDesensitizeProperties();
        // defaultConfig.setEnable(true);
        // PROPERTIES.put(MdfLogConstant.LOG_DESENSITIZE_CONFIG_KEY,defaultConfig);
    }



    public static void putObject(String key, Object value) {
        PROPERTIES.put(key, value);
    }

    public static Object getObject(String key) {
        return PROPERTIES.get(key);
    }

    public static void removeObject(String key) {
        PROPERTIES.remove(key);
    }

    public static boolean contains(String key) {
        return PROPERTIES.containsKey(key);
    }

    public static List<DesensitizeRule> getDefaultGlobalRules() {
        return DEFAULT_GLOBAL_RULES;
    }
}
