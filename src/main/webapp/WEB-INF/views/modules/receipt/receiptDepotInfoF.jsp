<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>出库信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/receipt/receiptDepotInfo/list">库房管理</a></li>
		<li class="active"><a href="#">添加出库信息 </a></li>
	</ul>
	<form action = "" id = "searchForm"  method="post" class="breadcrumb form-search">
		<input id="id" name="id" type="hidden" value=""/>
	</form>
	<form:form id="inputForm" modelAttribute="receiptInvoice" action="${ctx}/receipt/receiptDepotInfo/saveZong?id=1" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">发票类型名称:</label>
			<div class="controls">					
					<select name="type.typeName" id="receiptType"  class="required" >
					<c:if test="${typeSize==0 }">
						<option value="">库存中没有添加发票类型产品</option>
					</c:if>
					<c:if test="${typeSize!=0 }">
					<option value="">请选择</option>
					<c:forEach items="${receiptType}" var="receipType">
						<option value="${receipType.typeName}">${receipType.typeName}元</option>
					</c:forEach>
					</c:if>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库数量:</label>
			<div class="controls">
			<form:input path="count" maxlength="9" class="required" 
				 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
				 onkeyup="value=value.replace(/[^\d]/g,'') "
				/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库原因:</label>
			<div class="controls">
					<select name="receiptType" id="outReason"  class="required" >
					<option value="">请选择</option>
					<option value="0">销售出库</option>
					<option value="1">调拨出库</option>
					<option value="2">盘底异常出库</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出库时间:</label>
			<div class="controls">
				${date}
				<input type="hidden"  name="createDate" id="time" value="${date}">
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyUsbKey:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
