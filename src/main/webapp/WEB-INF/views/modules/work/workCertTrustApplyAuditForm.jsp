<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>申请移动设备数量管理</title>
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
		function forbid(){
			top.$.jBox.open("iframe:${ctx}/work/workCertTrustApply/forbidForm?applyId="+$("#applyId").val(), "审核拒绝",600,400,{
				buttons:{"确定":"ok", "取消":true}, bottomText:"填写审核拒绝的原因",submit:function(v, h, f){
					//nodes = selectedTree.getSelectedNodes();
					if (v=="ok"){
						var suggest = h.find("iframe")[0].contentWindow.$("#suggest").val();
						window.location.href = "${ctx}/work/workCertTrustApply/audit?agree=0&applyId="+$("#applyId").val()+"&suggest="+suggest;
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="workCertTrustApply" action="${ctx}/work/workCertTrustApply/auditNext" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 80%">
		<thead>
		<th colspan="2">基本信息</th>
		</thead>
		<tbody>
		<tr>
			<td>业务编号</td>
			<td>${trust.sn }</td>
		</tr>
		<tr>
			<td>待办应用</td>
			<td>${dealInfo.configApp.appName }</td>
		</tr>
		<tr>
			<td>产品</td>
			<td>${productType[dealInfo.configProduct.productName] }</td>
		</tr>
		<tr>
			<td>产品标识</td>
			<td>
			<c:if test="${dealInfo.configProduct.productLabel==1 }">
			通用
			</c:if>
			<c:if test="${dealInfo.configProduct.productLabel==0 }">
			专用
			</c:if>
			</td>
		</tr>
		<tr>
			<td>业务类型</td>
			<td>申请可信移动设备</td>
		</tr>
		<tr>
			<td>申请数量</td>
			<td>
			${trust.applyCount }
			</td>
		</tr>
		<tr>
			<td>授权期限</td>
			<td>
			<fmt:formatDate value="${trust.workCertInfo.notafter }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		</tbody>
	</table>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width: 80%">
		<thead>
		<th colspan="2">主身份证书信息</th>
		</thead>
		<tbody>
		<tr>
			<td style="width:33%">key序列号</td>
			<td>${trust.keySn }</td>
		</tr>
		<tr>
			<td>证书序列号</td>
			<td>${trust.certSn }</td>
		</tr>
		<tr>
			<td>主题</td>
			<td>
			${trust.workCertInfo.subjectDn }
			</td>
		</tr>
		<tr>
			<td>签发时间</td>
			<td>
			<fmt:formatDate value="${trust.workCertInfo.notbefore }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		<tr>
			<td>到期时间</td>
			<td>
			<fmt:formatDate value="${trust.workCertInfo.notafter }" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
		</tbody>
	</table>
	<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed" style="width: 80%">
					<tbody>
						<tr><th colspan="5">工作记录信息</th></tr>
						<tr>
							<th>编号</th>
							<th>记录内容</th>
							<th>记录人</th>
							<th>受理网点</th>
							<th>记录时间</th>
						</tr>
						<tr>
							<td>1</td>
							<td><input type="text" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	
	
		<div class="form-actions" style="width:80%">
			<input name="workCompanyId" type="hidden" value="${dealInfo.workCompany.id }">
			<input name="appId" type="hidden" value="${dealInfo.configApp.id }">
			<input name="id" type="hidden" value="${trust.id }" id="applyId">
			<input name="dealInfoId" type="hidden" value="${dealInfo.id }">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="下一步"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="forbid()"
					value="拒绝" />
		</div>
	</form:form>
</body>
</html>
