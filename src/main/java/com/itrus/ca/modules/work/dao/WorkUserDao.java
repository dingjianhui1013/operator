/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.work.entity.WorkUser;

/**
 * 联系人DAO接口
 * @author WangHongwei
 * @version 2014-06-13
 */
public interface WorkUserDao extends WorkUserDaoCustom, CrudRepository<WorkUser, Long> {
	
}

/**
 * DAO自定义接口
 * @author WangHongwei
 */
interface WorkUserDaoCustom extends BaseDao<WorkUser> {

}

/**
 * DAO自定义接口实现
 * @author WangHongwei
 */
@Component
class WorkUserDaoImpl extends BaseDaoImpl<WorkUser> implements WorkUserDaoCustom {

}
