<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>模糊记录</title>
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
		<li ><a href="${ctx}/work/customer/list">业务记录</a></li>
		<li class="active"><a href="${ctx}/work/customer/insertFuzzy">新增模糊记录</a></li>
	</ul><br/>
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/customer/insertFuzzyj" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
<%-- 		<input type = "hidden" name = "dealInfoId" value = "${workDealInfo.id}"/> --%>
			<input type = "hidden" name = "state" value = "1"/>
		<div class="control-group">
			<label class="control-label">应用项目名称:</label>
			<div class="controls">
				<select name="appName">
				<option value=""> </option>
					<c:forEach items="${configApp}" var="configApp">
						<option value="${configApp.appName}"  <c:if test="${configApp.appName==workDealInfo.configApp.appName}">selected="selected"</c:if>>${configApp.appName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">客服接入:</label>
			<div class="controls">
				<select name = "access">
					<option value = "QQ" selected="selected">QQ</option>
					<option value = "电话">电话</option>
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
		<div class="control-group">
			<label class="control-label">业务咨询</label>
			<br>
			<div class="controls">
				<input type="checkbox" value="新办" name="ywzx">新办
				<input type="checkbox" value="更新 " name="ywzx">更新 
				<input type="checkbox" value="解锁 " name="ywzx">解锁 
				<input type="checkbox" value="变更 " name="ywzx">变更 
				<input type="checkbox" value="补办" name="ywzx">补办
				<input type="checkbox" value="用途 " name="ywzx">用途 
				<input type="checkbox" value="密码" name="ywzx">密码 
				<input type="checkbox" value="授权 " name="ywzx">授权 
				<input type="checkbox" value="合作" name="ywzx">合作
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务操作</label>
			<br>	
			<div class="controls">
				<input type="checkbox" value="驱动" name="ywcz">驱动
				<input type="checkbox" value="更新 " name="ywcz">更新 
				<input type="checkbox" value="解锁 " name="ywcz">解锁 
				<input type="checkbox" value="网络 " name="ywcz">网络 
				<input type="checkbox" value="key" name="ywcz">key
				<input type="checkbox" value="控件  " name="ywcz">控件 
				<input type="checkbox" value="浏览器 " name="ywcz">浏览器 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务系统</label>
			<br>
			<div class="controls">
				<input type="checkbox" value="业务咨询" name="ywxt">业务咨询
				<input type="checkbox" value="操作咨询 " name="ywxt">操作咨询
				<input type="checkbox" value="绑定操作 " name="ywxt">绑定操作
				<input type="checkbox" value="系统出错 " name="ywxt">系统出错
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">其他</label>
			<div class="controls">
				<input type="text" name="qita"/>
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
		<input type="hidden"  name="distinguish" value="2"/>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
