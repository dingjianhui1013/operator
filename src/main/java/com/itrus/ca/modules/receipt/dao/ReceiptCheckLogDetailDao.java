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
import com.itrus.ca.modules.receipt.entity.ReceiptCheckLogDetail;

/**
 * 发票盘点详情DAO接口
 * @author HuHao
 * @version 2015-03-20
 */
public interface ReceiptCheckLogDetailDao extends ReceiptCheckLogDetailDaoCustom, CrudRepository<ReceiptCheckLogDetail, Long> {

	@Modifying
	@Query("delete from ReceiptCheckLogDetail  where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ReceiptCheckLogDetailDaoCustom extends BaseDao<ReceiptCheckLogDetail> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ReceiptCheckLogDetailDaoImpl extends BaseDaoImpl<ReceiptCheckLogDetail> implements ReceiptCheckLogDetailDaoCustom {

}
