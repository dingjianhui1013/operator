<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		var windowH=$(window).height();
		$('.windowHeight').height(windowH);
		$("#scrollBar").scroll(function(){
			var tableWidth=$("#contentTable").width();
			var formWidth=$("#searchForm").width();
			var leftWidth=$("#scrollBar").scrollLeft();
			if((tableWidth-formWidth)-leftWidth>0)
				{
					$("#searchForm").css("margin-left",leftWidth);
					$("#ulId").css("margin-left",leftWidth);
				}
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		loading('正在提交，请稍等...');
		$("#searchForm").submit();
		return false;
	}
	
	function setApp(obj){
		var comAgentId = obj.value;
		var url = "${ctx}/settle/settlePayableDetail/setApps?comAgentId="+comAgentId + "&_="+new Date().getTime();
		$.getJSON(url, function(data) {
			var html = "";
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#appId").html(html);
		});
	}
	
	function setProduct(obj){
		var appId = obj.value;
		var url = "${ctx}/settle/settlePayableDetail/setProducts?appId="+appId + "&_="+new Date().getTime();
		$.getJSON(url, function(data) {
			var html = "";
			$.each(data, function(idx, ele) {
				html += "<input name='productIds' type='checkbox' value='"+ele.id+"'\">" + ele.name
			});

			$("#productId").html(html);
		});
	}
	
	function saveSettlementLog()
	{
		var comAgentId = $("#comAgentId").val();
		var appId = $("#appId").val();
		var productIds=' ';
		$("input[name='productIds']:checkbox").each(function(i,e){ 
			productIds+=$(e).val()+",";
			});
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(comAgentId==0){
			top.$.jBox.tip("代理商不能为空！");
		}else{
			var html = " &nbsp;&nbsp;&nbsp;&nbsp;请填写备注信息：<input class='input-medium' type='text' name='remarks' value='' style='margin-left:10px'/>";
			var submit=function(v,h,f)
			{
				if(v=='ok')
					{
						if(f.remarks=='')
						{
							top.$.jBox.tip("请填写备注。", 'error', { focusId: "remarks" });
					        return false;
						}else
							{
// 									
									$("#remarks").val(f.remarks);
									var remarks = $("#remarks").val();
									$.ajax({
										url:"${ctx}/settle/settlementLog/isExist",
										data:{'appId':appId,'comAgentId':comAgentId,'productIds':productIds,'startTime':startTime,'endTime':endTime,'remarks':remarks,_:new Date().getTime()},
										dataType:'json',
										success:function(data){
											if(data.status==1)
												{
													top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
						                            	if(v=='ok'){
						                            		loading('正在提交，请稍等...');
						                            		$.ajax({
						        								url:"${ctx}/settle/settlementLog/deleteAndSave",
						        								data:{'appId':appId,'comAgentId':comAgentId,'productIds':productIds,'startTime':startTime,'endTime':endTime,'remarks':remarks,_:new Date().getTime()},
						        								dataType:'json',
						        								success:function(data){
						        									if(data.status==1)
						        										{
						        											top.$.jBox.tip("删除并保存成功！");
						        										}else
						        											{
						        												top.$.jBox.tip("删除并保存失败！");
						        											}
						        								}
						                            		});
						                            	}
													});
											}else{
														
													// 	$("#remarks").val(f.remarks);
													var remarks = $("#remarks").val();
													loading('正在提交，请稍等...');
				 									var url="${ctx}/settle/settlementLog/saveSettlementLog";
				 									$.ajax({
				 										url:url,
				 										data:{'appId':appId,'comAgentId':comAgentId,'productIds':productIds,'startTime':startTime,'endTime':endTime,'remarks':remarks,_:new Date().getTime()},
				 										dataType:'json',
				 										success:function(data){
				 											if(data.status==1)
				 												{
				 													top.$.jBox.tip("保存成功！");
				 												}else{
				 													top.$.jBox.tip("保存失败！");
				 												}
				 										}
				 									});	
											}
									}
								});
							}
					}
			};
			top.$.jBox(html, { title:"填写备注",buttons:{"确定":"ok","关闭":true}, submit: submit });
		}
	}
	function searchForm(){
		var comAgentId = $("#comAgentId").val();
		if(comAgentId==0){
			top.$.jBox.tip("代理商不能为空！");
		}else{
			loading('正在提交，请稍等...');
			$("#searchForm").submit();
		}
	}
	function dcPayableDetail()
	{
		var comAgentId = $("#comAgentId").val();
		var appId = $("#appId").val();
		var productIds=' ';
		$("input[name='productIds']:checkbox").each(function(i,e){ 
			productIds+=$(e).val()+",";
			});
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(comAgentId==0){
			top.$.jBox.tip("代理商不能为空！");
		}else{
			var url="${ctx}/settle/settlePayableDetail/dcPayableDetail?comAgentId="+comAgentId+"&appId="+appId+"&productIds="+productIds
					+"&startTime="+startTime+"&endTime="+endTime;
				window.location.href=url;
			
		}
	}
	
	function dcCountDay(){
		if ($("#startTime").val()==""||$("#endTime").val()=="") {
			top.$.jBox.tip("请选定时间范围");
			return false;
		} else {
			if ($("#office").val()==""){
				top.$.jBox.tip("请选定网点");
			} else {
				var startTime = document.getElementById("startTime").value;
				var endTime = document.getElementById("endTime").value;
				var office = document.getElementById("office").value;
				window.location.href="${ctx}/statistic/StatisticDayData/exportCountDay?startTime="+startTime+"&endTime="+endTime+"&office="+office;
			}
		}
		
		
		
	}
</script>
</head>
<body>
<div style="overflow:auto;" class="windowHeight" id="scrollBar" >
	<ul class="nav nav-tabs" id="ulId" style="width:100%;">
		<li class="active"><a href="${ctx}/settle/settlePayableDetail/list">年限结算表</a></li>
		<li><a href="${ctx}/settle/settlementLog/list">年限结算保存查看</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/settle/settlePayableDetail/list" method="post"
		class="breadcrumb form-search" style="width:100%;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="remarks" type="hidden" value="" name="remarks"/>
		<div class="control-group">
			<label><font color="red" >*</font>代理商 ：</label>
			<select onchange="setApp(this)" name="comAgentId" id="comAgentId" class="required" >
				<option value="0">请选择</option>
				<c:forEach items="${comAgents }" var="comAgent" >
					<option value="${comAgent.id }" <c:if test="${comAgent.id == comAgentId}">
					selected="selected"
					</c:if> >${comAgent.agentName }</option>
				</c:forEach>
			</select>
			
			<label>应用名称 ：</label> 
			<select id="appId" name ="appId" onchange="setProduct(this)">
				<option value="0">请选择</option>
				<c:forEach items="${relationByComAgentId }" var="app" >
					<option value="${app.configApp.id }" <c:if test="${app.configApp.id == appId}">
					selected="selected"
					</c:if> >${app.configApp.appName }</option>
				</c:forEach>
			</select>
			<label>产品名称 ：</label><label id="productId">
			
			<c:forEach items="${products }" var="product" >
					
					<input name="productIds" type="checkbox" id="productIds"
						<c:forEach items="${productIdList }" var="a" >
							<c:if test="${a == product.id}">
								checked="checked"
							</c:if>
						</c:forEach>
					 value="${product.id }">${proType[product.productName ]}
				</c:forEach>
			</label>
		</div>
		<div style="width: 800px">                 
			<label>业务办理时间：</label>	    
				 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" id="endTime"/>
<!--  		  		<input id="btnSubmit" class="btn btn-primary"  -->
<!-- 				type="submit" value="查询" />  -->
				
				<input id="btnExport" class="btn btn-primary" onclick="searchForm()"
				type="button" value="查询" />
				
				&nbsp; <input id="dc" class="btn btn-primary" onclick="dcPayableDetail()"
				type="button" value="导出" />
				
				&nbsp;<input id="btnExport" class="btn btn-primary" onclick="saveSettlementLog()"
				type="button" value="保存" />
		</div>
	
	
		
	</form:form>
	<tags:message content="${message}" />	
	<div class="form-horizontal" >
		<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%" >
					<tr>
						<th colspan="${7+lenth*5 }" >统计周期：<fmt:formatDate pattern="yyyy-MM-dd" value="${startTime}"/>&nbsp;-&nbsp;<fmt:formatDate pattern="yyyy-MM-dd" value="${endTime}"/></th>
					</tr>
					<tr>
						<th colspan="${7+lenth*5 }">本期结算证书年限：（本次结算年数总数）</th>
					</tr>
					<tr>
						<th rowspan="2">序号</th>
						<th rowspan="2">单位名称</th>
						<th rowspan="2">经办人姓名</th>
						<th rowspan="2">产品名称</th>
						<c:forEach var="a" begin="1" end="${lenth}">
						
						<th colspan="5">第${a}次结算</th>
						</c:forEach>
						
						<th colspan="3">结算年限统计</th>
					</tr>
					<tr>
						
						<c:forEach begin="1" end="${lenth}">
						<th>缴费类型</th>
						<th>起始时间</th>
						<th>结束时间</th>
						<th>业务类型</th>
						<th>结算(年)</th>
						</c:forEach>
						<th>已结算（年）</th>
						<th>本期结算（年）</th>
						<th>剩余结算（年）</th>
					</tr>
					<c:forEach items="${dealInfos }" var="dealInfo" varStatus="status">
						<tr>
							<td>${status.index + 1}</td>
							<td>${dealInfo.workCompany.companyName}</td>
							<td>${dealInfo.workCertInfo.workCertApplyInfo.name}</td>
							<td>${proType[dealInfo.configProduct.productName]}</td>
							<c:forEach items="${dealInfo.detailList }" var="detail">

									<c:if test="${detail.method==1}"><td>标准</td></c:if> 
	 								<c:if test="${detail.method==2}"><td>政府统一采购</td></c:if> 
	 								<c:if test="${detail.method==3}"><td>合同采购</td></c:if> 
								<td><fmt:formatDate	value="${detail.startDate }" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${detail.endDate }" pattern="yyyy-MM-dd" /></td>
								<td><c:if test="${detail.dealInfoType=='null '}"> </c:if> 
								<c:if test="${detail.dealInfoType!='null '}">${detail.dealInfoType}</c:if> </td>
								<td>${detail.settleYear }</td>
							</c:forEach>
							<c:forEach begin="1" end="${lenth - dealInfo.detailList.size() }">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</c:forEach>
							<td>${dealInfo.yyNum}</td>
							<td>${dealInfo.lastNum}</td>
							<td><c:if test="${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy<0}">0</c:if>
							
								<c:if test="${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy>=0}">${dealInfo.totalNum - dealInfo.yyNum - dealInfo.lastNum-dealInfo.occupy}</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
		
	
	
		
	</div>
	</div>
</body>
</html>
