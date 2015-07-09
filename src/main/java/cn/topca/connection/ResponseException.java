package cn.topca.connection;

/**
 * Copyright (c) 2011-2014 北京天诚安信科技有限公司 <service-appeal@topca.cn>
 * <p/>
 * 响应错误信息
 * 所有的响应错误信息通过该异常类通知上层
 *
 * @author 王翾旻 <wang_xuanmin@itrus.com.cn>
 * @version 2014-04-10 16:58
 */
public class ResponseException extends Exception {

    /**
     * 构建异常响应错误信息
     *
     * @param errorCode 异常编码
     * @param message   异常信息
     */
    public ResponseException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 获取异常响应编码（由响应方提供）
     *
     * @return 异常响应编码
     */
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return "[ERR: 0x" + Integer.toHexString(errorCode) + "] " + super.getMessage();
    }

    /**
     * 异常响应编码（由响应方提供）
     */
    private final int errorCode;
}
