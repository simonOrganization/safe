package com.lchtime.safetyexpress.ui.chat.hx.bean;

import com.hyphenate.easeui.bean.BasicResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class JoinGroups implements Serializable {


    /**
     * code : 10
     * info : 显示好友
     */

    public BasicResult result;

    public List<GroupBean> create_qun; //创建的群
    public List<GroupBean> add_qun;     //加入的群



}
