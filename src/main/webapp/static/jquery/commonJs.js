//各个业务界面重复的js很多 之后如果再有重复的js写到这里 或者有时间把之前那些js拿出来整合到这里

/**
 * 检验是否为手机号或座机号码
 * 
 * 单位联系电话用
 */
function checkContactMobil(obj,o){
		$("#"+o).hide();
		var mobil = $(obj).val();
		
		
		var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	    var ip = new RegExp(isPhone);
	    var im = new RegExp(isMob);
	    
	    if(ip.test(mobil)||im.test(mobil)){
	    	if($("#phonepro").text()!=""){
				$("#phonepro").hide();
			}
	    	
	        return true;
	    }
	    else{
	    	if($("#phonepro").text()!=""){

				return false; 
			}
	    	$("#companyMobile").after("<span id='phonepro' style='color:red'>请输入正确的号码</span>");
	        return false;
	    }
		
	}