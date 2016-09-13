/**
 *2016年8月16日 下午7:34:01
 */
package com.itrus.ca.modules.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */

public class ModifyPreidThread implements Runnable {

	WorkDealInfoService workDealInfoService = SpringContextHolder
			.getBean(WorkDealInfoService.class);

	private List<String> all;

	Logger log = Logger.getLogger(ModifyPreidThread.class);

	private static final int MAX_THREAD = 30;

	public ModifyPreidThread(List<String> all) {
		this.all = all;
	}

	public void run() {
		try {
			log.info("修复prev_id子线程开始处理,本线程处理条数:" + all.size());
			long s = System.currentTimeMillis();
			int preNum = all.size() / MAX_THREAD;

			if (all.size() <= preNum) {
				new Thread(new Inner(all)).start();
			} else {
				for (int i = 0; i < MAX_THREAD; i++) {
					int start = i * preNum;
					int end = (i + 1) * preNum;
					List<String> l = all.subList(start, end);
					new Thread(new Inner(l)).start();
				}
				int remainder = all.size() % MAX_THREAD; // 余数处理
				if (remainder > 0) {
					List<String> l = all.subList(MAX_THREAD * preNum,
							all.size());
					new Thread(new Inner(l)).start();
				}
			}

			log.info("修复prev_id子线程处理完毕,本线程处理条数:" + all.size() + ",处理时间:"
					+ (System.currentTimeMillis() - s) / 1000 + "s");
		} catch (IllegalStateException e) {
			// 事务问题，可以不显示
		} catch (Exception e) {
			e.printStackTrace();
			log.error(StringHelper.getStackInfo(e));
		}
	}

	private class Inner implements Runnable {
		private List<String> l;

		public Inner(List<String> lst) {
			this.l = lst;
		}

		public void run() {
			try {
				workDealInfoService.processPreId(l);
			} catch (IllegalStateException e) {
				// 事务问题，可以不显示
			} catch (Exception e) {
				e.printStackTrace();
				log.error(StringHelper.getStackInfo(e));
			}
		}
	}

}
