<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.input-medium{width:206px;}
</style>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(
		function() {
			
			var url = "${ctx}/work/workDealInfo/app?_="+new Date().getTime();
			$.getJSON(url,function(d) {
								appData = d;
								$("#app").bigAutocomplete(
												{data : d.lis,
													callback : function(data) {
														$("#product").html("");
														$("#product").attr("onchange","setStyleList()");
														var url1 = "${ctx}/work/workDealInfo/product?appId=";
														
														var productHtml="";
														productHtml+="<option value='0'>请选择</option>";
														
														$.getJSON(url1 + data.result+"&_="+new Date().getTime(),
																
																		function(da) {
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
			
			if("${workDealInfo.id}"!=null && "${workDealInfo.id}"!=""){
				var boundLabelList = "${boundLabelList}";
				$("#agentId").attr("onchange","setTemplateList()");
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
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function hisgoMoney(obj) {
		var html = "<div style='padding:10px;'><input type='radio' value='0' name='yourname' checked='checked'>变更缴费类型<br><input type='radio' value='1' name='yourname'>现金退费</div>";
		var submit = function(v, h, f) {
			if (f.yourname == '') {
				$.jBox.tip("请选择退费方式。", 'error', {
					focusId : "yourname"
				}); // 关闭设置 yourname 为焦点
				return false;
			}
			window.location.href = "${ctx}/work/workDealInfoAudit/backMoneyFrom?id="
					+ obj + "&type=" + f.yourname;
			return true;
		};

		top.$.jBox(html, {
			title : "请选择退费方式！",
			submit : submit
		});
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
								var url = "${ctx}/work/workDealInfo/findById?dealInfoId="+id;
								if(dealType.indexOf("3")>=0){
									$.getJSON(url + "&_="+new Date().getTime(),	function(data){
												if (data.status==1){
													if(data.isMainTain==1){
														top.$.jBox.tip("更新业务已在自助系统中办理！");
													}else{
														if (data.isUpdate==0) {
															top.$.jBox.tip("证书未在更新范围内，不允许更新此证书 ！");
														}else{
															window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
														}
													}
												}else{
													top.$.jBox.tip("系统异常");
												}
									});
								}else if(dealType.indexOf("4")>=0){
									window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
									
								}else{
									$.getJSON(url + "&_="+new Date().getTime(),	function(data){
										if (data.status==1){
											if(dealType.indexOf("1")>=0&&data.isMainTain==1){
												top.$.jBox.tip("业务已在自助系统中办理！");
											}else{
												if (data.isDeal==1) {
													top.$.jBox.tip("证书已过期，不允许此证书办理所选业务 ！");
												}else{
													window.location.href = "${ctx}/work/workDealInfo/typeForm?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
												}
											}
										}else{
											top.$.jBox.tip("系统异常");
										}
									});
									
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
	
	function returnDealInfo(obj){
		var id = obj;
		window.location.href = "${ctx}/work/workDealInfo/typeFormReturnUpdate?id="+id;
	}
	
	
	
	function addAttach() {
		if($("#appId").val() == ""){
			top.$.jBox.tip("请输入应用名称！");
		}
		/* var product = $("input[name='product']:checked").val()
		$("#productT").val(product);
		if(product == null || product == ''){
			top.$.jBox.tip("请选择产品！");
        	return false;
		}
		var lable = $("input[name='lable']:checked").val()
		$("#lableT").val(lable);
		if(lable == null || lable == ''){
			top.$.jBox.tip("请选择应用标识！");
        	return false;
		}
		
		
		$("#agentIdT").val($("#agentId").val());
		if($("#agentId").val() == "0"){
			top.$.jBox.tip("请配置产品的计费策略模板！");
        	return false;
		} */
		
		if($("#product").val()==0){
			top.$.jBox.tip("请选择要办理的产品！");
			return false;
		}
		
		if($("#agentId").val()==0){
			top.$.jBox.tip("请选择计费策略类型！");
			return false;
		}else{
			if($("#agentDetailId").val()==0){
				top.$.jBox.tip("请选择计费策略模板！");
				return false;
			}
		}
		
		
		$("#agentIdT").val($("#agentId").val());
		$("#agentDetailIdT").val($("#agentDetailId").val());
		var year = $("input[name='year']:checked").val()
		$("#yearT").val(year);
		if(year == null || year == ''){
			top.$.jBox.tip("请选择申请年数！");
        	return false;
		}
		
		if($("#fileName").val() == ""){
			top.$.jBox.tip("导入文件格式有误，导入文件应为Excel文件，请确认");
        	return false;
        }
        if($("#fileName").val().indexOf('.xls')<0) {
        	top.$.jBox.tip("导入文件格式有误，导入文件应为Excel文件，请确认");
            return false;
        }
        top.$.jBox.tip("正在批量导入新增数据...", 'loading');
       
		var options = {
			type : 'post',
			dataType : 'json',
			success : function(data) {
				//console.log(data);
				if(data.status=='1'){
					top.$.jBox.tip("上传成功");
					  setTimeout(function (){
	            		    //something you want delayed
	            		    	$("#searchForm").submit();
	            		//	window.location.reload();
	            		   }, 1500); // how long do you want the delay to be? 
	            
				}else if(data.status=='-1'){
					top.$.jBox.tip("上传失败!");
					var info = "失败信息:<br>"+data.msg;
					top.$.jBox.info(info);
					document.getElementById("declareDiv").style.display= "none";
					//top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else if(data.status==null||typeof(data.status)=='undefined'){
					top.$.jBox.tip("上传失败!");
					document.getElementById("declareDiv").style.display= "none";
					//top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else{
					top.$.jBox.tip("上传失败!");
					var info = "失败信息:<br>"+data.msg;
					top.$.jBox.info(info);
					document.getElementById("declareDiv").style.display= "none";
					//top.$.jBox.tip("上传失败："+data.errorMsg);
					//$("#searchForm").submit();
				}
			}
		};
		$('#materialImport').ajaxSubmit(options);
	}
	

	
	function checkAll(obj){
		var check = $($("#contentTable").find("#checkAll"));
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if (check.is(":checked") == true) {
			$('input:checkbox').each(function() {
		        $(this).attr('checked', true);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == true) {
					var checkOne = check.val();
					if (checkIds.indexOf(checkOne)<0) {
						if(checkIds==''){
							checkIds+=check.val();
						}else{
							checkIds+=","+check.val();
						}
					}
				}
			}
			checkIds = checkIds.replace(",,", ",");
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}else{
			$('input:checkbox').each(function () {
		        $(this).attr('checked',false);
			});
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
				if (check.is(":checked") == false && check.val()!="${page.pageNo}") {
					checkIds = checkIds.replace(check.val(), "");
					checkIds = checkIds.replace(",,", ",");
				}
			}
			if (checkIds==",") {
				$("#checkIds").val("");
			}else{
				$("#checkIds").val(checkIds);
			}
		}
	}
	
	
	function changeCheck(obj){
		var checkIds = $("#checkIds").val();
		var xz = $("#contentTable").find("[name='oneDealCheck']");
		if(checkIds.indexOf($(obj).val())>-1){
			checkIds = checkIds.replace($(obj).val(), "");
		}
		
		for (var a = 0; a <xz.length; a++) {
			var check = $($("#contentTable").find("[name='oneDealCheck']")[a]);
			if (check.is(":checked") == true && check.val()!="${page.pageNo}") {
				var checkOne = check.val();
				if (checkIds.indexOf(checkOne)<0) {
					if(checkIds==''){
						checkIds+=check.val();
					}else{
						checkIds+=","+check.val();
					}
				}
			}
		}
		checkIds = checkIds.replace(",,", ",");
		if (checkIds==",") {
			$("#checkIds").val("");
		}else{
			$("#checkIds").val(checkIds);
		}
	}
	
	function updateCertOK(){
		var checkIds = $("#checkIds").val();
		if(checkIds==null||checkIds==""){
			top.$.jBox.tip("请选择您要更新的证书！");
		}else{
			var url = "${ctx}/work/workDealInfo/checkUpdateByIds?dealInfoIds="+checkIds+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if (data.status==1){
					if (data.isUpdate==0) {
						var info = "错误信息为:<br>&nbsp;&nbsp;&nbsp;&nbsp;"+data.html;
						top.$.jBox.info(info);
					}else{
						$("#appIdd").val(data.appIdd);
						$("#updateSize").val(data.updateSize);
						$("#productNamee").val(data.productNamee);
						$("#labell").val(data.labell);
						var lable = data.labell;
						showAgentByUpdate(lable);
						$("#declareDivUpdate").show();
					}
				}else{
					top.$.jBox.tip("系统异常");
				}
			});
		}
		
	}
	
	function hideUpdateDiv(){
		
		$("#declareDivUpdate").hide();
		
	}
	
	/* 
	* 功能:根据产品带回计费模版
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showAgentByUpdate(obj){
		var lable = obj;
		var productName = $("#productNamee").val();
		var url = "${ctx}/work/workDealInfo/showAgentProduct?lable="+lable+"&productName="+productName+"&app="+$("#appIdd").val()+"&infoType=1&_="+new Date().getTime();
		$.getJSON(url,function(data){
			if(data.tempStyle!=-1){
				var map = data.typeMap;
				var agentHtml="";
				$("#agentIdByUpdate").attr("onchange","setStyleListByUpdate("+lable+")");
				$.each(map, function(i, item){
					agentHtml+="<option onchange=setStyleListByUpdate("+lable+")  value='"+item.id+"'>" + item.name + "</option>";
				});
				$("#agentIdByUpdate").html(agentHtml);
				var styleList = data.boundStyleList;
				var styleHtml="";
				$.each(styleList, function(i, item2){
					if(i==0){
						$("#bounddId").val(item2.id);
						showYearByUpdate();
					}
					styleHtml +="<option value='"+item2.id+"'>" + item2.name + "</option>";
				});
				$("#agentDetailIdByUpdate").html(styleHtml);
			}else{
				var agentHtml="<option value='0'>请选择</option>";
				$("#agentIdByUpdate").html(agentHtml);
				var styleHtml="<option value='0'>请选择</option>";
				$("#agentDetailIdByUpdate").html(styleHtml);
				
				$("#year1U").show();
				$("#word1U").show();
				$("#year2U").show();
				$("#word2U").show();
				$("#year4U").show();
				$("#word4U").show();
				$("#year5U").show();
				$("#word5U").show();
				$(".prompt").css("display","none");
				top.$.jBox.tip("请先配置计费策略！"); 
			}
			
			
		});
	}
	
	/*
	* 给计费策略模版配置赋值
	*/
	function setStyleListByUpdate(obj){
		var lable = obj;
		var productName = $("#productNamee").val();
		var agentId = $("#agentIdByUpdate").val();
		if (agentId!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+$("#appIdd").val()+"&infoType=1&style="+agentId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				var styleList = data.array;
				var styleHtml="";
				$.each(styleList,function(i,item){
					if(i==0){
						$("#bounddId").val(item.id);
						showYearByUpdate();
					}
					styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
					
				});
				$("#agentDetailIdByUpdate").html(styleHtml);
			});
		}else{
			top.$.jBox.tip("请您选择产品！");
			
		}
	}
	
	/* 
	* 功能:根据产品带回年限
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showYearByUpdate(){
		var agentId = $("#bounddId").val();
		//var url = "${ctx}/work/workDealInfo/showYear?lable="+lable+"&productName="+productName+"&app="+$("#appId").val()+"&infoType=0&_="+new Date().getTime();
		var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+agentId+"&infoType=1&_="+new Date().getTime();
		
		$.getJSON(url, function(data) {
			if (data.year1) {
				$("#year1U").show();
				$("#word1U").show();
			} else {
				$("#year1U").hide();
				$("#word1U").hide();
			}
			if (data.year2) {
				$("#year2U").show();
				$("#word2U").show();
			} else {
				$("#year2U").hide();
				$("#word2U").hide();
			}
			if (data.year3) {
				$("#year3U").show();
				$("#word3U").show();
			} else {
				$("#year3U").hide();
				$("#word3U").hide();
			}
			if (data.year4) {
				$("#year4U").show();
				$("#word4U").show();
			} else {
				$("#year4U").hide();
				$("#word4U").hide();
			}
			if (data.year5) {
				$("#year5U").show();
				$("#word5U").show();
			} else {
				$("#year5U").hide();
				$("#word5U").hide();
			}
			
			if(data.pos){
				$("#pay1").show();
				$("#methodPay1").show();
			}else{
				$("#pay1").hide();
				$("#methodPay1").hide();
			}
			if(data.money){
				$("#pay2").show();
				$("#methodPay2").show();
			}else{
				$("#pay2").hide();
				$("#methodPay2").hide();
			}
			if(data.bank){
				$("#pay3").show();
				$("#methodPay3").show();
			}else{
				$("#pay3").hide();
				$("#methodPay3").hide();
			}
			
			var updateSize = $("#updateSize").val();
			var agentType = $("#agentIdByUpdate").val();
			if(agentType != 1){
				if(parseInt(updateSize)>parseInt(data.avaNum)){
					top.$.jBox.tip("该计费策略模版更新剩余数量不足，请检查！");
					$("#buttonByUpdate").attr("onclick","#");
				}else{
					$("#buttonByUpdate").attr("onclick","updateSubmit()");
				}
				
			}
			
			
		});
		
	}
	
	//获取计费模版相对应的年限
	function setYearByBoundIdByUpdate(){
		var boundId = $("#agentDetailIdByUpdate").val();
		$("#bounddId").val(boundId);
		showYearByUpdate();
	}
	
	
	function updateSubmit(){
		
		var agentDetailIdByUpdate = $("#agentDetailIdByUpdate").val()
		if(agentDetailIdByUpdate==null || agentDetailIdByUpdate==0){
			top.$.jBox.tip("请选择计费策略模版！");
        	return false;
		}
		
		var year = $("input[name='yearU']:checked").val()
		if(year == null || year == ''){
			top.$.jBox.tip("请选择更新年数！");
        	return false;
		}
		var methodPay = $("input[name='methodPay']:checked").val()
		if(methodPay == null || methodPay == ''){
			top.$.jBox.tip("请选择缴费类型！");
        	return false;
		}
		
		var bounddId = $("#bounddId").val();
		var checkIds = $("#checkIds").val();
		
		 top.$.jBox.tip("正在批量更新业务...", 'loading');
			window.location.href = "${ctx}/work/workDealInfo/updateDealInfos?dealInfoIds="
			+ checkIds + "&year=" + year + "&bounddId=" + bounddId + "&methodPay="+methodPay ;
		
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
<!--  <script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE9.js"></script>-->
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<script type="text/javascript">
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
			if (da.type1) {
				$("#lable1").attr("checked", "checked");
				//showYear(1);
				showAgent(1);
			}else if (da.type0) {
				$("#lable0").attr("checked", "checked");
				//showYear(0);
				showAgent(0)
			}
			
		});
	}

	
	/* 
	* 功能:根据产品带回计费模版
	* 传参：lable+name
	* 返回值：年限1，2，4，5是否为true
	*/ 
	function showAgent(obj){
		var product = obj;
		var url = "${ctx}/work/workDealInfo/showAgentProductById?productId="+product+"&infoType=0&_="+new Date().getTime();
		$.getJSON(url,function(data){
			if(data.tempStyle!=-1){
				var map = data.typeMap;
				var agentHtml="";
				$("#agentId").attr("onchange","setTemplateList()");
				$.each(map, function(i, item){
					agentHtml+="<option onchange=setTemplateList()  value='"+item.id+"'>" + item.name + "</option>";
					
					
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
				$("#year3").show();
				$("#word3").show();
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
	* 给计费策略类型配置赋值
	*/
	function setStyleList(){
		var product = $("#product").val();
		var agentHtml="";
		var styleHtml="";
		if (product!=0) {
			var url = "${ctx}/work/workDealInfo/setStyleList1?productId="+product+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				showAgent(product);
				
				agentHtml+="<option value='0'>请选择</option>";
				$.each(data, function(i, item){					 
					 if(item=="1"){	
							agentHtml+="<option value='"+item+"'>标准</option>";
					}else if(item=="2"){
							agentHtml+="<option value='"+item+"'>政府统一采购</option>";
					}else if(item=="3"){
							agentHtml+="<option value='"+item+"'>合同采购</option>";
					}
				});	
				$("#agentId").html(agentHtml);
				$("#agentDetailId").html("");
				styleHtml+="<option value='0'>请选择</option>";
				$("#agentDetailId").html(styleHtml);
				
				
				if($("#agentId option").length==1){
					top.$.jBox.tip("请先配置计费策略！");
					return;
				}
				}); 	
		}
	}
	/*
	* 给计费策略模版配置赋值
	*/
	function setTemplateList(){
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
			if (data.year3) {
				$("#year3").show();
				$("#word3").show();
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
			
			var boundId =  $("#agentDetailId").val(); 
			var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#surplusNum").val(data.surplusNum);
				if($("#surplusNum").val()==0&&$("#agentId").val()!=1){
					top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！");
					$("#agentMes").show();
				}else{
					$("#agentMes").hide();
				}
				
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
	
	function replaceChecks(){
		$("#checkIds").val("");
	}
	
	function checkAdd(id)
	{
		var url="${ctx}/signature/signatureInfo/check";
		$.ajax({
			type:"POST",
			url:url,
			data:{"signatureId":id,"_":new Date().getTime()},
			dataType:"json",
			success:function(data){
				if(data.status=="1")
					{
						top.$.jBox.tip("此证书有未完成的印章，请先完成办理中的的印章业务！");
					}else if(data.status=="0")
						{
							window.location.href="${ctx}/signature/signatureInfo/form?workDealInfoId="+id;
						}
			}
		});

	}
	
	
</script>




</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/work/workDealInfo/">业务办理列表</a></li>
		<shiro:hasPermission name="work:workDealInfo:edit">
			<li><a href="${ctx}/work/workDealInfo/form">业务办理添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workDealInfo"
		action="${ctx}/work/workDealInfo/list" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="dealId" type="hidden" value="${workDealInfo.id}" />
		<div>
			<label>&nbsp;&nbsp;应用名称 ：&nbsp;&nbsp;</label>
			<select name="alias"
				id="alias">
				<option value="">请选择应用</option>
				<c:forEach items="${configAppList}" var="app">
					<option value="${app.id}"
						<c:if test="${app.id==alias}">
					selected="selected"
					</c:if>>${app.appName}</option>
				</c:forEach>
			</select>
			<label>&nbsp;&nbsp;产品名称：</label>
			<select name="productName"
				id="productName">
				<option value="">请选择产品</option>
				<option value="1"  <c:if test="${productName==1}">selected="selected"</c:if>>企业证书</option>
				<option value="2" <c:if test="${productName==2}">selected="selected"</c:if>>个人证书(企业)</option>
				<option value="3" <c:if test="${productName==3}">selected="selected"</c:if>>机构证书</option>
				<option value="4" <c:if test="${productName==4}">selected="selected"</c:if>>可信移动设备</option>
				<option value="6" <c:if test="${productName==6}">selected="selected"</c:if>>个人证书(机构)</option>
			</select>
			<label>业务状态：</label>
			<select name="dealInfoStatus" id="dealInfoStatus">
				<option value="">请选择业务类型</option>
				<c:forEach items="${wdiStatus}" var="type">
					<option value="${type.key}"
						<c:if test="${type.key==workType}">
					selected="selected"
					</c:if>>${type.value}</option>
				</c:forEach>
			</select> 
			<br>
		</div>
		<div style="margin-top: 8px">
<!-- 			&nbsp;&nbsp;<label>证书持有人姓名：</label> -->
<%-- 			<form:input path="workUser.contactName" htmlEscape="false" --%>
<%-- 				maxlength="50" class="input-medium" /> --%>
			&nbsp;&nbsp;<label>组代码号：</label>
			&nbsp;&nbsp; <form:input path="workCompany.organizationNumber" htmlEscape="false"
				maxlength="50" class="input-medium" />
		<label>&nbsp;&nbsp;KEY编码：</label>
			<form:input path="keySn" htmlEscape="false" maxlength="50"
				class="input-medium" />
			<label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" />
				
		</div>
		<div style="margin-top: 8px">
			&nbsp;&nbsp;<label>到期日期：&nbsp;</label> &nbsp;&nbsp;<input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" /> 
			&nbsp;&nbsp;<label>制证日期：</label> <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${makeCertStartTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="makeCertStartTime" id="makeCertStartTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${makeCertEndTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="makeCertEndTime" /> 
				<br />
				<br />
				<div style="padding-left:100px;">
				<a target="_blank" href="${ctx}/template/xls/batchImportDealInfo.xlsx" class="btn btn-primary">批量新增模板下载</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="manyAdd" data-toggle="modal" href="#declareDiv" class="btn btn-primary">批量新增导入</a>&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="manyUpdate" data-toggle="modal" href="javaScript:updateCertOK();" class="btn btn-primary">批量更新证书</a>
			 
			
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a data-toggle="modal" href="${ctx}/work/workDealInfo/deleteList" class="btn btn-primary">删除批量新增信息</a>
				<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds }"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmit"
				class="btn btn-primary" type="submit" onclick="replaceChecks()" value="查询" />
				</div>
		</div>
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll" name="oneDealCheck" value="${page.pageNo}" 
				<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
				</c:forEach>
				onchange="checkAll(this)"
				/> </th>
				<th>业务编号</th>
				<th>应用名称</th>
				<th>单位名称</th>
<!-- 				<th>证书持有人</th> -->
				<th>经办人</th>
				<th>产品名称</th>
				<th>证书类型</th>
				<th>KEY编码</th>
				<th>制证日期</th>
				<th>有效期</th>
				<th>到期日期</th>
				<th>业务状态</th>
				<th>操作</th>
				<th>印章操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td>
					
					<c:if test="${workDealInfo.dealInfoStatus==7 && (workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays)<61 }">
						<input type="checkbox" name="oneDealCheck" value = "${workDealInfo.id}" 
						<c:forEach items="${ids }" var="id">
							<c:if test="${id==workDealInfo.id }"> checked="checked"</c:if>
						</c:forEach>
						onchange="changeCheck(this)"
						 /> 
					 </c:if>
					 
					 </td>
					<td>${workDealInfo.svn}</td>
					<td>${workDealInfo.configAppName}</td>
					<td><a
						href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.companyName}</a></td>

					<td>${workDealInfo.certApplyInfoName }</td>
					<td>${proType[workDealInfo.productName]}</td>
					<td>
						<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
						<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
						<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
						<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
					</td>
					<td>${workDealInfo.keySn }</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${workDealInfo.signDate}"/></td>
					<td><c:if
							test="${not empty workDealInfo.notafter && not empty workDealInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
						</c:if></td>
					<td><fmt:formatDate
							value="${workDealInfo.notafter }"
							pattern="yyyy-MM-dd" /> </td>
				
					<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
					<td><c:if
							test="${workDealInfo.dealInfoStatus==3||workDealInfo.dealInfoStatus==7 }">
							<a href="javascript:maintain(${workDealInfo.id})">维护</a>
							<c:if test="${workDealInfo.dealInfoType!=10 }">
								<a href="javascript:hisgoMoney(${workDealInfo.id})">退费</a>
							</c:if>
						</c:if> 
						
				
						
						
						<c:if test="${workDealInfo.dealInfoStatus==5 }">
						<shiro:hasPermission name="work:workDealInfo:jiaofei">
							<a href="${ctx}/work/workDealInfo/pay?id=${workDealInfo.id}">缴费</a>
							</shiro:hasPermission>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==8 }">
							<a href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">继续编辑</a>&nbsp;&nbsp;
					</c:if>
					
					<c:if test="${workDealInfo.dealInfoStatus==15 }">
							<a href="javascript:returnDealInfo(${workDealInfo.id})">编辑基本信息</a>&nbsp;&nbsp;
					</c:if> 					
					<c:if test="${workDealInfo.dealInfoStatus==9 }">
							<shiro:hasPermission name="work:workDealInfo:makezheng"><a
								href="${ctx}/work/workDealInfoOperation/make?id=${workDealInfo.id}">制证</a></shiro:hasPermission>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==4 }">
							<a
								href="${ctx}/work/workDealInfoOperation/errorForm?id=${workDealInfo.id}">再次编辑</a>&nbsp;&nbsp;
					</c:if> <c:if
							test="${workDealInfo.dealInfoStatus==8||workDealInfo.dealInfoStatus==5 }">
							<a href="${ctx}/work/workDealInfo/delete?id=${workDealInfo.id}"
								onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>&nbsp;&nbsp;
					</c:if>
					<c:if test="${workDealInfo.dealInfoStatus == 13}">
						<shiro:hasPermission name="work:workDealInfo:makezheng">
						<a href="${ctx}/work/workDealInfoAudit/makeDealInfo?id=${workDealInfo.id}">制证</a>
						</shiro:hasPermission>
					</c:if>
					</td>
					
					<td>
						<c:if test="${workDealInfo.dealInfoStatus==7 }">

 								<a href="javascript:checkAdd(${workDealInfo.id})">印章授权</a>
						</c:if>
					</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>
	
	<div id="declareDiv" style="width: 800px;margin-left: -450px;" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/work/workDealInfo/addAttach"
				enctype="multipart/form-data">
				<div class="row-fluid">
			<input type="hidden" name="appId" id="appId" />
			<input type="hidden" name="productT" id="productT"/>
			<input type="hidden" name="lableT" id="lableT"/>
			<input type="hidden" name="dealInfoTypeT" value="0"/>
			
			<input type="hidden" name="agentIdT" id="agentIdT"/>
			<input type="hidden" name="agentDetailIdT" id="agentDetailIdT"/>
			<input type="hidden" name="yearT" id="yearT"/>
			
			<table class="table table-striped table-bordered table-condensed">
				<tbody>
					<tr>
						<th colspan="4" style="font-size: 20px;">基本信息</th>
					</tr>
					<tr>
						<th style="width: 80px;"><span class="prompt"
							style="color: red; display: none;">*</span>应用名称：</th>
						<td style="width: 260px;">		
						<input type="text" name="configApp" id="app" />			
						</td>
						<th>选择产品：</th>
						<td id="productTdId">
									<select name="product"  id="product">
									<c:forEach items="${proList}" var="product">
									<option value="${product.id}" <c:if test="${product.id==workDealInfo.configProduct.id }">selected="selected"</c:if> >${product.name}</option>
									</c:forEach>
									</select>	
						</td>
					</tr>
					
					<tr>
						<th style="width: 80px;">计费策略类型：</th>
						<td style="width: 260px;">
						<input type="hidden" id="boundId">
						<select id="agentId" name="agentId">
								<option value="0">请选择</option>
						</select></td>
						<th>计费策略模版：</th>
						<td id="productTdId">
							<select
								onchange="setYearByBoundId()" id="agentDetailId"
								name="agentDetailId">
									<option value="0">请选择</option>
							</select>
						</td>
					</tr>
					
					<tr>
						
						<th style="width: 80px;">申请年数：</th>
						<td style="width: 260px;">
							<input type="radio" name="year" value="1" id="year1"
								<c:if test="${empty workDealInfo.year}">checked</c:if>
								<c:if test="${workDealInfo.year==1}">checked</c:if>><span
								id="word1">1年</span> <input type="radio" name="year" value="2"
								id="year2" <c:if test="${workDealInfo.year==2}">checked</c:if>><span
								id="word2">2年 </span>
								<input type="radio" name="year" value="3"
								id="year3" <c:if test="${workDealInfo.year==3}">checked</c:if>><span
								id="word3">3年</span>
								
								<input type="radio" name="year" value="4"
								id="year4" <c:if test="${workDealInfo.year==4}">checked</c:if>><span
								id="word4">4年</span><input type="radio" name="year" value="5"
								id="year5" <c:if test="${workDealInfo.year==5}">checked</c:if>><span
								id="word5">5年</span>
						</td>
						
						<th>业务类型：</th>
						<td id="productTdId">
							<input type="checkbox" checked="checked"
								disabled="disabled">新增证书 
						</td>
					</tr>
					
					<tr >
						
						<th>导入文件：</th>
						<td id="productTdId">
							<input id="fileName" name="fileName" type="file" multiple="multiple" />
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
	
	
	
	
	
	
	
	<div id="declareDivUpdate" style="width: 800px;margin-left: -450px;" class="modal hide">
		<div class="modal-header">
			<h3>批量更新</h3>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
			<table class="table table-striped table-bordered table-condensed">
				<tbody>
					<tr>
						<th style="width: 80px;">计费策略类型：</th>
						<td style="width: 260px;">
						<select id="agentIdByUpdate" name="agentIdByUpdate">
								<option value="0">请选择</option>
						</select></td>
						<th>计费策略模版：</th>
						<td id="productTdId">
							<select
								onchange="setYearByBoundIdByUpdate()" id="agentDetailIdByUpdate"
								name="agentDetailIdByUpdate">
									<option value="0">请选择</option>
							</select>
							
							<input type="hidden" id="appIdd">
							<input type="hidden" id="updateSize">
							<input type="hidden" id="productNamee">
							<input type="hidden" id="labell">
							<input type="hidden" id="bounddId">
							
						</td>
					</tr>
					<tr>
						<th style="width: 80px;">申请年数：</th>
						<td style="width: 260px;">
							<input type="radio" name="yearU" value="1" id="year1U"
								>
								<span
								id="word1U">1年</span> <input type="radio" name="yearU" value="2"
								id="year2U"><span
								id="word2U">2年 </span>
								
								<input type="radio" name="yearU" value="3"
								id="year3U">
								<span
								id="word3U">3年</span>
								
								<input type="radio" name="yearU" value="4"
								id="year4U" >
								<span
								id="word4U">4年</span>
								
								
								<input type="radio" name="yearU" value="5"
								id="year5U"><span
								id="word5U">5年</span>
						</td>
						
						<th>缴费方式：</th>
						<td >
							<input type="radio" name="methodPay" value="0"
								id="pay1">
							<span id="methodPay1">pos</span>
							<input type="radio" name="methodPay" value="1"
								id="pay2">
							<span id="methodPay2">现金缴费</span>
							<input type="radio" name="methodPay" value="2"
								id="pay3">
							<span id="methodPay3">银行转账</span>
						</td>
						
					</tr>
				</tbody>
			</table>
		</div>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hideUpdateDiv()" class="btn">取消</a> 
				<a
				href="javascript:void(0)" data-dismiss="modal" id="buttonByUpdate"
				onclick="updateSubmit()" class="btn btn-primary">更新</a>
		</div>
	</div>
	
	
	
	
	
	
	
	
	
</body>
</html>
