<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>应用管理</title>
	<meta name="decorator" content="default"/>
<script src="${ctxStatic}/jquery/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript">
		$(document).ready(function() {
			$("#raAccountContract").bind("click",showRaAccountContractForm);
			$("#raAccountExtendInfoContract").bind("click",showRaAccountExtendInfoContractForm);
		});
		
		function showRaAccountContractForm(productId,applyId){
			var url;
			url = "${ctx}/profile/configRaAccount/bindList?configProductId="+productId;
			top.$.jBox.open("iframe:"+url, "RA绑定", 600, 600, {
					buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
						if(v == 'ok'){
							javascript:window.location.href='${ctx}/profile/configProduct?appId='+applyId;
						}
					}
			});
		}
		function showRaAccountExtendInfoContractForm(productId,applyId){
			var url;
				url = "${ctx}/profile/configRaAccountExtendInfo/bindList?configProductId="+productId;
			top.$.jBox.open("iframe:"+url, "证书模版绑定", 600, 600, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						javascript:window.location.href='${ctx}/profile/configProduct?appId='+applyId;
					}
				}
		});
		}
		/* function showRaAccountExtendInfoContractForm(productId){
			var url;
				url = "${ctx}/profile/configRaAccountExtendInfo/bindList?configProductId="+productId;
			top.$.jBox.open("iframe:"+url, "证书模版配置", 600, 600, {
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if(v == 'ok'){
						var table = h.find("iframe")[0].contentWindow.raAccountExtendInfoForm;
						//console.log(table);
						var url = $(table).attr('action');
						var data=$(table).serialize();
						$.ajax({
							  type: 'POST',
							  url: url,
							  data: data,
							  dataType : 'json',
							  success:function(data){
									if(data.status=='1'){
										top.$.jBox.tip("保存证书模版配置成功");
										$("#searchForm").submit();
									}else{
										top.$.jBox.tip("保存证书模版配置失败");
										$("#searchForm").submit();
									}
								}
							});
					}
				}
		});
		} */
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/profile/configProduct?appId=${appId}">产品列表</a></li>
		<shiro:hasPermission name="profile:configProduct:edit"><li><a href="${ctx}/profile/configProduct/form?appId=${appId}">产品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="configProduct" action="${ctx}/profile/configProduct?appId=${appId}" method="post" class="breadcrumb form-search">
		<label>产品名称 ：</label><select name="productName">
			<option <c:if test="${productName==-1}">selected</c:if>  value="-1">所有产品</option>
			<c:forEach items="${proList }" var="pro">
				<option <c:if test="${productName==pro.id }">selected</c:if> value="${pro.id }">${pro.name }</option>
			</c:forEach>
		</select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="5">${title }</th>
			</tr>
		</thead>
		<tbody>
		<tr>
							<th>序号</th>
							<th>产品名称</th>
							<th>产品标识</th>
							<th>备注</th>
							<shiro:hasPermission name="profile:configProduct:edit">
							<th>操作</th>
							</shiro:hasPermission></tr>
	
		<c:forEach items="${page}" var="configProduct" varStatus="status">
			<tr>
				<td>${ status.index + 1}</td>
				<td>${configProduct.productName}</td>
				<td><c:if test="${configProduct.productLabel==1 }">
				专用
				</c:if>
				<c:if test="${configProduct.productLabel==0 }">
				通用
				</c:if></td>
				<td>${configProduct.remarks}</td>
				<td>
			
				<input type="hidden" name="id" id="id" value="${configProduct.id}"/>
				<input type="hidden" name="appId" id="appId" value="${appId}"/>
				<shiro:hasPermission name="profile:configProduct:edit">
				<a href="${ctx}/profile/configProduct/form?id=${configProduct.id}&appId=${configProduct.configApp.id}" class="btn btn-link">编辑</a>
				<c:if test="${configProduct.raAccountId==null}"><a onclick="showRaAccountContractForm(${configProduct.id},${configProduct.configApp.id})"  href="javascript:" class="btn btn-link" style="_padding-top:6px;">绑定RA模板</a></c:if>
				<c:if test="${configProduct.raAccountId!=null}">已绑定RA模板</c:if>
				<c:if test="${configProduct.raAccountId!=null}"><a href="${ctx}/profile/configProduct/unBind?productId=${configProduct.id}")"  href="javascript:" class="btn btn-link" style="_padding-top:6px;">解除绑定RA模板</a></c:if>
				</shiro:hasPermission>
				<shiro:hasPermission name="profile:configRaAccountExtendInfo:edit">
				<c:if test="${configProduct.raAccountId !=null&&configProduct.raAccountExtedId==null}">
				<%-- <input type="hidden" name="raAccountExtedId" id="raAccountExtedId" value="${configProduct.raAccountExtedId}}"/>  --%>
				<a onclick="showRaAccountExtendInfoContractForm(${configProduct.id},${configProduct.configApp.id})" 
				href="javascript:" class="btn btn-link" style="_padding-top:6px;">证书模版绑定</a>
				</c:if>
				<c:if test="${configProduct.raAccountExtedId!=null}">已绑定证书模板</c:if>
				<c:if test="${configProduct.raAccountExtedId!=null}"><a href="${ctx}/profile/configProduct/unCertBind?productId=${configProduct.id}")"  href="javascript:" class="btn btn-link" style="_padding-top:6px;">解除绑定证书模板</a></c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
