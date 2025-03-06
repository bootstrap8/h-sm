package com.github.hbq969.code.sm.config;

import com.github.hbq969.code.sm.i18n.web.I18nCtrl;
import com.github.hbq969.code.sm.login.ctrl.LoginCtrl;
import com.github.hbq969.code.sm.login.ctrl.MenuCtrl;
import com.github.hbq969.code.sm.login.ctrl.RoleCtrl;
import com.github.hbq969.code.sm.login.ctrl.UserCtrl;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.service.impl.LoginServiceImpl;
import com.github.hbq969.code.sm.login.session.SessionInterceptor;
import com.github.hbq969.code.sm.perm.advice.PermHandler;
import com.github.hbq969.code.sm.perm.service.impl.CacheServiceImpl;
import com.github.hbq969.code.sm.perm.service.impl.PermServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@Slf4j
public class SMConfig {

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-LoginConfig")
    LoginConfig loginConfig() {
        return new LoginConfig();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-SessionInterceptor")
    SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-LoginService")
    LoginService loginService() {
        return new LoginServiceImpl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-LoginCtrl")
    LoginCtrl loginCtrl() {
        return new LoginCtrl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-MenuCtrl")
    MenuCtrl menuCtrl() {
        return new MenuCtrl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-RoleCtrl")
    RoleCtrl roleCtrl() {
        return new RoleCtrl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-UserCtrl")
    UserCtrl userCtrl() {
        return new UserCtrl();
    }


    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-PermServiceImpl")
    PermServiceImpl permService() {
        return new PermServiceImpl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-CacheServiceImpl")
    CacheServiceImpl cacheService() {
        return new CacheServiceImpl();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-PermHandler")
    PermHandler permHandler() {
        return new PermHandler();
    }

    @ConditionalOnProperty(prefix = "spring.mvc.interceptors.login", name = "enabled", havingValue = "true")
    @Bean("h-sm-I18nCtrl")
    I18nCtrl i18nCtrl() {
        return new I18nCtrl();
    }
}
