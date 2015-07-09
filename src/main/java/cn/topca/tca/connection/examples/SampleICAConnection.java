package cn.topca.tca.connection.examples;

import cn.topca.connection.Connection;
import cn.topca.connection.InvalidCodecParameterException;
import cn.topca.connection.ResponseException;
import cn.topca.connection.examples.SampleConnectionProvider;
import cn.topca.connection.protocol.CodecParameter;
import cn.topca.connection.protocol.CodecProtocol;
import cn.topca.core.NoSuchServiceAlgorithm;
import cn.topca.tca.connection.protocol.TCAProtocolProvider;
import cn.topca.tca.connection.protocol.ica.ICACipherParameter;
import cn.topca.tca.connection.protocol.ica.ICANameValuePair;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMCert;
import static cn.topca.security.bc.openssl.OpenSslUtil.decodePEMPrivateKey;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 测试私钥，密码password
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
 * @version 2014-04-25 16:15
 */
public class SampleICAConnection {
    public static void main(String[] args) throws IOException, InvalidCodecParameterException, ResponseException, NoSuchServiceAlgorithm {
        // 注册连接服务提供者
        SampleConnectionProvider.getInstance().register();
        // 注册连接协议提供者
        TCAProtocolProvider.getInstance().register();
        // 注册加密服务提供者，用于解析PEM格式密钥
        Security.addProvider(new BouncyCastleProvider());

        SampleICAConnection connection = new SampleICAConnection();

        String base64Cert = connection.certReq();
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(base64Cert)));
            System.out.println(cert);
        } catch (CertificateException e) {
            e.printStackTrace();
        }

    }

    static final String CA_CRS_URL = "http://demo.topca.cn:8080/TopCA/crs/evpCrs";

    static final String RA_NATIVE_CERT = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDZjCCAs+gAwIBAgIUZ9WqTGmrrnpZwLdA+CNhnyyYcbQwDQYJKoZIhvcNAQEF\n" +
            "BQAwUzEaMBgGA1UEAwwR6K6+5aSH6K+B5Lmm5a2QQ0ExGDAWBgNVBAsMD+a1i+iv\n" +
            "lemDqOivleeUqDEbMBkGA1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMB4XDTE0MDQy\n" +
            "NjAxMzcwMVoXDTE1MDQyNjAxMzcwMVowajEXMBUGA1UEAwwOUkHorr7lpIfor4Hk\n" +
            "uaYxFTATBgkqhkiG9w0BCQEWBnRAdC5jbjELMAkGA1UECwwCUkExDTALBgNVBAoM\n" +
            "BFRlc3QxCzAJBgNVBAYTAkNOMQ8wDQYHYIEcAQYKAQwCQUEwgZ8wDQYJKoZIhvcN\n" +
            "AQEBBQADgY0AMIGJAoGBAJtIel0eGY/FHTRmt9CNvL9sQDl4njSH26qjMTNVQjyQ\n" +
            "SnQjSaBBNwHEyuAdWE3JzVBGE2l0tzbpAkXo5H04BGMFTuv5q5PcbD5WzGM5gvk0\n" +
            "Cq/vit3ooNwpJFTVYNxmYn0TMVFitrNey94OI93rS4yy78xI80mJIRXaz5CsVeFZ\n" +
            "AgMBAAGjggEeMIIBGjAdBgNVHQ4EFgQUAmjm6pAW+w2650fwE7tmawZf6lowHwYD\n" +
            "VR0jBBgwFoAUIRruKrfLhkggPVbBr2P3OZ7FF8MwCQYDVR0TBAIwADAOBgNVHQ8B\n" +
            "Af8EBAMCBsAwSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRwOi8vWW91\n" +
            "cl9TZXJ2ZXJfTmFtZTpQb3J0L1RvcENBL2xvZHBfQmFzZUROMHEGA1UdHwEB/wRn\n" +
            "MGUwY6BhoF+GXWh0dHA6Ly8xOTIuMTY4LjE0NS4xNzY6ODA4MC9Ub3BDQS9wdWJs\n" +
            "aWMvaXRydXNjcmw/Q0E9NjExQ0UyNTQ3MTBEMzFFNDUzM0Y0QkMyRTVCMjVBQjkx\n" +
            "NDBFNjFGNDANBgkqhkiG9w0BAQUFAAOBgQAG5SEbmtxKXNt8FZ+GUB88t5BxFEll\n" +
            "vryXpP8wLpy8NuQk/f67NQfLYRpoGQMepWehB48T/+pOGkoHBAxy9ndjTpJesY7H\n" +
            "6txqY5+l+G73Lp5eZqP8CdjDwa0elRMpmsPnV1c0qOnxyquE0zzDfuOblBdoT3bg\n" +
            "jJboQzJUST7WOw==\n" +
            "-----END CERTIFICATE-----";

    static final String CA_RECIPIENT_CERT = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDbDCCAtWgAwIBAgIULkIGrHPdqKXHAIx6bDFhSBQsdtUwDQYJKoZIhvcNAQEF\n" +
            "BQAwUzEaMBgGA1UEAwwR6K6+5aSH6K+B5Lmm5a2QQ0ExGDAWBgNVBAsMD+a1i+iv\n" +
            "lemDqOivleeUqDEbMBkGA1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMB4XDTE0MDQy\n" +
            "NTA2MDcwM1oXDTE1MDQyNTA2MDcwM1owcDEPMA0GA1UEAwwGVGVzdEFBMRUwEwYJ\n" +
            "KoZIhvcNAQkBFgZ0QHQuY24xGDAWBgNVBAsMD+a1i+ivlemDqOivleeUqDEbMBkG\n" +
            "A1UECgwS5aSp6K+a5a6J5L+h6K+V55SoMQ8wDQYHYIEcAQYKAQwCQUEwgZ8wDQYJ\n" +
            "KoZIhvcNAQEBBQADgY0AMIGJAoGBAIFikIsr5D9zgNiI4xWHRPjf1UdQwrV98R9t\n" +
            "T0q6DGKydgjDW2E3xjY8Qaa9vOobS+gKjD8WD7Bv4pFwBBmqPy6M3sRE7N4kalvw\n" +
            "mEeNaqYxCYoLACrHawxXI8PV6DIgAsSUYD9B2rM3zuX/UHBN+2ePo4JrrhNC/QGr\n" +
            "e1AWji4LAgMBAAGjggEeMIIBGjAdBgNVHQ4EFgQUA38QzECFabEub9Hhx+H9grE1\n" +
            "eaMwHwYDVR0jBBgwFoAUIRruKrfLhkggPVbBr2P3OZ7FF8MwCQYDVR0TBAIwADAO\n" +
            "BgNVHQ8BAf8EBAMCBsAwSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRw\n" +
            "Oi8vWW91cl9TZXJ2ZXJfTmFtZTpQb3J0L1RvcENBL2xvZHBfQmFzZUROMHEGA1Ud\n" +
            "HwEB/wRnMGUwY6BhoF+GXWh0dHA6Ly8xOTIuMTY4LjE0NS4xNzY6ODA4MC9Ub3BD\n" +
            "QS9wdWJsaWMvaXRydXNjcmw/Q0E9NjExQ0UyNTQ3MTBEMzFFNDUzM0Y0QkMyRTVC\n" +
            "MjVBQjkxNDBFNjFGNDANBgkqhkiG9w0BAQUFAAOBgQCYw+bCxjCm5cWnYxYCHo+w\n" +
            "3fz4JZeUlpi7UmMcL/T+RmZwc8yjIhPR8nugc3ANhu12ks4wacq86yzN6FZ4nF8a\n" +
            "eJtZEDDRwXNih/FtSDcg5yS+wo4aEWLsxqus99MrdQ/WQSeIz21Zp7hlnb5xcxYY\n" +
            "CA7m6psc2kZC4No099HErw==\n" +
            "-----END CERTIFICATE-----";
    Connection connection;

    SampleICAConnection() throws IOException, InvalidCodecParameterException, NoSuchServiceAlgorithm {
        connection = Connection.getInstance("SampleHttp");
        CodecProtocol protocol = CodecProtocol.getInstance("ICA");
        CodecParameter parameter = new ICACipherParameter(
                decodePEMCert(CA_RECIPIENT_CERT),
                decodePEMPrivateKey(RA_NATIVE_PKEY, "password".toCharArray()),
                decodePEMCert(RA_NATIVE_CERT));
        connection.setCodecProtocol(protocol, parameter);
        connection.connect(CA_CRS_URL);
    }

    String certReq() throws IOException, ResponseException {
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
        ICANameValuePair requestMessage = new ICANameValuePair();
        requestMessage.put("operation", "makeCert".getBytes());
        requestMessage.put("accountHash", "E1FA09013B2AAD0718C7F4FE5584F3C7".getBytes());
        requestMessage.put("certReqBuf", csr.getBytes());
        requestMessage.put("userName", "testCRS".getBytes());
        requestMessage.put("userEmail", "crs@t.cn".getBytes());

        ICANameValuePair responseMessage = (ICANameValuePair) connection.getSession().doRequest(requestMessage);
        return new String(responseMessage.get("certSignBuf"));
    }

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
