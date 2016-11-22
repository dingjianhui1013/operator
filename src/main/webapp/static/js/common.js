// JavaScript Document
$(function() {

	$("#viewMore").click(
			function() {
				var t = $(this);
				if (t.hasClass('click')) {
					t.removeClass('click').addClass('noClick').html(
							"查看“易证通”数字证书用户责任书");
					t.siblings('#noticeShowBox').hide();
				} else {
					t.removeClass('noClick').addClass('click').html(
							"关闭“易证通”数字证书用户责任书");
					t.siblings('#noticeShowBox').show();
				}
			});

	// 禁止后退键 作用于Firefox、Opera
	document.onkeypress = banBackSpace;
	// 禁止后退键 作用于IE、Chrome
	document.onkeydown = banBackSpace;
})

// 处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
function banBackSpace(e) {
	var ev = e || window.event;// 获取event对象
	var obj = ev.target || ev.srcElement;// 获取事件源
	var t = obj.type || obj.getAttribute('type');// 获取事件源类型
	// 获取作为判断条件的事件类型
	var vReadOnly = obj.getAttribute('readonly');
	var vEnabled = obj.getAttribute('enabled');
	// 处理null值情况
	vReadOnly = (vReadOnly == null) ? false : vReadOnly;
	vEnabled = (vEnabled == null) ? true : vEnabled;
	// 当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	// 并且readonly属性为true或enabled属性为false的，则退格键失效
	var flag1 = (ev.keyCode == 8
			&& (t == "password" || t == "text" || t == "textarea") && (vReadOnly == true || vEnabled != true)) ? true
			: false;
	// 当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true
			: false;
	// 判断
	if (flag2) {
		return false;
	}
	if (flag1) {
		return false;
	}
}

function PreviewImage(fileObj, imgPreviewId, divPreviewId, picBigBoxId) {
	var allowExtention = ".jpg,.bmp,.gif,.png";
	var extention = fileObj.value.substring(fileObj.value.lastIndexOf(".") + 1)
			.toLowerCase();
	var browserVersion = window.navigator.userAgent.toUpperCase();
	var MAXWIDTH = 100;
	var MAXHEIGHT = 100;
	if (allowExtention.indexOf(extention) > -1) {
		var imgPreview = document.getElementById(imgPreviewId);
		imgPreview.onload = function() {
			imgPreview.removeAttribute("width");
			imgPreview.removeAttribute("height");
			var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT,imgPreview.offsetWidth, imgPreview.offsetHeight);
			imgPreview.width = rect.width;
			imgPreview.height = rect.height;
			imgPreview.style.marginLeft = rect.left + 'px';
			imgPreview.style.marginTop = rect.top + 'px';
		}
		if (imgPreviewId == 'imghead') {
			$("#companyImage1").val(fileObj.value);
		}
		if (imgPreviewId == 'imghead1') {
			$("#transactorImage1").val(fileObj.value);
		}
		if (fileObj.files) {
			if (window.FileReader) {
				var reader = new FileReader();
				reader.onload = function(e) {
					imgPreview.setAttribute("src", e.target.result);
					
					alert(e.target.result);
					
					$('#' + picBigBoxId + ' img').attr("src", e.target.result);
				}
				reader.readAsDataURL(fileObj.files[0]);
			} else if (browserVersion.indexOf("SAFARI") > -1) {
				alert("不支持Safari6.0以下浏览器的图片预览!");
			}
		} else if (browserVersion.indexOf("MSIE") > -1) {
			if (browserVersion.indexOf("MSIE 6") > -1) {// ie6
				imgPreview.setAttribute("src", fileObj.value);
				$('#' + picBigBoxId + ' img').attr("src", e.target.result);
			} else {// ie[7-9]
				fileObj.select();
				if (browserVersion.indexOf("MSIE 9") > -1
						|| browserVersion.indexOf("MSIE 8") > -1)
					fileObj.blur();
				//缩略图
				var newPreview = document.createElement("img");
				newPreview.setAttribute("id", imgPreviewId);
				newPreview.style.width = document.getElementById(imgPreviewId).width+ "px";
				newPreview.style.height = document.getElementById(imgPreviewId).height+ "px";
				newPreview.style.border = "1px solid #d2e2e2";
				newPreview.src = contextPath+'/images/transparent.gif';
				newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='"
						+ document.selection.createRange().text + "')";
				var tempDivPreview = document.getElementById(divPreviewId);
				tempDivPreview.getElementsByTagName('img')[0].removeNode(true);
				tempDivPreview.getElementsByTagName("a")[0].appendChild(newPreview);
				//原图
				var tempDivPreview1 = document.getElementById(picBigBoxId);
				var img = tempDivPreview1.getElementsByTagName('img')[0];
				tempDivPreview1.removeChild(img);
				var newpicBigBox = document.createElement("img");
				newpicBigBox.style.width="842px"; 
				newpicBigBox.style.height='595px';
				newpicBigBox.src = contextPath+'/images/transparent.gif';
				newpicBigBox.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='"
						+ document.selection.createRange().text + "')";
				tempDivPreview1.appendChild(newpicBigBox);
			}
		} else if (browserVersion.indexOf("FIREFOX") > -1) {// firefox
			var firefoxVersion = parseFloat(browserVersion.toLowerCase().match(
					/firefox\/([\d.]+)/)[1]);
			if (firefoxVersion < 7) {
				imgPreview.setAttribute("src", fileObj.files[0].getAsDataURL());
				$('#' + picBigBoxId + ' img').attr("src",
						fileObj.files[0].getAsDataURL());
			} else {// firefox7.0+
				imgPreview.setAttribute("src", window.URL
						.createObjectURL(fileObj.files[0]));
				$('#' + picBigBoxId + ' img').attr("src",
						window.URL.createObjectURL(fileObj.files[0]));
			}
		} else {
			imgPreview.setAttribute("src", fileObj.value);
			$('#' + picBigBoxId + ' img').attr("src", fileObj.value);
		}
	} else {
		alert("仅支持" + allowExtention + "为后缀名的文件!");
		fileObj.value = "";// 清空选中文件
		if (browserVersion.indexOf("MSIE") > -1) {
			fileObj.select();
			document.selection.clear();
		}
		fileObj.outerHTML = fileObj.outerHTML;
	}
}

function clacImgZoomParam(maxWidth, maxHeight, width, height) {
	var param = {
		top : 0,
		left : 0,
		width : width,
		height : height
	};
	if (width > maxWidth || height > maxHeight) {
		rateWidth = width / maxWidth;
		rateHeight = height / maxHeight;
		if (rateWidth > rateHeight) {
			param.width = maxWidth;
			param.height = Math.round(height / rateWidth);
		} else {
			param.width = Math.round(width / rateHeight);
			param.height = maxHeight;
		}
	}
	param.left = Math.round((maxWidth - param.width) / 2);
	param.top = Math.round((maxHeight - param.height) / 2);
	return param;
}

// key
var ukeyadmin = null;
$(document).ready(
	function() {
		// itrusukeyadmin.CAB,检测KEY序列号
		var urlArray = new Array();
		urlArray = window.location.toString().split('/');
		var base = urlArray[0] + '//' + window.location.host + '/'
				+ urlArray[3];
		var objStr = "<object id='ukeyadmin2' codebase='"
				+ base
				+ "/download/itrusukeyadmin.cab#version=3,1,15,1012' classid='clsid:05395F06-244C-4599-A359-5F442B857C28'></object>";
		ukeyadmin = $(objStr).appendTo(document.body)[0];
});

// 动态得到输入框中的位数
function sumDigit(inputId) {
	$("#check_" + inputId).show();
	$("#check_" + inputId).html("已输入" + $("#" + inputId).val().length + "位");
}

// 验证地税管理部门的科所
function checkManagementPlace() {
	var managementPlace = $("#managementPlace").val();
	managementPlace = $.trim(managementPlace); 
	if (managementPlace.length == 0) {
		$("#check_managementPlace").show();
		$("#check_managementPlace").html("请输入科所");
		$("#check_error").html("请输入科所");
		return false;
	} else {
		$("#check_managementPlace").hide();
		$("#check_managementPlace").html("");
		return true;
	}
}

// 验证社保编码
function checkSecurityNumber() {
	var securityNumber = $("#securityNumber").val();
	securityNumber = $.trim(securityNumber); 
	if (securityNumber.length == 0) {
		$("#check_securityNumber").show();
		$("#check_securityNumber").html("请输入社保编码");
		$("#check_error").html("请输入社保编码");
		return false;
	} else if (securityNumber.length != 18) {
		$("#check_securityNumber").show();
		$("#check_securityNumber").html("请输入正确的社保编码");
		$("#check_error").html("请输入正确的社保编码");
		return false;
	} else {
		$("#check_securityNumber").hide();
		$("#check_securityNumber").html("");
		return true;
	}
}

// 验证单位名称
function checkCompanyName() {
	var companyName = $("#companyNameId").val();
	companyName = $.trim(companyName); 
	if (companyName.length == 0) {
		$("#check_companyName").show();
		$("#check_companyName").html("请输入单位名称");
		$("#check_error").html("请输入单位名称");
		return false;
	} else {
		$("#check_companyName").hide();
		$("#check_companyName").html("");
		return true;
	}
}

// 选择单位类型
function changeUserType(type) {
	$('#businessLicenseId').attr("disabled", "disabled");
	$('#companyLegalId').attr("disabled", "disabled");
	$('#publicOrganizationId').attr("disabled", "disabled");
	$('#otherId').attr("disabled", "disabled");
	$('#businessLicenseId').val('');
	$('#companyLegalId').val('');
	$('#publicOrganizationId').val('');
	$('#otherId').val('');
	$("#check_businessLicenseId").hide();
	$("#check_companyLegalId").hide();
	$("#check_publicOrganizationId").hide();
	$("#check_otherId").hide();
	$('#' + type).attr("disabled", false);
	$('#' + type).val('请输入证件号...')
}

// 单位类型所对应的证件号
function getValue(id, value, type) {
	if (value == "") {
		$("#check_" + id).show();
		$("#check_" + id).html("请输入" + type);
		return false;
	} else if (value.length > 15) {
		$("#check_" + id).show();
		$("#check_" + id).html("请输入正确的" + type);
		return false;
	} else {
		$("#check_" + id).hide();
		$("#check_" + id).html("");
		$("#organizationRegisterNumber").val(value);
		return true;
	}
}

// 验证组织机构代码
function organizationInfoByOrganizationNumber() {
	var organizationNumber = $("#organizationNumberPart").val();
	organizationNumber = $.trim(organizationNumber); 
	if (organizationNumber.length == 0) {
		$("#check_organizationNumberPart").hide();
		$("#check_organizationNumberPart").html("");
		return true;
	} else if (organizationNumber.length != 9) {
		$("#check_organizationNumberPart").show();
		$("#check_organizationNumberPart").html("请输入正确的组织机构代码");
		$("#check_error").html("请输入正确的组织机构代码");
		return false;
	} else {
		$("#check_organizationNumberPart").hide();
		$("#check_organizationNumberPart").html("");
		return true;
	}
}

// 验证代办人姓名
function checkUserName() {
	var userName = $("#name").val();
	userName = $.trim(userName);
	if (userName.length == 0) {
		$("#check_name").show();
		$("#check_name").html("请输入经办人姓名");
		$("#check_error").html("请输入经办人姓名");
		return false;
	} else {
		$("#check_name").hide();
		$("#check_name").html("");
		return true;
	}
}

// 验证证件号码
function checkDocumentNumber() {
	var documentNumber = $("#documentNumber").val();
	documentNumber = $.trim(documentNumber); 
	var transactorDocumentType = $("#transactorDocumentType").val();
	var filter = /^[\da-z]+$/i;
	if(transactorDocumentType=='身份证'){
		if (documentNumber.length == 0) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入身份证号");
			$("#check_error").html("请输入身份证号");
			return false;
		} else if (documentNumber.length != 18||!filter.test(documentNumber)) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入正确的身份证号");  
			$("#check_error").html("请输入正确的身份证号");
			return false;
		} else {
			$("#check_documentNumber").hide();
			$("#check_documentNumber").html("");
			return true;
		}
	}else if(transactorDocumentType=='军官证'){
		var filter1 = /^[\d\u3400-\u9FFF]+$/;
		if (documentNumber.length == 0) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入军官证号");
			$("#check_error").html("请输入军官证号");
			return false;
		} else if (documentNumber.length >18||!filter1.test(documentNumber)) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入正确的军官证号");
			$("#check_error").html("请输入正确的军官证号");
			return false;
		} else {
			$("#check_documentNumber").hide();
			$("#check_documentNumber").html("");
			return true;
		}
	}else if(transactorDocumentType=='其他'){
		if (documentNumber.length == 0) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入证件号码");
			$("#check_error").html("请输入证件号码");
			return false;
		} else if (documentNumber.length >18||!filter.test(documentNumber)) {
			$("#check_documentNumber").show();
			$("#check_documentNumber").html("请输入正确的证件号码");
			$("#check_error").html("请输入正确的证件号码");
			return false;
		} else {
			$("#check_documentNumber").hide();
			$("#check_documentNumber").html("");
			return true;
		}
		
	}
}

function removedocumentNumber(){
	$("#documentNumber").val("");
	$("#check_documentNumber").hide();
	$("#check_documentNumber").html("");
	
}

// 验证邮件
function checkEmail() {
	var email = $("#email").val();
	email = $.trim(email); 
	var filter = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
	if (email.length != 0) {
		if (filter.test(email)) {
			$("#check_email").hide();
			$("#check_email").html("");
			return true;
		} else {
			$("#check_email").show();
			$("#check_email").html("请输入正确的邮件地址");
			$("#check_error").html("请输入正确的邮件地址");
			return false;
		}
	} else {
		$("#check_email").show();
		$("#check_email").html("请输入邮件地址");
		$("#check_error").html("请输入邮件地址");
		return false;
	}
}

// 验证代办人手机号码
function checkPhone() {
	var phone = $("#phone").val();
	phone = $.trim(phone); 
	var filter = /^(1+\d{10})$/;
	if (phone.length != 0) {
		if (filter.test(phone)) {
			$("#check_phone").show();
			$("#check_phone").html("");
			return true;
		} else {
			$("#check_phone").show();
			$("#check_phone").html("请输入正确的手机号码");
			$("#check_error").html("请输入正确的手机号码");
			return false;
		}
	} else {
		$("#check_phone").show();
		$("#check_phone").html("请输入手机号码");
		$("#check_error").html("请输入手机号码");
		return false;
	}
}

// 输入代办人的信息时 收件人的信息自动填入相同的值
function linkage(inputId) {
	if (inputId == "name") {
		$("#check_receiverName").hide();
		$("#check_receiverName").html("");
		$("#receiverName").val($("#" + inputId).val());
	} else if (inputId == "phone") {
		$("#check_receiverPhone").hide();
		$("#check_receiverPhone").html("");
		$("#check_phone").show();
		$("#receiverPhone").val($("#" + inputId).val());
		$("#check_phone").html("已输入" + $("#" + inputId).val().length + "位");
	}
}

// 验证收件人的姓名
function checkReceiverName() {
	var userName = $("#receiverName").val();
	userName = $.trim(userName); 
	if (userName.length == 0) {
		$("#check_receiverName").show();
		$("#check_receiverName").html("请输入收件人姓名");
		$("#check_error").html("请输入收件人姓名");
		return false;
	} else {
		$("#check_receiverName").hide();
		$("#check_receiverName").html("");
		return true;
	}
}

// 验证收件人的电话
function checkReceiverPhone() {
	var phone = $("#receiverPhone").val();
	  phone = $.trim(phone); 
	var filter = /^(1+\d{10})$/;
	if (phone.length != 0) {
		if (filter.test(phone)) {
			$("#check_receiverPhone").hide();
			$("#check_receiverPhone").html("");
			return true;
		} else {
			$("#check_receiverPhone").show();
			$("#check_receiverPhone").html("请输入正确的手机号码");
			$("#check_error").html("请输入正确的手机号码");
			return false;
		}
	} else {
		$("#check_receiverPhone").show();
		$("#check_receiverPhone").html("请输入收件人的手机号码");
		$("#check_error").html("请输入收件人的手机号码");
		return false;
	}
}

// 验证收件人地址
function checkReceiverAddress() {
	var address = $("#receiverAddress").val();
	address = $.trim(address); 
	if (address.length == 0) {
		$("#check_receiverAddress").show();
		$("#check_receiverAddress").html("请输入收件人地址");
		$("#check_error").html("请输入收件人地址");
		return false;
	} else {
		$("#check_receiverAddress").hide();
		$("#check_receiverAddress").html("");
		return true;
	}
}

// 验证收件人的邮政编码
function checkReceiverNumber() {
	var receiverNumber = $("#receiverNumber").val();
	receiverNumber = $.trim(receiverNumber); 
	if (receiverNumber.length == 0) {
		$("#check_receiverNumber").show();
		$("#check_receiverNumber").html("请输入收件人的邮政编码");
		$("#check_error").html("请输入收件人的邮政编码");
		return false;
	} else {
		$("#check_receiverNumber").hide();
		$("#check_receiverNumber").html("");
		return true;
	}
}

// 验证地税
function checkApplyFlag1() {
	 if ($("#privince").val() == 0) {
		alert("请选择地税管理部门所在市(州)");
		return false;
	} else if ($("#city").val() == 0) {
		alert("请选择地税管理部门所在区(县、分局)");
		return false;
	} else if (!checkManagementPlace()) {
		$("#check_error").show();
		return false;
	} else {
		$("#check_error").hide();
		return true;
	}
}

// 验证社保
function checkApplyFlag2() {
	var securityAddress = $("#securityAddress").val();
	if ($("#securityAddress").val() == 0) {
		alert("请选择参保区县");
		return false;
	} else if (!checkSecurityNumber()) {
		$("#check_error").show();
		return false;
	}else{
		$("#check_error").hide();
		return true;
	}
	
}

// 验证申请表
function checkApply() {
	var companyType = $("input[name='companyType_show']:checked").val();
	var organizationRegisterNumber = $("#organizationRegisterNumber").val();
	var receiverType = $("input[name='receiverType_show']:checked").val();
	var applicationPeriod = $("input[name='applicationPeriod_show']:checked").val();
	var exampleInputFile = $("#companyImage1").val();
	var exampleInputFile1 = $("#transactorImage1").val();
	if (checkCompanyName() && organizationInfoByOrganizationNumber()
			&& checkUserName() && checkDocumentNumber() && checkEmail()
			&& checkPhone() && checkReceiverName() && checkReceiverPhone()
			&& checkReceiverAddress() && checkReceiverNumber()) {
		$("#check_error").hide();
		if (typeof (applicationPeriod) == "undefined") {
			alert("请选择申请年限");
			return false;
		} else if (typeof (companyType) == "undefined") {
			alert("请选择单位类型");
			return false;
		} else if (organizationRegisterNumber.length == 0) {
			alert("请输入注册号");
			return false;
		} else if (exampleInputFile.length == 0) {
			alert("请上传单位电子证件");
			return false;
		} else if (exampleInputFile1.length == 0) {
			alert("请上传个人电子证件");
			return false;
		} else if (typeof (receiverType) == "undefined") {
			alert("请选择收件方式");
			return false;
		} else {
			$("#applicationPeriod").val(applicationPeriod);
			$('#orgIdtype').val(companyType);
			$("#receiverType").val(receiverType);
			return true;
		}
	}else{
		$("#check_error").show();
	
	}
}
// 提交
function formSubmit() {
	var applyFlag1 = $("#applyFlag1").val();// 是否有低保
	var applyFlag2 = $("#applyFlag1").val();// 是否有社保
	if (applyFlag1 == 1 && applyFlag2 == 1) {
		if (checkApplyFlag1() && checkApplyFlag2() && checkApply()) {
			$('#createNewApplication').submit();
		}
	} else if (applyFlag1 == 0 && applyFlag2 == 1) {
		if (checkApplyFlag2() && checkApply()) {
			$('#createNewApplication').submit();
		}
	} else if (applyFlag1 == 1 && applyFlag2 == 0) {
		if (checkApplyFlag1() && checkApply()) {
			$('#createNewApplication').submit();
		}
	} else {
		if (checkApply()) {
			$('#createNewApplication').submit();
		}
	}
}


function checkRenovate(){
	var companyType = $("input[name='companyType_show']:checked").val();
	var organizationRegisterNumber = $("#organizationRegisterNumber").val();
	var receiverType = $("input[name='receiverType_show']:checked").val();
	var applicationPeriod = $("input[name='applicationPeriod_show']:checked").val();
	var exampleInputFile = $("#companyImage1").val();
	var exampleInputFile1 = $("#transactorImage1").val();
	if (checkCompanyName() && organizationInfoByOrganizationNumber()
			&& checkUserName() && checkDocumentNumber() && checkEmail()
			&& checkPhone() && checkReceiverName() && checkReceiverPhone()
			&& checkReceiverAddress() && checkReceiverNumber()) {
		alert(true);
		$("#companyName1").val($("#companyNameId").val());
		$("#companyNumber1").val($("#organizationNumberPart").val());
		$("#transactorName1").val($("#name").val());
		$("#transactorDocumentType1").val($("#transactorDocumentType").val());
		$("#transactorDocumentNumber1").val($("#documentNumber").val());
		$("#transactorEmail1").val($("#email").val());
		$("#transactorPhone1").val($("#phone").val());
		$("#check_error").hide();
		if (typeof (applicationPeriod) == "undefined") {
			alert("请选择申请年限");
			return false;
		} else if (typeof (companyType) == "undefined") {
			alert("请选择单位类型");
			return false;
		} else if (organizationRegisterNumber.length == 0) {
			alert("请输入注册号");
			return false;
		} else if (exampleInputFile.length == 0) {
			alert("请上传单位电子证件");
			return false;
		} else if (exampleInputFile1.length == 0) {
			alert("请上传个人电子证件");
			return false;
		} else if (typeof (receiverType) == "undefined") {
			alert("请选择收件方式");
			return false;
		} else {
			$("#applicationPeriod").val(applicationPeriod);
			$('#orgIdtype').val(companyType);
			$("#receiverType").val(receiverType);
			return true;
		}
	}else{
		$("#check_error").show();
	
	}
	
	
	
	
}
