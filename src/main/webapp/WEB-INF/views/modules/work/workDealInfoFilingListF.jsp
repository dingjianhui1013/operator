<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>业务办理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var index=1;
	var String=null;
		$(document).ready(function() {
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
			
		function pilianggui()
		{
			var workDealInfoIds=[];
			var ids=$("#checkIds").val();
			workDealInfoIds=ids.split(",");
			window.location.href="${ctx}/work/workDealInfoFiling/gui?ids="+workDealInfoIds;
		
		}
		function quangui()
		{
			window.location.href="${ctx}/work/workDealInfoFiling/quangui";
		}
		//改变ids值
		function changeCheck(obj){
			
			var checkIds = $("#checkIds").val();
			var xz = $("#contentTable").find("[name='xz']");
			if(checkIds.indexOf($(obj).val())>-1){
				checkIds = checkIds.replace($(obj).val(), "");
			}			
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='xz']")[a]);
				if (check.is(":checked") == true) {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne)<0) {
						if(checkIds==''){
							checkIds+=check.val();
						}else{
							checkIds+=","+check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds==",") {
				$("#checkIds").val("");
				document.getElementById('ckcount').innerHTML=0;
			}else{
				$("#checkIds").val(checkIds);
				var a=$("#checkIds").val().split(",");
				var b=0;
				for(var i=0;i<a.length;i++)
					{
						if(a[i]=="")
							{
								b+=1;
							}
						
					}
				document.getElementById('ckcount').innerHTML=a.length-b;
			}
			
		}
		
		function quan(obj){
			var check = $($("#contentTable").find("#checkAll"));
			var checkIds = $("#checkIds").val();
			var xz = $("#contentTable").find("[name='xz']");
			if (check.is(":checked") == true) {
				$('input:checkbox').each(function() {
			        $(this).attr('checked', true);
				});
				for (var a = 0; a <xz.length; a++) {
					var check = $($("#contentTable").find("[name='xz']")[a]);
					if (check.is(":checked") == true) {
						var checkOne = check.val();
						if (checkIds.indexOf(checkOne)<0) {
							if(checkIds==''){
								checkIds+=check.val();
							}else{
								checkIds+=","+check.val();
							}
						}
					}
				}
				checkIds = checkIds.replace(",,", ",");
				if (checkIds==",") {
					$("#checkIds").val("");
					document.getElementById('ckcount').innerHTML=0;
				}else{
					$("#checkIds").val(checkIds);
					var a=checkIds.split(",");
					document.getElementById('ckcount').innerHTML=a.length;
					
				}
				
			}else{
				$('input:checkbox').each(function () {
			        $(this).attr('checked',false);
				});
				for (var a = 0; a <xz.length; a++) {
					var check = $($("#contentTable").find("[name='xz']")[a]);
					if (check.is(":checked") == false) {
						checkIds = checkIds.replace(check.val(), "");
						checkIds = checkIds.replace(",,", ",");
					}
				}
				if (checkIds==",") {
					$("#checkIds").val("");
					document.getElementById('ckcount').innerHTML=0;
				}else{
					$("#checkIds").val(checkIds);
					if(checkIds=="")
						{
							document.getElementById('ckcount').innerHTML=0;
						}else
							{
								var a=checkIds.split(",");
								var b=0;
								for(var i=0;i<a.length;i++)
									{
										if(a[i]=="")
											{
												b+=1;
											}
										
									}
								document.getElementById('ckcount').innerHTML=a.length-b;
							}
					
				}
			}
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li <c:if test="${workDealInfo.status==1 }"> class="active" </c:if> ><a href="${ctx}/work/workDealInfoFiling/list?status=1">已归档用户</a></li>
		<li <c:if test="${workDealInfo.status==0 }"> class="active" </c:if> ><a href="${ctx}/work/workDealInfoFiling/list?status=0">未归档用户</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo" action="${ctx}/work/workDealInfoFiling/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input type = "hidden" value = "${workDealInfo.status }" name = "status" />
		<div>
		<label>Key序列号：</label><form:input path="keySn" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>单位名称：</label><form:input path="workCompany.companyName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>联系人姓名：</label><form:input path="workUser.contactName" htmlEscape="false" maxlength="50" class="input-medium"/><br/>
		</div>
		<div style="margin-top: 8px">
		<label>&nbsp;&nbsp;固定电话：</label><form:input path="workUser.contactTel" htmlEscape="false" maxlength="50" class="input-medium"/>
		<label>移动电话：</label><form:input path="workUser.contactPhone" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<c:if test="${status==0 }">&nbsp;<input id="piGui" class="btn btn-primary" type="button" value="批量归档" onclick="pilianggui()"/>
		&nbsp;<input id="allGui" class="btn btn-primary" type="button" value="全部归档" onclick="quangui()"/></c:if>
		</div>
		<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds}" />
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<c:if test="${status==0 }">
			<tr>
				<th colspan="9" id="changeCount">共选择&nbsp;<span id="ckcount"><c:choose>
						<c:when test="${count==null}">0</c:when>
						<c:otherwise>${count}</c:otherwise>
					</c:choose></span>&nbsp;条数据</th>
			</tr>
		</c:if>
			<tr>
				<th><c:if test="${status==0 }"><input type="checkbox" class="quan" name="xz" onclick="quan(this)" value="" id="checkAll" /></c:if>用户ID</th>
				<th id="ceshi">单位名称</th>
				<th>应用名称</th>
				<th>业务类型</th>
				<th>证书类型</th>
				<th>归档人</th>
				<th>归档日期</th>
				<th>归档编号</th>
				<th>操作</th>
		<tbody>
		<c:forEach items="${page.list}" var="workDealInfo">
			<tr>
				<td><c:if test="${status==0 }">
				<input type="checkbox" class="duo" name="xz"  value="${workDealInfo.id }" 
			<c:forEach items="${ids }" var="id">
				<c:if test="${id==workDealInfo.id }"> checked="checked"</c:if>
			</c:forEach>
			onchange="changeCheck(this)"/></c:if>${workDealInfo.id}</td>
				<td><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
				<td>${workDealInfo.configApp.appName}</td>
				<td>
					<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
					<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
					<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
					<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
				
				
				</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>${workDealInfo.updateBy.name}</td>
				<td>${workDealInfo.archiveDate}</td>
				<td>${workDealInfo.userSn}</td>
				<td>
					<c:if test="${workDealInfo.status==1 }"><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">查看</a></c:if>
					<c:if test="${workDealInfo.status==0 }"><a href="${ctx}/work/workDealInfoFiling/updateStatus?id=${workDealInfo.id}">归档</a></c:if>
    				
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
