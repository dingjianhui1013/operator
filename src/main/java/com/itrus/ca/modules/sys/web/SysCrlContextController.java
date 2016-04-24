/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.sys.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;

import cn.emay.sdk.communication.util.Base64;
import cn.topca.sp.x509.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.log.service.LogUtil;
import com.itrus.ca.modules.sys.entity.SingleCvm;
import com.itrus.ca.modules.sys.entity.SysCrlContext;
import com.itrus.ca.modules.sys.service.SysCrlContextService;

/**
 * 信任源管理Controller
 * 
 * @author WangHongwei
 * @version 2014-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysCrlContext")
public class SysCrlContextController extends BaseController {

	@Autowired
	private SysCrlContextService sysCrlContextService;
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public SysCrlContext get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return sysCrlContextService.get(id);
		} else {
			return new SysCrlContext();
		}
	}

	/**
	 * 跳转添加页面
	 * 
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = { "insertFrom"})
	public String insetFrom() {
		return "modules/sys/sysCrlContextInsert";
	}
	
	/**
	 * 页面列表显示
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = { "list", "" })
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<SysCrlContext> page = sysCrlContextService
				.find(new Page<SysCrlContext>(request, response));
		model.addAttribute("page", page);
		return "modules/sys/sysCrlContextList";
	}
	
	/**
	 * 查看数据
	 * @param sysCrlContext
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = "form")
	public String form(SysCrlContext sysCrlContext, Model model) {
		model.addAttribute("sysCrlContext", sysCrlContext);
		return "modules/sys/sysCrlContextFrom";
	}
	
	/**
	 * 跳转修改页面
	 * @param sysCrlContext
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = "update")
	public String update(SysCrlContext sysCrlContext, Model model) {
		model.addAttribute("sysCrlContext", sysCrlContext);
		return "modules/sys/sysCrlContextUpdate";
	}

	/**
	 * 修改处理
	 * @param sysCrlContext
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = "updateCrl")
	public String updateCrl(SysCrlContext sysCrlContext, 
			@RequestParam() MultipartFile fileCrl,Model model,
			RedirectAttributes redirectAttributes) {
		String message = null;
		if(fileCrl.getSize()==0){
			sysCrlContextService.save(sysCrlContext);
		}else {
			File file1 = new File("D:\\tmp\\"+System.currentTimeMillis()+"."+fileCrl.getOriginalFilename().substring(
					fileCrl.getOriginalFilename().lastIndexOf(".") + 1));
			if (!file1.exists()) {
				file1.mkdirs();
			}
			try {
				fileCrl.transferTo(file1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				FileInputStream fis = new FileInputStream(file1);
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				//System.out.println(buffer);
				sysCrlContext.setCaCertBuf(buffer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// 检查是否包含CA证书
			if (sysCrlContext.getCaCertBuf() == null
					|| sysCrlContext.getCaCertBuf().length == 0) {
				model.addAttribute("message", "必须选择CA证书");
				return "modules/sys/sysCrlContextInsert";
			}
			try {
				X509Certificate caCert = X509Certificate.getInstance(sysCrlContext.getCaCertBuf());
				sysCrlContext.setIssuerdn(caCert.getIssuerDNString());
				sysCrlContext.setCertSn(caCert.getSerialNumber() + "");
				sysCrlContext.setCertSubject(caCert.getSubjectDNString());
				sysCrlContext.setCertStartTime(caCert.getNotBefore());
				sysCrlContext.setCertEndTime(caCert.getNotAfter()); 
				sysCrlContextService.save(sysCrlContext);
			} catch (Exception e) {
				if (e instanceof SignatureException)
					message = "CRL签名验证失败，请您检查CRL是否为CA签发。";
				else if (e instanceof CertificateException)
					message = "X509Certificate对象实例化失败，请您检查CA证书格式是否正确。";
				else if (e instanceof CRLException)
					message = "X509CRL对象实例化失败，请您检查CRL文件格式是否正确。";
				model.addAttribute("message", message);
				return "redirect:" + Global.getAdminPath()
						+"/sys/sysCrlContext/list/";
			}
		}
		logUtil.saveSysLog("系统管理", "修改信任源"+sysCrlContext.getCrlName()+"成功", null);
		addMessage(redirectAttributes, "修改成功！");
		
		try {
			SingleCvm.getInstance().reInit();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CRLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:" + Global.getAdminPath()
				+"/sys/sysCrlContext/list/";
	}

	
	
	/**
	 * 添加
	 * @param sysCrlContext
	 * @param file
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = "save")
	public String save(
			@Valid SysCrlContext sysCrlContext,
			@RequestParam() MultipartFile file, Model model,
			RedirectAttributes redirectAttributes) {
		File file1 = new File("D:\\tmp\\"+System.currentTimeMillis()+"."+file.getOriginalFilename().substring(
				file.getOriginalFilename().lastIndexOf(".") + 1));
		if (!file1.exists()) {
			file1.mkdirs();
		}
		try {
			file.transferTo(file1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			FileInputStream fis = new FileInputStream(file1);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
		
			sysCrlContext.setCaCertBuf(buffer);
			
			sysCrlContext.setCaCert(Base64.base64Str(buffer));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 检查是否包含CA证书
		if (sysCrlContext.getCaCertBuf() == null
				|| sysCrlContext.getCaCertBuf().length == 0) {
			model.addAttribute("message", "必须选择CA证书");
			return "modules/sys/sysCrlContextInsert";
		}
		String message = null;
		
		try {
			cn.topca.sp.x509.X509Certificate caCert = cn.topca.sp.x509.X509Certificate.getInstance(sysCrlContext.getCaCertBuf());
			sysCrlContext.setIssuerdn(caCert.getIssuerDNString());
			sysCrlContext.setCertSn(caCert.getSerialNumber() + "");
			sysCrlContext.setCertSubject(caCert.getSubjectDNString());
			sysCrlContext.setCertStartTime(caCert.getNotBefore());
			sysCrlContext.setCertEndTime(caCert.getNotAfter()); 
		} catch (Exception e) {
			if (e instanceof SignatureException)
				message = "CRL签名验证失败，请您检查CRL是否为CA签发。";
			else if (e instanceof CertificateException)
				message = "X509Certificate对象实例化失败，请您检查CA证书格式是否正确。";
			else if (e instanceof CRLException)
				message = "X509CRL对象实例化失败，请您检查CRL文件格式是否正确。";
			else{
				message = e.getMessage();
			}
			model.addAttribute("message", message);
			
			return "redirect:" + Global.getAdminPath()
					+"/sys/sysCrlContext/list/";
		}
		sysCrlContextService.save(sysCrlContext);
		
		
		try {
			SingleCvm.getInstance().reInit();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CRLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logUtil.saveSysLog("系统管理", "添加信任源"+sysCrlContext.getCrlName()+"成功", null);
		addMessage(redirectAttributes, "保存信任源管理成功");
		return "redirect:" + Global.getAdminPath()
				+ "/sys/sysCrlContext/list/";
	}

	/**
	 * 删除
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:sysCrlContext:view")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		sysCrlContextService.delete(id);
		logUtil.saveSysLog("系统管理", "删除id为"+id+"的信任源成功", null);
		addMessage(redirectAttributes, "删除信任源管理成功");
		
		try {
			SingleCvm.getInstance().reInit();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CRLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:" + Global.getAdminPath()
				+"/sys/sysCrlContext/list/";
	}

}
