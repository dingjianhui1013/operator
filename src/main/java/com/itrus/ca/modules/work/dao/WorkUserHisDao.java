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
import com.itrus.ca.modules.work.entity.WorkUserHis;

/**
 * 原始经办人信息DAO接口
 * @author ZhangShaoBo
 * @version 2014-07-09
 */
public interface WorkUserHisDao extends WorkUserHisDaoCustom, CrudRepository<WorkUserHis, Long> {
	
}

/**
 * DAO自定义接口
 * @author ZhangShaoBo
 */
interface WorkUserHisDaoCustom extends BaseDao<WorkUserHis> {

}

/**
 * DAO自定义接口实现
 * @author ZhangShaoBo
 */
@Component
class WorkUserHisDaoImpl extends BaseDaoImpl<WorkUserHis> implements WorkUserHisDaoCustom {

}
