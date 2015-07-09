<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
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
		function onType(obj) {
			if ($(obj).prop("checked")) {
				$("#type1").removeAttr("checked");
				$("#type2").removeAttr("checked");
				$("#type3").removeAttr("checked");
			} 
			$("#reissueType").hide();
		}
		function onType1(obj) {
			if ($(obj).prop("checked")) {
				$("#type4").removeAttr("checked");
			}
			if ($("#type2").prop("checked")){
				$("#reissueType").show();
			} else {
				$("#reissueType").hide();
			}
		}
		
		
	</script>
</head>
<body>
	<form action="${ctx}/work/workDealInfo/typeForm" id="typeForm">
	<input type="hidden" name="id" value="${id }">
	<div style="padding: 10px;">
		<input type="checkbox" onchange="javascript:onType1(this)" id="type1" value="1"
			name="dealType" />变更<br> 
		<input type="checkbox"
			onclick="javascript:onType1(this);" value="2" id="type2" name="dealType" />补办<br>
		<div id="reissueType" style="display: none">
			<input type="radio" name="reissueType" checked="checked" value="1"/>遗失补办
			<input type="radio" name="reissueType" value="2"/>损坏更换
		</div>
			
		<input type="checkbox" onclick="javascript:onType1(this);" value="3"
			id="type3" name="dealType" />更新<br> 
		<input type="checkbox" onclick="javascript:onType(this);" id="type4" value="4" name="dealType" />吊销<br>
	</div>
	</form>
</body>
</html>
