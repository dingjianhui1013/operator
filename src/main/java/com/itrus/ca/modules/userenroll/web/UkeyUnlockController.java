package com.itrus.ca.modules.userenroll.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.itrus.ca.common.utils.StringUtils;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.profile.entity.ConfigRaAccountExtendInfo;
import com.itrus.ca.modules.profile.service.ConfigRaAccountExtendInfoService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.service.SystemService;
import com.itrus.ca.modules.userenroll.entity.KeyUnlock;
import com.itrus.ca.modules.userenroll.service.KeyUnlockService;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

@RequestMapping("/unlock")
@Controller
public class UkeyUnlockController extends BaseController{

	@Autowired
	KeyUsbKeyService keyService;
	
	@Autowired
	WorkDealInfoService dealInfoService;
	
	@Autowired
	KeyUnlockService unlockService;

	@Autowired
	private ConfigRaAccountExtendInfoService configRaAccountExtendInfoService;

	
	@Autowired
	private KeyUnlockService keyUnlockService;	
	
	LogUtil logUtil = new LogUtil();
	
	@Autowired
	SystemService systemService;
	@RequestMapping(value="unlockApply")
	public String unlockApply(Model uiModel){
		return "userEnroll/pages/unlockCert";
	}
	
    // 申请解锁调用
    @RequestMapping(value = "enroll", produces = "text/html")
    public String enroll(KeyUnlock keyunlock, Model uiModel,BindingResult br,HttpServletRequest request
    		,@RequestParam(required=false)String lastId
    		
    		) {
    	if (br.hasErrors()) {
			System.out.println(br.getGlobalError());
		}
    	if (lastId!=null&&lastId.equals("")) {
    		keyunlock = keyUnlockService.get(Long.parseLong(lastId));
		}
    	if(lastId!=null&&!lastId.equals("")){
    		KeyUnlock unlock = keyUnlockService.get(Long.parseLong(lastId));
    		unlock.setState(1);
    		unlock.setStatus("ISFINISH");
			keyUnlockService.save(unlock);
    		
    	}
    	System.out.println("keyunlock.getKeySn = " + keyunlock.getKeySn());
		System.out.println("keyunlock.getReqCode = " + keyunlock.getReqCode());
		System.out.println("keyunlock.getCertCn = " + keyunlock.getCertCn());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("keysn", keyunlock.getKeySn());
		map.put("keyLength", keyunlock.getKeySn().length());
		
		User user = new User();
		user = systemService.getUser(1L);
		keyunlock.setCreateBy(user);
		keyunlock.setUpdateBy(user);
		keyunlock.setDelFlag(KeyUnlock.DEL_FLAG_NORMAL);
		keyunlock.setCreateTime(new Timestamp(System.currentTimeMillis()));
		keyunlock.setStatus("ENROLL");
		unlockService.save(keyunlock);
		
    	uiModel.addAttribute("enroll_id", keyunlock.getId());	
		uiModel.addAttribute("status", "解锁申请已提交成功，等待管理员审批！");	
		logUtil.saveTerminalLog(request.getRemoteHost(), keyunlock.getKeySn()+"提交解锁请求", StringUtils.getRemoteAddr(request), keyunlock.getKeySn(), "申请解锁");
        return "iLetter/zhengshufuwu_jsfw2";
    }
    
    // 查询解锁状态
    @RequestMapping(value = "query")
    @ResponseBody
    public String query(String keySn, Model uiModel,String certSn) {
    	JSONObject json = new JSONObject();
    	if(keySn==null||keySn.equals("")){
    		json.put("status", 1);
    		return json.toJSONString();
    	}
    	System.out.println("keyunlock.getKeySn() = " + keySn);
    	System.out.println("keyunlock.getCertSn() = "+certSn);
    	List<WorkDealInfo> all = dealInfoService.findByKeySn(keySn,certSn);
    	if (all==null||all.size()==0) {
    		json.put("status", 1);
    		return json.toJSONString();
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ConfigRaAccountExtendInfo extendInfo = configRaAccountExtendInfoService.get(all.get(0).getConfigProduct().getRaAccountExtedId());		
			json.put("certCN", extendInfo.getCommonNameDisplayName().equals("0")?all.get(0).getWorkCompany().getCompanyName():(extendInfo.getCommonNameDisplayName().equals("1")?all.get(0).getWorkUser().getContactName():all.get(0).getWorkUserHis().getContactName()));
			
			json.put("start", sdf.format(all.get(0).getWorkCertInfo().getNotbefore()));
			json.put("end", sdf.format(all.get(0).getWorkCertInfo().getNotafter()));
		}
    	// 查询请求
    	KeyUnlock keyunlock1 = unlockService.findByKeySn(keySn,certSn);
    	if(keyunlock1==null){
    		// 记录日志
			json.put("status", "不能识别解锁任务");
			return json.toJSONString();
    	}
    	// 状态
    	String status = keyunlock1.getStatus();
    	// 更新状态信息
    	json.put("shix", "OK");
    	if (keyunlock1.getValidDate()!=null&&keyunlock1.getValidDate().before(new Date())) {
			json.put("shix", "shix");
		}
    	
    	
    	if( status.equals("APPROVE") ){
    		keyunlock1.setStatus("DOWNLOAD");
    		keyunlock1.setDownloadTime(new Timestamp(System.currentTimeMillis()));
    		unlockService.save(keyunlock1);	
    	}
    	
    	// 返回信息
    	json.put("state", keyunlock1.getState());
    	json.put("id", keyunlock1.getId());
		json.put("status", "OK");
		json.put("unlockstatus", status);
		json.put("repCode", keyunlock1.getRepCode());
		json.put("id", keyunlock1.getId());
		return json.toJSONString();
    }
    
    //解锁成功后请求
    @RequestMapping(value = "unlock", produces = "text/html")
    public String unlock(@RequestParam(required=false)String id, Model uiModel,HttpServletRequest request) {
    	KeyUnlock keyunlock1 = unlockService.get(Long.valueOf(id));
    	
    	
//    	KeyUnlock keyunlock1 = unlockService.findByKeySn(keyunlock.getKeySn());
    	
    	if(keyunlock1==null){
			uiModel.addAttribute("status", "不能识别解锁任务");	
	        return "unlock/query";
    	}
    	System.out.println("keyunlock.getId() = " + keyunlock1.getId());
    	System.out.println("keyunlock.getKeySn() = " + keyunlock1.getKeySn());
    	

//		// 验证Windows License是否超限，如果超限并且Key是新Key，则终止服务返回错误
//		if(cacheCustomer.getLicense().checkWinCountUsed()==false){
//			ActivityMsgExample example = new ActivityMsgExample();
//			ActivityMsgExample.Criteria criteria1 = example.or();
//			criteria1.andOsTypeEqualTo("windows");
//			criteria1.andKeySnEqualTo(keyunlock.getKeySn());
//			
//			Long tnum=sqlSession.selectOne("com.itrus.ukey.db.ActivityMsgMapper.countTerminalNumByExample", example);
//			if(tnum==0){
//				Date curTime = new Date();
//				if(cacheCustomer.getLicense().getWinLogTime().getTime()+10*60*1000<curTime.getTime()){
//					cacheCustomer.getLicense().setWinLogTime(curTime);
//					LogUtil.syslog(sqlSession, "License超限", "解锁处理失败，Windows终端License超限！");
//				}
//				uiModel.addAttribute("status", "Windows终端License超限！");	
//		        return "unlock/enroll";
//			}
//		}
		
    	// 状态
    	String status = keyunlock1.getStatus();
    	// 更新状态信息
    	if( status.equals("DOWNLOAD") ){
    		keyunlock1.setStatus("UNLOCK");
    		keyunlock1.setUnlockTime(new Timestamp(System.currentTimeMillis()));
    		unlockService.save(keyunlock1);
    	}
    	if( status.equals("APPROVE") ){
    		keyunlock1.setStatus("UNLOCK");
    		keyunlock1.setUnlockTime(new Timestamp(System.currentTimeMillis()));
    		unlockService.save(keyunlock1);
    	}
    	
    	status = keyunlock1.getStatus();
    	
    	// 返回信息
		uiModel.addAttribute("status", "解锁成功，请牢记您设置的新口令！");
		uiModel.addAttribute("unlockstatus", status);
		logUtil.saveTerminalLog(request.getRemoteHost(), keyunlock1.getKeySn()+"解锁成功", StringUtils.getRemoteAddr(request), keyunlock1.getKeySn(), "解锁成功");

        return "iLetter/zhengshufuwu_jsfw4";
    }
    
    // 列表所有信息
	//@RequestMapping(produces = "text/html")
	public String list(
			Model uiModel) {
//		ProjectKeyInfoExample projectkeyinfoex = new ProjectKeyInfoExample();
//		List<ProjectKeyInfo> projectkeyinfoall = sqlSession.selectList("com.itrus.ukey.db.ProjectKeyInfoMapper.selectByExample", projectkeyinfoex);
//
//		for(ProjectKeyInfo keyinfo:projectkeyinfoall){
//			System.out.println(keyinfo.getId()+":"+keyinfo.getSn1()+":"+keyinfo.getSn2());
//		}
//		
//
//		ProjectKeyInfoSort.sort(projectkeyinfoall);
//		
//		System.out.println("------------------");
//		for(ProjectKeyInfo keyinfo:projectkeyinfoall){
//			System.out.println(keyinfo.getId()+":"+keyinfo.getSn1()+":"+keyinfo.getSn2());
//		}
		
		return "unlock/info";
	}
	
	@RequestMapping(value = "jump")
	@ResponseBody
	public String jump(){
		JSONObject json = new JSONObject();
		return json.toJSONString();
	}
}
