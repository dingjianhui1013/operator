<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="decorator" content="default" />
<title>支付信息退费管理</title>
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function quitMoney(id, residueMoney) {

		var url = "${ctx}/finance/financeQuitMoney/quitMoneyWorkDealInfo?quitMoneyId="
				+ id;
		var html = "<div style='padding:30px;'>"
				+ "<span style='font-weight:bold;'>退费金额：</span>"
				+ residueMoney
				+ "</br>"
				+ "</br>"
				+ "<span style='font-weight:bold;'>退费原因：</span>变更缴费类型操作记录</div>";
		var quitReason;
		var submit = function(v, h, f) {
			if (v == "close") {
				return true;
			}
			quitReason = "变更缴费类型操作记录";
			if (v == "ok") {
				top.$.jBox.confirm("是否确认退费", '系统提示', function(v, h, f) {
					if (v == 'ok') {
						$.ajax({
							type : 'post',
							url : url + "&_=" + new Date().getTime(),
							dataType : 'json',
							data : {
								quitReason : quitReason
							},
							success : function(data) {
								if (data.status == '1') {
									window.location.reload();
									top.$.jBox.tip("退费成功");
								} else {
									top.$.jBox.tip("退费失败");
								}
							}
						});
					}
				}, {
					buttonsFocus : 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}

		};
		top.$.jBox(html, {
			title : "退费信息",
			buttons : {
				"确定" : "ok",
				"关闭" : "close"
			},
			submit : submit
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/finance/financeQuitMoney/">退费信息列表</a></li>
	</ul>

	<form id="searchForm" action="${ctx}/finance/financeQuitMoney/list"
		method="post" class="breadcrumb form-search">
		<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}" />
		<input type="hidden" id="pageSize" name="pageSize"
			value="${page.pageSize }" />
		<div>
			<label>&nbsp;&nbsp;联系人&nbsp; ：</label> <input name="commUserName"
				class="input-medium" /> <label>&nbsp;&nbsp;支付时间&nbsp; ：</label> <input
				id="payStartTime" class="input-medium Wdate" type="text"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="${payStartTime }" maxlength="20" readonly="readonly"
				name="payStartTime" />&nbsp;&nbsp;至&nbsp;&nbsp; <input
				class="input-medium Wdate" type="text"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'payStartTime\')}'});"
				value="${payEndTime }" maxlength="20" readonly="readonly"
				name="payEndTime" />
			<div style="margin-top: 8px">
				<label>退费时间：</label> <input id="quitStartTime"
					class="input-medium Wdate" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
					value="${quitStartTime }" maxlength="20" readonly="readonly"
					name="quitStartTime" />&nbsp;&nbsp;至&nbsp;&nbsp; <input
					class="input-medium Wdate" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'quitStartTime\')}'});"
					value="${quitEndTime }" maxlength="20" readonly="readonly"
					name="quitEndTime" /> &nbsp;<input id="btnSubmit"
					class="btn btn-primary" type="submit" value="查询" />
			</div>
		</div>
	</form>

	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="13">共&nbsp;${count}&nbsp;条数据</th>
			</tr>
			<tr>
				<th>付款编号</th>
				<th>付款单位名称</th>
				<th>支付金额</th>
				<th>联系人</th>
				<th>联系方式</th>
				<th>付款时间</th>
				<th>付款方式</th>
				<th>退费金额</th>
				<th>退费时间</th>
				<th>退费原因</th>
				<th>受理窗口</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list }" var="financeQuitMoney" varStatus="f">
				<tr>
					<td>${financeQuitMoney.id }</td>
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workCompany.companyName }
					</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
					${financeQuitMoney.financePaymentInfo.company }
					</c:if></td>
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workPayInfo.workTotalMoney }
					</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
							<fmt:formatNumber
								value="${financeQuitMoney.financePaymentInfo.paymentMoney}"
								type="number" />
						</c:if></td>

					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workUser.contactName }
					</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
					${financeQuitMoney.financePaymentInfo.commUserName}
					</c:if></td>
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workUser.contactEmail }
					</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
					${financeQuitMoney.financePaymentInfo.commMobile}
					</c:if></td>
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
							<fmt:formatDate
								value="${financeQuitMoney.workDealInfo.workCertInfo.signDate}"
								pattern="yyyy-MM-dd" />
						</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
							<fmt:formatDate
								value="${financeQuitMoney.financePaymentInfo.payDate}"
								pattern="yyyy-MM-dd" />
						</c:if></td>
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodPos }">POS机付款&nbsp;</c:if>
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodMoney }">现金交易&nbsp;</c:if>
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodAlipay }">支付宝交易&nbsp;</c:if>
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodGov }">政府统一采购&nbsp;</c:if>
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodContract }">合同采购&nbsp;</c:if>
							<c:if
								test="${financeQuitMoney.workDealInfo.workPayInfo.methodBank }">银行转账&nbsp;</c:if>
						</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
					${financeQuitMoney.financePaymentInfo.paymentMethodName}
					</c:if></td>
					<td><fmt:formatNumber value="${financeQuitMoney.quitMoney }"
							type="number" /></td>
					<td><fmt:formatDate value="${financeQuitMoney.quitDate }"
							pattern="yyyy-MM-dd" /></td>
					<td width="80px" style="word-break: break-all"><c:if
							test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.ression  }
					</c:if> <c:if test="${financeQuitMoney.workDealInfo ==null }">
					${financeQuitMoney.quitReason  }
					</c:if></td>





					<td>${financeQuitMoney.quitWindow }</td>
					<td><c:if test="${financeQuitMoney.status==1 }">未处理</c:if> <c:if
							test="${financeQuitMoney.status==2 }">已处理</c:if> <c:if
							test="${financeQuitMoney.status==null }">已完成</c:if></td>
					<td><c:if test="${financeQuitMoney.status==1 }">
							<a
								href="javascript:quitMoney(${financeQuitMoney.id }, ${financeQuitMoney.quitMoney})">确认退费</a>

						</c:if></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>