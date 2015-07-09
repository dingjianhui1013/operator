<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>盘点管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			rules:{
				inventorySurplus:"required"
			},
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
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
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function search(d){
		var isNull=0;
		for (var i = 1; i <=d; i++) {
			var jv = $("#inventorySurplus"+i).val();
			$("#inventoryMes"+i).html("");
			if (jv==null||jv=="") {
				isNull=1;
				$("#inventoryMes"+i).html("不能为空!");
			}
		}
		if(isNull==1){
			top.$.jBox.closeTip();
		}else{
		var isReasonNull=0;
		 for (var i = 1; i <= d; i++) {
			var av =  $("#afterTotal"+i).val();			
			var jv = $("#inventorySurplus"+i).val();
			$("#reasonMes"+i).html("");
			if (av!=jv) {
				var reason = $("#differenceReason"+i).val();
				if (reason==null||reason=="") {
					isReasonNull=1;
					$("#reasonMes"+i).html("盘点差异,不能为空！");
				}
			}
		} 
		 if(isReasonNull==1){
			 
			 top.$.jBox.closeTip();
		 }else{
			 document.searchForm.submit();			
		 }
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/key/keyCheckLog/">盘点管理</a></li>
		<li class="active"><a href="${ctx}/key/keyCheckLog/checkList?depotId=${depotId}">库房盘点</a></li>
	</ul>
	<form:form id="searchForm" name ="searchForm" modelAttribute="keyCheckLog"
		action="${ctx}/key/keyCheckLog/save" method="post" >
		<tags:message content="${message}" />
	 <font size="4" >库房信息：</font>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<tbody>
				<tr>
					<td>库房名称：</td>
					<td>${statis.keyUsbKeyDepot.depotName}</td>
					<td>所在区域：</td>
					<td>${statis.keyUsbKeyDepot.office.parent.name}</td>
				</tr>
				<tr>
					<td>上期结余：</td>
					<td>${statis.totalCount}</td>
					<td>本期入库：</td>
					<td>${statis.inCount}</td>
				</tr>
				<tr>
					<td>本期出库：</td>
					<td>${statis.outCount}</td>
					<td>当前结余：</td>
					<td>${statis.totalEndCount}</td>
				</tr>
		</tbody>
	</table>
	<font size="4" >盘点信息：</font> 
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
				
					<th>库房名称</th>
					<th>所在区域</th>
					<th>所在网点</th>
					
					<th>厂商名称</th>
					<th>Key类型名称</th>
					<th>上期结余</th>
					<th>本期入库</th>
					<th>本期出库</th>
					<th>当前结余</th>
					<th>盘点数</th>
					<th>差异说明</th>
					<th>盘点开<br/>始时间</th>
					<th>盘点结<br/>束时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${statisticList}" var="sta" varStatus="status">
					<tr>
					<td>
					${sta.keyUsbKeyDepot.depotName}
					</td>
					
					<td>
					${sta.keyUsbKeyDepot.office.parent.name}
					</td>
					<td>
					${sta.keyUsbKeyDepot.office.name}
					</td>
						<td>${sta.keyGeneralInfo.configSupplier.supplierName}
						<input type="hidden" name="keyGeneralInfoId" value="${sta.keyGeneralInfo.id}"/>
						</td>
						<td>${sta.keyGeneralInfo.name}</td>
						<td>${sta.totalCount}
						<input type="hidden" name="total" value="${sta.totalCount}"/>
						</td>
						<td>${sta.inCount}
						<input type="hidden" name="in" value="${sta.inCount}"/>
						</td>
						<td>${sta.outCount}
						<input type="hidden" name="out" value="${sta.outCount}"/>
						</td>
						<td>${sta.totalEndCount}
						<input type="hidden" id="afterTotal${status.index+1}" name="afterTotal" value="${sta.totalEndCount}"/>
						</td>
						<td><input type="text" id="inventorySurplus${status.index+1}" name="inventorySurplus" 
							maxlength="9" style="width:90px"
							onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
							onkeyup="value=value.replace(/[^\d]/g,'')"  />
							<span id="inventoryMes${status.index+1}" style="color:red;"></span>
							
							</td>

						<td><input type="text" id="differenceReason${status.index+1}" name="differenceReason" 
							class="required" style="width:90px"/>
							<span id="reasonMes${status.index+1}" style="color:red;"></span>
							</td>
						<td><fmt:formatDate value="${time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${end}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
			<tr><td colspan="13" style="text-align:center;">
				<input type="hidden" name="depotId" value="${depotId }"/>
				<input type="hidden" name="checkNumber" value="${checkNumber }"/>
				<input type="hidden" name="time" value="<fmt:formatDate value="${time}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>		
				<input type="hidden" name="end" value="<fmt:formatDate value="${end}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				<c:if test="${len!=0}">
				 <a id="btnSubmit"  class = "btn btn-primary" href= "javascript:search(${statisticList.size()});" onclick="return confirmx('确定提交此次盘点的数据吗？', this.href)" >确定</a>
				</c:if>
			</td></tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>
