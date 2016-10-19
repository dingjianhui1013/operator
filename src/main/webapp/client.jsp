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
		
		if(confirm('是否确认修复first_cert_sn?')){
			$("#returnJsonData").html("正在生成...........");
			var url = "test/updateFirstCertSN?updateCount="+$("#updateCount").val()+"&appid="+$("#updateAppid").val()+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixPreid(){
		$("#make").hide();
		
		if(confirm('是否确认重新生成prev_id?')){
			$("#returnJsonData").html("正在生成...........");
			var url = "test/fixPreId?prevIdCount="+$("#prevIdCount").val()+"&prevIdAppid="+$("#prevIdAppid").val()
					+"&prevFirstCertSN="+$("#prevFirstCertSN").val()+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixSVN(){
		$("#make").hide();
		
		if(confirm('是否确认修复svn?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixSVN?svnCount=0&svnAppid="+$("#svnAppid").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixFirstDealInfoType(){
		$("#make").hide();
		
		if(confirm('是否确认修复首条业务类型?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixFirstDealInfoType?fixAppId="+$("#fixAppId").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixMutilAdd(){
		$("#make").hide();
		
		if(confirm('是否确认修复同一业务链有多个新增类型?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixMutilAdd?fixAppId="+$("#mutiAppId").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixNotEquals(){
		$("#make").hide();
		
		if(confirm('是否确认修复首证书和首条记录证书不符问题?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixNotEquals?fixAppId="+$("#fixNotEqualsAppId").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	
	function fixFirstCertSN(){
		$("#make").hide();
		
		if(confirm('是否确认修复firstCertSN?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixFirstCertSN?fixFirstCertSnAppid="+$("#fixFirstCertSnAppid").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
	}
	
	function fixErrorFirstCertSn(){
		$("#make").hide();
		if(confirm('是否确认修复错位首证书的firstCertSN?')){
			$("#returnJsonData").html("正在修复...........");
			var url = "test/fixErrorFirstCertSn?fixErrorFirstCertSnAppId="+$("#fixErrorFirstCertSnAppId").val()
					+"&_="+new Date().getTime();
			$.getJSON(url,function(data){
				$("#make").show();
				$("#returnJsonData").html(data.msg);
			});
		}else{
			return false;
		}
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
	};
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
						<li><a href="#panel-4893688" data-toggle="tab">step0 - 修复错位首证书数据(较慢)</a></li>
						<li><a href="#panel-4893684" data-toggle="tab">step1 - 整理首证书数据</a></li>
						<li><a href="#panel-4893682" data-toggle="tab">step2 - 重新生成prev_id数据</a></li>
						<!-- 
						<li><a href="#panel-4893683" data-toggle="tab">修复svn数据</a></li>
						 -->
						<li><a href="#panel-4893685" data-toggle="tab">step3 - 修复首条业务类型不是新增问题</a></li>
						<li><a href="#panel-4893686" data-toggle="tab">step4 - 修复同一业务链多条新增问题</a></li>
						<!-- 
						<li><a href="#panel-4893687" data-toggle="tab">修复首证书序列号和自身序列号不符问题</a></li>
						 -->
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
									</div>
									<div class="span12">
										<input id="updateCount"/>&nbsp;更新记录数(填0为更新全部，数据多时不建议全部更新) 
										<br/>
										<input id="updateAppid"/>&nbsp;应用ID(更新所有应用时填0或不填，慎用)
										<br/><br/>
										<button class="btn btn-warning" type="button"
											onclick="updateFirstCertSN()" id="update">更新首张证书数据</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="panel-4893682">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>重新生成prev_id</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										
										<textarea id="prevFirstCertSN" rows="4" cols="150" class="txt"></textarea>&nbsp;可不填,指定首证书序列号,用来单条修复和测试,可以处理多条，用英文输入法的逗号分隔
										<br/>
										<input id="prevIdCount"/>&nbsp;重新生成记录总数(填0为重新全部生成,其他数字为指定修复的记录总数) 
										<br/>
										<input id="prevIdAppid"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixPreid()"	 id="update">修复prev_id</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						<!-- 
						<div class="tab-pane" id="panel-4893683">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复SVN</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="svnAppid"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixSVN()"	 id="update">修复svn</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						 -->
						<div class="tab-pane" id="panel-4893684">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复错位的firstCertSN</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="fixFirstCertSnAppid"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixFirstCertSN()"	 id="update">修复错位的firstCertSN</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="panel-4893685">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复首条业务类型</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="fixAppId"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixFirstDealInfoType()"	 id="update">修复首条业务类型</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="panel-4893686">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复同一业务链有多个新增问题</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="mutiAppId"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixMutilAdd()"	 id="update">修复多新增类型业务</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						<!-- 
						<div class="tab-pane" id="panel-4893687">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复首证书序列号和首条证书序列号不符问题</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="fixNotEqualsAppId"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixNotEquals()"	 id="update">修复首证书序列号和首条证书序列号不符问题</button>
										<br>
									</div>
								</div>
							</div>
						</div>
						 -->
						 <div class="tab-pane" id="panel-4893688">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>修复错位首证书数据(较慢)</p>
										</blockquote>
										<br>
									</div>
									<div class="span12">
										<input id="fixErrorFirstCertSnAppId"/>&nbsp;应用ID(指定应用ID，必填)
										<br/><br/>
										<button class="btn btn-warning" type="button"
										onclick="fixErrorFirstCertSn()"	 id="update">修复错位首证书数据</button>
										<br>
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