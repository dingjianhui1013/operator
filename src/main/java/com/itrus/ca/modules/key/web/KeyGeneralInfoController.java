/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.key.web;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.util.Base64;
import com.itrus.ca.common.config.Global;
import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.utils.AdminPinEncKey;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.web.BaseController;
import com.itrus.ca.modules.profile.entity.ConfigSupplier;
import com.itrus.ca.modules.profile.entity.ConfigSupplierProductRelation;
import com.itrus.ca.modules.profile.service.ConfigSupplierProductRelationService;
import com.itrus.ca.modules.profile.service.ConfigSupplierService;
import com.itrus.ca.modules.sys.entity.Office;
import com.itrus.ca.modules.sys.entity.User;
import com.itrus.ca.modules.sys.utils.UserUtils;
import com.itrus.ca.modules.constant.KeyType;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.entity.KeyManufacturer;
import com.itrus.ca.modules.key.entity.KeyTypeObj;
import com.itrus.ca.modules.key.entity.KeyUsbKey;
import com.itrus.ca.modules.key.service.KeyGeneralInfoService;
import com.itrus.ca.modules.key.service.KeyUsbKeyDepotService;
import com.itrus.ca.modules.key.service.KeyUsbKeyService;
import com.itrus.ca.modules.log.service.LogUtil;

/**
 * key类型信息Controller
 * 
 * @author ZhangJingtao
 * @version 2014-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/key/keyGeneralInfo")
public class KeyGeneralInfoController extends BaseController {

	@Autowired
	private KeyGeneralInfoService keyGeneralInfoService;

	@Autowired
	private ConfigSupplierService configSupplierService;
	
	@Autowired
	private KeyUsbKeyService keyUsbKeyService;
	
	@Autowired
	private ConfigSupplierProductRelationService configSupplierProductRelationService;
	
	
	private LogUtil logUtil = new LogUtil();

	@ModelAttribute
	public KeyGeneralInfo get(@RequestParam(required = false) Long id) {
		if (id != null) {
			return keyGeneralInfoService.get(id);
		} else {
			return new KeyGeneralInfo();
		}
	}

	@RequiresPermissions("key:keyGeneralInfo:view")
	@RequestMapping(value = { "list", "" })
	public String list(KeyGeneralInfo keyGeneralInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model, Long supplierId) {
		User user = UserUtils.getUser();
		Page<KeyGeneralInfo> page = keyGeneralInfoService.find(
				new Page<KeyGeneralInfo>(request, response), keyGeneralInfo,
				supplierId);
		model.addAttribute("page", page);
		model.addAttribute("supplierId", supplierId);
		return "modules/key/keyGeneralInfoList";
	}

	@RequiresPermissions("key:keyGeneralInfo:view")
	@RequestMapping(value = "form")
	public String form(KeyGeneralInfo keyGeneralInfo, Model model,
			Long supplierId) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (keyGeneralInfo!=null&&!StringHelper.isNull(keyGeneralInfo.getDefaultSoPin())) {
			String unlockCipher = "AES";
			SecretKeySpec skeySpec = new SecretKeySpec(
					AdminPinEncKey.adminPinEncKey.substring(0, 16)
							.getBytes(), unlockCipher);
			IvParameterSpec ivSpec = new IvParameterSpec(
					AdminPinEncKey.adminPinEncKey.substring(16, 32)
							.getBytes());
			Cipher cipher = Cipher.getInstance(unlockCipher
					+ "/CBC/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec, null);
			byte[] decadminpin = cipher.doFinal(com.itrus.util.Base64
					.decode(keyGeneralInfo.getDefaultSoPin()
							.getBytes()));
			String sdecadminpin = new String(decadminpin);
			keyGeneralInfo.setDefaultSoPin(sdecadminpin);	
		}
		
		model.addAttribute("keyGeneralInfo", keyGeneralInfo);
		model.addAttribute("supplierId", supplierId);
		KeyType keyTypes = new KeyType();
		List<KeyTypeObj> keyTypeObjs = keyTypes.getTypeList();

		model.addAttribute("keyTypeObjs", keyTypeObjs);
		return "modules/key/keyGeneralInfoForm";
	}

	@RequiresPermissions("key:keyGeneralInfo:edit")
	@RequestMapping(value = "save")
	public String save(KeyGeneralInfo keyGeneralInfo, Long supplierId,
			Model model, RedirectAttributes redirectAttributes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		if (!beanValidator(model, keyGeneralInfo)) {
			return form(keyGeneralInfo, model, supplierId);
		}

		if (keyGeneralInfo.getId() == null) {
			List<KeyGeneralInfo> generalInfos = keyGeneralInfoService
					.findByName(keyGeneralInfo.getName());
			if (generalInfos.size() > 0) {
				model.addAttribute("keyGeneralInfo", keyGeneralInfo);
				model.addAttribute("supplierId", supplierId);
				model.addAttribute("message", "'"+keyGeneralInfo.getName()+"'"+"Key类型已被使用，请重新选择！");
				KeyType keyTypes = new KeyType();
				List<KeyTypeObj> keyTypeObjs = keyTypes.getTypeList();

				model.addAttribute("keyTypeObjs", keyTypeObjs);
				return "modules/key/keyGeneralInfoForm";
			}
		}
		
		ConfigSupplier supplier = configSupplierService.get(supplierId);
		//supplier.setId(supplierId);
		//KeyManufacturer manu = new KeyManufacturer();
		//manu.setId(manufacturerId);
		//keyGeneralInfo.setKeyManufacturer(manu);
		keyGeneralInfo.setConfigSupplier(supplier);
		if (keyGeneralInfo.getDefaultSoPinType().equals("nonauto")) {// 处理一次加密
			String adminPin = keyGeneralInfo.getDefaultSoPin();
			String unlockCipher = "AES";
			SecretKeySpec skeySpec = new SecretKeySpec(
					AdminPinEncKey.adminPinEncKey.substring(0, 16).getBytes(),
					unlockCipher);
			IvParameterSpec ivSpec = new IvParameterSpec(
					AdminPinEncKey.adminPinEncKey.substring(16, 32).getBytes());
			Cipher cipher = Cipher.getInstance(unlockCipher
					+ "/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec, null);
			byte[] encadminpin = cipher.doFinal(adminPin.getBytes());
			String sencadminpin = new String(
					new com.itrus.util.Base64().encode(encadminpin));
			keyGeneralInfo.setDefaultSoPin(sencadminpin);
		}
		String detail = "";
		if (keyGeneralInfo.getId()==null) {
			detail = "新建key类型信息："+keyGeneralInfo.getName()+"编号：";
		} else {
			detail = "修改key类型信息："+keyGeneralInfo.getName()+"编号：";
		}
		keyGeneralInfoService.save(keyGeneralInfo);
		
		ConfigSupplierProductRelation configSupplierProductRelation = new ConfigSupplierProductRelation();
		
		configSupplierProductRelation.setConfigSupplier(supplier);
		configSupplierProductRelation.setProductType(Integer.parseInt(keyGeneralInfo.getId().toString()));
		
		configSupplierProductRelationService.save(configSupplierProductRelation);
		
		
		
		addMessage(redirectAttributes, "保存key类型信息'" + keyGeneralInfo.getName()
				+ "'成功");
		logUtil.saveSysLog("库存管理", detail+keyGeneralInfo.getId(), "");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyGeneralInfo/?repage&supplierId="
				+ supplierId;

	}

	@RequiresPermissions("key:keyGeneralInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes,
			Long manufacturerId, Model model) {

		List<KeyUsbKey> kuk = keyUsbKeyService.findByGeneralInfoId(id);
		if (kuk.size() > 0) {
			addMessage(redirectAttributes, "Key类型正在使用，不能删除！");
		} else {
			keyGeneralInfoService.delete(id);
			addMessage(redirectAttributes, "删除key类型信息成功!");
		}
		logUtil.saveSysLog("库存信息", "删除key类型信息：编号"+id, "");
		return "redirect:" + Global.getAdminPath()
				+ "/key/keyGeneralInfo/?repage&manufacturerId="
				+ manufacturerId;
	}

	//通过AJAX查询厂商下的所有KEY类型
	@RequestMapping(value = "addGeneralInfo")
	@ResponseBody
	public String delete(Long supplierId) throws JSONException {
		JSONObject  json = new org.json.JSONObject();
		JSONArray array = new JSONArray();		
		try {
			List<KeyGeneralInfo> genes = keyGeneralInfoService.findBySupplierId(supplierId);
			for (KeyGeneralInfo gene : genes) {
				json = new JSONObject();
				json.put("id", gene.getId());
				json.put("name", gene.getName());
				array.put(json);
			}				
			// 检查是否有权限操作
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "0");
		}
		return array.toString();
	}
	
}
