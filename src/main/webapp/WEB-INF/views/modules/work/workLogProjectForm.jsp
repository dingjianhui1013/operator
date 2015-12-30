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
		<li class="active"><a href="${ctx}/work/workLog/form?id=${workLog.id}">项目记录<shiro:hasPermission name="work:workLog:edit">${not empty workLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="work:workLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form name = "customerInsert" id = "inputForm" action ="${ctx}/work/customer/insertCustomer"  class="form-horizontal">
		<tags:message content="${message}"/>
		<input type = "hidden" name = "dealInfoId" value = "${workLog.id}"/>
		<div class="control-group">
			<label class="control-label">应用项目名称:</label>
			<div class="controls">
				${workLog.configApp.appName}
			</div>
		</div>
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label">应用名称:</label> -->
<!-- 			<div class="controls"> -->
<%-- 				${workLog.configApp.appName } --%>
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="control-group">
			<label class="control-label">服务方式:</label>
			<div class="controls">
				<select name = "access" disabled="disabled">
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
				<select id="serType" name = "serType" disabled="disabled">
					<option <c:if test="${workLog.serType=='日常客服' }">selected="selected"</c:if> value = "日常客服">日常客服</option>
					<option <c:if test="${workLog.serType=='温馨提示' }">selected="selected"</c:if> value = "温馨提示">温馨提示</option>
					<option <c:if test="${workLog.serType=='更新提示' }">selected="selected"</c:if> value = "更新提示">更新提示</option>
					<option <c:if test="${workLog.serType=='回访' }">selected="selected"</c:if> value = "回访">回访</option>
					<option <c:if test="${workLog.serType=='提示' }">selected="selected"</c:if> value = "提示">提示</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量:</label>
			<div class="controls">
				<input type = "text"  disabled="disabled" value = "${workLog.count }" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本号:</label>
			<div class="controls">
				<input type = "text"  disabled="disabled" value = "${workLog.versionNumber }" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务咨询</label>
			<br>
			<div class="controls">
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'新办')==true}"> checked="checked"</c:if> name="ywzx">新办
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'更新')==true}"> checked="checked"</c:if> name="ywzx">更新 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'解锁')==true}"> checked="checked"</c:if> name="ywzx">解锁 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'变更')==true}"> checked="checked"</c:if> name="ywzx">变更 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'补办')==true}"> checked="checked"</c:if> name="ywzx">补办
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'用途')==true}"> checked="checked"</c:if> name="ywzx">用途 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'密码')==true}"> checked="checked"</c:if> name="ywzx">密码 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'授权')==true}"> checked="checked"</c:if> name="ywzx">授权 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywzx,'合作')==true}"> checked="checked"</c:if> name="ywzx">合作
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务操作</label>
			<br>	
			<div class="controls">
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'驱动')==true}"> checked="checked"</c:if> name="ywcz">驱动
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'更新')==true}"> checked="checked"</c:if> name="ywcz">更新 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'解锁')==true}"> checked="checked"</c:if> name="ywcz">解锁 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'网络')==true}"> checked="checked"</c:if> name="ywcz">网络 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'key')==true}"> checked="checked"</c:if> name="ywcz">key
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'控件')==true}"> checked="checked"</c:if> name="ywcz">控件 
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywcz,'浏览器')==true}"> checked="checked"</c:if> name="ywcz">浏览器 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务系统</label>
			<br>
			<div class="controls">
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywxt,'业务咨询')==true}"> checked="checked"</c:if> name="ywxt">业务咨询
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywxt,'操作咨询')==true}"> checked="checked"</c:if> name="ywxt">操作咨询
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywxt,'绑定操作')==true}"> checked="checked"</c:if> name="ywxt">绑定操作
				<input type="checkbox" disabled="disabled" <c:if test="${fn:contains(workLog.ywxt,'系统出错')==true}"> checked="checked"</c:if> name="ywxt">系统出错
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">其他:</label>
			<div class="controls">
				<input type = "text"  disabled="disabled" value = "${workLog.probleType}" class="required" onkeyup="value=this.value.search(/^[a-zA-Z\u4e00-\u9fa5]+$/)?'':value"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">记录人员:</label>
			<div class="controls">
				<input type = "text"  disabled="disabled" value = "${workLog.createBy.name }" class="required" onkeyup="value=this.value.search(/^[a-zA-Z\u4e00-\u9fa5]+$/)?'':value"/>
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
				<input <c:if test="${workLog.completeType==0 }">checked="checked"</c:if> type="radio" value = "0" name = "completeType" disabled="disabled" />是
				<input <c:if test="${workLog.completeType==1 }">checked="checked"</c:if> type="radio" value = "1" name = "completeType"  disabled="disabled"/>否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">遗留问题:</label>
			<div class="controls">
				<textarea disabled="disabled" id="remarks" class="valid" cols="4" rows="4" style="resize: none;" name="leftoverProblem">${workLog.leftoverProblem }</textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>
