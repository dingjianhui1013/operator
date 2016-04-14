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
import com.itrus.ca.modules.signature.entity.SignatureInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;


/**
 * 证书信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-16
 */
public interface SignatureInfoDao extends SignatureInfoDaoCustom, CrudRepository<SignatureInfo, Long> {

	@Modifying
	@Query("From SignatureInfo w where w.svn like ?1  order by w.createDate desc")
	public List<SignatureInfo> findSvn(String svn);
	
	@Modifying
	@Query("update SignatureInfo set delFlag='" + WorkDealInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public void deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface SignatureInfoDaoCustom extends BaseDao<SignatureInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class SignatureInfoDaoImpl extends BaseDaoImpl<SignatureInfo> implements SignatureInfoDaoCustom {

}
