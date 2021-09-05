package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRoleRepository extends JpaRepository<AdminUserRole, Long> {

    List<AdminUserRole> findByUserId(Long userId);

    List<AdminUserRole> deleteByUserId(Long userId);

    Long deleteByRoleId(Long roleId);

}
