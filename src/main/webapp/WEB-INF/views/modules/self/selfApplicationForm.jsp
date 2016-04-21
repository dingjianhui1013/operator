<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申请表信息</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.round_box_close{display:none;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.small_pic a').fancyZoom({scaleImg: true, closeOnClick: true});
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			productLabel();
		});
		
		var str = '${tempStyle01}';
		var str01 = '${tempStyle01}';
		var type =2;
		
		$(document).ready(function(){
			if(str!=''&&str!=null){
				setTimeout(function(){
					$("#agentId option").each(function(){
				        if($(this).val() == str){
					        $(this).attr("selected",true);
					        setStyleList(type);
					         $("#agentDetailId option").each(function(){
						        if($(this).val() == str01){
						        	$(this).attr("selected",true);
						        }
							}); 
				        }  
				    });     
				},500)
			}
		});
		
		//根据商品获取引用标识
		function productLabel() {
			selected = true;
			var productName =${selfApplication.productName} ;
			var appId = ${appId};
			var url = "${ctx}/work/workDealInfo/type?name=" + productName + "&appId="
					+ appId+"&_="+new Date().getTime();
			$.getJSON(url, function(da) {
				if (da.type1) {
					showAgent(1);
					type = 1;
				}else if (da.type0) {
					showAgent(0);
					type = 0;
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
			var productName =${selfApplication.productName} ;
			var appId = ${appId};
			var url = "${ctx}/work/workDealInfo/showAgentProduct?lable="+lable+"&productName="+productName+"&app="+appId+"&infoType=0&_="+new Date().getTime();
			$.getJSON(url,function(data){
				if(data.tempStyle!=-1){
					var map = data.typeMap;
					var agentHtml="<option value='0'>请选择计费策略类型</option>";
					$("#agentId").attr("onchange","setStyleList("+lable+")");
					$.each(map, function(i, item){
						agentHtml+="<option value='"+item.id+"'>" + item.name + "</option>";
						
					});
					$("#agentId").html(agentHtml);
					var styleList = data.boundStyleList;
					var styleHtml="<option value='0'>请选择计费策略模版</option>";
					$("#agentDetailId").html(styleHtml);
				}else{
					var agentHtml="<option value='0'>请选择计费策略类型</option>";
					$("#agentId").html(agentHtml);
					var styleHtml="<option value='0'>请选择计费策略模版</option>";
					$("#agentDetailId").html(styleHtml);
				}
			});
		}
		
		/*
		* 给计费策略模版配置赋值
		*/
		function setStyleList(obj){
			var lable = obj;
			var agentId = $("#agentId").val();
			var productName =${selfApplication.productName} ;
			var appId = ${appId};
			if (agentId!=0) {
				var url = "${ctx}/work/workDealInfo/setStyleList?lable="+lable+"&productName="+productName+"&app="+appId+"&infoType=0&style="+agentId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					var styleList = data.array;
					var styleHtml="";
					$("#agentDetailId").attr("onchange","showYear()");
					$.each(styleList,function(i,item){
						if(i==0){
							$("#boundId").val(item.id);
						}
						styleHtml +="<option value='"+item.id+"'>" + item.name + "</option>";
					});
					
					$("#agentDetailId").html(styleHtml);
				});
			}else{
				top.$.jBox.tip("请选择计费策略模版");
				var styleHtml="<option value='0'>请选择计费策略模版</option>";
				$("#agentDetailId").html(styleHtml);
			}
		}
		
		/* 
		* 功能:根据产品带回年限
		* 传参：lable+name
		* 返回值：年限1，2，4，5是否为true
		*/ 
		function showYear(){
			var boundId = $("#agentDetailId").val();
			var url = "${ctx}/work/workDealInfo/showYearNew?boundId="+boundId+"&infoType=0&_="+new Date().getTime();
			$.getJSON(url, function(data) {
				var a = new Array(5);
				if (data.year1) {
					a[0] = '1';
				} else{
					a[0] = '0';
				}
				if (data.year2) {
					a[1] = '2';
				} else {
					a[1] = '0';
				}
				if (data.year3) {
					a[2] = '3';
				} else{
					a[2] = '0';
				} if (data.year4) {
					a[3] = '4';
				} else{
					a[3] = '0';
				} if (data.year5) {
					a[4] = '5';
				}else{
					a[4] = '0';
				}
				var flag = false;
				for (var i = 0; i < a.length; i++) {
					if('${selfApplication.applicationPeriod}'==a[i]){
						flag = true;
						break;
					}
				}
				if(!flag){
					top.$.jBox.tip("此计费策略模版没有对应的年限费用，请重新选择模板");
					productLabel();
				}
				var url="${ctx}/work/workDealInfo/checkSurplusNum?boundId="+boundId+"&_="+new Date().getTime();
				$.getJSON(url,function(data){
					if(data.surplusNum==0&&$("#agentId").val()!=1){
						top.$.jBox.tip("此计费策略模版剩余数量为零，不能进行业务办理！");
					}
				});
			});
		}
		
		function agreeApply(){
			var agentDetailId =  $("#agentDetailId").val();
			var agentId = $("#agentId").val();
			if(agentId == 0){
				top.$.jBox.tip("请选择计费策略类型");
				return false;
			}else if (agentDetailId ==0){
				top.$.jBox.tip("请选择计费策略模版");
				return false;
			}else{
				$("#btnSubmit").attr("disabled","disabled");
				$("#inputForm").submit();
				return true;
			}
		}
		
		function denyApply(){
			$('#myModal').modal('hide');
			var denyText =  $("#denyText").val();
			if(denyText == null || denyText == ""){
				top.$.jBox.tip("请输入请输入拒绝的原因");
			}else{
				window.location.href = "${ctx}/self/selfApplication/denyApply?id=${selfApplication.id}&denyText="+denyText;
			}
			
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/self/selfApplication/">申请表信息</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="selfApplication" action="${ctx}/self/selfApplication/agreeApply" method="post" class="form-horizontal">
		<form:hidden path="id" value = "${selfApplication.id}"/>
		<tags:message content="${message}"/>
		<div class="form-horizontal">
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="2">基本信息</th>
				</tr>
			</thead>
			<tr>
				<td >项目名称：</td><td>${selfApplication.appName} </td>
			</tr>
			<tr>
				<td >产品类型:
				</td>
				<td>
					<c:if test="${selfApplication.productName=='1'}">
						企业证书
					</c:if>
					<c:if test="${selfApplication.productName=='2'}">
						个人证书(企业)
					</c:if>
					<c:if test="${selfApplication.productName=='3'}">
						机构证书
					</c:if>
					<c:if test="${selfApplication.productName=='4'}">
						可信移动设备
					</c:if>
					<c:if test="${selfApplication.productName=='6'}">
						个人证书(机构)
					</c:if>
				</td>
			</tr>
			<c:if test = "${applyFlag1 eq true }">
				<tr>
					<td >应用标识：</td>
					<td>
						<c:if test = "${productLabel=='0'}">
							专用证书
						</c:if>
						<c:if test = "${productLabel=='1'}">
							'易证通'通用证书" 
						</c:if>
					</td>
				</tr>
				<tr>
					<td>地税管理部门:</td>
					<td>
						${selfApplication.managementProvince }&nbsp;市（州）&nbsp;&nbsp;
						${selfApplication.managementDistrict }&nbsp;区（县、分局）&nbsp;&nbsp;
						${selfApplication.managementPlace }&nbsp;科（所）
					</td>
				</tr>
			</c:if>
			<c:if test = "${applyFlag2 eq true }">
				<tr>
					<td>参保区县:</td>
					<td>${selfApplication.securityAddress }</td>
				</tr>
				<tr>
					<td>社保编码:</td>
					<td>${selfApplication.securityNumber }</td>
				</tr>
			</c:if>
			<tr>
				<td >证书业务类型：</td>
				<td>
					<c:if test = "${selfApplication.businessType =='0'}">
						新增
					</c:if>
					<c:if test = "${selfApplication.businessType == '1'}">
						更新
					</c:if>
					<c:if test = "${selfApplication.businessType == '2'}">
						变更
					</c:if>
				</td>
			</tr>
			<tr>
				<td >证书申请年限:</td><td>${selfApplication.applicationPeriod}年</td>
			</tr>
			<thead>
				<tr>
					<th colspan="2">单位信息</th>
				</tr>
			</thead>
			<tr>
				<td >单位名称:</td><td>${selfApplication.companyName}</td>
			</tr>
			<tr>
				<td>单位类型：</td><td>${selfApplication.companyType}</td>
			</tr>
			<tr>
				<c:if test = "${selfApplication.companyType == '企业' }">
					<td>营业执照注册号/社会信用代码号:</td>
					<td>${selfApplication.companyTypeNumber }</td>
				</c:if>
				<c:if test = "${selfApplication.companyType == '事业' }">
					<td>单位法人登记号:</td>
					<td>${selfApplication.companyTypeNumber }</td>
				</c:if>
				<c:if test = "${selfApplication.companyType == '社会团体' }">
					<td>社会团体登记注册号:</td><td>${selfApplication.companyTypeNumber }</td>
				</c:if>
				<c:if test = "${selfApplication.companyType == '其他' }">
					<td>其他:</td><td>${selfApplication.companyTypeNumber }</td>
				</c:if>
			</tr>
			<c:if test = "${selfApplication.companyNumber!=''&&selfApplication.companyNumber!=null}">
				<tr>
					<td>组织机构代码:</td><td>${selfApplication.companyNumber}</td>
				</tr>
			</c:if>
			<tr>
				<td>单位电子证件:</td>
				<td class ="small_pic">
					<a href="#picBigBox" ><img id="imghead" style = "width:100px;height:75px" src="${imgUrl}/${companyImage }" ></a> 
				</td>
			</tr>
			<thead>
				<tr>
					<th colspan="2">经办人信息</th>
				</tr>
			</thead>
			<tr>
				<td >经办人姓名:</td><td>${selfApplication.transactorName}</td>
			</tr>
			<tr>
				<td >${selfApplication.transactorDocumentType }:</td><td>${selfApplication.transactorDocumentNumber}</td>
			</tr>
			<tr>
				<td>电子邮件:</td><td>${selfApplication.transactorEmail}</td>
			</tr>
			<tr>
				<td >联系电话(手机):</td><td>${selfApplication.transactorPhone}</td>
			</tr>
			<tr>
				<td >个人电子证件:</td>
				<td class = "small_pic">
					<a href="#picBigBox1">
						<img id="imghead1" style = "width:100px;height:75px"  src="${imgUrl }/${transactorImage}" >
					</a>
				</td>
			</tr>
			<tr>
				<td>收件方式:</td>
				<td>
					 <c:if test = "${selfApplication.receiverType == '0' }">
                    	自取
                    </c:if>
                    <c:if test = "${selfApplication.receiverType == '1' }">
                    	邮寄
                    </c:if>
				</td>
			</tr>
			<thead>
				<tr>
					<th colspan="2">快递收件信息</th>
				</tr>
			</thead>
			<tr>
				<td >收件人姓名:</td><td>${selfApplication.receiverName}</td>
			</tr>
			<tr>
				<td >联系电话(手机):</td><td>${selfApplication.receiverPhone}</td>
			</tr>
			<tr>
				<td>收件地址:</td><td>${selfApplication.receiverAddress}</td>
			</tr>
			<tr>
				<td>邮政编码:</td><td>${selfApplication.receiverNumber}</td>
			</tr>
			<thead>
				<tr>
					<th colspan="2">计费信息</th>
				</tr>
			</thead>
			<c:if test = "${selfApplication.status == '0'}">
			<tr>
				<td>
					计费策略类型：
				</td>
				<td >
					<select id="agentId" name="agentId">
						<option value="0">请选择计费策略类型</option>
					</select> 
				</td>
			</tr>
			<tr>
				<td >
					计费策略模版：
				</td>
				<td >
					<select  id="agentDetailId" name="agentDetailId">
						<option value="0">请选择计费策略模版</option>
					</select>
				</td>
			</tr>
			</c:if>
			 <c:if test = "${selfApplication.status != '0'}">
			 <tr>
				<td>
					计费策略类型：
				</td>
				<td >
					<c:if test = "${tempStyle == '1'}">
					标准
					</c:if>
					<c:if test = "${tempStyle == '2'}">
					政府统一采购
					</c:if>
					<c:if test = "${tempStyle == '3'}">
					合同采购
					</c:if>
				</td>
			</tr>
			<tr>
				<td >
					计费策略模版：
				</td>
				<td >
				${tempName}
				</td>
			</tr>
			 </c:if>
		</table>
		</div>
		<div class="form-actions">
		<c:if test = "${selfApplication.status == '0'}">
         	<input id="btnSubmit" class="btn btn-primary" type="button" onclick = "agreeApply()" value = "同意"> &nbsp;
			<input id="denySubmit" class="btn btn-primary" data-toggle="modal" data-target="#myModal" type = "button" value = "拒绝" />
         </c:if>
         <c:if test = "${selfApplication.status != '0'}">
         	<a class="btn btn-primary" href="${ctx}/self/selfApplication/" role="button">返回</a>
         </c:if>
		</div>
	</form:form>
		<div id="picBigBox" style="display:none;">
		<img src="${imgUrl }/${companyImage }"  >
	</div>
	<div id="picBigBox1" style="display:none;">
		<img src="${imgUrl }/${transactorImage}">
	</div>
	<!-- Modal -->
	<div class="modal hide fade" id="myModal" tabindex="-1" role="dialog">
		<div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
			<h3 id="myModalLabel">请输入拒绝的原因</h3>
		</div>
		<div class="modal-body">
		 <textarea style="width:520px; height:100px;resize:none;" id = "denyText"></textarea> 
		</div>
		<div class="modal-footer">
			<a href="JavaScript:void(0);" class="btn" data-dismiss="modal">关闭</a>
			<a href="JavaScript:void(0);" onclick = "denyApply()" class="btn btn-primary">保存</a>
		</div>
	</div>
</body>
</html>
