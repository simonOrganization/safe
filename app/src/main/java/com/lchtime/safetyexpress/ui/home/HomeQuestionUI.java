package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.adapter.HomeQuestionAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.NewsBean;
import com.lchtime.safetyexpress.bean.WenDaBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.Const;
import com.lchtime.safetyexpress.ui.TabUI;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.lchtime.safetyexpress.R.id.ll_home_question;

/**
 * 疑难问答
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_question_ui)
public class HomeQuestionUI extends BaseUI {

    public static final int LOGIN = 111;
    //列表展示
    @ViewInject(R.id.lv_home_question)
    private PullLoadMoreRecyclerView lv_home_question;

    @ViewInject(R.id.tv_home_question)
    private TextView tv_home_question;


    @ViewInject(R.id.home_new_fragment_rc)
    private RecyclerView recyclerView;

    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    RelativeLayout success;
    @ViewInject(R.id.error_btn_retry)
    Button error_btn_retry;

    private HomeQuestionAdapter homeQuestionAdapter;
    private HomeQuestionProtocal protocal;
    private List<WenDaBean.TwBean> list = new ArrayList<>();
    private CircleImageView civ;
    private TextView nikName;
    private Handler handler = new Handler();
    private int footPage = 0;
    private View view;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("问答");
        view = View.inflate(this, R.layout.home_question_header,null);
        civ = (CircleImageView) view.findViewById(R.id.cv_wd_photo);
        nikName = (TextView) view.findViewById(R.id.tv_wd_nikname);

        LinearLayout ll_home_question = (LinearLayout) view.findViewById(R.id.ll_home_question);
        ll_home_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SpTools.getString(mContext , Constants.userId, ""))){
                    CommonUtils.toastMessage("登陆后才能看问答");
                    Intent intent = new Intent(mContext , LoginUI.class);
                    startActivityForResult(intent , LOGIN);
                    return;
                }
                //我的问答
                Intent intent = new Intent(HomeQuestionUI.this,MyQuestion.class);
                startActivity(intent);
            }
        });

        initMyInfo();
        initListener();


    }

    public void initMyInfo(){
        //如果没有登录
        if(TextUtils.isEmpty(SpTools.getString(mContext , Constants.userId, ""))){
            civ.setVisibility(View.GONE);
            nikName.setText("马上登录，参与问答");
        }else{
            civ.setVisibility(View.VISIBLE);
            File file = new File(MyApplication.getContext().getFilesDir(), Constants.photo_name);//将要保存图片的路径
            //如果没有加载过图片了
            if (!file.exists()){
                civ.setImageDrawable(getResources().getDrawable(R.drawable.vip_test_icon));
                if (!TextUtils.isEmpty(InitInfo.vipInfoBean.user_detail.ud_photo_fileid)){
                    UpdataImageUtils.getUrlBitmap(InitInfo.vipInfoBean.user_detail.ud_photo_fileid, new UpdataImageUtils.BitmapListener() {
                        @Override
                        public void giveBitmap(final Bitmap bitmap) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UpdataImageUtils.saveBitmapFile(bitmap,Constants.photo_name);
                                    civ.setImageBitmap(bitmap);
                                }
                            });

                        }
                    });

                }

            }else {
                civ.setImageBitmap(BitmapUtils.getBitmap(file.getAbsolutePath()));
            }
            if (InitInfo.vipInfoBean != null && InitInfo.vipInfoBean.user_detail != null) {
                nikName.setText(InitInfo.vipInfoBean.user_detail.ud_nickname);
            }
        }

    }

    private int totalPage = 1;
    private void initListener() {
        lv_home_question.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage = 0;
                        if (protocal == null){
                            protocal = new HomeQuestionProtocal();
                        }
                        protocal.getWenDaList("0",new HomeQuestionProtocal.QuestionListener() {
                            @Override
                            public void questionResponse(Object response) {
                                if(response == null){
                                    setErrorVisiblity();
                                    return;
                                }
                                final WenDaBean bean = (WenDaBean) response;
                                list.clear();
                                if (bean.tw != null) {
                                    list.addAll(bean.tw);
                                }
                                wrapper.notifyDataSetChanged();
                                lv_home_question.setPullLoadMoreCompleted();
                            }
                        });

                    }
                },0);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        footPage++;
                        if (footPage > totalPage){
                            CommonUtils.toastMessage("没有更多了！！！");
                            lv_home_question.setPullLoadMoreCompleted();
                            return;
                        }
                        if (protocal == null){
                            protocal = new HomeQuestionProtocal();
                        }
                        protocal.getWenDaList(footPage + "",new HomeQuestionProtocal.QuestionListener() {
                            @Override
                            public void questionResponse(Object response) {
                                if (response == null){
                                    CommonUtils.toastMessage("请检查网络");
                                    return;
                                }
                                final WenDaBean bean = (WenDaBean) response;
                                if (bean.tw != null) {
                                    list.addAll(bean.tw);
                                }
                                totalPage = bean.totalpage;
                                wrapper.notifyDataSetChanged();
                                lv_home_question.setPullLoadMoreCompleted();
                            }
                        });

                    }
                },0);
            }
        });

        tv_home_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SpTools.getString(mContext , Constants.userId, ""))){
                    CommonUtils.toastMessage("登陆后才能提问");
                    Intent intent = new Intent(mContext , LoginUI.class);
                    startActivityForResult(intent , LOGIN);
                    return;
                }
                if(isFullPersionDate()){//如果资料完善
                    //提问按钮的监听
                    Intent intent = new Intent(HomeQuestionUI.this,AskQuestionActivity.class);
                    startActivity(intent);
                }else{ //如果资料不完善
                    CommonUtils.toastMessage("请完善资料后提问");
                }
            }
        });


        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingVisiblity();
                initMyInfo();
                initListener();
                prepareData();
            }
        });
    }
    /**
     * 检查个人资料是否完善，检查行业，岗位，地理位置
     */
    private boolean isFullPersionDate(){
        return (!SpTools.getString(mContext , Constants.ud_profession , "").equals("")&&
                !SpTools.getString(mContext, Constants.ud_post , "").equals("")&&
                !SpTools.getString(mContext, Constants.ud_addr , "").equals(""));
    }


    private HeaderAndFooterWrapper wrapper;
    @Override
    protected void prepareData() {
        if (protocal == null){
            protocal = new HomeQuestionProtocal();
        }
        protocal.getWenDaList("0",new HomeQuestionProtocal.QuestionListener() {
            @Override
            public void questionResponse(Object response) {
                if (response == null){
                    setErrorVisiblity();
                    return;
                }
                final WenDaBean bean = (WenDaBean) response;
                list.clear();
                if (bean.tw != null) {
                    list.addAll(bean.tw);
                }
                if (list.size()== 0){
                    setEmptyVisiblity();
                    return;
                }
                totalPage = bean.totalpage;
                homeQuestionAdapter = new HomeQuestionAdapter(HomeQuestionUI.this,list);
                wrapper = new HeaderAndFooterWrapper(homeQuestionAdapter);
                wrapper.addHeaderView(view);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeQuestionUI.this));
                lv_home_question.setAdapter(wrapper);
                setSuccessVisiblity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN){
            initMyInfo();
        }
    }

    public void setLoadingVisiblity(){
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setEmptyVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
    }
    public void setErrorVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        success.setVisibility(View.GONE);
    }
    public void setSuccessVisiblity(){
        loading.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
    }

}
