package com.github.hbq969.code.sm.perm.service.impl;

import com.github.hbq969.code.sm.login.session.UserContext;
import com.github.hbq969.code.sm.perm.dao.entity.MenuPermEntity;
import com.github.hbq969.code.sm.perm.service.CacheService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CacheServiceImpl implements CacheService {
    private volatile Map<String, List<MenuPermEntity>> cache = new HashMap<>();

    @Override
    public void setCache(Map<String, List<MenuPermEntity>> cache) {
        this.cache = cache;
    }

    @Override
    public List<MenuPermEntity> queryMenuPerms(String menuName) {
        List<MenuPermEntity> mpe = cache.get(menuName);
        if (mpe == null) {
            log.warn("菜单 {} 没有配置@SMRequiresPermission权限注解，请关注。", menuName);
            return null;
        }
        return Collections.unmodifiableList(mpe);
    }

    @Override
    public void permCheck(String menuName, String apiKey) {
        // 获取权限内的菜单信息
        if (!UserContext.get().isAdmin()) {
            Set<String> perms = UserContext.get().getPermissionSet();
            if (!perms.contains(String.join(",", menuName, apiKey))) {
                throw new UnsupportedOperationException(String.format("不允许的权限[%s:%s]", menuName, apiKey));
            }
        }
    }

    @Override
    public Set<String> querySupportedMenus() {
        return Collections.unmodifiableSet(cache.keySet());
    }
}
