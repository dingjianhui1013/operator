<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>证书结算统计表管理</title>
<meta name="decorator" content="default" />
<style type="text/css">
	.table th,.table td{
		text-align: center;
		}
</style>
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
				</select>  <label>组合业务：</label><input type="checkbox" name="multiType"
					id="multiType" value="true"
					<c:if test="${multiType}">checked="checked"</c:if>>
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
					
							
					<c:set var="xz" value="0"/>
					<c:set var="xzqy" value="0"/>
					<c:set var="xzgeqy" value="0"/>
					<c:set var="xzgejg" value="0"/>
					<c:set var="xzjg" value="0"/>
						<c:if test="${sumList.total.xzqyadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzqyadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.xzqyadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.xzqyadd5!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzqy" value="${xzqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.xzgrQadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.xzgrQadd2!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrQadd5!=0}">
								<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgeqy" value="${xzgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.xzgrGadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzgrGadd5!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzgejg" value="${xzgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd1!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd2!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd4!=0}">
							<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.xzjgadd5!=0}">
						<c:set var="xz" value="${xz+1}"/>
							<c:set var="xzjg" value="${xzjg+1}"/>
						</c:if>
						<!-- 更新 -->
						<c:set var="gx" value="0"/>
						<c:set var="gxqy" value="0"/>
					<c:set var="gxgeqy" value="0"/>
					<c:set var="gxgejg" value="0"/>
					<c:set var="gxjg" value="0"/>
					
						<c:if test="${sumList.total.gxqyadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.gxqyadd2!=0}">
						
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxqyadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxqy" value="${gxqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.gxgrQadd2!=0}">
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrQadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgeqy" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.gxgrGadd2!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxgrGadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxgejg" value="${gxgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd1!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd2!=0}">
						<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd4!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.gxjgadd5!=0}">
							<c:set var="gx" value="${gx+1}"/>
							<c:set var="gxjg" value="${gxjg+1}"/>
						</c:if>
						<!--遗失补办  -->
						<c:set var="ysbb" value="0"/>
						<c:set var="ysbbqy" value="0"/>
						<c:set var="ysbbgeqy" value="0"/>
						<c:set var="ysbbgejg" value="0"/>
						<c:set var="ysbbjg" value="0"/>
						<c:if test="${sumList.total.lostCerateqy!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbqy" value="${ysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCerategrQ!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgeqy" value="${ysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCerategrG!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbgejg" value="${ysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.lostCeratejg!=0}">
							<c:set var="ysbb" value="${ysbb+1}"/>
							<c:set var="ysbbjg" value="${ysbbjg+1}"/>
						</c:if>
						<!--损坏更换  -->
						<c:set var="shgh" value="0"/>
						<c:set var="shghqy" value="0"/>
						<c:set var="shghgeqy" value="0"/>
						<c:set var="shghgejg" value="0"/>
						<c:set var="shghjg" value="0"/>
						<c:if test="${sumList.total.damageCertificateqy!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghqy" value="${shghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificategrQ!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgeqy" value="${shghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificategrG!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghgejg" value="${shghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.damageCertificatejg!=0}">
							<c:set var="shgh" value="${shgh+1}"/>
							<c:set var="shghjg" value="${shghjg+1}"/>
						</c:if>
						
						<!--变更  -->
						<c:set var="bg" value="0"/>
						<c:set var="bgqy" value="0"/>
						<c:set var="bggeqy" value="0"/>
						<c:set var="bggejg" value="0"/>
						<c:set var="bgjg" value="0"/>
						<c:if test="${sumList.total.modifyNumqy!=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bgqy" value="${bgqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumgrQ!=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bggeqy" value="${bggeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumgrG!=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bggejg" value="${bggejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.modifyNumjg!=0}">
							<c:set var="bg" value="${bg+1}"/>
							<c:set var="bgjg" value="${bgjg+1}"/>
						</c:if>
					
					<!--变更+遗失补办  -->
						<c:set var="bgysbb" value="0"/>
						<c:set var="bgysbbqy" value="0"/>
						<c:set var="bgysbbgeqy" value="0"/>
						<c:set var="bgysbbgejg" value="0"/>
						<c:set var="bgysbbjg" value="0"/>
						<c:if test="${sumList.total.changeLostqyNum!=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbqy" value="${bgysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostgrQNum!=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbgeqy" value="${bgysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostgrGNum!=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbgejg" value="${bgysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeLostjgNum!=0}">
							<c:set var="bgysbb" value="${bgysbb+1}"/>
							<c:set var="bgysbbjg" value="${bgysbbjg+1}"/>
						</c:if>
						<!--变更+损坏更换  -->
						<c:set var="bgshgh" value="0"/>
						<c:set var="bgshghqy" value="0"/>
						<c:set var="bgshghgeqy" value="0"/>
						<c:set var="bgshghgejg" value="0"/>
						<c:set var="bgshghjg" value="0"/>
						<c:if test="${sumList.total.changeReplaceqyNum!=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghqy" value="${bgshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacegrQNum!=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghgeqy" value="${bgshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacegrGNum!=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghgejg" value="${bgshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeReplacejgNum!=0}">
							<c:set var="bgshgh" value="${bgshgh+1}"/>
							<c:set var="bgshghjg" value="${bgshghjg+1}"/>
						</c:if>
						<!-- 更新+遗失补办 -->
						<c:set var="gxysbb" value="0"/>
						<c:set var="gxysbbqy" value="0"/>
					<c:set var="gxysbbgeqy" value="0"/>
					<c:set var="gxysbbgejg" value="0"/>
					<c:set var="gxysbbjg" value="0"/>
					
						<c:if test="${sumList.total.updateLostqyNum!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum2!=0}">
						
						<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum4!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostqyNum5!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbqy" value="${gxysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum!=0}">
							<c:set var="gxysbbysbb" value="${gxysbbysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum2!=0}">
						<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum4!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrQNum5!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgeqy" value="${gxysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum2!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum4!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostgrGNum5!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbgejg" value="${gxysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum2!=0}">
						<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum4!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateLostjgNum5!=0}">
							<c:set var="gxysbb" value="${gxysbb+1}"/>
							<c:set var="gxysbbjg" value="${gxysbbjg+1}"/>
						</c:if>
						<!-- 更新+损坏更换 -->
						<c:set var="gxshgh" value="0"/>
						<c:set var="gxshghqy" value="0"/>
					<c:set var="gxshghgeqy" value="0"/>
					<c:set var="gxshghgejg" value="0"/>
					<c:set var="gxshghjg" value="0"/>
					
						<c:if test="${sumList.total.updateReplaceqyNum!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2!=0}">
						
						<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghqy" value="${gxshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2!=0}">
						<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgeqy" value="${gxgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgxgejg" value="${gxshghgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghgejg" value="${gxshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum2!=0}">
						<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum4!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateReplacejgNum5!=0}">
							<c:set var="gxshgh" value="${gxshgh+1}"/>
							<c:set var="gxshghjg" value="${gxshghjg+1}"/>
						</c:if>
						<!-- 更新 +变更-->
						<c:set var="gxbg" value="0"/>
						<c:set var="gxbgqy" value="0"/>
					<c:set var="gxbggeqy" value="0"/>
					<c:set var="gxbggejg" value="0"/>
					<c:set var="gxbgjg" value="0"/>
					
						<c:if test="${sumList.total.updateChangeqyNum!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum2!=0}">
						
						<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum4!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangeqyNum5!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgqy" value="${gxbgqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum2!=0}">
						<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum4!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrQNum5!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggeqy" value="${gxbggeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum2!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum4!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangegrGNum5!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbggejg" value="${gxbggejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum2!=0}">
						<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum4!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.updateChangejgNum5!=0}">
							<c:set var="gxbg" value="${gxbg+1}"/>
							<c:set var="gxbgjg" value="${gxbgjg+1}"/>
						</c:if>
						
						<!-- 更新 +变更+遗失补办-->
						<c:set var="gxbgysbb" value="0"/>
						<c:set var="gxbgysbbqy" value="0"/>
					<c:set var="gxbgysbbgeqy" value="0"/>
					<c:set var="gxbgysbbgejg" value="0"/>
					<c:set var="gxbgysbbjg" value="0"/>
					
						<c:if test="${sumList.total.changeUpdateLostqyNum!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2!=0}">
						
						<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbqy" value="${gxbgysbbqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2!=0}">
						<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgeqy" value="${gxbgysbbgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbgejg" value="${gxbgysbbgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2!=0}">
						<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5!=0}">
							<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
							<c:set var="gxbgysbbjg" value="${gxbgysbbjg+1}"/>
						</c:if>
						
						<!-- 更新 +变更+损坏更换-->
						<c:set var="gxbgshgh" value="0"/>
						<c:set var="gxbgshghqy" value="0"/>
					<c:set var="gxbgshghgeqy" value="0"/>
					<c:set var="gxbgshghgejg" value="0"/>
					<c:set var="gxbgshghjg" value="0"/>
					
						<c:if test="${sumList.total.changeUpdateReplaceqyNum!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2!=0}">
						
						<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghqy" value="${gxbgshghqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
							
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2!=0}">
						<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgeqy" value="${gxbgshghgeqy+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
						
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghgejg" value="${gxbgshghgejg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2!=0}">
						<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
						</c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5!=0}">
							<c:set var="gxbgshgh" value="${gxbgshgh+1}"/>
							<c:set var="gxbgshghjg" value="${gxbgshghjg+1}"/>
						</c:if>
					
					
				<th rowspan="3">月份</th>
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
				<th colspan="${ysbbqy}" rowspan="2" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${ysbbgeqy >0}">
				<th colspan="${ysbbgeqy}" rowspan="2" style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${ysbbgejg >0}">
				<th colspan="${ysbbgejg}" rowspan="2" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${ysbbjg >0}">
				<th colspan="${ysbbjg} " rowspan="2" style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${shghqy >0}">
				<th colspan="${shghqy}" rowspan="2" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${shghgeqy >0}">
				<th colspan="${shghgeqy}" rowspan="2" style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${shghgejg >0}">
				<th colspan="${shghgejg}" rowspan="2" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${shghjg >0}">
				<th colspan="${ysbbjg} " rowspan="2" style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${bgqy >0}">
				<th colspan="${bgqy}" rowspan="2" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${bggeqy >0}">
				<th colspan="${bggeqy}" rowspan="2" style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bggejg >0}">
				<th colspan="${bggejg}" rowspan="2" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgjg >0}">
				<th colspan="${bgjg} " rowspan="2" style="text-align: center">机构证书</th>
   			 </c:if>
   			 	<c:if test="${multiType}">
				 <c:if test="${bgysbbqy >0}">
				<th colspan="${bgysbbqy}" rowspan="2" style="text-align: center">企业证书${bgysbbqy}</th>
   			 </c:if>
   			  <c:if test="${bgysbbgeqy >0}">
				<th colspan="${bgysbbgeqy}" rowspan="2" style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bgysbbgejg >0}">
				<th colspan="${bgysbbgejg}" rowspan="2" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgysbbjg >0}">
				<th colspan="${bgysbbjg} " rowspan="2" style="text-align: center">机构证书</th>
   			 </c:if>
   			 
   			   <c:if test="${bgshghqy >0}">
				<th colspan="${bgshghqy}" rowspan="2" style="text-align: center">企业证书</th>
   			 </c:if>
   			  <c:if test="${bgshghgeqy >0}">
				<th colspan="${bgshghgeqy}" rowspan="2" style="text-align: center">个人证书（企业）</th>
   			 </c:if>
   			 <c:if test="${bgshghgejg >0}">
				<th colspan="${bgshghgejg}" rowspan="2" style="text-align: center">个人证书（机构）</th>
   			 </c:if>
   			 <c:if test="${bgshghjg >0}">
				<th colspan="${bgysbbjg} " rowspan="2" style="text-align: center">机构证书</th>
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
			
						<c:if test="${sumList.total.xzqyadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.xzqyadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.xzqyadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.xzqyadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.xzgrQadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.xzgrQadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.xzgrQadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.xzgrQadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.xzgrGadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.xzgrGadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.xzgrGadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.xzgrGadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.xzjgadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.xzjgadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.xzjgadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.xzjgadd5!=0}"><th>五年</th></c:if>
						
						<c:if test="${sumList.total.gxqyadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.gxqyadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.gxqyadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.gxqyadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.gxgrQadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.gxgrQadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.gxgrQadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.gxgrQadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.gxgrGadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.gxgrGadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.gxgrGadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.gxgrGadd5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.gxjgadd1!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.gxjgadd2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.gxjgadd4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.gxjgadd5!=0}"><th>五年</th></c:if>
						<c:if test="${multiType}">
						<c:if test="${sumList.total.updateLostqyNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateLostqyNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateLostqyNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateLostqyNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateLostjgNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateLostjgNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateLostjgNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateLostjgNum5!=0}"><th>五年</th></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5!=0}"><th>五年</th></c:if> 
						
						<c:if test="${sumList.total.updateChangeqyNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.updateChangejgNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.updateChangejgNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.updateChangejgNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.updateChangejgNum5!=0}"><th>五年</th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateLostqyNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5!=0}"><th>五年</th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateReplaceqyNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5!=0}"><th>五年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum!=0}"><th>一年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2!=0}"><th>二年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4!=0}"><th>四年</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5!=0}"><th>五年</th></c:if>
				 		</c:if>
	 		</tr>
				
				
						
						<c:forEach items="${sumList}" var ="sum">						
						<c:if test="${sum.key!='total'}">
						<tr>
						<td><c:if test="${sum.key!='total'}">${sum.key}</c:if></td>
								
							<c:if test="${sumList.total.xzqyadd1!=0}"><td>${sum.value.xzqyadd1}</td></c:if>
						<c:if test="${sumList.total.xzqyadd2!=0}"><td>${sum.value.xzqyadd2}</td></c:if>
						<c:if test="${sumList.total.xzqyadd4!=0}"><td>${sum.value.xzqyadd4}</td></c:if>
						<c:if test="${sumList.total.xzqyadd5!=0}"><td>${sum.value.xzqyadd5}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd1!=0}"><td>${sum.value.xzgrQadd1}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd2!=0}"><td>${sum.value.xzgrQadd2}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd4!=0}"><td>${sum.value.xzgrQadd4}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd5!=0}"><td>${sum.value.xzgrQadd5}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd1!=0}"><td>${sum.value.xzgrGadd1}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd2!=0}"><td>${sum.value.xzgrGadd2}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd4!=0}"><td>${sum.value.xzgrGadd4}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd5!=0}"><td>${sum.value.xzgrGadd5}</td></c:if>
						<c:if test="${sumList.total.xzjgadd1!=0}"><td>${sum.value.xzjgadd1}</td></c:if>
						<c:if test="${sumList.total.xzjgadd2!=0}"><td>${sum.value.xzjgadd2}</td></c:if>
						<c:if test="${sumList.total.xzjgadd4!=0}"><td>${sum.value.xzjgadd4}</td></c:if>
						<c:if test="${sumList.total.xzjgadd5!=0}"><td>${sum.value.xzjgadd5}</td></c:if>
						
						<c:if test="${sumList.total.gxqyadd1!=0}"><td>${sum.value.gxqyadd1}</td></c:if>
						<c:if test="${sumList.total.gxqyadd2!=0}"><td>${sum.value.gxqyadd2}</td></c:if>
						<c:if test="${sumList.total.gxqyadd4!=0}"><td>${sum.value.gxqyadd4}</td></c:if>
						<c:if test="${sumList.total.gxqyadd5!=0}"><td>${sum.value.gxqyadd5}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd1!=0}"><td>${sum.value.gxgrQadd1}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd2!=0}"><td>${sum.value.gxgrQadd2}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd4!=0}"><td>${sum.value.gxgrQadd4}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd5!=0}"><td>${sum.value.gxgrQadd5}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd1!=0}"><td>${sum.value.gxgrGadd1}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd2!=0}"><td>${sum.value.gxgrGadd2}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd4!=0}"><td>${sum.value.gxgrGadd4}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd5!=0}"><td>${sum.value.gxgrGadd5}</td></c:if>
						<c:if test="${sumList.total.gxjgadd1!=0}"><td>${sum.value.gxjgadd1}</td></c:if>
						<c:if test="${sumList.total.gxjgadd2!=0}"><td>${sum.value.gxjgadd2}</td></c:if>
						<c:if test="${sumList.total.gxjgadd4!=0}"><td>${sum.value.gxjgadd4}</td></c:if>
						<c:if test="${sumList.total.gxjgadd5!=0}"><td>${sum.value.gxjgadd5}</td></c:if>
						
						<c:if test="${sumList.total.lostCerateqy!=0}"><td>${sum.value.lostCerateqy}</td></c:if>
						<c:if test="${sumList.total.lostCerategrQ!=0}"><td>${sum.value.lostCerategrQ}</td></c:if>
						<c:if test="${sumList.total.lostCerategrG!=0}"><td>${sum.value.lostCerategrG}</td></c:if>
						<c:if test="${sumList.total.lostCeratejg!=0}"><td>${sum.value.lostCeratejg}</td></c:if>
						
						<c:if test="${sumList.total.damageCertificateqy!=0}"><td>${sum.value.damageCertificateqy}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrQ!=0}"><td>${sum.value.damageCertificategrQ}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrG!=0}"><td>${sum.value.damageCertificategrG}</td></c:if>
						<c:if test="${sumList.total.damageCertificatejg!=0}"><td>${sum.value.damageCertificatejg}</td></c:if>
						
						<c:if test="${sumList.total.modifyNumqy!=0}"><td>${sum.value.modifyNumqy}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrQ!=0}"><td>${sum.value.modifyNumgrQ}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrG!=0}"><td>${sum.value.modifyNumgrG}</td></c:if>
						<c:if test="${sumList.total.modifyNumjg!=0}"><td>${sum.value.modifyNumjg}</td></c:if>
						<c:if test="${multiType}">
						<c:if test="${sumList.total.changeLostqyNum!=0}"><td>${sum.value.changeLostqyNum}</td></c:if>
						<c:if test="${sumList.total.changeLostgrQNum!=0}"><td>${sum.value.changeLostgrQNum}</td></c:if>
						<c:if test="${sumList.total.changeLostgrGNum!=0}"><td>${sum.value.changeLostgrGNum}</td></c:if>
						<c:if test="${sumList.total.changeLostjgNum!=0}"><td>${sum.value.changeLostjgNum}</td></c:if>
						
						<c:if test="${sumList.total.changeReplaceqyNum!=0}"><td>${sum.value.changeReplaceqyNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrQNum!=0}"><td>${sum.value.changeReplacegrQNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrGNum!=0}"><td>${sum.value.changeReplacegrGNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacejgNum!=0}"><td>${sum.value.changeReplacejgNum}</td></c:if>
						
						<c:if test="${sumList.total.updateLostqyNum!=0}"><td>${sum.value.updateLostqyNum}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum2!=0}"><td>${sum.value.updateLostqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum4!=0}"><td>${sum.value.updateLostqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum5!=0}"><td>${sum.value.updateLostqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum!=0}"><td>${sum.value.updateLostgrQNum}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2!=0}"><td>${sum.value.updateLostgrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4!=0}"><td>${sum.value.updateLostgrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5!=0}"><td>${sum.value.updateLostgrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum!=0}"><td>${sum.value.updateLostgrGNum}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2!=0}"><td>${sum.value.updateLostgrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4!=0}"><td>${sum.value.updateLostgrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5!=0}"><td>${sum.value.updateLostgrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum!=0}"><td>${sum.value.updateLostjgNum}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum2!=0}"><td>${sum.value.updateLostjgNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum4!=0}"><td>${sum.value.updateLostjgNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum5!=0}"><td>${sum.value.updateLostjgNum5}</td></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum!=0}"><td>${sum.value.updateReplaceqyNum}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2!=0}"><td>${sum.value.updateReplaceqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4!=0}"><td>${sum.value.updateReplaceqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5!=0}"><td>${sum.value.updateReplaceqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum!=0}"><td>${sum.value.updateReplacegrQNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2!=0}"><td>${sum.value.updateReplacegrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4!=0}"><td>${sum.value.updateReplacegrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5!=0}"><td>${sum.value.updateReplacegrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum!=0}"><td>${sum.value.updateReplacegrGNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2!=0}"><td>${sum.value.updateReplacegrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4!=0}"><td>${sum.value.updateReplacegrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5!=0}"><td>${sum.value.updateReplacegrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum!=0}"><td>${sum.value.updateReplacejgNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2!=0}"><td>${sum.value.updateReplacejgNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4!=0}"><td>${sum.value.updateReplacejgNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5!=0}"><td>${sum.value.updateReplacejgNum5}</td></c:if> 
						
						 <c:if test="${sumList.total.updateChangeqyNum!=0}"><td>${sum.value.updateChangeqyNum}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2!=0}"><td>${sum.value.updateChangeqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4!=0}"><td>${sum.value.updateChangeqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5!=0}"><td>${sum.value.updateChangeqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum!=0}"><td>${sum.value.updateChangegrQNum}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2!=0}"><td>${sum.value.updateChangegrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4!=0}"><td>${sum.value.updateChangegrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5!=0}"><td>${sum.value.updateChangegrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum!=0}"><td>${sum.value.updateChangegrGNum}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2!=0}"><td>${sum.value.updateChangegrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4!=0}"><td>${sum.value.updateChangegrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5!=0}"><td>${sum.value.updateChangegrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum!=0}"><td>${sum.value.updateChangejgNum}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum2!=0}"><td>${sum.value.updateChangejgNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum4!=0}"><td>${sum.value.updateChangejgNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum5!=0}"><td>${sum.value.updateChangejgNum5}</td></c:if> 
						
						
						 <c:if test="${sumList.total.changeUpdateLostqyNum!=0}"><th>${sum.value.changeUpdateLostqyNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2!=0}"><th>${sum.value.changeUpdateLostqyNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4!=0}"><th>${sum.value.changeUpdateLostqyNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5!=0}"><th>${sum.value.changeUpdateLostqyNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum!=0}"><th>${sum.value.changeUpdateLostgrQNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2!=0}"><th>${sum.value.changeUpdateLostgrQNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4!=0}"><th>${sum.value.changeUpdateLostgrQNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5!=0}"><th>${sum.value.changeUpdateLostgrQNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum!=0}"><th>${sum.value.changeUpdateLostgrGNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2!=0}"><th>${sum.value.changeUpdateLostgrGNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4!=0}"><th>${sum.value.changeUpdateLostgrGNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5!=0}"><th>${sum.value.changeUpdateLostgrGNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum!=0}"><th>${sum.value.changeUpdateLostjgNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2!=0}"><th>${sum.value.changeUpdateLostjgNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4!=0}"><th>${sum.value.changeUpdateLostjgNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5!=0}"><th>${sum.value.changeUpdateLostjgNum5}</th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateReplaceqyNum!=0}"><th>${sum.value.changeUpdateReplaceqyNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2!=0}"><th>${sum.value.changeUpdateReplaceqyNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4!=0}"><th>${sum.value.changeUpdateReplaceqyNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5!=0}"><th>${sum.value.changeUpdateReplaceqyNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum!=0}"><th>${sum.value.changeUpdateReplacegrQNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2!=0}"><th>${sum.value.changeUpdateReplacegrQNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4!=0}"><th>${sum.value.changeUpdateReplacegrQNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5!=0}"><th>${sum.value.changeUpdateReplacegrQNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum!=0}"><th>${sum.value.changeUpdateReplacegrGNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2!=0}"><th>${sum.value.changeUpdateReplacegrGNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4!=0}"><th>${sum.value.changeUpdateReplacegrGNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5!=0}"><th>${sum.value.changeUpdateReplacegrGNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum!=0}"><th>${sum.value.changeUpdateReplacejgNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2!=0}"><th>${sum.value.changeUpdateReplacejgNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4!=0}"><th>${sum.value.changeUpdateReplacejgNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5!=0}"><th>${sum.value.changeUpdateReplacejgNum5}</th></c:if> 
						</c:if>
						</tr>
						</c:if>
					
							</c:forEach>
							
						
			
			<tr> 
				
						<th>总计</th>
						<c:if test="${sumList.total.xzqyadd1!=0}"><td>${sumList.total.xzqyadd1}</td></c:if>
						<c:if test="${sumList.total.xzqyadd2!=0}"><td>${sumList.total.xzqyadd2}</td></c:if>
						<c:if test="${sumList.total.xzqyadd4!=0}"><td>${sumList.total.xzqyadd4}</td></c:if>
						<c:if test="${sumList.total.xzqyadd5!=0}"><td>${sumList.total.xzqyadd5}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd1!=0}"><td>${sumList.total.xzgrQadd1}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd2!=0}"><td>${sumList.total.xzgrQadd2}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd4!=0}"><td>${sumList.total.xzgrQadd4}</td></c:if>
						<c:if test="${sumList.total.xzgrQadd5!=0}"><td>${sumList.total.xzgrQadd5}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd1!=0}"><td>${sumList.total.xzgrGadd1}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd2!=0}"><td>${sumList.total.xzgrGadd2}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd4!=0}"><td>${sumList.total.xzgrGadd4}</td></c:if>
						<c:if test="${sumList.total.xzgrGadd5!=0}"><td>${sumList.total.xzgrGadd5}</td></c:if>
						<c:if test="${sumList.total.xzjgadd1!=0}"><td>${sumList.total.xzjgadd1}</td></c:if>
						<c:if test="${sumList.total.xzjgadd2!=0}"><td>${sumList.total.xzjgadd2}</td></c:if>
						<c:if test="${sumList.total.xzjgadd4!=0}"><td>${sumList.total.xzjgadd4}</td></c:if>
						<c:if test="${sumList.total.xzjgadd5!=0}"><td>${sumList.total.xzjgadd5}</td></c:if>
						
						<c:if test="${sumList.total.gxqyadd1!=0}"><td>${sumList.total.gxqyadd1}</td></c:if>
						<c:if test="${sumList.total.gxqyadd2!=0}"><td>${sumList.total.gxqyadd2}</td></c:if>
						<c:if test="${sumList.total.gxqyadd4!=0}"><td>${sumList.total.gxqyadd4}</td></c:if>
						<c:if test="${sumList.total.gxqyadd5!=0}"><td>${sumList.total.gxqyadd5}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd1!=0}"><td>${sumList.total.gxgrQadd1}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd2!=0}"><td>${sumList.total.gxgrQadd2}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd4!=0}"><td>${sumList.total.gxgrQadd4}</td></c:if>
						<c:if test="${sumList.total.gxgrQadd5!=0}"><td>${sumList.total.gxgrQadd5}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd1!=0}"><td>${sumList.total.gxgrGadd1}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd2!=0}"><td>${sumList.total.gxgrGadd2}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd4!=0}"><td>${sumList.total.gxgrGadd4}</td></c:if>
						<c:if test="${sumList.total.gxgrGadd5!=0}"><td>${sumList.total.gxgrGadd5}</td></c:if>
						<c:if test="${sumList.total.gxjgadd1!=0}"><td>${sumList.total.gxjgadd1}</td></c:if>
						<c:if test="${sumList.total.gxjgadd2!=0}"><td>${sumList.total.gxjgadd2}</td></c:if>
						<c:if test="${sumList.total.gxjgadd4!=0}"><td>${sumList.total.gxjgadd4}</td></c:if>
						<c:if test="${sumList.total.gxjgadd5!=0}"><td>${sumList.total.gxjgadd5}</td></c:if>
						
						<c:if test="${sumList.total.lostCerateqy!=0}"><td>${sumList.total.lostCerateqy}</td></c:if>
						<c:if test="${sumList.total.lostCerategrQ!=0}"><td>${sumList.total.lostCerategrQ}</td></c:if>
						<c:if test="${sumList.total.lostCerategrG!=0}"><td>${sumList.total.lostCerategrG}</td></c:if>
						<c:if test="${sumList.total.lostCeratejg!=0}"><td>${sumList.total.lostCeratejg}</td></c:if>
						
						<c:if test="${sumList.total.damageCertificateqy!=0}"><td>${sumList.total.damageCertificateqy}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrQ!=0}"><td>${sumList.total.damageCertificategrQ}</td></c:if>
						<c:if test="${sumList.total.damageCertificategrG!=0}"><td>${sumList.total.damageCertificategrG}</td></c:if>
						<c:if test="${sumList.total.damageCertificatejg!=0}"><td>${sumList.total.damageCertificatejg}</td></c:if>
						
						<c:if test="${sumList.total.modifyNumqy!=0}"><td>${sumList.total.modifyNumqy}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrQ!=0}"><td>${sumList.total.modifyNumgrQ}</td></c:if>
						<c:if test="${sumList.total.modifyNumgrG!=0}"><td>${sumList.total.modifyNumgrG}</td></c:if>
						<c:if test="${sumList.total.modifyNumjg!=0}"><td>${sumList.total.modifyNumjg}</td></c:if>
						<c:if test="${multiType}">
						<c:if test="${sumList.total.changeLostqyNum!=0}"><td>${sumList.total.changeLostqyNum}</td></c:if>
						<c:if test="${sumList.total.changeLostgrQNum!=0}"><td>${sumList.total.changeLostgrQNum}</td></c:if>
						<c:if test="${sumList.total.changeLostgrGNum!=0}"><td>${sumList.total.changeLostgrGNum}</td></c:if>
						<c:if test="${sumList.total.changeLostjgNum!=0}"><td>${sumList.total.changeLostjgNum}</td></c:if>
						
						<c:if test="${sumList.total.changeReplaceqyNum!=0}"><td>${sumList.total.changeReplaceqyNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrQNum!=0}"><td>${sumList.total.changeReplacegrQNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacegrGNum!=0}"><td>${sumList.total.changeReplacegrGNum}</td></c:if>
						<c:if test="${sumList.total.changeReplacejgNum!=0}"><td>${sumList.total.changeReplacejgNum}</td></c:if>
						
						<c:if test="${sumList.total.updateLostqyNum!=0}"><td>${sumList.total.updateLostqyNum}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum2!=0}"><td>${sumList.total.updateLostqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum4!=0}"><td>${sumList.total.updateLostqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostqyNum5!=0}"><td>${sumList.total.updateLostqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum!=0}"><td>${sumList.total.updateLostgrQNum}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum2!=0}"><td>${sumList.total.updateLostgrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum4!=0}"><td>${sumList.total.updateLostgrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostgrQNum5!=0}"><td>${sumList.total.updateLostgrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum!=0}"><td>${sumList.total.updateLostgrGNum}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum2!=0}"><td>${sumList.total.updateLostgrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum4!=0}"><td>${sumList.total.updateLostgrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostgrGNum5!=0}"><td>${sumList.total.updateLostgrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum!=0}"><td>${sumList.total.updateLostjgNum}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum2!=0}"><td>${sumList.total.updateLostjgNum2}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum4!=0}"><td>${sumList.total.updateLostjgNum4}</td></c:if>
						<c:if test="${sumList.total.updateLostjgNum5!=0}"><td>${sumList.total.updateLostjgNum5}</td></c:if>
						
						 <c:if test="${sumList.total.updateReplaceqyNum!=0}"><td>${sumList.total.updateReplaceqyNum}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum2!=0}"><td>${sumList.total.updateReplaceqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum4!=0}"><td>${sumList.total.updateReplaceqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplaceqyNum5!=0}"><td>${sumList.total.updateReplaceqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum!=0}"><td>${sumList.total.updateReplacegrQNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum2!=0}"><td>${sumList.total.updateReplacegrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum4!=0}"><td>${sumList.total.updateReplacegrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrQNum5!=0}"><td>${sumList.total.updateReplacegrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum!=0}"><td>${sumList.total.updateReplacegrGNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum2!=0}"><td>${sumList.total.updateReplacegrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum4!=0}"><td>${sumList.total.updateReplacegrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacegrGNum5!=0}"><td>${sumList.total.updateReplacegrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum!=0}"><td>${sumList.total.updateReplacejgNum}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum2!=0}"><td>${sumList.total.updateReplacejgNum2}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum4!=0}"><td>${sumList.total.updateReplacejgNum4}</td></c:if>
						<c:if test="${sumList.total.updateReplacejgNum5!=0}"><td>${sumList.total.updateReplacejgNum5}</td></c:if> 
						
						  <c:if test="${sumList.total.updateChangeqyNum!=0}"><td>${sumList.total.updateChangeqyNum}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum2!=0}"><td>${sumList.total.updateChangeqyNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum4!=0}"><td>${sumList.total.updateChangeqyNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangeqyNum5!=0}"><td>${sumList.total.updateChangeqyNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum!=0}"><td>${sumList.total.updateChangegrQNum}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum2!=0}"><td>${sumList.total.updateChangegrQNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum4!=0}"><td>${sumList.total.updateChangegrQNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangegrQNum5!=0}"><td>${sumList.total.updateChangegrQNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum!=0}"><td>${sumList.total.updateChangegrGNum}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum2!=0}"><td>${sumList.total.updateChangegrGNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum4!=0}"><td>${sumList.total.updateChangegrGNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangegrGNum5!=0}"><td>${sumList.total.updateChangegrGNum5}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum!=0}"><td>${sumList.total.updateChangejgNum}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum2!=0}"><td>${sumList.total.updateChangejgNum2}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum4!=0}"><td>${sumList.total.updateChangejgNum4}</td></c:if>
						<c:if test="${sumList.total.updateChangejgNum5!=0}"><td>${sumList.total.updateChangejgNum5}</td></c:if>  
						
						
						 
							<c:if test="${sumList.total.changeUpdateLostqyNum!=0}"><th>${sumList.total.changeUpdateLostqyNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum2!=0}"><th>${sumList.total.changeUpdateLostqyNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum4!=0}"><th>${sumList.total.changeUpdateLostqyNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostqyNum5!=0}"><th>${sumList.total.changeUpdateLostqyNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum!=0}"><th>${sumList.total.changeUpdateLostgrQNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum2!=0}"><th>${sumList.total.changeUpdateLostgrQNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum4!=0}"><th>${sumList.total.changeUpdateLostgrQNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrQNum5!=0}"><th>${sumList.total.changeUpdateLostgrQNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum!=0}"><th>${sumList.total.changeUpdateLostgrGNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum2!=0}"><th>${sumList.total.changeUpdateLostgrGNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum4!=0}"><th>${sumList.total.changeUpdateLostgrGNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostgrGNum5!=0}"><th>${sumList.total.changeUpdateLostgrGNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum!=0}"><th>${sumList.total.changeUpdateLostjgNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum2!=0}"><th>${sumList.total.changeUpdateLostjgNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum4!=0}"><th>${sumList.total.changeUpdateLostjgNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateLostjgNum5!=0}"><th>${sumList.total.changeUpdateLostjgNum5}</th></c:if> 
						
						 <c:if test="${sumList.total.changeUpdateReplaceqyNum!=0}"><th>${sumList.total.changeUpdateReplaceqyNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum2!=0}"><th>${sumList.total.changeUpdateReplaceqyNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum4!=0}"><th>${sumList.total.changeUpdateReplaceqyNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplaceqyNum5!=0}"><th>${sumList.total.changeUpdateReplaceqyNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum!=0}"><th>${sumList.total.changeUpdateReplacegrQNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum2!=0}"><th>${sumList.total.changeUpdateReplacegrQNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum4!=0}"><th>${sumList.total.changeUpdateReplacegrQNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrQNum5!=0}"><th>${sumList.total.changeUpdateReplacegrQNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum!=0}"><th>${sumList.total.changeUpdateReplacegrGNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum2!=0}"><th>${sumList.total.changeUpdateReplacegrGNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum4!=0}"><th>${sumList.totalchangeUpdateReplacegrGNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacegrGNum5!=0}"><th>${sumList.total.changeUpdateReplacegrGNum5}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum!=0}"><th>${sumList.total.changeUpdateReplacejgNum}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum2!=0}"><th>${sumList.total.changeUpdateReplacejgNum2}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum4!=0}"><th>${sumList.total.changeUpdateReplacejgNum4}</th></c:if>
						<c:if test="${sumList.total.changeUpdateReplacejgNum5!=0}"><th>${sumList.total.changeUpdateReplacejgNum5}</th></c:if> 
						</c:if>
			</tr>
		</table>
		</div>
	</div>
</body>
</html>
