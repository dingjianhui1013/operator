/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.ixin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.common.persistence.BaseEntity;
import com.itrus.ca.modules.ixin.entity.IxinData;

/**
 * IXIN数据采集DAO接口
 * @author HuHao
 * @version 2016-07-14
 */
public interface IxinDataDao extends IxinDataDaoCustom, CrudRepository<IxinData, Long> {

	@Modifying
	@Query("update IxinData set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface IxinDataDaoCustom extends BaseDao<IxinData> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class IxinDataDaoImpl extends BaseDaoImpl<IxinData> implements IxinDataDaoCustom {

}
