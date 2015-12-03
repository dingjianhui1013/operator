/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.message.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.message.entity.MessageSending;

/**
 * 消息发送DAO接口
 * @author qt
 * @version 2015-11-30
 */
public interface MessageSendingDao extends MessageSendingDaoCustom, CrudRepository<MessageSending, Long> {

	@Modifying
	@Query("update MessageSending set delFlag='" + MessageSending.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
}

/**
 * DAO自定义接口
 * @author qt
 */
interface MessageSendingDaoCustom extends BaseDao<MessageSending> {

}

/**
 * DAO自定义接口实现
 * @author qt
 */
@Component
class MessageSendingDaoImpl extends BaseDaoImpl<MessageSending> implements MessageSendingDaoCustom {

}
