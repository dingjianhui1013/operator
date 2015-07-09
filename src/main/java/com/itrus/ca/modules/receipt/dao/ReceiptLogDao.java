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
import com.itrus.ca.modules.receipt.entity.ReceiptLog;

/**
 * 发票定时统计DAO接口
 * @author WangHongwei
 * @version 2014-07-15
 */
public interface ReceiptLogDao extends ReceiptLogDaoCustom, CrudRepository<ReceiptLog, Long> {

	
}

/**
 * DAO自定义接口
 * @author WangHongwei
 */
interface ReceiptLogDaoCustom extends BaseDao<ReceiptLog> {

}

/**
 * DAO自定义接口实现
 * @author WangHongwei
 */
@Component
class ReceiptLogDaoImpl extends BaseDaoImpl<ReceiptLog> implements ReceiptLogDaoCustom {

}
