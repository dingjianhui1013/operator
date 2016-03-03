/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.service;

import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.work.dao.WorkLogDao;
import com.itrus.ca.modules.work.entity.WorkCompany;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkLog;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工作记录Service
 * @author WangHongwei
 * @version 2014-06-13
 */
@Component
@Transactional(readOnly = true)
public class WorkLogService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(WorkLogService.class);
	
	@Autowired
	private WorkLogDao workLogDao;
	
	public WorkLog get(Long id) {
		return workLogDao.findOne(id);
	}
	
	public Page<WorkLog> find(Page<WorkLog> page, WorkLog workLog) {
		
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		//		if (StringUtils.isNotEmpty(workLog.getName())){
//			dc.add(Restrictions.like("name", "%"+workLog.getName()+"%"));
//		}
//		dc.add(Restrictions.eq(WorkLog.DEL_FLAG, WorkLog.DEL_FLAG_NORMAL));
		dc.add(Restrictions.eq("state",1));
		if(workLog.getWorkCompany()!=null){
			dc.add(Restrictions.eq("workCompany",workLog.getWorkCompany()));
		}
		dc.addOrder(Order.desc("id"));
		return workLogDao.find(page, dc);
	}
	
	public String findName(String name){
		String retu = "true";
		if(name!=null || "".equals(name)){
			Query off = workLogDao.createSqlQuery("SELECT s.OFFICE_ID from WORK_INFO_LOG w,SYS_USER s where s.ID = w.CREATE_BY AND s.NAME Like '%"+name+"%'");
			List lists = off.list();
			if(lists.size()==0){
				retu = "false";
			}
		}
		return retu;
	}
	public List<WorkLog> findworkLogs(String name,Date staDate,Date endDate,Long appId)
	{
		
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		if(name!=null&&!"".equals(name))
		{
			dc.add(Restrictions.like("createBy.name", "%"+name+"%"));
		}
		if(staDate!=null&&!"".equals(staDate))
		{
			dc.add(Restrictions.ge("createDate",staDate));
		}
		if(endDate!=null&&!"".equals(endDate))
		{
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			dc.add(Restrictions.le("createDate",endDate));
		}
		if(appId!=null&&!"".equals(appId))
		{
			dc.add(Restrictions.eq("configApp.id",appId));
		}
		dc.add(Restrictions.ne("distinguish","3"));
		return workLogDao.find(dc);
	}
	public List<Map<String, Object>> findtj(List<Office> offices,String name,String staDate,String endDate){
		String sqlOffice ="";
		if(name!=null && "".equals(name)){
			Query off = workLogDao.createSqlQuery("SELECT s.OFFICE_ID from WORK_INFO_LOG w,SYS_USER s where s.ID = w.CREATE_BY AND s.NAME Like '%"+name+"%'");
			List lists = off.list();
			if(lists.size()!=0){
				String[] quchong = new String[lists.size()];
				for(int i=0;i<lists.size();i++){
					quchong[i] = lists.get(i).toString();
				}
				String[] quchonghou = array_unique(quchong);
		        for(String bianli : quchonghou) {
		            sqlOffice += bianli + ",";
		        }
		        for(int i = 0;i<quchonghou.length;i++){
	                if(i==(quchonghou.length-1)){
	                    sqlOffice+=quchonghou[i];
	                }else{
	                    sqlOffice+=quchonghou[i]+",";
	                }
	            }
			}
		}else{
		    for(int i = 0;i<offices.size();i++){
		        if(i==(offices.size()-1)){
		            sqlOffice+=offices.get(i).getId();
		        }else{
		            sqlOffice+=offices.get(i).getId()+",";
		        }
		    }
		}
		String timeSQL = "";
		if(staDate!=null&&!staDate.equals("")){
			timeSQL+=" and l.create_date >= to_date('"+staDate+"','yyyy-mm-dd') ";
		}
		if(endDate!=null&&!endDate.equals("")){
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = simpleDateFormat.parse(endDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				endDate = simpleDateFormat.format(calendar.getTime());
				timeSQL += " and l.create_date <= to_date('" + endDate + "','yyyy-mm-dd') ";
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		Query query = workLogDao.createSqlQuery("select a.app_name as dealInfoId,SUM(CASE WHEN l.ser_type = '日常客服' then 1 else 0 end) as rc, SUM(CASE WHEN l.ser_type = '温馨提示' then 1 else 0 end) as wx, SUM(CASE WHEN l.ser_type = '更新提示' then 1 else 0 end) as gx,SUM(CASE WHEN l.ser_type = '回访' then 1 else 0 end) as hf,SUM(CASE WHEN l.ser_type = '培训' then 1 else 0 end) as px FROM work_info_log l, config_app a where l.config_app = a.id and l.office_id in ("+sqlOffice+") "+timeSQL+" group by a.app_name");
		List list = query.list();
		List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<result.size();i++){
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}
	public List<Map<String, Object>> findRctj(List<Office> offices,String name,String staDate,String endDate,Long appId){
		String sqlOffice ="";
		name = name==null?"":name;
		//if(name!=null || "".equals(name)){
			Query off = workLogDao.createSqlQuery("SELECT s.OFFICE_ID from WORK_INFO_LOG w,SYS_USER s where s.ID = w.CREATE_BY AND s.NAME Like '%"+name+"%'");
			List lists = off.list();
			if(lists.size()!=0){
				String[] quchong = new String[lists.size()];
				for(int i=0;i<lists.size();i++){
					quchong[i] = lists.get(i).toString();
				}
				String[] quchonghou = array_unique(quchong);
		        for(String bianli : quchonghou) {
		            sqlOffice += bianli + ",";
		        }
			}
//		}else{
//			for(Office office :offices){
//				sqlOffice+=office.getId()+",";
//			}
//		}
		String timeSQL = "";
		if(staDate!=null&&!staDate.equals("")){
			timeSQL+=" and l.create_date >= to_date('"+staDate+"','yyyy-mm-dd') ";
		}
		if(endDate!=null&&!endDate.equals("")){
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = simpleDateFormat.parse(endDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				endDate = simpleDateFormat.format(calendar.getTime());
				timeSQL += " and l.create_date <= to_date('" + endDate + "','yyyy-mm-dd') ";
			}catch (Exception e ){
				e.printStackTrace();
			}
		}
		if (appId!=null) {
			timeSQL+="and l.config_app="+appId;
		}

		//Query query = workLogDao.createSqlQuery("select l.CUSTOMER_SERVICE_ACCESS as CUSTOMER_SERVICE_ACCESS,SUM(CASE WHEN l.proble_type='业务咨询' then 1 else 0 end) as zx,SUM(CASE WHEN l.proble_type='环境' then 1 else 0 end) as hj,SUM(CASE WHEN l.proble_type='驱动' then 1 else 0 end) as qd,SUM(CASE WHEN l.proble_type='key' then 1 else 0 end) as ky,SUM(CASE WHEN l.proble_type='网络' then 1 else 0 end) as wl,SUM(CASE WHEN l.proble_type='更新操作' then 1 else 0 end) as gx ,SUM(CASE WHEN l.proble_type='解锁' then 1 else 0 end) as js,SUM(CASE WHEN l.proble_type='业务系统' then 1 else 0 end) as xt, SUM(CASE WHEN l.proble_type='业务操作' then 1 else 0 end) as cz,SUM(CASE WHEN l.proble_type='其他' then 1 else 0 end) as qt  FROM work_info_log l where l.CUSTOMER_SERVICE_ACCESS is not null and l.office_id in ("+sql+")"+timeSQL+" GROUP BY l.CUSTOMER_SERVICE_ACCESS");
        String sqlstmt = "SELECT l.CUSTOMER_SERVICE_ACCESS as CUSTOMER_SERVICE_ACCESS,SUM(CASE WHEN l.proble_type='业务咨询' then 1 else 0 end) as zx,SUM(CASE WHEN l.proble_type='环境' then 1 else 0 end) as hj,SUM(CASE WHEN l.proble_type='驱动' then 1 else 0 end) as qd,SUM(CASE WHEN l.proble_type='key' then 1 else 0 end) as ky,SUM(CASE WHEN l.proble_type='网络' then 1 else 0 end) as wl,SUM(CASE WHEN l.proble_type='更新操作' then 1 else 0 end) as gx ,SUM(CASE WHEN l.proble_type='解锁' then 1 else 0 end) as js,SUM(CASE WHEN l.proble_type='业务系统' then 1 else 0 end) as xt, SUM(CASE WHEN l.proble_type='业务操作' then 1 else 0 end) as cz,SUM(CASE WHEN l.proble_type='其他' then 1 else 0 end) as qt  FROM work_info_log l where l.CUSTOMER_SERVICE_ACCESS is not null";
        if(sqlOffice.length() > 0){
            String sql = sqlOffice.substring(0, sqlOffice.length()-1);
            sqlstmt = sqlstmt + " AND l.office_id in ("+sql+")"+timeSQL;
        }
        sqlstmt = sqlstmt + " GROUP BY l.CUSTOMER_SERVICE_ACCESS";
        Query query = workLogDao.createSqlQuery(sqlstmt);

        List list = query.list();
		List result = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0;i<result.size();i++){
			Map<String, Object> map = (Map<String, Object>) result.get(i);
			mapList.add(map);
		}
		return mapList;
	}
	
	public Page<WorkLog> findByCompany(Page<WorkLog> page, WorkLog workLog,List<WorkCompany> companies,String comName) {
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		dc.createAlias("workCompany", "workCompany");
		if(comName!=null&&!comName.equals("")){
			dc.add(Restrictions.like("workCompany.companyName", "%"+EscapeUtil.escapeLike(comName)+"%"));
		}
		if(companies.size()>0){
			dc.add(Restrictions.in("workCompany", companies));
		}
		dc.addOrder(Order.desc("id"));
		return workLogDao.find(page, dc);
	}
	
	/**
	 * 根绝业务数据取Log信息
	 * @param
	 */
	public List<WorkLog> findByDealInfo(WorkDealInfo workDealInfo){
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		if (workDealInfo != null) {
			dc.add(Restrictions.eq("workDealInfo", workDealInfo));
			dc.createAlias("workDealInfo", "workDealInfo");
		}
		dc.add(Restrictions.isNotNull("recordContent"));
		dc.add(Restrictions.ne("recordContent"," "));
		dc.add(Restrictions.eq(WorkLog.DEL_FLAG, WorkLog.DEL_FLAG_NORMAL));
		return workLogDao.find(dc);
	}
	@Transactional(readOnly = false)
	public void save(WorkLog workLog) {
		workLogDao.save(workLog);
	}
	
	@Transactional(readOnly = false)
	public void save(List<WorkLog> workLog) {
		workLogDao.save(workLog);
	}

	public Page<WorkLog> findKfList(Page<WorkLog> page, WorkLog workLog) {
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		if(workLog.getWorkCompany()!=null&&workLog.getWorkCompany().getCompanyName()!=null&&!workLog.getWorkCompany().getCompanyName().equals("")){
			dc.createAlias("workCompany","workCompany");
			dc.add(Restrictions.like("workCompany.companyName", "%"+EscapeUtil.escapeLike(workLog.getWorkCompany().getCompanyName())+"%"));
		}
		if(workLog.getConfigApp()!=null)
		{
			if(workLog.getConfigApp().getId()!=null)
			{
				dc.add(Restrictions.eq("configApp.id",workLog.getConfigApp().getId()));
			}
		}
		if(workLog.getAccess()!=null&&!workLog.getAccess().equals("")){
			dc.add(Restrictions.eq("access", workLog.getAccess()));
		}
		if(workLog.getDistinguish()!=null&&!workLog.getDistinguish().equals(""))
		{
			dc.add(Restrictions.eq("distinguish", workLog.getDistinguish()));
		}
		if(workLog.getCompleteType()!=null&&!"".equals(workLog.getCompleteType()))
		{
			dc.add(Restrictions.eq("completeType",workLog.getCompleteType()));
		}
		dc.add(Restrictions.eq("state",1));
		dc.addOrder(Order.desc("id"));
		return workLogDao.find(page,dc);
	}
	public Page<WorkLog> findKpList(Page<WorkLog> page, WorkLog workLog) {
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		if(workLog.getAccess()!=null&&!workLog.getAccess().equals("")){
			dc.add(Restrictions.eq("access", workLog.getAccess()));
		}
		if(workLog.getSerType()!=null&&!workLog.getSerType().equals("")){
			dc.add(Restrictions.eq("serType", workLog.getSerType()));
		}
		if(workLog.getDistinguish()!=null&&!workLog.getDistinguish().equals(""))
		{
			dc.add(Restrictions.eq("distinguish", workLog.getDistinguish()));
		}
		if(workLog.getConfigApp()!=null)
		{
			if(workLog.getConfigApp().getId()!=null)
			{
				dc.add(Restrictions.eq("configApp.id",workLog.getConfigApp().getId()));
			}
		}
		dc.add(Restrictions.eq("state",1));
		dc.addOrder(Order.desc("id"));
		return workLogDao.find(page,dc);
	}
	
	//list去重
	public static String[] array_unique(String[] a) {
        // array_unique
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < a.length; i++) {
            if(!list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        return (String[])list.toArray(new String[list.size()]);
    }
	
	/**
	 * 根据客服接入类型查询咨询类用户数量
	* @Title: getSumByAccess
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param access
	* @param @return    设定文件
	* @return Integer    返回类型
	* @throws
	 */
	public Integer getSumByAccess(String access, Date startTime, Date endTime, Long userId, String probleType){
		DetachedCriteria dc = workLogDao.createDetachedCriteria();
		dc.createAlias("createBy","createBy");
		dc.add(Restrictions.eq("access", access));
		dc.add(Restrictions.eq("probleType", probleType));
		if (startTime != null && endTime != null) {
			dc.add(Restrictions.ge("createDate", startTime));
			dc.add(Restrictions.le("createDate", endTime));
		}
		if (userId != null && userId != -1L) {
			dc.add(Restrictions.eq("createBy.id", userId));
		}
		
		dc.addOrder(Order.desc("id"));
		return workLogDao.find(dc).size();
	}
	
	public List<User> findAllUser(){
		Query off = workLogDao
				.createSqlQuery("SELECT s.ID,s.NAME from SYS_USER s").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return off.list();
	}
	public Page<WorkLog> findworkLogAchievements(Page<WorkLog> page,String name, Date startTime, Date endTime){
		DetachedCriteria dc=workLogDao.createDetachedCriteria();
		dc.createAlias("createBy", "createBy");
		if(name!=null&&!"".equals(name))
		{
			dc.add(Restrictions.eq("createBy.name", name));
		}
		if (startTime != null ) {
			dc.add(Restrictions.ge("createDate", startTime));
		}
		if(endTime != null)
		{
			dc.add(Restrictions.le("createDate", endTime));
		}
		dc.add(Restrictions.isNotNull("distinguish"));
		dc.addOrder(Order.desc("distinguish"));
		return workLogDao.find(page,dc);
	}
}
