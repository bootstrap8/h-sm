package com.github.hbq969.code.sm.login.utils;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.spring.i18n.I18nCtrl;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class I18nUtils {

    private static Map<String, String> LANG_MAP = new HashMap() {{
        put("zh", "zh-CN");
        put("en", "en-US");
        put("ja", "ja-JP");
    }};

    public static String getFullLanguage(SpringContext context) {
        I18nCtrl i18nCtrl = context.getBean(I18nCtrl.class);
        if (i18nCtrl == null) {
            return "zh-CN";
        }
        String language = i18nCtrl.getLang().getBody();
        if (StringUtils.isEmpty(language)) {
            return "zh-CN";
        }
        if (StringUtils.contains(language, "-")) {
            return language;
        } else {
            MapDictHelperImpl dict = context.getBean(MapDictHelperImpl.class);
            if (dict == null) {
                String fullLanguage = LANG_MAP.get(language);
                if (fullLanguage == null) {
                    throw new UnsupportedOperationException(String.format("Not supported Language: %s", language));
                }
                return fullLanguage;
            } else {
                String fullLanguage = dict.queryValue("i18n,language1", language);
                if (fullLanguage == null) {
                    log.warn("字典中未找到 i18n,language1 配置数据，请检查。");
                    fullLanguage = LANG_MAP.get(language);
                }
                if (fullLanguage == null) {
                    throw new UnsupportedOperationException(String.format("Not supported Language: %s", language));
                }
                return fullLanguage;
            }
        }
    }
}
