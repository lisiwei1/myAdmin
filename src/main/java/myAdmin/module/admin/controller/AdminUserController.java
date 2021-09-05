package myAdmin.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import myAdmin.common.ControllerResult;
import myAdmin.core.authority.Authentication;
import myAdmin.core.exception.BusinessException;
import myAdmin.module.admin.bean.po.AdminUser;
import myAdmin.module.admin.bean.vo.UserReq;
import myAdmin.module.admin.bean.vo.UserResp;
import myAdmin.module.admin.service.AdminUserService;
import myAdmin.util.AdminHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/user")
@Api(tags = "后台管理-用户模块")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @ApiIgnore
    @ApiOperation("初始化用户，获取所有权限")
    @PostMapping("/initUser")
    public ControllerResult<String> initUser(){
        userService.initUser();
        return ControllerResult.success("初始化用户成功!");
    }

    @ApiIgnore
    @ApiOperation("发起请求，用于测试token是否过期")
    @GetMapping("/requestTest")
    public ControllerResult<String> requestTest(){
        return ControllerResult.success("请求成功");
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/getUserList")
    @ApiImplicitParam(name = "token", value = "令牌", required = true, paramType = "header")
    @Authentication("system:user:list")
    public ControllerResult<List<AdminUser>> getUserList(@RequestHeader("token") String token){
        return ControllerResult.success(userService.getUserList());
    }

    @ApiOperation("用户详情")
    @GetMapping("/getUserDetail")
    @Authentication("system:user:query")
    public ControllerResult<UserResp> getUserDetail(HttpServletRequest request, HttpServletResponse response, @ApiParam(value = "用户主键", required = true) @RequestParam Long userId){
        return ControllerResult.success(userService.getUserDetail(userId));
    }

    @ApiOperation("添加用户")
    @PostMapping("/addUser")
    @Authentication("system:user:add")
    public ControllerResult<AdminUser> addUser(@Validated @RequestBody UserReq req){
        if (!StringUtils.isNoneBlank(req.getLoginName(), req.getUserName(), req.getPassword())) {
            throw new BusinessException("用户名密码不能为空");
        }
        return ControllerResult.success(userService.addUser(req.getLoginName(), req.getUserName(), req.getPassword(),
                req.getStatus(), req.getRemark(), req.getRoleIds()));
    }

    @ApiOperation("删除用户")
    @PostMapping("/deleteUser")
    @Authentication("system:user:delete")
    public ControllerResult<Object> deleteUser(@ApiParam(value = "用户主键", required = true) @RequestParam Long userId){
        userService.deleteUser(userId);
        return ControllerResult.success("删除用户成功", null);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/editUser")
    @Authentication("system:user:edit")
    public ControllerResult<UserResp> editUser(
            @ApiParam(value = "用户主键", required = true) @RequestParam Long userId,
            @ApiParam(value = "帐号状态（0停用 1正常）", required = true) @RequestParam Integer status,
            @ApiParam(value = "备注", required = true) @RequestParam String remark,
            @ApiParam(value = "角色主键集合", required = true) @RequestParam String roleIds){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        return ControllerResult.success(userService.editUser(userId, status, remark, roleIds, user.getLoginName()));
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPwd")
    @Authentication("system:user:resetPwd")
    public ControllerResult<String> resetPwd(
            @ApiParam(value = "用户主键", required = true) @RequestParam Long pkid,
            @ApiParam(value = "用户密码", required = true) @RequestParam String password){
        return ControllerResult.success(userService.resetPwd(pkid, password));
    }

}
