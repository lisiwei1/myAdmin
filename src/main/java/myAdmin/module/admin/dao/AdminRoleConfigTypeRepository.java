package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminRoleConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminRoleConfigTypeRepository extends JpaRepository<AdminRoleConfigType, Long> {

    Long deleteByRoleIdAndConfigTypeIdIn(Long roleId, Set<Long> configTypeId);

    List<AdminRoleConfigType> findByRoleId(Long roleId);

    List<AdminRoleConfigType> findByRoleIdIn(Set<Long> roleIdSet);

    Long deleteByConfigTypeId(Long configTypeId);

}
