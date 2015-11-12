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
import com.itrus.ca.modules.settle.entity.KeyPurchase;

/**
 * key采购记录DAO接口
 * @author DingJianHui
 * @version 2015-11-09
 */
public interface KeyPurchaseDao extends KeyPurchaseDaoCustom, CrudRepository<KeyPurchase, Long> {

	@Modifying
	@Query("update KeyPurchase set delFlag='" + KeyPurchase.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author DingJianHui
 */
interface KeyPurchaseDaoCustom extends BaseDao<KeyPurchase> {

}

/**
 * DAO自定义接口实现
 * @author DingJianHui
 */
@Component
class KeyPurchaseDaoImpl extends BaseDaoImpl<KeyPurchase> implements KeyPurchaseDaoCustom {

}
