<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>调拨管理管理</title>
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
	
	function showRefusalReason(applyId){
		var url = "${ctx}/key/keyAllocateApply/showRefusalReason?applyId="+applyId;
		top.$.jBox.open("iframe:"+url, "拒绝原因", 600, 280, {
				buttons:{"关闭":true}, submit:function(v, h, f){
				}
		});
	}
	
	function deletefun(){
		
	}
	
	
	function showAllocateApplyArrival(applyId){
		var url = "${ctx}/key/keyAllocateApply/AllocateApplyArrivalShow?applyId="+applyId;
		top.$.jBox.open("iframe:"+url, "确认到货", 600, 280, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						
						$("#assessment").attr("onclick","deletefun()");	
						
						var url = "${ctx}/key/keyAllocateApply/arrivalSave?applyId="+applyId+"&_="+new Date().getTime();
						 $.getJSON(url,function(data){
				            if(data.status==1){
				            	top.$.jBox.tip("入库成功!");	
				            	  setTimeout(function (){
				            		    //something you want delayed
				            			window.location.reload();
				            		   }, 5); // how long do you want the delay to be? 
				            
				            }else{
				            	top.$.jBox.tip("入库失败!");	
				            }
						});
					}
				}
		});
	}
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		
		<li class="active"><a href="${ctx}/key/keyAllocateApply/">调拨管理</a></li>
		<shiro:hasPermission name="key:keyAllocateApply:edit">
		<li><a href="${ctx}/key/keyAllocateApply/form">调拨申请</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="keyAllocateApply"
		action="${ctx}/key/keyAllocateApply/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>审批状态 ：</label><select name="state">
					<option value="">所有状态</option>
					<option <c:if test="${state==1 }">selected</c:if> value="1">待审批</option>
					<option <c:if test="${state==2 }">selected</c:if> value="2">同意</option>
					<option <c:if test="${state==3 }">selected</c:if> value="3">拒绝</option>
					<option <c:if test="${state==4 }">selected</c:if> value="4">已到货</option>
		</select>
		<div style="margin-top: 8px">
			<label>到货时间：</label>
			<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required" 
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required" 
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>采购库房</th>
				<th>采购网点</th>
				<th>申请数量</th>
				<th>key类型名称(坏KEY类型名称)</th>
				<th>key类型标识</th>
				
				
		<!-- 		<th>返还坏key数量</th>
				<th>坏key类型名称</th>
				<th>坏key类型标识</th>
				 -->
				<th>紧急程度</th>
				<th>要求到货日期</th>
				<th>审批状态</th>
				<shiro:hasPermission name="key:keyAllocateApply:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyAllocateApply">
				<tr>
					<td>${keyAllocateApply.keyUsbKeyDepot.depotName}</td>
					<td>${keyAllocateApply.sysOffice.name}</td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.applyNewNum}
								<br/>
					</c:forEach></td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.keyGeneralInfo.configSupplier.supplierName}-${detail.keyGeneralInfo.name}
					        	<c:if test="${keyAllocateApply.applyType==2 }">
					        	(${detail.badKeyGeneralInfo.configSupplier.supplierName}-${detail.badKeyGeneralInfo.name})
					        	</c:if>
								<br/>
					</c:forEach></td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.keyGeneralInfo.model}
								<br/>
					</c:forEach></td>
					<td>${keyAllocateApply.leavelName}</td>
					<td><fmt:formatDate value="${keyAllocateApply.warehouseDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

					
					<td>${keyAllocateApply.stateName}</td>
					
						<td>
						<shiro:hasPermission name="key:keyAllocateApply:edit">
					        	<c:if test="${keyAllocateApply.state==2 && keyAllocateApply.keyUsbKeyDepot.id==depot.id}">
					     <a
							href="javascript:void(0)" id="assessment" onclick="showAllocateApplyArrival(${keyAllocateApply.id})">确认到货</a>			
						</c:if>
						</shiro:hasPermission>
						<c:if test="${keyAllocateApply.state==3 }">
						<a
							href="javascript:void(0)" onclick="showRefusalReason(${keyAllocateApply.id})">拒绝原因</a>
						</c:if>
						
						</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
