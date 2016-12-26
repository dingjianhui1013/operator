/**
 * need jquery & jquery cookie
 */

var cenroll = null;

var szOID_RSA_MD5RSA = "1.2.840.113549.1.1.4";
var szOID_RSA_SHA1RSA = "1.2.840.113549.1.1.5";
var szOID_SM2_SM3SM2 = "1.2.156.10197.1.501";

var _useLegibleName =true;
var legibleNameMap = {
		  "飞天 2001":"EnterSafe ePass2001 CSP For SCCA V1.0",
		  "飞天 ePass 3000 GM":"FEITIAN ePassNG CSP For SCCA V2.0",
		  "海泰":"iTruschina Cryptographic Provider(0301) v1.0",
		  "RSA软证书":"Microsoft Enhanced Cryptographic Provider v1.0", 
		  "SM2软证书":"iTrusChina Cryptographic Service Provider v1.0.0"
};
//"天威盾":"iTruschina Cryptographic Provider(0301) v1.0",
// "RSA软证书":"Microsoft Enhanced Cryptographic Provider v1.0", 
//"SM2软证书":"iTrusChina Cryptographic Service Provider v1.0.0",
//"飞天 ePass 3000 GM":"EnterSafe ePass3000GM CSP v1.0",
//"华大智宝 SJK102":"CIDC Cryptographic Service Provider v3.0.0",
//"神州龙芯":"ChinaCpu USB Key CSP v1.0",

var globalAllowedProviders = new Array();

$(function () {
    cenroll = loadItrusEnroll();
    initXEnroll();
});

/**
 * installCert 安装CA颁发下来的证书
 * @param certChain(mandatory) 包含用户证书及证书链的Base64格式P7字符串
 */
function installCert(certChain) {
    try {
        cenroll.Reset();
        cenroll.DeleteRequestCert = false;
        cenroll.WriteCertToCSP = true;
        cenroll.acceptPKCS7(certChain);
        return true;
    } catch (e) {
        if (-2147023673 == e.number) {
            alert("您取消了我们为您颁发的数字证书安装，证书安装失败！\n在您还未离开本页面前，您还可以点击“安装数字证书”按钮安装。");
            return false;
        } else if (-2146885628 == e.number) {
            alert("您的证书已经成功安装过了！");
            return false;
        } else {
            alert("安装证书发生错误！\n错误号: " + e.number + "\n错误描述: " + e.description);
            return false;
        }
    }
}

/**
 * installCAChain 安装CA证书链
 * @param cACertSignBufP7(mandatory) PKCS7格式证书链
 */
function installCAChain(cACertSignBufP7) {
    try {
        //cenroll.InstallPKCS7(cACertSignBufP7);acceptPKCS7
        cenroll.acceptPKCS7(cACertSignBufP7);
        return true
    } catch (e) {
        alert(e.description);
        if (-2147023673 == e.number) {
            return false;
        } else {
            alert("安装证书链时发生错误！\n错误原因：" + e.description + "\n错误代码：" + toHex(e.number));
            return false;
        }
    }
}

/**
 * findProviders 查询本地计算机的密钥服务提供者，并显示在指定的<select>中
 * @param objProviderList(mandatory) 密钥服务提供者列表<select>对象
 * @param defaultProvider(optional) 缺省密钥服务提供者名称
 * @param allowedProviders(optional) 允许的密钥服务提供者Array
 */
function findProviders(objProviderList, defaultProvider, allowedProviders) {
    if (typeof(defaultProvider) == "undefined" || defaultProvider == "")
        defaultProvider = "Microsoft Base Cryptographic Provider v1.0";
    var i = 0;
    var nIndex = 0;
    var providerType = 1;//The default value for this property is 1
    var maxProviderType = 1; //max is 24

    var providerFullName;
    addGlobalAllowedProvider(allowedProviders);
    var lastSelectedProvider = $.cookie("lastSelectedProvider");

    while (providerType <= maxProviderType) {
        i = 0; //从0开始枚举
        cenroll.ProviderType = providerType;
        while (true) {
            try {
                providerFullName = cenroll.enumProviders(i, 0);
                if (providerFullName == "" || providerFullName == null) {
                    break;
                }
                if (i > 1000) {
                    alert("bug: You can't have above 1000 csp!")
                    break;
                }
            } catch (e) {
                break;
            }
            if (providerFullName.length == 0) {
                break;
            } else {
                if (globalAllowedProviders.length > 0) {

                    for (var j = 0; j < globalAllowedProviders.length; j++) {
                        if (providerFullName == globalAllowedProviders[j]) {

                            $(objProviderList).append("<option value = " + providerType + " title='" + getProviderName(providerFullName) + "'>" + getProviderName(providerFullName) + "</option>");
                            nIndex++; //总索引
                            break;
                        }
                    }
                } else { //typeof(allowedProviders)=="undefined" || allowedProviders == ""
                    $(objProviderList).append("<option value = " + providerType + " title='" + getProviderName(providerFullName) + "'>" +  getProviderName(providerFullName)  + "</option>");
                    nIndex++; //总索引
                }

                if (typeof(lastSelectedProvider) == "string") { //如果lastSelectedProvider有值
                    if (providerFullName == lastSelectedProvider) //判断当前是否为上次选中的CSP（注意：这两个条件不能合并）
                        objProviderList.selectedIndex = nIndex - 1;
                } else { // lastSelectedProvider无值
                    if (providerFullName == defaultProvider)//判断当前是否为默认CSP
                        objProviderList.selectedIndex = nIndex - 1;
                }
                i++;
            }
        }
        providerType++;
    }
    if (objProviderList.length == 0) {
        $(objProviderList).append("<option value = ''>您的电脑上未安装指定USB KEY的驱动程序！</option>");
    }
}

/**
 * genEnrollCSR 申请证书，创建密钥对并生成证书签名请求
 * @param objProviderList(mandatory) 加密服务提供者列表的<select>对象
 * @param cryptFlag(optional)
 *        0x0表示私钥既不可以导出，又不要求强私钥保护
 *        0x1表示私钥可导出，默认值
 *        0x2表示强私钥保护
 *        0x3(0x1|0x2)表示私钥既可以导出，又要求强私钥保护
 */
function genEnrollCSR(objProviderList, keyLen, cryptFlag) {
    var selectedItem = $("option:selected", objProviderList);
    var providerFullName = getProviderFullName(selectedItem.text());
    var providerType = selectedItem.val();
    return genKeyAndCSREx(providerFullName, providerType, keyLen, cryptFlag);
}

/**
 * genRenewCSR 更新证书，生成更新证书的证书签名请求
 * @param objProviderList(mandatory) 加密服务提供者列表的<select>对象
 * @param cryptFlag(mandatory)
 *        0x0表示私钥既不可以导出，又不要求强私钥保护
 *        0x1表示私钥可导出，默认值
 *        0x2表示强私钥保护
 *        0x3(0x1|0x2)表示私钥既可以导出，又要求强私钥保护
 * @param objOldCert(mandatory) 要更新的证书对象（PTALib.Certificate）
 */
function genRenewCSR(objProviderList, cryptFlag, keyLen, objOldCert, useOldKey) {
    var oldCertCSP = objOldCert.CSP; //旧证书CSP
    var providerFullName, providerType;
    
    if (typeof(objProviderList) == "string") {
        providerFullName = getProviderFullName(objProviderList);
        providerType = 1;
    } else if (typeof(objProviderList) == "object") {

        providerFullName = getProviderFullName(objProviderList.find("option:selected").text());
        providerType = objProviderList.find("option:selected").val();
    } else {
        alert("Paramter [objProviderList] is not correct.");
        return "";
    }

    // useOldKey = true;
    if (oldCertCSP != providerFullName) {
        var info = "您选择的密钥服务提供者与您正在更新的证书不匹配！"
            + "\n如果您点击“确定”，将会生成新的密钥对进行更新，点击“取消”重新选择密钥服务提供者。";

        if (!window.confirm(info)) {
            return "";
        } else
            useOldKey = false;
    }
    if (useOldKey) {
        //使用旧的密钥对，更新后的证书只是更新了证书有效期
        return genKeyAndCSREx2(providerFullName, providerType, keyLen, cryptFlag, objOldCert.KeyContainer, objOldCert.keySpec);
    } else {
        //生成新的密钥对，更新后的证书不仅更新了证书有效期，而且换了密钥对
        return genKeyAndCSREx(providerFullName, providerType, keyLen, cryptFlag);
    }
}

/**
 * genKeyAndCSR()必须包含xenroll_install.js和xenroll_function.js
 * genKeyAndCSR 产生密钥对，并返回证书签名请求CSR
 * @param providerFullName(mandatory) 密钥服务提供者名称
 * @param providerType(mandatory) 密钥服务提供者类型
 * @param cryptFlag(optional)
 *        0x0表示私钥既不可以导出，又不要求强私钥保护
 *        0x1表示私钥可导出，默认值
 *        0x2表示强私钥保护
 *        0x3(0x1|0x2)表示私钥既可以导出，又要求强私钥保护
 * @param keyContainer(optional)
 *        可使用PTALib.Certificate对象的.KeyContainer方法获取原证书的密钥容器
 *        密钥容器名称，更新证书时需要设置，会使用原来的密钥对发出签名请求。
 *        如果在原证书存储在USB KEY中，更新的证书会自动覆盖老证书
 * @return 证书申请请求CSR
 */
function genKeyAndCSR(providerFullName, providerType, cryptFlag, keyContainer) {

    try {
        cenroll.Reset(); //首先Reset
        cenroll.ProviderName = providerFullName;
        cenroll.ProviderType = providerType;
        cenroll.DeleteRequestCert = false;
        var keyflags = 0;
        if (typeof(cryptFlag) != "number") {
            cryptFlag = 0x00000001; //表示私钥可导出，默认值
        }
        keyflags = keyflags | cryptFlag;

        if (typeof(keyContainer) == "string" && keyContainer != "") {//适用于更新证书
            cenroll.UseExistingKeySet = true;
            cenroll.ContainerName = keyContainer;
        }
        cenroll.HashAlgorithm = "SHA1"; //SHA1
        cenroll.KeySpec = 1;

        var csr = "";
        cenroll.GenKeyFlags = 0x04000000 | keyflags; //1024bits
        //objCEnroll.GenKeyFlags = 0x02000000 | keyflags; //512bits，一旦出错，不再尝试512bits的密钥对
        csr = cenroll.createPKCS10("CN=itrus_enroll", "1.3.6.1.5.5.7.3.2");
        $.cookie("lastSelectedProvider", providerFullName);
        ("lastSelectedProvider")
        return csr.replace(/\r*\n/g, "");
    } catch (e) {
        var keyNotPresent = "指定的密钥服务提供者不能提供服务！可能出现的原因："
            + "\n1、您没有插入USB KEY，或者插入的USB KEY不能识别。"
            + "\n2、您的USB KEY还没有初始化。";
        var keyContainerNotPresent = "指定的KeyContainer不能提供服务！\n如果您正在更新证书，请选择原证书的密钥服务提供者(CSP)。";
        if (-2147023673 == e.number //800704C7 User Canceled
            || -2147418113 == e.number || -2146893795 == e.number //Zhong chao USB key User Canceled when input PIN
            || -2146434962 == e.number //FT ePass2001 USB key User Canceld
            ) {
            return "";
        } else if (-2146893802 == e.number) { //80090016
            if (providerFullName.indexOf("SafeSign") != -1)
                alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            else
                alert(keyContainerNotPresent); //当KeyContainer无法提供服务时，其他KEY会报这个错误
            return "";
        } else if (-2146435060 == e.number //8010000C FTSafe ePass2000没插KEY会报
            || -2146893792 == e.number //80090020 FEITIAN ePassNG没插KEY会报
            ) {
            alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            return "";
        } else if (-2146955245 == e.number) {
            alert("创建新密钥容器错误:0x80081013(00000005)\n提示：请将当前站点加入可信站点！");
            return "";
        }
        else {//创建1024位密钥对或产生CSR时发生其他未知错误，将错误报告给用户
            alert("在证书请求过程中发生错误！\n错误原因：" + e.description + "\n错误代码：" + e.number);
            return "";
        }
    }
}

function genKeyAndCSREx2(providerFullName, providerType, keyLen, cryptFlag, keyContainer, certKeySpec) {
    try {
    	//alert(certKeySpec);
        cenroll.Reset(); //首先Reset
        cenroll.ProviderName = providerFullName;
        cenroll.ProviderType = providerType;
        cenroll.SignAlgOId = szOID_RSA_SHA1RSA;
        cenroll.HashAlgorithm = "SHA1"; //SHA1
        //1：生成密钥在加密位；2：生成密钥在签名位
        cenroll.KeySpec = certKeySpec;        // 20140916 原为 1
        cenroll.DeleteRequestCert = false;
        var keyflags = 0;
        if (typeof(cryptFlag) != "number") {
            cryptFlag = 0x00000001; //表示私钥可导出，默认值
        }
        keyflags = keyflags | cryptFlag;

        var keyLenFlag = 0x04000000;
        if (typeof(keyLen) != "number") {
            keyLenFlag = 0x04000000; // 1024位
        } else {
            if (keyLen == 256) {
                cenroll.SignAlgOId = szOID_SM2_SM3SM2;
                cenroll.HashAlgorithm = "SM3";
            }
            keyLenFlag = keyLen << 16;
        }
        if (typeof(keyContainer) == "string" && keyContainer != "") {//适用于更新证书
            cenroll.UseExistingKeySet = true;
            cenroll.ContainerName = keyContainer;
        }
        var csr = "";
        cenroll.GenKeyFlags = keyLenFlag | keyflags;
        csr = cenroll.generatePKCS10("CN=itrus_enroll", "1.3.6.1.5.5.7.3.2");
        
        return csr.replace(/\r*\n/g, "");
    } catch (e) {
    	alert(e);
        var keyNotPresent = "指定的密钥服务提供者不能提供服务！可能出现的原因："
            + "\n1、您没有插入USB KEY，或者插入的USB KEY不能识别。"
            + "\n2、您的USB KEY还没有初始化。";
        var keyContainerNotPresent = "指定的KeyContainer不能提供服务！\n如果您正在更新证书，请选择原证书的密钥服务提供者(CSP)。";
        if (-2147023673 == e.number //800704C7 User Canceled
            || -2147418113 == e.number || -2146893795 == e.number //Zhong chao USB key User Canceled when input PIN
            || -2146434962 == e.number //FT ePass2001 USB key User Canceld
            ) {
            return "";
        } else if (-2146893802 == e.number) { //80090016
            if (providerFullName.indexOf("SafeSign") != -1)
                alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            else
                alert(keyContainerNotPresent); //当KeyContainer无法提供服务时，其他KEY会报这个错误
            return "";
        } else if (-2146435060 == e.number //8010000C FTSafe ePass2000没插KEY会报
            || -2146893792 == e.number //80090020 FEITIAN ePassNG没插KEY会报
            ) {
            alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            return "";
        } else if (-2146955245 == e.number) {
            alert("创建新密钥容器错误:0x80081013(00000005)\n提示：请将当前站点加入可信站点！");
            return "";
        }
        else {//创建1024位密钥对或产生CSR时发生其他未知错误，将错误报告给用户
            alert("在证书请求过程中发生错误！\n错误原因：" + e.description + "(可能原因：密钥长度与类型不匹配！)\n错误代码：" + e.number);
            return "";
        }
    }
}

function genKeyAndCSREx(providerFullName, providerType, keyLen, cryptFlag, keyContainer) {
    try {
        cenroll.Reset(); //首先Reset
        cenroll.ProviderName = providerFullName;
        cenroll.ProviderType = providerType;
        cenroll.SignAlgOId = szOID_RSA_SHA1RSA;
        cenroll.HashAlgorithm = "SHA1"; //SHA1
        //1：生成密钥在加密位；2：生成密钥在签名位
        cenroll.KeySpec = 2;        // 20140916 原为 1
        cenroll.DeleteRequestCert = false;
        var keyflags = 0;
        if (typeof(cryptFlag) != "number") {
            cryptFlag = 0x00000001; //表示私钥可导出，默认值
        }
        keyflags = keyflags | cryptFlag;

        var keyLenFlag = 0x04000000;
        if (typeof(keyLen) != "number") {
            keyLenFlag = 0x04000000; // 1024位
        } else {
            if (keyLen == 256) {
                cenroll.KeySpec = 2;
                cenroll.SignAlgOId = szOID_SM2_SM3SM2;
                cenroll.HashAlgorithm = "SM3";
            }
            keyLenFlag = keyLen << 16;
        }
        if (typeof(keyContainer) == "string" && keyContainer != "") {//适用于更新证书
            cenroll.UseExistingKeySet = true;
            cenroll.ContainerName = keyContainer;
        }
        var csr = "";
        cenroll.GenKeyFlags = keyLenFlag | keyflags;
        csr = cenroll.generatePKCS10("CN=itrus_enroll", "1.3.6.1.5.5.7.3.2");
        
        return csr.replace(/\r*\n/g, "");
    } catch (e) {
    	alert(e);
        var keyNotPresent = "指定的密钥服务提供者不能提供服务！可能出现的原因："
            + "\n1、您没有插入USB KEY，或者插入的USB KEY不能识别。"
            + "\n2、您的USB KEY还没有初始化。";
        var keyContainerNotPresent = "指定的KeyContainer不能提供服务！\n如果您正在更新证书，请选择原证书的密钥服务提供者(CSP)。";
        if (-2147023673 == e.number //800704C7 User Canceled
            || -2147418113 == e.number || -2146893795 == e.number //Zhong chao USB key User Canceled when input PIN
            || -2146434962 == e.number //FT ePass2001 USB key User Canceld
            ) {
            return "";
        } else if (-2146893802 == e.number) { //80090016
            if (providerFullName.indexOf("SafeSign") != -1)
                alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            else
                alert(keyContainerNotPresent); //当KeyContainer无法提供服务时，其他KEY会报这个错误
            return "";
        } else if (-2146435060 == e.number //8010000C FTSafe ePass2000没插KEY会报
            || -2146893792 == e.number //80090020 FEITIAN ePassNG没插KEY会报
            ) {
            alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            return "";
        } else if (-2146955245 == e.number) {
            alert("创建新密钥容器错误:0x80081013(00000005)\n提示：请将当前站点加入可信站点！");
            return "";
        }
        else {//创建1024位密钥对或产生CSR时发生其他未知错误，将错误报告给用户
            alert("在证书请求过程中发生错误！\n错误原因：" + e.description + "(可能原因：密钥长度与类型不匹配！)\n错误代码：" + e.number);
            return "";
        }
    }
}
//不存储cookie的生成reqBuf的方法
function genKeyAndCSRExNoCookie(providerFullName, providerType, keyLen, cryptFlag, keyContainer) {
    try {
        cenroll.Reset(); //首先Reset
        cenroll.ProviderName = providerFullName;
        cenroll.ProviderType = providerType;
        cenroll.SignAlgOId = szOID_RSA_SHA1RSA;
        cenroll.HashAlgorithm = "SHA1"; //SHA1
        //1：生成密钥在加密位；2：生成密钥在签名位
        cenroll.KeySpec = 2;
        //alert();// 20140916 原为 1
        cenroll.DeleteRequestCert = false;
        var keyflags = 0;
        if (typeof(cryptFlag) != "number") {
            cryptFlag = 0x00000001; //表示私钥可导出，默认值
        }
        keyflags = keyflags | cryptFlag;

        var keyLenFlag = 0x04000000;
        if (typeof(keyLen) != "number") {
            keyLenFlag = 0x04000000; // 1024位
        } else {
            if (keyLen == 256) {
                cenroll.KeySpec = 2;
                cenroll.SignAlgOId = szOID_SM2_SM3SM2;
                cenroll.HashAlgorithm = "SM3";
            }
            keyLenFlag = keyLen << 16;
        }
        if (typeof(keyContainer) == "string" && keyContainer != "") {//适用于更新证书
            cenroll.UseExistingKeySet = true;
            cenroll.ContainerName = keyContainer;
        }
        var csr = "";
        cenroll.GenKeyFlags = keyLenFlag | keyflags;
        csr = cenroll.generatePKCS10("CN=itrus_enroll", "1.3.6.1.5.5.7.3.2");
        return csr.replace(/\r*\n/g, "");
    } catch (e) {
        var keyNotPresent = "指定的密钥服务提供者不能提供服务！可能出现的原因："
            + "\n1、您没有插入USB KEY，或者插入的USB KEY不能识别。"
            + "\n2、您的USB KEY还没有初始化。";
        var keyContainerNotPresent = "指定的KeyContainer不能提供服务！\n如果您正在更新证书，请选择原证书的密钥服务提供者(CSP)。";
        if (-2147023673 == e.number //800704C7 User Canceled
            || -2147418113 == e.number || -2146893795 == e.number //Zhong chao USB key User Canceled when input PIN
            || -2146434962 == e.number //FT ePass2001 USB key User Canceld
            ) {
            return "";
        } else if (-2146893802 == e.number) { //80090016
            if (providerFullName.indexOf("SafeSign") != -1)
                alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            else
                alert(keyContainerNotPresent); //当KeyContainer无法提供服务时，其他KEY会报这个错误
            return "";
        } else if (-2146435060 == e.number //8010000C FTSafe ePass2000没插KEY会报
            || -2146893792 == e.number //80090020 FEITIAN ePassNG没插KEY会报
            ) {
            alert(keyNotPresent); //捷德的KEY没插KEY会报这个错误
            return "";
        } else if (-2146955245 == e.number) {
            alert("创建新密钥容器错误:0x80081013(00000005)\n提示：请将当前站点加入可信站点！");
            return "";
        }
        else {//创建1024位密钥对或产生CSR时发生其他未知错误，将错误报告给用户
            alert("在证书请求过程中发生错误！\n错误原因：" + e.description + "(可能原因：密钥长度与类型不匹配！)\n错误代码：" + e.number);
            return "";
        }
    }
}

/**
 * IsValidBrowser 判断是否IE浏览器
 * @return true:是IE，false:不是IE
 */
function IsValidBrowser() {
    if (navigator.userAgent.indexOf("MSIE")>0||navigator.userAgent.toLocaleLowerCase().indexOf("trident")>-1) {
        //是IE浏览器
        return true;
    } else {
        return false;
    }
}
/**
 * IsWindowsOfOS 判断是否windows操作系统
 * @return true是windows ； false非windows
 */
function IsWindowsOfOS() {
    if (navigator.userAgent.indexOf("NT") >= 0) {
//		判断windows系统
//		navigator.userAgent.substr(navigator.userAgent.indexOf("NT")+3, 3)
//		5.1是windowsXP系统，6.0是Vista系统，6.1是Windows7系统
        return true;
    } else {
        return false;
    }
}

function URLDecode(psEncodeString) {
    // Create a regular expression to search all +s in the string
    var lsRegExp = /\+/g;
    // Return the decoded string
    return unescape(String(psEncodeString).replace(lsRegExp, " "));
}

function installCertKmc(certSign, certKmc, kmcReq1, kmcReq2, kmcReq3, kmcRep1, kmcRep2, kmcRep3, installMode) {

    if (certKmc.length < 1)
        certKmc = kmcRep2;
    var kmcRep1Arr = kmcRep1.split("&");
    var kmcRep1Map = {};
    for (var i = 0; i < kmcRep1Arr.length; i++) {
        var nv = kmcRep1Arr[i].split("=");
        kmcRep1Map[nv[0]] = URLDecode(nv[1]);
        //	alert(nv[0] + "\n" + nv[1] + "\n" + kmcRep1Map[nv[0]]);
    }
    try {
        cenroll.Reset();
        cenroll.containerName = "";
        cenroll.KeySpec = 1;
        cenroll.DeleteRequestCert = false;
        cenroll.WriteCertToCSP = true;
        if(installMode == "ENCRYPT") {
            //不解密KMC信息
            var ret = cenroll.installEncCert(
                certSign,
                kmcRep1Map["userSeal"],
                certKmc,
                kmcRep1Map["encPrivateKeyUser"],
                kmcRep1Map["userCipher"],
                kmcRep1Map["userIV"]
            );
        } else {
            var ret = cenroll.acceptSeal(
                certSign,
                kmcRep1Map["userSeal"],
                certKmc,
                kmcRep1Map["encPrivateKeyUser"],
                kmcRep1Map["userCipher"],
                kmcRep1Map["userIV"]
            );
        }

        return true;
    } catch (e) {
        alert("安装加密证书发生错误！\n错误号: " + e.number + "\n错误描述: " + e.description);
        return false;
    }
}

function useLegibleName(boolean) {
    if (typeof(boolean) == "undefined")
        return _useLegibleName;
    if (typeof(boolean) != "boolean" && boolean != "true") {
        boolean = false;
    }
    if (boolean) {
        if (_useLegibleName == true) return; //防止重复调用addAllowedProvider
        _useLegibleName = true
        for (var legibleName in legibleNameMap) {
            addGlobalAllowedProvider(legibleNameMap[legibleName]);
        }
    } else {
        _useLegibleName = false
        globalAllowedProviders = new Array(); // clean
    }
}

function getProviderName(providerFullName) {
    if (useLegibleName()) {
        for (var legibleName in legibleNameMap) {
            if (legibleNameMap[legibleName] == providerFullName)
                return legibleName;
        }
    }
    return providerFullName;
}

function getProviderFullName(providerName) {
    if (legibleNameMap[providerName]) {
        return legibleNameMap[providerName];
    }
    return providerName
}

function addGlobalAllowedProvider(allowedProviders) {
    var i = globalAllowedProviders.length;
    if (typeof(allowedProviders) == "string") {
        globalAllowedProviders[i] = allowedProviders;
    } else if (typeof(allowedProviders) == "object" && allowedProviders.length > 0) {
        for (var j = 0; j < allowedProviders.length; j++) {
            globalAllowedProviders[i + j] = allowedProviders[j];
        }
    }
}

function initXEnroll() {
    useLegibleName(true);
}

function loadItrusEnroll() {
    var cenroll = null;
    var urlArray = new Array();
    urlArray = window.location.toString().split('/');
    var base = urlArray[0]+'//' + window.location.host + '/' + urlArray[3];

    var ieCenrollObject = "<object id='cenroll' codebase='" + base + "/download" + "/itruscertctl.cab#version=1,1,2013,801' classid='clsid:DD2257CE-4FEE-455A-AD8B-860BEEE25ED6'></object>";
    var ffCenrollObject = "<embed id='cenroll' type='application/iTrusCertEnroll.CertEnroll.Version.1' width='0' height='0'></embed>";
   if (IsValidBrowser()) {
        cenroll = $(ieCenrollObject).appendTo(document.body)[0];
    } else {
        var length = navigator.plugins.length;
        var check_install = 0;
        for (var i = 0; i < length; i++) {
            var index = (navigator.plugins[i].name).indexOf("iTrusPTA");
            if (index > -1) {
                check_install ++;
                break;
            }
        }
        if (check_install > 0) {
            cenroll = $(ffCenrollObject).appendTo(document.body)[0];
            try{
                cenroll.Version();
            }catch(e){
                var href = window.location.href.toString();
                if(href.indexOf("/console")==-1&&href.indexOf("/offlineCA")==-1&&href.indexOf("/ca")==-1){
                    //if (window.location.href != base + "/showDownPTA") {
                      //  window.location.href = base + "/showDownPTA";
                    //}
                }
            }
            //cenroll = $(ffCenrollObject).appendTo(document.body)[0];
        } else {
            var href = window.location.href.toString();
            if(href.indexOf("/console")==-1&&href.indexOf("/offlineCA")==-1&&href.indexOf("/ca")==-1){
                //if (window.location.href != base + "/showDownPTA") {
                  //  window.location.href = base + "/showDownPTA";
                //}
            }
        }
    }
    return cenroll;
}

function filter(str){
	if(str){
		str = str.replace(/\+/g,"%2B");
	    str = str.replace(/\&/g,"%26");	
	}
    return str;
}
