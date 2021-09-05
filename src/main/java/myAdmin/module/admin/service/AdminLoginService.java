package myAdmin.module.admin.service;

import com.google.common.collect.Lists;
import myAdmin.config.AdminConfigure;
import myAdmin.core.exception.BusinessException;
import myAdmin.core.request.MyRequestContextHolder;
import myAdmin.module.admin.bean.bo.UserCache;
import myAdmin.module.admin.bean.po.*;
import myAdmin.module.admin.bean.vo.LoginResp;
import myAdmin.module.admin.dao.*;
import myAdmin.util.AESUtil;
import myAdmin.util.ExpiryMap;
import myAdmin.util.Md5Util;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AdminLoginService {

    @Autowired
    private AdminConfigure adminConfig;
    @Autowired
    private AdminUserRepository userRepository;
    @Autowired
    private AdminRoleRepository roleRepository;
    @Autowired
    private AdminMenuRepository menuRepository;
    @Autowired
    private AdminUserRoleRepository userRoleRepository;
    @Autowired
    private AdminRoleMenuRepository roleMenuRepository;

    //用户登录缓存，token和权限
    public static final ExpiryMap<Long, UserCache> UserLoginCache = new ExpiryMap<Long, UserCache>(true);

    //缓存有效期
    private long getCacheExpiresTime(){ return TimeUnit.SECONDS.toMillis(adminConfig.getExpires()); }

    /**
     * 登录
     * @param loginName 登录名
     * @param password  密码
     * @return  结果
     */
    public LoginResp login(String loginName, String password) {
        AdminUser user = userRepository.
                findOneByLoginNameAndPassword(loginName, encryptionPassword(password))
                .orElseThrow(() -> new BusinessException("用户名/密码不对"));
        if(user.getStatus() == 0){
            throw new BusinessException("当前账号已停用");
        }
        //生成token
        Long userId = user.getPkid();
        String token = getToken(userId, password);
        //将用户信息和权限缓存起来，并设置有效期
        List<AdminMenu> menuList = getUserPerm(Long.valueOf(userId));
        Set<String> permSet = menuList.stream().map(AdminMenu::getPerms).collect(Collectors.toSet());
        UserCache userCache = new UserCache();
        userCache.setToken(token);
        userCache.setPermSet(permSet);
        //记录在缓存中
        UserLoginCache.put(userId, userCache, getCacheExpiresTime());
        //获取按钮权限
        List<String> btnPermsList = menuList.stream().filter(m -> m.getMenuType() == 3).map(AdminMenu::getPerms).collect(Collectors.toList());
        //过滤非菜单
        menuList = menuList.stream().filter(m -> m.getMenuType() != 3).collect(Collectors.toList());

        user.setLoginTime(LocalDateTime.now());
        user.setLoginIp(MyRequestContextHolder.getIp());
        userRepository.save(user);
        return new LoginResp(token, user, menuList, btnPermsList);
    }

    /**
     * 登出
     * @param userId
     */
    public void logoutUser(Long userId){
        UserLoginCache.remove(userId);
    }

    //获取用户的权限
    private List<AdminMenu> getUserPerm(Long userId){
        //查找当前用户拥有的角色
        List<AdminUserRole> roleList = userRoleRepository.findByUserId(userId);
        Set<Long> roleIdsSet = roleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toSet());
        //过滤停用的角色
        Set<Long> legalRoleSet = roleRepository.findAllByPkidInAndStatus(roleIdsSet, 1).stream().map(AdminRole::getPkid).collect(Collectors.toSet());
        //查找角色拥有的权限
        List<AdminRoleMenu> roleMenuList = roleMenuRepository.findAllByRoleIdIn(legalRoleSet);
        Set<Long> menuSet = roleMenuList.stream().map(AdminRoleMenu::getMenuId).collect(Collectors.toSet());
        //过滤停用的权限
        List<AdminMenu> menuList = menuRepository.findAllByPkidInAndStatusOrderByParentIdAscOrderNumAsc(menuSet, 1);
        return menuList;
    }

    /**
     * 密码加密
     * @param password
     * @return
     */
    protected String encryptionPassword(String password){
        return Md5Util.MD5(password + adminConfig.getSalt());
    }

    /**
     * 获取token
     * @param userId
     * @return
     */
    private String getToken(Long userId, String password){
        //移除token
        UserLoginCache.remove(userId);
        //重新获取
        String token_original = Md5Util.MD5(password + adminConfig.getSecretKey() + System.currentTimeMillis());
        //将用户信息保存在token中
        return AESUtil.AESEncode(adminConfig.getAesKey(), token_original + "." + userId);
    }

    /**
     * 刷新缓存有效时间
     * @param userId
     * @param userCache
     */
    public void refreshLoginState(Long userId, UserCache userCache){
        UserLoginCache.put(userId, userCache, getCacheExpiresTime());
    }

    /**
     * 查询在线用户
     * @return
     */
    public List<AdminUser> getOnLineUserList(){
        if (UserLoginCache.size() == 0) {
            return Lists.newArrayList();
        }
        Set<Long> userIdSet = new HashSet<>();
        UserLoginCache.forEach((key, value) -> {
            userIdSet.add(key);
        });
        return userRepository.findByPkidIn(userIdSet);
    }
}
