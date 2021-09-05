package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "添加用户出入参")
public class UserReq {

    @NotNull
    @ApiModelProperty(value = "用户名")
    private String loginName;

    @NotNull
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "用户密码")
    private String password;

    @NotNull
    @ApiModelProperty(value = "帐号状态（0停用 1正常）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色主键集合")
    private List<Long> roleIds;

}
