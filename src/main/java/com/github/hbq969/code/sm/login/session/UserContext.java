package com.github.hbq969.code.sm.login.session;

import cn.hutool.core.lang.Assert;
import com.github.hbq969.code.sm.login.model.UserInfo;
import org.apache.commons.lang3.StringUtils;

public class UserContext {
    private static ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public static void set(UserInfo ui) {
        tl.set(ui);
    }

    public static UserInfo get() {
        UserInfo ui = tl.get();
        Assert.notNull(ui, "会话失效，请先登录");
        return ui;
    }

    public static void remove() {
        tl.remove();
    }

    public static boolean sameUser(String userName) {
        return StringUtils.equals(get().getUserName(), userName);
    }

    public static boolean permitAllow(String userName) {
        return get().isAdmin() || sameUser(userName);
    }
}
