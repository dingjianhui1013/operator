<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<style type="text/css">
.accordion-heading, .table th{width:140px;}
.table-condensed td{width:485px;}
.Wdate{width:206px;}
.btmBorder{border-bottom:1px solid #ddd}
.accordion-heading,.table th,.accordion-heading,.table td{ vertical-align: middle;}

.zoominner {background: none repeat scroll 0 0 #FFFFFF; padding: 5px 10px 10px; text-align: left;}
.zoominner p {height:30px; _position:absolute; _right:2px; _top:5px;}
.zoominner p a { background: url("../images/imgzoom_tb.gif") no-repeat scroll 0 0 transparent; float: left; height: 17px; line-height: 100px; margin-left: 10px; overflow: hidden; width: 17px;}
.zoominner p a.imgadjust {background-position: -40px 0;}
.zoominner p a.imgclose { background-position: -80px 0; cursor:pointer;}
.y {float: right; margin-bottom:10px;}
.ctnlist .text img{ cursor:pointer;}
#imgzoom_cover{background-color:#000000; filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=70); opacity:0.7; position:absolute; z-index:800; top:0px; left: 0px; width:100%; display:none;}
#imgzoom{ display:none; z-index:801; position:absolute;}
#imgzoom_img{_width:300px; _height:200px; width:700px; height:600px; background:url(../images/imageloading.gif) center center no-repeat;}
#imgzoom_zoomlayer{ _width:300px; _height:200px; _position:relative; _padding-top:30px; min-width:300px; min-height:200px;}

</style>
<script type="text/javascript" src="${ctxStatic }/js/content_zoom.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>
<script type="text/javascript">
    var ctx = "${ctx}";
    var province = "${workDealInfo.workCompany.province}";
    var city = "${workDealInfo.workCompany.city}";
    var district = "${workDealInfo.workCompany.district}";
</script>

<script type="text/javascript">
	$(document).ready(function() {
		
						//图片的显示
						if("${imgNames}"!=null && "${imgNames}"!=""){
							
							
							var imgNames = "${imgNames}";
							
							var str1 = new Array();                      
							str1 = imgNames.split(",");  
					
							for(var i = 0;i < str1.length; i++){
								var str = "<img src='/images/"+str1[i]+"' style='width: 100px; height: 80px;'>";
								
								$("#imgLayer").append(str);
								
								var imgBoxMod=$(".ctnlist .text img");
								
							    imgPop(imgBoxMod);
							}
							
								
							
							
						}				
		
						getPrivince();
						$('div.small_pic a').fancyZoom({scaleImg: true, closeOnClick: true});
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

						
							if(navigator.userAgent.indexOf("IE")!=-1){
							
							if(navigator.userAgent.indexOf("IE 8")!=-1){
								$("#organizationNumber1").bind("propertychange", function() {
									count('organizationNumber1','zzcount')
								});
								$("#comCertficateNumber1").bind("propertychange", function() {
									count('comCertficateNumber1','zjcount')
								});
								$("#companyMobile").bind("propertychange", function() {
									count('companyMobile','dwtcount')
								});
								$("#conCertNumber1").bind("propertychange", function() {
									count('conCertNumber1','zjmcount')
								});
								$("#contactPhone1").bind("propertychange", function() {
									count('contactPhone1','zjtcount')
								});
								$("#contactTel1").bind("propertychange", function() {
									count('contactTel1','ywidcount')
								});
							}else{
								$("#organizationNumber1").attr("onpropertychange","count('organizationNumber1','zzcount')");
								$("#comCertficateNumber1").attr("onpropertychange","count('comCertficateNumber1','zjcount')");
								$("#companyMobile").attr("onpropertychange","count('companyMobile','dwtcount')");
								$("#conCertNumber1").attr("onpropertychange","count('conCertNumber1','zjmcount')");
								$("#contactPhone1").attr("onpropertychange","count('contactPhone1','zjtcount')");
								$("#contactTel1").attr("onpropertychange","count('contactTel1','ywidcount')");
							}	
						}else{
							$("#organizationNumber1").attr("oninput","count('organizationNumber1','zzcount')");
							$("#comCertficateNumber1").attr("oninput","count('comCertficateNumber1','zjcount')");
							$("#companyMobile").attr("oninput","count('companyMobile','dwtcount')");
							$("#conCertNumber1").attr("oninput","count('conCertNumber1','zjmcount')");
							$("#contactPhone1").attr("oninput","count('contactPhone1','zjtcount')");
							$("#contactTel1").attr("oninput","count('contactTel1','ywidcount')");
						}
						
						if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!=""){
							var boundLabelList = "${boundLabelList}";
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
							
							
							
							
							var product = $("#product").val();
							var agentId = $("#agentId").val();
							
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setTemplateList?productId="+product+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									var styleList = data.array;
									var styleHtml="";
									$.each(styleList,function(i,item){
										if(item.agentId=="${workDealInfo.configChargeAgentId}"){
											styleHtml +="<option selected='selected'  value='"+item.id+"'>" + item.name + "</option>";
											$("#boundId").val(item.id);
											
										}else{
											styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
										}							
										
									});
									$("#agentDetailId").html(styleHtml);
									
									
									
									var agentBountId = $("#boundId").val();
									
									
									var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentBountId+"&infoType=1&_="+new Date().getTime();
											
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
										if (data.year3) {
											$("#year3").show();
											$("#word3").show();
										} else {
											$("#year3").hide();
											$("#word3").hide();
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
										
										$("input[name='year']:eq("+(${workDealInfo.year}-1)+")").attr("checked",'checked');
										$("input[name='manMadeDamage'][value='" + ${workDealInfo.manMadeDamage} + "']").attr("checked",'checked');
										//经信委
										if(data.support){
											$("#supportDateTh").show();
											$("#supportDateTd").show();
											$("#expirationDate").val(data.expirationDate);
										}
										if(!data.support){
											$("#supportDateTh").hide();
											$("#supportDateTd").hide();
											$("#expirationDate").val(null);
										}
										
										
										var boundId =  $("#agentDetailId").val(); 
										var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
										/* $.getJSON(url,function(data){
											$("#surplusNum").val(data.surplusNum);
											if($("#surplusNum").val()==0 && $("#agentId").val()!=1){
												top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！");
												$("#agentMes").show();
											}else{
												$("#agentMes").hide();
											}
										}); */
										
										
										var arr = [ data.nameDisplayName,
												data.orgunitDisplayName,
												data.emailDisplayName,
												data.commonNameDisplayName,
												data.addtionalField1DisplayName,
												data.addtionalField2DisplayName,
												data.addtionalField3DisplayName,
												data.addtionalField4DisplayName,
												data.addtionalField5DisplayName,
												data.addtionalField6DisplayName,
												data.addtionalField7DisplayName,
												data.addtionalField8DisplayName ]
										var arrList = arr.unique();
										//清除所有必填项显示
										$(".prompt").css("display", "none");
										for (var i = 0; i < arrList.length; i++) {
											if (arrList[i] != "product") {
												$("input[name='" + arrList[i] + "']").attr(
														"required", "required");
												$("input[name='" + arrList[i] + "']")
														.parent().prev().find("span")
														.show();
												if(arrList[i] != "contacEmail"){
													
													$("input[name='" + arrList[i] + "']").attr("readonly","readonly");
												}
												
												
												
												
												
											} else {
												$("input[name='" + arrList[i] + "']").attr(
														"required", "required");
												$("input[name='" + arrList[i] + "']")
														.parent().parent().prev().find(
																"span").show();
											}
										}
									});
									
									
								});
							}
						}
						
						
						
							$("#product").change(function(){
							
							var product = $("#product").val();
							var agentHtml="";
							var styleHtml="";
							if (product!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList1?productId="+product+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									
									$.each(data, function(i, item){					 
										 if(item.styleId=="1"){	
												agentHtml+="<option value='"+item.styleId+"'>标准</option>";
										}else if(item.styleId=="2"){
												agentHtml+="<option value='"+item.styleId+"'>政府统一采购</option>";
										}else if(item.styleId=="3"){
												agentHtml+="<option value='"+item.styleId+"'>合同采购</option>";
										}
										 
										 if(item.agentId!=null){
											 $("#boundId").val(item.agentId);
											 showYear();
											 styleHtml +="<option value='"+item.agentId+"'>" + item.agentName + "</option>"; 
										 }
											
										
									});	
									
									if(agentHtml==""){
										
										agentHtml+="<option value='0'>请选择</option>";
										$("#agentId").html(agentHtml);
										styleHtml+="<option value='0'>请选择</option>";
										$("#agentDetailId").html(styleHtml);
										top.$.jBox.tip("请先配置计费策略！");
										return;
									}
									
									$("#agentId").html(agentHtml);
									$("#agentDetailId").html("");
									$("#agentDetailId").html(styleHtml);
									
									
									
									}); 	
							}
							
						});
						
						
						$("#agentId").change(function(){
							var product = $("#product").val();
							var agentId = $("#agentId").val();
							if (agentId!=0) {
								var url = "${ctx}/work/workDealInfo/setTemplateList?productId="+product+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
								top.$.jBox.tip("请您选择计费策略类型！");
								
							}
							
						});
						
						
						
						
						
						//经信委
						$("input[name='year']").change(function(){
							$("#expirationDate").val("");						
						});
						
						$("#expirationDate").blur(function(){
							
							$("#year1").removeAttr("checked");
							$("#year2").removeAttr("checked");
							$("#year3").removeAttr("checked");
							$("#year4").removeAttr("checked");
							$("#year5").removeAttr("checked");
						});
						
						//禁用所有的按钮 下拉框
						$(".row-fluid.disabled").find("*").each(function() {
							$(this).attr("disabled", "disabled");
						}); 
						
						

					});
	Array.prototype.unique = function() {
		this.sort(); //先排序
		var res = [ this[0] ];
		for (var i = 1; i < this.length; i++) {
			if (this[i] !== res[res.length - 1]) {
				res.push(this[i]);
			}
		}
		return res;
	}
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
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/area.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>

<script type="text/javascript">	
function buttonFrom() {
	var pageType = '${pageType}';
	if(pageType=='audit'){
		$("#recordForm").attr("action", "${ctx}/work/workDealInfoAudit/auditLoad");
	}else{
		$("#recordForm").attr("action", "${ctx}/work/workDealInfoAudit/verifyLoad");
	}
	var saveYear = 0;
	$("input[name=recordContent]").val($("#recordContent").val());
	$("#recordForm").submit();
}
	function refuse() {
		//top.$.jBox.open("iframe:${ctx}/work/workDealInfoAudit/jujueFrom?id=${workDealInfo.id}", "审核拒绝",600,400);
		var pageType = '${pageType}';
		if(pageType=='audit'){
			pageType='鉴别';
		}else{
			pageType='验证';
		}
		top.$.jBox
				.open(
						"iframe:${ctx}/work/workDealInfoAudit/jujueFrom?id=${workDealInfo.id}",
						pageType+"拒绝",
						600,
						400,
						{
							buttons : {
								"确定" : "ok",
								"取消" : true
							},
							bottomText : "填写"+pageType+"拒绝的原因",
							submit : function(v, h, f) {
								var suggest = h.find("iframe")[0].contentWindow
										.$("#suggest")[0].value;
								//nodes = selectedTree.getSelectedNodes();
								if (v == "ok") {
									if(pageType=='鉴别'){
										window.location.href = "${ctx}/work/workDealInfoAudit/jujue?status=0&id=${workDealInfo.id}&remarks="
											+ suggest;
									}else{
										window.location.href = "${ctx}/work/workDealInfoAudit/jujue?status=1&id=${workDealInfo.id}&remarks="
											+ suggest;
									}
									
									return true;
								}
							},
							loaded : function(h) {
								$(".jbox-content", top.document).css(
										"overflow-y", "hidden");
							}
						});
	}
	
	function setJBRName(){
		var name = $("#contactName1").val();
		$("#pName").val(name);
		$("#pNameH").val(name);
	}
	
	function setJBRCard(o){
		var card = $("#conCertNumber1").val();
		$("#pIDCard").val(card);
		$("#pIDCardH").val(card);
		$("#"+o).hide();
	}
	
	function setJBRMail(){
		var mail = $("#contacEmail").val();
		$("#pEmail").val(mail);
		$("#pEmailH").val(mail);
	}
		
	

	
	
	//获取计费模版相对应的年限
	function setYearByBoundId(){
		var boundId = $("#agentDetailId").val();
		$("#boundId").val(boundId);
		showYear();
	} 
	
	
	
	function showYear(){
		var agentBountId = $("#boundId").val();
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentBountId+"&infoType=1&_="+new Date().getTime();
				
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
			if (data.year3) {
				$("#year3").show();
				$("#word3").show();
			} else {
				$("#year3").hide();
				$("#word3").hide();
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
			
			
			//经信委
			if(data.support){
				$("#supportDateTh").show();
				$("#supportDateTd").show();
				$("#expirationDate").val(data.expirationDate);
			}
			if(!data.support){
				$("#supportDateTh").hide();
				$("#supportDateTd").hide();
				$("#expirationDate").val(null);
			}
			
			
			var boundId =  $("#agentDetailId").val(); 
			var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
			/* $.getJSON(url,function(data){
				$("#surplusNum").val(data.surplusNum);
				if($("#surplusNum").val()==0 && $("#agentId").val()!=1){
					top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！");
					$("#agentMes").show();
				}else{
					$("#agentMes").hide();
				}
			}); */
		});
	
	}
	
	
	
	function changeDealInfoType(){
		var id = "${workDealInfo.id}";
		var submit = function (v, h, f) {
							    if (v == 'ok'){
							    	maintain(id);
							    }
							   		 return true; //close
							};
							top.$.jBox.confirm("您确定需要更换业务类型吗？", "提示", submit);
	}
	
	function maintain(obj) {
		top.$.jBox
		.open(
				"iframe:${ctx}/work/workDealInfo/typeShow?infoId="+obj,
				"请选择业务类型",
				500,
				300,
				{
					buttons : {
						"确定" : "ok",
						"关闭" : true
					},
					submit : function(v, h, f) {
						if (v == "ok") {
							var table = h.find("iframe")[0].contentWindow.typeForm;
							var dealTypes = $(table).find("input[name='dealType']");
							var dealType = "";
							var reissueType = "";
							for (var i = 0; i < dealTypes.length; i++) {
								if (dealTypes[i].checked == true) {
									if (i == 0) {
										dealType = dealTypes[i].value;
									} else {
										dealType = dealType + "," + dealTypes[i].value;
									}
								}
							}
							var id = $(table).find("input[name='id']").val();
							var reissueTypes = $(table).find("input[name='reissueType']");
							for (var i = 0; i < reissueTypes.length; i++) {
								if (reissueTypes[i].checked == true) {
									reissueType = reissueTypes[i].value;
								}
							}
							if (dealType == "") {
								top.$.jBox.tip("请选择业务类型");
							} else {
								
								if(dealType.indexOf("3")>=0){
									var url = "${ctx}/work/workDealInfo/findById?dealInfoId="+id;
									$.getJSON(url + "&_="+new Date().getTime(),	function(data){
												if (data.status==1){
													if (data.isUpdate==0) {
														top.$.jBox.tip("证书未在更新范围内，不允许更新此证书 ！");
													}else{
														window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
													}
												}else{
													top.$.jBox.tip("系统异常");
												}
									});
								}else{
									window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
								}
							}
						}
					},
					loaded : function(h) {
						$(".jbox-content", top.document).css(
								"overflow-y", "hidden");
					}
				});
	};
	function count(o,c){
		$("#"+c).show();
 		$("#"+c).html($("#"+o).val().length);
	}
	function qxCount(o)
	{
		$("#"+o).hide();
	}
	function hqcount(o,c)
	{
		$("#"+c).show();
 		$("#"+c).html($("#"+o).val().length);
	}
	
	
	
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/list">业务办理列表</a></li>
		<li class="active"><a
			href="#">业务<c:if test="${not empty update}">更新</c:if><c:if test="${not empty change}">变更</c:if><c:if test="${not empty reissue}">补办</c:if><c:if test="${not empty revoke}">吊销</c:if></a></li>
	</ul>
	<form:form id="inputForm"
		action="${ctx}/work/workDealInfoOperation/maintainSaveUpdate" 
		method="post" enctype="multipart/form-data"
		class="form-horizontal">
		<tags:message content="${message}" />
		
		<div id="append_parent"></div>
		
		
		<div class="list ctnlist">

		<div class="text">
		

		<p id="imgLayer" align="left" >
		<input id="imgNames" name="imgNames" type="hidden"/>
		</p>
		</div>
		</div>
		
		<div class="row-fluid disabled">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">基本信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>应用名称：</th>
							<td class="tdWidth"><input type="text" name="configApp" disabled="disabled"
								value="${workDealInfo.configApp.appName }" id="4" />
								
								<input type="hidden" id="appId" value="${workDealInfo.configApp.id }" />
								
								</td>
							
							
							
							<th><span class="prompt" style="  display: none;">*</span>业务类型：</th>
							<c:if test="${workDealInfo.dealInfoType==0}">
								<td><input type="checkbox" name="dealInfoType" value="0" checked="checked" disabled="disabled"
									checked>新增证书
								</td> 
							</c:if>
							<c:if test="${workDealInfo.dealInfoType==1}">
								<td>
								<input type="checkbox" disabled="disabled" checked="checked" value="0" name="dealInfoType">
								<font style="">更新证书</font>
								<input type="hidden" value="0" name="dealInfoType">
								<c:if test="${workDealInfo.dealInfoType1==2}"><input type="checkbox" disabled="disabled" checked="checked" value = "1"
									name="dealInfoType1"><font style="">遗失补办</font><input type="hidden" value="1" name="dealInfoType1"></c:if>
									<c:if test="${workDealInfo.dealInfoType1==3}"><input type="checkbox" disabled="disabled" checked="checked" value = "2"
									name="dealInfoType1"><font style="">损坏更换</font><input type="hidden" value="2" name="dealInfoType1"></c:if>
								
								&nbsp;&nbsp;
									<!-- <input class="btn btn-primary" type="button" value="更改业务类型" onclick="changeDealInfoType()"  /> -->
								</td>
							</c:if>
						</tr>
					
						<tr>

							<th><span class="prompt" style="  display: none;">*</span>选择产品：</th>
							<td>
							<select name="product"  id="product">
									<c:forEach items="${proList}" var="product">
										<option value="${product.id}" <c:if test="${product.id==workDealInfo.configProduct.id }">selected="selected"</c:if> >${product.name}</option>
									</c:forEach>
							</select>		
							</td>

							

							
							


							<th ><span class="prompt"
								style="  display: none;">*</span>申请年数：</th>
							<td>
								<input type="radio" name="year" value="1" id="year1">
								<span id="word1">1年</span>
								<input type="radio" name="year" value="2" id="year2">
								<span id="word2">2年 </span>
								
								<input type="radio" name="year" value="3" id="year3">
								<span id="word3">3年</span>
								
								<input type="radio" name="year" value="4" id="year4">
								<span id="word4">4年</span>
								<input type="radio" name="year" value="5" id="year5">
								<span id="word5">5年</span>
							</td>
							

						</tr>
						
						<tr>
						<th><span class="prompt"
								style="  display: none;">*</span>计费策略类型：</th>
							<td class="tdWidth">
							<select id="agentId" name="agentId">
								<option value="0">请选择</option> 
							</select> 
							<input type="hidden" id="boundId">
							<input type="hidden" value="${workDealInfo.payType}" id="payType" />
							</td>
						
						<th><span class="prompt"
								style="  display: none;">*</span>计费策略模版：</th>
							<td><select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select>  &nbsp;<label id="agentMes" style=" display: none;">不可用</label>
							<input type="hidden" id="surplusNum" />
							</td>
							<c:if test="${reissue==2}">
						
							<th>人为损坏：</th>
							<td><input type="radio" name="manMadeDamage" value="true">是
							 <input type="radio" name="manMadeDamage"value="false">否</td>
						
						</c:if>
						
						</tr>
						<tr>
						
						<th id="supportDateTh" style="display: none">选择截止日期：</th>
						<td id="supportDateTd" style="display: none">
								<input class="input-medium Wdate" type="text"
							 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
							 maxlength="20" readonly="readonly" <%-- value="<fmt:formatDate value="${expirationDate}" pattern="yyyy-MM-dd"/>" --%>
							name="expirationDate" id="expirationDate"/>
							</td>
							
						</tr>
						
						
						
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid disabled">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">单位信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>单位名称：</th>
							<td class="tdWidth"><input type="text" name="companyName" id="companyName"
								maxlength="50" value="${workDealInfo.workCompany.companyName}" />


							</td>
							<th>单位类型：</th>
							<td><select name="companyType">
									<option value="1" id="companyType1"
										<c:if test="${workDealInfo.workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workDealInfo.workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if test="${workDealInfo.workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4"
										<c:if test="${workDealInfo.workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if test="${workDealInfo.workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>

						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>组织机构代码：</th>
							<td class="tdWidth"><input type="text" name="organizationNumber"
								id="organizationNumber1"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								value="${workDealInfo.workCompany.organizationNumber}"
								maxlength="18"  onblur="qxCount('zzcount')" onfocus="hqcount('organizationNumber1','zzcount')"/><span id="zzcount" style="  margin-left: 10px"></span></td>
							<th>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate"
								
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th>服务级别：</th>
							<td class="tdWidth"><select name="selectLv">
									<option value="0" id="selectLv0"
										<c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th>单位证照：</th>
							<td><select name="comCertificateType">
									<option value="0" id="comCertificateType0"
										<c:if test="${workDealInfo.workCompany.comCertificateType==0 }">selected</c:if>>营业执照</option>
									<option value="1" id="comCertificateType1"
										<c:if test="${workDealInfo.workCompany.comCertificateType==1 }">selected</c:if>>事业单位法人登记证</option>
									<option value="2" id="comCertificateType2"
										<c:if test="${workDealInfo.workCompany.comCertificateType==2 }">selected</c:if>>社会团体登记证</option>
									<option value="3" id="comCertificateType3"
										<c:if test="${workDealInfo.workCompany.comCertificateType==3 }">selected</c:if>>其他</option>
							</select></td>

						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>证件号：</th>
							<td class="tdWidth"><input type="text" name="comCertficateNumber"
								id="comCertficateNumber1" maxlength="18"
								value="${workDealInfo.workCompany.comCertficateNumber}"  onblur="qxCount('zjhcount')" onfocus="hqcount('comCertficateNumber1','zjhcount')"/><span id="zjhcount" style="  margin-left: 10px"></span></td>
							<th>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>

						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>法人姓名：</th>
							<td class="tdWidth" style="vertical-align: middle;"><input type="text" name="legalName"
								value="${workDealInfo.workCompany.legalName}"></td>
							<th>行政所属区：</th>
							<td><select id="s_province" name="s_province" onchange="getCity(this.options[this.options.selectedIndex].id)" style="width: 105px;"></select>&nbsp;&nbsp; 
								<select id="s_city" name="s_city" onchange="getTown(this.options[this.options.selectedIndex].id)" style="width: 105px;"></select>&nbsp;&nbsp; 
								<select id="s_county" name="s_county" style="width: 105px;"></select> 
								<script type="text/javascript">
								if(${provinceId!=null}){
									getCity(${provinceId});
								}
								if(${cityId!=null}){
									getTown(${cityId});
								}	
								</script>
								</td>


						</tr>
						<tr>
						
							<th>区域备注：</th>
							<td><input type="text" name="areaRemark"  value="${workDealInfo.workCompany.areaRemark }"></td>			
										
							<th><span class="prompt" style="  display: none;">*</span>街道地址：</th>
							<td class="tdWidth"><input type="text" name="address"
								value="${workDealInfo.workCompany.address}"></td>
							
						</tr>
						<tr>
						
							<th class="btmBorder"><span class="prompt" style="  display: none;">*</span>单位联系电话：</th>
							<td class="btmBorder"><input type="text" name="companyMobile" 
								id="companyMobile"
								value="${workDealInfo.workCompany.companyMobile }" 
								
								onblur="checkContactMobil(this,'dwtcount')"
								
								onfocus="hqcount('companyMobile','dwtcount')"/>
								<span id="dwtcount" style="  margin-left: 10px"></span></td>
						
						
							<th><span class="prompt" style="  display: none;">*</span>备注信息：</th>
							<td class="tdWidth"><input type="text" name="remarks" id="remarks"
								value="${workDealInfo.workCompany.remarks }"></td>
								
															
						</tr>
						
						<c:if test="${workDealInfo.selfImage.id!=null }">
						<tr>
							<th>单位电子证件:</th>
							<td class ="small_pic">
								
								<div class="col-sm-9">
										<label for="exampleInputFile" class="uploadBtn">上传文件</label>
										<input type="file" id="exampleInputFile" name = "companyImage" class="uploadFileInput" onchange="PreviewImage(this,'imghead','preview','picBigBox')">
			                            <br><br>
			                            <div class="previewImg" id="preview">
			                            	<div class="small_pic">
													<a href="#picBigBox" >
														<img id="imghead" style = "width:100px;height:75px" src="${imgUrl}/${workDealInfo.selfImage.companyImage }" >
			                                		</a> 
			                                </div>
			                            </div>
									</div>
							</td>
							</tr>
								</c:if>
						
						

					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid disabled">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">证书持有人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>证书持有人姓名:</th>
							<td class="tdWidth"><input type="text" name="contactName" id="contactName1"
								value="${workDealInfo.workUser.contactName }" onblur="setJBRName()" /></td>
							<th>证书持有人证件:</th>
							<td><select name="conCertType">
									<option value="0" id="conCertType0"
										<c:if test="${workDealInfo.workUser.conCertType==0 }">selected</c:if>>身份证</option>
									<option value="1" id="conCertType1"
										<c:if test="${workDealInfo.workUser.conCertType==1 }">selected</c:if>>军官证</option>
									<option value="2" id="conCertType2"
										<c:if test="${workDealInfo.workUser.conCertType==2 }">selected</c:if>>其他</option>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>证件号码:</th>
							<td class="tdWidth"><input type="text" name="conCertNumber"
								id="conCertNumber1" onblur="setJBRCard('zjmcount')"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" maxlength="18"
								value="${workDealInfo.workUser.conCertNumber }"  onfocus="hqcount('conCertNumber1','zjmcount')"/><span id="zjmcount" style="  margin-left: 10px"></span></td>
							<th><span class="prompt" style="  display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail" onblur="setJBRMail()"
								class="email" maxlength="30"
								value="${workDealInfo.workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>证书持有人手机号:</th>
							<td class="tdWidth"><input type="text" name="contactPhone"
								id="contactPhone1" maxlength="11" class="number"
								value="<fmt:formatNumber pattern="#">${workDealInfo.workUser.contactPhone }</fmt:formatNumber>"  onblur="qxCount('zjtcount')" onfocus="hqcount('contactPhone1','zjtcount')"/><span id="zjtcount" style="  margin-left: 10px"></span> <input
								type="hidden" name="contactPhone" id="contactPhone"
								maxlength="11" class="number" disabled="disabled"
								onkeyup="this.value=this.value.replace(/\D/g,'')"
								value="<fmt:formatNumber pattern="#">${workDealInfo.workUser.contactPhone }</fmt:formatNumber>" /></td>
							<th class="btmBorder"><span class="prompt" style="  display: none;">*</span>业务系统UID:</th>
							<td class="btmBorder"><input type="text" name="contactTel" id="contactTel1"
								maxlength="20" value="${workDealInfo.workUser.contactTel }"  onblur="qxCount('ywIDcount')" onfocus="hqcount('contactTel1','ywIDcount')"/><span id="ywIDcount" style="  margin-left: 10px"></span>
							</td>
						</tr>
						<tr>
							<th>证书持有人性别:</th>
							<td class="tdWidth"><input name="contactSex" id="sex0"
								<c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if>
								type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp; <input
								name="contactSex" id="sex1"
								<c:if test="${workDealInfo.workUser.contactSex=='女' }">checked</c:if>
								type="radio" value="女">女</td>
								
							<c:if test="${workDealInfo.selfImage.id!=null }">
							<th >个人电子证件:</th>
							<td class = "small_pic">
								
								
								<div class="col-sm-9">
									<label for="exampleInputFile1" class="uploadBtn">上传图片</label>
									<input type="file" id="exampleInputFile1" name = "transactorImage" class="uploadFileInput"  onChange="PreviewImage(this,'imghead1','preview1','picBigBox1')">
									<br><br>
									<div id="preview1" class="previewImg">
	                            		<div class="small_pic">
		                            		<a href="#picBigBox1" >
		                            		
		                            		   <img id="imghead1" style = "width:100px;height:75px"  src="${imgUrl }/${workDealInfo.selfImage.transactorImage}" >
		                            		</a>
	                            		</div>
	                            	</div>
                        		</div>
							</td>	
								</c:if>		
							
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid disabled">
			<div class="span12" id="proposer">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>经办人姓名:</th>
							<td><input type="text" id="pName" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" />
								<input type="hidden" id="pNameH" name="pName" /></td>
								
								<th class="btmBorder"><span class="prompt" style="  display: none;">*</span>经办人身份证号:</th>
								<td class="btmBorder"><input type="text" name="pIDCard" id="pIDCard" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" />
								<input type="hidden" id="pIDCardH" name="pIDCard" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="  display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail" disabled="disabled"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.email }" />
								<input type="hidden" id="pEmailH" name="pEmail" /></td>
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
		<input type="hidden" name="deal_info_status" value="5">
		<input type="hidden" name="workDealInfoId" value="${workDealInfo.id }">
	</form:form>
	<div class="form-actions"
		style="text-align: center; width: 100%; border-top: none;">
		<input type="button" class="btn btn-primary" value="通过"
			onclick="javascript:buttonFrom()" /> <input class="btn"
			type="button" value="拒绝" onclick="javascript:refuse()" /> <span
			id="mssg" style="color: red"></span>
	</div>

	<form id="recordForm" method="post"
		action="${ctx}/work/workDealInfoAudit/auditLoad">
		<input type="hidden" name="recordContent"> <input
			type="hidden" name="id" value="${workDealInfo.id}">
	</form>
	<div id="picBigBox" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.companyImage }"  >
	</div>
	<div id="picBigBox1" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.transactorImage }" >
	</div>
	
	
</body>
</html>