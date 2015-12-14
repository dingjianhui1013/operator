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
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/workLog/saveFi"  class="form-horizontal" method="post">
		<tags:message content="${message}"/>
		<input type = "hidden" name = "dealInfoId" value = "${workLog.workDealInfo.id}"/>
		<input type = "hidden" name = "id" value = "${workLog.id}"/>
		<div class="control-group">
			<label class="control-label">服务对象:</label>
			<div class="controls">
			<select name="workCompany.companyName">
					<option value=""></option>
					<c:forEach items="${list}" var="company">
						<c:if test="${company.companyName!=null}"><option value="${company.companyName}" <c:if test="${workLog.workCompany.id==company.id}"> selected="selected" </c:if>>${company.companyName}</option></c:if>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">客服接入:</label>
			<div class="controls">
				<select name = "access">
					<option <c:if test="${workLog.access=='电话' }">selected="selected"</c:if>  value = "电话">电话</option>
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
			<label class="control-label">服务主题:</label>
			<div class="controls">
				<input type = "text" name = "serTitle" class="required" value = "${workLog.serTitle }" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">详细记录:</label>
			<div class="controls">
				<textarea class="valid" cols="4" rows="4" style="resize: none;" name="recordContent">${workLog.recordContent }</textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
				<input type = "text" name = "tel" class="required" value = "${workLog.serTitle }" />
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