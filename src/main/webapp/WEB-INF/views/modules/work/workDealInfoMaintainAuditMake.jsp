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
	$(document).ready(function() {
		
		//itrusukeyadmin.CAB,检测KEY序列号
		
		var urlArray = new Array();
		
		urlArray = window.location.toString().split('/');
		
	    var base = urlArray[0]+'//' + window.location.host + '/' + urlArray[3];	    
	    var objStr = "<object id='ukeyadmin2' codebase='"+base+"/download/itrusukeyadmin.cab#version=3,1,15,1012' classid='clsid:05395F06-244C-4599-A359-5F442B857C28'></object>";
	    ukeyadmin = $(objStr).appendTo(document.body)[0];
	    
		//生成provider
		$.each(legibleNameMap, function(idx, value, ele) {
			$("#provider").append("<option value='1'>" + idx + "</option>");
		});
		
		if("${workDealInfo.dealInfoType}" == "0" || "${workDealInfo.dealInfoType1}" == "2" || "${workDealInfo.dealInfoType1}" == "3" ){
			checkKeyGene();
		}
		
		//经信委
		if(${addCertDays!=null}){
			$("#addCertDays").val(${addCertDays});
			$("#donate").hide();
			$("#addCertDays").hide();
			$("#expirationDate").show();
		}
		
		
		
		/* if(${isChangeBusiness==true}){
			$("#certSortTr").show();
		} */
		
		
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
		}
	}

	function quick(keySN) {
		
	
		
		var day = baseDay;
		var csr;
		var len = parseInt("${keyLen }");
		var selectedItem = $("option:selected", $("[name=provider]")[0]);
		cspStr = selectedItem.text();
		if (cspStr.indexOf("软证书") > -1) {
			keySN = "rzs";
		}
		
		
		
		if (cspStr.indexOf("SM2") > -1) {
			len = 256;
		}
		
		
		//如果是新增或遗失补办或损坏更换的:
		if("${workDealInfo.dealInfoType}"=="0" || "${workDealInfo.dealInfoType1}"=="2" || "${workDealInfo.dealInfoType1}"=="3"){
			//新增的生成csr
			if ($("[name=provider]").val().length > 0) {
				csr = genEnrollCSR($("[name=provider]")[0], len, 1);
			}
		}else{
				csr = getCsrByOldCert(len);
		}
		csr = filter(csr);
		
		if (csr == "") {//异常业务
			alert("Error: csr is null!")
			return false;
		}
		
		
		
		cspStr = encodeURI(encodeURI(cspStr));
		var url = "${ctx}/ca/makeCert?reqOverrideValidity=" + day
				+ "&certProvider=" + cspStr + "&keySn=" + keySN + "&csr=" + csr
				+ "&dealInfoId=${workDealInfo.id}&addCertDays="+$("#addCertDays").val()+/* "&certSort="+$("#certSort").val()+ */"&_="+new Date().getTime();
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
									top.$.jBox.info("证书获取失败,请检查您的证书是否输入,有误或是否已插入USB KEY");
								}
							} catch (e) {
								t = 0;
							}
							var updateUrl = "${ctx}/ca/installResult?dealInfoId=${workDealInfo.id}&result="+t+"&_="+new Date().getTime();
							$.getJSON(updateUrl,function(res) {
												if (res.status == 1) {
													var html = "<div class='control-group'><label class='control-label'>证书序列号:</label><div class='controls'>"+data.sn+"</div></div>";
													html += "<div class='control-group'><label class='control-label'>颁发者:</label><div class='controls'>"+data.issuer+"</div></div>";
													html += "<div class='control-group'><label class='control-label'>主题:</label><div class='controls'>"+data.subject+"</div></div>";
													html += "<div class='control-group'><label class='control-label'>有效起止日期:</label><div class='controls'>"+data.notbefore+"至"+data.notafter+"</div></div>";
													
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
													top.$.jBox(html, {persistent: true, title: "证书信息", submit: submit });
													
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

			
		}else
			{
				$("#negative").html("");
			}
	}
	
	function makeCert() {
		var providerName=$("#provider").find("option:selected").text();
		try {
			var keys = ukeyadmin.refresh(); //检测KEY
			if (keys == 0 && providerName.indexOf("软证书")==-1) {
				alert("没有检测到UKEY");
			} else {
				var keySn = $("#keySn").val();
// 				var providerName=$("#provider").find("option:selected").text();
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
				}else{
					var selectedItem = $("option:selected", $("[name=provider]")[0]);
					var cspStr = encodeURI(encodeURI(selectedItem.text()));
					var url = "${ctx}/ca/validateCspIsValid?csp="+cspStr+"&_=" + new Date().getTime();
					$.getJSON(url,
							function(data){
						if (data.status==1) {
							if (data.isLastOne==1) {
								top.$.jBox.info("KEY序列号与新增时的KEY序列号不同，请使用同一个KEY办理业务！");	
							}else{
								quick(sn);
							}
						} else {
							top.$.jBox.tip("库存中没有该Key类型");
						}
					});
				}
			}
		} catch (e) {
			if(providerName.indexOf("软证书")==-1)
			{
				alert("没有检测到UKEY");
			}
		}
		 
		
	}

	function getCsrByOldCert(len) {
		useOldKey = true;
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
			
			var csr = genRenewCSR(csp, 1, len, objOldCert,useOldKey);
			if (csr.length == 0) {
				return "";
			}
			return filter(csr);
		} catch (e) {
			top.$.jBox
			.info("证书更新过程中发生错误,key里边没有序列号为${signSerialNumber}的证书");
			return false;
		}
	}
	function newKey(obj){
		if($(obj).prop("checked")){
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
	
	
	function returnPayment(){
		var submit = function (v, h, f) {
		    if (v == 'ok'){
		    	var id = "${workDealInfo.id}";
				window.location.href="${ctx}/work/workPayInfo/returnPayment?workDealInfoId="+id;
		    }
		   		 return true; //close
		};
		top.$.jBox.confirm("是否重新缴费？", "提示", submit);
	}
	
	
	function cancelMaintenance(){
		var submit = function (v, h, f) {
		    if (v == 'ok'){
		    	var id = "${workDealInfo.id}";
				window.location.href = "${ctx}/work/workDealInfo/cancelMaintenanceByReturn?id="+id;
		    }
		   		 return true; //close
		};
		top.$.jBox.confirm("是否取消本次维护？", "提示", submit);
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
				<td><c:if test="${workDealInfo.expirationDate==null }">${workDealInfo.year*365+workDealInfo.lastDays }</c:if>
				<c:if test="${workDealInfo.expirationDate!=null }">${validiteDays}</c:if>
				&nbsp;
				<span id="donate">赠送</span><input
					type="text" style="width: 100px" id="addCertDays"
					class="num required" onblur="addCertDaysCheck()" value="0" onkeyup="value=value.replace(/[^\- \d.]/g,'')">天
					&nbsp; &nbsp; &nbsp; &nbsp;	
					<span id="expirationDate" style="display: none">指定截止日期为:<fmt:formatDate pattern="yyyy-MM-dd" value="${expirationDate}"/></span>
					<span id="negative"></span>
				</td>
			</tr>
			<tr>
				<td>CSP</td>
				<td><select name="provider" id="provider"
					<c:if test="${(workDealInfo.dealInfoType==0 || workDealInfo.dealInfoType1==2 || workDealInfo.dealInfoType1==3)}">
				onchange="checkKeyGene()" 
				</c:if>>
				</select> <input type="checkbox"
					<c:if test="${!(workDealInfo.dealInfoType==0 || workDealInfo.dealInfoType1==2 || workDealInfo.dealInfoType1==3)}"> style="display: none;" </c:if>
					name="keyStatus" onclick="newKey(this)" /> <label
					<c:if test="${!(workDealInfo.dealInfoType==0 || workDealInfo.dealInfoType1==2 || workDealInfo.dealInfoType1==3)}"> style="display: none;" </c:if>>新Key制证</label>



				</td>



			</tr>
			<tr>
				<td>key序列号</td>
				<td><input type="text" id="keySn" /> <input type="button"
					class="btn" value="检测key" onclick="javascript:selectKeyNum();" />

					<input type="button" style="display: none;" class="btn btn-inverse"
					value="初始化key" id="csh" onclick="keyInit()" /></td>
			</tr>
			
			<!-- <tr style="display: none"  id="certSortTr">
				<td>多证书编号</td>
				<td><input type="text" id="certSort" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                                    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}" /> 
				<font color="green">注:多证书编号错误修复</font>
				</td>
			</tr> -->
			
		</table>
		<form>
			<input type="hidden" value="" name="" /> <input type="hidden"
				value="" name="" />
			<div class="form-actions"
				style="text-align: center; width: 100%; border-top: none;">
				<input class="btn btn-primary" type="button" id="makeCertButton"
					onclick="makeCert()" value="制 证" />&nbsp;


				<c:if test="${workDealInfo.dealInfoType != 0 }">
					<input class="btn btn-primary" type="button"
						onclick="returnPayment()" value="重新缴费" />
				&nbsp;<a class="btn btn-primary"
						href="javascript:cancelMaintenance()">取消本次维护</a>
				</c:if>
				<label id="msg" style="color: red;"></label>



			</div>
		</form>
	</div>
</body>
</html>
