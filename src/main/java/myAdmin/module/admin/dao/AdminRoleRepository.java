package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {

    List<AdminRole> findAllByRoleName(String roleName);

    List<AdminRole> findAllByRoleNameAndPkidNot(String roleName, Long pkid);

    List<AdminRole> findAllByPkidInAndStatus(Set<Long> pkidSet, Integer status);

}
