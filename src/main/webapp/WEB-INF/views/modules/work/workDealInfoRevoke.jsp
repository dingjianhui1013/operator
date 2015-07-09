<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css" rel="stylesheet" />
<script type="text/javascript">
document.onkeydown = function(event) {
	var target, code, tag;
	if (!event) {
		event = window.event; //针对ie浏览器  
		target = event.srcElement;
		code = event.keyCode;
		if (code == 13) {
			tag = target.tagName;
			if (tag == "TEXTAREA") {
				return true;
			} else {
				return false;
			}
		}
	} else {
		target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
		code = event.keyCode;
		if (code == 13) {
			tag = target.tagName;
			if (tag == "INPUT") {
				return false;
			} else {
				return true;
			}
		}
	}
};
</script>
<script type="text/javascript">
		function revoke(dealInfoId){
			var url = "${ctx}/ca/revokeCert?dealInfoId=";
			$.getJSON(
					url + dealInfoId+"&_="+new Date().getTime(),
					function(data){
						if (data.status==1){
							top.$.jBox
							.tip("吊销成功");
							window.location.href="${ctx}/work/workDealInfo/list";
						}
						if (data.status==-1){
							top.$.jBox
							.tip("吊销失败");
							window.location.href="${ctx}/work/workDealInfo/list";
						}
					}
			)
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/list">业务办理列表</a></li>
		<li class="active"><a href="${ctx}/work/workDealInfoOperation/revokeFrom?id=${workDealInfo.id}">业务吊销</a></li>
	</ul>
	
	<form:form id="inputForm" action=""
		method="POST" class="form-horizontal">
		<tags:message content="${message}" />
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">基本信息</th>
						</tr>
						<tr>
							<th>代办应用：</th>
							<td><input type="text" name="configApp" disabled="disabled"
								value="${workDealInfo.configApp.appName }" id="app" /></td>
							<th>选择产品：</th>
							<td><input type="text" name="product" disabled="disabled"
								value="${pro[workDealInfo.configProduct.productName] }" /></td>
						</tr>
						<tr>
							<th>应用标识：</th>
							<td><input type="radio" disabled="disabled" name="lable"
								<c:if test="${workDealInfo.configProduct.productLabel==0 }">checked="checked"</c:if>
								id="lable0" value="0">通用 &nbsp; &nbsp; <input
								type="radio" disabled="disabled" name="lable"
								<c:if test="${workDealInfo.configProduct.productLabel==1 }">checked="checked"</c:if>
								id="lable1" value="1">专用</td>
							<th>业务类型：</th>
							<td><input type="checkbox" disabled="disabled" checked="checked" name="dealInfType" >吊销证书</td>
						</tr>
						<tr>
							<th>申请年数：</th>
							<td><input type="radio" name="year" value="1" disabled="disabled" 
								<c:if test="${workDealInfo.year==1}">checked</c:if>>1年 <input
								type="radio" name="year" value="2"	disabled="disabled" 
								<c:if test="${workDealInfo.year==2}">checked</c:if>>2年 <input
								type="radio" name="year" value="4" disabled="disabled" 
								<c:if test="${workDealInfo.year==4}">checked</c:if>>4年</td>
							<th>用户分类：</th>
							<td><input type="radio" name="yar" value="1"
								disabled="disabled"
								<c:if test="${workDealInfo.classifying==0}">checked</c:if>>内资
								<input type="radio" name="yar" value="2" disabled="disabled"
								<c:if test="${workDealInfo.classifying==1}">checked</c:if>>外资
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">单位信息</th>
						</tr>
						<tr>
							<th>单位名称：</th>
							<td><input type="text" name="companyName"
								disabled="disabled"
								value="${workDealInfo.workCompany.companyName}"></td>
							<th>单位类型：</th>
							<td>
								<select name="companyType" disabled="disabled">
									<option value="1" id="companyType1" <c:if test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2" <c:if test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3" <c:if test="${workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4" <c:if test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5" <c:if test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
								</select>
							</td>
							
						</tr>
						<tr>
							<th>组织机构代码：</th>
							<td><input type="text" name="organizationNumber"
								disabled="disabled"
								value="${workDealInfo.workCompany.organizationNumber}" /></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" disabled="disabled"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>
							
						</tr>
						<tr>
							<th>服务级别：</th>
							<td><select name="selectLv" disabled="disabled">
									<option value="0" id="selectLv0" <c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1" <c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
								</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType" disabled="disabled">
									<option
										<c:if test="${workDealInfo.workCompany.comCertificateType==0}">selected="selected"</c:if>>营业执照</option>
									<option
										<c:if test="${workDealInfo.workCompany.comCertificateType==1}">selected="selected"</c:if>>事业单位法人登记证</option>
									<option
										<c:if test="${workDealInfo.workCompany.comCertificateType==2}">selected="selected"</c:if>>社会团体登记证</option>
									<option
										<c:if test="${workDealInfo.workCompany.comCertificateType==3}">selected="selected"</c:if>>其他</option>
							</select></td>
							
						</tr>
						<tr>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								disabled="disabled"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>
							<th>法人姓名：</th>
							<td><input type="text" name="legalName" disabled="disabled"
								value="${workDealInfo.workCompany.legalName}"></td>
						</tr>
						<tr>
							<th>行政所属区：</th>
							<td>${workDealInfo.workCompany.province}&nbsp;
								${workDealInfo.workCompany.city}&nbsp;
								${workDealInfo.workCompany.district}
							</td>
							<th>街道地址：</th>
							<td><input type="text" name="address" disabled="disabled"
								value="${workDealInfo.workCompany.address}"></td>
						</tr>
						<tr>
							<th>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								disabled="disabled"
								value="${workDealInfo.workCompany.comCertficateNumber}" /></td>
							<th>单位联系电话：</th>
							<td><input type="text" name="companyMobile"
								disabled="disabled" id="companyMobile"
								value="${workDealInfo.workCompany.companyMobile }"></td>
						</tr>
						<tr>
							<th>备注信息：</th>
							<td><input type="text" name="remarks" disabled="disabled"
								id="remarks" value="${workDealInfo.workCompany.remarks }"></td>
						</tr>

					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th>经办人姓名:</th>
							<td><input type="text" name="contactName" id="contactName"
								disabled="disabled"
								value="${workDealInfo.workUser.contactName }" /></td>
							<th>经办人证件:</th>
							<td><select name="conCertType" disabled="disabled">
									<option value="0" id="conCertType0"
										<c:if test="${workDealInfo.workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workDealInfo.workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workDealInfo.workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th>证件号码:</th>
							<td><input type="text" name="conCertNumber"
								id="conCertNumber" disabled="disabled"
								value="${workDealInfo.workUser.conCertNumber }" /></td>
							<th>经办人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								disabled="disabled"
								value="${workDealInfo.workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th>经办人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								disabled="disabled"
								value="${workDealInfo.workUser.contactPhone }" /></td>
							<th>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel"
								disabled="disabled" value="${workDealInfo.workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th>经办人性别:</th>
							<td><input name="contactSex" id="sex0" disabled="disabled" <c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if> type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="contactSex" id="sex1" disabled="disabled" <c:if test="${workDealInfo.workUser.contactSex=='女' }">checked</c:if> type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer" <c:if test="${workDealInfo.configProduct.productName!=2}"> style="display:none"</c:if>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr><th colspan="4" style="font-size: 20px;">申请人信息</th></tr>
						<tr>
							<th>申请人姓名:</th>
							<td><input type="text" name="pName" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" /></td>
						</tr>
						<tr>
							<th>身份证号:</th>
							<td><input type="text" name="pIDCard" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th>申请人邮箱:</th>
							<td><input type="text" name="pEmail" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.email }" /></td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="5" style="font-size: 20px;">工作信息记录</th>
						</tr>
						<tr>
							<th>编号</th>
							<th>记录内容</th>
							<th>记录人</th>
							<th>受理网点</th>
							<th>记录时间</th>
						</tr>
						<!--<c:forEach items="${workLog}" var="workLog" varStatus="status">
							<tr>
								<td>${status.count }</td>
								<td>${workLog.recordContent }</td>
								<td>${workLog.createBy.name }</td>
								<td>${workLog.createBy.office.name }</td>
								<td><fmt:formatDate value="${workLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</c:forEach>-->
						<tr>
							<td>1</td>
							<td><input type="text" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" id="appId" name="appId" />
		<input type="hidden" name="deal_info_status" value="5">
		<div class="control-group span12">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr>
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2"><shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="button" onclick="revoke(${workDealInfo.id})" 
										value="吊  销" />&nbsp;</shiro:hasPermission> <input id="btnCancel" class="btn"
								type="button" value="返 回" onclick="history.go(-1)" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>
</html>
