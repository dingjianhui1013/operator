package cn.topca.security.bc.util;

import cn.topca.security.JCAJCEUtils;
import cn.topca.security.bc.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * OID名称转换
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-03 11:05
 */
public class OIDUtil {
    private OIDUtil() {
    }

    protected static Map oidMap = new HashMap();
    protected static Map nameMap = new HashMap();

    static {
        // add oid
        addSymmetricKeyOid();
        addDigestOid();

        // add name
        addSymmetricKeyName();
    }

    private static void addSymmetricKeyName() {
        nameMap.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), "DESede");
        nameMap.put(GMObjectIdentifiers.sm4.getId(), "SM4");
    }

    private static void addDigestOid() {
        oidMap.put("SHA", OIWObjectIdentifiers.idSHA1);
        oidMap.put("SHA1", OIWObjectIdentifiers.idSHA1);
        oidMap.put("SHA224", NISTObjectIdentifiers.id_sha224);
        oidMap.put("SHA256", NISTObjectIdentifiers.id_sha256);
        oidMap.put("SHA384", NISTObjectIdentifiers.id_sha384);
        oidMap.put("SHA512", NISTObjectIdentifiers.id_sha512);
        oidMap.put("SM3", GMObjectIdentifiers.sm3.getId());
    }

    private static void addSymmetricKeyOid() {
        oidMap.put("DESede", PKCSObjectIdentifiers.des_EDE3_CBC.getId());
        oidMap.put("TripleDES", PKCSObjectIdentifiers.des_EDE3_CBC.getId());
        oidMap.put("SM4", GMObjectIdentifiers.sm4.getId());
    }

    public static String getOID(String name) {
        String oid = (String) oidMap.get(name);
        if (oid != null)
            return oid;
        return name;
    }

    public static String getName(String oid) {
        if (JCAJCEUtils.isValidIdentifier(oid)) {
            String name = (String) nameMap.get(oid);
            if (name != null)
                return name;
        }
        return oid;
    }
}
