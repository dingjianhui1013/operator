<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算统计测试页面</title>
<link rel="stylesheet" type="text/css" href="js/test/bootstrap.min.css">
<script type="text/javascript" src="js/test/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/test/bootstrap.min.js"></script>
<script type="text/javascript" src="js/pta_topca.js"></script>
<script type="text/javascript" src="js/xenroll.js"></script>
<script src="static/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function settle(obj) {
		if ($("#autoDay").val() == "") {
			$("#returnJsonData").html("（ ´☣///_ゝ///☣｀）  日期必选！");
			return false;
		}
		if (obj == 0) {//发放量统计
			var url = "test/cert?curDate=" + $("#autoDay").val() + "&_="
					+ new Date().getTime();
			$.getJSON(url, function(data) {
				if(data.status==1){
					$("#returnJsonData").html("操作成功："+data.msg+new Date().getTime());
				}else{
					$("#returnJsonData").html("操作失败："+data.msg+new Date().getTime());
				}
			});
		} else {
			var url = "test/day?curDate=" + $("#autoDay").val() + "&_="
					+ new Date().getTime();
			$.getJSON(url, function(data) {
				if(data.status==1){
					$("#returnJsonData").html("操作成功："+data.msg+new Date().getTime());
				}else{
					$("#returnJsonData").html("操作失败："+data.msg+new Date().getTime());
				}
			});
		}
	}
	function clearData(obj) {
		if ($("#clearDay").val() == "") {
			$("#returnJsonData").html("（ ´☣///_ゝ///☣｀）  日期必选！");
			return false;
		}
		var url = "test/clear?type=" + obj + "&day=" + $("#clearDay").val()
				+ "&_=" + new Date().getTime();
		$.getJSON(url, function(data) {
			if(data.status==1){
				$("#returnJsonData").html("操作成功："+data.msg+new Date().getTime());
			}else{
				$("#returnJsonData").html("操作失败："+new Date().getTime());
			}
		});
	}
	function sendMail() {
		var url = "test/sendMail?_=" + new Date().getTime();
		$.getJSON(url, function(data) {
			if(data.status==1){
				$("#returnJsonData").html("操作成功,如未收到邮件请检查邮件服务配置----："+new Date().getTime());
			}else{
				$("#returnJsonData").html("操作失败："+new Date().getTime());
			}
		});
	}
	function viewInfo(){
		$("#info").show();
		$("#tb").html("");
		var url = "test/showAllDealInfo?_=" + new Date().getTime();
		var html = "";
		$.getJSON(url,function(data){
			if(data.status==1){
				$.each(data.result,function(idx,ele){
					var c = "info";
					if(idx%2==0){
						c = "warning";
					}
					html += "<tr class='"+c+"'><td>"+ele.id+"</td>";
					html += "<td>"+ele.sn+"</td>";
					html += "<td>"+ele.product+"</td>";
					html += "<td>"+ele.year+"</td>";
					html += "<td>"+ele.keysn+"</td>";
					html += "<td>"+ele.certsn+"</td>";
					html += "<td>"+ele.certDate+"</td>";
					html += "<td>"+ele.obtain+"</td>";
					html += "<td>"+ele.isSettle+"</td>";
					html += "<td>"+ele.settleYear+"</td>";
					html += "<td>"+ele.payMethod+"</td>";
					html += "<td>"+ele.dealType1+"</td>";
					html += "<td>"+ele.dealType2+"</td>";
					html += "<td>"+ele.dealTotal+"</td>";
					html += "<td>"+ele.payTotal+"</td>";
					html += "<td>"+ele.receiptAmount+"</td>";
					html += "<td>"+ele.dealinfostatus+"</td>";
					html += "<td>"+ele.office+"</td>";
					html += "<td>"+ele.user+"</td>";
					html += "<td>"+ele.ou+"</td>";
					html += "<td>"+ele.isTest+"</td>";
					html += "</tr>";
				});
				$("#tb").html(html);
				$("#returnJsonData").html("操作成功，tips:可以将数据复制到Excel中筛选");
			}else{
				$("#tb").html("查询失败....( ⊙ o ⊙ )");
			}
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
						<li class="active"><a href="#panel-98562" data-toggle="tab">定时任务</a></li>
						<li><a href="#panel-489367" data-toggle="tab">业务台账</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="panel-98562">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>清除当天定时任务数据</p>
											<small>将指定某天的定时任务数据清除</small>
										</blockquote>
										<input id="clearDay" name="startTime" type="text"
											readonly="readonly" maxlength="20" class="Wdate"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" /><br /> <input
											type="button" class="btn btn-warning" onclick="clearData(0)"
											value="清除证书发放量统计" /> <input type="button"
											class="btn btn-warning" onclick="clearData(1)"
											value="清除日经营统计" />
									</div>
								</div>
							</div>
							<br />
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>立即执行定时任务</p>
											<small>效果同定时任务，注意执行前一定先执行一次清空当天的数据，否则可能出现重复数据,注意，无论取哪天定时任务，余量数据是当前的实时数据</small>
										</blockquote>
										<input id="autoDay" name="startTime" type="text"
											readonly="readonly" maxlength="20" class="Wdate"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" /><br />
										<button class="btn btn-warning" type="button"
											onclick="settle(0)">执行证书发放量统计</button>
										<button class="btn btn-warning" type="button"
											onclick="settle(1)">执行日经营统计</button>
									</div>
								</div>
							</div>
							<br />
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>立即发送邮件</p>
											<small>效果同定时任务，需要到业务配置中配置邮件模板，能够对发票和key库存低于预警值的仓库发送邮件提醒</small>
										</blockquote>
										<button class="btn btn-warning" type="button"
											onclick="sendMail()">执行</button>
									</div>
								</div>
							</div>
						</div>
						<div class="tab-pane" id="panel-489367">
							<div class="container-fluid">
								<div class="row-fluid">
									<div class="span12">
										<blockquote>
											<p>获取当前系统中所有已完成的(包括吊销的)业务数据</p>
											<small>按时间排序，包括业务ID、keySn、certSn、网点、操作人员、是否结算、结算年限、付款方式、业务类型1、业务类型2、发票金额</small>
										</blockquote>
										<button class="btn btn-warning" type="button"
											onclick="viewInfo()">查看</button>
									</div>
								</div>
								</br>
								<div class="container-fluid" id="info" style="display: none">
									<div class="row-fluid">
										<div class="span12">
											<table
												class="table table-bordered table-hover table-condensed">
												<thead>
													<tr>
														<th>系统ID</th>
														<th>业务编号</th>
														<th>产品名称</th>
														<th>业务办理年限</th>
														<th>key序列号</th>
														<th>证书序列号</th>
														<th>证书有效期</th>
														<th>获取时间</th>
														<th>是否结算</th>
														<th>结算年限</th>
														<th>付款方式</th>
														<th>业务类型1</th>
														<th>业务类型2</th>
														<th>业务金额</th>
														<th>支付金额</th>
														<th>发票金额</th>
														<th>业务状态</th>
														<th>网点</th>
														<th>操作人员</th>
														<th>ou</th>
														<th>是否是测试RA</th>
													</tr>
												</thead>
												<tbody id="tb">
												</tbody>
											</table>
										</div>
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