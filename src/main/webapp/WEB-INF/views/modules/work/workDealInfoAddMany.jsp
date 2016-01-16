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
						$("#app").focus();
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
</script>

</head>
<body style="overflow: scroll">
	<form id="inputForm" action="${ctx}/work/workDealInfoSed/addAttach" method="post" class="form-horizontal"
				enctype="multipart/form-data">
		<div class="row-fluid">
		<input type="hidden" id="appId" name="appId"/>
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
							<c:forEach items="${proList }"
									var="pro">
									<div id="product${pro.id }">
										<input type="radio" name="product" id="pro${pro.id }"
											onclick="productLabel(${pro.id})"
											value="${pro.id}">${pro.name }&nbsp;&nbsp;&nbsp;
									</div>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th style="width: 80px;">应用标识：</th>
						<td style="width: 260px;">
							<input type="radio" name="lable" id="lable0"
								value="0" disabled="disabled" onclick="showAgent(0)">通用&nbsp;
								&nbsp; <input type="radio" name="lable" id="lable1" value="1"
								disabled="disabled" onclick="showAgent(1)">专用
						</td>
						<th>业务类型：</th>
						<td id="productTdId">
							<input type="checkbox" checked="checked"
								disabled="disabled">新增证书 <input type="hidden"
								name="dealInfoType" value="0">
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
								id="word2">2年 </span><input type="radio" name="year" value="4"
								id="year4" <c:if test="${workDealInfo.year==4}">checked</c:if>><span
								id="word4">4年</span><input type="radio" name="year" value="5"
								id="year5" <c:if test="${workDealInfo.year==5}">checked</c:if>><span
								id="word5">5年</span>
						</td>
						<th>导入文件：</th>
						<td id="productTdId">
							<input id="fileName" name="fileName" type="file" multiple="multiple" />
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</form>
</body>

</html>
