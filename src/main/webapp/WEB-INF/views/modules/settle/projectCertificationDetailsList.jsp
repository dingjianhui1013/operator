<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目发证明细管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	
		function dca() {
			if ($("#alias").val() == "") {
				top.$.jBox.tip("请选择项目");
				
			} else {
				if ($("#startTime").val() == "" || $("#endTime").val() == "") {
					top.$.jBox.tip("请选定时间范围");
				} else {
					var alias = $("#alias").val();
					var startTime = $("#startTime").val();
					var endTime = $("#endTime").val();

					window.location.href = "${ctx }/settle/projectCertificationDetails/export?alias="
							+ alias
							+ "&startTime="
							+ startTime
							+ "&endTime="
							+ endTime;
				}
			}
		}
		function alarmValue(dealInfoId) {
			var url = "${ctx}/work/workDealInfo/showWorkDeal?dealInfoId="
					+ dealInfoId;
			top.$.jBox.open("iframe:" + url, "查看", 550, 480, {
				buttons : {
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
		<li class="active"><a href="${ctx}/settle/projectCertificationDetails/">项目发证明细列表</a></li>
		<shiro:hasPermission name="settle:projectCertificationDetails:edit"><li><a href="${ctx}/settle/projectCertificationDetails/form">项目发证明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="projectCertificationDetails" action="${ctx}/settle/projectCertificationDetails/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>项目名称：</label> 
			<select name="alias"
				id="alias">
				<option value="">请选择项目</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==alias}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
			<label>项目时间 ：</label> 
		<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:dca()" class="btn btn-primary">导出</a>
			</div>
	</form:form>
	<tags:message content="${message}"/>
		
		<c:if test="${projectcount!=null}">
		<table id="projectcountTable"
		class="table table-striped table-bordered table-condensed">
		
		<c:set var="xz" value="0"/>
		<c:if test="${projectcount.add1>0}">
			<c:set var="xz" value="${xz+1}"/>
		</c:if>
		<c:if test="${projectcount.add2>0}">
			<c:set var="xz" value="${xz+1}"/>
		</c:if>
		<c:if test="${projectcount.add3>0}">
			<c:set var="xz" value="${xz+1}"/>
		</c:if>
		<c:if test="${projectcount.add4>0}">
			<c:set var="xz" value="${xz+1}"/>
		</c:if>
		<c:if test="${projectcount.add5>0}">
			<c:set var="xz" value="${xz+1}"/>
		</c:if>
		
		<c:set var="gx" value="0"/>
		<c:if test="${projectcount.renew1>0}">
			<c:set var="gx" value="${gx+1}"/>
		</c:if>
		<c:if test="${projectcount.renew2>0}">
			<c:set var="gx" value="${gx+1}"/>
		</c:if>
		<c:if test="${projectcount.renew3>0}">
			<c:set var="gx" value="${gx+1}"/>
		</c:if>
		<c:if test="${projectcount.renew4>0}">
			<c:set var="gx" value="${gx+1}"/>
		</c:if>
		<c:if test="${projectcount.renew5>0}">
			<c:set var="gx" value="${gx+1}"/>
		</c:if>
		
		<c:set var="gxbg" value="0"/>
		<c:if test="${projectcount.updateChangeNum>0}">
			<c:set var="gxbg" value="${gxbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateChangeNum2>0}">
			<c:set var="gxbg" value="${gxbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateChangeNum3>0}">
			<c:set var="gxbg" value="${gxbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateChangeNum4>0}">
			<c:set var="gxbg" value="${gxbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateChangeNum5>0}">
			<c:set var="gxbg" value="${gxbg+1}"/>
		</c:if>
		
		<c:set var="gxysbb" value="0"/>
		<c:if test="${projectcount.updateLostNum>0}">
			<c:set var="gxysbb" value="${gxysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.updateLostNum2>0}">
			<c:set var="gxysbb" value="${gxysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.updateLostNum3>0}">
			<c:set var="gxysbb" value="${gxysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.updateLostNum4>0}">
			<c:set var="gxysbb" value="${gxysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.updateLostNum5>0}">
			<c:set var="gxysbb" value="${gxysbb+1}"/>
		</c:if>
		
		<c:set var="gxshbg" value="0"/>
		<c:if test="${projectcount.updateReplaceNum>0}">
			<c:set var="gxshbg" value="${gxshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateReplaceNum2>0}">
			<c:set var="gxshbg" value="${gxshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateReplaceNum3>0}">
			<c:set var="gxshbg" value="${gxshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateReplaceNum4>0}">
			<c:set var="gxshbg" value="${gxshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.updateReplaceNum5>0}">
			<c:set var="gxshbg" value="${gxshbg+1}"/>
		</c:if>
		
		<c:set var="gxbgysbb" value="0"/>
		<c:if test="${projectcount.changeUpdateLostNum>0}">
			<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateLostNum2>0}">
			<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateLostNum3>0}">
			<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateLostNum4>0}">
			<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateLostNum5>0}">
			<c:set var="gxbgysbb" value="${gxbgysbb+1}"/>
		</c:if>
		
		<c:set var="gxbgshbg" value="0"/>
		<c:if test="${projectcount.changeUpdateReplaceNum>0}">
			<c:set var="gxbgshbg" value="${gxbgshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateReplaceNum2>0}">
			<c:set var="gxbgshbg" value="${gxbgshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateReplaceNum3>0}">
			<c:set var="gxbgshbg" value="${gxbgshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateReplaceNum4>0}">
			<c:set var="gxbgshbg" value="${gxbgshbg+1}"/>
		</c:if>
		<c:if test="${projectcount.changeUpdateReplaceNum5>0}">
			<c:set var="gxbgshbg" value="${gxbgshbg+1}"/>
		</c:if>
			<thead>
				<tr>
					<c:if test="${xz>0}">
						<th colspan="${xz}" style="text-align:center; vertical-align: middle;">新增</th>
					</c:if>
					<c:if test="${gx>0 }">
						<th colspan="${gx}" style="text-align:center; vertical-align: middle;">更新</th>
					</c:if>
					<c:if test="${projectcount.lostCerate>0}">
						<th rowspan="2" style="text-align:center; vertical-align: middle;">遗失补办</th>
					</c:if>
					<c:if test="${projectcount.damageCertificate>0}">
						<th rowspan="2" style="text-align:center; vertical-align: middle;">损坏更换</th>
					</c:if>
					<c:if test="${projectcount.modifyNum>0}">
						<th rowspan="2" style="text-align:center; vertical-align: middle;">信息变更</th>
					</c:if>
					<c:if test="${gxbg>0 }">
						<th colspan="${gxbg}" style="text-align:center; vertical-align: middle;">更新+变更</th>
					</c:if>
					<c:if test="${gxysbb>0}">
						<th colspan="${gxysbb}" style="text-align:center; vertical-align: middle;">更新+遗失补办</th>
					</c:if>
					<c:if test="${gxshbg>0 }">
						<th colspan="${gxshbg}" style="text-align:center; vertical-align: middle;">更新+损坏更换</th>
					</c:if>
					<c:if test="${projectcount.changeLostNum>0}">
						<th rowspan="2" style="text-align:center; vertical-align: middle;">变更+遗失补办</th>
					</c:if>
					<c:if test="${projectcount.changeReplaceNum>0}">
						<th rowspan="2" style="text-align:center; vertical-align: middle;">变更+损坏更换</th>
					</c:if>
					<c:if test="${gxbgysbb>0}">
						<th colspan="${gxbgysbb}" style="text-align:center; vertical-align: middle;">更新+变更+遗失补办</th>
					</c:if>
					<c:if test="${gxbgshbg>0}">
						<th colspan="${gxbgshbg}" style="text-align:center; vertical-align: middle;">更新+变更+损坏更换</th>
					</c:if>
				</tr>
				<tr>
				<c:if test="${projectcount.add1>0}">
						<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.add2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.add3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.add4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.add5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.renew1>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.renew2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.renew3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.renew4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.renew5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
					<c:if test="${projectcount.updateChangeNum3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum>0}">
					<td style="text-align:center; vertical-align: middle;">1年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum2>0}">
					<td style="text-align:center; vertical-align: middle;">2年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum3>0}">
					<td style="text-align:center; vertical-align: middle;">3年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum4>0}">
					<td style="text-align:center; vertical-align: middle;">4年</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum5>0}">
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</c:if>
			</tr> 
			</thead>
			<tbody>
			<tr>
				<c:if test="${projectcount.add1>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.add1}</td>
				</c:if>
				<c:if test="${projectcount.add2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.add2}</td>
				</c:if>
				<c:if test="${projectcount.add3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.add3}</td>
				</c:if>
				<c:if test="${projectcount.add4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.add4}</td>
				</c:if>
				<c:if test="${projectcount.add5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.add5}</td>
				</c:if>
				
				<c:if test="${projectcount.renew1>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.renew1}</td>
				</c:if>
				<c:if test="${projectcount.renew2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.renew2}</td>
				</c:if>
					<c:if test="${projectcount.renew3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.renew3}</td>
				</c:if>
				<c:if test="${projectcount.renew4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.renew4}</td>
				</c:if>
				<c:if test="${projectcount.renew5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.renew5}</td>
				</c:if>
				<c:if test="${projectcount.lostCerate>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.lostCerate}</td>
				</c:if>
				<c:if test="${projectcount.damageCertificate>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.damageCertificate}</td>
				</c:if>
				<c:if test="${projectcount.modifyNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.modifyNum}</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateChangeNum}</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateChangeNum2}</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateChangeNum3}</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateChangeNum4}</td>
				</c:if>
				<c:if test="${projectcount.updateChangeNum5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateChangeNum5}</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateLostNum}</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateLostNum2}</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateLostNum3}</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateLostNum4}</td>
				</c:if>
				<c:if test="${projectcount.updateLostNum5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateLostNum5}</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateReplaceNum}</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateReplaceNum2}</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateReplaceNum3}</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateReplaceNum4}</td>
				</c:if>
				<c:if test="${projectcount.updateReplaceNum5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.updateReplaceNum5}</td>
				</c:if>
				<c:if test="${projectcount.changeLostNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeLostNum}</td>
				</c:if>
				<c:if test="${projectcount.changeReplaceNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeReplaceNum}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateLostNum}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateLostNum2}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateLostNum3}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateLostNum4}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateLostNum5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateLostNum5}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateReplaceNum}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum2>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateReplaceNum2}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum3>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateReplaceNum3}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum4>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateReplaceNum4}</td>
				</c:if>
				<c:if test="${projectcount.changeUpdateReplaceNum5>0}">
					<td style="text-align:center; vertical-align: middle;">${projectcount.changeUpdateReplaceNum5}</td>
				</c:if>
			</tr>
			</tbody>
		
	</table>
	</c:if>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>ID</th>
				<th>单位名称</th>
				<th>业务类型</th>
				<th>证书类型</th>
				<th>证书有效期</th>
				<th>制证时间</th>
				<th>所属区域</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		 <c:set var="zje"  value="0"/>  
			<c:forEach items="${page.list}" var="workDealInfo" varStatus="status">
			
				<tr>
				<td>${status.index+1 }</td>
				<td>${workDealInfo.workCompany.companyName}</td>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<td>
					<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
					<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
					<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
					<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
				</td>
				<td><c:if
							test="${not empty workDealInfo.workCertInfo.notafter && not empty workDealInfo.workCertInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
					</c:if></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${workDealInfo.workCertInfo.signDate}"/></td>
				<td>
					<c:if test="${workDealInfo.workCompany.district!=null}">${workDealInfo.workCompany.district}</c:if>
				</td>
				<td><a href="javaScript:alarmValue( ${workDealInfo.id} )">查看 </a></td>
				</tr>
				
			</c:forEach>
				<shiro:hasPermission name="settle:settleKey:edit"><td>
				</td></shiro:hasPermission>
				
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
