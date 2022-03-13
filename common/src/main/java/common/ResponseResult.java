package common;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 4024631868353586264L;
    private int status;
    private String message;
    private T body;

    public int getStatus() {
        return status;
    }

    public ResponseResult<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ResponseResult<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public static <T> ResponseResult<T> ok() {
        return new ResponseResult<T>()
                .setMessage(ResponseCode.OK.getReasonPhrase())
                .setStatus(ResponseCode.OK.value());
    }

    public static <T> ResponseResult<T> ok(T body) {
        return new ResponseResult<T>()
                .setBody(body)
                .setMessage(ResponseCode.OK.getReasonPhrase())
                .setStatus(ResponseCode.OK.value());
    }

    public static <T> ResponseResult<T> status(ResponseCode status) {
        return new ResponseResult<T>()
                .setStatus(status.value())
                .setMessage(status.getReasonPhrase());
    }

    public static <T> ResponseResult<T> failed() {
        return new ResponseResult<T>()
                .setStatus(ResponseCode.INTERNAL_SERVER_ERROR.value())
                .setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static <T> ResponseResult<T> failed(String message) {
        return new ResponseResult<T>()
                .setStatus(ResponseCode.INTERNAL_SERVER_ERROR.value())
                .setMessage(ResponseCode.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static <T> ResponseResult<T> lockFailure() {
        return new ResponseResult<T>()
                .setStatus(ResponseCode.INTERNAL_SERVER_ERROR.value())
                .setMessage("lock failure");
    }
}
