<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>key结算功能管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function dca()
		{	
			
			var supplierId = $("#supplierId").val();
			var keyId = $("#keyId").val();
			
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			alert(supplierId+":"+keyId+":"+startTime+":"+endTime);
			window.location.href="${ctx }/settle/keySettle/export?supplierId="+supplierId+"&keyId="+keyId+"&startTime="+startTime+"&endTime="+endTime;
		}
		function addGene() {
			var supplierId = $("#supplierId").prop('value');
			var url = "${ctx}/key/keyGeneralInfo/addGeneralInfo?supplierId=";
			$.getJSON(url + supplierId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data, function(idx, ele) {
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</ooption>"
				});
				$("#keyId").html(html);
			});
		}
			function peration(){
				
				var price = $("#money").val();
				var count = $("#count").val();	
				if (price!=""&&count!="") {
					var total = price*count;
					$("#keySubtotal").html(total);		
				}
		}
	
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="#">四川CA对账统计表</a></li>
		<li><a href="${ctx}/settle/supplierSettle/showTableT">付款结算清单</a></li>
		<li><a href="${ctx}/settle/settleKey/list">KEY数量统计</a></li>
		<li class="active"><a href="${ctx}/settle/keySettle/list">KEY结算功能列表</a></li>
		<li><a href="${ctx}/settle/keyPurchase">KEY采购记录</a></li>
		
	</ul>
	<form:form id="searchForm"  action="${ctx}/settle/keySettle" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div>
			<label>KEY 厂商：</label> 
			<select name="supplierId"
				id="supplierId" onchange="addGene()">
				<option value="">请选择</option>
				<c:forEach items="${suppliers}" var="supplier">
					<option value="${supplier.id}"
						<c:if test="${supplier.id==configSupplierId}">
						selected="selected"
					</c:if>>${supplier.supplierName}</option>
				</c:forEach>
			</select> 
			<label>KEY类型 ：</label> 
			<select name="keyId" id="keyId">
				<option value="">请选择</option>
				<c:forEach items="${keys}" var="key">
					<option value="${key.id}"
						<c:if test="${key.id==keyId}">
					selected="selected"
					</c:if>>${key.name}</option>
				</c:forEach>
			</select>
			<label>KEY编码：</label>
			<input type="text"  name="keySn"  id="keySn" value="${keySn }"/>
		</div>
		<br />
		<label>入库时间：</label> 
		<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		&nbsp;&nbsp;&nbsp;&nbsp;
<!-- 		<a href="javascript:dca()" class="btn btn-primary">导出</a> -->
		<a href="javascript:dca()" class="btn btn-primary">导出</a>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>入库时间</th>
				<th>产品名称</th>
				<th>KEY码(起始码)</th>
				<th>KEY码(终止码)</th>
				<th>数量</th>
				<th>单价</th>
				<th>小计</th>
				<th>状态</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		 <c:set var="zje"  value="0"/>  
			<c:forEach items="${page.list}" var="keyPurchase" varStatus="status">
			
				<tr>
				<td>${status.index+1 }</td>
				<td>
				<fmt:formatDate value="${keyPurchase.storageDate }" pattern="yyyy-MM-dd"/>
				</td>
				
				<td>${keyPurchase.appName }</td>
				<td>${keyPurchase.startCode }</td>
				<td>${keyPurchase.endCode }</td>
				<td id="count">${keyPurchase.count}</td>
				<td id="money">${keyPurchase.money}</td>
				<td id="keySubtotal">${keyPurchase.count*keyPurchase.money}</td>
				<c:set var="zje"  value="${zje+keyPurchase.count*keyPurchase.money}"/> 
				<td>
					<c:if test="${keyPurchase.status==1}">已付</c:if>
					<c:if test="${keyPurchase.status==0}">未付</c:if>
					<c:if test="${keyPurchase.status==null}">状态位置</c:if>
				</td>
				<td>${keyPurchase.remarks}</td>
				</tr>
				
			</c:forEach>
				<shiro:hasPermission name="settle:settleKey:edit"><td>
				</td></shiro:hasPermission>
				<tr>	
					<td></td>
					<td colspan="7">总计：${zs}个</td>
					<td>总金额：${zje}元</td>
			</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
				
