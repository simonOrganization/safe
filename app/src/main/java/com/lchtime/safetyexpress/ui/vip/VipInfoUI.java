package com.lchtime.safetyexpress.ui.vip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.CardBean;
import com.lchtime.safetyexpress.bean.CheckUpdataBean;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.PostBean;
import com.lchtime.safetyexpress.bean.ProfessionBean;
import com.lchtime.safetyexpress.bean.UpdataBean;
import com.lchtime.safetyexpress.bean.VipInfoBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.chat.hx.DemoHelper;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.DialogUtil;
import com.lchtime.safetyexpress.utils.LoginInternetRequest;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    private static final int NIKNAME_CODE = 0;
    private static final int PART_CODE = 1;
    private static final int COMPANY_CODE = 2;
    private static final int SIMPLE_CODE = 3;
    private static final int CITY_CODE = 4;

    private static int currentOption = -1;
    //自定义选项卡
    private OptionsPickerView pvCustomOptions;
    //自定义事件选择卡
    private TimePickerView pvCustomTime;

    private ArrayList<CardBean> cardItem = new ArrayList<>();
    //选择拍摄或者选取头像的popwindow

    private PopupWindow popupWindow;

    private View contentView;
    //昵称
    @ViewInject(R.id.tv_info_nickname)
    private TextView tv_nikname;
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

    //手机号
    @ViewInject(R.id.tv_info_phone)
    private TextView tv_phone;

    //公司名称
    @ViewInject(R.id.tv_info_company_name)
    private TextView tv_company_name;
    //性别
    @ViewInject(R.id.tv_info_sex)
    private TextView tv_sex;

    //个人简介
    @ViewInject(R.id.tv_info_simple)
    private TextView tv_simple;

    //头像
    @ViewInject(R.id.viv_info_icon)
    private CircleImageView viv_icon;
    //整个activity
    @ViewInject(R.id.vip_info_ui)
    private View vip_info_ui;


    private String updateDate;
    private String userId;
    private Gson gson;
    private String phoneNum;
    private UpdataImageUtils updataImageUtils;
    private HashMap<String, String> map;

    private List<ProfessionBean.ProfessionItemBean> professionList;
    private List <PostBean.PostItemBean> postList;
    //临时存储个人信息
    private VipInfoBean.UserDetail allInfo;
    private String phtotoPath;

    private Handler handler ;
    private FunctionOptions options;
    private DialogUtil mDialog;


    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("个人资料");
        rightTextVisible("保存");
        mDialog = new DialogUtil(mContext);
        initPopWindow();
//        selectPicPop = new SelectPicPop(vip_info_ui, VipInfoUI.this, R.layout.activity_pic_pop);
        handler = new Handler();
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

        if (map == null) {
            map = new HashMap<>();
        }

        userId = SpTools.getString(this, Constants.userId,"");
        gson = new Gson();
        updataImageUtils = new UpdataImageUtils();
        professionList = new ArrayList<>();
        postList = new ArrayList<>();

        allInfo = new VipInfoBean.UserDetail();



        //初始化行业
        initHangYe();

        //初始化自定义选项卡
        initCustomOptionPicker();
        //初始化自定义事件选项卡
        initCustomTimePicker();

        //初始化行业、岗位、所在地的记录
        initCheckOptions();

        if (InitInfo.vipInfoBean != null) {
            initVipInfo(InitInfo.vipInfoBean);
        }
    }

    private void initCheckOptions() {
        CheckUpdataBean.ud_addr = InitInfo.vipInfoBean.user_detail.ud_addr;
        CheckUpdataBean.ud_profession = InitInfo.vipInfoBean.user_detail.ud_profession;
        CheckUpdataBean.ud_post = InitInfo.vipInfoBean.user_detail.ud_post;
    }

    private void initHangYe() {

        if (InitInfo.vipInfoBean != null) {
            if (InitInfo.professionBean == null || InitInfo.professionBean.hy == null||InitInfo.professionBean.hy.size() == 0) {
                LoginInternetRequest.getProfession(SpTools.getString(this, Constants.userId, ""), new LoginInternetRequest.ForResultListener() {
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
                        //初始化岗位
                        initGangWei();
                    }
                });
            }else {
                professionList.addAll(InitInfo.professionBean.hy);
                initGangWei();
            }
        }
    }

    private void initGangWei() {

        if (InitInfo.vipInfoBean != null ) {
            if (InitInfo.postBean == null || InitInfo.postBean.gw == null || InitInfo.postBean.gw.size() == 0) {
                LoginInternetRequest.getPost(SpTools.getString(this, Constants.userId, ""), new LoginInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if (!TextUtils.isEmpty(code)) {
                            PostBean postBean = gson.fromJson(code, PostBean.class);
                            if (postBean != null) {
                                postList.addAll(postBean.gw);
                            }
                        }else {
                            CommonUtils.toastMessage("初始化岗位失败！");
                        }

                    }
                });
            }else {
                postList.addAll(InitInfo.postBean.gw);
            }
        }
    }


    private void initVipInfo(VipInfoBean bean) {
        if (bean.user_detail != null) {
            tv_nikname.setText(TextUtils.isEmpty(bean.user_detail.ud_nickname) ? "未设置" : bean.user_detail.ud_nickname);
            //手机号码
            tv_phone.setText(getPhone(bean.user_detail.ub_phone));
            //岗位
            tv_dowhat.setText(TextUtils.isEmpty(bean.user_detail.ud_post) ? "未设置" : bean.user_detail.ud_post);
            //行业
            tv_work.setText(TextUtils.isEmpty(bean.user_detail.ud_profession) ? "未设置" : bean.user_detail.ud_profession);
            tv_workpart.setText(TextUtils.isEmpty(bean.user_detail.ud_bm) ? "未设置" : bean.user_detail.ud_bm);
            tv_address.setText(TextUtils.isEmpty(bean.user_detail.ud_addr) ? "未设置" : bean.user_detail.ud_addr);
            tv_company_name.setText(TextUtils.isEmpty(bean.user_detail.ud_company_name) ? "未设置" : bean.user_detail.ud_company_name);
            tv_birthday.setText(TextUtils.isEmpty(bean.user_detail.ud_borth) ? "未设置" : bean.user_detail.ud_borth);
            tv_sex.setText(TextUtils.isEmpty(bean.user_detail.ud_sex) ? "未设置" : bean.user_detail.ud_sex);
            //个人简介
            tv_simple.setText(TextUtils.isEmpty(bean.user_detail.ud_memo) ? "未设置" : bean.user_detail.ud_memo);


            File file = new File(MyApplication.getContext().getFilesDir(),Constants.photo_name);//将要保存图片的路径
            //如果没有加载过图片了
            if (!file.exists()){
                viv_icon.setImageDrawable(getResources().getDrawable(R.drawable.circle_user_image));
                if (!TextUtils.isEmpty(bean.user_detail.ud_photo_fileid)){
                    UpdataImageUtils.getUrlBitmap(bean.user_detail.ud_photo_fileid, new UpdataImageUtils.BitmapListener() {
                        @Override
                        public void giveBitmap(final Bitmap bitmap) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UpdataImageUtils.saveBitmapFile(bitmap,Constants.photo_name);
                                    viv_icon.setImageBitmap(bitmap);
                                }
                            });

                        }
                    });

                }

            }else {
                viv_icon.setImageBitmap(BitmapUtils.getBitmap(file.getAbsolutePath()));
            }
        }

    }
    /**
     * 获取加密的手机号
     */
    private String getPhone(String phone){
        if(TextUtils.isEmpty(phone))
            return "";
        String head = phone.substring(0 , 3);
        String end = phone.substring(7);

        return head + "****" + end;
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
        contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
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
        intent.putExtra("data",tv_nikname.getText().toString().trim());
        startActivityForResult(intent,NIKNAME_CODE);
    }

    /**
     * 公司名称
     * @param view
     */
    @OnClick(R.id.ll_info_company)
    private void getCompany(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoCompanyUI.class);
        intent.putExtra("data",tv_company_name.getText().toString().trim());
        startActivityForResult(intent,COMPANY_CODE);
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
        Intent intent = new Intent(this,VipInfoPartUI.class);
        intent.putExtra("data",tv_workpart.getText().toString().trim());
        startActivityForResult(intent,PART_CODE);
    }

    /**
     * 所在地
     * @param view
     */
    @OnClick(R.id.ll_info_address)
    private void getAddress(View view){
        Intent intent = new Intent(this,SelectCityActivity.class);
        startActivityForResult(intent,CITY_CODE);
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
     * 个人简介
     * @param view
     */
    @OnClick(R.id.ll_info_simple)
    private void getSimple(View view){
        Intent intent = new Intent(VipInfoUI.this, VipInfoSimpleUI.class);
        intent.putExtra("data",tv_simple.getText().toString().trim());
        startActivityForResult(intent,SIMPLE_CODE);
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
                        map.put("ud_profession",tx);
                        tv_work.setText(tx);
                        allInfo.ud_profession = tx;
                        CheckUpdataBean.ud_profession = tx;
                        break;
                    case WORK_DOWHAT:
                        map.put("ud_post",tx);
                        tv_dowhat.setText(tx);
                        allInfo.ud_post = tx;
                        CheckUpdataBean.ud_post = tx;
                        break;
                    case SEX:
                        map.put("ud_sex",tx);
                        tv_sex.setText(tx);
                        allInfo.ud_sex = tx;
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
                map.put("ud_borth",getTime(date));
                allInfo.ud_borth = getTime(date);
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
               if (professionList != null) {
                   for (int i = 0; i < professionList.size(); i++) {

                       cardItem.add(new CardBean(i, professionList.get(i).hy_name));

                   }
               }

                break;
            case WORK_DOWHAT:
                if (postList != null) {
                    for (int i = 0; i < postList.size(); i++) {

                        cardItem.add(new CardBean(i, postList.get(i).gw_name));

                    }
                }
                break;
            case SEX:
                cardItem.add(new CardBean(0, "男"));
                cardItem.add(new CardBean(1, "女"));

                break;
        }


    }

    @Override
    public void onClick(View v) {

        if(options == null){
            // 可选择图片的数量
            // 是否打开剪切选项
            options = new FunctionOptions.Builder()
                    .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                    .setSelectMode(FunctionConfig.MODE_SINGLE) // 可选择图片的数量
                    .setEnableCrop(true) // 是否打开剪切选项
                    .setShowCamera(false)
                    .setCropMode(FunctionConfig.CROP_MODEL_1_1)
                    .setCompress(true)
                    .setCompressFlag(1)
                    .create();
        }
        switch (v.getId()){
            case R.id.tv_picture_list:

                PictureConfig.getPictureConfig().init(options).openPhoto(this, resultCallback);
                break;
            case R.id.tv_takepic:
                PictureConfig.getPictureConfig().init(options).startOpenCamera(this, resultCallback);
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
            if (media.isCut() && media.isCompressed()) {
                // 裁剪过
                phtotoPath = media.getCompressPath();
                mDialog.show();
                updataImageUtils.upDataPic(phtotoPath, mDialog ,new UpdataImageUtils.UpdataPicListener() {
                    //上传头像的回调
                    @Override
                    public void onResponse(String response) {
                        UpdataBean updataBean = gson.fromJson(response, UpdataBean.class);
                        if (updataBean == null){
                            Toast.makeText(VipInfoUI.this,"上传图片失败",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isEmpty(phtotoPath)) {
                            viv_icon.setImageBitmap(BitmapUtils.getBitmap(phtotoPath));
                        }

                        if (updataBean != null&& updataBean.file_ids != null) {
                            allInfo.ud_photo_fileid = updataBean.file_ids.get(0);
                            //上传编辑信息
                            map.put("ud_photo_fileid", allInfo.ud_photo_fileid + "");
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NIKNAME_CODE){
            if (data != null) {
                String editNikname = data.getStringExtra("editNikname");
                map.put("ud_nickname",editNikname);
                tv_nikname.setText(editNikname);
                allInfo.ud_nickname = editNikname;
            }

        }else if (requestCode == PART_CODE){
            if (data != null) {
                String editPart = data.getStringExtra("editPart");
                map.put("ud_bm",editPart);
                tv_workpart.setText(editPart);
                allInfo.ud_bm = editPart;
            }

        }else if (requestCode == COMPANY_CODE){
            if (data != null) {
                String companyName = data.getStringExtra("companyName");
                map.put("ud_company_name",companyName);
                tv_company_name.setText(companyName);
                allInfo.ud_company_name = companyName;
            }

        }else if (requestCode == SIMPLE_CODE){
            if (data != null) {
                String simple = data.getStringExtra("simple");
                map.put("ud_memo",simple);
                tv_simple.setText(simple);
                allInfo.ud_memo = simple;
            }

        }else if (requestCode == CITY_CODE){
            if (data != null) {
                String city = data.getStringExtra("city");
                map.put("ud_addr",city);
                tv_address.setText(city);
                allInfo.ud_addr = city;
                CheckUpdataBean.ud_addr = city;
            }

        }


    }

    /**
     * 保存
     * @param
     */
    @Override
    protected void clickEvent() {
       if (TextUtils.isEmpty(CheckUpdataBean.ud_addr)||TextUtils.isEmpty(CheckUpdataBean.ud_post)||TextUtils.isEmpty(CheckUpdataBean.ud_profession)){
           Toast.makeText(this,"填写行业、岗位、地址三个必填选项才可上传",Toast.LENGTH_SHORT).show();
           return;
       }
        if (map.size() > 0) {
            changeInfo();
        }else {
            Toast.makeText(this,"您没有更改任何信息",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 修改个人资料
     */
    private void changeInfo() {
        mDialog.show();
        String uid = SpTools.getString(this, Constants.userId, "");
        LoginInternetRequest.editVipInfo(InitInfo.phoneNumber, map, uid, mDialog ,new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                //Toast.makeText(VipInfoUI.this, "上传成功", Toast.LENGTH_SHORT).show();
                //将裁剪得图片转换成bitmap
                Bitmap zoomBitMap = null;
                if (!TextUtils.isEmpty(phtotoPath)) {
                    zoomBitMap = BitmapFactory.decodeFile(phtotoPath);
                    UpdataImageUtils.saveBitmapFile(zoomBitMap, Constants.photo_name);//先保存文件到本地
                }
                InitInfo.vipInfoBean.user_detail.ud_nickname =
                        allInfo.ud_nickname == null ? InitInfo.vipInfoBean.user_detail.ud_nickname : allInfo.ud_nickname;
                //行业
                InitInfo.vipInfoBean.user_detail.ud_profession =
                        allInfo.ud_profession == null ? InitInfo.vipInfoBean.user_detail.ud_profession : allInfo.ud_profession;
                //岗位
                InitInfo.vipInfoBean.user_detail.ud_post =
                        allInfo.ud_post == null ? InitInfo.vipInfoBean.user_detail.ud_post : allInfo.ud_post;
                //岗位
                InitInfo.vipInfoBean.user_detail.ud_bm =
                        allInfo.ud_bm == null ? InitInfo.vipInfoBean.user_detail.ud_bm : allInfo.ud_bm;
                InitInfo.vipInfoBean.user_detail.ud_addr =
                        allInfo.ud_addr == null ? InitInfo.vipInfoBean.user_detail.ud_addr : allInfo.ud_addr;
                InitInfo.vipInfoBean.user_detail.ud_company_name =
                        allInfo.ud_company_name == null ? InitInfo.vipInfoBean.user_detail.ud_company_name : allInfo.ud_company_name;
                InitInfo.vipInfoBean.user_detail.ud_borth =
                        allInfo.ud_borth == null ? InitInfo.vipInfoBean.user_detail.ud_borth : allInfo.ud_borth;
                InitInfo.vipInfoBean.user_detail.ud_sex =
                        allInfo.ud_sex == null ? InitInfo.vipInfoBean.user_detail.ud_sex : allInfo.ud_sex;
                //备注
                InitInfo.vipInfoBean.user_detail.ud_memo =
                        allInfo.ud_memo == null ? InitInfo.vipInfoBean.user_detail.ud_memo : allInfo.ud_memo;
                InitInfo.vipInfoBean.user_detail.ud_photo_fileid =
                        allInfo.ud_photo_fileid == null ? InitInfo.vipInfoBean.user_detail.ud_photo_fileid : allInfo.ud_photo_fileid;



                //此处可以加上上传图片到环信的过程
                //EMClient.getInstance().updateCurrentUserNick(allInfo.ud_nickname == null ? InitInfo.vipInfoBean.user_detail.ud_nickname : allInfo.ud_nickname);
                if(zoomBitMap != null)
                DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(Bitmap2Bytes(zoomBitMap));
                if(allInfo.ud_nickname != null)
                DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(allInfo.ud_nickname);
                finish();
            }
        });
    }

    public byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }
}
