function light_font(tagName, light){
if(light){
$("#"+tagName).css("color", "black");
}else{
$("#"+tagName).css("color", "#a6a9ad");
}
} 


function username_on_focus(value, defaultValue){
var tagName = "j_username";
if(value==defaultValue){
$("#j_username").val("");
}
light_font("j_username", true);
}
function username_on_blur(value, defaultValue){
if(!value){
$("#j_username").val(defaultValue);
}
light_font("j_username", false);
} 


function password_on_focus(value, defaultValue){
	var tagName = "j_password";
	$("#j_passwordShow").hide();
	$("#j_password").show().focus();
	light_font("j_password", true);
}
function password_on_blur(value, defaultValue){
	if(!value){
	$("#j_password").hide();
	$("#j_passwordShow").show();
	} 
	light_font("j_password", false);
}