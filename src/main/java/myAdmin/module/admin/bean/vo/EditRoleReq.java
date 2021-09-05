package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "编辑角色入参")
public class EditRoleReq {

    @NotNull
    @ApiModelProperty(value = "角色主键", required = true)
    private Long roleId;

    @NotNull
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @NotNull
    @ApiModelProperty(value = "角色状态（0停用 1正常）", required = true)
    private Integer status;

    @ApiModelProperty(value = "权限主键集合")
    private List<Long> menuIds;

    @Column(name="Remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
