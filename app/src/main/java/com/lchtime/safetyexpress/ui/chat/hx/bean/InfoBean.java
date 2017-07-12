package com.lchtime.safetyexpress.ui.chat.hx.bean;

import java.util.List;

/**
 * Created by Dreamer on 2017/6/8.
 */

public class InfoBean {

    /**
     * code : 10
     * info : 查找群成功
     */

    public ResultBean result;
    /**
     * ud_nickname : 98号测试群
     * ud_photo_fileid : false
     * sq_info : 测试
     * sq_profession : 建筑
     * sq_addr : 安徽
     * hx_account : 18083906387970
     * master : 13693600116
     * qun_type : 2
     * inQun : 0
     */

    public QunBean qun;
    /**
     * ud_ub_id : 98
     * ud_nickname : 梁仓
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/98/2017/05/24/cf56699db3f72e40fffdbdaeb878d393.png
     * hx_account : 13693600116
     * quner_type : 群主
     */

    public List<QunersBean> quners;


    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class QunBean {
        public String ud_nickname;
        public String ud_photo_fileid;
        public String sq_info;
        public String sq_profession;
        public String sq_addr;
        public String hx_account;
        public String master;
        public String qun_type;
        public String inQun;
    }

    public static class QunersBean {
        public String ud_ub_id;
        public String ud_nickname;
        public String ud_photo_fileid;
        public String hx_account;
        public String quner_type;

        //取用本地的图片
        public int pic_resource;
        public String toString() {
            String var1 = this.ud_nickname;
            return var1 ;
        }
    }
}
