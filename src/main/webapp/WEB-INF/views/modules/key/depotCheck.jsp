<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>库存查看</title>
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/">库存信息查看</a></li>

	</ul>

	<tags:message content="${message}" />
	仓库信息
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>库存数量</th>
				<th>已使用量</th>
				<th>库存余量</th>
            </tr>
		</thead>
		<tbody>
			
				<tr>
					<td>${keyUsbKeyDepot.depotName}</td>
					<td>${keyUsbKeyDepot.office.parent.name}</td>
					<td></td>
					<td></td>
					<td></td>
					
				</tr>
			
		</tbody>
	</table>
	
	仓库下Key的信息
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>厂商名称</th>
				<th>Key类型名称</th>
				<th>入库数量</th>
				<th>出库用量</th>
				<th>余量</th>
				<th>盘点后的余量</th>
				<th>差异说明</th>
            </tr>
		</thead>
		<tbody>
			
				<tr>
					<td>${keyManufacturer.name}</td>
					<td><c:forEach items="${keyUsbKeyDepot.keyDepotGeneralStatisticsList}" var="statis">
					        	${statis.keyGeneralInfo.name}
								</br>
					</c:forEach>		</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					
				</tr>
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
