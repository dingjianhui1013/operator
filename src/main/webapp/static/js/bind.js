
function addPayInfoToListSoSB() {
	$("#view").show();
	$("#financeMoneyTr").remove();
	$("#financePosTr").remove();
	$("#financeBankTr").remove();
	$("#financeAlipayTr").remove();
	$("#financeGovTr").remove();
	$("#financeContractTr").remove();

	var officeName = $("#officeNameHid").val();
	var userName = $("#userNameHid").val();
	var curDate = $("#dateHid").val();
	var companyName = $("#companyHid").val();
	var html = "";
	// 现金
	if ($("#money").val() != "0" && $("#money").val() != "0.0"
			&& $("#money").val() != "" && $("#money").val() != "00.0") {
		html += "<tr id='financeMoneyTr'><td></td><td>"
				+ companyName
				+ "</td><td>"
				+ parseInt($("#money").val())
				+ "</td><td>"
				+ officeName
				+ "</td><td>"
				+ userName
				+ "</td><td>"
				+ curDate
				+ "</td><td>现金</td><td></td><td></td><td><input type='button' onclick='removePayInfo(1)' class='btn btn-primary' value='删除' /></td>";
		$("#money").attr("readonly", "readonly");
	}
	// pos
	if ($("#pos").val() != "0" && $("#pos").val() != "0.0"
			&& $("#pos").val() != "" && $("#pos").val() != "00.0") {
		html += "<tr id='financePosTr'><td></td><td>"
				+ companyName
				+ "</td><td>"
				+ parseInt($("#pos").val())
				+ "</td><td>"
				+ officeName
				+ "</td><td>"
				+ userName
				+ "</td><td>"
				+ curDate
				+ "</td><td>POS收款</td><td></td><td></td><td><input type='button' onclick='removePayInfo(2)' class='btn btn-primary' value='删除' /></td>";
		$("#pos").attr("readonly", "readonly");
	}
	// 政府统一采购
	if ($("#gc").val() == "1") {
		$("#financeTd").html("");
		html += "<tr id='financeGovTr'><td></td><td>"
				+ companyName
				+ "</td><td></td><td>"
				+ officeName
				+ "</td><td>"
				+ userName
				+ "</td><td>"
				+ curDate
				+ "</td><td>政府统一采购</td><td></td><td></td><td><input type='button' onclick='removePayInfo(5)' class='btn btn-primary' value='删除' /></td>";
	}
	// 合同采购
	if ($("#cc").val() == "1") {
		$("#financeTd").html("");
		html += "<tr id='financeContractTr'><td></td><td>"
				+ companyName
				+ "</td><td></td><td>"
				+ officeName
				+ "</td><td>"
				+ userName
				+ "</td><td>"
				+ curDate
				+ "</td><td>合同采购</td><td></td><td></td><td><input type='button' onclick='removePayInfo(6)' class='btn btn-primary' value='删除' /></td>";
	}
	$("#financeTd").append(html);
}

function removePayInfo(obj) {
	if (obj == 1) {
		$("#financeMoneyTr").remove();
		$("#money").val(0);
		$("#money").removeAttr("readonly");
	}
	if (obj == 2) {
		$("#financePosTr").remove();
		$("#pos").val(0);
		$("#pos").removeAttr("readonly");
	}
	if (obj == 3) {
		$("#financeBankTr").remove();
		$("#bank").val(0);
		$("#bank").removeAttr("readonly");
	}
	if (obj == 4) {
		$("#financeAlipayTr").remove();
		$("#alipay").val(0);
		$("#alipay").removeAttr("readonly");
	}
	if (obj == 5) {
		$("#financeGovTr").remove();
	}
	if (obj == 6) {
		$("#financeContractTr").remove();
	}
	fixMoney();
}

function choose(obj) {
	$("#paymentMoney1").val("0");
	if (($(obj).prop("checked"))) {
		$("#mc").attr("disabled", "disabled");
		$("#bc").attr("disabled", "disabled");
		$("#pc").attr("disabled", "disabled");
		$("#ac").attr("disabled", "disabled");
		$("#ks").attr("disabled", "disabled");
		$("#mc").removeAttr("checked");
		$("#bc").removeAttr("checked");
		$("#pc").removeAttr("checked");
		$("#ac").removeAttr("checked");
		$("#ks").removeAttr("checked");
		if ($(obj).attr("methodval") == 0) {
			$("#cc").attr("disabled", "disabled");
			$("#cc").removeAttr("checked");
		}
		if ($(obj).attr("methodval") == 1) {
			$("#gc").attr("disabled", "disabled");
			$("#gc").removeAttr("checked");
		}
		$("#payment").html("");
	} else {
		$("#mc").removeAttr("disabled");
		$("#bc").removeAttr("disabled");
		$("#pc").removeAttr("disabled");
		$("#ac").removeAttr("disabled");
		$("#ks").removeAttr("disabled");
		if ($(obj).attr("methodval") == 0) {
			$("#cc").removeAttr("disabled");
		}
		if ($(obj).attr("methodval") == 1) {
			$("#gc").removeAttr("disabled");
		}
	}
}

function removePayinfoTr(obj) {
	$(obj).parent().parent().remove();
}

function delCookie(name) {// 为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
	var date = new Date();
	date.setTime(date.getTime() - 10000);
	document.cookie = name + "=a; expires=" + date.toGMTString();
}

function SetCookie(name, value) {
	var Days = 30; // 此 cookie 将被保存 30 天
	var exp = new Date(); // new Date("December 31, 9998");
	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();
}

function getCookie(name)// 取cookies函数
{
	var arr = document.cookie
			.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
	if (arr != null)
		return unescape(arr[2]);
	return null;
}

// 确认按钮
function addPayInfoToList() {
	var useReceipt = false;
	var checkMoney = true;//是否要检查金额够
	var receiptMoney = 0;
	$("#view").show();
	$("#financeMoneyTr").remove();
	$("#financePosTr").remove();
	$("#financeBankTr").remove();
	$("#financeAlipayTr").remove();
	$("#financeGovTr").remove();
	$("#financeContractTr").remove();
	$("#contentTable").show();
	var payInfoIds =  "";
	var officeName = $("#officeNameHid").val();
	var userName = $("#userNameHid").val();
	var curDate = $("#dateHid").val();
	var companyName = $("#companyHid").val();
	var sumMoney = parseFloat($("#sumMoney").html());
	var html = "";
	if($("#money").val()==''){
		$("#money").val(0);
	}
	if($("#pos").val()==''){
		$("#pos").val(0);
	}
	if (parseFloat($("#money").val()) + parseFloat($("#pos").val()) > sumMoney) {
		top.$.jBox.tip("您的支付金额已超出，请确认支付金额!");
		removePayInfoToList();
		return false;
	} else if (parseFloat($("#money").val()) + parseFloat($("#pos").val()) < sumMoney) {
		sumMoney -= (parseFloat($("#money").val()) + parseFloat($("#pos").val()));
		
		$("#payment")
		.find(":checked")
		.prop('checked', true)
		.each(
				function() {var financePayInfoId = $(this).attr("financePayInfoId");
				payInfoIds += financePayInfoId + ",";
				});
		
		if(payInfoIds!=""){
			
			checkPayInfoList(payInfoIds , sumMoney);
		}

	} else if (parseFloat($("#money").val()) + parseFloat($("#pos").val()) == sumMoney) {
		sumMoney = 0;
	}
	
	
	if(parseFloat($("#money").val()) + parseFloat($("#pos").val()) >= sumMoney ||payInfoIds=="") {
	// 现金
	if ($("#money").val() != "0" && $("#money").val() != "0.0"
			&& $("#money").val() != "") {
		html += "<tr id='financeMoneyTr'><td>" + companyName + "</td><td>"
				+ parseInt($("#money").val()) + "</td><td>" + officeName
				+ "</td><td>" + userName + "</td><td>" + curDate
				+ "</td><td>现金</td><td></td><td></td>";
		$("#money").attr("readonly", "readonly");
		useReceipt = true;
		receiptMoney += parseInt($("#money").val());
	}
	// pos
	if ($("#pos").val() != "0" && $("#pos").val() != "0.0"
			&& $("#pos").val() != "") {
		html += "<tr id='financePosTr'><td>" + companyName + "</td><td>"
				+ parseInt($("#pos").val()) + "</td><td>" + officeName
				+ "</td><td>" + userName + "</td><td>" + curDate
				+ "</td><td>POS收款</td><td></td><td></td>";
		$("#pos").attr("readonly", "readonly");
		useReceipt = true;
		receiptMoney += parseInt($("#pos").val());
	}
	if (sumMoney > 0&&checkMoney) {
		top.$.jBox.tip("您的支付金额少于本次应付金额，请确认支付金额!");
		removePayInfoToList();
		return false;
	}
	$("#mc").attr("disabled", "disabled");
	$("#pc").attr("disabled", "disabled");
	$("#ks").attr("disabled", "disabled");
	
	$("#financeTd").append(html);
	$("#shouldMoney").val(parseFloat($("#sumMoney").val())-(sumMoney));
	$("#collectMoney").val($("#allTotalMoney").val());
	$("input[name='receiptAmount']").val($("#allTotalMoney").val());

	$("#sff0").prop("checked",true);
	$("#sff1").prop("checked",false);
	var btn = document.getElementById("settle");  
	btn.value = "删除";  
	btn.onclick = function(){removePayInfoToList();};  
	
	
	}
}
// 删除按钮
function removePayInfoToList() {
	var sff = 1;
	$("#financeTd").html("");
	$("#ks").removeAttr("disabled");
	$("#mc").removeAttr("disabled");
	$("#bc").removeAttr("disabled");
	$("#pc").removeAttr("disabled");
	$("#ac").removeAttr("disabled");
	$("#ks").removeAttr("disabled");
	$("#cc").removeAttr("disabled");
	$("#gc").removeAttr("disabled");
	if($("#sff0").prop("checked")) {
		sff = 0;
	}
	if($("#sff1").prop("checked")) {
		sff = 1;
	}
	$("body").find(":checked").prop('checked', true).each(function() {
		$(this).removeAttr("checked");
	});
	if(sff==0){
		//$("#sff0").attr("checked","checked");
		$("#sff0").prop("checked");

	} else {
		//$("#sff1").attr("checked","checked");
		$("#sff1").prop("checked");


	}
	$("#money").val("0");
	$("#pos").val("0");
	$("#bank").val("0");
	$("#alipay").val("0");
	$("#shouldMoney").val($("#sumMoney").val());
	$("#collectMoney").val("0");
	
	
	var btn = document.getElementById("settle");  
	   btn.value = "确认";  
	   btn.onclick = function(){addPayInfoToList();};  
	
	
	
	//$("#settle").val("确认");
	//$("#settle").attr("onclick", "addPayInfoToList()");
	$("#contentTable").hide();

}