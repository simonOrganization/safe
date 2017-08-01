package com.lchtime.safetyexpress.ui.home;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.HeaderAndFooterWrapper;
import com.lchtime.safetyexpress.adapter.HomeImgAdapter;
import com.lchtime.safetyexpress.adapter.QuetionDetailAdapter;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.InitInfo;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.WenDaDetailBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ImageUtils;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lchtime.safetyexpress.views.MyGridView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android-cp on 2017/5/11.           疑难问答的详情界面
 */
@ContentView(R.layout.home_question_detail)
public class HomeQuewstionDetail extends BaseUI {

    public static final int QUEWSTION_DETAIL_REQUEST = 100;
    public static final int QUEWSTION_DETAIL_RESULT = 101;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.v_title)
    TextView vTitle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_home_question)
    TextView tvHomeQuestion;

    @ViewInject(R.id.loading)
    RelativeLayout loading;
    @ViewInject(R.id.empty)
    RelativeLayout empty;
    @ViewInject(R.id.error)
    RelativeLayout error;
    @ViewInject(R.id.success)
    RelativeLayout success;

//    @BindView(R.id.rc_question_huifu)
//    RecyclerView rcQuestionHuifu;
    //列表展示
    @ViewInject(R.id.rc_question_huifu)
    private PullLoadMoreRecyclerView lv_home_question;
    @ViewInject(R.id.home_new_fragment_rc)
    private RecyclerView rcQuestionHuifu;

    TextView tvHomeQuestionTitle;
    TextView tvHomeQuestionDescrib;
    MyGridView mgvHomeQuestion;
    ImageView onePicHomeQuestion;
    TextView tvHomeQuestionNum;
    TextView tvHomeFocusNum;
    LinearLayout llInviteFriend;
    private String qid = "";

    private HomeQuestionProtocal protocal;
    private QuetionDetailAdapter HuiFuAdapter;
    private List<WenDaDetailBean.HdinfoBean> huiFuList = new ArrayList<>();
    private WenDaDetailBean detailBean;
    private HeaderAndFooterWrapper wrapperAdapter;
    private LinearLayout ll_guanzhu;
    private LinearLayout ll_yiguanzhu;
    private RelativeLayout rl_isguanzhu;
    private ImageView openIv;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InitInfo.wendaDetail){
            setControlBasis();
            prepareData();
            InitInfo.wendaDetail = false;
        }

    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setTitle("问答");
        View view = View.inflate(this,R.layout.qeuwstion_detail_header,null);
        initView(view);
        HuiFuAdapter = new QuetionDetailAdapter(HomeQuewstionDetail.this, huiFuList);
        wrapperAdapter = new HeaderAndFooterWrapper(HuiFuAdapter);
        wrapperAdapter.addHeaderView(view);
//        rcQuestionHuifu.setLayoutManager(new LinearLayoutManager(this));
        rcQuestionHuifu.setLayoutManager(new GridLayoutManager(this,1));
        rcQuestionHuifu.setAdapter(wrapperAdapter);
//        rcQuestionHuifu.setAdapter(HuiFuAdapter);
        qid = getIntent().getStringExtra("q_id");

        initListner();

    }

    private int page = 1;
    private int totalPage = 1;
    private void initListner() {
        tvHomeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SpTools.getString(mContext , Constants.userId, ""))) {
                    Intent intent = new Intent(HomeQuewstionDetail.this,LoginUI.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(HomeQuewstionDetail.this,AnswerQuestionActivity.class);
                    intent.putExtra("q_id",qid);
                    intent.putExtra("title",detailBean.wenti.q_title);
                    startActivityForResult(intent , QUEWSTION_DETAIL_REQUEST);
                }

            }
        });
        llInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SpTools.getString(mContext , Constants.userId, ""))) {
                    CommonUtils.toastMessage("您还未登录，暂无数据");
                }else{
                    Intent intent = new Intent(HomeQuewstionDetail.this,InviteFriendActivity.class);
                    intent.putExtra("q_id",qid);
                    startActivity(intent);
                }


            }
        });
        lv_home_question.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                refresh("1");
            }

            @Override
            public void onLoadMore() {
                page++;
                if (page > totalPage){
                   // CommonUtils.toastMessage("没有更多了");
                    lv_home_question.setPullLoadMoreCompleted();
                    return;
                }
                isLoadMore = true;
                refresh(page + "");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == QUEWSTION_DETAIL_RESULT){
            prepareData();
        }


    }

    private void initView(View view) {
        tvHomeQuestionTitle = (TextView) view.findViewById(R.id.tv_home_question_title);
        tvHomeQuestionDescrib = (TextView) view.findViewById(R.id.tv_home_question_describ);
        mgvHomeQuestion = (MyGridView) view.findViewById(R.id.mgv_home_question);
        onePicHomeQuestion = (ImageView) view.findViewById(R.id.one_pic_home_question);
        tvHomeQuestionNum = (TextView) view.findViewById(R.id.tv_home_question_num);
        tvHomeFocusNum = (TextView) view.findViewById(R.id.tv_home_focus_num);
        llInviteFriend = (LinearLayout) view.findViewById(R.id.ll_invite_friend);
        ll_guanzhu = (LinearLayout) view.findViewById(R.id.ll_guanzhu);
        ll_yiguanzhu = (LinearLayout) view.findViewById(R.id.ll_yiguanzhu);
        rl_isguanzhu = (RelativeLayout) view.findViewById(R.id.rl_isguanzhu);
        openIv = (ImageView) view.findViewById(R.id.iv_open);
    }


    @Override
    protected void prepareData() {
        String page = "1";
        refresh(page);
    }
    private boolean isLoadMore = false;
    private void refresh(final String page) {
        if (protocal == null) {
            protocal = new HomeQuestionProtocal();
        }
        protocal.getWenDaDetail(page,qid, new HomeQuestionProtocal.QuestionListener() {
            @Override
            public void questionResponse(Object response) {
                if (response == null){
                    setErrorVisiblity();
                    lv_home_question.setPullLoadMoreCompleted();
                    return;
                }
                final WenDaDetailBean bean = (WenDaDetailBean) response;
                detailBean = bean;
                totalPage = bean.totalpage;
                if (Integer.parseInt(page) == 0 || Integer.parseInt(page) == 1) {
                    tvHomeQuestionTitle.setText(bean.wenti.q_title);
                    tvHomeQuestionDescrib.setText(bean.wenti.q_description);
                    tvHomeQuestionNum.setText(bean.wenti.hdNum + "回答");
                    tvHomeFocusNum.setText(bean.wenti.gzNum + "人关注");
                    if ("0".equals(bean.wenti.is_gz)) {
                        ll_guanzhu.setVisibility(View.VISIBLE);
                        ll_yiguanzhu.setVisibility(View.GONE);
                    } else {
                        ll_guanzhu.setVisibility(View.GONE);
                        ll_yiguanzhu.setVisibility(View.VISIBLE);
                    }


                    rl_isguanzhu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击关注按钮
                            protocal.postGuanZhu(bean.wenti.q_id, bean.wenti.is_gz, new HomeQuestionProtocal.QuestionListener() {
                                @Override
                                public void questionResponse(Object response) {
                                    Result result = (Result) response;
                                    if ("10".equals(result.result.code)) {
                                        //刷新数据
                                        prepareData();
                                    }
                                    CommonUtils.toastMessage(result.result.getInfo());
                                }
                            });

                        }
                    });
                    if (bean.wenti.pic.size() == 1) {
                        //一张图
                        mgvHomeQuestion.setVisibility(View.GONE);
                        onePicHomeQuestion.setVisibility(View.VISIBLE);
                        Picasso.with(HomeQuewstionDetail.this).load(bean.wenti.pic.get(0)).transform(ImageUtils.getTransformation(onePicHomeQuestion)).into(onePicHomeQuestion);
                    } else {
                        //多张图或者没图
                        if (bean.wenti.pic.size() == 0) {
                            mgvHomeQuestion.setVisibility(View.GONE);
                            onePicHomeQuestion.setVisibility(View.GONE);
                        } else {
                            onePicHomeQuestion.setVisibility(View.GONE);
                            mgvHomeQuestion.setVisibility(View.VISIBLE);
                            HomeImgAdapter adapter = new HomeImgAdapter(HomeQuewstionDetail.this, bean.wenti.pic);
                            mgvHomeQuestion.setAdapter(adapter);
                        }
                    }
                    tvHomeQuestionDescrib.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            //会重复执行，获取到行数马上移除监听
                            tvHomeQuestionDescrib.getViewTreeObserver().removeOnPreDrawListener(this);
                            if(tvHomeQuestionDescrib.getLineCount() > 1){
                                tvHomeQuestionDescrib.setMaxLines(1);
                                openIv.setVisibility(View.VISIBLE);
                                openIv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tvHomeQuestionDescrib.setMaxLines(1000);
                                        openIv.setVisibility(View.GONE);
                                    }
                                });
                            }



                            return false;
                        }
                    });




                }
                //初始化评论
                if (!isLoadMore) {
                    huiFuList.clear();
                }
                huiFuList.addAll(bean.hdinfo);
//                if (huiFuList.size() == 0 ){
//                    setEmptyVisiblity();
//                    return;
//                }
                wrapperAdapter.notifyDataSetChanged();
                setSuccessVisiblity();
                lv_home_question.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_home_question.setPullLoadMoreCompleted();
                    }
                },500);

                isLoadMore = false;
            }
        });
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
