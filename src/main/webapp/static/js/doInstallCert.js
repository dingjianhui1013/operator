/**
 * 安装证书方法
 * @param data
 */
var result = false;
function DoInstallCert(data) {
	var _result = false;
	var certSignBufP7 = data.signBufP7;
	
	/*if (certSignBufP7.length > 0) {
		
		_result = installCert(certSignBufP7);
		result = _result;
	}
	if(!_result)
		return false;
		
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
			
			_result = installCertKmc(certSignBuf, certKmcBuf, null, null, null,
					certKmcRep1, certKmcRep2, null, installMode);
			result = _result;
		}
	}
	return _result;*/
	
	if(certSignBufP7.length > 0 && data.kmcvalid == 0){
		_result = installCert(certSignBufP7);
		result = _result;
		
	}else if(certSignBufP7.length > 0 && data.kmcvalid == 1){
		_result = installCert(certSignBufP7);
		result = _result;
		
		if(!_result)
			return _result;
		
		var certSignBuf = data.signBuf;
		if (!certSignBuf || certSignBuf.length == 0) {
			alert("certSignBuf为空,无法安装证书!");
			throw ("");
		}
		
		if (data.kmcvalid == 1) {
			
			
			var certKmcBuf = data.certKmcBuf;
			var certKmcRep1 = data.certKmcRep1;
			var certKmcRep2 = data.certKmcRep2;
			var installMode = data.installMode;
			if (certKmcRep1.length > 0) {
				
				_result = installCertKmc(certSignBuf, certKmcBuf, null, null, null,
						certKmcRep1, certKmcRep2, null, installMode);
				result = _result;
			}
		}
	}
	
	return _result;
}
