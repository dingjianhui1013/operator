package com.itrus.ca.modules.finance.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.modules.finance.dao.FinanceQuitMoneyDao;
import com.itrus.ca.modules.finance.entity.FinancePaymentInfo;
import com.itrus.ca.modules.finance.entity.FinanceQuitMoney;
import com.itrus.ca.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class FinanceQuitMoneyService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(FinanceQuitMoneyService.class);

	@Autowired
	FinanceQuitMoneyDao financeQuitMoneyDao;

	public FinanceQuitMoney get(Long id) {
		return financeQuitMoneyDao.findOne(id);
	}
	
	
	@Transactional(readOnly = false)
	public Page<FinanceQuitMoney> findAll(Page<FinanceQuitMoney> page, String commUserName, String payStartTime,
			String payEndTime, String quitStartTime, String quitEndTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = financeQuitMoneyDao.createDetachedCriteria();
		dc.createAlias("financePaymentInfo", "financePaymentInfo");
		if (!Strings.isNullOrEmpty(commUserName)) {
			dc.add(Restrictions.eq("financePaymentInfo.commUserName", commUserName));
		}
		try {
			if (payStartTime != null && payEndTime != null && !"".equals(payEndTime) && !"".equals(payStartTime)) {
				dc.add(Restrictions.ge("financePaymentInfo.payDate", format.parse(payStartTime)));
				dc.add(Restrictions.le("financePaymentInfo.payDate", format.parse(payEndTime)));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			if (quitStartTime != null && quitEndTime != null && !"".equals(quitEndTime) && !"".equals(quitStartTime)) {
				dc.add(Restrictions.ge("quitDate", format.parse(quitStartTime)));
				dc.add(Restrictions.le("quitDate", format.parse(quitEndTime)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dc.addOrder(Order.desc("id"));
		return financeQuitMoneyDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(FinanceQuitMoney financeQuitMoney) {
		financeQuitMoneyDao.save(financeQuitMoney);
	}

	public List<FinanceQuitMoney> findAll(String commUserName, String payStartTime, String payEndTime,
			String quitStartTime, String quitEndTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DetachedCriteria dc = financeQuitMoneyDao.createDetachedCriteria();

		if (!Strings.isNullOrEmpty(commUserName)) {
			dc.createAlias("financePaymentInfo", "financePaymentInfo");
			dc.add(Restrictions.eq("financePaymentInfo.commUserName", commUserName));
		}
		try {

			if (payStartTime != null && payEndTime != null && !"".equals(payEndTime) && !"".equals(payStartTime)) {
				dc.createAlias("financePaymentInfo", "financePaymentInfo");
				dc.add(Restrictions.ge("financePaymentInfo.payDate", format.parse(payStartTime)));
				dc.add(Restrictions.le("financePaymentInfo.payDate", format.parse(payEndTime)));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			if (quitStartTime != null && quitEndTime != null && !"".equals(quitEndTime) && !"".equals(quitStartTime)) {
				dc.add(Restrictions.ge("quitDate", format.parse(quitStartTime)));
				dc.add(Restrictions.le("quitDate", format.parse(quitEndTime)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dc.addOrder(Order.desc("id"));
		return financeQuitMoneyDao.find(dc);
	}

	@Transactional(readOnly = false)
	public void save(FinancePaymentInfo financePaymentInfo, String quitReason) {
		FinanceQuitMoney financeQuitMoney = new FinanceQuitMoney();
		financeQuitMoney.setQuitDate(new Date());
		financeQuitMoney.setQuitMoney(financePaymentInfo.getResidueMoney());
		financeQuitMoney.setQuitReason(quitReason);
		financeQuitMoney.setQuitWindow(UserUtils.getUser().getOffice().getName());
		financeQuitMoney.setFinancePaymentInfo(financePaymentInfo);
		financeQuitMoneyDao.save(financeQuitMoney);
	}
}
