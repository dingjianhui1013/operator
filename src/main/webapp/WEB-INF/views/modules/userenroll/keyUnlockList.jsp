<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>解锁审批管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function audit(obj){
			var url = "${ctx}/userenroll/keyUnlock/audit?id="+obj+"&agree=";
			var submit = function (v, h, f) {
			    if (v == true){
					url += "1"+"&_=" + new Date().getTime();
			    }else{
			    	
			    	var msg=encodeURI(encodeURI($("#auditReason").val()));
			    	
			    	if(msg==null||msg==""){
			    		
			    		$.jBox.tip("请输入拒绝原因！");
			    	return false;	
			    	}
			    	url += "0&msg="+msg+"&_=" + new Date().getTime();
			    }
			    $.jBox.tip("操作成功","info");
			    setTimeout(function (){
			    	  $.getJSON(url,function(data){
							if(data.status==1){
								window.location.reload();
								return true;
							}		
				    	});
        		   }, 2000); 

			    
			  
			};
			var infoUrl = "${ctx}/userenroll/keyUnlock/getUnlockInfo?id="+obj+"&_="+new Date().getTime();
			$.getJSON(infoUrl,function(data){
				
				var html = "未查询到相关证书信息";
				if(data.status==1){
						html = "<div class='container-fluid'><div class='row-fluid'><div class='span6'><div class='control-group'><label class='control-label'>应用名称:</label><div class='controls'>"
								+ data.appName
								+ "</div></div>";
						html += "<div class='control-group'><label class='control-label'>产品名称:</label><div class='controls'>"
								+ data.productName
								+ "</div></div>";
						html += "<div class='control-group'><label class='control-label'>业务类型:</label><div class='controls'>"
								+ data.dealInfoType
								+ "</div></div>";	
						html += "<div class='control-group'><label class='control-label'>证书序列号:</label><div class='controls' style='white-space:normal; word-break:break-all;'>"
									+ data.sn
									+ "</div></div></div><div class='span6'>";
						html += "<div class='control-group'><label class='control-label'>key序列号:</label><div class='controls'>"
								+ data.keysn
								+ "</div></div>";
						html += "<div class='control-group'><label class='control-label'>颁发者:</label><div class='controls'>"
								+ data.issuer
								+ "</div></div>";
						html += "<div class='control-group'><label class='control-label'>主题:</label><div class='controls'>"
								+ data.subject
								+ "</div></div>";
						html += "<div class='control-group'><label class='control-label'>有效起止日期:</label><div class='controls'>"
								+ data.notbefore
								+ "至"
								+ data.notafter
								+ "</div></div></div></div></div>";
				}
				html += "<div class='control-group'><label class='control-label'>审核意见:</label><div class='controls'><textarea id='auditReason' style='width:90%' rows='3' cols='100'></textarea></div></div>";
				$.jBox.confirm(html, "key解锁审核", submit, { buttons: { '通过': true, '拒绝': false},width:600 });
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<%-- <li><a href="${ctx}/work/workDealInfoAudit/list">鉴证业务</a></li>
		<shiro:hasPermission name="userenroll:trustdevice:audit">
			<li><a href="${ctx}/work/workDealInfoAudit/listTrustCountApply">审核移动设备数量</a></li>
		</shiro:hasPermission>
		<li><a
			href="${ctx}/work/workDealInfoAudit/exceptionList?dealInfoStatus=1">异常业务</a></li> --%>
		<shiro:hasPermission name="userenroll:keyUnlock:view">
			<li class="active"><a href="${ctx}/userenroll/keyUnlock/list">KEY解锁</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUnlock"
		action="${ctx}/userenroll/keyUnlock/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>key序列号 ：</label>
		<form:input path="keySn" htmlEscape="false" maxlength="50"
			class="input-medium" />
			<label>单位名称 ：</label>
		<input type="text" name="companyString" id="companyString"   maxlength="50" value="${companyString }"
			class="input-medium" />
			
				<label>经办人 ：</label>
		<input type="text" name="attnName" id="attnName"   maxlength="50"   value="${attnName }"  
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" 
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>key序列号</th>
				<th>单位名称</th>
				<th>经办人</th>
				<th>审核状态</th>
				<shiro:hasPermission name="userenroll:keyUnlock:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyUnlock" varStatus="status">
				<tr>
					<td>${status.count }</td>
					<td>${keyUnlock.keySn}</td>
					<td>${keyUnlock.companyName}</td>
					<td>${keyUnlock.attnName}</td>
					<td><c:choose>
							<c:when test="${keyUnlock.status=='ENROLL' }">待审核</c:when>
							<c:when test="${keyUnlock.status=='FORBID' }">审核不通过</c:when>
							<c:when test="${keyUnlock.status=='ISFINISH' }">审核不通过</c:when>
							<c:otherwise>审核通过</c:otherwise>
						</c:choose></td>
					<shiro:hasPermission name="userenroll:keyUnlock:edit">
						<td><c:if test="${keyUnlock.status=='ENROLL' }">
								<a href="javascript:void(0)" onclick="audit(${keyUnlock.id})">审核</a>
							</c:if></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
