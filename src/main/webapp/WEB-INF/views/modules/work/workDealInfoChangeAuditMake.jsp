<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>制证</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/cert/pta_topca.js"></script>
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript">
	var sn;
	var cspStr;
	var ukeyadmin = null;
	var baseDay = parseInt("${workDealInfo.year*365+workDealInfo.lastDays }");
	$(document)
			.ready(
					function() {
						//itrusukeyadmin.CAB,检测KEY序列号
						var urlArray = new Array();
						urlArray = window.location.toString().split('/');
						var base = urlArray[0] + '//' + window.location.host
								+ '/' + urlArray[3];
						var objStr = "<object id='ukeyadmin2' codebase='"+base+"/download/itrusukeyadmin.cab#version=3,1,15,1012' classid='clsid:05395F06-244C-4599-A359-5F442B857C28'></object>";
						ukeyadmin = $(objStr).appendTo(document.body)[0];

						//生成provider
						$.each(legibleNameMap, function(idx, value, ele) {
							$("#provider").append(
									"<option value='1'>" + idx + "</option>");
						});
					});

	function buttonFrom() {
		window.location.href = "${ctx}/work/workDealInfoAudit/makeDealInfo?id=${workDealInfo.id}";
	}
	function backMoney() {
		window.location.href = "${ctx}/work/workDealInfoAudit/list";
	}
	function refuse() {
		window.location.href = "${ctx}/work/workDealInfoAudit/list";
	}

	function selectKeyNum() {
		try {
			var keys = ukeyadmin.refresh(); //检测KEY
			if (keys == 0) {
				alert("没有检测到UKEY");
			} else {
				ukeyadmin.openKey(0);
				var keySn = ukeyadmin.getkeyserialnumber(0);
				sn = keySn;
				$("#keySn").attr("value", keySn);
				$("#keySn").css("color", "red");
			}
		} catch (e) {
			alert("没有检测到UKEY");
		}
	}

	function quick(keySN) {
	
		
		var day = baseDay + parseInt($("#addCertDays").val());
		var csr;
		var len = 1024;
		var selectedItem = $("option:selected", $("[name=provider]")[0]);
		cspStr = selectedItem.text();
		if (cspStr.indexOf("软证书") > -1) {
			keySN = "rzs";
		}
		if (cspStr.indexOf("SM2") > -1) {
			len = 256;
		}
		//新增的生成csr
		//if ($("[name=provider]").val().length > 0) {
		//	csr = genEnrollCSR($("[name=provider]")[0], len, 1);
		//}
		//如果是更新的:
		csr = getCsrByOldCert(len);

		csr = filter(csr);
		if (csr == "") {//异常业务
			return false;
		}
		cspStr = encodeURI(encodeURI(cspStr));
		var url = "${ctx}/ca/makeCert?reqOverrideValidity=" + day
				+ "&certProvider=" + cspStr + "&keySn=" + keySN + "&csr=" + csr
				+ "&dealInfoId=${workDealInfo.id}&_=" + new Date().getTime();
		$
				.ajax({
					url : url,
					async : false,
					dataType : 'json',
					success : function(data) {
						if (data.status == 1) {
							var t = 0;
							try {
								$("#sort").html(data.sort);
								var install_result = DoInstallCert(data);
								if (install_result) {
									t = 1;
									top.$.jBox.tip("安装证书成功!");

								} else {
									top.$.jBox
											.info("证书获取失败,请检查您的证书是否输入,有误或是否已插入USB KEY");
								}
							} catch (e) {
							}
							
							var updateUrl = "${ctx}/ca/installResult?dealInfoId=${workDealInfo.id}&result="
									+ t + "&_=" + new Date().getTime();
							$
									.getJSON(
											updateUrl,
											function(res) {
												if (res.status == 1) {
													var html = "<div class='control-group'><label class='control-label'>证书序列号:</label><div class='controls'>"
															+ data.sn
															+ "</div></div>";
													html += "<div class='control-group'><label class='control-label'>颁发者:</label><div class='controls'>"
															+ data.issuer
															+ "</div></div>";
													html += "<div class='control-group'><label class='control-label'>主题:</label><div class='controls'>"
															+ data.subject
															+ "</div></div>";
													html += "<div class='control-group'><label class='control-label'>有效起止日期:</label><div class='controls'>"
															+ data.notbefore
															+ "至"
															+ data.notafter
															+ "</div></div>";
												

													var changeCertValUrl = "${ctx}/signature/signatureInfo/changeCertVal?certSn="+data.sn+"&keySn="+keySN+"&_="+new Date().getTime();
													$.getJSON(changeCertValUrl,function(effect) {
														      if(effect.status==-1){
														    	  top.$.jBox.tip("印章操作失败 "+effect.msg);
														      }		
													});
													
													var submit = function (v, h, f) {
														window.location.href = "${ctx}/work/workDealInfo/list";
													    return true;
													};
													top.$.jBox(html, { title: "证书信息", submit: submit });
													
													if(res.isChange==true){top.$.jBox.tip("请及时完成印章变更操作！");}			
															
															
															
												} else {
													top.$.jBox
															.tip("出库失败，请检查是否有该类型库存");
													window.location.href = "${ctx}/work/workDealInfo/list";
												}
											});
						} else {
							alert(data.msg);
							window.location.href = "${ctx}/work/workDealInfoAudit/exceptionList?dealInfoStatus=1";
						}
					}
				});
	}

	function makeCert() {
		var providerName = $("#provider").find("option:selected").text();
		if (providerName.indexOf("软证书") >= 0) {
			var selectedItem = $("option:selected", $("[name=provider]")[0]);
			var cspStr = encodeURI(encodeURI(selectedItem.text()));
			var url = "${ctx}/ca/validateCspIsValid?csp=" + cspStr + "&_="
					+ new Date().getTime();
			$.getJSON(url, function(data) {
				if (data.status == 1) {
					quick(sn);
				} else {
					top.$.jBox.tip("库存中没有该Key类型");
				}
			});
		} else {

			var selectedItem = $("option:selected", $("[name=provider]")[0]);
			var cspStr = encodeURI(encodeURI(selectedItem.text()));
			var url = "${ctx}/ca/validateCspIsValid?csp=" + cspStr + "&_="
					+ new Date().getTime();
			$.getJSON(url, function(data) {
				quick(sn);
			});

		}

	}

	function getCsrByOldCert(len) {
		useOldKey = true;
		//setOverlapPeriod(30);
		var certArray = filterCerts("", 0, "${signSerialNumber}");//查找当前第一张证书,被更新的
		var objOldCert;
		var csp = legibleNameMap[cspStr];
		for (var i = 0; i < certArray.length; i++) {
			if (certArray[i].CSP == csp) {
				objOldCert = certArray[i];
				break;
			}
		}
		try {
			var csr = genRenewCSR(csp, 1, len, objOldCert, useOldKey);
			if (csr.length == 0) {
				return "";
			}
			return filter(csr);
		} catch (e) {
			top.$.jBox.tip("证书更新过程中发生错误,需要证书:${signSerialNumber}");
			//return false;
		}
	}
	function newKey(obj) {
		if ($(obj).prop("checked")) {
			$("#csh").show();
		} else {
			$("#csh").hide();
		}
	}

	function addCertDaysCheck() {
		if ($("#addCertDays").val() < 0) {
			var submit = function(v, h, f) {
				if (v != 'ok') {
					$("#addCertDays").val(0);
				}
				return true;
			}
			top.$.jBox.confirm("您确定赠送的时间是：" + $("#addCertDays").val() + "天么？",
					"提示", submit);
		}
	}
</script>
</head>
<body>
	<div style="display: none">
		<object id="ukeyadmin"
			codeBase="itrusukeyadmin.cab#version=3,1,15,1012"
			classid="clsid:05395F06-244C-4599-A359-5F442B857C28"></object>
	</div>

	<ul class="nav nav-tabs">
	</ul>
	<tags:message content="${message}" />
	<div class="form-horizontal">
		<div class="control-group"></div>
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="2">证书制作相关信息</th>
				</tr>
			</thead>
			<tr>
				<td>应用名称：${workDealInfo.configApp.appName} <br />证书类型：${pt[workDealInfo.configProduct.productName] }
				</td>
				<td>业务类型：
					${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;${wdiType[workDealInfo.dealInfoType3]}
					<br />应用标识： <c:if
						test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if> <c:if
						test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
				</td>
			</tr>
		</table>
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>模板项</th>
					<th>选择项</th>
					<th style="width: 50%">证书申请时填入的值</th>
				</tr>
			</thead>
			<tr>
				<td>证书CN</td>
				<td>单位名称</td>
				<td style="width: 50%">${workDealInfo.workCompany.companyName }</td>
			</tr>
			<tr>
				<td>证书SN</td>
				<td>组织机构代码</td>
				<td style="width: 50%">${workDealInfo.workCompany.organizationNumber }</td>
			</tr>
			<tr>
				<td>accountOrgunit</td>
				<td>应用名称</td>
				<td style="width: 50%">${workDealInfo.configApp.appName }</td>
			</tr>
			<tr>
				<td>证书邮件</td>
				<td>经办人邮箱</td>
				<td style="width: 50%">${workDealInfo.workUser.contactEmail }</td>
			</tr>
			<tr>
				<td>userAdditionalField4</td>
				<td>工商营业执照注册号</td>
				<td style="width: 50%">${workDealInfo.workCompany.comCertficateNumber }</td>
			</tr>
			<tr>
				<td>userAdditionalField3</td>
				<td>多证书编号(使用者编号)(SCEGB)</td>
				<td id="sort" style="width: 50%">${workDealInfo.certSort }</td>
			</tr>
			<c:forEach items="${list }" var="lis">
				<tr>
					<c:forEach items="${lis }" var="li">
						<td>${li}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="2">证书的基本信息</th>
				</tr>
			</thead>
			<tr>
				<td>证书有效期</td>
				<td>${workDealInfo.year*365+workDealInfo.lastDays }&nbsp;赠送<input
					type="text" style="width: 100px" id="addCertDays"
					class="num required" onblur="addCertDaysCheck()" value="0">天
				</td>
			</tr>
			<tr>
				<td>CSP</td>
				<td><select name="provider" id="provider">
				</select></td>
			</tr>
			<tr>
				<td>key序列号</td>
				<td><input type="text" id="keySn" /> <input type="button"
					class="btn btn-primary" value="检测key"
					onclick="javascript:selectKeyNum();" /></td>
			</tr>
		</table>
		<form>
			<input type="hidden" value="" name="" /> <input type="hidden"
				value="" name="" />
			<div class="form-actions"
				style="text-align: center; width: 100%; border-top: none;">
				<input class="btn btn-primary" type="button" onclick="makeCert()"
					value="制 证" />&nbsp;
			</div>
		</form>
	</div>
</body>
</html>
