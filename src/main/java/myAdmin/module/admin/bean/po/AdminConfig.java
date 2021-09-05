package myAdmin.module.admin.bean.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import myAdmin.common.id.IdType;

@Entity
@Data
@ApiModel("配置表")
@Table(name="sys_config")
public class AdminConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_CONFIG)
	@TableGenerator(name = IdType.SYS_CONFIG, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
	@Column(name="PKID")
	@ApiModelProperty(value = "主键")
    private Long pkid;
	
	@Column(name="TypeCode")
	@ApiModelProperty(value = "参数类型编码")
    private Integer typeCode;
	
	@Column(name="ConfigName")
	@ApiModelProperty(value = "参数名称")
    private String configName;
	
	@Column(name="ConfigKey")
	@ApiModelProperty(value = "参数键名")
	private String configKey;
	
	@Column(name="ConfigValue")
	@ApiModelProperty(value = "参数键值")
	private String configValue;
	
	@Column(name="OrderNum")
	@ApiModelProperty(value = "排序顺序")
	private Integer orderNum;
	
	@Column(name="Status")
	@ApiModelProperty(value = "状态（0停用 1启用）")
	private Integer status;
	
	@Column(name="CreateBy")
	@ApiModelProperty(value = "创建者")
	private String createBy;
	
	@Column(name="CreateTime")
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	
	@Column(name="UpdateBy")
	@ApiModelProperty(value = "更新者")
	private String updateBy;
	
	@Column(name="UpdateTime")
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;
	
	@Column(name="Remark")
	@ApiModelProperty(value = "备注")
	private String remark;
	
}
