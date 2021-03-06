<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>日经营统计</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {


        });
        /**
        *   校验当前日期是否已经进行统计 ajax
         */
        function checkDate(){
            var countDate = $("#countDate").val();//获得需要统计的日期
            var countDatehide = $("#countDateHidden").val();
            if(countDate==""){//统计日期不能为空
                top.$.jBox.tip("请选择统计日期");
                return false;
            }
            if(countDate > countDatehide){
                top.$.jBox.tip("只能统计"+countDatehide+"(含)之前的数据");
                return false;
            }
            var url = "${ctx}/statistic/statisticSealDayData/checkDate?countDate="+countDate+"&_="+new Date().getTime();
            loading('正在提交，请稍等...');
            $.ajax({
                //type: 'POST',
                url: url,
                //data: data,
                dataType : 'json',
                success:function(data){
                    if(data.status==0){
                        top.$.jBox.confirm(data.msg,'系统提示',function(v,h,f){
                            if(v=='ok'){
                                $.ajax({
                                    url:"${ctx}/statistic/statisticSealDayData/statisticAgain?countDate="+countDate+"&_="+new Date().getTime(),
                                    dataType:'json',
                                    success:function(data){
                                        top.$.jBox.tip(data.msg);
                                        if(data.status==0){
                                            top.$.jBox.tip(data.msg);
                                        }else if(data.status==1){
                                            top.$.jBox.tip(data.msg);
                                            window.location="${ctx}/statistic/statisticSealDayData/dayDateCount?countDate="+countDate;
                                        }
                                    }
                                });
                            }
                        },{buttonsFocus:1});
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    }else if(data.status==1){
                        top.$.jBox.tip(data.msg);
                        window.location="${ctx}/statistic/statisticSealDayData/dayDateCount?countDate="+countDate;
                    }else if(data.status==2){
                        top.$.jBox.error(data.msg);
                        //window.location.reload();
                    }
                }
            });

        }
        /**
         *   保存当前日期统计数据ajax
          */
         function saveData(){
        	var countDate = $("#countDate").val();
             var url = "${ctx}/statistic/statisticSealDayData/statisticSave?countDate="+countDate+"&_="+new Date().getTime();
             $.ajax({
                 url: url,
                 dataType : 'json',
                 success:function(data){
                     if(data.status==0){
                    	 top.$.jBox.error(data.msg);
                     }else if(data.status==1){
                         top.$.jBox.tip(data.msg);                        
                     }else{
                         top.$.jBox.error(data.msg);
                     }
                 }
             });
         }
    </script>
</head>
<body>


<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/statistic/statisticSealDayData/dayDateCount">印章日经营统计表</a></li>    
</ul>

<div class="breadcrumb form-search">

    <label>统计日期：</label>
    <input id="countDate" name="countDate"
           type="text" readonly="readonly" maxlength="20"
           class="Wdate required"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"
           value="<fmt:formatDate value="${countDate}" pattern="yyyy-MM-dd"/>"/>
    <input type="hidden" name="theDate" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" id="countDateHidden"/>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input id="tjButton" class="btn btn-primary" type="button" onclick="checkDate();"
            value="印章日经营统计"/>
     <c:if test="${sessionScope.statisticSealDayData!=null && sessionScope.statisticSealDayData.statisticDate !=null}">
      <input id="tjButton" class="btn btn-primary" type="button" onclick="saveData();" value="确定（保存）"/>    
     </c:if>
</div>
<div>

<c:if test="${sessionScope.statisticSealDayData!=null && sessionScope.statisticSealDayData.statisticDate !=null}">
<c:set var="statisticSealDayData" value="${sessionScope.statisticSealDayData}"></c:set>
<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">日期</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">入库</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">总量</th>
				<th colspan="3" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth"> 日结</th>
				<th colspan="1" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">余量</th>
				
			</tr>
			 <tr>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">印章</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">费用</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
			</tr>			
		</thead>
		<tbody>
				<tr>
					<td  style="text-align:center; vertical-align: middle;"><fmt:formatDate value="${statisticSealDayData.statisticDate}" pattern="yyyy-MM-dd"/></td>
				<td>${statisticSealDayData.receiptIn}</td>
				<td>${statisticSealDayData.receiptTotal}</td>
				<td>${statisticSealDayData.sealDay}</td>
				<td>${statisticSealDayData.sealMoney}</td>
				<td>${statisticSealDayData.receiptDay}</td>
				<td>${statisticSealDayData.receiptSurplus}</td>
				</tr>
		</tbody>
	</table>


	<c:set var="appDatas" value="${sessionScope.appDatas}"></c:set>
	<c:forEach items="${appDatas}" var="appData"> 


	
	<c:if test="${appData.sealTotal==0}">
	
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr > 
				     <td style="text-align:center; vertical-align: middle;" >
						<fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/>
					</td>
					 <td  colspan="18">
						${appData.app.appName}应用今天没有办理业务
					</td>
				</tr> 
			</tbody>
		</table>
	</c:if>
	<c:if test="${appData.sealTotal!=0}">
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th rowspan="4" style="text-align:center; vertical-align: middle;">日期</th>
					<th colspan="18" style="text-align:center; vertical-align: middle;">${appData.app.appName}</th>
				</tr>
				<tr> 
				    <th colspan="15" style="text-align:center; vertical-align: middle;">业务办理</th>
					<th colspan="3" style="text-align:center; vertical-align: middle;">小计</th> 
				</tr> 
				<tr>
					<td colspan="5" style="text-align:center; vertical-align: middle;">新增</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">更新</td>
					<td colspan="5" style="text-align:center; vertical-align: middle;">变更</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">印章</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">费用</td>
					<td rowspan="2" style="text-align:center; vertical-align: middle;">发票</td> 
				
				</tr>
				<tr>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
					<td style="text-align:center; vertical-align: middle;">1年</td>
					<td style="text-align:center; vertical-align: middle;">2年</td>
					<td style="text-align:center; vertical-align: middle;">3年</td>
					<td style="text-align:center; vertical-align: middle;">4年</td>
					<td style="text-align:center; vertical-align: middle;">5年</td>
				</tr> 
			</thead>
			<tbody>
				<tr>
					<td style="text-align:center; vertical-align: middle;" >
					<fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td style="text-align:center; vertical-align: middle;" >${appData.addOne}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.addTwo}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.addThree}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.addFour}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.addFive}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renewOne}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renewTwo}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renewThree}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renewFour}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renewFive}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeOne}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeTwo}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeThree}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeFour}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeFive}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.sealTotal}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.sealMoney}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.receiptTotal}</td>
				</tr>
			</tbody>
		</table>
	</c:if>

	
	</c:forEach>	
	
</c:if>
</div>
</body>
</html>