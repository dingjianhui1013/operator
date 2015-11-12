<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商应用计费策略模板管理</title>
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
				$("input[name='tempStyle']").bind("click", this, function() {
					var v = $(this).val();
					if (v == "1") {
						$("#chargeMethods").show();
					} else {
						$("#chargeMethods").hide();
					}
				});
			});
</script>

<script type="text/javascript">
	var add = '<select name="addYear" id="addDiv"><option <c:if test="${configChargeAgentDetail.chargeYear==1}">selected</c:if> value="1">一年</option><option <c:if test="${configChargeAgentDetail.chargeYear==2}">selected</c:if> value="2">二年</option><option <c:if test="${configChargeAgentDetail.chargeYear==4}">selected</c:if> value="4">四年</option></select><input type="text" name="addMoney">';
	var update = '';

	function addLine(obj) {
	}
</script>
</head>
<body>
	<form:form class="form-horizontal">
		<tags:message content="${message}" />
		<br>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">计费策略模板名称:</label>
						<div class="controls" style="margin-top: 3px;">
							${configChargeAgent.tempName }</div>
					</div>
				</td>
				<td></td>
			</tr>

			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">模板类型:</label>
						<div class="controls" style="margin-top: 3px;">
							<c:if test="${configChargeAgent.tempStyle=='1'}">标准</c:if>
							<c:if test="${configChargeAgent.tempStyle=='2'}">政府统一采购</c:if>
							<c:if test="${configChargeAgent.tempStyle=='3'}">合同采购</c:if>
						</div>
					</div>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">新增:</label>
						<div class="controls" style="margin-top: 2px;">
							一年：
							<fmt:formatNumber pattern="#,##0.00">${xz1}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两年：
						<fmt:formatNumber pattern="#,##0.00">${xz2}</fmt:formatNumber>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							四年：
							<fmt:formatNumber pattern="#,##0.00">${xz4}</fmt:formatNumber>
						</div>
					</div></td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;五年：
						<fmt:formatNumber pattern="#,##0.00">${xz5}</fmt:formatNumber>
					</div></td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">更新:</label>
						<div class="controls" style="margin-top: 2px;">
							一年：
							<fmt:formatNumber pattern="#,##0.00">${gx1}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两年：
						<fmt:formatNumber pattern="#,##0.00">${gx2}</fmt:formatNumber>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="control-group">
						<div class="controls" style="margin-top: 2px;">
							四年：
							<fmt:formatNumber pattern="#,##0.00">${gx4}</fmt:formatNumber>
						</div>
					</div></td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;五年：
						<fmt:formatNumber pattern="#,##0.00">${gx5}</fmt:formatNumber>
					</div></td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">补办:</label>
						<div class="controls" style="margin-top: 2px;">
							遗失补办:
							<fmt:formatNumber pattern="#,##0.00">${bb0}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;损坏更换:
						<fmt:formatNumber pattern="#,##0.00">${bb1}</fmt:formatNumber>
					</div></td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">变更:</label>
						<div class="controls">${th}</div>
					</div>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">开户费:</label>
						<div class="controls">${khf}</div>
					</div>
				</td>
				<td></td>
			</tr>

			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">可信移动设备:</label>
						<div class="controls" style="margin-top: 2px;">
							半年:
							<fmt:formatNumber pattern="#,##0.00">${trustDevice0}</fmt:formatNumber>
						</div>
					</div>
				</td>
				<td><div class="control-group" style="margin-top: 2px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一年:
						<fmt:formatNumber pattern="#,##0.00">${trustDevice1}</fmt:formatNumber>
					</div></td>
			</tr>
			<tr>
				<td>
					<div class="control-group" id="chargeMethods">
						<label class="control-label">付款方式:</label>
						<div class="controls">
							<c:if test="${configChargeAgent.chargeMethodPos}">pos</c:if>
							&nbsp;&nbsp;&nbsp;
							<c:if test="${configChargeAgent.chargeMethodMoney}">现金缴费</c:if>
							&nbsp;&nbsp;&nbsp;
							<c:if test="${configChargeAgent.chargeMethodBank}">银行转账</c:if>
							&nbsp;&nbsp;&nbsp;<br>
						</div>
					</div>
				</td>
				<td></td>
			</tr>

			<c:if test="${configChargeAgent.tempStyle!='1'}">
				<tr>
					<td>
						<div class="control-group">
							<label class="control-label">开始时间:</label>
							<div class="controls" style="margin-top: 2px;">
								<fmt:formatDate pattern="yyyy-MM-dd"
									value="${configChargeAgent.htStartTime}" />
							</div>
						</div>
					</td>
					<td>
						<div class="control-group" style="margin-top: 2px;">
							截止时间: <label><fmt:formatDate pattern="yyyy-MM-dd"
									value="${configChargeAgent.htEndTime}" /></label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="control-group">
							<label class="control-label">配置新增数量:</label>
							<div class="controls" style="margin-top: 2px;">${ configChargeAgent.configureNum}</div>
						</div>
					</td>
					<td>
						<div class="control-group" style="margin-top: 2px;">
							配置更新数量: <label>${configChargeAgent.configureUpdateNum}</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="control-group">
							<label class="control-label">新增剩余数量:</label>
							<div class="controls" style="margin-top: 2px;">${configChargeAgent.surplusNum}</div>
						</div>
					</td>
					<td>
						<div class="control-group" style="margin-top: 2px;">
							更新剩余数量: <label>${configChargeAgent.surplusUpdateNum}</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="control-group">
							<label class="control-label">新增已用数量:</label>
							<div class="controls" style="margin-top: 2px;">${ configChargeAgent.availableNum + configChargeAgent.reserveNum}</div>
						</div>
					</td>
					<td>
						<div class="control-group" style="margin-top: 2px;">
							更新已用数量: <label>${ configChargeAgent.availableUpdateNum + configChargeAgent.reserveUpdateNum}</label>
						</div>
					</td>
				</tr>


			</c:if>

		</table>
	</form:form>
</body>
</html>
