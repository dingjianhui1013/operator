<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>支付信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
				/* $("#menuButton1").bind("click",showCompanyList); */
			});

	function showCompanyList() {
		var url = "${ctx}/work/workCompany"+"?_="+new Date().getTime();
		top.$.jBox.open("iframe:" + url, "选择单位名称", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
				if (v == 'ok') {

					var table = h.find("iframe")[0].contentWindow.contentTable;
					var radios = $(table).find("input[name='companyId']");
					for (var i = 0; i < radios.length; i++) {
						if (radios[i].checked == true) {
							var raidoVal = radios[i].value.split("&");
							$("#companyId").val(raidoVal[0]);
							$("#customerName").val(raidoVal[1]);
							return;
						}
					}
					top.$.jBox.tip("请选择单位名称!");
				}
			}
		});
	}
	
	
	function checkMobile(obj) { 
		var mobie = $(obj).val();
		var regu = /^[1][0-9][0-9]{9}$/; 
		var re = new RegExp(regu); 
		if (re.test(mobie)) { 
			if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
			return true; 
		} else { 
			if($("#phonepro").text()!=""){
				$(obj).focus(); //让手机文本框获得焦点 
				return false; 
			}
			$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
			/* top.$.jBox.tip("请输入正确的手机号码!");  */
			$(obj).focus(); //让手机文本框获得焦点 
			return false; 
		} 
	}

	function checkLr(){
		var serialNum = $("#serialNum").val();
		var company = $("#company").val();
		var paymentBank = $("#paymentBank").val();
		var id = "${financePaymentInfo.id}";
		if(serialNum=="" || paymentBank==""){//流水号、银行有为空值，不校验
			$("#inputForm").submit();
		}else{
			$.ajax({
				url:"${ctx}/finance/financePaymentInfo/checkLr?&_="+new Date().getTime(),
				type:'POST',
				dataType : 'json',
				data:{
					serialNum:serialNum,
					company:company,
					paymentBank:paymentBank,
					id:id
				},
				success:function(data){
					if(data.status==1){
						top.$.jBox.tip(data.msg);
					}else if(data.status == 0){

						$("#inputForm").submit();

					}
				}
			});	}
		}

	/* function validValue(obj){
		var payMethodV = $(obj).val();
		$("#bank_").hide();
		$("#account_").hide();
		if(payMethodV == 3){
			$("#bank_").show();
			$("#account_").show();
		}
		if(payMethodV == 4){
			$("#account_").show();
		}
	} */
</script>
<script type="text/javascript">
	$(document).ready(function() {
		var nameUrl = "${ctx}/finance/financePaymentInfo/companyName";
		$.getJSON(nameUrl, function(da) {
			/* alert(da); */
			$("#company").bigAutocomplete({
				data : da.list
			});
		});
	})
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/finance/financePaymentInfo/payList">支付信息列表</a></li>
		<li class="active"><a
			href="${ctx}/finance/financePaymentInfo/form?id=${financePaymentInfo.id}">支付信息<shiro:hasPermission
					name="finance:financePaymentInfo:edit">${not empty financePaymentInfo.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="finance:financePaymentInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="financePaymentInfo"
		action="${ctx}/finance/financePaymentInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		
		<div class="control-group">
			<label class="control-label">
				交易流水号:
			</label>
			<div class="controls">
				<form:input path="serialNum" htmlEscape="false" class="number" maxlength="20" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;单位名称:</label>
			<div class="controls">

				<%-- <input  id="companyId" name="companyId" class="input-medium required" 
					type="hidden" value="${financePaymentInfo.workCompany.id }"> --%>
				<%-- 	<input tabindex="-1" id="customerName" name="customerName" 
				 type="text" value="${financePaymentInfo.workCompany.companyName}"
				 maxlength="50" class="input-medium required" style="" disabled="disabled">
				  --%>

				<form:input path="company" htmlEscape="false" maxlength="50" 
					class="required" id="company" />


				<!-- 	<a tabindex="-1" id="menuButton1" href="javascript:" class="btn " style="_padding-top:6px;">
				&nbsp;<i class="icon-search"></i>&nbsp;</a> -->
				&nbsp;&nbsp;
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;付款金额(元):</label>
			<div class="controls">
			<input <c:if test="${financePaymentInfo.id!=null}">readonly="readonly" </c:if>type="text" name="paymentMoney" maxlength="12" value="${financePaymentInfo.paymentMoney}" class="number" class="required"/>
				<%-- <form:input path="paymentMoney" htmlEscape="false" maxlength="12"
					class="required" /> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;付款方式:</label>
			<div class="controls">
				<form:select path="paymentMethod" htmlEscape="false" id="paymentMethod" 
					class="required">
					<form:option value="1">现金</form:option>
					<form:option value="2">POS收款</form:option>
					<form:option value="3">银行转账</form:option>
					<form:option value="4">支付宝转账</form:option>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				付款银行:
			</label>
			<div class="controls">
				<form:input path="paymentBank" id="paymentBank" htmlEscape="false" maxlength="20" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">
				付款账号:
			</label>
			<div class="controls">
				<form:input path="paymentAccount" id="paymentAccount" htmlEscape="false" class="number" maxlength="20"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
				<form:input path="commUserName" htmlEscape="false" maxlength="11" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
				<form:input path="commMobile" id="contactPhone"  htmlEscape="false"  onblur="checkMobile(this)"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>&nbsp;付款时间:</label>
			<div class="controls">
				<input id="payDate" name="payDate" type="text"
					readonly="readonly" maxlength="10" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,maxDate:'%y-%M-%d'});"
					value="<fmt:formatDate value="${financePaymentInfo.payDate}" pattern="yyyy-MM-dd"/>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" cols="4" rows="4"
					style="resize: none;"></form:textarea>
			</div>
		</div>
		
		<%-- <div class="row-fluid">
			<div class="span6">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="3" style="font-size: 20px;">工作信息记录</th>
						</tr>
						<tr>
							
							<th>记录人</th>
							<th>受理网点</th>
							<th>记录时间</th>
						</tr>
						<c:forEach items="${workLog}" var="workLog" varStatus="status">
							<tr>
								
								<td>${workLog.createBy.name }</td>
								<td>${workLog.createBy.office.name }</td>
								<td><fmt:formatDate value="${workLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</c:forEach>
						 <tr>
							
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr> 
					</tbody>
				</table>
			</div>
		</div> --%>
		
		<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="button"
					value="保 存" onclick="checkLr();"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
