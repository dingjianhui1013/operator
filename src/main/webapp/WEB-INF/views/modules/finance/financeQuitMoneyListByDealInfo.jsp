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

	function quitMoney(id , residueMoney) {
		var ression = document.getElementById(id).value;
		var url = "${ctx}/finance/financeQuitMoney/quitMoneyWorkDealInfo?quitMoneyId="
				+ id;
		var html = "<div style='padding:30px;'>"
				+ "<span style='font-weight:bold;'>退费金额：</span>"
				+ residueMoney
				+ "</br>"
				+ "</br>"
				+ "<span style='font-weight:bold;'>退费原因：</span>"+ression+"</div>";
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
		<li class="active"><a href="${ctx}/finance/financeQuitMoney/dealQuitList">业务退费</a></li>
	</ul>

	<form:form id="searchForm"
	    action="${ctx}/finance/financeQuitMoney/dealQuitList"
		method="post" class="breadcrumb form-search">
		<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}" />
		<input type="hidden" id="pageSize" name="pageSize"
			value="${page.pageSize }" />
		<div>
			<label>&nbsp;&nbsp;单位名称&nbsp; ：</label>  
				<input name="companyName" value="${companyName }" 
			 class="input-medium" />
			
			<label>经办人 ：</label>
			<input name="contactName" value="${contactName }"
			 class="input-medium" />

			
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
	</form:form>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="13">共&nbsp;${count}&nbsp;条数据</th>
			</tr>
			<tr>
				<th>业务编号</th>
				<th>受理网点</th>
				<th>应用名称</th>
				<th>单位名称</th>
				<th>经办人</th>
				<th>经办人号码</th>
				
				<th>退费金额</th>
				<th>退费时间</th>
				<th>退费原因</th>
			
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list }" var="financeQuitMoney" varStatus="f">
				<tr>
					<td>${financeQuitMoney.workDealInfo.svn }</td>
					
					<td>${financeQuitMoney.quitWindow }</td>
					
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.configApp.appName }
					</c:if> </td>
									
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workCompany.companyName }
					</c:if></td>

					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workUserHis.contactName }
					</c:if></td>
					
					
					<td><c:if test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.workDealInfo.workUserHis.contactPhone }
					</c:if></td>
					
					<td><fmt:formatNumber value="${financeQuitMoney.quitMoney }"
							type="number" /></td>
							
							
					<td><fmt:formatDate value="${financeQuitMoney.quitDate }"
							pattern="yyyy-MM-dd" /></td>
							
					<td width="80px" style="word-break: break-all"><c:if
							test="${financeQuitMoney.workDealInfo !=null }">
					${financeQuitMoney.ression  }
					</c:if></td>

	
					<td><c:if test="${financeQuitMoney.status==1 }">未处理</c:if> <c:if
							test="${financeQuitMoney.status==2 }">已处理</c:if> <c:if
							test="${financeQuitMoney.status==null }">已完成</c:if></td>
					<td><c:if test="${financeQuitMoney.status==1 }">
							<a
								href="javascript:quitMoney(${financeQuitMoney.id }, ${financeQuitMoney.workDealInfo.workPayInfo.workTotalMoney})">确认退费</a>
							
							<input type="hidden" value="${financeQuitMoney.quitReason }" id="${financeQuitMoney.id }" />
						</c:if>
						
					
						
						</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>