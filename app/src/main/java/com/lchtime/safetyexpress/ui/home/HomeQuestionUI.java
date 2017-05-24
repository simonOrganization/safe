package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.BitmapUtils;
import com.lchtime.safetyexpress.utils.UpdataImageUtils;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.CircleImageView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 疑难问答
 * Created by user on 2017/4/18.
 */
@ContentView(R.layout.home_question_ui)
public class HomeQuestionUI extends BaseUI {

    //列表展示
    @ViewInject(R.id.lv_home_question)
    private PullLoadMoreRecyclerView lv_home_question;

    @ViewInject(R.id.tv_home_question)
    private TextView tv_home_question;


    @ViewInject(R.id.home_new_fragment_rc)
    private RecyclerView recyclerView;

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
                //我的问答
                Intent intent = new Intent(HomeQuestionUI.this,MyQuestion.class);
                startActivity(intent);
            }
        });

        initMyInfo();
        initListener();


    }

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
                                final WenDaBean bean = (WenDaBean) response;
                                list.clear();
                                if (bean.tw != null) {
                                    list.addAll(bean.tw);
                                }
                                homeQuestionAdapter.notifyDataSetChanged();
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
                        if (protocal == null){
                            protocal = new HomeQuestionProtocal();
                        }
                        protocal.getWenDaList(footPage + "",new HomeQuestionProtocal.QuestionListener() {
                            @Override
                            public void questionResponse(Object response) {
                                final WenDaBean bean = (WenDaBean) response;
                                if (bean.tw != null) {
                                    list.addAll(bean.tw);
                                }
                                homeQuestionAdapter.notifyDataSetChanged();
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
                //提问按钮的监听
                Intent intent = new Intent(HomeQuestionUI.this,AskQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initMyInfo(){
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

        nikName.setText(InitInfo.vipInfoBean.user_detail.ud_nickname);
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
                final WenDaBean bean = (WenDaBean) response;
                list.clear();
                if (bean.tw != null) {
                    list.addAll(bean.tw);
                }
                homeQuestionAdapter = new HomeQuestionAdapter(HomeQuestionUI.this,list);
                wrapper = new HeaderAndFooterWrapper(homeQuestionAdapter);
                wrapper.addHeaderView(view);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeQuestionUI.this));
                lv_home_question.setAdapter(wrapper);
            }
        });
    }

}
