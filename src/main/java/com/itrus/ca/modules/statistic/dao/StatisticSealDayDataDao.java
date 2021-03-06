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
import com.itrus.ca.modules.statistic.entity.StatisticSealDayData;

/**
 * 印章日经营DAO接口
 * @author DingJianHui
 * @version 2016-03-23
 */
public interface StatisticSealDayDataDao extends StatisticSealDayDataDaoCustom, CrudRepository<StatisticSealDayData, Long> {

//	@Modifying
//	@Query("update StatisticSealDayData set delFlag='" + StatisticSealDayData.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
//	
}

/**
 * DAO自定义接口
 * @author DingJianHui
 */
interface StatisticSealDayDataDaoCustom extends BaseDao<StatisticSealDayData> {

}

/**
 * DAO自定义接口实现
 * @author DingJianHui
 */
@Component
class StatisticSealDayDataDaoImpl extends BaseDaoImpl<StatisticSealDayData> implements StatisticSealDayDataDaoCustom {

}
