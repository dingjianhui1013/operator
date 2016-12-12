<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic }/js/content_zoom.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<style type="text/css">
.accordion-heading, .table th{width:140px;}
.table-condensed td{width:485px;}
.Wdate{width:206px;}
.btmBorder{border-bottom:1px solid #ddd}
.accordion-heading,.table th,.accordion-heading,.table td{ vertical-align: middle;}


.zoominner {background: none repeat scroll 0 0 #FFFFFF; padding: 5px 10px 10px; text-align: left;}
/* .zoominner p {height:30px; _position:absolute; _right:2px; _top:5px;}
.zoominner p a { /* background: url("../images/imgzoom_tb.gif") no-repeat scroll 0 0 transparent;  float: left; height: 17px; line-height: 100px; margin-left: 10px;  overflow: hidden;  width: 17px;}
.zoominner p a.imgadjust {background-position: -40px 0;} */
.zoominner a.imgclose{ cursor:pointer;position:absolute;z-index:9999;right:-6px; top:-6px; color:#333; font-size:30px; display:block;}
.zoominner a.imgclose:hover{text-decoration:none;}
.y {float: right; margin-bottom:10px;}
.ctnlist .text img{ cursor:pointer;}
#imgzoom_cover{background-color:#000000; filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=70); opacity:0.7; position:absolute; z-index:800; top:0px; left: 0px; width:100%; display:none;}
#imgzoom{ display:none; z-index:801; position:absolute;}
#imgzoom_img{_width:300px; _height:200px; width:700px; height:600px; background:url(../images/imageloading.gif) center center no-repeat;}
#imgzoom_zoomlayer{ _width:300px; _height:200px; _position:relative; _padding-top:30px; min-width:300px; min-height:200px; padding:17px;}

.imgLayerBox{margin-bottom:20px;overflow:hidden;}
.uploadImgList{ float:left; border:1px solid #ddd;margin-right:10px; padding:2px; position:relative}
.uploadImgName{ text-align:center; font-size:12px; font-weight:bold; margin-top:4px; height:22px; line-height:22px;border-top:1px solid #ddd;margin-bottom:0px;}
.smBtn{width:100px; height:20px;}
.btnGrop{margin-bottom:5px;}
.s-closeBtn{ position:absolute; right:-4px; top:0px; font-size:20px; cursor:pointer;}

</style>

<script type="text/javascript">
    var ctx = "${ctx}";
    var ctxStatic = "${ctxStatic}";
    var imgPath = "${imgPath}";
    var province = "${workDealInfo.workCompany.province}";
    var city = "${workDealInfo.workCompany.city}";
    var district = "${workDealInfo.workCompany.district}";
</script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDrag.js"></script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDialog.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>
<script type="text/javascript">
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
					str = $("<div class='uploadImgList' style='border-style:solid;border-width:2px;border-color:green' ><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p></div>');
				}else{
					str = $("<div class='uploadImgList'><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p></div>');
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
						$('div.small_pic a').fancyZoom({scaleImg: true, closeOnClick: true});
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
								$("#organizationNumber1").bind("propertychange", function() {
									count('organizationNumber1','zzcount')
								});
								$("#comCertficateNumber1").bind("propertychange", function() {
									count('comCertficateNumber1','zjcount')
								});
								$("#companyMobile").bind("propertychange", function() {
									count('companyMobile','dwtcount')
								});
								$("#conCertNumber1").bind("propertychange", function() {
									count('conCertNumber1','zjmcount')
								});
								$("#contactPhone1").bind("propertychange", function() {
									count('contactPhone1','zjtcount')
								});
								$("#contactTel1").bind("propertychange", function() {
									count('contactTel1','ywidcount')
								});
								$("#pIDCard").bind("propertychange", function() {
									count('pIDCard','pIDcount')
								});
							}else{
								$("#organizationNumber1").attr("onpropertychange","count('organizationNumber1','zzcount')");
								$("#comCertficateNumber1").attr("onpropertychange","count('comCertficateNumber1','zjcount')");
								$("#companyMobile").attr("onpropertychange","count('companyMobile','dwtcount')");
								$("#conCertNumber1").attr("onpropertychange","count('conCertNumber1','zjmcount')");
								$("#contactPhone1").attr("onpropertychange","count('contactPhone1','zjtcount')");
								$("#contactTel1").attr("onpropertychange","count('contactTel1','ywidcount')");
								$("#pIDCard").attr("onpropertychange","count('pIDCard','pIDcount')");
									
							}
							
							
							
						}else{
							$("#organizationNumber1").attr("oninput","count('organizationNumber1','zzcount')");
							$("#comCertficateNumber1").attr("oninput","count('comCertficateNumber1','zjcount')");
							$("#companyMobile").attr("oninput","count('companyMobile','dwtcount')");
							$("#conCertNumber1").attr("oninput","count('conCertNumber1','zjmcount')");
							$("#contactPhone1").attr("oninput","count('contactPhone1','zjtcount')");
							$("#contactTel1").attr("oninput","count('contactTel1','ywidcount')");
							$("#pIDCard").attr("oninput","count('pIDCard','pIDcount')");
						}
						
						

						var url = "${ctx}/work/workDealInfo/showYear?lable=${workDealInfo.configProduct.productLabel}&productName=${workDealInfo.configProduct.productName}&app=${workDealInfo.configApp.id}&infoType=${empty update?'':1}&_="
								+ new Date().getTime();
						$.getJSON(url, function(data) {
							var arr = [ data.nameDisplayName,
									data.orgunitDisplayName,
									data.emailDisplayName,
									data.commonNameDisplayName,
									data.addtionalField1DisplayName,
									data.addtionalField2DisplayName,
									data.addtionalField3DisplayName,
									data.addtionalField4DisplayName,
									data.addtionalField5DisplayName,
									data.addtionalField6DisplayName,
									data.addtionalField7DisplayName,
									data.addtionalField8DisplayName ]
							var arrList = arr.unique();
							//清除所有必填项显示
							$(".prompt").css("display", "none");
							for (var i = 0; i < arrList.length; i++) {
								if (arrList[i] != "product") {
									$("input[name='" + arrList[i] + "']").attr(
											"required", "required");
									$("input[name='" + arrList[i] + "']")
											.parent().prev().find("span")
											.show();
								} else {
									$("input[name='" + arrList[i] + "']").attr(
											"required", "required");
									$("input[name='" + arrList[i] + "']")
											.parent().parent().prev().find(
													"span").show();
								}
							}
						});
						
						
						if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!=""){
							var boundLabelList = "${boundLabelList}";
							
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
										}else{
											styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
										}
									});
									$("#agentDetailId").html(styleHtml);
								});
							}
						}
						
						
						
						$("#product").change(function(){
							
							var product = $("#product").val();
							var agentHtml="";
							var styleHtml="";
							if (product!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList1?productId="+product+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									
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
									$("#agentDetailId").html("");
									$("#agentDetailId").html(styleHtml);
									
									
									
									}); 	
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
										}
										styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
									});
									$("#agentDetailId").html(styleHtml);
								});
							}else{
								top.$.jBox.tip("请您选择计费策略类型！");
								
							}
							
						});	
						
						
						

					});
	Array.prototype.unique = function() {
		this.sort(); //先排序
		var res = [ this[0] ];
		for (var i = 1; i < this.length; i++) {
			if (this[i] !== res[res.length - 1]) {
				res.push(this[i]);
			}
		}
		return res;
	}
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

	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleList(obj){
		var lable = obj;
		var product = $("#product").val();
		var agentId = $("#agentId").val();
		var appId = $("#appId").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+product+"&app="+appId+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				var styleList = data.array;
				var styleHtml="";
				$.each(styleList,function(i,item){
					if(i==0){
						$("#boundId").val(item.id);
					}
					styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
				});
				$("#agentDetailId").html(styleHtml);
			});
		}else{
			top.$.jBox.tip("请您选择产品！");
			
		}
	}
	
	
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/area.js"></script>
<script type="text/javascript">
	function onSubmit() {
		$("#newInfoId").val(getCookie("work_deal_info_id"));
		delCookie("work_deal_info_id");
		$("#inputForm").submit();
	}

	
	function setJBRName(){
		var name = $("#contactName1").val();
		$("#pName").val(name);
	}
	
	function setJBRCard(o){
		var card = $("#conCertNumber1").val();
		$("#pIDCard").val(card);
		$("#"+o).hide();
	}
	
	function setJBRMail(){
		var mail = $("#contacEmail").val();
		$("#pEmail").val(mail);
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/list">业务办理列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfo/typeForm?id=${workDealInfo.id}&reissueType=${reissue}&dealType=${dealType}">业务变更<c:if test="${not empty reissue}">补办</c:if></a></li>
	</ul>
	<form:form id="inputForm"
		action="${ctx}/work/workDealInfoOperationSed/maintainSaveReturnChange" 
		method="post" enctype="multipart/form-data"
		class="form-horizontal">
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
					<tbody>
						<tr>
							<!-- <th colspan="4" style="font-size: 20px;">基本信息</th> -->
							<th colspan="1" style="font-size: 20px;"><span
								class="prompt" style="color: red; display: none;">*</span>基本信息</th>	
							<th colspan="3"> <a href="#" data-toggle="modal">
							<input id="scan" class="btn btn-primary smBtn" onclick="scanningInfoEnter()" data-toggle="modal" value="扫描录入" /></a>	
							</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>应用名称：</th>
							<td><input type="text" name="configApp" disabled="disabled"
								value="${workDealInfo.configApp.appName }" id="app" />
								<input type="hidden" id="appId" value="${workDealInfo.configApp.id }" />
							</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<td>
									<input type="checkbox" disabled="disabled" checked="checked"
										value="3" name="dealInfoType2">
										<font color="red" style="font-weight:bold;">信息变更 </font>
										<input
										type="hidden" value="3" name="dealInfoType2">
										
									<c:if test="${reissue==2}"><input type="checkbox" disabled="disabled" checked="checked" value = "1"
								name="dealInfoType1">
								<font color="red" style="font-weight:bold;">遗失补办</font>
								<input type="hidden" value="2" name="dealInfoType1"></c:if>
								<c:if test="${reissue==3}"><input type="checkbox" disabled="disabled" checked="checked" value = "2"
								name="dealInfoType1">
								<font color="red" style="font-weight:bold;">损坏更换</font>
								<input type="hidden" value="3" name="dealInfoType1"></c:if>
							</td>	
						</tr>
						<tr>
							
							<th><span class="prompt" style="color: red; display: none;">*</span>选择产品：</th>
							<td>
							<select name="product"  id="product">
									<c:forEach items="${proList}" var="product">
										<option value="${product.id}" <c:if test="${product.id==workDealInfo.configProduct.id }">selected="selected"</c:if> >${product.name}</option>
									</c:forEach>
							</select>	
							</td>
							
							<th><span class="prompt" style="color: red; display: none;">*</span>申请年数：</th>
							<td><input type="radio" id="delay" checked="checked"
								name="year" disabled="disabled"> <span>不延期</span>
								</td>
										
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>计费策略类型：</th>
							<td>
							
							<select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select>
			
			</td>
			<c:if test="${reissue==2}">
						
							<th>人为损坏：</th>
							<td><input type="radio" name="manMadeDamage" value="true">是
							 <input type="radio" name="manMadeDamage"value="false">否</td>
						
						</c:if>
			
						<th><span class="prompt" style="color: red; display: none;">*</span>计费策略模版：</th>
							<td>
							
							<select	 id="agentDetailId" name="agentDetailId">
									<option value="0">请选择</option>
							</select> 
							
							</td>
							
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
							<th colspan="4" style="font-size: 20px;">单位信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" id="companyName"
								maxlength="50" value="${workDealInfo.workCompany.companyName}" />


							</td>
							<th>单位类型：</th>
							<td><select name="companyType">
									<option value="1" id="companyType1"
										<c:if test="${workDealInfo.workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workDealInfo.workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if test="${workDealInfo.workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4"
										<c:if test="${workDealInfo.workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if test="${workDealInfo.workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码：</th>
							<td><input type="text" name="organizationNumber"
								id="organizationNumber1"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								value="${workDealInfo.workCompany.organizationNumber}"
								maxlength="18"  onblur="qxCount('zzcount')" onfocus="hqcount('organizationNumber1','zzcount')"/><span id="zzcount" style="color : red; margin-left: 10px"></span></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate"
								
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0"
										<c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType">
									<option value="0" id="comCertificateType0"
										<c:if test="${workDealInfo.workCompany.comCertificateType==0 }">selected</c:if>>营业执照</option>
									<option value="1" id="comCertificateType1"
										<c:if test="${workDealInfo.workCompany.comCertificateType==1 }">selected</c:if>>事业单位法人登记证</option>
									<option value="2" id="comCertificateType2"
										<c:if test="${workDealInfo.workCompany.comCertificateType==2 }">selected</c:if>>社会团体登记证</option>
									<option value="3" id="comCertificateType3"
										<c:if test="${workDealInfo.workCompany.comCertificateType==3 }">selected</c:if>>其他</option>
							</select></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								id="comCertficateNumber1" maxlength="18"
								value="${workDealInfo.workCompany.comCertficateNumber}"  onblur="qxCount('zjcount')" onfocus="hqcount('comCertficateNumber1','zjcount')"/><span id="zjcount" style="color : red; margin-left: 10px"></span></td>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>法人姓名：</th>
							<td style="vertical-align: middle;"><input type="text" name="legalName"
								value="${workDealInfo.workCompany.legalName}" ></td>
							<th>行政所属区：</th>
							<td><select id="s_province" name="s_province" onchange="getCity(this.options[this.options.selectedIndex].id)" style="width: 100px;"></select>&nbsp;&nbsp; 
								<select id="s_city" name="s_city" onchange="getTown(this.options[this.options.selectedIndex].id)" style="width: 100px;"></select>&nbsp;&nbsp; 
								<select id="s_county" name="s_county" style="width: 100px;"></select> 
								<script type="text/javascript">
								if(${provinceId!=null}){
									getCity(${provinceId});
								}
								if(${cityId!=null}){
									getTown(${cityId});
								}	
								</script>
								<div style="margin-top: 8px;">
									<span class="prompt" style="color: red; display: none;">*</span>区域备注：
								</div></td>


						</tr>
						<tr>
						<th>区域备注：</th>
						<td><input type="text" name="areaRemark" value="${workDealInfo.workCompany.areaRemark }" ></td>
						
							<th><span class="prompt" style="color: red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address"
								value="${workDealInfo.workCompany.address}"></td>
													</tr>
						<tr>
						
						
						<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>单位联系电话：</th>
							<td class="btmBorder"><input type="text" name="companyMobile" class="number"
								id="companyMobile"
								value="${workDealInfo.workCompany.companyMobile }"  onblur="qxCount('dwtcount')"  onfocus="hqcount('companyMobile','dwtcount')"/><span id="dwtcount" style="color : red; margin-left: 10px"></span></td>
						
						
							<th><span class="prompt" style="color: red; display: none;">*</span>备注信息：</th>
							<td><input type="text" name="remarks" id="remarks"
								value="${workDealInfo.workCompany.remarks }"></td>
							
							
							
						</tr>
						
						
						
						<c:if test="${workDealInfo.selfImage.id!=null }">
							<tr>
							<th>单位电子证件:</th>
							<td class ="small_pic">
								
								<div class="col-sm-9">
										<label for="exampleInputFile" class="uploadBtn">上传文件</label>
										<input type="file" id="exampleInputFile" name = "companyImage" class="uploadFileInput" onchange="PreviewImage(this,'imghead','preview','picBigBox')">
			                            <br><br>
			                            <div class="previewImg" id="preview">
			                            	<div class="small_pic">
													<a href="#picBigBox" ><img id="imghead" style = "width:100px;height:75px" src="${imgUrl}/${workDealInfo.selfImage.companyImage }" ></a>
			                                </div>
			                            </div>
									</div>
							</td>
							</tr>
								</c:if>

					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">证书持有人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName1"
								value="${workDealInfo.workUser.contactName }" onblur="setJBRName()" /></td>
							<th>证书持有人证件:</th>
							<td><select name="conCertType">
									<option value="0" id="conCertType0"
										<c:if test="${workDealInfo.workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workDealInfo.workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workDealInfo.workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber"
								id="conCertNumber1" onblur="setJBRCard('zjmcount')"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="18"
								value="${workDealInfo.workUser.conCertNumber }" onfocus="hqcount('conCertNumber1','zjmcount')"/><span id="zjmcount" style="color : red; margin-left: 10px"></span></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail" onblur="setJBRMail()"
								class="email" maxlength="30"
								value="${workDealInfo.workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone"
								id="contactPhone1" maxlength="11" class="number"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								value="<fmt:formatNumber pattern="#">${workDealInfo.workUser.contactPhone }</fmt:formatNumber>"  onblur="qxCount('zjtcount')" onfocus="hqcount('contactPhone1','zjtcount')"/><span id="zjtcount" style="color : red; margin-left: 10px"></span><input
								type="hidden" name="contactPhone" id="contactPhone"
								maxlength="11" class="number" disabled="disabled"
								value="<fmt:formatNumber pattern="#">${workDealInfo.workUser.contactPhone }</fmt:formatNumber>" /></td>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>业务系统UID:</th>
							<td class="btmBorder"><input type="text" name="contactTel" id="contactTel1"
								maxlength="20" value="${workDealInfo.workUser.contactTel }"  onblur="qxCount('ywidcount')" onfocus="hqcount('contactTel1','ywidcount')"/><span id="ywidcount" style="color : red; margin-left: 10px"></span>
							</td>
						</tr>
						<tr>
							<th>证书持有人性别:</th>
							<td><input name="contactSex" id="sex0"
								<c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if>
								type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="contactSex" id="sex1"
								<c:if test="${workDealInfo.workUser.contactSex=='女' }">checked</c:if>
								type="radio" value="女">女</td>
							
							<c:if test="${workDealInfo.selfImage.id!=null }">
							<th >个人电子证件:</th>
							<td class = "small_pic">
								
								
								<div class="col-sm-9">
									<label for="exampleInputFile1" class="uploadBtn">上传图片</label>
									<input type="file" id="exampleInputFile1" name = "transactorImage" class="uploadFileInput"  onChange="PreviewImage(this,'imghead1','preview1','picBigBox1')">
									<br><br>
									<div id="preview1" class="previewImg">
	                            		<div class="small_pic">
		                            		<a href="#picBigBox1" >
		                            		   <img id="imghead1" style = "width:100px;height:75px"  src="${imgUrl }/${workDealInfo.selfImage.transactorImage}" >
	                            			</a>
	                            		</div>
	                            	</div>
                        		</div>
							</td>	
								</c:if>	
							
							
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
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" /></td>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>经办人身份证号:</th>
							<td class="btmBorder"><input type="text" name="pIDCard" id="pIDCard"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }"  onblur="qxCount('pIDcount')" onfocus="hqcount('pIDCard','pIDcount')"/><span id="pIDcount" style="color : red; margin-left: 10px"></span></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.email }" /></td>
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
						<tr>
							<td>1</td>
							<td><input type="text" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" name="deal_info_status" value="5">
		<input type="hidden" name="workDealInfoId" value="${workDealInfo.id }">
		<div class="control-group span12">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr>
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2"><shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="button"
										onclick="onSubmit()" value="提 交" />&nbsp;</shiro:hasPermission> <input
								id="btnCancel" class="btn" type="button" value="返 回"
								onclick="history.go(-1)" /> <input type="hidden" id="isOK"
								name="isOK" value="${isOK }"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>

<div id="picBigBox" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.companyImage }"  >
	</div>
	<div id="picBigBox1" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.transactorImage }" >
	</div>
		

</body>
</html>
