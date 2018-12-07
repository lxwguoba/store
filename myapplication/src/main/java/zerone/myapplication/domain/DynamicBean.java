package zerone.myapplication.domain;

import java.util.List;

/**
 * Created by 刘兴文 on 2018-12-04.
 */

public class DynamicBean  {

    /**
     * id : 1
     * content : 这时候什么
     * views : 0
     * created_at : 1543912634
     * thumb : [{"id":6,"thumb":"http://zerone.01nnt.com/uploads/store/2/20181204045356880.jpg"},{"id":5,"thumb":"http://zerone.01nnt.com/uploads/store/2/20181204045356675.jpg"}]
     */

    private int id;
    private String content;
    private String views;
    private String created_at;
    private List<ThumbBean> thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<ThumbBean> getThumb() {
        return thumb;
    }

    public void setThumb(List<ThumbBean> thumb) {
        this.thumb = thumb;
    }

    public static class ThumbBean {
        /**
         * id : 6
         * thumb : http://zerone.01nnt.com/uploads/store/2/20181204045356880.jpg
         */

        private int id;
        private String thumb;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }
}
