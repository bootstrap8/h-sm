package com.github.hbq969.code.sm.i18n.web;

import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.Map;

@RequestMapping(path = "/hbq969-sm/i18n")
@Slf4j
@Api(tags = "页面使用-国际化管理")
public class I18nCtrl implements ICommonControl {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private com.github.hbq969.code.common.spring.i18n.I18nCtrl i18nCtrl;

    @Autowired
    private MapDictHelperImpl dict;

    @ApiOperation("设置语言")
    @RequestMapping(path = "/lang", method = RequestMethod.PUT)
    @ResponseBody
    public ReturnMessage<String> langSet(@RequestBody Map map) {
        return i18nCtrl.langSet(map);
    }

    @ApiOperation("获取语言")
    @RequestMapping(path = "/lang", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<String> getLang() {
        return ReturnMessage.success(String.join("-",
                Locale.getDefault().getLanguage(), Locale.getDefault().getCountry()));
    }

    @ApiOperation("获取语言数据")
    @RequestMapping(path = "/lang/data", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<Map<String,String>> getLangData() {
        String key = String.join("", "lang", ",", Locale.getDefault().getLanguage(), "-", Locale.getDefault().getCountry());
        log.info("查询 {} 数据", key);
        return ReturnMessage.success(dict.queryPairs(key));
    }
}
