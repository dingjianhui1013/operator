/**
 *2016年9月14日 下午2:22:15
 */
package com.itrus.ca.modules.task;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private static Map<String, Integer> num = new Hashtable<String, Integer>();

	private static final int MAX_THREAD = 30;

	private Set<String> allsvn = Collections
			.synchronizedSet(new HashSet<String>());

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

	public static Integer getNum(String officeName) {
		if (num.containsKey(officeName)) {
			if (num.get(officeName) == null) {
				num.put(officeName, 0);
				return 0;
			} else {
				return num.get(officeName);
			}
		} else {
			num.put(officeName, 0);
			return 0;
		}
	}

	public static void plus(String officeName, Integer current) {
		Integer n = current + 1;
		num.put(officeName, n);
	}

	private class Inner implements Runnable {
		private List<WorkDealInfo> l;

		public Inner(List<WorkDealInfo> lst) {
			this.l = lst;
		}

		public synchronized String getSvn(String head, String officeName) {
			String numStr = StringHelper.completeText(num.get(officeName)
					.toString(), 6);
			return head + "-" + numStr;
		}

		public synchronized void run() {
			for (WorkDealInfo e : l) {
				try {
					Office office = officeService.get(e.getOfficeId());
					String firstSvn = workDealInfoService.getSVN(e,
							office.getName(), appid, getNum(office.getName()));
					String[] ele = firstSvn.split("-");
					String head = firstSvn.replace("-" + ele[3], "");
					Integer current = getNum(office.getName());
					String svn = getSvnByHead(office.getName(), head,current);
					workDealInfoService.modifySvn(e.getId(), svn);

				} catch (Exception ex) {
					// 事务问题，可以不显示
					continue;
				}

			}

		}

		private synchronized String getSvnByHead(String officeName,
				String head, Integer current) {
			plus(officeName, current);
			String svn = getSvn(head, officeName);
			if (allsvn.contains(svn)) {
				return getSvnByHead(officeName, head, current);
			}
			allsvn.add(svn);
			return svn;
		}
	}
}
