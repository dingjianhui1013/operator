<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>单位名称管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfoFiling/ulist">咨询类用户</a></li>
		<li class="active"><a href="${ctx}/work/customer/insertUserFrom">新增用户</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="workCompany"
		action="${ctx}/work/customer/insertUser" method="post"
		class="form-horizontal">
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">联系人信息</label>
		</div>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">姓名:</label>
						<div class="controls">
							<input type="text" name="contactName" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">性别:</label>
						<div class="controls">
							<input type="radio" value="男" name="contactSex" />男 <input
								type="radio" value="女" name="contactSex" class="required"/>女
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">移动电话:</label>
						<div class="controls">
							<input type="text" name="contactPhone"  maxlength="11" class="mobile" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">部门名称:</label>
						<div class="controls">
							<input type="text" name="department"/>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">固定电话:</label>
						<div class="controls">
							<input type="text" name="contactTel" maxlength="13" class="number"/>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">联系地址:</label>
						<div class="controls">
							<input type="text" name="address" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">电子邮箱:</label>
						<div class="controls">
							<input type="text" name="contactEmail" class="email" />
						</div>
					</div>
				</td>
				<td></td>
			</tr>
		</table>
		<div class="control-group">
			<label class="control-label">单位基本信息</label>
		</div>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">单位名称:</label>
						<div class="controls">
							<input type="text" name="companyName" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">组织机构代码证号:</label>
						<div class="controls">
							<input type="text" name="organizationNumber" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">单位类型:</label>
						<div class="controls">
							<select name="companyType">
								<option value="1">企业</option>
								<option value="2">事业单位</option>
								<option value="3">政府机关</option>
								<option value="4">社会团体</option>
								<option value="5">其他</option>
							</select>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">组织机构代码证有效期:</label>
						<div class="controls">
							<input id="endTime" name="endTime" type="text"
								readonly="readonly" maxlength="10" class="Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">行政所属区:</label>
						<div class="controls">
							<select id="s_province" name="province" style="width: 100px;">
							</select>&nbsp;&nbsp; <select id="s_city" name="city"
								style="width: 100px;"></select>&nbsp;&nbsp; <select
								id="s_county" name="district" style="width: 100px;"></select>
							<script type="text/javascript" src="area.js"></script>
							<script type="text/javascript">
								_init_area();
							</script>
							<div id="show"></div>
						</div>
					</div>

				</td>
				<td>
					<div class="control-group">
						<label class="control-label">法人姓名:</label>
						<div class="controls">
							<input type="text" name="legalName" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">街道地址:</label>
						<div class="controls">
							<input type="text" name="address" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">备注信息:</label>
						<div class="controls">
							<textarea id="remarks" class="valid" cols="4" rows="4"
								style="resize: none;" name="remarks"></textarea>
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="下一步" /> <input id="btnCancel" class="btn" type="button"
				value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
