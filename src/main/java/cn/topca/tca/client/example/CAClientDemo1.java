package cn.topca.tca.client.example;

import cn.topca.connection.ResponseException;
import cn.topca.connection.examples.SampleConnectionProvider;
import cn.topca.security.JCAJCEUtils;
import cn.topca.tca.client.*;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMCert;
import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMPrivateKey;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <support_topca@itrus.com.cn>
 * <p/>
 * CAClient 示例
 * <pre>
 * -----BEGIN RSA PRIVATE KEY-----
 * Proc-Type: 4,ENCRYPTED
 * DEK-Info: AES-128-CBC,b1e4a0e820352452d5ecbebfae16a59a
 *
 * Xx9eGvCWD93Dpa8hQ6rFk0AmjAb8a+0kq0T0uLE+7AwiQrhcjUKA8vxlc3o8doUZ
 * eXMAoKSmTg97bGzHS+UnHK5USZHbVlmGTN8dGb9B/d4aJkIwz8CcHpyqOlCENJQv
 * 6Vldn9MqkZ0ju0KLBotzGaV8S0qmt52i4nV2JEGzSuDDjL5WYDUDLdVVOE0LHwQI
 * Xc5a+sTkBFJuYjj/N+y+q58xHLD1Zrldh+jIQqu3l9ADjJRgQSKlqOFOCZpnV4bZ
 * JotISqsMMG2/HJ9PyI6fMgFFbsKOJVPKuMTIN8BUdHBIJTbbDSiRYL0dGRypTfbe
 * voSxczIiF+WXHMnfgUfUP1cO3p8rm/s4B37E12n0OIKRZwA2S9Ye8L2R/Jefwd/k
 * 6LTXLJxldKgLpsvCiCqq6okvRmmy/sUgkDmOsmjLwVLvx1NOW3v1q5356zy7xofk
 * GlkqlpkuI25P/3jVVRvIoZQV/rkbHD0/TWHHUBefcH8NQ40jwIPTequhSSflLWal
 * 1meVB08//skS7wjXE7wKHVIIfokt6Y2SOTt6wM2Cj4ThFh1TJLji+ZhXmnQSUlyn
 * OTC/ZCTnOvr/qEy3rqjWNluMFCbC3mqRzHMkDkHW4l3hTfWR2xiGPYKw4AeXuY6R
 * x5ziJk5dTHX+66IfgTcAnx462Z7FqFz7dxaRKw7kdtMLDcYnx69PtpLtCbU1J2+z
 * 0rs/tGxWbpzFnxVLk2XPbxtlnMeMKv+mIhO/PbMCBeUx2wMICXihxdIjMXCsH8ub
 * OGB2kkoerKJ5sH1q1UKEulg1B1mTdKVnnymtMtMXpKgsYzCFZqjDBQ5dfklT6vTj
 * -----END RSA PRIVATE KEY-----
 * </pre>
 * 测试公钥
 * <pre>
 * -----BEGIN PUBLIC KEY-----
 * MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBYpCLK+Q/c4DYiOMVh0T439VH
 * UMK1ffEfbU9KugxisnYIw1thN8Y2PEGmvbzqG0voCow/Fg+wb+KRcAQZqj8ujN7E
 * ROzeJGpb8JhHjWqmMQmKCwAqx2sMVyPD1egyIALElGA/QdqzN87l/1BwTftnj6OC
 * a64TQv0Bq3tQFo4uCwIDAQAB
 * -----END PUBLIC KEY-----
 * </pre>
 * 测试CSR（用于向CA申请RA设备证书）
 * <pre>
 * -----BEGIN NEW CERTIFICATE REQUEST-----
 * MIIBfjCB6AIBADAPMQ0wCwYDVQQDEwR0ZW1wMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCb
 * SHpdHhmPxR00ZrfQjby/bEA5eJ40h9uqozEzVUI8kEp0I0mgQTcBxMrgHVhNyc1QRhNpdLc26QJF
 * 6OR9OARjBU7r+auT3Gw+VsxjOYL5NAqv74rd6KDcKSRU1WDcZmJ9EzFRYrazXsveDiPd60uMsu/M
 * SPNJiSEV2s+QrFXhWQIDAQABoDAwLgYJKoZIhvcNAQkOMSEwHzAdBgNVHQ4EFgQUAmjm6pAW+w26
 * 50fwE7tmawZf6lowDQYJKoZIhvcNAQELBQADgYEAVuQbpNACtSkzNxZ5OuvqICyCLZHle8nHcodk
 * sFU0Ca++wViXs5WAfBx5/3DzfrHx+SVbfh29fI88GFEIRn6OKEZmCZGUql34rFyzwmFjcAwxsVE1
 * aUImQCZsS1RsqX2PwJd/E70TOWjWuwartWHkaUU5LDosPflT+vLAprUEi1M=
 * -----END NEW CERTIFICATE REQUEST-----
 * </pre>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-05-04 14:22
 */
public class CAClientDemo1 {
    public static void main(String[] args) throws IOException {

        /* init system */
        // 注册连接服务提供者
        SampleConnectionProvider.getInstance().register();
        // 注册加密服务提供者，用于解析PEM格式密钥
        JCAJCEUtils.register(new BouncyCastleProvider());
        /* init properties */
        String connection = "SampleHttp"; // 按实际情况实现连接服务

        Certificate recipientCertificate = decodePEMCert(CA_RECIPIENT_CERT);
        PrivateKey nativePrivateKey = decodePEMPrivateKey(RA_NATIVE_PKEY, "password".toCharArray());
        Certificate nativeCertificate = decodePEMCert(RA_NATIVE_CERT);

        /* setup ca client */
        CAClient client = CAClient.getInstance(connection, CA_URL);
//        client.setRecipientCertificate(recipientCertificate);
//        client.setNativePrivateKey(nativePrivateKey);
//        client.setNativeCertificate(nativeCertificate);
//        client.setEncryptionProvider(provider); //加密机


        // 申请的证书
        Certificate cert = null;
        /* request certificate action */
        try {
            String csr = "MIIC5TCCAc0CAQAwcDELMAkGA1UEBhMCQ04xEDAOBgNVBAgTB0JlaWppbmcxEDAOBgNVBAcTB0Jl" +
                    "aWppbmcxFzAVBgNVBAoTDlRvcENBIFJEQ2VudGVyMQ4wDAYDVQQLEwVUb3BDQTEUMBIGA1UEAxML" +
                    "VGVzdFVzZXJBUEkwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDerATFWezY9GWEMFXy" +
                    "pxxlLQ/H5l9bhXD4J8G6R+th2jBvEvXv3Xow2xbCj3Z+H558f8OJdD5ybOshtfNqaqwKqjBUrA+l" +
                    "zA4HtK8x27z3KS+0FwV6qXHbWQLi34F9flattrcRYiu086OobbCxDapFrkSEplrnF6+rVldCeyGk" +
                    "jxp6uJkIRjlEf2rJCPY/IAZMaoUBLLfLW05gk8Zujij0d05QwClXLlvp0xfkVFqMti7oQ4/EjboP" +
                    "Ico7Ry32CUNelm42cd9s2GlGCoeDvmn9AfzqA6mth3us1RIrOMvl1BBMDM4HK6tSRYw2MuCsW1F8" +
                    "vyeIGN6p7hmCPz39oL5ZAgMBAAGgMDAuBgkqhkiG9w0BCQ4xITAfMB0GA1UdDgQWBBR/O0aWiMsd" +
                    "RJOOuPLM/bzpoa1UaDANBgkqhkiG9w0BAQsFAAOCAQEArUI+Dx+A9+odPFyLVGvxSRFoCDBpjRfd" +
                    "OYRABHe89bnvDn/Ymz1/m9Fbydewd1Vz6nGPr65rDr80zO8DZrD9eW+vF5Y5zsc8kjjK6kRy/DLL" +
                    "WfOyShsgCevYtwiqUsnYX9zfkhxMJmpebKnr7676FV7DD9VwYEOICVkx4BdtY7Z34CIJJGTggp/h" +
                    "BxQDDBolEDdHnj+z6o8rPo2hjoZdaJRydglS0VdbaV5XZBgrU8nLD50MdWYGnx4cvnjU9o5IH7kN" +
                    "xp2q8tmz/b/MyeOajHJoCtiMDuAc93V30XcpOVb63I3mj0m44s32YeG5LS5Zt8E72m3wmUZfhQdw" +
                    "jvUmdg==";
            CertificateRequest certificateRequest = new CertificateRequest();
            certificateRequest.setAccountHash(RA_ACCOUNT);
            certificateRequest.setCertReqBuf(csr);
            certificateRequest.setUserName("CAClientDemoUser");
            certificateRequest.setUserEmail("user@demo.cn");
            CertificateResponse certificateResponse = client.requestCertificate(certificateRequest);
            String base64Cert = certificateResponse.getCertSignBuf();
//            String base64CertChain = certificateResponse.getCertSignBufP7();
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                cert = cf.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(base64Cert)));
                System.out.println(cert);
                System.out.println(certificateResponse.getCertSerialNumber());
                System.out.println(((X509Certificate) cert).getSerialNumber().toString(16).toUpperCase());
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } catch (ResponseException e) {
            e.printStackTrace();
        }

        /* revoke certificate action */
        if (cert == null)
            return;
        try {
            RevokeRequest revokeRequest = new RevokeRequest();
            revokeRequest.setAccountHash(RA_ACCOUNT);
            //  revokeRequest.setCertSerialNumber(((X509Certificate) cert).getSerialNumber().toString(16).toUpperCase());

            //   revokeRequest.setCertSerialNumber(certificateResponse.getCertSerialNumber());
            revokeRequest.setCertRevokeReason(RevokeRequest.certificateHold);
            RevokeResponse revokeResponse = client.revokeCertificate(revokeRequest);
            System.out.println("Revoke Cert:");
            System.out.println(revokeResponse.getCertSerialNumber());
            System.out.println(revokeResponse.getCertRevokeReason());
            System.out.println(revokeResponse.getCertRevokeDate());
        } catch (ResponseException e) {
            e.printStackTrace();
        }
    }

    static final String CA_URL = "http://localhost:8085/TopCA/crs/crs";
    //static final String CA_URL = "http://192.168.30.183:8080/TopCA/crs/evpCrs"; evpCrs是加密通道
    static final String RA_ACCOUNT = "059A1782E816F71212AE704AA2F1EFE6"; //ra账户

    static final String RA_NATIVE_CERT = "-----BEGIN CERTIFICATE-----\n" +
            "MIICaTCCAdKgAwIBAgIUPjDTTUz7A1WP33Btz2Lg7BLEvMUwDQYJKoZIhvcNAQEFBQAwNzEMMAoGA1UEAwwDRVNBMQwwCgYDVQQLDANFU0ExDDAKBgNVBAoMA0VTQTELMAkGA1UEBhMCQ04wHhcNMTQwNTA1MDIxNjUxWhcNMTUwNTA1MDIxNjUxWjBPMQ0wCwYDVQQDDARSQUFBMQ8wDQYDVQQLDAZSQTIwNDgxDzANBgNVBAoMBlJBMjA0ODELMAkGA1UEBgwCQ04xDzANBgdggRwBBgoBDAJBQTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAm0h6XR4Zj8UdNGa30I28v2xAOXieNIfbqqMxM1VCPJBKdCNJoEE3AcTK4B1YTcnNUEYTaXS3NukCRejkfTgEYwVO6/mrk9xsPlbMYzmC+TQKr++K3eig3CkkVNVg3GZifRMxUWK2s17L3g4j3etLjLLvzEjzSYkhFdrPkKxV4VkCAwEAAaNaMFgwCQYDVR0TBAIwADALBgNVHQ8EBAMCBsAwHwYDVR0jBBgwFoAU81YVlv+bULEqGVYA8sHPhHTJj90wHQYDVR0OBBYEFAJo5uqQFvsNuudH8BO7ZmsGX+paMA0GCSqGSIb3DQEBBQUAA4GBAG5SB46OUSjMAIqmrWAvIxLeYaSdqTAmRz/HJYjZtHnXicGFizAhRXbP1zCOuRtgemjHYz7jvDhzBrwfuW3/JmIpahTMb+ZFK+wHb7mgJgDoyNQa/SAovaCeNA3XljLme4JX7/J+WHXTjcrN3k4visn2q2TsrMChiYP4ilbCt4sz\n" +
            "-----END CERTIFICATE-----";

    static final String CA_RECIPIENT_CERT = "-----BEGIN CERTIFICATE-----\n" +
            "MIICaTCCAdKgAwIBAgIUCR4ui6lTi3Etw66dwAMhDDA0V+cwDQYJKoZIhvcNAQEFBQAwNzEMMAoGA1UEAwwDRVNBMQwwCgYDVQQLDANFU0ExDDAKBgNVBAoMA0VTQTELMAkGA1UEBhMCQ04wHhcNMTQwNTA1MDIxNjU0WhcNMTUwNTA1MDIxNjU0WjBPMQ0wCwYDVQQDDARDQUFBMQ8wDQYDVQQLDAZSQTIwNDgxDzANBgNVBAoMBlJBMjA0ODELMAkGA1UEBgwCQ04xDzANBgdggRwBBgoBDAJBQTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAtkbsSsO1yxiZQMp2oehiYVjAsuZCbqW3EbmcHWCjGZk4znojSdi9eKgvTiBGvsKFzJO9ecRUhtVGwtq/nDBD1yAax26qbIqjZ+HudPJWorDDExs4shZSOMiN4EN6Frqd/zkSzflgWAFeZBM1Wm/hdM9EL7+FdOU92ya97ANrpL8CAwEAAaNaMFgwCQYDVR0TBAIwADALBgNVHQ8EBAMCBsAwHwYDVR0jBBgwFoAU81YVlv+bULEqGVYA8sHPhHTJj90wHQYDVR0OBBYEFGrGLvGMnbd9vkXmYWShmkjKaU5VMA0GCSqGSIb3DQEBBQUAA4GBAHjBsg0cc51Y2qdxVf12PhXbACE9LwO+n36kGD/L0yfCwQasam+9JqxrGUOGo4Uvig1RjSY/0RUUcZI/mxdUn30hp3cPl1huEBCaQWwitgtFYoiYRdAP35GrbYygD0ffObsBEcRdwHsWzjAusduxilAOUdPDS1cVSoAv69PTGymI\n" +
            "-----END CERTIFICATE-----";

    static final String RA_NATIVE_PKEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "Proc-Type: 4,ENCRYPTED\n" +
            "DEK-Info: AES-128-CBC,b1e4a0e820352452d5ecbebfae16a59a\n" +
            "\n" +
            "Xx9eGvCWD93Dpa8hQ6rFk0AmjAb8a+0kq0T0uLE+7AwiQrhcjUKA8vxlc3o8doUZ\n" +
            "eXMAoKSmTg97bGzHS+UnHK5USZHbVlmGTN8dGb9B/d4aJkIwz8CcHpyqOlCENJQv\n" +
            "6Vldn9MqkZ0ju0KLBotzGaV8S0qmt52i4nV2JEGzSuDDjL5WYDUDLdVVOE0LHwQI\n" +
            "Xc5a+sTkBFJuYjj/N+y+q58xHLD1Zrldh+jIQqu3l9ADjJRgQSKlqOFOCZpnV4bZ\n" +
            "JotISqsMMG2/HJ9PyI6fMgFFbsKOJVPKuMTIN8BUdHBIJTbbDSiRYL0dGRypTfbe\n" +
            "voSxczIiF+WXHMnfgUfUP1cO3p8rm/s4B37E12n0OIKRZwA2S9Ye8L2R/Jefwd/k\n" +
            "6LTXLJxldKgLpsvCiCqq6okvRmmy/sUgkDmOsmjLwVLvx1NOW3v1q5356zy7xofk\n" +
            "GlkqlpkuI25P/3jVVRvIoZQV/rkbHD0/TWHHUBefcH8NQ40jwIPTequhSSflLWal\n" +
            "1meVB08//skS7wjXE7wKHVIIfokt6Y2SOTt6wM2Cj4ThFh1TJLji+ZhXmnQSUlyn\n" +
            "OTC/ZCTnOvr/qEy3rqjWNluMFCbC3mqRzHMkDkHW4l3hTfWR2xiGPYKw4AeXuY6R\n" +
            "x5ziJk5dTHX+66IfgTcAnx462Z7FqFz7dxaRKw7kdtMLDcYnx69PtpLtCbU1J2+z\n" +
            "0rs/tGxWbpzFnxVLk2XPbxtlnMeMKv+mIhO/PbMCBeUx2wMICXihxdIjMXCsH8ub\n" +
            "OGB2kkoerKJ5sH1q1UKEulg1B1mTdKVnnymtMtMXpKgsYzCFZqjDBQ5dfklT6vTj\n" +
            "-----END RSA PRIVATE KEY-----";
}
