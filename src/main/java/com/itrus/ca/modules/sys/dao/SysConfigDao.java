/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.sys.entity.SysConfig;

/**
 * 邮件配置DAO接口
 * @author ZhangShaoBo
 * @version 2014-06-03
 */
public interface SysConfigDao extends SysConfigDaoCustom, CrudRepository<SysConfig, Long> {

	@Modifying
	@Query("update SysConfig set delFlag='" + SysConfig.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Query("from SysConfig where type in ?1")
	public List<SysConfig> selectByTypeConfig(List<String> type);

	@Query("from SysConfig where type='sms_url' OR type='sms_user_name' OR type='sms_user_pass'")
	public List<SysConfig> selectByTypeConfig();
}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface SysConfigDaoCustom extends BaseDao<SysConfig> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class SysConfigDaoImpl extends BaseDaoImpl<SysConfig> implements SysConfigDaoCustom {

}
