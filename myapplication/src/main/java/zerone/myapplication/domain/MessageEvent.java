package zerone.myapplication.domain;

/**
 * Created by Administrator on 2018-11-24.
 */

public class MessageEvent {
    private String message;
    private int code;

    public MessageEvent(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
