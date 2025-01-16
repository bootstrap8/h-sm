package com.github.hbq969.code.sm.login.ctrl;

import com.github.hbq969.code.common.encrypt.ext.config.Decrypt;
import com.github.hbq969.code.common.restful.ICommonControl;
import com.github.hbq969.code.common.restful.ReturnMessage;
import com.github.hbq969.code.sm.login.dao.entity.UserEntity;
import com.github.hbq969.code.sm.login.model.PasswordModify;
import com.github.hbq969.code.sm.login.model.ResetPassword;
import com.github.hbq969.code.sm.login.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/hbq969-sm/users")
@Slf4j
@Api(tags = "应用维护-用户管理")
public class UserCtrl implements ICommonControl {

    @Autowired
    private LoginService loginService;

    @ApiOperation("分页查询用户信息")
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> queryUserList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestBody UserEntity q) {
        return ReturnMessage.success(loginService.queryUserList(pageNum, pageSize, q));
    }

    @ApiOperation("新增用户")
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMessage<?> saveUser(@RequestBody UserEntity user) {
        loginService.saveUserEntity(user);
        return ReturnMessage.success("保存成功");
    }

    @ApiOperation("修改用户")
    @RequestMapping(path = "/user", method = RequestMethod.PUT)
    @ResponseBody
    public ReturnMessage<?> updateUser(@RequestBody UserEntity user) {
        loginService.updateUserEntity(user);
        return ReturnMessage.success("修改成功");
    }

    @ApiOperation("删除用户")
    @RequestMapping(path = "/user", method = RequestMethod.DELETE)
    @ResponseBody
    public ReturnMessage<?> deleteUser(@RequestParam(name = "username") String username) {
        loginService.deleteUserEntity(username);
        return ReturnMessage.success("删除成功");
    }

    @ApiOperation("修改密码")
    @RequestMapping(path = "/user/pass", method = RequestMethod.PUT)
    @ResponseBody
    @Decrypt
    public ReturnMessage<?> updatePass(@RequestBody PasswordModify modify) {
        loginService.updatePassword(modify);
        return ReturnMessage.success("修改成功");
    }

    @ApiOperation("重置密码")
    @RequestMapping(path = "/user/reset", method = RequestMethod.POST)
    @ResponseBody
    @Decrypt
    public ReturnMessage<?> resetPassword(@RequestBody ResetPassword rp) {
        loginService.resetPassword(rp);
        return ReturnMessage.success("重置成功");
    }
}
