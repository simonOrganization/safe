package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CardBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 个人资料
 * Created by user on 2017/4/15.
 */
@ContentView(R.layout.vip_info_ui)
public class VipInfoUI extends BaseUI implements View.OnClickListener,PopupWindow.OnDismissListener{

    public static final int WORK = 0;
    public static final int WORK_DOWHAT = 1;
    public static final int WORK_PART = 2;
    public static final int ADDRESS = 3;
    public static final int SEX = 5;

    private static int currentOption = -1;
    //自定义选项卡
    private OptionsPickerView pvCustomOptions;
    //自定义事件选择卡
    private TimePickerView pvCustomTime;

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    //选择拍摄或者选取头像的popwindow

    private PopupWindow popupWindow;

    private View contentView;
    //生日
    @ViewInject(R.id.tv_info_birthday)
    private TextView tv_birthday;
    //行业
    @ViewInject(R.id.tv_info_work)
    private TextView tv_work;
    //岗位
    @ViewInject(R.id.tv_info_dowhat)
    private TextView tv_dowhat;
    //部门
    @ViewInject(R.id.tv_info_workpart)
    private TextView tv_workpart;
    //所在地
    @ViewInject(R.id.tv_info_address)
    private TextView tv_address;
    //性别
    @ViewInject(R.id.tv_info_sex)
    private TextView tv_sex;

    //性别
    @ViewInject(R.id.vip_info_ui)
    private View vip_info_ui;


    private String updateDate;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("个人资料");

        initPopWindow();
//        selectPicPop = new SelectPicPop(vip_info_ui, VipInfoUI.this, R.layout.activity_pic_pop);
    }

    private void initPopWindow() {

        contentView = LayoutInflater.from(VipInfoUI.this).inflate(
                R.layout.activity_pic_pop, null);
        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(this);
        ColorDrawable dw = new ColorDrawable(55000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setFocusable(true);// 取得焦点
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的

        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);


    }

    @Override
    protected void prepareData() {

        //初始化自定义选项卡
        initCustomOptionPicker();
        //初始化自定义事件选项卡
        initCustomTimePicker();
    }


    /**
     * 头像
     * @param view
     */
    @OnClick(R.id.ll_info_icon)
    private void getPicture(View view){
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

        contentView.findViewById(R.id.tv_picture_list).setOnClickListener(this);
        contentView.findViewById(R.id.tv_takepic).setOnClickListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 昵称
     * @param view
     */
    @OnClick(R.id.ll_info_nickname)
    private void getNickname(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoNicknameUI.class);
        startActivity(intent);
    }

    /**
     * 行业
     * @param view
     */
    @OnClick(R.id.ll_info_work)
    private void getWork(View view){
        currentOption = WORK;
        getData(currentOption);
        pvCustomOptions.show(); //弹出自定义条件选择器
    }
    /**
     * 岗位
     * @param view
     */
    @OnClick(R.id.ll_info_dowhat)
    private void getDoWhat(View view){
        currentOption = WORK_DOWHAT;
        getData(currentOption);
        pvCustomOptions.show(); //弹出自定义条件选择器
    }
    /**
     * 部门
     * @param view
     */
    @OnClick(R.id.ll_info_workpart)
    private void getWorkPart(View view){
        currentOption = WORK_PART;
        getData(currentOption);
        pvCustomOptions.show(); //弹出自定义条件选择器
    }

    /**
     * 所在地
     * @param view
     */
    @OnClick(R.id.ll_info_address)
    private void getAddress(View view){
        currentOption = ADDRESS;
        getData(currentOption);
        pvCustomOptions.show(); //弹出自定义条件选择器
    }

    /**
     * 手机号
     * @param view
     */
    @OnClick(R.id.ll_info_phone)
    private void getPhone(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoPhoneUI.class);
        startActivity(intent);
    }

    /**
     * 生日
     * @param view
     */
    @OnClick(R.id.ll_info_birthday)
    private void getBirthday(View view){
        pvCustomTime.show();
    }
    /**
     * 性别
     * @param view
     */
    @OnClick(R.id.ll_info_sex)
    private void getSex(View view){
        currentOption = SEX;
        getData(currentOption);
        pvCustomOptions.show(); //弹出自定义条件选择器
    }

    /**
     * 密码
     * @param view
     */
    @OnClick(R.id.ll_info_password)
    private void getPassword(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoPasswordUI.class);
        startActivity(intent);
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();

                //选中条目回显到当前条目
                switch (currentOption){
                    case WORK:
                        tv_work.setText(tx);
                        break;
                    case WORK_DOWHAT:
                        tv_dowhat.setText(tx);
                        break;
                    case WORK_PART:
                        tv_workpart.setText(tx);
                        break;
                    case SEX:
                        tv_sex.setText(tx);
                        break;
                }
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


    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        selectedDate.set(1980,0,1);
        Calendar startDate = Calendar.getInstance();
        startDate.set(1960,0,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2017,0,1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_birthday.setText(getTime(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_sure);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(getResources().getColor(R.color.title_999))
                .setLabel("年","月","日","时","分","秒")
                .build();

    }

    //转换时间格式
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void getData(int what) {
        cardItem.clear();
        switch (what){
            case WORK:
                for (int i = 0; i < 5; i++) {
                    cardItem.add(new CardBean(i, "行业 " + i));
                }
                break;
            case WORK_DOWHAT:
                for (int i = 0; i < 5; i++) {
                    cardItem.add(new CardBean(i, "岗位 " + i));
                }
                break;
            case WORK_PART:
                for (int i = 0; i < 5; i++) {
                    cardItem.add(new CardBean(i, "部门 " + i));
                }
                break;
            case ADDRESS:
                for (int i = 0; i < 5; i++) {
                    cardItem.add(new CardBean(i, "所在地 " + i));
                }
                break;
            case SEX:
                cardItem.add(new CardBean(0, "男"));
                cardItem.add(new CardBean(0, "女"));

                break;
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_picture_list:
                FunctionOptions options = new FunctionOptions.Builder()
                        . setSelectMode(FunctionConfig.MODE_SINGLE) // 可选择图片的数量
                        .create();
                PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
                break;
            case R.id.tv_takepic:
                PictureConfig.getPictureConfig().startOpenCamera(this, resultCallback);
                break;
        }
        popupWindow.dismiss();
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            Log.i("callBack_result", resultList.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }

        }
    };

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }
}
