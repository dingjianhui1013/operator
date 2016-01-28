<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书结算统计表管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	var windowH=$(window).height();
	$('.windowHeight').height(windowH);
	$("#scrollBar").scroll(function(){
		var leftWidth=$("#scrollBar").scrollLeft();
		$("#searchForm").css("margin-left",leftWidth);
		$("#ulId").css("margin-left",leftWidth);
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

	function showDays(applyId, productId, startDate, endDate, month, officeId) {
		var url = "${ctx}/statistic/statisticCertData/showDays?applyId="
				+ applyId + "&productId=" + productId + "&startDate="
				+ startDate + "&endDate=" + endDate + "&month=" + month
				+ "&officeId=" + officeId;
		top.$.jBox.open("iframe:" + url, "业务明细", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			iframeScrolling : 'yes',
			submit : function(v, h, f) {
			}
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

		window.location.href = "${ctx }/settle/certificateSettlementStatistics/export?applyId="
				+ applyId
				+ "&areaId="
				+ areaId
				+ "&officeId="
				+ officeId
				+ "&proList="
				+ proList
				+ "&workTypes="
				+ workTypes
				+ "&startDate="
				+ startDate
				+ "&endDate="
				+ endDate
				+ "&multiType=" + multiType;
	}
	/* function dcZS() {

		var form = $("#searchForm");

		form.action = "${ctx }/settle/certificateSettlementStatistics/export";

		form.submit();
	} */
</script>
</head>
<body>
	<div style="overflow: auto;" class="windowHeight" id="scrollBar">
		<ul class="nav nav-tabs" id="ulId" style="width: 100%;">
			<li class="active"><a
				href="${ctx}/settle/certificateSettlementStatistics/">证书结算统计表列表</a></li>
			<shiro:hasPermission
				name="settle:certificateSettlementStatistics:edit">
				<li><a
					href="${ctx}/settle/certificateSettlementStatistics/form">证书结算统计表添加</a></li>
			</shiro:hasPermission>
		</ul>
		<form:form id="searchForm"
			modelAttribute="certificateSettlementStatistics"
			action="${ctx}/settle/certificateSettlementStatistics/" method="post"
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
				</select> <%-- <label>组合业务：</label><input type="checkbox" name="multiType"
					id="multiType" value="true"
					<c:if test="${multiType}">checked="checked"</c:if>> --%>
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
				</select> <label>业务类型：</label>
				<c:forEach items="${workTypes}" var="type">
					<input type="checkbox" name="workTypes" id="workTypes"
						value="${type.id}"
						<c:forEach items="${workType}" var="workType">
						<c:if test="${workType==type.id}">checked="checked"</c:if> 
					</c:forEach>>					
					${type.name}
				</c:forEach>
			</div>
			
			<div style="margin-top: 8px">
				<label>统&nbsp;&nbsp;计&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</label> <input
					id="startTime" name="startDate" type="text" readonly="readonly"
					maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
					value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;
				<input id="endTime" name="endDate" type="text" readonly="readonly"
					maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
					value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" />

				<label>产品名称 ：</label>
				<c:forEach items="${proList}" var="pro">
					<input type="checkbox" name="proList" id="proList"
						value="${pro.id}"
						<c:forEach items="${productId}" var="productId">
						<c:if test="${productId==pro.id}">checked="checked"</c:if> 
					</c:forEach>>					
					${pro.name}
				</c:forEach>
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

		<div class="form-horizontal">

		<table class="table table-striped table-bordered table-condensed" >
			<tr>
				<th colspan="${index+1}" style="text-align: center;">项目:${yingyong}</th>
			</tr>
			<tr>
				<!--新增  -->
				<c:forEach items="${sumList}" var="sum">
					<c:if test="${sum.key=='total'}">
					<c:set var="xz" value="0"/>
					<c:set var="xzqy" value="0"/>
					<c:set var="xzgeqy" value="0"/>
					<c:set var="xzgejg" value="0"/>
					<c:set var="xzjg" value="0"/>
					<c:set var="xzqy1" value="0"/>
					<c:set var="xzqy2" value="0"/>
					<c:set var="xzqy4" value="0"/>
					<c:set var="xzqy5" value="0"/>
					<c:set var="xzgeqy1" value="0"/>
					<c:set var="xzgeqy2" value="0"/>
					<c:set var="xzgeqy4" value="0"/>
					<c:set var="xzgeqy5" value="0"/>
					<c:set var="xzgejg1" value="0"/>
					<c:set var="xzgejg2" value="0"/>
					<c:set var="xzgejg4" value="0"/>
					<c:set var="xzgejg5" value="0"/>
					<c:set var="xzjg1" value="0"/>
					<c:set var="xzjg2" value="0"/>
					<c:set var="xzjg4" value="0"/>
					<c:set var="xzjg5" value="0"/>
						<c:if test="${sum.value.xzqyadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="xzqy1" value="${xzqy1+1}"/>
						</c:if>
						<c:if test="${sum.value.xzqyadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="xzqy2" value="${xzqy2+1}"/>
							
						</c:if>
						<c:if test="${sum.value.xzqyadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="xzqy4" value="${xzqy4+1}"/>
							
						</c:if>
						<c:if test="${sum.value.xzqyadd5!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							<c:set var="xzqy5" value="${xzqy5+1}"/>
							
						</c:if>
						<c:if test="${sum.value.xzgrQadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="xzgeqy1" value="${xzgeqy1+1}"/>
							
						</c:if>
						<c:if test="${sum.value.xzgrQadd2!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="xzgeqy2" value="${xzgeqy2+1}"/>
						</c:if>
						<c:if test="${sum.value.xzgrQadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="xzgeqy4" value="${xzgeqy4+1}"/>
						</c:if>
						<c:if test="${sum.value.xzgrQadd5!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							<c:set var="xzgeqy5" value="${xzgeqy5+1}"/>
						</c:if>
						<c:if test="${sum.value.xzgrGadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="xzgejg1" value="${xzgejg1+1}"/>
						
						</c:if>
						<c:if test="${sum.value.xzgrGadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="xzgejg2" value="${xzgejg2+1}"/>
						</c:if>
						<c:if test="${sum.value.xzgrGadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="xzgejg4" value="${xzgejg4+1}"/>
						</c:if>
						<c:if test="${sum.value.xzgrGadd5!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
							<c:set var="xzgejg5" value="${xzgejg5+1}"/>
						</c:if>
						<c:if test="${sum.value.xzjgadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="xzjg1" value="${xzjg1+1}"/>
						</c:if>
						<c:if test="${sum.value.xzjgadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="xzjg2" value="${xzjg2+1}"/>
						</c:if>
						<c:if test="${sum.value.xzjgadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="xzjg4" value="${xzjg4+1}"/>
						</c:if>
						<c:if test="${sum.value.xzjgadd5!=0}">
						<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
							<c:set var="xzjg5" value="${xzjg5+1}"/>
						</c:if>
						<!-- 更新 -->
						<c:set var="gx" value="0"/>
						<c:set var="gxqy" value="0"/>
					<c:set var="gxgeqy" value="0"/>
					<c:set var="gxgejg" value="0"/>
					<c:set var="gxjg" value="0"/>
					<c:set var="gxqy1" value="0"/>
					<c:set var="gxqy2" value="0"/>
					<c:set var="gxqy4" value="0"/>
					<c:set var="gxqy5" value="0"/>
					<c:set var="gxgeqy1" value="0"/>
					<c:set var="gxgeqy2" value="0"/>
					<c:set var="gxgeqy4" value="0"/>
					<c:set var="gxgeqy5" value="0"/>
					<c:set var="gxgejg1" value="0"/>
					<c:set var="gxgejg2" value="0"/>
					<c:set var="gxgejg4" value="0"/>
					<c:set var="gxgejg5" value="0"/>
					<c:set var="gxjg1" value="0"/>
					<c:set var="gxjg2" value="0"/>
					<c:set var="gxjg4" value="0"/>
					<c:set var="gxjg5" value="0"/>
						<c:if test="${sum.value.gxqyadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="gxqy1" value="${gxqy+1}"/>
							
						</c:if>
						<c:if test="${sum.value.gxqyadd2!=0}">
						
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="gxqy2" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxqyadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="gxqy4" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxqyadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							<c:set var="gxqy5" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrQadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="gxgeqy1" value="${gxgeqy+1}"/>
							
						</c:if>
						<c:if test="${sum.value.gxgrQadd2!=0}">
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="gxgeqy2" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrQadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="gxgeqy4" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrQadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							<c:set var="gxgeqy5" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrGadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="gxgejg1" value="${gxgejg+1}"/>
						
						</c:if>
						<c:if test="${sum.value.gxgrGadd2!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="gxgejg2" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrGadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="gxgejg4" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxgrGadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
							<c:set var="gxgejg5" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxjgadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="gxjg1" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxjgadd2!=0}">
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="gxjg2" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxjgadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="gxjg4" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sum.value.gxjgadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
							<c:set var="gxjg5" value="${gxjg+1}"/>
						</c:if>
						<!--遗失补办  -->
						<c:set var="ysbb" value="0"/>
						<c:set var="ysbbqy" value="0"/>
						<c:set var="ysbbgeqy" value="0"/>
						<c:set var="ysbbgejg" value="0"/>
						<c:set var="ysbbjg" value="0"/>
						<c:if test="${sum.value.lostCerateqy!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbqy" value="${ysbbqy+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCerategrQ!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgeqy" value="${ysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCerategrG!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgejg" value="${ysbbgejg+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCeratejg!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbjg" value="${ysbbjg+1}"/>
						</c:if>
						<!--损坏更换  -->
						<c:set var="shgh" value="0"/>
						<c:set var="shghqy" value="0"/>
						<c:set var="shghgeqy" value="0"/>
						<c:set var="shghgejg" value="0"/>
						<c:set var="shghjg" value="0"/>
						<c:if test="${sum.value.lostCerateqy!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghqy" value="${shghqy+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCerategrQ!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgeqy" value="${shghgeqy+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCerategrG!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgejg" value="${shghgejg+1}"/>
						</c:if>
						<c:if test="${sum.value.lostCeratejg!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghjg" value="${shghjg+1}"/>
						</c:if>
					</c:if>
					</c:forEach>
					
					
				<th rowspan="3">月份</th>
				<th colspan="${xz}">新增</th>
				<th colspan="${gx}">更新</th>
				<th colspan="${ysbb}">遗失补办${ysbb}</th>
				<th colspan="${shgh}">损坏更换${shgh}</th>
				<th colspan="${e}">变更</th>
				<th colspan="${f}">变更+遗失补办</th>
				<th colspan="${g}">变更+损坏更换</th>
				<th colspan="${h}">更新+遗失补办</th>
				<th colspan="${i}">更新+损坏更换</th>
				<th colspan="${j}">更新+变更</th>
				<th colspan="${k}">更新+变更+遗失补办</th>
				<th colspan="${l}">更新+变更+损坏更换</th>
			</tr>
			<tr>
				<th colspan="${xzqy}">企业证书</th>
				<th colspan="${xzgeqy }">个人证书（企业）</th>
				<th colspan="${xzgejg}">个人证书（机构）</th>
				<th colspan="${xzjg}">机构证书</th>
				<th colspan="${gxqy}">企业证书</th>
				<th colspan="${gxgeqy }">个人证书（企业）</th>
				<th colspan="${gxgejg}">个人证书（机构）</th>
				<th colspan="${gxjg}">机构证书</th>
				<th colspan="${ysbbqy}" >企业证书${ysbbqy}</th>
				<th colspan="${ysbbgeqy}">个人证书（企业）${ysbbgeqy}</th>
				<th colspan="${ysbbgejg}" >个人证书（机构）${ysbbgejg}</th>
				<th colspan="${ysbbjg} " >机构证书${ysbbjg}</th>
			</tr>
			<tr>
				<c:forEach items="${sumList}" var="sum">
					<c:if test="${sum.key=='total'}">
						<c:if test="${sum.value.xzqyadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.xzqyadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.xzqyadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.xzqyadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.xzgrQadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.xzgrQadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.xzgrQadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.xzgrQadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.xzgrGadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.xzgrGadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.xzgrGadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.xzgrGadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.xzjgadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.xzjgadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.xzjgadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.xzjgadd5!=0}"><th>五年</th></c:if>
						
						<c:if test="${sum.value.gxqyadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.gxqyadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.gxqyadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.gxqyadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.gxgrQadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.gxgrQadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.gxgrQadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.gxgrQadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.gxgrGadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.gxgrGadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.gxgrGadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.gxgrGadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sum.value.gxjgadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sum.value.gxjgadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sum.value.gxjgadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sum.value.gxjgadd5!=0}"><th>五年</th></c:if>
					</c:if>
				</c:forEach>
	 		</tr>
				
					<%-- 	<td><c:if test="${sum.key!='total'}">${sum.key}</c:if></td>
						<c:if test="${sum.key=='total'&&sum.value.xzqyadd1!=0}">
							<td>1
								<c:if test="${sum.key!='total'}">${sum.value.xzqyadd1}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzqyadd2!=0}">
							<td>2
								<c:if test="${sum.key!='total'}">${sum.value.xzqyadd2}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzqyadd4!=0}">
							<td>4
								<c:if test="${sum.key!='total'}">${sum.value.xzqyadd4}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzqyadd5!=0}">
							<td>5
								<c:if test="${sum.key!='total'}">${sum.value.xzqyadd5}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzgrQadd1!=0}">
							<td>1
								<c:if test="${sum.key!='total'}">${sum.value.xzgrQadd1}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzgrQadd2!=0}">
							<td>2
								<c:if test="${sum.key!='total'}">${sum.value.xzgrQadd2}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzgrQadd4!=0}">
							<td>
								<c:if test="${sum.key!='total'}">${sum.value.xzgrQadd4}</c:if>
							</td>
						</c:if>
						<c:if test="${sum.key=='total'&&sum.value.xzgrQadd5!=0}">
							<td>
								<c:if test="${sum.key!='total'}">${sum.value.xzgrQadd5}</c:if>
							</td>
						</c:if> --%>
						
							<c:forEach items="${sumList}" var ="sum">
						<tr>
						<c:if test="${sum.key!='total'}">
						<td><c:if test="${sum.key!='total'}">${sum.key}</c:if></td>
								
						<c:if test="${xzqy1!=0}"><th><c:if test="${sum.value.xzqyadd1==null}">0</c:if>${sum.value.xzqyadd1}</th></c:if>
						<c:if test="${xzqy2!=0}"><th><c:if test="${sum.value.xzqyadd2==null}">0</c:if>${sum.value.xzqyadd2}</th></c:if>
						<c:if test="${xzqy4!=0}"><th><c:if test="${sum.value.xzqyadd4==null}">0</c:if>${sum.value.xzqyadd4}</th></c:if>
						<c:if test="${xzqy5!=0}"><th><c:if test="${sum.value.xzqyadd5==null}">0</c:if>${sum.value.xzqyadd5}</th></c:if>
						<c:if test="${xzgeqy1!=0}"><th><c:if test="${sum.value.xzgrQadd1==null}">0</c:if>${sum.value.xzgrQadd1}</th></c:if>
						<c:if test="${xzgeqy2!=0}"><th><c:if test="${sum.value.xzgrQadd2==null}">0</c:if>${sum.value.xzgrQadd2}</th></c:if>
						<c:if test="${xzgeqy4!=0}"><th><c:if test="${sum.value.xzgrQadd4==null}">0</c:if>${sum.value.xzgrQadd4}</th></c:if>
						<c:if test="${xzgeqy5!=0}"><th><c:if test="${sum.value.xzgrQadd5==null}">0</c:if>${sum.value.xzgrQadd5}</th></c:if>
						<c:if test="${xzgejg1!=0}"><th><c:if test="${sum.value.xzgrGadd1==null}">0</c:if>${sum.value.xzgrGadd1}</th></c:if>
						<c:if test="${xzgejg2!=0}"><th><c:if test="${sum.value.xzgrGadd2==null}">0</c:if>${sum.value.xzgrGadd2}</th></c:if>
						<c:if test="${xzgejg4!=0}"><th><c:if test="${sum.value.xzgrGadd4==null}">0</c:if>${sum.value.xzgrGadd4}</th></c:if>
						<c:if test="${xzgejg5!=0}"><th><c:if test="${sum.value.xzgrGadd5==null}">0</c:if>${sum.value.xzgrGadd5}</th></c:if>
						<c:if test="${xzjg1!=0}"><th><c:if test="${sum.value.xzjgadd1==null}">0</c:if>${sum.value.xzjgadd1}</th></c:if>
						<c:if test="${xzjg2!=0}"><th><c:if test="${sum.value.xzjgadd2==null}">0</c:if>${sum.value.xzjgadd2}</th></c:if>
						<c:if test="${xzjg4!=0}"><th><c:if test="${sum.value.xzjgadd4==null}">0</c:if>${sum.value.xzjgadd4}</th></c:if>
						<c:if test="${xzjg5!=0}"><th><c:if test="${sum.value.xzjgadd5==null}">0</c:if>${sum.value.xzjgadd5}</th></c:if>
						
						
						<c:if test="${gxqy1!=0}"><th><c:if test="${sum.value.gxqyadd1==null}">0</c:if>${sum.value.gxqyadd1}</th></c:if>
						<c:if test="${gxqy!=0}"><th><c:if test="${sum.value.gxqyadd2==null}">0</c:if>${sum.value.gxqyadd2}</th></c:if>
						<c:if test="${gxqy4!=0}"><th><c:if test="${sum.value.gxqyadd4==null}">0</c:if>${sum.value.gxqyadd4}</th></c:if>
						<c:if test="${gxqy5!=0}"><th><c:if test="${sum.value.gxqyadd5==null}">0</c:if>${sum.value.gxqyadd5}</th></c:if>
						<c:if test="${gxgeqy1!=0}"><th><c:if test="${sum.value.gxgrQadd1==null}">0</c:if>${sum.value.gxgrQadd1}</th></c:if>
						<c:if test="${gxgeqy2!=0}"><th><c:if test="${sum.value.gxgrQadd2==null}">0</c:if>${sum.value.gxgrQadd2}</th></c:if>
						<c:if test="${gxgeqy4!=0}"><th><c:if test="${sum.value.gxgrQadd4==null}">0</c:if>${sum.value.gxgrQadd4}</th></c:if>
						<c:if test="${gxgeqy5!=0}"><th><c:if test="${sum.value.gxgrQadd5==null}">0</c:if>${sum.value.gxgrQadd5}</th></c:if>
						<c:if test="${gxgejg1!=0}"><th><c:if test="${sum.value.gxgrGadd1==null}">0</c:if>${sum.value.gxgrGadd1}</th></c:if>
						<c:if test="${gxgejg2!=0}"><th><c:if test="${sum.value.gxgrGadd2==null}">0</c:if>${sum.value.gxgrGadd2}</th></c:if>
						<c:if test="${gxgejg4!=0}"><th><c:if test="${sum.value.gxgrGadd4==null}">0</c:if>${sum.value.gxgrGadd4}</th></c:if>
						<c:if test="${gxgejg5!=0}"><th><c:if test="${sum.value.gxgrGadd5==null}">0</c:if>${sum.value.gxgrGadd5}</th></c:if>
						<c:if test="${gxjg1!=0}"><th><c:if test="${sum.value.gxjgadd1==null}">0</c:if>${sum.value.gxjgadd1}</th></c:if>
						<c:if test="${gxjg2!=0}"><th><c:if test="${sum.value.gxjgadd2==null}">0</c:if>${sum.value.gxjgadd2}</th></c:if>
						<c:if test="${gxjg4!=0}"><th><c:if test="${sum.value.gxjgadd4==null}">0</c:if>${sum.value.gxjgadd4}</th></c:if>
						<c:if test="${gxjg5!=0}"><th><c:if test="${sum.value.gxjgadd5==null}">0</c:if>${sum.value.gxjgadd5}</th></c:if>
						
						<c:if test="${sum.value.lostCerateqy!=0}"><th><c:if test="${sum.value.lostCerateqy==null}">0</c:if>${sum.value.lostCerateqy}</th></c:if>
						<c:if test="${sum.value.lostCerategrQ!=0}"><th><c:if test="${sum.value.lostCerategrQ==null}">0</c:if>${sum.value.lostCerategrQ}</th></c:if>
						<c:if test="${sum.value.lostCerategrG!=0}"><th><c:if test="${sum.value.lostCerategrG==null}">0</c:if>${sum.value.lostCerategrG}</th></c:if>
						<c:if test="${sum.value.lostCeratejg!=0}"><th><c:if test="${sum.value.lostCeratejg==null}">0</c:if>${sum.value.lostCeratejg}</th></c:if>
						</c:if>
					</tr>
							</c:forEach>
							
						
			
			<tr> 
				<c:forEach items="${sumList}" var="sum">
					<c:if test="${sum.key=='total'}">
						<th>总计</th>
						<c:if test="${sum.value.xzqyadd1!=0}"><th>${sum.value.xzqyadd1}</th></c:if>
						<c:if test="${sum.value.xzqyadd2!=0}"><th>${sum.value.xzqyadd2}</th></c:if>
						<c:if test="${sum.value.xzqyadd4!=0}"><th>${sum.value.xzqyadd4}</th></c:if>
						<c:if test="${sum.value.xzqyadd5!=0}"><th>${sum.value.xzqyadd5}</th></c:if>
						<c:if test="${sum.value.xzgrQadd1!=0}"><th>${sum.value.xzgrQadd1}</th></c:if>
						<c:if test="${sum.value.xzgrQadd2!=0}"><th>${sum.value.xzgrQadd2}</th></c:if>
						<c:if test="${sum.value.xzgrQadd4!=0}"><th>${sum.value.xzgrQadd4}</th></c:if>
						<c:if test="${sum.value.xzgrQadd5!=0}"><th>${sum.value.xzgrQadd5}</th></c:if>
						<c:if test="${sum.value.xzgrGadd1!=0}"><th>${sum.value.xzgrGadd1}</th></c:if>
						<c:if test="${sum.value.xzgrGadd2!=0}"><th>${sum.value.xzgrGadd2}</th></c:if>
						<c:if test="${sum.value.xzgrGadd4!=0}"><th>${sum.value.xzgrGadd4}</th></c:if>
						<c:if test="${sum.value.xzgrGadd5!=0}"><th>${sum.value.xzgrGadd5}</th></c:if>
						<c:if test="${sum.value.xzjgadd1!=0}"><th>${sum.value.xzjgadd1}</th></c:if>
						<c:if test="${sum.value.xzjgadd2!=0}"><th>${sum.value.xzjgadd2}</th></c:if>
						<c:if test="${sum.value.xzjgadd4!=0}"><th>${sum.value.xzjgadd4}</th></c:if>
						<c:if test="${sum.value.xzjgadd5!=0}"><th>${sum.value.xzjgadd5}</th></c:if>
						
						<c:if test="${sum.value.gxqyadd1!=0}"><th>${sum.value.gxqyadd1}</th></c:if>
						<c:if test="${sum.value.gxqyadd2!=0}"><th>${sum.value.gxqyadd2}</th></c:if>
						<c:if test="${sum.value.gxqyadd4!=0}"><th>${sum.value.gxqyadd4}</th></c:if>
						<c:if test="${sum.value.gxqyadd5!=0}"><th>${sum.value.gxqyadd5}</th></c:if>
						<c:if test="${sum.value.gxgrQadd1!=0}"><th>${sum.value.gxgrQadd1}</th></c:if>
						<c:if test="${sum.value.gxgrQadd2!=0}"><th>${sum.value.gxgrQadd2}</th></c:if>
						<c:if test="${sum.value.gxgrQadd4!=0}"><th>${sum.value.gxgrQadd4}</th></c:if>
						<c:if test="${sum.value.gxgrQadd5!=0}"><th>${sum.value.gxgrQadd5}</th></c:if>
						<c:if test="${sum.value.gxgrGadd1!=0}"><th>${sum.value.gxgrGadd1}</th></c:if>
						<c:if test="${sum.value.gxgrGadd2!=0}"><th>${sum.value.gxgrGadd2}</th></c:if>
						<c:if test="${sum.value.gxgrGadd4!=0}"><th>${sum.value.gxgrGadd4}</th></c:if>
						<c:if test="${sum.value.gxgrGadd5!=0}"><th>${sum.value.gxgrGadd5}</th></c:if>
						<c:if test="${sum.value.gxjgadd1!=0}"><th>${sum.value.gxjgadd1}</th></c:if>
						<c:if test="${sum.value.gxjgadd2!=0}"><th>${sum.value.gxjgadd2}</th></c:if>
						<c:if test="${sum.value.gxjgadd4!=0}"><th>${sum.value.gxjgadd4}</th></c:if>
						<c:if test="${sum.value.gxjgadd5!=0}"><th>${sum.value.gxjgadd5}</th></c:if>
						<c:if test="${sum.value.lostCerateqy!=0}"><th>${sum.value.lostCerateqy}</th></c:if>
						<c:if test="${sum.value.lostCerategrQ!=0}"><th>${sum.value.lostCerategrQ}</th></c:if>
						<c:if test="${sum.value.lostCerategrG!=0}"><th>${sum.value.lostCerategrG}</th></c:if>
						<c:if test="${sum.value.lostCeratejg!=0}"><th>${sum.value.lostCeratejg}</th></c:if>
					</c:if>
				</c:forEach>
			</tr>
			<%-- <tr>
			<th rowspan="3" style="text-align:center;">月份</th>
			<c:forEach items="${deal_pro }" var="deal_pro">'
				<c:set var="count" value="0"/>
				<c:forEach items="${deal_pro.value}" var="deal_proV">
					<c:set var="count" value="${count+1}"/>
				</c:forEach>
				<c:if test="${count>0 }">
					<c:set var="i" value="0"/>
					<c:forEach items="${dealInfoType_Year }" var="dealInfoType_Year">
						<c:if test="${dealInfoType_Year.deal==deal_pro.key}">
							<c:forEach items="${dealInfoType_Year.year}" var="years">
								<c:set var="i" value="${i+1}"/>
							</c:forEach>
						</c:if>
					</c:forEach>
					<th colspan="${i}">
						<c:if test="${deal_pro.key==0}">
							新增
						</c:if>
						<c:if test="${deal_pro.key==100}">
							更新
						</c:if>
						<c:if test="${deal_pro.key==3}">
							损坏更换
						</c:if>
						<c:if test="${deal_pro.key==2}">
							遗失补办
						</c:if>
						<c:if test="${deal_pro.key==4}">
							变更
						</c:if>
						<c:if test="${deal_pro.key==024}">
							变更+遗失补办
						</c:if>
						<c:if test="${deal_pro.key==034}">
							变更+损坏更换
						</c:if>
						<c:if test="${deal_pro.key==120}">
							更新+遗失补办
						</c:if>
						<c:if test="${deal_pro.key==130}">
							更新+损坏更换
						</c:if>
						<c:if test="${deal_pro.key==104}">
							更新+变更
						</c:if>
						<c:if test="${deal_pro.key==124}">
							更新+损坏更换+变更
						</c:if>
						<c:if test="${deal_pro.key==134}">
							更新+损坏更换+变更
						</c:if>
						
					</th>
				</c:if>
			</c:forEach>				
			</tr>
			<tr>
				<c:forEach items="${deal_pro}" var="deal_pro">
					<c:forEach items="${dealInfoType_Year }" var="dealInfoType_Year">
						<c:if test="${deal_pro.key==dealInfoType_Year.deal}">
							<c:set var="ii" value="0"/>
							<c:forEach items="${dealInfoType_Year.year}" var="dealInfoType_YearY">
								<c:set var="ii" value="${ii+1 }"/>
							</c:forEach>
							<c:if test="${deal_pro.key==034}">
								
								<th colspan="${ii}" rowspan="2">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
								</th>
							</c:if>
							<c:if test="${deal_pro.key==024}">
								
								<th colspan="${ii}" rowspan="2">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
								</th>
							</c:if>
							<c:if test="${deal_pro.key==2}">
								
								<th colspan="${ii}" rowspan="2">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
								</th>
							</c:if>
							<c:if test="${deal_pro.key==3}">
								
								<th colspan="${ii}" rowspan="2">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
								</th>
							</c:if>
							<c:if test="${deal_pro.key==4}">
								
								<th colspan="${ii}" rowspan="2">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
								</th>
							</c:if>
							<th colspan="${ii}">
									<c:if test="${dealInfoType_Year.producType==1}">
										企业证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==2}">
										个人证书（企业）
									</c:if>
									<c:if test="${dealInfoType_Year.producType==3}">
										机构证书
									</c:if>
									<c:if test="${dealInfoType_Year.producType==6}">
										个人证书（机构）
									</c:if>
									
							</th>
						</c:if>
					</c:forEach>
				</c:forEach>
			</tr>
			<tr>
				<c:forEach items="${deal_pro}" var="deal_pro">
					<c:forEach items="${dealInfoType_Year }" var="dealInfoType_Year">
						<c:if test="${deal_pro.key==dealInfoType_Year.deal}">
							<c:forEach items="${dealInfoType_Year.year}" var="dealInfoType_YearY">
								<c:if test="${deal_pro.key==034}"></c:if>
								<th>${dealInfoType_YearY}年</th>
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:forEach>
			</tr>
			<c:forEach items="${month}" var="month">
=======
			<table class="table table-striped table-bordered table-condensed" >
				<thead>
					<tr>
						<th colspan="17" style="text-align: center;">项目:${yingyong}</th>
					</tr>
					<%-- <tr>
					<th rowspan="2">月份</th>
					
					<th colspan=4>新增（企业）</th>
					<th colspan=4>新增（个人）</th>
					<th colspan=4>更新（企业）</th>
					<th colspan=4>更新（个人）</th>
					<c:if test="${multiType==false}">
						<th rowspan="2">遗失补办</th>
						<th rowspan="2">损坏更换</th>
						<th rowspan="2">信息变更</th>
					</c:if>
					<c:if test="${multiType}">
						<th >更新+遗失补办</th>
						<th >更新+损坏更换</th>
						<th >更新+更换/补办+信息变更</th>
					</c:if>
				</tr>
>>>>>>> .r21488
				<tr>
				<c:if test="${multiType}">
					<c:forEach var="i" begin="1" end="7">
						<th>一年</th>
						<th>二年</th>
						<th>四年</th>
						<th>五年</th>
					</c:forEach >
				</c:if>
					<c:forEach var="i" begin="1" end="4">
						<th>一年</th>
						<th>二年</th>
						<th>四年</th>
						<th>五年</th>
					</c:forEach>
				</tr>
<<<<<<< .mine
			</c:forEach> --%>
		</table>

				
				 	
	<%-- 				<tr>

						<th rowspan="3">月份</th>
						<th colspan="16" style="text-align: center">新增</th>
						<th colspan="16" style="text-align: center">更新</th>
						<th colspan="4" style="text-align: center">遗失补办</th>
						<th colspan="4" style="text-align: center">损坏更换</th>
						<th colspan="4" style="text-align: center">信息变更</th>
						<th colspan="16" style="text-align: center">更新+变更</th>
						<th colspan="16" style="text-align: center">更新+遗失补办</th>
						<th colspan="16" style="text-align: center">更新+损坏更换</th>
						<th colspan="4" style="text-align: center"">变更+遗失补办</th>
						<th colspan="4" style="text-align: center">变更+损坏更换</th>
						<th colspan="16" style="text-align: center">更新+变更+遗失补办</th>
						<th colspan="16" style="text-align: center">更新+变更+损坏更换</th>
					</tr>
						<tr>
						<c:forEach var="i" begin="1" end="2">
							<th colspan="4">企业证书</th>
							<th colspan="4">个人证书（企业）</th>
							<th colspan="4">个人证书（机构）</th>
							<th colspan="4">机构证书</th>
						</c:forEach>
						<c:forEach var="i" begin="3" end="5">
							<th  rowspan="2">企业证书</th>
							<th  rowspan="2">个人证书（企业）</th>
							<th  rowspan="2">个人证书（机构）</th>
							<th  rowspan="2">机构证书</th>
						</c:forEach>
						<c:forEach var="i" begin="6" end="8">
							<th colspan="4">企业证书</th>
							<th colspan="4">个人证书（企业）</th>
							<th colspan="4">个人证书（机构）</th>
							<th colspan="4">机构证书</th>
						</c:forEach>
						<c:forEach var="i" begin="9" end="10">
							<th  rowspan="2">企业证书</th>
							<th  rowspan="2">个人证书（企业）</th>
							<th  rowspan="2">个人证书（机构）</th>
							<th  rowspan="2">机构证书</th>
						</c:forEach>
						<c:forEach var="i" begin="11" end="12">
							<th colspan="4">企业证书</th>
							<th colspan="4">个人证书（企业）</th>
							<th colspan="4">个人证书（机构）</th>
							<th colspan="4">机构证书</th>
						</c:forEach>
					</tr> 
					
						<c:forEach var="i" begin="1" end="28">
							<th >一年</th>
							<th >二年</th>
							<th >四年</th>
							<th >五年</th>
						</c:forEach>
						
					</tr> 
					
		
				</thead>
				

		 		<c:forEach items="${sumList}" var="sum">
					<tr>
						<td>${sum.key }</td>
						<td>${sum.value.xzqyadd1 }</td>
						<td>${sum.value.xzqyadd2 }</td>
						<td>${sum.value.xzqyadd4 }</td>
						<td>${sum.value.xzqyadd5 }</td>
						<td>${sum.value.xzgrQadd1 }</td>
						<td>${sum.value.xzgrQadd2 }</td>
						<td>${sum.value.xzgrQadd4 }</td>
						<td>${sum.value.xzgrQadd5 }</td>
						<td>${sum.value.xzgrGadd1 }</td>
						<td>${sum.value.xzgrGadd2 }</td>
						<td>${sum.value.xzgrGadd4 }</td>
						<td>${sum.value.xzgrGadd5 }</td>
						<td>${sum.value.xzjgadd1 }</td>
						<td>${sum.value.xzjgadd2 }</td>
						<td>${sum.value.xzjgadd4 }</td>
						<td>${sum.value.xzjgadd5 }</td>
						<td>${sum.value.gxqyadd1}</td>
						<td>${sum.value.gxqyadd2}</td>
						<td>${sum.value.gxqyadd4}</td>
						<td>${sum.value.gxqyadd5}</td>
						<td>${sum.value.gxgrQadd1}</td>
						<td>${sum.value.gxgrQadd2}</td>
						<td>${sum.value.gxgrQadd4}</td>
						<td>${sum.value.gxgrQadd5}</td>
						<td>${sum.value.gxgrGadd1}</td>
						<td>${sum.value.gxgrGadd2}</td>
						<td>${sum.value.gxgrGadd4}</td>
						<td>${sum.value.gxgrGadd5}</td>
						<td>${sum.value.gxjgadd1}</td>
						<td>${sum.value.gxjgadd2}</td>
						<td>${sum.value.gxjgadd4}</td>
						<td>${sum.value.gxjgadd5}</td>
						<td>${sum.value.lostCerateqy}</td>
						<td>${sum.value.lostCerategrQ}</td>
						<td>${sum.value.lostCerategrG}</td>
						<td>${sum.value.lostCeratejg}</td>
						<td>${sum.value.damageCertificateqy}</td>
						<td>${sum.value.damageCertificategrQ}</td>
						<td>${sum.value.damageCertificategrG}</td>
						<td>${sum.value.damageCertificatejg}</td>
						<td>${sum.value.modifyNumqy}</td>
						<td>${sum.value.modifyNumgrQ}</td>
						<td>${sum.value.modifyNumgrG}</td>
						<td>${sum.value.modifyNumjg}</td>
						<td>${sum.value.updateChangeqyNum}</td>
						<td>${sum.value.updateChangeqyNum2}</td>
						<td>${sum.value.updateChangeqyNum4}</td>
						<td>${sum.value.updateChangeqyNum5}</td>
						<td>${sum.value.updateChangegrQNum}</td>
						<td>${sum.value.updateChangegrQNum2}</td>
						<td>${sum.value.updateChangegrQNum4}</td>
						<td>${sum.value.updateChangegrQNum5}</td>
						<td>${sum.value.updateChangegrGNum}</td>
						<td>${sum.value.updateChangegrGNum2}</td>
						<td>${sum.value.updateChangegrGNum4}</td>
						<td>${sum.value.updateChangegrGNum5}</td>
						<td>${sum.value.updateChangejgNum}</td>
						<td>${sum.value.updateChangejgNum2}</td>
						<td>${sum.value.updateChangejgNum4}</td>
						<td>${sum.value.updateChangejgNum5}</td>
						<td>${sum.value.updateLostqyNum}</td>
						<td>${sum.value.updateLostqyNum2}</td>
						<td>${sum.value.updateLostqyNum4}</td>
						<td>${sum.value.updateLostqyNum5}</td>
						<td>${sum.value.updateLostgrQNum}</td>
						<td>${sum.value.updateLostgrQNum2}</td>
						<td>${sum.value.updateLostgrQNum4}</td>
						<td>${sum.value.updateLostgrQNum5}</td>
						<td>${sum.value.updateLostgrGNum}</td>
						<td>${sum.value.updateLostgrGNum2}</td>
						<td>${sum.value.updateLostgrGNum4}</td>
						<td>${sum.value.updateLostgrGNum5}</td>
						<td>${sum.value.updateLostjgNum}</td>
						<td>${sum.value.updateLostjgNum2}</td>
						<td>${sum.value.updateLostjgNum4}</td>
						<td>${sum.value.updateLostjgNum5}</td>
						<td>${sum.value.updateReplaceqyNum}</td>
						<td>${sum.value.updateReplaceqyNum2}</td>
						<td>${sum.value.updateReplaceqyNum4}</td>
						<td>${sum.value.updateReplaceqyNum5}</td>
						<td>${sum.value.updateReplacegrQNum}</td>
						<td>${sum.value.updateReplacegrQNum2}</td>
						<td>${sum.value.updateReplacegrQNum4}</td>
						<td>${sum.value.updateReplacegrQNum5}</td>
						<td>${sum.value.updateReplacegrGNum}</td>
						<td>${sum.value.updateReplacegrGNum2}</td>
						<td>${sum.value.updateReplacegrGNum4}</td>
						<td>${sum.value.updateReplacegrGNum5}</td>
						<td>${sum.value.updateReplacejgNum}</td>
						<td>${sum.value.updateReplacejgNum2}</td>
						<td>${sum.value.updateReplacejgNum4}</td>
						<td>${sum.value.updateReplacejgNum5}</td>
						<td>${sum.value.changeLostqyNum}</td>
						<td>${sum.value.changeLostgrQNum}</td>
						<td>${sum.value.changeLostgrGNum}</td>
						<td>${sum.value.changeLostjgNum}</td>
						<td>${sum.value.changeReplaceqyNum}</td>
						<td>${sum.value.changeReplacegrQNum}</td>
						<td>${sum.value.changeReplacegrGNum}</td>
						<td>${sum.value.changeReplacejgNum}</td>
						<td>${sum.value.changeUpdateLostqyNum}</td>
						<td>${sum.value.changeUpdateLostqyNum2}</td>
						<td>${sum.value.changeUpdateLostqyNum4}</td>
						<td>${sum.value.changeUpdateLostqyNum5}</td>
						<td>${sum.value.changeUpdateLostgrQNum}</td>
						<td>${sum.value.changeUpdateLostgrQNum2}</td>
						<td>${sum.value.changeUpdateLostgrQNum4}</td>
						<td>${sum.value.changeUpdateLostgrQNum5}</td>
						<td>${sum.value.changeUpdateLostgrGNum}</td>
						<td>${sum.value.changeUpdateLostgrGNum2}</td>
						<td>${sum.value.changeUpdateLostgrGNum4}</td>
						<td>${sum.value.changeUpdateLostgrGNum5}</td>
						<td>${sum.value.changeUpdateLostjgNum}</td>
						<td>${sum.value.changeUpdateLostjgNum2}</td>
						<td>${sum.value.changeUpdateLostjgNum4}</td>
						<td>${sum.value.changeUpdateLostjgNum5}</td>
						<td>${sum.value.changeUpdateReplaceqyNum}</td>
						<td>${sum.value.changeUpdateReplaceqyNum2}</td>
						<td>${sum.value.changeUpdateReplaceqyNum4}</td>
						<td>${sum.value.changeUpdateReplaceqyNum5}</td>
						<td>${sum.value.changeUpdateReplacegrQNum}</td>
						<td>${sum.value.changeUpdateReplacegrQNum2}</td>
						<td>${sum.value.changeUpdateReplacegrQNum4}</td>
						<td>${sum.value.changeUpdateReplacegrQNum5}</td>
						<td>${sum.value.changeUpdateReplacegrGNum}</td>
						<td>${sum.value.changeUpdateReplacegrGNum2}</td>
						<td>${sum.value.changeUpdateReplacegrGNum4}</td>
						<td>${sum.value.changeUpdateReplacegrGNum5}</td>
						<td>${sum.value.changeUpdateReplacejgNum}</td>
						<td>${sum.value.changeUpdateReplacejgNum2}</td>
						<td>${sum.value.changeUpdateReplacejgNum4}</td>
						<td>${sum.value.changeUpdateReplacejgNum5}</td>
						
						
						
						
						<td>${sum.value.oneAdd1 }</td>
						<c:set var="oneA1" value="${oneA1+sum.value.oneAdd1}"></c:set>
						<td>${sum.value.oneAdd2 }</td>
						<c:set var="oneA2" value="${oneA2+sum.value.oneAdd2}"></c:set>
						<td>${sum.value.oneAdd4 }</td>
						<c:set var="oneA4" value="${oneA4+sum.value.oneAdd4}"></c:set>
						<td>${sum.value.oneAdd5 }</td>
						<c:set var="oneA5" value="${oneA5+sum.value.oneAdd5}"></c:set>
						<td>${twoA1+sum.value.twoAdd1 + sum.value.fourAdd1 }</td>
						<c:set var="twoA1"
							value="${twoA1+sum.value.twoAdd1 + sum.value.fourAdd1 }"></c:set>
						<td>${sum.value.twoAdd2 + sum.value.fourAdd2  }</td>
						<c:set var="twoA2"
							value="${twoA2+sum.value.twoAdd2 + sum.value.fourAdd2 }"></c:set>
						<td>${sum.value.twoAdd4 + sum.value.fourAdd4 }</td>
						<c:set var="twoA4"
							value="${twoA4+sum.value.twoAdd4 + sum.value.fourAdd4}"></c:set>
						<td>${sum.value.twoAdd5 + sum.value.fourAdd5 }</td>
						<c:set var="twoA5"
							value="${twoA5+sum.value.twoAdd5 + sum.value.fourAdd5}"></c:set>

						<td>${sum.value.oneRenew1}</td>
						<c:set var="oneR1" value="${oneR1+sum.value.oneRenew1}"></c:set>
						<td>${sum.value.oneRenew2}</td>
						<c:set var="oneR2" value="${oneR2+sum.value.oneRenew2}"></c:set>
						<td>${sum.value.oneRenew4}</td>
						<c:set var="oneR4" value="${oneR4+sum.value.oneRenew4}"></c:set>
						<td>${sum.value.oneRenew5}</td>
						<c:set var="oneR5" value="${oneR5+sum.value.oneRenew5}"></c:set>

						<td>${sum.value.twoRenew1 +sum.value.fourRenew1 }</td>
						<c:set var="twoR1"
							value="${twoR1+sum.value.twoRenew1 +sum.value.fourRenew1 }"></c:set>
						<td>${sum.value.twoRenew2 +sum.value.fourRenew2  }</td>
						<c:set var="twoR2"
							value="${twoR2+sum.value.twoRenew2 +sum.value.fourRenew2 }"></c:set>
						<td>${sum.value.twoRenew4 +sum.value.fourRenew4  }</td>
						<c:set var="twoR4"
							value="${twoR4+sum.value.twoRenew4 +sum.value.fourRenew4 }"></c:set>
						<td>${sum.value.twoRenew5 +sum.value.fourRenew5  }</td>
						<c:set var="twoR5"
							value="${twoR5+sum.value.twoRenew5 +sum.value.fourRenew5}"></c:set>
						<td>${sum.value.replacementLosted}</td>
						<c:set var="replacementLosted"
							value="${replacementLosted+sum.value.replacementLosted}"></c:set>
						<td>${sum.value.replacementDamaged}</td>
						<c:set var="replacementDamaged"
							value="${replacementDamaged+sum.value.replacementDamaged}"></c:set>
						<td>${sum.value.alterInfomation}</td>
						<c:set var="alterInfomation"
							value="${alterInfomation+sum.value.alterInfomation}"></c:set>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>
						<td>${sum.value.alterInfomation}</td>


					</tr>
				</c:forEach> 

				 <tr>
					<td>总计</td>
					<td>${oneA1}</td>
					<td>${oneA2}</td>
					<td>${oneA4}</td>
					<td>${oneA5}</td>
					<td>${twoA1}</td>
					<td>${twoA2}</td>
					<td>${twoA4}</td>
					<td>${twoA5}</td>
					<td>${oneR1}</td>
					<td>${oneR2}</td>
					<td>${oneR4}</td>
					<td>${oneR5}</td>
					<td>${twoR1}</td>
					<td>${twoR2}</td>
					<td>${twoR4}</td>
					<td>${twoR5}</td>
					<td>${replacementLosted}</td>
					<td>${replacementDamaged}</td>
					<td>${alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>
					<td>${sum.value.alterInfomation}</td>


		

				</tr> 
			</table>
>>>>>>> .r21488 --%>

		</div>
	</div>
</body>
</html>
