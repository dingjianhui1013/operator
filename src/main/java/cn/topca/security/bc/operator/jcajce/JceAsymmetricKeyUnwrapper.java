package cn.topca.security.bc.operator.jcajce;

import cn.topca.crypto.Cipher;
import cn.topca.security.bc.jcajce.DefaultJcaJceHelper;
import cn.topca.security.bc.jcajce.NamedJcaJceHelper;
import cn.topca.security.bc.jcajce.ProviderJcaJceHelper;
import cn.topca.security.bc.util.OIDUtil;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.AsymmetricKeyUnwrapper;
import org.bouncycastle.operator.GenericKey;
import org.bouncycastle.operator.OperatorException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 10:52
 */
public class JceAsymmetricKeyUnwrapper extends AsymmetricKeyUnwrapper {
    private OperatorHelper helper = new OperatorHelper(new DefaultJcaJceHelper());
    private PrivateKey privKey;

    public JceAsymmetricKeyUnwrapper(AlgorithmIdentifier algorithmIdentifier, PrivateKey privKey) {
        super(algorithmIdentifier);

        this.privKey = privKey;
    }

    public JceAsymmetricKeyUnwrapper setProvider(Provider provider) {
        this.helper = new OperatorHelper(new ProviderJcaJceHelper(provider));

        return this;
    }

    public JceAsymmetricKeyUnwrapper setProvider(String providerName) {
        this.helper = new OperatorHelper(new NamedJcaJceHelper(providerName));

        return this;
    }

    public GenericKey generateUnwrappedKey(AlgorithmIdentifier encryptedKeyAlgorithm, byte[] encryptedKey)
            throws OperatorException {
        try {
            Key sKey = null;

            Cipher keyCipher = helper.createAsymmetricWrapper(this.getAlgorithmIdentifier().getAlgorithm());
            String keyAlgName = OIDUtil.getName(encryptedKeyAlgorithm.getAlgorithm().getId());
            try {
                keyCipher.init(Cipher.UNWRAP_MODE, privKey);
                sKey = keyCipher.unwrap(encryptedKey, keyAlgName, Cipher.SECRET_KEY);
            } catch (GeneralSecurityException e) {
            } catch (IllegalStateException e) {
            } catch (UnsupportedOperationException e) {
            } catch (ProviderException e) {
            }

            // some providers do not support UNWRAP (this appears to be only for asymmetric algorithms)
            if (sKey == null) {
                keyCipher.init(Cipher.DECRYPT_MODE, privKey);
                sKey = new SecretKeySpec(keyCipher.doFinal(encryptedKey), keyAlgName);
            }

            return new GenericKey(sKey);
        } catch (InvalidKeyException e) {
            throw new OperatorException("key invalid: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new OperatorException("illegal blocksize: " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new OperatorException("bad padding: " + e.getMessage(), e);
        }
    }
}
