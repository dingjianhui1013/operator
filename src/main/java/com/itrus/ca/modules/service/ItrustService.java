package com.itrus.ca.modules.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.constant.ProductType;
import com.itrus.ca.modules.constant.WorkDealInfoStatus;
import com.itrus.ca.modules.profile.dao.ConfigAppDao;
import com.itrus.ca.modules.profile.dao.ConfigAppOfficeRelationDao;
import com.itrus.ca.modules.profile.dao.ConfigProductDao;
import com.itrus.ca.modules.profile.dao.ConfigRaAccountDao;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.entity.ConfigRaAccount;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.work.dao.WorkCertInfoDao;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 查询数据
 * @author ZhangJingtao
 *
 */
@Service
public class ItrustService {
	@Autowired
	WorkDealInfoDao workDealInfoDao;
	
	@Autowired
	ConfigRaAccountDao raAccountDao;
	
	@Autowired
	ConfigAppDao configAppDao;
	
	@Autowired
	ConfigProductDao productDao;
	
	@Autowired
	WorkCertInfoDao certInfoDao;
	
	@Autowired
	ConfigAppOfficeRelationDao configAppOfficeRelationDao;
	
	
	/**
	 * 根据序列号查询
	 * @param sn
	 * @param type
	 * @return
	 */
	public WorkDealInfo findBySn(String sn,Integer type){
		try {
			DetachedCriteria dc = workDealInfoDao.createDetachedCriteria();
			if (type==0) {//证书
				dc.add(Restrictions.like("certSn","%"+EscapeUtil.escapeLike(sn)+"%"));
			}
			if (type==1) {//key
				dc.add(Restrictions.eq("keySn",sn));
			}
			dc.add(Restrictions.eq("dealInfoStatus", WorkDealInfoStatus.STATUS_CERT_OBTAINED));
			dc.addOrder(Order.desc("id"));
			List<WorkDealInfo> infos = workDealInfoDao.find(dc);
			if (infos!=null&&infos.size()!=0) {
				return infos.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据应用id获得移动设备证书的信息
	 * @param id
	 * @return
	 * @deprecated 后期改了需求，设备证书发放的RA就是主身份的RA
	 */
	public ConfigRaAccount findByAppId(Long id){
		try {
			DetachedCriteria dc = productDao.createDetachedCriteria();
			dc.createAlias("configApp", "configApp");
			dc.add(Restrictions.eq("configApp.id", id));
			dc.add(Restrictions.eq("productName", "4"));
			List<ConfigProduct> products = productDao.find(dc);
			if (products!=null&&products.size()!=0) {
				return (ConfigRaAccount) products.get(0).getConfigRaAccounts().toArray()[0];
			}
		} catch (Exception e) {
			return new ConfigRaAccount();
		}
		return new ConfigRaAccount();
	}
	
	/**
	 * 根据证书序列号查询可信设备数量
	 * @param certSn
	 * @return
	 */
	public WorkCertInfo getTrustDeviceCount(String certSn){
		DetachedCriteria dc = certInfoDao.createDetachedCriteria();
		dc.add(Restrictions.like("serialnumber","%"+EscapeUtil.escapeLike(certSn)+"%"));
//		dc.add(Restrictions.eqOrIsNull("isMaster", true));
		List<WorkCertInfo> certs = certInfoDao.find(dc);
		if (certs!=null&&certs.size()!=0) {
			return certs.get(0);
		}
		return null;
	}
	/**
	 * 获得所有支持通用的应用ids
	 * @return
	 */
	public List<Long> listCommonApp(){
		DetachedCriteria dc = configAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq("supportCommon", true));
		List<ConfigApp> apps = configAppDao.find(dc);
		List<Long> ids = new ArrayList<Long>();
		for (ConfigApp app : apps) {
			ids.add(app.getId());
		}
		return ids;
	}
	
	public List<ConfigApp> getUserAppsByOfficeId(List<Office> offices){
		DetachedCriteria dc = configAppOfficeRelationDao.createDetachedCriteria();
		dc.createAlias("sysOffice", "sysOffice");
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.in("sysOffice", offices));
		dc.add(Restrictions.eq("configApp.delFlag", ConfigApp.DEL_FLAG_NORMAL));
		
//		List<ConfigAppOfficeRelation> relations = configAppOfficeRelationDao.find(dc);
		List<ConfigAppOfficeRelation> relations = (List<ConfigAppOfficeRelation>) configAppOfficeRelationDao.findAll();
		List<ConfigApp> apps = new ArrayList<ConfigApp>();
		for (ConfigAppOfficeRelation relation : relations) {
			if (relation.getConfigApp().getId()==null) {
				continue;
			}
			if (apps.contains(relation.getConfigApp())) {
				continue;
			}
			apps.add(relation.getConfigApp());
		}
		return apps;
	}
	
	public ConfigRaAccount findRaById(Long id){
		return raAccountDao.findOne(id);
	}
	
	
}
