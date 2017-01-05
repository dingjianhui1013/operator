<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript" src="js/test/jquery-1.7.2.min.js"></script>

<title>Insert title here</title>

<script type="text/javascript">
$(document).ready(
		function() {
			
			
			var link = $("#link").val();
			
			if(link == 1){
				window.open("http://testbss.scca.com.cn:8011/my-webapp/register");	
			}else if(link == 2){
				window.open("http://testbss.scca.com.cn:8011/my-webapp/registerQuery");
			}else if(link == 3){
				window.open("http://testbss.scca.com.cn:8011/my-webapp/feitian.jsp");
				
				
			}
			
			
		});

</script>



</head>


<body>

	
  <input id="link" type="hidden" value="<%=request.getParameter("method")%>" />

</body>
</html>