<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>盘点信息管理</title>
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
		
		
		function saveDate(i){
			var depotId = i;
			$("#pdDepotId").val(depotId);
		}
		
		function savecheckedTime(){
			var changeTime = $("#changeTwoTime").val();
			if (changeTime==null||changeTime=='') {
				top.$.jBox.tip("请选择盘点截止时间！");
				return;
			}
			var depotId = $("#pdDepotId").val();
			var checkUrl = "${ctx}/receipt/receiptCheckLog/checkTime?depotId="+depotId+"&endDate="+changeTime;
			$.getJSON(checkUrl, function(da) {
				if (da.status==1) {
					if (da.after==1) {
						var url ="${ctx}/receipt/receiptCheckLog/checkLogFrom?id="+depotId+"&time="+changeTime;
						window.location.href=url;
					}else{
						top.$.jBox.tip("所选时间已进行过盘点，无法再次盘点！");
					}
				} 
				if (da.status==-1) {
					top.$.jBox.tip("结束时间验证失败！");
				}
			});
		}
		
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/receipt/receiptCheckLog/li">盘点</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptDepotInfo" action="${ctx}/receipt/receiptCheckLog/li" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>库房名称 ：</label><form:input path="receiptName" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所在网点</th>
				<th>联系人</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="receiptDepotInfo">
			<tr <c:if test="${receiptDepotInfo.receiptType==0}">style="color: red"</c:if>>
				<td>${receiptDepotInfo.receiptName}</td>
				<td>${receiptDepotInfo.office.areaName}</td>
				<td>${receiptDepotInfo.office.name}</td>
				<td>${receiptDepotInfo.receiptCommUser}</td>
				<td>${receiptDepotInfo.receiptCommMobile}</td>
				<td>
					<shiro:hasPermission name="receipt:receiptCheckLog:pand">
						<c:if test="${receiptDepotInfo.receiptType!=0}">
	    						<a href="#modal-container" data-toggle="modal" onclick="saveDate(${receiptDepotInfo.id})">盘点</a>
	    				</c:if>
    				</shiro:hasPermission>
					<shiro:hasPermission name="receipt:receiptCheckLog:success">
	    				<c:if test="${receiptDepotInfo.receiptType==0}">
		    				<a href="${ctx}/receipt/receiptCheckLog/checkLogFwFrom?id=${receiptDepotInfo.id}">复位</a>
	    				</c:if>
    				</shiro:hasPermission>
    				<a href="${ctx}/receipt/receiptCheckLog/checkLogFromF?id=${receiptDepotInfo.id}">查看</a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	<div id="modal-container" class="modal hide fade" style="width:400px;height:200px;left:50%;top:100px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" >
			 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				选择盘点截止时间
			</h3>
		</div>
		<div class="modal-body"  style="height: 210px">
		<div class="control-group">
			<label class="control-label">盘点截止时间:</label>
			<input id="changeTwoTime" name="changeTwoTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate:'#F{$dp.$D(\'pdate\')}'});"
				value="<fmt:formatDate value="${pDate}" pattern="yyyy-MM-dd"/>"
				/>
				<input type="hidden"  id="pdDepotId" name="pdDepotId" />
			<input type="hidden" id="pdate" name="pdate" value="<fmt:formatDate value="${pDate}" pattern="yyyy-MM-dd"/>">
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="savecheckedTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
		</div>
		</div>
	
</body>
</html>
