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
import com.itrus.ca.modules.statistic.entity.StatisticCertData;

/**
 * 证书发放统计DAO接口
 * @author HUHAO
 * @version 2014-07-08
 */
public interface StatisticCertDataDao extends StatisticCertDataDaoCustom, CrudRepository<StatisticCertData, Long> {

	//@Modifying
	//@Query("update StatisticCertData set delFlag='" + StatisticCertData.DEL_FLAG_DELETE + "' where id = ?1")
	//public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface StatisticCertDataDaoCustom extends BaseDao<StatisticCertData> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class StatisticCertDataDaoImpl extends BaseDaoImpl<StatisticCertData> implements StatisticCertDataDaoCustom {

}
