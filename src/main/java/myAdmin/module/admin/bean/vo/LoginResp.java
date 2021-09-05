package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myAdmin.module.admin.bean.po.AdminMenu;
import myAdmin.module.admin.bean.po.AdminUser;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录出参")
public class LoginResp {

    @ApiModelProperty(value = "身份认证临时令牌")
    private String token;

    @ApiModelProperty(value = "用户数据")
    private AdminUser userInfo;

    @ApiModelProperty(value = "路由信息")
    private List<AdminMenu> treeMenus;

    @ApiModelProperty(value = "按钮权限标识")
    private List<String> btnPermsList;

}
