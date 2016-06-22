/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.DataEntity;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.sys.dao.OfficeDao;
import com.itrus.ca.modules.sys.dao.RoleDao;
import com.itrus.ca.modules.sys.dao.UserDao;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.Role;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	public Office get(Long id) {
		return officeDao.findOne(id);
	}
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}
	public List<Office> findall(){
		return (List<Office>) officeDao.findAll();
	}
	/**
	 * 区域列表 （禁用启用都显示）
	* @Title: find
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param page
	* @param @param office
	* @param @return    设定文件
	* @return Page<Office>    返回类型
	* @throws
	 */
	public Page<Office> find(Page<Office> page,Office office) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(office.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(office.getName())+"%"));
		}
		
		dc.add(Restrictions.eq("delFlag", DataEntity.DEL_FLAG_NORMAL));
		Office off = new Office();
		off.setId(1L);
		dc.add(Restrictions.eq("parent", off));
		//dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.ne("id", 34L));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(page, dc);
	}
	
	/**
	 * 应用授权
	 * 只查询没有被del标记为删除的
	* @Title: findByDel
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param page
	* @param @param office
	* @param @return    设定文件
	* @return Page<Office>    返回类型
	* @throws
	 */
	public Page<Office> findByDel(Page<Office> page,Office office) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(office.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(office.getName())+"%"));
		}
		Office off = new Office();
		off.setId(1L);
		dc.add(Restrictions.ne("parent", off));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.ne("id", 34L));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(page, dc);
	}
	
	
	
	public Page<Office> findOffice(Page<Office> page,Office office) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = officeDao.createDetachedCriteria();
//		if (StringUtils.isNotEmpty(office.getName())){
//			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(office.getName())+"%"));
//		}
		dc.add(Restrictions.eq("areaName",office.getName()));
		dc.add(Restrictions.eq("parent", office));
		//dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.ne("id", 34L));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(page, dc);
	}
	
	public List<Office> find(Long areaId) {
		User user = UserUtils.getUser();
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(areaId.toString())){
			dc.add(Restrictions.eq("area.id",areaId));
		}
		dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	
	
	public List<Office> findByParentId(Long areaId) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if(areaId==null){
			return null;
		}
		if (areaId!=null) {
			
			if (StringUtils.isNotEmpty(areaId.toString())){
				dc.add(Restrictions.eq("parent.id",areaId));
			}else{
				return null;
			}
		}
		dc.add(Restrictions.neOrIsNotNull("type", "1"));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	public List<Office> findByParentIds(List<Long> areaIds) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if(areaIds==null){
			return null;
		}
		if (areaIds!=null) {
			
			if (StringUtils.isNotEmpty(areaIds.toString())){
				dc.add(Restrictions.in("parent.id",areaIds));
			}else{
				return null;
			}
		}
		dc.add(Restrictions.neOrIsNotNull("type", "1"));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	public List<Long> findOfficeIdsByParentId(Long areaId) {		
		return officeDao.findOfficeIdsByParentId(areaId);
	}
	
	/**
	 * 查询该区域下所有网点包括被禁用的
	* @Title: findOfficesByParentId
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param areaId
	* @param @return    设定文件
	* @return List<Office>    返回类型
	* @throws
	 */
	public List<Office> findOfficesByParentId(Long areaId) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(areaId.toString())){
			dc.add(Restrictions.eq("parent.id",areaId));
		}
		dc.add(Restrictions.eq("type", "2"));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	
	
	public List<Office> findByParentId(Long areaId,Integer type) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(areaId.toString())){
			dc.add(Restrictions.eq("parent.id",areaId));
		}
		dc.add(Restrictions.eq("type", String.valueOf(type)));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	
	
	public  List<Office> findByIds(Page<Office> page,List<Long> officeIds) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		if (officeIds.size()>0){
			dc.add(Restrictions.in("id", officeIds));
		}
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.addOrder(Order.asc("code"));
		dc.addOrder(Order.desc("id"));
		return officeDao.find(dc);
	}
	public Office findById(Long officeId)
	{
		return officeDao.findOne(officeId);
		
	}
	@Transactional(readOnly = false)
	public void save(Office office){
		officeDao.save(office);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		officeDao.deleteById(id);
		//UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	public void deleteL(Office office){
		officeDao.deleteById(office.getId());
	}
	
	
	@Transactional(readOnly = false)
	public List<Office> findAllByidNotInRaletion() {
		return officeDao.findAllByIdNotInRelation();
	}
	
	@Transactional(readOnly = false)
	public List<Office> findAllByid(Long id) {
		return officeDao.findAllById(id);
	}
	
	@Transactional(readOnly = false)
	public List<Office> findByAgentId(Long id){
		return officeDao.findByAgentId(id);
	}

	public List<Office> selectAreaList() {
		return officeDao.selectAreaList();
	}

	public void updateOffice(Office off, Long id, String name, String name2,String name3,String name4) {
		officeDao.updateOffice(off,id,name,name2,name3,name4);
	}
	
	
	/**
	 * 根据用户获得区域(1)or网点(2)
	 * @param user
	 * @param type
	 * @return
	 */
	public List<Office> getOfficeByType(User user,Integer type) {
		if (!user.isAdmin()) {
			List<Role> roles = user.getRoleList();
			List<Office> result = new ArrayList<Office>();
			Office area = user.getCompany();
			Office office = user.getOffice();
			
			for (Role r : roles) {
				if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
					return UserUtils.getOfficeList(type);
				}
				else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){//所在区域及以下
					if (type==1) {//区域
						if (area.getId()==1L) {//四川CA总网点
							result.add(user.getCompany());
							result.addAll(findByParentId(area.getId(),type));
							return result;
						}else {
							result.add(area);
						}
					}else {//网点
						if (area.getId()==1L) {
							return UserUtils.getOfficeList(type);//返回所有
						}else {//非总网点，所在区域及以下
							result.addAll(findByParentId(area.getId(),type));//此时 区域的子office是网点
						}
					}
				}
				else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){//所在区域数据
					if (type==1) {//区域
						result.add(area);
					}else {//网点
						result.add(office);
					}
				}
				else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){//所在网点及以下数据
					if (type==1) {//区域
						result.add(area);
					}else {//网点
						result.add(office);
					}
				}
				else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){//所在网点数据
					if (type==1) {//区域
						result.add(area);
					}else {//网点
						result.add(office);
					}
				}
				else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())) {//个人数据
					if (type==1) {//区域
						result.add(area);
					}else {//网点
						result.add(office);
					}
				}
				else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){//自定义
					List<Office> offices = 	r.getOfficeList();
					for (Office o : offices) {
						if (o.getType().equals(type.toString())) {
							result.add(o);
						}
					}
				}
			}
			//去重
			List<Office> signleResultList = new ArrayList<Office>();
			for (Office r : result) {
				if (signleResultList.contains(r)) {
					continue;
				}
				signleResultList.add(r);
			}
			return signleResultList;
		}else {
			return UserUtils.getOfficeList(type);
		}
	}

	public List<Office> selectArea(String areaName) {
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		dc.add(Restrictions.eq("name", areaName));
		dc.add(Restrictions.eq(Office.DEL_FLAG, Office.DEL_FLAG_NORMAL));
		return officeDao.find(dc);
	}

	@Transactional(readOnly = false)
	public List<Office> getOfficeList(Integer type) {
		List<Office> officeList = null;
		//User user = getUser();
		DetachedCriteria dc = officeDao.createDetachedCriteria();
		//dc.add(dataScopeFilter(user, dc.getAlias(), ""));
		dc.add(Restrictions.eq("delFlag", Office.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("type", type.toString()));
		dc.addOrder(Order.asc("code"));
		officeList = officeDao.find(dc);
		return officeList;
	}
	
	/**
	 * 将网点或区域下所有角色归属到四川CA
	* @Title: updateUserByOffice
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param offices
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public void updateUserByOffice(List<Office> offices){
		Office company = new Office();
		company.setId(1L);
		userDao.updateCompany(company, offices);
		
		Office office = new Office();
		office.setId(34L);
		userDao.updateOffice(office, offices);
	}
	
	/**
	 * 查询当前网点或区域有没有管理员。有返回true。没有返回false
	* @Title: findUserByOffice
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param offices
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public boolean findUserByOffice(List<Office> offices){
		List<User> users = userDao.findByOffices(offices);
		if (users.size()>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据网点修改角色归属网点，归属到四川CA
	* @Title: updateRoleByOffice
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param offices    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void updateRoleByOffice(List<Office> offices){
		Office company = new Office();
		company.setId(1L);
		roleDao.updateOffice(company, offices);
	}
	
	/**
	 * 根据网点删除角色网点关联表数据
	* @Title: deleteRoleOffice
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	public void deleteRoleOffice(List<Office> offices){
		for (Office office : offices) {
			Query query = officeDao.createSqlQuery("delete sys_role_office where office_id = '"+ office.getId() +"'");
		}
	}
	
	public boolean findRoleByOffice(List<Office> offices){
		List<Role> list = roleDao.findByOffice(offices);
		if (list.size()>0) {
			return true;
		}
		return false;
	}
}
