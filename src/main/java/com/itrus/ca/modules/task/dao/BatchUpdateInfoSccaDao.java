package com.itrus.ca.modules.task.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.task.entity.BatchUpdateInfoScca;

public interface BatchUpdateInfoSccaDao extends BatchUpdateInfoSccaDaoCustom, CrudRepository<BatchUpdateInfoScca, Long> {

}

/**
 * DAO自定义接口
 * @author ZhangJingtao
 */
interface BatchUpdateInfoSccaDaoCustom extends BaseDao<BatchUpdateInfoScca> {

}

/**
 * DAO自定义接口实现
 * @author ZhangJingtao
 */
@Repository
class BatchUpdateInfoSccaDaoImpl extends BaseDaoImpl<BatchUpdateInfoScca> implements BatchUpdateInfoSccaDaoCustom {

}
