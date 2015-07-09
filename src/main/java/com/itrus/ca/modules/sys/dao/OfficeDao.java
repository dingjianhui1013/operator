/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2013-05-15
 */
public interface OfficeDao extends OfficeDaoCustom, CrudRepository<Office, Long> {

	@Modifying
	@Query("update Office set delFlag='" + Office.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	public List<Office> findByParentIdsLike(String parentIds);

	@Query("FROM Office o where o.id NOT IN (SELECT c.office.id from ConfigAgentOfficeRelation c) and o.delFlag=0")
	public List<Office> findAllByIdNotInRelation();
	
	@Modifying
	@Query("Select a from Office a,ConfigAgentOfficeRelation c where a.id=c.office.id AND c.configCommercialAgent.id= ?1 ")
	public List<Office> findByAgentId(Long id);

	@Modifying
	@Query("FROM Office o where o.id NOT IN (SELECT c.office.id from ConfigAgentOfficeRelation c where c.configCommercialAgent.id!=?1)  and o.delFlag=0")
	public List<Office> findAllById(Long id);

	@Modifying
	@Query("From Office o where o.parent.id=1 and o.id!=1")
	public List<Office> selectAreaList();

	@Modifying
	@Query("update Office set parent =?1,name=?3,areaName=?4,phone=?5,address=?6 where id =?2")
	public void updateOffice(Office off, Long id, String name, String name2,String name3,String name4);
	
	
//	@Query("from Office where delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
//	public List<Office> findAllList();
//	
//	@Query("from Office where (id=?1 or parent.id=?1 or parentIds like ?2) and delFlag='" + Office.DEL_FLAG_NORMAL + "' order by code")
//	public List<Office> findAllChild(Long parentId, String likeParentIds);
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface OfficeDaoCustom extends BaseDao<Office> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Repository
class OfficeDaoImpl extends BaseDaoImpl<Office> implements OfficeDaoCustom {

}
