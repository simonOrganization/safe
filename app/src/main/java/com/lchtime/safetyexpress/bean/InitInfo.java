package com.lchtime.safetyexpress.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android-cp on 2017/4/27.
 */

public class InitInfo {

    //public static VipInfoBean vipInfoBean;
    //public static String phoneNumber;
    //记录已经登录，但是否有网络的情况
    //public static boolean isLogin = false;
    //public static boolean isShowed = true;

    public static ProfessionBean professionBean;
    public static PostBean postBean;




    public static boolean circleRefresh = false;
    public static boolean homeRefresh = false;
    public static boolean wendaDetail = false;




    public static List<String> up_accounts = new ArrayList<>();

    public static boolean isShowJLGZ = true;
}
