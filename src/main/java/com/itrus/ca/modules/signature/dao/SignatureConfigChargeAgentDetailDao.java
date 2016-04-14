
package com.itrus.ca.modules.signature.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgentDetail;

/**
 * 签章配置DAO接口
 * @author CYC
 * @version 2016-03-14
 */
public interface SignatureConfigChargeAgentDetailDao extends SignatureConfigChargeAgentDetailDaoCustom, CrudRepository<SignatureConfigChargeAgentDetail, Long> {

	@Modifying
	@Query("delete SignatureConfigChargeAgentDetail c where c.signatureConfigChargeAgent.id=?1")
	public void deleteByAgentId(Long id);

}

/**
 * DAO自定义接口
 * @author CYC
 */
interface SignatureConfigChargeAgentDetailDaoCustom extends BaseDao<SignatureConfigChargeAgentDetail> {

}

/**
 * DAO自定义接口实现
 * @author CYC
 */
@Component
class SignatureConfigChargeAgentDetailDaoImpl extends BaseDaoImpl<SignatureConfigChargeAgentDetail> implements SignatureConfigChargeAgentDetailDaoCustom {

}
