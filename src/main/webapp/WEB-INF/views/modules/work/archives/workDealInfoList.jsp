<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function(){
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
</script>


<script type="text/javascript">
	document.onkeydown = function(event) {
		var target, code, tag;
		if (!event) {
			event = window.event; //针对ie浏览器  
			target = event.srcElement;
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "TEXTAREA") {
					return true;
				} else {
					return false;
				}
			}
		} else {
			target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
			code = event.keyCode;
			if (code == 13) {
				tag = target.tagName;
				if (tag == "INPUT") {
					return false;
				} else {
					return true;
				}
			}
		}
	};
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/work/workDealInfo/">未归档档案</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		
				<br />
				&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit"
				class="btn btn-primary" type="submit" value="查询" />
	
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" name="oneDealCheck" value="${page.pageNo}" 
				<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
				</c:forEach>
				onchange="checkAll(this)"
				/> </th>
				<th>业务编号</th>
				<th>应用名称</th>
				<th>单位名称</th>

				<th>经办人</th>
				<th>产品名称</th>
				<th>证书类型</th>
				<th>KEY编码</th>
				<th>制证日期</th>
				<th>有效期</th>
				<th>到期日期</th>
				<th>业务状态</th>
				<th>操作</th>
				<th>印章操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>
					
					<c:if test="${workDealInfo.dealInfoStatus==7}">
						<input type="checkbox" name="oneDealCheck" value = "${workDealInfo.id}" 
						<c:forEach items="${ids }" var="id">
							<c:if test="${id==workDealInfo.id }"> checked="checked"</c:if>
						</c:forEach>
						onchange="changeCheck(this)"
						 /> 
					 </c:if>
					 
					 </td>
					<td>${workDealInfo.svn}</td>
					<td>${workDealInfo.configAppName}</td>
					<td><a
						href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.companyName}</a></td>
<%-- 					<td>${workDealInfo.workUser.contactName}</td> --%>
					<td>${workDealInfo.certApplyInfoName }</td>
					<td>${proType[workDealInfo.productName]}</td>
					<td>
						<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
						<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
						<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
						<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
					</td>
					<td>${workDealInfo.keySn }</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${workDealInfo.signDate}"/></td>
					<td><c:if
							test="${not empty workDealInfo.notafter && not empty workDealInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
						</c:if></td>
					<td><fmt:formatDate
							value="${workDealInfo.notafter }"
							pattern="yyyy-MM-dd" /> </td>
				
					<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
					<td><c:if
							test="${workDealInfo.dealInfoStatus==3||workDealInfo.dealInfoStatus==7 }">
							<a href="javascript:maintain(${workDealInfo.id})">维护</a>
							<c:if test="${workDealInfo.dealInfoType!=10 }">
								<a href="javascript:hisgoMoney(${workDealInfo.id})">退费</a>
							</c:if>
						</c:if> 
						
						
						
						
						<c:if test="${workDealInfo.dealInfoStatus==5 }">
						<shiro:hasPermission name="work:workDealInfo:jiaofei">
							<a href="${ctx}/work/workDealInfo/pay?id=${workDealInfo.id}">缴费</a>
							</shiro:hasPermission>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==8 }">
							<a href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">继续编辑</a>&nbsp;&nbsp;
					</c:if>
					
					<c:if test="${workDealInfo.dealInfoStatus==15 }">
							<a href="javascript:returnDealInfo(${workDealInfo.id})">编辑基本信息</a>&nbsp;&nbsp;
					</c:if> 					
					<c:if test="${workDealInfo.dealInfoStatus==9 }">
							<shiro:hasPermission name="work:workDealInfo:makezheng"><a
								href="${ctx}/work/workDealInfoOperation/make?id=${workDealInfo.id}">制证</a></shiro:hasPermission>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==4 }">
							<a
								href="${ctx}/work/workDealInfoOperation/errorForm?id=${workDealInfo.id}">再次编辑</a>&nbsp;&nbsp;
					</c:if> <c:if
							test="${workDealInfo.dealInfoStatus==8||workDealInfo.dealInfoStatus==5 }">
							<a href="${ctx}/work/workDealInfo/delete?id=${workDealInfo.id}"
								onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>&nbsp;&nbsp;
					</c:if>
					<c:if test="${workDealInfo.dealInfoStatus == 13}">
						<shiro:hasPermission name="work:workDealInfo:makezheng">
						<a href="${ctx}/work/workDealInfoAudit/makeDealInfo?id=${workDealInfo.id}">制证</a>
						</shiro:hasPermission>
					</c:if>
					</td>
					<td>
						<c:if test="${workDealInfo.dealInfoStatus==7 }">
<%-- 						    <a href="${ctx}/signature/signatureInfo/form?workDealInfoId=${workDealInfo.id}">印章授权</a> --%>
 								<a href="javascript:checkAdd(${workDealInfo.id})">印章授权</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>
	
	<div id="declareDiv" style="width: 800px;margin-left: -450px;" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/work/workDealInfo/addAttach"
				enctype="multipart/form-data">
				<div class="row-fluid">
			<input type="hidden" name="appId" id="appId" />
			<input type="hidden" name="productT" id="productT"/>
			<input type="hidden" name="lableT" id="lableT"/>
			<input type="hidden" name="dealInfoTypeT" value="0"/>
			
			<input type="hidden" name="agentIdT" id="agentIdT"/>
			<input type="hidden" name="agentDetailIdT" id="agentDetailIdT"/>
			<input type="hidden" name="yearT" id="yearT"/>
			
			<table class="table table-striped table-bordered table-condensed">
				<tbody>
					<tr>
						<th colspan="4" style="font-size: 20px;">基本信息</th>
					</tr>
					<tr>
						<th style="width: 80px;"><span class="prompt"
							style="color: red; display: none;">*</span>应用名称：</th>
						<td style="width: 260px;">		
						<input type="text" name="configApp" id="app" />			
						</td>
						<th>选择产品：</th>
						<td id="productTdId">
							<c:forEach items="${proList }"
									var="pro">
									<div id="product${pro.id }">
										<input type="radio" name ="product" id="pro${pro.id }"
											onclick="productLabel(${pro.id})"
											value="${pro.id}">${pro.name }&nbsp;&nbsp;&nbsp;
									</div>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th style="width: 80px;">应用标识：</th>
						<td style="width: 260px;">
							<input type="radio" name="lable" id="lable0"
								value="0" disabled="disabled" onclick="showAgent(0)">通用&nbsp;
								&nbsp; <input type="radio" name="lable" id="lable1" value="1"
								disabled="disabled" onclick="showAgent(1)">专用
						</td>
						<th>业务类型：</th>
						<td id="productTdId">
							<input type="checkbox" checked="checked"
								disabled="disabled">新增证书 
						</td>
					</tr>
					<tr>
						<th style="width: 80px;">计费策略类型：</th>
						<td style="width: 260px;">
						<input type="hidden" id="boundId">
						<select id="agentId" name="agentId">
								<option value="0">请选择</option>
						</select></td>
						<th>计费策略模版：</th>
						<td id="productTdId">
							<select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select>
						</td>
					</tr>
					<tr>
						<th style="width: 80px;">申请年数：</th>
						<td style="width: 260px;">
							<input type="radio" name="year" value="1" id="year1"
								<c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span
								id="word1">1年</span> <input type="radio" name="year" value="2"
								id="year2" <c:if test="${workDealInfo.year==2}">checked</c:if>><span
								id="word2">2年 </span><input type="radio" name="year" value="4"
								id="year4" <c:if test="${workDealInfo.year==4}">checked</c:if>><span
								id="word4">4年</span><input type="radio" name="year" value="5"
								id="year5" <c:if test="${workDealInfo.year==5}">checked</c:if>><span
								id="word5">5年</span>
						</td>
						<th>导入文件：</th>
						<td id="productTdId">
							<input id="fileName" name="fileName" type="file" multiple="multiple" />
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
