package com.lchtime.safetyexpress.ui.chat.hx.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CardBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dreamer on 2017/6/9.
 */

public class CardUtils {

    //自定义选项卡
    private static OptionsPickerView pvCustomOptions;
    private static ArrayList<CardBean> cardItem = new ArrayList<>();


    public static void show(TextView callBackView,Activity activity){
        //初始化行业
        initHangYe(activity);

        //初始化自定义选项卡
        initCustomOptionPicker(callBackView,activity);

        //行业
        if (cardItem.size() == 0 && professionList != null && professionList.size() != 0) {
            for (int i = 0; i < professionList.size(); i++) {

                cardItem.add(new CardBean(i, professionList.get(i).hy_name));

            }
        }

        pvCustomOptions.show(); //弹出自定义条件选择器
    }

    public static void initCustomOptionPicker(final TextView tv,Activity activity) {//条件选择器初始化，自定义布局


        pvCustomOptions = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();

                //选中条目回显到当前条目
//				map.put("ud_profession",tx);
                tv.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSure = (TextView) v.findViewById(R.id.tv_sure);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .isDialog(false)
                .build();

        pvCustomOptions.setPicker(cardItem);//添加数据

    }


    private static Gson gson = new Gson();
    private static List<ProfessionBean.ProfessionItemBean> professionList = new ArrayList<>();
    private static void initHangYe(Activity activity) {
        VipInfoBean vipInfoBean = SpTools.getUser(activity);
        if (vipInfoBean != null) {
            if (InitInfo.professionBean == null || InitInfo.professionBean.hy == null|| InitInfo.professionBean.hy.size() == 0) {
                LoginInternetRequest.getProfession(SpTools.getUserId(MyApplication.getContext()), new LoginInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if (!TextUtils.isEmpty(code)) {
                            ProfessionBean professionBean = gson.fromJson(code, ProfessionBean.class);
                            if (professionBean != null) {
                                professionList.addAll(professionBean.hy);

                            }
                        }else {
                            CommonUtils.toastMessage("初始化行业失败");
                        }
                    }
                });
            }else {
                professionList.addAll(InitInfo.professionBean.hy);
            }
        }
    }

}
