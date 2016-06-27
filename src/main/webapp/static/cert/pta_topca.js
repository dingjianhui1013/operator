var iTrusPTA = null;

var INPUT_BASE64 = 0x1;
var INPUT_HEX = 0x2;
var OUTPUT_BASE64 = 0x4;
var OUTPUT_HEX = 0x8;
var INNER_CONTENT = 0x10;

var _pta_OverlapPeriod = 30;
var FUNCOPT_NOALERT = 0x80000000;

//  密钥用法选项
var KEY_USAGE_UNDEFINED		= 0x00
var KEY_USAGE_ALL			= 0x01
var KEY_USAGE_CRL_SIGN		= 0x02
var KEY_USAGE_CERT_SIGN		= 0x04
var KEY_USAGE_KEY_AGREEMENT	= 0x08
var KEY_USAGE_DATA_ENCIPHERMENT	= 0x10
var KEY_USAGE_KEY_ENCIPHERMENT	= 0x20
var KEY_USAGE_NON_REPUDIATION	= 0x40
var KEY_USAGE_DIGITAL_SIGNATURE	= 0x80

$(function(){
   iTrusPTA = loadItrusPTA();
});
/**
 * class Names
 * @method getItem(name) return names' first value
 * @method getItems(name) return names' value sting array object
 */
function Names(distinguishName) {
    this.names = init(distinguishName);

    this.getItem = function (name) {
        var values = this.names.get(name);
        if (null == values) {
            return null;
        } else {
            return values[0];
        }
    }

    this.getItems = function (name) {
        return this.names.get(name);
    }

    function init(dn) {
        var _names = new Hashtable();
        var partition = ", ";

        var Items = dn.split(partition);
        var itemString = "";
        for (var i = Items.length - 1; i >= 0; i--) {
            if (itemString != "") {
                itemString = Items[i] + itemString;
            } else {
                itemString = Items[i];
            }

            var pos = itemString.indexOf("=");
            if (-1 == pos) {
                itemString = partition + itemString;
                continue;
            } else {
                var name = itemString.substring(0, pos);
                var value = itemString.substring(pos + 1, itemString.length);
                // wipe off the limitrophe quotation marks
                if (value.indexOf("\"") == 0 && (value.length - 1) == value.lastIndexOf("\"")) {
                    value = value.substring(1, value.length);
                    value = value.substring(0, value.length - 1);
                }

                if (_names.containsKey(name)) {
                    var array = _names.get(name);

                    array.push(value);
                    _names.remove(name);
                    _names.put(name, array);
                } else {
                    var array = new Array();
                    array.push(value);
                    _names.put(name, array);
                }
                itemString = "";
            }
        }
        return _names;
    }
}

/**
 * setOverlapPeriod 设置允许更新的证书到期天数
 * @param days 整型
 */
function setOverlapPeriod(days) {
    _pta_OverlapPeriod = days;
}

function PTADate(date){
    return $.browser.msie?new Date(date):new Function('return '+date+';')();
}

/**
 * JSDateAdd Javascript 计算给定日期+天数
 * @param theDate: 给定日期，Date类型
 * @param days: 整型
 * @return 计算结果，Date类型
 */
function JSDateAdd(theDate, days) {
    var dateValue = theDate.valueOf();
    dateValue += days * 1000 * 60 * 60 * 24;
    var newDate = new Date(dateValue);
    return newDate;
}
/**
 * JSDateDiffByDays Javascript 计算两个日期之间的间隔天数
 * @param date1: 给定日期1，Date类型
 * @param date2: 给定日期2，Date类型
 * @return 天数，整型
 */
function JSDateDiffByDays(date1, date2) {
    var mill = date1.valueOf() - date2.valueOf();
    var millStr = new String(mill / 1000 / 60 / 60 / 24)
    return parseInt(millStr);
}

function blurFilterCerts(subjectDN, dateFlag, serialNumber) {
    var m_certs = filterCerts("", dateFlag, "")
    var f_certs = new Array();
    if (m_certs.length <= 0 || (subjectDN == null && serialNumber == null) || (subjectDN == "" && serialNumber == ""))
        return m_certs;
    var tmpDN = null;
    var tmpSerialNumber = null;
    for (var k = 0; k < m_certs.length; k++) {
        tmpDN = m_certs[k].Subject;
        tmpSerialNumber = m_certs[k].SerialNumber;
        if (eval("tmpDN.search(/" + ((subjectDN != null && subjectDN != "") ? subjectDN : ".") + "/i)") >= 0
            && eval("tmpSerialNumber.search(/" + ((serialNumber != null && serialNumber != "") ? serialNumber : ".") + "/i)") >= 0)
            f_certs.push(m_certs[k])
    }
    return f_certs
}

/**
 * filterCerts 根据所设置条件过滤证书
 * @param arrayIssuerDN(optional) Array() or string，缺省为""，证书的颁发者字符串和字符串数组，支持多个CA时使用字符串数组
 * @param dateFlag(optional) 缺省为0，0表示所有证书，1表示处于有效期内的证书，2表示待更新证书，3表示未生效或已过期证书
 * @param serialNumber(optional) 缺省为""，证书序列号（微软格式）
 * @return Array(), PTALib.Certificate
 */
function filterCerts(arrayIssuerDN, dateFlag, serialNumber) {
	  
	
	var m_certs = new Array();
    var i = 0;
    if (arrayIssuerDN == null) {
        arrayIssuerDN = new Array("");
    } else if (typeof(arrayIssuerDN) == "string") {
        arrayIssuerDN = new Array(arrayIssuerDN);
    }
    if (typeof(serialNumber) == "undefined")
        serialNumber = "";
  
    
    for (i = 0; i < arrayIssuerDN.length; i++) {
        var CertFilter = iTrusPTA.Filter;
        CertFilter.Clear();
        CertFilter.Issuer = arrayIssuerDN[i];
        CertFilter.SerialNumber = serialNumber;
        var t_Certs = iTrusPTA.MyCertificates; //临时变量
        var now = new Date();
        if (parseInt(t_Certs.Count) > 0) { //找到了证书
            for (var j = 1; j <= parseInt(t_Certs.Count); j++) {
                var validFrom = new Date(eval(t_Certs(j).ValidFrom));
                var validTo = new Date(eval(t_Certs(j).ValidTo));
                var keyUsage = t_Certs(j).KeyUsage;
                switch (dateFlag) {
                    case 0://所有证书
                        m_certs.push(t_Certs(j));
                        break;
                    case 1://处于有效期内的证书
                        //validFrom     validTo
                        //          now
                        if (validFrom < now && now < validTo)
                            m_certs.push(t_Certs(j));
                        break;
                    case 2://待更新证书
                        //validFrom     validTo-30     validTo
                        //                         now
                        if (JSDateAdd(validTo, -_pta_OverlapPeriod) < now && now < validTo) {
                            //KEY_USAGE_KEY_AGREEMENT	= &H08
                            //KEY_USAGE_DATA_ENCIPHERMENT=&H10
                            //KEY_USAGE_KEY_ENCIPHERMENT= &H20
                            keyUsage = keyUsage & ~0x08;
                            keyUsage = keyUsage & ~0x10;
                            keyUsage = keyUsage & ~0x20;
                            if (keyUsage != 0) {
                                m_certs.push(t_Certs(j));
                            }
                        }
                        break;
                    case 3://未生效或已过期证书
                        //     validFrom     validTo
                        // now                       now
                        if (now < validFrom || validTo < now)
                            m_certs.push(t_Certs(j));
                        break;
                    default://缺省当作所有证书处理
                        m_certs.push(t_Certs(j));
                        break;
                }
            }
        }
    }

    return m_certs;
}

/**
 * signLogonData 登陆签名
 * @param certList 证书列表<select>对象
 * @param inputToSign: 用于签名登陆的被签名<input>对象
 * @return 成功返回签名值，失败返回""
 */
function signLogonData(signer, inputToSign) {
    try {
        var signedData;
        var ptaVersion = iTrusPTA.Version;
        if (ptaVersion == null) {
            //PTA Version = 1.0.0.3
            signedData = signer.SignMessage(inputToSign.value, OUTPUT_BASE64 | FUNCOPT_NOALERT);
        } else {
            //PTA Version > 2
            if (inputToSign.value.indexOf("LOGONDATA:") == -1)
                inputToSign.value = "LOGONDATA:" + inputToSign.value;
            signedData = signer.SignLogonData(inputToSign.value, OUTPUT_BASE64);
        }
        return signedData;
    } catch (e) {
        if (-2147483135 == e.number) {
            //用户取消签名
        } else if (e.number == -2146885621) {
            alert("您不拥有证书“" + CurCert.CommonName + "”的私钥，签名失败。");
            return "";
        } else {
            alert("PTA签名时发生错误\n错误号: " + e.number + "\n错误描述: " + e.description);
            return "";
        }
    }
}

/**
 * verifySignature 验证签名
 * @param strToSign: 原文
 * @param base64StrSignature: 签名值
 * @return 成功: 返回签名证书对象，失败: 返回null
 */
function verifySignature(strToSign, base64StrSignature) {
    try {
        var signCert = iTrusPTA.VerifySignature(strToSign, base64StrSignature, INPUT_BASE64);
        alert("签名验证成功。签名者为“" + signCert.CommonName + "”。");
        return true;
    } catch (e) {
        if (e.number == -2146893818)
            alert("签名验证失败。\n签名值与原文不匹配，内容已遭篡改。");
        else
            alert("PTA模块发生错误\n错误号: " + e.number + "\n错误描述: " + e.description);
        return false;
    }
}

/**
 * signMessage 数字签名
 * @param plainText: 原文
 * @param signCert 用于签名的证书对象，可以使用GetSingleCertificate函数获得证书对象
 *             ，或者使用SelectSingleCertificate函数选择<select>中列出的证书
 * @return 成功: 返回签名值，失败: 返回""
 */
function signMessage(plainText, signCert) {
    var signedStr;
    var signCert;
    try {
        signedStr = signCert.SignMessage(plainText, OUTPUT_BASE64 |  FUNCOPT_NOALERT);
    } catch (e) {
        if ((e.number == -2147483135)
            || e.number == -2146881278 // sign confirm canceled
            || e.number == -2146434962 // FT2001 PIN Login canceled
            ) {
            return "";//User canceled
        } else if (e.number == -2146885621)
            alert("您不拥有证书“" + signCert.CommonName + "”的私钥，签名失败。");
        else
            alert("PTA模块发生错误\n错误号: " + e.number + "\n错误描述: " + e.description);
        return "";
    }
    return signedStr;
}

/**
 * signCSR 更新证书时需要调用，对更新证书的CSR
 * @param objOldCert(mandatory) 要更新的证书对象（PTALib.Certificate）
 * @param csr(mandatory) 证书签名请求
 */
function signCSR(objOldCert, csr) {
    try {
        var ptaVersion = iTrusPTA.Version;
        return objOldCert.SignLogonData("LOGONDATA:" + csr, OUTPUT_BASE64 | INNER_CONTENT);
    } catch (e) {
        if (-2147483135 == e.number) {
            //用户取消签名
            return "";
        } else if (e.number == -2146885621) {
            alert("您不拥有证书“" + objOldCert.CommonName + "”的私钥，签名失败。");
            return "";
        } else {
            alert("PTA签名时发生错误\n错误号: " + e.number + "\n错误描述: " + e.description);
            return "";
        }
    }
}

function loadItrusPTA(){
    var iTrusPTA = null;
    var urlArray = new Array();
    urlArray = window.location.toString().split('/');
    var base = urlArray[0]+'//' + window.location.host + '/' + urlArray[3];
    //var iePtaObject = "<object id='iTrusPTA' codebase='"+base+"/download/itruscertctl.CAB#version=2,5,7,6' classid='clsid:F1F0506B-E2DC-4910-9CFC-4D7B18FEA5F9'></object>";
    var iePtaObject = "<object id='iTrusPTA' classid='clsid:41992E4D-6707-4342-842E-7EF66DED5163'></object>";
    var ffPtaObject = "<embed  id='iTrusPTA' type='application/iTrusPTA.iTrusPTA.Version.1' width='0' height='0'></embed>";
    if (IsValidBrowser()) {
        iTrusPTA = $(iePtaObject).appendTo(document.body)[0];
    } else {
        var length = navigator.plugins.length;
        var check_install = 0;
        for (var i = 0; i < length; i++) {
            var index = (navigator.plugins[i].name).indexOf("iTrusPTA");
            if (index > -1) {
                check_install++;
                break;
            }
        }
        if (check_install > 0) {
            iTrusPTA = $(ffPtaObject).appendTo(document.body)[0];
        } else {
            var href = window.location.href.toString();

            if(href.indexOf("/console")==-1&&href.indexOf("/offlineCA")==-1&&href.indexOf("/ca")==-1){
                //if (window.location.href != base + "/showDownPTA") {
                  //  window.location.href = base + "/showDownPTA";
                //}
            }
        }
    }
    return iTrusPTA;
}
/*******************************************************************************************
 * Object: Hashtable
 * Description: Implementation of hashtable
 * Author: Uzi Refaeli
 *******************************************************************************************/

//======================================= Properties ========================================
Hashtable.prototype.hash = null;
Hashtable.prototype.keys = null;
Hashtable.prototype.location = null;

/**
 * Hashtable - Constructor
 * Create a new Hashtable object.
 */
function Hashtable() {
    this.hash = new Array();
    this.keys = new Array();

    this.location = 0;
}

Hashtable.prototype.containsKey = function (key) {
    if (this.hash[key] == null)
        return false;
    else
        return true;
}

/**
 * put
 * Add new key
 * param: key - String, key name
 * param: value - Object, the object to insert
 */
Hashtable.prototype.put = function (key, value) {
    if (value == null)
        return;

    if (this.hash[key] == null)
        this.keys[this.keys.length] = key;

    this.hash[key] = value;
}

/**
 * get
 * Return an element
 * param: key - String, key name
 * Return: object - The requested object
 */
Hashtable.prototype.get = function (key) {
    return this.hash[key];
}

/**
 * remove
 * Remove an element
 * param: key - String, key name
 */
Hashtable.prototype.remove = function (key) {
    for (var i = 0; i < this.keys.length; i++) {
        //did we found our key?
        if (key == this.keys[i]) {
            //remove it from the hash
            this.hash[this.keys[i]] = null;
            //and throw away the key...
            this.keys.splice(i, 1);
            return;
        }
    }
}

/**
 * size
 * Return: Number of elements in the hashtable
 */
Hashtable.prototype.size = function () {
    return this.keys.length;
}

/**
 * populateItems
 * Deprecated
 */
Hashtable.prototype.populateItems = function () {
}

/**
 * next
 * Return: true if theres more items
 */
Hashtable.prototype.next = function () {
    if (++this.location < this.keys.length)
        return true;
    else
        return false;
}

/**
 * moveFirst
 * Move to the first item.
 */
Hashtable.prototype.moveFirst = function () {
    try {
        this.location = -1;
    } catch (e) {/*//do nothing here :-)*/
    }
}

/**
 * moveLast
 * Move to the last item.
 */
Hashtable.prototype.moveLast = function () {
    try {
        this.location = this.keys.length - 1;
    } catch (e) {/*//do nothing here :-)*/
    }
}

/**
 * getKey
 * Return: The value of item in the hash
 */
Hashtable.prototype.getKey = function () {
    try {
        return this.keys[this.location];
    } catch (e) {
        return null;
    }
}

/**
 * getValue
 * Return: The value of item in the hash
 */
Hashtable.prototype.getValue = function () {
    try {
        return this.hash[this.keys[this.location]];
    } catch (e) {
        return null;
    }
}

/**
 * getKey
 * Return: The first key contains the given value, or null if not found
 */
Hashtable.prototype.getKeyOfValue = function (value) {
    for (var i = 0; i < this.keys.length; i++)
        if (this.hash[this.keys[i]] == value)
            return this.keys[i]
    return null;
}


/**
 * toString
 * Returns a string representation of this Hashtable object in the form of a set of entries,
 * enclosed in braces and separated by the ASCII characters ", " (comma and space).
 * Each entry is rendered as the key, an equals sign =, and the associated element,
 * where the toString method is used to convert the key and element to strings.
 * Return: a string representation of this hashtable.
 */
Hashtable.prototype.toString = function () {

    try {
        var s = new Array(this.keys.length);
        s[s.length] = "{";

        for (var i = 0; i < this.keys.length; i++) {
            s[s.length] = this.keys[i];
            s[s.length] = "=";
            var v = this.hash[this.keys[i]];
            if (v)
                s[s.length] = v.toString();
            else
                s[s.length] = "null";

            if (i != this.keys.length - 1)
                s[s.length] = ", ";
        }
    } catch (e) {
        //do nothing here :-)
    } finally {
        s[s.length] = "}";
    }

    return s.join("");
}

/**
 * add
 * Concatanates hashtable to another hashtable.
 */
Hashtable.prototype.add = function (ht) {
    try {
        ht.moveFirst();
        while (ht.next()) {
            var key = ht.getKey();
            //put the new value in both cases (exists or not).
            this.hash[key] = ht.getValue();
            //but if it is a new key also increase the key set
            if (this.get(key) != null) {
                this.keys[this.keys.length] = key;
            }
        }
    } catch (e) {
        //do nothing here :-)
    } finally {
        return this;
    }
};
