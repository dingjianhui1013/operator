<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目发证明细管理</title>
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
	
		function dca() {
			if ($("#alias").val() == "") {
				top.$.jBox.tip("请选择项目");
				return false;
			} else {
				if ($("#startTime").val() == "" || $("#endTime").val() == "") {
					top.$.jBox.tip("请选定时间范围");
				} else {
					var alias = $("#alias").val();
					var startTime = $("#startTime").val();
					var endTime = $("#endTime").val();

					window.location.href = "${ctx }/settle/projectCertificationDetails/export?alias="
							+ alias
							+ "&startTime="
							+ startTime
							+ "&endTime="
							+ endTime;
				}
			}
		}
		function alarmValue(dealInfoId) {
			var url = "${ctx}/work/workDealInfo/showWorkDeal?dealInfoId="
					+ dealInfoId;
			top.$.jBox.open("iframe:" + url, "查看", 550, 480, {
				buttons : {
					"关闭" : true
				},
				submit : function(v, h, f) {

				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settle/projectCertificationDetails/">项目发证明细列表</a></li>
		<shiro:hasPermission name="settle:projectCertificationDetails:edit"><li><a href="${ctx}/settle/projectCertificationDetails/form">项目发证明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="projectCertificationDetails" action="${ctx}/settle/projectCertificationDetails/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>项目名称：</label> 
			<select name="alias"
				id="alias">
				<option value="">请选择项目</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==alias}">
					selected="selected"
					</c:if>>${app.alias}</option>
				</c:forEach>
			</select>
			<label>项目时间 ：</label> 
		<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:dca()" class="btn btn-primary">导出</a>
			</div>
	</form:form>
	<tags:message content="${message}"/>
			<c:if test="${projectcount!=null }">
		<table id="projectcountTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
				<tr>
					<td rowspan=2>新增：</td>
					<td>个人证书 一年期证书${projectcount.addPersonalYearCertificate}张</td>
					<td>个人证书 二年期证书 ${projectcount.addPersonalTwoYearCertificate}张</td>
					<td>个人证书 四年期证书 ${projectcount.addPersonalFourYearCertificate}张</td>
				</tr>
				<tr>
					
					<td>企业证书 一年期证书${projectcount.addcompanyYearCertificate}张</td>
					<td>企业证书 二年期证书 ${projectcount.addcompanyTwoYearCertificate}张</td>
					<td>企业证书 四年期证书 ${projectcount.addcompanyFourYearCertificate}张</td>
				</tr>
				<tr>
					<td rowspan=2>更新：</td>
					<td>个人证书 一年期证书${projectcount.updatePersonalYearCertificate}张</td>
					<td>个人证书 二年期证书 ${projectcount.updatePersonalTwoYearCertificate}张</td>
					<td>个人证书 四年期证书 ${projectcount.updatePersonalFourYearCertificate}张</td>
				</tr>
				<tr>
					
					<td>企业证书 一年期证书${projectcount.updatecompanyYearCertificate}张</td>
					<td>企业证书 二年期证书 ${projectcount.updatecompanyTwoYearCertificate}张</td>
					<td>企业证书 四年期证书 ${projectcount.updateFourYearCertificate}张</td>
				</tr>
				<tr>
					<td colspan=2>遗失补办：证书${projectcount.lostCerate}张</td>
					<td colspan=2>损坏更换：证书${projectcount.damageCertificate}张</td>
					
				</tr>
		</thead>
		
	</table>
	</c:if>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>ID</th>
				<th>单位名称</th>
				<th>业务类型</th>
				<th>证书类型</th>
				<th>证书有效期</th>
				<th>制证时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		 <c:set var="zje"  value="0"/>  
			<c:forEach items="${page.list}" var="workDealInfo" varStatus="status">
			
				<tr>
				<td>${status.index+1 }</td>
				<td>${workDealInfo.workCompany.companyName}</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>
					<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
					<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
					<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
					<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
				</td>
				<td><c:if
							test="${not empty workDealInfo.workCertInfo.notafter && not empty workDealInfo.workCertInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
					</c:if></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${workDealInfo.workCertInfo.signDate}"/></td>
				<td><a href="javaScript:alarmValue( ${workDealInfo.id} )">查看 </a></td>
				</tr>
				
			</c:forEach>
				<shiro:hasPermission name="settle:settleKey:edit"><td>
				</td></shiro:hasPermission>
				
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
