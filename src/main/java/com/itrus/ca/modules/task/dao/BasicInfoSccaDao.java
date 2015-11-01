/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.task.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.task.entity.BasicInfoScca;

/**
 * 中间表数据DAO接口
 * @author ZhangJingtao
 * @version 2014-08-18
 */
public interface BasicInfoSccaDao extends BasicInfoSccaDaoCustom, CrudRepository<BasicInfoScca, Long> {

}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface BasicInfoSccaDaoCustom extends BaseDao<BasicInfoScca> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Repository
class BasicInfoSccaDaoImpl extends BaseDaoImpl<BasicInfoScca> implements BasicInfoSccaDaoCustom {

}
