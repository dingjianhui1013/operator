/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.userenroll.service;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.itrus.ca.common.persistence.Page;
import com.itrus.ca.common.service.BaseService;
import com.itrus.ca.common.utils.AdminPinEncKey;
import com.itrus.ca.common.utils.EscapeUtil;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.modules.key.entity.KeyGeneralInfo;
import com.itrus.ca.modules.key.service.KeyUsbKeyInvoiceService;
import com.itrus.ca.modules.service.ItrustProxyUtil;
import com.itrus.ca.modules.userenroll.entity.KeyUnlock;
import com.itrus.ca.modules.userenroll.dao.KeyUnlockDao;

/**
 * 解锁审批Service
 * 
 * @author ZhangJingtao
 * @version 2014-06-22
 */
@Component
@Transactional(readOnly = true)
public class KeyUnlockService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(KeyUnlockService.class);

	@Autowired
	private KeyUnlockDao keyUnlockDao;

	@Value(value = "${ixin.url}")
	private String ixinUrl;

	@Value(value = "${ixin.key}")
	String key;

	@Autowired
	KeyUsbKeyInvoiceService invoiceService;

	public KeyUnlock get(Long id) {
		return keyUnlockDao.findOne(id);
	}

	public Page<KeyUnlock> find(Page<KeyUnlock> page, KeyUnlock keyUnlock, List<String> certSns) {
		DetachedCriteria dc = keyUnlockDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(keyUnlock.getKeySn())) {
			dc.add(Restrictions.like("keySn", "%" + EscapeUtil.escapeLike(keyUnlock.getKeySn()) + "%"));
		}
		if (certSns.size() > 0) {
			dc.add(Restrictions.in("certCn", certSns));
		}
		dc.add(Restrictions.eq(KeyUnlock.DEL_FLAG, KeyUnlock.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return keyUnlockDao.find(page, dc);
	}

	public Page<KeyUnlock> find(Page<KeyUnlock> page, KeyUnlock keyUnlock, String companyString, String attnName) {
		// DetachedCriteria dc = keyUnlockDao.createDetachedCriteria();
		// if (StringUtils.isNotEmpty(keyUnlock.getKeySn())){
		// dc.add(Restrictions.like("keySn",
		// "%"+EscapeUtil.escapeLike(keyUnlock.getKeySn())+"%"));
		// }
		// if (certSns.size()>0) {
		// dc.add(Restrictions.in("certCn", certSns));
		// }
		// dc.add(Restrictions.eq(KeyUnlock.DEL_FLAG,
		// KeyUnlock.DEL_FLAG_NORMAL));
		// dc.addOrder(Order.desc("id"));
		String hql = "select ku  from KeyUnlock ku,WorkDealInfo wdi where ku.certCn = wdi.certSn and ku.keySn like ?  and  ku.delFlag = 0"
				+ " and wdi.workUser.contactName like ? and wdi.workCompany.companyName like ? ";

		// find(Page<E> page, String qlString, Object... parameter);
//		return keyUnlockDao.find(page, hql,
//				EscapeUtil.escapeLike(keyUnlock.getKeySn() == null ? "%%" : keyUnlock.getKeySn()),
//				 StringUtils.isBlank(attnName) ? "%%" : attnName,
//				StringUtils.isBlank(companyString) ? "%%" : companyString);
		return keyUnlockDao.find(page, hql,StringUtils.isBlank(keyUnlock.getKeySn())?"%%" : "%"+keyUnlock.getKeySn()+"%"
				,StringUtils.isBlank(attnName)?"%%" : "%"+attnName+"%"
				,StringUtils.isBlank(companyString)?"%%" : "%"+companyString+"%");
	}

	@Transactional(readOnly = false)
	public void save(KeyUnlock keyUnlock) {
		keyUnlockDao.save(keyUnlock);
	}

	/**
	 * 根据key序列号和证书序列号查询解锁请求
	 * 
	 * @param keySn
	 * @return
	 */
	public KeyUnlock findByKeySn(String keySn, String certSn) {
		DetachedCriteria dc = keyUnlockDao.createDetachedCriteria();
		dc.add(Restrictions.eq("keySn", keySn));
		dc.add(Restrictions.eq("certCn", certSn));
		dc.add(Restrictions.ne("status", "UNLOCK"));
		dc.add(Restrictions.ne("status", "ISFINISH"));

		dc.add(Restrictions.eq(KeyUnlock.DEL_FLAG, KeyUnlock.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		List<KeyUnlock> all = keyUnlockDao.find(dc);
		if (all.size() != 0) {
			return all.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		keyUnlockDao.deleteById(id);
	}

	/**
	 * * nonauto:手动指定管理员PIN autoht:"自动计算：海泰", autoft:"自动计算：飞天",
	 * autokoal:"自动计算：格尔(宁波CA)"
	 * 
	 * @param id
	 * @param agree
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	@Transactional(readOnly = false)
	public boolean audit(Long id, Integer agree, String msg, HttpServletRequest request)
			throws ClientProtocolException, IOException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		KeyUnlock unlock = keyUnlockDao.findOne(id);
		if (agree == 1) {
			String respCode = new String();
			String recode = unlock.getReqCode();
			KeyGeneralInfo generalInfo = invoiceService.findDefaultSoPinTypeByKeysn(unlock.getKeySn());
			if (generalInfo == null) {
				return false;
			}
			String adminPinType = generalInfo.getDefaultSoPinType();
			String adminPin = generalInfo.getDefaultSoPin();
			if (adminPin != null && !adminPin.equals("")) {
				String unlockCipher = "AES";
				SecretKeySpec skeySpec = new SecretKeySpec(AdminPinEncKey.adminPinEncKey.substring(0, 16).getBytes(),
						unlockCipher);
				IvParameterSpec ivSpec = new IvParameterSpec(
						AdminPinEncKey.adminPinEncKey.substring(16, 32).getBytes());
				Cipher cipher = Cipher.getInstance(unlockCipher + "/CBC/PKCS5Padding");

				cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec, null);
				byte[] decadminpin = cipher.doFinal(com.itrus.util.Base64.decode(adminPin.getBytes()));
				adminPin = new String(decadminpin);
			} else {
				adminPin = "";
			}

			// keyunlocks/key/{sn}
			String url = ixinUrl + "/rest/key/" + unlock.getKeySn() + "/unlock";
			System.out.println("url:" + url);
			System.out.println("adminPin:" + adminPin);
			System.out.println("adminPin:" + adminPinType);
			HttpClient client = new DefaultHttpClient();
			Map<String, String> params = new HashMap<String, String>();
			params.put("reCode", recode);
			params.put("adminPinType", adminPinType);
			params.put("adminPin", adminPin);
			respCode = ItrustProxyUtil.sendPost(client, url, params, request, key);
			try {
				JSONObject json = new JSONObject(respCode);
				if (json.getInt("status") == 0) {
					unlock.setRepCode(json.getString("repCode"));
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH, 3);
					unlock.setValidDate(calendar.getTime());
					unlock.setStatus("APPROVE");
					unlock.setRemarks(msg);
					keyUnlockDao.save(unlock);
				} else {
					return false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return false;
			}
		} else {
			unlock.setStatus(agree == 1 ? "APPROVE" : "FORBID");
			unlock.setRemarks(msg);
			keyUnlockDao.save(unlock);
		}
		return false;
	}

}
