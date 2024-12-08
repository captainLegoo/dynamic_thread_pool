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

    private Response(Builder<T> builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static class Builder<T> {
        private String code;
        private String message;
        private T data;

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Response<T> build() {
            return new Response<>(this);
        }
    }
}
