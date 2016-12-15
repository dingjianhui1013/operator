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

.zoominner {background: none repeat scroll 0 0 #FFFFFF; padding: 5px 10px 10px; text-align: left;}
/* .zoominner p {height:30px; _position:absolute; _right:2px; _top:5px;}
.zoominner p a { /* background: url("../images/imgzoom_tb.gif") no-repeat scroll 0 0 transparent;  float: left; height: 17px; line-height: 100px; margin-left: 10px;  overflow: hidden;  width: 17px;}
.zoominner p a.imgadjust {background-position: -40px 0;} */
.zoominner a.imgclose{ cursor:pointer;position:absolute;z-index:9999;right:-6px; top:-6px; color:#333; font-size:30px; display:block;}
.zoominner a.imgclose:hover{text-decoration:none;}
.y {float: right; margin-bottom:10px;}
.ctnlist .text img{ cursor:pointer;}
#imgzoom_cover{background-color:#000000; filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=70); opacity:0.7; position:absolute; z-index:800; top:0px; left: 0px; width:100%; display:none;}
#imgzoom{ display:none; z-index:801; position:absolute;}
#imgzoom_img{_width:300px; _height:200px; width:700px; height:600px; background:url(../images/imageloading.gif) center center no-repeat;}
#imgzoom_zoomlayer{ _width:300px; _height:200px; _position:relative; _padding-top:30px; min-width:300px; min-height:200px; padding:17px;}

.imgLayerBox{margin-bottom:20px;overflow:hidden;}
.uploadImgList{ float:left; border:1px solid #ddd;margin-right:10px; padding:2px; position:relative}
.uploadImgName{ text-align:center; font-size:12px; font-weight:bold; margin-top:4px; height:22px; line-height:22px;border-top:1px solid #ddd;margin-bottom:0px;}
.smBtn{width:100px; height:20px;}
.btnGrop{margin-bottom:5px;}
.s-closeBtn{ position:absolute; right:-4px; top:0px; font-size:20px; cursor:pointer;}


.accordion-heading, .table th{width:140px;}
.table-condensed td{width:485px;}
.Wdate{width:206px;}
.btmBorder{border-bottom:1px solid #ddd}
.accordion-heading,.table th,.accordion-heading,.table td{ vertical-align: middle;}
</style>
<script type="text/javascript">
	var ctx = "${ctx}";
	var ctxStatic = "${ctxStatic}";
	var imgPath = "${imgPath}";
</script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDrag.js"></script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDialog.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>
<script type="text/javascript">
$(document).ready(function() {
			
	
	if("${imgNames}"!=null && "${imgNames}"!=""){
		var imgNames = "${imgNames}";
		var str1 = new Array();                      
		str1 = imgNames.split(",");  

		var namestr = "";
		for(var i = 0;i < str1.length; i++){
			//取出图片的状态
			var imgstatus = str1[i].substring(str1[i].lastIndexOf('##')+2,str1[i].length);
			var str ='';
			if(imgstatus==-2){
				str = $("<div class='uploadImgList' style='border-style:solid;border-width:2px;border-color:green' ><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p></div>');
			}else{
				str = $("<div class='uploadImgList'><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p></div>');
			}
			$("#imgLayer").append(str);
			var imgBoxMod=$(".ctnlist .text img");
		    imgPop(imgBoxMod);
		    //imgDel(str);
		    str1[i]=str1[i].substring(0,str1[i].lastIndexOf('##'));
		    namestr+=str1[i].substring(str1[i].lastIndexOf('/')+1,str1[i].length)+",";
		}
		if(namestr!=''){
			$("#imgNames").val(namestr.substring(0,namestr.length-1));
		}
	}
	
	
	
	
	
			if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!="" && "${workDealInfo.isIxin}"){
				var boundLabelList = "${boundLabelList}";
				var lable = "${workDealInfo.configProduct.productLabel}";
				$("#agentId").attr("onchange","setStyleList("+lable+")");
				var agentHtml="";
				//alert(boundLabelList);
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
			}
			
			
			var product = $("#product").val();
			var agentId = $("#agentId").val();
			var appId = $("#appId").val();
			if (agentId!=0) {
				var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+product+"&app="+appId+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
			showAgent();
			
			
			
			if(${expirationDate!=null}){
				$("#applyYear").hide();
			}
			
			var pageType = '${pageType}';
			if(pageType=='audit'){
				pageType='鉴别';
			}else{
				pageType='验证';
				$("input[name='year']").each(function(i,one){
					$(one).attr("disabled","disabled");
				})
				$("#agentId").attr("disabled","disabled");
				$("#agentDetailId").attr("disabled","disabled");
				
			}
			
		});
	function showAgent(){
		var lable=$('input[name="lable"]:checked ').val();
		var productName = $("#product").val();
		var url = "${ctx}/work/workDealInfoSed/showAgentProduct?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		$.getJSON(url,function(data){
			if(data.tempStyle!=-1){
				var styleList = data.boundStyleList;
				$.each(styleList, function(i, item2){
					if(i==0){
						showz(item2.id);//页面boundId 没有赋上值，这里重新赋值
					}
				});
			}
		});
	}
	function showz(agentid)
	{
		var agentId = agentid;
		//var url = "${ctx}/work/workDealInfo/showYear?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		var url = "${ctx}/work/workDealInfoSed/showYearNew?boundId="+agentId+"&infoType=0&_="+new Date().getTime();
		$.getJSON(url, function(data) {
			var arr = [data.nameDisplayName,data.orgunitDisplayName,data.emailDisplayName,data.commonNameDisplayName,data.addtionalField1DisplayName,data.addtionalField2DisplayName,data.addtionalField3DisplayName,data.addtionalField4DisplayName,data.addtionalField5DisplayName,data.addtionalField6DisplayName,data.addtionalField7DisplayName,data.addtionalField8DisplayName]
			var arrList = $.unique(arr);
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
		});
	}
	function buttonFrom() {
		var pageType = '${pageType}';
		if(pageType=='audit'){
			$("#recordForm").attr("action", "${ctx}/work/workDealInfoAudit/auditLoad");
		}else{
			$("#recordForm").attr("action", "${ctx}/work/workDealInfoAudit/verifyLoad");
		}
		var saveYear = 0;
		if("${workDealInfo.isIxin}"){
			if($("#agentDetailId").val()!=0 && $("#agentId").val()!=1){
				if($("#surplusNum").val()==0){
					top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
					return false;
				}else{
					
					var boundId = $("#agentDetailId").val();
					var url = "${ctx}/profile/configChargeAgent/checkAgentIsZero?agentDetailId="+boundId+"&_="+new Date().getTime();
					$.getJSON(url,function(data){
						if(data.status==0){
							top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！"); 
						}else{
							if ($("#year1").prop("checked") == true) {
								saveYear = $("#year1").val();
							}
							if ($("#year2").prop("checked") == true) {
								saveYear = $("#year2").val();
							}
							if ($("#year3").prop("checked") == true) {
								saveYear = $("#year3").val();
							}
							if ($("#year4").prop("checked") == true) {
								saveYear = $("#year4").val();
							}
							if ($("#year5").prop("checked") == true) {
								saveYear = $("#year5").val();
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
					});
					
					
					
				}
			}else{
				if ($("#year1").prop("checked") == true) {
					saveYear = $("#year1").val();
				}
				if ($("#year2").prop("checked") == true) {
					saveYear = $("#year2").val();
				}
				if ($("#year3").prop("checked") == true) {
					saveYear = $("#year3").val();
				}
				if ($("#year4").prop("checked") == true) {
					saveYear = $("#year4").val();
				}
				if ($("#year5").prop("checked") == true) {
					saveYear = $("#year5").val();
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
			
			
			
			
			
			
			
			
		}else if ($("#dealInfoType").val() == 0 ||$("#dealInfoType").val() == 1 ) {//新增证书和更新证书
			$("input[name=recordContent]").val($("#recordContent").val());
			$("#recordForm").submit();
		}else {
			if ($("#year1").prop("checked") == true) {
				saveYear = $("#year1").val();
			}
			if ($("#year2").prop("checked") == true) {
				saveYear = $("#year2").val();
			}
			if ($("#year3").prop("checked") == true) {
				saveYear = $("#year3").val();
			}
			if ($("#year4").prop("checked") == true) {
				saveYear = $("#year4").val();
			}
			if ($("#year5").prop("checked") == true) {
				saveYear = $("#year5").val();
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
	
	
	function showYear(){
		var agentId = $("#boundId").val();
		//var url = "${ctx}/work/workDealInfo/showYear?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentId+"&infoType=1&_="+new Date().getTime();
		
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
			if('${workDealInfo.year}'!=''){
				$("input[name='year']:eq("+(${workDealInfo.year}-1)+")").attr("checked",'checked');
				$("input[name='manMadeDamage'][value='" + ${workDealInfo.manMadeDamage} + "']").attr("checked",'checked');
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
			$.getJSON(url,function(data){
				$("#surplusNum").val(data.surplusNum);
			});
			
			
			
		});
		
	}
	
	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleList(obj){
		var lable = obj;
		var product = $("#product").val();
		var agentId = $("#agentId").val();
		var appId = $("#appId").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+product+"&app="+appId+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
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
	
	function onSubmit() {
		var content =$("input[id=recordContent]").val();
		$("#inputForm").attr(
				"action",
				"${ctx}/work/workDealInfoOperation/maintainAddSave?workDealInfoId="+${workDealInfo.id}+"&recordContent="+content);
		$("#inputForm").submit();
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfoAudit/list">业务审核列表</a></li>
		<c:if test="${iseditor !='iseditor'}">
			<li class="active"><a
				href="${ctx}/work/workDealInfoAudit/auditFrom?id=${workDealInfo.id}">业务审核
	
			</a></li>
		</c:if>
		<c:if test="${iseditor =='iseditor'}">
			<li class="active"><a href="#">业务补录</a></li>
		</c:if>
	</ul>
	<form:form id="inputForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<tags:message content="${message}" />
		
		
		
		
		<div id="append_parent"></div>
		
		
		<div class="list ctnlist">

		<div class="text">
		<div id="imgLayer" align="left" class="imgLayerBox">
		<input id="imgNames" name="imgNames" type="hidden"/>
		</div>
		</div>
		</div>
		
		
		
		
		
		
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<c:if test="${iseditor !='iseditor'}">
								<th colspan="4" style="font-size: 20px;">基本信息</th>
							</c:if>
							<c:if test="${iseditor =='iseditor'}">
								<th colspan="1" style="font-size: 20px;"><span
									class="prompt" style="color: red; display: none;">*</span>基本信息</th>	
								<th colspan="3"> <a href="#" data-toggle="modal">
								<input id="scan" class="btn btn-primary smBtn" onclick="scanningInfoEnter(false)" data-toggle="modal" value="扫描录入" /></a>	
								</th>
							</c:if>
						</tr>
						<tr>
							<th>代办应用：</th>
							<td><input type="text" name="configApp" disabled="disabled"
								value="${workDealInfo.configApp.appName }" id="app" />
								<input type="hidden" id="appId" value="${workDealInfo.configApp.id }" />
								</td>
							<th>选择产品：</th>
							<td><input type="text" name="product" disabled="disabled" 
								value="${proType[workDealInfo.configProduct.productName] }" />
								<input type="hidden" id="product" value="${workDealInfo.configProduct.productName }" />	
							</td>
								
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
							<c:if test="${empty workDealInfo.isIxin}"> 
								<th><span class="prompt" style="color: red; display: none;">*</span>计费策略类型：</th>
							<td>
							
							<select  disabled="disabled" >
								<option <c:if test="${workDealInfo.payType==1 }">selected</c:if> value="1" >标准</option>
								<option <c:if test="${workDealInfo.payType==2 }">selected</c:if> value="2" >政府统一采购</option>
								<option <c:if test="${workDealInfo.payType==3 }">selected</c:if> value="3" >合同采购</option>
							</select>
							</td>
							</tr>
						<tr>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>计费策略模版：</th>
							<td class="btmBorder">
							
							<select id="agentId"
								name="agentId" disabled="disabled">
								<option value="${jfMBId}">${jfMB}</option>
							</select>
							</td>
							</c:if>
								<c:if test="${workDealInfo.isIxin eq true}"> 
								<th><span class="prompt"
								style="color: red; display: none;">*</span>计费策略类型：</th>
							<td><select id="agentId"
								name="agentId">
									<option value="0">请选择</option>
							</select> <input type="hidden" id="boundId"></td>
							
							<th class="btmBorder"><span class="prompt"
								style="color: red; display: none;">*</span>计费策略模版：</th>
							<td class="btmBorder"><select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select> 
							<input type="hidden" id="surplusNum" />

							</td>
								</c:if>
										
							</tr>
						<tr>			
						
							<th>申请年数：</th>

							<td><c:if test="${workDealInfo.dealInfoType!=0 }">
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
									<span style="color: red" id="mmsg"></span>
								</c:if> <c:if test="${workDealInfo.dealInfoType==0 }">
									&nbsp;&nbsp;<span id="applyYear">${workDealInfo.year}年</span>
								</c:if></td>
							
							<c:if test="${expirationDate!=null }">
							<th id="supportDateTh">指定截止日期：</th>
							<td id="supportDateTd">
								<input class="input-medium Wdate" type="text" 
							required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
							 maxlength="20" readonly="readonly" value="<fmt:formatDate value="${expirationDate}" pattern="yyyy-MM-dd"/>"
							name="expirationDate" id="expirationDate"/>
							</td>
							</c:if>	
							
							
																
								
							
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
							<td><input type="text" name="companyName" id="companyName"
								disabled="disabled"
								value="${workDealInfo.workCompany.companyName}"></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位类型：</th>
							<td><select name="companyType" disabled="disabled">
									<option value="1" id="companyType1"
										<c:if 

test="${workDealInfo.workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if 

test="${workDealInfo.workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if 

test="${workDealInfo.workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4"
										<c:if 

test="${workDealInfo.workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5"
										<c:if 

test="${workDealInfo.workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码：</th>
							<td><input type="text" name="organizationNumber"
								disabled="disabled" id="organizationNumber"
								value="${workDealInfo.workCompany.organizationNumber}" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" disabled="disabled"
								type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								value="<fmt:formatDate 

value="${workDealInfo.workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"></input></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>服务级别：</th>
							<td><select name="selectLv" disabled="disabled">
									<option value="0" id="selectLv0"
										<c:if 

test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1"
										<c:if 

test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
							</select></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照：</th>
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
							<th><span class="prompt" style="color: red; display: none;">*</span>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text"
								disabled="disabled"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" maxlength="20"
								readonly="readonly" name="comCertficateTime"
								value="<fmt:formatDate 

value="${workDealInfo.workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"></input></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>法人姓名：</th>
							<td><input type="text" name="legalName" disabled="disabled"
								value="${workDealInfo.workCompany.legalName}"></td>
						</tr>
						<tr>
							<th>行政所属区：</th>
							<td>
							
							
							<select id="s_province" name="s_province" disabled="disabled"
								style="width: 100px;">
							</select>&nbsp;&nbsp; <select id="s_city" name="s_city" disabled="disabled"
								style="width: 100px;"></select>&nbsp;&nbsp; <select
								id="s_county" name="s_county" style="width: 100px;" disabled="disabled"></select> 
								<script type="text/javascript">
								$("#s_province").append('<option value="${workDealInfo.workCompany.province}" selected="selected">${workDealInfo.workCompany.province}</option>');
								$("#s_city").append('<option value="${workDealInfo.workCompany.city}" selected="selected">${workDealInfo.workCompany.city}</option>');
								$("#s_county").append('<option value="${workDealInfo.workCompany.district}" selected="selected">${workDealInfo.workCompany.district}</option>');
								</script>
								
								<c:if test="${workDealInfo.workCompany.areaRemark!=null}">
								<div style="margin-top: 8px;">
									区域备注：<input type="text" name="areaRemark"
									value="${workDealInfo.workCompany.areaRemark }" disabled="disabled" style="width:242px">
								</div>
							</c:if>
						
								
								
							
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
							<th class="btmBorder">单位联系电话：</th>
							<td class="btmBorder"><input type="text" name="companyMobile"
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
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								onblur="nameFill(this)"
								value="${workDealInfo.workUser.contactName }" /></td>
							<th>证书持有人证件:</th>
							<td><select name="conCertType" disabled="disabled">
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
							<th><span class="prompt" style="color: red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber"
								id="conCertNumber"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.conCertNumber }"
								onblur="numberFill()" /></td>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactEmail }"
								onblur="emailFill(this)" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactPhone }"
								onblur="checkMobile(this)" /></td>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>业务系统UID:</th>
							<td class="btmBorder"><input type="text" name="contactTel" id="contactTel"
								<c:if test="${!canEdit }">disabled="disabled"</c:if>
								value="${workDealInfo.workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>证书持有人性别:${workDealInfo.workUser.contactSex}</th>
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
			<div class="span12" id="proposer"<%-- <c:if test="${workDealInfo.configProduct.productName!=2}"> 

style="display:none"</c:if> --%>>
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人姓名:</th>
							<td><input type="text" name="pName"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workCertApplyInfo.name }" /></td>
							<th class="btmBorder"><span class="prompt" style="color: red; display: none;">*</span>经办人身份证号:</th>
							<td class="btmBorder"><input type="text" name="pIDCard"
								<c:if test="${!canEdit 

}">disabled="disabled"</c:if>
								value="${workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color: red; display: none;">*</span>经办人邮箱:</th>
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
							<td><input type="text" id="recordContent" name="recordContent"></td>
							<td>${user.name }</td>
							<td>${user.office.name }</td>
							<td>${date }</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<input type="hidden" name="deal_info_status" value="5">
		<c:if test="${iseditor =='iseditor'}">
			<div class="control-group span12">
				<div class="span12">
					<table class="table">
						<tbody>
							<tr>
								<td style="text-align: center; width: 100%; border-top: none;"
									colspan="2"><shiro:hasPermission
										name="work:workDealInfo:edit">
										<input id="btnSubmit" class="btn btn-primary" type="button"
											onclick="onSubmit()" value="提 交" />&nbsp;</shiro:hasPermission> <input
									id="btnCancel" class="btn" type="button" value="返 回"
									onclick="history.go(-1)" /> <input type="hidden" id="isOK"
									name="isOK" value="${isOK }"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
	</form:form>
	<c:if test="${iseditor !='iseditor'}">
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
	</c:if>
</body>
</html>
