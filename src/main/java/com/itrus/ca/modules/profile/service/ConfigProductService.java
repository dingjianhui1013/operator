/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.profile.service;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigChargeAgent;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.profile.dao.ConfigProductDao;

/**
 * 应用Service
 * @author HuHao
 * @version 2014-06-03
 */
@Component
@Transactional(readOnly = true)
public class ConfigProductService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ConfigProductService.class);
	
	@Autowired
	private ConfigProductDao configProductDao;
	
	@Autowired
	private ConfigAppService configAppService;
	
	public ConfigProduct get(Long id) {
		return configProductDao.findOne(id);
	}
	
	public Page<ConfigProduct> find(Page<ConfigProduct> page, ConfigProduct configProduct) {
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		
		if (StringUtils.isNotEmpty(configProduct.getProductName())){
			dc.add(Restrictions.like("productName", "%"+EscapeUtil.escapeLike(configProduct.getProductName())+"%"));
		}
		dc.createAlias("configApp", "configApp");
		if (configProduct.getConfigApp()!=null) {
			dc.add(Restrictions.isNotNull("configApp.id"));
			dc.add(Restrictions.like("configApp.appName", "%"+EscapeUtil.escapeLike(configProduct.getConfigApp().getAppName())+"%"));
			if (configProduct.getConfigApp().getId()!=null) {
				dc.add(Restrictions.eq("configApp.id", configProduct.getConfigApp().getId()));

			}
		}
		
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(page, dc);
	}
	
	public List<ConfigProduct> findByName(String name , Long appId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.like("productName", "%"+EscapeUtil.escapeLike(name)+"%"));
		dc.add(Restrictions.eq("configApp.id",appId));
		dc.add(Restrictions.isNotNull("raAccountId"));
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(dc);
	}
	
	public List<ConfigProduct> findByStart( ConfigProduct configProduct) {
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		if (configProduct.getProductName()!=null) {
			if (!configProduct.getProductName().equals("-1")){
				dc.add(Restrictions.like("productName", "%"+EscapeUtil.escapeLike(configProduct.getProductName())+"%"));
			}
		}
		
		if (configProduct.getConfigApp()!=null) {
			if (StringUtils.isNotEmpty(configProduct.getConfigApp().getId().toString())) {
				dc.add(Restrictions.eq("configApp.id", configProduct.getConfigApp().getId()));
			}
		}
		
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(dc);
	}
	
	public  List<ConfigProduct> findByIdOrName(String productName, Long appId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();

		dc.add(Restrictions.like("productName","%" + EscapeUtil.escapeLike(productName) + "%"));
		dc.add(Restrictions.eq("configApp.id",appId));
		return configProductDao.find(dc);
	}
	
	public ConfigProduct findByExtedId(Long raAccountExtedId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("raAccountExtedId", raAccountExtedId));
		dc.add(Restrictions.eq("delFlag", ConfigProduct.DEL_FLAG_NORMAL));
		if (configProductDao.find(dc).size()>0) {
			return configProductDao.find(dc).get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 根据产品名称和产品标识查出唯一产品
	* @Title: findByIdOrLable
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param productName
	* @param @param lable
	* @param @return    设定文件
	* @return List<ConfigProduct>    返回类型
	* @throws
	 */
	public  ConfigProduct findByIdOrLable(Long app, String productName, Integer lable){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configApp.id",app));
		dc.add(Restrictions.like("productName","%" + EscapeUtil.escapeLike(productName) + "%"));
		dc.add(Restrictions.eq("productLabel",lable));
		return configProductDao.find(dc).get(0);
	}
	
	
	
	public List<ConfigProduct> findByAppId(ConfigProduct configProduct) {
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		if (configProduct.getConfigApp()!=null) {
			if (StringUtils.isNotEmpty(configProduct.getConfigApp().getId().toString())) {
				dc.add(Restrictions.eq("configApp.id", configProduct.getConfigApp().getId()));
			}
		}
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(dc);
	}
	
	public List<ConfigProduct> findByAppIdAndRa(ConfigProduct configProduct) {
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		if (configProduct.getConfigApp()!=null) {
			if (StringUtils.isNotEmpty(configProduct.getConfigApp().getId().toString())) {
				dc.add(Restrictions.eq("configApp.id", configProduct.getConfigApp().getId()));
			}
		}
		dc.add(Restrictions.isNotNull("raAccountId"));
		dc.add(Restrictions.isNotNull("raAccountExtedId"));
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(dc);
	}
	
	public List<ConfigProduct> findByApp(Long appId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq(ConfigProduct.DEL_FLAG, ConfigProduct.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return configProductDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public List<ConfigProduct> findAll(){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.add(Restrictions.eq("delFlag", ConfigProduct.DEL_FLAG_NORMAL));
		return configProductDao.find(dc);
	}
	
	@Transactional(readOnly = false)
	public void save(ConfigProduct configProduct) {
		configProductDao.save(configProduct);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		configProductDao.deleteById(id);
	}
	/**
	 * 校验产品是否已存在
	 * @param product
	 * @return
	 */
	public boolean validateProductValid(ConfigProduct product){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("productName", product.getProductName()));
//		dc.add(Restrictions.eq("productLabel", product.getProductLabel()));
		if (product.getId()!=null) {
			dc.add(Restrictions.ne("id", product.getId()));
		}
		dc.add(Restrictions.eq("configApp.id", product.getConfigApp().getId()));
		long count = configProductDao.count(dc);
		if (count==0) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 根据产品名、应用名、标识查询产品
	 * @date:2014年8月18日
	 * @user:京涛
	 * @return_type:ConfigProduct
	 */
	public ConfigProduct findByNamesLabel(Integer productLabel,String appName,String productName){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("productName", productName));
		dc.add(Restrictions.eq("configApp.appName", appName));
		dc.add(Restrictions.eq("productLabel", productLabel));
		List<ConfigProduct> products = configProductDao.find(dc);
		if (products.size()==0) {
			return null;
		}else {
			return products.get(0);
		}
	}
	
	
	
	
	//根据产品ID和应用ID来查询数据
	public ConfigProduct findByAppIdProduct(Long appId,Long productId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		dc.add(Restrictions.eq("id", productId));
		List<ConfigProduct> products = configProductDao.find(dc);
		return products.get(0);
	}
	
	//根据产品ID和应用ID来查询数据
	public List<ConfigProduct> findByAppId(Long appId){
		DetachedCriteria dc = configProductDao.createDetachedCriteria();
		dc.createAlias("configApp", "configApp");
		dc.add(Restrictions.eq("configApp.id", appId));
		return configProductDao.find(dc);
	}
	

	/*public List<ConfigProduct> findProductId(Long applyId) {
		
		return null;
	}*/
}


