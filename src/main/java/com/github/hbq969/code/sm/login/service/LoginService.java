package com.github.hbq969.code.sm.login.service;

import com.github.hbq969.code.common.lang.Init;
import com.github.hbq969.code.sm.login.dao.entity.*;
import com.github.hbq969.code.sm.login.event.SMInfoEvent;
import com.github.hbq969.code.sm.login.model.ResetPassword;
import com.github.hbq969.code.sm.login.model.UserInfo;
import com.github.hbq969.code.sm.login.model.LoginInfo;
import com.github.hbq969.code.sm.login.model.PasswordModify;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface LoginService{

    PageInfo<RoleEntity> queryRoleList(int pageNum, int pageSize, RoleEntity q);

    void saveRoleEntity(RoleEntity entity);

    void updateRoleEntity(RoleEntity entity);

    @Transactional(rollbackFor = Exception.class)
    void deleteRoleEntity(String roleName);

    List<Map> queryRoleMenus(String roleName);

    PageInfo<UserEntity> queryUserList(int pageNum, int pageSize, UserEntity q);

    void saveUserEntity(UserEntity entity);

    void updateUserEntity(UserEntity entity);

    void deleteUserEntity(String username);

    void updatePassword(PasswordModify passwordModify);

    void resetPassword(ResetPassword rp);

    PageInfo<MenuEntity> queryMenuList(int pageNum, int pageSize, MenuEntity q);

    List<MenuEntity> queryAllMenuList();

    void saveMenuEntity(MenuEntity entity);

    void updateMenuEntity(MenuEntity entity);

    void deleteMenuEntity(String name);

    @Transactional(rollbackFor = Exception.class)
    void updateRoleMenus(RoleMenuEntity roleMenuEntity);

    void login(LoginInfo login, HttpServletRequest request, HttpServletResponse response);

    void refreshSessionInfo(HttpServletRequest request,UserEntity user);

    void logout(HttpServletRequest request, HttpServletResponse response);

    UserInfo getUserInfo(HttpServletRequest request);

    HttpSession getSession(String sid);

    void loadSMInfo();
}
