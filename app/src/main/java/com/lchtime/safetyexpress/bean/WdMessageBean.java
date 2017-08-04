package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class WdMessageBean {

    /**
     * result : {"code":"10","info":"显示问题和答案详情成功"}
     * q_info : {"q_title":"高层发生火灾，怎样正确逃生自救？？？","pic":[]}
     * a_info : {"a_id":80,"aq_id":64,"aq_title":"高层发生火灾，怎样正确逃生自救？？？","a_ub_id":119,"a_context":"她现在你现在","a_dz":"0","a_dc":"2","a_create":"1498012627","pic":["http://www.aqkcw.com/upload/136/2017/07/13/d7183a01cc65fd3506d03fa6e08db1fc.jpg"],"fileids":["136_2017_07_13_d7183a01cc65fd3506d03fa6e08db1fc"]}
     */

    public ResultBean result;
    public QInfoBean q_info;
    public AInfoBean a_info;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public QInfoBean getQ_info() {
        return q_info;
    }

    public void setQ_info(QInfoBean q_info) {
        this.q_info = q_info;
    }

    public AInfoBean getA_info() {
        return a_info;
    }

    public void setA_info(AInfoBean a_info) {
        this.a_info = a_info;
    }

    public static class ResultBean {
        /**
         * code : 10
         * info : 显示问题和答案详情成功
         */

        public String code;
        public String info;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public static class QInfoBean {
        /**
         * q_title : 高层发生火灾，怎样正确逃生自救？？？
         * pic : []
         */

        public String q_title;
        public List<String> pic;

        public String getQ_title() {
            return q_title;
        }

        public void setQ_title(String q_title) {
            this.q_title = q_title;
        }

        public List<?> getPic() {
            return pic;
        }

        public void setPic(List<String> pic) {
            this.pic = pic;
        }
    }

    public static class AInfoBean {
        /**
         * a_id : 80
         * aq_id : 64
         * aq_title : 高层发生火灾，怎样正确逃生自救？？？
         * a_ub_id : 119
         * a_context : 她现在你现在
         * a_dz : 0
         * a_dc : 2
         * a_create : 1498012627
         * pic : ["http://www.aqkcw.com/upload/136/2017/07/13/d7183a01cc65fd3506d03fa6e08db1fc.jpg"]
         * fileids : ["136_2017_07_13_d7183a01cc65fd3506d03fa6e08db1fc"]
         */

        public int a_id;
        public int aq_id;
        public String aq_title;
        public int a_ub_id;
        public String a_context;
        public String a_dz;
        public String a_dc;
        public String a_create;
        public ArrayList<String> pic;
        public List<String> fileids;

        public int getA_id() {
            return a_id;
        }

        public void setA_id(int a_id) {
            this.a_id = a_id;
        }

        public int getAq_id() {
            return aq_id;
        }

        public void setAq_id(int aq_id) {
            this.aq_id = aq_id;
        }

        public String getAq_title() {
            return aq_title;
        }

        public void setAq_title(String aq_title) {
            this.aq_title = aq_title;
        }

        public int getA_ub_id() {
            return a_ub_id;
        }

        public void setA_ub_id(int a_ub_id) {
            this.a_ub_id = a_ub_id;
        }

        public String getA_context() {
            return a_context;
        }

        public void setA_context(String a_context) {
            this.a_context = a_context;
        }

        public String getA_dz() {
            return a_dz;
        }

        public void setA_dz(String a_dz) {
            this.a_dz = a_dz;
        }

        public String getA_dc() {
            return a_dc;
        }

        public void setA_dc(String a_dc) {
            this.a_dc = a_dc;
        }

        public String getA_create() {
            return a_create;
        }

        public void setA_create(String a_create) {
            this.a_create = a_create;
        }

        public ArrayList<String> getPic() {
            return pic;
        }

        public void setPic(ArrayList<String> pic) {
            this.pic = pic;
        }

        public List<String> getFileids() {
            return fileids;
        }

        public void setFileids(List<String> fileids) {
            this.fileids = fileids;
        }
    }
}
