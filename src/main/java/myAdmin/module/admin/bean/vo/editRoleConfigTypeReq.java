package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "编辑角色参数类型入参")
public class editRoleConfigTypeReq {

    @NotNull
    @ApiModelProperty(value = "角色主键", required = true)
    private Long roleId;

    @ApiModelProperty(value = "参数类型主键集合")
    private List<Long> configIds;

}
