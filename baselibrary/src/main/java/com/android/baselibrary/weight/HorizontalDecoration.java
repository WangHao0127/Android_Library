package com.android.baselibrary.weight;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

/**
 * Author: WangHao
 * Created On: 2018/10/11 0011 15:39
 * Description:
 */
public class HorizontalDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int dividerWidth;

    public static final int RECOMMEND_ITEM_VIEW = 3;

    /**
     * @param spanCount      gridLayoutManager 列数
     * @param dividerWidthDp 分割块宽高,单位:dp
     */
    public HorizontalDecoration(Context context, int spanCount, int dividerWidthDp) {
        this.spanCount = spanCount;

        this.dividerWidth = DensityUtil.dp2px(context, dividerWidthDp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View child, RecyclerView parent,
        RecyclerView.State state) {
        super.getItemOffsets(outRect, child, parent, state);

        //        int position = parent.getChildAdapterPosition(child);
        //        int type = parent.getAdapter().getItemViewType(position);

        BaseQuickAdapter adapter = (BaseQuickAdapter) parent.getAdapter();
        int itemPosition =
            ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();

        if (adapter.getItemViewType(itemPosition)
            == BaseQuickAdapter.HEADER_VIEW) { //判断如果有头部，将在减去头部的位置
            return;
        }

        int column =
            (itemPosition - adapter.getHeaderLayoutCount()) % spanCount;// 计算这个child 处于第几列

        outRect.left = (column * dividerWidth / spanCount);
        outRect.right = dividerWidth - (column + 1) * dividerWidth / spanCount;

    }
}