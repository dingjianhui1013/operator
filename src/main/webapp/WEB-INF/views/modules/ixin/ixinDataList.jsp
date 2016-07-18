<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>IXIN数据采集管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function checkMore(){
			$("#configAppList").hide();
			$("#configAppListAll").show();
		}
		
		function checkLetter(){
			$("#configAppList").show();
			$("#configAppListAll").hide();
		}
		
		//选择项目类型
		function changeAppType(obj) {
			var checkIds = $("#configProjectTypeIds").val();
			var xz = $("#appTypeContent").find("[name='appTypeName']");
			if (checkIds.indexOf($(obj).val()) > -1) {
				checkIds = checkIds.replace($(obj).val(), "");
			}
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#appTypeContent").find("[name='appTypeName']")[a]);
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
				$("#configProjectTypeIds").val("");
			} else {
				$("#configProjectTypeIds").val(checkIds);
			}
		}
		
		//选择应用
		function changeApp(obj) {
			var checkIds = $("#configAppIds").val();
			var xz = $("#appContent").find("[name='appName']");
			if (checkIds.indexOf($(obj).val()) > -1) {
				checkIds = checkIds.replace($(obj).val(), "");
			}
			for (var a = 0; a < xz.length; a++) {
				var check = $($("#appContent").find("[name='appName']")[a]);
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
				$("#configAppIds").val("");
			} else {
				$("#configAppIds").val(checkIds);
			}
		}
		
		function exportCollect(){
			var startTime=$("#startTime").val();
			var endTime=$("#endTime").val();
			var configProjectTypeIds = $("#configProjectTypeIds").val();
			var configAppIds = $("#configAppIds").val();
			window.location.href="${ctx}/ixin/ixinData/exportDetail?configProjectTypeIds="+configProjectTypeIds+"&configAppIds="+configAppIds+"&startTime="+startTime+"&endTime="+endTime;
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ixin/ixinData/">IXIN数据采集列表</a></li>
		<shiro:hasPermission name="ixin:ixinData:edit"><li><a href="${ctx}/ixin/ixinData/form">IXIN数据采集添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ixinData" action="${ctx}/ixin/ixinData/" method="post" class="breadcrumb form-search">
		<input id ="configProjectTypeIds" name = "configProjectTypeIds" type ="hidden" value = "${configProjectTypeIds }">
		<input id ="configAppIds" name = "configAppIds" type ="hidden"  value= "${configAppIds }">
		<div id = "appTypeContent">
			<label>&nbsp;&nbsp;项目类型 ：&nbsp;&nbsp;</label>
				<c:forEach items="${typesList}" var="typeList">
				<input type ="checkBox" name ="appTypeName" onchange = "changeAppType(this)"
					<c:forEach items="${configProjectTypeIds }" var="id">
						<c:if test="${id==typeList.id }"> checked="checked"</c:if>
					</c:forEach>
				 value = "${typeList.id}">
					${typeList.projectName}
				</c:forEach>
		</div>
		<div id = "appContent">
			<label>&nbsp;&nbsp;应用名称 ：&nbsp;&nbsp;</label>
			<c:if test = "${appListSize>10 }">
			<div >
			<c:forEach items="${appList}" var="configApp"  end = "10">
				<input type ="checkBox" name ="appName" onchange="changeApp(this)"
				<c:forEach items="${configAppIds }" var="id">
					<c:if test="${id==configApp.id }"> checked="checked"</c:if>
				</c:forEach>
				 value = "${configApp.id}">
					${configApp.appName}
			</c:forEach>
			<div id = "configAppList">
			<p><input type = "button" onclick = "checkMore();" value = "查看更多"></p>
			</div>
			
			<div id = "configAppListAll" style="display: none" >
			<c:forEach items="${appList}" var="configApp" begin = "11">
				<input type ="checkBox" name ="appName" value = "${configApp.id}">
					${configApp.appName}
			</c:forEach>
			<p>
			<input type = "button" onclick = "checkLetter();" value = "隐藏应用"></p>
			</div>
			</div>
			</c:if>
			<c:if test = "${appListSize<=10 }">
			<c:forEach items="${appList}" var="configApp">
				<input type ="checkBox" name ="appType" value = "${configApp.id}">
					${configApp.appName}
			</c:forEach>
			</c:if>
		</div>
		<div>
		<label>使用时间&nbsp;</label> &nbsp;&nbsp;<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 到 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime"  id="endTime"/> 
		</div>
	<div>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input  style="text-align:center" class="btn btn-info" onclick="exportCollect()" type="button" value="导出">
	</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>应用名</th><th>在用证书数量</th><th>存活证书数量</th><th>存活率</th></tr></thead>
		<tbody>
		 <c:forEach items="${list}" var="ixinDataVo">
			<tr>
				<td>${ixinDataVo.appName}</td>
				<td>${ixinDataVo.certNumber}</td>
				<td>${ixinDataVo.survivalNumber}</td>
				<td>${ixinDataVo.rate}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>