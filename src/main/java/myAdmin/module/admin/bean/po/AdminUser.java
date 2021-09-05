package myAdmin.module.admin.bean.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import myAdmin.common.id.IdType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@ApiModel("用户表")
@Table(name="sys_user")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_USER)
    @TableGenerator(name = IdType.SYS_USER, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
    @Column(name="PKID")
    @ApiModelProperty(value = "用户主键")
    private Long pkid;

    @Column(name="LoginName")
    @ApiModelProperty(value = "登录名")
    private String loginName;

    @Column(name="UserName")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Column(name="Password")
    @ApiModelProperty(value = "用户密码")
    private String password;

    @Column(name="Status")
    @ApiModelProperty(value = "帐号状态（0停用 1正常）")
    private Integer status;

    @Column(name="LoginIp")
    @ApiModelProperty(value = "最近登录IP")
    private String loginIp;

    @Column(name="LoginTime")
    @ApiModelProperty(value = "最近登录时间")
    private LocalDateTime loginTime;

    @Column(name="createBy")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @Column(name="CreateTime")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @Column(name="UpdateBy")
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @Column(name="UpdateTime")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @Column(name="Remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
