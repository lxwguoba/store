package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-12.
 */

public class MessageS implements Serializable {
    private CashFlowBean message;

    public CashFlowBean getMessage() {
        return message;
    }

    public void setMessage(CashFlowBean message) {
        this.message = message;
    }
}
