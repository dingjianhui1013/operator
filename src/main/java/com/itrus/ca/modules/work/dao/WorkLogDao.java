/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkLog;
import com.itrus.ca.modules.work.entity.WorkPayInfo;

/**
 * 工作记录DAO接口
 * @author WangHongwei
 * @version 2014-06-13
 */
public interface WorkLogDao extends WorkLogDaoCustom, CrudRepository<WorkLog, Long> {
	
}

/**
 * DAO自定义接口
 * @author WangHongwei
 */
interface WorkLogDaoCustom extends BaseDao<WorkLog> {

}

/**
 * DAO自定义接口实现
 * @author WangHongwei
 */
@Component
class WorkLogDaoImpl extends BaseDaoImpl<WorkLog> implements WorkLogDaoCustom {

}
