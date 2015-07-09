package com.itrus.ca.modules.statistic.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.statistic.entity.StatisticDayData;

/**
 * 日经营管理DAO接口
 * @author CaoYichao
 * @version 2014-07-08
 */
public interface StatisticDayDataDao extends StatisticDayDataDaoCustom, CrudRepository<StatisticDayData, Long> {


	/*@Modifying
	//@Query("update KeyAllocateApply set delFlag='" + StatisticDayData.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);*/



	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface StatisticDayDataDaoCustom extends BaseDao<StatisticDayData> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class StatisticDayDataDaoImpl extends BaseDaoImpl<StatisticDayData> implements StatisticDayDataDaoCustom {

}
