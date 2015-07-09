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
	</script>
</head>
<body>
	 <div class="form-horizontal" style="overflow-x:auto;overflow-y:auto;max-width: 2100px">
	 
	 <table class="table table-striped table-bordered table-condensed">
			<thead >
				<tr>
					<th style="width: 110px;"></th>
					<th style="width: 126px;"></th>
					<th style="width: 63px;"> </th>
					<c:forEach items="${dayList }" var="dayList">
					<th colspan="2">${dayList}</th>
					</c:forEach>
						<!-- 
						<c:forEach var="day" begin="1" end="${list.zCertDays.size() }">
							<th colspan="2" style="width: 126px;">${month}-<c:if test="${day>9}">${day }</c:if><c:if test="${day<10 }">0${day }</c:if></th>
						</c:forEach>
						-->
						<!--<c:forEach var="day" begin="1" end="31">
						<th colspan="2" style="width: 126px;">${month}-<c:if test="${day>9}">${day }</c:if><c:if test="${day<10 }">0${day }</c:if></th>
						</c:forEach>-->
				</tr>
				<tr>
					<th></th>
					<th></th>
					<th></th>
						<c:forEach items="${dayList }" var="dayList">
							<th style="width: 63px;">新增</th>
							<th style="width: 63px;">更新</th>
						</c:forEach>
				</tr>
			</thead>
			
			<c:forEach items="${sumList }" var="sum">
			<tr>
				<th rowspan="12">${sum.configApp.appName }</th>
				<th rowspan="4">自费企业</th>
				<td>一年期</td>
				<c:forEach items="${sum.certDays }" var="oneSm1">
					<td>${oneSm1.oneAdd1 }</td>
					<td>${oneSm1.oneRenew1 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>两年期</td>
				<c:forEach items="${sum.certDays }" var="oneSm2">
					<td>${oneSm2.oneAdd2 }</td>
					<td>${oneSm2.oneRenew2 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>四年期</td>
				<c:forEach items="${sum.certDays }" var="oneSm4">
					<td>${oneSm4.oneAdd4 }</td>
					<td>${oneSm4.oneRenew4 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>五年期</td>
				<c:forEach items="${sum.certDays }" var="oneSm5">
					<td>${oneSm5.oneAdd5 }</td>
					<td>${oneSm5.oneRenew5 }</td>
				</c:forEach>
			</tr>
			<tr>
				<th rowspan="4">合同企业</th>
				<td>一年期</td>
				<c:forEach items="${sum.certDays }" var="twoSm1">
					<td>${twoSm1.twoAdd1 }</td>
					<td>${twoSm1.twoRenew1 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>两年期</td>
				<c:forEach items="${sum.certDays }" var="twoSm2">
					<td>${twoSm2.twoAdd2 }</td>
					<td>${twoSm2.twoRenew2 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>四年期</td>
				<c:forEach items="${sum.certDays }" var="twoSm4">
					<td>${twoSm4.twoAdd4 }</td>
					<td>${twoSm4.twoRenew4 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>五年期</td>
				<c:forEach items="${sum.certDays }" var="twoSm5">
					<td>${twoSm5.twoAdd5 }</td>
					<td>${twoSm5.twoRenew5 }</td>
				</c:forEach>
			</tr>
			<tr>
				<th rowspan="4">政府统一采购</th>
				<td>一年期</td>
				<c:forEach items="${sum.certDays }" var="fourSm1">
					<td>${fourSm1.fourAdd1 }</td>
					<td>${fourSm1.fourRenew1 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>两年期</td>
				<c:forEach items="${sum.certDays }" var="fourSm2">
					<td>${fourSm2.fourAdd2 }</td>
					<td>${fourSm2.fourRenew2 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>四年期</td>
				<c:forEach items="${sum.certDays }" var="fourSm4">
					<td>${fourSm4.fourAdd4 }</td>
					<td>${fourSm4.fourRenew4 }</td>
				</c:forEach>
			</tr>
			<tr>
				<td>五年期</td>
				<c:forEach items="${sum.certDays }" var="fourSm5">
					<td>${fourSm5.fourAdd5 }</td>
					<td>${fourSm5.fourRenew5 }</td>
				</c:forEach>
			</tr>
			</c:forEach>
			
		</table>
		</div>
</body>
</html>
