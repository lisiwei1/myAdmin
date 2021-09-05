package myAdmin.module.admin.dao;

import myAdmin.module.admin.bean.po.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long> {

    List<AdminMenu> findAllByPkidInAndStatusOrderByParentIdAscOrderNumAsc(Set<Long> pkid, Integer status);

    List<AdminMenu> findByMenuNameAndPkidNot(String menuName, Long pkid);

    List<AdminMenu> findByMenuName(String menuName);

}
