package myAdmin.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import myAdmin.common.ControllerResult;
import myAdmin.core.authority.Authentication;
import myAdmin.module.admin.bean.vo.LoginResp;
import myAdmin.module.admin.service.AdminLoginService;
import myAdmin.util.AdminHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/login")
@Api(tags = "后台管理-登录模块")
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;

    @ApiOperation("登录")
    @PostMapping("/loginUser")
    public ControllerResult<LoginResp> login(
            @ApiParam(value = "登录名", required = true) @RequestParam String loginName,
            @ApiParam(value = "密码", required = true) @RequestParam String password){
        return ControllerResult.success(adminLoginService.login(loginName, password));
    }

    //TODO 验证码

    @ApiOperation("注销登录")
    @PostMapping("/logoutMySelf")
    public ControllerResult<?> logoutMySelf(){
        adminLoginService.logoutUser(AdminHelper.getUserId());
        return ControllerResult.success("注销登录成功");
    }

    @ApiOperation("下线指定用户")
    @PostMapping("/logoutUser")
    @Authentication("system:login:logout")
    public ControllerResult<?> logoutUser(
            @ApiParam(value = "用户主键", required = true) @RequestParam Long userId){
        adminLoginService.logoutUser(userId);
        return ControllerResult.success("下线成功");
    }

    @ApiOperation("获取在线用户")
    @GetMapping("/getOnLineUserList")
    @Authentication("system:login:list")
    public ControllerResult<?> getOnLineUserList(){
        return ControllerResult.success(adminLoginService.getOnLineUserList());
    }

}
