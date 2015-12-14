<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>退费</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/cert/pta_topca.js"></script>
<script type="text/javascript" src="${ctxStatic}/cert/xenroll.js"></script>
<script type="text/javascript">
	var sn;
	var cspStr;
	var ukeyadmin = null;
	$(document).ready(function() {
		var revoke = '${revoke}';
		if(revoke=="true"){
			$("#keySn").removeAttr("readonly");
			$("#keyButton").show();
			var urlArray = new Array();
			urlArray = window.location.toString().split('/');
		    var base = urlArray[0]+'//' + window.location.host + '/' + urlArray[3];	    
		    var objStr = "<object id='ukeyadmin2' codebase='"+base+"/download/itrusukeyadmin.CAB#version=3,1,15,1012' classid='clsid:05395F06-244C-4599-A359-5F442B857C28'></object>";
		    ukeyadmin = $(objStr).appendTo(document.body)[0];
		    
			//生成provider
			$.each(legibleNameMap, function(idx, value, ele) {
				$("#provider").append("<option value='1'>" + idx + "</option>");
			});
			
		}else{
			var boundLabelList = "${boundLabelList}";
			var lable = "${workDealInfo.configProduct.productLabel}";
			$("#agentId").attr("onchange","setStyleList("+lable+")");
			var agentHtml="";
			var obj= $.parseJSON(boundLabelList);
			$.each(obj, function(i, item){
				 if(item==1){
					if (item=="${workDealInfo.payType}") {
						agentHtml+="<option selected='selected' value='"+item+"'>标准</option>";
					}else{
						agentHtml+="<option value='"+item+"'>标准</option>";
					}
					
				}else if(item==2){
					if (item=="${workDealInfo.payType}") {
						agentHtml+="<option selected='selected' value='"+item+"'>政府统一采购</option>";
					}else{
						agentHtml+="<option value='"+item+"'>政府统一采购</option>";
					}
				}else if(item==3){
					if (item=="${workDealInfo.payType}") {
						agentHtml+="<option selected='selected' value='"+item+"'>合同采购</option>";
					}else{
						agentHtml+="<option value='"+item+"'>合同采购</option>";
					}
				} 
			}); 
			$("#agentId").html(agentHtml);
			var productName = ${workDealInfo.configProduct.productName}; 
			var app = ${workDealInfo.configApp.id};
			
			var agentId = $("#agentId").val();
			if (agentId!=0) {
				var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+app+"&infoType=1&style="+agentId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					var styleList = data.array;
					var styleHtml="";
					$.each(styleList,function(i,item){
						if(item.agentId=="${workDealInfo.configChargeAgentId}"){
							styleHtml +="<option selected='selected'  value='"+item.agentId+"'>" + item.name + "</option>";
							$("#boundId").val(item.id);
						}else{
							styleHtml +="<option value='"+item.agentId+"'>" + item.name + "</option>";
						}
					});
					$("#agentDetailId").html(styleHtml);
				});
			}
		}
	});
	
	function selectKeyNum() {
		var dealKeySn = "${workDealInfo.keySn}";
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
				if (dealKeySn!=keySn) {
					alert("业务办理时keySn与此keySn不相同，请手动调节keySn序列号");
					$("#isSame").val("buyiyang");
				}else{
					$("#isSame").val("yiyang");
				}
			}
		} catch (e) {
			alert("没有检测到UKEY");
		}
	}
	
	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleList(obj){
		var lable = obj;
		var productName = ${workDealInfo.configProduct.productName}; 
		var app = ${workDealInfo.configApp.id};
		var agentId = $("#agentId").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+app+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				var styleList = data.array;
				var styleHtml="";
				$.each(styleList,function(i,item){
					if(i==0){
						$("#boundId").val(item.id);
					}
					styleHtml +="<option value='"+item.agentId+"'>" + item.name + "</option>";
				});
				$("#agentDetailId").html(styleHtml);
			});
		}else{
			top.$.jBox.tip("请您选择产品！");
			
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
			.info("key里边没有序列号为${signSerialNumber}的证书");
			return false;
		} 
	}
	
	

	function buttonFrom(){
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
		csr = getCsrByOldCert(len);
		if(csr){
			var keySnDealInfo = ${workDealInfo.keySn };
			var keySn = $("#keySn").val();
			if(keySnDealInfo != keySn){
				top.$.jBox.tip("业务办理时keySn与此keySn不相同，请手动调节keySn序列号");
			}else{
				var revoke = 1;//不吊销
				if($("#revoke").prop("checked")){
					revoke = 0;//吊销
				}
					window.location.href="${ctx}/work/workDealInfoAudit/backMoney1?id=${workDealInfo.id}&revoke="+revoke+"&keySn="+keySn+"&receiptAmount="+$("#tfpVal").val();
			}
		}
	}
	
	function selectKeyNum() {
		var dealKeySn = "${workDealInfo.keySn}";
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
				if (dealKeySn!=keySn) {
					alert("当前KEY与办理业务使用的KEY不一致！");
					$("#isSame").val("buyiyang");
				}else{
					$("#isSame").val("yiyang");
				}
			}
		} catch (e) {
			alert("没有检测到UKEY");
		}
	}
	function changeInputStatus(obj) {
		var nextInput = $(obj).parent().next().find("input")[0];
		var nextButton = $(nextInput).parent().next().find("input")[0];
		if ($(obj).prop("checked")) {
			$(nextInput).removeAttr("readonly");
			if (nextButton!=""){
				$(nextButton).show();
			}
		} else {
			//console.log("yyyy");
			$(nextInput).attr("readonly", "readonly");
			$(nextInput).val("");
			if (nextButton!=""){
				$(nextButton).hide();
			}
		}
	}
	
	function checkAgent(){
		var type = "${workDealInfo.dealInfoType}";
		if(type=='12'){
			top.$.jBox.tip("不能重复更改缴费类型！");
			return false;
		}
		var agentDetailId = $("#agentDetailId").val();
		var configChargeAgentId = ${workDealInfo.configChargeAgentId};
		if(agentDetailId == configChargeAgentId){
			top.$.jBox.tip("计费策略模板相同，请重新选择您要更换的模板！");
		}else{
			var url = "${ctx}/work/workDealInfoAudit/getAgentMoney?dealInfoId="+${workDealInfo.id}+"&newAgentId="+agentDetailId + "&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if (data.status==0) {
					var html = "";
					var iseq = 0;
					if(parseFloat(data.oldAdd)>parseFloat(data.newAdd)){
						var money = data.oldAdd-data.newAdd;
						html = "变更此缴费方式需要退回"+money+"元！您确定继续变更缴费方式么？";
						iseq = 1;
					}else if(parseFloat(data.oldAdd)<parseFloat(data.newAdd)){
						var money = data.newAdd - data.oldAdd;
						html = "变更此缴费方式需要缴纳"+money+"元！您确定继续变更缴费方式么？";
						var iseq = 2;
					}else if(parseFloat(data.oldAdd)==parseFloat(data.newAdd)){
						html = "变更此缴费方式不需要缴纳或退回任何费用！您确定继续变更缴费方式么？";
						var iseq = 3;
					}
					var submit = function (v, h, f) {
					    if (v == 'ok') {
					      
					    	window.location.href="${ctx}/work/workDealInfoAudit/backMoney?id=${workDealInfo.id}&agentDetailId="+agentDetailId+"&iseq="+iseq;
					    	
					    }
					    else if (v == 'cancel') {
					        // 取消
					    }
					    return true; //close
					};

					top.$.jBox.confirm(html, "提示", submit);
				}else{
					top.$.jBox.tip("系统异常");
					
				}
		
					
				
				
			});
		}
	}
	
	
	
	
</script>
</head>
<body>
 	<div style="display: none">	
		<object id="ukeyadmin" codeBase="itrusukeyadmin.cab#version=3,1,13,705" classid="clsid:05395F06-244C-4599-A359-5F442B857C28"></object>
	</div>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed" style="width:80%;margin:0px auto;float:center">
					<tbody>
						<tr>
							<th colspan="4">收费信息</th>
						</tr>
						<tr>
							<th>单位名称:</th>
							<td>${workDealInfo.workCompany.companyName }</td>
							<th>经办人:</th>
							<td>${workDealInfo.workUser.contactName }</td>
						</tr>
						<tr>
							<th>证书类型:</th>
							<td>${product }</td>
							<th>应用标识：</th>
							<td><c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
							<c:if test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if></td>
						</tr>
						<tr>
							<th>业务类型：</th>
							<td>${workDealInfo.dealInfoType}</td>
							<th>年限：</th>
							<td>${workDealInfo.year }</td>
						</tr>							
					</tbody>
				</table>

			</div>
		</div>
		<div class="row-fluid" id="view"
			style=" width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>收款编号</th>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>收费备注</th>
						</tr>
					</thead>
					<tbody id="financeTd">
							<c:forEach items="${fpir }" var="list">
						<tr id="f+${list.sn}">
							<td>${list.sn }</td>
							<td>${list.financePaymentInfo.company }</td>
							<td>${list.money }</td>
							<td>${list.financePaymentInfo.createBy.office.name }</td>
							<td>${list.financePaymentInfo.createBy.name }</td>
							<td>
							<fmt:formatDate value="${list.financePaymentInfo.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td><c:if test="${list.financePaymentInfo.paymentMethod==1 }">现金</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==2 }">POS收款</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==3 }">银行转账</c:if>
							<c:if test="${list.financePaymentInfo.paymentMethod==4 }">支付宝付款</c:if></td>
							<td>${list.financePaymentInfo.remarks }</td>
						</tr>
					</c:forEach>
					<c:forEach var="info" items="${infos }">
						<tr>
							<c:forEach var="i" items="${info }">
							<td>${i }</td>
							</c:forEach>
						</tr>
					</c:forEach>
						
					</tbody>
				</table>

			</div>
		</div>
		<div class="row-fluid"
			style="width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>收费基准</th>
							<th>开户</th>
							<th>新增</th>
							<th>更新</th>
							<th>遗失补办</th>
							<th>损坏更换</th>
							<th>信息变更</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td></td>
							<td align="center">${workDealInfo.workPayInfo.openAccountMoney==null?0:workDealInfo.workPayInfo.openAccountMoney }</td>

							<td align="center">${workDealInfo.workPayInfo.addCert==null?0:workDealInfo.workPayInfo.addCert }</td>
							<td align="center">${workDealInfo.workPayInfo.updateCert==null?0:workDealInfo.workPayInfo.updateCert }</td>
							<td align="center">${workDealInfo.workPayInfo.errorReplaceCert==null?0:workDealInfo.workPayInfo.errorReplaceCert }</td>
							<td align="center">${workDealInfo.workPayInfo.lostReplaceCert==null?0:workDealInfo.workPayInfo.lostReplaceCert }</td>
							<td align="center">${workDealInfo.workPayInfo.infoChange==null?0:workDealInfo.workPayInfo.infoChange }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		

		<div class="row-fluid" id="payTable"
			style="display: none; width: 80%; margin: 0px auto; float: center">
			<div class="span12">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>

							<th>收款编号</th>
							<th>付款单位名称</th>
							<th>收费金额</th>
							<th>收费窗口</th>
							<th>收费人</th>
							<th>收费时间</th>
							<th>收费方式</th>
							<th>收费备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="payment">
			
					</tbody>
				</table>
			</div>
		</div>
				<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-striped table-bordered table-condensed"
						style=" margin: 0px auto; float: center;vertical-align: inherit;">
						<tbody>
							<tr>
								<th colspan="4">明细</th>
							</tr>
							<tr>
								<td><input type="checkbox" value="1" name="methodMoney" id="mc" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodMoney }">checked="checked"</c:if>>现金<input
									type="text" name="money" id="money" onchange="fixMoney()"
									readonly="readonly"  value="${workDealInfo.workPayInfo.money }"></td>
								<td><input type="checkbox" value="1" name="methodPos" id="pc" disabled="disabled"
									onclick="changeInputStatus(this)"  <c:if test="${workDealInfo.workPayInfo.methodPos }">checked="checked"</c:if>>POS收款
									<input
									type="text" name="posMoney" id="pos" onchange="fixMoney()"
									readonly="readonly"  value="${workDealInfo.workPayInfo.posMoney }">
									
									</td>
								<td>
								
								</td>
								</tr>
							<tr>
								<td>
									<input type="checkbox" value="1" name="methodGov" onclick="choose(this)" id="gc" disabled="disabled"
								 		<c:if test="${agent.tempStyle == 1 }">checked="checked"</c:if>>标准：
								 		<c:if test="${agent.tempStyle == 1 }">${agent.tempName}</c:if>
								</td>
								<td>
								
								<input type="checkbox" value="1" name="methodGov" onclick="choose(this)" id="gc" disabled="disabled"
								 <c:if test="${agent.tempStyle == 2 }">checked="checked"</c:if>>政府统一采购：
								<c:if test="${agent.tempStyle == 2 }">${agent.tempName}</c:if>
								
								</td>
								<td>
								
								
								<input type="checkbox" value="1" name="methodContract" onclick="choose(this)" id="cc" disabled="disabled"
								 <c:if test="${agent.tempStyle == 3 }">checked="checked"</c:if>>合同采购：
								 <c:if test="${agent.tempStyle == 3 }">${agent.tempName}</c:if>
								 
								</td>
								<td>
									<input type="hidden" id="officeNameHid" value="${officeName }">
									<input type="hidden" id="userNameHid" value="${userName }">
									<input type="hidden" id="dateHid" value="${date }">
									<input type="hidden" id="companyHid" value="${workDealInfo.workCompany.companyName }">
								</td>
							</tr>
							<tr>
								<td>业务应收合计:</td>
								<td><input type="text" id="sumMoney" name="workTotalMoney" disabled="disabled" value="${type0+type1+type2+type3+type4+type5+type6 }"></td>
								<td>已收发票</td>
								<td>${workDealInfo.workPayInfo.receiptAmount }</td>
							</tr>
							<tr>
							<c:if test="${bgType }">
								<td>变更缴费类型为：</td>
								<td>
									计费策略类型：
									<select id="agentId" name="agentId"  >
										<option value="0">请选择</option>
									</select>
									<br>
									计费策略模版：
									<select id="agentDetailId" name="agentDetailId">
										<option value="0">请选择</option>
									</select>
								
									<!-- <input type="radio" id="gov" name="payType" checked="checked">政府统一采购
									<input type="radio" id="contract" name="payType">合同采购 -->
								</td>
								<td><input type="checkbox" id="thfapiao" onclick="changeInputStatus(this)">退还发票</td>
								<td>发票金额：<input type="text" id="tfpVal" readonly="readonly"></td>
							</c:if>
							<c:if test="${!bgType }">
								<td><input type="checkbox" id="thfapiao"
									onclick="changeInputStatus(this)">退还发票</td>
								<td>发票金额：<input type="text" id="tfpVal" readonly="readonly"></td>
								<td></td><td></td>
							</c:if>
						</tr>
						 <c:if test="${revoke }">
							<tr>
								<td><input type="checkbox" id="revoke" 
								<c:if test="${revoke }">checked="checked" disabled="disabled"</c:if> <c:if test="${!revoke }">disabled</c:if>>吊销</td>
								<td><input type="checkbox" id="tKey" <c:if test="${!tKey }">disabled</c:if> checked="checked" onclick="changeInputStatus(this)">退还key</td>
								<td>
								CSP:<select name="provider" id="provider" ></select>
								<br>
								业务key序列号：<input type="text"  readonly="readonly" value="${workDealInfo.keySn }"><br>
								当前key序列号：<input type="text" id="keySn" name="keySn" readonly="readonly">
								</td>
								<td><input type="button" class="btn btn-primary" value="检测key" id="keyButton" style="display: none"
									onclick="javascript:selectKeyNum()" />
									<input type="hidden" id="isSame"/>
									</td>
							</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="form-actions"  style="text-align: center; width: 100%; border-top: none;">
				
				
				<c:if test="${revoke }">
					<input class="btn btn-primary" type="button" value="退费" onclick="javascript:buttonFrom();"/>&nbsp;
				</c:if>
				
				<c:if test="${!revoke}">
					<input class="btn btn-primary" type="button" value="变更" onclick="javascript:checkAgent();"/>&nbsp;
				
				</c:if>
				
			    
			    
			    
			<input id="btnCancel" class="btn" type="button" value="取 消"
				onclick="history.go(-1)" />
		</div>
</body>
</html>
