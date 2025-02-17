package com.github.hbq969.code.sm.login.session;

import com.github.hbq969.code.common.spring.interceptor.AbstractHandlerInterceptor;
import com.github.hbq969.code.sm.config.LoginConfig;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
public class SessionInterceptor extends AbstractHandlerInterceptor {

    @Autowired
    private LoginService loginService;
    private long currentTimeMillis = 0;

    @Autowired
    private LoginConfig conf;

    @Override
    public List<String> getExcludedPathPatterns() {
        return conf.getExcludeUrls();
    }

    @Override
    public List<String> getPathPatterns() {
        return conf.getIncludeUrls();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = false;
        String jsid = CookieUtils.getCookieValue(request, conf.getSessionKey());
        String sid = jsid;
        if (StringUtils.contains(jsid, ".")) {
            sid = jsid.substring(0, jsid.indexOf("."));
        }
        if (StringUtils.isEmpty(sid)) {
        } else {
            HttpSession hs = loginService.getSession(sid);
            if (hs == null) {
            } else if (null != hs.getAttribute("h-sm-user")) {
                result = true;
                UserInfo ui = (UserInfo) hs.getAttribute("h-sm-user");
                UserContext.set(ui);
                if (currentTimeMillis == 0 || System.currentTimeMillis() - currentTimeMillis > 5000) {
                    Cookie jsessionCookie = new Cookie(conf.getSessionKey(), jsid);
                    jsessionCookie.setMaxAge((int) conf.getCookieMaxAgeSec());
                    jsessionCookie.setPath("/");
                    jsessionCookie.setHttpOnly(true);
                    response.addCookie(jsessionCookie);
                    currentTimeMillis = System.currentTimeMillis();
                }
            }
        }
        if (!result) {
            log.warn("会话失效，或已注销");
            invalidateSession(conf, response);
        }
        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }

    private static void invalidateSession(LoginConfig conf, HttpServletResponse response) throws IOException {
        Cookie jsessionCookie = new Cookie(conf.getSessionKey(), null);
        jsessionCookie.setMaxAge(5);
        jsessionCookie.setPath("/");
        jsessionCookie.setHttpOnly(true);
        response.addCookie(jsessionCookie);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 1;
    }

    public static void main(String[] args) {
        log.info("{}", RandomStringUtils.random(8, true, true));
    }

}
