<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>调拨管理管理</title>
<meta name="decorator" content="default" />
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
			});

	function showBad() {
		if(isReplace){
			return;
		}
		$("#contentTable").html(badKeyTable);
		isReplace = true;
		$("#aNN").hide();
	}
	function hideBad() {
		if(!isReplace){
			return;	
		}
		$("#contentTable").html(applyKeyTable);
		isReplace = false;	
		$("#aNN").show();
	}
	function checkTime(){
		var nowDate = $("#warehouseDate").val();
	    var  d   =   new   Date(nowDate.replace(/-/g,   "/")); 
	 
	    if ( d < new Date()) {
	    	top.$.jBox.tip(" 到货日期录入有误，请重新录入!");
	    	return false;
		}
	}
	
	
	
	
</script>
<script type="text/javascript">
	var badSel;
	var applyInput;
	var applySel;
	var badKeyTable;
	var applyKeyTable;
    var isReplace = false;
	
	$(document)
			.ready(
					function() {
						badSel = $("#badKeyType").html();
						applyInput = $("#applyInput").html();
						applySel = $("#applyKeyType").html();
						badKeyTable = $("#contentTable").html();
						$("#badKeyType").remove();
						$("#badTh").remove();
						applyKeyTable = $("#contentTable").html();
						
						$("#addLine").click(function() {
							var html = "<tr><td>"+applyInput+"</td>";
							if(isReplace){
								 html += "<td>"+badSel+"</td>";	
							}
							html += "<td>"+applySel+"</td></tr>";
							$("#contentTable").append(html);
						});
						
						$("#removeLine").click(function() {
							if($("#contentTable").find("tr").length==2) return;
							$("#contentTable").find("tr:last").remove();
						});
					})
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/key/keyAllocateApply/">调拨管理</a></li>
		<li class="active"><a
			href="${ctx}/key/keyAllocateApply/form?id=${keyAllocateApply.id}">调拨申请</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="keyAllocateApply"
		action="${ctx}/key/keyAllocateApply/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>采购网点:</label>
			<div class="controls">
				<select name="officeId" id="officeId" class="required">
					<option value="${office.id}">${office.name}</option>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>采购库房:</label>
			<div class="controls">
				<select name="depotId" id="depotId" class="required">
				
				<c:if test="${depotId== null }">
					<option value="">${message}</option>
				</c:if>
					<c:forEach items="${depots }" var="depot">
						<option value="${depot.id }">${depot.depotName }</option>
					</c:forEach>
				</select>
				<%-- <span>${message}</span> --%>
				<input type="hidden" name="depotId" value="${depotId}"  >
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>申请对象:</label>
			<div class="controls">
				<input type="text"  value="${parentDepot.depotName}" disabled="disabled">
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>申请类型:</label>
			<div class="controls">
				<input type="radio" name="applyType" value="1" onclick="hideBad()"
					checked="checked" id="applyRadio">申请新key 
				<!-- 	<input type="radio"
					name="applyType" value="2" onclick="showBad()">坏key置换 -->
			</div>
		</div>
		<div class="control-group">	
			<label class="control-label"><span style="color : red">*</span>申请详情:</label>
			<div class="controls">
			<input type="button" class="btn btn-success" value="+" style="width:34px" id="addLine"/>
			<input type="button" class="btn btn-info" value="-" style="width:34px" id="removeLine"/>
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed" style="width: 60%">
					<tr>
						<th>调拨申请数量</th>
						<th id="badTh">坏key类型名称</th>
						<th>申请key类型名称</th>
					</tr>
					<tr>
						<td id="applyInput">
						<input type="text" 
						onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
						onkeyup="value=value.replace(/[^\d]/g,'')" 
						 name="applyNewNum" maxlength="9" class="required"/>
						</td>
						<td id="badKeyType">
						<select name="badKeyType"
							class="required">
								<option value="">请选择</option>
								<c:forEach items="${geneList }" var="gene">
									<option value="${gene.id }">${gene.configSupplier.supplierName }-${gene.name }</option>
								</c:forEach>
						</select>
						</td>
						<td id="applyKeyType">
						<select name="applyKeyType"
							class="required">
								<option value="">请选择</option>
								<c:forEach items="${geneList }" var="gene">
									<option value="${gene.id }">${gene.configSupplier.supplierName }-${gene.name }</option>
								</c:forEach>
						</select>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">购买紧急程度:</label>
			<div class="controls">
				<input type="radio" name="leavel" value="1" checked="checked">
				一般 <input type="radio" name="leavel" value="2"> 紧急
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color : red">*</span>要求到货日期:</label>
			<div class="controls">
				<input id="warehouseDate" name="warehouseDate" type="text"
					readonly="readonly" maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
					value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="key:keyAllocateApply:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return checkTime()"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
