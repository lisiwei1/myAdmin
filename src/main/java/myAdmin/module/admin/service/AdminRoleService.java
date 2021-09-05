package myAdmin.module.admin.service;

import myAdmin.common.id.IdType;
import myAdmin.core.exception.BusinessException;
import myAdmin.module.admin.bean.po.AdminRole;
import myAdmin.module.admin.bean.po.AdminRoleConfigType;
import myAdmin.module.admin.bean.po.AdminRoleMenu;
import myAdmin.module.admin.bean.po.AdminUserRole;
import myAdmin.module.admin.bean.vo.RoleResp;
import myAdmin.module.admin.dao.AdminRoleConfigTypeRepository;
import myAdmin.module.admin.dao.AdminRoleMenuRepository;
import myAdmin.module.admin.dao.AdminRoleRepository;
import myAdmin.module.admin.dao.AdminUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleRepository roleRepository;
    @Autowired
    private AdminRoleMenuRepository roleMenuRepository;
    @Autowired
    private AdminUserRoleRepository userRoleRepository;
    @Autowired
    private AdminRoleConfigTypeRepository roleConfigTypeRepository;

    /**
     * 查所有角色
     * @return
     */
    public List<AdminRole> getRoleList(){
        return roleRepository.findAll();
    }

    /**
     * 查询指定角色详情
     * @param pkid
     * @return
     */
    public RoleResp getRoleDetail(Long pkid) {
        AdminRole role = roleRepository.findById(pkid).orElseThrow(() -> new BusinessException("不存在此角色"));
        List<Long> menuIds = roleMenuRepository.findAllByRoleId(pkid).stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toList());
        RoleResp resp = new RoleResp(role, menuIds);
        return resp;
    }

    /**
     * 查询指定用户拥有的角色
     * @param userId
     * @return
     */
    public List<AdminUserRole> getRoleByUserId(Long userId){
        return userRoleRepository.findByUserId(userId);
    }

    /**
     * 添加角色
     * @param roleKey
     * @param roleName
     * @param status
     * @param orderNum
     * @param menuIds
     * @param remark
     */
    @Transactional
    public void addRole(String roleKey, String roleName, Integer status, Integer orderNum, List<Long> menuIds, String remark) {
        List<AdminRole> roleList = roleRepository.findAllByRoleName(roleName);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException("当前角色已存在");
        }
        AdminRole role = new AdminRole();
        role.setRoleName(roleName);
        role.setStatus(status);
        role.setRoleKey(roleKey);
        role.setOrderNum(orderNum);
        role.setCreateBy(null);
        role.setCreateTime(LocalDateTime.now());
        role.setRemark(remark);
        roleRepository.save(role);
        //指定角色添加权限
        addRoleMenu(role.getPkid(), menuIds);
    }

    /**
     * 删除角色
     * @param pkid
     * @return
     */
    @Transactional
    public AdminRole deleteRole(Long pkid) {
        AdminRole role = roleRepository.findById(pkid).orElseThrow(() -> new BusinessException("不存在此角色"));
        //删除角色所有权限
        roleMenuRepository.deleteByRoleId(pkid);
        //删除用户角色表中的数据
        userRoleRepository.deleteByRoleId(pkid);
        //删除角色
        roleRepository.delete(role);
        return role;
    }

    /**
     * 编辑角色
     * @param roleId
     * @param roleName
     * @param roleKey
     * @param orderNum
     * @param status
     * @param menuIds
     * @param remark
     */
    @Transactional
    public void editRole(Long roleId, String roleName, String roleKey, Integer orderNum, Integer status, List<Long> menuIds, String remark) {
        List<AdminRole> roleList = roleRepository.findAllByRoleNameAndPkidNot(roleName, roleId);
        if (!CollectionUtils.isEmpty(roleList)) {
            throw new BusinessException("当前角色名称已存在");
        }
        AdminRole role = roleRepository.findById(roleId).orElseThrow(() -> new BusinessException("不存在此角色"));
        role.setRoleName(roleName);
        role.setRoleKey(roleKey);
        role.setOrderNum(orderNum);
        role.setStatus(status);
        role.setRemark(remark);
        roleRepository.save(role);

        //指定角色添加权限
        addRoleMenu(roleId, menuIds);
    }

    //指定角色添加权限
    @Transactional
    private void addRoleMenu(Long roleId,List<Long> menuIds) {
        //查询当前角色的所有权限
        List<AdminRoleMenu> roleMenuList = roleMenuRepository.findAllByRoleId(roleId);
        Set<Long> oldMenuIdSet = roleMenuList.stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toSet());
        Set<Long> newMenuIdSet = new HashSet<>(menuIds);

        //删除的权限
        Set<Long> minSet = new HashSet<>(oldMenuIdSet);
        minSet.removeAll(newMenuIdSet);
        roleMenuRepository.deleteByRoleIdAndMenuIdIn(roleId, minSet);
        //新增的权限
        Set<Long> addSet = new HashSet<>(newMenuIdSet);
        addSet.removeAll(oldMenuIdSet);
        for (Long menuId : addSet) {
            AdminRoleMenu entity = new AdminRoleMenu();
            entity.setMenuId(menuId);
            entity.setRoleId(roleId);
            roleMenuRepository.saveAndFlush(entity);
        }
    }

    /**
     * 查询角色的参数类型权限
     * @param roleId
     * @return
     */
    public List<AdminRoleConfigType> getRoleConfigTypeDetail(Long roleId){
        return roleConfigTypeRepository.findByRoleId(roleId);
    }

    /**
     * 保存角色的参数类型权限
     * @param roleId
     * @param configIds
     */
    @Transactional
    public void editRoleConfigType(Long roleId, List<Long> configIds) {
        //查询当前角色的所拥有的参数类型
        List<AdminRoleConfigType> list = roleConfigTypeRepository.findByRoleId(roleId);
        Set<Long> oldConfigIdSet = list.stream().map(AdminRoleConfigType::getConfigTypeId).collect(Collectors.toSet());
        Set<Long> newConfigIdSet = new HashSet<>(configIds);

        //删除
        Set<Long> minSet = new HashSet<>(oldConfigIdSet);
        minSet.removeAll(newConfigIdSet);
        roleConfigTypeRepository.deleteByRoleIdAndConfigTypeIdIn(roleId, minSet);
        //添加
        Set<Long> addSet = new HashSet<>(newConfigIdSet);
        addSet.removeAll(oldConfigIdSet);
        for (Long configId : addSet) {
            AdminRoleConfigType entity = new AdminRoleConfigType();
            entity.setConfigTypeId(configId);
            entity.setRoleId(roleId);
            roleConfigTypeRepository.saveAndFlush(entity);
        }
    }

    //创建超级用户
    public void initRole(){
        Optional<AdminRole> roleOptional = roleRepository.findById(1L);
        if(roleOptional.isPresent()){
            return;
        }
        AdminRole role = new AdminRole();
        role.setPkid(1L);
        role.setRoleName("超级用户");
        role.setStatus(1);
        role.setOrderNum(1);
        role.setRoleKey("admin");
        roleRepository.save(role);
    }
}
