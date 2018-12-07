package zerone.myapplication.domain;

import java.util.List;

/**
 * Created by Administrator on 2018-11-20.
 */

public class ResponseBean {

    /**
     * dbean : {"d_contents":"汀江国家湿地公园位于福建省长汀县三洲镇，范围涉及河田、三洲、濯田3个乡镇12个行政村，规划总面积590.9公顷，共设置保育区、宣教展示区、合理利用区和管理服务区等4个功能区，致力在保护湿地资源、景观资源基础上，将公园打造成集\u201c客家母亲河\u2014\u2014汀江生态修复典范\u201d","d_create_time":"2018-11-20 10:50:11","d_id":19,"duid":1}
     * duser : {"duid":1,"duname":"锅巴哈哈"}
     * list : ["https://ctwxl.com/images/dt/2df852f3cfdf4916882bf4981189916e.jpg","https://ctwxl.com/images/dt/63606709065b41669b63086b771a6791.jpg","https://ctwxl.com/images/dt/6edb1b1cf7a34c75bb12baf753667ddc.jpg","https://ctwxl.com/images/dt/c9a3221c634d43a9bae5237b314d29b5.jpg"]
     */

    private DbeanBean dbean;
    private DuserBean duser;
    private List<String> list;

    public DbeanBean getDbean() {
        return dbean;
    }

    public void setDbean(DbeanBean dbean) {
        this.dbean = dbean;
    }

    public DuserBean getDuser() {
        return duser;
    }

    public void setDuser(DuserBean duser) {
        this.duser = duser;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public static class DbeanBean {
        /**
         * d_contents : 汀江国家湿地公园位于福建省长汀县三洲镇，范围涉及河田、三洲、濯田3个乡镇12个行政村，规划总面积590.9公顷，共设置保育区、宣教展示区、合理利用区和管理服务区等4个功能区，致力在保护湿地资源、景观资源基础上，将公园打造成集“客家母亲河——汀江生态修复典范”
         * d_create_time : 2018-11-20 10:50:11
         * d_id : 19
         * duid : 1
         */

        private String d_contents;
        private String d_create_time;
        private int d_id;
        private int duid;

        public String getD_contents() {
            return d_contents;
        }

        public void setD_contents(String d_contents) {
            this.d_contents = d_contents;
        }

        public String getD_create_time() {
            return d_create_time;
        }

        public void setD_create_time(String d_create_time) {
            this.d_create_time = d_create_time;
        }

        public int getD_id() {
            return d_id;
        }

        public void setD_id(int d_id) {
            this.d_id = d_id;
        }

        public int getDuid() {
            return duid;
        }

        public void setDuid(int duid) {
            this.duid = duid;
        }
    }

    public static class DuserBean {
        /**
         * duid : 1
         * duname : 锅巴哈哈
         */

        private int duid;
        private String duname;

        public int getDuid() {
            return duid;
        }

        public void setDuid(int duid) {
            this.duid = duid;
        }

        public String getDuname() {
            return duname;
        }

        public void setDuname(String duname) {
            this.duname = duname;
        }
    }
}
