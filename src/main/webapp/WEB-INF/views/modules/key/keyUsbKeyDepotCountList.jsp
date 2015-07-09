<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key入库信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler : function(form) {
				loading('正在提交，请稍等...');
				form.submit();
			}
		});
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
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#office").html(html);
		});

	}
	function addGene() {
		var supplierId = $("#supplierId").prop("value");
		var url = "${ctx}/key/keyGeneralInfo/addGeneralInfo?supplierId=";
		$.getJSON(url + supplierId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</ooption>"
			});

			$("#keyId").html(html);
		});
	}

	function judgement() {
		if ($("#startTime").val() == "") {
			alert("起始时间不能为空");
			return false;
		}
		if ($("#endTime").val() == "") {
			alert("结束时间不能为空");
			return false;
		}

	}
	
	
	function showIn(obj) {
		var supplierId = $("#supplierId").val();
		var keyId = $("#keyId").val();
		var url = "${ctx}/key/keyUsbKey/depotCountInList?startTime="+$("#startTime").val()+" 00:00:00&endTime="+$("#endTime").val()+" 23:59:59&keyId="+keyId+"&supplierId="+supplierId+"&depotId="+obj;
		top.$.jBox.open("iframe:" + url, "入库详情", 1200, 520, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
	}
	function showOut(obj) {
		var supplierId = $("#supplierId").val();
		var keyId = $("#keyId").val();
		var url = "${ctx}/key/keyUsbKeyInvoice/depotCountOutList?startTime="+$("#startTime").val()+" 00:00:00&endTime="+$("#endTime").val()+" 23:59:59&keyId="+keyId+"&supplierId="+supplierId+"&depotId="+obj;
		top.$.jBox.open("iframe:" + url, "出库详情", 1200, 520, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/listCount">库存统计列表</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKeyDepot"
		action="${ctx}/key/keyUsbKeyDepot/listCount" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />

		<div style="margin-top: 9px">
			<label>库房名称 ：</label>
			<form:input name="depotName" path="depotName" value="${depotName}"
				htmlEscape="false" maxlength="50" class="input-medium" />
		</div>
		<br/>
		<div>
			<label>KEY 厂商：</label> <select name="supplierId"
				id="supplierId" onchange="addGene()">
				<option value="">请选择</option>
				<c:forEach items="${suppliers}" var="supplier">
					<option value="${supplier.id}"
						<c:if test="${supplier.id==supplierId}">
					selected="selected"
					</c:if>>${supplier.supplierName}</option>
				</c:forEach>
			</select> <label>KEY类型 ：</label> <select name="keyId" id="keyId">
				<option value="">请选择</option>
				<c:forEach items="${keys}" var="key">
					<option value="${key.id}"
						<c:if test="${key.id==keyId}">
					selected="selected"
					</c:if>>${key.name}</option>
				</c:forEach>
			</select>
		</div>
		<br />

		<div>
			<label>选择区域 ：</label>
		<select name="area" id="area" onchange="addOffice()">
			<c:if test="${offsListSize!=1 }">

				<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
		<select name="office" id="office">
			<c:if test="${offsListSize!=1 }">
				<option value="">请选择</option>
			</c:if>
			<c:forEach items="${offices}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==office}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>

		</select>

		</div>
		<br />
		<label><font color="red">*</font>统计时间：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		&nbsp; &nbsp; &nbsp; &nbsp; <input id="btnSubmit"
			class="btn btn-primary" type="submit" 
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所属网点</th>
				<th>key类型名称</th>
				<th>起始数量</th>
				<th>入库数量</th>
				<th>出库数量</th>
				<th>截止数量</th>
				<th>操作</th>
			</tr>
		</thead>

		<tbody id="tbody">
			<c:forEach items="${page.list}" var="keyUsbKeyDepot">
				<tr>
					<td><a href="${ctx}/key/keyUsbKeyDepot/form?id=${keyUsbKeyDepot.id}">${keyUsbKeyDepot.depotName}</a></td>
					<td>${keyUsbKeyDepot.office.parent.name}</td>
					<td>${keyUsbKeyDepot.office.name}</td>
					<td><c:forEach
							items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}"
							var="statis">
					        	${statis.keyGeneralInfo.name}
								<br />
						</c:forEach>
						${keyUsbKeyDepot.totolName} <br /> 
					</td>
					<td><c:forEach
							items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}"
							var="statis">
					        	${statis.totalCount}
								<br />
						</c:forEach>
						${keyUsbKeyDepot.totolCount}
						</td>
					<td><c:forEach
							items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}"
							var="statis">
					        	${statis.inCount}
								<br />
						</c:forEach>
						${keyUsbKeyDepot.inCount} 
						</td>
					<td><c:forEach
							items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}"
							var="statis">
					        	${statis.outCount}
								<br />
						</c:forEach>
						${keyUsbKeyDepot.outCount}
						</td>
					<td> <c:forEach
							items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}"
							var="statis">
					        	${statis.totalEndCount}
								<br />
						</c:forEach>
						${keyUsbKeyDepot.totalEndCount} 
						</td>
					<td><a
						href="javascript:showIn(${keyUsbKeyDepot.id})"  >入库详情</a>
						<a
						href="javascript:showOut(${keyUsbKeyDepot.id})" >出库详情</a>
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
