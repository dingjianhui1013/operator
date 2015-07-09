package com.itrus.ca.modules.profile.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigApp;

/**
 * 应用DAO接口
 * 
 * @author HuHao
 * @version 2014-06-03
 */
public interface ConfigAppDao extends ConfigAppDaoCustom,
		CrudRepository<ConfigApp, Long> {

	@Modifying
	@Query("update ConfigApp set delFlag='" + ConfigApp.DEL_FLAG_DELETE
			+ "' where id = ?1")
	public int deleteById(Long id);

	@Query("from ConfigApp c Where c.id Not IN (SELECT c1.configApp.id from ConfigAgentAppRelation c1)")
	public List<ConfigApp> findAllConfigApp();

	@Modifying
	@Query("Select a from ConfigApp a,ConfigAgentAppRelation c where a.id=c.configApp.id AND c.configCommercialAgent.id= ?1")
	public List<ConfigApp> findByAgentId(Long id);

	@Modifying
	@Query("FROM ConfigApp o where o.id NOT IN (SELECT c.configApp.id from ConfigAgentAppRelation c where c.configCommercialAgent.id!=?1)")
	public List<ConfigApp> findAllById(Long id);

	@Modifying
	@Query("FROM ConfigApp a where a.appName=?1")
	public List<ConfigApp> findAppByName(String name);

	@Query("FROM ConfigApp a where a.delFlag!=1")
	public List<ConfigApp> selectAll();

	@Modifying
	@Query("FROM ConfigApp a where a.appName like ?1")
	public List<ConfigApp> findByAppName(String name);

	@Modifying
	@Query("FROM ConfigApp a where a.id=?1")
	public List<ConfigApp> findById(Long id);

}

/**
 * DAO自定义接口
 * 
 * @author HuHao
 */
interface ConfigAppDaoCustom extends BaseDao<ConfigApp> {

}

/**
 * DAO自定义接口实现
 * 
 * @author HuHao
 */
@Component
class ConfigAppDaoImpl extends BaseDaoImpl<ConfigApp> implements
		ConfigAppDaoCustom {

}
