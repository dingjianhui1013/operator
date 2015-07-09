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
import com.itrus.ca.modules.key.entity.KeyDepotGeneralStatistics;

/**
 * 库存统计DAO接口
 * @author HUHAO
 * @version 2014-06-30
 */
public interface KeyDepotGeneralStatisticsDao extends KeyDepotGeneralStatisticsDaoCustom, CrudRepository<KeyDepotGeneralStatistics, Long> {
//
//	@Modifying
//	@Query("update KeyDepotGeneralStatistics set delFlag='" + KeyDepotGeneralStatistics.DEL_FLAG_DELETE + "' where id = ?1")
//	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface KeyDepotGeneralStatisticsDaoCustom extends BaseDao<KeyDepotGeneralStatistics> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class KeyDepotGeneralStatisticsDaoImpl extends BaseDaoImpl<KeyDepotGeneralStatistics> implements KeyDepotGeneralStatisticsDaoCustom {

}
