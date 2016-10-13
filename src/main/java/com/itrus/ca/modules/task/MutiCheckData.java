package com.itrus.ca.modules.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.task.vo.WorkDealInfoVo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

public class MutiCheckData implements Runnable {

	WorkDealInfoService workDealInfoService = SpringContextHolder
			.getBean(WorkDealInfoService.class);
	
	Logger log = Logger.getLogger(MutiCheckData.class);
	
	private List<Object> list;
	
	
	
	public MutiCheckData(List<Object> list) {
		super();
		this.list = list;
	}



	@Override
	public void run() {
		
		String firstCertSN = null;
		
		
		
		for(Object Id:list){
			List<WorkDealInfoVo> list = workDealInfoService.findByPrevId((Long)Id);
			
			firstCertSN = list.get(0).getFirstCertSN();
			
			for(WorkDealInfoVo dealInfo : list){
				if(!dealInfo.getFirstCertSN().equals(firstCertSN)){
						for(WorkDealInfoVo dealInfo1 : list){
							
							log.debug(dealInfo1);
							
						}
						log.debug("==========================================================================================");
						break;

					
				}
			}
			
		}

	}

}
