/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.signature.entity.SignatureConfigChargeAgent;

/**
 * 签章计费策略DAO接口
 * @author CYC
 * @version 2016-02-23
 */
public interface SignatureConfigChargeAgentDao extends SignatureConfigChargeAgentDaoCustom, CrudRepository<SignatureConfigChargeAgent, Long> {

	@Modifying
	@Query("delete SignatureConfigChargeAgent where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update SignatureConfigChargeAgent set delFlag='" + SignatureConfigChargeAgent.DEL_FLAG_DELETE + "' where id = ?1")
	public int unDeleteById(Long id);
	
	
	@Query("FROM SignatureConfigChargeAgent")
	public List<SignatureConfigChargeAgent> selectAll();
}

/**
 * DAO自定义接口
 * @author CYC
 */
interface SignatureConfigChargeAgentDaoCustom extends BaseDao<SignatureConfigChargeAgent> {

}

/**
 * DAO自定义接口实现
 * @author CYC
 */
@Component
class SignatureConfigChargeAgentDaoImpl extends BaseDaoImpl<SignatureConfigChargeAgent> implements SignatureConfigChargeAgentDaoCustom {

}
