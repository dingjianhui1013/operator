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
				</select> <label>组合业务：</label><input type="checkbox" name="multiType"
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
			<table class="table table-striped table-bordered table-condensed">
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
				 --%>
					<tr>

						<th rowspan="3">月份</th>
						<th colspan=8>新增（企业）</th>
						<th colspan=8>新增（个人）</th>
						<th colspan=8>更新（企业）</th>
						<th colspan=8>更新（个人）</th>
						<th rowspan="2" colspan="2">遗失补办</th>
						<th rowspan="2" colspan="2">损坏更换</th>
						<th rowspan="2" colspan="2">信息变更</th>
						<th colspan="8">更新+变更</th>
						<th colspan="8">更新+遗失补办</th>
						<th colspan="8">更新+损坏更换</th>
						<th rowspan="2" colspan="2">变更+遗失补办</th>
						<th rowspan="2" colspan="2">变更+损坏更换</th>
						<th colspan="8">更新+变更+遗失补办</th>
						<th colspan="8">更新+变更+损坏更换</th>
					</tr>
					<tr>
						<c:forEach var="i" begin="1" end="4">
							<th colspan="2">一年</th>
							<th colspan="2">二年</th>
							<th colspan="2">四年</th>
							<th colspan="2">五年</th>
						</c:forEach>
						<c:forEach var="i" begin="8" end="10">
							<th colspan="2">一年</th>
							<th colspan="2">二年</th>
							<th colspan="2">四年</th>
							<th colspan="2">五年</th>
						</c:forEach>
						<c:forEach var="i" begin="13" end="14">
							<th colspan="2">一年</th>
							<th colspan="2">二年</th>
							<th colspan="2">四年</th>
							<th colspan="2">五年</th>
						</c:forEach>
					</tr>
					<tr>
						<c:forEach var="i" begin="1" end="41">
							<th>现金</th>
							<th>pos</th>

						</c:forEach>
					</tr>
					<!-- <tr>
				<td style="text-align:center; vertical-align: middle;" colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;" colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">1年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">2年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">4年</td>
				<td style="text-align:center; vertical-align: middle;"colspan="2">5年</td>
				
			</tr>  -->
					<!-- <tr>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
				<td style="text-align:center; vertical-align: middle;">现金</td>
				<td style="text-align:center; vertical-align: middle;">pos</td>
			</tr>  -->
				</thead>

				<c:forEach items="${sumList }" var="sum">
					<tr>
						<td>${sum.key }</td>
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
		</div>
	</div>
</body>
</html>
