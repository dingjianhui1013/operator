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
<script type="text/javascript">
	$(document)
			.ready(
					function() {
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
									$("#conCertNumber1").bind("propertychange", function() {
										count('conCertNumber1','zjmcount')
									});
									$("#contactPhone1").bind("propertychange", function() {
										count('contactPhone1','zjtcount')
									});
									$("#contactTel1").bind("propertychange", function() {
										count('contactTel1','ywidcount')
									});
								}else{
									$("#conCertNumber1").attr("onpropertychange","count('conCertNumber1','zjmcount')");
									$("#contactPhone1").attr("onpropertychange","count('contactPhone1','zjtcount')");
									$("#contactTel1").attr("onpropertychange","count('contactTel1','ywidcount')");
										
								}
							
							
							
							
						}else{
					
							$("#conCertNumber1").attr("oninput","count('conCertNumber1','zjmcount')");
							$("#contactPhone1").attr("oninput","count('contactPhone1','zjtcount')");
							$("#contactTel1").attr("oninput","count('contactTel1','ywidcount')");
						
						}
						
						
						

						var url = "${ctx}/work/workDealInfo/showYear?lable=${workDealInfo.configProduct.productLabel}&productName=${workDealInfo.configProduct.productName}&app=${workDealInfo.configApp.id}&infoType=${empty update?'':1}&_="
								+ new Date().getTime();
						$.getJSON(url, function(data) {
							if (data.year1) {
								$("#year1").show();
								$("#word1").show();
							} else {
								$("#year1").hide();
								$("#word1").hide();
							}
							cc
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
									if(arrList[i] == "contactName"){
										$("#contactName1").attr("disabled","disabled");
									}else if(arrList[i] == "conCertType"){
										$("#conCertType1").attr("disabled","disabled");
									}else if(arrList[i] == "conCertNumber"){
										$("#conCertNumber1").attr("disabled","disabled");
									}else if(arrList[i] == "contactPhone"){
										$("#contactPhone1").attr("disabled","disabled");
									}else if(arrList[i] == "contactTel"){
										$("#contactTel1").attr("disabled","disabled");
									}else if(arrList[i] == "contactSex"){
										$("#sex0").attr("disabled","disabled");
										$("#sex1").attr("disabled","disabled");
									}
								}else {
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
							var lable = "${workDealInfo.configProduct.productLabel}";
							$("#agentId").attr("onchange","setStyleList("+lable+")");
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
							var appId = $("#appId").val();
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+product+"&app="+appId+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
<script type="text/javascript">
	function onSubmit() {
		$("#newInfoId").val(getCookie("work_deal_info_id"));
		delCookie("work_deal_info_id");
		$("#inputForm").submit();
	}

	
	function changeDealInfoType(){
		var id = "${workDealInfo.id}";
		var submit = function (v, h, f) {
							    if (v == 'ok'){
							    	maintain(id);
							    }
							   		 return true; //close
							};
							top.$.jBox.confirm("您确定需要更换业务类型吗？", "提示", submit);
	}
	
	function maintain(obj) {
		top.$.jBox
		.open(
				"iframe:${ctx}/work/workDealInfo/typeShow?infoId="+obj,
				"请选择业务类型",
				500,
				300,
				{
					buttons : {
						"确定" : "ok",
						"关闭" : true
					},
					submit : function(v, h, f) {
						if (v == "ok") {
							var table = h.find("iframe")[0].contentWindow.typeForm;
							var dealTypes = $(table).find("input[name='dealType']");
							var dealType = "";
							var reissueType = "";
							for (var i = 0; i < dealTypes.length; i++) {
								if (dealTypes[i].checked == true) {
									if (i == 0) {
										dealType = dealTypes[i].value;
									} else {
										dealType = dealType + "," + dealTypes[i].value;
									}
								}
							}
							var id = $(table).find("input[name='id']").val();
							var reissueTypes = $(table).find("input[name='reissueType']");
							for (var i = 0; i < reissueTypes.length; i++) {
								if (reissueTypes[i].checked == true) {
									reissueType = reissueTypes[i].value;
								}
							}
							if (dealType == "") {
								top.$.jBox.tip("请选择业务类型");
							} else {
								
								if(dealType.indexOf("3")>=0){
									var url = "${ctx}/work/workDealInfo/findById?dealInfoId="+id;
									$.getJSON(url + "&_="+new Date().getTime(),	function(data){
												if (data.status==1){
													if (data.isUpdate==0) {
														top.$.jBox.tip("证书未在更新范围内，不允许更新此证书 ！");
													}else{
														window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
													}
												}else{
													top.$.jBox.tip("系统异常");
												}
									});
								}else{
									window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
								}
							}
						}
					},
					loaded : function(h) {
						$(".jbox-content", top.document).css(
								"overflow-y", "hidden");
					}
				});
	};
	function setJBRCard(o){
		var card = $("#conCertNumber1").val();
		$("#pIDCard").val(card);
		$("#"+o).hide();
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
			href="${ctx}/work/workDealInfo/typeForm?id=${workDealInfo.id}&reissueType=${reissue}&dealType=${dealType}">业务补办</a></li>
	</ul>
	<form:form id="inputForm"
		action="${ctx}/work/workDealInfoOperationSed/maintainSaveReturnLost" 
		method="post" enctype="multipart/form-data"
		class="form-horizontal">
		<tags:message content="${message}" />
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="6" style="font-size: 20px;">基本信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>应用名称：</th>
							<td><input type="text" name="configApp" disabled="disabled"
								value="${workDealInfo.configApp.appName }" id="app" />
								<input type="hidden" id="appId" value="${workDealInfo.configApp.id }" />
								</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>选择产品：</th>
							<td colspan="3"><input type="text" name="product" disabled="disabled"
								value="${pro[workDealInfo.configProduct.productName] }" />
								<input type="hidden" id="product" value="${workDealInfo.configProduct.productName }" />	
								</td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>应用标识：</th>
							<td><input type="radio" disabled="disabled" name="lable"
								<c:if test="${workDealInfo.configProduct.productLabel==0}">checked="checked"</c:if>
								id="lable0" value="0">通用 &nbsp; &nbsp; <input
								type="radio" disabled="disabled" name="lable"
								<c:if test="${workDealInfo.configProduct.productLabel==1}">checked="checked"</c:if>
								id="lable1" value="1">专用</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<td colspan="3">
								
								<c:if test="${reissue==2}"><input type="checkbox" disabled="disabled" checked="checked" value = "1"
								name="dealInfoType1"><font color="red" style="font-weight:bold;">遗失补办</font><input type="hidden" value="1" name="dealInfoType1"></c:if>
								<c:if test="${reissue==3}"><input type="checkbox" disabled="disabled" checked="checked" value = "2"
								name="dealInfoType1"><font color="red" style="font-weight:bold;">损坏更换</font><input type="hidden" value="2" name="dealInfoType1"></c:if>
								
							</td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>申请年数：</th>
							<td><input type="radio" id="delay" checked="checked"
								name="year" disabled="disabled"> <span>不延期</span>

							</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>计费策略类型：</th>
							<td style="width: 100px;">
							
							<select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select>
							
							</td>
			
								<th><span class="prompt" style="color: red; display: none;">*</span>计费策略模版：</th>
							<td>
							
							<select	 id="agentDetailId" name="agentDetailId">
									<option value="0">请选择</option>
							</select> 
							
							
							</td>
						</tr>
						<c:if test="${reissue==2}">
						<tr id="manMade">
							<th>人为损坏：</th>
							<td><input type="radio" name="manMadeDamage" value="true">是
							 <input type="radio" name="manMadeDamage"value="false">否</td>
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
							<th colspan="4" style="font-size: 20px;">单位信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" id="companyName" disabled="disabled"
								maxlength="50" value="${workDealInfo.workCompany.companyName}" />


							</td>
							<th>单位类型：</th>
							<td><select name="companyType" disabled="disabled">
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
							<td><input type="text" name="organizationNumber" disabled="disabled"
								id="organizationNumber1"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								value="${workDealInfo.workCompany.organizationNumber}"
								maxlength="18" /></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" disabled="disabled"
								
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th>服务级别：</th>
							<td><select name="selectLv" disabled="disabled">
									<option value="0" id="selectLv0"
										<c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType" disabled="disabled">
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
							<td><input type="text" name="comCertficateNumber" disabled="disabled"
								id="comCertficateNumber1" maxlength="18"
								value="${workDealInfo.workCompany.comCertficateNumber}" /></td>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text" disabled="disabled"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>法人姓名：</th>
							<td><input type="text" name="legalName" disabled="disabled"
								value="${workDealInfo.workCompany.legalName}"></td>
							<th>行政所属区：</th>
							<td><select id="s_province" name="s_province" disabled="disabled"
								style="width: 100px;">
							</select>&nbsp;&nbsp; <select id="s_city" name="s_city" disabled="disabled"
								style="width: 100px;"></select>&nbsp;&nbsp; <select disabled="disabled"
								id="s_county" name="s_county" style="width: 100px;"></select> <script
									type="text/javascript">
									_init_area();
									$("#s_province")
											.append(
													'<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
									$("#s_city")
											.append(
													'<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
									$("#s_county")
											.append(
													'<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
								</script>
								<div style="margin-top: 8px;">
									<span class="prompt" style="color: red; display: none;">*</span>区域备注：<input
										type="text" name="areaRemark" disabled="disabled"
										value="${workDealInfo.workCompany.areaRemark }">
								</div></td>


						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address" disabled="disabled"
								value="${workDealInfo.workCompany.address}"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位联系电话：</th>
							<td><input type="text" name="companyMobile" class="number" disabled="disabled"
								id="companyMobile"
								value="${workDealInfo.workCompany.companyMobile }"></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>备注信息：</th>
							<td><input type="text" name="remarks" id="remarks" disabled="disabled"
								value="${workDealInfo.workCompany.remarks }"></td>
							
							<c:if test="${workDealInfo.selfImage.id!=null }">
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
								</c:if>
							
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
							<th colspan="4" style="font-size: 20px;">证书持有人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName1"
								value="${workDealInfo.workUser.contactName }" onblur="setJBRName()" /></td>
							<th>证书持有人证件:</th>
							<td><select name="conCertType" id="conCertType1">
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
								onblur="setJBRCard('zjmcount')"
								id="conCertNumber1" 
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="18"
								value="${workDealInfo.workUser.conCertNumber }"  onfocus="hqcount('conCertNumber1','zjmcount')"/><span id="zjmcount" style="color: red; margin-left: 10px"></span></td>
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
								value="<fmt:formatNumber pattern="#">${workDealInfo.workUser.contactPhone }</fmt:formatNumber>"  onblur="qxCount('zjtcount')" onfocus="hqcount('contactPhone1','zjtcount')"/><span id="zjtcount" style="color: red; margin-left: 10px"></span> 
							</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel1"
								maxlength="20" value="${workDealInfo.workUser.contactTel }"  onblur="qxCount('ywIDcount')" onfocus="hqcount('contactTel1','ywIDcount')"/><span id="ywIDcount" style="color: red; margin-left: 10px"></span>
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
							<td><input type="text" name="pName" id="pName" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人身份证号:</th>
							<td><input type="text" name="pIDCard" id="pIDCard" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail" disabled="disabled"
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
