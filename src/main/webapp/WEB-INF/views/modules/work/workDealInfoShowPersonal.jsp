<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>已办理证书明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function fillCompany(obj,obj1)
		{
			var radioCheck= $(obj1).val();  
	        if("1"==radioCheck){  
	            $(obj1).attr("checked",false);  
	            $(obj1).val("0");  
	            $("#workdealinfoId").val("");
	            localStorage.setItem("cid","");
	        }else{   
// 	        	$("input['#xz']:checked").attr("checked","false");
	            $(obj1).val("1");  
	            $("#workdealinfoId").val(obj);
	            localStorage.setItem("cid",obj);
	        }  
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="searchForm"  action="${ctx}/work/workDealInfo/showCertPersonal" method="post" >
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th>编号</th>
		<th>应用名称</th>
		<th>证书类型</th>
		<th>持有人</th>
        <th>经办人</th>
        <th>操作</th>
        </tr>
        </thead>
		<tbody>
		<c:forEach items="${list}" var="cert">
			<tr>
				<td>${cert.svn}</td>
				<td>${cert.configApp.appName }</td>
				<td>${pro[cert.configProduct.productName]}</td>
				<td>${cert.workUser.contactName}</td>
				<td>${cert.workCertInfo.workCertApplyInfo.name}</td>
				<td><input type="radio"  onclick="fillCompany('${cert.id}',this)"  value="0" name="xz" />选择</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<input type="hidden" value="" name="workdealinfoId" id="workdealinfoId"/>
	</form>
</body>
</html>
