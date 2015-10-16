<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>key入库信息管理</title>
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
		
		function addDepotName(){
			var officeId = $("#officeId").val();
			var url ="${ctx}/sys/office/getDepotName?offId="
			$.getJSON(url+officeId+"&_="+new Date().getTime(),function(data){
					if (data.status==1) {
						 $("#depotName").attr("value",data.name+"库房");
					}
			});
		 }
		
		function addOffice(){
		var areaId = $("#areaId").val();
		var url = "${ctx}/sys/office/addOffices?areaId=";
		 $.getJSON(url+areaId+"&_="+new Date().getTime(),function(data){
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data,function(idx,ele){
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">"+ele.name	+"</ooption>"
			});
			$("#officeId").html(html);
		});
	 }
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/key/keyUsbKeyDepot/">库存管理</a></li>
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/form?id=${keyUsbKeyDepot.id}"><shiro:hasPermission name="key:keyUsbKeyDepot:edit">${not empty keyUsbKeyDepot.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="key:keyUsbKeyDepot:edit"></shiro:lacksPermission>库房</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="keyUsbKeyDepot" action="${ctx}/key/keyUsbKeyDepot/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>区域名称:</label>
			<div class="controls">
				<select name="areaId" id="areaId"  class="required" onchange="addOffice()" 
				<c:if  test="${not empty keyUsbKeyDepot.id}">
					disabled="true" 
				</c:if>
				>
					<option value="">请选择</option>
					<c:forEach items="${offices}" var="office">
						<option value="${office.id }" 
						<c:if  test="${ keyUsbKeyDepot.office.parent.id==office.id}">
							selected="selected" 
						</c:if>
						>${office.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>网点名称:</label>
			<div class="controls">
			
					<select name="officeId" id="officeId" class="required"  onchange="addDepotName()" 
						<c:if  test="${not empty keyUsbKeyDepot.id}">
							disabled="true" 
						</c:if>
					 onchange="" >
					<c:choose>	
						<c:when test="${not empty keyUsbKeyDepot.id}"> 
	   							<option value="${keyUsbKeyDepot.office.id}">${keyUsbKeyDepot.office.name}</option>
	   						</c:when>
	   						<c:otherwise> 
	   							<option value="">请选择</option>
	   						</c:otherwise>
						</c:choose>	
						
						<c:if  test="${not empty offids}">
								<c:forEach items="${offids}" var="off">
								<option value="${off.id }" 
								<c:if  test="${ keyUsbKeyDepot.office.id==off.id}">
									selected="selected" 
								</c:if>
								>${off.name }</option>
								</c:forEach>
						</c:if>
					</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>库房名称:</label>
			<div class="controls">
				<form:input path="depotName" htmlEscape="false" maxlength="30" 
				class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>预警人姓名:</label>
			<div class="controls">
				<form:input path="warningName" id="warningName" htmlEscape="false" maxlength="50" 
				class="required"/>
			</div>
		</div>
		<div class="control-group">
		<font size="4">联系人信息：</font>
		</div>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>联系人名称:</label>
						<div class="controls">
							<form:input path="linkmanName" htmlEscape="false" maxlength="10" 
							class="required"/>
						</div>	
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">部门名称:</label>
						<div class="controls">
							<form:input path="linkmanOrg" htmlEscape="false" 
							maxlength="30" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">性别:</label>
						<div class="controls">
						<input type="radio"  checked="checked"  name="linkmanSex" value="1"
						<c:if  test="${keyUsbKeyDepot.linkmanSex==1}">
								checked="checked"
							</c:if>
						
						>男
						<input type="radio"  name="linkmanSex" value="0"
						<c:if  test="${keyUsbKeyDepot.linkmanSex==0}">
								checked="checked"
							</c:if>
						>女
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">邮政邮编:</label>
						<div class="controls">
							<%-- <form:input path="linkmanPost" id="" htmlEscape="false" maxlength="50"  onafterpaste="this.value=this.value.replace(/\D/g,"")" onkeyup="this.value=this.value.replace(/\D/g,"")" /> --%>
							<input type="text" name="linkmanPost" value="${keyUsbKeyDepot.linkmanPost} " onafterpaste="this.value=this.value.replace(/\D/g,'')" >
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>移动电话:</label>
						<div class="controls">
							<form:input path="linkmanMobilePhone"  
							id="contactPhone" htmlEscape="false" maxlength="11" 
							cssClass="required mobile"/>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>电子邮箱:</label>
						<div class="controls">
							<form:input path="linkmanEmail" id="contacEmail" 
							htmlEscape="false" maxlength="50" cssClass="required email"/>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">固定电话:</label>
						<div class="controls">
							<form:input path="linkmanTel" htmlEscape="false" class="number"
							maxlength="12"  />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">联系地址:</label>
						<div class="controls">
							<form:input path="linkmanLocation" htmlEscape="false" 
							maxlength="80" />
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyUsbKeyDepot:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
