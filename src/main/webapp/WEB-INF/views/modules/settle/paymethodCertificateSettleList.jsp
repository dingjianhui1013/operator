<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书结算统计表-缴费统计</title>
<meta name="decorator" content="default" />
<style type="text/css">
	.table th,.table td{
		text-align: center;
		}
.insertTable{border:none;}
.table .insertTable th:first-child,.table .insertTable td:first-child{border-left:none;}
.table .insertTable td{width:60px;}
.addTable tr:nth-of-type(4) th {
    padding: 4px 0px;
}

</style>
<script type="text/javascript">
$(document).ready(function() {
	var windowH=$(window).height();
	$('.windowHeight').height(windowH);
	$("#scrollBar").scroll(function(){
		var tableWidth=$("#tableW").width();
		var leftWidth=$("#scrollBar").scrollLeft();
		var formWidth=$("#searchForm").width();
		if((tableWidth-formWidth)>=leftWidth)
			{
				$("#searchForm").css("margin-left",leftWidth);
				$("#ulId").css("margin-left",leftWidth);
			}
	});
	
	$("#searchForm").validate({
		submitHandler: function(form){
			loading('正在提交，请稍等...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				//alert(element[0].tagName);
				(element.parent().find('label:last-child')).after(error);
			} else {
				//alert(element[0].tagName);
				error.insertAfter(element);
			}
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
		var areaId = $("#areaId").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId + "&_=" + new Date().getTime(), function(data) {
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
	function changeAgentId() {
		var tempStyle = $("#tempStyle").prop('value');
		var url = "${ctx}/profile/configChargeAgent/changeAgentId?tempStyle=";
		$.getJSON(url + tempStyle + "&_=" + new Date().getTime(),
				function(data) {
					var html = "";
					//console.log(data);
					html += "<option value=\""+""+"\">请选择</ooption>";
					$.each(data, function(idx, ele) {
						//console.log(idx);
						//console.log(ele);

						html += "<option value=\""+ele.id+"\">" + ele.name
								+ "</ooption>"
					});
					$("#agentId").html(html);
				});
	}

	function onSubmit() {
		if ($("#startTime").val() == "" || $("#endTime").val() == "") {
			top.$.jBox.tip("请选定时间范围");
			return false;
		} else {
			if ($("#applyId").val() == "") {
				top.$.jBox.tip("请选择应用");
				return false;
			} else {
				return true;
			}
		}
	}
	function dcZS() {
		var applyId = $("#applyId").val();
		var areaId = $("#areaId").val();
		var officeId = $("#officeId").val();
		//var proList = $("#proList").val();
		//var workTypes = $("#workTypes").val();
		var startDate = $("#startTime").val();
		var endDate = $("#endTime").val();
		var tempStyle = $("#tempStyle").val();
		var agentId = $("#agentId").val();
		
		
		if (applyId == null || applyId == "") {
			top.$.jBox.tip("请选择应用");
			return false;
		}
		var proList = "";
		var checks = $("input[name=proList]:checked");
		$.each(checks, function(idx, ele) {
			if (proList == "") {

				proList = $(ele).val();
			} else {
				proList = proList + "," + $(ele).val();
			}

		});
		var workTypes = "";
		var checks1 = $("input[name=workTypes]:checked");
		$.each(checks1, function(idx, ele) {
			if (workTypes == "") {
				workTypes = $(ele).val();
			} else {
				workTypes = workTypes + "," + $(ele).val();
			}

		});

		var multiType = $("input[name=multiType]").is(':checked')
		
		window.location.href = "${ctx }/settle/paymethodCertificateSettle/export?applyId="
				+ applyId
				+ "&areaId="
				+ areaId
				+ "&officeId="
				+ officeId
				+ "&proList="
				+ proList
				+ "&workTypes="
				+ workTypes
				+ "&tempStyle="
				+ tempStyle
				+ "&agentId="
				+ agentId				
				+ "&startDate="
				+ startDate
				+ "&endDate="
				+ endDate
				+ "&multiType=" + multiType;
	}
	/* function dcZS() {

		var form = $("#searchForm");

		form.action = "${ctx }/settle/paymethodCertificateSettle/export";

		form.submit();
	} */
</script>
</head>
<body>
	<div style="overflow: auto;" class="windowHeight" id="scrollBar">
		<ul class="nav nav-tabs" id="ulId" style="width: 100%;">
			<li class="active"><a
				href="${ctx}/settle/paymethodCertificateSettle/">证书结算统计表-缴费统计</a></li>		
		</ul>
		<form:form id="searchForm"
			modelAttribute="paymethodCertificateSettle"
			action="${ctx}/settle/paymethodCertificateSettle/" method="post"
			class="breadcrumb form-search" style="width:100%;">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" />
			<div style="margin-top: 9px;" >
				<label>应&nbsp; &nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 用：</label> <select
					name="applyId" id="applyId">
					<option value="">请选择应用</option>
					<c:forEach items="${configAppList}" var="app">
						<option value="${app.id}"
							<c:if test="${app.id == applyId}">
					selected="selected"
					</c:if>>${app.appName}</option>
					</c:forEach>
				</select> <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选择区域：</label>
				<select name="areaId" id="areaId" onchange="addOffice()">
					<option value="">请选择</option>
					<c:forEach items="${offsList}" var="off">
						<option value="${off.id}"
							<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
					</c:forEach>
				</select> <label>选择网点：</label> <select name="officeId" id="officeId">
					<option value="">请选择</option>
					<c:forEach items="${offices}" var="off">
						<option value="${off.id}"
							<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
					</c:forEach>
				</select>  
			</div>

			<div style="margin-top: 10px">
				<label>计费策略类型：</label> <select name="tempStyle" id="tempStyle"
					onchange="changeAgentId()">
					<option value="0">请选择</option>
					<option value="1"
						<c:if test="${tempStyle==1}"> selected="selected"</c:if>>标准</option>
					<option value="2"
						<c:if test="${tempStyle==2}"> selected="selected"</c:if>>政府统一采购</option>
					<option value="3"
						<c:if test="${tempStyle==3}"> selected="selected"</c:if>>合同采购</option>
				</select> <label>计费策略名称 ：</label> <select name="agentId" id="agentId">
					<option value="">请选择</option>
					<c:forEach items="${agentList}" var="agen">
						<option value="${agen.id}"
							<c:if test="${agen.id==agentId}">
					selected="selected"
					</c:if>>${agen.tempName}</option>
					</c:forEach>
				</select> 
				<label>组合业务：</label><input type="checkbox" name="multiType"
					id="multiType" value="true"
					<c:if test="${multiType}">checked="checked"</c:if>>
			</div>
			<div style="margin-top: 8px">
				<label>业务类型：</label>
					<c:forEach items="${workTypes}" var="type">
						<input type="checkbox" name="workTypes" id="workTypes"
							value="${type.id}" 
							<c:if test="${workType==null }">checked="checked"</c:if>
							<c:forEach items="${workType}" var="workType">
							<c:if test="${workType==type.id}">checked="checked"</c:if> 
						</c:forEach> />					
						${type.name}
					</c:forEach>
					&nbsp;&nbsp;
				<label>产品名称 ：</label>
				<c:forEach items="${proList}" var="pro">
					<input type="checkbox" name="proList" id="proList"
						value="${pro.id}" 
						<c:if test="${productId==null}">checked="checked"</c:if>
						<c:forEach items="${productId}" var="productId">
						<c:if test="${productId==pro.id}">checked="checked"</c:if> 
					</c:forEach> />					
					${pro.name}
				</c:forEach>
				
			</div>
			<div style="margin-top: 8px">
				<label>统计时间 ：</label> <input
					id="startTime" name="startDate" type="text" readonly="readonly"
					maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
					value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
				<input id="endTime" name="endDate" type="text" readonly="readonly"
					maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
					value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" />

				
				<%-- <c:forEach items="${proList }" var="pro">
				<input type="checkbox" name="proList" id="proList" value="${pro.id}">${pro.name}
			</c:forEach> --%>
				&nbsp;&nbsp; <input id="btnSubmit" class="btn btn-primary"
					onclick="return onSubmit();" type="submit" value="查询" /> <input
					id="exportZS" class="btn btn-primary" onclick="dcZS()"
					type="button" value="导出" />
			</div>



		</form:form>
		<tags:message content="${message}" />

		<div class="form-horizontal" style="margin-right:0px;">

		<table class="table table-striped table-bordered table-condensed addTable" id="tableW">
			
				<!--新增  -->
					
					<c:set var="index" value="0"/>
					<c:set var="xz" value="0"/>
					<c:set var="xzqy" value="0"/>
					<c:set var="xzgeqy" value="0"/>
					<c:set var="xzgejg" value="0"/>
					<c:set var="xzjg" value="0"/>
						<c:if test="${sumList.total.xzqyadd1.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzqyadd2.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzqyadd3.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzqyadd4.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzqyadd5.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd1.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd2.totalCount!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd3.totalCount!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd4.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd5.totalCount!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd1.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
						<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd2.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd3.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd4.totalCount!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd5.totalCount !=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd1.totalCount !=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd2.totalCount !=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd3.totalCount !=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd4.totalCount !=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd5.totalCount !=0}">
						<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!-- 更新 -->
						<c:set var="gx" value="0"/>
						<c:set var="gxqy" value="0"/>
						<c:set var="gxgeqy" value="0"/>
						<c:set var="gxgejg" value="0"/>
						<c:set var="gxjg" value="0"/>
					
						<c:if test="${sumList.total.gxqyadd1.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd2.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd3.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd4.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd5.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd1.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd2.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd3.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd4.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd5.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd1.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd2.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd3.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd4.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd5.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd1.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd2.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd3.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd4.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd5.totalCount !=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!--遗失补办  -->
						<c:set var="ysbb" value="0"/>
						<c:set var="ysbbqy" value="0"/>
						<c:set var="ysbbgeqy" value="0"/>
						<c:set var="ysbbgejg" value="0"/>
						<c:set var="ysbbjg" value="0"/>
						<c:if test="${sumList.total.lostCerateqy.totalCount !=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbqy" value="${ysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCerategrQ.totalCount !=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgeqy" value="${ysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCerategrG.totalCount !=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgejg" value="${ysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCeratejg.totalCount !=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbjg" value="${ysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!--损坏更换  -->
						<c:set var="shgh" value="0"/>
						<c:set var="shghqy" value="0"/>
						<c:set var="shghgeqy" value="0"/>
						<c:set var="shghgejg" value="0"/>
						<c:set var="shghjg" value="0"/>
						<c:if test="${sumList.total.damageCertificateqy.totalCount !=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghqy" value="${shghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificategrQ.totalCount !=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgeqy" value="${shghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificategrG.totalCount !=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgejg" value="${shghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificatejg.totalCount !=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghjg" value="${shghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						
						<!--变更  -->
						<c:set var="bg" value="0"/>
						<c:set var="bgqy" value="0"/>
						<c:set var="bggeqy" value="0"/>
						<c:set var="bggejg" value="0"/>
						<c:set var="bgjg" value="0"/>
						<c:if test="${sumList.total.modifyNumqy.totalCount !=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bgqy" value="${bgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumgrQ.totalCount !=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bggeqy" value="${bggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumgrG.totalCount !=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bggejg" value="${bggejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumjg.totalCount !=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bgjg" value="${bgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
					
					<!--变更+遗失补办  -->
						<c:set var="bgysbb" value="0"/>
						<c:set var="bgysbbqy" value="0"/>
						<c:set var="bgysbbgeqy" value="0"/>
						<c:set var="bgysbbgejg" value="0"/>
						<c:set var="bgysbbjg" value="0"/>
						<c:if test="${sumList.total.changeLostqyNum.totalCount !=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbqy" value="${bgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostgrQNum.totalCount !=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbgeqy" value="${bgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostgrGNum.totalCount !=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbgejg" value="${bgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostjgNum.totalCount !=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbjg" value="${bgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!--变更+损坏更换  -->
						<c:set var="bgshgh" value="0"/>
						<c:set var="bgshghqy" value="0"/>
						<c:set var="bgshghgeqy" value="0"/>
						<c:set var="bgshghgejg" value="0"/>
						<c:set var="bgshghjg" value="0"/>
						<c:if test="${sumList.total.changeReplaceqyNum.totalCount !=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghqy" value="${bgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacegrQNum.totalCount !=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghgeqy" value="${bgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacegrGNum.totalCount !=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghgejg" value="${bgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacejgNum.totalCount !=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghjg" value="${bgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!-- 更新+遗失补办 -->
						<c:set var="gxysbb" value="0"/>
						<c:set var="gxysbbqy" value="0"/>
					<c:set var="gxysbbgeqy" value="0"/>
					<c:set var="gxysbbgejg" value="0"/>
					<c:set var="gxysbbjg" value="0"/>
					
						<c:if test="${sumList.total.updateLostqyNum.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum2.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum3.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum4.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum5.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum2.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum3.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum4.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum5.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
						<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum2.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum3.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum4.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum5.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum2.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum3.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum4.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum5.totalCount !=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!-- 更新+损坏更换 -->
						<c:set var="gxshgh" value="0"/>
						<c:set var="gxshghqy" value="0"/>
						<c:set var="gxshghgeqy" value="0"/>
						<c:set var="gxshghgejg" value="0"/>
						<c:set var="gxshghjg" value="0"/>
						<c:if test="${sumList.total.updateReplaceqyNum.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum3.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum3.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgxgejg" value="${gxshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum3.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum2.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum3.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum4.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum5.totalCount !=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<!-- 更新 +变更-->
						<c:set var="gxbg" value="0"/>
						<c:set var="gxbgqy" value="0"/>
						<c:set var="gxbggeqy" value="0"/>
						<c:set var="gxbggejg" value="0"/>
						<c:set var="gxbgjg" value="0"/>
					
						<c:if test="${sumList.total.updateChangeqyNum.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum2.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum3.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum4.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum5.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum2.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum3.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum4.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum5.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
						<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum2.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum3.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum4.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum5.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum2.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum3.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum4.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum5.totalCount !=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						
						<!-- 更新 +变更+遗失补办-->
						<c:set var="gxbgysbb" value="0"/>
						<c:set var="gxbgysbbqy" value="0"/>
						<c:set var="gxbgysbbgeqy" value="0"/>
						<c:set var="gxbgysbbgejg" value="0"/>
						<c:set var="gxbgysbbjg" value="0"/>
						<c:if test="${sumList.total.changeUpdateLostqyNum.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum3.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum3.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum3.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum3.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5.totalCount !=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						
						<!-- 更新 +变更+损坏更换-->
						<c:set var="gxbgshgh" value="0"/>
						<c:set var="gxbgshghqy" value="0"/>
						<c:set var="gxbgshghgeqy" value="0"/>
						<c:set var="gxbgshghgejg" value="0"/>
						<c:set var="gxbgshghjg" value="0"/>
					
						<c:if test="${sumList.total.changeUpdateReplaceqyNum.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum3.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum3.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum3.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum3.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5.totalCount !=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
							<c:set var="index" value="${index+1}"/>
						</c:if>
			<tr>
				<th colspan="${index+1}" style="text-align: center;">项目:${yingyong}</th>
			</tr>
			<tr>
				<th rowspan="3" style="width:10%;vertical-align:middle">月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份</th>
			<c:if test="${xz >0}">
				<th colspan="${xz}">新增</th>
   			 </c:if>
   			 <c:if test="${gx >0}" >
				<th colspan="${gx}" >更新</th>
   			 </c:if>
   			 <c:if test="${ysbb >0}">
				<th colspan="${ysbb}" >遗失补办</th>
   			 </c:if>
   			 <c:if test="${shgh >0}">
				<th colspan="${shgh}" > 损坏更换</th>
   			 </c:if>
   			 <c:if test="${bg >0}">
				<th colspan="${bg}" >变更</th>
   			 </c:if>
   			 <c:if test="${multiType}">
   			 <c:if test="${bgysbb >0}">
				<th colspan="${bgysbb}" >变更+遗失补办</th>
   			 </c:if>
   			 <c:if test="${bgshgh >0}">
				<th colspan="${bgshgh}" >变更+损坏更换</th>
   			 </c:if>
   			 <c:if test="${gxysbb >0}">
				<th colspan="${gxysbb}" > 更新+遗失补办</th>
   			 </c:if>
   			  <c:if test="${gxshgh >0}">
				<th colspan="${gxshgh}" >更新+损坏更换</th>
   			 </c:if>
   			  <c:if test="${gxbg >0}">
				<th colspan="${gxbg}" > 更新+变更</th>
   			 </c:if> 
   			  <c:if test="${gxbgysbb >0}">
				<th colspan="${gxbgysbb}" >更新+变更+遗失补办</th>
   			 </c:if> 
   		 	  <c:if test="${gxbgshgh >0}">
				<th colspan="${gxbgshgh}" > 更新+变更+损坏更换</th>
   			 </c:if> 
   			 </c:if>
				
				
			</tr>
			<tr>
			  <c:if test="${xzqy >0}">
				<th colspan="${xzqy}"style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${xzgeqy >0}">
				<th colspan="${xzgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${xzgejg >0}">
				<th colspan="${xzgejg}"style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${xzjg >0}">
				<th colspan="${xzjg}" style="text-align: center">机构证书</th>
   			 </c:if>
   			  <c:if test="${gxqy >0}">
				<th colspan="${gxqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxgeqy >0}">
				<th colspan="${gxgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxgejg >0}">
				<th colspan="${gxgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxjg >0}">
				<th colspan="${gxjg}" style="text-align: center">机构证书</th>
   			 </c:if>
   			  <c:if test="${ysbbqy >0}">
				<th colspan="${ysbbqy}"  style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${ysbbgeqy >0}">
				<th colspan="${ysbbgeqy}"  style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${ysbbgejg >0}">
				<th colspan="${ysbbgejg}"  style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${ysbbjg >0}">
				<th colspan="${ysbbjg} "  style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${shghqy >0}">
				<th colspan="${shghqy}"  style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${shghgeqy >0}">
				<th colspan="${shghgeqy}"  style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${shghgejg >0}">
				<th colspan="${shghgejg}"  style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${shghjg >0}">
				<th colspan="${shghjg} "  style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${bgqy >0}">
				<th colspan="${bgqy}"  style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${bggeqy >0}">
				<th colspan="${bggeqy}"  style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bggejg >0}">
				<th colspan="${bggejg}"  style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgjg >0}">
				<th colspan="${bgjg} "  style="text-align: center">机构证书</th>
   			 </c:if>
   			 	<c:if test="${multiType}">
				 <c:if test="${bgysbbqy >0}">
				<th colspan="${bgysbbqy}"  style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${bgysbbgeqy >0}">
				<th colspan="${bgysbbgeqy}"  style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bgysbbgejg >0}">
				<th colspan="${bgysbbgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgysbbjg >0}">
				<th colspan="${bgysbbjg} "  style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${bgshghqy >0}">
				<th colspan="${bgshghqy}"  style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${bgshghgeqy >0}">
				<th colspan="${bgshghgeqy}"  style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bgshghgejg >0}">
				<th colspan="${bgshghgejg}"  style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgshghjg >0}">
				<th colspan="${bgysbbjg} "  style="text-align: center">机构证书</th>
   			 </c:if>
   			  <c:if test="${gxysbbqy >0}">
				<th colspan="${gxysbbqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxysbbgeqy >0}">
				<th colspan="${gxysbbgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxysbbgejg >0}">
				<th colspan="${gxysbbgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxysbbjg >0}">
				<th colspan="${gxysbbjg}" style="text-align: center">机构证书</th>
   			 </c:if>
   			   <c:if test="${gxshghqy >0}">
				<th colspan="${gxshghqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxshghgeqy >0}">
				<th colspan="${gxshghgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxshghgejg >0}">
				<th colspan="${gxshghgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxshghjg >0}">
				<th colspan="${gxshghjg}" style="text-align: center">机构证书</th>
   			 </c:if> 
   			   <c:if test="${gxbgqy >0}">
				<th colspan="${gxbgqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxbggeqy >0}">
				<th colspan="${gxbggeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxbggejg >0}">
				<th colspan="${gxbggejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxbgjg >0}">
				<th colspan="${gxbgjg}" style="text-align: center">机构证书</th>
   			 </c:if> 
   			 
   			   <c:if test="${gxbgysbbqy >0}">
				<th colspan="${gxbgysbbqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxbgysbbgeqy >0}">
				<th colspan="${gxbgysbbgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxbgysbbgejg >0}">
				<th colspan="${gxbgysbbgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxbgysbbjg >0}">
				<th colspan="${gxbgysbbjg}" style="text-align: center">机构证书</th>
   			 </c:if> 
   			 
   			   <c:if test="${gxbgshghqy >0}">
				<th colspan="${gxbgshghqy}" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${gxbgshghgeqy >0}">
				<th colspan="${gxbgshghgeqy }"style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			  <c:if test="${gxbgshghgejg >0}">
				<th colspan="${gxbgshghgejg}" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			  <c:if test="${gxbgshghjg >0}">
				<th colspan="${gxbgshghjg}" style="text-align: center">机构证书</th>
   			 </c:if> 
   			 </c:if>
			</tr>
			<tr>
			
						<c:if test="${sumList.total.xzqyadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzqyadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzqyadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzqyadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzqyadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzqyadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzqyadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzqyadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzqyadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzqyadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrQadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrQadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrQadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrQadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrQadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrQadd3 }" theader="三 年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrQadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrQadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrQadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrQadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrGadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrGadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrGadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrGadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrGadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrGadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrGadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrGadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzgrGadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzgrGadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzjgadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzjgadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzjgadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzjgadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzjgadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzjgadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzjgadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzjgadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.xzjgadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.xzjgadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						
						<c:if test="${sumList.total.gxqyadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxqyadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxqyadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxqyadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxqyadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxqyadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxqyadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxqyadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxqyadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxqyadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrQadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrQadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrQadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrQadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrQadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrQadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrQadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrQadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrQadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrQadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrGadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrGadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrGadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrGadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrGadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrGadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrGadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrGadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxgrGadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxgrGadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxjgadd1.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxjgadd1 }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxjgadd2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxjgadd2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxjgadd3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxjgadd3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxjgadd4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxjgadd4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.gxjgadd5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.gxjgadd5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.lostCerateqy.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.lostCerateqy }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.lostCerategrQ.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.lostCerategrQ }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.lostCerategrG.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.lostCerategrG }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.lostCeratejg.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.lostCeratejg }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.damageCertificateqy.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.damageCertificateqy }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.damageCertificategrQ.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.damageCertificategrQ }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.damageCertificategrG.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.damageCertificategrG }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.damageCertificatejg.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.damageCertificatejg }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.modifyNumqy.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.modifyNumqy }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.modifyNumgrQ.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.modifyNumgrQ }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.modifyNumgrG.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.modifyNumgrG }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.modifyNumjg.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.modifyNumjg }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						
						
						<c:if test="${multiType}">
						<c:if test="${sumList.total.changeLostqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeLostqyNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeLostgrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeLostgrQNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeLostgrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeLostgrGNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeLostjgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeLostjgNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>						
						<c:if test="${sumList.total.changeReplaceqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeReplaceqyNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeReplacegrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeReplacegrQNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeReplacegrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeReplacegrGNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeReplacejgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeReplacejgNum }" theader="全部"></tags:settlepaymethodheader></th></c:if>
						
						<c:if test="${sumList.total.updateLostqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostqyNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostqyNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostqyNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostqyNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostqyNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostqyNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostqyNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostqyNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostqyNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrQNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrQNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrQNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrQNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrQNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrGNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrGNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrGNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrGNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostgrGNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostjgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostjgNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostjgNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostjgNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostjgNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostjgNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostjgNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostjgNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateLostjgNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateLostjgNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplaceqyNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplaceqyNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplaceqyNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplaceqyNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplaceqyNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrQNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrQNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrQNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrQNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrQNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrGNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrGNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrGNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrGNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacegrGNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacejgNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacejgNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacejgNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacejgNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateReplacejgNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if> 
						
						<c:if test="${sumList.total.updateChangeqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangeqyNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangeqyNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangeqyNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangeqyNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangeqyNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrQNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrQNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrQNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrQNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrQNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrGNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrGNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrGNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrGNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangegrGNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangejgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangejgNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangejgNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangejgNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangejgNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangejgNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangejgNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangejgNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.updateChangejgNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.updateChangejgNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateLostqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostqyNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostqyNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostqyNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostqyNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostqyNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrQNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrQNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrQNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrQNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrQNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrGNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrGNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrGNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrGNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostgrGNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostjgNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostjgNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostjgNum3 }" theader= "三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostjgNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateLostjgNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateReplaceqyNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplaceqyNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplaceqyNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplaceqyNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplaceqyNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplaceqyNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrQNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrQNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrQNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrQNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrQNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrGNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrGNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrGNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrGNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacegrGNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacejgNum }" theader="一年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacejgNum2 }" theader="二年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum3.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacejgNum3 }" theader="三年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacejgNum4 }" theader="四年"></tags:settlepaymethodheader></th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5.totalCount !=0}"><th><tags:settlepaymethodheader total="${sumList.total.changeUpdateReplacejgNum5 }" theader="五年"></tags:settlepaymethodheader></th></c:if>
				 		</c:if>
	 		</tr>
				
				
						
						<c:forEach items="${sumList}" var ="sum">						
						<c:if test="${sum.key!='total' and  sum.key!='totalColumn'}">
						<tr>
						<td><c:if test="${sum.key!='total' and  sum.key!='totalColumn' }">${sum.key}</c:if></td>
														
						<c:if test="${sumList.total.xzqyadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzqyadd1}" total="${sumList.total.xzqyadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzqyadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzqyadd2}" total="${sumList.total.xzqyadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzqyadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzqyadd3}" total="${sumList.total.xzqyadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzqyadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzqyadd4}" total="${sumList.total.xzqyadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzqyadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzqyadd5}" total="${sumList.total.xzqyadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrQadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrQadd1}" total="${sumList.total.xzgrQadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrQadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrQadd2}" total="${sumList.total.xzgrQadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrQadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrQadd3}" total="${sumList.total.xzgrQadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrQadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrQadd4}" total="${sumList.total.xzgrQadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrQadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrQadd5}" total="${sumList.total.xzgrQadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrGadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrGadd1}" total="${sumList.total.xzgrGadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrGadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrGadd2}" total="${sumList.total.xzgrGadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrGadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrGadd3}" total="${sumList.total.xzgrGadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrGadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrGadd4}" total="${sumList.total.xzgrGadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzgrGadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzgrGadd5}" total="${sumList.total.xzgrGadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzjgadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzjgadd1}" total="${sumList.total.xzjgadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzjgadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzjgadd2}" total="${sumList.total.xzjgadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzjgadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzjgadd3}" total="${sumList.total.xzjgadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzjgadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzjgadd4}" total="${sumList.total.xzjgadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.xzjgadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.xzjgadd5}" total="${sumList.total.xzjgadd5 }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.gxqyadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxqyadd1}" total="${sumList.total.gxqyadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxqyadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxqyadd2}" total="${sumList.total.gxqyadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxqyadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxqyadd3}" total="${sumList.total.gxqyadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxqyadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxqyadd4}" total="${sumList.total.gxqyadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxqyadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxqyadd5}" total="${sumList.total.gxqyadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrQadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrQadd1}" total="${sumList.total.gxgrQadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrQadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrQadd2}" total="${sumList.total.gxgrQadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrQadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrQadd3}" total="${sumList.total.gxgrQadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrQadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrQadd4}" total="${sumList.total.gxgrQadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrQadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrQadd5}" total="${sumList.total.gxgrQadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrGadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrGadd1}" total="${sumList.total.gxgrGadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrGadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrGadd2}" total="${sumList.total.gxgrGadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrGadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrGadd3}" total="${sumList.total.gxgrGadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrGadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrGadd4}" total="${sumList.total.gxgrGadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxgrGadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxgrGadd5}" total="${sumList.total.gxgrGadd5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxjgadd1.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxjgadd1}" total="${sumList.total.gxjgadd1 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxjgadd2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxjgadd2}" total="${sumList.total.gxjgadd2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxjgadd3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxjgadd3}" total="${sumList.total.gxjgadd3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxjgadd4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxjgadd4}" total="${sumList.total.gxjgadd4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.gxjgadd5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.gxjgadd5}" total="${sumList.total.gxjgadd5 }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.lostCerateqy.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.lostCerateqy}" total="${sumList.total.lostCerateqy }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.lostCerategrQ.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.lostCerategrQ}" total="${sumList.total.lostCerategrQ }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.lostCerategrG.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.lostCerategrG}" total="${sumList.total.lostCerategrG }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.lostCeratejg.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.lostCeratejg}" total="${sumList.total.lostCeratejg }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.damageCertificateqy.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.damageCertificateqy}" total="${sumList.total.damageCertificateqy }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.damageCertificategrQ.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.damageCertificategrQ}" total="${sumList.total.damageCertificategrQ }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.damageCertificategrG.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.damageCertificategrG}" total="${sumList.total.damageCertificategrG }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.damageCertificatejg.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.damageCertificatejg}" total="${sumList.total.damageCertificatejg }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.modifyNumqy.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.modifyNumqy}" total="${sumList.total.modifyNumqy }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.modifyNumgrQ.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.modifyNumgrQ}" total="${sumList.total.modifyNumgrQ }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.modifyNumgrG.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.modifyNumgrG}" total="${sumList.total.modifyNumgrG }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.modifyNumjg.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.modifyNumjg}" total="${sumList.total.modifyNumjg }"></tags:settlepaymethod></td></c:if>
						<c:if test="${multiType}">
						<c:if test="${sumList.total.changeLostqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeLostqyNum}" total="${sumList.total.changeLostqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeLostgrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeLostgrQNum}" total="${sumList.total.changeLostgrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeLostgrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeLostgrGNum}" total="${sumList.total.changeLostgrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeLostjgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeLostjgNum}" total="${sumList.total.changeLostjgNum }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.changeReplaceqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeReplaceqyNum}" total="${sumList.total.changeReplaceqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeReplacegrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeReplacegrQNum}" total="${sumList.total.changeReplacegrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeReplacegrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeReplacegrGNum}" total="${sumList.total.changeReplacegrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeReplacejgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeReplacejgNum}" total="${sumList.total.changeReplacejgNum }"></tags:settlepaymethod></td></c:if>
						
						<c:if test="${sumList.total.updateLostqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostqyNum}" total="${sumList.total.updateLostqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostqyNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostqyNum2}" total="${sumList.total.updateLostqyNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostqyNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostqyNum3}" total="${sumList.total.updateLostqyNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostqyNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostqyNum4}" total="${sumList.total.updateLostqyNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostqyNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostqyNum5}" total="${sumList.total.updateLostqyNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrQNum}" total="${sumList.total.updateLostgrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrQNum2}" total="${sumList.total.updateLostgrQNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrQNum3}" total="${sumList.total.updateLostgrQNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrQNum4}" total="${sumList.total.updateLostgrQNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrQNum5}" total="${sumList.total.updateLostgrQNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrGNum}" total="${sumList.total.updateLostgrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrGNum2}" total="${sumList.total.updateLostgrGNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrGNum3}" total="${sumList.total.updateLostgrGNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrGNum4}" total="${sumList.total.updateLostgrGNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostgrGNum5}" total="${sumList.total.updateLostgrGNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostjgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostjgNum}" total="${sumList.total.updateLostjgNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostjgNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostjgNum2}" total="${sumList.total.updateLostjgNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostjgNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostjgNum3}" total="${sumList.total.updateLostjgNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostjgNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostjgNum4}" total="${sumList.total.updateLostjgNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateLostjgNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateLostjgNum5}" total="${sumList.total.updateLostjgNum5 }"></tags:settlepaymethod></td></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplaceqyNum}" total="${sumList.total.updateReplaceqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplaceqyNum2}" total="${sumList.total.updateReplaceqyNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplaceqyNum3}" total="${sumList.total.updateReplaceqyNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplaceqyNum4}" total="${sumList.total.updateReplaceqyNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplaceqyNum5}" total="${sumList.total.updateReplaceqyNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrQNum}" total="${sumList.total.updateReplacegrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrQNum2}" total="${sumList.total.updateReplacegrQNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrQNum3}" total="${sumList.total.updateReplacegrQNum3}"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrQNum4}" total="${sumList.total.updateReplacegrQNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrQNum5}" total="${sumList.total.updateReplacegrQNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrGNum}" total="${sumList.total.updateReplacegrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrGNum2}" total="${sumList.total.updateReplacegrGNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrGNum3}" total="${sumList.total.updateReplacegrGNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrGNum4}" total="${sumList.total.updateReplacegrGNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacegrGNum5}" total="${sumList.total.updateReplacegrGNum5}"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacejgNum}" total="${sumList.total.updateReplacejgNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacejgNum2}" total="${sumList.total.updateReplacejgNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacejgNum3}" total="${sumList.total.updateReplacejgNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacejgNum4}" total="${sumList.total.updateReplacejgNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateReplacejgNum5}" total="${sumList.total.updateReplacejgNum5 }"></tags:settlepaymethod></td></c:if> 
						
						 <c:if test="${sumList.total.updateChangeqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangeqyNum}" total="${sumList.total.updateChangeqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangeqyNum2}" total="${sumList.total.updateChangeqyNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangeqyNum3}" total="${sumList.total.updateChangeqyNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangeqyNum4}" total="${sumList.total.updateChangeqyNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangeqyNum5}" total="${sumList.total.updateChangeqyNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrQNum}" total="${sumList.total.updateChangegrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrQNum2}" total="${sumList.total.updateChangegrQNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrQNum3}" total="${sumList.total.updateChangegrQNum3}"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrQNum4}" total="${sumList.total.updateChangegrQNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrQNum5}" total="${sumList.total.updateChangegrQNum5}"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrGNum}" total="${sumList.total.updateChangegrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrGNum2}" total="${sumList.total.updateChangegrGNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrGNum3}" total="${sumList.total.updateChangegrGNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrGNum4}" total="${sumList.total.updateChangegrGNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangegrGNum5}" total="${sumList.total.updateChangegrGNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangejgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangejgNum}" total="${sumList.total.updateChangejgNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangejgNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangejgNum2}" total="${sumList.total.updateChangejgNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangejgNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangejgNum3}" total="${sumList.total.updateChangejgNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangejgNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangejgNum4}" total="${sumList.total.updateChangejgNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.updateChangejgNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.updateChangejgNum5}" total="${sumList.total.updateChangejgNum5 }"></tags:settlepaymethod></td></c:if> 
						
						
						 <c:if test="${sumList.total.changeUpdateLostqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostqyNum}" total="${sumList.total.changeUpdateLostqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostqyNum2}" total="${sumList.total.changeUpdateLostqyNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostqyNum3}" total="${sumList.total.changeUpdateLostqyNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostqyNum4}" total="${sumList.total.changeUpdateLostqyNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostqyNum5}" total="${sumList.total.changeUpdateLostqyNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrQNum}" total="${sumList.total.changeUpdateLostgrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrQNum2}" total="${sumList.total.changeUpdateLostgrQNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrQNum3}" total="${sumList.total.changeUpdateLostgrQNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrQNum4}" total="${sumList.total.changeUpdateLostgrQNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrQNum5}" total="${sumList.total.changeUpdateLostgrQNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrGNum}" total="${sumList.total.changeUpdateLostgrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrGNum2}" total="${sumList.total.changeUpdateLostgrGNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrGNum3}" total="${sumList.total.changeUpdateLostgrGNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrGNum4}" total="${sumList.total.changeUpdateLostgrGNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostgrGNum5}" total="${sumList.total.changeUpdateLostgrGNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostjgNum}" total="${sumList.total.changeUpdateLostjgNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostjgNum2}" total="${sumList.total.changeUpdateLostjgNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostjgNum3}" total="${sumList.total.changeUpdateLostjgNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostjgNum4}" total="${sumList.total.changeUpdateLostjgNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateLostjgNum5}" total="${sumList.total.changeUpdateLostjgNum5 }"></tags:settlepaymethod></td></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateReplaceqyNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplaceqyNum}" total="${sumList.total.changeUpdateReplaceqyNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplaceqyNum2}" total="${sumList.total.changeUpdateReplaceqyNum2}"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplaceqyNum3}" total="${sumList.total.changeUpdateReplaceqyNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplaceqyNum4}" total="${sumList.total.changeUpdateReplaceqyNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplaceqyNum5}" total="${sumList.total.changeUpdateReplaceqyNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrQNum}" total="${sumList.total.changeUpdateReplacegrQNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrQNum2}" total="${sumList.total.changeUpdateReplacegrQNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrQNum3}" total="${sumList.total.changeUpdateReplacegrQNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrQNum4}" total="${sumList.total.changeUpdateReplacegrQNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrQNum5}" total="${sumList.total.changeUpdateReplacegrQNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrGNum}" total="${sumList.total.changeUpdateReplacegrGNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrGNum2}" total="${sumList.total.changeUpdateReplacegrGNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrGNum3}" total="${sumList.total.changeUpdateReplacegrGNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrGNum4}" total="${sumList.total.changeUpdateReplacegrGNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacegrGNum5}" total="${sumList.total.changeUpdateReplacegrGNum5 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacejgNum}" total="${sumList.total.changeUpdateReplacejgNum }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacejgNum2}" total="${sumList.total.changeUpdateReplacejgNum2 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum3.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacejgNum3}" total="${sumList.total.changeUpdateReplacejgNum3 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacejgNum4}" total="${sumList.total.changeUpdateReplacejgNum4 }"></tags:settlepaymethod></td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5.totalCount !=0}"><td><tags:settlepaymethod content="${sum.value.changeUpdateReplacejgNum5}" total="${sumList.total.changeUpdateReplacejgNum5 }"></tags:settlepaymethod></td></c:if> 
						</c:if>
						</tr>
						</c:if>
					
							</c:forEach>
							
						
			
			<tr> 
				
						<th>总计</th>
						<c:if test="${sumList.total.xzqyadd1.totalCount !=0}"><td>${sumList.total.xzqyadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzqyadd2.totalCount !=0}"><td>${sumList.total.xzqyadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzqyadd3.totalCount !=0}"><td>${sumList.total.xzqyadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzqyadd4.totalCount !=0}"><td>${sumList.total.xzqyadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzqyadd5.totalCount !=0}"><td>${sumList.total.xzqyadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd1.totalCount !=0}"><td>${sumList.total.xzgrQadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd2.totalCount !=0}"><td>${sumList.total.xzgrQadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd3.totalCount !=0}"><td>${sumList.total.xzgrQadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd4.totalCount !=0}"><td>${sumList.total.xzgrQadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd5.totalCount !=0}"><td>${sumList.total.xzgrQadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd1.totalCount !=0}"><td>${sumList.total.xzgrGadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd2.totalCount !=0}"><td>${sumList.total.xzgrGadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd3.totalCount !=0}"><td>${sumList.total.xzgrGadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd4.totalCount !=0}"><td>${sumList.total.xzgrGadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd5.totalCount !=0}"><td>${sumList.total.xzgrGadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzjgadd1.totalCount !=0}"><td>${sumList.total.xzjgadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzjgadd2.totalCount !=0}"><td>${sumList.total.xzjgadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzjgadd3.totalCount !=0}"><td>${sumList.total.xzjgadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzjgadd4.totalCount !=0}"><td>${sumList.total.xzjgadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.xzjgadd5.totalCount !=0}"><td>${sumList.total.xzjgadd5.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.gxqyadd1.totalCount !=0}"><td>${sumList.total.gxqyadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxqyadd2.totalCount !=0}"><td>${sumList.total.gxqyadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxqyadd3.totalCount !=0}"><td>${sumList.total.gxqyadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxqyadd4.totalCount !=0}"><td>${sumList.total.gxqyadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxqyadd5.totalCount !=0}"><td>${sumList.total.gxqyadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd1.totalCount !=0}"><td>${sumList.total.gxgrQadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd2.totalCount !=0}"><td>${sumList.total.gxgrQadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd3.totalCount !=0}"><td>${sumList.total.gxgrQadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd4.totalCount !=0}"><td>${sumList.total.gxgrQadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd5.totalCount !=0}"><td>${sumList.total.gxgrQadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd1.totalCount !=0}"><td>${sumList.total.gxgrGadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd2.totalCount !=0}"><td>${sumList.total.gxgrGadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd3.totalCount !=0}"><td>${sumList.total.gxgrGadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd4.totalCount !=0}"><td>${sumList.total.gxgrGadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd5.totalCount !=0}"><td>${sumList.total.gxgrGadd5.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxjgadd1.totalCount !=0}"><td>${sumList.total.gxjgadd1.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxjgadd2.totalCount !=0}"><td>${sumList.total.gxjgadd2.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxjgadd3.totalCount !=0}"><td>${sumList.total.gxjgadd3.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxjgadd4.totalCount !=0}"><td>${sumList.total.gxjgadd4.totalCount}</td></c:if>
						<c:if test="${sumList.total.gxjgadd5.totalCount !=0}"><td>${sumList.total.gxjgadd5.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.lostCerateqy.totalCount !=0}"><td>${sumList.total.lostCerateqy.totalCount}</td></c:if>
						<c:if test="${sumList.total.lostCerategrQ.totalCount !=0}"><td>${sumList.total.lostCerategrQ.totalCount}</td></c:if>
						<c:if test="${sumList.total.lostCerategrG.totalCount !=0}"><td>${sumList.total.lostCerategrG.totalCount}</td></c:if>
						<c:if test="${sumList.total.lostCeratejg.totalCount !=0}"><td>${sumList.total.lostCeratejg.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.damageCertificateqy.totalCount !=0}"><td>${sumList.total.damageCertificateqy.totalCount}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrQ.totalCount !=0}"><td>${sumList.total.damageCertificategrQ.totalCount}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrG.totalCount !=0}"><td>${sumList.total.damageCertificategrG.totalCount}</td></c:if>
						<c:if test="${sumList.total.damageCertificatejg.totalCount !=0}"><td>${sumList.total.damageCertificatejg.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.modifyNumqy.totalCount !=0}"><td>${sumList.total.modifyNumqy.totalCount}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrQ.totalCount !=0}"><td>${sumList.total.modifyNumgrQ.totalCount}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrG.totalCount !=0}"><td>${sumList.total.modifyNumgrG.totalCount}</td></c:if>
						<c:if test="${sumList.total.modifyNumjg.totalCount !=0}"><td>${sumList.total.modifyNumjg.totalCount}</td></c:if>
						<c:if test="${multiType}">
						<c:if test="${sumList.total.changeLostqyNum.totalCount !=0}"><td>${sumList.total.changeLostqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeLostgrQNum.totalCount !=0}"><td>${sumList.total.changeLostgrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeLostgrGNum.totalCount !=0}"><td>${sumList.total.changeLostgrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeLostjgNum.totalCount !=0}"><td>${sumList.total.changeLostjgNum.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.changeReplaceqyNum.totalCount !=0}"><td>${sumList.total.changeReplaceqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrQNum.totalCount !=0}"><td>${sumList.total.changeReplacegrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrGNum.totalCount !=0}"><td>${sumList.total.changeReplacegrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeReplacejgNum.totalCount !=0}"><td>${sumList.total.changeReplacejgNum.totalCount}</td></c:if>
						
						<c:if test="${sumList.total.updateLostqyNum.totalCount !=0}"><td>${sumList.total.updateLostqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum2.totalCount !=0}"><td>${sumList.total.updateLostqyNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum3.totalCount !=0}"><td>${sumList.total.updateLostqyNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum4.totalCount !=0}"><td>${sumList.total.updateLostqyNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum5.totalCount !=0}"><td>${sumList.total.updateLostqyNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum.totalCount !=0}"><td>${sumList.total.updateLostgrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2.totalCount !=0}"><td>${sumList.total.updateLostgrQNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum3.totalCount !=0}"><td>${sumList.total.updateLostgrQNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4.totalCount !=0}"><td>${sumList.total.updateLostgrQNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5.totalCount !=0}"><td>${sumList.total.updateLostgrQNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum.totalCount !=0}"><td>${sumList.total.updateLostgrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2.totalCount !=0}"><td>${sumList.total.updateLostgrGNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum3.totalCount !=0}"><td>${sumList.total.updateLostgrGNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4.totalCount !=0}"><td>${sumList.total.updateLostgrGNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5.totalCount !=0}"><td>${sumList.total.updateLostgrGNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum.totalCount !=0}"><td>${sumList.total.updateLostjgNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum2.totalCount !=0}"><td>${sumList.total.updateLostjgNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum3.totalCount !=0}"><td>${sumList.total.updateLostjgNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum4.totalCount !=0}"><td>${sumList.total.updateLostjgNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum5.totalCount !=0}"><td>${sumList.total.updateLostjgNum5.totalCount}</td></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum.totalCount !=0}"><td>${sumList.total.updateReplaceqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2.totalCount !=0}"><td>${sumList.total.updateReplaceqyNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum3.totalCount !=0}"><td>${sumList.total.updateReplaceqyNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4.totalCount !=0}"><td>${sumList.total.updateReplaceqyNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5.totalCount !=0}"><td>${sumList.total.updateReplaceqyNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum.totalCount !=0}"><td>${sumList.total.updateReplacegrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2.totalCount !=0}"><td>${sumList.total.updateReplacegrQNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum3.totalCount !=0}"><td>${sumList.total.updateReplacegrQNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4.totalCount !=0}"><td>${sumList.total.updateReplacegrQNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5.totalCount !=0}"><td>${sumList.total.updateReplacegrQNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum.totalCount !=0}"><td>${sumList.total.updateReplacegrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2.totalCount !=0}"><td>${sumList.total.updateReplacegrGNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum3.totalCount !=0}"><td>${sumList.total.updateReplacegrGNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4.totalCount !=0}"><td>${sumList.total.updateReplacegrGNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5.totalCount !=0}"><td>${sumList.total.updateReplacegrGNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum.totalCount !=0}"><td>${sumList.total.updateReplacejgNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2.totalCount !=0}"><td>${sumList.total.updateReplacejgNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum3.totalCount !=0}"><td>${sumList.total.updateReplacejgNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4.totalCount !=0}"><td>${sumList.total.updateReplacejgNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5.totalCount !=0}"><td>${sumList.total.updateReplacejgNum5.totalCount}</td></c:if> 
						
						  <c:if test="${sumList.total.updateChangeqyNum.totalCount !=0}"><td>${sumList.total.updateChangeqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2.totalCount !=0}"><td>${sumList.total.updateChangeqyNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum3.totalCount !=0}"><td>${sumList.total.updateChangeqyNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4.totalCount !=0}"><td>${sumList.total.updateChangeqyNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5.totalCount !=0}"><td>${sumList.total.updateChangeqyNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum.totalCount !=0}"><td>${sumList.total.updateChangegrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2.totalCount !=0}"><td>${sumList.total.updateChangegrQNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum3.totalCount !=0}"><td>${sumList.total.updateChangegrQNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4.totalCount !=0}"><td>${sumList.total.updateChangegrQNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5.totalCount !=0}"><td>${sumList.total.updateChangegrQNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum.totalCount !=0}"><td>${sumList.total.updateChangegrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2.totalCount !=0}"><td>${sumList.total.updateChangegrGNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum3.totalCount !=0}"><td>${sumList.total.updateChangegrGNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4.totalCount !=0}"><td>${sumList.total.updateChangegrGNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5.totalCount !=0}"><td>${sumList.total.updateChangegrGNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum.totalCount !=0}"><td>${sumList.total.updateChangejgNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum2.totalCount !=0}"><td>${sumList.total.updateChangejgNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum3.totalCount !=0}"><td>${sumList.total.updateChangejgNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum4.totalCount !=0}"><td>${sumList.total.updateChangejgNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum5.totalCount !=0}"><td>${sumList.total.updateChangejgNum5.totalCount}</td></c:if>  
						
						
						 
						<c:if test="${sumList.total.changeUpdateLostqyNum.totalCount !=0}"><td>${sumList.total.changeUpdateLostqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2.totalCount !=0}"><td>${sumList.total.changeUpdateLostqyNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum3.totalCount !=0}"><td>${sumList.total.changeUpdateLostqyNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4.totalCount !=0}"><td>${sumList.total.changeUpdateLostqyNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5.totalCount !=0}"><td>${sumList.total.changeUpdateLostqyNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrQNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum3.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrQNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrQNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrQNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrGNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum3.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrGNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrGNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5.totalCount !=0}"><td>${sumList.total.changeUpdateLostgrGNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum.totalCount !=0}"><td>${sumList.total.changeUpdateLostjgNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2.totalCount !=0}"><td>${sumList.total.changeUpdateLostjgNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum3.totalCount !=0}"><td>${sumList.total.changeUpdateLostjgNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4.totalCount !=0}"><td>${sumList.total.changeUpdateLostjgNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5.totalCount !=0}"><td>${sumList.total.changeUpdateLostjgNum5.totalCount}</td></c:if> 
						
						<c:if test="${sumList.total.changeUpdateReplaceqyNum.totalCount !=0}"><td>${sumList.total.changeUpdateReplaceqyNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2.totalCount !=0}"><td>${sumList.total.changeUpdateReplaceqyNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum3.totalCount !=0}"><td>${sumList.total.changeUpdateReplaceqyNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4.totalCount !=0}"><td>${sumList.total.changeUpdateReplaceqyNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5.totalCount !=0}"><td>${sumList.total.changeUpdateReplaceqyNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrQNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrQNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum3.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrQNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrQNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrQNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrGNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrGNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum3.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrGNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4.totalCount !=0}"><td>${sumList.totalchangeUpdateReplacegrGNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5.totalCount !=0}"><td>${sumList.total.changeUpdateReplacegrGNum5.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum.totalCount !=0}"><td>${sumList.total.changeUpdateReplacejgNum.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2.totalCount !=0}"><td>${sumList.total.changeUpdateReplacejgNum2.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum3.totalCount !=0}"><td>${sumList.total.changeUpdateReplacejgNum3.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4.totalCount !=0}"><td>${sumList.total.changeUpdateReplacejgNum4.totalCount}</td></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5.totalCount !=0}"><td>${sumList.total.changeUpdateReplacejgNum5.totalCount}</td></c:if> 
						</c:if>
			</tr>
		</table>
		</div>
	</div>
</body>
</html>
