<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>调拨审批</title>
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
	
	
	function deletefun(){
		
	}
	
	
	function showAssessmentForm(applyId){
		
		var url = "${ctx}/key/keyAllocateApply/listModel?applyId="+applyId+"&_="+new Date().getTime();
		top.$.jBox.open("iframe:"+url, "调拨审批", 600, 280, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						
						$("#assessment").attr("onclick","deletefun()");	
						
						var table = h.find("iframe")[0].contentWindow.assessmentForm;
						//console.log(table);
						var url = $(table).attr('action')+"?_="+new Date().getTime();
						var data=$(table).serialize();
						$.ajax({
							  type: 'POST',
							  url: url,
							  data: data,
							  dataType : 'json',
							  success:function(data){
								   if(data.status=="1"){
										top.$.jBox.tip("审批成功!");
										
										 setTimeout(function (){
						            		    //something you want delayed
											 $("#searchForm").submit();
						            		   }, 5); // how long do you want the delay to be? 
									}else if(data.status=="0"){
										top.$.jBox.tip("审批失败，请采购网点所需要的Key类型产品！");
										 setTimeout(function (){
						            		    //something you want delayed
											 $("#searchForm").submit();
						            		   }, 5); // how long do you want the delay to be? 
									}else if(data.status=="2"){
										top.$.jBox.tip("审批失败，请采购网点所需要的Key类型产品库存不足，请采购！");
										 setTimeout(function (){
						            		    //something you want delayed
											 $("#searchForm").submit();
						            		   }, 5); // how long do you want the delay to be? 
									}else if(data.status =="3"){
									   top.$.jBox.tip("审批失败，申请数量应小于总库余量！");
									   setTimeout(function (){
					            		    //something you want delayed
										 $("#searchForm").submit();
					            		   }, 5); // how long do you want the delay to be? 
								   }
								}
							  
							});
					}
				}
		});
	}
	
	function badKeyInfo(applyId){
		var url = "${ctx}/key/keyAllocateApply/badKeyInForm?applyId="+applyId+"&_="+new Date().getTime();
		top.$.jBox.open("iframe:"+url, "调拨审批", 600, 580, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						var table = h.find("iframe")[0].contentWindow.assessmentForm;
						//console.log(table);
						var url = $(table).attr('action')+"?_="+new Date().getTime();
						var data=$(table).serialize();
						$.ajax({
							  type: 'POST',
							  url: url,
							  data: data,
							  dataType : 'json',
							  success:function(data){
								   if(data.status=="1"){
										top.$.jBox.tip("坏key入库成功!");
										$("#searchForm").submit();
									}else{
										top.$.jBox.tip("坏key入库失败!");
										$("#searchForm").submit();
									}
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
		<shiro:hasPermission name="key:keyAllocateApply:approval">
		<li class="active"><a href="${ctx}/key/keyAllocateApply/assessmentList">调拨审批</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="keyAllocateApply"
		action="${ctx}/key/keyAllocateApply/assessmentList" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>所属网点：</label>
		<select name="office">
			<option value="">请选择网点</option>
			<c:forEach items="${offices }" var="office">
				<option value="${office.id }" <c:if test="${office.id==officeId }">selected</c:if>>${office.name}</option>
			</c:forEach>
		</select>
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
			maxlength="20" class="Wdate required" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
			<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="20" class="Wdate required" required="required"
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
				<th>申请类型</th>
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
				<shiro:hasPermission name="key:keyAllocateApply:approval">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyAllocateApply">
				<tr>
					<td>${keyAllocateApply.keyUsbKeyDepot.depotName}</td>
					<td>${keyAllocateApply.sysOffice.name}</td>
					<td>
					<c:if test="${keyAllocateApply.applyType==1 }">
					申请新key
					</c:if>
					<c:if test="${keyAllocateApply.applyType==2 }">
					坏key置换
					</c:if>
					</td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.applyNewNum}
								</br>
					</c:forEach></td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.keyGeneralInfo.configSupplier.supplierName}-${detail.keyGeneralInfo.name}
					        	<c:if test="${keyAllocateApply.applyType==2 }">
					        	(${detail.badKeyGeneralInfo.configSupplier.supplierName}-${detail.badKeyGeneralInfo.name})
					        	</c:if>
								</br>
					</c:forEach></td>
					<td><c:forEach items="${keyAllocateApply.keyApplyDetails}" var="detail">
					        	${detail.keyGeneralInfo.model}
								</br>
					</c:forEach></td>
					<td style="vertical-align: middle;">${keyAllocateApply.leavelName}</td>
					<td style="vertical-align: middle;"><fmt:formatDate value="${keyAllocateApply.warehouseDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					
					<td>
					        	${keyAllocateApply.stateName}
								
					</td>
					<shiro:hasPermission name="key:keyAllocateApply:approval">
						<td>
						
						
							
							<c:if test="${keyAllocateApply.state==1 }">
						<a href="javascript:void(0)" id="assessment"  onclick="showAssessmentForm(${keyAllocateApply.id})">审批</a>
						</c:if>

							

							<c:if test="${keyAllocateApply.isBadKeyIn==1 && (keyAllocateApply.state==2||keyAllocateApply.state==4)}">

							<a
							href="javascript:void(0)" onclick="badKeyInfo(${keyAllocateApply.id})">坏key入库</a>
							</c:if>  
				
							
						</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
