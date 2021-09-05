package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myAdmin.module.admin.bean.po.AdminUser;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户详情")
public class UserResp {

    @ApiModelProperty(value = "用户详情")
    private AdminUser user;

    @ApiModelProperty(value = "角色主键")
    private List<Long> roleIds;

}
