/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2013-01-15
 */
public interface UserDao extends UserDaoCustom, CrudRepository<User, Long> {
	
	@Query("from User where loginName = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public User findByLoginName(String loginName);

	@Modifying
	@Query("update User set delFlag='" + User.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update User set password=?1 where id = ?2")
	public int updatePasswordById(String newPassword, Long id);
	
	@Modifying
	@Query("update User set loginIp=?1, loginDate=?2 where id = ?3")
	public int updateLoginInfo(String loginIp, Date loginDate, Long id);
	
	@Modifying
	@Query("update User set company=?1 where company in ?2")
	public int updateCompany(Office office, List<Office> offices);
	
	@Modifying
	@Query("update User set office=?1 where office in ?2")
	public int updateOffice(Office office, List<Office> offices);
	
	@Query("from User where office in ?1 or company in ?1")
	public List<User> findByOffices(List<Office> offices);
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface UserDaoCustom extends BaseDao<User> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Repository
class UserDaoImpl extends BaseDaoImpl<User> implements UserDaoCustom {

}
