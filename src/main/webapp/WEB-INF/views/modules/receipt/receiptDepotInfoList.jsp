<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>发票信息管理</title>
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
		
		function alarmValue(depotId){
			var url = "${ctx}/receipt/receiptDepotInfo/alarmForm?depotId="+depotId;
			top.$.jBox.open("iframe:"+url, "预警值设置",600,60, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						var table = h.find("iframe")[0].contentWindow.assessmentForm;
						//console.log(table);
						var url = $(table).attr('action')+"?_="+new Date().getTime();
						var data=$(table).serialize();
						$.ajax({
							type: 'POST',
							url: url,
							data: data,
							dataType : 'json',
							success:function(data){
								if(data.status=="1"){
									top.$.jBox.tip("保存成功!");
									$("#searchForm").submit();
								}else{
									top.$.jBox.tip("保存失败!");
									$("#searchForm").submit();
								}
							}
						});
					}
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/receipt/receiptDepotInfo/list">库房管理</a></li>
		<shiro:hasPermission name="receipt:receiptDepotInfo:addKF">
			<li><a href="${ctx}/receipt/receiptDepotInfo/form">添加库房</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptDepotInfo" action="${ctx}/receipt/receiptDepotInfo/" method="post" class="breadcrumb form-search">
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
				<th>所属网点</th>
				
				<th>管理网点</th>
				
				<th>发票余量/元</th>
				<th>联系人</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptDepotInfo">
			<tr>
				<td><a href="${ctx}/receipt/receiptDepotInfo/updateFrom?id=${receiptDepotInfo.id}">${receiptDepotInfo.receiptName}</a></td>
				<td>${receiptDepotInfo.area.name}</td>
				<td>${receiptDepotInfo.office.name}</td>
				
				<td>${receiptDepotInfo.parent.office.name}</td>
				
				
				<td>
				<fmt:parseNumber parseLocale="#0.00">${receiptDepotInfo.receiptResidue}</fmt:parseNumber>
				</td>
				<td>${receiptDepotInfo.receiptCommUser}</td>
				<td>${receiptDepotInfo.receiptCommMobile}</td>
				<td>
    				<!-- <a href="javascript:alarmValue(${receiptDepotInfo.id})">入库</a> -->
    				<shiro:hasPermission name="receipt:receiptDepotInfo:view">
    					<a href="${ctx}/receipt/receiptDepotInfo/rukuList?id=${receiptDepotInfo.id}">入库管理</a>
    				</shiro:hasPermission>
    				<a href="${ctx}/receipt/receiptDepotInfo/chukuList?id=${receiptDepotInfo.id}">出库管理</a>
    				<a href="${ctx}/receipt/receiptDepotInfo/updateFrom?id=${receiptDepotInfo.id}">编辑</a>
    				<c:if test="${receiptDepotInfo.id!=zkid}">
						<a href="${ctx}/receipt/receiptDepotInfo/delete?id=${receiptDepotInfo.id}" onclick="return confirmx('确认要删除该发票信息吗？', this.href)">删除</a>
    				</c:if>
					<a href="javascript:void(0)" onclick="alarmValue(${receiptDepotInfo.id})">预警值设置</a>
					<c:if test="${isSysadmin == true}"><a
							href="${ctx}/receipt/receiptDepotInfo/assign?id=${receiptDepotInfo.id}">分配下级库房</a></c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
