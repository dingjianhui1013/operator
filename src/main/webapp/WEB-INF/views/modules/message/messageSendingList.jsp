<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息发送管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function addOffice() {
		var areaId = $("#areaId").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId + "&_=" + new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#officeId").html(html);
		});

	}
	function checkAll(obj) {
		var check = $($("#contentTable").find("#checkAll"));
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (check.is(":checked") == true) {
			$('input:checkbox').each(function() {
				$(this).attr('checked', true);
			});
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == true) {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne) < 0) {
						if (checkIds == '') {
							checkIds += check.val();
						} else {
							checkIds += "," + check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds == ",") {
				$("#checkIds").val("");
			} else {
				$("#checkIds").val(checkIds);
			}
		} else {
			$('input:checkbox').each(function() {
				$(this).attr('checked', false);
			});
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == false) {
					checkIds = checkIds.replace(check.val(), "");
					checkIds = checkIds.replace(",,", ",");
				}
			}
			if (checkIds == ",") {
				$("#checkIds").val("");
			} else {
				$("#checkIds").val(checkIds);
			}
		}
	}

	function changeCheck(obj) {
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (checkIds.indexOf($(obj).val()) > -1) {
			checkIds = checkIds.replace($(obj).val(), "");
		}
		for (var a = 0; a < xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == true) {
				var checkOne = check.val();
				if (checkIds.indexOf(checkOne) < 0) {
					if (checkIds == '') {
						checkIds += check.val();
					} else {
						checkIds += "," + check.val();
					}
				}
			}
		}
		checkIds = checkIds.replace(",,", ",");
		if (checkIds == ",") {
			$("#checkIds").val("");
		} else {
			$("#checkIds").val(checkIds);
		}
	}
	function selectData()
	{
		var apply = $("#apply").val();
		var companyName = $("#companyName").val();
		var contactName = $("#contactName").val();
		var workType = $("#workType").val();
		var dealInfoStatus = $("#dealInfoStatus").val();
		var areaId = $("#areaId").val();
		var officeId = $("#officeId").val();
		var province = $("#s_province").val();
		var city = $("#s_city").val();
		var district = $("#s_county").val();
		if(apply==null || apply=="")
			{
				$.jBox.tip("请选择应用！");
			}else{
					var url="${ctx}/message/messageSending/selectData";
					$.ajax({
						url:url,
						type:"POST",
						data:{"apply":apply,"companyName":companyName,"contactName":contactName,"workType":workType,
								"dealInfoStatus":dealInfoStatus,"areaId":areaId,"officeId":officeId,
								"province":province,"city":city,"district":district},
						dataType:"text",
						success:function(data)
						{
							$("#checkIds").val(data);
							var xz = $("#contentTable").find("[name='oneDealCheck']");
							for (var a = 0; a < xz.length; a++) {
								var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
								if (check.is(":checked") == false) {
									check.attr("checked","true");
								}
							}
						}
					});
			}
	}
	function send() {
		var checkIds = $("#checkIds").val();
		var apply = $("#apply").val();
		var companyName = $("#companyName").val();
		var contactName = $("#contactName").val();
		var workType = $("#workType").val();
		var dealInfoStatus = $("#dealInfoStatus").val();
		var areaId = $("#areaId").val();
		var officeId = $("#officeId").val();
		var smsId = $("#smsId").val();
		 if (checkIds == null || checkIds == "") {
			$.jBox.tip("请选择需要发送信息的公司！");
		}
		if ($("#smsId").val() == null || $("#smsId").val() == "") {
			$.jBox.tip("请选择短信模板！");
			}
		if(smsId!="" && checkIds !="")
				{
				var url = "${ctx}/message/messageSending/send?checkIds=" + checkIds
				+ "&apply=" + apply + "&companyName=" + companyName
				+ "&contactName=" + contactName + "&workType=" + workType
				+ "&dealInfoStatus=" + dealInfoStatus + "&areaId=" + areaId
				+ "&officeId=" + officeId + "&smsId=" + smsId + "&_="
				+ new Date().getTime();
		var submit = function(v, h, f) {
			if (v == true) {
				$.getJSON(url, function(data) {
					if (data.status == '1') {
						top.$.jBox.tip("发送完成");
						setTimeout(function() {
							//something you want delayed
							$("#searchForm").submit();
// 							window.location.reload();
							window.location.href="${ctx}/message/messageSending/";
						}, 1500); // how long do you want the delay to be? 

					} else if (data.status == '-1') {
						top.$.jBox.tip("发送失败!");
						var info = "失败信息:<br>" + data.msg;
						top.$.jBox.info(info);
						//top.$.jBox.tip("上传失败"+data.msg);
						//$("#searchForm").submit();
					} else {
						top.$.jBox.tip("上传失败!");
						var info = "失败信息:<br>" + data.msg;
						top.$.jBox.info(info);
						//top.$.jBox.tip("上传失败："+data.errorMsg);
						//$("#searchForm").submit();
					}

				});

			}

		};
		var updateUrl = "${ctx}/message/messageSending/checkone?checkIds="
				+ checkIds + "&apply=" + apply + "&companyName=" + companyName
				+ "&contactName=" + contactName + "&workType=" + workType
				+ "&dealInfoStatus=" + dealInfoStatus + "&areaId=" + areaId
				+ "&officeId=" + officeId + "&smsId=" + smsId + "&_="
				+ new Date().getTime()

		$.getJSON(	updateUrl,
						function(data) {

							var html = "";
							if (data.status == 1) {

								html = "<label class='control-label'>总共条数:</label><div class='controls'>"
										+ data.size + "</div>";
								html += "<label class='control-label'>短信内容:</label><div class='controls'>"
										+ data.content + "</div>";

							}

							top.$.jBox.confirm(html, "短信内容", submit, {
								buttons : {
									'确认发送' : true,
									'返回' : false
								}
							});
						});
				}
		
		
	}
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/message/smsConfiguration/">短信配置</a></li>
		<li class="active"><a href="${ctx}/message/messageSending/list">消息发送</a></li>
		<li><a href="${ctx}/message/messageSending/search">消息查看</a></li>
		<li><a href="${ctx}/message/emailExtraction/list">邮箱提取</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="messageSending"
		action="${ctx}/message/messageSending/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>

			<label>应用名称：</label> <select name="apply" id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select> <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" id="companyName" />

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <label>证书持有人：</label>
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="16" class="input-medium" id="contactName" />
		</div>
		<br>
		<div>
			<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> <label>选择区域：</label> <select name="areaId" id="areaId"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>选择网点 ：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="selectData()"
				value="选择全部数据" />
		</div>
		<br>
		<div>
		<label>业务状态：</label>
		<select name="dealInfoStatus" id="dealInfoStatus">
			<option value="">请选择业务类型</option>
			<c:forEach items="${wdiStatus}" var="type">
				<option value="${type.key}"
					<c:if test="${type.key==dealInfoStatus}">
					selected="selected"
					</c:if>>${type.value}</option>
			</c:forEach>
		</select>


		<label>短信模板：</label>
		<select name="smsId" id="smsId">
			<option value="">请选择</option>
			<c:forEach items="${smsConfigurationList}" var="sms">
				<option value="${sms.id}"
					<c:if test="${sms.id==smsId}">
					selected="selected"
					</c:if>>${sms.messageName}</option>
			</c:forEach>
		</select>
		<label>行政所属区：</label>
		<select id="s_province" name="workCompany.province"
			style="width: 100px;">
		</select>
								
								&nbsp;&nbsp; 
								<select id="s_city" name="workCompany.city"
			style="width: 100px;">
		</select>&nbsp;&nbsp; <select id="s_county" name="workCompany.district"
			style="width: 100px;">
		</select>
		<script type="text/javascript">
			_init_area();
			$("#s_province")
					.append(
							'<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
			$("#s_city")
					.append(
							'<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
			$("#s_county")
					.append(
							'<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
		</script>
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
			<a href="javascript:send()" class="btn btn-primary">发送</a> <input
				type="hidden" name="checkIds" id="checkIds" value="${checkIds }" />
				
		</div>
	
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" name="dealCheck"
					value=""
					<%-- 	<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
				</c:forEach> --%>
					onchange="checkAll(this)" /></th>
				<th>序号</th>
				<th>应用名称</th>
				<th>单位名称</th>
				<th>经办人名称</th>
				<th>证书持有人电话</th>
				<th>经办人邮箱</th>
				<th>到期时间</th>

			</tr>
		</thead>
		<tbody>
			<c:set var="zje" value="0" />
			<c:forEach items="${page.list}" var="messageSend" varStatus="status">

				<tr>
					<td><c:if test="${messageSend.workUser.contactPhone!=null}">
							<input type="checkbox" name="oneDealCheck"
								value="${messageSend.id}"
								<c:forEach items="${ids }" var="id">
							<c:if test="${id==messageSend.id }"> checked="checked"</c:if>
						</c:forEach>
								onchange="changeCheck(this)" id="oneDealCheck" />
						</c:if></td>

					<td>${status.index+1 }</td>
					<td>${messageSend.configApp.alias}</td>
					<td>${messageSend.workCompany.companyName}</td>
					<td>${messageSend.workCertInfo.workCertApplyInfo.name}</td>
					<td>${messageSend.workUser.contactPhone}</td>
					<td>${messageSend.workCertInfo.workCertApplyInfo.email}</td>
					<td><fmt:formatDate value="${messageSend.notafter }"
							pattern="yyyy-MM-dd" /></td>
				</tr>

			</c:forEach>
			<shiro:hasPermission name="settle:settleKey:edit">
				<td></td>
			</shiro:hasPermission>

		</tbody>
	</table>


	<div class="pagination">${page}</div>
</body>
</html>
