package myAdmin.module.admin.bean.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Treeselect树结构实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "权限出入参")
public class TreeMenu {

    @ApiModelProperty(value = "表主键")
    private Long pkid;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "父菜单id")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "菜单类型（1目录 2菜单 3按钮）")
    private Integer menuType;

    @ApiModelProperty(value = "菜单状态（0隐藏 1显示）")
    private Integer visible;

    @ApiModelProperty(value = "菜单状态（0停用 1正常）")
    private Integer status;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "是否拥有权限")
    private Integer isOwn;

    @ApiModelProperty(value = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeMenu> children;

}
