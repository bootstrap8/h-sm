package com.github.hbq969.code.sm.login.ctrl;

import com.github.hbq969.code.common.log.api.Log;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleMenuEntity;
import com.github.hbq969.code.sm.login.model.RoleMenu;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : hbq969@gmail.com
 * @description : 角色管理
 * @createTime : 2025/1/2 10:57
 */
@RequestMapping(path = "/hbq969-sm/roles")
@Slf4j
@Api(tags = "应用维护-角色管理")
public class RoleCtrl implements ICommonControl {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @ApiOperation("分页查询角色列表")
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseBody
    @SMRequiresPermissions(menu = "Role",apiKey = "queryRoleList",apiDesc = "分页查询角色列表")
    public ReturnMessage<PageInfo<RoleEntity>> queryRoleList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @RequestBody RoleEntity q) {
        return ReturnMessage.success(loginService.queryRoleList(pageNum, pageSize, q));
    }

    @ApiOperation("新增角色")
    @RequestMapping(path = "/role", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Role",apiKey = "saveRole",apiDesc = "新增角色")
    public ReturnMessage<String> saveRole(@RequestBody RoleEntity role) {
        loginService.saveRoleEntity(role);
        return ReturnMessage.success(messageSource.getMessage("ctrl.save.result", null, Locale.getDefault()));
    }

    @ApiOperation("修改角色")
    @RequestMapping(path = "/role", method = RequestMethod.PUT)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Role",apiKey = "updateRole",apiDesc = "修改角色")
    public ReturnMessage<?> updateRole(@RequestBody RoleEntity role) {
        loginService.updateRoleEntity(role);
        return ReturnMessage.success(messageSource.getMessage("ctrl.update.result", null, Locale.getDefault()));
    }

    @ApiOperation("删除角色")
    @RequestMapping(path = "/role", method = RequestMethod.DELETE)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Role",apiKey = "deleteRole",apiDesc = "删除角色")
    public ReturnMessage<?> deleteRole(@RequestParam(name = "name") String name) {
        loginService.deleteRoleEntity(name);
        return ReturnMessage.success(messageSource.getMessage("ctrl.delete.result", null, Locale.getDefault()));
    }

    @ApiOperation("保存角色关联菜单")
    @RequestMapping(path = "/role/menus", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Role",apiKey = "saveRoleMenus",apiDesc = "保存角色关联菜单")
    public ReturnMessage<?> saveRoleMenus(@RequestBody RoleMenuEntity rme) {
        loginService.updateRoleMenus(rme);
        return ReturnMessage.success(messageSource.getMessage("ctrl.save.result", null, Locale.getDefault()));
    }

    @ApiOperation("查询角色菜单配置")
    @RequestMapping(path = "/role/menus", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "Role",apiKey = "queryRoleMenus",apiDesc = "查询角色菜单配置")
    public ReturnMessage<RoleMenu> queryRoleMenus(@RequestParam(name = "name") String name) {
        RoleMenu roleMenu = new RoleMenu();
        List<Map> conf = loginService.queryRoleMenus(name);
        Set<String> set = conf.stream().map(m -> MapUtils.getString(m, "key")).collect(Collectors.toSet());
        roleMenu.setConf(set);
        Map<String, String> level1Map = loginService.queryAllMenuList()
                .stream().collect(Collectors.toMap(m -> m.getName(), m -> m.getMenuDesc()));
        roleMenu.setAll(loginService.queryMenuList(-1, -1, new MenuEntity()).getList().stream()
                .map(m -> ImmutableMap.of("key", m.getName(), "label", m.getMenuLevel() == 2 ? String.join("/", level1Map.get(m.getParentId()), m.getMenuDesc()) : m.getMenuDesc()))
                .collect(Collectors.toList()));
        return ReturnMessage.success(roleMenu);
    }
}
