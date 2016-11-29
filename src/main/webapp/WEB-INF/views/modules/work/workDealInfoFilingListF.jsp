<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>业务办理管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp"%>
	<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
	<script type="text/javascript">
	var index=1;
		$(document).ready(function() {
			$("#area").val(${area});
			$("#officeId").val(${officeId});
			$("#certType").val(${certType});
			$("#workType").val(${workType});
			$("#payMethod").val(${payMethod});
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
		
		
		
		function show(){
			document.getElementById("btnSubmit").style.display="none";
			document.getElementById("gjcx").style.display="none";
			if(document.getElementById("exportZS")!=undefined){
				document.getElementById("exportZS").style.display="none";
			}
			document.getElementById("advanced").style.display="";
			if(document.getElementById("piGui")!=undefined){
				document.getElementById("piGui").style.display="none";
			}
			if(document.getElementById("allGui")!=undefined){
				document.getElementById("allGui").style.display="none";
			}
		}
		function hidde(){
			document.getElementById("advanced").style.display="none";
			document.getElementById("btnSubmit").style.display="";
			document.getElementById("gjcx").style.display="";
			if(document.getElementById("exportZS")!=undefined){
				document.getElementById("exportZS").style.display="";
			}
			if(document.getElementById("piGui")!=undefined){
				document.getElementById("piGui").style.display="";
			}
			if(document.getElementById("allGui")!=undefined){
				document.getElementById("allGui").style.display="";
			}
		}
		
		function addOffice() {
			var areaId = $("#area").prop('value');
			var url = "${ctx}/sys/office/addOffices?areaId=";
			$.getJSON(url + areaId+"&_\="+new Date().getTime(), function(data) {
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
		
		function dc()
		{
			var keySn = $("#keySn").val();
			var companyName=$("input[id='workCompany.companyName']").val();
			var contactName=$("input[id='workUser.contactName']").val();
			var contactTel=$("input[id='workUser.contactTel']").val();
			var contactPhone=$("input[id='workUser.contactPhone']").val();
			var area=$("#area").val();
			var officeId =$("#officeId").val();
			var certType =$("#certType").val();
			var workType =$("#workType").val();
			var payType =$("#payType").val();
			var province=$("#s_province").val();
			var city =$("#s_city").val();
			var district=$("#s_county").val();
			var payMethod=$("#payMethod").val();
			var status=$("input[name='status']").val();

			
			var expurl =  "${ctx}/work/workDealInfoFiling/exportSerial?keySn="+keySn+"&companyName="+companyName+"&contactName="+contactName
					+"&contactTel="+contactTel+"&contactPhone="+contactPhone+"&area="+area
					+"&officeId="+officeId+"&certType="+certType+"&workType="+workType+"&payType="+payType+"&province="+province
					+"&city="+city+"&district="+district+"&payMethod="+payMethod+"&status="+status;
			//console.log(expurl);
			window.open(expurl);
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
		&nbsp;<input id="gjcx" style="text-align:center" class="btn btn-primary" onclick="show()" type="button" value="高级">
		<c:if test="${status==1 }">&nbsp;<input id="exportZS" style="text-align:center" class="btn btn-primary" onclick="dc()" type="button" value="导出"></c:if>
		<c:if test="${status==0 }">&nbsp;<input id="piGui" class="btn btn-primary" type="button" value="批量归档" onclick="pilianggui()"/>
		&nbsp;<input id="allGui" class="btn btn-primary" type="button" value="全部归档" onclick="quangui()"/></c:if>
		</div>
		<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds}" />
		<br/>
		<div id="advanced" style="display:none">
		<div>
			<label>按受理信息查询：</label><br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>受理区域：</label><select name="area" id="area" onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==area}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			<label>受理网点：</label>  <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select>
			<label>产品名称：</label> <select name="certType" id="certType">
				<option value="">请选择</option>
				<c:forEach items="${certTypes}" var="type" varStatus="status">
					<option value="${type.id}"
						<c:if test="${type.id==certType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>业务类型：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>
							<c:if test="${type.id==11}">现金退费</c:if>
							<c:if test="${type.id!=11}">${type.name}</c:if>
						</option>
				</c:forEach>
			</select> 				
			<label>计费策略类型：</label> 
			<form:select path="payType">
				<form:option value="0">请选择</form:option>
				<form:option value="1">标准</form:option>
				<form:option value="2">政府统一采购</form:option>
				<form:option value="3">合同采购</form:option>
			</form:select>
			<label>行政所属区：</label> 	
			<select id="s_province" name="workCompany.province"	style="width: 100px;"></select>&nbsp;&nbsp; 
			<select id="s_city"	name="workCompany.city" style="width: 100px;"></select>&nbsp;&nbsp; 
			<select	id="s_county" name="workCompany.district" style="width: 100px;"></select> 
			<script	type="text/javascript">
				_init_area();
				$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
				$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
				$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
			</script>	
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label>付款方式 ：</label> <select name="payMethod" id="payMethod">
				<option value="0"
					<c:if test="${payMethod=='0'}"> selected="selected" </c:if>>全部</option>
				<option value="1"
					<c:if test="${payMethod=='1'}"> selected="selected" </c:if>>现金</option>
				<option value="2"
					<c:if test="${payMethod=='2'}"> selected="selected" </c:if>>POS收款</option>
				<option value="3"
					<c:if test="${payMethod=='3'}"> selected="selected" </c:if>>银行转帐</option>
				<option value="4"
					<c:if test="${payMethod=='4'}"> selected="selected" </c:if>>支付宝转账</option>
				<option value="5"
					<c:if test="${payMethod=='5'}"> selected="selected" </c:if>>政府统一采购</option>
				<option value="6"
					<c:if test="${payMethod=='6'}"> selected="selected" </c:if>>合同采购</option>
			</select>
									
		</div>
		<br>
		<div>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				&nbsp;<input style="text-align:center" class="btn btn-primary" onclick="hidde()" type="button" value="收起">
				<c:if test="${status==1 }">&nbsp;<input id="exportZS" style="text-align:center" class="btn btn-primary" onclick="dc()" type="button" value="导出"></c:if>
				<c:if test="${status==0 }">&nbsp;<input id="piGui" class="btn btn-primary" type="button" value="批量归档" onclick="pilianggui()"/>
				&nbsp;<input id="allGui" class="btn btn-primary" type="button" value="全部归档" onclick="quangui()"/></c:if>
		</div>
		<br>
			<br />
		</div>
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
					<c:if test="${status==1 }"><a href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">查看</a></c:if>
					<c:if test="${status==0 }"><a href="${ctx}/work/workDealInfoFiling/updateStatus?id=${workDealInfo.id}">归档</a></c:if>
    				
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
