package myAdmin.module.admin.bean.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import myAdmin.common.id.IdType;

@Entity
@Data
@ApiModel("角色表")
@Table(name="sys_role")
public class AdminRole{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_ROLE)
	@TableGenerator(name = IdType.SYS_ROLE, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
	@Column(name="PKID")
	@ApiModelProperty(value = "角色主键")
    private Long pkid;
	
	@Column(name="RoleName")
	@ApiModelProperty(value = "角色名称")
	private String roleName;
	
	@Column(name="Status")
	@ApiModelProperty(value = "角色状态（0停用 1正常）")
	private Integer status;
	
	@Column(name="OrderNum")
	@ApiModelProperty(value = "显示顺序")
	private Integer orderNum;
	
	@Column(name="CreateBy")
	@ApiModelProperty(value = "创建者")
	private String createBy;
	
	@Column(name="CreateTime")
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@Column(name="UpdateBy")
	@ApiModelProperty(value = "更新人")
	private Long updateBy;

	@Column(name="UpdateTime")
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;
	
	@Column(name="RoleKey")
	@ApiModelProperty(value = "角色权限字符串")
	private String roleKey;
	
	@Column(name="Remark")
	@ApiModelProperty(value = "备注")
	private String remark;

}
