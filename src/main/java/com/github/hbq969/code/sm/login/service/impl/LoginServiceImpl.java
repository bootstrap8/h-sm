package com.github.hbq969.code.sm.login.service.impl;

import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.spring.i18n.LangInfo;
import com.github.hbq969.code.common.spring.i18n.LanguageEvent;
import com.github.hbq969.code.common.utils.GsonUtils;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.sm.config.LoginConfig;
import com.github.hbq969.code.sm.login.dao.LoginDao;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleMenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.UserEntity;
import com.github.hbq969.code.sm.login.model.*;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.login.session.UserContext;
import com.github.hbq969.code.sm.login.utils.I18nUtils;
import com.github.hbq969.code.sm.perm.dao.entity.MenuPermEntity;
import com.github.hbq969.code.sm.perm.service.CacheService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
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
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class LoginServiceImpl implements LoginService, InitializingBean, ApplicationListener<LanguageEvent> {

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

    @Autowired
    private CacheService cacheService;

    private Cache<String, HttpSession> sessions;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void afterPropertiesSet() throws Exception {
        initial();
        cookieInitial();
    }

    @Override
    public void onApplicationEvent(LanguageEvent event) {
        LangInfo langInfo = (LangInfo) event.getSource();
        log.info("监听到语言变化事件: {}", GsonUtils.toJson(langInfo));
        String lang = langInfo.getLang();
        conf.getSmInitialScript().setLanguage(lang);
        conf.getServiceInitialScript().setLanguage(lang);
        initialScript();
    }


    @Override
    public void loadSMInfo() {
        log.info("监听到sm信息变化事件");
        try {
            Map map = loginDao.querySMInfo(app);
            if (log.isDebugEnabled()) {
                log.debug("重新加载sm信息: {}", map);
            }
            String info = MapUtils.getString(map, "info_content", "{}");
            SMInfo sm = GsonUtils.fromJson(info, SMInfo.class);
            conf.setSmInfo(sm);
        } catch (Exception e) {
            log.error("重新加载sm信息异常: {}", e.getMessage());
            SMInfo sm = new SMInfo();
            sm.setTitle(app);
            conf.setSmInfo(sm);
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
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.permission.adminOnly"));
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
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.permission.adminOnly"));
        }
        loginDao.deleteUserEntity(app, username);
    }

    @Override
    public void updatePassword(PasswordModify passwordModify) {
        UserInfo ui = UserContext.get();
        if (null == ui || (!ui.isAdmin() && !StringUtils.equals(ui.getUserName(), passwordModify.getUsername()))) {
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.pwd.doNot"));
        }
        UserEntity ue = loginDao.queryUserEntity(app, passwordModify.getUsername());
        if (ue == null) {
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.username.notExists"));
        }
        if (!encoder.matches(passwordModify.getOldPassword(), ue.getPassword())) {
            throw new IllegalArgumentException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.oldPwd.notRight"));
        }
        passwordModify.hash(encoder);
        loginDao.updateUserPassword(app, passwordModify);
    }

    @Override
    public void resetPassword(ResetPassword rp) {
        UserInfo ui = UserContext.get();
        if (null == ui || !StringUtils.equals("ADMIN", ui.getRoleName())) {
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.permission.adminOnly"));
        }
        if (!rp.same()) {
            throw new IllegalArgumentException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.pwd.twiceWrong"));
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
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.permission.adminOnly"));
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
            throw new UnsupportedOperationException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.menu.notConfig"));
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
            throw new IllegalArgumentException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.pwd.wrong"));
        }
    }

    @Override
    public void refreshSessionInfo(HttpServletRequest request, UserEntity user) {
        resetSessionUserInfo(request, user);
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
        Set<String> permissions = queryPermission(list);
        ui.setPermissionSet(permissions);
        List<MenuEntity> confMenus = groupSortMenu(list);
        ui.setMenus(confMenus);
        session.setAttribute("h-sm-user", ui);
        log.info("{}会话, id: {}, user: {}", logKey, session.getId(), GsonUtils.toJson(ui));
        if (StringUtils.equals("创建", logKey)) {
            sessions.put(session.getId(), session);
        }
    }

    private Set<String> queryPermission(List<MenuEntity> list) {
        Set<String> set = new HashSet<>();
        for (MenuEntity menuEntity : list) {
            if (menuEntity.getMenuLevel() == 2) {
                List<MenuPermEntity> pl = cacheService.queryMenuPerms(menuEntity.getName());
                log.info("加载菜单 {} 的接口信息: {}", menuEntity.getName(),
                        pl == null ? "[]" : GsonUtils.toJson(pl.stream().map(p -> p.apiInfo()).collect(Collectors.toList())));
                if (CollectionUtils.isNotEmpty(pl)) {
                    List<String> perms = pl.stream()
                            .map(p -> String.join(",", p.getMenuName(), p.getApiKey()))
                            .collect(Collectors.toList());
                    set.addAll(perms);
                }
            }
        }
        return set;
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
            Cookie jsessionCookie = new Cookie(conf.getSessionKey(), null);
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
            throw new RuntimeException(com.github.hbq969.code.common.utils.I18nUtils.getMessage(context, "service.login.session.invalid"));
        }
    }

    private void initial() {
        createTables();
        initialScript();
    }

    private void createTables() {
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
        try {
            loginDao.createSMSystem();
        } catch (Exception e) {
        }
    }

    private void initialScript() {
        // 1.初始化h-sm的脚本数据
        Charset charset = Charset.forName(conf.getSmInitialScript().getCharset());
        String filename = String.join("", conf.getSmInitialScript().getPrefix(), "-", conf.getSmInitialScript().getLanguage(), ".sql");
        com.github.hbq969.code.common.utils.InitScriptUtils.initial(context, filename, charset,
                sql -> sql.replaceAll("h-sm", app),
                () -> dict.reloadImmediately());
        // 2.初始化当前服务的脚本数据
        String language = I18nUtils.getFullLanguage(context);
        filename = String.join("", conf.getServiceInitialScript().getPrefix(), "-", conf.getServiceInitialScript().getLanguage(), ".sql");
        com.github.hbq969.code.common.utils.InitScriptUtils.initial(context, filename, charset, null,
                () -> loadSMInfo());
    }

    private void cookieInitial() {
        this.sessions = CacheBuilder.newBuilder().maximumSize(500).initialCapacity(100).concurrencyLevel(10).expireAfterAccess(conf.getCookieMaxAgeSec(), TimeUnit.SECONDS).removalListener((RemovalListener<String, HttpSession>) notif -> {
            log.info("session自动过期，sid: {}", notif.getKey());
            notif.getValue().invalidate();
        }).build();
    }
}

