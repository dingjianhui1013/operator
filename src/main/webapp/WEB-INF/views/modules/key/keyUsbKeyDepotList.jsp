<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>key入库信息管理</title>
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
	
	function alarmValue(depotId){
		var url = "${ctx}/key/keyDepotGeneralStatistics/alarmForm?depotId="+depotId+"&_="+new Date().getTime();
		top.$.jBox.open("iframe:"+url, "预警值设置", 600, 300, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						var table = h.find("iframe")[0].contentWindow.assessmentForm;
						//console.log(table);
						var url = $(table).attr('action')+"?_="+new Date().getTime();
						var data=$(table).serialize();
						var alarm = $(table).find("input[name='alarm']").val();
						if (alarm == null) {
							
						} else if (alarm == ""){
							top.$.jBox.tip("预警值不能为空！");
						} else if (alarm >= 0){
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
						} else if (alarm < 0){
							top.$.jBox.tip("预警值不能为负数！");
						}
					}
				}
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/">库存管理</a></li>
		<shiro:hasPermission name="key:keyUsbKeyDepot:edit">
			<li><a href="${ctx}/key/keyUsbKeyDepot/form">添加库房</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="keyUsbKeyDepot"
		action="${ctx}/key/keyUsbKeyDepot/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>库房名称 ：</label>
		<form:input path="depotName" htmlEscape="false" maxlength="50"
			class="input-medium" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所属网点</th>
				<th>库房余量</th>
				<th>key类型名称</th>
				<th>key类型标识</th>
				<th>key类型余量</th>
				<th>联系人</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="keyUsbKeyDepot">
				<tr>
					<td><a
						href="${ctx}/key/keyUsbKeyDepot/form?id=${keyUsbKeyDepot.id}">${keyUsbKeyDepot.depotName}</a></td>
					<td>${keyUsbKeyDepot.office.parent.name}</td>
					<td>${keyUsbKeyDepot.office.name}</td>
					<td>${keyUsbKeyDepot.inCount}</td>
					<td>
					<c:forEach items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}" var="statis">
					        	${statis.keyGeneralInfo.name}
								</br>
					</c:forEach>		
					</td>
					<td>
						<c:forEach items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}" var="statis">
						        	${statis.keyGeneralInfo.model}
									</br>
						</c:forEach>		
					</td>
					<td>
						<c:forEach items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}" var="statis">
						        	${statis.inCount}
									</br>
						</c:forEach>
					</td>
					<td>${keyUsbKeyDepot.linkmanName}</td>
					<td>${keyUsbKeyDepot.linkmanMobilePhone}</td>
					
						<td>
						<a
							href="${ctx}/key/keyUsbKey/list?depotId=${keyUsbKeyDepot.id}">入库管理</a>
						<a
							href="${ctx}/key/keyUsbKeyInvoice/list?depotId=${keyUsbKeyDepot.id}">出库管理</a>
					<shiro:hasPermission name="key:keyUsbKeyDepot:edit">
						<a
							href="${ctx}/key/keyUsbKeyDepot/form?id=${keyUsbKeyDepot.id}">编辑</a>
							
							<c:if test="${keyUsbKeyDepot.canDel()}">
							<a
							href="${ctx}/key/keyUsbKeyDepot/delete?id=${keyUsbKeyDepot.id}"
							onclick="return confirmx('确认要删除该库房信息吗？', this.href)">删除</a>
							</c:if>
					</shiro:hasPermission>
						<a
							href="javascript:void(0)" onclick="alarmValue(${keyUsbKeyDepot.id})">预警值设置</a>
							</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
