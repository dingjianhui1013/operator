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
import com.itrus.ca.modules.receipt.entity.ReceiptAllocateApply;

/**
 * 调拨信息DAO接口
 * @author WHW
 * @version 2014-07-02
 */
public interface ReceiptAllocateApplyDao extends ReceiptAllocateApplyDaoCustom, CrudRepository<ReceiptAllocateApply, Long> {

	@Modifying
	@Query("update ReceiptAllocateApply set delFlag='" + ReceiptAllocateApply.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author WHW
 */
interface ReceiptAllocateApplyDaoCustom extends BaseDao<ReceiptAllocateApply> {

}

/**
 * DAO自定义接口实现
 * @author WHW
 */
@Component
class ReceiptAllocateApplyDaoImpl extends BaseDaoImpl<ReceiptAllocateApply> implements ReceiptAllocateApplyDaoCustom {

}
