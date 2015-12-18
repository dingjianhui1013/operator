<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>工作记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if($("#serType").val()=="日常客服"){
				$("#wtlx").attr("style","display:none");
			}
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
		<li class="active"><a href="${ctx}/work/workLog/form?id=${workLog.id}">修改客服记录</a></li>
	</ul><br/>
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/workLog/saveK"  class="form-horizontal" method="post">
		<tags:message content="${message}"/>
		<input type = "hidden" name = "dealInfoId" value = "${workLog.workDealInfo.id}"/>
		<input type = "hidden" name = "id" value = "${workLog.id}"/>
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
				${workLog.workCompany.companyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">应用名称:</label>
			<div class="controls">
				${workLog.configApp.appName }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">客服接入:</label>
			<div class="controls">
				<select name = "access">
					<option <c:if test="${workLog.access=='电话' }">selected="selected"</c:if>   value = "电话">电话</option>
					<option <c:if test="${workLog.access=='QQ' }">selected="selected"</c:if>  value = "QQ">QQ</option>
					<option <c:if test="${workLog.access=='QQ远程' }">selected="selected"</c:if> value = "QQ远程">QQ远程</option>
					<option <c:if test="${workLog.access=='在线工具' }">selected="selected"</c:if> value = "在线工具">在线工具</option>
					<option <c:if test="${workLog.access=='邮件' }">selected="selected"</c:if> value = "邮件">邮件</option>
					<option <c:if test="${workLog.access=='短信' }">selected="selected"</c:if> value = "短信">短信</option>
					<option <c:if test="${workLog.access=='其他' }">selected="selected"</c:if> value = "其他">其他</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务类型:</label>
			<div class="controls">
				<select  id="serType" name = "serType">
					<option <c:if test="${workLog.serType=='日常客服' }">selected="selected"</c:if> value = "日常客服">日常客服</option>
					<option <c:if test="${workLog.serType=='温馨提示' }">selected="selected"</c:if> value = "温馨提示">温馨提示</option>
					<option <c:if test="${workLog.serType=='更新提示' }">selected="selected"</c:if> value = "更新提示">更新提示</option>
					<option <c:if test="${workLog.serType=='回访' }">selected="selected"</c:if> value = "回访">回访</option>
					<option <c:if test="${workLog.serType=='提示' }">selected="selected"</c:if> value = "提示">提示</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">业务咨询</label>
			<br>
			<div class="controls">
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'新办')==true}"> checked="checked"</c:if> value="新办" name="ywzx">新办
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'更新')==true}"> checked="checked"</c:if> value="更新" name="ywzx">更新 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'解锁')==true}"> checked="checked"</c:if> value="解锁" name="ywzx">解锁 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'变更')==true}"> checked="checked"</c:if> value="变更" name="ywzx">变更 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'补办')==true}"> checked="checked"</c:if> value="补办" name="ywzx">补办
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'用途')==true}"> checked="checked"</c:if> value="用途" name="ywzx">用途 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'密码')==true}"> checked="checked"</c:if> value="密码" name="ywzx">密码 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'授权')==true}"> checked="checked"</c:if> value="授权" name="ywzx">授权 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywzx,'合作')==true}"> checked="checked"</c:if> value="合作" name="ywzx">合作
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务操作</label>
			<br>	
			<div class="controls">
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'驱动')==true}"> checked="checked"</c:if> value="驱动" name="ywcz">驱动
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'更新')==true}"> checked="checked"</c:if> value="更新" name="ywcz">更新 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'解锁')==true}"> checked="checked"</c:if> value="解锁" name="ywcz">解锁 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'网络')==true}"> checked="checked"</c:if> value="网络" name="ywcz">网络 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'key')==true}"> checked="checked"</c:if> value="key" name="ywcz">key
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'控件')==true}"> checked="checked"</c:if> value="控件" name="ywcz">控件 
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywcz,'浏览器')==true}"> checked="checked"</c:if> value="浏览器" name="ywcz">浏览器 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务系统</label>
			<br>
			<div class="controls">
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywxt,'业务咨询')==true}"> checked="checked"</c:if> value="业务咨询" name="ywxt">业务咨询
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywxt,'操作咨询')==true}"> checked="checked"</c:if> value="操作咨询" name="ywxt">操作咨询
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywxt,'绑定操作')==true}"> checked="checked"</c:if> value="绑定操作" name="ywxt">绑定操作
				<input type="checkbox"  <c:if test="${fn:contains(workLog.ywxt,'系统出错')==true}"> checked="checked"</c:if> value="系统出错" name="ywxt">系统出错
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">其他:</label>
			<div class="controls">
				<input type="text" name="probleType" value="${workLog.probleType}">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录人员:</label>
			<div class="controls">
				${userName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录时间:</label>
			<div class="controls">
				${workLog.creatTime }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否完成:</label>
			<div class="controls">
				<input <c:if test="${workLog.completeType==0 }">checked="checked"</c:if> type="radio" value = "0" name = "completeType" />是
				<input <c:if test="${workLog.completeType==1 }">checked="checked"</c:if> type="radio" value = "1" name = "completeType" />否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">遗留问题:</label>
			<div class="controls">
				<textarea  class="valid" cols="4" rows="4" style="resize: none;" name="leftoverProblem">${workLog.leftoverProblem }</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
