<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商应用计费策略模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
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
//			var t = $("#tStyle").val();
//			if(t=="1" || t==""){
//				$("#chargeMethods").show();
//
//			}else{
//				$("#chargeMethods").hide();
//
//			}
			$("input[name='tempStyle']").bind("click",this,function(){
				var v = $(this).val();
//				if(v=="1"){
//					$("#chargeMethods").show();
//				}else{
//					$("#chargeMethods").hide();
//				}
				$("#tStyle").val(v);
		});
			
			if($("#tStyle").val()==1){
				$("#configureNumDiv").hide();
			}
			
		});
		function checkUsed(){

			var tempId = $("#configChargeAgentId").val();
			var tempName = $("#tempName").val();
			if(tempName==''){
				top.$.jBox.tip("计费策略模板名称不能为空");
				return false;
			}
			if($("#tStyle").val()==""){
				top.$.jBox.tip("模板类型为必选项");
				return false;
			}
			if($("#xz1").val()=="" && $("#xz2").val()=="" && $("#xz3").val()=="" && $("#xz4").val()==""){
				top.$.jBox.tip("业务类型：新增 ，请至少输入一项计费策略");
				return false;
			}

			if($("#gx1").val()=="" && $("#gx2").val()=="" && $("#gx3").val()=="" && $("#gx4").val()==""){
				top.$.jBox.tip("业务类型：更新 ，请至少输入一项计费策略");
				return false;
			}

			if($("#bb0").val()=="" && $("#bb1").val()==""){
				top.$.jBox.tip("业务类型：补办 ，请至少输入一项计费策略");
				return false;
			}
			if($("#th").val()==""){
				top.$.jBox.tip("业务类型：变更 为必输项");
				return false;
			}
			if($("#khf").val()==""){
				top.$.jBox.tip("业务类型：开户费 为必输项");
				return false;
			}
			if($("#trustDevice0").val()=="" && $("#trustDevice1").val()==""){
				top.$.jBox.tip("业务类型：可信移动设备 请至少输入一项计费策略");
				return false;
			}
			if($("#chargeMethodPos").attr("checked")!="checked" &&
					$("#chargeMethodMoney").attr("checked")!="checked" &&
					$("#chargeMethodBank").attr("checked")!="checked"){
				top.$.jBox.tip("缴费方式至少需要选择一项");
				return false;
			}
			
			
			var tempStyle = $('input:radio:checked').val();
			if(tempStyle!=1){
				if($("#configureNum").val()==""){
					top.$.jBox.tip("请输入配置数量");
					return false;
				}else{
					
					if(parseInt($("#configureNum").val())<parseInt($("#avaRes").val())){
						top.$.jBox.tip("配置数量应大于已使用数量！请重新输入！");
						return false;
					}
					
					
				}
			}
			$.ajax({
				url:"${ctx}/profile/configChargeAgent/checkChargeAgent?tempId="+tempId+"&_="+new Date().getTime(),
				type:'POST',
				dataType : 'json',
				data:{
					tempId:tempId,
					tempName:tempName
				},
				success:function(data){
					if(data.status==0){
						top.$.jBox.tip(data.msg);
					}else if(data.status == 1){
						top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
							if(v=='ok'){
								$("#inputForm").submit();
							}
						});
					}else if(data.status == 2){
						$("#inputForm").submit();
					}
				}
			});
		}

	</script>
	
	<script type="text/javascript">
		var add = '<select name="addYear" id="addDiv"><option <c:if test="${configChargeAgentDetail.chargeYear==1}">selected</c:if> value="1">一年</option><option <c:if test="${configChargeAgentDetail.chargeYear==2}">selected</c:if> value="2">二年</option><option <c:if test="${configChargeAgentDetail.chargeYear==4}">selected</c:if> value="4">四年</option></select><input type="text" name="addMoney">';
		var update = ''
		function addLine(obj){
		}
		
		
		function checkBZ(){
			var tempStyle = $('input:radio:checked').val();
			if($("#agentId").val() == null || $("#agentId").val()==""){
				$("#configureNum").val("");
				
			}
			if(tempStyle==1){
				$("#configureNumDiv").hide();
				$("#surplusNumDiv").hide();
				$("#availableNumDiv").hide();
			}else{
				$("#configureNumDiv").show();
				
				if($("#agentId").val() != null && $("#agentId").val()!=""){
					
					$("#surplusNumDiv").show();
					$("#availableNumDiv").show();
				}
			}
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/profile/configChargeAgent/getChargeAgentList">计费策略模板列表</a></li>
		<li class="active"><a href="${ctx}/profile/configChargeAgent/form?id=${configChargeAgent.id}">计策策略模板${not empty configChargeAgent.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="configChargeAgent" action="${ctx}/profile/configChargeAgent/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>计费策略模板名称:</label>
			<div class="controls">
				<input type="text" name="tempName" id="tempName" value="${configChargeAgent.tempName }" >
			</div>
		</div>
		<input type="hidden" name="configChargeAgentId" id="configChargeAgentId"value="${configChargeAgent.id}"  >
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>模板类型:</label>
			<div class="controls">

				<form:radiobutton path="tempStyle" value="1" onclick="checkBZ()" />标准
				<form:radiobutton path="tempStyle" value="2" onclick="checkBZ()" />政府统一采购
				<form:radiobutton path="tempStyle" value="3" onclick="checkBZ()"/>合同采购
				<input type="hidden" id="tStyle" value="${configChargeAgent.tempStyle}"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">业务类型:</label>
			<div class="controls">

			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>新增:</label>
			<div class="controls">
				一年：<input type="text" id="xz1" name="addMoney" value="<fmt:formatNumber value='${xz1}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
				两年：<input type="text" id="xz2"name="addMoney" value="<fmt:formatNumber value='${xz2}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
				四年：<input type="text" id="xz3" name="addMoney" value="<fmt:formatNumber value='${xz4}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
				五年：<input type="text" id="xz4" name="addMoney" value="<fmt:formatNumber value='${xz5}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>更新:</label>
			<div class="controls" id="updateDiv">
				一年：<input type="text" id="gx1" name="updateMoney" value="<fmt:formatNumber value='${gx1}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
				两年：<input type="text" id="gx2"name="updateMoney" value="<fmt:formatNumber value='${gx2}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
				四年：<input type="text" id="gx3"name="updateMoney" value="<fmt:formatNumber value='${gx4}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br>
				五年：<input type="text" id="gx4"name="updateMoney" value="<fmt:formatNumber value='${gx5}' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"><br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>补办:</label>
			<div class="controls" id="reissueDiv">
				遗失补办:<input type="text" id="bb0" name="reissueMoney0" value="<fmt:formatNumber value='${bb0}' pattern='#,###0'/>"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
				损坏更换:<input type="text" id="bb1" name="reissueMoney1" value="<fmt:formatNumber value='${bb1}' pattern='#,###0'/>"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/><br>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>变更:</label>
			<div class="controls">
				<input type="text" id="th" name="changeMoney" value="<fmt:formatNumber value='${th }' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>开户费:</label>
			<div class="controls">
				<input type="text" id="khf" name="openAccountMoney" value="<fmt:formatNumber value='${khf }' pattern='#,###0'/>"  maxlength="9"  onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>可信移动设备:</label>
			<div class="controls">
				半年:<input type="text" id="trustDevice0" name="trustDeviceMoney" value="<fmt:formatNumber value='${trustDevice0 }' pattern='#,###0'/>"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
				一年:<input type="text" id="trustDevice1" name="trustDeviceMoney" value="<fmt:formatNumber value='${trustDevice1 }' pattern='#,###0'/>"  maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
			</div>
		</div>
		
		
				<%-- <select name="workType">
					<option <c:if test="${configChargeAgentDetail.workType==0}">selected</c:if> value="0">新增</option>
					<option <c:if test="${configChargeAgentDetail.workType==1}">selected</c:if>  value="1">更新</option>
					<option <c:if test="${configChargeAgentDetail.workType==2}">selected</c:if>  value="2">补办</option>
					<option <c:if test="${configChargeAgentDetail.workType==3}">selected</c:if>  value="3">变更</option>
					<option <c:if test="${configChargeAgentDetail.workType==4}">selected</c:if>  value="4">开户费</option>
				</select> --%>

		<div class="control-group" id="chargeMethods">
			<label class="control-label"><font color="red">*</font>缴费方式:</label>
			<div class="controls" >
				<input name="chargeMethodPos" id="chargeMethodPos"<c:if test="${configChargeAgent.chargeMethodPos}">checked</c:if> type="checkbox" value="0">pos&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodMoney" id="chargeMethodMoney"<c:if test="${configChargeAgent.chargeMethodMoney}">checked</c:if> type="checkbox" value="0">现金缴费&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodBank" id="chargeMethodBank"<c:if test="${configChargeAgent.chargeMethodBank}">checked</c:if> type="checkbox" value="0">银行转账	&nbsp;&nbsp;&nbsp;<br>
			</div>
		</div>
		<div class="control-group" id="configureNumDiv" >
			<label class="control-label"><font color="red">*</font>配置数量:</label>
			<div class="controls" >
				<input name="configureNum" id="configureNum"  type="text" value="${configChargeAgent.configureNum}" onkeyup="value=value.replace(/[^\d]/g,'')"   />
			</div>
		</div>
		<c:if test="${id!=null && configChargeAgent.tempStyle!=1 }">
		<div class="control-group" id="surplusNumDiv"   >
			<label class="control-label">剩余数量:</label>
			<div class="controls" >
				${configChargeAgent.surplusNum}
				<input type="hidden" id="avaNum" value="${configChargeAgent.availableNum}">
			</div>
		</div>
		<div class="control-group" id="availableNumDiv" >
			<label class="control-label">已用数量:</label>
			<div class="controls" >
				${configChargeAgent.availableNum  + configChargeAgent.reserveNum}
				
				<input type="hidden" id="avaRes" name="avaRes" value="${configChargeAgent.availableNum  + configChargeAgent.reserveNum}">
				<input type="hidden" id="agentId" value="${id }" /> 
			</div>
		</div>
		</c:if>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configChargeAgent:edit"><input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="checkUsed();"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
