<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>短信配置管理</title>
<meta name="decorator" content="default" />

	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script type="text/javascript">
	$(document).ready(function() {
	
			/* $("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“txt”格式文件！"});
			}); */
		});
	/* function addAttach() {
		if($("#fileName").val() == ""){
			top.$.jBox.tip("导入文件格式有误，导入文件应为txt文件，请确认");
        	return false;
        }
        if($("#fileName").val().indexOf('.txt')<0) {
        	top.$.jBox.tip("导入文件格式有误，导入文件应为txt文件，请确认");
            return false;
        }
		var options = {
			type : 'post',
			dataType : 'json',
			success : function(data) {
				//console.log(data);
				if(data.status=='1'){
					top.$.jBox.tip("上传成功");
					  setTimeout(function (){
	            		    //something you want delayed
	            		    	$("#searchForm").submit();
	            		//	window.location.reload();
	            		   }, 3000); // how long do you want the delay to be? 
	            
				}else if(data.status=='-1'){
					top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else{
					top.$.jBox.tip("上传失败："+data.errorMsg);
					//$("#searchForm").submit();
				}
			}
		};
		$('#importBox').ajaxSubmit(options); 
	}*/
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="" action="${ctx}/message/smsConfiguration/import"
			method="post" enctype="multipart/form-data"
			style="padding-left: 20px; text-align: center;">
			<br /> <input id="uploadFile" name="file" type="file"
				style="width: 330px" /><br /> <br /> <input id="btnImportSubmit"
				class="btn btn-primary" type="submit" value="   导    入   " />
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
		<li class="active"><a
			href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">短信配置<shiro:hasPermission
					name="message:smsConfiguration:edit">${not empty smsConfiguration.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="message:smsConfiguration:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="smsConfiguForm" modelAttribute="smsConfiguration"
		action="${ctx}/message/smsConfiguration/save" method="post"
		class="form-horizontal">

		<input type="hidden" id="RaId" name="Id" value=${SmsConfiguration.id}>
		<tags:message content="${message}" />

		<div class="control-group">
			<label class="control-label"><span style="color: red">*</span>&nbsp;短信模板名称:</label>
			<div class="controls">
				<form:hidden path="id" htmlEscape="false" maxlength="50"
					class="required" />
				<form:input path="messageName" htmlEscape="false" maxlength="50"
					class="required" />
			</div>
			<br> 
			
			</div>
		</div>



		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp; <input id="btnCancel"
				class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>

</body>
</html>
