package zerone.myapplication.domain;

/**
 * Created by Administrator on 2018-11-20.
 */

public class ProgressBarBean {
    //总数量
    private Long total;
    //上传的数量
    private Long current;
    //进度
    private int percentage;


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public int getPercentage() {
        int p = (int) (current / total) * 100;
        return p;
    }
}
