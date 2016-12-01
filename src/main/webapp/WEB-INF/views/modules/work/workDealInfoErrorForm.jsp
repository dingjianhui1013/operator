<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<style type="text/css">
.accordion-heading, .table th{width:140px;}
.table-condensed td{width:485px;}
.Wdate{width:206px;}
.btmBorder{border-bottom:1px solid #ddd}
.accordion-heading,.table th,.accordion-heading,.table td{ vertical-align: middle;}

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

</style>

<script type="text/javascript">
    var ctx = "${ctx}";
    var ctxStatic = "${ctxStatic}";
    var imgPath = "${imgPath}";
    var province = "${workDealInfo.workCompany.province}";
    var city = "${workDealInfo.workCompany.city}";
    var district = "${workDealInfo.workCompany.district}";
</script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDrag.js"></script>
<script type="text/javascript" src="${ctxStatic}/dialog/zDialog.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		getPrivince();
		
		if("${imgNames}"!=null && "${imgNames}"!=""){
			var imgNames = "${imgNames}";
			var str1 = new Array();                      
			str1 = imgNames.split(",");  
	
			var namestr = "";
			for(var i = 0;i < str1.length; i++){
				var str = $("<div class='uploadImgList'><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p><span class="s-closeBtn icon-remove-sign" data="'+str1[i]+'"></span></div>');
				$("#imgLayer").append(str);
				var imgBoxMod=$(".ctnlist .text img");
			    imgPop(imgBoxMod);
			    imgDel(str);
			    namestr+=str1[i].substring(str1[i].lastIndexOf('/')+1,str1[i].length)+",";
			}
			if(namestr!=''){
				$("#imgNames").val(namestr.substring(0,namestr.length-1));
			}
		}
		
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
								.getJSON(url,function(d) {$("#app").bigAutocomplete({
																data : d.lis,
																callback : function(data) {

																	var url1 = "${ctx}/work/workDealInfo/product?appId=";
																	var productHtml="";
																	productHtml+="<option value='0'>请选择</option>";
																	$.getJSON(url1 + data.result+"&_="+new Date().getTime(),function(da) {
																		$("#appId").val(da.appId);
																		if (da.product10!=null) {
																			productHtml+="<option value='"+da.product10+"'>企业证书[通用]</option>";
																		} 
																		if (da.product11!=null) {
																			productHtml+="<option value='"+da.product11+"'>企业证书[专用]</option>";
																		}
																		if (da.product20!=null) {
																			productHtml+="<option value='"+da.product20+"'>个人证书(企业)[通用]</option>";
																		} 
																		if (da.product21!=null) {
																			productHtml+="<option value='"+da.product21+"'>个人证书(企业)[专用]</option>";
																		}
																		if (da.product30!=null) {
																			productHtml+="<option value='"+da.product30+"'>机构证书[通用]</option>";
																		} 
																		if (da.product31!=null) {
																			productHtml+="<option value='"+da.product31+"'>机构证书[专用]</option>";
																		}
																		if (da.product40!=null) {
																			productHtml+="<option value='"+da.product40+"'>可信移动设备[通用]</option>";
																		} 
																		if (da.product41!=null) {
																			productHtml+="<option value='"+da.product41+"'>可信移动设备[专用]</option>";
																		}
																		if (da.product60!=null) {
																			productHtml+="<option value='"+da.product60+"'>个人证书(机构)[通用]</option>";
																		} 
																		if (da.product61!=null) {
																			productHtml+="<option value='"+da.product61+"'>个人证书(机构)[专用]</option>";
																		}
																		
																		$("#product").html(productHtml);
																					});
																}
															});
										});

						var url = "${ctx}/work/workDealInfo/tt?_="+new Date().getTime();
						$
								.getJSON(url,function(da) {$("#tt").bigAutocomplete(
															{data : da.lis,callback : function(data) {
																	var url1 = "${ctx}/work/workDealInfo/cert?id=";
																	$.getJSON(url1 + data.result+"&_="+new Date().getTime(),function(d) {
																						$("#companyId").val(d.companyId);
																						$("#companyName").val(d.companyName);
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
																						$("#organizationNumber").val(d.organizationNumber);
																						$("#orgExpirationTime").val(d.orgExpirationTime);
																						if (d.selectLv==0) {
																							$("#selectLv0").attr("selected","selected");
																						}
																						if (d.selectLv==1) {
																							$("#selectLv1").attr("selected","selected");
																						}
																						if (d.comCertificateType == 0) {
																							$("#comCertificateType0").attr("selected","selected");
																						}
																						if (d.comCertificateType == 1) {
																							$("#comCertificateType1").attr("selected","selected");
																						}
																						if (d.comCertificateType == 2) {
																							$("#comCertificateType2").attr("selected","selected");
																						}
																						if (d.comCertificateType == 3) {
																							$("#comCertificateType3").attr("selected","selected");
																						}
																						if (d.comCertificateType == 4) {
																							$("#comCertificateType4").attr("selected","selected");
																						}
																						$("#comCertficateNumber").val(d.comCertficateNumber);
																						$("#comCertficateTime").val(d.comCertficateTime);
																						$("#legalName").val(d.legalName);
																						$("#address").val(d.address);
																						$("#companyMobile").val(d.companyMobile);
																						$("#remarks").val(d.remarks);
																						$("#s_province").val(d.province);
																						$("#contactName").val(d.contactName);
																						if ('男' == d.conCertSex) {
																							$("#sex0").attr("checked","checked");
																						}
																						if (d.conCertSex =='女') {
																							$("#sex1").attr("checked","checked");
																						}
																						if (d.conCertType == 0) {
																							$("#conCertType0").attr("selected","selected");
																						}
																						if (d.conCertType == 1) {
																							$("#conCertType1").attr("selected","selected");
																						}
																						if (d.conCertType == 2) {
																							$("#conCertType2").attr("selected","selected");
																						}
																						$("#contacEmail").val(d.contacEmail);
																						$("#contactPhone").val(d.contactPhone);
																						$("#contactTel").val(d.contactTel);
																						$("#workuserId").val(d.workuserId);
																						$("#conCertNumber").val(d.conCertNumber);
																						$("#s_city").append('<option value="'+d.city+'" selected="selected">'+d.city+'</option>');
																						$("#s_county").append('<option value="'+d.district+'" selected="selected">'+d.district+'</option>');
																						showCert(d.companyId);
																					});
																}
															});
										});
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
						
						}
						
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
										showYear();
									}else{
										styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
									}
									
								});
								$("#agentDetailId").html(styleHtml);
								
								
								
							});
						}
						
						
$("#product").change(function(){
							
							var product = $("#product").val();
							var agentHtml="";
							var styleHtml="";
							if (product!=0) {
								var url = "${ctx}/work/workDealInfo/setStyleList1?productId="+product+"&_="+new Date().getTime();
								$.getJSON(url,function(data){
									/* showAgent(product); */
									
									
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
									showYear();
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
						
						if(${expirationDate!=null}){
							$("#year1").removeAttr("checked");
							$("#year2").removeAttr("checked");
							$("#year3").removeAttr("checked");
							$("#year4").removeAttr("checked");
							$("#year5").removeAttr("checked");
						}
						
					});
	
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
	
	
</script>
<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery.bigautocomplete.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/city.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/area.js"></script>
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
	function os() {
		$("#inputForm")
				.attr("action", "${ctx}/work/workDealInfo/temporarySave");
		$("#inputForm").submit();
	}

	function productLabel(data) {
		var appId = $("#appId").val();
		var url = "${ctx}/work/workDealInfo/type?name=" + data + "&appId="
				+ appId;
		$.getJSON(url+"&_="+new Date().getTime(), function(da) {
			if (da.type0) {
				$("#lable0").attr("checked", "checked");
				showYear(0);
			}
			if (da.type1) {
				$("#lable1").attr("checked", "checked");
				showYear(1);
			}
		});
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
	function onSubmit(){
		$("#lable0").removeAttr("disabled");
		$("#lable1").removeAttr("disabled");
		$("#dealInfoType").removeAttr("disabled");
		return true;
	}
	/* 
	* 功能:根据产品带回年限
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showYear(){
		
		var agentId = $("#boundId").val();
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
			}
			if(!data.support){
				$("#supportDateTh").hide();
				$("#supportDateTd").hide();
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
	
	function numberFill(){
		$("#pIDCard").val($("#conCertNumber").val());
	}
	
	function emailFill(obj){
		var a = form_check(obj);
		if (a){
			$("#pEmail").val($("#contacEmail").val());
		}
	}
	
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
				return false; 
			}
			$("#contactPhone").after("<span id='phonepro' style='color:red'>请输入正确的手机号码</span>");
			return false; 
		} 
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
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<li class="active"><a
			href="${ctx}/work/workDealInfoOperation/errorForm?id=${workDealInfo.id}">业务编辑</a></li>
	</ul>
	
	<%-- <div id="modal-container" class="modal hide fade" style="width:800px;height:700px;right:10%;top:34px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		
		<!--图像采集区域 -->
		<div id="imageCollection" style="width: 800px; height: 700px;display: none">
        <object id="VideoInputCtl" classid="CLSID:30516390-004F-40B9-9FC6-C9096B59262E" style="width: 100%; height: 80%;"></object>
    	
    	
    	<div class="control-group">
		
			<div class="control-group" align="center">
				<div class="form-group btnGrop">
					<button id="qrsq" class="btn btn-primary" onclick="changeDevice()">切换摄像头</button>&nbsp;&nbsp;&nbsp;			
					<button id="qrsq" class="btn btn-primary" onclick="setPropertyDevice()">设置装置属性</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="getcompanyinfo()">单位信息录入</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="getholderinfo()">持有人信息录入</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="getoperatorinfo()">经办人信息录入</button>
				</div>
				<div class="form-group btnGrop">
					<button id="qrsq" class="btn btn-primary" onclick="applicationphotograph('${imgPath}')">申请表拍照</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="workCompanyphotograph('${imgPath}')">单位证件拍照</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="workCertApplyInfophotograph('${imgPath}')">经办人身份证拍照</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="workUserphotograph('${imgPath}')">持有人身份证拍照</button>&nbsp;&nbsp;&nbsp;
					<button id="qrsq" class="btn btn-primary" onclick="headphotograph('${imgPath}')">照片拍照</button>&nbsp;&nbsp;&nbsp;
				</div>
				<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			</div>
		</div>
    	
    	
    	
    	</div>
		
	</div> --%>
	
	<form:form id="inputForm" action="${ctx}/work/workDealInfo/save"
		method="POST" class="form-horizontal">
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
							<!-- <th colspan="4" style="font-size: 20px;"><span class="prompt" style="color:red; display: none;">*</span>基本信息</th> -->
							<th colspan="1" style="font-size: 20px;"><span
								class="prompt" style="color: red; display: none;">*</span>基本信息</th>	
							<th colspan="3"> <a href="#" data-toggle="modal">
							<input id="scan" class="btn btn-primary smBtn" onclick="scanningInfoEnter()" data-toggle="modal" value="扫描录入" /></a>	
							</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>代办应用：</th>
							<td ><input type="text" name="configApp" 
								value="${workDealInfo.configApp.appName }" id="app" /></td>
							
							<%-- <th><span class="prompt" style="color:red; display: none;">*</span>业务类型：</th>--%>
							<th><span class="prompt" style="color: red; display: none;">*</span>业务类型：</th>
							<c:if test="${workDealInfo.dealInfoType==0}">
								<td><input type="checkbox" name="dealInfoType" value="0" checked="checked" disabled="disabled"
									checked>新增证书
								</td> 
							</c:if>
							<c:if test="${workDealInfo.dealInfoType==1}">
								<td>
								<input type="checkbox" disabled="disabled" checked="checked" value="0" name="dealInfoType">
								<font color="grey" style="font-weight:bold;">更新证书</font>
								<input type="hidden" value="0" name="dealInfoType">
								<c:if test="${workDealInfo.dealInfoType1==2}"><input type="checkbox" disabled="disabled" checked="checked" value = "1"
									name="dealInfoType1"><font color="red" style="font-weight:bold;">遗失补办</font><input type="hidden" value="1" name="dealInfoType1"></c:if>
									<c:if test="${workDealInfo.dealInfoType1==3}"><input type="checkbox" disabled="disabled" checked="checked" value = "2"
									name="dealInfoType1"><font color="red" style="font-weight:bold;">损坏更换</font><input type="hidden" value="2" name="dealInfoType1"></c:if>
								
								&nbsp;&nbsp;
									<!-- <input class="btn btn-primary" type="button" value="更改业务类型" onclick="changeDealInfoType()"  /> -->
								</td>
							</c:if>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>选择产品：</th>
							<td>
							
							<select name="product"  id="product">
									<c:forEach items="${proList}" var="product">
										<option value="${product.id}" <c:if test="${product.id==workDealInfo.configProduct.id }">selected="selected"</c:if> >${product.name}</option>
									</c:forEach>
							</select>	
							</td>
						
							<th style="width: 100px;"><span class="prompt" style="color:red; display: none;">*</span>申请年数：</th>
							<td><input type="radio" name="year" value="1" id="year1"
								<c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span id="word1">1年</span> <input
								type="radio" name="year" value="2" id="year2"
								<c:if test="${workDealInfo.year==2}">checked</c:if>><span id="word2">2年 </span>
								
								<input
								type="radio" name="year" value="2" id="year3"
								<c:if test="${workDealInfo.year==3}">checked</c:if>><span id="word3">3年</span>
								
								<input
								type="radio" name="year" value="4" id="year4"
								<c:if test="${workDealInfo.year==4}">checked</c:if>><span id="word4">4年</span><input
								type="radio" name="year" value="5" id="year5"
								<c:if test="${workDealInfo.year==5}">checked</c:if>><span id="word5">5年</span>
							</td>
						
							
						</tr>
						
						
						<tr>
						<th ><span class="prompt"
								style="color: red; display: none;">*</span>计费策略类型：</th>
							<td  style="width: 250px;"><select id="agentId"
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
							</td>
						</tr>
						
						<tr>
								<th   id="supportDateTh" style="display: none">选择截止日期：</th>
						<td   id="supportDateTd" style="display: none">
								<input class="input-medium Wdate" type="text"
							 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
							 maxlength="20" readonly="readonly" value="<fmt:formatDate value="${expirationDate}" pattern="yyyy-MM-dd"/>"
							name="expirationDate" id="expirationDate"/>
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
							<th><span class="prompt" style="color:red; display: none;">*</span>单位名称：</th>
							<td><input type="text" name="companyName" 
								value="${workCompany.companyName }" id="tt"> <input
								type="hidden" name="companyId" value="${workCompany.id }"
								id="companyId"></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位类型：</th>
							<td><select name="companyType" >
									<option value="1" id="companyType1"
										<c:if test="${workCompany.companyType==1 }">selected</c:if>>企业</option>
									<option value="2" id="companyType2"
										<c:if test="${workCompany.companyType==2 }">selected</c:if>>事业单位</option>
									<option value="3" id="companyType3"
										<c:if test="${workCompany.companyType==3 }">selected</c:if>>政府机关</option>
									<option value="4" id="companyType4" <c:if test="${workCompany.companyType==4 }">selected</c:if>>社会团体</option>
									<option value="5" id="companyType5" <c:if test="${workCompany.companyType==5 }">selected</c:if>>其他</option>
							</select></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>组织机构代码：</th>
							<td><input type="text" name="organizationNumber" 
								value="${workCompany.organizationNumber }"
								id="organizationNumber" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>组织机构代码有效期：</th>
							<td><input class="input-medium Wdate" 
								value="<fmt:formatDate value="${workCompany.orgExpirationTime }" pattern="yyyy-MM-dd"/>"
								type="text" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
								maxlength="20" readonly="readonly" name="orgExpirationTime"
								id="orgExpirationTime" /></td>

						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>服务级别：</th>
							<td><select name="selectLv">
									<option value="0" id="selectLv0" <c:if test="${workDealInfo.workCompany.selectLv==0}">selected</c:if>>大客户</option>
									<option value="1" id="selectLv1" <c:if test="${workDealInfo.workCompany.selectLv==1}">selected</c:if>>普通客户</option>
								</select></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位证照：</th>
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
							<th><span class="prompt" style="color:red; display: none;">*</span>证件号：</th>
							<td><input type="text" name="comCertficateNumber"
								value="${workCompany.comCertficateNumber }"
								id="comCertficateNumber" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>单位证照有效期：</th>
							<td><input class="input-medium Wdate" type="text" 
								value="<fmt:formatDate value="${workCompany.comCertficateTime }"  pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" 
								maxlength="20" readonly="readonly" name="comCertficateTime"
								id="comCertficateTime" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none; ">*</span>法人姓名：</th>
							<td style="vertical-align: middle;"><input type="text" name="legalName" id="legalName" 
								value="${workCompany.legalName }"></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>行政所属区：</th>
							<td><select id="s_province" name="s_province" onchange="getCity(this.options[this.options.selectedIndex].id)" style="width: 100px;"></select>&nbsp;&nbsp; 
								<select id="s_city" name="s_city" onchange="getTown(this.options[this.options.selectedIndex].id)" style="width: 100px;"></select>&nbsp;&nbsp; 
								<select id="s_county" name="s_county" style="width: 100px;"></select> 
								<script type="text/javascript">
								if(${provinceId!=null}){
									getCity(${provinceId});
								}
								if(${cityId!=null}){
									getTown(${cityId});
								}
								</script>
								
								<div id="show"></div></td>
						</tr>
						<tr>
							<th>区域备注：</th>
							<td><input type="text" name="areaRemark" value="${workDealInfo.workCompany.areaRemark }" ></td>
						
							<th><span class="prompt" style="color:red; display: none;">*</span>街道地址：</th>
							<td><input type="text" name="address" id="address"
								value="${workCompany.address }"></td>
							

						</tr>
						<tr>
							<th class="btmBorder"><span class="prompt" style="color:red; display: none;">*</span>单位联系电话：</th>
							<td class="btmBorder"><input type="text" name="companyMobile" 
								id="companyMobile" value="${workCompany.companyMobile }"></td>
						
						
							<th><span class="prompt" style="color:red; display: none;">*</span>备注信息：</th>
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
							<th><span class="prompt" style="color:red; display: none;">*</span>证书持有人姓名:</th>
							<td><input type="text" name="contactName" id="contactName" 
								maxlength="20" value="${workUser.contactName }" /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>证书持有人证件:</th>
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
							<th><span class="prompt" style="color:red; display: none;">*</span>证件号码:</th>
							<td><input type="text" name="conCertNumber" maxlength="18"
								onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
								id="conCertNumber" value="${workUser.conCertNumber }"
								onchange="numberFill()" 
								 /></td>
							<th><span class="prompt" style="color:red; display: none;">*</span>证书持有人电子邮件:</th>
							<td><input type="text" name="contacEmail" id="contacEmail" maxlength="50" onchange="emailFill(this)"
								value="${workUser.contactEmail }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>证书持有人手机号:</th>
							<td><input type="text" name="contactPhone" id="contactPhone" maxlength="11"
								value="${workUser.contactPhone }" 
								onblur="checkMobile(this)"
								/></td>
							<th class="btmBorder"><span class="prompt" style="color:red; display: none;">*</span>业务系统UID:</th>
							<td class="btmBorder"><input type="text" name="contactTel" id="contactTel" maxlength="20"
								value="${workUser.contactTel }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>证书持有人性别:</th>
							<td><input name="contactSex" id="sex0" 
							<c:if test="${workDealInfo.workUser.contactSex=='男' }">checked</c:if> 
							type="radio" value="男">男&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="contactSex" id="sex1" 
							<c:if test="${workDealInfo.workUser.contactSex=='女'}">checked</c:if> 
							type="radio" value="女">女</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" id="proposer" >
				<table class="table table-striped table-bordered table-condensed">
					<tbody>
						<tr>
							<th colspan="4" style="font-size: 20px;">经办人信息</th>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人姓名:</th>
							<td><input type="text" name="pName" id="pName" maxlength="20"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.name }" onchange="checkSqr(this);"/></td>
							<th class="btmBorder"><span class="prompt" style="color:red; display: none;">*</span>身份证号:</th>
							<td class="btmBorder"><input type="text" name="pIDCard" maxlength="18" id="pIDCard"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.idCard }" /></td>
						</tr>
						<tr>
							<th><span class="prompt" style="color:red; display: none;">*</span>经办人邮箱:</th>
							<td><input type="text" name="pEmail" id="pEmail"
								value="${workDealInfo.workCertInfo.workCertApplyInfo.email }" onchange="form_check(this)"  /></td>
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
								<td><fmt:formatDate value="${workLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
		<div class="control-group span12">
			<div class="span12">
				<table class="table">
					<tbody>
						<tr>
							<td style="text-align: center; width: 100%; border-top: none;"
								colspan="2">&nbsp; <shiro:hasPermission
									name="work:workDealInfo:edit">
									<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return onSubmit()"
										value="提 交" />&nbsp;</shiro:hasPermission> <input id="btnCancel" class="btn"
								type="button" value="返 回" onclick="history.go(-1)" />
								
								</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>

</html>
