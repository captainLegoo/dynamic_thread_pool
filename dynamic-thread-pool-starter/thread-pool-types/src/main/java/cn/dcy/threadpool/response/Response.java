package cn.dcy.threadpool.response;

/**
 * @author Kyle
 * @date 2024/06/23
 */
public class Response<T> {
    private String code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(T data) {
        this.code = "1";
        this.message = "success";
        this.data = data;
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
