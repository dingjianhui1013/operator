/**
 *2016年10月14日 上午3:58:04
 */
package com.itrus.ca.modules.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */
public class FixFirstSNThread3 implements Runnable {

	private Logger log = Logger.getLogger("fix");
	private Logger exLog = Logger.getLogger("ex");
	private List<String> all;
	private static final int MAX_THREAD = 30;

	WorkDealInfoService workDealInfoService = SpringContextHolder
			.getBean(WorkDealInfoService.class);

	public FixFirstSNThread3(List<String> all) {
		this.all = all;
	}

	@Override
	public void run() {
		try {
			int preNum = all.size() / MAX_THREAD;
			preNum = preNum == 0 ? all.size() : preNum;

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
		} catch (Exception e) {
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
	}

	private class Inner implements Runnable {
		private List<String> l;

		public Inner(List<String> lst) {
			this.l = lst;
		}

		public synchronized void run() {
			try {
				workDealInfoService.fixFirstCertSNByError3(l);
			} catch (Exception ex) {
				// 事务问题，可以不显示
			}
		}
	}
}
