
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
var appData;
var selected = false;
	$(document)
			.ready(
					function() {
						$("#name").focus();
						$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});

						
						
						
						var url = "${ctx}/work/workDealInfo/app?_="+new Date().getTime();
						$
								.getJSON(
										url,
										function(d) {
											appData = d;
											$("#app")
													.bigAutocomplete(
															{
																data : d.lis,
																callback : function(
																		data) {
																	$("#pro1").removeAttr("disabled");
																	$("#pro2").removeAttr("disabled");
																	$("#pro3").removeAttr("disabled");
																	$("#pro4").removeAttr("disabled");
																	$("#pro5").removeAttr("disabled");
																	$("#pro6").removeAttr("disabled");
																	var url1 = "${ctx}/work/workDealInfo/product?appId=";

																	$
																			.getJSON(
																					url1
																							+ data.result+"&_="+new Date().getTime(),
																					function(
																							da) {
																						$(
																								"#appId")
																								.val(
																										da.appId);
																						if (!da.product1) {
																							$(
																									"#product1")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product1")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product2) {
																							$(
																									"#product2")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product2")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product3) {
																							$(
																									"#product3")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product3")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product4) {
																							$(
																									"#product4")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product4")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product5) {
																							$(
																									"#product5")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product5")
																									.attr(
																											"style",
																											"display:");
																						}
																						if (!da.product6) {
																							$(
																									"#product6")
																									.attr(
																											"style",
																											"display:none");
																						} else {
																							$(
																									"#product6s")
																									.attr(
																											"style",
																											"display:");
																						}
																					});
																}
															});
										});

						var url = "${ctx}/work/workDealInfo/tt?_="+new Date().getTime();
						$
								.getJSON(
										url,
										function(da) {
											$("#tt")
													.bigAutocomplete(
															{
																data : da.lis,
																callback : function(
																		data) {
																	var url1 = "${ctx}/work/workDealInfo/cert?id=";
																	$
																			.getJSON(
																					url1
																							+ data.result+"&_="+new Date().getTime(),
																					function(
																							d) {
																						$("#companyId")
																								.val(
																										d.companyId);
																						$(
																								"#companyName")
																								.val(
																										d.companyName);
																						if (d.workCompany.companyType == 1) {
																							$("#companyType1").attr("selected","selected");
																						}
																						if (d.companyType == 2) {
																							$("#companyType2").attr("selected","selected");
																						}
																						if (d.companyType == 3) {
																							$("#companyType3").attr("selected","selected");
																						}
																						if (d.companyType == 4) {
																							$("#companyType4").attr("selected","selected");
																						}
																						if (d.companyType == 5) {
																							$("#companyType5").attr("selected","selected");
																						}
																						$(
																								"#organizationNumber")
																								.val(
																										d.organizationNumber);
																						$(
																								"#orgExpirationTime")
																								.val(
																										d.orgExpirationTime);
																						if (d.selectLv==0) {
																							$("#selectLv0").attr("selected","selected");
																						}
																						if (d.selectLv==1) {
																							$("#selectLv1").attr("selected","selected");
																						}
																						if (d.comCertificateType == 0) {
																							$(
																									"#comCertificateType0")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 1) {
																							$(
																									"#comCertificateType1")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 2) {
																							$(
																									"#comCertificateType2")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 3) {
																							$(
																									"#comCertificateType3")
																									.attr("selected","selected");
																						}
																						if (d.comCertificateType == 4) {
																							$(
																									"#comCertificateType4")
																									.attr("selected","selected");
																						}
																						$(
																								"#comCertficateNumber")
																								.val(
																										d.comCertficateNumber);
																						$(
																								"#comCertficateTime")
																								.val(
																										d.comCertficateTime);
																						$(
																								"#legalName")
																								.val(
																										d.legalName);
																						$(
																								"#address")
																								.val(
																										d.address);
																						$(
																								"#companyMobile")
																								.val(
																										d.companyMobile);
																						$(
																								"#remarks")
																								.val(
																										d.remarks);
																						$(
																								"#s_province")
																								.val(
																										d.province);
																						$(
																								"#contactName")
																								.val(
																										d.contactName);
																						if (d.conCertType == 0) {
																							$(
																									"#conCertType0")
																									.attr("selected","selected");
																						}
																						if (d.conCertType == 1) {
																							$(
																									"#conCertType1")
																									.attr("selected","selected");
																						}
																						if (d.conCertType == 2) {
																							$(
																									"#conCertType2")
																									.attr("selected","selected");
																						}
																						if ('男' == d.conCertSex) {
																							$(
																									"#sex0")
																									.attr("checked","checked");
																						}
																						if (d.conCertSex =='女') {
																							$(
																									"#sex1")
																									.attr("checked","checked");
																						}
																						$(
																								"#contacEmail")
																								.val(
																										d.contacEmail);
																						$(
																								"#contactPhone")
																								.val(
																										d.contactPhone);
																						$(
																								"#contactTel")
																								.val(
																										d.contactTel);
																						$(
																								"#workuserId")
																								.val(
																										d.workuserId);
																						$(
																								"#conCertNumber")
																								.val(
																										d.conCertNumber);
																						$("#pName").val(d.contactName);
																						if(d.conCertType==0){
																							$("#pIDCard").val(d.conCertNumber);
																						}
																						$("#pEmail").val(d.contacEmail);
																						if(d.province != null){
																							$("#s_province").append('<option value="'+d.province+'" selected="selected">'+d.province+'</option>');
																							$("#s_city").append('<option value="'+d.city+'" selected="selected">'+d.city+'</option>');
																							$("#s_county").append('<option value="'+d.district+'" selected="selected">'+d.district+'</option>');
																						}
																						
																						showCert(d.companyId);
																					});
																}
															});
										});
						if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!=""){
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
							
							
							
							var productName = $("input[name='product']:checked").val();
							var agentId = $("#agentId").val();
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									var styleList = data.array;
									var styleHtml="";
									$.each(styleList,function(i,item){
										if(item.agentId=="${workDealInfo.configChargeAgentId}"){
											styleHtml +="<option selected='selected'  value='"+item.id+"'>" + item.name + "</option>";
											$("#boundId").val(item.id);
											showYear();
										}else{
											styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
										}
										
									});
									$("#agentDetailId").html(styleHtml);
								});
							}
						}

	});
	function nameFill(obj){
		var a = checkJbrxm(obj);
		if(a) {
			$("#pName").val($("#contactName").val());
		}

	}
	function emailFill(obj){
		var a = form_check(obj);
		if (a){
			$("#pEmail").val($("#contacEmail").val());
		}
	}
	function numberFill(){
		$("#pIDCard").val($("#conCertNumber").val());
	}
	function onSubmit(){
		//if($("#appId").val()==null) {
			//var app = $("#app").val();
			//for (var i=0; i<appData.length;i++) {
				//if (appData[i].title==app) {
				//	$("#appId").val(appData[i].result);
					//return true;
				//} else {
				//	top.$.jBox.tip("该应用不存在!"); 
				//	return false;
				//}
			//} 
		//} else {
			//return true;
		//}
		//console.log($("#productTdId").find("[name='product']"));
		//console.log($("#productTdId").find("[name='product']").length);
		var productLength = $("#productTdId").find("[name='product']").length;
		for (var a = 0; a <productLength; a++) {
			var radioIndex = $($("#productTdId").find("[name='product']")[a]);
			if(radioIndex.is(":checked")){
				selected = true;
			}
		}
		if(!selected){
			top.$.jBox.tip("请选择要办理的产品!");
			return false;
		}
		if($("#tt").val()!="" && !checkDwmc($("#tt"))){
			top.$.jBox.tip("单位名称格式有误!");
			return false;
		}
		if($("#contactName").val()!="" && !checkJbrxm($("#contactName"))){
			top.$.jBox.tip("经办人姓名格式有误!");
			return false;
		}
		
		if($("#pName").val()!="" &&!checkSqr($("#pName"))){
			top.$.jBox.tip("申请人姓名格式有误!");
			return false;
		}
		if($("#appId").val()=="") {
			$("#app").val("");
			top.$.jBox.tip("该应用不存在!"); 
			$("#app").focus(); //让手机文本框获得焦点 
			return false;
		} else if ($("input[name='year']").val() == null || $("input[name='year']").val() == ""){
			top.$.jBox.tip("请选择申请年限!"); 
			$("input[name='year']").focus(); //让手机文本框获得焦点 
			return false;
		} 
		/* else if($("input[name='payType']").val() == null || $("input[name='payType']").val() == ""){
			top.$.jBox.tip("请配置计费策略"); 
			$("input[name='payType']").focus(); //让手机文本框获得焦点 
			return false;
		} */
		else if($("#agentDetailId").val()==0){
			top.$.jBox.tip("请配置计费策略"); 
			$("#agentDetailId").focus(); 
			return false;
		}else if($("#agentDetailId").val()!=0 && $("#agentId").val()!=1){
			if($("#surplusNum").val()==0){
				top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
				return false;
			}else{
				return true;
			}
		}else {
			$("#lable0").removeAttr("disabled");
			$("#lable1").removeAttr("disabled");
			return true;
		}
		
		
		
		
		
		

	}
</script>
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
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
	function os(obj) {
		if(onSubmit()){
			if (obj == "one") {
				$("#inputForm")
				.attr("action", "${ctx}/work/workDealInfo/temporarySave");
				$("#inputForm").submit();
			}
			if (obj == "many") {
				$.ajax({
					type:"POST", //表单提交类型
					url:"${ctx}/work/workDealInfo/manySave", //表单提交目标
					data:$("#inputForm").serialize(), //表单数据
					dataType:"json",
					success:function(msg){
							if(msg.status == 1){//msg 是后台调用action时，你穿过来的参数
								top.$.jBox.tip("录入成功，可再次录入"); 
							}else{
								top.$.jBox.tip("录入失败，请重新编辑"); 
							}
						}
					});
			}
			
		}
		
	}

	function showCert(companyId) {
		var url = "${ctx}/work/workDealInfo/showCert?id=" + companyId;
		top.$.jBox.open("iframe:" + url, "已有证书明细", 800, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function(v, h, f) {
			}
		});
	}
	
	function del(data){
		
		window.location.href = "${ctx}/work/workDealInfo/delete?id=${workDealInfo.id}";
	}
	/* 
	* 功能：判断用户输入的邮箱格式是否正确 
	* 传参：无 
	* 返回值：true or false 
	*/ 
	function form_check(obj) {
		var email = $(obj).val(); //获取邮箱地址
		//判断邮箱格式是否正确
		if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email)) {
			if($("#emailpro").text()!=""){
//				$(obj).focus(); //让手机文本框获得焦点
				return false;
			}
			$("#contacEmail").after("<span id='emailpro' style='color:red'>邮箱格式错误!</span>");
		/* 	top.$.jBox.tip("邮箱格式错误!");  */
//			$(obj).focus(); //让邮箱文本框获得焦点
			return false;
		}
		if($("#emailpro").text()!=""){
			$("#emailpro").hide();
		}
		return true;
	}

	function checkDwmc(obj) {
		var dwmc = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'\-_() （） ]+$/.test(dwmc)) {
			if($("#dwmcpro").text()!=""){
				return false;
			}
			$("#tt").after("<span id='dwmcpro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#dwmcpro").text()!=""){
			$("#dwmcpro").hide();
		}
		return true;
	}
	function checkSqr(obj){
		var sqr = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'\-_() （） ]+$/.test(sqr)) {
			if($("#pNamepro").text()!=""){
				return false;
			}
			$("#pName").after("<span id='pNamepro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#pNamepro").text()!=""){
			$("#pNamepro").hide();
		}
		return true;
	}
	function checkJbrxm(obj) {
		var jbr = $(obj).val();

		if(!/^[\u4e00-\u9fa5a-zA-Z0-9\.*,'\-_() （） ]+$/.test(jbr)) {
			if($("#contactNamepro").text()!=""){
				return false;
			}
			$("#contactName").after("<span id='contactNamepro' style='color:red'>格式错误!</span>");
			return false;
		}
		if($("#contactNamepro").text()!=""){
			$("#contactNamepro").hide();
		}
		return true;
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
			if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
			return true; 
		} else { 
			if($("#phonepro").text()!=""){
//				$(obj).focus(); //让手机文本框获得焦点 
				return false; 
			}
			$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
			/* top.$.jBox.tip("请输入正确的手机号码!");  */
//			$(obj).focus(); //让手机文本框获得焦点 
			return false; 
		} 
	} 
	
	//根据商品获取引用标识
	function productLabel(data) {
		selected = true;
		var appId = $("#appId").val();
		var url = "${ctx}/work/workDealInfo/type?name=" + data + "&appId="
				+ appId+"&_="+new Date().getTime();
		$.getJSON(url, function(da) {
			if (da.type0&&da.type1){
				$("#lable0").removeAttr("disabled");
				$("#lable1").removeAttr("disabled");
			}
			if (da.type0) {
				$("#lable0").attr("checked", "checked");
				//showYear(0);
				showAgent(0)
			}
			if (da.type1) {
				$("#lable1").attr("checked", "checked");
				//showYear(1);
				showAgent(1);
			}
		});
	}

	
	/* 
	* 功能:根据产品带回计费模版
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showAgent(obj){
		var lable = obj;
		var productName = $("input[name='product']:checked").val();
		var url = "${ctx}/work/workDealInfo/showAgentProduct?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		$.getJSON(url,function(data){
			if(data.tempStyle!=-1){
				var map = data.typeMap;
				var agentHtml="";
				$("#agentId").attr("onchange","setStyleList("+lable+")");
				$.each(map, function(i, item){
					agentHtml+="<option onchange=setStyleList("+lable+")  value='"+item.id+"'>" + item.name + "</option>";
					
					
				});
				$("#agentId").html(agentHtml);
				var styleList = data.boundStyleList;
				var styleHtml="";
				$.each(styleList, function(i, item2){
					if(i==0){
						$("#boundId").val(item2.id);
						showYear();
					}
					styleHtml +="<option value='"+item2.id+"'>" + item2.name + "</option>";
				});
				$("#agentDetailId").html(styleHtml);
			}else{
				var agentHtml="<option value='0'>请选择</option>";
				$("#agentId").html(agentHtml);
				var styleHtml="<option value='0'>请选择</option>";
				$("#agentDetailId").html(styleHtml);
				
				$("#year1").show();
				$("#word1").show();
				$("#year2").show();
				$("#word2").show();
				$("#year4").show();
				$("#word4").show();
				$("#year5").show();
				$("#word5").show();
				$(".prompt").css("display","none");
				top.$.jBox.tip("请先配置计费策略！"); 
			}
			
			
		});
	}
	
	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleList(obj){
		var lable = obj;
		var productName = $("input[name='product']:checked").val();
		var agentId = $("#agentId").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				var styleList = data.array;
				var styleHtml="";
				$.each(styleList,function(i,item){
					if(i==0){
						$("#boundId").val(item.id);
						showYear();
					}
					
					styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
					
					
					
				});
				$("#agentDetailId").html(styleHtml);
			});
		}else{
			top.$.jBox.tip("请您选择产品！");
			
		}
	}
	
	
	//获取计费模版相对应的年限
	function setYearByBoundId(){
		var boundId = $("#agentDetailId").val();
		$("#boundId").val(boundId);
		showYear();
	}
	
	
	/* 
	* 功能:根据产品带回年限
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showYear(){
		var agentId = $("#boundId").val();
		//var url = "${ctx}/work/workDealInfo/showYear?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentId+"&infoType=0&_="+new Date().getTime();
		
		$.getJSON(url, function(data) {
			if (data.year1) {
				$("#year1").show();
				$("#word1").show();
			} else {
				$("#year1").hide();
				$("#word1").hide();
			}
			if (data.year2) {
				$("#year2").show();
				$("#word2").show();
			} else {
				$("#year2").hide();
				$("#word2").hide();
			}
			if (data.year4) {
				$("#year4").show();
				$("#word4").show();
			} else {
				$("#year4").hide();
				$("#word4").hide();
			}
			if (data.year5) {
				$("#year5").show();
				$("#word5").show();
			} else {
				$("#year5").hide();
				$("#word5").hide();
			}
			var arr = [data.nameDisplayName,data.orgunitDisplayName,data.emailDisplayName,data.commonNameDisplayName,data.addtionalField1DisplayName,data.addtionalField2DisplayName,data.addtionalField3DisplayName,data.addtionalField4DisplayName,data.addtionalField5DisplayName,data.addtionalField6DisplayName,data.addtionalField7DisplayName,data.addtionalField8DisplayName]
			var arrList = arr.unique();
			//清除所有必填项显示
			$(".prompt").css("display","none");
			for (var i = 0; i < arrList.length; i++) {
				if (arrList[i] != "product") {
					$("input[name='"+arrList[i]+"']").attr("required","required");
					$("input[name='"+arrList[i]+"']").parent().prev().find("span").show();
				} else {
					$("input[name='"+arrList[i]+"']").attr("required","required");
					$("input[name='"+arrList[i]+"']").parent().parent().prev().find("span").show();
				}
			}
			
			
			var boundId =  $("#agentDetailId").val(); 
			var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#surplusNum").val(data.surplusNum);
			});
			
			
			
		});
		
	}
	Array.prototype.unique = function(){
		 this.sort(); //先排序
		 var res = [this[0]];
		 for(var i = 1; i < this.length; i++){
		  if(this[i] !== res[res.length - 1]){
		   res.push(this[i]);
		  }
		 }
		 return res;
		}
</script>

</head>
<body style="overflow: scroll">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">业务新增</a></li>
	</ul>
	<form:form id="inputForm" action="${ctx}/work/workDealInfo/save" 
		method="POST" class="form-horizontal">
		<tags:message content="${message}" />
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed" >
					<tbody>
						<tr>
							<th colspan="6" style="font-size: 20px;"><span
								class="prompt" style="color: red; display: none;">*</span>基本信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>代办应用：</th>
							<td colspan="3"><input type="text" name="configApp"
								value="${workDealInfo.configApp.appName }" id="app" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>选择产品：</th>
							<td id="productTdId"><c:forEach items="${proList }"
									var="pro">
									<div id="product${pro.id }">
										<input type="radio" name="product" id="pro${pro.id }"
											onclick="productLabel(${pro.id})"
											<c:if test="${workDealInfo.configProduct.productName==pro.id}">checked</c:if>
											value="${pro.id}">${pro.name }&nbsp;&nbsp;&nbsp;
									</div>
								</c:forEach></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>应用标识：</th>
							<td colspan="3"><input type="radio" name="lable" id="lable0"
								value="0" disabled="disabled" onclick="showYear(0)"
								<c:if test="${workDealInfo.configProduct.productLabel==0}">checked</c:if>>通用&nbsp;
								&nbsp; <input type="radio" name="lable" id="lable1" value="1"
								disabled="disabled" onclick="showYear(1)"
								<c:if test="${workDealInfo.configProduct.productLabel==1}">checked</c:if>>专用</td>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<td><input type="checkbox" checked="checked"
								disabled="disabled">新增证书 <input type="hidden"
								name="dealInfoType" value="0"></td>
						</tr>
						<tr>

							<th style="width: 100px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略类型：</th>
							<td style="width: 250px;"><select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select> <input type="hidden" id="boundId"></td>
							<th style="width: 100px;"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模版：</th>
							<td style="width: 250px;"><select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select> 
							<input type="hidden" id="surplusNum" />
							<!-- 		计费策略类型：
							<select id="agentId" name="agentId" >
								<option value="0">请选择</option>
							</select>
							
							计费策略模版：
								<select id="agentDetailId" name="agentDetailId" >
									<option value="0">请选择</option>
								</select>
							 --> <%-- 			 		<input type="radio" name="payType" id="payType1" <c:if test="${workDealInfo.payType == 1}">checked</c:if> value="1">标准
								<input type="radio" name="payType" id="payType2" <c:if test="${workDealInfo.payType == 2}">checked</c:if> value="2">政府统一采购
								<input type="radio" name="payType" id="payType3" <c:if test="${workDealInfo.payType == 3}">checked</c:if> value="3">合同采购 --%>

							</td>

							<th style="width: 100px;"><span class="prompt"
								style="color: red; display: none;">*</span>申请年数：</th>
							<td><input type="radio" name="year" value="1" id="year1"
								<c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span
								id="word1">1年</span> <input type="radio" name="year" value="2"
								id="year2" <c:if test="${workDealInfo.year==2}">checked</c:if>><span
								id="word2">2年 </span><input type="radio" name="year" value="4"
								id="year4" <c:if test="${workDealInfo.year==4}">checked</c:if>><span
								id="word4">4年</span><input type="radio" name="year" value="5"
								id="year5" <c:if test="${workDealInfo.year==5}">checked</c:if>><span
								id="word5">5年</span></td>

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
							<th><span class="prompt" style="color: red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" maxlength="50"
								value="${workCompany.companyName }" id="tt"
								onchange="checkDwmc(this)"> <input type="hidden"
								name="companyId" value="${workCompany.id }" id="companyId"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位类型：</th>
							<td><select name="companyType">
									<option value="1" id="companyType1"
										<c:if test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if test="${workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4"
										<c:if test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码：</th>
							<td><input type="text" name="organizationNumber"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								value="${workCompany.organizationNumber }" maxlength="20"
								id="organizationNumber" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate"
								value="<fmt:formatDate value="${workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								id="orgExpirationTime" /></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0"
										<c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照：</th>
							<td><select name="comCertificateType">
									<option value="0" id="comCertificateType0"
										<c:if test="${workCompany.comCertificateType==0 }">selected</c:if>>营业执照</option>
									<option value="1" id="comCertificateType1"
										<c:if test="${workCompany.comCertificateType==1 }">selected</c:if>>事业单位法人登记证</option>
									<option value="2" id="comCertificateType2"
										<c:if test="${workCompany.comCertificateType==2 }">selected</c:if>>社会团体登记证</option>
									<option value="3" id="comCertificateType3"
										<c:if test="${workCompany.comCertificateType==3 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								value="${workCompany.comCertficateNumber }" maxlength="18"
								id="comCertficateNumber" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								value="<fmt:formatDate value="${workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								id="comCertficateTime" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>法人姓名：</th>
							<td><input type="text" name="legalName" id="legalName"
								value="${workCompany.legalName }" maxlength="20"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>行政所属区：</th>
							<td><select id="s_province" name="s_province"
								style="width: 100px;"></select>&nbsp;&nbsp; <select id="s_city"
								name="s_city" style="width: 100px;"></select>&nbsp;&nbsp; <select
								id="s_county" name="s_county" style="width: 100px;"></select> <script
									type="text/javascript">
									_init_area();
									$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
									$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
									$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
								</script>
								<div id="show"></div>
								<div style="margin-top: 8px;">
									区域备注：<input type="text" name="areaRemark"
										value="${workCompany.areaRemark }" maxlength="50">
								</div></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address" id="address"
								value="${workCompany.address }" maxlength="50"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位联系电话：</th>
							<td><input type="text" name="companyMobile" maxlength="11"
								id="companyMobile" value="${workCompany.companyMobile }"
								class="number"></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>备注信息：</th>
							<td><input type="text" name="remarks" id="remarks"
								value="${workCompany.remarks }"></td>
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
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName"
								maxlength="20" value="${workUser.contactName }"
								onblur="nameFill(this)" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人证件:</th>
							<td><select name="conCertType">
									<option value="0" id="conCertType0"
										<c:if test="${workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workUser.conCertType==2 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber" maxlength="18"
								id="conCertNumber" value="${workUser.conCertNumber }"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								onchange="numberFill()" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								maxlength="30" value="${workUser.contactEmail }"
								onchange="emailFill(this)" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								maxlength="11" value="${workUser.contactPhone }"
								onblur="checkMobile(this)" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务系统UID:</th>
							<td><input type="text" name="contactTel" id="contactTel"
								maxlength="20" value="${workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人性别:</th>
							<td><input name="contactSex" id="sex0"
								<c:if test="${workUser.contactSex=='男' }">checked</c:if>
								type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="contactSex" id="sex1"
								<c:if test="${workUser.contactSex=='女' }">checked</c:if>
								type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer"<%-- <c:if test="${workDealInfo.configProduct.productName!=2}"> style="display:none"</c:if> --%>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人姓名:</th>
							<td><input type="text" name="pName" id="pName"
								maxlength="20" value="${workCertApplyInfo.name }"
								onchange="checkSqr(this);" /></td>
						</tr>

						<tr>
							<th><span class="prompt" style="color: red;">*</span>身份证号:</th>
							<td><input type="text" name="pIDCard" id="pIDCard"
								maxlength="18" value="${workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail"
								value="${workCertApplyInfo.email }" onchange="form_check(this)" /></td>
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
							<td><input type="text" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" id="workDealInfoId" name="workDealInfoId"
			value="${workDealInfo.id}" />
		<input type="hidden" id="appId" name="appId"
			value="${workDealInfo.configApp.id}" />



		<input type="hidden" id="workuserId" name="workuserId" />

		<div class="row-fluid">

			<div class="span12">
				<table class="table">
					<tbody>
						<tr class="form-actions">
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2">&nbsp; <shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="submit"
										onclick="return onSubmit()" value="提 交" />&nbsp;</shiro:hasPermission> <input
								id="btnCancel" class="btn" type="button" value="返 回"
								onclick="history.go(-1)" /> <input id="btnSubmit"
								class="btn  btn-primary" type="button" value="保 存"
								onclick="os('one')" /> <input id="btnSubmit"
								class="btn  btn-primary" type="button" value="保存并再次添加"
								onclick="os('many')" /> <c:if test="${!empty workDealInfo.id}">
									<input id="btnSubmit" class="btn" type="button" value="删除"
										onclick="del(${workDealInfo.id})" />
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>

</html>
