/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.finance.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.work.entity.WorkCompany;

/**
 * 支付信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-08
 */
public interface FinancePaymentInfoDao extends FinancePaymentInfoDaoCustom, CrudRepository<FinancePaymentInfo, Long> {

	@Modifying
	@Query("update FinancePaymentInfo set delFlag='" + FinancePaymentInfo.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Query("select distinct company from FinancePaymentInfo where del_flag = '0'")
	public List<String> findCompanyId();
	
}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface FinancePaymentInfoDaoCustom extends BaseDao<FinancePaymentInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class FinancePaymentInfoDaoImpl extends BaseDaoImpl<FinancePaymentInfo> implements FinancePaymentInfoDaoCustom {

}
