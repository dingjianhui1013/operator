<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章业务管理</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	function exportCollect(){
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		
		window.open("${ctx}/report/businessReport/exportCollect?startTime="+startTime+"&endTime="+endTime);
	}
	
</script>



</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/report/businessReport/listByDate">证书发放汇总列表</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/report/businessReport/listByDate" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<div>

			&nbsp;&nbsp;<label>时间范围：&nbsp;</label> &nbsp;&nbsp;<input
				class="input-medium Wdate" type="text" required="required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>"
				maxlength="20" readonly="readonly" name="startTime" id="startTime" />
			至 <input class="input-medium Wdate" type="text" required="required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>"
				maxlength="20" readonly="readonly" name="endTime" id="endTime"/>

			&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input  style="text-align:center" class="btn btn-info" onclick="exportCollect()" type="button" value="导出">
				
			<label style="float: right"><b>注:点击蓝色数字可查看明细</b></label>	
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>应用名称</th>
				<th>有效数量</th>
				<th>新增数量</th>
				<th>已更新数量</th>
				<th>未更新数量</th>
				<th>维护数量</th>
		<tbody>
			<c:forEach items="${list}" var="dealInfoVO">
				<tr>
					<td>${dealInfoVO.appName }</td>
					<td><c:if test="${dealInfoVO.validCount ne 0}">
							<a style="width:100px;height:20px;display:block;"
								href="${ctx}/report/businessReport/listDetailDealInfo?appId=${dealInfoVO.appId }&&startTime=${start}&&endTime=${end}&&method=0">${dealInfoVO.validCount }</a>
						</c:if> <c:if test="${dealInfoVO.validCount eq 0}">${dealInfoVO.validCount }</c:if></td>

					<td><c:if test="${dealInfoVO.newCount ne 0}">
							<a  style="width:100px;height:20px;display:block;" href="${ctx}/report/businessReport/listDetailDealInfo?appId=${dealInfoVO.appId }&&startTime=${start}&&endTime=${end}&&method=1">${dealInfoVO.newCount }</a>
						</c:if> <c:if test="${dealInfoVO.newCount eq 0}">${dealInfoVO.newCount }</c:if></td>

					<td><c:if test="${dealInfoVO.updateCount ne 0}">
							<a style="width:100px;height:20px;display:block;" href="${ctx}/report/businessReport/listDetailDealInfo?appId=${dealInfoVO.appId }&&startTime=${start}&&endTime=${end}&&method=2">${dealInfoVO.updateCount }</a>
						</c:if> <c:if test="${dealInfoVO.updateCount eq 0}">${dealInfoVO.updateCount }</c:if></td>

					<td><c:if test="${dealInfoVO.unUpdateCount ne 0}">
							<a style="width:100px;height:20px;display:block;" href="${ctx}/report/businessReport/listDetailDealInfo?appId=${dealInfoVO.appId }&&startTime=${start}&&endTime=${end}&&method=3">${dealInfoVO.unUpdateCount }</a>
						</c:if> <c:if test="${dealInfoVO.unUpdateCount eq 0}">${dealInfoVO.unUpdateCount }</c:if></td>

					<td><c:if test="${dealInfoVO.maintenanceCount ne 0}">
							<a style="width:100px;height:20px;display:block;" href="${ctx}/report/businessReport/listDetailDealInfo?appId=${dealInfoVO.appId }&&startTime=${start}&&endTime=${end}&&method=4">${dealInfoVO.maintenanceCount }</a>
						</c:if> <c:if test="${dealInfoVO.maintenanceCount eq 0}">${dealInfoVO.maintenanceCount }</c:if></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
	
	
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">有效数量: <b>用户证书状态有效数量</b></span>
			<span class="span4">已更新数量: <b>办理的更新业务数量</b></span>
			<span class="span4">维护数量: <b>办理的除新增,更新外的其他业务数量</b></span>
		</div>
		<div class="row-fluid span8">
			<span class="span4">新增数量: <b>办理的新增业务数量</b></span>
			
			<span class="span4">未更新数量: <b>办理的更新业务,开权限未制证数量</b></span>
		</div>
	</div>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>


</body>
</html>
