<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用管理</title>
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
		
		function addOffice() {
			var areaId = $("#areaId").prop('value');
			var url = "${ctx}/sys/office/addOffices?areaId=";
			$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				//console.log(data);
				html +="<option value=''>请选择</option>";
				$.each(data, function(idx, ele) {
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</option>"
				});
				//alert(html);
				$("#officeId").html(html);
			});
		}
		
		function products() {
			var congifApplyId = $("#congifApplyId").prop('value');
			var url = "${ctx}/settle/agentSettle/getProductName?applyId=";
			$.getJSON(url + congifApplyId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				html +="<option value=''>请选择</option>";
				$.each(data, function(idx, ele) {
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</option>";
				});
				//alert(html);
				$("#productId").html(html);
			});
		}

		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a
			href="${ctx}/profile/configChargeAgent/getChargeAgentList">计费策略模板列表</a></li>
		<li class="active"><a
			href="${ctx}/profile/configChargeAgent/showBoundDealInfoDetails?agentId=${agentId }">使用记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ConfigAgentBoundDealInfo" action="${ctx}/profile/configChargeAgent/showBoundDealInfoDetails?agentId=${agentId }" method="post" class="breadcrumb form-search">
			<div class="control-group">
			<label>选择区域 ：</label> <select name="areaId" id="areaId"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> 
			<label>选择网点 ：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="office">
					<option value="${office.id}"
						<c:if test="${office.id==officeId}">
						selected="selected"
						</c:if>>${office.name}</option>
				</c:forEach>
			</select> 
	</div>
	<div class="control-group">
			 <label>应用名称 ：</label> <select name="congifApplyId" id="congifApplyId" onchange="products()">
				<option value="">请选择</option>
				<c:forEach items="${configApps}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==congifApplyId}">
						selected="selected"
						</c:if>>${app.appName}</option>
				</c:forEach>
		 </select>  
		   <label>产品名称 ：</label> <select name="productId" id="productId" >
				<option value="">请选择</option>
				<c:forEach items="${configProducts}" var="pro">
					<option value="${pro.id}"
						<c:if test="${pro.id==productId}">
						selected="selected"
						</c:if>>
					   ${productType[pro.productName]}${workDealInfo.configProduct.productLabel==0? "(通用)":"(专用)"}
						</option>
				</c:forEach>
			</select> 
	</div>
	<div class="control-group">
			<label>&nbsp;&nbsp;&nbsp;办理人 ：</label> <input type="text" name="handle" value="${handle }"/>
			<label>办理时间 ：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate" required="required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;
				
				<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
				
				<%-- <input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" /> --%>
				
				
				
		&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		</div>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
					<th>序号</th>
					<th>模版名称</th>
					<th>办理人</th>
					<th>办理时间</th>
					<th>办理网点</th>
					<th>业务编号</th>
					<th>应用名称</th>
					<th>产品名称</th>
					<th>单位名称</th>
					<th>业务类型</th>
					<th>业务状态</th>
								
					
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="boundDealInfo" varStatus="status">
		
			<tr>
			
				<td>${status.index + 1}</td>
				<td>${boundDealInfo.agent.tempName}</td>
				<td>${boundDealInfo.createBy.name}</td>
				<td>
				<fmt:formatDate value="${boundDealInfo.createDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${boundOffices[status.count-1].name}
				</td>
				<td>${boundDealInfo.dealInfo.svn}</td>
				<td>${boundDealInfo.dealInfo.configApp.appName}</td>
				<td>${proType[boundDealInfo.dealInfo.configProduct.productName]}</td>
				<td>${boundDealInfo.dealInfo.workCompany.companyName}</td>
				<td>
				
						<c:if test="${boundDealInfo.dealInfo.dealInfoType!=null}">${wdiType[boundDealInfo.dealInfo.dealInfoType]}</c:if>
						<c:if test="${boundDealInfo.dealInfo.dealInfoType1!=null}">${wdiType[boundDealInfo.dealInfo.dealInfoType1]}</c:if>
						<c:if test="${boundDealInfo.dealInfo.dealInfoType2!=null}">${wdiType[boundDealInfo.dealInfo.dealInfoType2]}</c:if>
						<c:if test="${boundDealInfo.dealInfo.dealInfoType3!=null}">${wdiType[boundDealInfo.dealInfo.dealInfoType3]}</c:if>
				
				</td>
				
				
				
				<td>${wdiStatus[boundDealInfo.dealInfo.dealInfoStatus]}</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
