package com.github.hbq969.code.sm.login.model;

import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class UserInfo extends com.github.hbq969.code.common.spring.context.UserInfo {
    private String roleName;
    private List<MenuEntity> menus;
    private transient Set<String> permissionSet;

    public boolean isAdmin() {
        return StringUtils.equals("ADMIN", roleName);
    }
}

