package cn.dcy.threadpool.api.response;

import java.io.Serializable;

/**
 * @author Kyle
 * @date 2024/06/23
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 7000723935764546321L;

    private String code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
