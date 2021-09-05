package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminRoleMenuRepository extends JpaRepository<AdminRoleMenu, Long> {

    List<AdminRoleMenu> findAllByRoleIdIn(Set<Long> roleId);

    List<AdminRoleMenu> findAllByRoleId(Long roleId);

    Long deleteByRoleId(Long roleId);

    Long deleteByRoleIdAndMenuIdIn(Long roleId, Set<Long> menuIds);

    void deleteByMenuId(Long menuId);

}
