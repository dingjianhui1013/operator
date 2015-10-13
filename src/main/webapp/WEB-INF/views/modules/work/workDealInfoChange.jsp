<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
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
	function dealType(obj){
		if($(obj).prop("checked")){
			$("#delay").removeAttr("checked");
			$("#year1").removeAttr("disabled");
			$("#year2").removeAttr("disabled");
			$("#year4").removeAttr("disabled");
		} else {
			$("#delay").attr("checked","checked");
			$("#year1").attr("disabled","disabled");
			$("#year2").attr("disabled","disabled");
			$("#year4").attr("disabled","disabled");
			$("#year1").removeAttr("checked");
			$("#year2").removeAttr("checked");
			$("#year4").removeAttr("checked");
		}
	}
	function onSubmit(){
		$("#workDealInfoId").val(getCookie("work_deal_info_change"));
		delCookie("work_deal_info_change");
		return true;
	}
</script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/list">业务办理列表</a></li>
		<li class="active"><a href="${ctx}/work/workDealInfoOperation/changeFrom?id=${workDealInfo.id}">业务变更</a></li>
	</ul>
	<form:form id="inputForm" action="${ctx}/work/workDealInfoOperation/changeSave?id=${workDealInfo.id }"
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
							<td><input type="checkbox"	name="dealInfType" value="4" checked="checked" disabled="disabled">信息变更&nbsp;&nbsp;
							<input type="checkbox"	name="dealInfType1"  value="1" onclick="dealType(this)">更新证书</td>
						</tr>
						<tr>
							<th>申请年数：</th>
							<td><input type="radio" id="delay" checked="checked" disabled="disabled">不延期
								<input type="radio" name="year" value="1"
								disabled="disabled" id="year1">1年 <input type="radio" name="year"
								value="2"
								disabled="disabled" id="year2">2年 <input type="radio" name="year"
								value="4"
								disabled="disabled" id="year4">4年</td>
							<th>用户分类：</th>
							<td><input type="radio" name="yar" value="1" disabled="disabled"
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
								<select name="companyType">
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
								value="${workDealInfo.workCompany.organizationNumber}" /></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" required="required"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.orgExpirationTime}"  pattern="yyyy-MM-dd"/>"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"></input></td>
							
						</tr>
						<tr>
							<th>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0" <c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1" <c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
								</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType">
									<option value="0" <c:if test="${workCompany.comCertificateType==0 }">selected</c:if>>营业执照</option>
									<option value="1" <c:if test="${workCompany.comCertificateType==1 }">selected</c:if>>事业单位法人登记证</option>
									<option value="2" <c:if test="${workCompany.comCertificateType==2 }">selected</c:if>>社会团体登记证</option>
									<option value="3" <c:if test="${workCompany.comCertificateType==3 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								value="${workDealInfo.workCompany.comCertficateNumber}" /></td>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" required="required"
								maxlength="20" readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime}"   pattern="yyyy-MM-dd"/>"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"></input></td>
						</tr>
						<tr>
							<th>法人姓名：</th>
							<td><input type="text" name="legalName"
								value="${workDealInfo.workCompany.legalName}"></td>
							<th>行政所属区：</th>
							<td><select id="s_province" name="s_province"
								style="width: 100px;">
							</select>&nbsp;&nbsp; <select id="s_city" name="s_city"
								style="width: 100px;"></select>&nbsp;&nbsp; <select
								id="s_county" name="s_county" style="width: 100px;"></select>
								 <script
									type="text/javascript">
									_init_area();
									$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
									$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
									$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
								</script></td>
						
						</tr>
						<tr>
							<th>街道地址：</th>
							<td><input type="text" name="address"
								value="${workDealInfo.workCompany.address}"></td>
							<th>单位联系电话：</th>
							<td><input type="text" name="companyMobile"
								id="companyMobile"
								value="${workDealInfo.workCompany.companyMobile }"></td>
						</tr>
						<tr>
							<th>备注信息：</th>
							<td><input type="text" name="remarks" id="remarks"
								value="${workDealInfo.workCompany.remarks }"></td>
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
								value="${workDealInfo.workUser.contactName }" /></td>
							<th>经办人证件:</th>
							<td><select name="conCertType">
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
								id="conCertNumber"
								value="${workDealInfo.workUser.conCertNumber }" /></td>
							<th>经办人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail" class="email"
								value="${workDealInfo.workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th>经办人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								value="${workDealInfo.workUser.contactPhone }" /></td>
							<th>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel"
								value="${workDealInfo.workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th>经办人性别:</th>
							<td><input name="contactSex" id="sex0" <c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if> type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="contactSex" id="sex1" <c:if test="${workDealInfo.workUser.contactSex=='女' }">checked</c:if> type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer" <c:if test="${workDealInfo.configProduct.productName!=2&&workDealInfo.configProduct.productName!=6}"> style="display:none"</c:if>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr><th colspan="4" style="font-size: 20px;">申请人信息</th></tr>
						<tr>
							<th>申请人姓名:</th>
							<td><input type="text" name="pName" <c:if test="${workDealInfo.configProduct.productName==2||workDealInfo.configProduct.productName==6}">disabled="disabled" </c:if>
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" /></td>
						</tr>
						<tr>
							<th>身份证号:</th>
							<td><input type="text" name="pIDCard"  <c:if test="${workDealInfo.configProduct.productName==2||workDealInfo.configProduct.productName==6}">disabled="disabled" </c:if>
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th>申请人邮箱:</th>
							<td><input type="text" name="pEmail"  <c:if test="${workDealInfo.configProduct.productName==2||workDealInfo.configProduct.productName==6}">required="required" </c:if>
							 class="email"
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
		<input type="hidden" id="workDealInfoId" name="workDealInfoId" />
		<div class="control-group span12">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr>
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2"><shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return onSubmit()"
										value="提  交" />&nbsp;</shiro:hasPermission> <input id="btnCancel" class="btn"
								type="button" value="返 回" onclick="history.go(-1)" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>
</html>
