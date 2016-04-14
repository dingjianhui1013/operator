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
import com.itrus.ca.modules.signature.entity.SignaturePayInfo;

/**
 * 计费策略模版详情历史DAO接口
 * @author HuHao
 * @version 2016-03-23
 */
public interface SignaturePayInfoDao extends SignaturePayInfoDaoCustom, CrudRepository<SignaturePayInfo, Long> {

	@Modifying
	@Query("update SignaturePayInfo set delFlag='" + SignaturePayInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface SignaturePayInfoDaoCustom extends BaseDao<SignaturePayInfo> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class SignaturePayInfoDaoImpl extends BaseDaoImpl<SignaturePayInfo> implements SignaturePayInfoDaoCustom {

}
