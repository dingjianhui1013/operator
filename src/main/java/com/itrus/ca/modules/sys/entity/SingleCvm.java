package com.itrus.ca.modules.sys.entity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.itrus.ca.modules.sys.service.SysCrlContextService;
import cn.topca.sp.cvm.CVM;
import cn.topca.sp.x509.X509Certificate;

public class SingleCvm {
	
		@Autowired
		private static SysCrlContextService sysCrlContextService;
	
		
	    private static CVM singleCvm;
	
	    private SingleCvm() {

	    }
	    public static CVM getCvm() {
	    	 
	  try {
		  if (singleCvm == null) {
			 	singleCvm = new CVM();
			 	List<SysCrlContext> sysCrlContexts  = sysCrlContextService.findAll();
			 	for(SysCrlContext sysCrlContext:sysCrlContexts){
			 		singleCvm.addSupportCA(X509Certificate.getInstance(sysCrlContext.getCaCertBuf()), sysCrlContext.getCrlUrl(), sysCrlContext.getRetryPolicy(), sysCrlContext.getCheckCrl());	
			 	}
		   }

	} catch (Exception e) {
		
	}
	       return singleCvm;

	 

	    }

	
}
