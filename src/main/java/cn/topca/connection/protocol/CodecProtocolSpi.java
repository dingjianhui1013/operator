package cn.topca.connection.protocol;

import cn.topca.connection.InvalidCodecParameterException;
import cn.topca.connection.Message;
import cn.topca.core.ServiceProviderInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * 通信协议服务接口
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 13:59
 */
public abstract class CodecProtocolSpi implements ServiceProviderInterface {

    protected CodecProtocolSpi() {
    }

    /**
     * 设置并应用协议参数
     *
     * @param parameter 协议对信息进行封装、解析过程中需要的参数信息
     * @throws cn.topca.connection.InvalidCodecParameterException 当前协议无法识别协议参数或传入的协议参数不能满足协议要求时抛出异常
     */
    protected void engineSetParameter(CodecParameter parameter) throws InvalidCodecParameterException {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前协议参数
     *
     * @return 协议参数
     */
    protected CodecParameter engineGetParameter() {
        throw new UnsupportedOperationException();
    }


    /**
     * 对消息进行编码
     *
     * @param message 消息对象
     * @param out     通信输出流
     * @throws IOException
     */
    protected abstract void engineEncode(Message message, OutputStream out) throws IOException;

    /**
     * 对消息进行编码
     *
     * @param message 通信消息
     * @return 二进制编码
     * @throws IOException
     */
    protected byte[] engineEncode(Message message) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        engineEncode(message, out);
        return out.toByteArray();
    }

    /**
     * 对消息进行解码
     *
     * @param in 通信输入流
     * @return 消息对象
     * @throws IOException
     */
    protected abstract Message engineDecode(InputStream in) throws IOException;

    /**
     * 对消息进行解码
     *
     * @param encoded 消息二进制编码
     * @return 消息对象
     * @throws IOException
     */
    protected Message engineDecode(byte[] encoded) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(encoded);
        return engineDecode(in);
    }
}
