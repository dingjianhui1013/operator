/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.task.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.task.dao.BasicInfoSccaDao;
import com.itrus.ca.modules.task.entity.BasicInfoScca;
import com.itrus.ca.modules.work.entity.WorkCompany;

/**
 * 中间表数据Service
 * 
 * @author ZhangJingtao
 * @version 2014-08-18
 */
@Service
@Transactional(readOnly = true)
public class BasicInfoSccaService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory
			.getLogger(BasicInfoSccaService.class);

	@Autowired
	private BasicInfoSccaDao basicInfoSccaDao;

	public BasicInfoScca get(Long id) {
		return basicInfoSccaDao.findOne(id);
	}

	public Page<BasicInfoScca> find(Page<BasicInfoScca> page,
			BasicInfoScca basicInfoScca) {
		DetachedCriteria dc = basicInfoSccaDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(basicInfoScca.getName())) {
			dc.add(Restrictions.like("name",
					"%" + EscapeUtil.escapeLike(basicInfoScca.getName()) + "%"));
		}
		dc.addOrder(Order.desc("id"));
		return basicInfoSccaDao.find(page, dc);
	}

	public void deleteImpTempByFirstCertSN(String firstCertSN) {
		String sql = "delete from imptemp where FIRST_CERT_SN like '%"
				+ firstCertSN + "%'";
		basicInfoSccaDao.exeSql(sql);

	}

	public void saveImpTemp(String firstCertSN) {
		if (StringHelper.isNull(firstCertSN) || isExistTemp(firstCertSN.trim()))
			return;
		String sql = "insert into imptemp(FIRST_CERT_SN)";
		sql += " values('" + firstCertSN.trim() + "')";
		basicInfoSccaDao.exeSql(sql);
	}

	public boolean isExistTemp(String firstCertSN) {
		String sql = "select count(*) C from imptemp where FIRST_CERT_SN like '%"
				+ firstCertSN + "%'";
		List<Map> lst = null;
		try {
			lst = basicInfoSccaDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lst == null || lst.size() < 0)
			return false;
		Map m = lst.get(0);
		if (new Long(m.get("C").toString()) > 0)
			return true;
		return false;
	}

	@Transactional(readOnly = false)
	public void save(BasicInfoScca basicInfoScca) {
		basicInfoSccaDao.save(basicInfoScca);
	}

	@Transactional(readOnly = false)
	public void saveList(List<BasicInfoScca> sccas) {
		basicInfoSccaDao.save(sccas);
	}

	@Transactional(readOnly = false)
	public void delete(BasicInfoScca basicInfoScca) {
		basicInfoSccaDao.delete(basicInfoScca);
	}

	@Transactional(readOnly = false)
	public void deleteList(List<BasicInfoScca> sccas) {
		basicInfoSccaDao.delete(sccas);
	}

	@Transactional(readOnly = false)
	public List<BasicInfoScca> findAll() {
		DetachedCriteria dc = basicInfoSccaDao.createDetachedCriteria();
		dc.add(Restrictions.or(Restrictions.ne("used", true),
				Restrictions.isNull("used")));
		return (List<BasicInfoScca>) basicInfoSccaDao.find(dc);
	}

	public long getCount() {
		String sql = "select count(*) c from BASE_INFO_SCCA";
		sql += " where FIRST_CERT_SN is not null";
		sql += " and USED is null";
		List<Map> lst = null;
		try {
			lst = basicInfoSccaDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lst == null || lst.size() < 0)
			return 0l;
		Map m = lst.get(0);
		return new Long(m.get("C").toString());
	}

	/**
	 * used is null && firstCertSN is not null && ronwum<=max
	 * 
	 * @param max
	 * @return List<BasicInfoScca>
	 */
	public List<BasicInfoScca> find(int max) {
		DetachedCriteria dc = basicInfoSccaDao.createDetachedCriteria();
		dc.add(Restrictions.or(Restrictions.ne("used", true),
				Restrictions.isNull("used")));
		dc.add(Restrictions.isNotNull("firstCertSN"));
		Page<BasicInfoScca> page = new Page<BasicInfoScca>(1, 1000);
		page = basicInfoSccaDao.find(page, dc);
		if (page != null && page.getList() != null && page.getList().size() > 0)
			return page.getList();
		return null;
	}

	public long count(boolean firstCertSnIsNull) {
		String sql = "select count(*) c from BASE_INFO_SCCA";
		if (firstCertSnIsNull)
			sql += " where FIRST_CERT_SN is null";
		List<Map> lst = null;
		try {
			lst = basicInfoSccaDao.findBySQLListMap(sql, 0, 0);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException
				| InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lst == null || lst.size() < 0)
			return 0l;
		Map m = lst.get(0);
		return new Long(m.get("C").toString());
	}

	
	
	
}
