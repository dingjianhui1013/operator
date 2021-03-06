<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		
		if(${isSelectedAll==0||isSelectedAll==null}){
			$("#selectAllData").val("全选");
			$("#selectAllData").attr("onclick","selectData()");	
		}else{
			$("#selectAllData").val("取消");
			$("#selectAllData").attr("onclick","deleteSelect()");	
		}
		
		
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	function checkAll(obj){
		var check = $($("#contentTable").find("#checkAll"));
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (check.is(":checked") == true ) {
			$('input:checkbox').each(function() {
		        $(this).attr('checked', true);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == true && check.val()!="${page.pageNo}") {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne)<0) {
						if(checkIds==''){
							checkIds+=check.val();
						}else{
							checkIds+=","+check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}else{
			$('input:checkbox').each(function () {
		        $(this).attr('checked',false);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == false && check.val()!="${page.pageNo}") {
					checkIds = checkIds.replace(check.val(), "");
					checkIds = checkIds.replace(",,", ",");
				}
			}
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}
	}
	
	
	function changeCheck(obj){
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if(checkIds.indexOf($(obj).val())>-1){
			checkIds = checkIds.replace($(obj).val(), "");
		}
		for (var a = 0; a <xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == true) {
				var checkOne = check.val();
				if (checkIds.indexOf(checkOne)<0) {
					if(checkIds==''){
						checkIds+=check.val();
					}else{
						checkIds+=","+check.val();
					}
				}
			}
		}
		checkIds = checkIds.replace(",,", ",");
		if (checkIds==",") {
			$("#checkIds").val("");
		}else{
			$("#checkIds").val(checkIds);
		}
	}
	
	function deleteDealInfoIds(){
		var checkIds = $("#checkIds").val();
		if(checkIds==null||checkIds==""){
			top.$.jBox.tip("请选择您要删除的业务！");
		}else{
			top.$.jBox.tip("正在删除批量导入新增数据...", 'loading');
			var url = "${ctx}/work/workDealInfo/deleteDealInfoIds?dealInfoIds="+checkIds+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if (data.status==1){
					top.$.jBox.tip("删除成功！");
					  setTimeout(function (){
	            			window.location.href="${ctx}/work/workDealInfo/deleteList";
	            		   }, 1500); 
				}else{
					top.$.jBox.tip("系统异常！");
				}
			});
		}
		
		
		
		
	}
	
	
	
	
	function selectData()
	{
		var companyName = $("#companyName").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var agentId = $("#agentId").val();
		
		var url="${ctx}/work/workDealInfo/deleteAllData";
					$.ajax({
						url:url,
						type:"POST",
						data:{"companyName":companyName,"startTime":startTime,"endTime":endTime,"agentId":agentId,_:new Date().getTime()},
						dataType:"text",
						success:function(data)
						{
							$("#checkIds").val(data);
							$("#isSelectedAll").val(1);
							var xz = $("#contentTable").find("[name='oneDealCheck']");
							for (var a = 0; a < xz.length; a++) {
								var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
								if (check.is(":checked") == false) {
									check.attr("checked","true");
								}
							}
							
							 $("#checkAll").attr("checked","true"); 
							 
							 
							 $("#selectAllData").val("取消");
							$("#selectAllData").attr("onclick","deleteSelect()");	 
						}
					});
			
	}
	
	
	 function deleteSelect(){
			
			$('input:checkbox').each(function () {
		        $(this).attr('checked',false);
			});
			
			$("#checkIds").val("");
			$("#isSelectedAll").val(0);
			$("#selectAllData").val("全选");
			$("#selectAllData").attr("onclick","selectData()");	
		} 
	
	
	
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<shiro:hasPermission name="work:workDealInfo:edit">
			<li class="active"><a href="${ctx}/work/workDealInfo/deleteList">删除批量新增信息</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/deleteList" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="dealId" type="hidden" value="${workDealInfo.id}" />
		<div>
			
			&nbsp;&nbsp; <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"  id="companyName"
				maxlength="50" class="input-medium" />
				
				
			&nbsp;&nbsp;
			<label>导入日期：&nbsp;</label> &nbsp;&nbsp;<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime"  id="endTime"/>
			
			
			
			&nbsp;&nbsp;<label>计费策略模板：</label>
			<select name="agentId" id="agentId">
				<option value="">请选择业务类型</option>
				<c:forEach items="${agents}" var="agent">
					<option value="${agent.id}"
						<c:if test="${agent.id==agentId}">
					selected="selected"
					</c:if>>${agent.tempName}</option>
				</c:forEach>
			</select> 			
			
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit"
				class="btn btn-primary" type="submit" value="查询" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				
				<input id="selectAllData" class="btn btn-primary" type="button" onclick="selectData()" value="全选" />
				<input type="hidden"  name="isSelectedAll"  id="isSelectedAll"  value="${isSelectedAll }"/>
				<a id="manyUpdate" data-toggle="modal" href="javaScript:deleteDealInfoIds();" class="btn btn-primary">批量删除</a>
				
				
				
				<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds }"/>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<%-- <th><input type="checkbox" id="checkAll" name="oneDealCheck" value="${page.pageNo}" 
				<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
				</c:forEach>
				onchange="checkAll(this)"
				/> </th> --%>
				
				<th><input type="checkbox" id="checkAll" name="checkAll"
				
				<c:if test="${isSelectedAll==1}"> checked="checked"</c:if>
					 value="" onchange="checkAll(this)" /></th>
				
				
				<th>业务编号</th>
				<th>别名</th>
				<th>单位名称</th>
				<th>证书持有人</th>
				<th>经办人</th>
				<th>产品名称</th>
				<th>业务类型</th>
				<th>KEY编码</th>
				<th>制证日期</th>
				<th>有效期</th>
				<th>到期日期</th>
				<th>业务状态</th>
				<th>操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>
					
					<c:if test="${workDealInfo.dealInfoStatus!=6}">
						<input type="checkbox" name="oneDealCheck" value = "${workDealInfo.id}" 
						<c:forEach items="${ids }" var="id">
							<c:if test="${id==workDealInfo.id }"> checked="checked"</c:if>
						</c:forEach>
						onchange="changeCheck(this)"
						 /> 
					 </c:if>
					 
					 </td>
					<td>${workDealInfo.svn}</td>
					<td>${workDealInfo.configApp.alias}</td>
					<td><a
						href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
					<td>${workDealInfo.workUser.contactName}</td>
					<td>${workDealInfo.workCertInfo.workCertApplyInfo.name}</td>
					<td>${proType[workDealInfo.configProduct.productName]}</td>
					<td>
						<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
						<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
						<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
						<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
					</td>
					<td>${workDealInfo.keySn }</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${workDealInfo.workCertInfo.signDate}"/> </td>
					<td><c:if
							test="${not empty workDealInfo.workCertInfo.notafter && not empty workDealInfo.workCertInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
						</c:if></td>
					<td><fmt:formatDate
							value="${workDealInfo.notafter }"
							pattern="yyyy-MM-dd" /> </td>
				
					<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
					<td>
							<a href="${ctx}/work/workDealInfo/deleteByDealInfoId?dealInfoId=${workDealInfo.id}"
								onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>&nbsp;&nbsp;
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>
	
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/work/workDealInfo/addAttach"
				enctype="multipart/form-data">
				<input id="fileName" name="fileName" type="file" multiple="multiple" />
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
