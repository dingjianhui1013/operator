function addMsg(){
	$("#payMsg").html("付款方式：" +
			"**银行 卡号：********************");
}


var result = false;
function DoInstallCert1(data) {
	var certSignBufP7 = data.signBufP7;
	if (certSignBufP7.length > 0) {
		result = installCert(certSignBufP7);
	}
	var certSignBuf = data.signBuf;
	if (!certSignBuf || certSignBuf.length == 0) {
		throw ("");
	}
	if (data.kmcvalid == 1) {
		var certKmcBuf = data.certKmcBuf;
		var certKmcRep1 = data.certKmcRep1;
		var certKmcRep2 = data.certKmcRep2;
		var installMode = data.installMode;
		if (certKmcRep1.length > 0) {
			installCertKmc(certSignBuf, certKmcBuf, null, null, null,
					certKmcRep1, certKmcRep2, null, installMode);
		}
	}
}
//function DoInstallCert1(data) {
//	var certSignBufP7 = data.signBufP7;
//	if (certSignBufP7.length > 0) {
//		result = installCert(certSignBufP7);
//	}
//	var certSignBuf = data.signBuf;
//	if (!certSignBuf || certSignBuf.length == 0) {
//		throw ("");
//	}
//	var certKmcBuf = "";
//	var certKmcRep1 = "";
//	var certKmcRep2 = "";
//	var installMode = "";
//	if (certKmcRep1.length > 0) {
//		installCertKmc(certSignBuf, certKmcBuf, null, null, null,
//				certKmcRep1, certKmcRep2, null, installMode);
//		var sn = cenroll.encCertSerialNumber;
//	}
//}
