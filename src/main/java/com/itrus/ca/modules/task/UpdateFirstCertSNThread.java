/**
 *2016年8月23日 上午11:13:04
 */
package com.itrus.ca.modules.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * @author: liubin
 *
 */
@Service
public class UpdateFirstCertSNThread {
	@Autowired
	WorkDealInfoService workDealInfoService;

	private static int MAX_THREAD = 10;

	public void process(int updateCount, int appid) {
		// workDealInfoService.fixAllDataFirstCertSN(updateCount);

		List<Long> lst = workDealInfoService.findNullFirstCertSNByCount(
				updateCount, appid);

		int count = lst.size();
		int size = lst.size() > 300 ? lst.size() / MAX_THREAD : lst.size();
		int end = 0;
		int start = 0;
		for (int i = 0; i < MAX_THREAD; i++) {
			start = i == 0 ? 0 : end;
			end = size + i * size;
			if (end > count)
				break;
			new Thread(new Inner(lst.subList(start, end))).start();
		}
		if (end < count) {
			new Thread(new Inner(lst.subList(end, count))).start();
		}

	}

	private class Inner implements Runnable {

		private List<Long> lst;

		public Inner(List<Long> lst) {
			this.lst = lst;
		}

		public void run() {
			for (Long e : lst) {
				try {
					workDealInfoService.fixFirstCertSN(e);
				} catch (IllegalStateException ex) {
					// 事务问题，可以不显示
					continue;
				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}
			}
		}
	}
}
