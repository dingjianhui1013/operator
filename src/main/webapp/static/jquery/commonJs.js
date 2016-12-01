//各个业务界面重复的js很多 之后如果再有重复的js写到这里 或者有时间把之前那些js拿出来整合到这里  引入改js之前 务必先引入jQuery zDrag zDialog

/**
 * VideoInput ActiveX API:
 * 
 * GetDeviceCount() :传回可以被打开的装置总数
 * 
 * GetDeviceName(nDeviceIndex) :传回装置名称
 * 
 * StartPlayDevice(nDeviceIndex) :开始播放指定的装置,其他正在播放的装置会被停止.
 * 
 * GetDeviceIndex() :传回目前正在播放的装置的编号.
 * 					 装置编号总是大于或等于0.如果没有任何装置正在播放,传回0.
 * 
 * 
 * */






/**
 * 设置全局变量
 * 
 * */
var szDefaultDevice = "[1a3c:010d]";                  //设置装置的默认摄像头为主摄像头

var nDeviceIndex = 1;                                 //装置的编号(0:旋转摄像头;1:主摄像头)

var localStoragePath = "D:\\operator\\temp\\";        //本地存放图片的路径

var suffixImg = ".jpg";                               //设置图片后缀,固定为jpg格式

var requestURI = "/work/workDealInfo/saveUploadImg";  //图片上传的请求路径

var fileUploadPath = "";                              //上传路径,从application.properties 中获取.
/**
 * @author 萧龙纳云
 * 
 * 点击扫描录入,触发开启装置事件
 * */
function scanningInfoEnter() {
	
	//$("#imageCollection").show();
	var diag = new Dialog();
	diag.Width = 900;
	diag.Height = 700;
	diag.Title = "扫描录入";
	var html ='<div style="width:100%;height:100%;top:0;background-color:white">';
		html+='<object id="VideoInputCtl" classid="CLSID:30516390-004F-40B9-9FC6-C9096B59262E" style="width: 100%; height: 80%;"></object>';
		html+='<div class="control-group">';
		html+='<div class="control-group" align="center" >';
		html+='	<div class="form-group btnGrop">';
		html+='	<button id="qrsq" class="btn btn-primary" onclick="changeDevice()">切换摄像头</button>&nbsp;&nbsp;&nbsp;';			
		html+='	<button id="qrsq" class="btn btn-primary" onclick="setPropertyDevice()">设置装置属性</button>&nbsp;&nbsp;&nbsp;';
		html+='	<button id="qrsq" class="btn btn-primary" onclick="getcompanyinfo()">单位信息录入</button>&nbsp;&nbsp;&nbsp;';
		html+='	<button id="qrsq" class="btn btn-primary" onclick="getholderinfo()">持有人信息录入</button>&nbsp;&nbsp;&nbsp;';
		html+='	<button id="qrsq" class="btn btn-primary" onclick="getoperatorinfo()">经办人信息录入</button>';
		html+='	</div>';
		html+='	<div class="form-group btnGrop">';
		html+='		<button id="qrsq" class="btn btn-primary" onclick="applicationphotograph('+imgPath+')">申请表拍照</button>&nbsp;&nbsp;&nbsp;';
		html+='		<button id="qrsq" class="btn btn-primary" onclick="workCompanyphotograph('+imgPath+')">单位证件拍照</button>&nbsp;&nbsp;&nbsp;';
		html+='		<button id="qrsq" class="btn btn-primary" onclick="workCertApplyInfophotograph('+imgPath+')">经办人身份证拍照</button>&nbsp;&nbsp;&nbsp;';
		html+='		<button id="qrsq" class="btn btn-primary" onclick="workUserphotograph('+imgPath+')">持有人身份证拍照</button>&nbsp;&nbsp;&nbsp;';
		html+='		<button id="qrsq" class="btn btn-primary" onclick="headphotograph('+imgPath+')">照片拍照</button>&nbsp;&nbsp;&nbsp;';
		html+='	</div>';
		/*html+='	<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> ';*/
		html+='</div>';
		html+='</div>';
	diag.InnerHtml=html;
	diag.show();
	diag.CancelEvent=release;
	var nDeviceCount = VideoInputCtl.GetDeviceCount();
    var szDeviceName = VideoInputCtl.GetDeviceName(1);
        
    if (nDeviceCount > 0 && szDefaultDevice.length > 0)
        opendevice();
    
}


/**
 * @author 萧龙纳云
 * 
 * 开启装置方法
 * */
function opendevice() {

    if (!VideoInputCtl.IsDeviceOpened(nDeviceIndex))
        VideoInputCtl.OpenDevice(nDeviceIndex);
    VideoInputCtl.StartPlayDevice(nDeviceIndex);
    VideoInputCtl.SetDeleteAfterHttpPost(true);
}


/**
 * @author 萧龙纳云
 * 
 * 更换播放装置
 * 
 * */
function changeDevice() {
    if(nDeviceIndex==0){
    	nDeviceIndex = 1;
    }else{
    	nDeviceIndex = 0;
    }

    opendevice();
   
}





/**
 * @author 萧龙纳云
 * 
 * 利用高拍仪和身份证阅读器读取身份证信息录入到证书持有人信息中,并且默认录入到经办人信息
 * 持有人姓名
 * 持有人身份证号
 * 经办人姓名
 * 经办人身份证
 * */
function getholderinfo(){
	
	VideoInputCtl.SetDeviceIdcard(nDeviceIndex, 1);         //开启 or 关闭 读取二代证功能
	
	VideoInputCtl.GrabToFile(localStoragePath+"Test.jpg");  //需先执行拍照功能，才可取得此次二代证资讯
    var names = VideoInputCtl.GetIdcardResult(0);
    var idno = VideoInputCtl.GetIdcardResult(5);
    $("#contactName").val(names);
    $("#conCertNumber").val(idno);
	
    if( $("#pIDCard").val()==""&&$("#pName").val()==""){
    	$("#pIDCard").val(idno);
    	$("#pName").val(names);
    }
   
}

/**
 * @author 萧龙纳云
 * 
 * 利用高拍仪和身份证阅读器读取身份证信息录入到经办人信息
 * 姓名
 * 身份证号
 * 
 * */
function getoperatorinfo(){
	VideoInputCtl.SetDeviceIdcard(nDeviceIndex, 1);        //开启 or 关闭 读取二代证功能
	
	VideoInputCtl.GrabToFile(localStoragePath+"Test.jpg"); //需先执行拍照功能，才可取得此次二代证资讯
    var names = VideoInputCtl.GetIdcardResult(0);
    $("#pName").val(names);

	var idno = VideoInputCtl.GetIdcardResult(5);
	$("#pIDCard").val(idno);
	
}



/**
 * @author 萧龙纳云
 * 
 * 利用高拍仪读取单位证件信息并录入到单位信息中
 * */
function getcompanyinfo(){
	 nDeviceIndex = VideoInputCtl.GetDeviceIndex();
	 
	 VideoInputCtl.SetDeviceQRcode(nDeviceIndex, 1);           //开启 or 关闭 QRcode功能
	 
	 VideoInputCtl.GrabToFile(localStoragePath+"Test.jpg");    //将照片存放到本地路径下

     if (VideoInputCtl.GetDeviceQRcode(nDeviceIndex)) {
         var nCount = VideoInputCtl.GetQRcodeCount();

             szType = VideoInputCtl.GetQRcodeTypeName(0);
             szText = VideoInputCtl.GetQRcodeContent(0);
      
             console.log(szText);
             
             var strs = splitQRcode(szText);
             if(strs!=null){
            	 $("#companyName").val(strs[2]);
            	 $("#organizationNumber").val(strs[0]);
             }
             
             else{
            	 $("#companyName").val("");
            	 $("#organizationNumber").val("");
             }
     }
     
}


/**
 * @author 萧龙纳云
 * 
 * 调整装置属性设置
 * */
function setPropertyDevice(){
	nDeviceIndex = VideoInputCtl.GetDeviceIndex();
    VideoInputCtl.ShowDevicePages(nDeviceIndex);
}

/**
 * @author 萧龙纳云
 * 
 * 单位证件拍照
 * */
function workCompanyphotograph(imgPath){
	
	if(fileUploadPath.length == 0){
		fileUploadPath = imgPath
	}
	
	var imgName = "workcompany-"+ new Date().getTime()+suffixImg;
	afterUpload(imgName);	
}

/**
 * @author 萧龙纳云
 * 
 * 申请表拍照
 * */
function applicationphotograph(imgPath,clientAddr){

	
	if(fileUploadPath.length == 0){
		fileUploadPath = imgPath
	}
	var imgName = "application-"+ new Date().getTime()+suffixImg;
	afterUpload(imgName);	
}

/**
 * @author 萧龙纳云
 * 
 * 经办人身份证拍照
 * */
function workCertApplyInfophotograph(imgPath){

	if(fileUploadPath.length == 0){
		fileUploadPath = imgPath
	}
	
	var imgName = "workcertapplyinfo-"+ new Date().getTime()+suffixImg;
	afterUpload(imgName);	
}

/**
 * @author 萧龙纳云
 * 
 * 头像拍照
 * */
function headphotograph(imgPath){

	if(fileUploadPath.length == 0){
		fileUploadPath = imgPath
	}
	
	var imgName = "head-"+ new Date().getTime()+suffixImg;
	afterUpload(imgName);	
}

/**
 * @author 萧龙纳云
 * 
 * 关闭头像
 * */
function release() {
	var nDeviceIndex = VideoInputCtl.GetDeviceIndex();

	if (VideoInputCtl.IsDeviceOpened(nDeviceIndex))
	{
		VideoInputCtl.CloseDevice(nDeviceIndex);
	}
}

/**
 * @author 萧龙纳云
 * 
 * 持有人身份证拍照
 * */
function workUserphotograph(imgPath){

	if(fileUploadPath.length == 0){
		fileUploadPath = imgPath
	}
	
	var imgName = "workuser-"+ new Date().getTime()+suffixImg;
	afterUpload(imgName);	
}

/**
 * @author 萧龙纳云
 * 
 * 拍照后上传
 * 
 * */
function Upload(imgName,szBase64)
{	
	var address = getAddress()+requestURI;
	console.log(address);
	var header = '';
	var result = "";

	if (imgName.replace(/^\s+|\s+$/g, '') != '')
	{
		header += '_FileName: ' + imgName + '\r\n';
	}

	 $.ajax({
	        type: "post",
	        dataType: "text",
	        url: requestURI,
	        data: {"_FileName":imgName,"szBase64":szBase64},
	        success: function (data) {
	            if (data != "") {
	            	str==1

	            }
	        }
	    });
		
   return str;
}



/**
 * @author 萧龙纳云
 * 
 * 获得地址:协议名称+IP地址+端口号+项目根目录
 * 
 * */
function getAddress(){
	//获得当前全部路径
	var currentPath = window.document.location.href;
	
    //获取当前从根目录开始之后的路径
	var pathName = window.document.location.pathname;
   
    //获取当前从根目录开始之前的路径
    var localhostPath = currentPath.substring(0, currentPath.indexOf(pathName));
   
    //获取当前的根目录
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    
    return localhostPath+projectName;
}

//内容页图片删除
function imgDel(imgDiv){
		imgDiv.find("span").each(function(i,one){
			$(one).live('click',function(){
				var name = $(one).attr("data");
				$(this).closest(".uploadImgList").remove();
				
				var imageNames = $("#imgNames").val();
				imageNames=imageNames.replace(","+name+",",",");
				imageNames=imageNames.replace(","+name,"");
				imageNames=imageNames.replace(name+",","");
				$("#imgNames").val(imageNames);
			});
		});
}


/**
 * @author 萧龙纳云
 * 
 * 上传后的操作
 * */
function afterUpload(imgName){
	var path = localStoragePath+imgName;
	
	//VideoInputCtl.GrabToFile(path);

	var szBase64 = VideoInputCtl.GrabToBase64(".jpg");

	var address = getAddress()+requestURI;
	if (imgName.replace(/^\s+|\s+$/g, '') != '')
	{
		$.ajax({
			type: "post",
			dataType: "text",
			url: address,
			data: {"_FileName":imgName,"szBase64":szBase64},
			success: function (data) {
				if (data == 'true') {
					var str = $("<div class='uploadImgList'><img src='data:image/tiff;base64,"+szBase64+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(imgName)+'</p><span class="s-closeBtn icon-remove-sign" data="'+imgName+'"></span></div>');
					
					$("#imgLayer").append(str);
					
					var imgBoxMod=$(".ctnlist .text img");
					
				    imgPop(imgBoxMod);
				    imgDel(str);
				    if($("#imgNames").val()==""){
						$("#imgNames").val(imgName);
					}else{
						$("#imgNames").val($("#imgNames").val()+","+imgName);
					}
					
				}
			}
		});
	}
}



/**
 * @author 萧龙纳云
 * 
 * 获取前台展示名称
 * */
function getDisplayName(imgName){
	if(imgName.indexOf("application")>-1){
		return "申请表";
	}else if(imgName.indexOf("workcompany")>-1){
		return "单位证件";
	}else if(imgName.indexOf("workcertapplyinfo")>-1){
		return "经办人身份证";
	}else if(imgName.indexOf("workuser")>-1){
		return "持有人身份证";
	}else if(imgName.indexOf("head")>-1){
		return "头像采集";
	}
	
}



/**
 * @author 萧龙纳云
 * 
 * 分割二维码解析的原生字符串
 * 
 * 例如:
 * 	统一社会信用代码：91510114730209375B
 *	企业注册号：510125000012682
 *	名称：四川华一钢结构有限公司
 *	登记机关：成都市新都区市场和质量监督管理局
 *	登记日期：2016-08-23
 *	企业信用信息公示系统网址：http://gsxt.scaic.gov.cn  http://gsxt.cdcredit.gov.cn
 *
 * 方法步骤:
 * 1:以换行符做第一次分割
 * 2:以冒号(中文)做第二次分割
 * 
 * @return strs
 * 
 * strs[0]:统一社会信用代码
 * strs[1]:企业注册号
 * strs[2]:名称
 * strs[3]:登记机关
 * strs[4]:登记日期
 * strs[5]:企业信用信息公示系统网址
 * 
 * */
function splitQRcode(stringObj){
	if(stringObj.length>0){
		var strFirst = new Array();                       //定义一个数组,来装载第一次分割后的字符 
		strFirst = stringObj.split("\n");                 //以换行符做第一次分割 
		
		var strs = [];                                    //定义一个数组,来装载最终的字符数组
		
		
		for(var i = 0;i < strFirst.length; i++) {
			var strFirstSplit = strFirst[i];
			var strSecond = new Array();                  //定义一个数组,来装载第二次分割后的字符
			strSecond = strFirstSplit.split("：");		  //以冒号(中文)做第二次分割
			
			strs.push(strSecond[1]);                      //将第二个字符放入strs,也就是冒号后的部分
		}	
		
		return strs;
	}else{
		return null;
	}
}






	
/**
 * @author 萧龙纳云
 * 
 * 检验是否为手机号或座机号码
 * 
 * 单位联系电话用
 */
function checkContactMobil(obj,o){
		$("#"+o).hide();
		var mobil = $(obj).val();
		
		
		var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	    var ip = new RegExp(isPhone);
	    var im = new RegExp(isMob);
	    
	    if(ip.test(mobil)||im.test(mobil)){
	    	if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
	    	
	        return true;
	    }
	    else{
	    	if($("#phonepro").text()!=""){

				return false; 
			}
	    	$("#companyMobile").after("<span id='phonepro' style='color:red'>请输入正确的号码</span>");
	        return false;
	    }
		
}


/**
 * 点击图片放大js
 * 
 * */

//内容页图片点击放大效果函数主体开始
function imgPop(imgBoxMod){
    imgBoxMod.each(function(){
    //超过最大尺寸时自动缩放内容页图片尺寸
    var ctnImgWidth=$(this).width();
    if(ctnImgWidth>618){
            $(this).width(618);
        }
    //点击图片弹出层放大图片效果
    $(this).click( function(){
    	
    	var str = "<div id='imgzoom'>"
    				+"<div id='imgzoom_zoomlayer' class='zoominner'>"
//    					+"<p><span class='y'>"
    						+"<a title='关闭' class='imgclose'><span class='icon-remove-sign'></span></a>"
//    					+"</span></p>"
    					+"<div id='imgzoom_img' class='hm'>"
    					+"<img src='' id='imgzoom_zoom' style='cursor:pointer'>"
    					+"</div>"
    				+"</div>"
    			   +"</div>"
    			   +"<div id='imgzoom_cover'></div>";
    	
        $("#append_parent").html(str); //生成HTML代码
        var domHeight =$(document).height(); //文档区域高度
        $("#imgzoom_cover").css({"display":"block","height":domHeight});
        var imgLink=$(this).attr("src");
        $("#imgzoom_img #imgzoom_zoom").attr("src",imgLink);
        $("#imgzoom").css("display","block");
        imgboxPlace();
        })
})
        //关闭按钮
$("#append_parent .imgclose").live('click',function(){
    $("#imgzoom").css("display","none");
    $("#imgzoom_cover").css("display","none");
})

//弹出窗口位置
function imgboxPlace(){
    var cwinwidth=$("#imgzoom").width();
    var cwinheight=$("#imgzoom").height();
    var browserwidth =$(window).width();//窗口可视区域宽度
    var browserheight =$(window).height(); //窗口可视区域高度
    
   
    
    
    var scrollLeft=$(window).scrollLeft(); //滚动条的当前左边界值
    var scrollTop=$(window).scrollTop(); //滚动条的当前上边界值 
    
    
   
    
    var imgload_left=scrollLeft+(browserwidth-cwinwidth)/2;
    var imgload_top=scrollTop+(browserheight-cwinheight)/2;
    $("#imgzoom").css({"left":imgload_left,"top":imgload_top});
    }
}



	