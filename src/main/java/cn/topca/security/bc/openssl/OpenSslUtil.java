package cn.topca.security.bc.openssl;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 结合BouncyCastle的一些有用的工具集
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-25 12:44
 */
public class OpenSslUtil {
    /**
     * 转换SubjectPublicKeyInfo到PublicKey对象
     *
     * @param publicKeyInfo BC的SubjectPublicKey对象
     * @return Java标准公钥对象
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public static PublicKey convertPublicKey(SubjectPublicKeyInfo publicKeyInfo) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(publicKeyInfo.getAlgorithmId().getAlgorithm().getId());
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyInfo.getEncoded()));
    }

    /**
     * 解码PEM编码格式的数字证书
     *
     * @param pemCert PEM格式数字证书
     * @return
     */
    public static Certificate decodePEMCert(String pemCert) {
        try {
            PEMReader reader = new PEMReader(new StringReader(pemCert));
            return (Certificate) reader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码PEM编码格式的私钥
     *
     * @param pemKey   PEM格式私钥
     * @param password 私钥保护密码
     * @return
     */
    public static PrivateKey decodePEMPrivateKey(String pemKey, final char[] password) {
        try {
            PEMReader reader = new PEMReader(new StringReader(pemKey), new PasswordFinder() {
                public char[] getPassword() {
                    return password;
                }
            });
            return ((KeyPair) reader.readObject()).getPrivate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
