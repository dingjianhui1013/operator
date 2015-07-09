package cn.topca.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.MacSpi;
import javax.crypto.ShortBufferException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * Crypto代理，用于兼容未签名的JCE
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 14:26
 */
public class Mac implements Cloneable {

    private final static Logger log = LoggerFactory.getLogger(Mac.class);

    // The provider
    private final Provider provider;
    // The provider implementation (delegate)
    private MacSpi spi;
    // The name of the service algorithm.
    private final String algorithm;

    // The wrapped service
    private final javax.crypto.Mac jceService;

    // Has this object been initialized?
    private boolean initialized = false;

    /**
     * Creates a MAC object.
     *
     * @param macSpi    the delegate
     * @param provider  the provider
     * @param algorithm the algorithm
     */
    protected Mac(MacSpi macSpi, Provider provider, String algorithm) {
        this.spi = macSpi;
        this.jceService = null;
        this.provider = provider;
        this.algorithm = algorithm;
    }

    public Mac(javax.crypto.Mac mac) {
        this.spi = null;
        this.jceService = mac;
        this.provider = mac.getProvider();
        this.algorithm = mac.getAlgorithm();
    }

    /**
     * Returns the algorithm name of this <code>Mac</code> object.
     * <p/>
     * <p>This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this
     * <code>Mac</code> object.
     *
     * @return the algorithm name of this <code>Mac</code> object.
     */
    public final String getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Returns a <code>Mac</code> object that implements the
     * specified MAC algorithm.
     * <p/>
     * <p> This method traverses the list of registered security Providers,
     * starting with the most preferred Provider.
     * A new Mac object encapsulating the
     * MacSpi implementation from the first
     * Provider that supports the specified algorithm is returned.
     * <p/>
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *                  See the Mac section in the <a href=
     *                  "{@docRoot}/../technotes/guides/security/StandardNames.html#Mac">
     *                  Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     *                  for information about standard algorithm names.
     * @return the new <code>Mac</code> object.
     * @throws NoSuchAlgorithmException if no Provider supports a
     *                                  MacSpi implementation for the
     *                                  specified algorithm.
     * @see java.security.Provider
     */
    public static final Mac getInstance(String algorithm)
            throws NoSuchAlgorithmException {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(algorithm);
            return new Mac(mac);
        } catch (Exception e) {
        }
        Provider[] providers = Security.getProviders("Mac." + algorithm);
        if (providers != null && providers.length > 0) {
            return getInstance(algorithm, providers[0]);
        }
        throw new NoSuchAlgorithmException(algorithm + " Mac not available");
    }

    /**
     * Returns a <code>Mac</code> object that implements the
     * specified MAC algorithm.
     * <p/>
     * <p> A new Mac object encapsulating the
     * MacSpi implementation from the specified provider
     * is returned.  The specified provider must be registered
     * in the security provider list.
     * <p/>
     * <p> Note that the list of registered providers may be retrieved via
     * the {@link Security#getProviders() Security.getProviders()} method.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *                  See the Mac section in the <a href=
     *                  "{@docRoot}/../technotes/guides/security/StandardNames.html#Mac">
     *                  Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     *                  for information about standard algorithm names.
     * @param provider  the name of the provider.
     * @return the new <code>Mac</code> object.
     * @throws NoSuchAlgorithmException if a MacSpi
     *                                  implementation for the specified algorithm is not
     *                                  available from the specified provider.
     * @throws NoSuchProviderException  if the specified provider is not
     *                                  registered in the security provider list.
     * @throws IllegalArgumentException if the <code>provider</code>
     *                                  is null or empty.
     * @see java.security.Provider
     */
    public static final Mac getInstance(String algorithm, String provider)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        Provider p = Security.getProvider(provider);
        if (p == null) {
            throw new NoSuchProviderException(provider);
        }
        return getInstance(algorithm, p);
    }

    /**
     * Returns a <code>Mac</code> object that implements the
     * specified MAC algorithm.
     * <p/>
     * <p> A new Mac object encapsulating the
     * MacSpi implementation from the specified Provider
     * object is returned.  Note that the specified Provider object
     * does not have to be registered in the provider list.
     *
     * @param algorithm the standard name of the requested MAC algorithm.
     *                  See the Mac section in the <a href=
     *                  "{@docRoot}/../technotes/guides/security/StandardNames.html#Mac">
     *                  Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     *                  for information about standard algorithm names.
     * @param provider  the provider.
     * @return the new <code>Mac</code> object.
     * @throws NoSuchAlgorithmException if a MacSpi
     *                                  implementation for the specified algorithm is not available
     *                                  from the specified Provider object.
     * @throws IllegalArgumentException if the <code>provider</code>
     *                                  is null.
     * @see java.security.Provider
     */
    public static final Mac getInstance(String algorithm, Provider provider)
            throws NoSuchAlgorithmException {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(algorithm);
            return new Mac(mac);
        } catch (Exception e) {
        }
        MacSpi spi = null;
        Provider.Service service = provider.getService("Mac", algorithm);
        if (service != null)
            spi = (MacSpi) service.newInstance(null);
        if (spi != null)
            return new Mac(spi, provider, service.getAlgorithm());
        throw new NoSuchAlgorithmException("no such " + algorithm + " for " + provider.getName());
    }

    /**
     * Returns the provider of this <code>Mac</code> object.
     *
     * @return the provider of this <code>Mac</code> object.
     */
    public final Provider getProvider() {
        return this.provider;
    }

    /**
     * Returns the length of the MAC in bytes.
     *
     * @return the MAC length in bytes.
     */
    public final int getMacLength() {
        if (jceService != null) {
            return jceService.getMacLength();
        }
        return (Integer) JCEAgentUtils.invokeSpi(spi, "engineGetMacLength", null);
    }

    /**
     * Initializes this <code>Mac</code> object with the given key.
     *
     * @param key the key.
     * @throws InvalidKeyException if the given key is inappropriate for
     *                             initializing this MAC.
     */
    public final void init(Key key) throws InvalidKeyException {
        if (jceService != null) {
            jceService.init(key);
            return;
        }
//        try {
        JCEAgentUtils.invokeSpi(spi, "engineInit", new Class[]{Key.class}, key, null);
//        } catch (InvalidAlgorithmParameterException e) {
//            throw new InvalidKeyException("init() failed", e);
//        }
        initialized = true;
    }

    /**
     * Initializes this <code>Mac</code> object with the given key and
     * algorithm parameters.
     *
     * @param key    the key.
     * @param params the algorithm parameters.
     * @throws InvalidKeyException                if the given key is inappropriate for
     *                                            initializing this MAC.
     * @throws InvalidAlgorithmParameterException if the given algorithm
     *                                            parameters are inappropriate for this MAC.
     */
    public final void init(Key key, AlgorithmParameterSpec params)
            throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (jceService != null) {
            jceService.init(key, params);
            return;
        }
        JCEAgentUtils.invokeSpi(spi, "engineInit", new Class[]{Key.class, AlgorithmParameterSpec.class}, key, params);
        initialized = true;
    }

    /**
     * Processes the given byte.
     *
     * @param input the input byte to be processed.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final void update(byte input) throws IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (jceService != null) {
            jceService.update(input);
            return;
        }
        JCEAgentUtils.invokeSpi(spi, "engineUpdate", new Class[]{byte.class}, input);
    }

    /**
     * Processes the given array of bytes.
     *
     * @param input the array of bytes to be processed.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final void update(byte[] input) throws IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (input != null) {
            if (jceService != null) {
                jceService.update(input);
                return;
            }
            JCEAgentUtils.invokeSpi(spi, "engineUpdate", new Class[]{byte[].class, int.class, int.class}, input, 0, input.length);
        }
    }

    /**
     * Processes the first <code>len</code> bytes in <code>input</code>,
     * starting at <code>offset</code> inclusive.
     *
     * @param input  the input buffer.
     * @param offset the offset in <code>input</code> where the input starts.
     * @param len    the number of bytes to process.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final void update(byte[] input, int offset, int len)
            throws IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }

        if (input != null) {
            if ((offset < 0) || (len > (input.length - offset)) || (len < 0))
                throw new IllegalArgumentException("Bad arguments");
            if (jceService != null) {
                jceService.update(input, offset, len);
                return;
            }
            JCEAgentUtils.invokeSpi(spi, "engineUpdate", new Class[]{byte[].class, int.class, int.class}, input, offset, len);
        }
    }

    /**
     * Processes <code>input.remaining()</code> bytes in the ByteBuffer
     * <code>input</code>, starting at <code>input.position()</code>.
     * Upon return, the buffer's position will be equal to its limit;
     * its limit will not have changed.
     *
     * @param input the ByteBuffer
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     * @since 1.5
     */
    public final void update(ByteBuffer input) {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (input == null) {
            throw new IllegalArgumentException("Buffer must not be null");
        }
        JCEAgentUtils.invokeSpi(spi, "engineUpdate", new Class[]{ByteBuffer.class}, input);
    }

    /**
     * Finishes the MAC operation.
     * <p/>
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     * That is, the object is reset and available to generate another MAC from
     * the same key, if desired, via new calls to <code>update</code> and
     * <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     *
     * @return the MAC result.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final byte[] doFinal() throws IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        byte[] mac = (byte[]) JCEAgentUtils.invokeSpi(spi, "engineDoFinal", null);
        JCEAgentUtils.invokeSpi(spi, "engineReset", null);
        return mac;
    }

    /**
     * Finishes the MAC operation.
     * <p/>
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     * That is, the object is reset and available to generate another MAC from
     * the same key, if desired, via new calls to <code>update</code> and
     * <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     * <p/>
     * <p>The MAC result is stored in <code>output</code>, starting at
     * <code>outOffset</code> inclusive.
     *
     * @param output    the buffer where the MAC result is stored
     * @param outOffset the offset in <code>output</code> where the MAC is
     *                  stored
     * @throws ShortBufferException  if the given output buffer is too small
     *                               to hold the result
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final void doFinal(byte[] output, int outOffset)
            throws ShortBufferException, IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        int macLen = getMacLength();
        if (output == null || output.length - outOffset < macLen) {
            throw new ShortBufferException
                    ("Cannot store MAC in output buffer");
        }
        byte[] mac = doFinal();
        System.arraycopy(mac, 0, output, outOffset, macLen);
        return;
    }

    /**
     * Processes the given array of bytes and finishes the MAC operation.
     * <p/>
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     * That is, the object is reset and available to generate another MAC from
     * the same key, if desired, via new calls to <code>update</code> and
     * <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     *
     * @param input data in bytes
     * @return the MAC result.
     * @throws IllegalStateException if this <code>Mac</code> has not been
     *                               initialized.
     */
    public final byte[] doFinal(byte[] input) throws IllegalStateException {
        if (initialized == false) {
            throw new IllegalStateException("MAC not initialized");
        }
        update(input);
        return doFinal();
    }

    /**
     * Resets this <code>Mac</code> object.
     * <p/>
     * <p>A call to this method resets this <code>Mac</code> object to the
     * state it was in when previously initialized via a call to
     * <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     * That is, the object is reset and available to generate another MAC from
     * the same key, if desired, via new calls to <code>update</code> and
     * <code>doFinal</code>.
     * (In order to reuse this <code>Mac</code> object with a different key,
     * it must be reinitialized via a call to <code>init(Key)</code> or
     * <code>init(Key, AlgorithmParameterSpec)</code>.
     */
    public final void reset() {
        JCEAgentUtils.invokeSpi(spi, "engineReset", null);
    }

    /**
     * Returns a clone if the provider implementation is cloneable.
     *
     * @return a clone if the provider implementation is cloneable.
     * @throws CloneNotSupportedException if this is called on a
     *                                    delegate that does not support <code>Cloneable</code>.
     */
    public final Object clone() throws CloneNotSupportedException {
        Mac that = (Mac) super.clone();
        that.spi = (MacSpi) this.spi.clone();
        return that;
    }
}