package com.lchtime.safetyexpress.bean;

import java.util.List;

/**
 * Created by android-cp on 2017/5/10.
 */

public class AddSubscribBean {

    /**
     * code : 10
     * info : 成功
     */

    public ResultBean result;
    /**
     * result : {"code":"10","info":"成功"}
     * tj : [{"ud_ub_id":1,"ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/92/2017/04/28/27eb23aea85e57ad4b5d4c663c81aff3.","ud_nickname":"安全快车用户8814","ud_profession":"互联网/电子商务","ud_post":"打杂","ud_company_name":"","dy":4},{"ud_ub_id":89,"ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/89/2017/05/02/e728cc3961ce5e83f7a68716ac6e3c9b.png","ud_nickname":"Hrewhlrwjhtjkw","ud_profession":"计算机硬件","ud_post":"大幅度","ud_company_name":"","dy":0},{"ud_ub_id":98,"ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/82/2017/04/28/2c223800d88ef9f054f73ecd7e0943d6.","ud_nickname":"爸爸","ud_profession":"互联网/电子商务","ud_post":"双方都","ud_company_name":"","dy":0},{"ud_ub_id":99,"ud_photo_fileid":"http://fcar.lchtime.cn:8001/upload/99/2017/05/04/b64224ae6db6b31bc902d451b8a7d93e.png","ud_nickname":"呀呀呀","ud_profession":"互联网/电子商务","ud_post":"双方都","ud_company_name":"","dy":0}]
     * totalpage : 1
     */

    public int totalpage;
    /**
     * ud_ub_id : 1
     * ud_photo_fileid : http://fcar.lchtime.cn:8001/upload/92/2017/04/28/27eb23aea85e57ad4b5d4c663c81aff3.
     * ud_nickname : 安全快车用户8814
     * ud_profession : 互联网/电子商务
     * ud_post : 打杂
     * ud_company_name :
     * dy : 4
     */

    public List<ItemBean> tj;
    public List<ItemBean> all;

    public static class ResultBean {
        public String code;
        public String info;
    }

    public static class ItemBean {
        public String ud_ub_id;
        public String ud_photo_fileid;
        public String ud_nickname;
        public String dy;
        public String is_dy;
        public String user;

    }
}
