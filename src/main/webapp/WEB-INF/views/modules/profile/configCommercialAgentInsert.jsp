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
				<c:forEach items="${menuList}" var="menu">
					{id:${menu.id},pId:0, name:"${menu.appName}"},
	            </c:forEach>];
		// 初始化树结构
		var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
		// 默认选择节点
		var ids = "${role.menuIds}".split(",");
		for(var i=0; i<ids.length; i++) {
			var node = tree.getNodeByParam("id", ids[i]);
			try{tree.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree.expandAll(true);
		
		// 用户-机构
		var zNodes2=[
				<c:forEach items="${officeList}" var="office">
				{id:${office.id}, pId:${not empty office.parent?office.parent.id:0}, name:"${office.name}"},
	            </c:forEach>];
		// 初始化树结构
		var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
		// 不选择父节点
		tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
		// 默认选择节点
		var ids2 = "${role.officeIds}".split(",");
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
		<li class="active"><a href="${ctx}/profile/configCommercialAgent/insertFrom">代理商添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configCommercialAgent" action="${ctx}/profile/configCommercialAgent/save" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>代理商名称:</label>
			<div class="controls">
				<form:input path="agentName" id="agentName" htmlEscape="false" maxlength="25" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">代理商类型:</label>
			<div class="controls">
				<table>
					<tr>
						<td>
							<form:checkbox  id="agentType1" path="agentType1" onclick="javascript:agentType1Chenge();"/>市场推广</from>
						</td>
						<td>
							<form:checkbox id="agentType2" path="agentType2" onclick="javascript:agentType2Chenge();"/>劳务关系</from>
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
			<label class="control-label"><span style="color:red;">*</span>联系人姓名:</label>
			<div class="controls">
				<form:input path="agentCommUserName" id="agentCommUserName" htmlEscape="false" maxlength="15" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系人电话:</label>
			<div class="controls">
				<form:input path="agentCommMobile" htmlEscape="false" maxlength="11"  class="mobile" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>代理商地址:</label>
			<div class="controls">
				<form:input path="agentAddress" id="agentAddress" htmlEscape="false" maxlength="40" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>代理合同有效期:</label>
			<div class="controls">
				<label class="lbl">   
				</label>
					<input class="input-medium Wdate required"  type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20" readonly="readonly" name="timeStart" id="timeStart"></input>
				至<input  class="input-medium Wdate required"  type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'timeStart\')}'});" maxlength="20" readonly="readonly" name="timeEnd" id="timeEnd"></input>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"><span style="color:red;">*</span>结算年限:</label>
			<div class="controls">
				<form:input path="settlementPeriod"  htmlEscape="false" maxlength="1" class="required" 
				onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" onkeyup="value=value.replace(/[^\d]/g,'')" 
				/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="agentRemark" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
