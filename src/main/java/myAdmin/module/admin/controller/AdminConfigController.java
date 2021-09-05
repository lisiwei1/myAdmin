package myAdmin.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import myAdmin.common.ControllerResult;
import myAdmin.config.AdminConfigure;
import myAdmin.core.authority.Authentication;
import myAdmin.core.exception.BusinessException;
import myAdmin.core.interceptor.MyInterceptorContextHolder;
import myAdmin.module.admin.bean.po.AdminConfig;
import myAdmin.module.admin.bean.po.AdminConfigType;
import myAdmin.module.admin.bean.po.AdminUser;
import myAdmin.module.admin.service.AdminConfigService;
import myAdmin.module.admin.service.AdminUserService;
import myAdmin.util.AdminHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/config")
@Api(tags = "后台管理-参数配置模块")
public class AdminConfigController {

    @Autowired
    private AdminConfigService configService;
    @Autowired
    private AdminConfigure adminConfigure;
    @Autowired
    private AdminUserService userService;

    @ApiOperation("从缓存中读取配置数据")
    @PostMapping("/getConfigValueFromCache")
    public ControllerResult<List<AdminConfig>> getConfigValueFromCache(
            @ApiParam(value = "配置类型编码", required = true) @RequestParam Integer typeCode,
            @ApiParam(value = "配置key", required = false) @RequestParam(required = false) String configKey){
        return ControllerResult.success(AdminConfigService.getConfigValueFromCache(typeCode, configKey));
    }

    @ApiOperation("查询参数类型")
    @GetMapping("/getTypeList")
    @Authentication("config:type:list")
    public ControllerResult<List<AdminConfigType>> getTypeList(){
        return ControllerResult.success(configService.getTypeList());
    }

    @ApiOperation("查询角色的参数类型")
    @GetMapping("/getTypeListByUser")
    //这个不需要权限标识，因为依赖与用户所对应的角色的权限
    public ControllerResult<List<AdminConfigType>> getTypeListByUser(){
        return ControllerResult.success(configService.getTypeListByUser(AdminHelper.getUserId()));
    }

    @ApiOperation("添加参数类型")
    @PostMapping("/addType")
    @Authentication("config:type:add")
    public ControllerResult<AdminConfigType> addType(
            @ApiParam(value = "类型编码", required = true) @RequestParam Integer typeCode,
            @ApiParam(value = "类型名称", required = true) @RequestParam String typeName,
            @ApiParam(value = "排序顺序", required = true) @RequestParam Integer orderNum,
            @ApiParam(value = "备注") @RequestParam(required = false) String remark){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        return ControllerResult.success(configService.addConfigType(typeCode, typeName, orderNum, remark, user.getUserName()));
    }

    @ApiOperation("删除参数类型")
    @PostMapping("/deleteType")
    @Authentication("config:type:delete")
    public ControllerResult<AdminConfigType> deleteType(@ApiParam(value = "类型主键", required = true) @RequestParam Long pkid){
        configService.deleteConfigType(pkid);
        return ControllerResult.success(null);
    }

    @ApiOperation("查询参数类型详情")
    @PostMapping("/getTypeDetail")
    @Authentication("config:type:query")
    public ControllerResult<AdminConfigType> getConfigTypeDetail(@ApiParam(value = "参数类型主键", required = true) @RequestParam Long pkid){
        return ControllerResult.success(configService.getConfigTypeDetail(pkid));
    }

    @ApiOperation("编辑参数类型")
    @PostMapping("/editType")
    @Authentication("config:type:edit")
    public ControllerResult<AdminConfigType> editType(
            @ApiParam(value = "主键", required = true) @RequestParam Long pkid,
            @ApiParam(value = "类型编码", required = true) @RequestParam Integer typeCode,
            @ApiParam(value = "类型名称", required = true) @RequestParam String typeName,
            @ApiParam(value = "显示顺序", required = true) @RequestParam Integer orderNum,
            @ApiParam(value = "备注", required = true) @RequestParam String remark){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        return ControllerResult.success(configService.editConfigType(pkid, typeCode, typeName, orderNum, remark, user.getUserName()));
    }

    @ApiOperation("查询参数列表")
    @PostMapping("/getConfigList")
    @Authentication("config:detail:list")
    public ControllerResult<List<AdminConfig>> getList(
            @ApiParam(value = "参数类型编码", required = true) @RequestParam Integer typeCode,
            @ApiParam(value = "参数类型编码") @RequestParam(required = false) Integer status){
        return ControllerResult.success(configService.getList(typeCode, status));
    }

    @ApiOperation("添加参数")
    @PostMapping("/addConfig")
    @Authentication("config:detail:add")
    public ControllerResult<AdminConfig> addConfig(
            @ApiParam(value = "参数类型编码", required = true) @RequestParam Integer typeCode,
            @ApiParam(value = "参数名称", required = true) @RequestParam String configName,
            @ApiParam(value = "参数键名", required = true) @RequestParam String configKey,
            @ApiParam(value = "参数键值", required = true) @RequestParam String configValue,
            @ApiParam(value = "显示顺序", required = true) @RequestParam Integer orderNum,
            @ApiParam(value = "状态（0停用 1启用）", required = true) @RequestParam Integer status,
            @ApiParam(value = "备注") @RequestParam(required = false) String remark){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        return ControllerResult.success(configService.addConfig(typeCode, configName, configKey, configValue, orderNum, status, user.getUserName(), remark));
    }

    @ApiOperation("查询参数详情")
    @PostMapping("/getConfigDetail")
    @Authentication("config:detail:query")
    public ControllerResult<AdminConfig> getConfigDetail(@ApiParam(value = "参数主键", required = true) @RequestParam Long pkid){
        return ControllerResult.success(configService.getConfigDetail(pkid));
    }

    @ApiOperation("删除参数")
    @PostMapping("/deleteConfig")
    @Authentication("config:detail:delete")
    public ControllerResult<AdminConfig> deleteConfig(@ApiParam(value = "参数表主键", required = true) @RequestParam Long pkid){
        configService.deleteConfig(pkid);
        return ControllerResult.success(null);
    }

    @ApiOperation("编辑参数")
    @PostMapping("/editConfig")
    @Authentication("config:detail:edit")
    public ControllerResult<AdminConfig> editConfig(
            @ApiParam(value = "参数表主键主键", required = true) @RequestParam Long pkid,
            @ApiParam(value = "参数名称", required = true) @RequestParam String configName,
            @ApiParam(value = "参数键名", required = true) @RequestParam String configKey,
            @ApiParam(value = "参数键值", required = true) @RequestParam String configValue,
            @ApiParam(value = "显示顺序", required = true) @RequestParam Integer orderNum,
            @ApiParam(value = "状态（0停用 1启用）", required = true) @RequestParam Integer status,
            @ApiParam(value = "备注") @RequestParam(required = false) String remark){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        return ControllerResult.success(configService.editConfig(pkid, configName, configKey, configValue, orderNum, status, user.getUserName(), remark));
    }

    @ApiOperation("更新参数详情状态")
    @PostMapping("/editConfigDeatilStatus")
    @Authentication("config:detail:editStatus")
    public ControllerResult<Object> editConfigDeatilStatus(
            @ApiParam(value = "主键", required = true) @RequestParam Long pkid,
            @ApiParam(value = "状态,1启用，0停用",  required = true) @RequestParam Integer status){
        AdminUser user = userService.getInfo(AdminHelper.getUserId());
        configService.editConfigDeatilStatus(pkid, status, user.getUserName());
        return ControllerResult.success("更新参数详情状态成功", null);
    }

}
