package cn.topca.security;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * Collection of static utility methods used by the security framework.
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 14:21
 */
public final class JCAJCEUtils {

    private JCAJCEUtils() {
        // no instantiation
    }

    // lock to use for synchronization
    private static final Object LOCK = JCAJCEUtils.class;

    // cached SecureRandom instance
    private static volatile SecureRandom secureRandom;

    // size of the temporary arrays we use. Should fit into the CPU's 1st
    // level cache and could be adjusted based on the platform
    private final static int ARRAY_SIZE = 4096;

    /**
     * Get the size of a temporary buffer array to use in order to be
     * cache efficient. totalSize indicates the total amount of data to
     * be buffered. Used by the engineUpdate(ByteBuffer) methods.
     */
    public static int getTempArraySize(int totalSize) {
        return Math.min(ARRAY_SIZE, totalSize);
    }

    /**
     * Get a SecureRandom instance. This method should me used by JDK
     * internal code in favor of calling "new SecureRandom()". That needs to
     * iterate through the provider table to find the default SecureRandom
     * implementation, which is fairly inefficient.
     */
    public static SecureRandom getSecureRandom() {
        // we use double checked locking to minimize synchronization
        // works because we use a volatile reference
        SecureRandom r = secureRandom;
        if (r == null) {
            synchronized (LOCK) {
                r = secureRandom;
                if (r == null) {
                    r = new SecureRandom();
                    secureRandom = r;
                }
            }
        }
        return r;
    }

    /**
     * 判断是否为国产算法
     *
     * @param algorithm 算法名称或OID
     * @return {@code ture}国产算法
     */
    public static boolean isGMAlgorithm(String algorithm) {
        return algorithm.startsWith("1.2.156.10197.1") || algorithm.toUpperCase().matches(".*SM[1-9]|SSF33");
    }

    /**
     * 是否为有效的OID
     *
     * @param identifier OID
     * @return
     */
    public static boolean isValidIdentifier(String identifier) {
        if (identifier.length() < 3
                || identifier.charAt(1) != '.') {
            return false;
        }
        char first = identifier.charAt(0);
        if (first < '0' || first > '2') {
            return false;
        }
        boolean periodAllowed = false;
        for (int i = identifier.length() - 1; i >= 2; i--) {
            char ch = identifier.charAt(i);
            if ('0' <= ch && ch <= '9') {
                periodAllowed = true;
                continue;
            }
            if (ch == '.') {
                if (!periodAllowed) {
                    return false;
                }
                periodAllowed = false;
                continue;
            }
            return false;
        }
        return periodAllowed;
    }

    /**
     * 注册加密服务提供者
     *
     * @param provider 加密服务提供者
     * @param position 优先级
     */
    public static void register(Provider provider, int position) {
        if (Security.getProvider(provider.getName()) == null) {
            if (position > 0)
                Security.insertProviderAt(provider, position);
            else
                Security.addProvider(provider);
        }
    }

    /**
     * 注册加密服务提供者
     *
     * @param provider 加密服务提供者
     */
    public static void register(Provider provider) {
        register(provider, -1);
    }

}