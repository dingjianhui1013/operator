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
import com.itrus.ca.modules.key.entity.KeyAllocateApply;
import com.itrus.ca.modules.settle.entity.SettleSupplierCertDetail;

/**
 * 调拨管理DAO接口
 * @author HUHAO
 * @version 2014-06-28
 */
public interface SettleSupplierCertDetailDao extends SettleSupplierCertDetailDaoCustom, CrudRepository<SettleSupplierCertDetail, Long> {

	@Modifying
	@Query("update KeyAllocateApply set delFlag='" + KeyAllocateApply.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author HUHAO
 */
interface SettleSupplierCertDetailDaoCustom extends BaseDao<SettleSupplierCertDetail> {

}

/**
 * DAO自定义接口实现
 * @author HUHAO
 */
@Component
class SettleSupplierCertDetailDaoImpl extends BaseDaoImpl<SettleSupplierCertDetail> implements SettleSupplierCertDetailDaoCustom {

}
