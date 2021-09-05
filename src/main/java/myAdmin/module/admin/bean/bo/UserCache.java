package myAdmin.module.admin.bean.bo;

import lombok.Data;

import java.util.Set;

/**
 * 用户登录缓存
 */
@Data
public class UserCache {

    //用户登录获取的token
    private String token;

    //用户登录时获取到的权限
    private Set<String> permSet;

}
