package myAdmin.module.admin.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myAdmin.module.admin.bean.po.AdminRole;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色出参")
public class RoleResp {

    @ApiModelProperty(value = "角色主键")
    private AdminRole role;

    @ApiModelProperty(value = "权限主键集合")
    private List<Long> menuIds;

}
