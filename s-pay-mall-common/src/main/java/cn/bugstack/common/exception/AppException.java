package cn.bugstack.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 应用自定义异常
 * @create 2024-02-25 12:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 8653090271840061986L;

    /**
     * 异常码
     */
    private String code;

    /**
     * 异常信息
     */
    private String info;

    public AppException(String code) {
        this.code = code;
    }

    public AppException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AppException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public AppException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

    @Override
    public String toString() {
        return "cn.bugstack.common.exception.AppException{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

}