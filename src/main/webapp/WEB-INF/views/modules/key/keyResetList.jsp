<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>盘点管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyCheckLog/">复位列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyCheckLog"
		action="${ctx}/key/keyCheckLog/saveReset" method="post" >
		<tags:message content="${message}" />
		<font size="4" >仓库信息：</font>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<tbody>
				<tr>
					<td>库房名称：</td>
					<td>${statis.keyUsbKeyDepot.depotName}</td>
					<td>所在区域：</td>
					<td>${statis.keyUsbKeyDepot.office.parent.name}</td>
				</tr>
				<tr>
					<td>上期结余：</td>
					<td>${statis.totalCount}</td>
					<td>本期入库：</td>
					<td>${statis.inCount}</td>
				</tr>
				<tr>
					<td>本期出库：</td>
					<td>${statis.outCount}</td>
					<td>当前结余：</td>
					<td>${statis.totalEndCount}</td>
				</tr>
		</tbody>
	</table>
	<font size="4" >盘点信息：</font>
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>					
					<th>厂商名称</th>
					<th>Key类型名称</th>
					<th>上期结余</th>
					<th>本期入库</th>
					<th>本期出库</th>
					<th>当前结余</th>
					<th>盘点数</th>
					<th>差异说明</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${checkLogsByCheckNumbe}" var="checkLog" varStatus="status">
					<tr>
						<td>${checkLog.keyGeneralInfo.configSupplier.supplierName}
						<input type="hidden" name="checkLogId" value="${checkLog.id }" />
						</td>
						<td>${checkLog.keyGeneralInfo.name}
						</td>					
						<td>${checkLog.beforeTotal}
						</td>
						<td>${checkLog.beforeResidue}
						</td>
						<td>${checkLog.beforeOut}
						</td>
						<td>	${checkLog.afterTotal}
						</td>
						<td>	${checkLog.afterResidue}
						</td>
						<td>${checkLog.fixRemark}
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
			<tr><td colspan="8" style="text-align:center;">
				 <input id="btnSubmit"   class="btn btn-primary" type="submit" value="复位"/>
			</td></tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>
