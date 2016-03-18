<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content"
	type="com.itrus.ca.modules.settle.vo.CertificatePayMethodDetails"
	required="true" description="支付方式明细"%>
<%@ attribute name="total"
	type="com.itrus.ca.modules.settle.vo.CertificatePayMethodDetails"
	required="true" description="支付方式汇总数据"%>

<c:if test="${not empty content}">
	<c:if test="${not empty content.methods}">
		<table width="100%">
			<tr>
				<c:forEach var="payMethodMap" items="${total.methods}">
					<c:set var="payMethod" value="${payMethodMap.key}" />
					<td><c:if test="${payMethod eq '1' }">
					${content.methodPosCount}
					</c:if> <c:if test="${payMethod eq '10' }">
					${content.methodMoneyCount}
					</c:if> <c:if test="${payMethod eq '11' }">
					${content.methodMoneyAndPosCount}
					</c:if> <c:if test="${payMethod eq '100' }">
					${content.methodBankCount}
					</c:if> <c:if test="${payMethod eq '101' }">
					${content.methodBankAndPosCount}
					</c:if> <c:if test="${payMethod eq '110' }">
					${content.methodBankAndMoneyCount}
					</c:if> <c:if test="${payMethod eq '1000' }">
					${content.methodAlipayCount}
					</c:if> <c:if test="${payMethod eq '1001' }">
					${content.methodAlipayAndPosCount}
					</c:if> <c:if test="${payMethod eq '1010' }">
					${content.methodAlipayAndMoneyCount}
					</c:if> <c:if test="${payMethod eq '1100' }">
					${content.methodAlipayAndBankCount}
					</c:if>
					</td>
				</c:forEach>
			</tr>
		</table>
	</c:if>
</c:if>
