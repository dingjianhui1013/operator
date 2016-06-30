<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>发票信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate(
					{
						submitHandler : function(form) {
							loading('正在提交，请稍等...');
							form.submit();
						}
					});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function addOffice(){
			var areaId = $("#areaId").prop('value');
			var url = "${ctx}/sys/office/addOffices?areaId=";
			 $.getJSON(url+areaId+"&_="+new Date().getTime(),function(data){
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data,function(idx,ele){
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">"+ele.name	+"</ooption>"
				});
				
				$("#officeId").html(html);
			});
			
		 }
		
		function mxform(data){
			var url = "${ctx}/receipt/tong/litj?id="+data+"&startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val();
			top.$.jBox.open("iframe:" + url, "查看详情", 800, 420, {
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
		<li class="active"><a href="${ctx}/receipt/tong/listTJ">发票统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="receiptDepotInfo"
		action="${ctx}/receipt/tong/listTJ" method="post"
		class="breadcrumb form-search">
		<input id="skey" name="skey" type="hidden" value="1" />
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div>
		<label>库房名称 ：</label>
<!-- 		<input type="text" name="receiptName" id="receiptName" maxlength="50" -->
<%-- 			class="input-medium"  value="${receiptName }" /> --%>
			<select name="receiptName" id="receiptName">
				<option value="">请选择库房</option>
				<c:forEach items="${receipts}" var="receipts">
					<option value="${receipts.receiptName}" <c:if test="${receipts.receiptName==receiptName}">selected="selected"</c:if>>${receipts.receiptName}</option>
				</c:forEach>
			</select>
	
		<label>选择区域 ：</label>
		<select name="areaId" id="areaId" onchange="addOffice()">
			<option value="">请选择</option>
			<c:forEach items="${offsList}" var="off">
				<option value="${off.id}"
					<c:if test="${off.id==areaId}">
					selected="selected"
					</c:if>>${off.name}</option>
			</c:forEach>
		</select>
		<label>选择网点 ：</label>
			<select name="officeId" id="officeId">
				<option value="">请选择</option>
				<c:forEach items="${offices}" var="off">
					<option value="${off.id}"
						<c:if test="${off.id==officeId}">
						selected="selected"
						</c:if>>${off.name}</option>
				</c:forEach>
			</select><br/>
		</div>
		<div style="margin-top: 8px">
		<label>统计时间 ：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="11" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="11" class="input-medium Wdate" required="required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="查询" />
			</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>库房名称</th>
				<th>所在区域</th>
				<th>所属网点</th>
				<th>联系人</th>
				<th>电话</th>
				<th>起始数量</th>
				<th>入库金额/元</th>
				<th>出库金额/元</th>
				<th>库房余额/元</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="receiptDepotInfo" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${receiptDepotInfo.receiptName}</td>
				<td>${receiptDepotInfo.area.name}</td>
				<td>${receiptDepotInfo.office.name}</td>
				<td>${receiptDepotInfo.receiptCommUser}</td>
				<td>${receiptDepotInfo.receiptCommMobile}</td>
				<td>
				<c:if test="${receiptDepotInfo.id==1}">
				<table >
					<c:forEach items="${receiptDepotInfo.beforeList }" var="before"  >
						<tr>
							<td style="border: none; text-align: right; background-color: white;">${before.key }元</td>
							<td style="border: none; text-align: right; background-color: white;">${before.value }张</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="2" style="border: none; text-align: center;">${receiptDepotInfo.prewarning }</td>
					</tr>
				</table>
				</c:if>
				
				
							<c:if test="${receiptDepotInfo.id!=1}">${receiptDepotInfo.prewarning }</c:if>
				</td>
				<td>
				
				<c:if test="${receiptDepotInfo.id==1}">
				<table>
					<c:forEach items="${receiptDepotInfo.inList }" var="in"  >
						<tr>
							<td style="border: none; text-align: right; background-color: white;">${in.key }元</td>
							<td style="border: none; text-align: right; background-color: white;">${in.value }张</td>
						</tr>
					</c:forEach>
						<tr>
						<td colspan="2" style="border: none; text-align: center;">${receiptDepotInfo.receiptTotal }</td>
					</tr>
				</table>
				</c:if>
				
				
					<c:if test="${receiptDepotInfo.id!=1}">${receiptDepotInfo.receiptTotal }</c:if>
				
				
				
				
				</td>
				<td>
						<c:if test="${receiptDepotInfo.id==1}">
				<table >
					<c:forEach items="${receiptDepotInfo.outList }" var="out"  >
						<tr>
							<td style="border: none; text-align: right; background-color: white;">${out.key }元</td>
							<td style="border: none; text-align: right; background-color: white;">${out.value }张</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="2" style="border: none; text-align: center;">${receiptDepotInfo.receiptOut}</td>
					</tr>
				</table>
				</c:if>
				
				
					<c:if test="${receiptDepotInfo.id!=1}">${receiptDepotInfo.receiptOut}</c:if>
				</td>
				<td>
				<c:if test="${receiptDepotInfo.id==1}">
				<table >
					<c:forEach items="${receiptDepotInfo.afterList }" var="after"  >
						<tr>
							<td style="border: none; text-align: right; background-color: white;">${after.key }元</td>
							<td style="border: none; text-align: right; background-color: white;">${after.value }张</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="2" style="border: none; text-align: center;">${receiptDepotInfo.receiptResidue}</td>
					</tr>
				</table>
				</c:if>
					
					
					<c:if test="${receiptDepotInfo.id!=1}">${receiptDepotInfo.receiptResidue}</c:if>
					
				</td>
				<td>
    				<a href="javascript:mxform(${receiptDepotInfo.id});">查看</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<form name = "mxTJ" action="${ctx}/receipt/tong/litj" method="post" >
		<input type = "hidden" name = "startTime" id="staDate"  />
		<input type = "hidden" name = "endTime"  id ="endDate" />
		<input type = "hidden" name = "id" id = "depotId"  />
	</form>
</body>
</html>
