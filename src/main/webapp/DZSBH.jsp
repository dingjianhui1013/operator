<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>多证书编号测试接口页面</title>
<script type="text/javascript" src="js/test/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		
	});
	
	function org(){
		var orgNum = $("#orgNum").val();
		var proId = $("#proId").val();
		var url = "certSort/findCertSort";
		var urlLabel = "certSort/findCertSort?organizationNumber="+orgNum+"&productTdId="+proId+"&_="+new Date().getTime();
		$("#rogLabel").html(urlLabel);
		$.ajax({
			url : url,
			data:{organizationNumber:orgNum,productTdId:proId,_:new Date().getTime()},
			dataType : 'json',
			success : function(data) {
				var str = '{"certsInSccA":'+ data.certsInSccA +',"status":"'+data.status+'","errmsg":"'+ data.errmsg +'"}';
				$("#orgResult").html(str);
			}});
	}
	
	function com(){
		var companyName = $("#companyName").val();
		var comId = $("#comId").val();
		var url = "certSort/findCertSort";
		
		var urlLabel = "certSort/findCertSort?companyName="+companyName+"&productTdId="+comId+"&_="+new Date().getTime();
		$("#comLabel").html(urlLabel);
		$.ajax({
			url : url,
			data:{companyName:companyName,productTdId:comId,_:new Date().getTime()},
			dataType : 'json',
			success : function(data) {
				var str = '{"certsInSccA":'+ data.certsInSccA +',"status":"'+data.status+'","errmsg":"'+ data.errmsg +'"}';
				$("#comResult").html(str);
			}});
	}
	
	function comNum(){
		var conCertNumber = $("#conCertNumber").val();
		var conNumId = $("#conNumId").val();
		var url = "certSort/findCertSort";
		
		var urlLabel = "certSort/findCertSort?conCertNumber="+conCertNumber+"&productTdId="+conNumId+"&_="+new Date().getTime();
		$("#comNumLabel").html(urlLabel);
		$.ajax({
			url : url,
			data:{conCertNumber:conCertNumber,productTdId:conNumId,_:new Date().getTime()},
			dataType : 'json',
			success : function(data) {
				var str = '{"certsInSccA":'+ data.certsInSccA +',"status":"'+data.status+'","errmsg":"'+ data.errmsg +'"}';
				$("#comNumResult").html(str);
			}});
	}
	
	function comName(){
		var contactName = $("#contactName").val();
		var conNumId = $("#conNumId").val();
		var url = "certSort/findCertSort";
		
		var urlLabel = "certSort/findCertSort?contactName="+contactName+"&productTdId="+conNumId+"&_="+new Date().getTime();
		$("#comNameLabel").html(urlLabel);
		$.ajax({
			url : url,
			data:{contactName:contactName,productTdId:conNumId,_:new Date().getTime()},
			dataType : 'json',
			success : function(data) {
				var str = '{"certsInSccA":'+ data.certsInSccA +',"status":"'+data.status+'","errmsg":"'+ data.errmsg +'"}';
				$("#comNameResult").html(str);
			}});
	}
</script>
</head>
<body>
	<table border="2">
		<tr><th>描述</th><th>参数</th><th>访问接口地址及参数</th><th>返回值</th></tr>
		<tr>
			<td width="25%">根据组织机构查询企业证书或机构证书数量</td>
			<td width="25%"><br>组织机构编号(organizationNumber):<input type="text" id="orgNum"/><br><br>
				产品类型(productTdId):
				<select id="proId">
					<option value="1">企业证书</option>
					<option value="3">机构证书</option>
				</select><br>
				<br><input type="button" onclick="org()" value="查询"/>
			</td>
			<td width="25%">
				<label id = "rogLabel"></label>
			
			</td>
			<td width="25%">
				<label id = "orgResult"></label>
			
			</td>
		</tr>
		<tr>
			<td width="25%">根据单位名称查询企业证书数量</td>
			<td width="25%"><br>companyName(单位名称):<input type="text" id="companyName"/><br><br>
				产品类型(productTdId):
				<select id="comId">
					<option value="3">机构证书</option>
				</select><br>
				<br><input type="button" onclick="com()" value="查询"/>
			</td>
			<td width="25%">
				<label id = "comLabel"></label>
			
			</td>
			<td width="25%">
				<label id = "comResult"></label>
			
			</td>
		</tr>
		<tr>
			<td width="25%">根据持证人身份证号查询个人证书(企业、机构)数量</td>
			<td width="25%"><br>conCertNumber(持证人身份证号):<input type="text" id="conCertNumber"/><br><br>
				产品类型(productTdId):
				<select id="conNumId">
					<option value="2">个人证书(企业)</option>
					<option value="6">个人证书(机构)</option>
				</select><br>
				<br><input type="button" onclick="comNum()" value="查询"/>
			</td>
			<td width="25%">
				<label id = "comNumLabel"></label>
			
			</td>
			<td width="25%">
				<label id = "comNumResult"></label>
			
			</td>
		</tr>
		
		<tr>
			<td width="25%">根据持证人姓名查询个人证书(企业、机构)数量</td>
			<td width="25%"><br>contactName(持证人姓名):<input type="text" id="contactName"/><br><br>
				产品类型(productTdId):
				<select id="conNameId">
					<option value="2">个人证书(企业)</option>
					<option value="6">个人证书(机构)</option>
				</select><br>
				<br><input type="button" onclick="comName()" value="查询"/>
			</td>
			<td width="25%">
				<label id = "comNameLabel"></label>
			
			</td>
			<td width="25%">
				<label id = "comNameResult"></label>
			
			</td>
		</tr>
		
		
		
	</table>
	
	
</body>
</html>