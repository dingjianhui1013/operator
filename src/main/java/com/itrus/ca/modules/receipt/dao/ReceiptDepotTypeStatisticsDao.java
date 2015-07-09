/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.receipt.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotTypeStatistics;

/**
 * 发票库存详情DAO接口
 * @author HuHao
 * @version 2015-03-18
 */
public interface ReceiptDepotTypeStatisticsDao extends ReceiptDepotTypeStatisticsDaoCustom, CrudRepository<ReceiptDepotTypeStatistics, Long> {

	@Modifying
	@Query("update ReceiptDepotTypeStatistics set delFlag='" + ReceiptDepotTypeStatistics.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ReceiptDepotTypeStatisticsDaoCustom extends BaseDao<ReceiptDepotTypeStatistics> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ReceiptDepotTypeStatisticsDaoImpl extends BaseDaoImpl<ReceiptDepotTypeStatistics> implements ReceiptDepotTypeStatisticsDaoCustom {

}
