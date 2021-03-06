package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lchtime.safetyexpress.CircleH5Activity;
import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.VideoH5Activity;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.circle.SubscribActivity;
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.login.LoginUI;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.vip.MyCircleActiveActivity;
import com.lchtime.safetyexpress.ui.vip.MyConllected;
import com.lchtime.safetyexpress.ui.vip.fragment.CircleFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ImageUtils;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.weight.LoginDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.lchtime.safetyexpress.ui.circle.CircleUI.CITY_DETAIL_CODE;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CircleHodler> {
    private final int MAX_LINE_COUNT = 6;       //显示的行数，超过隐藏
    private final int MAX_LINE = 1000;       //显示的最大行数
    private final int STATE_UNKNOW = -1;        //表示不知道现在是什么状态
    private final int STATE_NOT_OVERFLOW = 1;   //文本行数不能超过限定行数
    private final int STATE_COLLAPSED = 2;      //文本行数超过限定行数，没有进行折叠的
    private final int STATE_EXPANDED = 3;       //文本超过限定行数，被点击全文展开

    private Activity context;
    private List<QzContextBean> circleOneList;
    private boolean isCircle = false;
    private String ub_id;
    private String action;
    //private boolean isVideo = false;
    private SparseArray<Integer> mTextStateList;

    private boolean isShowDy = true;

    public CircleAdapter(Activity context, List<QzContextBean> circleOneList) {
        this.context = context;
        this.circleOneList = circleOneList;
        ub_id = SpTools.getUserId(context);
        mTextStateList = new SparseArray<>();
    }

    public CircleAdapter(Activity context, List<QzContextBean> circleOneList, boolean isShowDy) {
        this(context , circleOneList);
        this.isShowDy = isShowDy;
    }

    public void setShowDy(boolean isShowDy) {
        this.isShowDy = isShowDy;
    }

    @Override
    public CircleHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_item, parent, false);


        return new CircleHodler(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final CircleHodler holder, final int position) {
        Log.i("---------------", position + "");
//        holder.itemView.setTag(position);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int screenWith = ScreenUtil.getScreenSize(context)[0];
        final QzContextBean bean = circleOneList.get(position);
        //获取recycleView 的   下面可以设置item的宽
        ViewGroup.LayoutParams layoutParamsss = holder.circle_item_image_rc.getLayoutParams();

        //如果是长文章
        if(!TextUtils.isEmpty(bean.qc_cwz)){
            holder.mLongContentLl.setVisibility(View.VISIBLE);
            holder.mCircleRl.setVisibility(View.GONE);
            if(bean.qc_auth.trim().equals("")){
                holder.mLongAuthTv.setVisibility(View.GONE);
            }else{
                holder.mLongAuthTv.setText("作者：" + bean.qc_auth);
                holder.mLongAuthTv.setVisibility(View.VISIBLE);
            }
            if(bean.qc_context.trim().equals("")){
                holder.mLongContentTv.setVisibility(View.GONE);
            }else{
                holder.mLongContentTv.setVisibility(View.VISIBLE);
                holder.mLongContentTv.setText(bean.qc_context);
            }

            if(bean.qc_title.trim().equals("")){
                holder.mLongTitleTv.setVisibility(View.GONE);
            }else{
                holder.mLongTitleTv.setText(bean.qc_title);
                holder.mLongTitleTv.setVisibility(View.VISIBLE);
            }
            Glide.with(context)
                    //.load(bean.qc_cwz)
                    .load(bean.qc_cwz)
                    .placeholder(R.drawable.icon_long_image)
                    .error(R.drawable.icon_long_image)
                    .into(holder.mLongImg);
            holder.mLongContentLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, H5DetailUI.class);
                    intent.putExtra("newsId", bean.qc_id);
                    intent.putExtra("type", "circle");
                    context.startActivity(intent);
                }
            });
        }else{
            holder.mLongContentLl.setVisibility(View.GONE);
            holder.mCircleRl.setVisibility(View.VISIBLE);
            if (bean.qc_video == null || bean.qc_video.equals("0") || bean.qc_video.equals("")) {
                //如果有图片
                holder.circle_item_shipin_1.setVisibility(View.GONE);
                holder.circle_item_image_rc.setVisibility(View.VISIBLE);
                //一片张图
                if (bean.pic.size() == 1) {
                    layoutParamsss.width = screenWith / 3;
                    holder.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 1));
                } else if (bean.pic.size() == 4) {
                    //四张图片
                    //ViewGroup.LayoutParams layoutParamsss = holder.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = screenWith / 2;
                    holder.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 2));
                } else {
                    //多张图片
                    //ViewGroup.LayoutParams layoutParamsss = holder.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    holder.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 3));
                }
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.pic);
                holder.circle_item_image_rc.setAdapter(imageAdapter);
                imageAdapter.setOnItemSelectLs(new CircleImageAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Intent intent = new Intent(context, CirclePhone.class);
                        intent.putExtra("url", bean.pic);
                        intent.putExtra("pos", pos);
                        context.startActivity(intent);
                    }
                });
            } else {

                //视频
                if (bean.pic.size() > 0) {
                    //isVideo = true;
                    holder.circle_item_shipin_1.setVisibility(View.VISIBLE);
                    holder.iv_recommend_play.setVisibility(View.VISIBLE);
                    holder.circle_item_image_rc.setVisibility(View.GONE);
                    Glide.with(context).load(bean.pic.get(0))
                            //.transform(ImageUtils.getTransformation(holder.circle_item_shipin))
                            .into(holder.circle_item_shipin);
                    holder.circle_item_shipin_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MediaActivity.class);
                            intent.putExtra("url" , bean.qc_video);
                            intent.putExtra("img_url" , bean.pic.get(0));
                            context.startActivity(intent);
                            //JCVideoPlayer.releaseAllVideos();
                        }
                    });
                } else {
                    holder.circle_item_shipin_1.setVisibility(View.GONE);
                    holder.circle_item_image_rc.setVisibility(View.GONE);
                }

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        return;
                    }

                    Intent intent = new Intent(context, CircleH5Activity.class);
                    intent.putExtra("data", bean);
                    intent.putExtra("type", "circle");
                    context.startActivityForResult(intent , CITY_DETAIL_CODE);

                }
            });
        }

        final CircleProtocal protocal = new CircleProtocal();
        if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
            Glide.with(context).load(bean.ud_photo_fileid).into(holder.iv_circle_photo);
        } else {
            Glide.with(context).load(R.drawable.circle_user_image).into(holder.iv_circle_photo);
        }
        if (!TextUtils.isEmpty( bean.qc_ub_id)) {
            holder.iv_circle_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleInfoUI.class);
                    intent.putExtra("uid", bean.qc_ub_id);
                    context.startActivity(intent);
                }
            });
        }

        //holder.circle_item_company_name.setText(bean.user);
        holder.circle_item_title.setText(bean.qc_auth);
        holder.circle_item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleInfoUI.class);
                intent.putExtra("uid", bean.qc_ub_id);
                context.startActivity(intent);
            }
        });
        holder.contentTv.setText(bean.qc_context);
        holder.circle_item_talk.setText(bean.qc_pinglun);
        if (!isShowDy) {
            holder.circle_item_subscribe.setVisibility(View.GONE);
        }
        holder.circle_item_subscribe.setChecked("1".equals(bean.is_dy));
        holder.circle_item_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)) {
                    LoginDialog dialog = new LoginDialog(context, new LoginDialog.onClickLogin() {
                        @Override
                        public void OnClickLogin() {
                            Intent intent = new Intent(context,LoginUI.class);
                            context.startActivity(intent);
                        }
                    });
                    dialog.show();
                    holder.circle_item_subscribe.setChecked("1".equals(bean.is_dy));
                    return;
                } else {
                    //String type = "0".equals(bean.is_dy) ? "1" : "0";
                    String type = holder.circle_item_subscribe.isChecked() ? "1" : "0";
                    protocal.changeSubscribe(userid, bean.qc_ub_id, type, new CircleProtocal.CircleListener() {
                        @Override
                        public void circleResponse(CircleBean response) {
                            if (response == null) {
                                CommonUtils.toastMessage("网络请求失败");
                                return;
                            }
                            if (context instanceof CircleUI) {
                                ((CircleUI) context).refreshData("1");
                            }

                            if (context instanceof SubscribActivity) {
                                ((SubscribActivity) context).refreshData("1");
                            }
                        }
                    });
                }
            }
        });

        int state = mTextStateList.get(position, STATE_UNKNOW);

        switch (state) {
            case STATE_UNKNOW://        如果该item是第一次初始化，则取获取文本的行数
                holder.contentTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回调会调用多次，获取完行数后注销监听
                        holder.contentTv.getViewTreeObserver().removeOnPreDrawListener(this);
                        //holder.contentTv.getViewTreeObserver().addOnPreDrawListener(null);
                        //如果行数大于限定显示行数
                        if (holder.contentTv.getLineCount() > MAX_LINE_COUNT) {
                            holder.contentTv.setMaxLines(MAX_LINE_COUNT);
                            holder.hide_show_tv.setVisibility(View.VISIBLE);
                            holder.hide_show_tv.setText("展开");
                            //保存这条是超过规定行数的
                            mTextStateList.put(position, STATE_COLLAPSED);
                        } else {
                            holder.hide_show_tv.setVisibility(View.GONE);
                            //保存这条没有超过规定行数
                            mTextStateList.put(position, STATE_NOT_OVERFLOW);
                        }
                        return true;
                    }
                });
                break;
            case STATE_COLLAPSED: //超过行数的，未展开的
                holder.contentTv.setMaxLines(MAX_LINE_COUNT);
                holder.hide_show_tv.setVisibility(View.VISIBLE);
                holder.hide_show_tv.setText("展开");
                break;
            case STATE_NOT_OVERFLOW:   //没有超过行数的
                holder.hide_show_tv.setVisibility(View.GONE);
                break;
            case STATE_EXPANDED:        //超过行数，点击展开后的
                holder.contentTv.setMaxLines(MAX_LINE);
                holder.hide_show_tv.setVisibility(View.VISIBLE);
                holder.hide_show_tv.setText("收起");

                break;
        }

        //设置展开关闭按钮的点击监听
        holder.hide_show_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(position);
                switch (state) {
                    case STATE_COLLAPSED: //超过行数的，未展开的,点击"展开" 按钮
                        holder.contentTv.setMaxLines(MAX_LINE);
                        holder.hide_show_tv.setText("收起");
                        mTextStateList.put(position, STATE_EXPANDED);
                        break;
                    case STATE_EXPANDED:        //超过行数，点击展开后的，点击"收起" 按钮
                        holder.contentTv.setMaxLines(MAX_LINE_COUNT);
                        holder.hide_show_tv.setText("展开");
                        mTextStateList.put(position, STATE_COLLAPSED);
                        break;
                }


            }
        });

        //设置删除按钮
        setCheckBox(holder.rb_delete, position);
        //点赞
        setGreate(holder, bean, protocal);

        //比down
        setDown(holder, bean, protocal);

        holder.circle_item_time.setText(CommonUtils.getSpaceTime(Long.parseLong(bean.qc_date)));
        //holder.circle_item_subscribe_num.setText(bean.dyNum + "已订阅");

        //如果是本人发的圈子
        if (bean.qc_ub_id.equals(ub_id)) {
            holder.tv_delete.setVisibility(View.VISIBLE);
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCircle(position, bean.qc_id, protocal);
                }
            });
            holder.circle_item_subscribe.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_delete.setVisibility(View.INVISIBLE);
            if(isShowDy)
            holder.circle_item_subscribe.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 删除圈子
     */
    private void deleteCircle(final int position, String qc_id, CircleProtocal protocal) {
        //MyCircleActiveBean.QuanziBean bean = circleOneList.get(position);
        String userid = SpTools.getUserId(context);
        if (TextUtils.isEmpty(userid)) {
            CommonUtils.toastMessage("没有登陆！！");
            //holder.ivCircleItemGreat.setChecked("1".equals(bean.zan));
            return;
        }
        protocal.getDeleteCircle(userid, qc_id, new CircleProtocal.NormalListener() {
            @Override
            public void normalResponse(Object response) {
                if (response == null) {
                    CommonUtils.toastMessage("删除圈子失败，请稍后再试");
                    ((MyCircleActiveActivity) context).setIsLoading(false);
                    return;
                }
                Result bean = (Result) response;
                CommonUtils.toastMessage(bean.result.info);
                circleOneList.remove(position);
                ((CircleUI) context).notifyDataSetChanged();
                //notifyDataSetChanged();
                //((MyCircleActiveActivity)context).setIsLoading(false);
            }
        });
    }

    private boolean greate;
    private boolean down;

    private void setGreate(final CircleHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circle_item_great.setText(bean.qc_zc);
        Log.i("qaz", "setGreate: " + bean.qc_zc + "bean.qc_zc");
        holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
        //点赞逻辑监听
        holder.iv_circle_item_great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greate = !"1".equals(bean.zan);
                if (greate) {
                    action = "0";
                } else {
                    action = "1";
                }
                Log.i("qaz", "onClick: "+action );
                Log.i("qaz", "onClick: "+bean.zan  + "bean.zan");
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                    return;
                } else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "1", "0", action, new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            if (response == null) {
                                greate = true;
                               // holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            Result result = (Result) response;
                            Log.i("qaz", "normalResponse: " + result.dzNum + "------" + result.result.code);
                            if (!result.result.code.equals("10")) {
                               // holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            } else {
                                greate = false;
                                holder.circle_item_great.setText(result.dzNum);
                              //  holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof MyConllected){
                                    ((MyConllected) context).refreshItemData(bean.qc_id);
                                }

                            }
                           // holder.iv_circle_item_great.setClickable("1".equals(bean.zan));
                            CommonUtils.toastMessage(result.result.getInfo());
                        }
                    });

                }

            }
        });
    }

    private void setDown(final CircleHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circle_item_low.setText(bean.qc_fd);
        holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
        //点赞逻辑监听
       /* if ("1".equals(bean.cai)){
            holder.iv_circle_item_low.setClickable(false);
            return;
        }*/
        holder.iv_circle_item_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down = !"1".equals(bean.cai);
                if (down) {
                    action = "0";
                } else {
                    action = "1";
                }
                String userid = SpTools.getUserId(context);
                if (TextUtils.isEmpty(userid)) {
                    CommonUtils.toastMessage("没有登陆！！");
                    holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                    return;
                } else {
                    //请求网络数据
                    protocal.updataZanOrCai(userid, bean.qc_id, "0", "1", action, new CircleProtocal.NormalListener() {
                        @Override
                        public void normalResponse(Object response) {
                            if (response == null) {
                                down = true;
                                //holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                                holder.iv_circle_item_low.setClickable(true);
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            Result result = (Result) response;
                            if (!result.result.code.equals("10")) {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            } else {
                                greate = false;
                                holder.iv_circle_item_low.setText(result.dzNum);
                               // holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }else if (context instanceof MyConllected){
                                    ((MyConllected) context).refreshItemData(bean.qc_id);
                                }
                            }
                           // holder.iv_circle_item_low.setClickable(true);
                            CommonUtils.toastMessage(result.result.getInfo());
                        }
                    });

                    //holder.iv_circle_item_low.setChecked(true);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return circleOneList.size();
    }

    /**
     * 更新标记
     */
    public void upDateLabel() {
        mTextStateList.clear();
    }


    class CircleHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.circle_item_image_rc)
        RecyclerView circle_item_image_rc;
        @BindView(R.id.circle_item_shipin)
        ImageView circle_item_shipin;
        @BindView(R.id.iv_circle_photo)
        ImageView iv_circle_photo;
        /*@BindView(R.id.circle_item_company_name)
        TextView circle_item_company_name;*/
        @BindView(R.id.circle_item_title)
        TextView circle_item_title;
        @BindView(R.id.circle_item_content)
        TextView contentTv;
        @BindView(R.id.hide_show)
        TextView hide_show_tv;
        @BindView(R.id.circle_item_talk)
        TextView circle_item_talk;
        @BindView(R.id.circle_item_great)
        TextView circle_item_great;
        //点赞图片
        @BindView(R.id.iv_circle_item_great)
        RadioButton iv_circle_item_great;
        @BindView(R.id.circle_item_low)
        TextView circle_item_low;
        //比down图片
        @BindView(R.id.iv_circle_item_low)
        RadioButton iv_circle_item_low;
        @BindView(R.id.circle_item_time)
        TextView circle_item_time;
        @BindView(R.id.circle_item_subscribe)
        CheckBox circle_item_subscribe;
        /*@BindView(R.id.circle_item_subscribe_num)
        TextView circle_item_subscribe_num;*/
        @BindView(R.id.rb_delete)
        CheckBox rb_delete;
        @BindView(R.id.circle_item_shipin_1)
        RelativeLayout circle_item_shipin_1;
        @BindView(R.id.tv_delete)
        TextView tv_delete;

        @BindView(R.id.iv_recommend_play)
        ImageView iv_recommend_play;

        @BindView(R.id.rl_content)
        RelativeLayout mCircleRl; //正常圈子的内容

        @BindView(R.id.ll_long_circle)
        LinearLayout mLongContentLl; //长文章的内容
        @BindView(R.id.iv_image)
        ImageView mLongImg;             //长文章的图片
        @BindView(R.id.tv_title)
        TextView mLongTitleTv;
        @BindView(R.id.tv_auther)
        TextView mLongAuthTv;
        @BindView(R.id.tv_content)
        TextView mLongContentTv;



        public CircleHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void setIsCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    public List<QzContextBean> list = new ArrayList<>();

    public List<QzContextBean> getUpdataList() {
        return list;
    }

    private Fragment fragment;
    private boolean flag = false;

    public void setCheckBoxShow(Fragment fragment, boolean isShow) {
        this.fragment = fragment;
        flag = isShow;
    }

    public void setCheckBox(CheckBox rb, final int position) {
        //设置删除按钮
        if (flag == true) {

            //设置checkbox的自定义背景

            /*Drawable drawable = fragment.getActivity().getResources().getDrawable(R.drawable.rb_delete);

            //设置drawable对象的大小
            drawable.setBounds(0,0,20,20);

            //设置CheckBox对象的位置，对应为左、上、右、下
            rb.setCompoundDrawables(drawable,null,null,null);
            */
            //显示
            rb.setVisibility(View.VISIBLE);
            final QzContextBean bean = circleOneList.get(position);
            rb.setChecked(bean.isCheck);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof CircleFragment) {
                        //如果之前不是选中的状态
                        if (!bean.isCheck) {
                            list.add(bean);
                            ((CircleFragment) fragment).updataDeleteNum(1);

                        } else {
                            list.remove(bean);
                            ((CircleFragment) fragment).updataDeleteNum(-1);
                        }
                    }
                    bean.isCheck = !bean.isCheck;

                }
            });

        } else {
            //隐藏
            rb.setVisibility(View.GONE);
        }
    }


}
