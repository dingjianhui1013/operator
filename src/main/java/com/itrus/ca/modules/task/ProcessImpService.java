/**
 *2016年11月1日 下午3:15:31
 */
package com.itrus.ca.modules.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itrus.ca.common.service.SendMsgService;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.key.service.KeyDepotGeneralStatisticsService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.service.ConfigAgentAppRelationService;
import com.itrus.ca.modules.profile.service.ConfigAgentOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentDetailService;
import com.itrus.ca.modules.profile.service.ConfigChargeAgentService;
import com.itrus.ca.modules.profile.service.ConfigProductService;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptEnterInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptInvoiceService;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;
import com.itrus.ca.modules.statistic.service.StatisticAppDataService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataProductService;
import com.itrus.ca.modules.statistic.service.StatisticCertDataService;
import com.itrus.ca.modules.statistic.service.StatisticDayDataService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.task.entity.BasicInfoScca;
import com.itrus.ca.modules.task.service.BasicInfoSccaService;
import com.itrus.ca.modules.work.dao.WorkDealInfoDao;
import com.itrus.ca.modules.work.service.WorkCertApplyInfoService;
import com.itrus.ca.modules.work.service.WorkCertInfoService;
import com.itrus.ca.modules.work.service.WorkCompanyHisService;
import com.itrus.ca.modules.work.service.WorkCompanyService;
import com.itrus.ca.modules.work.service.WorkDealInfoService;
import com.itrus.ca.modules.work.service.WorkLogService;
import com.itrus.ca.modules.work.service.WorkPayInfoService;
import com.itrus.ca.modules.work.service.WorkUserHisService;
import com.itrus.ca.modules.work.service.WorkUserService;

/**
 * @author: liubin
 *
 */
@Service
public class ProcessImpService {

	/**
	 * 主处理流程,限制每次处理的批量最大数
	 * 
	 * @param all
	 * @param officeId
	 * @param maxProcess
	 */
	public void process(List<BasicInfoScca> all, long officeId, int maxProcess) {
		if (all == null || all.size() <= 0)
			all = basicInfoSccaService.find(maxProcess);
		String firstSvn = getFirstSvn(officeId);
		if (StringHelper.isNull(firstSvn))
			return;
		Integer num = Integer.valueOf(firstSvn.split("-")[3]);
		String head = firstSvn.replace("-" + firstSvn.split("-")[3], "");

		for (int i = 0; i < all.size(); i++) {
			String svn = getSvn(head, num);
			num++;
			all.get(i).setSvnNum(svn);
		}
		// 启线程异步处理
		List<Thread> allThread = processAllThread(all, officeId, 100);

		for (Thread thread1 : allThread) {
			try {
				thread1.join();
			} catch (InterruptedException e) {
				exLog.error(StringHelper.getStackInfo(e));
				e.printStackTrace();
			}
		}
		// 递归
		List<BasicInfoScca> temp = basicInfoSccaService.find(maxProcess);
		// 数据都处理完毕
		if (temp == null || temp.size() <= 0) {
			processEnd();
			return;
		}
		// 还有数据则递归
		process(temp, officeId, maxProcess);
	}

	/**
	 * 数据都处理完毕后执行
	 */
	private void processEnd() {
		String sql = "select FIRST_CERT_SN from imptemp";
		List<String> firstSnAll = workDealInfoDao.findBySql(sql);
		if (firstSnAll == null || firstSnAll.size() <= 0)
			return;
		// 业务链中，只有最后一条是0，前面的都是1
		try {
			new Thread(new ModifyPreidThread(firstSnAll)).start();
		} catch (Exception e) {
			e.printStackTrace();
			exLog.error(StringHelper.getStackInfo(e));
		}
	}

	private List<Thread> processAllThread(List<BasicInfoScca> all,
			long officeId, int threadCount) {
		User createBy = new User(1L);
		List<Thread> allThread = new ArrayList<Thread>();
		int tempSize = all.size();
		if (tempSize <= threadCount) {
			List<BasicInfoScca> transToTrheads = new ArrayList<BasicInfoScca>();
			for (int i = 0; i < tempSize; i++) {
				transToTrheads.add(all.get(i));
			}
			MutiProcess mp = new MutiProcess(transToTrheads, officeId,
					createBy, 1);
			Thread thread = new Thread(mp);
			thread.start();
			allThread.add(thread);
		} else {
			int divisor = tempSize / threadCount;
			int remainder = tempSize % threadCount;

			for (int i = 0; i < threadCount; i++) {
				int newSize = divisor;
				if (i < remainder) {
					newSize = divisor + 1;
				}
				List<BasicInfoScca> transToTrheads = new ArrayList<BasicInfoScca>();
				for (int j = newSize - 1; j >= 0; j--) {
					transToTrheads.add(all.get(j));
					all.remove(j);
				}
				MutiProcess mp = new MutiProcess(transToTrheads, officeId,
						createBy, (i + 1));
				Thread thread = new Thread(mp);
				thread.start();
				allThread.add(thread);
			}
		}
		return allThread;
	}

	public static String getSvn(String head, Integer num) {
		String numStr = "00000" + num;
		return head + "-"
				+ numStr.substring(numStr.length() - 6, numStr.length());
	}

	private String getFirstSvn(long officeId) {
		String firstSvn = "";
		if (officeId != 1L) {
			Office office = officeService.get(officeId);
			List<User> users = office.getUserList();
			if (users.size() == 0) {
				return "";
			}
			firstSvn = workDealInfoService.getSVN(office.getName());
		}
		return firstSvn;

	}

	@Autowired
	KeyUsbKeyService keyUsbKeyService;
	@Autowired
	KeyUsbKeyInvoiceService keyUsbKeyInvoiceService;
	@Autowired
	ReceiptEnterInfoService receiptEnterInfoService;
	@Autowired
	ReceiptInvoiceService receiptInvoiceService;
	@Autowired
	WorkDealInfoService workDealInfoService;
	@Autowired
	ReceiptDepotInfoService receiptDepotInfoService;
	@Autowired
	ReceiptLogService receiptLogService;
	@Autowired
	private ConfigAppService configAppService;
	@Autowired
	ConfigAppOfficeRelationService configAppOfficeRelationService;
	@Autowired
	StatisticCertDataService statisticCertDataService;
	@Autowired
	StatisticAppDataService statisticAppDataService;
	@Autowired
	private KeyDepotGeneralStatisticsService keyDepotGeneralStatisticsService;
	@Autowired
	private KeyUsbKeyDepotService keyUsbKeyDepotService;
	@Autowired
	private StatisticCertDataProductService statisticCertDataProductService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private StatisticDayDataService statisticDayDataService;
	@Autowired
	private SendMsgService msgService;
	@Autowired
	private ConfigAgentAppRelationService configAgentAppRelationService;
	@Autowired
	private ConfigAgentOfficeRelationService configAgentOfficeRelationService;
	@Autowired
	private WorkCertInfoService workCertInfoService;
	@Autowired
	private WorkLogService workLogService;
	@Autowired
	private ConfigChargeAgentDetailService configChargeAgentDetailService;
	@Autowired
	private ConfigChargeAgentService configChargeAgentService;
	@Autowired
	private WorkPayInfoService workPayInfoService;
	@Autowired
	private WorkUserService workUserService;
	@Autowired
	private WorkCompanyService workCompanyService;
	@Autowired
	private ConfigProductService configProductService;
	@Autowired
	private WorkCompanyHisService workCompanyHisService;
	@Autowired
	private WorkUserHisService workUserHisService;
	@Autowired
	private BasicInfoSccaService basicInfoSccaService;
	@Autowired
	private WorkCertApplyInfoService workCertApplyInfoService;
	@Autowired
	private UpdateFirstCertSNThread updateFirstCertSNThread;

	@Autowired
	private WorkDealInfoDao workDealInfoDao;

	private LogUtil logUtil = new LogUtil();

	private Log exLog = LogFactory.getLog("ex");

	Log impLog = LogFactory.getLog("DataFile");

}
