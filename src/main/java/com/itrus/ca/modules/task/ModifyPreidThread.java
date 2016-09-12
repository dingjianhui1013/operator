/**
 *2016年8月16日 下午7:34:01
 */
package com.itrus.ca.modules.task;

import java.util.Set;

import org.apache.log4j.Logger;
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

	Logger log = Logger.getLogger(ModifyPreidThread.class);

	public ModifyPreidThread(Set<String> all) {
		this.all = all;
	}

	public void run() {
		try {
			log.info("修复prev_id子线程开始处理,本线程处理条数:" + all.size());
			long s = System.currentTimeMillis();
			workDealInfoService.processPreId(all);
			log.info("修复prev_id子线程处理完毕,本线程处理条数:" + all.size() + ",处理时间:"
					+ (System.currentTimeMillis() - s) / 1000 + "s");
		} catch (IllegalStateException e) {
			// 事务问题，可以不显示
		} catch (Exception e) {
			e.printStackTrace();
			log.error(StringHelper.getStackInfo(e));
		}
	}

}
