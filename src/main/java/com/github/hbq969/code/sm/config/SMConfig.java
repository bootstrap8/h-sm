package com.github.hbq969.code.sm.config;

import com.github.hbq969.code.sm.login.ctrl.LoginCtrl;
import com.github.hbq969.code.sm.login.ctrl.MenuCtrl;
import com.github.hbq969.code.sm.login.ctrl.RoleCtrl;
import com.github.hbq969.code.sm.login.ctrl.UserCtrl;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.service.impl.LoginServiceImpl;
import com.github.hbq969.code.sm.login.session.SessionInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

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
}
