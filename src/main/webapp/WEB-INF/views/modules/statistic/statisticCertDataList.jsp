<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>证书发放量统计</title>
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


		function showDays(applyId,productId,startDate,endDate,month,officeId){
			var url = "${ctx}/statistic/statisticCertData/showDays?applyId="+applyId+"&productId="+productId+"&startDate="+startDate+"&endDate="+endDate+"&month="+month+"&officeId="+officeId;
			top.$.jBox.open("iframe:" + url, "业务明细", 800 , 420 , {
				buttons : {
					"确定" : "ok",
					"关闭" : true
				},  
				iframeScrolling: 'yes',
				submit : function(v, h, f) {
				}
			});
		}
// 		function onSubmit(){
// 			if ($("#startTime").val()==""||$("#endTime").val()=="") {
// 				top.$.jBox.tip("请选定时间范围");
// 				return false;
// 			} else {
// 				if ($("#officeId").val()==""){
// 					top.$.jBox.tip("请选定网点");
// 					return false;
// 				} else {
// 					return true;
// 				}
// 			}
// 		}

		function dcZS()
		{
			if ($("#startTime").val()==""||$("#endTime").val()=="") {
				top.$.jBox.tip("请选定时间范围");
				return false;
			} else {
				if ($("#officeId").val()==""){
					top.$.jBox.tip("请选定网点");
				} else {
					var applyId = document.getElementById("applyId").value;
					var proList = document.getElementById("proList").value;
					var areaId = document.getElementById("areaId").value;
					var officeId = document.getElementById("officeId").value;
					var startDate = document.getElementById("startTime").value;
					var endDate =  document.getElementById("endTime").value;
					window.location.href="${ctx}/statistic/statisticCertData/exportZS?applyId="+applyId+"&proList="+proList+"&areaId="+areaId+"&officeId="+officeId+"&startDate="+startDate+"&endDate="+endDate;
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/statistic/statisticCertDataProduct">证书发放量统计</a></li>
		<li class="active"><a href="${ctx}/statistic/statisticCertData">项目发放证书统计</a></li>
	</ul>
	<form:form id="searchForm"  action="${ctx}/statistic/statisticCertData" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		<div>
		<label>应&nbsp; &nbsp; &nbsp; 用 ：</label> 
			<select name="applyId" id="applyId">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id == applyId}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
		<label>产品名称 ：</label>
		<select name="proList" id="proList">
			<option <c:if test="${productId==-1}">selected</c:if>  value="-1">所有产品</option>
			<c:forEach items="${proList }" var="pro">
				<option <c:if test="${productId==pro.id }">selected</c:if> value="${pro.id }">${pro.name }</option>
			</c:forEach>
		</select>
		</div>
		 
		<div style="margin-top: 8px">
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
			</select>
			</div>
			<div style="margin-top: 8px">
		<label>统计时间 ：</label>
		<input id="startTime" name="startDate" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
			value="${startDate}"/>&nbsp;-&nbsp;
				<input id="endTime" name="endDate" type="text" readonly="readonly"
			maxlength="20" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="${endDate}" />
		&nbsp;&nbsp;
		
<!-- 		<input id="btnSubmit" class="btn btn-primary" onclick="return onSubmit();" type="submit" value="查询"/> -->
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>	
		<input id="exportZS" class="btn btn-primary" onclick="dcZS()" type="button" value="导出"/>
		
		</div>
	</form:form>
	<tags:message content="${message}"/>
	
	 <div class="form-horizontal">
	 <table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
					<th colspan="${monthList.size()*4+4 }">项目发放证书统计</th>
				</tr>
				<tr>
					<th colspan="${monthList.size()*4+4}">应用:${yingyong} </th>
				</tr> 
				<tr>
					<th>应用</th>
					<th>费用归属</th>
					<th>证书年限</th>
					<c:forEach items="${monthList }" var="month">
						<th colspan="2"><a href="javascript:showDays('${applyId }','${productId }','${startDate }','${endDate }','${month }','${officeId }')">${month }</a></th>
					</c:forEach>
					<th></th>
				</tr>
				<tr>
					<th></th>
					<th></th>
					<th></th>
					<c:forEach  begin="1" end="${monthList.size() }">
						<th>新增</th>
						<th>更新</th>						
					</c:forEach>
					<th>小计</th>
				</tr>
			</thead>
			
			<c:forEach items="${sumList }" var="sum">
			<tr>
				<td rowspan="15">${sum.configApp.appName }</td>
				<td rowspan="5">自费企业</td>
				<td>一年期</td>
				<c:set value="0" var="subtotal" /> 
				<c:forEach items="${sum.certMonths }" var="oneSm1">
					<td>${oneSm1.oneAdd1 }</td>
					<td>${oneSm1.oneRenew1 }</td>					
					<c:set value="${subtotal +oneSm1.oneAdd1+oneSm1.oneRenew1 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>两年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="oneSm2">
					<td>${oneSm2.oneAdd2 }</td>
					<td>${oneSm2.oneRenew2 }</td>					
					<c:set value="${subtotal +oneSm2.oneAdd2+oneSm2.oneRenew2 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			
			<tr>
				<td>三年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="oneSm3">
					<td>
						<c:if test="${oneSm3.oneAdd3==null}">0</c:if> 
						<c:if test="${oneSm3.oneAdd3!=null}">${oneSm3.oneAdd3}</c:if>
					</td>
					<td>
						<c:if test="${oneSm3.oneRenew3==null}">0</c:if> 
						<c:if test="${oneSm3.oneRenew3!=null}">${oneSm3.oneRenew3}</c:if>
					</td>					
					<c:set value="${subtotal +oneSm3.oneAdd3+oneSm3.oneRenew3 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			
			<tr>
				<td>四年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="oneSm4">
					<td>${oneSm4.oneAdd4 }</td>
					<td>${oneSm4.oneRenew4 }</td>					
					<c:set value="${subtotal +oneSm4.oneAdd4+oneSm4.oneRenew4 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>五年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="oneSm5">
					<td>${oneSm5.oneAdd5 }</td>
					<td>${oneSm5.oneRenew5 }</td>					
					<c:set value="${subtotal +oneSm5.oneAdd5+oneSm5.oneRenew5 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td rowspan="5">合同企业</td>
				<td>一年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="twoSm1">
					<td>${twoSm1.twoAdd1 }</td>
					<td>${twoSm1.twoRenew1 }</td>					
					<c:set value="${subtotal +twoSm1.twoAdd1+twoSm1.twoRenew1 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>两年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="twoSm2">
					<td>${twoSm2.twoAdd2 }</td>
					<td>${twoSm2.twoRenew2 }</td>
					<c:set value="${subtotal +twoSm2.twoAdd2+twoSm2.twoRenew2  }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			
			<tr>
				<td>三年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="twoSm3">
					<td>
						<c:if test="${twoSm3.twoAdd3==null}">0</c:if> 
						<c:if test="${twoSm3.twoAdd3!=null}">${twoSm3.twoAdd3}</c:if>
					</td>
					<td>
						<c:if test="${twoSm3.twoRenew3==null}">0</c:if> 
						<c:if test="${twoSm3.twoRenew3!=null}">${twoSm3.twoRenew3}</c:if>
					</td>
					<c:set value="${subtotal +twoSm3.twoAdd3+twoSm3.twoRenew3}" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			
			<tr>
				<td>四年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="twoSm4">
					<td>${twoSm4.twoAdd4 }</td>
					<td>${twoSm4.twoRenew4 }</td>
					<c:set value="${subtotal +twoSm4.twoAdd4+twoSm4.twoRenew4 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>五年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="twoSm5">
					<td>${twoSm5.twoAdd5 }</td>
					<td>${twoSm5.twoRenew5 }</td>
					<c:set value="${subtotal +twoSm5.twoAdd5+twoSm5.twoRenew5}" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td rowspan="5">政府统一采购</td>
				<td>一年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="fourSm1">
					<td>${fourSm1.fourAdd1 }</td>
					<td>${fourSm1.fourRenew1 }</td>
					<c:set value="${subtotal +fourSm1.fourAdd1+fourSm1.fourRenew1 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>两年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="fourSm2">
					<td>${fourSm2.fourAdd2 }</td>
					<td>${fourSm2.fourRenew2 }</td>					
					<c:set value="${subtotal +fourSm2.fourAdd2+fourSm2.fourRenew2 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			<tr>
				<td>三年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="fourSm3">
					<td>
						<c:if test="${fourSm3.fourAdd3==null}">0</c:if> 
						<c:if test="${fourSm3.fourAdd3!=null}">${fourSm3.fourAdd3}</c:if>
					</td>
					<td>
						<c:if test="${fourSm3.fourRenew3 ==null}">0</c:if> 
						<c:if test="${fourSm3.fourRenew3 !=null}">${fourSm3.fourRenew3 }</c:if>
					</td>					
					<c:set value="${subtotal +fourSm3.fourAdd3+fourSm3.fourRenew3 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			
			
			<tr>
				<td>四年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="fourSm4">
					<td>${fourSm4.fourAdd4 }</td>
					<td>${fourSm4.fourRenew4 }</td>
					<c:set value="${subtotal +fourSm4.fourAdd4+fourSm4.fourRenew4 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			<tr>
				<td>五年期</td>
				<c:set value="0" var="subtotal" />
				<c:forEach items="${sum.certMonths }" var="fourSm5">
					<td>${fourSm5.fourAdd5 }</td>
					<td>${fourSm5.fourRenew5 }</td>
					<c:set value="${subtotal +fourSm5.fourAdd5+fourSm5.fourRenew5 }" var="subtotal" /> 
				</c:forEach>
				<td>${subtotal}</td>
			</tr>
			</c:forEach>
			<tr>
				<td>总计</td>
				<td></td>
				<td></td>
				<c:set var="allCountNew" value="0"/>
				<c:forEach items="${totalListNum }" var="sum">
					<td>${sum}</td>
					<c:set var="allCountNew" value="${allCountNew + sum }"/>
				</c:forEach>
				<td>${allCountNew}</td>
			</tr>
		</table>
		</div>
</body>
</html>
