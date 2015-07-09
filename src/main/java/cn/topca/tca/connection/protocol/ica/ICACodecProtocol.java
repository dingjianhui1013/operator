package cn.topca.tca.connection.protocol.ica;

import cn.topca.connection.InvalidCodecParameterException;
import cn.topca.connection.Message;
import cn.topca.connection.protocol.CodecParameter;
import cn.topca.connection.protocol.CodecProtocolSpi;
import cn.topca.security.bc.cms.CMSAlgorithm;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * <p>ICA协议<br/>
 * 继承自iTrusCA2，协议结构如下<br/>
 * 版本1：ICA协议初始版本<br/>
 * data_length(4bytes)|status(2bytes)|content_array_length(2bytes)|content_array_length * [key_length(2bytes)|key_content(key_length * 1byte)|value_length(2bytes)|value_content(value_length * 1byte)]<br/>
 * 版本2：解决大数据问题<br/>
 * data_length(4bytes)|status(2bytes)|content_array_length(2bytes)|content_array_length * [key_length(2bytes)|key_content(key_length * 1byte)|value_length(4bytes)|value_content(value_length * 1byte)]<br/>
 * 版本3：协议中定义了版本号，建立兼容机制<br/>
 * 0x01(1byte)|data_length(3bytes)|version(1byte)|status(2bytes)|content_array_length(2bytes)|content_array_length * [key_length(2bytes)|key_content(key_length * 1byte)|value_length(4bytes)|value_content(value_length * 1byte)]<br/>
 * </p>
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 14:29
 */
public class ICACodecProtocol extends CodecProtocolSpi {
    private int version;
    private String charsetEncoding;
    private boolean cipher;
    private Certificate recipientCertificate;
    private PrivateKey nativePrivateKey;
    private Certificate nativeCertificate;
    private String encryptionAlgorithm;
    private Provider encryptionProvider;

    /**
     * 在协议中应用当前参数
     *
     * @param parameter 协议参数{@link ICABasicParameter}|{@link ICACipherParameter}
     * @throws cn.topca.connection.InvalidCodecParameterException 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时抛出异常
     */
    @Override
    protected void engineSetParameter(CodecParameter parameter) throws InvalidCodecParameterException {
        String errMsg = null;
        if (parameter instanceof ICACipherParameter) {
            cipher = true;
            ICACipherParameter param = (ICACipherParameter) parameter;
            recipientCertificate = param.getRecipientCertificate();
            nativePrivateKey = param.getNativePrivateKey();
            nativeCertificate = param.getNativeCertificate();
            encryptionAlgorithm = param.getEncryptionAlgorithm();
            if (param.getEncryptionProvider() != null)
                encryptionProvider = Security.getProvider(param.getEncryptionProvider());
            else
                encryptionProvider = null;
            setParameter(param);
            if (recipientCertificate == null)
                errMsg = "recipientCertificate can not be null";
            if (nativePrivateKey == null)
                errMsg = "nativePrivateKey can not be null";
            if (nativeCertificate == null)
                errMsg = "nativeCertificate can not be null";
        } else if (parameter instanceof ICABasicParameter) {
            cipher = false;
            setParameter((ICABasicParameter) parameter);
        } else {
            errMsg = "parameter must be ICAProtocolParameter";
        }
        if (errMsg != null)
            throw new InvalidCodecParameterException(errMsg);
    }

    private void setParameter(ICABasicParameter parameter) {
        version = parameter.getVersion();
        charsetEncoding = parameter.getCharsetEncoding();
    }

    /**
     * 对消息进行编码
     *
     * @param message 消息对象
     * @param out     通信输出流
     * @throws IOException
     */
    @Override
    public void engineEncode(Message message, OutputStream out) throws IOException {
        ICANameValuePair inData;
        if (message instanceof ICANameValuePair) {
            inData = (ICANameValuePair) message;
        } else {
            inData = new ICANameValuePair();
            fillICANameValuePair(inData, message, charsetEncoding);
        }
        ICANameValuePair nv;
        if (cipher) {
            nv = new ICANameValuePair();
            nv.put(ENC_DATA_NAME, _encrypt(inData));
        } else {
            nv = inData;
        }
        _encode(nv, out);
    }

    private byte[] _encrypt(ICANameValuePair message) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        _encode(message, buffer);
        buffer.flush();
        byte[] origin = buffer.toByteArray();
        buffer.reset();
        ASN1ObjectIdentifier encryptionAlg;
        assert encryptionAlgorithm != null; // Default is "DESede3"
        if (encryptionAlgorithm.toUpperCase().equals("SM4"))
            encryptionAlg = CMSAlgorithm.SM4;
        else if (encryptionAlgorithm.toUpperCase().matches("AES(128)?"))
            encryptionAlg = CMSAlgorithm.AES128_CBC;
        else if (encryptionAlgorithm.toUpperCase().equals("AES192"))
            encryptionAlg = CMSAlgorithm.AES192_CBC;
        else if (encryptionAlgorithm.toUpperCase().equals("AES192"))
            encryptionAlg = CMSAlgorithm.AES192_CBC;
        else
            encryptionAlg = CMSAlgorithm.DES_EDE3_CBC;
        try {
            return ICAProtocolUtil.encAndSignToP7(origin, recipientCertificate, encryptionAlg, nativePrivateKey, nativeCertificate, null, encryptionProvider);
        } catch (CMSException e) {
            IOException ex = new IOException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (CertificateEncodingException e) {
            IOException ex = new IOException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (OperatorCreationException e) {
            IOException ex = new IOException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    private static final String ENC_DATA_NAME = "ENCRYPTDATA";

    /**
     * 对消息进行解码
     *
     * @param in 通信输入流
     * @return 消息对象
     * @throws IOException
     */
    @Override
    public Message engineDecode(InputStream in) throws IOException {
        ICANameValuePair nv = _decode(in);
        if (nv.containsKey(ENC_DATA_NAME))
            try {
                return _decrypt(new ByteArrayInputStream(nv.get(ENC_DATA_NAME)));
            } catch (CMSException e) {
                IOException ex = new IOException(e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (CertificateException e) {
                IOException ex = new IOException(e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (OperatorCreationException e) {
                IOException ex = new IOException(e.getMessage());
                ex.initCause(e);
                throw ex;
            } catch (CertException e) {
                IOException ex = new IOException(e.getMessage());
                ex.initCause(e);
                throw ex;
            }
        return nv;
    }

    private ICANameValuePair _decrypt(InputStream signedData) throws IOException, CMSException, CertificateException, OperatorCreationException, CertException {
        byte[] icaData = ICAProtocolUtil.verifyAndDecFormP7(signedData, null, nativePrivateKey, encryptionProvider);
        return _decode(new ByteArrayInputStream(icaData));
    }

    /**
     * 消息编码
     *
     * @param nv  ICA格式消息对象
     * @param out 数据输出流
     * @throws IOException
     */
    private void _encode(ICANameValuePair nv, OutputStream out) throws IOException {
        DataOutputStream output = new DataOutputStream(out);
        //get total length and nv pair count
        int length = 0; //data length
        int nvCount = nv.size();    //name value pair number to write
        length += (version < 3 ? 0 : 1) + 2 + 2;    //version, status, nvCount
        length += 2 * nvCount;    // names
        length += (version < 3 ? 2 : 4) * nvCount;   // values
        length += nv.getDataLength(); // data

        // write header data
        if (version >= 3) {
            output.writeInt(length | Integer.MIN_VALUE); // write total length
            output.writeByte(version);                   // write version
        } else
            output.writeInt(length); // write total length
        output.writeShort(0);                            // write status number
        output.writeShort(nvCount);                      // write data count

        // write data
        Iterator<Map.Entry<String, byte[]>> it;
        for (it = nv.entrySet().iterator(); it.hasNext(); ) {    // write data
            Map.Entry<String, byte[]> entry = it.next();
            String name = entry.getKey();
            byte[] value = entry.getValue();
            output.writeShort(name.length());    // write name length
            output.write(name.getBytes(charsetEncoding));    // write name
            if (version < 3)    // write value length
                output.writeShort(value.length);
            else
                output.writeInt(value.length);
            output.write(value);    // write value
        }
    }

    /**
     * 消息解码
     *
     * @param in 数据输入流
     * @return ICA格式消息对象
     * @throws IOException
     */
    private ICANameValuePair _decode(InputStream in) throws IOException {
        DataInputStream input = new DataInputStream(in);
        ICANameValuePair nv = new ICANameValuePair();

        int tg1 = input.readInt();
        int tg2 = input.readUnsignedByte();
        if (tg1 == 1701999215 && tg2 == 'r') {     // 兼容原TopCA远程异常信息传递方式
            // error
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] tmpBuf = new byte[255];
            int n;
            while (-1 != (n = input.read(tmpBuf))) {
                output.write(tmpBuf, 0, n);
            }
            String error;
            error = new String(output.toByteArray(), charsetEncoding);
            throw new IOException("ERROR :" + error);
        }

        int version = (tg1 & Integer.MIN_VALUE) == 0 ? 2 : tg2;

        int status = (version < 3) ? (tg2 >> 8 | input.readUnsignedByte()) : input.readUnsignedShort();    //read status
        if (status > 0) {
            throw new IOException("Remote code: 0x" + parseStatus(status));
        }
        int dataCount = input.readUnsignedShort();    //read nv count

        for (int i = 0; i < dataCount; i++) {
            int len;
            byte[] buffer;

            len = input.readShort();    // read name length
            buffer = new byte[len];
            input.readFully(buffer);    //read name
            String name = new String(buffer, charsetEncoding);

            len = version < 3 ? input.readShort() : input.readInt();    //read value length
            buffer = new byte[len];
            input.readFully(buffer);    //read value

            nv.put(name, buffer);
        }
        return nv;
    }

    private static final String dict = "0123456789ABCDEF";

    private String parseStatus(int status) {
        StringBuffer buffer = new StringBuffer(4);
        for (int i = 0; i < 4; i++) {
            buffer.append(dict.indexOf(status >> (32 - 8 * i)));
        }
        return buffer.toString();
    }

    public static void fillICANameValuePair(ICANameValuePair nv, Message message, String charsetEncoding) {
        Class msgClass = message.getClass();
        for (Field field : msgClass.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                String value = (String) field.get(message);
                if (value != null)
                    nv.put(name, value.getBytes(charsetEncoding));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fillMessage(Message message, ICANameValuePair nv, String charsetEncoding) {
        Class msgClass = message.getClass();
        for (Field field : message.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                byte[] value = nv.get(name);
                if (value != null)
                    field.set(message, new String(value, charsetEncoding));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
