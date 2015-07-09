package cn.topca.security.bc.cms;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cms.PasswordRecipientInfo;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.PasswordRecipient;
import org.bouncycastle.cms.PasswordRecipientId;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientOperator;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 扩展BouncyCastle用于支持国产算法
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-28 19:43
 */
public class PasswordRecipientInformation
        extends RecipientInformation {
    static Map KEYSIZES = new HashMap();
    static Map BLOCKSIZES = new HashMap();

    static {
        BLOCKSIZES.put(CMSAlgorithm.DES_EDE3_CBC, new Integer(8));
        BLOCKSIZES.put(CMSAlgorithm.AES128_CBC, new Integer(16));
        BLOCKSIZES.put(CMSAlgorithm.AES192_CBC, new Integer(16));
        BLOCKSIZES.put(CMSAlgorithm.AES256_CBC, new Integer(16));
        BLOCKSIZES.put(CMSAlgorithm.SM4, new Integer(16));

        KEYSIZES.put(CMSAlgorithm.DES_EDE3_CBC, new Integer(192));
        KEYSIZES.put(CMSAlgorithm.AES128_CBC, new Integer(128));
        KEYSIZES.put(CMSAlgorithm.AES192_CBC, new Integer(192));
        KEYSIZES.put(CMSAlgorithm.AES256_CBC, new Integer(256));
        KEYSIZES.put(CMSAlgorithm.SM4, new Integer(128));
    }

    private PasswordRecipientInfo info;

    PasswordRecipientInformation(
            PasswordRecipientInfo info,
            AlgorithmIdentifier messageAlgorithm,
            CMSSecureReadable secureReadable,
            AuthAttributesProvider additionalData) {
        super(info.getKeyEncryptionAlgorithm(), messageAlgorithm, secureReadable, additionalData);

        this.info = info;
        this.rid = new PasswordRecipientId();
    }

    /**
     * return the object identifier for the key derivation algorithm, or null
     * if there is none present.
     *
     * @return OID for key derivation algorithm, if present.
     */
    public String getKeyDerivationAlgOID() {
        if (info.getKeyDerivationAlgorithm() != null) {
            return info.getKeyDerivationAlgorithm().getObjectId().getId();
        }

        return null;
    }

    /**
     * return the ASN.1 encoded key derivation algorithm parameters, or null if
     * there aren't any.
     *
     * @return ASN.1 encoding of key derivation algorithm parameters.
     */
    public byte[] getKeyDerivationAlgParams() {
        try {
            if (info.getKeyDerivationAlgorithm() != null) {
                DEREncodable params = info.getKeyDerivationAlgorithm().getParameters();
                if (params != null) {
                    return params.getDERObject().getEncoded();
                }
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("exception getting encryption parameters " + e);
        }
    }

    /**
     * return an AlgorithmParameters object representing the parameters to the
     * key derivation algorithm to the recipient.
     *
     * @return AlgorithmParameters object, null if there aren't any.
     */
    public AlgorithmParameters getKeyDerivationAlgParameters(String provider)
            throws NoSuchProviderException {
        return getKeyDerivationAlgParameters(CMSUtils.getProvider(provider));
    }

    /**
     * return an AlgorithmParameters object representing the parameters to the
     * key derivation algorithm to the recipient.
     *
     * @return AlgorithmParameters object, null if there aren't any.
     */
    public AlgorithmParameters getKeyDerivationAlgParameters(Provider provider) {
        try {
            if (info.getKeyDerivationAlgorithm() != null) {
                DEREncodable params = info.getKeyDerivationAlgorithm().getParameters();
                if (params != null) {
                    AlgorithmParameters algP = AlgorithmParameters.getInstance(info.getKeyDerivationAlgorithm().getObjectId().toString(), provider);

                    algP.init(params.getDERObject().getEncoded());

                    return algP;
                }
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("exception getting encryption parameters " + e);
        }
    }

    protected RecipientOperator getRecipientOperator(Recipient recipient)
            throws CMSException, IOException {
        PasswordRecipient pbeRecipient = (PasswordRecipient) recipient;
        AlgorithmIdentifier kekAlg = AlgorithmIdentifier.getInstance(info.getKeyEncryptionAlgorithm());
        ASN1Sequence kekAlgParams = (ASN1Sequence) kekAlg.getParameters();
        DERObjectIdentifier kekAlgName = DERObjectIdentifier.getInstance(kekAlgParams.getObjectAt(0));
        PBKDF2Params params = PBKDF2Params.getInstance(info.getKeyDerivationAlgorithm().getParameters());

        byte[] derivedKey;
        int keySize = ((Integer) KEYSIZES.get(kekAlgName)).intValue();

        if (pbeRecipient.getPasswordConversionScheme() == PasswordRecipient.PKCS5_SCHEME2) {
            PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator();

            gen.init(PBEParametersGenerator.PKCS5PasswordToBytes(pbeRecipient.getPassword()), params.getSalt(), params.getIterationCount().intValue());

            derivedKey = ((KeyParameter) gen.generateDerivedParameters(keySize)).getKey();
        } else {
            PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator();

            gen.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(pbeRecipient.getPassword()), params.getSalt(), params.getIterationCount().intValue());

            derivedKey = ((KeyParameter) gen.generateDerivedParameters(keySize)).getKey();
        }

        return pbeRecipient.getRecipientOperator(AlgorithmIdentifier.getInstance(kekAlg.getParameters()), messageAlgorithm, derivedKey, info.getEncryptedKey().getOctets());
    }
}