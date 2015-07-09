<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate(
					{
						submitHandler : function(form) {
							loading('正在提交，请稍等...');
							form.submit();
						},
					});
		});
		function page(n,s){
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
		
		function showTable_f(data){
			$("#showTable_officeId").attr("value",$("#officeId").val());
			$("#showTable_agentId").attr("value",data);
			$("#showTable_startTime").attr("value",$("#startTime").val());
			$("#showTable_endTime").attr("value",$("#endTime").val());
			document.showTable.submit();
		}
	</script>
 <script type="text/javascript">
	function showInfo(appId,productId,dealInfoType){
		var url = "${ctx}/settle/agentSettle/showDealInfo?appId="+appId+"&productId="+productId+"&dealInfoType="+dealInfoType+"&startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val()+"&agentName="+$("#agent").val()+"&type=LW";
		url = encodeURI(url);
		url = encodeURI(url);
		top.$.jBox.open("iframe:" + url, "业务明细", 800, 420, {
			buttons : {
				"确定" : "ok",
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
		<li ><a href="${ctx}/settle/agentSettle/list">市场推广</a></li>
		<li class="active"><a href="${ctx}/settle/agentSettle/showT">劳务关系</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configCommercialAgent" action="${ctx}/settle/agentSettle/showT" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
		<label>选择区域 ：</label>
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
		<label>代理商名称 ：</label><select name="agentId" >
				<c:forEach items="${ConfigCommercialAgents }" var="agent">
					<option value="${agent.key }" <c:if test="${agentId eq agent.key} ">checked</c:if>>${agent.value }</option>
				</c:forEach>
		</select>
		</div>
		<div style="margin-top: 8px">
		<label>统计时间 ：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	 <div class="form-horizontal" <c:if test="${empty listSum }">style="display: none"</c:if>>
			<table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
					<th colspan="9">${title }</th>
				</tr>
			</thead>
			<tr>
				<th rowspan="2">应用名称</th>
				<th rowspan="2">产品名称</th>
				<th colspan="7">业务类型及数量</th>
			</tr>
			<tr>
				<th>新增业务数量</th>
				<th>更新业务数量</th>
				<th>遗失补办数量</th>
				<th>损坏更换数量</th>
				<th>信息变更数量</th>
				<th>证书吊销数量</th>
				<!-- <th>电子签章数量</th> -->
			</tr>
			<c:set var="addSum" value="0"/>
			<c:set var="updateSum" value="0"/>
			<c:set var="lostSum" value="0"/>
			<c:set var="reissueSum" value="0"/>
			<c:set var="changeSum" value="0"/>
			<c:set var="revokeSum" value="0"/>
			<%-- <c:set var="sealSum" value="0"/> --%>
			<c:forEach items="${listSum }" var="item">
			<c:set var = "sum" value="${item.value }"></c:set>
			<tr>
				<th>${sum.configApp.appName }</th>
				<th>${pro[sum.configProduct.productName]}${workDealInfo.configProduct.productLabel==0? "(通用)":"(专用)"}</th>
				<%-- <th>${startDate}</th> --%>
				<td><c:if test='${sum.addSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},0)">${sum.addSum }</a></c:if><c:if test='${sum.addSum==0 }'>${sum.addSum }</c:if></td>
				<td><c:if test='${sum.updateSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},1)">${sum.updateSum }</a></c:if><c:if test='${sum.updateSum==0 }'>${sum.updateSum }</c:if></td>
				<td><c:if test='${sum.lostSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},2)">${sum.lostSum }</a></c:if><c:if test='${sum.lostSum==0 }'>${sum.lostSum }</c:if></td>
				<td><c:if test='${sum.reissueSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},3)">${sum.reissueSum }</a></c:if><c:if test='${sum.reissueSum==0 }'>${sum.reissueSum }</c:if></td>
				<td><c:if test='${sum.changeSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},4)">${sum.changeSum }</a></c:if><c:if test='${sum.changeSum==0 }'>${sum.changeSum }</c:if></td>
				<td><c:if test='${sum.revokeSum>0 }'><a href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},5)">${sum.revokeSum }</a></c:if><c:if test='${sum.revokeSum==0 }'>${sum.revokeSum }</c:if></td>
				<%-- <td><a <c:if test='${sum.sealSum>0 }'>href="javascript:showInfo(${sum.configApp.id },${sum.configProduct.id},6)"</c:if>>${sum.sealSum }</a></td> --%>
			</tr>
			<c:set var="addSum" value="${addSum+sum.addSum }"/>
			<c:set var="updateSum" value="${updateSum+sum.updateSum }"/>
			<c:set var="lostSum" value="${lostSum+sum.lostSum }"/>
			<c:set var="reissueSum" value="${reissueSum+sum.reissueSum }"/>
			<c:set var="changeSum" value="${changeSum+sum.changeSum }"/>
			<c:set var="revokeSum" value="${revokeSum+sum.revokeSum }"/>
			<%-- <c:set var="sealSum" value="${sealSum+sum.sealSum }"/> --%>
			</c:forEach>
			
			<tr>
				<th>合计</th>
				<td></td>
				<td>${addSum }</td>
				<td>${updateSum }</td>
				<td>${lostSum }</td>
				<td>${reissueSum }</td>
				<td>${changeSum }</td>
				<td>${revokeSum }</td>
				<%-- <td>${sealSum }</td> --%>
			</tr>
			<tr>
				<th>共计</th>
				<td colspan="9">${addSum+updateSum+lostSum+reissueSum+changeSum+revokeSum }</td>
			</tr>
		</table>
		<input type="hidden" value="${startTime }" id="startDate">
		<input type="hidden" value="${endTime }" id="endDate">		
		<input type="hidden" value="${ConfigCommercialAgents.get(agentId) }" id="agent">
		</div>
</body>
</html>
