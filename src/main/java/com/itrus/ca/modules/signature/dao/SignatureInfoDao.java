/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.signature.entity.SignatureInfo;


/**
 * 证书信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-16
 */
public interface SignatureInfoDao extends SignatureInfoDaoCustom, CrudRepository<SignatureInfo, Long> {

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
