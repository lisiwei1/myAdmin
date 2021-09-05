package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminConfigRepository extends JpaRepository<AdminConfig, Long>, JpaSpecificationExecutor<AdminConfig> {

    Long deleteByTypeCode(Integer typeCode);

    List<AdminConfig> findAllByTypeCodeAndConfigKey(Integer typeCode, String configKey);

    List<AdminConfig> findAllByTypeCodeAndConfigKeyAndPkidNot(Integer typeCode, String configKey, Long pkid);

}
