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
import com.itrus.ca.modules.settle.entity.SettlementCollectData;


/**
 * 年限结算保存DAO接口
 * @author DingJianHui
 * @version 2016-03-07
 */
public interface SettlementCollectDataDao extends SettlementCollectDataDaoCustom, CrudRepository<SettlementCollectData, Long> {

	@Modifying
	@Query("update SettlementCollectData set delFlag='" + SettlementCollectData.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
}

/**
 * DAO自定义接口
 * @author DingJianHui
 */
interface SettlementCollectDataDaoCustom extends BaseDao<SettlementCollectData> {

}

/**
 * DAO自定义接口实现
 * @author DingJianHui
 */
@Component
class SettlementCollectDataDaoImpl extends BaseDaoImpl<SettlementCollectData> implements SettlementCollectDataDaoCustom {

}

