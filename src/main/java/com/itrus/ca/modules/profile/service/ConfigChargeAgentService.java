/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;

import java.util.ArrayList;
import java.util.List;

import com.itrus.ca.modules.profile.dao.ConfigChargeAgentDetailDao;
import com.itrus.ca.modules.profile.dao.ConfigProductDao;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgentDetail;
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
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.dao.ConfigChargeAgentDao;

/**
 * 代理商应用计费策略Service
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Component
@Transactional(readOnly = true)
public class ConfigChargeAgentService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigChargeAgentService.class);
	
	@Autowired
	private ConfigChargeAgentDao configChargeAgentDao;
	@Autowired
	private ConfigProductDao configProductDao;
	@Autowired
	private ConfigChargeAgentDetailDao configChargeAgentDetailDao;
	
	public ConfigChargeAgent get(Long id) {
		return configChargeAgentDao.findOne(id);
	}
	
	public Page<ConfigChargeAgent> find(Page<ConfigChargeAgent> page, ConfigChargeAgent configChargeAgent) {
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(configChargeAgent.getTempName())) {
			dc.add(Restrictions.like("tempName", "%"+configChargeAgent.getTempName()+"%"));
		}
		dc.addOrder(Order.desc("id"));
		return configChargeAgentDao.find(page, dc);
	}
	
	/*public ConfigChargeAgent findByAppAndProduct(ConfigApp configApp, ConfigProduct configProduct){
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		Long id = configApp.getId();
		
		dc.add(Restrictions.like("configApp", configApp));
		dc.add(Restrictions.like("configProduct", configProduct));
		List<ConfigChargeAgent> list = configChargeAgentDao.find(dc);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}*/
	
	public List<ConfigChargeAgent> findByProductId(Long productId){
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configProduct.id", productId));
		return configChargeAgentDao.find(dc);
	}
	
	
	/**
	 * 查找计费策略更新数量为0的老的数据
	 * @param productId
	 * @return
	 */
	
	public List<ConfigChargeAgent> findByNullUpdateNum(){
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		dc.add(Restrictions.isNull("configureUpdateNum"));
		List<String> styles = new ArrayList<String>();
		styles.add("2");
		styles.add("3");
		dc.add(Restrictions.in("tempStyle", styles));
		return configChargeAgentDao.find(dc);
	}
	
	

	/**
	 * 经过ID 查询计费策略模板
	 * @param id
	 * @return
	 */
	public List<ConfigChargeAgent> findById(Long id){
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		dc.add(Restrictions.eq("id", id));
		return configChargeAgentDao.find(dc);
	}

	public List<ConfigChargeAgent> findBBByProductId(Long productId,Integer type){
		DetachedCriteria dc = configChargeAgentDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configProduct.id", productId));
		dc.add(Restrictions.eq("workType", type));
		return configChargeAgentDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigChargeAgent configChargeAgent) {
		configChargeAgentDao.save(configChargeAgent);
	}
	
	@Transactional(readOnly = false)
	public void save(List<ConfigChargeAgent> configChargeAgents) {
		configChargeAgentDao.save(configChargeAgents);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configChargeAgentDao.deleteById(id);
	}

	@Deprecated
    public ConfigChargeAgent getAgentById(ConfigApp configApp, ConfigProduct configProduct) {
        // TODO Auto-generated method stub
        return null;
    }

	/**
	 *  获得缴费方式
	 * @param configAgentId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getChargeMethod(Long configAgentId){
		ConfigChargeAgent configChargeAgent = configChargeAgentDao.findOne(configAgentId);
		List<String> chargeMethods = new ArrayList<String>();
		//if("1".equals(configChargeAgent.getTempStyle())){//模板类型为通用
			if(configChargeAgent.getChargeMethodPos()){//pos
				chargeMethods.add("POS");
			}
			if(configChargeAgent.getChargeMethodMoney()){//现金
				chargeMethods.add("MONEY");
			}
			if(configChargeAgent.getChargeMethodBank()){//银行转账
				chargeMethods.add("BANK");
			}

			return chargeMethods;

		//}else{//政府统一采购 合同采购
			//return chargeMethods;
		//}
	}

	/**
	 *  判断模板是否被使用
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean checkUsed(Long id){
		DetachedCriteria detachedCriteria =configProductDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("chargeAgentId",id));
		List list = configProductDao.find(detachedCriteria);
		if(list.size()>0){
			return true;
		}else{
			return false;

		}
	}
	@Transactional(readOnly = false)
	public void deleteById(Long id){
		configChargeAgentDetailDao.deleteByAgentId(id);
		this.delete(id);
	}
	@Transactional(readOnly = true)
	public List<ConfigChargeAgent> findByName(String name){
		DetachedCriteria detachedCriteria = configChargeAgentDao.createDetachedCriteria();
		detachedCriteria.add(Restrictions.eq("tempName",name));
		List<ConfigChargeAgent> list = configChargeAgentDao.find(detachedCriteria);
		return list;
	}

}
