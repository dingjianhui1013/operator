<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息发送管理</title>
<meta name="decorator" content="default" />
<style type="text/css">
/* .Wdate{width:113px;} */
</style>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
	
	});
	function selectedAll()
	{
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		var index=0;
		for (var a = 0; a < xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == false) {
				index++;
			}
		}
		if(index==0)
			{
				$("#checkAll").attr("checked","true");
			}
	}
	
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function addOffice() {
		var areaId = $("#areaId").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId + "&_=" + new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#officeId").html(html);
		});
	}
	
	//选择当前页面全部
	function checkAll(obj) {
		var check = $($("#contentTable").find("#checkAll"));
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (check.is(":checked") == true) {
			$("#checkAllVal").val("1");
			$('input:checkbox').each(function() {
				$(this).attr('checked', true);
			});
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == true) {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne) < 0) {
						if (checkIds == '') {
							checkIds += check.val();
						} else {
							checkIds += "," + check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds == ",") {
				$("#checkIds").val("");
			} else {
				$("#checkIds").val(checkIds);
			}
		} else {
			$("#checkAllVal").val("0");
			$('input:checkbox').each(function() {
				$(this).attr('checked', false);
			});
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == false) {
					checkIds = checkIds.replace(check.val(), "");
					checkIds = checkIds.replace(",,", ",");
				}
			}
			if (checkIds == ",") {
				$("#checkIds").val("");
			} else {
				$("#checkIds").val(checkIds);
			}
		}
	}

	//单条数据选择
	function changeCheck(obj) {
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (checkIds.indexOf($(obj).val()) > -1) {
			checkIds = checkIds.replace($(obj).val(), "");
		}
		for (var a = 0; a < xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == true) {
				var checkOne = check.val();
				if (checkIds.indexOf(checkOne) < 0) {
					if (checkIds == '') {
						checkIds += check.val();
					} else {
						checkIds += "," + check.val();
					}
				}
			}
		}
		checkIds = checkIds.replace(",,", ",");
		if (checkIds == ",") {
			$("#checkIds").val("");
		} else {
			$("#checkIds").val(checkIds);
		}
	}
	
	//选择全部数据
	function selectData()
	{
		if($("#checkIds").val()==''||$("#checkIds").val()==null){
			var apply = $("#apply").val();
			var companyName = $("#companyName").val();
			var contactName = $("#contactName").val();
			var workType = $("#workType").val();
			var dealInfoStatus = $("#dealInfoStatus").val();
			var areaId = $("#areaId").val();
			var officeId = $("#officeId").val();
			var province = $("#s_province").val();
			var city = $("#s_city").val();
			var district = $("#s_county").val();
			$("#send").removeAttr("href");
			var url="${ctx}/message/messageSending/selectData";
			$.ajax({
				url:url,
				type:"POST",
				data:{"apply":apply,"companyName":companyName,"contactName":contactName,"workType":workType,
						"dealInfoStatus":dealInfoStatus,"areaId":areaId,"officeId":officeId,
						"province":province,"city":city,"district":district,_:new Date().getTime()},
				dataType:"text",
				success:function(data)
				{
					$("#checkIds").val(data);
					var xz = $("#contentTable").find("[name='oneDealCheck']");
					for (var a = 0; a < xz.length; a++) {
						var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
						if (check.is(":checked") == false) {
							check.attr("checked","true");
						}
					}
					$("#send").attr("href","javascript:send()");
					$("#checkAll").attr("checked","true");
				}
			});
				$("#btnSubmitAll").val("取消");
		}else{
			$("#checkIds").val("");
			var xz = $("#contentTable").find("[name='oneDealCheck']");
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
					check.attr("checked",false);
			}
			$("#checkAll").attr("checked",false);
			$("#btnSubmitAll").val("全选");
		}
	}
	
	//发送
	function send() {
		var checkIds = $("#checkIds").val();
		var apply = $("#apply").val();
		var companyName = $("#companyName").val();
		var contactName = $("#contactName").val();
		var workType = $("#workType").val();
		var dealInfoStatus = $("#dealInfoStatus").val();
		var areaId = $("#areaId").val();
		var officeId = $("#officeId").val();
		var smsId = $("#smsId").val();
		if (checkIds == "") {
			//$.jBox.tip("请选择需要发送信息的公司！");
			$.jBox.tip("请选择发送数据！");
		}
		if (checkIds != ""&&smsId == "") {
				$.jBox.tip("请选择短信模板！");
			}
		if(smsId!="" && checkIds !="")
				{
				var url = "${ctx}/message/messageSending/send?checkIds=" + checkIds
				+ "&apply=" + apply + "&companyName=" + companyName
				+ "&contactName=" + contactName + "&workType=" + workType
				+ "&dealInfoStatus=" + dealInfoStatus + "&areaId=" + areaId
				+ "&officeId=" + officeId + "&smsId=" + smsId + "&_="
				+ new Date().getTime();
		var submit = function(v, h, f) {
			if (v == true) {
				$.getJSON(url, function(data) {
					if (data.status == '1') {
						var html = "";
						html += "<label class='control-label'>总共条数:</label><div class='controls'>"
								+ data.size+ "</div>";
						html += "<label class='control-label'>发送成功条数:</label><div class='controls'>"
							+ data.courentSize+ "</div>";
						html += "<label class='control-label'>发送失败条数:</label><div class='controls'>"
							+ data.errorSize+ "</div>";
							if( data.errorSize>0){
								html += "<div class='controls' id = \"reasonButton\"><input type = \"button\" value =\"查看原因\" onclick = \"checkWhy()\"></div>";	
								html += "<div style =\"display:none\" id = \"errorReason\"><label class='control-label'>错误原因:</label>";
								$.each(data.errorList,function(idx, ele) {
									html += "<div class='controls'>"+ele.meg+ "</div>";
									
								});
								html += "<div class='controls'><input type = \"button\" value =\"隐藏原因\" onclick = \"hideWhy()\"></div></div>";
							}
							$("#sendMessage").append(html);
							var sendHtml = $("#sendMessage").html();
						if(data.errorSize>0){
							top.$.jBox.confirm(sendHtml, "短信内容", submit, {
								buttons : {
									'返回' : false
								}
							});		
						}else{
							top.$.jBox.tip("发送完成");
							setTimeout(function() {
								$("#searchForm").submit();

								window.location.href="${ctx}/message/messageSending/";
							}, 1500); 
						}
					} else if (data.status == '-1') {
						top.$.jBox.tip("发送失败!");
						var info = "失败信息:<br>" + data.msg;
						top.$.jBox.info(info);
				
					} else {
						top.$.jBox.tip("上传失败!");
						var info = "失败信息:<br>" + data.msg;
						top.$.jBox.info(info);
					
					}

				});

			}

		};
		
		var updateUrl = "${ctx}/message/messageSending/checkone?checkIds="
				+ checkIds + "&apply=" + apply + "&companyName=" + companyName
				+ "&contactName=" + contactName + "&workType=" + workType
				+ "&dealInfoStatus=" + dealInfoStatus + "&areaId=" + areaId
				+ "&officeId=" + officeId + "&smsId=" + smsId + "&_="
				+ new Date().getTime();

		$.getJSON(	updateUrl,
						function(data) {
							var html = "";
							html += "<label class='control-label'>总共条数:</label><div class='controls'>"
									+ data.size+ "</div>";
							html += "<label class='control-label'>正确条数:</label><div class='controls'>"
								+ data.courentSize+ "</div>";
							if(data.courentSize>0){
								html += "<label class='control-label'>短信内容:</label><div class='controls'>"
									+ data.content + "</div>";	
							}			
							html += "<label class='control-label'>错误条数:</label><div class='controls'>"
								+ data.errorSize+ "</div>";
								if( data.errorSize>0){
									html += "<div class='controls' id = \"reasonButton\"><input type = \"button\" value =\"查看原因\" onclick = \"checkWhy()\"></div>";	
									html += "<div style =\"display:none\" id = \"errorReason\"><label class='control-label'>错误原因:</label>";
									$.each(data.errorList,function(idx, ele) {
										html += "<div class='controls'>"+ele.meg+ "</div>";
										
									});
									html += "<div class='controls'><input type = \"button\" value =\"隐藏原因\" onclick = \"hideWhy()\"></div></div>";
								}
								$("#sendMessage").append(html);
								var sendHtml = $("#sendMessage").html();
						if(data.courentSize>0){
							top.$.jBox.confirm(sendHtml, "短信内容", submit, {
								buttons : {
									'确认发送' : true,
									'返回' : false
								}
							});		
						}else{
							top.$.jBox.confirm(sendHtml, "短信内容", submit, {
								buttons : {
									'返回' : false
								}
							});	
						}
						});
				}
		
	}
	
	//查询提交
	function querySubmit(){
		$("#checkIds").val();
		$("#searchForm").submit();
	}
</script>
<script type="text/javascript">
// 			_init_area();
// 			$("#s_province")
// 					.append(
// 							'<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
// 			$("#s_city")
// 					.append(
// 							'<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
// 			$("#s_county")
// 					.append(
// 							'<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置</a></li>
		<li class="active"><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li><a href="${ctx}/message/messageSending/search">消息查看</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="messageSending"
		action="${ctx}/message/messageSending/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>

			<label>应用名称：</label> <select name="apply" id="apply" data-placeholder="选择应用" class="editable-select">
				<option value=""></option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select> <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName" />

			<label>证书持有人：</label>
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName" />
		</div>
		<br>
		<div>
			<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> <label>选择区域：</label> <select name="areaId" id="areaId"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>选择网点 ：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			
		</div>
		<br>
		<div>
		<label>业务状态：</label>
		<select name="dealInfoStatus" id="dealInfoStatus">
			<option value="">请选择业务状态</option>
			<c:forEach items="${wdiStatus}" var="type">
				<option value="${type.key}"
					<c:if test="${type.key==dealInfoStatus}">
					selected="selected"
					</c:if>>${type.value}</option>
			</c:forEach>
		</select>


		<label>短信模板：</label>
		<select name="smsId" id="smsId">
			<option value="">请选择</option>
			<c:forEach items="${smsConfigurationList}" var="sms">
				<option value="${sms.id}"
					<c:if test="${sms.id==smsId}">
					selected="selected"
					</c:if>>${sms.messageName}</option>
			</c:forEach>
		</select>
		<label>行政所属区 ：</label>
		<select id="s_province" name="workCompany.province" style="width: 100px;">
		</select>
		<select id="s_city" name="workCompany.city" style="width: 100px;">
		</select>
		<select id="s_county" name="workCompany.district" style="width: 100px;">
		</select> 
		<script	type="text/javascript">
			_init_area();
			$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
			$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
			$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
		</script>
		
		</div>
		
		<div style="margin-top: 8px">
		
		<label>制证时间：</label> 
		
		<input id="makeCertStart" name="makeCertStart"	type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" value="<fmt:formatDate value="${makeCertStart}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="makeCertEnd" name="makeCertEnd" type="text"  readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'makeCertStart\')}'});"
				value="<fmt:formatDate value="${makeCertEnd}" pattern="yyyy-MM-dd"/>" />
		
		
		<label>到期时间：</label> 
		
		<input id="expiredStart" name="expiredStart"	type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" value="<fmt:formatDate value="${expiredStart}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
			<input id="expiredEnd" name="expiredEnd" type="text"  readonly="readonly" maxlength="20" class="input-medium Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'expiredStart\')}'});"
				value="<fmt:formatDate value="${expiredEnd}" pattern="yyyy-MM-dd"/>" />
			
		&nbsp; &nbsp; &nbsp; &nbsp; 
		
		<input id="btnSubmit" class="btn btn-primary" type="button" onclick = "querySubmit()"
			value="查询" />
		<a href="javascript:send()" class="btn btn-primary" id="send">发送</a> <input
			type="hidden" name="checkIds" id="checkIds" value="${checkIds }" />
			<input id="btnSubmitAll" class="btn btn-primary" type="button" onclick="selectData()"
				value="全选" />
			
		</div>
	
	</form:form>
	<tags:message content="${message}" />
	<script type="text/javascript">
	$(function(){
	    $('.editable-select').chosen();
	});
	</script>
	
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" name="checkAll"
					 value="" onchange="checkAll(this)" /></th>
				<th>序号</th>
				<th>应用名称</th>
				<th>单位名称</th>
				<th>经办人名称</th>
				<th>证书持有人电话</th>
				<th>经办人邮箱</th>
				<th>到期时间</th>

			</tr>
		</thead>
		<tbody>
			<c:set var="zje" value="0" />
			<c:set var="count" value="0" />
			<c:forEach items="${page.list}" var="messageSend" varStatus="status">
			<c:set var="count" value="${count+1 }" />
				<tr>
					<td><c:if test="${messageSend.workUser.contactPhone!=null}">
							<input type="checkbox" name="oneDealCheck"
								value="${messageSend.id}"
								<c:forEach items="${ids }" var="id">
							<c:if test="${id==messageSend.id }"> checked="checked"</c:if>
						</c:forEach>
								onchange="changeCheck(this)" id="oneDealCheck" />
						</c:if></td>

					<td>${status.index+1 }</td>
					<td>${messageSend.configApp.alias}</td>
					<td width="30%">${messageSend.workCompany.companyName}</td>
					<td>${messageSend.workCertInfo.workCertApplyInfo.name}</td>
					<td>${messageSend.workUser.contactPhone}</td>
					<td>${messageSend.workCertInfo.workCertApplyInfo.email}</td>
					<td><fmt:formatDate value="${messageSend.notafter }"
							pattern="yyyy-MM-dd" /></td>
				</tr>
			</c:forEach>
			<c:if test="${count>0}">
				<script	type="text/javascript">
						
						selectedAll();
				</script>
			</c:if>
			
			<shiro:hasPermission name="settle:settleKey:edit">
				<td></td>
			</shiro:hasPermission>

		</tbody>
	</table>
<div style = "display:none">
<div id = "sendMessage">
<script type="text/javascript">
	function checkWhy(){
		$("#errorReason").show();
		$("#reasonButton").hide();
	}
	function hideWhy(){
		$("#errorReason").hide();
		$("#reasonButton").show();
	}
		
	</script>
</div>
</div>
	<div class="pagination">${page}</div>
</body>
</html>
