<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
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
	
	function returnDealInfo(obj){
		var id = obj;
		var reissueType = "";
		var dealType = "3";
		window.location.href = "${ctx}/work/workDealInfo/typeFormReturnUpdate?id="+id+"&reissueType="+reissueType+"&dealType="+dealType;
	}
	
	
	
	function addAttach() {
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
					//top.$.jBox.tip("上传失败"+data.msg);
					//$("#searchForm").submit();
				}else{
					top.$.jBox.tip("上传失败!");
					var info = "失败信息:<br>"+data.msg;
					top.$.jBox.info(info);
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
				if (check.is(":checked") == false) {
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
			<label>&nbsp;&nbsp;Key ID ：&nbsp;&nbsp;</label>
			<form:input path="keySn" htmlEscape="false" maxlength="50"
				class="input-medium" />
			&nbsp;&nbsp; <label>单位名称：</label>
			<form:input path="workCompany.companyName" htmlEscape="false"
				maxlength="50" class="input-medium" />
			<label>证书持有人：</label>
			<form:input path="workUser.contactName" htmlEscape="false"
				maxlength="50" class="input-medium" />
			<br />
		</div>
		<div style="margin-top: 8px">
			<label>&nbsp;&nbsp;移动电话：</label>
			<form:input path="workUser.contactPhone" htmlEscape="false"
				maxlength="50" class="input-medium" />
			<label>证书到期时间：</label> <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="startTime" id="startTime"/> 至 <input class="input-medium Wdate" type="text"
				required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" maxlength="20" readonly="readonly"
				name="endTime" /> &nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit"
				class="btn btn-primary" type="submit" value="查询" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a target="_blank" href="${ctx}/enroll/downloadTemplate?fileName=batchImportDealInfo.xlsx&_=new Date().getTime()" class="btn btn-primary">批量新增模板下载</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="manyAdd" data-toggle="modal" href="#declareDiv" class="btn btn-primary">批量新增导入</a>
				
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a id="manyUpdate" data-toggle="modal" class="btn btn-primary">批量更新证书</a>
				<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds }"/>
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
				<th>单位名称</th>
				<th>证书持有人名称</th>
				<th>经办人名称</th>
				
				<th>企业类型</th>
				<th>所属应用</th>
				<th>别名</th>
				<th>产品名称</th>
				<th>业务类型</th>

				<th>Key编码</th>
				<th>证书到期时间</th>
				<th>有效期</th>

				<th>业务状态</th>
				<th>操作</th>
		<tbody>
			<c:forEach items="${page.list}" var="workDealInfo">
				<tr>
					<td><input type="checkbox" name="oneDealCheck" value = "${workDealInfo.id}" 
					<c:forEach items="${ids }" var="id">
						<c:if test="${id==workDealInfo.id }"> checked="checked"</c:if>
					</c:forEach>
					onchange="changeCheck(this)"
					 /> </td>
					<td>${workDealInfo.svn}</td>
					<td><a
						href="${ctx}/work/workDealInfoFiling/formF?id=${workDealInfo.id}">${workDealInfo.workCompany.companyName}</a></td>
					<td>
								${workDealInfo.workUser.contactName}
					</td>
					<td>
								${workDealInfo.workCertInfo.workCertApplyInfo.name}
					</td>
					
					
					<td><c:if test="${workDealInfo.workCompany.companyType==1}">企业</c:if>
						<c:if test="${workDealInfo.workCompany.companyType==2}">事业单位</c:if>
						<c:if test="${workDealInfo.workCompany.companyType==3}">政府机构</c:if>
						<c:if test="${workDealInfo.workCompany.companyType==4}">社会团体</c:if>
						<c:if test="${workDealInfo.workCompany.companyType==5}">其他</c:if>
					</td>
					<td>${workDealInfo.configApp.appName}</td>
					<td>${workDealInfo.configApp.alias}</td>
					<td>${proType[workDealInfo.configProduct.productName]}</td>
					<td>
						<c:if test="${workDealInfo.dealInfoType!=null}">${wdiType[workDealInfo.dealInfoType]}</c:if>
						<c:if test="${workDealInfo.dealInfoType1!=null}">${wdiType[workDealInfo.dealInfoType1]}</c:if>
						<c:if test="${workDealInfo.dealInfoType2!=null}">${wdiType[workDealInfo.dealInfoType2]}</c:if>
						<c:if test="${workDealInfo.dealInfoType3!=null}">${wdiType[workDealInfo.dealInfoType3]}</c:if>
					</td>
					<td>${workDealInfo.keySn }</td>
					<td><fmt:formatDate
							value="${workDealInfo.notafter }"
							pattern="yyyy-MM-dd" /> </td>
					<td><c:if
							test="${not empty workDealInfo.workCertInfo.notafter && not empty workDealInfo.workCertInfo.notbefore}">
							${empty workDealInfo.addCertDays?workDealInfo.year*365+workDealInfo.lastDays:workDealInfo.year*365+workDealInfo.lastDays+workDealInfo.addCertDays}（天）
						</c:if></td>
					<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
					<td><c:if
							test="${workDealInfo.dealInfoStatus==3||workDealInfo.dealInfoStatus==7 }">
							<a href="javascript:maintain(${workDealInfo.id})">维护</a>
							<c:if test="${workDealInfo.dealInfoType!=10 }">
								<a href="javascript:hisgoMoney(${workDealInfo.id})">退费</a>
							</c:if>
						</c:if> <c:if test="${workDealInfo.dealInfoStatus==5 }">
							<a href="${ctx}/work/workDealInfo/pay?id=${workDealInfo.id}">缴费</a>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==8 }">
							<a href="${ctx}/work/workDealInfo/form?id=${workDealInfo.id}">继续编辑</a>&nbsp;&nbsp;
					</c:if>
					
					<c:if test="${workDealInfo.dealInfoStatus==15 }">
							<a href="javascript:returnDealInfo(${workDealInfo.id})">编辑基本信息</a>&nbsp;&nbsp;
					</c:if> 					
					<c:if test="${workDealInfo.dealInfoStatus==9 }">
							<a
								href="${ctx}/work/workDealInfoOperation/make?id=${workDealInfo.id}">制证</a>&nbsp;&nbsp;
					</c:if> <c:if test="${workDealInfo.dealInfoStatus==4 }">
							<a
								href="${ctx}/work/workDealInfoOperation/errorForm?id=${workDealInfo.id}">再次编辑</a>&nbsp;&nbsp;
					</c:if> <c:if
							test="${workDealInfo.dealInfoStatus==8||workDealInfo.dealInfoStatus==5 }">
							<a href="${ctx}/work/workDealInfo/delete?id=${workDealInfo.id}"
								onclick="return confirmx('确认要删除该信息吗？', this.href)">删除</a>&nbsp;&nbsp;
					</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<span id="msg" style="color: red;"></span>
	
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/work/workDealInfo/addAttach"
				enctype="multipart/form-data">
				<input id="fileName" name="fileName" type="file" multiple="multiple" />
			</form>
		</div>
		<div class="modal-footer">
			<a href="javascript:void(0)" data-dismiss="modal" 
				onclick="hidenUpload()" class="btn">取消</a> <a
				href="javascript:void(0)" data-dismiss="modal" 
				onclick="addAttach()" class="btn btn-primary">导入</a>
		</div>
	</div>
</body>
</html>
