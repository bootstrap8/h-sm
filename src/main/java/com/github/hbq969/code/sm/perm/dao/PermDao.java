package com.github.hbq969.code.sm.perm.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermDao {
    void createMenuPermissions();

    void createRoleMenuPermissions();
}
