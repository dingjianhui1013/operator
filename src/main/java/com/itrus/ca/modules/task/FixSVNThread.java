/**
 *2016年9月14日 下午2:22:15
 */
package com.itrus.ca.modules.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */
public class FixSVNThread implements Runnable {

	private List<WorkDealInfo> all;

	private static volatile long num = 0;

	private static final int MAX_THREAD = 30;

	private Long appid;

	Logger log = Logger.getLogger(ModifyPreidThread.class);

	WorkDealInfoService workDealInfoService = SpringContextHolder
			.getBean(WorkDealInfoService.class);

	OfficeService officeService = SpringContextHolder
			.getBean(OfficeService.class);

	public FixSVNThread(Long appid, Integer count) {
		all = workDealInfoService.findListAppId(appid);
		this.appid = appid;
	}

	public void run() {
		try {
			workDealInfoService.setSvnToNull(appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int preNum = all.size() / MAX_THREAD;
		preNum = preNum == 0 ? all.size() : preNum;

		if (all.size() <= preNum) {
			new Thread(new Inner(all)).start();
		} else {
			for (int i = 0; i < MAX_THREAD; i++) {
				int start = i * preNum;
				int end = (i + 1) * preNum;
				List<WorkDealInfo> l = all.subList(start, end);
				new Thread(new Inner(l)).start();
			}
			int remainder = all.size() % MAX_THREAD; // 余数处理
			if (remainder > 0) {
				List<WorkDealInfo> l = all.subList(MAX_THREAD * preNum,
						all.size());
				new Thread(new Inner(l)).start();
			}
		}
	}

	private class Inner implements Runnable {
		private List<WorkDealInfo> l;

		public Inner(List<WorkDealInfo> lst) {
			this.l = lst;
		}

		public String getSvn(String head, Integer num) {
			String numStr = "00000" + num;
			return head + "-"
					+ numStr.substring(numStr.length() - 6, numStr.length());
		}

		public void run() {
			try {
				for (WorkDealInfo e : l) {
					Office office = officeService.get(e.getOfficeId());
					String firstSvn = workDealInfoService.getSVN(
							office.getName(), appid);
					Integer num = Integer.valueOf(firstSvn.split("-")[3]);
					String head = firstSvn.replace(
							"-" + firstSvn.split("-")[3], "");

					workDealInfoService.modifySvn(e.getId(), getSvn(head, num));
					System.out.println("firstSvn===============" + firstSvn
							+ " | " + e.getId() + " | " + e.getFirstCertSN());

				}

			} catch (IllegalStateException e) {
				// 事务问题，可以不显示
			} catch (Exception e) {
				e.printStackTrace();
				log.error(StringHelper.getStackInfo(e));
			}
		}
	}
}
