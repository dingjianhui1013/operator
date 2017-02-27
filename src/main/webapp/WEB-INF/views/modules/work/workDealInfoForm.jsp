<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />

<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>

<style type="text/css">



.zoominner {background: none repeat scroll 0 0 #FFFFFF; padding: 5px 10px 10px; text-align: left;}
/* .zoominner p {height:30px; _position:absolute; _right:2px; _top:5px;}
.zoominner p a { /* background: url("../images/imgzoom_tb.gif") no-repeat scroll 0 0 transparent;  float: left; height: 17px; line-height: 100px; margin-left: 10px;  overflow: hidden;  width: 17px;}
.zoominner p a.imgadjust {background-position: -40px 0;} */
.zoominner a.imgclose{ cursor:pointer;position:absolute;z-index:9999;right:0px; top:0px; color:#333; font-size:30px; display:block;}
.zoominner a.imgclose:hover{text-decoration:none;}
.y {float: right; margin-bottom:10px;}
.ctnlist .text img{ cursor:pointer;}
#imgzoom_cover{background-color:#000000; filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=70); opacity:0.7; position:absolute; z-index:800; top:0px; left: 0px; width:100%; display:none;}
#imgzoom{ display:none; z-index:801; position:absolute;}
#imgzoom_img{_width:300px; _height:200px; width:700px; height:600px; background:url(../images/imageloading.gif) center center no-repeat;}
#imgzoom_zoomlayer{ _width:300px; _height:200px; _position:relative; _padding-top:30px; min-width:600px; min-height:500px; padding:110px 110px 50px;}

.imgLayerBox{margin-bottom:20px;overflow:hidden;}
.uploadImgList{ float:left; border:1px solid #ddd;margin-right:10px; padding:15px 2px 10px; min-height:110px; position:relative}
.uploadImgName{ text-align:center; font-size:12px; font-weight:bold; margin-top:15px; height:22px; line-height:22px;border-top:1px solid #ddd;margin-bottom:0px;}
.smBtn{width:100px; height:20px;}
.btnGrop{margin-bottom:5px;}
.s-closeBtn{ position:absolute; right:-4px; top:0px; font-size:20px; cursor:pointer;}




.table-condensed th{width:140px;}
.table-condensed td{width:275px;}
input[readonly]{width:206px;}
.btmBorder{border-bottom:1px solid #ddd}
.accordion-heading,.table th,.accordion-heading,.table td{ vertical-align: middle;}
  .uploadPicBox{position:relative; width:10%; float:left}
  .closeBtn{position:absolute;top:7px; right:0px; width:40px; color:#333; font-size:20px;}
 .closeBtn:hover{text-decoration: none;color:#333; }
</style>


<script type="text/javascript">
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
var imgPath = "${imgPath}";
var province;
var city;
var district;
var appData;
var selected = false;
	$(document).ready(function() {
		
						getPrivince();
						if("${imgNames}"!=null && "${imgNames}"!=""){
							var imgNames = "${imgNames}";
							var str1 = new Array();                      
							str1 = imgNames.split(",");  
					
							var namestr = "";
							for(var i = 0;i < str1.length; i++){
								//取出图片的状态
								var imgstatus = str1[i].substring(str1[i].lastIndexOf('##')+2,str1[i].length);
								var str ='';
								if(imgstatus==-2){
									str = $("<div class='uploadImgList' style='border-style:solid;border-width:2px;border-color:green' ><img src='"+str1[i].substring(0,str1[i].lastIndexOf('##'))+"' style='width: 100px; height: 80px;' imgRotation='"+canRotation(str1[i])+"'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p><span class="s-closeBtn icon-remove-sign" data="'+str1[i]+'"></span></div>');
								}else{
									str = $("<div class='uploadImgList'><img src='"+str1[i].substring(0,str1[i].lastIndexOf('##'))+"' style='width: 100px; height: 80px;' imgRotation='"+canRotation(str1[i])+"'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p><span class="s-closeBtn icon-remove-sign" data="'+str1[i]+'"></span></div>');
								}
								$("#imgLayer").append(str);
								var imgBoxMod=$(".ctnlist .text img");
							    imgPop(imgBoxMod);
							    imgDel(str);
							    str1[i]=str1[i].substring(0,str1[i].lastIndexOf('##'));
							    namestr+=str1[i].substring(str1[i].lastIndexOf('/')+1,str1[i].length)+",";
							}
							if(namestr!=''){
								$("#imgNames").val(namestr.substring(0,namestr.length-1));
							}
						}
						
						$("#name").focus();
						$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
						
						

						if(navigator.userAgent.indexOf("IE")!=-1){
							
							if(navigator.userAgent.indexOf("IE 8")!=-1){
								
								$("#organizationNumber").bind("propertychange", function() {
									count('organizationNumber','zdcount')
								});
								$("#comCertficateNumber").bind("propertychange", function() {
									count('comCertficateNumber','zjcount')
								});
								$("#companyMobile").bind("propertychange", function() {
									count('companyMobile','dwcount')
								});
								$("#conCertNumber").bind("propertychange", function() {
									count('conCertNumber','zjhcount')
								});
								$("#contactPhone").bind("propertychange", function() {
									count('contactPhone','zjmcount')
								});
								$("#contactTel").bind("propertychange", function() {
									count('contactTel','ywidcount')
								});
								$("#pIDCard").bind("propertychange", function() {
									count('pIDCard','IDcount')
								});	
							}else{
								$("#organizationNumber").attr("onpropertychange","count('organizationNumber','zdcount')");
								$("#comCertficateNumber").attr("onpropertychange","count('comCertficateNumber','zjcount')");
								$("#companyMobile").attr("onpropertychange","count('companyMobile','dwcount')");
								$("#conCertNumber").attr("onpropertychange","count('conCertNumber','zjhcount')");
								$("#contactPhone").attr("onpropertychange","count('contactPhone','zjmcount')");
								$("#contactTel").attr("onpropertychange","count('contactTel','ywidcount')");
								$("#pIDCard").attr("onpropertychange","count('pIDCard','IDcount')");
									
							}	
						}else{
							$("#organizationNumber").attr("oninput","count('organizationNumber','zdcount')");
							$("#comCertficateNumber").attr("oninput","count('comCertficateNumber','zjcount')");
							$("#companyMobile").attr("oninput","count('companyMobile','dwcount')");
							$("#conCertNumber").attr("oninput","count('conCertNumber','zjhcount')");
							$("#contactPhone").attr("oninput","count('contactPhone','zjmcount')");
							$("#contactTel").attr("oninput","count('contactTel','ywidcount')");
							$("#pIDCard").attr("oninput","count('pIDCard','IDcount')");
						}
						
						
						var url = "${ctx}/work/workDealInfo/app?_="+new Date().getTime();
						$.getJSON(url,function(d) {
											appData = d;
											$("#app").bigAutocomplete(
															{data : d.lis,callback : function(data) {
																 
																    	        				$("#product").html("");

																								var url1 = "${ctx}/work/workDealInfo/product?appId=";
																								var productHtml="";
																								productHtml+="<option value='0'>请选择</option>";
																								$.getJSON(url1 + data.result+"&_="+new Date().getTime(),
																												function(da) {
																													$("#appId").val(da.appId);
																													if (da.product10!=null) {
																														productHtml+="<option value='"+da.product10+"'>企业证书[通用]</option>";
																													} 
																													if (da.product11!=null) {
																														productHtml+="<option value='"+da.product11+"'>企业证书[专用]</option>";
																													}
																													if (da.product20!=null) {
																														productHtml+="<option value='"+da.product20+"'>个人证书(企业)[通用]</option>";
																													} 
																													if (da.product21!=null) {
																														productHtml+="<option value='"+da.product21+"'>个人证书(企业)[专用]</option>";
																													}
																													if (da.product30!=null) {
																														productHtml+="<option value='"+da.product30+"'>机构证书[通用]</option>";
																													} 
																													if (da.product31!=null) {
																														productHtml+="<option value='"+da.product31+"'>机构证书[专用]</option>";
																													}
																													if (da.product40!=null) {
																														productHtml+="<option value='"+da.product40+"'>可信移动设备[通用]</option>";
																													} 
																													if (da.product41!=null) {
																														productHtml+="<option value='"+da.product41+"'>可信移动设备[专用]</option>";
																													}
																													if (da.product60!=null) {
																														productHtml+="<option value='"+da.product60+"'>个人证书(机构)[通用]</option>";
																													} 
																													if (da.product61!=null) {
																														productHtml+="<option value='"+da.product61+"'>个人证书(机构)[专用]</option>";
																													}
																													
																													$("#product").html(productHtml);
																												});
																    	                    	
																    	                    }
																    	
																                  
																	
													
																
															});
										});


						if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!=""){
							var boundLabelList = "${boundLabelList}";
							
							$("#agentId").attr("onchange","setTemplateList()");
							var agentHtml="";
							var obj= $.parseJSON(boundLabelList);
							$.each(obj, function(i, item){
								 if(item==1){
									if (item=="${workDealInfo.payType}") {
										agentHtml+="<option selected='selected' value='"+item+"'>标准</option>";
									}else{
										agentHtml+="<option value='"+item+"'>标准</option>";
									}
									
								}else if(item==2){
									if (item=="${workDealInfo.payType}") {
										agentHtml+="<option selected='selected' value='"+item+"'>政府统一采购</option>";
									}else{
										agentHtml+="<option value='"+item+"'>政府统一采购</option>";
									}
								}else if(item==3){
									if (item=="${workDealInfo.payType}") {
										agentHtml+="<option selected='selected' value='"+item+"'>合同采购</option>";
									}else{
										agentHtml+="<option value='"+item+"'>合同采购</option>";
									}
								} 
							}); 
							$("#agentId").html(agentHtml);
							
							
							
							var product = $("#product").val();
							var agentId = $("#agentId").val();
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setTemplateList?productId="+product+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									var styleList = data.array;
									var styleHtml="";
									$.each(styleList,function(i,item){
										if(item.agentId=="${workDealInfo.configChargeAgentId}"){
											styleHtml +="<option selected='selected'  value='"+item.id+"'>" + item.name + "</option>";
											$("#boundId").val(item.id);
											showYear();
										}else{
											styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
										}
										
									});
									$("#agentDetailId").html(styleHtml);
								});
							}
						}
						
						//经信委
						if(${isSupport==true}){
							$("#supportDateTh").show();
							$("#supportDateTd").show();
						}
						
						
						$("#product").change(function(){
							
							
							
							
							
							var product = $("#product").val();
							var agentHtml="";
							var styleHtml="";
							if (product!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList1?productId="+product+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									/* showAgent(product); */
									
									
									$.each(data, function(i, item){					 
										 if(item.styleId=="1"){	
												agentHtml+="<option value='"+item.styleId+"'>标准</option>";
										}else if(item.styleId=="2"){
												agentHtml+="<option value='"+item.styleId+"'>政府统一采购</option>";
										}else if(item.styleId=="3"){
												agentHtml+="<option value='"+item.styleId+"'>合同采购</option>";
										}
										 
										 if(item.agentId!=null){
											 $("#boundId").val(item.agentId);
											 styleHtml +="<option value='"+item.agentId+"'>" + item.agentName + "</option>"; 
										 }
											
										
									});	
									
									if(agentHtml==""){
										
										agentHtml+="<option value='0'>请选择</option>";
										$("#agentId").html(agentHtml);
										styleHtml+="<option value='0'>请选择</option>";
										$("#agentDetailId").html(styleHtml);
										top.$.jBox.tip("请先配置计费策略！");
										return;
									}
									
									$("#agentId").html(agentHtml);
									showYear();
									$("#agentDetailId").html("");
									$("#agentDetailId").html(styleHtml);
									
									
									
									}); 	
							}else{
								generateCertSort();
							}
							
							
							
						});
						
						
						
						
						$("#agentId").change(function(){
							var product = $("#product").val();
							var agentId = $("#agentId").val();
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setTemplateList?productId="+product+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									var styleList = data.array;
									var styleHtml="";
									$.each(styleList,function(i,item){
										if(i==0){
											$("#boundId").val(item.id);
											showYear();
										}
										styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
									});
									$("#agentDetailId").html(styleHtml);
								});
							}else{
								top.$.jBox.tip("请您选择计费策略类型！");
								
							}
							
						});
						
						
						//经信委
						$("input[name='year']").change(function(){
							$("#expirationDate").val("");						
						});
						
						$("#expirationDate").blur(function(){
							
							$("#year1").removeAttr("checked");
							$("#year2").removeAttr("checked");
							$("#year3").removeAttr("checked");
							$("#year4").removeAttr("checked");
							$("#year5").removeAttr("checked");
						});
						
						
						if(${expirationDate!=null}){
							$("#year1").removeAttr("checked");
							$("#year2").removeAttr("checked");
							$("#year3").removeAttr("checked");
							$("#year4").removeAttr("checked");
							$("#year5").removeAttr("checked");
						}
						
						
						
						
	});
	function nameFill(obj){
		var a = checkJbrxm(obj);
		if(a) {
			$("#pName").val($("#contactName").val());
		}

	}
	function emailFill(obj){
		var a = form_check(obj);
		if (a){
			$("#pEmail").val($("#contacEmail").val());
		}
	}
	function numberFill(){
		var temp  = document.all["conCertNumber"].value.replace(/[^\w\.\/]/ig,'');
		document.all["conCertNumber"].value = temp;
		document.all["pIDCard"].value=document.all["conCertNumber"].value;
	}
	function onSubmit(){
		
		var objCall = $("input[name='year']");
		var wayvalue = new Array();
		objCall.each(function(){
			if(typeof $(this).attr('checked') != 'undefined'){
				wayvalue.push($(this).val());
			}
		});
	
		
	//alert(wayvalue.length);	
	//alert($("input[name='year']").val());
		if($("#product").val()==0){
			top.$.jBox.tip("请选择要办理的产品！");
			return false;
		}
		
		if($("#agentId").val()==0){
			top.$.jBox.tip("请选择计费策略类型！");
			return false;
		}else{
			if($("#agentDetailId").val()==0){
				top.$.jBox.tip("请选择计费策略模板！");
				return false;
			}
		}
		if($("#companyName").val()!="" && !checkDwmc($("#companyName"))){
			top.$.jBox.tip("单位名称格式有误!");
			return false;
		}
		
		
		
		
		if($("#contactName").val()!="" && !checkJbrxm($("#contactName"))){
			top.$.jBox.tip("经办人姓名格式有误!");
			return false;
		}
		
		if($("#contacEmail").val()!="" && !form_check($("#contacEmail"))){
			top.$.jBox.tip("经办人电子邮箱格式有误!");
			return false;
		}
		
		
		if($("#pName").val()!="" &&!checkSqr($("#pName"))){
			top.$.jBox.tip("申请人姓名格式有误!");
			return false;
		}
		if($("#appId").val()=="") {
			$("#app").val("");
			top.$.jBox.tip("该应用不存在!"); 
			$("#app").focus(); //让手机文本框获得焦点 
			return false;
		} else if (wayvalue.length<1  && ($("#expirationDate").val() == null || $("#expirationDate").val() == "") ){
			top.$.jBox.tip("请选择申请年限或指定具体到期时间!"); 
			$("input[name='year']").focus(); //让手机文本框获得焦点 
			return false;
		} 
		
		 if($("#agentDetailId").val()==0){
			top.$.jBox.tip("请配置计费策略"); 
			$("#agentDetailId").focus(); 
			return false;
		}else if($("#agentDetailId").val()!=0  && $("#agentId").val()!=1){
			if($("#surplusNum").val()==0){
				top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
				return false;
			}else{
				var boundId = $("#agentDetailId").val();
				var url = "${ctx}/profile/configChargeAgent/checkAgentIsZero?agentDetailId="+boundId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if(data.status==0){
						top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
						return false;
					}else{
						$("#inputForm").submit();
						return true;
					}
				});
				return false;
			}
		}else {
			$("#lable0").removeAttr("disabled");
			$("#lable1").removeAttr("disabled");
			return true;
		}

	}
</script>
<script type="text/javascript">
	document.onkeydown = function(event) {
		var target, code, tag;
		if (!event) {
			event = window.event; //针对ie浏览器  
			target = event.srcElement;
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "TEXTAREA") {
					return true;
				} else {
					return false;
				}
			}
		} else {
			target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "INPUT") {
					return false;
				} else {
					return true;
				}
			}
		}
	};
	
	
	
	
	
</script>
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/area.js"></script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDrag.js"></script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDialog.js"></script>

<script type="text/javascript" src="${ctxStatic}/js/content_zoom.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--  <script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js"></script>-->
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">

	function os(obj) {
		
		if($("#product").val()==0){
			top.$.jBox.tip("请选择要办理的产品！");
			return false;
		}
		
		if($("#agentId").val()==0){
			top.$.jBox.tip("请选择计费策略类型！");
			return false;
		}else{
			if($("#agentDetailId").val()==0){
				top.$.jBox.tip("请选择计费策略模板！");
				return false;
			}
		}
		
		
		if($("#companyName").val()!="" && !checkDwmc($("#companyName"))){
			top.$.jBox.tip("单位名称格式有误!");
			return false;
		}
		
		if($("#contactName").val()!="" && !checkJbrxm($("#contactName"))){
			top.$.jBox.tip("经办人姓名格式有误!");
			return false;
		}
		
		if($("#contacEmail").val()!="" && !form_check($("#contacEmail"))){
			top.$.jBox.tip("经办人电子邮箱格式有误!");
			return false;
		}
		
		if($("#pName").val()!="" &&!checkSqr($("#pName"))){
			top.$.jBox.tip("申请人姓名格式有误!");
			return false;
		}
		if($("#appId").val()=="") {
			$("#app").val("");
			top.$.jBox.tip("该应用不存在!"); 
			$("#app").focus(); //让手机文本框获得焦点 
			return false;
		} else if ($("input[name='year']").val() == null || $("input[name='year']").val() == ""){
			top.$.jBox.tip("请选择申请年限!"); 
			$("input[name='year']").focus(); //让手机文本框获得焦点 
			return false;
		} 
		else if($("#agentDetailId").val()==0){
			top.$.jBox.tip("请配置计费策略"); 
			$("#agentDetailId").focus(); 
			return false;
		}else if($("#agentDetailId").val()!=0  && $("#agentId").val()!=1){
			if($("#surplusNum").val()==0){
				top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
				return false;
			}else{
				var boundId = $("#agentDetailId").val();
				var url = "${ctx}/profile/configChargeAgent/checkAgentIsZero?agentDetailId="+boundId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if(data.status==0){
						top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
						return false;
					}else{
						if (obj == "one") {
							$("#inputForm")
							.attr("action", "${ctx}/work/workDealInfo/temporarySave");
							$("#inputForm").submit();
						}
						if (obj == "many") {
							$.ajax({
								type:"POST", //表单提交类型
								url:"${ctx}/work/workDealInfo/manySave", //表单提交目标
								data:$("#inputForm").serialize(), //表单数据
								dataType:"json",
								success:function(msg){
										if(msg.status == 1){//msg 是后台调用action时，你穿过来的参数
											top.$.jBox.tip("录入成功，可再次录入"); 
										}else{
											top.$.jBox.tip("录入失败，请重新编辑"); 
										}
									}
								});
						}
					}
				});
			}
		}else if($("#agentDetailId").val()!=0  && $("#agentId").val()==1){
			if (obj == "one") {
				$("#inputForm")
				.attr("action", "${ctx}/work/workDealInfo/temporarySave");
				$("#inputForm").submit();
			}
			if (obj == "many") {
				$.ajax({
					type:"POST", //表单提交类型
					url:"${ctx}/work/workDealInfo/manySave", //表单提交目标
					data:$("#inputForm").serialize(), //表单数据
					dataType:"json",
					success:function(msg){
							if(msg.status == 1){//msg 是后台调用action时，你穿过来的参数
								top.$.jBox.tip("录入成功，可再次录入"); 
							}else{
								top.$.jBox.tip("录入失败，请重新编辑"); 
							}
						}
					});
			}
		}else {
			$("#lable0").removeAttr("disabled");
			$("#lable1").removeAttr("disabled");
			return true;
		}
		
	}

	
	function onSubmitOS(){
		var productLength = $("#productTdId").find("[name='product']").length;
		for (var a = 0; a <productLength; a++) {
			var radioIndex = $($("#productTdId").find("[name='product']")[a]);
			if(radioIndex.is(":checked")){
				selected = true;
			}
		}
		if(!selected){
			top.$.jBox.tip("请选择要办理的产品!");
			return false;
		}
		if($("#companyName").val()!="" && !checkDwmc($("#companyName"))){
			top.$.jBox.tip("单位名称格式有误!");
			return false;
		}
		if($("#contactName").val()!="" && !checkJbrxm($("#contactName"))){
			top.$.jBox.tip("经办人姓名格式有误!");
			return false;
		}
		
		if($("#contacEmail").val()!="" && !form_check($("#contacEmail"))){
			top.$.jBox.tip("经办人电子邮箱格式有误!");
			return false;
		}
		
		if($("#pName").val()!="" &&!checkSqr($("#pName"))){
			top.$.jBox.tip("申请人姓名格式有误!");
			return false;
		}
		if($("#appId").val()=="") {
			$("#app").val("");
			top.$.jBox.tip("该应用不存在!"); 
			$("#app").focus(); //让手机文本框获得焦点 
			return false;
		} else if ($("input[name='year']").val() == null || $("input[name='year']").val() == ""){
			top.$.jBox.tip("请选择申请年限!"); 
			$("input[name='year']").focus(); //让手机文本框获得焦点 
			return false;
		} 
		else if($("#agentDetailId").val()==0){
			top.$.jBox.tip("请配置计费策略"); 
			$("#agentDetailId").focus(); 
			return false;
		}else if($("#agentDetailId").val()!=0  && $("#agentId").val()!=1){
			if($("#surplusNum").val()==0){
				top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
				return false;
			}else{
				var boundId = $("#agentDetailId").val();
				var url = "${ctx}/profile/configChargeAgent/checkAgentIsZero?agentDetailId="+boundId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if(data.status==0){
						top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
						return false;
					}else{
						isOnSubmitOS = "yes";
						alert(isOnSubmitOS);
						return true;
					}
				});
			}
		}else {
			$("#lable0").removeAttr("disabled");
			$("#lable1").removeAttr("disabled");
			return true;
		}

	}
	
	function showCert(companyId,productId) {
		//var productId=$('input[name="product"]:checked ').val();
		if(productId!=null)
			{
				if(productId==1||productId==3||productId==4)
					{
						var urlajax = "${ctx}/work/workDealInfo/ajaxEnterpriseCount?companyIds=" + companyId+"&productId="+productId+"&_="+new Date().getTime();
						$.getJSON(urlajax,function(data){
							
							if(data.index>0)
								{
									var url = "${ctx}/work/workDealInfo/showCertEnterprise?companyIds=" + companyId+"&productId="+productId+"&workdealinfoIds="+data.workdealinfoss+"&_="+new Date().getTime();
									top.$.jBox.open("iframe:" + url, "已有证书明细", 800, 420, {
										buttons : {
											"确定" : "ok",
											"关闭" : true
										},
										submit : function(v, h, f) {
											if(v=='ok')
											{
												if(productId!=null)
													{
															var cid = localStorage.getItem("cid");
																//根据workdealinfoId获取单位详细信息
															if(cid!="")
																{
																	var url1="${ctx}/work/workDealInfo/findCompanyInformation?id="+cid+"&_="+new Date().getTime();
																	$.getJSON(url1,function(d){
																			$("#companyId").val(d.companyId);
																			$("#companyName").val(d.companyName);
																			if (d.workCompany.companyType == 1) {
																				$("#companyType1").attr("selected","selected");
																			}
																			if (d.companyType == 2) {
																				$("#companyType2").attr("selected","selected");
																			}
																			if (d.companyType == 3) {
																				$("#companyType3").attr("selected","selected");
																			}
																			if (d.companyType == 4) {
																				$("#companyType4").attr("selected","selected");
																			}
																			if (d.companyType == 5) {
																				$("#companyType5").attr("selected","selected");
																			}
																			$("#organizationNumber").val(d.organizationNumber);
																			$("#orgExpirationTime").val(d.orgExpirationTime);
																			if (d.selectLv==0) {
																				$("#selectLv0").attr("selected","selected");
																			}
																			if (d.selectLv==1) {
																				$("#selectLv1").attr("selected","selected");
																			}
																			if (d.comCertificateType == 0) {
																				$("#comCertificateType0").attr("selected","selected");
																			}
																			if (d.comCertificateType == 1) {
																				$("#comCertificateType1").attr("selected","selected");
																			}
																			if (d.comCertificateType == 2) {
																				$("#comCertificateType2").attr("selected","selected");
																			}
																			if (d.comCertificateType == 3) {
																				$("#comCertificateType3").attr("selected","selected");
																			}
																			if (d.comCertificateType == 4) {
																				$("#comCertificateType4").attr("selected","selected");
																			}
																			$("#comCertficateNumber").val(d.comCertficateNumber);
																			$("#comCertficateTime").val(d.comCertficateTime);
																			$("#legalName").val(d.legalName);
																			$("#address").val(d.address);
																			$("#companyMobile").val(d.companyMobile);
																			$("#remarks").val(d.remarks);
																			//$("#s_province").val(d.province);
																			$("#contactName").val(d.contactName);
																			if (d.conCertType == 0) {
																				$("#conCertType0").attr("selected","selected");
																			}
																			if (d.conCertType == 1) {
																				$("#conCertType1").attr("selected","selected");
																			}
																			if (d.conCertType == 2) {
																				$("#conCertType2").attr("selected","selected");
																			}
																			if ('男' == d.conCertSex) {
																				$("#sex0").attr("checked","checked");
																			}
																			if (d.conCertSex =='女') {
																				$("#sex1").attr("checked","checked");
																			}
																			$("#contacEmail").val(d.contacEmail);
																			$("#contactPhone").val(d.contactPhone);
																			$("#contactTel").val(d.contactTel);
																			$("#workuserId").val(d.workuserId);
																			$("#conCertNumber").val(d.conCertNumber);
																			$("#pName").val(d.contactName);
																			if(d.conCertType==0){
																				$("#pIDCard").val(d.conCertNumber);
																			}
																			$("#pEmail").val(d.contacEmail);
																			/* if(d.province != null){
																				$("#s_province").append('<option value="'+d.province+'" selected="selected">'+d.province+'</option>');
																				$("#s_city").append('<option value="'+d.city+'" selected="selected">'+d.city+'</option>');
																				$("#s_county").append('<option value="'+d.district+'" selected="selected">'+d.district+'</option>');
																			} */
																			
																			province = d.province;
																			city = d.city;
																			district = d.district;
																			
																			getPrivince();
																			
																			if(province!=null&&province.length>0){
																				getCity(d.provinceId);	
																			}
																			if(city!=null&&city.length>0){
																				getTown(d.cityId);	
																			}
																			
																			
																	});
																}else
																	{
																		true;
																	}
															
														}else
															{
																	top.$.jBox.tip("请选择产品"); 
															}
											
														}
													}
											});
								}
						});
					}else if(productId==2||productId==6)
					{
						var urlajax = "${ctx}/work/workDealInfo/ajaxPersonalCount?companyIds=" + companyId+"&productId="+productId+"&_="+new Date().getTime();
						$.getJSON(urlajax,function(data){
							if(data.index>0)
								{
									var url = "${ctx}/work/workDealInfo/showCertPersonal?companyIds="+companyId+"&productId="+productId+"&workdealinfoIds="+data.workdealinfoss+"&_="+new Date().getTime();
									top.$.jBox.open("iframe:" + url, "已有证书明细", 800, 420, {
										buttons : {
											"确定" : "ok",
											"关闭" : true
										},
										submit : function(v, h, f) {
											if(v=='ok')
											{
												if(productId!=null)
													{
															var cid = localStorage.getItem("cid");
																//根据workdealinfoId获取单位详细信息
															if(cid!="")
																{
																	var url1="${ctx}/work/workDealInfo/findCompanyInformation?id="+cid+"&_="+new Date().getTime;
																	$.getJSON(url1,function(d){
																			$("#companyId").val(d.companyId);
																			$("#companyName").val(d.companyName);
																			if (d.workCompany.companyType == 1) {
																				$("#companyType1").attr("selected","selected");
																			}
																			if (d.companyType == 2) {
																				$("#companyType2").attr("selected","selected");
																			}
																			if (d.companyType == 3) {
																				$("#companyType3").attr("selected","selected");
																			}
																			if (d.companyType == 4) {
																				$("#companyType4").attr("selected","selected");
																			}
																			if (d.companyType == 5) {
																				$("#companyType5").attr("selected","selected");
																			}
																			$("#organizationNumber").val(d.organizationNumber);
																			$("#orgExpirationTime").val(d.orgExpirationTime);
																			if (d.selectLv==0) {
																				$("#selectLv0").attr("selected","selected");
																			}
																			if (d.selectLv==1) {
																				$("#selectLv1").attr("selected","selected");
																			}
																			if (d.comCertificateType == 0) {
																				$("#comCertificateType0").attr("selected","selected");
																			}
																			if (d.comCertificateType == 1) {
																				$("#comCertificateType1").attr("selected","selected");
																			}
																			if (d.comCertificateType == 2) {
																				$("#comCertificateType2").attr("selected","selected");
																			}
																			if (d.comCertificateType == 3) {
																				$("#comCertificateType3").attr("selected","selected");
																			}
																			if (d.comCertificateType == 4) {
																				$("#comCertificateType4").attr("selected","selected");
																			}
																			$("#comCertficateNumber").val(d.comCertficateNumber);
																			$("#comCertficateTime").val(d.comCertficateTime);
																			$("#legalName").val(d.legalName);
																			$("#address").val(d.address);
																			$("#companyMobile").val(d.companyMobile);
																			$("#remarks").val(d.remarks);
																			//$("#s_province").val(d.province);
																			$("#contactName").val(d.contactName);
																			if (d.conCertType == 0) {
																				$("#conCertType0").attr("selected","selected");
																			}
																			if (d.conCertType == 1) {
																				$("#conCertType1").attr("selected","selected");
																			}
																			if (d.conCertType == 2) {
																				$("#conCertType2").attr("selected","selected");
																			}
																			if ('男' == d.conCertSex) {
																				$("#sex0").attr("checked","checked");
																			}
																			if (d.conCertSex =='女') {
																				$("#sex1").attr("checked","checked");
																			}
																			$("#contacEmail").val(d.contacEmail);
																			$("#contactPhone").val(d.contactPhone);
																			$("#contactTel").val(d.contactTel);
																			$("#workuserId").val(d.workuserId);
																			$("#conCertNumber").val(d.conCertNumber);
																			$("#pName").val(d.contactName);
																			if(d.conCertType==0){
																				$("#pIDCard").val(d.conCertNumber);
																			}
																			$("#pEmail").val(d.contacEmail);
																			/* if(d.province != null){
																				$("#s_province").append('<option value="'+d.province+'" selected="selected">'+d.province+'</option>');
																				$("#s_city").append('<option value="'+d.city+'" selected="selected">'+d.city+'</option>');
																				$("#s_county").append('<option value="'+d.district+'" selected="selected">'+d.district+'</option>');
																			} */
																			
																			
																			province = d.province;
																			city = d.city;
																			district = d.district;
																			
																			getPrivince();
																			
																			if(province.length>0){
																				getCity(d.provinceId);	
																			}
																			if(city.length>0){
																				getTown(d.cityId);	
																			}
																			
																	});
																}
															
														}else
															{
																	top.$.jBox.tip("请选择产品"); 
															}
											
														}
													}
											});
								}
						});
					}
			}
		
		}
	
	function del(data){
		
		window.location.href = "${ctx}/work/workDealInfo/delete?id=${workDealInfo.id}";
	}
	/* 
	* 功能：判断用户输入的邮箱格式是否正确 
	* 传参：无 
	* 返回值：true or false 
	*/ 
	function form_check(obj) {
		var email = $(obj).val(); //获取邮箱地址
		//判断邮箱格式是否正确
		if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email)) {
			if($("#emailpro").text()!=""){
//				$(obj).focus(); //让手机文本框获得焦点
				return false;
			}
			//$("#contacEmail").after("<span id='emailpro' style='color:red'>邮箱格式错误!</span>");
			$("#contacEmail").after("<span id='emailpro' style='color:red'>邮箱格式错误!</span>");
		/* 	top.$.jBox.tip("邮箱格式错误!");  */
//			$(obj).focus(); //让邮箱文本框获得焦点
			return false;
		}
		if($("#emailpro").text()!=""){
			$("#emailpro").hide();
		}
		return true;
	}

	function checkDwmc(obj) {
		/* var dwmc = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'、\-_() （） ]+$/.test(dwmc)) {
			if($("#dwmcpro").text()!=""){
				return false;
			}
			$("#companyName").after("<span id='dwmcpro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#dwmcpro").text()!=""){
			$("#dwmcpro").hide();
		} */
		
		
		
		
		return true;
	}
	
	
	function checkSqr(obj){
		var sqr = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'\-_() （） ]+$/.test(sqr)) {
			if($("#pNamepro").text()!=""){
				return false;
			}
			$("#pName").after("<span id='pNamepro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#pNamepro").text()!=""){
			$("#pNamepro").hide();
		}
		return true;
	}
	function checkJbrxm(obj) {
		var jbr = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'\-_() （） ]+$/.test(jbr)) {
			if($("#contactNamepro").text()!=""){
				return false;
			}
			$("#contactName").after("<span id='contactNamepro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#contactNamepro").text()!=""){
			$("#contactNamepro").hide();
		}
		return true;
	}
	/* 
	* 功能：判断用户输入的手机号格式是否正确 
	* 传参：无 
	* 返回值：true or false 
	*/ 
	function checkMobile(obj,o) { 
		$("#"+o).hide();
		var mobie = $(obj).val();
		var regu = /^[1][0-9][0-9]{9}$/; 
		var re = new RegExp(regu); 
		if (re.test(mobie)) { 
			if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
			return true; 
		} else { 
			if($("#phonepro").text()!=""){

				return false; 
			}
			$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
		
			return false; 
		} 
	}
	
	
	
	
	
	
	
	//获取计费模版相对应的年限
	function setYearByBoundId(){
		var boundId = $("#agentDetailId").val();
		$("#boundId").val(boundId);
		showYear();
	}
	
	
	/* 
	* 功能:根据产品带回年限
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showYear(){
		var agentId = $("#boundId").val();
		
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentId+"&infoType=0&_="+new Date().getTime();
		
		$.getJSON(url, function(data) {
			if (data.year1) {
				$("#year1").show();
				$("#word1").show();
			} else {
				$("#year1").hide();
				$("#word1").hide();
			}
			if (data.year2) {
				$("#year2").show();
				$("#word2").show();
			} else {
				$("#year2").hide();
				$("#word2").hide();
			}
			
			
			if (data.year3) {
				$("#year3").show();
				$("#word3").show();
			} else {
				$("#year3").hide();
				$("#word3").hide();
			}
			
			if (data.year4) {
				$("#year4").show();
				$("#word4").show();
			} else {
				$("#year4").hide();
				$("#word4").hide();
			}
			if (data.year5) {
				$("#year5").show();
				$("#word5").show();
			} else {
				$("#year5").hide();
				$("#word5").hide();
			}
			
			
			//经信委
			if(data.support){
				$("#supportDateTh").show();
				$("#supportDateTd").show();
				$("#expirationDate").val(data.expirationDate);
				
				
				
				$("#year1").removeAttr("checked");
				$("#year2").removeAttr("checked");
				$("#year3").removeAttr("checked");
				$("#year4").removeAttr("checked");
				$("#year5").removeAttr("checked");	
				
				
				
			}
			if(!data.support){
				$("#supportDateTh").hide();
				$("#supportDateTd").hide();
				$("#expirationDate").val(null);
				
			}
			
			generateCertSort();
			
			var arr = [data.nameDisplayName,data.orgunitDisplayName,data.emailDisplayName,data.commonNameDisplayName,data.commonNameDisplayName2,data.organizationDisplayName,data.addtionalField1DisplayName,data.addtionalField2DisplayName,data.addtionalField3DisplayName,data.addtionalField4DisplayName,data.addtionalField5DisplayName,data.addtionalField6DisplayName,data.addtionalField7DisplayName,data.addtionalField8DisplayName]
			var arrList = arr.unique();
			//清除所有必填项显示
			$(".prompt").css("display","none");
			for (var i = 0; i < arrList.length; i++) {
				if (arrList[i] != "product") {
					$("input[name='"+arrList[i]+"']").attr("required","required");
					$("input[name='"+arrList[i]+"']").parent().prev().find("span").show();
				} else {
					$("input[name='"+arrList[i]+"']").attr("required","required");
					$("input[name='"+arrList[i]+"']").parent().parent().prev().find("span").show();
				}
			}
			
			
			var boundId =  $("#agentDetailId").val(); 
			var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#surplusNum").val(data.surplusNum);
				if($("#surplusNum").val()==0&&$("#agentId").val()!=1){
					top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！");
					$("#agentMes").show();
				}else{
					$("#agentMes").hide();
				}
				
			});
			
			if(!data.support){
				if(data.year1){
					
					$("#year1").prop("checked",true);
					return;
				}else if(data.year2){
					$("#year2").prop("checked",true);
					return;
				}else if(data.year3){
					$("#year3").prop("checked",true);
					return;
				}else if(data.year4){
					$("#year4").prop("checked",true);
					return;
				}else{
					$("#year5").prop("checked",true);
					return;
				}
			}
			
			
		});	
	}
	Array.prototype.unique = function(){
		 this.sort(); //先排序
		 var res = [this[0]];
		 for(var i = 1; i < this.length; i++){
		  if(this[i] !== res[res.length - 1]){
		   res.push(this[i]);
		  }
		 }
		 return res;
		}
	
	function count(o,c){
		$("#"+c).show();
 		$("#"+c).html($("#"+o).val().length);
	}

	function qxCount(o)
	{
		$("#"+o).hide();
	}
	
	function hqcount(o,c)
	{
		$("#"+c).show();
 		$("#"+c).html($("#"+o).val().length);
	}
	function obtainCompanyInformation()
	{
		var companyName=$("#companyName").val();
		var productId = $("#product").val();
		if(companyName!=null&&companyName!="")
			{
				var url = "${ctx}/work/workDealInfo/completeCompanyName";
				$.ajax({
					type:"POST",
					url:url,
					data:{"companyname":companyName,"productId":productId,_:new Date().getTime()},
					dataType:'json',
					success:function(data){
						if(data.Idlis.length>0&&data.Idlis!=null)
							{
								showCert(data.Idlis,data.productId);
							}
					}
				});	
			}
	}
	
</script>

</head>
<body style="overflow: scroll">

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">业务新增</a></li>
	</ul>
		
	<form:form id="inputForm" action="${ctx}/work/workDealInfo/save"
		method="POST" class="form-horizontal">
		<tags:message content="${message}" />
		
		<div id="append_parent"></div>
		
		
		<div class="list ctnlist">

		<div class="text">
		

		<div id="imgLayer" align="left" class="imgLayerBox">
			<input id="imgNames" name="imgNames" type="hidden"/>
			
		</div>
		</div>
		</div>
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody id="workDealInfoBody">
						<tr>
							<th colspan="1" style="font-size: 20px;"><span
								class="prompt" style="color: red; display: none;">*</span>基本信息</th>	
							<th colspan="3"> 
							<shiro:hasPermission name="work:workDealInfo:saomiao">
								<a href="#" data-toggle="modal">
									<input id="scan" class="btn btn-primary smBtn" onclick="scanningInfoEnter()" data-toggle="modal" value="扫描录入" />
								</a>
							</shiro:hasPermission>
							</th>	
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>应用名称：</th>
							<td><input type="text" name="configApp"
								value="${workDealInfo.configApp.appName}" id="app" /></td>
							
							
							<th><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<td><input type="checkbox" checked="checked"
								disabled="disabled"><font color="red" style="font-weight:bold;">新增证书 </font><input type="hidden"
								name="dealInfoType" value="0"></td>
						</tr>
				
						<tr>


						
						    <th><span class="prompt" style="color: red; display: none;">*</span>选择产品：</th>
							<td id="productTdId">
									<select name="product"  id="product">
									<c:forEach items="${proList}" var="product">
										<option value="${product.id}" <c:if test="${product.id==workDealInfo.configProduct.id }">selected="selected"</c:if> >${product.name}</option>
									</c:forEach>
							</select>	

							</td>

							
							
							<th ><span class="prompt"
								style="color: red; display: none;">*</span>申请年数：</th>
							
							<td>
								<input type="radio" name="year" value="1" id="year1"
								 <c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span
								id="word1">1年</span> 
								
								<input type="radio" name="year" value="2"
								id="year2" <c:if test="${workDealInfo.year==2}">checked</c:if>><span
								id="word2">2年 </span>
								
								<input type="radio" name="year" value="3"
								id="year3" <c:if test="${workDealInfo.year==3}">checked</c:if>><span
								id="word3">3年 </span>
								
								<input type="radio" name="year" value="4"
								id="year4" <c:if test="${workDealInfo.year==4}">checked</c:if>><span
								id="word4">4年</span>
								
								<input type="radio" name="year" value="5"
								id="year5" <c:if test="${workDealInfo.year==5}">checked</c:if>><span
								id="word5">5年</span></td>
								
							
								

						</tr>
						
						
						<tr>
						
						<th ><span class="prompt"
								style="color: red; display: none;">*</span>计费策略类型：</th>
							<td ><select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select> <input type="hidden" id="boundId"></td>
						<th ><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模版：</th>
							<td ><select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select>&nbsp;<label id="agentMes" style="color: red; display: none;">不可用</label>
								<input type="hidden" id="surplusNum" />

							</td>
						

						<tr id="supportDateTr">
						<th id="supportDateTh" style="display: none" >选择截止日期：</th>
						<td id="supportDateTd" style="display: none">
								<input class="input-medium Wdate" type="text"
							 onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" 
							 maxlength="20" readonly="readonly" value="<fmt:formatDate value="${expirationDate}" pattern="yyyy-MM-dd"/>"
							name="expirationDate" id="expirationDate"/>
							</td> 
						
						</tr>
						
						
						
					</tbody>
				</table>
			</div>
		</div>
		
		
		
		
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody id="workCompanyBody">
						<tr>
							<th colspan="4" style="font-size: 20px;">单位信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" maxlength="50"
								value="${workCompany.companyName }" id="companyName"
								onchange="generateCertSort()" onblur="obtainCompanyInformation()"> <input type="hidden"
								name="companyId" value="${workCompany.id }" id="companyId"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位类型：</th>
							<td><select name="companyType">
									<option value="1" id="companyType1"
										<c:if test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="4" id="companyType4"
										<c:if test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>二级单位名称：</th>
							<td><input type="text" name="twoLevelCompanyName" maxlength="50"
								value="${workCompany.twoLevelCompanyName }" id="twoLevelCompanyName"> </td>
						
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码：</th>
							<td><input  type="text" name="organizationNumber"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								value="${workCompany.organizationNumber }" maxlength="18"
								id="organizationNumber"
								
								onchange="generateCertSort()"
								
								onblur="qxCount('zdcount')"
								onfocus="hqcount('organizationNumber','zdcount')" /><span
								id="zdcount" style="color: red; margin-left: 10px"></span></td>
						</tr>
						
						
						<tr>
							
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate"
								value="<fmt:formatDate value="${workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								id="orgExpirationTime" /></td>


							<th><span class="prompt" style="color: red; display: none;">*</span>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0"
										<c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
						</tr>
						<tr>
							
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照：</th>
							<td><select name="comCertificateType">
									<option value="0" id="comCertificateType0"
										<c:if test="${workCompany.comCertificateType==0 }">selected</c:if>>营业执照</option>
									<option value="1" id="comCertificateType1"
										<c:if test="${workCompany.comCertificateType==1 }">selected</c:if>>事业单位法人登记证</option>
									<option value="2" id="comCertificateType2"
										<c:if test="${workCompany.comCertificateType==2 }">selected</c:if>>社会团体登记证</option>
									<option value="3" id="comCertificateType3"
										<c:if test="${workCompany.comCertificateType==3 }">selected</c:if>>其他</option>
							</select></td>
							
							
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								value="${workCompany.comCertficateNumber }" maxlength="18"
								id="comCertficateNumber"
								
								onblur="qxCount('zjcount')"
								onfocus="hqcount('comCertficateNumber','zjcount')" /><span
								id="zjcount" style="color: red; margin-left: 10px"></span></td>
						</tr>
						<tr>
							
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								value="<fmt:formatDate value="${workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								id="comCertficateTime" /></td>
								
								
							<th><span class="prompt" style="color: red; display: none;">*</span>法人姓名：</th>
							<td style="vertical-align:middle"><input type="text" name="legalName" id="legalName"
								value="${workCompany.legalName }" maxlength="20"></td>	
						</tr>
						<tr>
							
							<th style="vertical-align:middle"><span class="prompt" style="color: red; display: none;">*</span>行政所属区：</th>
							<td><select id="s_province" onchange="getCity(this.options[this.options.selectedIndex].id)" name="s_province" style="width: 100px;*width:77px"></select>&nbsp;&nbsp; 
								<select id="s_city" onchange="getTown(this.options[this.options.selectedIndex].id)" name="s_city" style="width: 100px;*width:77px"></select>&nbsp;&nbsp; 
								<select id="s_county" name="s_county" style="width: 100px;*width:77px"></select>	
								
								
								
								<div id="show"></div>
							
								
								</td>
								
								
							 <th>区域备注：</th> 
							<td><input type="text" name="areaRemark"
										value="${workCompany.areaRemark }" maxlength="50" ></td>	
						</tr>
						
						
						<tr>
						   
							<th><span class="prompt" style="color: red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address" id="address"
								value="${workCompany.address }" maxlength="50"></td>
							
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>单位联系电话：</th>
								<td class="btmBorder"><input type="text" name="companyMobile"
								id="companyMobile" value="${workCompany.companyMobile }"
								 
								
								onblur="checkContactMobil(this,'dwcount')"
								onfocus="hqcount('companyMobile','dwcount')" /><span
								id="dwcount" style="color: red; margin-left: 10px"></span></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>备注信息：</th>
							<td><input type="text" name="remarks" id="remarks"
								value="${workCompany.remarks }"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
	
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody id="workUserBody">
						<tr>
							<th colspan="4" style="font-size: 20px;">证书持有人信息   </th> 
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName"
								maxlength="20" value="${workUser.contactName }" onchange="generateCertSort()"
								onblur="nameFill(this)" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人证件:</th>
							<td><select onchange="generateCertSort()" id="conCertType" name="conCertType">
									<option value="0" id="conCertType0"
										<c:if test="${workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber" maxlength="18"
								id="conCertNumber" value="${workUser.conCertNumber }"
								onkeyup="numberFill()" 
								onchange="generateCertSort()"
								onblur="qxCount('zjhcount')"
								onfocus="hqcount('conCertNumber','zjhcount')" /><span
								id="zjhcount" style="color: red; margin-left: 10px"></span></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								maxlength="30" value="${workUser.contactEmail }"
								onchange="emailFill(this)" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								maxlength="11" value="${workUser.contactPhone }"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" 
								onblur="checkMobile(this,'zjmcount')"
								
								onfocus="hqcount('contactPhone','zjmcount')" /><span
								id="zjmcount" style="color: red; margin-left: 10px"></span></td>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>业务系统UID:</th>
							<td class="btmBorder"><input type="text" name="contactTel" id="contactTel"
								maxlength="20" value="${workUser.contactTel }"
								
								onblur="qxCount('ywidcount')"
								onfocus="hqcount('contactTel','ywidcount')" /><span
								id="ywidcount" style="color: red; margin-left: 10px"></span></td>
						</tr>
						<tr id="workUserTr">
							<th  style="background:#f9f9f9"><span class="prompt" style="color: red; display: none;">*</span>证书持有人性别:</th>
							<td><input name="contactSex" id="sex0"
								<c:if test="${workUser.contactSex=='男' }">checked</c:if>
								type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="contactSex" id="sex1"
								<c:if test="${workUser.contactSex=='女' }">checked</c:if>
								type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人姓名:</th>
							<td><input type="text" name="pName" id="pName"
								maxlength="20" value="${workCertApplyInfo.name }"
								onchange="checkSqr(this);" /></td>
							<th class="btmBorder"><span class="prompt" style="color: red;">*</span>身份证号:</th>
							<td class="btmBorder"><input type="text" name="pIDCard" id="pIDCard"
								maxlength="18" value="${workCertApplyInfo.idCard }"
								onblur="qxCount('IDcount')"
								onfocus="hqcount('pIDCard','IDcount')" /><span id="IDcount"
								style="color: red; margin-left: 10px"></span></td>
						</tr>
						<tr>
							<th style="background:#f9f9f9"><span class="prompt" style="color: red; display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail"
								value="${workCertApplyInfo.email }" onchange="form_check(this)" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="5" style="font-size: 20px;">工作信息记录</th>
						</tr>
						<tr>
							<th>编号</th>
							<th>记录内容</th>
							<th>记录人</th>
							<th>受理网点</th>
							<th>记录时间</th>
						</tr>
						<c:forEach items="${workLog}" var="workLog" varStatus="status">
							<tr>
								<td>${status.count }</td>
								<td>${workLog.recordContent }</td>
								<td>${workLog.createBy.name }</td>
								<td>${workLog.createBy.office.name }</td>
								<td><fmt:formatDate value="${workLog.createDate }"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</c:forEach>
						<tr>
							<td>${workLog.size()+1 }</td>
							<td><input type="text" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" id="workDealInfoId" name="workDealInfoId"
			value="${workDealInfo.id}" />
		<input type="hidden" id="appId" name="appId"
			value="${workDealInfo.configApp.id}" />



		<input type="hidden" id="workuserId" name="workuserId" />

		<div class="row-fluid">

			<div class="span12">
				<table class="table">
					<tbody>
						<tr class="">
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2">&nbsp; <shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="submit"
										onclick="return onSubmit()" value="提 交" />&nbsp;</shiro:hasPermission> <input
								id="btnCancel" class="btn" type="button" value="返 回"
								onclick="history.go(-1)" /> <input id="btnSubmit"
								class="btn  btn-primary" type="button" value="保 存"
								onclick="os('one')" /> <input id="btnSubmit"
								class="btn  btn-primary" type="button" value="保存并再次添加"
								onclick="os('many')" /> <c:if test="${!empty workDealInfo.id}">
									<input id="btnSubmit" class="btn" type="button" value="删除"
										onclick="del(${workDealInfo.id})" />
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
	


</body>
 
 

</html>




