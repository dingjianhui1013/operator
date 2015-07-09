<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#agentType1").attr("checked")=="checked"){
				$("#menuTree").attr("style","float:left;");
			}else{
				$("#menuTree").attr("style","display:none");
			}
			if($("#agentType2").attr("checked")=="checked"){
				$("#officeTree").attr("style","float:left;");
			}else{
				$("#officeTree").attr("style","display:none");
			}
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var ids = [], nodes = tree2.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					$("#officeIds").val(ids);
					var ids2 = [], nodes2 = tree.getCheckedNodes(true);
					for(var i=0; i<nodes2.length; i++) {
						ids2.push(nodes2[i].id);
					}
					$("#appIds").val(ids2);
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
		// 用户-菜单  
		var zNodes=[
				<c:forEach items="${menuList}" var="menu">{id:${menu.id},pId:1, name:"${menu.appName}"},
	            </c:forEach>];
		// 初始化树结构
		var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
		// 默认选择节点
		var ids = "${configApps}".split(",");
		for(var i=0; i<ids.length; i++) {
			var node = tree.getNodeByParam("id", ids[i]);
			try{tree.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree.expandAll(true);
		
		// 用户-机构
		var zNodes2=[
				<c:forEach items="${officeList}" var="office">{id:${office.id}, pId:${not empty office.parent?office.parent.id:0}, name:"${office.name}"},
	            </c:forEach>];
		// 初始化树结构
		var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
		// 不选择父节点
		tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
		// 默认选择节点
		var ids2 = "${offices}".split(",");
		for(var i=0; i<ids2.length; i++) {
			var node = tree2.getNodeByParam("id", ids2[i]);
			try{tree2.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree2.expandAll(true);
	});
	
		function agentType1Chenge(){
			if($("#agentType1").attr("checked")=="checked"){
				$("#menuTree").attr("style","float:left;");
			}else{
				$("#menuTree").attr("style","display:none");
			}
		}
		function agentType2Chenge(){
			if($("#agentType2").attr("checked")=="checked"){
				$("#officeTree").attr("style","float:left;");
			}else{
				$("#officeTree").attr("style","display:none");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configCommercialAgent/">代理商列表</a></li>
		<li class="active"><a href="#">代理商修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configCommercialAgent" action="${ctx}/profile/configCommercialAgent/update" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>代理商名称:</label>
			<div class="controls">
				<form:input path="agentName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代理商类型:</label>
			<div class="controls">
				<table>
					<tr>
						<td>
							<form:checkbox  id="agentType1" path="agentType1" onchange="javascript:agentType1Chenge();"/>市场推广</from>
						</td>
						<td>
							<form:checkbox id="agentType2" path="agentType2" onchange="javascript:agentType2Chenge();"/>劳务关系</from>
						</td>
					</tr>
					<tr>
						<td>
							<div id="menuTree" class="ztree" style="margin-top:3px;float:left;display:none"></div>
							<input id="appIds" type = "hidden" name = "appIds"/>
						</td>
						<td>
							<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;display:none"></div>
							<input id="officeIds" type = "hidden" name = "officeIds"/>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>联系人姓名:</label>
			<div class="controls">
				<form:input path="agentCommUserName" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人电话:</label>
			<div class="controls">
				<form:input path="agentCommMobile" htmlEscape="false" maxlength="11" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>代理商地址:</label>
			<div class="controls">
				<form:input path="agentAddress" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>代理合同有效期:</label>
			<div class="controls">
				<label class="lbl">
				</label>
				<input id="timeStart" name="timeStart" type="text" readonly="readonly" maxlength="20" class=" input-medium Wdate required"
					value="<fmt:formatDate value="${configCommercialAgent.agentContractStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				至
				<input id="timeEnd" name="timeEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${configCommercialAgent.agentContractEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'timeStart\')}'});"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="agentRemark" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
