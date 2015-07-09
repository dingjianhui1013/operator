// JavaScript Document
$(document).ready(function(){
// 只能是字母和数字的验证     
  jQuery.validator.addMethod("istrnum", function(value, element) {     
    return this.optional(element) || (/^([a-zA-Z0-9]+)$/.test(value));     
  }, "必须是6-16位数字或字母！");    
  
 
	$("#form_xgpass").validate({
		
		rules: {
			    password: {
				required: true,
				minlength: 6
			},

			Newpassword: {
			required: true,
			minlength: 6,
			maxlength: 16,
			istrnum:true
			},
			Newpassword2: {
				required: true,
				minlength: 6,
				maxlength: 16,
				istrnum:true,
				equalTo: "#Newpassword"
			}
			
		},
		
		messages: {
			password: {
				required: '当前口令不能为空',
				minlength: '请至少输入6位口令'
			},
			Newpassword: {
				required: '新口令不能为空',
				minlength: '请输入6-16位的数字或字母！',
				maxlength: '请输入6-16位的数字或字母！',
				istrnum:'请输入6-16位的数字或字母！'
			},
			Newpassword2: {
				required: '确认新口令不能为空',
				minlength: '请输入6-16位的数字或字母！',
				maxlength: '请输入6-16位的数字或字母！',
				istrnum:'请输入6-16位的数字或字母！',				
				equalTo: '确认口令与新口令不一致',
				
			}
			
		},	
		
		errorElement: "em", //可以用其他标签，记住把样式也对应修改
		//success: function(label) {
			//label指向上面那个错误提示信息标签em
			//label.text(" ")				//清空错误提示消息
				//.addClass("success");	//加上自定义的success类
		//}

	  });
	  
  });


// function clearform(){  //重置表单函数
	//document.form1.password.value='';
	//document.form1.Newpassword.value='';
	//document.form1.Newpassword2.value='';
	//document.form1.password.focus();
	//return false;
//}