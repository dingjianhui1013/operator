<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>支付信息管理</title>
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
	
	
	function addAttach() {
		if($("#fileName").val() == ""){
			top.$.jBox.tip("导入文件格式有误，导入文件应为Excel文件，请确认");
        	return false;
        }
        if($("#fileName").val().indexOf('.xls')<0) {
        	top.$.jBox.tip("导入文件格式有误，导入文件应为Excel文件，请确认");
            return false;
        }
		var options = {
			type : 'post',
			dataType : 'json',
			success : function(data) {
				//console.log(data);
				if(data.status=='1'){
					top.$.jBox.tip("上传成功");
					  setTimeout(function (){
	            		    //something you want delayed
	            		    	$("#searchForm").submit();
	            		//	window.location.reload();
	            		   }, 3000); // how long do you want the delay to be? 
	            
				}else if(data.status=='-1'){
					top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else{
					top.$.jBox.tip("上传失败："+data.errorMsg);
					//$("#searchForm").submit();
				}
			}
		};
		$('#materialImport').ajaxSubmit(options);
	}
	
	function quitMoney(id, residueMoney){
		var url = "${ctx}/finance/financeQuitMoney/quitMoney?quitMoneyId=" + id;
		var html = "<div style='padding:30px;'>" 
					+ "<span style='font-weight:bold;'>付款编号：</span>" + id + "</br>" + "</br>"
					+ "<span style='font-weight:bold;'>剩余金额：</span>" + residueMoney + "</br>" + "</br>"
					+ "<span style='font-weight:bold;'>退费原因：</span><input type='text' id='quitReason' name='quitReason' /></div>";
		var quitReason;
		var submit = function(v, h, f){
			if(v=="close"){
				return true;
			}
			if(f.quitReason == ""){
				top.$.jBox.tip("请填写退费原因！");
				return false;
			}
			quitReason = f.quitReason;
			if(v == "ok"){
				top.$.jBox.confirm("是否确认退费",'系统提示',function(v,h,f){
					if(v=='ok'){
						$.ajax({
							type : 'post',
							url : url + "&_=" + new Date().getTime(),
							dataType : 'json',
							data : {quitReason:quitReason},
							success : function(data){
								if(data.status == '1'){
									window.location.reload();
									top.$.jBox.tip("退费成功");
								}else{
									top.$.jBox.tip("退费失败");
								}
							}
						});
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}


		};
		top.$.jBox(html, {title : "退费管理", buttons : {"确定" : "ok", "关闭" : "close"}, submit : submit});
	}
	
	function deleteDate(id){
		var url = "${ctx}/finance/financePaymentInfo/delete?id=" + id;
		top.$.jBox.confirm("确认删除该信息吗？",'系统提示',function(v,h,f){
			if(v=='ok'){
				location = url;
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');

	}
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/finance/financePaymentInfo/payList">支付信息列表</a></li>
		<shiro:hasPermission name="finance:financePaymentInfo:edit">
			<li><a href="${ctx}/finance/financePaymentInfo/form">支付信息添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="financePaymentInfo"
		action="${ctx}/finance/financePaymentInfo/payList" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
		<label>付款用户名称 ：</label>
		<form:input path="company" htmlEscape="false"
			maxlength="50" class="input-medium" />
		<label>联系人 ：</label>
		<form:input path="commUserName" htmlEscape="false"
			maxlength="50" class="input-medium" />
		<label>付款时间 ：</label>
		<input id="startTime" class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								value="${startTime }"
								maxlength="20" readonly="readonly" name="startTime" />至
		<input class="input-medium Wdate" type="text" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
								value="${endTime }"
								maxlength="20" readonly="readonly" name="endTime" />
		</div>
		<div style="margin-top: 8px">
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			<a id="btnImport" data-toggle="modal" href="#declareDiv" class="btn btn-primary">批量导入</a>
			<a target="_blank" href="${ctx}/enroll/downloadTemplate?fileName=finance.xlsx" class="btn btn-primary">模板下载</a>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>付款编号</th>
				<th style="width: 100px">付款单位名称</th>
				<th>支付金额</th>
				<th>剩余金额</th>
				<th>联系人</th>
				<th>联系方式</th>
				<th>付款时间</th>
				<th>付款方式</th>
				<th>绑定状态</th>
				<th>记录方式</th>
				<th>记录人员</th>
				<th>记录时间</th>
				<th>收费窗口</th>
				<th>备注</th>
					<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="financePaymentInfo">
				<tr>
					<td><a
						href="${ctx}/finance/financePaymentInfo/form?id=${financePaymentInfo.id}">${financePaymentInfo.id }</a>
					</td>
					<td style="word-break: break-all">${financePaymentInfo.company}</td>
					<td><fmt:formatNumber value="${financePaymentInfo.paymentMoney}" type="number"/> </td>
					<td><fmt:formatNumber value="${financePaymentInfo.residueMoney}" type="number" /></td>
					<td>${financePaymentInfo.commUserName}</td>
					<td>${financePaymentInfo.commMobile}</td>
					<td><fmt:formatDate value="${financePaymentInfo.payDate}"
							pattern="yyyy-MM-dd" /></td>
					<td>${financePaymentInfo.paymentMethodName}</td>
					<td>
					<c:choose>					
						<c:when test="${financePaymentInfo.bingdingTimes!=null}"> 
   						<a
						href="${ctx}/work/workDealInfo/financeList?financePaymentInfoId=${financePaymentInfo.id}">
							已绑定${financePaymentInfo.bingdingTimes}次</a>
   						</c:when>
   						<c:otherwise> 
   						未绑定!
   						</c:otherwise>
					</c:choose>	
					</td>
					<td>
						<c:choose>
							<c:when test="${financePaymentInfo.distinguish==0}">手动添加</c:when>
							<c:otherwise>批量导入</c:otherwise>
						</c:choose>
					</td>
					<td>${financePaymentInfo.createBy.name}</td>
					<td><fmt:formatDate value="${financePaymentInfo.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${financePaymentInfo.createBy.office.name}</td>		
					<td width="80px" style="word-break: break-all">${financePaymentInfo.remarks}</td>
					<td>
					<shiro:hasPermission name="finance:financePaymentInfo:edit">
						    <a
							href="${ctx}/finance/financePaymentInfo/show?id=${financePaymentInfo.id}">查看</a>
							<a
							href="${ctx}/finance/financePaymentInfo/form?id=${financePaymentInfo.id}">编辑</a>
							<c:if test="${financePaymentInfo.bingdingTimes == 0}">
							<a
							href="javascript:deleteDate(${financePaymentInfo.id});">删除</a></c:if>
					</shiro:hasPermission>
					
					<shiro:hasPermission name="finance:financePaymentInfo:editForm">
						    <a
							href="${ctx}/finance/financePaymentInfo/show?id=${financePaymentInfo.id}">查看</a>
							<a
							href="${ctx}/finance/financePaymentInfo/editForm?id=${financePaymentInfo.id}">编辑</a>
							<c:if test="${financePaymentInfo.bingdingTimes == 0}">
							<a
							href="javascript:deleteDate(${financePaymentInfo.id});">删除</a></c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="finance:financePaymentInfo:quit">
						<c:if test="${financePaymentInfo.residueMoney > 0}">
							<a href="javascript:quitMoney(${financePaymentInfo.id }, ${financePaymentInfo.residueMoney})">退费</a>
						</c:if>
					</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/finance/financePaymentInfo/addAttach"
				enctype="multipart/form-data">
				<input id="fileName" name="fileName" type="file" multiple="multiple" />
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
