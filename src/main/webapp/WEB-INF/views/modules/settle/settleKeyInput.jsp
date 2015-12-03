<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>key采购记录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				focusInvalid: false,
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.insertAfter($("#noComplete"));
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function changeKey()
		{
			var moneyType=$("#provider").find("option:selected").val();
			var url="${ctx}/settle/keyPurchase/changeKey";
			$.ajax({
				url:url,
				data:{"moneyType":moneyType,_:new Date().getTime()},
				dataType:'json',
				success:function(data){
					$("#unitPrice").val(data.money);
					$("#unitPrice1").val(data.money);
					if(data.endcode==0)
						{
							$("#startCode").val("");
						}else
							{
								$("#startCode").val(data.endcode+1);
							}
				}
			});	
		}
		function changeCount()
		{
			var startCode=parseInt($("#startCode").val());
			var endCode=parseInt($("#endCode").val());
			if(isNaN(startCode))
				{
					top.$.jBox.tip("请填写起始码");
				}else if(startCode > endCode){
					top.$.jBox.tip("截止码小于起始码");
				}
				
				if(!isNaN(startCode)&&!isNaN(endCode))
					{
						$("#count").val((endCode-startCode)+1);
					}
			if(isNaN(endCode))
				{
					top.$.jBox.tip("请填写截止码");
				}
			
		}
		function sub()
		{
			var count=$("#count").val();
			if(count<0)
				{
					top.$.jBox.tip("数量不能为负数");
					return false;
				}else{
					return true;
				}
		}
	</script>
	</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/settle/keyPurchase">key采购记录列表</a></li>
		<li class="active"><a href="${ctx}/settle/keyPurchase/form">KEY采购记录添加</a></li>
	</ul>
	<form:form id="inputForm" action="${ctx}/settle/keyPurchase/save" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">入库日期</label>
			<div class="controls"> 
				<input type="text" name="storageDate" readonly="readonly"
				maxlength="20" class="Wdate required" 
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value="${startdate}" pattern="yyyy-MM-dd HH-mm-SS"/>" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称 ：</label> 
			<div class="controls"> 
				<select name="appName" id="provider" onchange="changeKey()">
					<c:forEach items="${generalInfos}" var="ga">
						<option value="${ga.id}" >${ga.manuKeyName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">KEY码(起始码)：</label>
			<div class="controls">
				<input type="text"  name=startCode value="" id="startCode" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">KEY码(截止码)：</label>
			<div class="controls">
				<input type="text"  name="endCode" value="" id="endCode" class="required" onblur="changeCount()"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量：</label>
			<div class="controls">
				<input type="text"  name="count" value="" id="count" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单价：</label>
			<div class="controls">
				<input type="text" id="unitPrice1" name="money1" value="" disabled="disabled"/>
				<input type="hidden" id="unitPrice" name="money" value="" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
<!-- 				已付<input type="checkbox" name="status" value="1" onclick="alarmValue('complete','noComplete')" id="complete" class="required"/> -->
<!-- 				未付<input type="checkbox" name="status" value="0" onclick="alarmValue('noComplete','complete')" id="noComplete" class="required"/> -->
				已付<input type="radio" name="status" value="1"  id="complete" class="required"/>
				未付<input type="radio" name="status" value="0"  id="noComplete" class="required"/>
			</div>
		</div>
		<div class="control-group" >
			<label class="control-label">备注：</label>
			<div class="controls">
				<textarea rows="2" cols="2" id="text" name="remarks" id="remarks"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存" onclick="return sub()"/>&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	
</body>
</html>
