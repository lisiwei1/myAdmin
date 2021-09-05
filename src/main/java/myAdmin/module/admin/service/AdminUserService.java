package myAdmin.module.admin.service;

import myAdmin.common.id.IdType;
import myAdmin.config.AdminConfigure;
import myAdmin.core.exception.BusinessException;
import myAdmin.module.admin.bean.po.AdminUser;
import myAdmin.module.admin.bean.po.AdminUserRole;
import myAdmin.module.admin.bean.vo.UserResp;
import myAdmin.module.admin.dao.AdminUserRepository;
import myAdmin.module.admin.dao.AdminUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private AdminConfigure adminConfig;
    @Autowired
    private AdminUserRepository userRepository;
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private AdminMenuService adminMenuService;
    @Autowired
    private AdminUserRoleRepository userRoleRepository;
    @Autowired
    private AdminRoleService roleService;


    /**
     * 初始化用户
     */
    public void initUser(){
        Optional<AdminUser> userOptional = userRepository.findById(1l);
        if (userOptional.isPresent()){
            adminMenuService.initAdminAuthority();
            throw new BusinessException("已存在初始化的用户，无需重复初始化");
        }
        AdminUser user = new AdminUser();
        user.setPkid(1L);
        user.setLoginName("admin");
        user.setUserName("超级用户");
        user.setPassword(adminLoginService.encryptionPassword(adminConfig.getInitPassword()));
        user.setStatus(1);
        user.setRemark("初始用户");
        userRepository.save(user);
        adminMenuService.initAdminAuthority();
    }

    public AdminUser getInfo(Long userId) {
        AdminUser user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("不存在此用户"));
        return user;
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        AdminUser user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("找不到当前用户信息"));
        String userName = user.getUserName();
        String md5Password = user.getPassword();
        String pwd = adminLoginService.encryptionPassword(oldPassword);
        if (!pwd.equals(md5Password)) {
            throw new BusinessException("密码错误");
        }
        user.setPassword(adminLoginService.encryptionPassword(newPassword));
        userRepository.save(user);
    }

    //获取所有用户
    public List<AdminUser> getUserList(){
        return userRepository.findAll();
    }

    //查询用户详情
    public UserResp getUserDetail(Long userId) {
        AdminUser user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("找不到当前用户 "));
        List<Long> roleIds = userRoleRepository.findByUserId(user.getPkid()).stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        UserResp resp = new UserResp(user, roleIds);
        return resp;
    }

    //添加用户
    @Transactional
    public AdminUser addUser(String loginName, String userName, String password, Integer status, String remark, List<Long> roleIds) {
        Optional<AdminUser> adminOptional = userRepository.findOneByLoginName(userName);
        if (adminOptional.isPresent()) {
            throw new BusinessException("当前用户已存在");
        }
        AdminUser user = new AdminUser();
        user.setLoginName(loginName);
        user.setUserName(userName);
        user.setPassword(adminLoginService.encryptionPassword(password));
        user.setStatus(status);
        user.setRemark(remark);
        user.setCreateTime(LocalDateTime.now());
        user = userRepository.saveAndFlush(user);

        if (!CollectionUtils.isEmpty(roleIds)) {
            for(Long roleId : roleIds) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getPkid());
                userRoleRepository.save(userRole);
            }
        }
        user.setPassword(password);
        return user;
    }

    //删除用户
    @Transactional
    public void deleteUser(Long userId) {
        AdminUser user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("不存在此用户"));
        //删除用户角色表中的数据
        userRepository.delete(user);
        userRoleRepository.deleteByUserId(userId);
    }

    //编辑用户
    @Transactional
    public UserResp editUser(Long userId, Integer status, String remark, String roleIds, String loginName) {
        AdminUser user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("不存在此用户"));
        user.setStatus(status);
        user.setRemark(remark);
        user.setUpdateBy(loginName);
        user.setUpdateTime(LocalDateTime.now());
        userRepository.saveAndFlush(user);
        List<String> roleIdList = Arrays.asList(roleIds.split(","));
        List<Long> rolePkids = new ArrayList<Long>();
        userRoleRepository.deleteByUserId(userId);
        if (!CollectionUtils.isEmpty(roleIdList)) {
            rolePkids = roleIdList.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
            for(Long roleId : rolePkids) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRoleRepository.save(userRole);
            }
        }
        UserResp resp = new UserResp(user, rolePkids);
        return resp;
    }

    //重置密码
    public String resetPwd(Long pkid, String passowrd) {
        AdminUser user = userRepository.findById(pkid).orElseThrow(
                () -> new BusinessException("不存在此用户"));
        if (pkid == 1L) {
            throw new BusinessException("不允许操作超级管理员用户");
        }
        user.setPassword(adminLoginService.encryptionPassword(passowrd));
        userRepository.saveAndFlush(user);
        return passowrd;
    }

}
