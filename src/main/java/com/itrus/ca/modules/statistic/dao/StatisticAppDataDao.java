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
import com.itrus.ca.modules.statistic.entity.StatisticAppData;

/**
 * 日经营统计DAO接口
 * @author ZhangJingtao
 * @version 2014-07-15
 */
public interface StatisticAppDataDao extends StatisticAppDataDaoCustom, CrudRepository<StatisticAppData, Long> {
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface StatisticAppDataDaoCustom extends BaseDao<StatisticAppData> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class StatisticAppDataDaoImpl extends BaseDaoImpl<StatisticAppData> implements StatisticAppDataDaoCustom {

}
