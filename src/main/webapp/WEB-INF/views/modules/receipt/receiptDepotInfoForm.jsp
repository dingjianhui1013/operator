<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>发票信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var offs;
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
		

		function addOffice(){
			var areaId = $("#areaId").val();
			var url = "${ctx}/sys/office/addOffices?areaId=";
			$.getJSON(url+areaId+"&_=" + new Date().getTime(),function(data){
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
		function addDepotName(){
			var officeId = $("#officeId").val();
			var url ="${ctx}/sys/office/getDepotName?offId="
			$.getJSON(url+officeId+"&_=" + new Date().getTime(),function(data){
					if (data.status==1) {
						 $("#depotName").attr("value",data.name);
					}
			});
		 }
		
		function changeOffiec(){
			if($("#officeId").val()!=""){
				var officeId = $("#officeId").val();
				var url ="${ctx}/sys/office/getDepotName?offId="
				$.getJSON(url+officeId+"&_="+new Date().getTime(),function(data){
					if (data.status==1) {
						 $("#depotName").attr("value",data.name+"库房");
					}
				});
			}
		}
		
		function checkMobile(obj) { 
			var mobie = $(obj).val();
			var regu = /^[1][0-9][0-9]{9}$/; 
			var re = new RegExp(regu); 
			if (re.test(mobie)) { 
				if($("#phonepro").text()!=""){
					$("#phonepro").hide();
				}
				return true; 
			} else { 
				if($("#phonepro").text()!=""){
					$(obj).focus(); //让手机文本框获得焦点 
					return false; 
				}
				$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
				/* top.$.jBox.tip("请输入正确的手机号码!");  */
				$(obj).focus(); //让手机文本框获得焦点 
				return false; 
			} 
		} 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptDepotInfo/">库房管理</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptDepotInfo/form?id=${receiptDepotInfo.id}">库房添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="receiptDepotInfo" action="${ctx}/receipt/receiptDepotInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>区域名称:</label>
			<div class="controls">
				<select name="areaId" id="areaId"  class="required" onchange="addOffice()" >
					<option value="">请选择</option>
					<c:forEach items="${offices}" var="office">
						<option value="${office.id }" >${office.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>网点名称:</label>
			<div class="controls">
				<select name="officeId" id="officeId" class="required" onchange="changeOffiec()"  >
				 	<option value="">请选择</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font color="red">*</font>库房名称:</label>
			<div class="controls">
				<form:input path="receiptName" id="depotName" htmlEscape="false" maxlength="50" 
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
			<h4 class="control-label">联系人信息</h4>
		</div>
		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>联系人姓名:</label>
						<div class="controls">
							<form:input path="receiptCommUser" htmlEscape="false" maxlength="50" onblur="isLetterOrChin(this)" class="required"/>
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">部门名称:</label>
						<div class="controls">
							<form:input path="department" htmlEscape="false" maxlength="50" onblur="isNumOrLetterOrChin(this)"/>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">性别:</label>
						<div class="controls">
							<form:radiobutton path="sex" value="0" />男
							<form:radiobutton path="sex" value="1" />女
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">邮政编码:</label>
						<div class="controls">
							<form:input path="codeZip" htmlEscape="false" maxlength="50" class="number"/>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>移动电话	:</label>
						<div class="controls">

							<%-- <form:input path="phone" htmlEscape="false" maxlength="50" id="contactPhone" onchange="checkMobile(this)"/>
 --%>
							<form:input path="phone" htmlEscape="false" maxlength="11" 
							cssClass="required mobile" />

						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label"><font color="red">*</font>电子邮箱:</label>
						<div class="controls">
							<form:input path="email" htmlEscape="false" maxlength="30" 
							cssClass="required email" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">固定电话:</label>
						<div class="controls">
							<form:input path="receiptCommMobile" htmlEscape="false" 
							class="number"
							maxlength="11" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">联系地址:</label>
						<div class="controls">
							<form:input path="address" htmlEscape="false" maxlength="50" />
						</div>
					</div>
				</td>
			</tr>			
		</table>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="取 消" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
