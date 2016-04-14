/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.signature.service;
import java.util.List;

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
import com.itrus.ca.modules.signature.entity.SignatureAgentBoundConfigApp;
import com.itrus.ca.modules.signature.dao.SignatureAgentBoundConfigAppDao;

/**
 * 计费策略模版详情历史Service
 * @author HuHao
 * @version 2016-03-20
 */
@Component
@Transactional(readOnly = true)
public class SignatureAgentBoundConfigAppService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SignatureAgentBoundConfigAppService.class);
	
	@Autowired
	private SignatureAgentBoundConfigAppDao signatureAgentBoundConfigAppDao;
	
	public SignatureAgentBoundConfigApp get(Long id) {
		return signatureAgentBoundConfigAppDao.findOne(id);
	}
	
	public Page<SignatureAgentBoundConfigApp> find(Page<SignatureAgentBoundConfigApp> page, SignatureAgentBoundConfigApp signatureAgentBoundConfigApp) {
		DetachedCriteria dc = signatureAgentBoundConfigAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq(SignatureAgentBoundConfigApp.DEL_FLAG, SignatureAgentBoundConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signatureAgentBoundConfigAppDao.find(page, dc);
	}
	
	
	public List<SignatureAgentBoundConfigApp> findByAppAgent(Long appId,Long agentId) {
		DetachedCriteria dc = signatureAgentBoundConfigAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		dc.add(Restrictions.eq("app.id", appId));
		dc.add(Restrictions.eq(SignatureAgentBoundConfigApp.DEL_FLAG, SignatureAgentBoundConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signatureAgentBoundConfigAppDao.find(dc);
	}
	
	public List<SignatureAgentBoundConfigApp> findByAgent(Long agentId) {
		DetachedCriteria dc = signatureAgentBoundConfigAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq("agent.id", agentId));
		dc.add(Restrictions.eq(SignatureAgentBoundConfigApp.DEL_FLAG, SignatureAgentBoundConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signatureAgentBoundConfigAppDao.find(dc);
	}
	
	
	
	public List<SignatureAgentBoundConfigApp> findByApp(Long appId) {
		DetachedCriteria dc = signatureAgentBoundConfigAppDao.createDetachedCriteria();
		dc.add(Restrictions.eq("app.id", appId));
		dc.add(Restrictions.eq(SignatureAgentBoundConfigApp.DEL_FLAG, SignatureAgentBoundConfigApp.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return signatureAgentBoundConfigAppDao.find(dc);
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(SignatureAgentBoundConfigApp signatureAgentBoundConfigApp) {
		signatureAgentBoundConfigAppDao.save(signatureAgentBoundConfigApp);
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		signatureAgentBoundConfigAppDao.deleteId(id);
	}
	
	
	
	
}
