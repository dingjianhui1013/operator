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

            var url = "${ctx}/statistic/StatisticDayData/checkDate?countDate="+countDate+"&_="+new Date().getTime();

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
                                    url:"${ctx}/statistic/StatisticDayData/statisticAgain?countDate="+countDate+"&_="+new Date().getTime(),
                                    dataType:'json',
                                    success:function(data){
                                        top.$.jBox.tip(data.msg);
                                        if(data.status==0){
                                            top.$.jBox.tip(data.msg);
                                        }else if(data.status==1){
                                            top.$.jBox.tip(data.msg);
                                            window.location="${ctx}/statistic/StatisticDayData/dayDateCount?countDate="+countDate;
                                        }
                                    }
                                });
                            }
                        },{buttonsFocus:1});
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    }else if(data.status==1){
                        top.$.jBox.tip(data.msg);
                        window.location="${ctx}/statistic/StatisticDayData/dayDateCount?countDate="+countDate;
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
            

             var url = "${ctx}/statistic/StatisticDayData/statisticSave?countDate="+countDate+"&_="+new Date().getTime();

             $.ajax({
                 //type: 'POST',
                 url: url,
                 //data: data,
                 dataType : 'json',
                 success:function(data){
                     if(data.status==0){
                    	 top.$.jBox.error(data.msg);
                     }else if(data.status==1){
                         top.$.jBox.tip(data.msg);                        
                     }else{
                         top.$.jBox.error(data.msg);
                         //window.location.reload();
                     }
                 }
             });
         }
    </script>
</head>
<body>


<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/statistic/StatisticDayData/dayDateCount">日经营统计表</a></li>    

</ul>

<div class="breadcrumb form-search">

    <label>统计日期：</label>
    <input id="countDate" name="countDate"
           type="text" readonly="readonly" maxlength="20"
           class="Wdate required"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,maxDate:'#F{$dp.$D(\'theDate\')}'});"
           value="<fmt:formatDate value="${countDate}" pattern="yyyy-MM-dd"/>"/>
    <input type="hidden" name="theDate" id="theDate" value="<fmt:formatDate value="${countDate}" pattern="yyyy-MM-dd"/>" id="countDateHidden"/>
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input id="tjButton" class="btn btn-primary" type="button" onclick="checkDate();"
            value="日经营统计"/>
     <c:if test="${sessionScope.statisticDayData!=null && sessionScope.statisticDayData.statisticDate !=null}">
      <input id="tjButton" class="btn btn-primary" type="button" onclick="saveData();"   value="确定（保存）"/>    
     </c:if>
</div>
<div>
<c:if test="${sessionScope.statisticDayData!=null && sessionScope.statisticDayData.statisticDate !=null}">
<c:set var="statisticDayData" value="${sessionScope.statisticDayData}"></c:set>
<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="2" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">日期</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">入库</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">总量</th>
				<th colspan="3" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth"> 日结</th>
				<th colspan="2" rowspan="1" style="text-align:center; vertical-align: middle;border: 1px solid #ddd;" class="thth">余量</th>
				
			</tr>
			 <tr>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">证书</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">费用</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="1" style="text-align:center; vertical-align: middle;">发票</td>
			</tr>			
		</thead>
		<tbody>
			
				<tr>
					<td><fmt:formatDate value="${statisticDayData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td>${statisticDayData.keyIn}</td>
					<td>${statisticDayData.receiptIn}</td>
					<td>${statisticDayData.keyTotal}</td>
					<td>${statisticDayData.receiptTotal}</td>
					<td>${statisticDayData.certTotal}</td>
					<td>${statisticDayData.certMoneyTotal}</td>
					<td>${statisticDayData.keyOver}</td>
				    <td>${statisticDayData.keyStoreTotal}</td>
				    <td>${statisticDayData.receiptStoreTotal}</td>				    
				</tr>
			
		</tbody>
	</table>
	<c:set var="appDatas" value="${sessionScope.appDatas}"></c:set>
	<c:forEach items="${appDatas}" var="appData"> 
	
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th rowspan="4" style="text-align:center; vertical-align: middle;">日期</th>
				<th colspan="36" style="text-align:center; vertical-align: middle;">${appData.app.appName}</th> 
				
				
			</tr>
			  
			<tr > 
			     <th colspan="33" style="text-align:center; vertical-align: middle;">业务办理</th>
				 <th colspan="3" style="text-align:center; vertical-align: middle;">小计</th> 
			</tr> 
			<tr>
				
				<td colspan="4" style="text-align:center; vertical-align: middle;">新增</td>
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">变更</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">损坏更换</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">遗失补办</td>
				
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新+变更</td>
				
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新+遗失补办</td>
				
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新+损坏更换</td>
				
				<td rowspan="2" style="text-align:center; vertical-align: middle;">变更+遗失补办</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">变更+损坏更换</td>
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新+变更+遗失补办</td>
				<td colspan="4" style="text-align:center; vertical-align: middle;">更新+变更+损坏更换</td>
				
				
				
				<td rowspan="2" style="text-align:center; vertical-align: middle;">证书</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">KEY</td>
				<td rowspan="2" style="text-align:center; vertical-align: middle;">发票</td> 
			
			</tr>
			<tr>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				<td style="text-align:center; vertical-align: middle;">1年</td>
				<td style="text-align:center; vertical-align: middle;">2年</td>
				<td style="text-align:center; vertical-align: middle;">4年</td>
				<td style="text-align:center; vertical-align: middle;">5年</td>
				
			</tr> 
		</thead>
		<tbody>
				<tr>
					<td style="text-align:center; vertical-align: middle;" >
					<fmt:formatDate value="${appData.statisticDate}" pattern="yyyy-MM-dd"/></td>
					<td style="text-align:center; vertical-align: middle;" >${appData.add1}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.add2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.add4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.add5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renew1}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renew2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renew4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.renew5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.modifyNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.reissueNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.lostReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateChangeNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateChangeNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateChangeNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateChangeNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateLostNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateLostNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateLostNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateReplaceNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateReplaceNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.updateReplaceNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateLostNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateLostNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateLostNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateLostNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateReplaceNum}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateReplaceNum2}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateReplaceNum4}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.changeUpdateReplaceNum5}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.certTotal}</td>
					<td style="text-align:center; vertical-align: middle;" >${appData.keyTotal}</td>
					<td style="text-align:center; vertical-align: middle;">${appData.receiptTotal}</td>
				</tr>
		</tbody>
	</table>
	
	</c:forEach>	
	
</c:if>
</div>


</body>
</html>
