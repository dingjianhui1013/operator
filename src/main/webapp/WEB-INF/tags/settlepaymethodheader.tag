<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="total"
	type="com.itrus.ca.modules.settle.vo.CertificatePayMethodDetails"
	required="true" description="支付方式汇总"%>
<%@ attribute name="theader" type="java.lang.String" required="true"
	description="列头"%>

<c:if test="${not empty total}">
	<table width="100%" class="insertTable">
		<tr>
			<th colspan="${fn:length(total.methods)}">${theader}</th>
		</tr>
		<tr>
			<c:forEach var="payMethodMap" items="${total.methods}">
				<td>${payMethodMap.value}</td>
			</c:forEach>
		</tr>
	</table>

</c:if>
