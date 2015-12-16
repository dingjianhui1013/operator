<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>短信配置管理</title>
<meta name="decorator" content="default" />
<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
</style>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnImport").click(function() {

			$.jBox($("#importBox").html(), {
				title : "导入数据",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“txt”格式文件！"
			});
		});

	});

	/* function onSubmit() {
		var file = $("#file").val();
		alert(file);
		if ($("#uploadFile").val() == "") {
			top.$.jBox.tip("未选择文件，请选择");
			return false;
		} else {
			return true;
		}
	} */
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();

		return false;
	}
	function cc() {
		var file = $("#file").val();
		alert(file);
	}
	function addAttach() {
		if($("#fileName").val() == ""){
			top.$.jBox.tip("请确认导入文件！");
        	return false;
        }
        if($("#fileName").val().indexOf('.txt')<0) {
        	top.$.jBox.tip("导入文件格式有误，导入文件应为txt文件，请确认");
            return false;
        }
        top.$.jBox.tip("正在上传文件...", 'loading');
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
	            		   }, 1500); // how long do you want the delay to be? 
	            
				}else if(data.status=='-1'){
					top.$.jBox.tip("上传失败!");
					var info = "失败信息:<br>"+data.msg;
					top.$.jBox.info(info);
					//top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else{
					top.$.jBox.tip("上传失败!");
					var info = "失败信息:<br>"+data.msg;
					top.$.jBox.info(info);
					//top.$.jBox.tip("上传失败："+data.errorMsg);
					//$("#searchForm").submit();
				}
			}
		};
		$('#materialImport').ajaxSubmit(options);
	}
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="" action="${ctx}/message/smsConfiguration/import"
			method="post" enctype="multipart/form-data"
			style="padding-left: 20px; text-align: center;">
			<br /> <input id="file" name="file" type="file" style="width: 330px" /><br />
			<br /> <input id="btnImportSubmit" class="btn btn-primary"
				onclick="onSubmit();" type="submit" value="   导    入   " />
		</form>
	</div>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/message/smsConfiguration/">短信配置列表</a></li>
		<li><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li><a href="${ctx}/message/messageSending/search">消息查看</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="smsConfiguration"
		action="${ctx}/message/smsConfiguration/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>短信模板名称 ：</label>
		<form:input path="messageName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			&nbsp;&nbsp;&nbsp;<!-- <input id="btnImport" class="btn btn-primary"
			type="button" value="导入短信模板" /> -->
		<a id="manyAdd" data-toggle="modal" href="#declareDiv"
			class="btn btn-primary">导入短信模板</a>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>短信模板名称</th>
				<shiro:hasPermission name="message:smsConfiguration:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="smsConfiguration">
				<tr>
					<td>${smsConfiguration.id}</a></td>
					<td><a
						href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">${smsConfiguration.messageName}</a></td>
					<shiro:hasPermission name="message:smsConfiguration:edit">
						<td><a
							href="${ctx}/message/smsConfiguration/form?id=${smsConfiguration.id}">修改</a>
							<a
							href="${ctx}/message/smsConfiguration/delete?id=${smsConfiguration.id}"
							onclick="return confirmx('确认要删除该短信配置吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport" action="${ctx}/message/smsConfiguration/test"
				enctype="multipart/form-data">
				<input id="fileName" name="fileName" type="file" multiple="multiple" />
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal"
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" onclick="addAttach()"
				class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
