/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.sys.entity.CommonAttach;



/**
 * 引用上传图片DAO接口
 * @author GaoYingwei
 * @version 2013-12-14
 */
public interface CommonAttachDao extends CommonAttachDaoCustom, CrudRepository<CommonAttach, Long> {

}

/**
 * DAO自定义接口
 * @author GaoYingwei
 */
interface CommonAttachDaoCustom extends BaseDao<CommonAttach> {

}

/**
 * DAO自定义接口实现
 * @author GaoYingwei
 */
@Component
class CommonAttachDaoImpl extends BaseDaoImpl<CommonAttach> implements CommonAttachDaoCustom {

}
