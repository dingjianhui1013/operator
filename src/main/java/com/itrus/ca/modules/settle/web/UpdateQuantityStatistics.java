package com.itrus.ca.modules.settle.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.settle.biz.impl.SettleBizI;
import com.itrus.ca.modules.settle.dao.SettleSupplierCertDetailDao;
import com.itrus.ca.modules.settle.entity.SettleSupplierCertDetail;

@Service
public class UpdateQuantityStatistics implements SettleBizI {

	@Autowired
	private SettleSupplierCertDetailDao settleSupplierCertDetailDao;

	@Override
	public boolean updateOUSettleInfo(String ou, Integer productType,
			Integer type, Integer amount, Integer year,ConfigSupplier configSupplier) {
		try {
			Date date = new Date();
			SettleSupplierCertDetail settleSlCD = findByOuAndProductType(ou,
					productType, date,configSupplier);
			switch (type) {
			case 1:
				settleSlCD.setReplaceAmount(settleSlCD.getReplaceAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);
				break;
			case 2:
				settleSlCD.setRevokeAmount(settleSlCD.getRevokeAmount() + amount);
				break;
			case 3:
				settleSlCD.setTestAmount(settleSlCD.getTestAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);
				break;
			case 4:
				settleSlCD.setRwBbAmount(settleSlCD.getRwBbAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);
				break;
			case 5:
				settleSlCD.setFrwBbAmount(settleSlCD.getFrwBbAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);
				break;
			case 6:
				settleSlCD.setChangeAmount(settleSlCD.getChangeAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);
				break;
			case -1:
				settleSlCD.setAddAmount(settleSlCD.getAddAmount() + amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() + amount);// 预留一个新增的
				break;
			}
			if (type != 2&&type != 3) {
				switch (year) {
				case 1:
					settleSlCD.setAmountYear1(settleSlCD.getAmountYear1() + amount);
					break;
				case 2:
					settleSlCD.setAmountYear2(settleSlCD.getAmountYear2() + amount);
					break;
				case 4:
					settleSlCD.setAmountYear4(settleSlCD.getAmountYear4() + amount);
					break;
				case 5:
					settleSlCD.setAmountYear5(settleSlCD.getAmountYear5() + amount);
					break;
				}
			}
			

			settleSupplierCertDetailDao.save(settleSlCD);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delOUSettleInfo(String ou, Integer productType,
			Integer type, Integer amount, Integer year,ConfigSupplier configSupplier,Date signDate) {
		try {
			Date date = signDate;
			SettleSupplierCertDetail settleSlCD = findByOuAndProductType(ou,
					productType, date,configSupplier);
			switch (type) {
			case 1:
				settleSlCD.setReplaceAmount(settleSlCD.getReplaceAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);
				break;
			case 2:
				settleSlCD.setRevokeAmount(settleSlCD.getRevokeAmount() - amount);
				break;
			case 3:
				settleSlCD.setTestAmount(settleSlCD.getTestAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);
				break;
			case 4:
				settleSlCD.setRwBbAmount(settleSlCD.getRwBbAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);
				break;
			case 5:
				settleSlCD.setFrwBbAmount(settleSlCD.getFrwBbAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);
				break;
			case 6:
				settleSlCD.setChangeAmount(settleSlCD.getChangeAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);
				break;
			case -1:
				settleSlCD.setAddAmount(settleSlCD.getAddAmount() - amount);
				settleSlCD.setTotalAmount(settleSlCD.getTotalAmount() - amount);// 预留一个新增的
				break;
			}
			if (type != 2&&type != 3) {
				switch (year) {
				case 1:
					settleSlCD.setAmountYear1(settleSlCD.getAmountYear1() - amount);
					break;
				case 2:
					settleSlCD.setAmountYear2(settleSlCD.getAmountYear2() - amount);
					break;
				case 4:
					settleSlCD.setAmountYear4(settleSlCD.getAmountYear4() - amount);
					break;
				}
			}
			settleSupplierCertDetailDao.save(settleSlCD);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public SettleSupplierCertDetail findByOuAndProductType(String ou,
			Integer productType, Date date,ConfigSupplier configSupplier) throws ParseException {
		DetachedCriteria dc = settleSupplierCertDetailDao
				.createDetachedCriteria();

		if (ou != null && "".equals(ou)) {
			dc.add(Restrictions.eq("ou", ou));
		}
		if (productType != null) {
			dc.add(Restrictions.eq("productType", productType));
		}
		dc.createAlias("configSupplier", "configSupplier");
		dc.add(Restrictions.eq("configSupplier.supplierName", configSupplier.getSupplierName()));
		// dc.add(Restrictions.eq(SettleSupplierCertDetail.DEL_FLAG,
		// SettleSupplierCertDetail.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		dc.add(Restrictions.eq("settleDate", sdf.parse(sdf.format(new Date()))));// 格式时间为2014-07-09 00:00:00
		List<SettleSupplierCertDetail> details = settleSupplierCertDetailDao
				.find(dc);
		if (details == null || details.size() == 0) {
			SettleSupplierCertDetail detail = new SettleSupplierCertDetail(configSupplier, sdf.parse(sdf.format(new Date())), productType, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, ou);
			detail.setAddAmount(0);
			settleSupplierCertDetailDao.save(detail);
			return detail;
		} else {
			return details.get(0);
		}
	}

}
