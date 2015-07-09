/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.key.entity.KeyUsbKeyDepot;

/**
 * key入库信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface KeyUsbKeyDepotDao extends KeyUsbKeyDepotDaoCustom, CrudRepository<KeyUsbKeyDepot, Long> {

	@Modifying
	@Query("update KeyUsbKeyDepot set delFlag='" + KeyUsbKeyDepot.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface KeyUsbKeyDepotDaoCustom extends BaseDao<KeyUsbKeyDepot> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class KeyUsbKeyDepotDaoImpl extends BaseDaoImpl<KeyUsbKeyDepot> implements KeyUsbKeyDepotDaoCustom {

}
