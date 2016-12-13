package com.itrus.ca.modules.task.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.task.dao.BatchUpdateInfoSccaDao;
import com.itrus.ca.modules.task.entity.BatchUpdateInfoScca;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * 批量更新中间表数据Service
 * 
 * @author ZhangJingtao
 * @version 2014-08-18
 */
@Service
@Transactional(readOnly = false)
public class BatchUpdateInfoSccaService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(BatchUpdateInfoSccaService.class);

	@Autowired
	private BatchUpdateInfoSccaDao batchUpdateInfoSccaDao;

	
	@Autowired
	private WorkDealInfoService workDealInfoService;
	
	public BatchUpdateInfoScca get(Long id) {
		return batchUpdateInfoSccaDao.findOne(id);
	}


	@Transactional(readOnly = false)
	public void save(BatchUpdateInfoScca batchUpdateInfoScca) {
		batchUpdateInfoSccaDao.save(batchUpdateInfoScca);
	}

	@Transactional(readOnly = false)
	public void saveList(List<BatchUpdateInfoScca> sccas) {
		batchUpdateInfoSccaDao.save(sccas);
	}

	@Transactional(readOnly = false)
	public void delete(BatchUpdateInfoScca sccas) {
		batchUpdateInfoSccaDao.delete(sccas);
	}

	@Transactional(readOnly = false)
	public void deleteList(List<BatchUpdateInfoScca> sccas) {
		batchUpdateInfoSccaDao.delete(sccas);
	}

	@Transactional(readOnly = true)
	public List<BatchUpdateInfoScca> findAll() {
		DetachedCriteria dc = batchUpdateInfoSccaDao.createDetachedCriteria();
		/*dc.add(Restrictions.or(Restrictions.ne("used", true),
				Restrictions.isNull("used")));*/
		return (List<BatchUpdateInfoScca>) batchUpdateInfoSccaDao.find(dc);
	}

	public long getCount() {
		String sql = "select count(*) c from BASE_INFO_SCCA";
		sql += " where FIRST_CERT_SN is not null";
		sql += " and USED is null";
		List<Map> lst = null;
		try {
			lst = batchUpdateInfoSccaDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			e.printStackTrace();
		}
		if (lst == null || lst.size() < 0)
			return 0l;
		Map m = lst.get(0);
		return new Long(m.get("C").toString());
	}

	
	public List<BatchUpdateInfoScca> findByDealInfoId(Long id){
		DetachedCriteria dc = batchUpdateInfoSccaDao.createDetachedCriteria();
		      dc.add(Restrictions.eq("workDealInfoId", id));
		return (List<BatchUpdateInfoScca>) batchUpdateInfoSccaDao.find(dc);
	}
	
	
	/**
	 * 
	 * 
	 * @return
	 * 
	 * */
    public int validate(BatchUpdateInfoScca scca){
    	
    	WorkDealInfo info = workDealInfoService.findByCertSnOne(scca.getCertSn().trim());
    	
    	if(info==null){
    		
    		logger.debug("未匹配证书序列号!证书序列号为:"+scca.getCertSn().trim());
    		
    		scca.setStatusCode(2);
    		
    		save(scca);
    		
    		return 2;
    	}
    	
    	if(!info.getKeySn().trim().equals(scca.getKeySn().trim())){
    	    
    		logger.debug("未匹配KEY序列号!临时表的KEY序列号为:"+scca.getKeySn().trim()+",业务表的Key序列号为:"+info.getKeySn().trim());
    	
    		scca.setStatusCode(3);
    		
    		save(scca);
    		
    		return 3;
    	}
    	
    	if(info.getDelFlag().equals("1")){
    		info = workDealInfoService.findLastByFirstCertSN(info.getFirstCertSN());
    		
    		
    	}
    	
    	
    	
    	if(!(info.getDealInfoStatus().equals("6")||info.getDealInfoStatus().equals("7"))){
    		logger.debug("业务不是制证成功或吊销状态!业务ID为:"+info.getId());
    		
    		scca.setStatusCode(4);
    	
    		save(scca);
    		
    		return 4;
    	
    	}
    	
    	
    	if((StringHelper.getDvalueDay(new Date(), info.getNotafter()) > 60)){
    		logger.debug("业务未在更新范围内!业务ID为:"+info.getId());
    		
    		scca.setStatusCode(5);
    		
    		save(scca);
    		
    		return 5;
    	}
    	
    	
    	info.setRemarkInfo(scca.getRemark());
    	
    	workDealInfoService.save(info);
    	
    	scca.setStatusCode(1);
		scca.setWorkDealInfoId(info.getId());
		save(scca);
    	return 1;
    	
    }
	
}
