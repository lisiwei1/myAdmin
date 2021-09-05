package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminUser;
import myAdmin.module.admin.bean.po.AdminUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findOneByLoginNameAndPassword(String loginName, String password);

    Optional<AdminUser> findOneByLoginName(String loginName);

    List<AdminUser> findByPkidIn(Set<Long> pkidSet);
}
