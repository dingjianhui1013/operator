<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>新建记录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#serType").change(function(){
				if($("#serType").val()!="日常客服"){
					$("#wtlx").attr('style',"display:none");
				}else{
					$("#wtlx").attr('style',"display:display");
				}
			});
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/work/customer/list">工作记录</a></li>
		<li  class="active"><a href="${ctx}/work/customer/insertCustomerFrom?id=${workDealInfo.id}">新增客服记录</a></li>
	</ul><br/>
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/customer/insertCustomer"  method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<input type = "hidden" name = "dealInfoId" value = "${workDealInfo.id}"/>
		<div class="control-group">
			<label class="control-label">服务对象:</label>
			<div class="controls">
				${workDealInfo.workCompany.companyName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">客服接入:</label>
			<div class="controls">
				<select name = "access">
					<option value = "电话">电话</option>
					<option value = "QQ">QQ</option>
					<option value = "QQ远程">QQ远程</option>
					<option value = "在线工具">在线工具</option>
					<option value = "邮件">邮件</option>
					<option value = "短信">短信</option>
					<option value = "其他">其他</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务类型:</label>
			<div class="controls">
				<select id= "serType" name = "serType">
					<option value = "日常客服">日常客服</option>
					<option value = "温馨提示">温馨提示</option>
					<option value = "更新提示">更新提示</option>
					<option value = "回访">回访</option>
					<option value = "培训">培训</option>
				</select>
			</div>
		</div>
		
		<div id="wtlx" class="control-group">
			<label class="control-label">问题类型:</label>
			<div class="controls">
				<select name = "probleType">
					<option value = "业务咨询">业务咨询</option>
					<option value = "环境">环境</option>
					<option value = "驱动">驱动</option>
					<option value = "key">key</option>
					<option value = "网络">网络</option>
					<option value = "解锁">解锁</option>
					<option value = "更新操作">更新操作</option>
					<option value = "业务系统">业务系统</option>
					<option value = "业务操作">业务操作</option>
					<option value = "其他">其他</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;服务主题:</label>
			<div class="controls">
				<input type = "text" name = "serTitle" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">详细记录:</label>
			<div class="controls">
				<textarea  class="valid" cols="4" rows="4" style="resize: none;" name="recordContent"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录人员:</label>
			<div class="controls">
				${userName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录时间:</label>
			<div class="controls">
				${nowDate }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否完成:</label>
			<div class="controls">
				<input type="radio" value = "0" name = "completeType" />是
				<input type="radio" value = "1" name = "completeType" />否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">遗留问题:</label>
			<div class="controls">
				<textarea id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="leftoverProblem"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
