/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.task.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.task.dao.BasicInfoSccaDao;
import com.itrus.ca.modules.task.entity.BasicInfoScca;

/**
 * 中间表数据Service
 * @author ZhangJingtao
 * @version 2014-08-18
 */
@Service
@Transactional(readOnly = true)
public class BasicInfoSccaService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(BasicInfoSccaService.class);
	
	@Autowired
	private BasicInfoSccaDao basicInfoSccaDao;
	
	public BasicInfoScca get(Long id) {
		return basicInfoSccaDao.findOne(id);
	}
	
	public Page<BasicInfoScca> find(Page<BasicInfoScca> page, BasicInfoScca basicInfoScca) {
		DetachedCriteria dc = basicInfoSccaDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(basicInfoScca.getName())){
			dc.add(Restrictions.like("name", "%"+EscapeUtil.escapeLike(basicInfoScca.getName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return basicInfoSccaDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(BasicInfoScca basicInfoScca) {
		basicInfoSccaDao.save(basicInfoScca);
	}
	
	@Transactional(readOnly  = false)
	public void  saveList(List<BasicInfoScca>  sccas){
		basicInfoSccaDao.save(sccas);
	}
	
	@Transactional(readOnly = false)
	public void delete(BasicInfoScca basicInfoScca) {
		basicInfoSccaDao.delete(basicInfoScca);
	}
	
	@Transactional(readOnly  = false)
	public void  deleteList(List<BasicInfoScca>  sccas){
		basicInfoSccaDao.delete(sccas);
	}
	
	


	@Transactional(readOnly = false)
	public List<BasicInfoScca> findAll(){
		DetachedCriteria dc = basicInfoSccaDao.createDetachedCriteria();
		dc.add(Restrictions.or(Restrictions.ne("used", true),Restrictions.isNull("used")));
		return (List<BasicInfoScca>) basicInfoSccaDao.find(dc);
	}
}
