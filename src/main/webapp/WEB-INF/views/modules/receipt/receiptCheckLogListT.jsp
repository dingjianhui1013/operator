<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>盘点信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function submit_form(){
			if($("#sta").val()==1){
				top.$.jBox.tip("存在尚未复位的盘点信息，请对其进行复位操作");
			}else{
				
				var next = 0;
				var count = $($("#mainTable").find("tr")[1]).find("[name='applyNumTD']").find("input").length;
				var td = $($("#mainTable").find("tr")[1]).find("[name='applyNumTD']");
				for (var a = 0; a < count-1; a++) {
					var num = $(td.find("input")[a]).val();
					if (num=="") {
						$(td.find("label")[a]).html("不能为空！");
						next = 1;
					}
				}
				
				
				 if(next==1){
					top.$.jBox.tip("请输入盘点后数量");
				}else{
			
						if(parseFloat($("#yuMoney").val())!=parseFloat($("#beforeResidue").val())){
							if($("#xxsm").val()==""){
								top.$.jBox.tip("差异说明不能为空");
							}else{
								document.createLog.submit();
							}
						}else{
							document.createLog.submit();
						}
				} 
			}
		}
		
		
		function multiply(obj){
			var count = $(obj).parent().find("input").length;	
			var size = parseInt(count)-parseInt(1);
			var total=0;
			for (var a = 0; a < size; a++) {
				var input = $($(obj).parent().find("input")[a]);
				var countValue = input.val();
					if (countValue=="") {
						countValue=0;
						
						$($(obj).parent().find("label")[a]).html("不能为空！");
					}else{
						$($(obj).parent().find("label")[a]).html("");
						
					}
					
				var type = input.attr("receiptType");
			total+=parseInt(countValue)*parseInt(type);
			}
			
			$(obj).parent().find("label:last").html(total);
			$("#yuMoney").val(total);
			
			
		}
		
		
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/receipt/receiptCheckLog/li">盘点</a></li>
		<li class="active"><a href="#">盘点信息列表</a></li>  
	</ul>  
	<input type="hidden" id = "sta" value = "${status }"/>
	<tags:message content="${message}"/>
	 <font size="4" >库房信息：</font>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<tbody>
				<tr>
					<td>库房名称：</td>
					<td>${receiptDepotInfo.receiptName}</td>
					<td>所在区域：</td>
					<td>${receiptDepotInfo.office.areaName}</td>
				</tr>
				<tr>
					<td>上期结余：</td>
					<td>${lastMoney}</td>
					<td>本期入库：</td>
					<td>${numMoney}</td>
				</tr>
				<tr>
					<td>本期出库：</td>
					<td>${yikaiMoney}</td>
					<td>当前结余：</td>
					<td>${receiptDepotInfo.receiptResidue}</td>
				</tr>
		</tbody>
	</table>
	<font size="4" >盘点信息：</font> 
	<form name = "createLog" action="${ctx}/receipt/receiptCheckLog/save" method="post" >
		<input type = "hidden" name = "receiptDepotInfo.id" value = "${receiptDepotInfo.id}"/>
		<input type = "hidden" name = "beforeTotal"  value = "${numMoney}"/> 
		<input type = "hidden" name = "beforeOut"  value = "${yikaiMoney}"/> 
		<input type = "hidden" name = "lastMoney"  value = "${lastMoney}"/> 
		<input type = "hidden" name = "beforeResidue" id ="beforeResidue" value = "${receiptDepotInfo.receiptResidue}"/> 
		<input type = "hidden" name = "staDate" value = "${staDate}"/> 
		<input type = "hidden" name = "endDa" value = "<fmt:formatDate value="${endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"/> 
		<input type = "hidden" name = "office.id" value = "${receiptDepotInfo.office.id}"/> 
		<table  id="mainTable"  class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>库房名称</th>
					<th>所在区域</th>
					<th>所在网点</th>
					<th>上次盘<br/>点余额</th>
					<th>入库<br/>金额/元</th>
					<th>已开<br/>金额/元</th>
					<th>剩余<br/>金额/元</th>
					<th>盘点后金额/元</th>
					<th>差异说明</th>
					<th>盘点开始时间</th>
					<th>盘点结束时间</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${receiptDepotInfo.receiptName}</td>
					<td>${receiptDepotInfo.office.areaName}</td>
					<td>${receiptDepotInfo.office.name}</td>
					<td>
					
						<c:if test="${receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${lastTypes }" var="lastType"  >
								<tr style="height: 33px;">
									<td  style="height: 32px;border: none; text-align: right;">${lastType.key }元</td>
									<td style="border: none; text-align: right;">${lastType.value }张
									<input  type="hidden" name="lastCount" value="${lastType.value}"/>
									</td>
								</tr>
							</c:forEach>
							
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${lastMoney}</td>
							</tr>
						</table>
					</c:if>
					
					<c:if test="${receiptDepotInfo.id!=1}">${lastMoney}</c:if>
				
					</td>
					<td>
					
					<c:if test="${receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${inTypes }" var="inType"  >
								<tr style="height: 33px;">
									<td  style=" height: 32px;border: none; text-align: right;">${inType.key }元</td>
									<td style="border: none; text-align: right;">${inType.value }张
									<input  type="hidden" name="inCount" value="${inType.value}"/>
									</td>
								</tr>
							</c:forEach>
							
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${numMoney}</td>
							</tr>
						</table>
					</c:if>
					<c:if test="${receiptDepotInfo.id!=1}">${numMoney}</c:if>
					</td>
					<td>
					
					<c:if test="${receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${outTypes }" var="outType"  >
								<tr>
									<td  style=" height: 32px;border: none; text-align: right;">${outType.key }元</td>
									<td style="border: none; text-align: right;">${outType.value }张
										<input  type="hidden" name="outCount" value="${outType.value}"/>
									</td>
								</tr>
							</c:forEach>
							
							<tr>
							<td colspan="2" style="border: none; text-align: center;">${yikaiMoney}</td>
							</tr>
						</table>
					</c:if>
						<c:if test="${receiptDepotInfo.id!=1}">${yikaiMoney}</c:if>
					</td>
					<td>
					<c:if test="${receiptDepotInfo.id==1}">
						<table>
							<c:forEach items="${yuTypes }" var="yuType"  >
								<tr>
									<td  style=" height: 32px;border: none; text-align: right;">${yuType.key }元</td>
									<td style="border: none; text-align: right;">${yuType.value }张
									
									<input  type="hidden" name="yuCount" value="${yuType.value}"/>
									</td>
								</tr>
							</c:forEach>
							
							<tr>
							<td colspan="2" style="border: none; text-align:center;">${receiptDepotInfo.receiptResidue}</td>
							</tr>
						</table>
					</c:if>
					<c:if test="${receiptDepotInfo.id!=1}">${receiptDepotInfo.receiptResidue}</c:if>
					
					</td>
					<td name="applyNumTD" id="applyNumTD">
					
					<c:if test="${receiptDepotInfo.id==1}">
						<c:forEach items="${lastTypes }" var="type">
								<input type="text" 
								onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
						onkeyup="value=value.replace(/[^\d]/g,'')" 
								name="afterCount"  onblur="multiply(this)" receiptType="${type.key }"
								/>
								
								
								
								
								<label style="color: red;"></label>
								<br/>
							</c:forEach>	
							<label style="margin-top: 4px;" ></label>
							<input type="hidden" name="afterResidue" id="yuMoney"  >
							
					</c:if>
					
					<c:if test="${receiptDepotInfo.id!=1}">
						<input type="text"  
							onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" 
							onkeyup="value=value.replace(/[^\d]/g,'')" 
						name="afterResidue" id="yuMoney" style="width: 100px"/>
					</c:if>
					
					
					
					</td>
					
					
					
					<td><input type="text" name="fixRemark" id="xxsm" style="width: 100px"/>
					
						<c:forEach items="${lastTypes }" var="type">
							 <input type="hidden"  name="typeDesc" value="${type.key }"/>
							</c:forEach>	
					
					</td>
					<td>
					<fmt:formatDate value="${staDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
					<fmt:formatDate value="${endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				<tr><td colspan="11" style="text-align:center;">
				<a href="javascript:submit_form()"  class="btn btn-primary"  onclick="return confirmx('您确认盘点后金额无误？',this.href)">确定</a>
			</td></tr>
				
			</tbody>
		</table>
	</form>
</body>
</html>
