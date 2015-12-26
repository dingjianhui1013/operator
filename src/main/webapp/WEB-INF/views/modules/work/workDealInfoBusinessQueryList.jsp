<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务查询</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function addOffice() {
		var areaId = $("#area").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+0+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#officeId").html(html);
		});
	}
	
	function alarmValue(dealInfoId){
		var url = "${ctx}/work/workDealInfo/showWorkDeal?dealInfoId="+dealInfoId;
		top.$.jBox.open("iframe:"+url, "查看", 550, 480, {
				buttons:{"关闭":true}, submit:function(v, h, f){
					
				}
		});
	}
	
	function show(){
		document.getElementById("btnSubmit").style.display="none";
		document.getElementById("tjjg").style.display="none";
		document.getElementById("gjcx").style.display="none";
		document.getElementById("exportZS").style.display="none";
		document.getElementById("resetTOP").style.display="none";
		document.getElementById("advanced").style.display="";
		}
	function hidde(){
		document.getElementById("advanced").style.display="none";
		document.getElementById("btnSubmit").style.display="";
		document.getElementById("tjjg").style.display="";
		document.getElementById("gjcx").style.display="";
		document.getElementById("exportZS").style.display="";
		document.getElementById("resetTOP").style.display="";
		}
	window.onload=function(){
		var area = document.getElementById("area").value;
		var officeId = document.getElementById("officeId").value;
		var year = document.getElementById("year").value;
		var payMethod = document.getElementById("payMethod").value;
		if(area != "" || officeId != "" || year != "" || payMethod !="0"){
			show();
		}
	}
	function dcZS(){
		var apply=$("#apply").val();
		var companyName=$("#companyName").val();
		var organizationNumber=$("#organizationNumber").val();
		var contactName=$("#contactName").val();
		var conCertNumber=$("#conCertNumber").val();
		var area=$("#area ").val();
		var officeId =$("#officeId").val();
		var certType =$("#certType").val();
		var workType =$("#workType").val();
		var keySn = $("#keySn").val();
		var createByname =$("#createByname").val();
		var zhizhengname=$("#zhizhengname").val();
		var updateByname=$("#updateByname").val();
		var payType =$("#payType").val();
		var s_province =$("#s_province").val();
		var s_city = $("#s_city").val();
		var s_county=$("#s_county").val();
		var luruStartTime =$("#luruStartTime").val();
		var luruEndTime=$("#luruEndTime").val();
		var jianzhengStartTime =$("#jianzhengStartTime").val();
		var jianzhengEndTime = $("#jianzhengEndTime").val();
		var zhizhengStartTime=$("#zhizhengStartTime").val();
		var zhizhengEndTime =$("#zhizhengEndTime").val();
		var daoqiStartTime=$("#daoqiStartTime").val();
		var daoqiEndTime=$("#daoqiEndTime").val();
		var year= $("#year").val();
		var payMethod=$("#payMethod").val();
		alert(s_county);
		window.open("${ctx}/work/workDealInfo/exportZS?certType="+certType+"&workType="+workType+"&apply="+apply+"&area="+area+"&officeId="+officeId+"&companyName="+companyName+
		"&organizationNumber="+organizationNumber+"&contactName="+contactName+"&conCertNumber="+conCertNumber+"&keySn="+keySn+"&createByname="+createByname+"&zhizhengname="+zhizhengname+
		"&updateByname="+updateByname+"&payType="+payType+"&s_province="+s_province+"&s_city="+s_city+"&s_county="+s_county+"&luruStartTime="+luruStartTime+
		"&jianzhengStartTime="+jianzhengStartTime+"&jianzhengEndTime="+jianzhengEndTime+"&zhizhengStartTime="+zhizhengStartTime+"&zhizhengEndTime="+zhizhengEndTime+
		"&daoqiStartTime="+daoqiStartTime+"&daoqiEndTime="+daoqiEndTime+"&luruEndTime="+luruEndTime+"&payMethod="+payMethod+"&year="+year);
	}
	function onSubmit(){
		var area = document.getElementById("area").value;
		var officeId = document.getElementById("officeId").value;
			if (area!=""&&officeId==""){
				top.$.jBox.tip("请选择网点");
				return false;
			} else {
				return true;
			}
	}
	
	function resetAll(){
		$("#companyName").val("");
		$("#apply").val("");
		$("#organizationNumber").val("");
		$("#contactName").val("");
		$("#conCertNumber").val("");
	}
	
	
	function resetAllTwo(){
		$("#companyName").val("");
		$("#apply").val("");
		$("#organizationNumber").val("");
		$("#contactName").val("");
		$("#conCertNumber").val("");
		
		$("#area").val("");
		$("#officeId").val("");
		$("#certType").val("");
		$("#workType").val("");
		$("#keySn").val("");
		$("#createByname").val("");
		$("#updateByname").val("");
		$("#zhizhengname").val("");
		$("#payType").val("0");
		$("#s_province").val("");
		$("#s_city").val("");
		$("#s_county").val("");
		$("#luruStartTime").val("");
		$("#luruEndTime").val("");
		$("#daoqiStartTime").val("");
		$("#daoqiEndTime").val("");
		$("#jianzhengStartTime").val("");
		$("#jianzhengEndTime").val("");
		$("#zhizhengStartTime").val("");
		$("#zhizhengEndTime").val("");
		$("#year").val("");
		$("#payMethod").val("0");
		
	}
	
	
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
</head>
<body onload=''>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/businessQuery">业务查询列表</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/businessQuery" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>按应用查询：</label><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>代办应用：</label> 
			<select name="apply"
				id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
			
		</div>
		<br>
		<div>
			<label>按证书信息查询：</label><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>单位名称：</label> 
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName"/>
			<label>组代码号：</label> 
			<form:input path="workCompany.organizationNumber" htmlEscape="false"
				maxlength="30" class="input-medium" id="organizationNumber"/>
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;
			<label>证书持有人：</label> 
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName"/>
			<label>身份证号：</label> 
			<form:input path="workUser.conCertNumber" htmlEscape="false"
				maxlength="18" class="input-medium" id="conCertNumber"/>
			<input id="btnSubmit" class="btn btn-primary" onclick="return onSubmit()" type="submit"
				value="查询" />
				<input id="resetTOP" type="button" class="btn btn-primary" onclick="javascript:resetAll()" value="重置"/>
				<input id="gjcx" style="text-align:center" class="btn btn-info" onclick="show()" type="button" value="高级">
				<input id="exportZS" style="text-align:center" class="btn btn-info" onclick="dcZS()" type="button" value="导出">
				<span id="tjjg">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;统计结果${page.count}条</span>
				<br />
		</div>
		<br>
		
			
		<br>
		<div id="advanced" style="display:none">
		<div>
			<label>按受理信息查询：</label><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>受理区域：</label><select name="area" id="area"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==area}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> 
			<label>受理网点：</label>  <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			<label>产品名称：</label> <select name="certType" id="certType">
				<option value="">请选择</option>
				<c:forEach items="${certTypes}" var="type" varStatus="status">
					<option value="${type.id}"
						<c:if test="${type.id==certType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
			<label>KEY编码：</label> <form:input path="keySn" htmlEscape="false"
				maxlength="30" class="input-medium" id="keySn"/>
				<!-- 录入人、鉴证人、制证人 现在由于业务中心未改造完。所以只能查两个字段 -->
			<label>录入人：</label> 
			<form:input path="inputUser.name" htmlEscape="false"
				maxlength="16" class="input-medium" id="createByname"/>
			<label>鉴证人：</label> 
			<form:input path="attestationUser.name" htmlEscape="false"
				maxlength="16" class="input-medium" id="updateByname"/>
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>制证人：</label> 
			<form:input path="businessCardUser.name" htmlEscape="false"
				maxlength="16" class="input-medium" id="zhizhengname"/>
				
				
			<label>计费策略类型：</label> 
			<form:select path="payType">
				<form:option value="0">请选择</form:option>
				<form:option value="1">标准</form:option>
				<form:option value="2">政府统一采购</form:option>
				<form:option value="3">合同采购</form:option>
			</form:select>
			<label>行政所属区：</label> 	
			<select id="s_province" name="workCompany.province"
								style="width: 100px;">
								</select>
								
								&nbsp;&nbsp; 
								<select id="s_city"
								name="workCompany.city" style="width: 100px;">
								</select>&nbsp;&nbsp; <select
								id="s_county" name="workCompany.district" style="width: 100px;">
								</select> <script
									type="text/javascript">
									_init_area();
									$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
									$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
									$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
								</script>		
		</div>
		<br>
		<div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>业务年限 ：</label> <select name="year" id="year">
				<option value="">请选择证书年限</option>
				<option value="1"
					<c:if test="${'1'==year}">
					selected="selected"
					</c:if>>1年</option>
				<option value="2"
					<c:if test="${'2'==year}">
					selected="selected"
					</c:if>>2年</option>
				<option value="4"
					<c:if test="${'4'==year}">
					selected="selected"
					</c:if>>4年</option>
				<option value="5"
					<c:if test="${'5'==year}">
					selected="selected"
					</c:if>>5年</option>
			</select> 
			<label>付款方式 ：</label> <select name="payMethod" id="payMethod">
				<option value="0"
					<c:if test="${payMethod=='0'}"> selected="selected" </c:if>>全部</option>
				<option value="1"
					<c:if test="${payMethod=='1'}"> selected="selected" </c:if>>现金</option>
				<option value="2"
					<c:if test="${payMethod=='2'}"> selected="selected" </c:if>>POS收款</option>
				<option value="3"
					<c:if test="${payMethod=='3'}"> selected="selected" </c:if>>银行转帐</option>
				<option value="4"
					<c:if test="${payMethod=='4'}"> selected="selected" </c:if>>支付宝转账</option>
				<option value="5"
					<c:if test="${payMethod=='5'}"> selected="selected" </c:if>>政府统一采购</option>
				<option value="6"
					<c:if test="${payMethod=='6'}"> selected="selected" </c:if>>合同采购</option>
			</select>
			
			</div>
		<br>
		<div>
			<label>按时间查询：</label><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>录入日期：</label> 
			<input id="luruStartTime" name="luruStartTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${luruStartTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="luruEndTime" name="luruEndTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'luruStartTime\')}'});"
				value="<fmt:formatDate value="${luruEndTime}" pattern="yyyy-MM-dd"/>" />
			
		
			
			<label>鉴证日期：</label> 
			<input id="jianzhengStartTime" name="jianzhengStartTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${jianzhengStartTime}" pattern="yyyy-MM-dd"/>" />
				&nbsp;-&nbsp;
				<input id="jianzhengEndTime" name="jianzhengEndTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'jianzhengStartTime\')}'});"
				value="<fmt:formatDate value="${jianzhengEndTime}" pattern="yyyy-MM-dd"/>" />
				<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>制证日期：</label> 
			<input id="zhizhengStartTime" name="zhizhengStartTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${zhizhengStartTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="zhizhengEndTime" name="zhizhengEndTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'zhizhengStartTime\')}'});"
				value="<fmt:formatDate value="${zhizhengEndTime}" pattern="yyyy-MM-dd"/>" />
			<label>到期日期：</label> 
			 <input id="daoqiStartTime" name="daoqiStartTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${daoqiStartTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="daoqiEndTime" name="daoqiEndTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'daoqiStartTime\')}'});"
				value="<fmt:formatDate value="${daoqiEndTime}" pattern="yyyy-MM-dd"/>" /> 
				<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmit" class="btn btn-primary" onclick="return onSubmit()" type="submit"
				value="查询" />
				<input onclick="javascript:resetAllTwo()" type="button" class="btn btn-primary" value="重置"/>
				<input style="text-align:center" class="btn btn-info" onclick="hidde()" type="button" value="收起">
				<input id="exportZS" style="text-align:center" class="btn btn-info" onclick="dcZS()" type="button" value="导出">
				&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;统计结果${page.count }条
		</div>
		<br>
		
		
			<br />
		</div>

	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>业务编号</th>
				<th>别名</th>
				<th>单位名称</th>
				<th>证书持有人</th>
				<th>经办人</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>Key编码</th>
				<th>制证日期</th>
				<th>有效期</th>
				<th>到期日期</th>
				<th>业务状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
				
					<td>${workDealInfo.svn}</td>
					<td>${workDealInfo.configApp.alias}</td>
					<td>${workDealInfo.workCompany.companyName}</td>
					<td>${workDealInfo.workUser.contactName}</td>
					<td>${workDealInfo.workCertInfo.workCertApplyInfo.name}</td>
					<td>${proType[workDealInfo.configProduct.productName]}</td>
					<td>
						<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
						<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
						<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
						<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
					</td>
					<td>${workDealInfo.keySn }</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${workDealInfo.workCertInfo.signDate}"/> </td>
					<td><c:if
							test="${not empty workDealInfo.workCertInfo.notafter && not empty workDealInfo.workCertInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
						</c:if>
					</td>
					<td><fmt:formatDate
							value="${workDealInfo.notafter }"
							pattern="yyyy-MM-dd" /> </td>
					<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
					
						<td><a href="javaScript:alarmValue( ${workDealInfo.id} )">查看 </a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
