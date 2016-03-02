<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>代理商管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	
		$("#btnExport").click(function(){
			top.$.jBox.confirm("确认要导出结算数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx }/settle/agentSettle/exportData");
					$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
		$("#btnSubmit").click(function(){
			$("#searchForm").validate({
				submitHandler : function(form) {
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
			$("#searchForm").attr("action","${ctx}/settle/agentSettle/list");
			$("#searchForm").submit();
			
		});
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}

	function addOffice() {
		var areaId = $("#areaId").prop('value');
		var url = "${ctx}/sys/office/addOffices?areaId=";
		$.getJSON(url + areaId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			//console.log(data);
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				//console.log(idx);
				//console.log(ele);
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</option>"
			});
			//alert(html);
			$("#officeId").html(html);
		});
	}
	function products() {
		var congifApplyId = $("#congifApplyId").prop('value');
		var url = "${ctx}/settle/agentSettle/getProductName?applyId=";
		$.getJSON(url + congifApplyId+"&_="+new Date().getTime(), function(data) {
			var html = "";
			html += "<option value=\""+""+"\">请选择</ooption>";
			$.each(data, function(idx, ele) {
				html += "<option value=\""+ele.id+"\">" + ele.name
						+ "</option>"
			});
			//alert(html);
			$("#productId").html(html);
		});
	}

	function showTable_f(data) {
		$("#showTable_officeId").attr("value", $("#officeId").val());
		$("#showTable_agentId").attr("value", data);
		$("#showTable_startTime").attr("value", $("#startTime").val());
		$("#showTable_endTime").attr("value", $("#endTime").val());
		document.showTable.submit();
	}
	function showDealInfo(appId,productId,dealInfoType, year){
		var url = "${ctx}/settle/agentSettle/showDealInfo?appId="+appId+"&productId="+productId+"&dealInfoType="+dealInfoType+"&startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val()+"&agentName="+$("#agent").val()+"&year=" + year +"&type=TG";
		url = encodeURI(url);
		url = encodeURI(url);
		top.$.jBox.open("iframe:" + url, "业务明细", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
	}
	function zk() {
		var zk = $(zkl).val();
		$("#mes").html("");
		if (zk != null && zk != 0) {
			if (zk > 100) {
				$("#mes").html("请输入1~100之间的整数!");
			} else {
				$("#accountsMoney").html($("#total").val() * zk/100);
			}

		} else {

			$("#mes").html("请输入1~100之间的整数!");
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settle/agentSettle/list">市场推广</a></li>
		<li><a href="${ctx}/settle/agentSettle/showT">劳务关系</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="configCommercialAgent"
		action="${ctx}/settle/agentSettle/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
	<div class="control-group">
			<label>选择区域 ：</label> <select name="areaId" id="areaId"
				onchange="addOffice()">
				<option value="">请选择</option>
				<c:forEach items="${offsList}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
				</c:forEach>
			</select> <label>选择网点 ：</label> <select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="office">
					<option value="${office.id}"
						<c:if test="${office.id==officeId}">
						selected="selected"
						</c:if>>${office.name}</option>
				</c:forEach>
			</select> <label>代理商名称 ：</label><select name="agentName" required="required">
				<c:forEach items="${ConfigCommercialAgents }" var="agent">
					<option value="${agent }" <c:if test="${agent==agentName }">selected="selected"</c:if>>${agent }</option>
				</c:forEach>
			</select>
		</div>
		<div  class="control-group" >
	    <label>应用名称 ：</label> <select name="congifApplyId" id="congifApplyId" onchange="products()">
				<option value="">请选择</option>
				<c:forEach items="${configApps}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==congifApplyId}">
						selected="selected"
						</c:if>>${app.appName}</option>
				</c:forEach>
		 </select>  
		   <label>产品名称 ：</label> <select name="productId" id="productId" >
				<option value="">请选择</option>
				<c:forEach items="${configProducts}" var="pro">
					<option value="${pro.id}"
						<c:if test="${pro.id==productId}">
						selected="selected"
						</c:if>>
					   ${productType[pro.productName]}${workDealInfo.configProduct.productLabel==0? "(通用)":"(专用)"}
						<%-- <c:if test='${pro.productName=="1"}'>企业证书</c:if>
						<c:if test='${pro.productName=="2"}'>个人证书(企业)</c:if> 
						<c:if test='${pro.productName=="3"}'>机构证书</c:if> 
						<c:if test='${pro.productName=="4"}'>可信移动设备</c:if> 
						<c:if test='${pro.productName=="5"}'>电子签章</c:if>
						<c:if test='${pro.productName=="5"}'>个人证书(机构)</c:if>  --%>
						</option>
				</c:forEach>
			</select> 
		 <label>有&nbsp;&nbsp;&nbsp;效&nbsp;&nbsp;&nbsp;期 ：</label> 
		 <input name="oneYear" type="checkbox" <c:if test="${oneYear}">checked</c:if> value="1">一年&nbsp;&nbsp;&nbsp;
		 <input name="twoYear" type="checkbox" <c:if test="${twoYear}">checked</c:if> value="1">二年&nbsp;&nbsp;&nbsp;
		 <input name="threeYear" type="checkbox" <c:if test="${threeYear}">checked</c:if> value="1">三年&nbsp;&nbsp;&nbsp;
		 <input name="fourYear" type="checkbox" <c:if test="${fourYear}">checked</c:if> value="1">四年&nbsp;&nbsp;&nbsp;
		 <input name="fiveYear" type="checkbox" <c:if test="${fiveYear}">checked</c:if> value="1">五年&nbsp;&nbsp;&nbsp;
		</div>
		<div class="control-group">
			<label>统计时间 ：</label> <input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="input-medium Wdate" required="required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp; <input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		</div>
	         <div style="width: 800px">                 
			<!-- <div style="width: 100px"> --><label>缴费类型：</label><!-- </div>	 -->	    
	<!-- 		 <div style="width: 700px; padding-left: 100px;"> -->
				<input name="chargeMethodPos" <c:if test="${chargeMethodPos}">checked</c:if> type="checkbox" value="1">pos&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodMoney" <c:if test="${chargeMethodMoney}">checked</c:if> type="checkbox" value="1">现金缴费&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodBank" <c:if test="${chargeMethodBank}">checked</c:if> type="checkbox" value="1">银行转账	&nbsp;&nbsp;&nbsp;
				<%-- <input name="chargeMethodAlipay" <c:if test="${configChargeAgent.chargeMethodAlipay}">checked</c:if> type="checkbox" value="1">支付宝&nbsp;&nbsp;&nbsp; --%>
				<input name="chargeMethodGov" <c:if test="${chargeMethodGov}">checked</c:if> type="checkbox" value="1">政府统一采购&nbsp;&nbsp;&nbsp;
				<input name="chargeMethodContract" <c:if test="${chargeMethodContract}">checked</c:if> type="checkbox" value="1">合同采购&nbsp;&nbsp;&nbsp;
			  <!--  </div>	 --> 
		  	<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" />
				&nbsp; <input id="btnExport" class="btn btn-primary"
				type="button" value="导出" />
		</div>
	
		
	</form:form>
	<tags:message content="${message}" />	
	<div class="form-horizontal"
		<c:if test="${empty listSum }">style="display: none"</c:if>>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
				<c:if test='${colspan==0 }'>
				<c:set var="colsTitle" value="6"></c:set>
				<c:set var="colsTotl" value="5"></c:set>
				</c:if>
				<c:if test='${colspan!=0 }'>
				<c:set var="colsTitle" value='${(colspan-1)*2+6}'></c:set>
				<c:set var="colsTotl" value='${(colspan-1)*2+5}'></c:set>
				</c:if>
			<th colspan='${colsTitle}'>${title }</th>
				</tr>
			</thead>
			<tr>
				<th rowspan="2">应用名称</th>
				<th rowspan="2">产品名称</th>
				<th colspan='${colspan}'>新增数量及价格</th>
				<th rowspan="2">实收(元)</th>
				<th colspan='${colspan}'>更新数量及价格</th>
				<th rowspan="2">实收(元)</th>
			</tr>
			<c:set var="total" value="0" />
			<c:set var="addOneSum" value="0" />
			<c:set var="addTwoSum" value="0" />
			<c:set var="addThreeSum" value="0" />
			<c:set var="addFourSum" value="0" />
			<c:set var="addFiveSum" value="0" />
			<c:set var="addTotal" value="0" />
			<c:set var="updateOneSum" value="0" />
			<c:set var="updateTwoSum" value="0" />
			<c:set var="updateThreeSum" value="0" />
			<c:set var="updateFourSum" value="0" />
			<c:set var="updateFiveSum" value="0" />
			<c:set var="updateTotal" value="0" />
			
			<tr>
				<c:if test='${oneYear==true}'><th>一年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${twoYear==true}'><th>二年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${threeYear==true}'><th>三年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${fourYear==true}'><th>四年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${fiveYear==true}'><th>五年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				
				<c:if test='${oneYear==true}'><th>一年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${twoYear==true}'><th>二年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${threeYear==true}'><th>三年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${fourYear==true}'><th>四年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
				<c:if test='${fiveYear==true}'><th>五年<br/>数量</th>
				<th>单价<br/>(元)</th></c:if>
			</tr>
			<c:forEach items="${listSum }" var="sum">
				<tr>
					<td width="120" style="word-break: break-all">${sum.appName }</td>
					<td>${sum.productName }(${sum.productLabel })</td>
					<c:if test='${sum.oneSum!=null }'><td><c:if test='${sum.oneSum>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },0,1 )">${sum.oneSum }</a></c:if><c:if test='${sum.oneSum==0 }'>${sum.oneSum }</c:if></td>
						<td>${sum.oneMoney }</td></c:if>
					<c:if test='${sum.twoSum!=null }'><td><c:if test='${sum.twoSum>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },0,2)">${sum.twoSum }</a></c:if><c:if test='${sum.twoSum==0 }'>${sum.twoSum }</c:if></td>
					<td>${sum.twoMoney }</td></c:if>
					<c:if test='${sum.threeSum!=null }'><td><c:if test='${sum.threeSum>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },0,3)">${sum.threeSum }</a></c:if><c:if test='${sum.threeSum==0 }'>${sum.threeSum }</c:if></td>
					<td>${sum.threeMoney }</td></c:if>
					<c:if test='${sum.fourSum!=null }'><td><c:if test='${sum.fourSum>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },0,4)">${sum.fourSum }</a></c:if><c:if test='${sum.fourSum==0 }'>${sum.fourSum }</c:if></td>
					<td>${sum.fourMoney }</td></c:if>
					<c:if test='${sum.fiveSum!=null }'><td><c:if test='${sum.fiveSum>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },0,5)">${sum.fiveSum }</a></c:if><c:if test='${sum.fiveSum==0 }'>${sum.fiveSum }</c:if></td>
					<td>${sum.fiveMoney }</td></c:if>
					<td>${sum.subTotal }</td>
					<c:if test='${sum.oneSum!=null }'><td><c:if test='${sum.oneSum1>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },1,1)">${sum.oneSum1 }</a></c:if><c:if test='${sum.oneSum1==0 }'>${sum.oneSum1 }</c:if></td>
					<td>${sum.oneMoney1 }</td></c:if>
					<c:if test='${sum.twoSum!=null }'><td><c:if test='${sum.twoSum1>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },1,2)">${sum.twoSum1 }</a></c:if><c:if test='${sum.twoSum1==0 }'>${sum.twoSum1 }</c:if></td>
					<td>${sum.twoMoney1 }</td></c:if>
					<c:if test='${sum.threeSum!=null }'><td><c:if test='${sum.threeSum1>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },1,3)">${sum.threeSum1 }</a></c:if><c:if test='${sum.threeSum1==0 }'>${sum.threeSum1 }</c:if></td>
					<td>${sum.threeMoney1 }</td></c:if>
					<c:if test='${sum.fourSum!=null }'><td><c:if test='${sum.fourSum1>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },1,4)">${sum.fourSum1 }</a></c:if><c:if test='${sum.fourSum1==0 }'>${sum.fourSum1 }</c:if></td>
					<td>${sum.fourMoney1 }</td></c:if>
					<c:if test='${sum.fiveSum!=null }'><td><c:if test='${sum.fiveSum1>0 }'><a
						href="javascript:showDealInfo(${sum.appId },${sum.productId },1,4)">${sum.fiveSum1 }</a></c:if><c:if test='${sum.fiveSum1==0 }'>${sum.fiveSum1 }</c:if></td>
					<td>${sum.fiveMoney1 }</td></c:if>
					<td>${sum.subTotal1 }</td>
				</tr>
				<c:set var="total" value="${total+sum.subTotal+sum.subTotal1 }"></c:set>
				<c:set var="addOneSum" value="${addOneSum+sum.oneSum }"></c:set>
				<c:set var="addTwoSum" value="${addTwoSum+sum.twoSum }" />
				<c:set var="addThreeSum" value="${addThreeSum+sum.threeSum }" />
				<c:set var="addFourSum" value="${addFourSum+sum.fourSum }" />
				<c:set var="addFiveSum" value="${addFiveSum+sum.fiveSum }" />
				<c:set var="addTotal" value="${addTotal+sum.subTotal }" />
				<c:set var="updateOneSum" value="${updateOneSum+sum.oneSum1 }" />
				<c:set var="updateTwoSum" value="${updateTwoSum+sum.twoSum1 }" />
				<c:set var="updateThreeSum" value="${updateThreeSum+sum.threeSum1 }" />
				<c:set var="updateFourSum" value="${updateFourSum+sum.fourSum1 }" />
				<c:set var="updateFiveSum" value="${updateFSum+sum.fiveSum1 }" />
				<c:set var="updateTotal" value="${updateTotal+sum.subTotal1}" />
			</c:forEach>
			<tr>
				<th>合计</th>
				 <td></td>
				<c:if test='${oneYear==true}'><td>${addOneSum }</td>
				<td></td></c:if>
				<c:if test='${twoYear==true}'><td>${addTwoSum }</td>
				<td></td></c:if>
				<c:if test='${threeYear==true}'><td>${addThreeSum }</td>
				<td></td></c:if>
				<c:if test='${fourYear==true}'><td>${addFourSum }</td>
				<td></td></c:if>
				<c:if test='${fiveYear==true}'><td>${addFiveSum }</td>
			    <td></td> </c:if>			
			    <td>${addTotal }</td>
				<c:if test='${oneYear==true}'><td>${updateOneSum }</td>
				<td></td></c:if>
				<c:if test='${twoYear==true }'><td>${updateTwoSum }</td>
				<td></td></c:if>
				<c:if test='${threeYear==true }'><td>${updateThreeSum }</td>
				<td></td></c:if>
				<c:if test='${fourYear==true}'><td>${updateFourSum }</td>
				<td></td></c:if>
				<c:if test='${fiveYear==true }'><td>${updateFiveSum }</td>
				<td></td></c:if>
				<td>${updateTotal }</td>
			</tr>
			<tr>
				<th>总计</th>
				<td colspan="${colsTitle-1}" align="center">${total }<input type="hidden" value="${total }" id="total">&nbsp;元</td>
			</tr>
			<tr>
				<th>折扣率</th>
				<td colspan='${colsTotl}'><input type="text"
					onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
					onkeyup="value=value.replace(/[^\d]/g,'') " maxlength="3" id="zkl">%&nbsp;&nbsp;<input
					class="btn btn-primary" type="button" onclick="zk()" value="确认" />&nbsp;&nbsp;<label
					id="mes" style="color: red;"></label></td>
			</tr>
			<tr>
				<th>应付款</th>
				<td colspan='${colsTotl}' ><label  id="accountsMoney" >${total }</label>&nbsp;元</td>
			</tr>
		</table>
		<input type="hidden" value="${startTime }" id="startDate">
		<input type="hidden" value="${endTime }" id="endDate">
		<input type="hidden" value="${agentName }" id="agent">
	</div>
</body>
</html>
