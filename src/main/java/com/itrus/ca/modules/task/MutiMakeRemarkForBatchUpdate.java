package com.itrus.ca.modules.task;

import java.util.List;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.task.entity.BatchUpdateInfoScca;
import com.itrus.ca.modules.task.service.BatchUpdateInfoSccaService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

public class MutiMakeRemarkForBatchUpdate implements Runnable {

	WorkDealInfoService workDealInfoService = SpringContextHolder.getBean(WorkDealInfoService.class);

	BatchUpdateInfoSccaService batchUpdateInfoSccaService = SpringContextHolder
			.getBean(BatchUpdateInfoSccaService.class);

	
	private List<BatchUpdateInfoScca> list;
	
	
	public MutiMakeRemarkForBatchUpdate(List<BatchUpdateInfoScca> list) {
		super();
		this.list = list;
	}
	
	
	@Override
	public void run() {
		for(BatchUpdateInfoScca info:list){
			 batchUpdateInfoSccaService.validate(info);
			
		}
	}

}
