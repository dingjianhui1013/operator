 <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>印章计费策略模板管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});

			});
	
	function checkUsed() {
		var tempName = $("#tempName").val();
		if (tempName == '') {
			top.$.jBox.tip("模板名称不能为空");
			return false;
		}else if ($("#xz1").val() == "" && $("#xz2").val() == ""
				&& $("#xz3").val() == "" && $("#xz4").val() == "" && $("#xz5").val() == "") {
			top.$.jBox.tip("业务类型：新增 ，请至少输入一项计费策略");
			return false;
		}else if ($("#gx1").val() == "" && $("#gx2").val() == ""
				&& $("#gx3").val() == "" && $("#gx4").val() == ""  && $("#gx5").val() == "") {
			top.$.jBox.tip("业务类型：更新 ，请至少输入一项计费策略");
			return false;
		}else if ($("#th").val() == "") {
			top.$.jBox.tip("业务类型：变更 为必输项");
			return false;
		}else if ($("#chargeMethodPos").attr("checked") != "checked"
				&& $("#chargeMethodMoney").attr("checked") != "checked") {
			top.$.jBox.tip("缴费方式至少需要选择一项");
			return false;
		}else {
				var tempId = $("#tempId").val();
				var url="${ctx}/signature/signatureChargeAgent/checkName";
				$.ajax({
					url:url,
					type:"POST",
					data:{"tempName":tempName,"Id":tempId,"_":new Date().getTime()},
					dataType:"JSON",
					success:function(data)
					{
						if(data.status==1)
							{
								top.$.jBox.tip("计费策略名称："+tempName+"已存在！");
								return false;
							}else
								{
									$("#inputForm").submit();
								}
					}
				});
		}
	}
</script>

<script type="text/javascript">
	var add = '<select name="addYear" id="addDiv"><option value="1">一年</option><option value="2">二年</option><option value="4">四年</option></select><input type="text" name="addMoney">';
	var update = ''
	function addLine(obj) {
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/signature/signatureChargeAgent/getChargeAgentList">印章计费策略模板列表</a></li>
		<li class="active"><a
			href="${ctx}/signature/signatureChargeAgent/form?id=''">印章计策策略模板修改</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="signatureConfigChargeAgent"
		action="${ctx}/signature/signatureChargeAgent/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>计费策略模板名称:</label>
						<div class="controls">
							<input type="text" name="tempName" id="tempName"
								value="${signatureConfigChargeAgent.tempName}" onblur="checkName()">
							<input type="hidden" name="tempId" id="tempId" value="${Id}"/>
						</div>
					</div> 
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">业务类型:</label>
						<div class="controls"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>新增:</label>
						<div class="controls">
							一年：<input type="text" id="xz1" name="addMoney"
								value="<fmt:formatNumber value='${xz1}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
				<td><div class="control-group">
						<div class="controls">
							两年：<input type="text" id="xz2" name="addMoney"
								value="<fmt:formatNumber value='${xz2}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<div class="controls">
							三年：<input type="text" id="xz3" name="addMoney"
								value="<fmt:formatNumber value='${xz3}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
				<td><div class="control-group">
						<div class="controls">
							四年：<input type="text" id="xz4" name="addMoney"
								value="<fmt:formatNumber value='${xz4}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<div class="controls">
							五年：<input type="text" id="xz5" name="addMoney"
								value="<fmt:formatNumber value='${xz5}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>更新:</label>
						<div class="controls">
							一年：<input type="text" id="gx1" name="updateMoney"
								value="<fmt:formatNumber value='${gx1}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
				<td><div class="control-group">
						<div class="controls">
							两年：<input type="text" id="gx2" name="updateMoney"
								value="<fmt:formatNumber value='${gx2}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<div class="controls">
							三年：<input type="text" id="gx3" name="updateMoney"
								value="<fmt:formatNumber value='${gx3}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
				<td><div class="control-group">
						<div class="controls">
							四年：<input type="text" id="gx4" name="updateMoney"
								value="<fmt:formatNumber value='${gx4}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			
			<tr>
				<td>
					<div class="control-group">
						<div class="controls">
							五年：<input type="text" id="gx5" name="updateMoney"
								value="<fmt:formatNumber value='${gx5}' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>

					<div class="control-group">
						<label class="control-label"><font color="red">*</font>变更:</label>
						<div class="controls">
							<input type="text" id="th" name="changeMoney"
								value="<fmt:formatNumber value='${th }' pattern='#,###0'/>"
								maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'') "
								>
						</div>
					</div>
				</td>
				<td>
			</td>
			</tr>
			<tr>
				<td>
					<div class="control-group" id="chargeMethods">
						<label class="control-label"><font color="red">*</font>付款方式:</label>
						<div class="controls">
						
							<input name="chargeMethodPos" id="chargeMethodPos"
								type="checkbox" value="0" <c:if test="${signatureConfigChargeAgent.chargeMethodPos==true }">checked="checked"</c:if>>pos&nbsp;&nbsp;&nbsp; <input
								name="chargeMethodMoney" id="chargeMethodMoney"
								type="checkbox" value="0" <c:if test="${signatureConfigChargeAgent.chargeMethodMoney==true }">checked="checked"</c:if>>现金缴费
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="form-actions">
			<shiro:hasPermission name="profile:configChargeAgent:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button"
					value="保 存" onclick="checkUsed();" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
				<input type="hidden" name="signatureConfigChargeAgentId" value="${signatureConfigChargeAgent.id}">
		</div>
	</form:form>
</body>
</html>
