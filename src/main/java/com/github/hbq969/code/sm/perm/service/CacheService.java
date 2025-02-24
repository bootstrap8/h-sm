package com.github.hbq969.code.sm.perm.service;

import com.github.hbq969.code.sm.perm.dao.entity.MenuPermEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {

    void setCache(Map<String, List<MenuPermEntity>> cache);

    /**
     * 菜单关联的api接口信息
     *
     * @param menuName
     * @return
     */
    List<MenuPermEntity> queryMenuPerms(String menuName);

    /**
     * 检查api接口是否有访问权限
     *
     * @param menuName
     * @param apiKey
     */
    void permCheck(String menuName, String apiKey);

    /**
     * 支持配置的菜单名称列表
     *
     * @return
     */
    Set<String> querySupportedMenus();
}
