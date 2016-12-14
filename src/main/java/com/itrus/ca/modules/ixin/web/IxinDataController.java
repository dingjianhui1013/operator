/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.ixin.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.Region;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.itrus.ca.common.utils.DateUtils;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.ixin.entity.IxinData;
import com.itrus.ca.modules.ixin.entity.Vo.IxinDataVo;
import com.itrus.ca.modules.ixin.service.IXINReportService;
import com.itrus.ca.modules.ixin.service.IxinDataService;
import com.itrus.ca.modules.profile.entity.ConfigApp;
import com.itrus.ca.modules.profile.entity.ConfigAppOfficeRelation;
import com.itrus.ca.modules.profile.entity.ConfigProjectType;
import com.itrus.ca.modules.profile.service.ConfigAppOfficeRelationService;
import com.itrus.ca.modules.profile.service.ConfigAppService;
import com.itrus.ca.modules.profile.service.ConfigProjectTypeService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.service.OfficeService;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.service.WorkDealInfoService;

/**
 * IXIN数据采集Controller
 * @author HuHao
 * @version 2016-07-14
 */
@Controller()
@RequestMapping(value = "${adminPath}/ixin/ixinData")
public class IxinDataController extends BaseController {

	@Autowired
	private IxinDataService ixinDataService;
	
    @Autowired
    private WorkDealInfoService workDealInfoService;
    
    @Autowired
    private ConfigProjectTypeService configProjectTypeService;
    
    @Autowired
    private ConfigAppService configAppService;
    
    @Autowired
    private OfficeService officeService;
    
    @Autowired
    private IXINReportService iXINReportService;
    
    @Autowired
    private ConfigAppOfficeRelationService configAppOfficeRelationService;
    
	@ModelAttribute
	public IxinData get(@RequestParam(required=false) Long id) {
		if (id != null){
			return ixinDataService.get(id);
		}else{
			return new IxinData();
		}
	}
	
	@RequiresPermissions("ixin:ixinData:view")
	@RequestMapping(value = {"list", ""})
	public String list(IxinData ixinData, String configProjectTypeIds,String configAppIds,Date startTime, Date endTime,HttpServletRequest request, Model model) {
	    SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
	    if(startTime==null&&endTime==null){
            try {
                startTime = formatter.parse(StringHelper.getFirstDay());
                endTime =new Date(); 
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	    //所有项目类型
	    List<ConfigProjectType> typesList = configProjectTypeService.findProjectTypeList();
        model.addAttribute("typesList",typesList);
	    List<Long> configTypelist = new ArrayList<Long>();
        if(StringUtils.isNotEmpty(configProjectTypeIds)){
            String ids[] = configProjectTypeIds.split(",");
            for (String ss : ids) {
                configTypelist.add(Long.parseLong(ss));
            }
        }
        List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
        //首先得到数据范围内的应用id
        List<Long> appIdsByOffice =  configAppOfficeRelationService.findAllAppIdsByOffices(officeList);
        List<ConfigApp> appList = Lists.newArrayList();
        if(StringUtils.isNotEmpty(configAppIds)){
            String ids[] = configAppIds.split(",");
            for (String id : ids) {
                ConfigApp configApp = configAppService.findByAppId( Long.parseLong(id));
                appList.add(configApp);
            }
        }else{
            //选择类型下的应用
            appList = configAppService.findByconfigProjectTypes(configTypelist,appIdsByOffice);
        }
        if(StringUtils.isNotEmpty(configProjectTypeIds)){
            model.addAttribute("appList", appList);
            model.addAttribute("appListSize", appList.size());
        }else{
        	
        	//得到权限下所有的app
            List<ConfigApp> appsList = configAppService.findByAppIds(appIdsByOffice);
            model.addAttribute("appList", appsList);
            model.addAttribute("appListSize", appsList.size());
        }
        
        //应用下所有的证书 的数量与存活数量
        List<Object> certNumberList = Lists.newArrayList();
        List<IxinDataVo> vos = Lists.newArrayList();
        for (ConfigApp configApp : appList) {
            certNumberList = Lists.newArrayList();
           List<IxinData> certList =  ixinDataService.findByApp(configApp,startTime,endTime);
           System.out.println(configApp.getAppName()+"应用的再用数量");
           if(certList.size()>0){
               for(IxinData ixinDatas:certList){
                   if(!certNumberList.contains(ixinDatas.getCertSn())){
                       certNumberList.add(ixinDatas.getCertSn());
                       System.out.println("应用名称："+configApp.getAppName()+"***"+"证书序列号:"+ixinDatas.getCertSn());
                   }
               }
           }
           int number = iXINReportService.findCountByDate(configApp.getId(),startTime,endTime);
           System.out.println(configApp.getAppName()+"应用的存活数量:"+number);
           IxinDataVo vo = new IxinDataVo();
           if(number>0){
               vo.setSurvivalNumber(number);
               Double rate = Double.valueOf(certNumberList.size())/Double.valueOf(number);
               NumberFormat nt = NumberFormat.getPercentInstance();
               //设置百分数精确度2即保留两位小数
               nt.setMinimumFractionDigits(2);
               //最后格式化并输出
               vo.setRate(nt.format(rate));
               vo.setAppName(configApp.getAppName());
               vo.setCertNumber(certNumberList.size());
               vos.add(vo);
           }
           
        }
        model.addAttribute("list", vos);
        model.addAttribute("configProjectTypeIds", configProjectTypeIds==""?null:configProjectTypeIds);
        model.addAttribute("configAppIds", configAppIds==""?null:configAppIds);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
		return "modules/ixin/ixinDataList";
	}

	@ResponseBody
    @RequestMapping(value = "findAppByType", method = RequestMethod.POST )
    public String findAppByType(String configProjectTypeIds,HttpServletRequest request){
        JSONObject json = new JSONObject();
        List<Long> configTypelist = new ArrayList<Long>();
        if(StringUtils.isNotEmpty(configProjectTypeIds)){
            String ids[] = configProjectTypeIds.split(",");
            for (String ss : ids) {
                configTypelist.add(Long.parseLong(ss));
            }
        }
        try {
        List<Object> list = new ArrayList<Object>();
        
        List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
        
        //首先得到数据范围内的应用id
        List<Long> appIdsByOffice =  configAppOfficeRelationService.findAllAppIdsByOffices(officeList);
        
        //选择类型下的应用
        List<ConfigApp> appList = configAppService.findByconfigProjectTypes(configTypelist,appIdsByOffice);
        if (appList.size()>0){
            for (ConfigApp configApp : appList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id",configApp.getId() );
                map.put("appName", configApp.getAppName());
                list.add(map);
            }
            json.put("status", "1");
            json.put("list", list);
            json.put("size", list.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
	}
	
	/**
	 * @param ixinData
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveData", method = RequestMethod.POST )
	public String saveData(@RequestBody String jsonString,HttpServletRequest request){
	    System.out.println("转码前="+jsonString);
	    jsonString =java.net.URLDecoder.decode(jsonString);
	    System.out.println("转码后="+jsonString);
	    jsonString = jsonString.substring(0, jsonString.indexOf("}")+1);
	    System.out.println("截取后="+jsonString);
	    JSONObject json = new JSONObject();
	    try {
	        JSONObject jsonObj = new JSONObject(jsonString);
	        Integer dataSize = Integer.parseInt(jsonObj.getString("dataSize"));
	        String ixinData = jsonObj.getString("ixinData");
	        String[] ixinDatas = ixinData.split("\\|\\|");
    	    if(dataSize==ixinDatas.length){
    	        List<IxinData> list = new ArrayList<IxinData>();
    	        for (int i = 0; i < ixinDatas.length; i++) {
    	            String datas[] = ixinDatas[i].split("@@");
    	            if(datas.length==8){
    	                IxinData ixindata = new IxinData();
                        ixindata.setAccessType(datas[0]);
                        ixindata.setKeySn(datas[1]);
                        ixindata.setCertSn(datas[2]);
                        ixindata.setCpuId(datas[3]);
                        ixindata.setiXinVersion(datas[4]);
                        ixindata.setOsVersion(datas[5]);
                        ixindata.setIEVersion(datas[6]);
                        ixindata.setCreateDate(new Date());
                        ixindata.setAccessTime(DateUtils.parseDate(datas[7]));
                       List<WorkDealInfo> workDealInfos =  workDealInfoService.findByCertSn(datas[2]);
                       if(workDealInfos.size()>0){
                           ixindata.setConfigApp(workDealInfos.get(0).getConfigApp());
                       }
                       String ip = request.getHeader("x-forwarded-for");
                       if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
                           ip = request.getHeader("Proxy-Client-IP");
                       }
                       if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
                           ip = request.getHeader("WL-Proxy-Client-IP");
                       }
                       if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
                           ip = request.getRemoteAddr();
                       }
                       ixindata.setIp(ip);
                        list.add(ixindata);
    	            }else{
    	                json.put("status", "-2");
                        json.put("msg", "传送的数据格式有误"); 
                        return json.toString();
                        
    	            }
    	        }
    	        ixinDataService.saveList(list);
                json.put("status", "1");
                json.put("msg", "成功保存");
                return json.toString();
    	     
    	    }else{
    	        json.put("status", "-1");
    	        json.put("msg", "接收到的数据条数不对");
    	        return json.toString();
    	    }
	    } catch (Exception e) {
            e.printStackTrace();
        }
	    return json.toString();
	}
	
  @RequestMapping(value = "exportDetail")
    public void exportDetail(@RequestParam(value = "configProjectTypeIds", required = false)String configProjectTypeIds,
            @RequestParam(value = "configAppIds", required = false)String configAppIds,
            @RequestParam(value = "startTime", required = false)Date startTime,
            @RequestParam(value = "endTime", required = false)Date endTime,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
      try {
      SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
      if(startTime==null&&endTime==null){
          startTime = formatter.parse(StringHelper.getFirstDay());
          endTime =new Date(); 
      }
      
      List<Long> configTypelist = new ArrayList<Long>();
      List<ConfigProjectType> typesList =Lists.newArrayList();
      if(StringUtils.isNotEmpty(configProjectTypeIds)){
          String ids[] = configProjectTypeIds.split(",");
          for (String ss : ids) {
              configTypelist.add(Long.parseLong(ss));
              ConfigProjectType configProjectType = configProjectTypeService.get(Long.parseLong(ss));
              typesList.add(configProjectType);
          }
      }
      
      List<Office> officeList = officeService.getOfficeByType(UserUtils.getUser(), 2);
      
      //首先得到数据范围内的应用id
      List<Long> appIdsByOffice =  configAppOfficeRelationService.findAllAppIdsByOffices(officeList);
      
      List<ConfigApp> appList = Lists.newArrayList();
      if(StringUtils.isNotEmpty(configAppIds)){
          String ids[] = configAppIds.split(",");
          for (String id : ids) {
              ConfigApp configApp = configAppService.findByAppId( Long.parseLong(id));
              appList.add(configApp);
          }
      }else{
          //选择类型下的应用
          appList = configAppService.findByconfigProjectTypes(configTypelist,appIdsByOffice);
      }
      
      //应用下所有的证书 的数量与存活数量
      List<Object> certNumberList = Lists.newArrayList();
      List<IxinDataVo> vos = Lists.newArrayList();
      for (ConfigApp configApp : appList) {
          certNumberList = Lists.newArrayList();
         List<IxinData> certList =  ixinDataService.findByApp(configApp,startTime,endTime);
         if(certList.size()>0){
             for(IxinData ixinDatas:certList){
                 if(!certNumberList.contains(ixinDatas.getCertSn())){
                     certNumberList.add(ixinDatas.getCertSn());
                 }
             }
         }
         int number = iXINReportService.findCountByDate(configApp.getId(),startTime,endTime);
         IxinDataVo vo = new IxinDataVo();
         if(number>0){
             vo.setSurvivalNumber(number);
             Double rate = Double.valueOf(certNumberList.size())/Double.valueOf(number);
             NumberFormat nt = NumberFormat.getPercentInstance();
             //设置百分数精确度2即保留两位小数
             nt.setMinimumFractionDigits(2);
             //最后格式化并输出
             vo.setRate(nt.format(rate));
             vo.setAppName(configApp.getAppName());
             vo.setCertNumber(certNumberList.size());
             vos.add(vo);
         }
      }
        
        HSSFWorkbook wb = new HSSFWorkbook();// 定义工作簿
        HSSFCellStyle style = wb.createCellStyle(); // 样式对象
        Cell cell = null;
        HSSFSheet sheet = wb.createSheet("I信采集数据");
        sheet.setDefaultColumnWidth(5);
//        sheet.addMergedRegion(new CellRangeAddress(0, (short)0, 0, (short)4));// 合并单元格
        sheet.addMergedRegion(new Region(0, (short)0, 0, (short)4));
        HSSFFont font = wb.createFont();

        // 第一行数据
        HSSFRow row0 = sheet.createRow(0);
        cell = row0.createCell(0);
        HSSFFont font0 = wb.createFont();
        HSSFCellStyle style0 = wb.createCellStyle();
        font0.setFontHeightInPoints((short) 18);
        style0.setFont(font0);
        style0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellStyle(style0);
        cell.setCellValue( "I信采集数据");
        
     // 第二行数据
//        sheet.addMergedRegion(new CellRangeAddress(1,(short)0, 1,(short)4));
        sheet.addMergedRegion(new Region(1, (short)0, 1, (short)4));
        HSSFRow row1 = sheet.createRow(1);
        cell = row1.createCell(0);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue("统计时间：" + DateUtils.formatDate(startTime, "yyyy-MM-dd") + "—"
                + DateUtils.formatDate(endTime, "yyyy-MM-dd"));
        // 第三行数据
        if(StringUtils.isNotEmpty(configProjectTypeIds)){
//            sheet.addMergedRegion(new CellRangeAddress(2, (short)0, 2, (short)4));
            sheet.addMergedRegion(new Region(2, (short)0, 2, (short)4));
            HSSFRow row3= sheet.createRow(2);
            cell = row3.createCell(0);
            font.setFontHeightInPoints((short) 10);
            style.setFont(font);
            cell.setCellStyle(style);
            String configTypeName = "";
            for (ConfigProjectType ConfigProjectType : typesList) {
                configTypeName+= ConfigProjectType.getProjectName()+"  ";
            }
            cell.setCellValue("项目类型:"+configTypeName);
        }
        
        if(StringUtils.isNotEmpty(configAppIds)){
            HSSFRow row4= sheet.createRow(3);
//            sheet.addMergedRegion(new CellRangeAddress(3, (short)0, 3, (short)4));
            sheet.addMergedRegion(new Region(3, (short)0, 3, (short)4));
            cell = row4.createCell(0);
            if(StringUtils.isEmpty(configProjectTypeIds)){
                 row4= sheet.createRow(2);
//                 sheet.addMergedRegion(new CellRangeAddress(2, (short)0, 2, (short)4));
                 sheet.addMergedRegion(new Region(2, (short)0, 2, (short)4));
                 cell = row4.createCell(0);
            }
            font.setFontHeightInPoints((short) 10);
            style.setFont(font);
            cell.setCellStyle(style);
            String appName = "";
            for (ConfigApp configApp  : appList) {
                appName+= configApp.getAppName()+"  ";
            }
            cell.setCellValue("应用名称:"+appName);
        }
        
        HSSFRow row2= sheet.createRow(4);
        if(StringUtils.isNotEmpty(configAppIds)&&StringUtils.isNotEmpty(configProjectTypeIds)){
            row2= sheet.createRow(4);
        }else if(StringUtils.isEmpty(configProjectTypeIds)&&StringUtils.isNotEmpty(configAppIds)){
            row2= sheet.createRow(3);
        }else if(StringUtils.isNotEmpty(configProjectTypeIds)&&StringUtils.isEmpty(configAppIds)){
            row2= sheet.createRow(3);
        }else{
            row2= sheet.createRow(2);
        }
        row2.createCell(0).setCellValue("应用名称");
        row2.createCell(1).setCellValue("在用证书数量");
        row2.createCell(2).setCellValue("存活证书数量");
        row2.createCell(3).setCellValue("存活率");
        
        for (int i = 0; i < vos.size(); i++) {
            HSSFRow rown = sheet.createRow(i + 3);
            if(StringUtils.isNotEmpty(configProjectTypeIds)&&StringUtils.isNotEmpty(configProjectTypeIds)){
                rown = sheet.createRow(i + 5);
            }else if(StringUtils.isEmpty(configProjectTypeIds)&&StringUtils.isNotEmpty(configProjectTypeIds)){
                rown = sheet.createRow(i + 4);
            }else if(StringUtils.isNotEmpty(configProjectTypeIds)&&StringUtils.isEmpty(configProjectTypeIds)){
                rown = sheet.createRow(i + 4);
            }
            if (vos.get(i).getAppName() == null) {
                rown.createCell(0).setCellValue("");
            } else {
                rown.createCell(0).setCellValue(vos.get(i).getAppName());
            }
            if(vos.get(i).getCertNumber()==0){
                rown.createCell(1).setCellValue(0);
            }else{
                rown.createCell(1).setCellValue(vos.get(i).getCertNumber());
            }
            
            if(vos.get(i).getSurvivalNumber()==0){
                rown.createCell(2).setCellValue(0);
            }else{
                rown.createCell(2).setCellValue(vos.get(i).getSurvivalNumber());
            }
            
            if(StringUtils.isEmpty(vos.get(i).getRate())){
                rown.createCell(2).setCellValue("0.00%");
            }else{
                rown.createCell(3).setCellValue(vos.get(i).getRate());
            }
        }
      
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String fileName= "I信采集数据.xls";
            response.reset();
            response.setContentType("application/x-download; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
            wb.write(baos);
            byte[] bytes = baos.toByteArray();
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            BufferedOutputStream bos = null;
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(bytes);
            bos.close();
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
                
        }
      } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
    }

}
