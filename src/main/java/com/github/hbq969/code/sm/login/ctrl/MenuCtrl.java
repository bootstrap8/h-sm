package com.github.hbq969.code.sm.login.ctrl;

import com.github.hbq969.code.common.log.api.Log;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.common.spring.context.SpringContext;
import com.github.hbq969.code.common.utils.I18nUtils;
import com.github.hbq969.code.dict.service.api.impl.MapDictHelperImpl;
import com.github.hbq969.code.sm.login.dao.entity.MenuEntity;
import com.github.hbq969.code.sm.login.model.MenuModel;
import com.github.hbq969.code.sm.login.service.LoginService;
import com.github.hbq969.code.sm.perm.api.SMRequiresPermissions;
import com.github.hbq969.code.sm.perm.service.CacheService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping(path = "/hbq969-sm/menus")
@Slf4j
@Api(tags = "应用维护-菜单管理")
public class MenuCtrl implements ICommonControl {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MapDictHelperImpl dict;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SpringContext context;

    @ApiOperation("查询菜单信息")
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseBody
    @SMRequiresPermissions(menu = "Menu", apiKey = "queryMenuPageList", apiDesc = "分页查询菜单信息")
    public ReturnMessage<MenuModel> queryMenuList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestBody MenuEntity q) {
        MenuModel mm = new MenuModel();
        PageInfo<MenuEntity> pg = loginService.queryMenuList(pageNum, pageSize, q);
        mm.setMenus(pg);
        pg = loginService.queryMenuList(-1, -1, new MenuEntity());
        List<MenuEntity> level1Menus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pg.getList())) {
            for (MenuEntity me : pg.getList()) {
                if (me.getMenuLevel() != null && me.getMenuLevel().intValue() == 1) {
                    level1Menus.add(me);
                }
            }
        }
        mm.setLevel1Menus(level1Menus);
        mm.setIconList(dict.queryPairList("menu,icon"));
        return ReturnMessage.success(mm);
    }

    @ApiOperation("查询菜单信息")
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "Menu", apiKey = "queryMenuList", apiDesc = "查询菜单信息")
    public ReturnMessage<MenuModel> queryMenuList() {
        MenuModel mm = new MenuModel();
        PageInfo<MenuEntity> pg = loginService.queryMenuList(-1, -1, new MenuEntity());
        mm.setMenus(pg);
        List<MenuEntity> level1Menus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pg.getList())) {
            for (MenuEntity me : pg.getList()) {
                if (me.getMenuLevel() != null && me.getMenuLevel().intValue() == 1) {
                    level1Menus.add(me);
                }
            }
        }
        mm.setLevel1Menus(level1Menus);
        mm.setIconList(dict.queryPairList("menu,icon"));
        return ReturnMessage.success(mm);
    }

    @ApiOperation("新增菜单")
    @RequestMapping(path = "/menu", method = RequestMethod.POST)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Menu", apiKey = "saveMenu", apiDesc = "新增菜单")
    public ReturnMessage<?> saveMenu(@RequestBody MenuEntity menu) {
        loginService.saveMenuEntity(menu);
        return ReturnMessage.success(I18nUtils.getMessage(context, "ctrl.save.result"));
    }

    @ApiOperation("修改菜单")
    @RequestMapping(path = "/menu", method = RequestMethod.PUT)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Menu", apiKey = "updateMenu", apiDesc = "修改菜单")
    public ReturnMessage<?> updateMenu(@RequestBody MenuEntity menu) {
        loginService.updateMenuEntity(menu);
        return ReturnMessage.success(I18nUtils.getMessage(context, "ctrl.update.result"));
    }

    @ApiOperation("删除菜单")
    @RequestMapping(path = "/menu", method = RequestMethod.DELETE)
    @ResponseBody
    @Log(collectResult = true)
    @SMRequiresPermissions(menu = "Menu", apiKey = "deleteMenu", apiDesc = "删除菜单")
    public ReturnMessage<?> deleteMenu(@RequestParam(name = "name") String name) {
        loginService.deleteMenuEntity(name);
        return ReturnMessage.success(I18nUtils.getMessage(context, "ctrl.delete.result"));
    }

    @ApiOperation("查询声明了权限的菜单名称列表")
    @RequestMapping(path = "/supported/menus", method = RequestMethod.GET)
    @ResponseBody
    @SMRequiresPermissions(menu = "Menu", apiKey = "querySupportedMenus", apiDesc = "查询声明了权限的菜单名称列表")
    public ReturnMessage<Set<String>> querySupportedMenus() {
        return ReturnMessage.success(cacheService.querySupportedMenus());
    }
}

