<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配下级库房</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/receipt/receiptDepotInfo/">库房列表</a></li>
		<li class="active"><a href="${ctx}/receipt/receiptDepotInfo/assign?id=${depot.id}"><shiro:hasPermission name="receipt:receiptDepotInfo:view">下级库房</shiro:hasPermission><shiro:lacksPermission name="receipt:receiptDepotInfo:view">分配列表</shiro:lacksPermission></a></li>
	
	</ul>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span4">库房名称: <b>${depot.receiptName}</b></span>
			<span class="span8">所属网点: ${depot.office.name}</span>
			
		</div>
	</div>
	<tags:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignDepotForm" action="" method="post" class="hide"></form>
		<a id="assignButton" href="javascript:" class="btn btn-primary">分配下级库房</a>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.$.jBox.open("iframe:${ctx}//receipt/receiptDepotInfo/depottodepot?id=${depot.id}", "分配下级库房",810,$(top.document).height()-240,{
					buttons:{"全选":"all","确定分配":"ok", "清除已选":"clear", "关闭":true}, bottomText:"为当前库房分配下级库房。",submit:function(v, h, f){
						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;
						//nodes = selectedTree.getSelectedNodes();
						
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
								top.$.jBox.tip("未给库房【${depot.receiptName}】分配下级库房！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#assignDepotForm').attr('action','${ctx}/receipt/receiptDepotInfo/assignDepot?id=${depot.id}&idsArr='+idsArr).submit();
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
		<th>发票余量/元</th>
		<th>联系人</th>
		<th>电话</th>
		
		<shiro:hasPermission name="receipt:receiptDepotInfo:view">
		<th>操作</th>
		</shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${depots}" var="receiptDepot">
			<tr>
				<td>${receiptDepot.receiptName}</td>
				<td>${receiptDepot.area.name}</td>
				<td>${receiptDepot.office.name}</td>
				<td>${receiptDepot.receiptResidue}</td>
				<td>
					${receiptDepot.receiptCommUser}
				</td>
				<td>
					${receiptDepot.receiptCommMobile}	
				</td>
				<shiro:hasPermission name="receipt:receiptDepotInfo:view"><td>
					<a href="${ctx}/receipt/receiptDepotInfo/outDepot?depotId=${receiptDepot.id}&parentId=${depot.id}" 
						onclick="return confirmx('确认要将<b>[${receiptDepot.receiptName}]</b><b></b>移除吗？', this.href)">移除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
