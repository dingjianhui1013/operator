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
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;

/**
 * 发票信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface ReceiptDepotInfoDao extends ReceiptDepotInfoDaoCustom, CrudRepository<ReceiptDepotInfo, Long> {

	@Modifying
	@Query("update ReceiptDepotInfo set delFlag='" + ReceiptDepotInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface ReceiptDepotInfoDaoCustom extends BaseDao<ReceiptDepotInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class ReceiptDepotInfoDaoImpl extends BaseDaoImpl<ReceiptDepotInfo> implements ReceiptDepotInfoDaoCustom {

}
