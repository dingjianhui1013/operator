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
import com.itrus.ca.modules.statistic.entity.StatisticCertDataProduct;

/**
 * 证书发放统计DAO接口
 * @author ZhangJingtao
 * @version 2014-07-16
 */
public interface StatisticCertDataProductDao extends StatisticCertDataProductDaoCustom, CrudRepository<StatisticCertDataProduct, Long> {
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface StatisticCertDataProductDaoCustom extends BaseDao<StatisticCertDataProduct> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class StatisticCertDataProductDaoImpl extends BaseDaoImpl<StatisticCertDataProduct> implements StatisticCertDataProductDaoCustom {

}
