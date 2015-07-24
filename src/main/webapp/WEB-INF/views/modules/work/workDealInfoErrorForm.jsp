<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document)
			.ready(
					function() {
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

						var url = "${ctx}/work/workDealInfo/app?_="+new Date().getTime();
						$
								.getJSON(
										url,
										function(d) {
											$("#app")
													.bigAutocomplete(
															{
																data : d.lis,
																callback : function(
																		data) {

																	var url1 = "${ctx}/work/workDealInfo/product?appId=";

																	$
																			.getJSON(
																					url1
																							+ data.result+"&_="+new Date().getTime(),
																					function(
																							da) {
																						$(
																								"#appId")
																								.val(
																										da.appId);
																						if (!da.product1) {
																							$(
																									"#product1")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product1")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product2) {
																							$(
																									"#product2")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product2")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product3) {
																							$(
																									"#product3")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product3")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product4) {
																							$(
																									"#product4")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product4")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product5) {
																							$(
																									"#product5")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product5")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product6) {
																							$(
																									"#product6")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product6s")
																									.attr(
																											"style",
																											"display:");
																						}
																					});
																}
															});
										});

						var url = "${ctx}/work/workDealInfo/tt?_="+new Date().getTime();
						$
								.getJSON(
										url,
										function(da) {
											$("#tt")
													.bigAutocomplete(
															{
																data : da.lis,
																callback : function(
																		data) {
																	var url1 = "${ctx}/work/workDealInfo/cert?id=";
																	$
																			.getJSON(
																					url1
																							+ data.result+"&_="+new Date().getTime(),
																					function(
																							d) {
																						$("#companyId")
																								.val(
																										d.companyId);
																						$(
																								"#companyName")
																								.val(
																										d.companyName);
																						if (d.workCompany.companyType == 1) {
																							$("#companyType1").attr("selected","selected");
																						}
																						if (d.companyType == 2) {
																							$("#companyType2").attr("selected","selected");
																						}
																						if (d.companyType == 3) {
																							$(
																									"#companyType3")
																									.attr("selected","selected");
																						}
																						if (d.companyType == 4) {
																							$("#companyType4").attr("selected","selected");
																						}
																						if (d.companyType == 5) {
																							$("#companyType5").attr("selected","selected");
																						}
																						$(
																								"#organizationNumber")
																								.val(
																										d.organizationNumber);
																						$(
																								"#orgExpirationTime")
																								.val(
																										d.orgExpirationTime);
																						if (d.selectLv==0) {
																							$("#selectLv0").attr("selected","selected");
																						}
																						if (d.selectLv==1) {
																							$("#selectLv1").attr("selected","selected");
																						}
																						if (d.comCertificateType == 0) {
																							$(
																									"#comCertificateType0")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 1) {
																							$(
																									"#comCertificateType1")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 2) {
																							$(
																									"#comCertificateType2")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 3) {
																							$(
																									"#comCertificateType3")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 4) {
																							$(
																									"#comCertificateType4")
																									.attr("selected","selected");
																						}
																						$(
																								"#comCertficateNumber")
																								.val(
																										d.comCertficateNumber);
																						$(
																								"#comCertficateTime")
																								.val(
																										d.comCertficateTime);
																						$(
																								"#legalName")
																								.val(
																										d.legalName);
																						$(
																								"#address")
																								.val(
																										d.address);
																						$(
																								"#companyMobile")
																								.val(
																										d.companyMobile);
																						$(
																								"#remarks")
																								.val(
																										d.remarks);
																						$(
																								"#s_province")
																								.val(
																										d.province);
																						$(
																								"#contactName")
																								.val(
																										d.contactName);
																						if ('男' == d.conCertSex) {
																							$(
																									"#sex0")
																									.attr("checked","checked");
																						}
																						if (d.conCertSex =='女') {
																							$(
																									"#sex1")
																									.attr("checked","checked");
																						}
																						if (d.conCertType == 0) {
																							$(
																									"#conCertType0")
																									.attr("selected","selected");
																						}
																						if (d.conCertType == 1) {
																							$(
																									"#conCertType1")
																									.attr("selected","selected");
																						}
																						if (d.conCertType == 2) {
																							$(
																									"#conCertType2")
																									.attr("selected","selected");
																						}
																						$(
																								"#contacEmail")
																								.val(
																										d.contacEmail);
																						$(
																								"#contactPhone")
																								.val(
																										d.contactPhone);
																						$(
																								"#contactTel")
																								.val(
																										d.contactTel);
																						$(
																								"#workuserId")
																								.val(
																										d.workuserId);
																						$(
																								"#conCertNumber")
																								.val(
																										d.conCertNumber);
																						$("#s_city").append('<option value="'+d.city+'" selected="selected">'+d.city+'</option>');
																						$("#s_county").append('<option value="'+d.district+'" selected="selected">'+d.district+'</option>');
																						showCert(d.companyId);
																					});
																}
															});
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
						
						}
						
						var productName = $("input[name='product']:checked").val();
						var agentId = $("#agentId").val();
						if (agentId!=0) {
							var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
						
						
										
						/* var url = "${ctx}/work/workDealInfo/showYear?lable=${workDealInfo.configProduct.productLabel}&productName=${workDealInfo.configProduct.productName}&app=${workDealInfo.configApp.id}&infoType=${workDealInfo.dealInfoType}&_="+new Date().getTime();
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
							
							
							if (data.tempStyle == 1) {
								$("#payType1").attr("checked","checked");
								$("#payType1").removeAttr("disabled");
								$("#payType2").attr("disabled","disabled");
								$("#payType3").attr("disabled","disabled");
							}else if (data.tempStyle == 2) {
								$("#payType2").attr("checked","checked");
								$("#payType2").removeAttr("disabled");
								$("#payType1").attr("disabled","disabled");
								$("#payType3").attr("disabled","disabled");
							}else if (data.tempStyle == 3) {
								$("#payType3").attr("checked","checked");
								$("#payType3").removeAttr("disabled");
								$("#payType1").attr("disabled","disabled");
								$("#payType2").attr("disabled","disabled");
							} else {
								top.$.jBox.tip("请先配置计费策略！"); 
							}
							
							
							
							
							
							var arr = [data.nameDisplayName,data.orgunitDisplayName,data.emailDisplayName,data.commonNameDisplayName,data.addtionalField1DisplayName,data.addtionalField2DisplayName,data.addtionalField3DisplayName,data.addtionalField4DisplayName,data.addtionalField5DisplayName,data.addtionalField6DisplayName,data.addtionalField7DisplayName,data.addtionalField8DisplayName]
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
						}); */

					});
	
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
	
	
	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleList(obj){
		var lable = obj;
		var productName = $("input[name='product']:checked").val();
		var agentId = $("#agentId").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
			top.$.jBox.tip("请您选择产品！");
			
		}
	}
	
	
	//获取计费模版相对应的年限
	function setYearByBoundId(){
		var boundId = $("#agentDetailId").val();
		$("#boundId").val(boundId);
		showYear();
	}
	
	
</script>
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
	function os() {
		$("#inputForm")
				.attr("action", "${ctx}/work/workDealInfo/temporarySave");
		$("#inputForm").submit();
	}

	function productLabel(data) {
		var appId = $("#appId").val();
		var url = "${ctx}/work/workDealInfo/type?name=" + data + "&appId="
				+ appId;
		$.getJSON(url+"&_="+new Date().getTime(), function(da) {
			if (da.type0) {
				$("#lable0").attr("checked", "checked");
				showYear(0);
			}
			if (da.type1) {
				$("#lable1").attr("checked", "checked");
				showYear(1);
			}
		});
		if (data == 2) {
			$("#proposer").attr("style", "display:");
		} else {
			$("#proposer").attr("style", "display:none");
		}
	}
	function showCert(companyId) {
		var url = "${ctx}/work/workDealInfo/showCert?id=" + companyId;
		top.$.jBox.open("iframe:" + url, "已有证书明细", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
	}
	function onSubmit(){
		$("#lable0").removeAttr("disabled");
		$("#lable1").removeAttr("disabled");
		$("#dealInfoType").removeAttr("disabled");
		return true;
	}
	/* 
	* 功能:根据产品带回年限
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showYear(){
		//var lable = obj;
		//var productName = $("input[name='product']:checked").val();
		var agentId = $("#boundId").val();
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentId+"&infoType=0&_="+new Date().getTime();
		//var url = "${ctx}/work/workDealInfo/showYear?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
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
			var arr = [data.nameDisplayName,data.orgunitDisplayName,data.emailDisplayName,data.commonNameDisplayName,data.addtionalField1DisplayName,data.addtionalField2DisplayName,data.addtionalField3DisplayName,data.addtionalField4DisplayName,data.addtionalField5DisplayName,data.addtionalField6DisplayName,data.addtionalField7DisplayName,data.addtionalField8DisplayName]
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
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfoOperation/errorForm?id=${workDealInfo.id}">业务编辑</a></li>
	</ul>
	<form:form id="inputForm" action="${ctx}/work/workDealInfo/save"
		method="POST" class="form-horizontal">
		<tags:message content="${message}" />
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="6" style="font-size: 20px;"><span class="prompt" style="color:red; display: none;">*</span>基本信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>代办应用：</th>
							<td colspan="3"><input type="text" name="configApp" 
								value="${workDealInfo.configApp.appName }" id="app" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>选择产品：</th>
								<td>
								<c:forEach items="${proList }" var="pro">
									<div id="product${pro.id }" <c:if test="${workDealInfo.configProduct.productName!=pro.id}">style="display:none;"</c:if>>
									<input type="radio" name="product" id="product"
										onclick="productLabel(${pro.id})"
										<c:if test="${workDealInfo.configProduct.productName==pro.id}">checked</c:if>
										
										value="${pro.id}">${pro.name }&nbsp;&nbsp;&nbsp;
									</div>
								</c:forEach>
								</td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>应用标识：</th>
							<td colspan="3"><input type="radio" name="lable" id="lable0" value="0" disabled="disabled"
							<c:if test="${workDealInfo.configProduct.productLabel==0}">checked</c:if>>通用&nbsp;
								&nbsp; <input type="radio" name="lable" id="lable1" value="1" disabled="disabled"
								<c:if test="${workDealInfo.configProduct.productLabel==1}">checked</c:if>>专用</td>
							<th><span class="prompt" style="color:red; display: none;">*</span>业务类型：</th>
							<td><input type="checkbox" name="dealInfoType" value="0" checked="checked" disabled="disabled"
								<c:if test="${workDealInfo.dealInfoType==0}">checked</c:if>>新增证书
							</td>
						</tr>
						<tr>
							
							<th style="width: 100px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略类型：</th>
							<td  style="width: 250px;"><select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select> <input type="hidden" id="boundId"></td>
							<th style="width: 100px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模版：</th>
							<td style="width: 250px;"><select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select>
							</td>
							
							<th style="width: 100px;"><span class="prompt" style="color:red; display: none;">*</span>申请年数：</th>
							<td><input type="radio" name="year" value="1" id="year1"
								<c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span id="word1">1年</span> <input
								type="radio" name="year" value="2" id="year2"
								<c:if test="${workDealInfo.year==2}">checked</c:if>><span id="word2">2年 </span><input
								type="radio" name="year" value="4" id="year4"
								<c:if test="${workDealInfo.year==4}">checked</c:if>><span id="word4">4年</span><input
								type="radio" name="year" value="5" id="year5"
								<c:if test="${workDealInfo.year==5}">checked</c:if>><span id="word5">5年</span>
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
							<th><span class="prompt" style="color:red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" 
								value="${workCompany.companyName }" id="tt"> <input
								type="hidden" name="companyId" value="${workCompany.id }"
								id="companyId"></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位类型：</th>
							<td><select name="companyType" >
									<option value="1" id="companyType1"
										<c:if test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if test="${workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4" <c:if test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5" <c:if test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>组织机构代码：</th>
							<td><input type="text" name="organizationNumber" 
								value="${workCompany.organizationNumber }"
								id="organizationNumber" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" 
								value="<fmt:formatDate value="${workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"
								type="text" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								id="orgExpirationTime" /></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0" <c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1" <c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
								</select></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位证照：</th>
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
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								value="${workCompany.comCertficateNumber }"
								id="comCertficateNumber" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text" 
								value="<fmt:formatDate value="${workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" 
								maxlength="20" readonly="readonly" name="comCertficateTime"
								id="comCertficateTime" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>法人姓名：</th>
							<td><input type="text" name="legalName" id="legalName" 
								value="${workCompany.legalName }"></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>行政所属区：</th>
							<td><select id="s_province" name="s_province"
								style="width: 100px;"></select>&nbsp;&nbsp; <select id="s_city"
								name="s_city" style="width: 100px;"></select>&nbsp;&nbsp; <select
								id="s_county" name="s_county" style="width: 100px;"></select> <script
									type="text/javascript">
									_init_area();
								</script>
								<div id="show"></div></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address" id="address"
								value="${workCompany.address }"></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位联系电话：</th>
							<td><input type="text" name="companyMobile" 
								id="companyMobile" value="${workCompany.companyMobile }"></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>备注信息：</th>
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
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人姓名:</th>
							<td><input type="text" name="contactName" id="contactName" 
								value="${workUser.contactName }" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人证件:</th>
							<td><select name="conCertType">
									<option value="0" id="conCertType0"
										<c:if test="${workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber"
								id="conCertNumber" value="${workUser.conCertNumber }" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								value="${workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								value="${workUser.contactPhone }" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel"
								value="${workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人性别:</th>
							<td><input name="contactSex" id="sex0" <c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if> type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="contactSex" id="sex1" <c:if test="${workDealInfo.workUser.contactSex=='女'}">checked</c:if> type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer"
				<c:if test="${workDealInfo.configProduct.productName!=2}"> style="display:none"</c:if>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">申请人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>姓名:</th>
							<td><input type="text" name="pName"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>身份证号:</th>
							<td><input type="text" name="pIDCard"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>邮箱:</th>
							<td><input type="text" name="pEmail"
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
						<c:forEach items="${workLog}" var="workLog" varStatus="status">
							<tr>
								<td>${status.count }</td>
								<td>${workLog.recordContent }</td>
								<td>${workLog.createBy.name }</td>
								<td>${workLog.createBy.office.name }</td>
								<td><fmt:formatDate value="${workLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
		<div class="control-group span12">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr>
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2">&nbsp; <shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return onSubmit()"
										value="提 交" />&nbsp;</shiro:hasPermission> <input id="btnCancel" class="btn"
								type="button" value="返 回" onclick="history.go(-1)" />
								
								</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>

</html>
