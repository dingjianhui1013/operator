function getTodayString() {

    var today = new Date();
    var strYear = today.getYear();
    var strMonth = today.getMonth() + 1;
    var strDay = today.getDay();
    if (strMonth.length < 10) {
        strMonth = "0" + strMonth;
    }
    if (strDay < 10) {
        strDay = "0" + strDay;
    }
    return (strYear + "-" + strMonth + "-" + strDay);
}

function getCurrentDateString(dateOffset) {

    var date = new Date();
    date.setTime(date.getTime() + dateOffset * 1000 * 60 * 60 * 24);

    var year = date.getYear();
    if (year >= 97 && year <= 99)
        year = 1900 + year;
    else if (year >= 0 && year <= 96)
        year = 2000 + year;
    else if ((year >= 100) && (year < 2000))
        year = 2000 + (year - 100);

    var mon = date.getMonth();
    ++mon;

    //小于10的用0补前面的位置
    var today;
    today = year + "-";
    if (mon < 10)
        today += "0" + mon + "-";
    else
        today += mon + "-";
    if (date.getDate() < 10)
        today += "0" + date.getDate();
    else
        today += date.getDate();

    return today;
}

function fucCheckNUM(NUM) {
    var i, j, strTemp;
    strTemp = "0123456789";
    if (NUM.length == 0)
        return 0;
    for (i = 0; i < NUM.length; i++) {
        j = strTemp.indexOf(NUM.charAt(i));
        if (j == -1) {
            //说明有字符不是数字
            return 0;
        }
    }
    //说明是数字
    return 1;
}

//判断是否为日期型
function isDate(theStr) {
    var the1st = theStr.indexOf('-');
    var the2nd = theStr.lastIndexOf('-');

    if (the1st == the2nd) {
        return(false);
    }
    else {
        var y = theStr.substring(0, the1st);
        var m = theStr.substring(the1st + 1, the2nd);
        var d = theStr.substring(the2nd + 1, theStr.length);
        var maxDays = 31;

        if (fucCheckNUM(m) == 0 || fucCheckNUM(d) == 0 || fucCheckNUM(y) == 0) {
            return(false);
        }
        else if (y.length < 4) {
            return(false);
        }
        else if ((m < 1) || (m > 12)) {
            return(false);
        }
        else if (m == 4 || m == 6 || m == 9 || m == 11) maxDays = 30;
        else if (m == 2) {
            if (y % 4 > 0) maxDays = 28;
            else if (y % 100 == 0 && y % 400 > 0) maxDays = 28;
            else maxDays = 29;
        }
        if ((m < 1) || (d > maxDays)) {
            return(false);
        }
        else {
            return(true);
        }
    }
}

//填充日期位数，月和日不足两位的以零补足
function fillDateString(theStr) {
    var the1st = theStr.indexOf('-');
    var the2nd = theStr.lastIndexOf('-');

    var y = theStr.substring(0, the1st);
    var m = theStr.substring(the1st + 1, the2nd);
    var d = theStr.substring(the2nd + 1, theStr.length);

    var strMonth = "";
    var strDay = "";

    if (m < 10 && m.length < 2)
        strMonth = "0" + m;
    else
        strMonth = m;

    if (d < 10 && d.length < 2)
        strDay = "0" + d;
    else
        strDay = d;

    return y + strMonth + strDay;
}

//生成数据库存储格式的日期字符串 yyyymmdd000000和yyyymmdd235959
//调用下列两个函数前需要使用isDate函数检查字符串是否为日期型
function convertBeginDateToDB(startDate) {

    var strDate;

    strDate = fillDateString(startDate);
    return strDate + "000000";
}
function convertEndDateToDB(endDate) {

    var strDate;

    strDate = fillDateString(endDate);
    return strDate + "235959";
}

function convertDBDateToPage(dbDate) {
    var formatDate;
    formatDate = dbDate.replace(/^(\d{4})(\d{2})(\d{2})\d+$/ig, "$1-$2-$3");
    return formatDate;
}

/*
 args:20111223123322
 return 2011年12月23日 12:33:22
 */
function formatDate(dateString, hasTime) {
    if (dateString && dateString.length == 14) {
        var year = dateString.substring(0, 4);
        var month = dateString.substring(4, 6);
        var day = dateString.substring(6, 8);
        var hour = dateString.substring(8, 10);
        var min = dateString.substring(10, 12);
        var second = dateString.substring(12, 14);

        if (hasTime) {
            return year + "年" + month + "月" + day + "日 " + hour + ":" + min + ":" + second;
        } else {
            return year + "年" + month + "月" + day + "日 ";
        }

    } else {
        return dateString;
    }
}

/*
 *toDefaultDateFormat 格式化日期格式，主要用于兼容chrome下日期格式不正确
 * @param date(mandatory) Date对象
 * @return String格式,如：2012年12月21日 00:00:00
 */
function toDefaultDateFormat(date) {
    var year = $.browser.msie?date.getFullYear():(date.getYear()+1900);
    var month = date.getMonth()+1;
    var day = date.getDate();
    var hour = date.getHours();
    var min = date.getMinutes();
    var sec = date.getSeconds();

    var fillTwo = function(number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return number;
        }
    };

    return year + "年" + month + "月" + day + "日 " + fillTwo(hour) + ":" + fillTwo(min) + ":" + fillTwo(sec);
}
/*
 *isNumber 判断是否是数字
 * @param val 需要判断的值
 * @return 如果是数字则返回数组，如果不是，则返回<0的值
 */
function isNumber(val){
    if(!val){
        return -1;
    }
    var reg = /\d{1}/;
    if(reg.test(val.replace(/ /g,""))){
        return parseInt(val.replace(/ /g,""));
    }else{
        return -1;
    }
}