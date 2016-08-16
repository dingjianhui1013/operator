/**
 *2016年8月16日 下午7:34:01
 */
package com.itrus.ca.modules.task;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */

public class ModifyPreidThread implements Runnable {

	WorkDealInfoService workDealInfoService = SpringContextHolder
			.getBean(WorkDealInfoService.class);

	private Set<String> all;

	public ModifyPreidThread(Set<String> all) {
		this.all = all;
	}

	public void run() {
		workDealInfoService.processPreId(all);
	}

}
