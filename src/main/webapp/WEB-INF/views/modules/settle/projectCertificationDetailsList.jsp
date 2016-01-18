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
			
<%-- 				<tr>
					
					<td>新增一年期证书${projectcount.add1}张</td>
					<td> 新增二年期证书 ${projectcount.add2}张</td>
					<td> 新增四年期证书 ${projectcount.add4}张</td>
					<td> 新增五年期证书 ${projectcount.add5}张</td>
				</tr>
				
				<tr>
					
					<td>更新 一年期证书${projectcount.renew1}张</td>
					<td> 更新二年期证书 ${projectcount.renew2}张</td>
					<td>更新 四年期证书 ${projectcount.renew4}张</td>
					<td> 更新五年期证书 ${projectcount.renew2}张</td>
				</tr>
				
				<tr>
					<td>遗失补办：证书${projectcount.lostCerate}张</td>
					<td>损坏更换：证书${projectcount.damageCertificate}张</td>
					<td>变更：证书</td>
					<td>更新+变更：证书</td>
				</tr>
				<tr>
					<td>更新+遗失补办：证书</td>
					<td>更新+损坏更换：证书</td>
					<td>变更+遗失补办：证书</td>
					<td>变更+损坏更换：证书</td>
				</tr>
				<tr>
					<td>更新+变更+遗失补办：证书</td>
					<td>更新+变更+损坏更换：证书</td>
					<td colspan="2">吊销：证书</td>
				</tr> --%>
			<thread>
				<tr>
					
					
					<th colspan=4>新增</th>
					<th colspan=4>更新</th>
					<th rowspan="2">遗失补办</th>
					<th rowspan="2">损坏更换</th>
					<th rowspan="2">信息变更</th>
					<th colspan="4">更新+变更</th>
					<th colspan="4">更新+遗失补办</th>
					<th colspan="4">更新+损坏更换</th>
					<th rowspan="2">变更+遗失补办</th>
					<th rowspan="2">变更+损坏更换</th>
					<th colspan="4">更新+变更+遗失补办</th>
					<th colspan="4">更新+变更+损坏更换</th>
				</tr>
				<tr>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				
			</tr> 
			</thread>
			<tbody>
				<td>${projectcount.add1}</td>
				<td>${projectcount.add2}</td>
				<td>${projectcount.add4}</td>
				<td>${projectcount.add5}</td>
				<td>${projectcount.renew1}</td>
				<td>${projectcount.renew2}</td>
				<td>${projectcount.renew4}</td>
				<td>${projectcount.renew5}</td>
				<td>${projectcount.lostCerate}</td>
				<td>${projectcount.damageCertificate}</td>
				<td>${projectcount.modifyNum}</td>
				<td>${projectcount.updateChangeNum}</td>
				<td>${projectcount.updateChangeNum2}</td>
				<td>${projectcount.updateChangeNum4}</td>
				<td>${projectcount.updateChangeNum5}</td>
				<td>${projectcount.updateLostNum}</td>
				<td>${projectcount.updateLostNum2}</td>
				<td>${projectcount.updateLostNum4}</td>
				<td>${projectcount.updateLostNum5}</td>
				<td>${projectcount.updateReplaceNum}</td>
				<td>${projectcount.updateReplaceNum2}</td>
				<td>${projectcount.updateReplaceNum4}</td>
				<td>${projectcount.updateReplaceNum5}</td>
				<td>${projectcount.changeLostNum}</td>
				<td>${projectcount.changeReplaceNum}</td>
				<td>${projectcount.changeUpdateLostNum}</td>
				<td>${projectcount.changeUpdateLostNum2}</td>
				<td>${projectcount.changeUpdateLostNum4}</td>
				<td>${projectcount.changeUpdateLostNum5}</td>
				<td>${projectcount.changeUpdateReplaceNum}</td>
				<td>${projectcount.changeUpdateReplaceNum2}</td>
				<td>${projectcount.changeUpdateReplaceNum4}</td>
				<td>${projectcount.changeUpdateReplaceNum5}</td>
				
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
