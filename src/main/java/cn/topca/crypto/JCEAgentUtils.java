package cn.topca.crypto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * Crypto代理工具集，用于兼容未签名的JCE
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-29 15:31
 */
public class JCEAgentUtils {

    /**
     * SPI的方法具有保护权限，因此使用反射方式调用。
     * @param spi
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     */
    static Object invokeSpi(Object spi, String methodName, Class<?>[] parameterTypes,
                            Object... args) {
        try {
            Method method = spi.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(spi, args);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
