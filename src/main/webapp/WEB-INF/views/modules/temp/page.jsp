<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
</head>
<body>
	<form id="aopProForm" method="post" style="display: none">
		<!-- <input id="fromAopPro" type="hidden" name="aopProjects" value=""> -->
	</form>
	<script type="text/javascript">
		function postByAopPro(fromAction) {
			//        $("#fromAopPro").val(proMap);
			$("#aopProForm").attr("action", fromAction);
			$("#aopProForm").submit();
		}
	</script>

	${content}
</body>
</html>