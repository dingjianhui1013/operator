/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.statistic.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.statistic.entity.StatisticSealAppData;

/**
 * 印章日经营详细表DAO接口
 * @author DingJianHui
 * @version 2016-03-23
 */
public interface StatisticSealAppDataDao extends StatisticSealAppDataDaoCustom, CrudRepository<StatisticSealAppData, Long> {

//	@Modifying
//	@Query("update StatisticSealAppData set delFlag='" + StatisticSealAppData.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author DingJianHui
 */
interface StatisticSealAppDataDaoCustom extends BaseDao<StatisticSealAppData> {

}

/**
 * DAO自定义接口实现
 * @author DingJianHui
 */
@Component
class StatisticSealAppDataDaoImpl extends BaseDaoImpl<StatisticSealAppData> implements StatisticSealAppDataDaoCustom {

}
