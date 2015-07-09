<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书更新统计</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler : function(form) {
				//loading('正在提交，请稍等...');
				form.submit();
			},
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
		<li class="active"><a href="${ctx}/work/workDealInfo/certCount">证书更新率统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/certUpdateCount" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-horizontal">
			<label>统计日期：</label> <input id="date" name="date" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${countDate}" pattern="yyyy-MM-dd"/>" />
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="证书更新率统计" />
		</div>
	</form:form>
	<div class="form-horizontal">
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="9">证书更新统计</th>
				</tr>
			</thead>
			<tr>
				<th colspan="1"></th>
				<th colspan="2">证书到期</th>
				<th colspan="1">证书更新率（当年累计）</th>
				<th colspan="2">证书过期</th>
				<th colspan="3">历史证书更新率</th>
			</tr>
			<tr>
				<th>项目</th>
				<th>3月</th>
				<th>1月</th>
				<th></th>
				<th>1月</th>
				<th>3月</th>
				<th>${oneYear}年</th>
				<th>${twoYear}年</th>
				<th>${threeyear}年</th>
			</tr>
			<c:forEach items="${certListSum}" var="certCount">
				<tr>
					<th>${certCount.configAppName}</th>
					<th>${certCount.aThreeMoth}</th>
					<th>${certCount.aOneMoth }</th>
						<th>${certCount.probability }</th>
					<th>${certCount.bOneMoth }</th>
					<th>${certCount.bThreeMoth}</th>
					<th>${certCount.probabilityOne }</th>
					<th>${certCount.probabilityTwo }</th>
					<th>${certCount.probabilityThree }</th> 
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
