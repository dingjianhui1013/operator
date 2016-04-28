package com.itrus.ca.modules.sys.entity;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.List;

import com.itrus.ca.common.utils.SpringContextHolder;
import com.itrus.ca.modules.sys.service.SysCrlContextService;
import cn.topca.sp.cvm.CVM;
import cn.topca.sp.x509.X509Certificate;

public class SingleCvm {

	// @Autowired
	// private SysCrlContextService sysCrlContextService;

	private static SingleCvm singleCvm;
	private Boolean isInit = false;
	private CVM cvm;

	private SingleCvm() {
	}

	public static boolean initialize() throws CertificateException, NoSuchProviderException, CRLException, IOException {
		return getInstance() != null;
	}

	synchronized public static SingleCvm getInstance()
			throws CertificateException, NoSuchProviderException, CRLException, IOException {
		if (singleCvm == null)
			singleCvm = new SingleCvm();
		if (!singleCvm.init()) {
			return null;
		}
		return singleCvm;
	}

	private boolean init() throws CertificateException, NoSuchProviderException, CRLException, IOException {
		if (!isInit) {

			if (cvm == null) {
				cvm = new CVM();
			}

			SysCrlContextService sysCrlContextService = SpringContextHolder.getBean(SysCrlContextService.class);
			List<SysCrlContext> sysCrlContexts = sysCrlContextService.findAll();
			if (sysCrlContexts != null && sysCrlContexts.size() > 0) {
				for (SysCrlContext sysCrlContext : sysCrlContexts) {
					cvm.addSupportCA(X509Certificate.getInstance(sysCrlContext.getCaCertBuf()), null,
							sysCrlContext.getCrlUrl(), sysCrlContext.getRetryPolicy(), sysCrlContext.getCheckCrl(),
							true);
				}
				isInit = true;
				return true;
			}
			return false;
		}
		return true;
	}

	public void reInit() throws CertificateException, NoSuchProviderException, CRLException, IOException {
		if (cvm != null) {
			cvm.clear();
			isInit = false;
			init();
		}

	}

	public CVM getCVM() throws CertificateException, NoSuchProviderException, CRLException, IOException {
		return cvm;
	}
}
