/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.settle.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.settle.entity.SettleKey;

/**
 * 供应商返修Key记录DAO接口
 * @author whw
 * @version 2014-07-16
 */
public interface SettleKeyDao extends SettleKeyDaoCustom, CrudRepository<SettleKey, Long> {

	
}

/**
 * DAO自定义接口
 * @author whw
 */
interface SettleKeyDaoCustom extends BaseDao<SettleKey> {

}

/**
 * DAO自定义接口实现
 * @author whw
 */
@Component
class SettleKeyDaoImpl extends BaseDaoImpl<SettleKey> implements SettleKeyDaoCustom {

}
