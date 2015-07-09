<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
</head>
<body>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>查看业务信息</h4></th>
			</tr>
			<tr>
				<th width="10%">业务编号:</th>
				<td width="20%">${workDealInfo.svn }</td>
				<th width="10%">应用名称:</th>
				<td width="20%">${workDealInfo.configApp.appName}</td>
			</tr>
			<tr>
				<th>产品名称:</th>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<th>证书应用标识:</th>
				<td><c:if test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if>
					<c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
				</td>
			</tr>
			<tr>
				<th>业务类型:</th>
				<td>
					${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}
				</td>
				<th>申请年数:</th>
				<td>${workDealInfo.year}年</td>
			</tr>
			<tr>
				<th>移动设备申请数量:</th>
				<td><c:if
						test="${workDealInfo.workCertInfo.trustDeviceCount == null}">0</c:if>
					<c:if test="${workDealInfo.workCertInfo.trustDeviceCount != null}">${workDealInfo.workCertInfo.trustDeviceCount}</c:if>
				</td>
				<th>用户分类:</th>
				<td><c:if test="${workDealInfo.classifying==0}">内资</c:if> <c:if
						test="${workDealInfo.classifying==1}">外资</c:if></td>
			</tr>
			<tr>
				<th>多证书编号:</th>
				<td>${workDealInfo.certSort }</td>
				<th>业务状态：</th>
				<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
			</tr>
			<tr>
				<th>受理网点:</th>
				<td>${workDealInfo.createBy.office.name }</td>
				<th>Key序列号:</th>
				<td>${workDealInfo.keySn }</td>
			</tr>
			<tr>
				<th>付款方式:</th>
				<td><c:if test="${workDealInfo.workPayInfo.methodPos }">POS机付款&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodMoney }">现金交易&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodAlipay }">支付宝交易&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodGov }">政府统一采购&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodContract }">合同采购&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodBank }">银行转账&nbsp;</c:if>
					${bangD}</td>
				<th>档案编号:</th>
				<td>${workDealInfo.userSn }</td>
			</tr>
			<tr>
				<th>发票信息:</th>
				<td><c:if test="${workDealInfo.workPayInfo.userReceipt}">金额：${workDealInfo.workPayInfo.receiptAmount}</c:if>
					<c:if test="${workDealInfo.workPayInfo.userReceipt==false}">无发票信息</c:if>
				</td>
				<th>归档时间:</th>
				<td >${workDealInfo.archiveDate }</td>
			</tr>
			<tr>
				<th>付款金额:</th>
				<td>${workDealInfo.workPayInfo.workTotalMoney }</td>
				<th>归档人:</th>
				<td>${workDealInfo.updateBy.name }</td>
			</tr>
		</thead>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>单位基本信息</h4></th>
			</tr>
			<tr>
				<th width="10%">单位名称:</th>
				<td width="20%">${workDealInfo.workCompanyHis.companyName}</td>
				<th width="10%">单位类型:</th>
				<td width="20%"><c:if
						test="${workDealInfo.workCompanyHis.companyType==1}">企业</c:if> <c:if
						test="${workDealInfo.workCompanyHis.companyType==2}">事业单位</c:if> <c:if
						test="${workDealInfo.workCompanyHis.companyType==3}">政府机构</c:if> <c:if
						test="${workUser.workCompanyHis.companyType==4}">社会团体</c:if> <c:if
						test="${workUser.workCompanyHis.companyType==5}">其他</c:if></td>
			</tr>
			<tr>
				<th>组织机构代码:</th>
				<td>${workDealInfo.workCompanyHis.organizationNumber}</td>
				<th>组织机构代码有效期:</th>
				<td><fmt:formatDate
						value="${workDealInfo.workCompanyHis.orgExpirationTime}"
						pattern="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				<th>服务级别:</th>
				<td><c:if test="${workDealInfo.workCompanyHis.selectLv==0}">大客户</c:if>
					<c:if test="${workDealInfo.workCompanyHis.selectLv==1}">普通客户</c:if></td>
				<th>单位证照:</th>
				<td><c:if
						test="${workDealInfo.workCompanyHis.comCertificateType==0}">营业执照</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==1}">事业单位法人登记证</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==2}">社会团体登记证</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==3}">其他</c:if>
				</td>
			</tr>
			<tr>
				<th>单位证照号:</th>
				<td>${workDealInfo.workCompanyHis.comCertficateNumber}</td>
				<th>单位证照有效期:</th>
				<td><fmt:formatDate
						value="${workDealInfo.workCompanyHis.comCertficateTime}"
						pattern="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				<th>法人姓名:</th>
				<td>${workDealInfo.workCompanyHis.legalName}</td>
				<th>行政所属区:</th>
				<td>${workDealInfo.workCompanyHis.province}
					${workDealInfo.workCompanyHis.city}
					${workDealInfo.workCompanyHis.district}</td>
			</tr>
			<tr>
				<th>街道地址:</th>
				<td>${workDealInfo.workCompanyHis.address}</td>
				<th>备注信息:</th>
				<td>${workDealInfo.workCompanyHis.remarks}</td>
			</tr>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>经办人基本信息</h4></th>
			</tr>
			<tr>
				<th width="10%">姓名:</th>
				<td width="20%">${workDealInfo.workUserHis.contactName}</td>
				<th width="10%">性别:</th>
				<td width="20%">${workDealInfo.workUserHis.contactSex}</td>
			</tr>
			<tr>
				<th>移动电话:</th>
				<td>${workDealInfo.workUserHis.contactPhone}</td>
				<th>固定电话:</th>
				<td>${workDealInfo.workUserHis.contactTel}</td>
			</tr>
			<tr>
				<th>证件类型:</th>
				<td><c:if test="${workDealInfo.workUserHis.conCertType==0 }">身份证</c:if>
					<c:if test="${workDealInfo.workUserHis.conCertType==1 }">军官证</c:if>
					<c:if test="${workDealInfo.workUserHis.conCertType==2 }">其他</c:if>
				</td>
				<th>证件号码:</th>
				<td>${workDealInfo.workUserHis.conCertNumber }</td>
			</tr>
			<tr>
				<th>部门名称:</th>
				<td>${workDealInfo.workUserHis.department}</td>
				<th>联系人编码:</th>
				<td>${workDealInfo.workUserHis.userSn}</td>
			</tr>
			<tr>
				<th>电子邮件:</th>
				<td>${workDealInfo.workUserHis.contactEmail}</td>
				<th>联系地址:</th>
				<td>${workDealInfo.workUserHis.address}</td>
			</tr>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="5"><h4>受理记录</h4></th>
			</tr>
		</thead>
		<tr>
			<th>编号</th>
			<th>记录内容</th>
			<th>记录人</th>
			<th>受理网点</th>
			<th>记录时间</th>
		</tr>
		<c:forEach items="${workLogs }" var="workLog" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${workLog.recordContent }</td>
				<td>${workLog.createBy.name }</td>
				<td>${workLog.office.name }</td>
				<td>${workLog.createDate }</td>
			</tr>
		</c:forEach>
	</table>
	<tags:message content="${message}" />
	<div class="form-actions">
		<input id="btnCancel" class="btn" type="button" value="返 回"
			onclick="history.go(-1)" />
	</div>
</body>
</html>
