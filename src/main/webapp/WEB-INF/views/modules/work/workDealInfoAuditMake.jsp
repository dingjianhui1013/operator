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
	var ukeyadmin = null;
	var baseDay = parseInt("${workDealInfo.year*365+workDealInfo.lastDays}");	
	$(document).ready(function() {
		//itrusukeyadmin.CAB,检测KEY序列号
		var urlArray = new Array();
		urlArray = window.location.toString().split('/');
	    var base = urlArray[0]+'//' + window.location.host + '/' + urlArray[3];	    
	    var objStr = "<object id='ukeyadmin2' codebase='"+base+"/download/itrusukeyadmin.cab#version=3,1,15,1012' classid='clsid:05395F06-244C-4599-A359-5F442B857C28'></object>";
	    ukeyadmin = $(objStr).appendTo(document.body)[0];
		//生成provider
		$.each(legibleNameMap, function(idx, value, ele) {
			//alert(idx+"==="+value);
			$("#provider").append("<option value='1'>" + idx + "</option>");
		});
		checkKeyGene();
		
		
		//经信委
		if(${addCertDays!=null}){
			$("#addCertDays").val(${addCertDays});
			$("#donate").hide();
			$("#addCertDays").hide();
			$("#expirationDate").show();
		}
		
		
	});

	
	function checkKeyGene(){
		var providerName = $("#provider").find("option:selected").text();
		var url = "${ctx}/key/keyUsbKeyDepot/checkKey";
		$.ajax({
			url:url,
			type:'POST',
			data:{deneName:providerName,_:new Date().getTime()},
			dataType:'json',success: function(data){
				if(data.status==0){
					top.$.jBox.tip(data.msg);
				}else if(data.status==1){
					$("#msg").html(data.msg);
					$("#makeCertButton").attr("disabled","disabled");
				}else if(data.status==2){
					$("#msg").html(data.msg);
					$("#makeCertButton").attr("disabled","disabled");
				}else if(data.status==4){
					$("#msg").html(data.msg);
					$("#makeCertButton").attr("disabled","disabled");
					
				}else if(data.status==3){
					$("#msg").html("");
					$("#makeCertButton").removeAttr("disabled");
				}
			}
		});
	}
	
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
				var keySn = $("#keySn").val();
				if(keySn=='' || keys==1){
					ukeyadmin.openKey(0);
					keySn = ukeyadmin.getkeyserialnumber(0);
				}else{
					ukeyadmin.openKey(0);
					var keySnTwo = ukeyadmin.getkeyserialnumber(0);
					if(keySn!=keySnTwo){
						keySn = keySnTwo;
					}else{
						ukeyadmin.openKey(1);
						var keySnSed = ukeyadmin.getkeyserialnumber(1);
						keySn = keySnSed;
					}
				}
				$("#keySn").attr("value", keySn);
				$("#keySn").css("color", "red");
				
				//keySn = keySnTwo;
				if(${workDealInfo.dealInfoType==0||workDealInfo.dealInfoType1==2 ||workDealInfo.dealInfoType1==3}){
				var url = "${ctx}/work/workDealInfo/findByKeySn?keySn="+keySn+"&dealId="+${workDealInfo.id}+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if (data.status==1) {
						if (data.isUser==1) {
							top.$.jBox.info("KEY序列号"+keySn+"已经使用，请更换一只新KEY！");
							return false;
						}
					}else{
						top.$.jBox.info("系统异常！");
					}
				});
				
			  }
			}
		} catch (e) {
			alert("没有检测到UKEY");
			return false;
		}
	}

	function quick(keySN) {
		var day = baseDay;
		var csr;
		var len = parseInt("${keyLen}");
		var selectedItem = $("option:selected", $("[name=provider]")[0]);
		var cspStr = selectedItem.text();
		if (cspStr.indexOf("软证书") > -1) {
			keySN = "rzs";
		}
		if (cspStr.indexOf("SM2") > -1) {
			len = 256;
		}
		//新增的生成csr
		if ($("[name=provider]").val().length > 0) {
			csr = genEnrollCSR($("[name=provider]")[0], len, 1);
		}
		//如果是更新的:
		//csr = getCsrByOldCert(len);
		csr = filter(csr);
		if (csr == "") {//异常业务
			return false;
		}
		cspStr = encodeURI(encodeURI(cspStr));
		var url = "${ctx}/ca/makeCert?reqOverrideValidity=" + day
		+ "&certProvider=" + cspStr + "&keySn=" + keySN + "&csr=" + csr
		+ "&dealInfoId=${workDealInfo.id}&addCertDays="+$("#addCertDays").val()+"&_="+new Date().getTime();
		$.ajax({
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
								top.$.jBox.tip("证书安装过程失败!");
							}
							var updateUrl = "${ctx}/ca/installResult?dealInfoId=${workDealInfo.id}&result="+t+"&_="+new Date().getTime();
							$.getJSON(
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
													var submit = function(v, h, f) {
														window.location.href = "${ctx}/work/workDealInfo/list";
														return true;
													};
													top.$.jBox(html, {
														persistent: true,
														title : "证书信息",
														submit : submit
													});
												} else {
													top.$.jBox
															.tip("出库失败，请检查是否有该类型库存");
													window.location.href = "${ctx}/work/workDealInfoAudit/list";
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
		
		try {
			var providerName=$("#provider").find("option:selected").text();
			var keys = ukeyadmin.refresh(); //检测KEY
			var providerName=$("#provider").find("option:selected").text();
			if (keys == 0 && providerName.indexOf("软证书")==-1) {
				alert("没有检测到UKEY");
			} else {
				var keySn = $("#keySn").val();
				if(providerName.indexOf("软证书")==-1)
					{
						if(keySn==''){
							top.$.jBox.tip("请您先检测KEY！");
							return false;
						}
					}
				
				sn = keySn;
			
				$("#keySn").attr("value", keySn);
				$("#keySn").css("color", "red");
				
				
				if(${workDealInfo.dealInfoType==0||workDealInfo.dealInfoType1==2 ||workDealInfo.dealInfoType1==3}){
				var url = "${ctx}/work/workDealInfo/findByKeySn?keySn="+keySn+"&dealId="+${workDealInfo.id}+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if (data.status==1) {
						if (data.isUser==1) {
							top.$.jBox.info("KEY序列号"+keySn+"已经使用，请更换一只新KEY！");
							return false;
						}else{
							
							if(data.isOK==1){
								top.$.jBox.info("此业务已经办理完成！");
								return false;
								
							}else{
								
								var selectedItem = $("option:selected", $("[name=provider]")[0]);
								var cspStr = encodeURI(encodeURI(selectedItem.text()));
								var url = "${ctx}/ca/validateCspIsValid?csp="+cspStr+"&_=" + new Date().getTime();
										$.getJSON(url,
												function(data){
												quick(sn);
										});
									
								
							}
							
							
							
						}
					}else{
						top.$.jBox.info("系统异常！");
					}
				});
				
				}
				
			}
		} catch (e) {
			alert("没有检测到UKEY");
			return false;
		}
		
	}

	function getCsrByOldCert(len) {
		useOldKey = true;
		setOverlapPeriod(30);
		var certArray = filterCerts("", 0, "");//查找当前第一张证书,被更新的
		var objOldCert;
		for (var i = 0; i < certArray.length; i++) {
			if (certArray[i].CSP == csp) {
				objOldCert = certArray[i];
				break;
			}
		}
		try {
			var csr = genRenewCSR(external.cspname, 1, len, objOldCert,
					useOldKey);
			if (csr.length == 0) {
				return "";
			}
			return filter(csr);
		} catch (e) {
			$("#textMsg").html(
					"证书更新过程中发生错误。\n错误原因：" + e.description + "\n错误代码："
							+ e.number);
			//return false;
		}
	}

	function newKey(obj) {
		if ($(obj).prop("checked")) {
			top.$.jBox.tip("初始化前请选择正确的CSP");
			$("#csh").show();
		} else {
			$("#csh").hide();
		}
	}
	function keyInit() {
		if($("#keySn").val()==""){
			selectKeyNum();
		}
		var selectedItem = $("option:selected", $("[name=provider]")[0]);
		var cspStr = encodeURI(selectedItem.text());
		top.$.jBox
				.open(
						"iframe:${ctx}/work/workDealInfo/initKey?_="+new Date().getTime()+"&csp="+cspStr+"&keySn="+$("#keySn").val(),
						"key初始化",
						800,
						600,
						{
							buttons : {
								"关闭" : true
							},
							submit : function(v, h, f) {
								
							},
							loaded : function(h) {
								$(".jbox-content", top.document).css(
										"overflow-y", "hidden");
							}
						});
	}
	function addCertDaysCheck(){
		if($("#addCertDays").val()==""){
			$("#addCertDays").val("0");
		}
		
		if($("#addCertDays").val()<0){
			$("#negative").html("");
			if(parseInt(${workDealInfo.year*365+workDealInfo.lastDays})+parseInt($("#addCertDays").val())>0)
				{
					$("#negative").html("<font color='red'>"+"您确定赠送的时间是："+$("#addCertDays").val()+"天么？"+"</font>");
				}else
					{
						top.$.jBox.tip("赠送天数填写有误请重新填写");
						$("#addCertDays").val("0");
					}
// 			var submit = function( v, h, f){
// 				if( v != 'ok'){
// 					$("#addCertDays").val(0);
// 				}				
// 				return true;
// 			}
// 			top.$.jBox.confirm("您确定赠送的时间是："+$("#addCertDays").val()+"天么？","提示",submit);
			
		}else
			{
				$("#negative").html("");
			}
	}
	
	function returnDZZ(id){
		var submit = function (v, h, f) {
		    if (v == 'ok'){
				window.location.href = "${ctx}/work/workDealInfo/returnDZZ?dealId="+id;
		    }
		   		 return true; //close
		};
		top.$.jBox.confirm("是否重新鉴证？", "提示", submit);
		
		
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
				<td style="width: 50%">业务类型：
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
				<td><c:if test="${workDealInfo.year==0&&workDealInfo.expirationDate==null }">${workDealInfo.year*365+workDealInfo.lastDays}</c:if>
					<c:if test="${workDealInfo.year!=0&&workDealInfo.expirationDate==null }">${workDealInfo.year*365+workDealInfo.lastDays}</c:if>
					<c:if test="${workDealInfo.expirationDate!=null }">${validiteDays}</c:if>
				&nbsp;
				<span id="donate">赠送</span><input
					type="text" style="width: 100px" id="addCertDays"
					class="num required" onblur="addCertDaysCheck()" value="0" onkeyup="value=value.replace(/[^\- \d.]/g,'')" >天
				&nbsp; &nbsp; &nbsp; &nbsp;	
					<span id="expirationDate" style="display: none">指定截止日期为:<fmt:formatDate pattern="yyyy-MM-dd" value="${expirationDate}"/></span>
					
					<span id="negative"></span>
				</td>
			</tr>
			<tr>
				<td>CSP</td>
				<td><select name="provider" id="provider"
					onchange="checkKeyGene()">
				</select> <input type="checkbox" name="keyStatus" onclick="newKey(this)" />新Key制证</td>
			</tr>
			<tr>
				<td>key序列号</td>
				<td><input type="text" id="keySn" /> <input type="button"
					class="btn btn-primary" value="检测key"
					onclick="javascript:selectKeyNum();" /> <input type="button"
					style="display: none" class="btn btn-inverse" value="初始化key"
					id="csh" onclick="keyInit()" /></td>
			</tr>
		</table>
		<form>
			<input type="hidden" value="" name="" /> <input type="hidden"
				value="" name="" />
		</form>
	</div>
	<div class="form-actions"
		style="text-align: center; width: 100%; border-top: none;">
		<input class="btn btn-primary" type="button" id="makeCertButton"
			onclick="makeCert()" value="制 证" />&nbsp; <input
			class="btn btn-primary" type="button" onclick="history.go(-1)"
			value="返回" />&nbsp; <input
			class="btn btn-primary" type="button" onclick="returnDZZ(${workDealInfo.id})"
			value="重新鉴证" />&nbsp;<label id="msg" style="color: red;"></label>
	</div>
</body>
</html>
