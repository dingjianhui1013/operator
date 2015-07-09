
$(function () {
	$(".k_div>label").each(function(i) {
		
        $(this).click(function(){
				$(".key_con:visible").hide();
				$(".key_con").eq(i).show();
			});
    });
	//登录脚本
	
	$("#logTop>a").each(function(i){
		   
			$(this).click(function(){
				window.aa=$(this).attr("id");
				if($(this).attr("id")=="a0"){
					$("#a1").attr("class","a_menu2");
					$(this).attr("class","a_menu1");
				}
				if($(this).attr("id")=="a1"){
					$("#a0").attr("class","a_menu3");
					$(this).attr("class","a_menu4");
				}
				$(".c_lgbox:visible").hide();
				$(".c_lgbox").eq(i).show();
				
			});
		});
	
	//编辑表格脚本
	var tdzjText,tdydText,tdemailText,tddetailedr;
	var admtdzjText,admtdydText,admtdemailText;
	//编辑管理员信息
	$(".admedit").each(function(i) {
        $(this).click(function(){
			  //取得td里的值
			  admtdzjText=$("#admzjphone"+i).text();
			  $("#admzjphone"+i).empty();
			  admtdydText=$("#admydphone"+i).text();
			  $("#admydphone"+i).empty();
			  admtdemailText=$("#admemail"+i).text();
			  $("#admemail"+i).empty();
			  //创建input文本框
			  var zjinput=$("<input type='text' class='edittxt'/>");
			  var ydinput=$("<input type='text' class='edittxt'/>");
			  var emailinput=$("<input type='text' class='edittxt'/>");
			  //给文本框的value赋值，td里的值
			  zjinput.attr("value",admtdzjText);
			  ydinput.attr("value",admtdydText);
			  emailinput.attr("value",admtdemailText);
			  
			  //在页面插入文本框
			  $("#admzjphone"+i).append(zjinput);
			  $("#admydphone"+i).append(ydinput);
			  $("#admemail"+i).append(emailinput);
			  //隐藏按钮
			  $(this).css("display","none");
			  $(".admsave").eq(i).show();
			  $(".admcancel").eq(i).show();
			 
			});
    });
	//点击保存按钮
	$(".admsave").each(function(i) {
		$(this).click(function(){
				//取得文本框里的值
				var zjinputText=$("#admzjphone"+i).children("input").val();
				var ydinputText=$("#admydphone"+i).children("input").val();
				var emailinputText=$("#admemail"+i).children("input").val();
				//将文本框里的值赋值给td
				$("#admzjphone"+i).html(zjinputText);
				$("#admydphone"+i).html(ydinputText);
				$("#admemail"+i).html(emailinputText);
				//隐藏按钮
				$(this).css("display","none");
				$(".admedit").eq(i).show();
				$(".admcancel").eq(i).hide();
			});
    });
	
	//点击取消按钮
	$(".admcancel").each(function(i) {
			$(this).click(function(){
			//点击取消,把原来的td的值再赋值给td
			$("#admzjphone"+i).html(admtdzjText);
			$("#admydphone"+i).html(admtdydText);
			$("#admemail"+i).html(admtdemailText);
			//隐藏按钮
			$(this).css("display","none");
			$(".admsave").eq(i).hide();
			$(".admedit").eq(i).show();
		});
    });
	
	
	//点击编辑按钮
	$(".edit").each(function(i) {
        $(this).click(function(){
			  //取得td里的值
			  tdzjText=$("#zjphone"+i).text();
			  $("#zjphone"+i).empty();
			  tdydText=$("#ydphone"+i).text();
			  $("#ydphone"+i).empty();
			  tdemailText=$("#email"+i).text();
			  $("#email"+i).empty();
			  tddetailedr=$("#detailedr"+i).text();
			  $("#detailedr"+i).empty();
			  //创建input文本框
			  var zjinput=$("<input type='text' class='edittxt'/>");
			  var ydinput=$("<input type='text' class='edittxt'/>");
			  var emailinput=$("<input type='text' class='edittxt'/>");
			  var detailedrinput=$("<input type='text' class='edittxt'/>");
			  //给文本框的value赋值，td里的值
			  zjinput.attr("value",tdzjText);
			  ydinput.attr("value",tdydText);
			  emailinput.attr("value",tdemailText);
			  detailedrinput.attr("value",tddetailedr);
			  
			  //在页面插入文本框
			  $("#zjphone"+i).append(zjinput);
			  $("#ydphone"+i).append(ydinput);
			  $("#email"+i).append(emailinput);
			  $("#detailedr"+i).append(detailedrinput);
			  //隐藏按钮
			  $(this).css("display","none");
			  $(".save").eq(i).show();
			  $(".cancel").eq(i).show();
			 
			});
    });
	//点击保存按钮
	$(".save").each(function(i) {
		$(this).click(function(){
				//取得文本框里的值
				var zjinputText=$("#zjphone"+i).children("input").val();
				var ydinputText=$("#ydphone"+i).children("input").val();
				var emailinputText=$("#email"+i).children("input").val();
				var detailedrinputText=$("#detailedr"+i).children("input").val();
				//将文本框里的值赋值给td
				$("#zjphone"+i).html(zjinputText);
				$("#ydphone"+i).html(ydinputText);
				$("#email"+i).html(emailinputText);
				$("#detailedr"+i).html(detailedrinputText);
				//隐藏按钮
				$(this).css("display","none");
				$(".edit").eq(i).show();
				$(".cancel").eq(i).hide();
			});
    });
	//点击取消按钮
	$(".cancel").each(function(i) {
			$(this).click(function(){
			//点击取消,把原来的td的值再赋值给td
			$("#zjphone"+i).html(tdzjText);
			$("#ydphone"+i).html(tdydText);
			$("#email"+i).html(tdemailText);
			$("#detailedr"+i).html(tddetailedr);
			//隐藏按钮
			$(this).css("display","none");
			$(".save").eq(i).hide();
			$(".edit").eq(i).show();
		});
    });
	//编辑表格脚本结束
	
	//左边导航脚本left.html
	$(".active").each(function (i) {
        $(this).click(function () {
            if ($(this).children("a").size() == 0) {
                if ($(this).find("i").attr("class") == "navi") {
                	//改变图标状态
                	$("#side-nav").find("i").removeClass("navi_down").addClass("navi");
                	$(this).find("i").removeClass("navi").addClass("navi_down");
                	$("#side-nav").find(".child").slideUp("50");
                    $(this).next(".child").slideDown("100");
                } else {
                	$(this).find("i").removeClass("navi_down").addClass("navi");
                    $(this).next(".child").slideUp("300");
                }
            }
        });
        $(this).find("a").click(function () {
            $("li[active_link='true']").removeAttr("active_link");
            $.cookie("active_menu_id", null, {path:"/"});
            $.cookie("active_menu_title", $(this).parent().attr("id"), {path:"/"});
        });
    });
		
	$(".child_c").each(function () {
        $(this).click(function () {
            if ($(this).find("i").attr("class") == "navchile_i") {
//            	$("#side-nav").find(".child").slideUp("fast");
            	$(this).next(".child_c_cbox").slideDown("fast");
                $(this).find("i").removeClass("navchile_i").addClass("navchile_i_down");
            } else {
                $(this).next(".child_c_cbox").slideUp("fast");
                $(this).find("i").removeClass("navchile_i_down").addClass("navchile_i");
            }

        });
    });

    $("li.child_c_no").each(function (i) {
        $(this).children("a").click(function () {
            $("li[active_link='true']").removeAttr("active_link");
            $(this).parent().css("background", "#eef6fd").attr("active_link", "true");
            $.cookie("active_menu_id", $(this).parent().attr("id"), {path:"/"});
            $.cookie("active_menu_title", null, {path:"/"});
        });
        $(this).mouseover(
            function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#eeeeee");
            }).mouseout(function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#f7f7f6");
            });
    });
    
    $("li.child_c").each(function (i) {
        $(this).mouseover(
            function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#eeeeee");
            }).mouseout(function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#f7f7f6");
            });
    });
    
    $("li.child_c_c").each(function (i) {
        $(this).children("a").click(function () {
            $("li[active_link='true']").removeAttr("active_link");
            $(this).parent().css("background", "#eef6fd").attr("active_link", "true");
            $.cookie("active_menu_id", $(this).parent().attr("id"), {path:"/"});
            $.cookie("active_menu_title", null, {path:"/"});
        });
        $(this).mouseover(
            function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#eeeeee");
            }).mouseout(function () {
                if ($(this).attr("active_link") != "true")
                    $(this).css("background", "#f7f7f6");
            });
    });
	
	var active_menu_id = $.cookie("active_menu_id");
    if (active_menu_id) {
        var parentTop = $("#" + active_menu_id).parents("li.child").prev();
        parentTop.find("i").removeClass("navi").addClass("navi_down");
        parentTop.next(".child").show();
        var parentSub = $("#" + active_menu_id).parents("li.child_c_cbox").prev();
        parentSub.find("i").removeClass("navchile_i").addClass("navchile_i_down");
        parentSub.next(".child_c_cbox").show();
        $("#" + active_menu_id).css("background", "#14B9D6").attr("active_link", "true");
    }
    var active_menu_title = $.cookie("active_menu_title");
    if (active_menu_title) {
        $("#" + active_menu_title).find("i").removeClass("navi").addClass("navi_down");
        $("#" + active_menu_title).next(".child").show();
    }
	//左边导航脚本left.html结束
	
		//表格脚本
		$(".table1>tbody>tr:odd").addClass("oddColor");
		$(".table1>tbody>tr").hover(
		function(){
			$(this).addClass("overColor");
		},
		function(){
			$(this).removeClass("overColor");
		});
		//tab1页脚本	
		$(".tabCon:first").show();
		$(".div_a_R:first").show();
		$(".nav_span:first").show();
		$(".myTab>li").each(function(i){
			$(this).click(function(){
				$(".tabCon:visible").hide();
				$(".div_a_R:visible").hide();
				$(".nav_span:visible").hide();
				$(".div_a_R").eq(i).show();
				$(".nav_span").eq(i).show();
				$(".tab1").attr("class","tab2");
				$(this).attr("class","tab1");
				$(".tabCon").eq(i).show();
			
			});
		});
		$(".mboxR_tab_L>p").each(function(i) {
			$(this).click(function(){
				$(".tabCon:visible").hide();
				$(".tab1").attr("class","tab2");
				$(this).attr("class","tab1");
				$(".tabCon").eq(i).show();
				});
            
        });
		
		//弹出
	});
	
	
function center(obj){
var windowWidth = document.documentElement.clientWidth;
var windowHeight = document.documentElement.clientHeight;
var popupHeight = $(obj).height();   
var popupWidth = $(obj).width();
$(obj).css({   
	 "position":"fixed",
	 "top":(windowHeight-popupHeight)/2,//可自定义top的值，离窗口底部的距离”
	 "left":(windowWidth-popupWidth)/2,
	 "_left":(windowWidth-popupWidth)/2,
	 "_position":"absolute",
     "_top":(windowHeight-popupHeight)/2//可自定义top的值，离窗口底部的距离”
 });
}


function tankDiv(eleId){
var ele = "#"+eleId;
$(ele).show();
center(ele);
var cH=document.documentElement.scrollHeight;
var cW=document.documentElement.scrollWidth;
$(window).scroll(function(){
  center(ele);
});
$(window).resize(function(){
 center(ele);
}); 
}
function showDiv(eleId){
var ele = "#"+eleId;
$(ele).show();
centerTooHeight(ele);
var cH=document.documentElement.scrollHeight;
var cW=document.documentElement.scrollWidth;
$(window).scroll(function(){
centerTooHeight(ele);
});
$(window).resize(function(){
centerTooHeight(ele);
});
}

function centerTooHeight(obj){
var windowWidth = document.documentElement.clientWidth;
var windowHeight = document.documentElement.clientHeight;
var popupHeight = $(obj).height();
var popupWidth = $(obj).width();
$(obj).css({
"position": "absolute",
"top": (windowHeight-popupHeight)/2+80,
"left": (windowWidth-popupWidth)/2
//"left": (windowWidth-popupWidth)/2-35
});
} 
function closeTank(id){
		var indexId=id;
		$('#' + indexId).hide();
		//$('#mask').hide();
}