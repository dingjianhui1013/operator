<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>盘点管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function checkSP(data){
		var url="${ctx}/key/keyCheckLog/checkFW?depotId="+data+"&_="+new Date().getTime();
		$.getJSON(url, function(da) {
			if (da.status==1) {
				top.$.jBox.tip("当前库存存在未复位信息！审批后再进行盘点操作！");
			} 
			if (da.status==0) {
				window.location.href="${ctx}/key/keyCheckLog/checkList?depotId="+data;
			}
		});
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
		
		var checkUrl = "${ctx}/key/keyCheckLog/checkTime?depotId="+depotId+"&endDate="+changeTime;
		$.getJSON(checkUrl, function(da) {
			if (da.status==1) {
				if (da.after==1) {
					var url ="${ctx}/key/keyCheckLog/checkList?depotId="+depotId+"&time="+changeTime;
					window.location.href=url;
				}else{
					top.$.jBox.tip("所选时间已进行过盘点，无法再次盘点！");
				}
			} 
			if (da.status==-1) {
				top.$.jBox.tip("盘点截止时间验证失败！");
			}
		});
		
		
		
	}
	
	
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyCheckLog/">盘点管理</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyCheckLog"
		action="${ctx}/key/keyCheckLog/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
			<div style="margin-top: 9px">
			<label>库房名称 ：</label>
			<input type="text" name="depotName" id="depotName" value="${depotName}"
				maxlength="30" class="input-medium" />
				&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit"
			class="btn btn-primary" type="submit" 
			value="查询" />
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所属网点</th>
				<th>联系人</th>
				<th>电话</th>
				<shiro:hasPermission name="key:keyCheckLog:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="depot">

				<tr <c:if test="${depot.isReset==1 }">style="color:red;"</c:if>>
					<td>${depot.depotName}</td>
					<td>${depot.office.parent.name}</td>
					<td>${depot.office.name}</td>
					<td>${depot.linkmanName}</td>
					<td>${depot.linkmanMobilePhone}</td>
					<td><shiro:hasPermission name="key:keyCheckLog:edit">
							<input type="hidden" id="depotId" value="${depot.id}">
							<%-- 	<a
							href="${ctx}/key/keyCheckLog/checkList?depotId=${depot.id}" onclick="">盘点</a> --%>
						<c:if test="${depot.isReset!=1 }">
							<a href="#modal-container" data-toggle="modal" onclick="saveDate(${depot.id})">盘点</a>
						</c:if>
						</shiro:hasPermission> <shiro:hasPermission name="key:keyCheckLog:reset">
							<c:if test="${depot.isReset==1 }">
								<a href="${ctx}/key/keyCheckLog/resetList?depotId=${depot.id}">复位</a>
							</c:if>
						</shiro:hasPermission> <a
						href="${ctx}/key/keyCheckLog/checkLogShowList?depotId=${depot.id}">查看</a>

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
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate:'#F{$dp.$D(\'pDate\')}'});"
				value="<fmt:formatDate value="${pDate}" pattern="yyyy-MM-dd"/>"
				/>
				<input type="hidden"  id="pdDepotId" name="pdDepotId" />
				<input type="hidden" id="pDate" name="pDate" value="<fmt:formatDate value="${pDate}" pattern="yyyy-MM-dd"/>">
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="savecheckedTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
			
		</div>
		</div>
</body>
</html>
