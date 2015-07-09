/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.sys.entity.SysConfig;
import com.itrus.ca.modules.sys.utils.ConfigConstant;
import com.itrus.ca.modules.sys.dao.SysConfigDao;

/**
 * 邮件配置Service
 * @author ZhangShaoBo
 * @version 2014-06-03
 */
@Component
@Transactional(readOnly = true)
public class SysConfigService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SysConfigService.class);
	
	@Autowired
	private SysConfigDao sysConfigDao;
	
	public SysConfig get(Long id) {
		return sysConfigDao.findOne(id);
	}
	
	public List<SysConfig> findByType(List<String> type){
		List<SysConfig> list = sysConfigDao.selectByTypeConfig(type);
		return list;
	}
	
	public Page<SysConfig> find(Page<SysConfig> page, SysConfig sysConfig) {
		DetachedCriteria dc = sysConfigDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SysConfig.DEL_FLAG, SysConfig.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return sysConfigDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(SysConfig sysConfig) {
		sysConfigDao.save(sysConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		sysConfigDao.deleteById(id);
	}
	
	/**
	 * 查询SMS短信配置
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String,SysConfig> selectConfigByType(){
		Map<String,SysConfig> map = new HashMap<String, SysConfig>(); 
		for(SysConfig config:sysConfigDao.selectByTypeConfig()){
			map.put(config.getType(), config);
		}
		return map;
	}
	/**
	 * 查询邮件配置
	 * @return
	 */
	@Transactional(readOnly = false)
	public HashMap<String,String> selectEmailConfig(){
		DetachedCriteria dc = sysConfigDao.createDetachedCriteria();
		List<String> types = new ArrayList<String>();
		types.add(ConfigConstant.EMAIL_URL);
		types.add(ConfigConstant.EMAIL_USER_NAME);
		types.add(ConfigConstant.EMAIL_USER_PASS);
		types.add(ConfigConstant.PORT);
		types.add(ConfigConstant.IS_NEED_SSL);
		dc.add(Restrictions.in("type", types));
		List<SysConfig> configs = sysConfigDao.find(dc);
		HashMap<String, String> result = new HashMap<String, String>();
		for (SysConfig config : configs) {
			result.put(config.getType(), config.getConfig());
		}
		return result;
	}

}
