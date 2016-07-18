<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配下级库房</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/key/keyUsbKeyDepot/">库房列表</a></li>
		<li class="active"><a href="${ctx}/key/keyUsbKeyDepot/assign?id=${depot.id}"><shiro:hasPermission name="key:keyUsbKeyDepot:edit">下级库房</shiro:hasPermission><shiro:lacksPermission name="key:keyUsbKeyDepot:edit">分配列表</shiro:lacksPermission></a></li>
	
	</ul>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">库房名称: <b>${depot.depotName}</b></span>
			<span class="span8">所属网点: ${depot.office.name}</span>
			
		</div>
	</div>
	<tags:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignDepotForm" action="" method="post" class="hide"></form>
		<a id="assignButton" href="javascript:" class="btn btn-primary">分配下级库房</a>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.$.jBox.open("iframe:${ctx}/key/keyUsbKeyDepot/depottodepot?id=${depot.id}", "分配下级库房",810,$(top.document).height()-240,{
					buttons:{"全选":"all","确定分配":"ok", "清除已选":"clear", "关闭":true}, bottomText:"为当前库房分配下级库房。",submit:function(v, h, f){
						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;
						
						if(v=="all"){
							h.find("iframe")[0].contentWindow.selectAll();
							return false;
						}
						
						if (v=="ok"){
							// 删除''的元素
							if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
								top.$.jBox.tip("未给库房【${depot.depotName}】分配下级库房！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#assignDepotForm').attr('action','${ctx}/key/keyUsbKeyDepot/assignDepot?id=${depot.id}&idsArr='+idsArr).submit();
					    	return true;
						} else if (v=="clear"){
							h.find("iframe")[0].contentWindow.clearAssign();
							return false;
		                }
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
		<th>库房名称</th>
		<th>所在区域</th>
		<th>所属网点</th>
		<th>库房余量</th>
		<th>key类型名称</th>
		<th>key类型标识</th>
		
		<shiro:hasPermission name="key:keyUsbKeyDepot:edit">
		<th>操作</th>
		</shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${depots}" var="keyDepot">
			<tr>
				<td>${keyDepot.depotName}</td>
				<td>${keyDepot.office.parent.name}</td>
				<td>${keyDepot.office.name}</td>
				<td>${keyDepot.inCount}</td>
				<td>
					<c:forEach items="${keyDepot.keyDepotGeneralStatisticsList}" var="statis">
					        	${statis.keyGeneralInfo.name}
								</br>
					</c:forEach>		
				</td>
				<td>
					<c:forEach items="${keyDepot.keyDepotGeneralStatisticsList}" var="statis">
						        ${statis.keyGeneralInfo.model}
								</br>
					</c:forEach>		
				</td>
				<shiro:hasPermission name="key:keyUsbKeyDepot:edit"><td>
					<a href="${ctx}/key/keyUsbKeyDepot/outDepot?depotId=${keyDepot.id}&parentId=${depot.id}" 
						onclick="return confirmx('确认要将<b>[${keyDepot.depotName}]</b><b></b>移除吗？', this.href)">移除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
