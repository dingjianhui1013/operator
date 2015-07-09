package com.itrus.ca.modules.connection;

import cn.topca.connection.Connection;
import cn.topca.core.NoSuchServiceAlgorithm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 测试类...
 * @author ZhangJingtao
 *
 */
public class CaConnectionExample {
    public static void main(String[] args) throws IOException, NoSuchServiceAlgorithm {
        // 注册连接服务提供者（只需要注册一次）
        registerProvider();
        // 调用连接服务
        connectTest1();
        connectTest2();
    }

    /**
     * 注册连接服务提供者
     */
    static void registerProvider() {
        CaConnectionPoolProvider connectionProvider = CaConnectionPoolProvider.getInstance();
        connectionProvider.register();
    }

    static void connectTest1() throws IOException, NoSuchServiceAlgorithm {
        Connection connection = getConnectionWithName();
        System.out.println(connect(connection, "http://www.baidu.com/"));
    }

    static void connectTest2() throws IOException, NoSuchServiceAlgorithm {
        Connection connection = getConnectionWithAlias();
        System.out.println(connect(connection, "https://www.baidu.com/"));
    }

    /**
     * 使用连接服务连接指定URL
     *
     * @param connection 连接服务实例
     * @param url        目标URL
     * @return 从目标URL获取的文本信息(以UTF8编码)
     * @throws IOException
     */
    static String connect(Connection connection, String url) throws IOException {
        connection.connect(url);
        InputStream in = connection.getSession().getInputStream();
        byte[] response = readResponse(in);
        if (response != null)
            return new String(response, "UTF-8");
        return null;
    }

    /**
     * 从输入流读取所有数据
     *
     * @param in 输入流
     * @return 从输入流读取到的数据
     * @throws IOException
     */
    static byte[] readResponse(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int l = in.read(buf);
            if (l < 1)
                break;
            out.write(buf, 0, l);
        }
        if (out.size() > 0)
            return out.toByteArray();
        return null;
    }

    /**
     * 通过注册的算法类型获取连接服务实例
     *
     * @return 连接服务实例
     */
    static Connection getConnectionWithName() throws NoSuchServiceAlgorithm {
        return Connection.getInstance("ConnectionPool");
    }

    /**
     * 通过注册的算法别名获取连接服务实例
     *
     * @return 连接服务实例
     */
    static Connection getConnectionWithAlias() throws NoSuchServiceAlgorithm {
        return Connection.getInstance("HTTP");
    }
}
