<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>业务办理管理</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/jquery/jquery.bigautocomplete.css"
	rel="stylesheet" />
<style type="text/css">

.zoominner {background: none repeat scroll 0 0 #FFFFFF; padding: 5px 10px 10px; text-align: left;}
/* .zoominner p {height:30px; _position:absolute; _right:2px; _top:5px;}
.zoominner p a { /* background: url("../images/imgzoom_tb.gif") no-repeat scroll 0 0 transparent;  float: left; height: 17px; line-height: 100px; margin-left: 10px;  overflow: hidden;  width: 17px;}
.zoominner p a.imgadjust {background-position: -40px 0;} */
.zoominner a.imgclose{ cursor:pointer;position:absolute;z-index:9999;right:-6px; top:-6px; color:#333; font-size:30px; display:block;}
.zoominner a.imgclose:hover{text-decoration:none;}
.y {float: right; margin-bottom:10px;}
.ctnlist .text img{ cursor:pointer;}
#imgzoom_cover{background-color:#000000; filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=70); opacity:0.7; position:absolute; z-index:800; top:0px; left: 0px; width:100%; display:none;}
#imgzoom{ display:none; z-index:801; position:absolute;}
#imgzoom_img{_width:300px; _height:200px; width:700px; height:600px; background:url(../images/imageloading.gif) center center no-repeat;}
#imgzoom_zoomlayer{ _width:300px; _height:200px; _position:relative; _padding-top:30px; min-width:300px; min-height:200px; padding:17px;}

.imgLayerBox{margin-bottom:20px;overflow:hidden;}
.uploadImgList{ float:left; border:1px solid #ddd;margin-right:10px; padding:2px; position:relative}
.uploadImgName{ text-align:center; font-size:12px; font-weight:bold; margin-top:4px; height:22px; line-height:22px;border-top:1px solid #ddd;margin-bottom:0px;}
.smBtn{width:100px; height:20px;}
.btnGrop{margin-bottom:5px;}
.s-closeBtn{ position:absolute; right:-4px; top:0px; font-size:20px; cursor:pointer;}

</style>
<script type="text/javascript" src="${ctxStatic}/jquery/commonJs.js"></script>
<script type="text/javascript">
	$(document).ready(
		function() {
			$('div.small_pic a').fancyZoom({scaleImg: true, closeOnClick: true});
			//图片的显示
			if("${imgNames}"!=null && "${imgNames}"!=""){
				var imgNames = "${imgNames}";
				var str1 = new Array();                      
				str1 = imgNames.split(",");  
		
				var namestr = "";
				for(var i = 0;i < str1.length; i++){
					var str = $("<div class='uploadImgList'><img src='"+str1[i]+"' style='width: 100px; height: 80px;'>"+'<p class="uploadImgName">'+getDisplayName(str1[i])+'</p></div>');
					$("#imgLayer").append(str);
					var imgBoxMod=$(".ctnlist .text img");
					imgPop1(imgBoxMod);
				    //imgDel(str);
				    namestr+=str1[i].substring(str1[i].lastIndexOf('/')+1,str1[i].length)+",";
				}
				if(namestr!=''){
					$("#imgNames").val(namestr.substring(0,namestr.length-1));
				}
			}
			
	        //关闭按钮
			$("#append_parent .imgclose").live('click',function(){
			    $("#imgzoom").css("display","none");
			    $("#imgzoom_cover").css("display","none");
			})
	});
	
	//内容页图片点击放大效果函数主体开始
	function imgPop1(imgBoxMod){
	    imgBoxMod.each(function(){
		    //超过最大尺寸时自动缩放内容页图片尺寸
		    var ctnImgWidth=$(this).width();
		    if(ctnImgWidth>618){
		            $(this).width(618);
		        }
		    //点击图片弹出层放大图片效果
		    $(this).click( function(){
		    	
		    	var str = "<div id='imgzoom'>"
		    				+"<div id='imgzoom_zoomlayer' class='zoominner'>"
	//	    					+"<p><span class='y'>"
		    						+"<a title='关闭' class='imgclose'><span class='icon-remove-sign'></span></a>"
	//	    					+"</span></p>"
		    					+"<div id='imgzoom_img' class='hm'>"
		    					+"<img src='' id='imgzoom_zoom' style='cursor:pointer'>"
		    					+"</div>"
		    				+"</div>"
		    			   +"</div>"
		    			   +"<div id='imgzoom_cover'></div>";
		    	
		        $("#append_parent").html(str); //生成HTML代码
		        var domHeight =$(document).height(); //文档区域高度
		        $("#imgzoom_cover").css({"display":"block","height":domHeight});
		        var imgLink=$(this).attr("src");
		        $("#imgzoom_img #imgzoom_zoom").attr("src",imgLink);
		        $("#imgzoom").css("display","block");
		        imgboxPlace1();
		        })
		})
	}
	
	//弹出窗口位置
	function imgboxPlace1(){
	    var cwinwidth=$("#imgzoom").width();
	    var cwinheight=$("#imgzoom").height();
	    var browserwidth =$(window).width();//窗口可视区域宽度
	    var browserheight =$(window).height(); //窗口可视区域高度
	    var scrollLeft=$(window).scrollLeft(); //滚动条的当前左边界值
	    var scrollTop=$(window).scrollTop(); //滚动条的当前上边界值 
	    var imgload_left=scrollLeft+(browserwidth-cwinwidth)/2;
	    var imgload_top=scrollTop+(browserheight-cwinheight)/2+100;
	    $("#imgzoom").css({"left":imgload_left,"top":imgload_top});
	}
</script>
</head>
<body>
		<div id="append_parent"></div>
		<div class="list ctnlist">
		<div class="text">
		<div id="imgLayer" align="left" class="imgLayerBox">
			<input id="imgNames" name="imgNames" type="hidden"/>
		</div>
		</div>
		</div>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>查看业务信息</h4></th>
			</tr>
			<tr>
				<th width="10%">业务编号:</th>
				<td width="20%">${workDealInfo.svn }</td>
				<th width="10%">应用名称:</th>
				<td width="20%">${workDealInfo.configApp.appName}</td>
			</tr>
			<tr>
				<th>产品名称:</th>
				<td>${proType[workDealInfo.configProduct.productName]}</td>
				<th>证书应用标识:</th>
				<td><c:if test="${workDealInfo.configProduct.productLabel==0 }">通用</c:if>
					<c:if test="${workDealInfo.configProduct.productLabel==1 }">专用</c:if>
				</td>
			</tr>
			<tr>
				<th>业务类型:</th>
				<td>
					<c:if test="${wdiType[workDealInfo.dealInfoType]!=null}">${wdiType[workDealInfo.dealInfoType]}&nbsp;&nbsp;</c:if>
					<c:if test="${wdiType[workDealInfo.dealInfoType1]!=null}">${wdiType[workDealInfo.dealInfoType1]}&nbsp;&nbsp;</c:if>
					<c:if test="${wdiType[workDealInfo.dealInfoType2]!=null}">${wdiType[workDealInfo.dealInfoType2]}&nbsp;&nbsp;</c:if>
					<c:if test="${wdiType[workDealInfo.dealInfoType3]!=null}">${wdiType[workDealInfo.dealInfoType3]}&nbsp;&nbsp;</c:if>
					
					
				</td>
				<th>申请年数:</th>
				<td>${workDealInfo.year}年</td>
			</tr>
			<tr>
				<th>移动设备申请数量:</th>
				<td><c:if
						test="${workDealInfo.workCertInfo.trustDeviceCount == null}">0</c:if>
					<c:if test="${workDealInfo.workCertInfo.trustDeviceCount != null}">${workDealInfo.workCertInfo.trustDeviceCount}</c:if>
				</td>
				<th>用户分类:</th>
				<td><c:if test="${workDealInfo.classifying==0}">内资</c:if> <c:if
						test="${workDealInfo.classifying==1}">外资</c:if></td>
			</tr>
			<tr>
				<th>多证书编号:</th>
				<td>${workDealInfo.certSort }</td>
				<th>业务状态：</th>
				<td>${wdiStatus[workDealInfo.dealInfoStatus]}</td>
			</tr>
			<tr>
				<th>受理网点:</th>
				<td>${workDealInfo.createBy.office.name }</td>
				<th>Key序列号:</th>
				<td>${workDealInfo.keySn }</td>
			</tr>
			<tr>
				<th>付款方式:</th>
				<td><c:if test="${workDealInfo.workPayInfo.methodPos }">POS机付款&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodMoney }">现金交易&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodAlipay }">支付宝交易&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodGov }">政府统一采购&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodContract }">合同采购&nbsp;</c:if>
					<c:if test="${workDealInfo.workPayInfo.methodBank }">银行转账&nbsp;</c:if>
					${bangD}</td>
				<th>档案编号:</th>
				<td>${workDealInfo.userSn }</td>
			</tr>
			<tr>
				<th>发票信息:</th>
				<td><c:if test="${workDealInfo.workPayInfo.userReceipt}">金额：${workDealInfo.workPayInfo.receiptAmount}</c:if>
					<c:if test="${workDealInfo.workPayInfo.userReceipt==false}">无发票信息</c:if>
				</td>
				<th>归档时间:</th>
				<td >${workDealInfo.archiveDate }</td>
			</tr>
			<tr>
				<th>付款金额:</th>
				<td>${workDealInfo.workPayInfo.workTotalMoney }</td>
				<th>归档人:</th>
				<td>${workDealInfo.updateBy.name }</td>
			</tr>
		</thead>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>单位基本信息</h4></th>
			</tr>
			<tr>
				<th width="10%">单位名称:</th>
				<td width="20%">${workDealInfo.workCompanyHis.companyName}</td>
				<th width="10%">单位类型:</th>
				<td width="20%"><c:if
						test="${workDealInfo.workCompanyHis.companyType==1}">企业</c:if> <c:if
						test="${workDealInfo.workCompanyHis.companyType==2}">事业单位</c:if> <c:if
						test="${workDealInfo.workCompanyHis.companyType==3}">政府机构</c:if> <c:if
						test="${workUser.workCompanyHis.companyType==4}">社会团体</c:if> <c:if
						test="${workUser.workCompanyHis.companyType==5}">其他</c:if></td>
			</tr>
			<tr>
				<th>组织机构代码:</th>
				<td>${workDealInfo.workCompanyHis.organizationNumber}</td>
				<th>组织机构代码有效期:</th>
				<td><fmt:formatDate
						value="${workDealInfo.workCompanyHis.orgExpirationTime}"
						pattern="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				<th>服务级别:</th>
				<td><c:if test="${workDealInfo.workCompanyHis.selectLv==0}">大客户</c:if>
					<c:if test="${workDealInfo.workCompanyHis.selectLv==1}">普通客户</c:if></td>
				<th>单位证照:</th>
				<td><c:if
						test="${workDealInfo.workCompanyHis.comCertificateType==0}">营业执照</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==1}">事业单位法人登记证</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==2}">社会团体登记证</c:if>
					<c:if test="${workDealInfo.workCompanyHis.comCertificateType==3}">其他</c:if>
				</td>
			</tr>
			<tr>
				<th>单位证照号:</th>
				<td>${workDealInfo.workCompanyHis.comCertficateNumber}</td>
				<th>单位证照有效期:</th>
				<td><fmt:formatDate
						value="${workDealInfo.workCompanyHis.comCertficateTime}"
						pattern="yyyy-MM-dd" /></td>
			</tr>
			<c:if test="${workDealInfo.selfImage.id!=null }">
			<tr>
				<th>单位电子证件:</th>
				<td class ="small_pic">
					<div class="col-sm-9">
                         <div class="previewImg" id="preview">
                         	<div class="small_pic">
							<a href="#picBigBox" >
								<img id="imghead" style = "width:100px;height:75px" src="${imgUrl}/${workDealInfo.selfImage.companyImage }" >
                             		</a> 
                             </div>
                         </div>
					</div>
				</td>
				</tr>
			</c:if>
			
			<tr>
				<th>法人姓名:</th>
				<td>${workDealInfo.workCompanyHis.legalName}</td>
				<th>行政所属区:</th>
				<td>${workDealInfo.workCompanyHis.province}
					${workDealInfo.workCompanyHis.city}
					${workDealInfo.workCompanyHis.district}</td>
			</tr>
			<tr>
				<th>街道地址:</th>
				<td>${workDealInfo.workCompanyHis.address}</td>
				<th>备注信息:</th>
				<td>${workDealInfo.workCompanyHis.remarks}</td>
			</tr>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="4"><h4>经办人基本信息</h4></th>
			</tr>
			<tr>
				<th width="10%">姓名:</th>
				<td width="20%">${workDealInfo.workUserHis.contactName}</td>
				<th width="10%">性别:</th>
				<td width="20%">${workDealInfo.workUserHis.contactSex}</td>
			</tr>
			<tr>
				<th>移动电话:</th>
				<td>${workDealInfo.workUserHis.contactPhone}</td>
				<th>业务系统UID:</th>
				<td>${workDealInfo.workUserHis.contactTel}</td>
			</tr>
			<tr>
				<th>证件类型:</th>
				<td><c:if test="${workDealInfo.workUserHis.conCertType==0 }">身份证</c:if>
					<c:if test="${workDealInfo.workUserHis.conCertType==1 }">军官证</c:if>
					<c:if test="${workDealInfo.workUserHis.conCertType==2 }">其他</c:if>
				</td>
				<th>证件号码:</th>
				<td>${workDealInfo.workUserHis.conCertNumber }</td>
			</tr>
			
			
			<c:if test="${workDealInfo.selfImage.id!=null }">
				<tr>
					<th >个人电子证件:</th>
					<td class = "small_pic">
						<div class="col-sm-9">
                          		<div class="small_pic">
                           		<a href="#picBigBox1" >
                           		   <img id="imghead1" style = "width:100px;height:75px"  src="${imgUrl }/${workDealInfo.selfImage.transactorImage}" >
                           		</a>
                          		</div>
                          	</div>
					</td>
				</tr>	
			</c:if>	
			<tr>
				<th>部门名称:</th>
				<td>${workDealInfo.workUserHis.department}</td>
				<th>联系人编码:</th>
				<td>${workDealInfo.workUserHis.userSn}</td>
			</tr>
			<tr>
				<th>电子邮件:</th>
				<td>${workDealInfo.workUserHis.contactEmail}</td>
				<th>联系地址:</th>
				<td>${workDealInfo.workUserHis.address}</td>
			</tr>
	</table>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="5"><h4>受理记录</h4></th>
			</tr>
		</thead>
		<tr>
			<th>编号</th>
			<th>记录内容</th>
			<th>记录人</th>
			<th>受理网点</th>
			<th>记录时间</th>
		</tr>
		<c:forEach items="${workLogs }" var="workLog" varStatus="status">
			<tr>
				<td>${status.index+1 }</td>
				<td>${workLog.recordContent }</td>
				<td>${workLog.createBy.name }</td>
				<td>${workLog.office.name }</td>
				<td>${workLog.createDate }</td>
			</tr>
		</c:forEach>
	</table>
	<tags:message content="${message}" />
	<div class="form-actions">
		<input id="btnCancel" class="btn" type="button" value="返 回"
			onclick="history.go(-1)" />
	</div>
	<div id="picBigBox" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.companyImage }"  >
	</div>
	<div id="picBigBox1" style="display:none;">
		<img src="${imgUrl }/${workDealInfo.selfImage.transactorImage }" >
	</div>
</body>
</html>
