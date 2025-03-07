package com.github.hbq969.code.sm.login.ctrl;

import com.github.hbq969.code.common.log.api.Log;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleEntity;
import com.github.hbq969.code.sm.login.dao.entity.RoleMenuEntity;
import com.github.hbq969.code.sm.login.model.RoleMenu;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @ApiOperation("分页查询角色列表")
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<PageInfo<RoleEntity>> queryRoleList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, @RequestBody RoleEntity q) {
        return ReturnMessage.success(loginService.queryRoleList(pageNum, pageSize, q));
    }

    @ApiOperation("新增角色")
    @RequestMapping(path = "/role", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    public ReturnMessage<String> saveRole(@RequestBody RoleEntity role) {
        loginService.saveRoleEntity(role);
        return ReturnMessage.success("保存成功");
    }

    @ApiOperation("修改角色")
    @RequestMapping(path = "/role", method = RequestMethod.PUT)
    @ResponseBody
    @Log(collectResult = true)
    public ReturnMessage<?> updateRole(@RequestBody RoleEntity role) {
        loginService.updateRoleEntity(role);
        return ReturnMessage.success("修改成功");
    }

    @ApiOperation("删除角色")
    @RequestMapping(path = "/role", method = RequestMethod.DELETE)
    @ResponseBody
    @Log(collectResult = true)
    public ReturnMessage<?> deleteRole(@RequestParam(name = "name") String name) {
        loginService.deleteRoleEntity(name);
        return ReturnMessage.success("删除成功");
    }

    @ApiOperation("保存角色关联菜单")
    @RequestMapping(path = "/role/menus", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    public ReturnMessage<?> saveRoleMenus(@RequestBody RoleMenuEntity rme) {
        loginService.updateRoleMenus(rme);
        return ReturnMessage.success("保存成功");
    }

    @ApiOperation("查询角色菜单配置")
    @RequestMapping(path = "/role/menus", method = RequestMethod.GET)
    @ResponseBody
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
