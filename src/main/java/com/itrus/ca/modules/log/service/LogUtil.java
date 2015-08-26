package com.itrus.ca.modules.log.service;

import java.util.Date;

import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.log.dao.SysOperateLogDao;
import com.itrus.ca.modules.log.dao.TerminalLogDao;
import com.itrus.ca.modules.log.entity.SysOperateLog;
import com.itrus.ca.modules.log.entity.TerminalLog;
import com.itrus.ca.modules.sys.entity.User;
/**
 * 记录日志
 * @author ZhangJingtao
 *
 */
public class LogUtil extends BaseService{
	
	TerminalLogDao terminalLogDao = SpringContextHolder.getBean(TerminalLogDao.class);
	
	SysOperateLogDao sysOperateLogDao = SpringContextHolder.getBean(SysOperateLogDao.class);
	/**
	 * 记录前台操作日志
	 */
	public void saveTerminalLog(String host,String info,String ip,String keySn,String type){
		try {
			TerminalLog log = new TerminalLog();
			log.setHostId(host);
			log.setInfo(info);
			log.setIp(ip);
			log.setKeySn(keySn);
			log.setType(type);
			log.setCreateDate(new Date());
			terminalLogDao.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 记录后台操作日志
	 */
	public void saveSysLog(String type,String detail,String exception){
		try {
			SysOperateLog sysOperateLog = new SysOperateLog();
			sysOperateLog.setType(type);
			sysOperateLog.setRemarks(detail);
			sysOperateLog.setException(exception);
			sysOperateLogDao.save(sysOperateLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 记录后台操作日志
	 */
	public void saveIxinSysLog(String type,String detail,String exception,User user){
		try {
			SysOperateLog sysOperateLog = new SysOperateLog();
			sysOperateLog.setType(type);
			sysOperateLog.setRemarks(detail);
			sysOperateLog.setException(exception);
			sysOperateLog.setCreateBy(user);
			sysOperateLog.setUpdateBy(user);
			sysOperateLogDao.save(sysOperateLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
