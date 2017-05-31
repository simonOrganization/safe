package com.lchtime.safetyexpress.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lchtime.safetyexpress.MyApplication;
import com.lchtime.safetyexpress.R;
import com.lchtime.safetyexpress.utils.WrapperUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;


/**
 * Created by zhy on 16/6/23.
 */
public class CircleHeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public CircleHeaderAndFooterWrapper(RecyclerView.Adapter adapter)
    {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mHeaderViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null)
        {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isHeaderViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position))
        {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount()
    {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (isHeaderViewPos(position))
        {
            return;
        }
        if (isFooterViewPos(position))
        {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }


    @Override
    public int getItemCount()
    {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null)
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position))
        {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view)
    {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view)
    {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    public int getFootersCount()
    {
        return mFootViews.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public long getHeaderId(int position) {
        //long headName = dataList.get(position).name.hashCode();

        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = View.inflate(MyApplication.getContext(),R.layout.circle_header,null);
        return new MyHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        LinearLayout circle_work;
        LinearLayout circle_gangwei;
        LinearLayout circle_address;
        ImageView circle_more;
        //行业
        TextView tv_hy_selected;
        //岗位
        TextView tv_gw_selected;
        //地区
        TextView tv_addr_selected;
        ImageView hy_indicator;
        ImageView gw_indicator;
        ImageView addr_indicator;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            circle_work = (LinearLayout) itemView.findViewById(R.id.circle_work);
            circle_gangwei = (LinearLayout) itemView.findViewById(R.id.circle_gangwei);
            circle_address = (LinearLayout) itemView.findViewById(R.id.circle_address);
            circle_more = (ImageView) itemView.findViewById(R.id.circle_more);
//        circle_layout_view = headerView.findViewById(R.id.circle_layout_view);
            tv_hy_selected = (TextView) itemView.findViewById(R.id.tv_hy_selected);
            tv_gw_selected = (TextView) itemView.findViewById(R.id.tv_gw_selected);
            tv_addr_selected = (TextView) itemView.findViewById(R.id.tv_addr_selected);
            hy_indicator = (ImageView) itemView.findViewById(R.id.iv_hy_indicator);
            gw_indicator = (ImageView) itemView.findViewById(R.id.iv_gw_indicator);
            addr_indicator = (ImageView) itemView.findViewById(R.id.iv_addr_indicator);
        } //head
    }

}
