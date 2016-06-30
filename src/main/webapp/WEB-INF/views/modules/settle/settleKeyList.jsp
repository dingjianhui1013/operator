<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>供应商返修Key记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function addGene() {
			var supplierId = $("#supplierId").prop('value');
			var url = "${ctx}/key/keyGeneralInfo/addGeneralInfo?supplierId=";
			$.getJSON(url + supplierId+"&_="+new Date().getTime(), function(data) {
				var html = "";
				//console.log(data);
				html += "<option value=\""+""+"\">请选择</ooption>";
				$.each(data, function(idx, ele) {
					//console.log(idx);
					//console.log(ele);
					html += "<option value=\""+ele.id+"\">" + ele.name
							+ "</ooption>"
				});
				$("#keyId").html(html);
			});
		}
		
		function saveTime(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if (startTime==null||startTime=='') {
				top.$.jBox.tip("请选择到货开始时间！");
				$("#modal-container-432382").hide();
				return ;
			}
			if (endTime==null||endTime=='') {
				top.$.jBox.tip("请选择到货结束时间！");
				$("#modal-container-432382").hide();
				return;
			}
			var changeTime = $("#changeTime").val();
			if (changeTime==null||changeTime=='') {
				top.$.jBox.tip("请选择要修改的时间！");
				return;
			}
			
		var url = "${ctx}/settle/settleKey/updateSome?updateDate="+changeTime+"&startTime="+startTime+"&endTime="+endTime;
		window.location.href=url;
		
		}
		function saveBackTime(){
			var startTime = $("#startBackTime").val();
			var endTime = $("#endBackTime").val();
			if (startTime==null||startTime=='') {
				top.$.jBox.tip("请选择返修开始时间！");
				$("#modal-container-432382").hide();
				return ;
			}
			if (endTime==null||endTime=='') {
				top.$.jBox.tip("请选择返修结束时间！");
				$("#modal-container-432382").hide();
				return;
			}
			var changeTime = $("#changeBackTime").val();
			if (changeTime==null||changeTime=='') {
				top.$.jBox.tip("请选择要修改的时间！");
				return;
			}
			
		var url = "${ctx}/settle/settleKey/updateBackSome?updateDate="+changeTime+"&startTime="+startTime+"&endTime="+endTime;
		window.location.href=url;
		
		}
		
		function savecheckedTime(){
			var checkIds = $("#checkIds").val();
			if(checkIds==null||checkIds==""){
				top.$.jBox.tip("请选择您要修改的数据！");
				$("#modal-container").hide();
				return;
			}
			var changeTime = $("#changeTwoTime").val();
			if (changeTime==null||changeTime=='') {
				top.$.jBox.tip("请选择要修改的时间！");
				return;
			}
			var url = "${ctx}/settle/settleKey/updateCheckSome?changeTime="+changeTime+"&checkIds="+checkIds;
			window.location.href=url;
			
		}
		function savecheckedBackTime(){
			var checkIds = $("#checkIds").val();
			if(checkIds==null||checkIds==""){
				top.$.jBox.tip("请选择您要修改的数据！");
				$("#modal-container").hide();
				return;
			}
			var changeTime = $("#changeBackTime1").val();
			if (changeTime==null||changeTime=='') {
				top.$.jBox.tip("请选择要修改的时间！");
				return;
			}
			var url = "${ctx}/settle/settleKey/updateCheckBackSome?changeTime="+changeTime+"&checkIds="+checkIds;
			window.location.href=url;
			
		}
		
		
		function changeCheck(obj){
			var checkIds = $("#checkIds").val();
			var xz = $("#contentTable").find("[name='xz']");
			if(checkIds.indexOf($(obj).val())>-1){
				checkIds = checkIds.replace($(obj).val(), "");
			}
			for (var a = 0; a <xz.length; a++) {
				var check = $($("#contentTable").find("[name='xz']")[a]);
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
		
		function onChange(){
			var supplierId = $("#supplierId").val();
			var keyId = $("#keyId").val();
			var keySn = $("#keySn").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			window.location.href="${ctx }/settle/settleKey/exportSettle?supplierId="+supplierId+"&keyId="+keyId+"&keySn="+keySn+"&startTime="+startTime+"&endTime="+endTime;				
		}
		
		function addAttach() {
			if($("#fileName").val() == ""){
				top.$.jBox.tip("请选择一个xls文件!");
	        	return false;
	        }
	        if($("#fileName").val().indexOf('.xls')<0) {
	        	top.$.jBox.tip("请选择一个xls文件!");
	            return false;
	        }
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
		            		   }, 3000); // how long do you want the delay to be? 
		            
					}else if(data.status=='-1'){
						top.$.jBox.tip("上传失败"+data.msg);
						//$("#searchForm").submit();
					}else{
						top.$.jBox.tip("上传失败："+data.errorMsg);
						//$("#searchForm").submit();
					}
				}
			};
			$('#materialImport').ajaxSubmit(options);
		}
		
		function checkAll(obj){
			
			var check = $($("#contentTable").find("#checkAll"));
			var checkIds = $("#checkIds").val();
			var xz = $("#contentTable").find("[name='xz']");
			if (check.is(":checked") == true) {
				$('input:checkbox').each(function() {
			        $(this).attr('checked', true);
				});
				for (var a = 0; a <xz.length; a++) {
					var check = $($("#contentTable").find("[name='xz']")[a]);
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
					var check = $($("#contentTable").find("[name='xz']")[a]);
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
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/settle/settleKey/">供应商返修Key记录列表</a></li>
		<li><a href="${ctx}/settle/settleKey/form">供应商返修Key记录添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="settleKey" action="${ctx}/settle/settleKey/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<div>
			<label>KEY 厂商：</label> 
			<select name="supplierId"
				id="supplierId" onchange="addGene()">
				<option value="">请选择</option>
				<c:forEach items="${suppliers}" var="supplier">
					<option value="${supplier.id}"
						<c:if test="${supplier.id==configSupplierId}">
						selected="selected"
					</c:if>>${supplier.supplierName}</option>
				</c:forEach>
			</select> <label>KEY类型 ：</label> 
			<select name="keyId" id="keyId">
				<option value="">请选择</option>
				<c:forEach items="${keys}" var="key">
					<option value="${key.id}"
						<c:if test="${key.id==keyId}">
					selected="selected"
					</c:if>>${key.name}</option>
				</c:forEach>
			</select>
			<label>KEY编码：</label>
			<input type="text"  name="keySn"  id="keySn" value="${keySn }"/>
		</div>
		<br />
		<label>到货时间：</label> 
		<input id="startTime" name="startTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				value="<fmt:formatDate value="${startTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endTime" name="endTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>" />
		<label>返修时间：</label>
		<input id="startBackTime" name="startBackTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				value="<fmt:formatDate value="${startBackTime}" pattern="yyyy-MM-dd"/>" />
			&nbsp;-&nbsp;<input id="endBackTime" name="endBackTime" type="text"
				readonly="readonly" maxlength="20" class="Wdate required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'startTime\')}'});"
				value="<fmt:formatDate value="${endBackTime}" pattern="yyyy-MM-dd"/>" />
				&nbsp; &nbsp; &nbsp; &nbsp;
				<br/><br/>
				&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
		<a href="#modal-container-432382" class="btn btn-primary" data-toggle="modal" >批量修改到货时间</a>
		<a href="#modal-container-432383" class="btn btn-primary" data-toggle="modal" >批量修改返修时间</a>
		<a target="_blank" href="${ctx}/template/xls/exportSettle.xlsx" class="btn btn-primary">模板下载</a>
		<a id="btnImport" data-toggle="modal" href="#declareDiv" class="btn btn-primary">批量导入</a>
		<a href="javascript:onChange()"   class="btn btn-primary">导出</a>
		<input type="hidden"  name="checkIds"  id="checkIds"  value="${checkIds }"/>
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="10px">
				<input type="checkbox" name="xz"  id="checkAll" value="${page.pageNo}" 
					<c:forEach items="${ids }" var="id">
					<c:if test="${id==page.pageNo}"> checked="checked"</c:if>
			</c:forEach>
			onchange="checkAll(this)"/>
				</th>
				<th>序号</th>
				<th>到货时间</th>
				<th>返修时间</th>
				<th>厂商名称</th>
				<th>KEY类型名称</th>
				<th>KEY编码</th>
				<th>操作人员</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="settleKey" varStatus="status">
			<tr>
			<td><input type="checkbox" name="xz"  value="${settleKey.id }" 
			<c:forEach items="${ids }" var="id">
					<c:if test="${id==settleKey.id }"> checked="checked"</c:if>
			</c:forEach>
			onchange="changeCheck(this)"/>
			
			
			</td>
				<td>${status.index+1 }</td>
				<td>
				<fmt:formatDate value="${settleKey.comeDate }" pattern="yyyy-MM-dd"/>
				</td>
				<td>
				<fmt:formatDate value="${settleKey.backDate }" pattern="yyyy-MM-dd"/>
				</td>
				<td>${settleKey.configSupplier.supplierName }</td>
				<td>${settleKey.keyGeneralInfo.name }</td>
				<td>${settleKey.keySn }</td>
				<td>${settleKey.sysUser.name }</td>
				<shiro:hasPermission name="settle:settleKey:edit"><td>
    				<a href="${ctx}/settle/settleKey/form?id=${settleKey.id}">修改</a>
					<a href="${ctx}/settle/settleKey/delete?id=${settleKey.id}" onclick="return confirmx('确认要删除该供应商返修Key记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
			<tr>	
					<td></td>
					<td colspan="7">总计：${page.count }个</td>
			</tr>
			<tr>	
					<td colspan="8"><a href="#modal-container" class="btn btn-primary" data-toggle="modal" >修改选中数据到货时间</a>
									<a href="#modal-container1" class="btn btn-primary" data-toggle="modal" >修改选中数据返修时间</a>
					</td>
			</tr>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<div id="modal-container-432382" class="modal hide fade" style="width:400px;height:200px;left:50%;top:100px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" >
			 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				修改到货日期
			</h3>
		</div>
		<div class="modal-body"  style="height: 210px">
		<div class="control-group">
			<label class="control-label">修改时间:</label>
			<input id="changeTime" name="changeTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				/>
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="saveTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
			
		</div>
		</div>
		<div id="modal-container-432383" class="modal hide fade" style="width:400px;height:200px;left:50%;top:100px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" >
			 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				修改返修日期
			</h3>
		</div>
		<div class="modal-body"  style="height: 210px">
		<div class="control-group">
			<label class="control-label">修改时间:</label>
			<input id="changeBackTime" name="changeBackTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				/>
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="saveBackTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
			
		</div>
		</div>
	
	<div id="modal-container" class="modal hide fade" style="width:400px;height:200px;left:50%;top:100px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" >
			 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				修改到货日期
			</h3>
		</div>
		<div class="modal-body"  style="height: 210px">
		<div class="control-group">
			<label class="control-label">修改时间:</label>
			<input id="changeTwoTime" name="changeTwoTime"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				/>
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="savecheckedTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
			
		</div>
		</div>
		
	<div id="modal-container1" class="modal hide fade" style="width:400px;height:200px;left:50%;top:100px" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" >
			 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				修改返修时间
			</h3>
		</div>
		<div class="modal-body"  style="height: 210px">
		<div class="control-group">
			<label class="control-label">修改时间:</label>
			<input id="changeBackTime1" name="changeBackTime1"
				type="text" readonly="readonly" maxlength="20"
				class="required Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
				/>
		</div>
		<div class="control-group" align="center">
				 <button id="qrsq" class="btn btn-primary" onclick="savecheckedBackTime()">确认</button>&nbsp;&nbsp;&nbsp;
				 <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> 
			 </div>
			
		</div>
		</div>
		
	<div id="declareDiv" class="modal hide fade">
		<div class="modal-header">
			<h3>批量导入</h3>
		</div>
		<div class="modal-body">
			<form id="materialImport"
				action="${ctx}/settle/settleKey/addAttach"
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
