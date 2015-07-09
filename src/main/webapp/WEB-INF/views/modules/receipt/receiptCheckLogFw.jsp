<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>盘点信息管理</title>
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

	function submit_form() {
		if ($("#sta").val() == 1) {
			top.$.jBox.tip("存在尚未复位的盘点信息，请对其进行复位操作");
			return false;
		}
		if ($("#yuMoney").val() == "") {
			alert("请输入盘点后金额");
		} else {
			if (isNaN($("#yuMoney").val())) {
				top.$.jBox.tip("盘点处只能输入数字");
			} else {
				if (parseFloat($("#yuMoney").val()) != parseFloat($(
						"#beforeResidue").val())) {
					if ($("#xxsm").val() == "") {
						top.$.jBox.tip("差异说明不能为空");
					} else {
						document.createLog.submit();
					}
				} else {
					document.createLog.submit();
				}
			}
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptCheckLog/li">盘点</a></li>
		<li class="active"><a href="#">盘点信息列表</a></li>
	</ul>
	<form action="${ctx}/receipt/receiptCheckLog/checkLogFrom"
		id="searchForm" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	</form>
	<input type="hidden" id="sta" value="${status }" />
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>盘点次数</th>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所在网点</th>
				<th>上次盘点余额/元</th>
				<th>入库金额/元</th>
				<th>已开金额/元</th>
				<th>剩余金额/元</th>
				<th>盘点后金额/元</th>
				<th>差异说明</th>
				<th>盘点开始时间</th>
				<th>盘点结束时间</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>第${receiptCheckLog.times}次</td>
				<td>${receiptCheckLog.receiptDepotInfo.receiptName}</td>
				<td>${receiptCheckLog.office.areaName}</td>
				<td>${receiptCheckLog.office.name}</td>
				<td>
					<c:if test="${receiptCheckLog.receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${lastsStrings }" var="lastType"  >
								<tr>
									<td  style="border: none; text-align: right;">${lastType.receiptType.typeName }元</td>
									<td style="border: none; text-align: right;">${lastType.count }张
									</td>
								</tr>
							</c:forEach>
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${receiptCheckLog.lastMoney }</td>
							</tr>
						</table>
					</c:if>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id!=1}">
				${receiptCheckLog.lastMoney }
				</c:if>
				
				</td>
				<td>
				<c:if test="${receiptCheckLog.receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${inStrings }" var="inType"  >
								<tr>
									<td  style="border: none; text-align: right;">${inType.receiptType.typeName }元</td>
									<td style="border: none; text-align: right;">${inType.count }张
									</td>
								</tr>
							</c:forEach>
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${receiptCheckLog.beforeTotal}</td>
							</tr>
						</table>
					</c:if>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id!=1}">
					${receiptCheckLog.beforeTotal}
				</c:if>
				
				
				</td>
				<td>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${outStrings }" var="outType"  >
								<tr>
									<td  style="border: none; text-align: right;">${outType.receiptType.typeName }元</td>
									<td style="border: none; text-align: right;">${outType.count }张
									</td>
								</tr>
							</c:forEach>
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${receiptCheckLog.beforeOut}</td>
							</tr>
						</table>
					</c:if>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id!=1}">
					${receiptCheckLog.beforeOut}
				</c:if>
				
				
				
				
				
				
				</td>
				<td>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${yuStrings }" var="yuType"  >
								<tr>
									<td  style="border: none; text-align: right;">${yuType.receiptType.typeName }元</td>
									<td style="border: none; text-align: right;">${yuType.count }张
									</td>
								</tr>
							</c:forEach>
							<tr>
							<td colspan="2" style="border: none; text-align: center;">	${receiptCheckLog.beforeResidue}</td>
							</tr>
						</table>
					</c:if>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id!=1}">
						${receiptCheckLog.beforeResidue}
				</c:if>
				
				
				
				</td>
				<td>
				<c:if test="${receiptCheckLog.receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${afterStrings }" var="afterType"  >
								<tr>
									<td  style="border: none; text-align: right;">${afterType.receiptType.typeName }元</td>
									<td style="border: none; text-align: right;">${afterType.count }张
									</td>
								</tr>
							</c:forEach>
							<tr>
							<td colspan="2" style="border: none; text-align: center;">	${receiptCheckLog.afterResidue}</td>
							</tr>
						</table>
					</c:if>
				
				<c:if test="${receiptCheckLog.receiptDepotInfo.id!=1}">
					${receiptCheckLog.afterResidue}
				</c:if>
				
				
				
				
				
				
				
				
				</td>
				<td>${receiptCheckLog.fixRemark}</td>
				<td><fmt:formatDate value="${receiptCheckLog.startDate}"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td><fmt:formatDate value="${receiptCheckLog.endDate}"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<tr>
				<td colspan="12" style="text-align:center;">
					<a class="btn btn-primary" href="${ctx}/receipt/receiptCheckLog/fuwei?id=${receiptCheckLog.id}">复位</a>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
