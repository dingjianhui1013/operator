<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息发送管理</title>
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
	function addOffice(){
		var areaId = $("#areaId").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		 $.getJSON(url+areaId+"&_="+new Date().getTime(),function(data){
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data,function(idx,ele){
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">"+ele.name	+"</ooption>"
			});
			
			$("#officeId").html(html);
		});
		
	 }
	function checkAll(obj){
		var check = $($("#contentTable").find("#checkAll"));
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (check.is(":checked") == true) {
			$('input:checkbox').each(function() {
		        $(this).attr('checked', true);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == true) {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne)<0) {
						if(checkIds==''){
							checkIds+=check.val();
						}else{
							checkIds+=","+check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}else{
			$('input:checkbox').each(function () {
		        $(this).attr('checked',false);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == false) {
					checkIds = checkIds.replace(check.val(), "");
					checkIds = checkIds.replace(",,", ",");
				}
			}
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}
	}
	
	
	function changeCheck(obj){
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if(checkIds.indexOf($(obj).val())>-1){
			checkIds = checkIds.replace($(obj).val(), "");
		}
		for (var a = 0; a <xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == true) {
				var checkOne = check.val();
				if (checkIds.indexOf(checkOne)<0) {
					if(checkIds==''){
						checkIds+=check.val();
					}else{
						checkIds+=","+check.val();
					}
				}
			}
		}
		checkIds = checkIds.replace(",,", ",");
		if (checkIds==",") {
			$("#checkIds").val("");
		}else{
			$("#checkIds").val(checkIds);
		}
	}
	function send(){
		var checkIds = $("#checkIds").val();
		
		if(checkIds==null || checkIds==""){
			top.$.jBox.tip("请选择需要发送信息的公司！");
		}else if($("#smsId").val()==null||$("#smsId").val()==""){
			top.$.jBox.tip("请选择短信模板！");
		}else{
			
			 var apply = $("#apply").val();
			var companyName = $("#companyName").val();
			
			var contactName = $("#contactName").val();
			var workType = $("#workType").val();
			var dealInfoStatus = $("#dealInfoStatus").val();
			var areaId = $("#areaId").val();
			var officeId = $("#officeId").val();
			var smsId = $("#smsId").val();
			window.location.href="${ctx }/message/messageSending/send?checkIds="+checkIds+"&apply="+apply+"&companyName="+companyName+"&contactName="+contactName
					+"&workType="+workType+"&dealInfoStatus="+dealInfoStatus+"&areaId="+areaId+"&officeId="+officeId+"&smsId="+smsId; 
		}
	}		
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
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

			<label>应用名称：</label> <select name="apply" id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select> <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName"/>

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>证书持有人：</label>
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName"/>
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
			</select> 
			<label>选择区域：</label>
		<select name="areaId" id="areaId" onchange="addOffice()">
			<option value="">请选择</option>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
			<select name="officeId" id="officeId">
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
		
			<label>业务状态：</label> <select
				name="dealInfoStatus" id="dealInfoStatus">
				<option value="">请选择业务类型</option>
				<c:forEach items="${wdiStatus}" var="type">
					<option value="${type.key}"
						<c:if test="${type.key==dealInfoStatus}">
					selected="selected"
					</c:if>>${type.value}</option>
				</c:forEach>
			</select>
			
		
		<label>短信模板：</label> <select name="smsId" id="smsId">
				<option value="">请选择</option>
				<c:forEach items="${smsConfigurationList}" var="sms">
					<option value="${sms.id}"
						<c:if test="${sms.id==smsId}">
					selected="selected"
					</c:if>>${sms.messageName}</option>
				</c:forEach>
			</select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:send()" class="btn btn-primary">发送</a>
			<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds }"/>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" name="oneDealCheck" value="${page.pageNo}" 
				<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
				</c:forEach>
				onchange="checkAll(this)"
				/> </th>
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
		 <c:set var="zje"  value="0"/>  
			<c:forEach items="${page.list}" var="messageSend" varStatus="status">
			
				<tr>
				<td>
					
					<c:if test="${messageSend.dealInfoStatus!=6}">
						<input type="checkbox" name="oneDealCheck" value = "${messageSend.id}" 
						<c:forEach items="${ids }" var="id">
							<c:if test="${id==messageSend.id }"> checked="checked"</c:if>
						</c:forEach>
						onchange="changeCheck(this)" id="oneDealCheck"
						 /> 
					 </c:if>
					 
					 </td>
				<td>${status.index+1 }</td>
				<td>${messageSend.configApp.alias}</td>
				<td>${messageSend.workCompany.companyName}</td>
				<td>${messageSend.workCertInfo.workCertApplyInfo.name}</td>
				<td>${messageSend.workUser.contactPhone}</td>
				<td>${messageSend.workCertInfo.workCertApplyInfo.email}</td>
				<td><fmt:formatDate value="${messageSend.notafter }" pattern="yyyy-MM-dd" /> </td>
				</tr>
				
			</c:forEach>
				<shiro:hasPermission name="settle:settleKey:edit"><td>
				</td></shiro:hasPermission>
				
		</tbody>
	</table>


	<div class="pagination">${page}</div>
</body>
</html>
