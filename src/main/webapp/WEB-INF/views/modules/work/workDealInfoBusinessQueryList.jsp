<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务查询</title>
<meta name="decorator" content="default" />
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
		var areaId = $("#area").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+0+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#officeId").html(html);
		});
	}
	
	function alarmValue(dealInfoId){
		var url = "${ctx}/work/workDealInfo/showWorkDeal?dealInfoId="+dealInfoId;
		top.$.jBox.open("iframe:"+url, "查看", 550, 480, {
				buttons:{"关闭":true}, submit:function(v, h, f){
					
				}
		});
	}
	
	function show(){
		document.getElementById("btnSubmit").style.display="none";
		document.getElementById("tjjg").style.display="none";
		document.getElementById("gjcx").style.display="none";
		document.getElementById("advanced").style.display="";
		}
	function hidde(){
		document.getElementById("advanced").style.display="none";
		document.getElementById("btnSubmit").style.display="";
		document.getElementById("tjjg").style.display="";
		document.getElementById("gjcx").style.display="";
		}
	window.onload=function(){
		var area = document.getElementById("area").value;
		var officeId = document.getElementById("officeId").value;
		var year = document.getElementById("year").value;
		var payMethod = document.getElementById("payMethod").value;
		if(area != "" || officeId != "" || year != "" || payMethod !="0"){
			show();
		}
	}
	function onSubmit(){
		var area = document.getElementById("area").value;
		var officeId = document.getElementById("officeId").value;
			if (area!=""&&officeId==""){
				top.$.jBox.tip("请选择网点");
				return false;
			} else {
				return true;
			}
	}
</script>
</head>
<body onload=''>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/work/workDealInfo/businessQuery">业务查询列表</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/businessQuery" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
			<label>证书类型 ：</label> <select name="certType" id="certType">
				<option value="">请选择</option>
				<c:forEach items="${certTypes}" var="type" varStatus="status">
					<option value="${type.id}"
						<c:if test="${type.id==certType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
			<label>业务类型 ：</label> <select name="workType" id="workType">
				<option value="">请选择业务类型</option>
				<c:forEach items="${workTypes}" var="type">
					<option value="${type.id}"
						<c:if test="${type.id==workType}">
					selected="selected"
					</c:if>>${type.name}</option>
				</c:forEach>
			</select> 
			<label>应&nbsp; &nbsp; &nbsp; 用 ：</label> <select name="apply"
				id="apply">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==apply}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
			</div>
		<br />
		<div>
			<label>业务办理时间：从</label> 
				<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
				到 
				<input id="endTime" name="endTime" type="text" readonly="readonly"
				maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
			<input id="btnSubmit" class="btn btn-primary" onclick="return onSubmit()" type="submit"
				value="查询" />
				<input id="gjcx" style="text-align:center" class="btn btn-info" onclick="show()" type="button" value="高级">
				<span id="tjjg">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;统计结果${page.count }条</span>
				<br />
		</div>	
		<br />
		<div id="advanced" style="display:none">
			<div>
			<label>所属区域 ：</label> <select name="area" id="area"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==area}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>所属网点 ：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select>
		</div>
		<br />	
		<div>
			<label>业务年限 ：</label> <select name="year" id="year">
				<option value="">请选择证书年限</option>
				<option value="1"
					<c:if test="${'1'==year}">
					selected="selected"
					</c:if>>1年</option>
				<option value="2"
					<c:if test="${'2'==year}">
					selected="selected"
					</c:if>>2年</option>
				<option value="4"
					<c:if test="${'4'==year}">
					selected="selected"
					</c:if>>4年</option>
				<option value="5"
					<c:if test="${'5'==year}">
					selected="selected"
					</c:if>>5年</option>
			</select> 
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
			<input id="btnSubmit" class="btn btn-primary" onclick="return onSubmit()" type="submit"
				value="查询" />
				<input style="text-align:center" class="btn btn-info" onclick="hidde()" type="button" value="收起">
				&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;统计结果${page.count }条
			</div>
			<br />
		</div>

	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>证书类型</th>
				<th>所属区域</th>
				<th>所属网点</th>
				<th>应用</th>
				<th>业务类型</th>
				<th>业务年限</th>
				<th>付款方式</th>
				<th>制证时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>${proType[workDealInfo.configProduct.productName]}</td>
					<td>${workDealInfo.createBy.company.name}</td>
					<td>${workDealInfo.createBy.office.name}</td>
					<td>${workDealInfo.configApp.appName}</td>
					<td>${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}</td>
					<td>${workDealInfo.year}年</td>
					<td> <c:if test="${workDealInfo.payType==1}">
							标准
						</c:if>
						<c:if test="${workDealInfo.payType==3}">
							合同采购
						</c:if>
						<c:if test="${workDealInfo.payType==2}">
							政府统一采购
						</c:if>
						<c:if test="${workDealInfo.workPayInfo.methodPos||(workDealInfo.workPayInfo.relationMethod==1)}">
							POS收款
						</c:if>
						<c:if test="${workDealInfo.workPayInfo.methodMoney||(workDealInfo.workPayInfo.relationMethod==0)}">
							现金
						</c:if>
						</td>
						<td>
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${workDealInfo.workCertInfo.signDate}"/> 
						</td>
						<td>
						<a href="javascript:void(0)" onclick="alarmValue( ${workDealInfo.id} )" >
						查看 </a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
