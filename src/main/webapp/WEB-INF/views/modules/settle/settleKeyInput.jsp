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
		
		function alarmValue(obj,obj2){
			var html = $('#bzBox').html();
			var submit = function (v, h, f) {
				if(v=='ok')
					{
					    if (f.remarks == '') {
					        top.$.jBox.tip("请填写备注。", 'error', { focusId: "remarks" });
					        return false;
					    }
					    $("#text").val(f.remarks);
						$("#"+obj).attr('checked',true);
						$("#"+obj2).attr('checked',false);
					    return true;
					}else
						{
								$("#"+obj).attr('checked',false);
						}
			};

			top.$.jBox(html, { title: "确定修改？",buttons:{"确定":"ok","关闭":true}, submit: submit });
		}
		function changeKey()
		{
			var moneyType=$("#provider").find("option:selected").val();
			var url="${ctx}/settle/keyPurchase/changeKey";
			$.ajax({
				url:url,
				data:{"moneyType":moneyType},
				dataType:'json',
				success:function(data){
					$("#unitPrice").val(data.money);
					$("#unitPrice1").val(data.money);
					if(data.endcode==0)
						{
							$("#startCode").val("");
						}else
							{
							$("#startCode").val(data.endcode);
							}
				}
			});	
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
				<input type="text"  name="endCode" value="" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量：</label>
			<div class="controls">
				<input type="text"  name="count" value=""/>
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
				已付<input type="checkbox" name="status" value="1" onclick="alarmValue('complete','noComplete')" id="complete" class="required"/>
				未付<input type="checkbox" name="status" value="0" onclick="alarmValue('noComplete','complete')" id="noComplete" class="required"/>
			</div>
		</div>
		<div id="bzBox" style="display: none">
			<div class="control-group" style="padding:20px;">
			<label class="control-label" style="margin-right:10px">备注：</label>
				<textarea rows="2" cols="2"  id="text" name="remarks"></textarea>
		</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	
</body>
</html>
