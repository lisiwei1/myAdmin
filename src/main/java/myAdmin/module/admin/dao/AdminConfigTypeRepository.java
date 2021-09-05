package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminConfigType;
import myAdmin.module.admin.bean.po.AdminRoleConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AdminConfigTypeRepository extends JpaRepository<AdminConfigType, Long> {

    List<AdminConfigType> findAllByTypeCode(Integer typeCode);

    List<AdminConfigType> findAllByTypeCodeAndPkidNot(Integer typeCode, Long pkid);

    List<AdminConfigType> findAllByOrderByOrderNumAsc();

    List<AdminConfigType> findAllByPkidInOrderByOrderNumAsc(Set<Long> pkid);

    Optional<AdminConfigType> findOneByTypeCode(Integer typeCode);
}
