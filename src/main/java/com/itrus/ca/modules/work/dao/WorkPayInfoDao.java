/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkPayInfo;

/**
 * 绑定信息DAO接口
 * @author HUHAO
 * @version 2014-06-13
 */
public interface WorkPayInfoDao extends WorkPayInfoDaoCustom, CrudRepository<WorkPayInfo, Long> {

	@Modifying
	@Query("update WorkPayInfo set delFlag='" + WorkPayInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface WorkPayInfoDaoCustom extends BaseDao<WorkPayInfo> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class WorkPayInfoDaoImpl extends BaseDaoImpl<WorkPayInfo> implements WorkPayInfoDaoCustom {

}
