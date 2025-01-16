package com.github.hbq969.code.sm.login.service.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.GsonUtils;
import com.github.hbq969.code.common.utils.StrUtils;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.sm.config.LoginConfig;
import com.github.hbq969.code.sm.login.dao.LoginDao;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleMenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.UserEntity;
import com.github.hbq969.code.sm.login.model.LoginInfo;
import com.github.hbq969.code.sm.login.model.PasswordModify;
import com.github.hbq969.code.sm.login.model.ResetPassword;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.session.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class LoginServiceImpl implements LoginService, InitializingBean {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private SpringContext context;

    @Autowired
    private LoginConfig conf;

    @Autowired
    private MapDictHelperImpl dict;

    @Value("${spring.application.name}")
    private String app;

    private Cache<String, HttpSession> sessions;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void afterPropertiesSet() throws Exception {
        initialData();
        log.info("配置的cookie、会话超时时间: {} 秒。", conf.getCookieMaxAgeSec());
        this.sessions = CacheBuilder.newBuilder().maximumSize(500).initialCapacity(100).concurrencyLevel(10).expireAfterAccess(conf.getCookieMaxAgeSec(), TimeUnit.SECONDS).removalListener((RemovalListener<String, HttpSession>) notif -> {
            log.info("session自动过期，sid: {}", notif.getKey());
            notif.getValue().invalidate();
        }).build();
    }

    private void initialData() {
        try {
            loginDao.createRoles();
        } catch (Exception e) {
        }
        try {
            loginDao.createMenus();
        } catch (Exception e) {
        }
        try {
            loginDao.createUsers();
        } catch (Exception e) {
        }
        try {
            loginDao.createRoleMenus();
        } catch (Exception e) {
        }
        initialScript();
    }

    private void initialScript() {
        try {
            List<String> lines = IOUtils.readLines(LoginServiceImpl.class.getResourceAsStream("/sm-initial.sql"), StandardCharsets.UTF_8);
            log.debug("Read sm-initial.sql, Lines: {}", CollectionUtils.size(lines));
            List<String> box = new ArrayList<>();
            for (String line : lines) {
                if (StrUtils.strEmpty(line) || StrUtils.strEmpty(line.trim())) {
                    continue;
                }
                line = line.trim();
                box.add(line);
                if (line.endsWith(";")) {
                    String sql = String.join("\n", box).replaceAll("h-sm", context.getProperty("spring.application.name"));
                    try {
                        context.getBean(JdbcTemplate.class).update(sql);
                    } catch (DataAccessException e) {
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("execute: {}", sql);
                    }
                    box.clear();
                }

            }
        } catch (Exception e) {
            dict.reloadImmediately();
        }
    }

    @Override
    public PageInfo<RoleEntity> queryRoleList(int pageNum, int pageSize, RoleEntity q) {
        q.setName(UserContext.get().getRoleName());
        q.withApp(context);
        if (pageNum < 0) {
            PageInfo<RoleEntity> pg = new PageInfo<>(loginDao.queryRoleList(q));
            return pg;
        } else {
            PageInfo<RoleEntity> pg = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> loginDao.queryRoleList(q));
            pg.getList().forEach(e -> e.convertDict(context));
            return pg;
        }
    }

    @Override
    public void saveRoleEntity(RoleEntity entity) {
        entity.permit();
        entity.initial();
        entity.withApp(context);
        loginDao.saveRoleEntity(entity);
    }

    @Override
    public void updateRoleEntity(RoleEntity entity) {
        entity.permit();
        entity.update();
        entity.withApp(context);
        loginDao.updateRoleEntity(entity);
    }

    @Override
    public void deleteRoleEntity(String roleName) {
        UserInfo ui = UserContext.get();
        if (ui == null || !StringUtils.equals("ADMIN", ui.getRoleName())) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
        loginDao.deleteMenuEntities(app, roleName);
        loginDao.deleteUserEntities(app, roleName);
        loginDao.deleteRoleEntity(app, roleName);
    }

    @Override
    public List<Map> queryRoleMenus(String roleName) {
        return loginDao.queryRoleMenus(app, roleName);
    }

    @Override
    public PageInfo<UserEntity> queryUserList(int pageNum, int pageSize, UserEntity q) {
        q.setRoleName(UserContext.get().getRoleName());
        q.setUsername(UserContext.get().getUserName());
        q.setApp(app);
        PageInfo<UserEntity> pg = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> loginDao.queryUserList(q));
        pg.getList().forEach(e -> e.convertDict(context));
        return pg;
    }

    @Override
    public void saveUserEntity(UserEntity entity) {
        entity.permit();
        entity.initial();
        entity.setApp(app);
        entity.hash(encoder);
        loginDao.saveUserEntity(entity);
    }

    @Override
    public void updateUserEntity(UserEntity entity) {
        entity.permit();
        entity.update();
        entity.setApp(app);
        loginDao.updateUserEntity(entity);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        resetSessionUserInfo(sra.getRequest(), null);
    }

    @Override
    public void deleteUserEntity(String username) {
        UserInfo ui = UserContext.get();
        if (null == ui || !StringUtils.equals("ADMIN", ui.getRoleName())) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
        loginDao.deleteUserEntity(app, username);
    }

    @Override
    public void updatePassword(PasswordModify passwordModify) {
        UserInfo ui = UserContext.get();
        if (null == ui || !StringUtils.equals(ui.getUserName(), passwordModify.getUsername())) {
            throw new UnsupportedOperationException("不能修改别人的密码");
        }
        UserEntity ue = loginDao.queryUserEntity(app, passwordModify.getUsername());
        if (ue == null) {
            throw new UnsupportedOperationException("用户不存在");
        }
        if (!encoder.matches(passwordModify.getOldPassword(), ue.getPassword())) {
            throw new IllegalArgumentException("老密码不对");
        }
        passwordModify.hash(encoder);
        loginDao.updateUserPassword(app, passwordModify);
    }

    @Override
    public void resetPassword(ResetPassword rp) {
        UserInfo ui = UserContext.get();
        if (null == ui || !StringUtils.equals("ADMIN", ui.getRoleName())) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
        if (!rp.same()) {
            throw new IllegalArgumentException("两次密码不一致，请检查");
        }
        PasswordModify modify = new PasswordModify();
        modify.setUsername(rp.getUsername());
        modify.setNewPassword(rp.getPassword1());
        modify.hash(encoder);
        loginDao.updateUserPassword(app, modify);
    }

    @Override
    public PageInfo<MenuEntity> queryMenuList(int pageNum, int pageSize, MenuEntity q) {
        q.setApp(app);
        if (pageNum < 0) {
            List<MenuEntity> menus = loginDao.queryMenuList(q);
            PageInfo<MenuEntity> pg = new PageInfo<>(menus);
            pg.setTotal(menus.size());
            return pg;
        } else {
            PageInfo<MenuEntity> pg = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> loginDao.queryMenuList(q));
            pg.getList().forEach(e -> e.convertDict(context));
            return pg;
        }
    }

    @Override
    public List<MenuEntity> queryAllMenuList() {
        MenuEntity me = new MenuEntity();
        me.setApp(app);
        List<MenuEntity> list = loginDao.queryMenuList(me);
        return groupSortMenu(list);
    }

    @Override
    public void saveMenuEntity(MenuEntity entity) {
        entity.initial(context);
        entity.setApp(app);
        loginDao.saveMenuEntity(entity);
    }

    @Override
    public void updateMenuEntity(MenuEntity entity) {
        entity.permit();
        entity.update(context);
        entity.setApp(app);
        loginDao.updateMenuEntity(entity);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        resetSessionUserInfo(sra.getRequest(), null);
    }

    @Override
    public void deleteMenuEntity(String name) {
        UserInfo ui = UserContext.get();
        if (null == ui || (!StringUtils.equals("ADMIN", ui.getRoleName()) && MenuEntity.SYSTEM_MENUS.contains(name))) {
            throw new UnsupportedOperationException("此操作只允许ADMIN角色");
        }
        loginDao.deleteMenuEntity(app, name);
        loginDao.deleteMenuForRole(app, name);
        List<MenuEntity> subMenus = loginDao.querySubMenuList(app, name);
        log.info("删除菜单: {}, {}", app, name);
        if (CollectionUtils.isNotEmpty(subMenus)) {
            for (MenuEntity subMenu : subMenus) {
                loginDao.deleteMenuEntity(app, subMenu.getName());
                loginDao.deleteMenuForRole(app, subMenu.getName());
                log.info("删除菜单: {}, {}", app, subMenu.getName());
            }
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        resetSessionUserInfo(sra.getRequest(), null);
    }

    @Override
    public void updateRoleMenus(RoleMenuEntity roleMenuEntity) {
        UserInfo ui = UserContext.get();
        if (!StringUtils.equals("ADMIN", ui.getRoleName())
                && !StringUtils.equals(ui.getRoleName(), roleMenuEntity.getRole().getName())) {
            throw new UnsupportedOperationException("不允许配置别的角色菜单");
        }
        loginDao.deleteMenuEntities(app, roleMenuEntity.getRole().getName());
        context.getBean(JdbcTemplate.class).batchUpdate("insert into h_role_menus(app,role_name,menu_name) values(?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MenuEntity me = roleMenuEntity.getMenus().get(i);
                log.info("保存菜单权限, roleName: {}, menuUrl: {}", roleMenuEntity.getRole().getName(), me.getUrl());
                ps.setString(1, app);
                ps.setString(2, roleMenuEntity.getRole().getName());
                ps.setString(3, me.getName());
            }

            @Override
            public int getBatchSize() {
                return roleMenuEntity.getMenus().size();
            }
        });
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        resetSessionUserInfo(sra.getRequest(), null);
    }

    @Override
    public void login(LoginInfo login, HttpServletRequest request, HttpServletResponse response) {
        UserEntity user = loginDao.queryUserByName(app, login.getUsername());
        log.info("查询到用户信息: {}", user);
        if (encoder.matches(login.getPassword(), user.getPassword())) {
            log.info("密码验证一致");
            resetSessionUserInfo(request, user);
        } else {
            throw new IllegalArgumentException("密码错误，请重试");
        }
    }

    private void resetSessionUserInfo(HttpServletRequest request, UserEntity user) {
        HttpSession session;
        String logKey = "创建";
        if (user == null) {
            session = request.getSession();
            UserInfo oldUser = (UserInfo) session.getAttribute("h-sm-user");
            user = loginDao.queryUserByName(app, oldUser.getUserName());
            logKey = "更新";
        } else {
            session = request.getSession(true);
        }
        // 创建会话对象
        UserInfo ui = new UserInfo();
        ui.setUserName(user.getUsername());
        ui.setRoleName(user.getRoleName());
        List<MenuEntity> list = loginDao.queryRoleMenus2(app, user.getRoleName());
        List<MenuEntity> confMenus = groupSortMenu(list);
        ui.setMenus(confMenus);
        session.setAttribute("h-sm-user", ui);
        log.info("{}会话, id: {}, user: {}", logKey, session.getId(), GsonUtils.toJson(ui));
        if (StringUtils.equals("创建", logKey)) {
            sessions.put(session.getId(), session);
        }
    }

    // 对菜单进行分组排序
    private List<MenuEntity> groupSortMenu(List<MenuEntity> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        List<MenuEntity> level1List = menus.stream().filter(m -> m.getMenuLevel() == 1)
                .sorted(Comparator.comparing(m -> m.getOrderIndex()))
                .collect(Collectors.toList());
        Map<String, List<MenuEntity>> level2Group = menus.stream()
                .filter(m -> m.getMenuLevel() == 2)
                .collect(Collectors.groupingBy(m -> m.getParentId()));
        Iterator<Map.Entry<String, List<MenuEntity>>> it = level2Group.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<MenuEntity>> next = it.next();
            List<MenuEntity> sorted = next.getValue().stream()
                    .sorted(Comparator.comparing(m -> m.getOrderIndex()))
                    .collect(Collectors.toList());
            level2Group.put(next.getKey(), sorted);
        }
        for (MenuEntity m : level1List) {
            List<MenuEntity> sub = level2Group.get(m.getName());
            if (CollectionUtils.isNotEmpty(sub)) {
                m.setMenus(sub);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("权限内的菜单数据: {}", GsonUtils.toJson(level1List));
        }
        return level1List;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session != null) {
            log.info("注销账号: {}, 会话: {}", session.getAttribute("h-sm-user"), session.getId());
            sessions.invalidate(session.getId());
            Cookie jsessionCookie = new Cookie("JSESSIONID", null);
            jsessionCookie.setMaxAge(5);
            jsessionCookie.setPath("/");
            jsessionCookie.setHttpOnly(true);
            response.addCookie(jsessionCookie);
        }
    }

    @Override
    public HttpSession getSession(String sid) {
        return sessions.getIfPresent(sid);
    }

    @Scheduled(fixedRate = 5000)
    void cleanExpiredSessions() {
        sessions.cleanUp();
    }

    @Override
    public UserInfo getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return (UserInfo) session.getAttribute("h-sm-user");
        } else {
            throw new RuntimeException("会话失效，请重新登录");
        }
    }
}

