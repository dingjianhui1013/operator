package cn.topca.connection.protocol;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 密文通信协议参数接口
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 11:21
 */
public interface CipherParameter extends CodecParameter {
    /**
     * 返回消息接收者数字证书。
     * 接收者数字证书用于保护（加密）传输过程中的消息加密密钥，
     * 接收者数字证书如果为X.509格式，则密钥扩展用法必须为“关键项”，且至少包含“密钥加密”，“数据加密”，“数据签名”。
     *
     * @return 消息接收者数字证书
     */
    public Certificate getRecipientCertificate();

    /**
     * 返回本地私钥。
     * 本地私钥用于获取（解密）传输过程中的加密密钥，签名传输的信息。
     *
     * @return 本地私钥
     */
    public PrivateKey getNativePrivateKey();

    /**
     * 返回本地证书。
     * 本地证书用于提供给接收者验明身份。
     *
     * @return 本地证书
     */
    public Certificate getNativeCertificate();

    /**
     * 返回对称密钥密码算法名称。
     * 该算法用于保护（加密、解密）传输消息。
     *
     * @return 对称密钥密码算法名称
     */
    public String getEncryptionAlgorithm();

    /**
     * 返回加密服务提供者名称。
     * 使用指定加密服务提供者对消息内容进行加密。
     *
     * @return 加密服务提供者名称
     */
    public String getEncryptionProvider();

}
