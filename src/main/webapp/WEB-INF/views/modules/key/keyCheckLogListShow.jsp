<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>盘点管理</title>
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
	
	function addGeneralInfo(){
		var supplierId = $("#supplierId").prop('value');
		var url = "${ctx}/key/keyGeneralInfo/addGeneralInfo?supplierId=";
		 $.getJSON(url+supplierId+"&_="+new Date().getTime(),function(data){
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data,function(idx,ele){
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">"+ele.name	+"</ooption>"
			});
			$("#geneId").html(html);
		});
	}
	
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/key/keyCheckLog/">盘点管理</a></li>
		<li class="active"><a href="${ctx}/key/keyCheckLog/">库存盘点历史记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyCheckLog"
		action="${ctx}/key/keyCheckLog/checkLogShowList?depotId=${depotId }" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		
		<label>厂商名称 ：</label>
		<select name="supplierId" id="supplierId" onchange="addGeneralInfo()" >
				<option value="">请选择</option>
				<c:forEach items="${suppliers}" var="supplier">
					<option value="${supplier.id}" 
					<c:if test="${supplier.id==supplierId}">
					selected="selected"
					</c:if>
					>${supplier.supplierName}</option>
				</c:forEach>
		</select>
		<label>Key类型名称 ：</label>
		<select name="geneId" id="geneId">
				<option value="">请选择</option>
				<c:forEach items="${genes}" var="gene">
					<option value="${gene.id}" 
					<c:if test="${gene.id==geneId}">
					selected="selected"
					</c:if> 
					>${gene.name}</option>
				</c:forEach>
		</select>
		
		
		
		
		<label>盘点时间：从</label>

		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
					到
				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
		
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>盘点次数</th>
				<th>厂商名称</th>
				<th>Key类型名称</th>
				<th>上期结余</th>
				<th>本期入库</th>
				<th>本期出库</th>
				<th>当前结余</th>
				<th>盘点数</th>
				<th>差异说明</th>
				<th>操作人员</th>
				<th>盘点开始时间</th>
				<th>盘点结束时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="checkLog">
				<tr>
					<td>第${checkLog.checkNumber}次</td>
					<td>${checkLog.keyGeneralInfo.configSupplier.supplierName}</td>
					<td>${checkLog.keyGeneralInfo.name}</td>
					
					<td>${checkLog.beforeTotal}</td>
					<td>${checkLog.beforeResidue}</td>
					<td>${checkLog.beforeOut}</td>
					<td>${checkLog.afterTotal}</td>
					<td>${checkLog.afterResidue}</td>
					
					<td>${checkLog.fixRemark}</td>
					<td>${checkLog.createBy.name}</td>
					<td><fmt:formatDate value="${checkLog.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td><fmt:formatDate value="${checkLog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
