<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<html>
<head>
	<title>地税和社保</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1});
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
			$("#addForm").validate({
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
			$("#btnButton").click(function(){
				var str="";
	            $("input[name='child_applyFlag1']:checkbox").each(function(){ 
	                if($(this).attr("checked")){
	                    str += $(this).val()+","
	                }
	            });
	            $("#applyFlag1").val(str);
	             var str1="";
	            $("input[name='child_applyFlag2']:checkbox").each(function(){ 
	                if($(this).attr("checked")){
	                    str1 += $(this).val()+","
	                }
	            });
	            $("#applyFlag2").val(str1);
	            var url = "${ctx }/profile/configApp/saveApplyFlag?selectApplyFlag1="+str+"&selectApplyFlag2="+str1+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
	            	if(data.status == '1'){
	            		 alert("保存成功");
	            		}else{
	            			 alert("保存失败");
	            		}
				});
			});
			selectAll();
		});
		
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		//全选和反选
		function selectCheckbox(obj){
			if($("[name='"+obj.name+"']").attr("checked")){
				 $("[name='child_"+obj.name+"']").attr("checked",'true');
			}else{
				$("[name='child_"+obj.name+"']").removeAttr("checked");
			}
		}
		
		function selectChildBox(id,childId,pName){
			if(pName =='applyFlag1'){
				var flag = document.getElementById("checkbox_"+childId).checked;
				if(flag){
					$("input[id='checkbox_"+childId+"']").attr("checked",'true');
				}else{
					$("input[id='checkbox_"+childId+"']").removeAttr("checked");
				}
			}
			if(pName =='applyFlag2'){
				var flag = document.getElementById("checkbox1_"+childId).checked;
				if(flag){
					$("input[id='checkbox1_"+childId+"']").attr("checked",'true');
				}else{
					$("input[id='checkbox1_"+childId+"']").removeAttr("checked");
				}
			}
			
			var boxs  = document.getElementsByName("child_"+pName);
			var num = 0;
			for(i=0;i<boxs.length;i++){
				if(boxs[i].checked == true){
					num ++;
				}
			}
			if(num>0){
				 $("[name='"+pName+"']").attr("checked",'true');
			}else{
				 $("[name='"+pName+"']").removeAttr("checked");
			}
		}
		
		function selectAll(){
			var boxs  = document.getElementsByName("child_applyFlag1");
			var num = 0;
			for(i=0;i<boxs.length;i++){
				if(boxs[i].checked == true){
					num ++;
				}
			}
			if(num>0){
				 $("[name='applyFlag1']").attr("checked",'true');
			}else{
				 $("[name='applyFlag1']").removeAttr("checked");
			}
			
			var boxs2  = document.getElementsByName("child_applyFlag2");
			var num2 = 0;
			for(i=0;i<boxs2.length;i++){
				if(boxs2[i].checked == true){
					num2 ++;
				}
			}
			if(num2>0){
				 $("[name='applyFlag2']").attr("checked",'true');
			}else{
				 $("[name='applyFlag2']").removeAttr("checked");
			}
		}
		function testMsg1(){
			
			var html = '该功能为自助配置，现自助系统申请表内容分为社保和地税2种格式，在此配置申请表对应的应用，可实现办理应用时动态展示申请表';
			
			top.$.jBox(html, {
				top: '0%',
				showType: 'slide',
				showSpeed: 'slow',
				title : "注意事项",
				persistent: true
			});
			
		}
		
		
		$(document).keypress(function(e)    
			    {    
			         switch(e.which)    
			        {    
			            case 90:    testMsg1();    
			            break;      
			        }
			         
			        $("#attention").hide(); 
		});  
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">地税和社保</a></li>
	<p id="attention" style="float: right;color: gray;">shift+z快捷键可查看注意事项</p>
	</ul>
	
	<tags:message content="${message}"/>
	<form action="${ctx }/profile/configApp/saveApplyFlag" method="post" id="idsForm">
		<input type = "hidden" name = "selectApplyFlag1" id = "applyFlag1" >
		<input type = "hidden" name = "selectApplyFlag2" id = "applyFlag2" >
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tbody>
				<tr id="all_applyFlag1" pId="0">
					<td><input type="checkbox" name="applyFlag1" onclick = "selectCheckbox(this)" id="checkbox_all_applyFlag1" value="all" />地税</td>
				</tr>
				<c:forEach items="${list}" var="configApp">
					<tr id="-1" pId="all_applyFlag1">
						<td >
							<input type="checkbox" name="child_applyFlag1" <c:if test = "${configApp.applyFlag1 eq true }"> checked</c:if> onclick = "selectChildBox('checkbox_all_applyFlag1','${configApp.id }','applyFlag1')" id="checkbox_${configApp.id }" value="${configApp.id }" />${configApp.appName}
						</td>
					</tr>
				</c:forEach>
				<tr id="all_applyFlag2" pId="0">
					<td><input type="checkbox" name="applyFlag2" onclick = "selectCheckbox(this)" id="checkbox_all_applyFlag2" value="all" />社保</td>
				</tr>
				<c:forEach items="${list}" var="configApp">
					<tr id="-1" pId="all_applyFlag2">
						<td >
							<input type="checkbox" name="child_applyFlag2" <c:if test = "${configApp.applyFlag2 eq true }"> checked</c:if> onclick = "selectChildBox('checkbox_all_applyFlag2','${configApp.id }','applyFlag2')" id="checkbox1_${configApp.id }" value="${configApp.id }" />${configApp.appName}
							
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input id="btnButton" class="btn btn-primary" type="button" value="保 存">&nbsp;
	</form>
</body>
</html>
