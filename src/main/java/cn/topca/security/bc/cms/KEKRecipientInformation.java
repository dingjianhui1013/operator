package cn.topca.security.bc.cms;

import cn.topca.crypto.Cipher;
import org.bouncycastle.asn1.cms.KEKIdentifier;
import org.bouncycastle.asn1.cms.KEKRecipientInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSTypedStream;
import org.bouncycastle.cms.KEKRecipient;
import org.bouncycastle.cms.KEKRecipientId;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientOperator;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:39
 */
public class KEKRecipientInformation
        extends RecipientInformation {
    private KEKRecipientInfo info;

    KEKRecipientInformation(
            KEKRecipientInfo info,
            AlgorithmIdentifier messageAlgorithm,
            CMSSecureReadable secureReadable,
            AuthAttributesProvider additionalData) {
        super(info.getKeyEncryptionAlgorithm(), messageAlgorithm, secureReadable, additionalData);

        this.info = info;

        KEKIdentifier kekId = info.getKekid();

        this.rid = new KEKRecipientId(kekId.getKeyIdentifier().getOctets());
    }

    /**
     * decrypt the content and return an input stream.
     */
    public CMSTypedStream getContentStream(
            Key key,
            String prov)
            throws CMSException, NoSuchProviderException {
        return getContentStream(key, CMSUtils.getProvider(prov));
    }

    /**
     * decrypt the content and return an input stream.
     */
    public CMSTypedStream getContentStream(
            Key key,
            Provider prov)
            throws CMSException {
        try {
            Cipher keyCipher = CMSEnvelopedHelper.INSTANCE.createSymmetricCipher(
                    keyEncAlg.getAlgorithm().getId(), prov);
            keyCipher.init(Cipher.UNWRAP_MODE, key);
            Key sKey = keyCipher.unwrap(info.getEncryptedKey().getOctets(), getContentAlgorithmName(),
                    Cipher.SECRET_KEY);

            return getContentFromSessionKey(sKey, prov);
        } catch (NoSuchAlgorithmException e) {
            throw new CMSException("can't find algorithm.", e);
        } catch (InvalidKeyException e) {
            throw new CMSException("key invalid in message.", e);
        } catch (NoSuchPaddingException e) {
            throw new CMSException("required padding not supported.", e);
        }
    }

    protected RecipientOperator getRecipientOperator(Recipient recipient)
            throws CMSException, IOException {
        return ((KEKRecipient) recipient).getRecipientOperator(keyEncAlg, messageAlgorithm, info.getEncryptedKey().getOctets());
    }
}