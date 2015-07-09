/* 验证参数是否是数字格式
 * 如果通过验证返回true,否则返回false  
 */
function isNum(obj){
	var id = obj.id + "Msg";
	var regu = /^[0-9]+$/;
	if(regu.test($(obj).val())){
		$("#"+id).html("");
	}else{
		$("#"+id).html("");
		$(obj).after("<span id='"+id+"' style='color:red'>格式错误!</span>");
		$(obj).val("");
	}
}

/* 验证参数是否为空
 * 如果为空返回true,否则返回false  
 */
function isNull(obj){
	if($(obj).val == ""){
		return true;
	}
	
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	return re.test($(obj).val);
}

/* 验证参数是否是email格式
 * 如果通过验证返回true,否则返回false  
 */
function isEmail(obj){
	var regu = /^[-_A-Za-z0-9]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
	return regu.test(email);
}

/* 验证参数是否是数字字母中文格式
 * 如果通过验证返回true,否则返回false  
 */
function isNumOrLetterOrChin(obj){
	var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";
	var id = obj.id + "Msg";
	var re = new RegExp(regu);
	if(re.test($(obj).val())){
		$("#"+id).html("");
	}else{
		$("#"+id).html("");
		$(obj).after("<span id='"+id+"' style='color:red'>格式错误!</span>");
		$(obj).val("");
	}
}

/* 验证参数是否是字母中文格式
 * 如果通过验证返回true,否则返回false  
 */
function isLetterOrChin(obj){
	var regu = "^[a-zA-Z\u4e00-\u9fa5]+$";
	var id = obj.id + "Msg";
	var re = new RegExp(regu);
	if(re.test($(obj).val())){
		$("#"+id).html("");
	}else{
		$("#"+id).html("");
		$(obj).after("<span id='"+id+"' style='color:red'>格式错误!</span>");
		$(obj).val("");
	}
}
