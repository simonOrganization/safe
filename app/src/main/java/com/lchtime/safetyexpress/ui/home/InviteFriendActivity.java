package com.lchtime.safetyexpress.ui.home;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.adapter.InviteFriendAdapter;
import com.lchtime.safetyexpress.ui.chat.hx.bean.ContactBean;
import com.lchtime.safetyexpress.bean.ContactListBean;
import com.lchtime.safetyexpress.ui.BaseUI;
import com.lchtime.safetyexpress.ui.home.protocal.HomeQuestionProtocal;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.refresh.PullLoadMoreRecyclerView;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by android-cp on 2017/5/24.邀请好友界面
 */
@ContentView(R.layout.activity_invite_friend)
public class InviteFriendActivity extends BaseUI {
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
    @BindView(R.id.rc_friend)
    PullLoadMoreRecyclerView rcFriend;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.empty)
    RelativeLayout empty;
    @BindView(R.id.error)
    RelativeLayout error;
    @BindView(R.id.success)
    LinearLayout success;
    @BindView(R.id.error_btn_retry)
    Button error_btn_retry;


    private InviteFriendAdapter adapter;
    private String q_id;
    public List<ContactBean> friendlist = new ArrayList<>();
    public Handler mHandler = new Handler();

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        ButterKnife.bind(this);
        setLoadingVisiblity();
        setTitle("邀请好友回答");
        q_id = getIntent().getStringExtra("q_id");
        adapter = new InviteFriendAdapter(friendlist,q_id);
        rcFriend.setLinearLayout();
        rcFriend.setAdapter(adapter);
        initListener();

    }

    private void initListener() {
        rcFriend.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                prepareData();
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rcFriend.setPullLoadMoreCompleted();
                    }
                },100);

            }
        });

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingVisiblity();
                prepareData();
            }
        });
    }

    private HomeQuestionProtocal protocal = new HomeQuestionProtocal();
    private Gson gson = new Gson();
    @Override
    protected void prepareData() {

        protocal.getMyFriends(new HomeQuestionProtocal.QuestionListener() {
            @Override
            public void questionResponse(Object response) {
                if (response == null){
                    //CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    rcFriend.setPullLoadMoreCompleted();
                    setErrorVisiblity();
                    return;
                }
                try {
                    ContactListBean bean = gson.fromJson((String) response, ContactListBean.class);
                    if ("10".equals(bean.result.code)){
                        friendlist.clear();
                        friendlist.addAll(bean.friendlist);
                        if (bean.friendlist == null || bean.friendlist.size() == 0){
                            setEmptyVisiblity();
                            return;
                        }
                        adapter.notifyDataSetChanged();
                        rcFriend.setPullLoadMoreCompleted();
                        setSuccessVisiblity();
                    }else {
                        //CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                        setErrorVisiblity();
                        rcFriend.setPullLoadMoreCompleted();
                    }
                }catch (Exception exception){
                    CommonUtils.toastMessage("请求好友数据失败，请稍后再试！");
                    setErrorVisiblity();
                    rcFriend.setPullLoadMoreCompleted();
                }

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
