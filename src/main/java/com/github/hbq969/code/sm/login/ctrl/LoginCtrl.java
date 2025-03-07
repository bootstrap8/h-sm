package com.github.hbq969.code.sm.login.ctrl;

import cn.hutool.core.util.RandomUtil;
import com.github.hbq969.code.common.encrypt.ext.config.Decrypt;
import com.github.hbq969.code.common.log.api.Log;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.I18nUtils;
import com.github.hbq969.code.sm.config.LoginConfig;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.model.LoginInfo;
import com.github.hbq969.code.sm.login.model.PermitInfo;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.session.UserContext;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequestMapping(path = "/hbq969-sm/system")
@Slf4j
@Api(tags = "应用维护-登录管理")
public class LoginCtrl implements ICommonControl {

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginConfig conf;

    @Autowired
    private SpringContext context;

    @ApiOperation("登录")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    @Decrypt
    @Log(collectPostBody = false, collectResult = true)
    public ReturnMessage<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginInfo info) {
        loginService.login(info, request, response);
        return ReturnMessage.success(I18nUtils.getMessage(context, "ctrl.login.result"));
    }

    @ApiOperation("注销")
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    public ReturnMessage<?> logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logout(request, response);
        return ReturnMessage.success(I18nUtils.getMessage(context, "ctrl.logout.result"));
    }

    @ApiOperation("获取账号信息")
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "User", apiKey = "getUserInfo", apiDesc = "获取账号信息")
    public ReturnMessage<PermitInfo> getUserInfo(HttpServletRequest request) {
        PermitInfo info = new PermitInfo();
        info.setSmInfo(conf.getSmInfo());
        info.setUser(loginService.getUserInfo(request));
        if (UserContext.get().isAdmin()) {
            List<MenuEntity> all = loginService.queryAllMenuList();
            info.setAllMenus(all);
        } else {
            info.setAllMenus(Collections.EMPTY_LIST);
        }
        return ReturnMessage.success(info);
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomString(16));
    }
}