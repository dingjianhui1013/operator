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
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApplyDetail;

/**
 * 申请发票类型详情管理DAO接口
 * @author HuHao
 * @version 2015-03-17
 */
public interface ReceiptAllocateApplyDetailDao extends ReceiptAllocateApplyDetailDaoCustom, CrudRepository<ReceiptAllocateApplyDetail, Long> {

	@Modifying
	@Query("delete from ReceiptAllocateApplyDetail where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HuHao
 */
interface ReceiptAllocateApplyDetailDaoCustom extends BaseDao<ReceiptAllocateApplyDetail> {

}

/**
 * DAO自定义接口实现
 * @author HuHao
 */
@Component
class ReceiptAllocateApplyDetailDaoImpl extends BaseDaoImpl<ReceiptAllocateApplyDetail> implements ReceiptAllocateApplyDetailDaoCustom {

}
