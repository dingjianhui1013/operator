package com.itrus.ca.modules.connection;

import cn.topca.connection.ConnectionSessionSpi;
import cn.topca.connection.RequestMessage;
import cn.topca.connection.ResponseException;
import cn.topca.connection.ResponseMessage;
import cn.topca.connection.protocol.CodecProtocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * HttpClient链接ca会话
 * @author ZhangJingtao
 *
 */
public class ConnectionPoolSession extends ConnectionSessionSpi {
    private CAhttpClient client;
    private boolean closed;

    protected ConnectionPoolSession(CAhttpClient client) throws IOException {
        if(client==null)
            throw new IOException("please connect first");
        this.client = client;
    }

    /**
     * 设置会话超时时间
     *
     * @param timeout 超时时间
     * @throws java.io.IOException
     */
    @Override
    protected void engineSetSessionTimeout(int timeout) throws IOException {
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    /**
     * 获取读取超时时间
     *
     * @return 超时时间
     */
    @Override
    protected int engineGetSessionTimeout() {
        return client.getParams().getIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpConnectionManager.CONNECT_TIMEOUT);
    }

    /**
     * 获取用于写入数据的输出流
     *
     * @return OutputStream 输出流
     * @throws java.io.IOException
     * @see java.io.OutputStream
     */
    @Override
    protected OutputStream engineGetOutputStream() throws IOException {
        if (closed)
            throw new IOException("Session closed");
        return client.getOutputStream();
    }

    /**
     * 获取用于读取数据的输入流
     *
     * @return InputStream 输入流
     * @throws java.io.IOException
     * @see java.io.InputStream
     */
    @Override
    protected InputStream engineGetInputStream() throws IOException {
        if (closed)
            throw new IOException("Session closed");
        return client.getInputStream();
    }

    /**
     * 关闭会话
     *
     * @param immediately {@code true}立即关闭
     * @throws java.io.IOException
     */
    @Override
    public void engineClose(boolean immediately) throws IOException {
        if (!immediately)
                sleep(ONE_SECOND);
        client.getConnectionManager().shutdown();
        closed = true;
    }

    private final static long ONE_SECOND = 1000;

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
        }
    }

    /**
     * 获取会话关闭标记
     *
     * @return {@code true}会话已关闭
     * @throws java.io.IOException
     */
    @Override
    public boolean engineIsClosed() throws IOException {
        return closed;
    }

    
    
	@Override
	protected ResponseMessage engineDoRequest(RequestMessage message,
			CodecProtocol protocol) throws IOException, ResponseException {
		protocol.encode(message, engineGetOutputStream());
		sendPost();
        return (ResponseMessage) protocol.decode(engineGetInputStream());
	}

	protected void sendPost() {
		try {
			client.setOutputStream(client.getOutputStream());
			HttpResponse response = client.execute(client.getHttpPost());
			client.setInputStream(response.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
