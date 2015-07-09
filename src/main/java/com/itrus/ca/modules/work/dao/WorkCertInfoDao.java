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
import com.itrus.ca.modules.work.entity.WorkCertInfo;

/**
 * 证书信息DAO接口
 * @author ZhangJingtao
 * @version 2014-06-16
 */
public interface WorkCertInfoDao extends WorkCertInfoDaoCustom, CrudRepository<WorkCertInfo, Long> {

}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface WorkCertInfoDaoCustom extends BaseDao<WorkCertInfo> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Component
class WorkCertInfoDaoImpl extends BaseDaoImpl<WorkCertInfo> implements WorkCertInfoDaoCustom {

}
