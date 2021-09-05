package myAdmin.module.admin.service;

import myAdmin.common.id.IdType;
import myAdmin.core.exception.BusinessException;
import myAdmin.module.admin.bean.po.AdminMenu;
import myAdmin.module.admin.bean.po.AdminRoleMenu;
import myAdmin.module.admin.bean.vo.TreeMenu;
import myAdmin.module.admin.dao.AdminMenuRepository;
import myAdmin.module.admin.dao.AdminRoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {

    @Autowired
    private AdminMenuRepository menuRepository;
    @Autowired
    private AdminRoleMenuRepository roleMenuRepository;

    //菜单类型（1目录 2菜单 3按钮）
    private static Integer[] MENUTYPE = {1, 2};

    //获取菜单下拉树列表
    public List<TreeMenu> menuList() {
        return createRoleMenuTree(menuRepository.findAll(), null, false);
    }

    /**
     * 查询菜单详情
     * @param pkid
     * @return
     */
    public AdminMenu getMenu(Long pkid) {
        return menuRepository.findById(pkid).orElseThrow(
                () -> new BusinessException("没有找到当前菜单"));
    }

    /**
     * 新增权限
     * @param menuName
     * @param parentId
     * @param orderNum
     * @param path
     * @param component
     * @param menuType
     * @param visible
     * @param status
     * @param perms
     * @param icon
     */
    public void addMenu(String menuName, Long parentId, Integer orderNum, String path, String component, Integer menuType,
                        Integer visible, Integer status, String perms, String icon) {
        //校验是否有重复数据
        List<AdminMenu> menuNameList = menuRepository.findByMenuName(menuName);
        if (!CollectionUtils.isEmpty(menuNameList)) {
            throw new BusinessException("权限名称不能重复");
        }
        AdminMenu menu = new AdminMenu();
        menu.setMenuName(menuName);
        menu.setParentId(parentId);
        menu.setOrderNum(orderNum);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setMenuType(menuType);
        menu.setVisible(visible);
        menu.setStatus(status);
        menu.setPerms(perms);
        menu.setIcon(icon);
        menuRepository.saveAndFlush(menu);
    }

    /**
     * 编辑权限
     * @param pkid
     * @param menuName
     * @param parentId
     * @param orderNum
     * @param path
     * @param component
     * @param menuType
     * @param visible
     * @param status
     * @param perms
     * @param icon
     */
    public void editMenu(Long pkid, String menuName, Long parentId, Integer orderNum, String path, String component, Integer menuType,
                         Integer visible, Integer status, String perms, String icon) {
        AdminMenu menu = menuRepository.findById(pkid).orElseThrow(
                () -> new BusinessException("找不到当前的权限"));
        //校验是否有重复数据
        List<AdminMenu> menuNameList = menuRepository.findByMenuNameAndPkidNot(menuName, pkid);
        if (!CollectionUtils.isEmpty(menuNameList)) {
            throw new BusinessException("权限名称不能重复");
        }

        menu.setMenuName(menuName);
        menu.setParentId(parentId);
        menu.setOrderNum(orderNum);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setMenuType(menuType);
        menu.setVisible(visible);
        menu.setStatus(status);
        menu.setPerms(perms);
        menu.setIcon(icon);
        menuRepository.saveAndFlush(menu);
    }

    //删除权限
    @Transactional
    public void deleteMenu(Long pkid) {
        //TODO 删除权限时是不是也要删除所有子权限 ？
        AdminMenu menu = menuRepository.findById(pkid).orElseThrow(
                () -> new BusinessException("找不到当前的权限"));
        menuRepository.delete(menu);
        //同时删除角色表下的权限
        roleMenuRepository.deleteByMenuId(pkid);
    }

    /**
     * 让超级用户角色获得所有权限
     */
    public void initAdminAuthority() {
        List<AdminMenu> menuList = menuRepository.findAll();
        if (CollectionUtils.isEmpty(menuList)) {
            throw new BusinessException("权限表没有数据");
        }
        Long roleId = 1L;
        List<AdminRoleMenu> roleMenuList = roleMenuRepository.findAllByRoleId(roleId);

        Set<Long> roleMenuSet = roleMenuList.stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toSet());
        Set<Long> menuSet = menuList.stream().map(AdminMenu::getPkid).collect(Collectors.toSet());

        Set<Long> resultSet = new HashSet<Long>();
        resultSet.addAll(menuSet);
        resultSet.removeAll(roleMenuSet);
        for (Long menuId : resultSet) {
            AdminRoleMenu entity = new AdminRoleMenu();
            entity.setMenuId(menuId);
            entity.setRoleId(roleId);
            roleMenuRepository.saveAndFlush(entity);
        }
    }

    /**
     * 角色权限树
     * @param menus
     * @param roleMenuIds
     * @param ignoreButtonMenu
     * @return
     */
    private List<TreeMenu> createRoleMenuTree(List<AdminMenu> menus, List<Long> roleMenuIds, Boolean ignoreButtonMenu){
        List<TreeMenu> list = new ArrayList<TreeMenu>();
        for(AdminMenu menu : menus) {
            Integer isOwn = 0;
            if (!CollectionUtils.isEmpty(roleMenuIds) && roleMenuIds.contains(menu.getPkid())) {
                isOwn = 1;
            }
            //获取路由菜单
            if (ignoreButtonMenu && (!Arrays.asList(MENUTYPE).contains(menu.getMenuType()) || isOwn == 0)) {
                continue;
            }
            TreeMenu tree = new TreeMenu(menu.getPkid(),menu.getMenuName(),menu.getParentId(),menu.getOrderNum(),menu.getPath(),
                    menu.getComponent(),menu.getMenuType(),menu.getVisible(),menu.getStatus(),menu.getPerms(),
                    menu.getIcon(),isOwn, null);
            list.add(tree);
        }
        List<TreeMenu> treeList = new ArrayList<TreeMenu>();
        for(TreeMenu tree : list) {
            //找到根
            if (tree.getParentId() == 0) {
                treeList.add(tree);
            }
            //找到子
            for(TreeMenu treeMenu : list) {
                if (treeMenu.getParentId().equals(tree.getPkid())) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<TreeMenu>());
                    }
                    tree.getChildren().add(treeMenu);
                }
            }
        }
        return treeList;
    }

    //构建前端路由所需要的菜单
    public List<TreeMenu> getRouter(List<Long> menuIds){
        return createRoleMenuTree(menuRepository.findAll(), menuIds, true);
    }

}
