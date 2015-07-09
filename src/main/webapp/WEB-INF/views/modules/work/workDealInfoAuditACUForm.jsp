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
	function buttonFrom() {
		var saveYear = 0;
		if ($("#dealInfoType").val() == 0) {
			$("input[name=recordContent]").val($("#recordContent").val());
			$("#recordForm").submit();
		} else {
			if ($("#year1").prop("checked") == true) {
				saveYear = $("#year1").val();
			}
			if ($("#year2").prop("checked") == true) {
				saveYear = $("#year2").val();
			}
			if ($("#year4").prop("checked") == true) {
				saveYear = $("#year4").val();
			}
			if (saveYear == 0) {
				$("#mmsg").html("请选择更新年限");
				$("#mssg").html("请选择更新年限");
			} else {
				$("#inputForm").attr(
						"action",
						"${ctx}/work/workDealInfoAudit/updateLoad?id=${workDealInfo.id}&year="
								+ saveYear);
				$("#inputForm").submit();
				//					window.location.href="${ctx}/work/workDealInfoAudit/updateLoad?id=${workDealInfo.id}&recordContent="+$("#recordContent").val()+"&year="+saveYear;
			}
		}
	}
	function refuse() {
		//top.$.jBox.open("iframe:${ctx}/work/workDealInfoAudit/jujueFrom?id=${workDealInfo.id}", "审核拒绝",600,400);
		top.$.jBox
				.open(
						"iframe:${ctx}/work/workDealInfoAudit/jujueFrom?id=${workDealInfo.id}",
						"审核拒绝",
						600,
						400,
						{
							buttons : {
								"确定" : "ok",
								"取消" : true
							},
							bottomText : "填写审核拒绝的原因",
							submit : function(v, h, f) {
								var suggest = h.find("iframe")[0].contentWindow
										.$("#suggest")[0].value;
								//nodes = selectedTree.getSelectedNodes();
								if (v == "ok") {
									window.location.href = "${ctx}/work/workDealInfoAudit/jujue?id=${workDealInfo.id}&remarks="
											+ suggest;
									return true;
								}
							},
							loaded : function(h) {
								$(".jbox-content", top.document).css(
										"overflow-y", "hidden");
							}
						});
	}

	function nameFill() {
		$("#pName").val($("#contactName").val());
	}
	function emailFill(obj) {
		var a = form_check(obj);
		if (a) {
			$("#pEmail").val($("#contacEmail").val());
		}
	}
	function numberFill() {
		$("#pIDCard").val($("#conCertNumber").val());
	}
	/* 
	 * 功能：判断用户输入的手机号格式是否正确 
	 * 传参：无 
	 * 返回值：true or false 
	 */
	function checkMobile(obj) {
		var mobie = $(obj).val();
		var regu = /^[1][0-9][0-9]{9}$/;
		var re = new RegExp(regu);
		if (re.test(mobie)) {
			if ($("#phonepro").text() != "") {
				$("#phonepro").hide();
			}
			return true;
		} else {
			if ($("#phonepro").text() != "") {
				$(obj).focus(); //让手机文本框获得焦点 
				return false;
			}
			$("#contactPhone").after(
					"<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
			/* top.$.jBox.tip("请输入正确的手机号码!");  */
			$(obj).focus(); //让手机文本框获得焦点 
			return false;
		}
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfoAudit/list">业务审核列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfoAudit/auditForm?id=${workDealInfo.id}">业务审核

		</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
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
								value="${proType[workDealInfo.configProduct.productName] }" /></td>
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
							<td><c:if test="${workDealInfo.dealInfoType==0 }">
									<input type="checkbox" disabled="disabled" checked="checked"
										name="dealInfType">新增证书</c:if> <input type="hidden"
								id="dealInfoType" value="${workDealInfo.dealInfoType }">
								<c:if test="${workDealInfo.dealInfoType1==6 }">
									<input type="checkbox" disabled="disabled" checked="checked"
										name="dealInfType">电子签章</c:if> <c:if
									test="${workDealInfo.dealInfoType==1 }">
									<input type="checkbox" disabled="disabled" checked="checked"
										name="dealInfType">更新证书</c:if></td>
						</tr>
						<tr>
							<th>申请年数：</th>

							<td><c:if test="${workDealInfo.dealInfoType!=0 }">
									<input type="radio" name="year" id="year1" value="1" />1年 
									<input type="radio" name="year" id="year2" value="2" />2年 
									<input type="radio" name="year" id="year4" value="4" />4年
									<input type="radio" name="year" id="year5" value="5" />4年
									<span style="color: red" id="mmsg"></span>
								</c:if> <c:if test="${workDealInfo.dealInfoType==0 }">
									<input type="radio" name="year" value="1" disabled="disabled"
										<c:if 

test="${workDealInfo.year==1}">checked</c:if>>1年 <input
										type="radio" name="year" value="2" disabled="disabled"
										<c:if 

test="${workDealInfo.year==2}">checked</c:if>>2年 <input
										type="radio" name="year" value="4" disabled="disabled"
										<c:if 

test="${workDealInfo.year==4}">checked</c:if>>4年<input
										type="radio" name="year" value="5" disabled="disabled"
										<c:if 

test="${workDealInfo.year==5}">checked</c:if>>5年
								</c:if></td>
							<th>用户分类：</th>
							<td><input type="radio" name="yar" value="1"
								disabled="disabled"
								<c:if test="${workDealInfo.classifying==0}">checked</c:if>>

								内资 <input type="radio" name="yar" value="2" disabled="disabled"
								<c:if test="${workDealInfo.classifying==1}">checked</c:if>>

								外资</td>
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
							<td><select name="companyType" disabled="disabled">
									<option value="1" id="companyType1"
										<c:if 

test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if 

test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if 

test="${workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4"
										<c:if 

test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if 

test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th>组织机构代码：</th>
							<td><input type="text" name="organizationNumber"
								disabled="disabled"
								value="${workDealInfo.workCompany.organizationNumber}" /></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" disabled="disabled"
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate 

value="${workDealInfo.workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"></input></td>
						</tr>
						<tr>
							<th>服务级别：</th>
							<td><select name="selectLv" disabled="disabled">
									<option value="0" id="selectLv0"
										<c:if 

test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if 

test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType" disabled="disabled">
									<option
										<c:if 

test="${workDealInfo.workCompany.comCertificateType==0}">selected="selected"</c:if>>营业执照</option>
									<option
										<c:if 

test="${workDealInfo.workCompany.comCertificateType==1}">selected="selected"</c:if>>事业单位法人登记证</option>
									<option
										<c:if 

test="${workDealInfo.workCompany.comCertificateType==2}">selected="selected"</c:if>>社会团体登记证</option>
									<option
										<c:if 

test="${workDealInfo.workCompany.comCertificateType==3}">selected="selected"</c:if>>其他</option>
							</select></td>

						</tr>
						<tr>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								disabled="disabled"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate 

value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>
							<th>法人姓名：</th>
							<td><input type="text" name="legalName" disabled="disabled"
								value="${workDealInfo.workCompany.legalName}"></td>
						</tr>
						<tr>
							<th>行政所属区：</th>
							<td>${workDealInfo.workCompany.province}&nbsp;
								${workDealInfo.workCompany.city}&nbsp;
								${workDealInfo.workCompany.district}</td>
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
								id="remarks" value="${workDealInfo.workCompany.remarks 

}"></td>
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
							<th colspan="4" style="font-size: 20px;">证书持有人信息</th>
						</tr>
						<tr>
							<th>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								onblur="nameFill(this)"
								value="${workDealInfo.workUser.contactName }" /></td>
							<th>证书持有人证件:</th>
							<td><select name="conCertType">
									<option value="0" id="conCertType0"
										<c:if 

test="${workDealInfo.workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if 

test="${workDealInfo.workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if 

test="${workDealInfo.workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th>证件号码:</th>
							<td><input type="text" name="conCertNumber"
								id="conCertNumber"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.conCertNumber }"
								onblur="numberFill()" /></td>
							<th>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactEmail }"
								onblur="emailFill(this)" /></td>
						</tr>
						<tr>
							<th>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactPhone }"
								onblur="checkMobile(this)" /></td>
							<th>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th>证书持有人性别:${workDealInfo.workUser.contactSex}</th>
							<td><input name="contactSex" id="sex0"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								<c:if test="${workDealInfo.workUser.contactSex=='男'}">checked="checked"</c:if>
								type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="contactSex" id="sex1"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								<c:if test="${workDealInfo.workUser.contactSex=='女' }">checked="checked"</c:if>
								type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer"
				<%-- <c:if test="${workDealInfo.configProduct.productName!=2}"> 

style="display:none"</c:if> --%>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th>经办人姓名:</th>
							<td><input type="text" name="pName"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workCertApplyInfo.name }" /></td>
						</tr>
						<tr>
							<th>经办人身份证号:</th>
							<td><input type="text" name="pIDCard"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th>经办人邮箱:</th>
							<td><input type="text" name="pEmail"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workCertApplyInfo.email }" /></td>
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
						<c:forEach items="${workLog}" var="workLog" varStatus="status">
							<tr>
								<td>${status.count }</td>
								<td>${workLog.recordContent }</td>
								<td>${workLog.createBy.name }</td>
								<td>${workLog.createBy.office.name }</td>
								<td><fmt:formatDate value="${workLog.createDate }"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</c:forEach>
						<tr>

							<td>${workLog.size()+1 }</td>
							<td><input type="text" id="recordContent"></td>
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
	</form:form>
	<div class="form-actions"
		style="text-align: center; width: 100%; border-top: none;">
		<input type="button" class="btn btn-primary" value="通过"
			onclick="javascript:buttonFrom()" /> <input class="btn"
			type="button" value="拒绝" onclick="javascript:refuse()" /> <span
			id="mssg" style="color: red"></span>
	</div>

	<form id="recordForm" method="post" action="${ctx}/work/workDealInfoAudit/auditLoad">
		<input type="hidden" name="recordContent"> 
		<input type="hidden" name="id" value="${workDealInfo.id}">
	</form>
</body>
</html>
