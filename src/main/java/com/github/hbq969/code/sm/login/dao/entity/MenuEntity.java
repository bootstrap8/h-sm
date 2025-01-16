package com.github.hbq969.code.sm.login.dao.entity;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.FormatTime;
import com.github.hbq969.code.dict.service.api.DictAware;
import com.github.hbq969.code.dict.service.api.DictModel;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.session.UserContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : hbq969@gmail.com
 * @description : 菜单信息
 * @createTime : 2025/1/2 11:15
 */
@Data
@Slf4j
public class MenuEntity implements DictModel, DictAware {
    private String app;
    private String name;
    private String menuDesc;
    private String url;
    private String parentId = "-";
    private Integer menuLevel = 1;
    private Integer orderIndex = 0;
    private String iconName = "LogIcon";
    private Long createdAt;
    private String fmtCreatedAt;
    private Long updatedAt;
    private String fmtUpdatedAt;
    private List<MenuEntity> menus;
    public static Set<String> SYSTEM_MENUS = new HashSet() {{
        add("system");
        add("Role");
        add("User");
        add("Menu");
    }};

    @Override
    public void convertDict(SpringContext context) {
        DictAware.super.convertDict(context);
        if (createdAt != null) {
            this.fmtCreatedAt = FormatTime.YYYYMMDDHHMISS.withSecs(createdAt.longValue());
        }
        if (updatedAt != null) {
            this.fmtUpdatedAt = FormatTime.YYYYMMDDHHMISS.withSecs(updatedAt.longValue());
        }
    }

    public void initial(SpringContext context) {
        this.createdAt = FormatTime.nowSecs();
        if (StringUtils.isEmpty(this.url)) {
            return;
        }
        if (StringUtils.startsWith(this.url, "inner:")) {
            String str = this.url.substring(6);
            String contextPath = context.getProperty("server.servlet.context-path");
            if (StringUtils.isNotEmpty(contextPath) && !StringUtils.startsWith(str, contextPath)) {
                this.url = "inner:" + contextPath + str;
            }
        }
    }

    public void update(SpringContext context) {
        this.updatedAt = FormatTime.nowSecs();
        if (StringUtils.isEmpty(this.url)) {
            return;
        }
        if (StringUtils.startsWith(this.url, "inner:")) {
            String str = this.url.substring(6);
            String contextPath = context.getProperty("server.servlet.context-path");
            if (StringUtils.isNotEmpty(contextPath)
                    && !StringUtils.startsWith(str, contextPath)
                    && !StringUtils.contains(str, contextPath)) {
                this.url = "inner:" + contextPath + str;
            }
        }
    }

    public void addMenuEntity(MenuEntity menuEntity) {
        if (menus == null) {
            menus = new ArrayList<>();
        }
        menus.add(menuEntity);
    }

    public void permit() {
        UserInfo ui = UserContext.get();
        if (null == ui || (!StringUtils.equals("ADMIN", ui.getRoleName()) && SYSTEM_MENUS.contains(name))) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
    }
}

