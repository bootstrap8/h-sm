package com.github.hbq969.code.sm.i18n.web;

import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.sm.login.dao.entity.UserEntity;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping(path = "/hbq969-sm/i18n")
@Slf4j
@Api(tags = "页面使用-国际化管理")
public class I18nCtrl implements ICommonControl {

    @Autowired
    private com.github.hbq969.code.common.spring.i18n.I18nCtrl i18nCtrl;

    @Autowired
    private MapDictHelperImpl dict;

    @Autowired
    private LoginService loginService;

    @ApiOperation("设置语言")
    @RequestMapping(path = "/lang", method = RequestMethod.PUT)
    @ResponseBody
    public ReturnMessage<String> langSet(HttpServletRequest request, @RequestBody Map map) {
        ReturnMessage<String> result = i18nCtrl.langSet(map);
        try {
            HttpSession hs = request.getSession();
            if (hs.getAttribute("h-sm-user") == null) {
                loginService.refreshSessionInfo(request, null);
            }
        } catch (Exception e) {
        }
        return result;
    }

    @ApiOperation("获取语言")
    @RequestMapping(path = "/lang", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<String> getLang() {
        ReturnMessage<String> result = i18nCtrl.getLang();
        String language = result.getBody();
        if (!StringUtils.contains(language, "-")) {
            String fullLang = dict.queryValue("i18n,language1", language);
            if (StringUtils.isNotEmpty(fullLang)) {
                language = fullLang;
            }
        }
        return ReturnMessage.success(language);
    }

    @ApiOperation("获取语言数据")
    @RequestMapping(path = "/lang/data", method = RequestMethod.GET)
    @ResponseBody
    public ReturnMessage<Map<String, String>> getLangData() {
        String key = getLang().getBody();
        log.info("查询 {} 数据", key);
        return ReturnMessage.success(dict.queryPairs(key));
    }
}
