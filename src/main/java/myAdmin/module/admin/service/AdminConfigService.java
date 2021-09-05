package myAdmin.module.admin.service;

import myAdmin.common.id.IdType;
import myAdmin.core.exception.BusinessException;
import myAdmin.module.admin.bean.po.AdminConfig;
import myAdmin.module.admin.bean.po.AdminConfigType;
import myAdmin.module.admin.bean.po.AdminRoleConfigType;
import myAdmin.module.admin.bean.po.AdminUserRole;
import myAdmin.module.admin.dao.AdminConfigRepository;
import myAdmin.module.admin.dao.AdminConfigTypeRepository;
import myAdmin.module.admin.dao.AdminRoleConfigTypeRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class AdminConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminConfigService.class);

    @Autowired
    private AdminConfigTypeRepository configTypeRepository;
    @Autowired
    private AdminConfigRepository configRepository;
    @Autowired
    private AdminRoleConfigTypeRepository roleConfigTypeRepository;
    @Autowired
    private AdminRoleService roleService;

    // 配置表数据缓存
    private static final ConcurrentMap<Integer, ConcurrentMap<String, AdminConfig>> configCache = new ConcurrentHashMap<>();

    // 查询配置表数据并缓存
    public void actionConfigToCache() {
        List<AdminConfig> configs = configRepository.findAll();
        LOGGER.info("查询到" + configs.size() + "条配置表数据");
        try {
            configCache.clear();
            if (!CollectionUtils.isEmpty(configs)) {
                for (AdminConfig config : configs) {
                    if (configCache.containsKey(config.getTypeCode())) {
                        ConcurrentMap<String, AdminConfig> map = configCache.get(config.getTypeCode());
                        map.put(config.getConfigKey(), config);
                    } else {
                        ConcurrentMap<String, AdminConfig> map = new ConcurrentHashMap<>();
                        map.put(config.getConfigKey(), config);
                        configCache.put(config.getTypeCode(), map);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("缓存配置表数据失败", e);
        }
    }

    // 从缓存中读取配置数据
    public static List<AdminConfig> getConfigValueFromCache(Integer typeCode, String configKey) {
        ConcurrentMap<String, AdminConfig> map = configCache.get(typeCode);
        if (map == null || map.isEmpty()) {
            throw new BusinessException("没有找到配置!");
        }
        List<AdminConfig> reList = new ArrayList<>();
        if (StringUtils.isBlank(configKey)) {
            // 遍历
            for (String key : map.keySet()) {
                reList.add(map.get(key));
            }
            return reList;
        }
        AdminConfig config = map.get(configKey);
        if (ObjectUtils.isEmpty(config)) {
            throw new BusinessException("没有找到配置!");
        }
        reList.add(map.get(configKey));
        return reList;
    }

    public static List<AdminConfig> getConfigValueFromCache(Integer typeCode) {
        return getConfigValueFromCache(typeCode, null);
    }

    // 从缓存获取配置的值
    public static String getSingleConfigValue(Integer typeCode, String configKey) {
        ConcurrentMap<String, AdminConfig> map = configCache.get(typeCode);
        AdminConfig config = map.get(configKey);
        if (ObjectUtils.isEmpty(config)) {
            throw new BusinessException("没有找到配置!");
        }
        return config.getConfigValue();
    }

    // 从缓存获取配置的状态
    public static boolean getSingleConfigStatus(Integer typeCode, String configKey) {
        ConcurrentMap<String, AdminConfig> map = configCache.get(typeCode);
        AdminConfig config = map.get(configKey);
        if (ObjectUtils.isEmpty(config)) {
            throw new BusinessException("没有找到配置!");
        }
        Integer status = config.getStatus();
        if (status == 1) {
            return true;
        }else if (status == 0) {
            return false;
        }else {
            throw new BusinessException("配置处于未知状态！");
        }
    }

    // 刷新配置缓存
    public void refreshCache(AdminConfig config) {
        if (configCache.containsKey(config.getTypeCode())) {
            ConcurrentMap<String, AdminConfig> map = configCache.get(config.getTypeCode());
            map.put(config.getConfigKey(), config);
        } else {
            ConcurrentMap<String, AdminConfig> map = new ConcurrentHashMap<>();
            map.put(config.getConfigKey(), config);
            configCache.put(config.getTypeCode(), map);
        }
    }

    // 删除缓存
    public void removeCacheData(Integer typeCode, String configKey) {
        ConcurrentMap<String, AdminConfig> map = configCache.get(typeCode);
        if (map != null && !map.isEmpty()) {
            map.remove(configKey);
            if (map == null || map.isEmpty()) {
                configCache.remove(typeCode);
            }
        }
    }

    // 删除缓存
    public void removeCacheData(Integer typeCode) {
        configCache.remove(typeCode);
    }

    // 查询参数类型表
    public List<AdminConfigType> getTypeList() {
        return configTypeRepository.findAllByOrderByOrderNumAsc();
    }

    // 查询角色的参数类型
    public List<AdminConfigType> getTypeListByUser(Long userId) {
        //查询当前用户拥有的所有角色
        List<AdminUserRole> userRoleList = roleService.getRoleByUserId(userId);
        Set<Long> roleIds = userRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toSet());
        //查询角色所拥有的参数类型
        List<AdminRoleConfigType> list = roleConfigTypeRepository.findByRoleIdIn(roleIds);
        Set<Long> pkidSet = list.stream().map(AdminRoleConfigType::getConfigTypeId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(pkidSet)) {
            return Collections.emptyList();
        }
        return configTypeRepository.findAllByPkidInOrderByOrderNumAsc(pkidSet);
    }

    // 新增参数类型
    public AdminConfigType addConfigType(Integer typeCode, String typeName, Integer orderNum, String remark,
                                        String createBy) {
        List<AdminConfigType> typeList = configTypeRepository.findAllByTypeCode(typeCode);
        if (!CollectionUtils.isEmpty(typeList)) {
            throw new BusinessException("类型不能重复！");
        }
        AdminConfigType entity = new AdminConfigType();
        entity.setTypeCode(typeCode);
        entity.setTypeName(typeName);
        entity.setRemark(remark);
        entity.setOrderNum(orderNum);
        entity.setCreateBy(createBy);
        entity.setCreateTime(LocalDateTime.now());
        return configTypeRepository.save(entity);
    }

    // 删除参数类型
    @Transactional
    public void deleteConfigType(Long pkid) {
        AdminConfigType entity = configTypeRepository.findById(pkid)
                .orElseThrow(() -> new BusinessException("当前要删除的数据不存在！"));
        // 删除此类型下的所有参数
        configRepository.deleteByTypeCode(entity.getTypeCode());
        configTypeRepository.delete(entity);
        //删除角色参数表对应的数据
        roleConfigTypeRepository.deleteByConfigTypeId(pkid);
        // 删除缓存
        removeCacheData(entity.getTypeCode());
    }

    // 参数类型详情
    public AdminConfigType getConfigTypeDetail(Long pkid) {
        return configTypeRepository.findById(pkid).orElseThrow(() -> new BusinessException("当前数据不存在！"));
    }

    public AdminConfigType getConfigTypeDetail(Integer code) {
        return configTypeRepository.findOneByTypeCode(code).orElseThrow(() -> new BusinessException("当前数据不存在！"));
    }

    // 修改参数类型
    public AdminConfigType editConfigType(Long pkid, Integer typeCode, String typeName, Integer orderNum, String remark,
                                         String updateBy) {
        AdminConfigType entity = configTypeRepository.findById(pkid)
                .orElseThrow(() -> new BusinessException("当前要编辑的数据不存在！"));
        List<AdminConfigType> entityList = configTypeRepository.findAllByTypeCodeAndPkidNot(typeCode, pkid);
        if (!CollectionUtils.isEmpty(entityList)) {
            throw new BusinessException("类型不能重复！");
        }
        entity.setTypeCode(typeCode);
        entity.setTypeName(typeName);
        entity.setOrderNum(orderNum);
        entity.setRemark(remark);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(LocalDateTime.now());
        return configTypeRepository.save(entity);
    }

    // 查询参数
    public List<AdminConfig> getList(Integer typeCode, Integer status) {
        Specification<AdminConfig> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (null != typeCode) {
                predicates.add(criteriaBuilder.equal(root.get("typeCode"), typeCode));
            }
            if (null != status) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            // 将多条件连接在一起
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return configRepository.findAll(spec);
    }

    // 添加参数
    @Transactional
    public AdminConfig addConfig(Integer typeCode, String configName, String configKey, String configValue,
                                Integer orderNum, Integer status, String createBy, String remark) {
        configTypeRepository.findOneByTypeCode(typeCode).orElseThrow(() -> new BusinessException("参数类型表的类型主键不存在"));
        List<AdminConfig> configList = configRepository.findAllByTypeCodeAndConfigKey(typeCode, configKey);
        if (!CollectionUtils.isEmpty(configList)) {
            throw new BusinessException("参数键值不能重复");
        }
        AdminConfig config = new AdminConfig();
        config.setTypeCode(typeCode);
        config.setConfigName(configName);
        config.setConfigKey(configKey);
        config.setConfigValue(configValue);
        config.setOrderNum(orderNum);
        config.setStatus(status);
        config.setCreateBy(createBy);
        config.setCreateTime(LocalDateTime.now());
        config.setRemark(remark);
        AdminConfig newConfig = configRepository.save(config);
        // 刷新缓存
        refreshCache(config);
        return newConfig;
    }

    // 删除参数
    @Transactional
    public void deleteConfig(Long pkid) {
        AdminConfig config = configRepository.findById(pkid).orElseThrow(() -> new BusinessException("要删除的参数不存在"));
        configRepository.delete(config);
        // 刷新缓存
        removeCacheData(config.getTypeCode(), config.getConfigKey());
    }

    // 参数详情
    public AdminConfig getConfigDetail(Long pkid) {
        return configRepository.findById(pkid).orElseThrow(() -> new BusinessException("当前要编辑的数据不存在！"));
    }

    // 修改参数
    @Transactional
    public AdminConfig editConfig(Long pkid, String configName, String configKey, String configValue, Integer orderNum,
                                 Integer status, String updateBy, String remark) {
        AdminConfig config = configRepository.findById(pkid).orElseThrow(() -> new BusinessException("当前要编辑的数据不存在！"));
        List<AdminConfig> configList = configRepository.findAllByTypeCodeAndConfigKeyAndPkidNot(config.getTypeCode(),
                configKey, pkid);
        if (!CollectionUtils.isEmpty(configList)) {
            throw new BusinessException("参数键值不能重复");
        }
        config.setConfigName(configName);
        config.setConfigKey(configKey);
        config.setConfigValue(configValue);
        config.setOrderNum(orderNum);
        config.setStatus(status);
        config.setUpdateBy(updateBy);
        config.setUpdateTime(LocalDateTime.now());
        config.setRemark(remark);
        configRepository.save(config);
        // 刷新缓存
        refreshCache(config);
        return config;
    }

    @Transactional
    public void editConfigDeatilStatus(Long pkid, Integer status, String userName) {
        AdminConfig config = configRepository.findById(pkid).orElseThrow(() -> new BusinessException("没有找到对应的数据"));
        config.setStatus(status);
        config.setUpdateBy(userName);
        config.setUpdateTime(LocalDateTime.now());
        configRepository.save(config);
        // 刷新缓存
        refreshCache(config);
    }

}
