/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;

/**
 * 原始单位信息DAO接口
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
public interface WorkCompanyHisDao extends WorkCompanyHisDaoCustom, CrudRepository<WorkCompanyHis, Long> {

	
}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface WorkCompanyHisDaoCustom extends BaseDao<WorkCompanyHis> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class WorkCompanyHisDaoImpl extends BaseDaoImpl<WorkCompanyHis> implements WorkCompanyHisDaoCustom {

}
