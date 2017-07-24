package com.lchtime.safetyexpress.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.H5DetailUI;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.bean.BasicResult;
import com.lchtime.safetyexpress.bean.Constants;
import com.lchtime.safetyexpress.bean.MyCircleActiveBean;
import com.lchtime.safetyexpress.bean.QzContextBean;
import com.lchtime.safetyexpress.bean.Result;
import com.lchtime.safetyexpress.bean.res.CircleBean;
import com.lchtime.safetyexpress.ui.circle.CircleUI;
import com.lchtime.safetyexpress.ui.circle.SingleInfoUI;
import com.lchtime.safetyexpress.ui.circle.SubscribActivity;
import com.lchtime.safetyexpress.ui.circle.protocal.CirclePhone;
import com.lchtime.safetyexpress.ui.circle.protocal.CircleProtocal;
import com.lchtime.safetyexpress.ui.news.MediaActivity;
import com.lchtime.safetyexpress.ui.vip.MyCircleActiveActivity;
import com.lchtime.safetyexpress.ui.vip.fragment.CircleFragment;
import com.lchtime.safetyexpress.utils.CommonUtils;
import com.lchtime.safetyexpress.utils.ImageUtils;
import com.lchtime.safetyexpress.utils.ScreenUtil;
import com.lchtime.safetyexpress.utils.SpTools;
import com.lchtime.safetyexpress.views.SpacesItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yxn on 2017/4/20.
 */

public class CircleAdapter extends RecyclerView.Adapter{
    private Activity context;
    private List<QzContextBean> circleOneList;
    private boolean isCircle = false;
    private String ub_id;
    private String action;

    public CircleAdapter(Activity context, List<QzContextBean> circleOneList) {
        this.context = context;
        this.circleOneList = circleOneList;
        ub_id = SpTools.getString(context, Constants.userId, "");
    }

    private boolean isShowDy = true;

    public void setShowDy(boolean isShowDy) {
        this.isShowDy = isShowDy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_item, parent, false);
        if (!isShowDy) {
            view.findViewById(R.id.circle_item_subscribe).setVisibility(View.GONE);
            view.findViewById(R.id.circle_item_subscribe_num).setVisibility(View.GONE);
        }

        return new CircleHodler(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.i("---------------", position + "");
//        holder.itemView.setTag(position);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int screenWith = ScreenUtil.getScreenSize(context)[0];
        final QzContextBean bean = circleOneList.get(position);
        if (holder instanceof CircleHodler) {
            CircleHodler circleHodler = (CircleHodler) holder;

            //如果有图片
            if (bean.qc_video == null || bean.qc_video.equals("0") || bean.qc_video.equals("")) {
                ((CircleHodler) holder).circle_item_shipin_1.setVisibility(View.GONE);
                ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.VISIBLE);
                //一片张图
                if (bean.pic.size() == 1) {
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = screenWith / 3;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 1));
                } else if (bean.pic.size() == 4) {
                    //四张图片
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = screenWith / 2;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 2));
                } else {
                    //多张图片
                    ViewGroup.LayoutParams layoutParamsss = circleHodler.circle_item_image_rc.getLayoutParams();
                    layoutParamsss.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    circleHodler.circle_item_image_rc.setLayoutManager(new GridLayoutManager(context, 3));
                }
                CircleImageAdapter imageAdapter = new CircleImageAdapter(context, bean.pic);
                circleHodler.circle_item_image_rc.setAdapter(imageAdapter);
                imageAdapter.setOnItemSelectLs(new CircleImageAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(View v, int pos) {

                        Log.i("qaz", "onItemClick: "+ bean.pic);
                        Intent intent = new Intent(context, CirclePhone.class);
                        intent.putExtra("url",  bean.pic);

                        context.startActivity(intent);
                    }
                });
            } else {

                //视频
                if (bean.pic.size() > 0) {
                    ((CircleHodler) holder).circle_item_shipin_1.setVisibility(View.VISIBLE);
                    ((CircleHodler) holder).iv_recommend_play.setVisibility(View.VISIBLE);
                    ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.GONE);
                    Picasso.with(context).load(bean.pic.get(0))
                            .transform(ImageUtils.getTransformation(((CircleHodler) holder).circle_item_shipin))
                            .into(((CircleHodler) holder).circle_item_shipin);
                } else {
                    ((CircleHodler) holder).circle_item_shipin_1.setVisibility(View.GONE);
                    ((CircleHodler) holder).circle_item_image_rc.setVisibility(View.GONE);
                }

            }
            final CircleProtocal protocal = new CircleProtocal();
            if (!TextUtils.isEmpty(bean.ud_photo_fileid)) {
                Picasso.with(context).load(bean.ud_photo_fileid).into(((CircleHodler) holder).iv_circle_photo);
            } else {
                Picasso.with(context).load(R.drawable.circle_user_image).into(((CircleHodler) holder).iv_circle_photo);
            }
            ((CircleHodler) holder).iv_circle_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleInfoUI.class);
                    intent.putExtra("uid", bean.qc_ub_id);
                    context.startActivity(intent);
                }
            });
            ((CircleHodler) holder).circle_item_company_name.setText(bean.user);
            ((CircleHodler) holder).circle_item_title.setText(bean.qc_auth);
            ((CircleHodler) holder).circle_item_content.setText(bean.qc_context);
            ((CircleHodler) holder).circle_item_talk.setText(bean.qc_pinglun);
            ((CircleHodler) holder).circle_item_subscribe.setChecked("1".equals(bean.is_dy));
            ((CircleHodler) holder).circle_item_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userid = SpTools.getString(context, Constants.userId, "");
                    if (TextUtils.isEmpty(userid)) {
                        CommonUtils.toastMessage("请登陆！！！");
                        ((CircleHodler) holder).circle_item_subscribe.setChecked("1".equals(bean.is_dy));
                        return;
                    } else {
                        String type = "0".equals(bean.is_dy) ? "1" : "0";
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


            //设置删除按钮
            setCheckBox(((CircleHodler) holder).rb_delete, position);
            //点赞
            setGreate((CircleHodler) holder, bean, protocal);

            //比down
            setDown((CircleHodler) holder, bean, protocal);

            ((CircleHodler) holder).circle_item_time.setText(CommonUtils.getSpaceTime(Long.parseLong(bean.qc_date)));
            ((CircleHodler) holder).circle_item_subscribe_num.setText(bean.dyNum + "已订阅");
            //circleHodler.circle_item_image_rc.addItemDecoration(new SpacesItemDecoration(10));


            ((CircleHodler) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        return;
                    }
                    Intent intent = new Intent(context, H5DetailUI.class);
                    intent.putExtra("newsId", bean.qc_id);
                    intent.putExtra("type", "circle");
                    context.startActivity(intent);
                }
            });
            //如果是本人发的圈子
            if (bean.qc_ub_id.equals(ub_id)) {
                ((CircleHodler) holder).tv_delete.setVisibility(View.VISIBLE);
                ((CircleHodler) holder).tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCircle(position, bean.qc_id, protocal);
                    }
                });
            } else {
                ((CircleHodler) holder).tv_delete.setVisibility(View.GONE);
            }


        }


    }


    /**
     * 删除圈子
     */
    private void deleteCircle(final int position, String qc_id, CircleProtocal protocal) {
        //MyCircleActiveBean.QuanziBean bean = circleOneList.get(position);
        String userid = SpTools.getString(context, Constants.userId, "");
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
                ((CircleUI) context).notifyDataSetChange();
                //notifyDataSetChanged();
                //((MyCircleActiveActivity)context).setIsLoading(false);
            }
        });
    }

    private boolean greate;
    private boolean down;

    private void setGreate(final CircleHodler holder, final QzContextBean bean, final CircleProtocal protocal) {
        holder.circle_item_great.setText(bean.qc_zc);
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
                String userid = SpTools.getString(context, Constants.userId, "");
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
                                holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            } else {
                                greate = false;
                                holder.iv_circle_item_great.setChecked("1".equals(bean.zan));
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            }
                            holder.iv_circle_item_great.setClickable(true);
                            CommonUtils.toastMessage(result.getInfo());
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
                String userid = SpTools.getString(context, Constants.userId, "");
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
                                holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                                holder.iv_circle_item_low.setClickable(true);
                                CommonUtils.toastMessage("请求网络失败");
                                return;
                            }
                            BasicResult result = (BasicResult) response;
                            if (!result.code.equals("10")) {
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            } else {
                                greate = false;
                                holder.iv_circle_item_low.setChecked("1".equals(bean.cai));
                                if (context instanceof CircleUI) {
                                    ((CircleUI) context).refreshItemData(bean.qc_id);
                                } else if (context instanceof SubscribActivity) {
                                    ((SubscribActivity) context).refreshItemData(bean.qc_id);
                                }
                            }
                            holder.iv_circle_item_low.setClickable(true);
                            CommonUtils.toastMessage(result.getInfo());
                        }
                    });

                    //holder.iv_circle_item_low.setChecked(true);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
//        Log.i("mmmmmmmmmmmmmmmmmmmmm",circleOneList.size() + "");
        return circleOneList.size();
//        return 18;
    }



    class CircleHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.circle_item_image_rc)
        RecyclerView circle_item_image_rc;
        @BindView(R.id.circle_item_shipin)
        ImageView circle_item_shipin;
        @BindView(R.id.iv_circle_photo)
        ImageView iv_circle_photo;
        @BindView(R.id.circle_item_company_name)
        TextView circle_item_company_name;
        @BindView(R.id.circle_item_title)
        TextView circle_item_title;
        @BindView(R.id.circle_item_content)
        TextView circle_item_content;
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
        @BindView(R.id.circle_item_subscribe_num)
        TextView circle_item_subscribe_num;
        @BindView(R.id.rb_delete)
        CheckBox rb_delete;
        @BindView(R.id.circle_item_shipin_1)
        RelativeLayout circle_item_shipin_1;
        @BindView(R.id.tv_delete)
        TextView tv_delete;

        @BindView(R.id.iv_recommend_play)
        ImageView iv_recommend_play;

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
