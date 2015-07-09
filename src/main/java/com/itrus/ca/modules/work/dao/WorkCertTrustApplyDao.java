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
import com.itrus.ca.modules.work.entity.WorkCertTrustApply;

/**
 * 申请移动设备数量DAO接口
 * @author ZhangJingtao
 * @version 2014-07-05
 */
public interface WorkCertTrustApplyDao extends WorkCertTrustApplyDaoCustom, CrudRepository<WorkCertTrustApply, Long> {

}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface WorkCertTrustApplyDaoCustom extends BaseDao<WorkCertTrustApply> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class WorkCertTrustApplyDaoImpl extends BaseDaoImpl<WorkCertTrustApply> implements WorkCertTrustApplyDaoCustom {

}
