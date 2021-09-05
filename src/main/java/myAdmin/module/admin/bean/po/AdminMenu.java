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
@ApiModel("权限表")
@Table(name="sys_menu")
public class AdminMenu {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_MENU)
	@TableGenerator(name = IdType.SYS_MENU, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
	@Column(name="PKID")
	@ApiModelProperty(value = "表主键")
    private Long pkid;

	@Column(name="MenuName")
	@ApiModelProperty(value = "菜单名称")
	private String menuName;
	
	@Column(name="ParentId")
	@ApiModelProperty(value = "父菜单id")
	private Long parentId;
	
	@Column(name="OrderNum")
	@ApiModelProperty(value = "显示顺序")
	private Integer orderNum;
	
	@Column(name="Path")
	@ApiModelProperty(value = "路由地址")
	private String path;
	
	@Column(name="Component")
	@ApiModelProperty(value = "组件路径")
	private String component;
	
	@Column(name="MenuType")
	@ApiModelProperty(value = "菜单类型（1目录 2菜单 3按钮）")
	private Integer menuType;
	
	@Column(name="Visible")
	@ApiModelProperty(value = "菜单状态（0隐藏 1显示）")
	private Integer visible;
	
	@Column(name="Status")
	@ApiModelProperty(value = "菜单状态（0停用 1正常）")
	private Integer status;
	
	@Column(name="Perms")
	@ApiModelProperty(value = "权限标识")
	private String perms;
	
	@Column(name="Icon")
	@ApiModelProperty(value = "菜单图标")
	private String icon;

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
	
}
