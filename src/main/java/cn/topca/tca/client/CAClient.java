package cn.topca.tca.client;

import cn.topca.connection.Connection;
import cn.topca.connection.ConnectionSession;
import cn.topca.connection.InvalidCodecParameterException;
import cn.topca.connection.ResponseException;
import cn.topca.connection.protocol.CharsetParameter;
import cn.topca.connection.protocol.CodecParameter;
import cn.topca.connection.protocol.CodecProtocol;
import cn.topca.core.NoSuchServiceAlgorithm;
import cn.topca.security.JCAJCEUtils;
import cn.topca.tca.connection.protocol.TCAProtocolProvider;
import cn.topca.tca.connection.protocol.ica.ICABasicParameter;
import cn.topca.tca.connection.protocol.ica.ICACipherParameter;
import cn.topca.tca.connection.protocol.ica.ICACodecProtocol;
import cn.topca.tca.connection.protocol.ica.ICANameValuePair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * CA服务适配器
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 21:51
 */
public class CAClient implements CAServiceInterface {

    private final Connection connection;
    private final CodecProtocol protocol;
    // ca connection url
    private final String url;
    // config changed
    private volatile boolean changed = true;
    // protocol version
    private int version = 3;
    // protocol charset encoding
    private String charsetEncoding;
    private Certificate recipientCertificate;
    private PrivateKey nativePrivateKey;
    private Certificate nativeCertificate;
    private String encryptionAlgorithm;
    private String encryptionProvider;

    static {
        // 注册连接协议提供者
        TCAProtocolProvider.getInstance().register();
        JCAJCEUtils.register(new BouncyCastleProvider());
    }

    protected CAClient(Connection connection, CodecProtocol protocol, String url) {
        this.connection = connection;
        this.protocol = protocol;
        this.url = url;
        initClient();
    }

    public static String getDefaultProtocol() {
        return "ICA";
    }

    /**
     * 签发证书
     *
     * @param request 证书申请请求数据
     * @return 证书申请响应数据
     * @throws IOException
     * @throws ResponseException
     */
    public CertificateResponse requestCertificate(CertificateRequest request) throws IOException, ResponseException {
        ICANameValuePair req = new ICANameValuePair();
        req.put("operation", "makeCert".getBytes());
        ICACodecProtocol.fillICANameValuePair(req, request, charsetEncoding);
        ICANameValuePair rep = doRequest(req);
        CertificateResponse response = new CertificateResponse();
        ICACodecProtocol.fillMessage(response, rep, charsetEncoding);
        return response;
    }

    /**
     * 吊销证书
     *
     * @param request 证书吊销请求数据
     * @return 证书吊销响应数据
     * @throws IOException
     * @throws ResponseException
     */
    public RevokeResponse revokeCertificate(RevokeRequest request) throws IOException, ResponseException {
        ICANameValuePair req = new ICANameValuePair();
        req.put("operation", "revokeCert".getBytes());
        ICACodecProtocol.fillICANameValuePair(req, request, charsetEncoding);
        ICANameValuePair rep = doRequest(req);
        RevokeResponse response = new RevokeResponse();
        ICACodecProtocol.fillMessage(response, rep, charsetEncoding);
        return response;
    }

    public String getCharsetEncoding() {
        return charsetEncoding;
    }

    public void setCharsetEncoding(String charsetEncoding) {
        this.changed = true;
        this.charsetEncoding = charsetEncoding;
    }

    public Certificate getRecipientCertificate() {
        return recipientCertificate;
    }

    public void setRecipientCertificate(Certificate recipientCertificate) {
        this.changed = true;
        this.recipientCertificate = recipientCertificate;
    }

    public PrivateKey getNativePrivateKey() {
        return nativePrivateKey;
    }

    public void setNativePrivateKey(PrivateKey nativePrivateKey) {
        this.changed = true;
        this.nativePrivateKey = nativePrivateKey;
    }

    public Certificate getNativeCertificate() {
        return nativeCertificate;
    }

    public void setNativeCertificate(Certificate nativeCertificate) {
        this.changed = true;
        this.nativeCertificate = nativeCertificate;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.changed = true;
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getEncryptionProvider() {
        return encryptionProvider;
    }

    public void setEncryptionProvider(String encryptionProvider) {
        this.changed = true;
        this.encryptionProvider = encryptionProvider;
    }

    protected ICANameValuePair doRequest(ICANameValuePair request) throws IOException, ResponseException {
        ICANameValuePair response = (ICANameValuePair) getSession().doRequest(request);
        int status = Integer.valueOf(response.get("status") != null ? new String(response.get("status"), charsetEncoding) : "0", 16);
        if (status > 0){
            String msg;
            if(response.get("message")!=null)
                msg = new String(response.get("message"),charsetEncoding);
            else
             msg = response.toString(charsetEncoding);
            throw new ResponseException(status, msg);
        }
        return response;
    }


    private ConnectionSession getSession() throws IOException {
        initClient();
        connection.connect(url);
        return connection.getSession();
    }

    private void initClient() {
        if (changed) {
            try {
                CodecParameter parameter;
                if (recipientCertificate == null || nativePrivateKey == null || nativeCertificate == null) {
                    parameter = new ICABasicParameter(version, charsetEncoding);
                } else {  // encrypt mode
                    parameter = new ICACipherParameter(version, charsetEncoding, recipientCertificate, nativePrivateKey, nativeCertificate, encryptionAlgorithm, encryptionProvider);
                }
                connection.setCodecProtocol(protocol, parameter);
                if (charsetEncoding == null)
                    charsetEncoding = ((CharsetParameter) parameter).getCharsetEncoding();
                changed = false;
            } catch (InvalidCodecParameterException e) {
                throw new UnSupportedProtocolException(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取CA客户端实例
     *
     * @param connection 连接适配器
     * @param protocol   数据编码协议
     * @param url        CA地址
     * @return CA客户端实例
     * @throws IOException
     */
    public static CAClient getInstance(String connection, String protocol, String url) throws IOException, NoSuchServiceAlgorithm {
        return new CAClient(Connection.getInstance(connection), CodecProtocol.getInstance(protocol), url);
    }

    /**
     * 获取CA客户端实例
     *
     * @param connection 连接适配器
     * @param url        CA地址
     * @return CA客户端实例
     * @throws IOException
     */
    public static CAClient getInstance(String connection, String url) throws IOException {
        try {
            return new CAClient(Connection.getInstance(connection), CodecProtocol.getInstance(getDefaultProtocol()), url);
        } catch (NoSuchServiceAlgorithm e) {
            throw new UnSupportedProtocolException(getDefaultProtocol(), e);
        }
    }

}
