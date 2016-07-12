<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据生成</title>
<link rel="stylesheet" type="text/css" href="js/test/bootstrap.min.css">
<script type="text/javascript" src="js/test/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/test/bootstrap.min.js"></script>
<script type="text/javascript" src="js/pta_topca.js"></script>
<script type="text/javascript" src="js/xenroll.js"></script>
<script src="static/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$.getJSON("test/getAddPrepareData?_="+new Date().getTime(),function(data){
			var html = "";
			var office = eval('(' + data.office + ')'); 
			$.each(office,function(idx,e){
				var ele = office[idx];
				html += "<option value='"+ele.id+"'>"+ele.name+"</option>";
			});
			$("#office").html(html);
			html = "";
		});
	})
	function makeData(){
		$("#make").hide();
		$("#returnJsonData").html("正在生成...........");
		var url = "test/importNewDealInfo?officeId="+$("#office").val()+"&pattern="+$("#pattern").val()+"&_="+new Date().getTime();
		$.getJSON(url,function(data){
			$("#make").show();
			$("#returnJsonData").html(data.msg);
		});
	}
	
	function updateFirstCertSN(){
		$("#make").hide();
		$("#returnJsonData").html("正在生成...........");
		var url = "test/updateFirstCertSN?&_="+new Date().getTime();
		$.getJSON(url,function(data){
			$("#make").show();
			$("#returnJsonData").html(data.msg);
		});
	}
	
	function makeUpdateData(){
		$("#update").hide();
		var autoPass = 0;
		$("#returnJsonData").html("正在生成...");
		if($("#pass").prop("checked")){
			autoPass = 1;
		}
		var url = "test/autoWriteUpdate?autoPass="+autoPass+"&_="+new Date().getTime();
		$.getJSON(url,function(data){
			$("#update").show();
			$("#returnJsonData").html(data.msg);
		});
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<address>
					<strong>操作结果</strong><br /> <abbr id="returnJsonData">P:</abbr>
				</address>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="tabbable tabs-left" id="tabs-443838">
					<ul class="nav nav-tabs">
						<li class="active"><a href="#panel-98562" data-toggle="tab">新增业务</a></li>
						<li><a href="#panel-489367" data-toggle="tab">更新业务</a></li>
						<li><a href="#panel-4893681" data-toggle="tab">更新首张证书序列号</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="panel-98562">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>为workCertInfo表中有WorkUserId值的相关数据新增workDealInfo，</p>
											<small>------</small>
										</blockquote>
										网点：<select id="office"></select><br/>
									    日期格式:<select id="pattern">
									    	<option value="yyyy-MM-dd HH:mm:ss.S">yyyy-MM-dd HH:mm:ss.S</option>
									    	<option value="yyyy-MM-dd HH:mm:ss">yyyy-MM-dd HH:mm:ss</option>
									    	<option value="yyyy-MM-dd">yyyy-MM-dd</option>
									    	<option value="yyyyMMdd HH:mm:ss.S">yyyyMMdd HH:mm:ss.S</option>
									    </select>
										<br/>
										<input type="button" class="btn btn-warning"
											onclick="makeData()" value="生成新增数据" id="make"/>
									</div>
								</div>
							</div>
							<br />
						</div>
						<div class="tab-pane" id="panel-489367">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>生成更新数据</p>
											<small>可设定生成的数据是否自动通过</small>
										</blockquote>
										<br>
										<button class="btn btn-warning" type="button"
											onclick="makeUpdateData()" id="update">生成更新数据</button>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="panel-4893681">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>更新首张证书数据</p>
										</blockquote>
										<br>
										<button class="btn btn-warning" type="button"
											onclick="updateFirstCertSN()" id="update">更新首张证书数据</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>