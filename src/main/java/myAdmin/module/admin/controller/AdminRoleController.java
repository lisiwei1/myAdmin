package myAdmin.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import myAdmin.common.ControllerResult;
import myAdmin.core.authority.Authentication;
import myAdmin.module.admin.bean.po.AdminRole;
import myAdmin.module.admin.bean.vo.EditRoleReq;
import myAdmin.module.admin.bean.vo.RoleReq;
import myAdmin.module.admin.bean.vo.RoleResp;
import myAdmin.module.admin.bean.vo.editRoleConfigTypeReq;
import myAdmin.module.admin.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/role")
@Api(tags = "后台管理-角色模块")
public class AdminRoleController {

    @Autowired
    private AdminRoleService roleService;

    @ApiOperation("查询角色列表")
    @GetMapping("/getRoleList")
    @Authentication("system:role:list")
    public ControllerResult<List<AdminRole>> getRoleList(){
        return ControllerResult.success(roleService.getRoleList());
    }

    @ApiOperation("角色详情")
    @GetMapping("/getRoleDetail")
    @Authentication("system:role:query")
    public ControllerResult<RoleResp> geRoletDetail(@ApiParam(value = "角色主键", required = true) @RequestParam Long pkid){
        return ControllerResult.success(roleService.getRoleDetail(pkid));
    }

    @ApiOperation("添加角色")
    @PostMapping("/addRole")
    @Authentication("system:role:add")
    public ControllerResult<AdminRole> addRole(@Validated @RequestBody RoleReq req){
        roleService.addRole(req.getRoleKey(), req.getRoleName(), req.getStatus(),
                req.getOrderNum(), req.getMenuIds(), req.getRemark());
        return ControllerResult.success(null);
    }

    @ApiOperation("删除角色")
    @PostMapping("/deleteRole")
    @Authentication("system:role:delete")
    public ControllerResult<AdminRole> deleteRole(@ApiParam(value = "角色主键", required = true) @RequestParam Long pkid){
        return ControllerResult.success(roleService.deleteRole(pkid));
    }

    @ApiOperation("编辑角色")
    @PostMapping("/editRole")
    @Authentication("system:role:edit")
    public ControllerResult<Object> editRole(@Validated @RequestBody EditRoleReq req){
        roleService.editRole(req.getRoleId(), req.getRoleName(), req.getRoleKey(), req.getOrderNum(), req.getStatus(), req.getMenuIds(), req.getRemark());
        return ControllerResult.success(null);
    }

    @ApiOperation("查询角色的参数权限")
    @PostMapping("/getRoleConfigTypeDetail")
    @Authentication("system:roleConfig:query")
    public ControllerResult<Object> getRoleConfigTypeDetail(@ApiParam(value = "角色主键", required = true) @RequestParam Long roleId){
        return ControllerResult.success(roleService.getRoleConfigTypeDetail(roleId));
    }

    @ApiOperation("编辑角色的参数权限")
    @PostMapping("/editRoleConfigType")
    @Authentication("system:roleConfig:edit")
    public ControllerResult<Object> editRoleConfigType(@Validated @RequestBody editRoleConfigTypeReq req){
        roleService.editRoleConfigType(req.getRoleId(), req.getConfigIds());
        return ControllerResult.success(null);
    }
}
