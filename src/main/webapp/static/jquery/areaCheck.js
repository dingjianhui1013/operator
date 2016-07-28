/*******************************************************************************
*
*	省，市，地区三级菜单联动选择，目前仅支持3级联动选择
*	创建时间:2014-1-22
*   最后修改时间:2013-1-22
*   最后修改人:zhou_jiang
*   
*   使用说明：
*   1. 引入js文件:
*     <script type="text/javascript" src="areaCheck.js"></script>
*   2.在页面加载完成后，调用下面JS来初始化控件
*     new areaSelectCheck("http://localhost:8080/testSSH2","地税","privinceId","cityId","townId","treeId","areaTreeCheck"); 
*     
*     对应参数说明:
*     (1)http://localhost:8080/testSSH2      该地址为访问地区选择的地址
*     (2)地税                                该参数为区别数据结构，对应webArea.DataStandard字段数据，方便选择时，只查询对应的数据进行选择，如地税
*     (3)privinceId                          主页面上隐藏字段的ID，代表第一级数据，即省。用户在主页面选择地址后，将自动把数据回填到主页面隐藏字段中
*     (4)cityId								 主页面上隐藏字段的ID，代表第二级数据，即市。用户在主页面选择地址后，将自动把数据回填到主页面隐藏字段中
*     (5)townId								 主页面上隐藏字段的ID，代表第三级数据，即县/城镇。用户在主页面选择地址后，将自动把数据回填到主页面隐藏字段中
*     (6)treeId								 该数据为主页面上隐藏字段的ID，主要用于在修改数据时，将最后一级数据的值填入到该隐藏控件中，这样就能完成修改数据的加载
*     														因此修改时，treeId这个控件的值一定要存在，然后再初始化控件。
*     (7)areaTreeCheck						 主页面上的控件ID，指定地址选择框在主页面上显示的位置。最终地址选择框会直接在该控件内部填充。
*
*
*
*     注意:以上操作请确认已经引入jquery,目前已知适配的版本jquery-1.9.1.js
 ******************************************************************************/


// 访问基本路径
var urlBase = "";
//查询数据   地税 or ""
var queryData = "";
//回填省份数据ID
var provinceId = "";
//回填城市数据ID
var cityId = "";
//回填县数据ID
var townId = "";
//最后一级数据ID
var cityCode = "";
/**
 *  引入JS文件
 * @param path
 */
function includeJs(path){
      var sobj = document.createElement('script');   
      sobj.type = "text/javascript";   
      sobj.src = path;
      var headobj = document.getElementsByTagName('head')[0];   
      headobj.appendChild(sobj);
}

/**
 *  引入CSS文件
 * @param path
 */
function includeCss(path){
	var fileref=document.createElement("link");
    fileref.rel = "stylesheet";
    fileref.type = "text/css";
    fileref.href = path;
    var headobj = document.getElementsByTagName('head')[0];   
    headobj.appendChild(fileref);
}

/**
 *  创建地区选择
 * @param urlBase
 * @param queryData
 * @param provinceId
 * @param cityId
 * @param townId
 * @param cityCode
 * @returns
 */
function areaSelectCheck(a_urlBase,a_queryData,a_provinceId,a_cityId,a_townId,a_cityCode,a_displayId){
	if(a_urlBase !=""){
		a_urlBase = a_urlBase + "/";
	}
	urlBase = a_urlBase;
	queryData = a_queryData;
	provinceId = a_provinceId;
	cityId = a_cityId;
	townId = a_townId;
	cityCode = a_cityCode;
	includeCss(urlBase + "resources/themes/areaProduct.css");
	
	//选择语句
	var selectHtml = '<div id="product-intro"><ul id="summary" class="dispatching"><li id="summary-stock" class="deliveryItem">' + 
					 '<div class="dd">'+
					 '<div id="store-selector" class=""><div class="text"><div>请选择</div><b></b></div>' + 
					 '<div class="content"><span class="clr"></span></div>' + 
					 '<div class="close" onclick="$(\'#store-selector\').removeClass(\'hover\')"></div>' + 
					 '</div><div id="store-prompt"></div></div></li></ul>' + 
					 '<input type="hidden" id="selectVal"/><input type="hidden" id="selectCode"/>' + 
					 '</div>';
	$("#" + a_displayId).html(selectHtml);
	
	
}



/*
Lib-v1.js 
Date: 2013-07-23 
*/

"object" != typeof JSON && (JSON = {}),
function($) {
   $.extend({
       _jsonp: {
           scripts: {},
           counter: 1,
           charset: "gb2312",
           head: document.getElementsByTagName("head")[0],
           name: function(callback) {
               var name = "_jsonp_" + (new Date).getTime() + "_" + this.counter;
               this.counter++;
               var cb = function(json) {
                   eval("delete " + name),
                   callback(json),
                   $._jsonp.head.removeChild($._jsonp.scripts[name]),
                   delete $._jsonp.scripts[name]
               };
               return eval(name + " = cb"),
               name
           },
           load: function(t, e) {
               var i = document.createElement("script");
               i.type = "text/javascript",
               i.charset = this.charset,
               i.src = t,
               this.head.appendChild(i),
               this.scripts[e] = i
           }
       },
       getJSONP: function(t, e) {
           var i = $._jsonp.name(e),
           t = t.replace(/{callback};/, i);
           return $._jsonp.load(t, i),
           this
       }
   })
} (jQuery),
function(t) {
   t.fn.Jdropdown = function(e, i) {
       if (this.length) {
           "function" == typeof e && (i = e, e = {});
           var n = t.extend({
               event: "mouseover",
               current: "hover",
               delay: 0
           },
           e || {}),
           a = "mouseover" == n.event ? "mouseout": "mouseleave";
           t.each(this, 
           function() {
               var e = null,
               r = null,
               o = !1;
               t(this).bind(n.event, 
               function() {
                   if (o) clearTimeout(r);
                   else {
                       var a = t(this);
                       e = setTimeout(function() {
                           a.addClass(n.current),
                           o = !0,
                           i && i(a)
                       },
                       n.delay)
                   }
               }).bind(a, 
               function() {
                   if (o) {
                       var i = t(this);
                       r = setTimeout(function() {
                           i.removeClass(n.current),
                           o = !1
                       },
                       n.delay)
                   } else clearTimeout(e)
               })
           })
       }
   }
} (jQuery)

























var iplocation = {};


var provinceCityJson = "{}";

var provinceAreaJson = "{}";

var provinceTownJson = {};

var msg = "";

window.stokeAddress=function(config){
	var _this=this;
	config=config||{};
	this.msg=config.msg;
	this.renderTo="#store-selector .content .clr";
	this.selectTab="#JD-stock .tab li";
	this.selectContent="#JD-stock div[data-widget='tab-content']";
	this.currentPageLoad =config.currentPageLoad||{"isLoad":true,"areaCookie":[1,72,0,0]}
	this.areaInfo={
		"currentLevel": 1,
	    "currentProvinceId": 1,"currentProvinceName":"",
		"currentCityId": 0,"currentCityName":"",
		"currentAreaId": 0,"currentAreaName":"",
		"currentTownId":0,"currentTownName":""
	};
	this.requestLevel = 3; //最少三级菜单
	this.init(config);	  //html
	this.getInitArea();//初始化地址
  };
  
stokeAddress.prototype={
	init:function(config){
	  var _this=this;
	  this.el=$(document.createElement('div'));
	  var html='<div data-widget="tabs" class="m JD-stock" id="JD-stock">'
		   +'<div class="mt">'
		   +' <ul class="tab">'
		   +' <li data-index="0" data-widget="tab-item" class="curr"><a href="#" onclick="return false;" class="hover"><em>请选择</em><i></i></a></li>'
		   +' <li data-index="1" data-widget="tab-item" style="display:none;"><a href="#" onclick="return false;" class=""><em>请选择</em><i></i></a></li>'
		   +'<li data-index="2" data-widget="tab-item" style="display:none;"><a href="#" onclick="return false;" class=""><em>请选择</em><i></i></a></li>'
		   +'<li data-index="3" data-widget="tab-item" style="display:none;"><a href="#" onclick="return false;" class=""><em>请选择</em><i></i></a></li>'
		   +' </ul>'
		   +'<div class="stock-line"></div>'
		   +'</div>'
		   +'<div class="mc" data-area="0" data-widget="tab-content" id="stock_province_item"></div>'
		   +'<div class="mc" data-area="1" data-widget="tab-content" id="stock_city_item"></div>'
		   +'<div class="mc" data-area="2" data-widget="tab-content" id="stock_area_item"></div>'
		   +'<div class="mc" data-area="3" data-widget="tab-content" id="stock_town_item"></div>'
		   +'</div>';
	  this.el.html(html);
	  this.el.find("#stock_province_item").html(this.msg)
	  this.el.insertBefore(this.renderTo);
	 $(_this.selectTab).bind('click',function(){
		var level = $(this).attr("data-index");
		level = new Number(level);
		$(_this.selectTab).removeClass("curr").eq(level).addClass("curr");
		$(_this.selectTab).find("a").removeClass("hover").eq(level).addClass("hover");
		$(_this.selectContent).hide().eq(level).show();
	  });
	 $("#stock_province_item a").unbind("click").click(function(config) {	
		_this.currentPageLoad.isLoad = false;
		try{
			
			//选在省级时，将数据清空
			_this.areaInfo.currentProvinceName = "";
			_this.areaInfo.currentCityName="";
			_this.areaInfo.currentAreaName="";
			_this.areaInfo.currentTownName="";
			
			
			_this.areaInfo.currentProvinceId = $(this).attr("data-value");
			_this.areaInfo.currentProvinceName = $(this).html();
			$(_this.selectTab).eq(0).find("em").html(_this.areaInfo.currentProvinceName);
			 stokeAddress.prototype.GetNextAreas(_this,_this.areaInfo.currentProvinceId,_this.areaInfo.currentCityId,_this.areaInfo.currentAreaId,_this.areaInfo.currentTownId,1);  //获取下一级
		 }catch (err){
		   alert(err)
		  }
	}).end();
	$("#store-selector .close").unbind("click").bind("click",function(){
		
	});
	
	//初始化数据来源
	//网络获取数据  
	
	$.ajax({
		   type: "GET",
		   url: urlBase + "getProvinceData.do",
		   data: "datastandard=" + encodeURIComponent(queryData),
		   async:false,
		   success:function(data){
			   var jsonData = eval("(" + data + ")");
			   iplocation = jsonData;
			   console.log("iplocation:" + iplocation);
		   }
		});
	
	$.ajax({
		   type: "GET",
		   url: urlBase + "getCityData.do",
		   data: "datastandard=" + encodeURIComponent(queryData),
		   async:false,
		   success: function(data){
			   
			   var jsonData = eval("(" + data + ")");
			   provinceCityJson = jsonData;
			   console.log("provinceCityJson:" + provinceCityJson);
		   }
		});

	
	$.ajax({
		   type: "GET",
		   url: urlBase + "getTownData.do",
		   data: "datastandard=" + encodeURIComponent(queryData),
		   async:false,
		   success: function(data){
			  
			   var jsonData =eval("(" + data + ")");
			   provinceAreaJson = jsonData;
			   console.log("provinceAreaJson:" + provinceAreaJson);
		   }
		});
	
	
	
  },
getInitArea:function(ipLoc){  //初始化配送地址
	
   var _this=this;
  /* if(!ipLoc){
	ipLoc = stokeAddress.prototype.getCookie("ipLoc-djd");
    }*/
	ipLoc = ipLoc?ipLoc.split("-"):_this.currentPageLoad.areaCookie;
	
	_this.areaInfo.currentProvinceId=ipLoc[0];
	_this.areaInfo.currentCityId=ipLoc[1];
	_this.areaInfo.currentAreaId=ipLoc[2];
	_this.areaInfo.currentTownId=ipLoc[3];
   _this.areaInfo.currentProvinceName = stokeAddress.prototype.getNameById(ipLoc[0]);
	currentLocation = _this.areaInfo.currentProvinceName;
	var province = iplocation[currentLocation];	
//	_this.areaInfo.currentProvinceId = province.id;
	$(_this.selectTab).eq(0).find("em").html(_this.areaInfo.currentProvinceName);   //填充省	
	stokeAddress.prototype.GetNextAreas(_this,ipLoc[0],ipLoc[1],ipLoc[2],ipLoc[3],1);  //获取下一级
},
GetNextAreas:function(_this,ProvinceId,AreaInfo,CityId,AreaId,level){	 //获取下一级

	var fids=arguments[level];
  if (level == _this.requestLevel){
	  $(_this.selectTab).removeClass("curr").eq(level-1).addClass("curr");
	 $(_this.selectTab).find("a").removeClass("hover").eq(level-1).addClass("hover");
	 var fid=arguments[level];
	 
	 
	if(level == 3 && provinceTownJson[fid+""]){	      //第3级并且有子级点
	  stokeAddress.prototype.getStockCallback(_this,fid,level);
	} else{
	  stokeAddress.prototype.getProvinceStockCallback(_this,ProvinceId,AreaInfo,CityId,AreaId,level); //级数结束 赋值文本框
	}
   } 
	
	
	
  
   //判断下一级时，需要先判断下一级是否存在数据
  if(level < _this.requestLevel && (provinceCityJson[fids+""] ||provinceAreaJson[fids+""] )){ //还需要获取下级地址
	_this.currentLevel = level +1;
	$(_this.selectTab).removeClass("curr").eq(level).addClass("curr");
	$(_this.selectTab).find("a").removeClass("hover").eq(level).addClass("hover");
	 stokeAddress.prototype.getChildAreaHtml(_this,arguments[level],level +1);
  }else{
	//下一级没有数据，直接填充数据到文本框
	stokeAddress.prototype.getProvinceStockCallback(_this,ProvinceId,AreaInfo,CityId,AreaId,level); //级数结束 赋值文本框
  }
},
getStockCallback:function(_this,fid,level){
	_this.requestLevel = 4;
	stokeAddress.prototype.getAreaList_callback(_this,provinceTownJson[fid+""],level+1);
	},
	
getProvinceStockCallback:function(_this,ProvinceId,AreaInfo,CityId,AreaId,level){
	if(_this.currentLevel==4){
	  for (var i=level,j=$(_this.selectTab).length;i<j;i++){
		$(_this.selectTab).eq(i).hide();
		$(_this.selectContent).eq(i).hide();
	  }
	}
	
	
    var address = _this.areaInfo.currentProvinceName+_this.areaInfo.currentCityName+_this.areaInfo.currentAreaName+_this.areaInfo.currentTownName;
    $("#selectVal").val(_this.areaInfo.currentProvinceName + "   " + _this.areaInfo.currentCityName + "   " + _this.areaInfo.currentAreaName);
    
    try{
    	$("#" + provinceId).val(_this.areaInfo.currentProvinceName);
        $("#" + cityId).val( _this.areaInfo.currentCityName);
        $("#" + townId).val(_this.areaInfo.currentAreaName);
        
        if(_this.areaInfo.currentAreaId){
        	$("#" + cityCode).val( _this.areaInfo.currentAreaId);	
        }else if(_this.areaInfo.currentCityId){
        	$("#" + cityCode).val( _this.areaInfo.currentCityId);
        }else{
        	$("#" + cityCode).val( _this.areaInfo.currentProvinceId);
        }
    }catch(ex){
    	//忽略错误 
    }
    
    
    
	$("#store-selector .text div").html(address).attr("title",address);
	 var adds = _this.areaInfo.currentProvinceId+"-"+_this.areaInfo.currentCityId+"-"+_this.areaInfo.currentAreaId+"-"+_this.areaInfo.currentTownId;
	 $("#selectCode").val(_this.areaInfo.currentProvinceId + "   " + _this.areaInfo.currentCityId + "   " + _this.areaInfo.currentAreaId);
	 
	 
	 
	stokeAddress.prototype.reBindStockEvent();  //文本框去掉hover样式

},
getChildAreaHtml:function(config,fid,level){
	
	
  var idName = stokeAddress.prototype.getIdNameByLevel(level);
  if (idName){
	
	//添加这个判断是为了防止下一级没有数据时，就不需要添加选择框  2013-12-26  Jack
	if(provinceCityJson[fid+""] || provinceAreaJson[fid+""]){
  
		$("#stock_province_item,#stock_city_item,#stock_area_item,#stock_town_item").hide();
		$("#"+idName).show().html("<div class='iloading'>正在加载中，请稍候...</div>");
		$(config.selectTab).show().removeClass("curr").eq(level-1).addClass("curr").find("em").html("请选择");
		
		for (var i=level,j=$(config.selectTab).length;i<j ;i++ ){
		  $(config.selectTab).eq(i).hide();
		}
	
	}
	
	if(level == 2 && provinceCityJson[fid+""]){	
		
	   stokeAddress.prototype.getAreaList_callback(config,provinceCityJson[fid+""],level);
		
		
	}else{
	  config.areaInfo.currentFid = fid;
	  
	  
	  
	  if(level == 3 && provinceAreaJson[fid+""]){	
		stokeAddress.prototype.getAreaList_callback(config,provinceAreaJson[fid+""],level);
	  }else{
	  
		stokeAddress.prototype.getAreaList_callback(config,provinceTownJson[fid+""],level);
		//http://d.360buy.com/area/get?fid=17&callback=stokeAddress.prototype.getAreaList_callback
           //$.getJSONP("http://d.360buy.com/area/get?fid="+fid+"&callback=stokeAddress.prototype.getAreaList_callback");  //有地址的时候在使用，先写死      
		}
	}
   }
},




getAreaList_callback:function(config,result,level){  //下级地址回调	
	config.currentLevel= level;
	stokeAddress.prototype.getAreaList(config,result,stokeAddress.prototype.getIdNameByLevel(level),level);
  },
getAreaList:function(config,result,idName,level){		
  if (idName && level){
  $("#"+idName).html("");
  var html = "<ul class='area-list'>";
  var longhtml = "";
  var longerhtml = "";
  if (result&&result.length > 0){
	for (var i=0,j=result.length;i<j ;i++ ){
		
		
	  result[i].name = result[i].name.replace(" ","");
	  if(result[i].name.length > 12){
		longerhtml += "<li class='longer-area'><a href='#' onclick='return false;' data-value='"+result[i].id+"'>"+result[i].name+"</a></li>";
	  }else if(result[i].name.length > 5){
		longhtml += "<li class='long-area'><a href='#' onclick='return false;' data-value='"+result[i].id+"'>"+result[i].name+"</a></li>";
	  }else{
		html += "<li><a href='#' onclick='return false;' data-value='"+result[i].id+"'>"+result[i].name+"</a></li>";
	  }
	}
  }else{
	html += "<li><a href='#' onclick='return false;' data-value='"+config.areaInfo.currentFid+"'> </a></li>";
  }
  html +=longhtml+longerhtml+"</ul>";
  $("#"+idName).html(html);
  
  $("#"+idName).find("a").click(function(){
	var areaId = $(this).attr("data-value");
	
	
	
	var areaName = $(this).html();
	var level = $(this).parent().parent().parent().attr("data-area");
	$(config.selectTab).eq(level).find("a").attr("title",areaName).find("em").html(areaName.length>6?areaName.substring(0,6):areaName);
	level = new Number(level)+1;
	if (level=="2"){
	  config.areaInfo.currentCityId = areaId;
	  config.areaInfo.currentCityName = areaName;
	  config.areaInfo.currentAreaId = 0;
	  config.areaInfo.currentAreaName = "";
	  config.areaInfo.currentTownId = 0;
	  config.areaInfo.currentTownName = "";
	}
	else if (level=="3"){
	  if (config.requestLevel == 4 && config.areaInfo.currentAreaId != areaId){
		config.requestLevel = 3;
	  }
		config.areaInfo.currentAreaId = areaId;
		config.areaInfo.currentAreaName = areaName;
		config.areaInfo.currentTownId = 0;
		config.areaInfo.currentTownName = "";
	}
	else if (level=="4"){
		config.areaInfo.currentTownId = areaId;
		config.areaInfo.currentTownName = areaName;
	}
	currentLocation = config.areaInfo.currentProvinceName;
	
	
	
	stokeAddress.prototype.GetNextAreas(config,config.areaInfo.currentProvinceId,config.areaInfo.currentCityId,config.areaInfo.currentAreaId,config.areaInfo.currentTownId,level);  //获取下一级
   });
		//页面初次加载
   if (config.currentPageLoad.isLoad){
	var tempDom = $("#"+idName+" a[data-value='"+config.currentPageLoad.areaCookie[level-1]+"']");
	if(config.currentPageLoad.areaCookie&&config.currentPageLoad.areaCookie[level-1]&&config.currentPageLoad.areaCookie[level-1].length>0&&tempDom.length>0){
		//本地cookie有该级地区ID
		tempDom.click();
	}else{
		$("#"+idName+" a:first").click();
	}
  }		
  }
},
getIdNameByLevel:function(level){
	var idName = "";
	if (level == 1){
		idName = "stock_province_item";
	}
	else if (level == 2){
		idName = "stock_city_item";
	}
	else if (level == 3){
		idName = "stock_area_item";
	}
	else if (level == 4){
		idName = "stock_town_item";
	}
	return idName;
   },
reBindStockEvent:function(){

				$("#store-selector").removeClass("hover");},
				getCookie: function(name) { 
					var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
					if (arr != null) return unescape(arr[2]);
					return null;
				  }, 
				 setCookie: function(name, value, expires, path, domain, secure) {
					var today = new Date(); 
					today.setTime(today.getTime());
					if (expires) expires = expires * 1000 * 60 * 60 * 24; 
					var expires_date = new Date(today.getTime() + (expires)); 
					document.cookie = name + '=' + escape(value) + ((expires) ? ';expires=' + expires_date.toGMTString() : '') + ((path) ? ';path=' + path : '') + ((domain) ? ';domain=' + domain : '') + ((secure) ? ';secure' : ''); 
				},	
getNameById:function(provinceId){
	for(var o in iplocation){
		if (iplocation[o]&&iplocation[o].id==provinceId){
			return o;
		}
	}
	return "请选择";
}
	};


$(function(){
	
	$("#store-selector").Jdropdown();
	var msg = "";
	var editJson = "";
	$.ajax({
		   type: "GET",
		   url: urlBase + "getMsgData.do?datastandard=" + encodeURIComponent(queryData),
		   async:false,
		   success:function(data){
			   msg = data;
		   }
		});
	
	$.ajax({
		   type: "GET",
		   url: urlBase + "getEditData.do",
		   data:"treeId=" + $("#" + cityCode).val(),
		   //data:"oper=getEditData&treeId=510183",
		   async:false,
		   success:function(data){
			   var jsonData = eval("("+data+")");
			   editJson = jsonData;
			   
		   }
		});
	 var privince=new stokeAddress({
		 msg:"<ul class='area-list'>" + msg + "</ul>",
		 currentPageLoad:{"isLoad":true,"areaCookie":[editJson.provinId,editJson.cityId,editJson.townId]}
	   });
	});


