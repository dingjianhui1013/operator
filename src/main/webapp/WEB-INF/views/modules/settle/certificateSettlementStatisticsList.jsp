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
								<th>${dealInfoType_YearY}年</th>
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:forEach>
			</tr>
			<c:forEach items="${month}" var="month">
				<tr>
					<td>${month}</td>
					<c:forEach items="${deal_pro}" var="deal_pro">
					<c:forEach items="${dealInfoType_Year }" var="dealInfoType_Year">
						<c:if test="${deal_pro.key==dealInfoType_Year.deal&&dealInfoType_Year.date==month}">
							<c:forEach items="${dealInfoType_Year.workCount}" var="dealInfoType_YearC">
								<td>${dealInfoType_YearC}</td>
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:forEach>
				</tr>
			</c:forEach>
		</table>
		</div>
	</div>
</body>
</html>
