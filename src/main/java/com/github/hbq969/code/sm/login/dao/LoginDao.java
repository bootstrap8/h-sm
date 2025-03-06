package com.github.hbq969.code.sm.login.dao;

import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleEntity;
import com.github.hbq969.code.sm.login.dao.entity.UserEntity;
import com.github.hbq969.code.sm.login.model.PasswordModify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginDao {
    void createUsers();

    void createRoles();

    void createMenus();

    void createRoleMenus();

    void createSMSystem();

    List<RoleEntity> queryRoleList(RoleEntity q);

    void saveRoleEntity(RoleEntity entity);

    void updateRoleEntity(RoleEntity entity);

    void deleteRoleEntity(@Param("app") String app, @Param("name") String name);

    List<Map> queryRoleMenus(@Param("app") String app, @Param("roleName") String roleName);

    List<MenuEntity> queryRoleMenus2(@Param("app") String app, @Param("roleName") String roleName);

    List<UserEntity> queryUserList(UserEntity q);

    UserEntity queryUserEntity(@Param("app") String app, @Param("username") String username);

    void saveUserEntity(UserEntity entity);

    void updateUserEntity(UserEntity entity);

    void deleteUserEntity(@Param("app") String app, @Param("username") String username);

    void deleteUserEntities(@Param("app") String app, @Param("roleName") String roleName);

    void updateUserPassword(@Param("app") String app, @Param("modify") PasswordModify passwordModify);

    List<MenuEntity> queryMenuList(MenuEntity q);

    void saveMenuEntity(MenuEntity entity);

    void updateMenuEntity(MenuEntity entity);

    void deleteMenuEntity(@Param("app") String app, @Param("name") String name);

    List<MenuEntity> querySubMenuList(@Param("app") String app, @Param("parentId") String parent);
    void deleteMenuEntities(@Param("app") String app, @Param("roleName") String roleName);

    void deleteMenuForRole(@Param("app") String app, @Param("menuName") String menuName);

    UserEntity queryUserByName(@Param("app") String app, @Param("name") String name);
}
