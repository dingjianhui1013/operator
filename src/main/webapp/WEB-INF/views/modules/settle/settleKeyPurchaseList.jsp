<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>key采购记录</title>
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
		function dca()
		{	
			
			var appName=$("#appName").val();
			var storageDate=$("#storageDate").val();
			var status=$("#status").val();
			window.location.href="${ctx}/settle/keyPurchase/export?appName="+appName+"&storageDate="+storageDate+"&status="+status;
		}
		function fk(id)
		{
			
			var html = $('#bzBox').html();
			var submit = function (v, h, f) {
				if(v=='ok')
					{
					    if (f.remarks == '') {
					        top.$.jBox.tip("请填写备注。", 'error', { focusId: "remarks" });
					        return false;
					    }else
					    	{
					    		window.location.href="${ctx}/settle/keyPurchase/updateStatus?keyID="+id+"&remarks="+f.remarks;
					    	}
					    return true;
					}else
						{
						}
			};

			top.$.jBox(html, { title: "确定付款？",buttons:{"确定":"ok","关闭":true}, submit: submit });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settle/keyPurchase">key采购记录列表</a></li>
		<li><a href="${ctx}/settle/keyPurchase/form">key采购记录添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="keyPurchase" action="${ctx}/settle/keyPurchase" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>产品名称</label> 
			<form:select path="appName" id="appName" >
			<form:option value="">请选择</form:option>
				<c:forEach items="${generalInfos }" var="ga">
					<form:option value="${ga.manuKeyName}">${ga.manuKeyName}</form:option>
				</c:forEach>
			</form:select>
		<label>入库时间：</label> 
			<input id="storageDate" name="storageDate" type="text" readonly="readonly"
			maxlength="10" class="input-medium Wdate"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="${storageDate}" required="required" />
		<label>状态：</label>
		<form:select path="status">
			<form:option value="">请选择</form:option>
			<form:option value="1">已付</form:option>
			<form:option value="0">未付</form:option>
		</form:select>
				&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:dca()" class="btn btn-primary">导出</a>
			</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>入库时间</th>
				<th>产品名称</th>
				<th>key起始码</th>
				<th>key截止码</th>
				<th>数量</th>
				<th>单价</th>
				<th>状态</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyPurchase" varStatus="index">
				<tr>
					<td>${index.count}</td>
					<td>${keyPurchase.storageDate}</td>
					<td>${keyPurchase.appName}</td>
					<td>${keyPurchase.startCode}</td>
					<td>${keyPurchase.endCode}</td>
					<td>${keyPurchase.count}</td>
					<td>${keyPurchase.money}</td>
					<td>
					<c:if test="${keyPurchase.status==1}">已付</c:if>
					<c:if test="${keyPurchase.status==0}">未付</c:if>
					<c:if test="${keyPurchase.status==null}">状态位置</c:if>
					</td>
					<td>${keyPurchase.remarks}</td>
					<td><shiro:hasPermission name="settle:keyPurchase:Confirm"><c:if test="${keyPurchase.status==0}"><a href="javascript:fk(${keyPurchase.id})">确认付款</a></c:if></shiro:hasPermission></td>
				</tr>

			</c:forEach>
		</tbody>
	</table>
	<div id="bzBox" style="display: none">
		<div class="control-group" style="padding:20px;">
			<label class="control-label" style="margin-right:10px">备注：</label>
			<textarea rows="2" cols="2"  id="text" name="remarks" id="remarks"></textarea>
		</div>
	</div>
	
	<div class="pagination">${page}</div>
</body>
</html>
