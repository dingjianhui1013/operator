<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>代理商管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#searchForm").validate({
				submitHandler : function(form) {
					
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
		function showTable_f(){
			document.showTable.submit();
		}
		
		function onChange(){
			var supplierId = $("#supplierId").val();
			var vtnUpplierId = $("#vtnUpplierId").val();
			var vtnProduct = $("#vtnProduct").val();
			var vtnapp = $("#vtnapp").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			
			if (startTime==null||startTime=='') {
				top.$.jBox.tip("请选择开始时间！");
				return;
			}
			if (endTime==null||endTime=='') {
				top.$.jBox.tip("请选择结束时间！");
				return;
			}

			window.location.href="${ctx }/settle/supplierSettle/exportFKJS?startTime="+startTime+"&endTime="+endTime+"&vtnUpplierId="+vtnUpplierId+"&vtnProduct="+vtnProduct+"&vtnapp="+vtnapp+"&configSupplierId="+supplierId;				
		}
		
		
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/settle/supplierSettle/showTableF">四川CA对账统计表</a></li>
		<li class="active"><a href="${ctx}/settle/supplierSettle/showTableT">付款结算清单</a></li>
		<li ><a href="${ctx}/settle/settleKey/list">KEY数量统计</a></li>
	</ul><br/>
	<form:form id="searchForm" modelAttribute="configSupplier" action="${ctx}/settle/supplierSettle/showTableT" 
	method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>供应商名称：</label>
		<form:select path="id" id="supplierId">
		 	<c:forEach items="${suppliers }" var="supplier">
				<form:option value="${supplier.id }">${supplier.supplierName }</form:option>
			</c:forEach>
		</form:select>
		<br/><br/>
		<label>VTN供应商：</label>
		<select name="vtnUpplierId" id="vtnUpplierId">
			<option value="">请选择</option>
		 	<c:forEach items="${configSuppliers }" var="configSupplier">
				<option value="${configSupplier.id }"
				<c:if test="${configSupplier.id==vtnUpplierId}">selected="selected"</c:if>
				>${configSupplier.supplierName }</option>
			</c:forEach>
		</select>
		
		<label>VTN产品名称：</label>
			<select name="vtnProduct" id="vtnProduct">
		<option value="">请选择</option>
		 	<c:forEach items="${vtnProducts }" var="prodect">
				<option value="${prodect.PRODUCT_NAME }"
			<c:if test="${prodect.PRODUCT_NAME==vtnProduct}">selected="selected"</c:if>
				>
				${prodect.PRODUCT_NAME }</option>
			</c:forEach>
		</select>
		
		<label>VTN应用名称：</label>
		<select name="vtnapp" id="vtnapp">
			<option value="">请选择</option>
		 	<c:forEach items="${vtnApps }" var="app">
				<option value="${app.APP_NAME }" 
				<c:if test="${app.APP_NAME==vtnapp}">selected="selected"</c:if>
				 >${app.APP_NAME }</option>
			</c:forEach>
		</select>
		<br/><br/>
		<label>结算时间 ：</label>
		<input id="startTime" name="startTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
			value="${startTime}"/><label id="startmes" style="color:red;"></label>&nbsp;-&nbsp;

				<input id="endTime" name="endTime" type="text" readonly="readonly"
			maxlength="10" class="Wdate required"
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'startTime\')}'});"
			value="${endTime}"/><label id="endmes" style="color:red;"></label>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		
		&nbsp;<a href="javascript:onChange()"   class="btn btn-primary">导出</a>
		
		
	</form:form>
	<c:set var="totalMoney" value="0"/>
	<div class="form-horizontal">
		<table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
					<th colspan="6"><h3>付款结算清单</h3></th>
				</tr>
			</thead>
			<tr>
				<th colspan="6">制表时间：${startTime } — ${endTime}</th>
			</tr>
			<tr>
				<th colspan="6">请款单位：${configSupplier.supplierName }</th>
			</tr>
			<tr>
				<th>采购日期</th>
				<th>产品名称</th>
				<th>项目名称</th>
				<th>数量/张</th>
				<th>单价/元</th>
				<th>小计/元</th>
			</tr>
			<tr>
				<th rowspan="9">${dateT }</th>
				<th>一年期机构/个人证书</th>
				<th>${oumap.ou0 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
							<c:if test="${sup.PRODUCT_TYPE==2 }">
							<c:set var="countOU0" value="${sup.YEAR1 }"></c:set>
							<c:set var="totolCount" value="${sup.YEAR1 }"></c:set>
							</c:if>
					</c:forEach>
					<c:if test="${countOU0==null||countOU0<1 }">
							0
					</c:if>
					<c:if test="${countOU0>0 }">
							${countOU0 }
					</c:if>
				</th>
				<th>
					${money.A1_2 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							
							<c:set var="totalMoney" value="${sup.YEAR1*money.A1_2 }"></c:set>
							<c:set var="moneyOU0" value="${sup.YEAR1*money.A1_2 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU0==null||moneyOU0<1 }">
						0.0
					</c:if>
					<c:if test="${moneyOU0>0 }">
							<fmt:formatNumber pattern="#0.0">${moneyOU0}</fmt:formatNumber>
					</c:if>
				</th>
			</tr>
			<tr>
				<th>一年期企业证书</th>
				<th>${oumap.ou1 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							<c:set var="totolCount" value="${totolCount+sup.YEAR1 }"></c:set>
							<c:set var="countOU1" value="${sup.YEAR1 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU1==null||countOU1<1 }">
							0
						</c:if>
							<c:if test="${countOU1>0 }">
							${countOU1 }
						</c:if>
						
				</th>
				<th>
					${money.A1_1 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR1*money.A1_1 }"></c:set>
							<c:set var="moneyOU1" value="${sup.YEAR1*money.A1_1 }"></c:set>
						</c:if>
					</c:forEach>
						<c:if test="${moneyOU1==null||moneyOU1<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU1>0}">
							<fmt:formatNumber pattern="#0.0">${moneyOU1 }</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>二年期机构/个人证书</th>
				<th>${oumap.ou2 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							${sup.YEAR2 }
							<c:set var="totolCount" value="${totolCount+sup.YEAR2 }"></c:set> 
							<c:set var="countOU2" value="${sup.YEAR2 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU2==null||countOU2<1 }">
							0
						</c:if>
						<c:if test="${countOU2>0 }">
							${countOU2 }
						</c:if>
				</th>
				<th>
					${money.A2_2 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR2*money.A2_2 }"></c:set>
							<c:set var="moneyOU2" value="${sup.YEAR2*money.A2_2}"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU2==null||moneyOU2<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU2>0 }">
								<fmt:formatNumber pattern="#0.0">${moneyOU2 }</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>二年期企业证书</th>
				<th>${oumap.ou3 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							
							<c:set var="totolCount" value="${totolCount+sup.YEAR2 }"></c:set>
							<c:set var="countOU3" value="${sup.YEAR2 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU3==null||countOU3<1 }">
							0
						</c:if>
						<c:if test="${countOU3>0 }">
							${countOU3 }
						</c:if>
				</th>
				<th>
					${money.A2_1 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR2*money.A2_1 }"></c:set>
							<c:set var="moneyOU3" value="${totalMoney+sup.YEAR2*money.A2_1 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU3==null||moneyOU3<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU3>0}">
							<fmt:formatNumber pattern="#0.0">${moneyOU3}</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>四年期机构/个人证书</th>
				<th>${oumap.ou4 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							<c:set var="totolCount" value="${totolCount+sup.YEAR4 }"></c:set>
								<c:set var="countOU4" value="${sup.YEAR4 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU4==null||countOU4<1 }">
							0
						</c:if>
						<c:if test="${countOU4==null||countOU4<1 }">
							${countOU4 }
						</c:if>
				</th>
				<th>
					${money.A4_2 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR4*money.A4_2  }"></c:set>
							<c:set var="moneyOU4" value="${sup.YEAR4*money.A4_2   }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU4==null||moneyOU4<1 }">
							0.0
						</c:if>
							<c:if test="${moneyOU4>0 }">
						<fmt:formatNumber pattern="#0.0">${moneyOU4}</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>四年期企业证书</th>
				<th>${oumap.ou5 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							<c:set var="totolCount" value="${totolCount+sup.YEAR4 }"></c:set>
							<c:set var="countOU5" value="${sup.YEAR4 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU5==null||countOU5<1 }">
							0
						</c:if>
						<c:if test="${countOU5>0 }">
							${countOU5}
						</c:if>
				</th>
				<th>
					${money.A4_1 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR4*money.A4_1   }"></c:set>
							<c:set var="moneyOU5" value="${sup.YEAR4*money.A4_1  }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU5==null||moneyOU5<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU5>0 }">
							<fmt:formatNumber pattern="#0.0">${moneyOU5  }</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>五年期机构/个人证书</th>
				<th>${oumap.ou6 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							<c:set var="totolCount" value="${totolCount+sup.YEAR5 }"></c:set>
							<c:set var="countOU6" value="${sup.YEAR5 }"></c:set>
						</c:if>
					</c:forEach>
						<c:if test="${countOU6==null||countOU6<1 }">
							0
						</c:if>
						<c:if test="${countOU6>0 }">
							${countOU6 }
						</c:if>
				</th>
				<th>
					${money.A5_2 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==2 }">
							
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR5*money.A5_2   }"></c:set>
							<c:set var="moneyOU6" value="${sup.YEAR5*money.A5_2   }"></c:set>
						</c:if>
					</c:forEach>
						<c:if test="${moneyOU6==null||moneyOU6<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU6>0 }">
							<fmt:formatNumber pattern="#0.0">${moneyOU6 }</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			<tr>
				<th>五年期企业证书</th>
				<th>${oumap.ou7 }</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							<c:set var="totolCount" value="${totolCount+sup.YEAR5 }"></c:set>
							<c:set var="countOU7" value="${sup.YEAR5 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${countOU7==null||countOU7<1 }">
							0
						</c:if>
						<c:if test="${countOU7>0 }">
							${countOU7 }
						</c:if>
				</th>
				<th>
					${money.A5_1 }
				</th>
				<th>
					<c:forEach items="${supplierlists }" var="sup">
						<c:if test="${sup.PRODUCT_TYPE==1 }">
							
							<c:set var="totalMoney" value="${totalMoney+sup.YEAR5*money.A5_1   }"></c:set>
							<c:set var="moneyOU7" value="${sup.YEAR5*money.A5_1 }"></c:set>
						</c:if>
					</c:forEach>
					<c:if test="${moneyOU7==null||moneyOU7<1 }">
							0.0
						</c:if>
						<c:if test="${moneyOU7>0 }">
							<fmt:formatNumber pattern="#0.0">${moneyOU7 }</fmt:formatNumber>
						</c:if>
				</th>
			</tr>
			
			
			<tr>
				<th>因KEY非人为原因补签证书</th>
				<th>${oumap.ou8 }</th>
				<th>
					<c:if test="${supplierlists.size()==1 }">
						${supplierlists.get(0).frw_bb_amount }
						<c:set var="totolCount" value="${totolCount+supplierlists.get(0).frw_bb_amount  }"></c:set>
						<%-- <c:set var="totalMoney" value="${totalMoney+supplierlists.get(0).frw_bb_amount }"></c:set>	 --%>				
					</c:if>
					<c:if test="${supplierlists.size()==2 }">
						${supplierlists.get(1).frw_bb_amount+supplierlists.get(0).frw_bb_amount }
						<c:set var="totolCount" value="${totolCount+supplierlists.get(1).frw_bb_amount+supplierlists.get(0).frw_bb_amount  }"></c:set>
						<%-- <c:set var="totalMoney" value="${totalMoney+supplierlists.get(1).frw_bb_amount+supplierlists.get(0).frw_bb_amount }"></c:set> --%>					
					</c:if>
				</th>
				<th></th>
				<th></th>
			</tr>
			<tr>
				<th>合计</th>
				<th></th>
				<th></th>
				<th>
				${totolCount }</th>
				<th></th>
				<th>
				<fmt:formatNumber pattern="#0.0">${totalMoney }</fmt:formatNumber>
				</th>
			</tr>
		</table>
		
		<table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
						<th colspan="7"><h3>VTN结算清单</h3></th>
				</tr>
				<tr>
					<th colspan="7">制表时间：${startTime } — ${endTime}</th>
				</tr>
				<tr>
					<th colspan="7">VTN供应商：
					<c:forEach items="${configSuppliers}" var="supplier">
						<c:if test="${supplier.id==vtnUpplierId }">${supplier.supplierName}</c:if>
					</c:forEach>
					</th>
				</tr>
				<tr>
					<th>产品名称</th>
					<th>应用名称</th>
					<th>数量/张</th>
					<th>单价/元</th>
					<th>小计/元</th>
				</tr>
				<c:forEach items="${settleVTNs }" var="sup">
					<tr>
						<th>${sup.PRODUCT_NAME}</th>
						<th>${sup.APP_NAME}</th>
						<th>${sup.COUNTTOTAL}</th>
						<th>
						<fmt:formatNumber value="${sup.onePrice}" pattern="#,#00.00#"></fmt:formatNumber>
						
						
						</th>
						<th>
						<fmt:formatNumber value="${sup.PRICETOTAL}" pattern="#,#00.00#"></fmt:formatNumber>
						
						</th>
					</tr>
				</c:forEach>
				<tr>
					<th>合计</th>
					<th></th>
					<th></th>
					<th></th>
					<th>
					<fmt:formatNumber value="${vtnTotalPrice}" pattern="#,#00.00#"></fmt:formatNumber>
					
					</th>
				</tr>
				
				
			</thead>
		</table>
		
	</div>
	<form action="${ctx}/settle/agentSettle/showT" name ="showTable" id = "showTable">
		<input id="showTable_officeId" type = "hidden" name = "officeId" value="${officeId }" />
		<input id="showTable_agentId" type = "hidden" name = "id" value = "${id }" />
		<input id="showTable_startTime" type = "hidden" name = "startDate" value = "${startDate }" />
		<input id="showTable_endTime" type = "hidden" name = "endDate" value = "${endDate }" />
	</form>
</body>
</html>
