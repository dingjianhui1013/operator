<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>调拨信息管理</title>
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
		
		function showRefusalReason(applyId){
			var url = "${ctx}/receipt/receiptAllocateApply/showRefusalReason?applyId="+applyId;
			top.$.jBox.open("iframe:"+url, "拒绝原因", 600, 280, {
					buttons:{"关闭":true}, submit:function(v, h, f){
					}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<shiro:hasPermission name="receipt:receiptAllocateApply:view">
			<li class="active"><a href="${ctx}/receipt/receiptAllocateApply/">调拨管理</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="receipt:receiptAllocateApply:view">
			<li><a href="${ctx}/receipt/receiptAllocateApply/form">调拨申请</a></li>
		</shiro:hasPermission>
	</ul>
	  <form:form id="searchForm" modelAttribute="receiptAllocateApply" action="${ctx}/receipt/receiptAllocateApply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>审批状态 ：</label><select name="state">
					<option value="">所有状态</option>
					<option <c:if test="${state==0 }">selected</c:if> value="0">待审批</option>
					<option <c:if test="${state==1 }">selected</c:if> value="1">同意</option>
					<option <c:if test="${state==2 }">selected</c:if> value="2">拒绝</option>
					<option <c:if test="${state==3 }">selected</c:if> value="3">完成</option>
		</select>
		<div style="margin-top: 8px">
			<label>到货时间：</label>
			<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
		<!-- <label>所属网点：</label>
		<select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select> -->
		
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>采购库房</th>
				<th>采购网点</th>
				<th>申请类型/元</th>
				<th>申请数量/张</th>
				<th>申请金额/元</th>
				<th>紧急程度</th>
				<th>要求到货日期</th>
				<th>审批状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptAllocateApply">
			<tr>
				<td>${receiptAllocateApply.receiptDepotInfo.receiptName }</td>
				<td>${receiptAllocateApply.office.name}</td>
				
				<td>
				<c:forEach items="${receiptAllocateApply.receiptApplyDetails }" var="detail">
					${detail.receiptType.typeName}<br/>
				</c:forEach>
				</td>
				<td>
				<c:forEach items="${receiptAllocateApply.receiptApplyDetails }" var="detail">
					${detail.applyNum}<br/>
				</c:forEach>
				</td>
				
				
				<td>${receiptAllocateApply.money}</td>
				<td>
					<c:if test="${receiptAllocateApply.leavel==0}">一般</c:if>
					<c:if test="${receiptAllocateApply.leavel==1}">紧急</c:if>
				</td>
				<td><fmt:formatDate value="${receiptAllocateApply.warehouseDate}" pattern="yyyy-MM-dd"/></td>
				<td>
					<c:if test="${receiptAllocateApply.state==0}">待审批</c:if>
					<c:if test="${receiptAllocateApply.state==1}">同意</c:if>
					<c:if test="${receiptAllocateApply.state==2}">拒绝</c:if>
					<c:if test="${receiptAllocateApply.state==3}">完成</c:if>
				</td>
				<td>
    				<c:if test="${receiptAllocateApply.state==1 && receiptAllocateApply.receiptDepotInfo.id==depot.id}">
    					<a href="${ctx}/receipt/receiptAllocateApply/formQR?id=${receiptAllocateApply.id}" onclick="return confirmx('如果你已收到发票${receiptAllocateApply.money}元，请点击确认完成入库操作！', this.href)">到货确认</a>
    				</c:if>
    				<c:if test="${receiptAllocateApply.state==2}">
    					<a href="#"  onclick="showRefusalReason(${receiptAllocateApply.id})">拒绝原因</a>
    				</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
