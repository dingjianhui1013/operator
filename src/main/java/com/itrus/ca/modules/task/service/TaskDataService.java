/**
 *2016年7月6日 下午6:50:09
 */
package com.itrus.ca.modules.task.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.task.dao.BasicInfoSccaDao;
import com.itrus.ca.modules.task.entity.BasicInfoScca;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDataService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(BasicInfoSccaService.class);

	@Autowired
	private BasicInfoSccaDao basicInfoSccaDao;

	@Autowired
	private WorkDealInfoService workDealInfoService;

	/**
	 * 处理完所有数据后，把preid填充
	 * 
	 * @param all
	 */
	public void processPreId(List<BasicInfoScca> all) {
		for (BasicInfoScca e : all) {
			// 根据首张证书序列判定是否需要处理workDealInfo表内的preId字段
			if (StringHelper.isNull(e.getFirstCertSN()))
				continue;
			// 只1条记录也不处理
			int c = workDealInfoService.getCountByFirstCertSN(e
					.getFirstCertSN());
			if (c <= 1)
				continue;
			List<WorkDealInfo> lst = workDealInfoService.findByFirstCertSN(e
					.getFirstCertSN());
		}
	}
	
	
	
	
}
