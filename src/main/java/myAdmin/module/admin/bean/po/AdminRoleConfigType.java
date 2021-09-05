package myAdmin.module.admin.bean.po;

import java.io.Serializable;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import myAdmin.common.id.IdType;

@Entity
@Data
@ApiModel("角色权限表")
@Table(name="sys_role_config_type")
public class AdminRoleConfigType {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_ROLE_CONFIG_TYPE)
	@TableGenerator(name = IdType.SYS_ROLE_CONFIG_TYPE, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
	@Column(name="PKID")
	@ApiModelProperty(value = "表主键")
    private Long pkid;
	
	@Column(name="RoleId")
	@ApiModelProperty(value = "角色表主键")
	private Long roleId;
	
	@Column(name="ConfigTypeId")
	@ApiModelProperty(value = "参数类型表主键")
	private Long configTypeId;
	
}
