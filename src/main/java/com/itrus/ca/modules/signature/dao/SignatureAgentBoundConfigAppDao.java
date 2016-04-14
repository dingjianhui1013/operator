/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;

/**
 * 计费策略模版详情历史DAO接口
 * @author HuHao
 * @version 2016-03-20
 */
public interface SignatureAgentBoundConfigAppDao extends SignatureAgentBoundConfigAppDaoCustom, CrudRepository<SignatureAgentBoundConfigApp, Long> {

	@Modifying
	@Query("update SignatureAgentBoundConfigApp set delFlag='" + SignatureAgentBoundConfigApp.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("delete from  SignatureAgentBoundConfigApp where id = ?1")
	public int deleteId(Long id);
	
	
	
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface SignatureAgentBoundConfigAppDaoCustom extends BaseDao<SignatureAgentBoundConfigApp> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class SignatureAgentBoundConfigAppDaoImpl extends BaseDaoImpl<SignatureAgentBoundConfigApp> implements SignatureAgentBoundConfigAppDaoCustom {

}
