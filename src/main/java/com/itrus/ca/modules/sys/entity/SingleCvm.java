package com.itrus.ca.modules.sys.entity;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


import com.itrus.ca.modules.sys.service.SysCrlContextService;
import cn.topca.sp.cvm.CVM;
import cn.topca.sp.x509.X509Certificate;

@Component
public class SingleCvm {

	@Autowired
	SysCrlContextService sysCrlContextService;
	private Boolean isInit = false;
	@Autowired
	private CVM cvm;

	public CVM getCVM() throws CertificateException, NoSuchProviderException, CRLException, IOException {
		if (cvm == null) {
			cvm = new CVM();
		}
		if (!isInit) {
			/*List<SysCrlContext> sysCrlContexts  = sysCrlContextService.findAll();
		 	for(SysCrlContext sysCrlContext:sysCrlContexts){
		 		cvm.addSupportCA(X509Certificate.getInstance(sysCrlContext.getCaCertBuf()), sysCrlContext.getCrlUrl(), sysCrlContext.getRetryPolicy(), sysCrlContext.getCheckCrl());	
		 	}	*/
			isInit = true;
		}
		return cvm;
	}

}
