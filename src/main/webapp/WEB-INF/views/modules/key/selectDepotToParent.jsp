<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配下级库房</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		var depotTree;   //待选库房
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			
			depotTree = $.fn.zTree.init($("#depotTree"), setting, classNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting1, selectedNodes);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		
		var setting1 = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClickRemove}};
		
		
		
		var classNodes = [
		  		<c:forEach items="${depotList}" var="depot">
				 {id:"${depot.id}",
			      pId:"0",
			      name:"${depot.depotName}"},
		        </c:forEach>];
		
		
		var pre_selectedNodes =[
		        <c:forEach items="${selectedList}" var="depot">
  		        	{id:"${depot.id}",
		  	         pId:"0",
		           	 name:"<font color='red' style='font-weight:bold;'>${depot.depotName}</font>"},
		           </c:forEach>];
		
		
		var selectedNodes =[
		        <c:forEach items="${selectedList}" var="depot">
		        {id:"${depot.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${depot.depotName}</font>"},
		        </c:forEach>];
		
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		
		//全选
		function selectAll(){
			
			
			var nodes = depotTree.getNodes();
			for(var i=0;i<nodes.length;i++){
			    var node = nodes[i];
			    if($.inArray(String(node.id), ids)<0){
			    	selectedTree.addNodes(null, node);
					ids.push(String(node.id));	
			    }
			    
			}
		}
		
		//点击选择项回调(增加)
		function treeOnClick(event, treeId, treeNode, clickFlag){
			
			/* if("depotTree"==treeNode){ */
				
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
				
			/* }; */
		};
		
		
		
		
		
		//点击选择项回调(减少)
		function treeOnClickRemove(event, treeId, treeNode, clickFlag){
			var nodes = selectedTree.getNodes();
			var array = new Array;
			if($.inArray(String(treeNode.id), pre_ids)>=0){
				return false;
			}
			
			
			for(var i=nodes.length-1;i>=0;i--){
				
				console.log(array);
				var node = nodes[i];
				if(node.id!=treeNode.id){
					array.push(node.id);
					ids.pop(String(node.id));
					
				}
				else{
					
					ids.pop(String(treeNode.id));
					selectedTree.removeNode(treeNode, null);
					for(var j=0;j<array.length;j++){
						ids.push(String(array[j]));
					}
					
					break;
					
				}
			}
		}
		
				
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给库房【${depot.depotName}】分配下级库房！";
					}else{
						tips = "已选库房清除成功！";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					
					$.fn.zTree.init($("#selectedTree"), setting1, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除库房【${depot.depotName}】下的已选库房？";
			top.$.jBox.confirm(tips, "清除确认", submit);
		};
	</script>
</head>
<body>
	<div id="assignRole" class="row-fluid span12">
		<div class="span2" style="border-right: 1px solid #A8A8A8;">
			<p>上级库房：</p>
			<div id="parentTree" class="ztree">
			<p>${depot.depotName }</p>
			</div>
		</div>
		<div class="span5">
			<p>待选库房：</p>
			<div id="depotTree" class="ztree"></div>
		</div>
		<div class="span5" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选库房：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>
