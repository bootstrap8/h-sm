package com.github.hbq969.code.sm.perm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.GsonUtils;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import com.github.hbq969.code.sm.perm.dao.PermDao;
import com.github.hbq969.code.sm.perm.dao.entity.MenuPermEntity;
import com.github.hbq969.code.sm.perm.service.CacheService;
import com.github.hbq969.code.sm.perm.service.PermService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PermServiceImpl implements PermService, InitializingBean {

    @Autowired
    private PermDao permDao;

    @Autowired
    private SpringContext context;

    @Value("${spring.application.name}")
    private String app;

    @Autowired
    private CacheService cacheService;

    @Override
    public void afterPropertiesSet() throws Exception {
        initialTableCreate();
        initial();
    }

    private void initialTableCreate() {
        try {
            permDao.createMenuPermissions();
        } catch (Exception e) {
        }
        try {
            permDao.createRoleMenuPermissions();
        } catch (Exception e) {
        }
    }

    @Override
    public void initial() {
        // 1. 初始化读取菜单接口信息
        Map<String, Object> beans = context.getContext().getBeansWithAnnotation(RestController.class);
        List<MenuPermEntity> entities = getMenuPermEntities(beans);
        // 2.将菜单和接口的关系加载到缓存
        cacheMenuPerms(entities);
    }

    private void cacheMenuPerms(List<MenuPermEntity> entities) {
        cacheService.setCache(entities.stream().collect(Collectors.groupingBy(p -> p.getMenuName())));
    }

    private void saveMenuPerms(List<MenuPermEntity> entities) {
        String sql = "insert into h_menu_perms (app,menu_name,api_key,api_desc) values (?,?,?,?)";
        CollectionUtil.split(entities, 200).forEach(sub -> {
            try {
                context.getBean(JdbcTemplate.class).batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MenuPermEntity entity = sub.get(i);
                        ps.setString(1, entity.getApp());
                        ps.setString(2, entity.getMenuName());
                        ps.setString(3, entity.getApiKey());
                        ps.setString(4, entity.getApiDesc());
                    }

                    @Override
                    public int getBatchSize() {
                        return sub.size();
                    }
                });
            } catch (DataAccessException e) {
                for (MenuPermEntity entity : sub) {
                    try {
                        context.getBean(JdbcTemplate.class).update(sql, ps -> {
                            ps.setString(1, entity.getApp());
                            ps.setString(2, entity.getMenuName());
                            ps.setString(3, entity.getApiKey());
                            ps.setString(4, entity.getApiDesc());
                        });
                    } catch (DataAccessException ex) {
                    }
                }
            }
        });
    }

    private List<MenuPermEntity> getMenuPermEntities(Map<String, Object> beans) {
        List<MenuPermEntity> entities = new ArrayList<>();
        for (Map.Entry<String, Object> e : beans.entrySet()) {
            Object rest = e.getValue();
            Class<?> c = AopProxyUtils.ultimateTargetClass(rest);
            // 获取类上面的权限
            SMRequiresPermissions clzPerms = AnnotationUtils.findAnnotation(c, SMRequiresPermissions.class);
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                if (m.isSynthetic()) {
                    continue;
                }
                MenuPermEntity entity = new MenuPermEntity();
                entity.setApp(app);
                // 方法上的权限信息
                SMRequiresPermissions mthPerms = AnnotationUtils.findAnnotation(m, SMRequiresPermissions.class);
                ApiOperation apiOperation = AnnotationUtils.findAnnotation(m, ApiOperation.class);
                if (clzPerms == null && mthPerms == null) {
                    continue;
                }
                if (mthPerms == null) {
                    entity.setMenuName(clzPerms.menu());
                    entity.setApiKey(m.getName());
                    String apiDesc = m.getName();
                    if (apiOperation != null && StringUtils.isNotEmpty(apiOperation.value())) {
                        apiDesc = apiOperation.value();
                    }
                    entity.setApiDesc(apiDesc);
                } else {
                    entity.setMenuName(mthPerms.menu());
                    entity.setApiKey(mthPerms.apiKey());
                    entity.setApiDesc(mthPerms.apiDesc());
                }
                entities.add(entity);
                log.info("解析到权限信息: {}", GsonUtils.toJson(entity));
            }
        }
        return entities;
    }


}
