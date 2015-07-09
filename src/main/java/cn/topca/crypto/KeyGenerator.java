package cn.topca.crypto;


import cn.topca.security.JCAJCEUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * Crypto代理，用于兼容未签名的JCE
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 14:10
 */

public class KeyGenerator {

    private final static Logger log = LoggerFactory.getLogger(KeyGenerator.class);

    // The provider
    private final Provider provider;
    // The provider implementation (delegate)
    private final KeyGeneratorSpi spi;
    // The name of the service algorithm.
    private final String algorithm;

    // The wrapped service
    private final javax.crypto.KeyGenerator jceService;

    public KeyGenerator(javax.crypto.KeyGenerator keyGenerator) {
        log.debug("#compatibility");
        log.debug("use JCE KeyGenerator for " + keyGenerator.getAlgorithm());
        this.spi = null;
        this.jceService = keyGenerator;
        this.algorithm = keyGenerator.getAlgorithm();
        this.provider = keyGenerator.getProvider();
    }

    protected KeyGenerator(KeyGeneratorSpi spi, Provider provider, String algorithm) {
        this.jceService = null;
        this.spi = spi;
        this.algorithm = algorithm;
        this.provider = provider;
    }

    public static KeyGenerator getInstance(String algorithm)
            throws NoSuchAlgorithmException {
        try {
            javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(algorithm);
            return new KeyGenerator(keyGenerator);
        } catch (Exception e) {
        }
        Provider[] providers = Security.getProviders("KeyGenerator." + algorithm);
        if (providers != null && providers.length > 0) {
            return getInstance(algorithm, providers[0]);
        }
        throw new NoSuchAlgorithmException(algorithm + " KeyGenerator not available");
    }

    public static KeyGenerator getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
        try {
            javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance(algorithm);
            return new KeyGenerator(keyGenerator);
        } catch (Exception e) {
        }
        KeyGeneratorSpi spi = null;
        Service service = provider.getService("KeyGenerator", algorithm);
        if (service != null)
            spi = (KeyGeneratorSpi) service.newInstance(null);
        if (spi != null)
            return new KeyGenerator(spi, provider, service.getAlgorithm());
        throw new NoSuchAlgorithmException("no such " + algorithm + " for " + provider.getName());
    }

    public static KeyGenerator getInstance(String algorithm, String provider) throws NoSuchAlgorithmException,
            NoSuchProviderException {
        Provider p = Security.getProvider(provider);
        if (p == null) {
            throw new NoSuchProviderException(provider);
        }
        return getInstance(algorithm, p);
    }

    /**
     * Returns the provider of this <code>KeyGenerator</code> object.
     *
     * @return the provider of this <code>KeyGenerator</code> object
     */
    public final Provider getProvider() {
        return this.provider;
    }

    /**
     * Returns the algorithm name of this <code>KeyGenerator</code> object.
     * <p/>
     * <p/>
     * This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this
     * <code>KeyGenerator</code> object..
     *
     * @return the algorithm name of this <code>KeyGenerator</code> object.
     */
    public final String getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Initializes this key generator.
     *
     * @param random the source of randomness for this generator
     */
    public final void init(SecureRandom random) {
        if (jceService != null) {
            jceService.init(random);
            return;
        }
        JCEAgentUtils.invokeSpi(spi, "engineInit", new Class[]{SecureRandom.class}, random);
    }

    /**
     * Initializes this key generator with the specified parameter set.
     * <p/>
     * <p/>
     * If this key generator requires any random bytes, it will get them using
     * the {@link SecureRandom <code>SecureRandom</code>} implementation of the
     * highest-priority installed provider as the source of randomness. (If none
     * of the installed providers supply an implementation of SecureRandom, a
     * system-provided source of randomness will be used.)
     *
     * @param params the key generation parameters
     * @throws InvalidAlgorithmParameterException if the given parameters are inappropriate for this key
     *                                            generator
     */
    public final void init(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException {
        init(params, JCAJCEUtils.getSecureRandom());
    }

    /**
     * Initializes this key generator with the specified parameter set and a
     * user-provided source of randomness.
     *
     * @param params the key generation parameters
     * @param random the source of randomness for this key generator
     * @throws InvalidAlgorithmParameterException if <code>params</code> is inappropriate for this key
     *                                            generator
     */
    public final void init(AlgorithmParameterSpec params, SecureRandom random)
            throws InvalidAlgorithmParameterException {
        if (jceService != null) {
            jceService.init(params, random);
            return;
        }
        JCEAgentUtils.invokeSpi(spi, "engineInit", new Class[]{AlgorithmParameterSpec.class,
                SecureRandom.class}, params, random);
    }

    /**
     * Initializes this key generator for a certain keysize.
     * <p/>
     * <p/>
     * If this key generator requires any random bytes, it will get them using
     * the {@link SecureRandom <code>SecureRandom</code>} implementation of the
     * highest-priority installed provider as the source of randomness. (If none
     * of the installed providers supply an implementation of SecureRandom, a
     * system-provided source of randomness will be used.)
     *
     * @param keysize the keysize. This is an algorithm-specific metric, specified
     *                in number of bits.
     * @throws InvalidParameterException if the keysize is wrong or not supported.
     */
    public final void init(int keysize) {
        init(keysize, JCAJCEUtils.getSecureRandom());
    }

    /**
     * Initializes this key generator for a certain keysize, using a
     * user-provided source of randomness.
     *
     * @param keysize the keysize. This is an algorithm-specific metric, specified
     *                in number of bits.
     * @param random  the source of randomness for this key generator
     * @throws InvalidParameterException if the keysize is wrong or not supported.
     */
    public final void init(int keysize, SecureRandom random) {
        if (jceService != null) {
            jceService.init(keysize, random);
            return;
        }
        JCEAgentUtils.invokeSpi(spi, "engineInit", new Class[]{int.class, SecureRandom.class},
                keysize, random);
    }

    /**
     * Generates a secret key.
     *
     * @return the new key
     */
    public final SecretKey generateKey() {
        if (jceService != null) {
            return jceService.generateKey();
        }
        return (SecretKey) JCEAgentUtils.invokeSpi(spi, "engineGenerateKey", null);
    }

}