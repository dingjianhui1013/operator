<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨管理管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="assessmentForm" modelAttribute="keyAllocateApply" action="${ctx}/key/keyAllocateApply/badKeyInSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="applyId" id="applyId" value="${apply.id }"/>
		
		<c:forEach items="${applyDetials}" var="detail">
		<div class="control-group">
			<label class="control-label">坏key类型:</label>
			<div class="controls">
				<label class="control-label">
					        	${detail.badKeyGeneralInfo.name}    
								<br/>
					</label>
			</div>
		</div>
		<div class="control-group" id="reason" >
			<label class="control-label">key数量:</label>
			<div class="controls">
				<label class="control-label">${detail.badKeyNum}支</label>
			</div>
		</div>
		</c:forEach>
		<%-- <div class="control-group" id="remarks" >
			<label class="control-label">坏key数量:</label>
			<div class="controls">
				<label class="control-label"><c:forEach items="${applyDetials}" var="detail">
					        	${detail.badKeyNum}
								
					</c:forEach></label>
			</div>
		</div> --%>
		<div class="control-group" id="reason" >
			<label class="control-label">入库原因:</label>
			<div class="controls">
				<label class="control-label">坏key入库</label>
			</div>
		</div>
		<div class="control-group" id="reason" >
			<label class="control-label">接收库房:</label>
			<div class="controls">
				<label class="control-label">${depot.depotName}</label>
			</div>
		</div>
		<div class="control-group" id="reason" >
			<label class="control-label">发货库房名称:</label>
			<div class="controls">
				<label class="control-label">${apply.keyUsbKeyDepot.depotName}</label>
			</div>
		</div>
	</form:form>
</body>
</html>
