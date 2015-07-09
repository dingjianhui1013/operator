package cn.topca.tca.client;

import cn.topca.connection.ResponseException;

import java.io.IOException;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * CA服务接口
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-21 22:41
 */
public interface CAServiceInterface {
    /**
     * 申请证书
     *
     * @param request 证书申请请求数据
     * @return 证书申请响应数据
     */
    public CertificateResponse requestCertificate(CertificateRequest request) throws IOException, ResponseException;

    /**
     * 吊销证书
     *
     * @param request 证书吊销请求数据
     * @return 证书吊销响应数据
     */
    public RevokeResponse revokeCertificate(RevokeRequest request) throws IOException, ResponseException;
}
