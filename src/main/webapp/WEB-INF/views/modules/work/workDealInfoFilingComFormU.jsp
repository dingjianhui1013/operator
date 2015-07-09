<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>联系人管理</title>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/work/workDealInfoFiling/formF?uid=${workDealInfo.id}">单位基本信息查看</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/userlist?uid=${workDealInfo.id}">联系人列表</a></li>
		<li ><a href="${ctx}/work/workDealInfoFiling/loglist?uid=${workDealInfo.id}">客服记录</a></li>
	</ul><br/>
	<div class="form-horizontal">
		<tags:message content="${message}"/>
		<c:if test="${key==0 }">
			<div class="control-group">
				<label class="control-label">暂无公司信息！</label>
			</div>
		</c:if>
		<c:if test="${key!=0 }">
		<div class="control-group">
			<label class="control-label">用户ID:</label>
			<div class="controls">
				${workDealInfo.id}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位名称:</label>
			<div class="controls">
				${workDealInfo.workCompany.companyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位类型:</label>
			<div class="controls">
				<c:if test="${workDealInfo.workCompany.companyType==1}">企业</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==2}">事业单位</c:if>
				<c:if test="${workDealInfo.workCompany.companyType==3}">政府机构</c:if>
				<c:if test="${workUser.workCompany.companyType==4}">社会团体</c:if>
				<c:if test="${workUser.workCompany.companyType==5}">其他</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组织机构代码:</label>
			<div class="controls">
				${workDealInfo.workCompany.organizationNumber}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组织机构代码有效期:</label>
			<div class="controls">
				<fmt:formatDate value="${workDealInfo.workCompany.orgExpirationTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">法人姓名:</label>
			<div class="controls">
				${workDealInfo.workCompany.legalName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">行政所属区:</label>
			<div class="controls">
				${workDealInfo.workCompany.province}&nbsp;
				${workDealInfo.workCompany.city}&nbsp;
				${workDealInfo.workCompany.district}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">街道地址:</label>
			<div class="controls">
				${workDealInfo.workCompany.address}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息:</label>
			<div class="controls">
				${workDealInfo.workCompany.remarks}
			</div>
		</div>
		</c:if>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
</body>
</html>
