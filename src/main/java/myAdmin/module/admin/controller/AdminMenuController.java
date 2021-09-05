package myAdmin.module.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import myAdmin.common.ControllerResult;
import myAdmin.core.authority.Authentication;
import myAdmin.module.admin.bean.po.AdminMenu;
import myAdmin.module.admin.bean.vo.TreeMenu;
import myAdmin.module.admin.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/menu")
@Api(tags = "后台管理-菜单模块")
public class AdminMenuController {

    @Autowired
    private AdminMenuService menuService;

    @ApiOperation("获取菜单列表")
    @GetMapping("/menuList")
    @Authentication("system:menu:list")
    public ControllerResult<List<TreeMenu>> menuList() {
        return ControllerResult.success(menuService.menuList());
    }

    @ApiOperation("查询菜单")
    @GetMapping("/getMenu")
    @Authentication("system:menu:query")
    public ControllerResult<AdminMenu> getMenu(@ApiParam(value = "菜单主键", required = true) @ RequestParam Long pkid) {
        return ControllerResult.success(menuService.getMenu(pkid));
    }

    @ApiOperation("新增菜单")
    @PostMapping("/addMenu")
    @Authentication("system:menu:add")
    public ControllerResult<AdminMenu> addMenu(@RequestBody AdminMenu req){
        menuService.addMenu(req.getMenuName(), req.getParentId(), req.getOrderNum(), req.getPath(),
                req.getComponent(), req.getMenuType(), req.getVisible(), req.getStatus(), req.getPerms(), req.getIcon());
        return ControllerResult.success("新增权限成功", null);
    }

    @ApiOperation("编辑菜单")
    @PostMapping("/editMenu")
    @Authentication("system:menu:edit")
    public ControllerResult<AdminMenu> editMenu(@RequestBody AdminMenu req){
        menuService.editMenu(req.getPkid(), req.getMenuName(), req.getParentId(), req.getOrderNum(), req.getPath(),
                req.getComponent(), req.getMenuType(), req.getVisible(), req.getStatus(), req.getPerms(), req.getIcon());
        return ControllerResult.success("编辑权限成功", null);
    }

    @ApiOperation("删除菜单")
    @PostMapping("/deleteMenu")
    @Authentication("system:menu:delete")
    public ControllerResult<AdminMenu> deleteMenu(@ApiParam(value = "菜单主键", required = true) @RequestParam Long pkid){
        menuService.deleteMenu(pkid);
        return ControllerResult.success("删除权限成功", null);
    }

    /**
     * 让超级用户获取所有权限
     * @return
     */
//    @ApiIgnore
//    @PostMapping("/initAdminAuthority")
//    public ControllerResult<Boolean> initAdminAuthority(){
//        //XXX 后面要优化
//        Long userId = 1L;
//        return ControllerResult.success(menuService.initAdminAuthority(userId));
//    }

}
