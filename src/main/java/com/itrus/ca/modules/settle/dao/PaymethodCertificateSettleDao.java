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
import com.itrus.ca.modules.settle.entity.PaymethodCertificateSettle;

/**
 * 支付方式证书结算DAO接口
 * @author qt
 * @version 2016-01-31
 */
public interface PaymethodCertificateSettleDao extends PaymethodCertificateSettleDaoCustom, CrudRepository<PaymethodCertificateSettle, Long> {

	@Modifying
	@Query("update PaymethodCertificateSettle set delFlag='" + PaymethodCertificateSettle.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface PaymethodCertificateSettleDaoCustom extends BaseDao<PaymethodCertificateSettle> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class PaymethodCertificateSettleDaoImpl extends BaseDaoImpl<PaymethodCertificateSettle> implements PaymethodCertificateSettleDaoCustom {

}
