<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function showTable_f(){
			document.showTable.submit();
		}
	</script>
<script type="text/javascript">
function zk(obj){
	var zk = $(obj).val();
	$("#accountsMoney").html($("#total").val()*zk);
}
function showDealInfo(appId,productId,dealInfoType,year,agentId){
		var url = "${ctx}/settle/agentSettle/showDealInfo?appId="+appId+"&productId="+productId+"&dealInfoType="+dealInfoType+"&year="+year+"&agentId="+agentId;
		top.$.jBox.open("iframe:" + url, "业务明细", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">市场推广</a></li>
		<li ><a href="javascript:showTable_f()">劳务关系</a></li>
	</ul><br/>
	<div class="form-horizontal">
				
		<table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
					<th colspan="16">${title }</th>
				</tr>
			</thead>
			<tr>
				<th rowspan="2">应用名称</th>
				<th rowspan="2">产品名称</th>
				<th colspan="6">新增数量及价格</th>
				<th rowspan="2">小计</th>
				<th colspan="6">更新数量及价格</th>
				<th rowspan="2">小计</th>
			</tr>
			<tr>
				<th>一年数量</th>
				<th>单价</th>
				<th>二年数量</th>
				<th>单价</th>
				<th>四年数量</th>
				<th>单价</th>
				<th>一年数量</th>
				<th>单价</th>
				<th>二年数量</th>
				<th>单价</th>
				<th>四年数量</th>
				<th>单价</th>
			</tr>
			<c:set var="total" value="0"/>
			<c:set var="addOneSum" value="0"/>
			<c:set var="addTwoSum" value="0"/>
			<c:set var="addFourSum" value="0"/>
			<c:set var="addTotal" value="0"/>
			<c:set var="updateOneSum" value="0"/>
			<c:set var="updateTwoSum" value="0"/>
			<c:set var="updateFourSum" value="0"/>
			<c:set var="updateTotal" value="0"/>
			<c:forEach items="${listSum }" var="sum">
			<tr>
				<td>${sum.appName }</td>
				<td>${sum.productName }</td>
				<td><a href="<c:if test='${sum.oneSum>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },0,1,${id })</c:if>">${sum.oneSum }</a></td>
				<td>${sum.oneMoney }</td>
				<td><a href="<c:if test='${sum.twoSum>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },0,2,${id })</c:if>">${sum.twoSum }</a></td>
				<td>${sum.twoMoney }</td>
				<td><a href="<c:if test='${sum.fourSum>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },0,4,${id })</c:if>">${sum.fourSum }</a></td>
				<td>${sum.fourMoney }</td>
				<td>${sum.subTotal }</td>
				<td><a href="<c:if test='${sum.oneSum1>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },1,1,${id })</c:if>">${sum.oneSum1 }</a></td>
				<td>${sum.oneMoney1 }</td>
				<td><a href="<c:if test='${sum.twoSum1>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },1,2,${id })</c:if>">${sum.twoSum1 }</a></td>
				<td>${sum.twoMoney1 }</td>
				<td><a href="<c:if test='${sum.fourSum1>0 }'>javascript:showDealInfo(${sum.appId },${sum.productId },1,4,${id })</c:if>">${sum.fourSum1 }</a></td>
				<td>${sum.fourMoney1 }</td>
				<td>${sum.subTotal1 }</td>
			</tr>
			<c:set var="total" value="${total+sum.subTotal+sum.subTotal1 }"></c:set>
			<c:set var="addOneSum" value="${addOneSum+sum.oneSum }"></c:set>
			<c:set var="addTwoSum" value="${addTwoSum+sum.twoSum }"/>
			<c:set var="addFourSum" value="${addFourSum+sum.FourSum }"/>
			<c:set var="addTotal" value="${addTotal+sum.subTotal }"/>
			<c:set var="updateOneSum" value="${updateOneSum+sum.oneSum1 }"/>
			<c:set var="updateTwoSum" value="${updateTwoSum+sum.twoSum1 }"/>
			<c:set var="updateFourSum" value="${updateFourSum+sum.fourSum1 }"/>
			<c:set var="updateTotal" value="${updateTotal+sum.subTotal1 }"/>
			</c:forEach>
			<tr>
				<th>合计</th>
				<td></td>
				<td>${addOneSum }</td>
				<td></td>
				<td>${addTwoSum }</td>
				<td></td>
				<td>${addFourSum }</td>
				<td></td>
				<td>${addTotal }</td>
				<td>${updateOneSum }</td>
				<td></td>
				<td>${updateTwoSum }</td>
				<td></td>
				<td>${updateFourSum }</td>
				<td></td>
				<td>${updateTotal }</td>
			</tr>
			<tr>
				<th>总计</th>
				<td colspan="15" align="center">${total }<input type="hidden" value="${total }" id="total"></td>
			</tr>
			<tr>
				<th>折扣率</th>
				<td colspan="15"><input type="text" onchange="zk(this)"></td>
			</tr>
			<tr>
				<th>应付款</th>
				<td colspan="15" id="accountsMoney">${total }</td>
			</tr>
		</table>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
	<form action="${ctx}/settle/agentSettle/showT" name ="showTable" id = "showTable">
		<input id="showTable_officeId" type = "hidden" name = "officeId" value="${officeId }" />
		<input id="showTable_agentId" type = "hidden" name = "id" value = "${id }" />
		<input id="showTable_startTime" type = "hidden" name = "startDate" value = "${startDate }" />
		<input id="showTable_endTime" type = "hidden" name = "endDate" value = "${endDate }" />
	</form>
</body>
</html>
