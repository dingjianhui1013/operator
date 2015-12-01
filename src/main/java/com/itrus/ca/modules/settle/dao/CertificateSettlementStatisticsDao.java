/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.settle.entity.CertificateSettlementStatistics;

/**
 * 证书结算统计表DAO接口
 * @author qt
 * @version 2015-11-22
 */
public interface CertificateSettlementStatisticsDao extends CertificateSettlementStatisticsDaoCustom, CrudRepository<CertificateSettlementStatistics, Long> {

	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface CertificateSettlementStatisticsDaoCustom extends BaseDao<CertificateSettlementStatistics> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class CertificateSettlementStatisticsDaoImpl extends BaseDaoImpl<CertificateSettlementStatistics> implements CertificateSettlementStatisticsDaoCustom {

}
