package myAdmin.module.admin.bean.po;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import myAdmin.common.id.IdType;

@Entity
@Data
@Table(name="sys_user_role")
public class AdminUserRole {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = IdType.SYS_USER_ROLE)
	@TableGenerator(name = IdType.SYS_USER_ROLE, table = IdType.Sequence_Table_Name, allocationSize = 1, pkColumnName = IdType.Sequence_Pk_Column_Name, valueColumnName = IdType.Sequence_Value_Column_Name)
	@Column(name="PKID")
    private Long pkid;

	//用户主键
	@Column(name="UserId")
	private Long userId;
	
	//角色主键
	@Column(name="RoleId")
	private Long roleId;
}
