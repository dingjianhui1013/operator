/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkCompanyHis;
import com.itrus.ca.modules.work.dao.WorkCompanyDao;

/**
 * 单位名称Service
 * @author HUHAO
 * @version 2014-06-12
 */
@Component
@Transactional(readOnly = true)
public class WorkCompanyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkCompanyService.class);
	
	@Autowired
	private WorkCompanyDao workCompanyDao;
	
	public WorkCompany get(Long id) {
		return workCompanyDao.findOne(id);
	}
	
	public WorkCompany findById(Long id) {
		return workCompanyDao.findById(id);
	}
	
//	public List<WorkCompany> getIds(List<Long> id) {
//		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
//		if(id.size()>0&&id!=null)
//		{	
//			dc.add(Restrictions.in("id", id));
//		}
//		dc.addOrder(Order.desc("id"));
//		return workCompanyDao.find(dc);
//	}
	
	public Page<WorkCompany> find(Page<WorkCompany> page, WorkCompany workCompany) {
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(workCompany.getCompanyName())){
			dc.add(Restrictions.like("companyName", "%"+EscapeUtil.escapeLike(workCompany.getCompanyName())+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return workCompanyDao.find(page, dc);
	}
	
	public Page<WorkCompany> findByComId(Page<WorkCompany> page, WorkCompany workCompany,List<WorkCompany> companies) {
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(workCompany.getCompanyName())){
			dc.add(Restrictions.like("companyName", "%"+EscapeUtil.escapeLike(workCompany.getCompanyName())+"%"));
		}
		List<Long> ids = new ArrayList<Long>();
		if(companies.size()>0){
			for(WorkCompany company: companies){
				ids.add(company.getId());
			}
			dc.add(Restrictions.in("id", ids));
		}
		dc.addOrder(Order.desc("id"));
		return workCompanyDao.find(page, dc);
	}
	
	//通过公司名称查询公司
	public List<WorkCompany> findByCompanyName(String companyName) {
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(companyName)){
			dc.add(Restrictions.eq("companyName", companyName));
		}
		dc.addOrder(Order.desc("id"));	
		return workCompanyDao.find(dc);
	}
	public WorkCompany findCompanyId(String companyName) {
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(companyName)){
			dc.add(Restrictions.eq("companyName", companyName));
		}
		dc.addOrder(Order.desc("id"));	
		List<WorkCompany> list = workCompanyDao.find(dc);
		WorkCompany workCompany = new WorkCompany();
		if (list.size()>0) {
			workCompany = list.get(0);
		}
		return workCompany;
	}
	//通过公司名和组织机构代码查询
	public WorkCompany finByNameAndNumber(String companyName, String organizationNumber){
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(companyName)){
			dc.add(Restrictions.eq("companyName", companyName));
		}
		dc.add(Restrictions.eq("organizationNumber", organizationNumber));
		dc.addOrder(Order.desc("id"));
	
		List<WorkCompany> list = workCompanyDao.find(dc);
		WorkCompany workCompany = new WorkCompany();
		if (list.size()>0) {
			workCompany = list.get(0);
		}
		return workCompany;
	}
	

	public List<WorkCompany> findAll(){
		return workCompanyDao.find("from WorkCompany", null);
	}
	
	/**
	 * 查询数据库保存的单位名称不为null的（自动带回）
	* @Title: findByNotNull
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @return    设定文件
	* @return List<WorkCompany>    返回类型
	* @throws
	 */
	public List<WorkCompany> findByNotNull(){
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		dc.add(Restrictions.isNotNull("companyName"));
		return workCompanyDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(WorkCompany workCompany) {
		workCompanyDao.save(workCompany);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkCompany> workCompany) {
		workCompanyDao.save(workCompany);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		workCompanyDao.deleteById(id);
	}
	
	public WorkCompanyHis change(WorkCompany workCompany){
		WorkCompanyHis workCompanyHis = new WorkCompanyHis();
		workCompanyHis.setActualCapital(workCompany.getActualCapital());
		workCompanyHis.setAddress(workCompany.getAddress());
		workCompanyHis.setAgeDisNumber(workCompany.getAgeDisNumber());
		workCompanyHis.setAnnualInspection(workCompany.getAnnualInspection());
		workCompanyHis.setBusinessNumber(workCompany.getBusinessNumber());
		workCompanyHis.setCity(workCompany.getCity());
		workCompanyHis.setComCertficateNumber(workCompany.getComCertficateNumber());
		workCompanyHis.setComCertficateTime(workCompany.getComCertficateTime());
		workCompanyHis.setComCertificateType(workCompany.getComCertificateType());
		workCompanyHis.setComEnglishName(workCompany.getComEnglishName());
		workCompanyHis.setCompanyIp(workCompany.getCompanyIp());
		workCompanyHis.setCompanyMobile(workCompany.getCompanyMobile());
		workCompanyHis.setCompanyName(workCompany.getCompanyName());
		workCompanyHis.setCompanyType(workCompany.getCompanyType());
		workCompanyHis.setCompanyWeb(workCompany.getCompanyWeb());
		workCompanyHis.setComPhone(workCompany.getComPhone());
		workCompanyHis.setDistrict(workCompany.getDistrict());
		workCompanyHis.setEnterpriseType(workCompany.getEnterpriseType());
		workCompanyHis.setIndustry(workCompany.getIndustry());
		workCompanyHis.setLegalName(workCompany.getLegalName());
		workCompanyHis.setMarketingScope(workCompany.getMarketingScope());
		workCompanyHis.setOrganizationNumber(workCompany.getOrganizationNumber());
		workCompanyHis.setOrgExpirationTime(workCompany.getOrgExpirationTime());
		workCompanyHis.setProvince(workCompany.getProvince());
		workCompanyHis.setRegisteredCapital(workCompany.getRegisteredCapital());
		workCompanyHis.setRemarks(workCompany.getRemarks());
		workCompanyHis.setSelectLv(workCompany.getSelectLv());
		workCompanyHis.setTcpNumber(workCompany.getTcpNumber());
		workCompanyHis.setZipCode(workCompany.getZipCode());
		workCompanyHis.setAreaRemark(workCompany.getAreaRemark());
		return workCompanyHis;
	}

	
	/**
	 * 添加公司判断是否存在
	 * @param organizationNumber
	 * @param companyName
	 * @return
	 */
	public List<WorkCompany> selectByNameAndNumber(String organizationNumber,
			String companyName) {
		DetachedCriteria dc = workCompanyDao.createDetachedCriteria();
		dc.add(Restrictions.eq("companyName", companyName));
		dc.add(Restrictions.eq("organizationNumber", organizationNumber));
		return workCompanyDao.find(dc);
	}
	
}
