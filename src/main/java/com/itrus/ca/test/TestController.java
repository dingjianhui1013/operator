package com.itrus.ca.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.itrus.ca.common.test.SpringTransactionalContextTests;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.receipt.entity.ReceiptDepotInfo;
import com.itrus.ca.modules.receipt.service.ReceiptDepotInfoService;
import com.itrus.ca.modules.receipt.service.ReceiptLogService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.task.AutoTask;

public class TestController extends SpringTransactionalContextTests{
	
	@Test
	public void testOffice(){
//		SystemService systemService = SpringContextHolder.getBean(SystemService.class);
//		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
//		User user = systemService.getUser(1L);
//		List<Office> offices = officeService.getOfficeByType(user, 2);
//		System.out.println(offices.size());
//		ReceiptDepotInfoService receiptDepotInfoService = SpringContextHolder.getBean(ReceiptDepotInfoService.class);
//		ReceiptLogService receiptLogService = SpringContextHolder.getBean(ReceiptLogService.class);
//		List<ReceiptDepotInfo> receiptDepotInfos = receiptDepotInfoService.findAllDepot();
//		for(ReceiptDepotInfo receiptDepotInfo :receiptDepotInfos){
//			ReceiptLog rl = new ReceiptLog();
//			rl.setReceiptDepotInfo(receiptDepotInfo);
//			Date date = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String dayTime = sdf.format(date);
//			try {
//				rl.setCreateDate(new Timestamp(sdf.parse(dayTime).getTime()));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			rl.setReceiptTotal(receiptDepotInfo.getReceiptTotal());
//			rl.setReceiptOut(receiptDepotInfo.getReceiptOut());
//			rl.setReceiptResidue(receiptDepotInfo.getReceiptResidue());
//		}
		AutoTask autoTask = new AutoTask();
//		autoTask.staticDayCertData();
		try {
			autoTask.workDayStatic();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
