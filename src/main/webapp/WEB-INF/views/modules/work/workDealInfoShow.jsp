<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>已办理证书明细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			localStorage.setItem("cid","");
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
	            localStorage.setItem("cid","");
	        }else{   
	            $(obj1).val("1");  
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
	<form id="searchForm"  action="${ctx}/work/workDealInfo/showCertEnterprise" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
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
		<c:forEach items="${page.list}" var="cert">
			<tr>
				<td>${cert.svn}</td>
				<td>${cert.configApp.appName }</td>
				<td>${pro[cert.configProduct.productName] }</td>
				<td>${cert.workUser.contactName}</td>
				<td>${cert.workCertInfo.workCertApplyInfo.name}</td>
				
<%-- 				<td></td> --%>
<%-- 				<td>${cert.certApplyInfoName}</td> --%>
				<td><input type="radio"  onclick="fillCompany('${cert.id}',this)"  value="0" name="xz" />选择</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<!-- 	<input type="text" value="" name="workdealinfoId" id="workdealinfoId"/> -->
	<input type="hidden" value="${companyId}" name="companyIds" />
	<input type="hidden" value="${productId}" name="productId" />
	</form>
	<div class="pagination">${page}</div>
</body>
</html>
