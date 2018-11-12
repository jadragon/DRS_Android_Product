package library.Component;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

public class AutoNewLineLayoutManager extends RecyclerView.LayoutManager {
    private int verticalScrollOffset = 0;
    private int totalHeight = 0;
    private Context context;
    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    //记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
    private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();
    private DisplayMetrics dm;
    private int divider;
    private int aloneType[] = {};

    public AutoNewLineLayoutManager(Context context) {
        this.context = context;
        dm = context.getResources().getDisplayMetrics();
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item，直接返回
        if (getItemCount() <= 0) return;
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        //定义竖直方向的偏移量
        int offsetX = getPaddingLeft();
        int offsetY = getPaddingTop();
        totalHeight = getPaddingTop();
        //上一位同學的高
        int preheigh = 0;
        int width=0;
        int height=0;
        //迴圈TAG
        aaa:
        for (int i = 0; i < getItemCount(); i++) {

            //这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            measureChildWithMargins(view, 0, 0);
            width = getDecoratedMeasuredWidth(view);
            height = getDecoratedMeasuredHeight(view);
            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            //獨立出設定好的viewType

            for (int viewtype : aloneType) {
                if (getItemViewType(view) == viewtype) {
                    offsetX = getPaddingLeft();

                    offsetY += (preheigh + divider);
                    totalHeight += (preheigh + divider);

                    frame.set(offsetX, offsetY, offsetX + width, offsetY + height);
                    offsetY += (height + divider);
                    totalHeight += (height + divider);
                    allItemFrames.put(i, frame);
                    hasAttachedItems.put(i, false);
                    preheigh += height;
                    continue aaa;
                }
            }
            //當寬度超過，換行
            if (offsetX + width + getPaddingRight() <= dm.widthPixels) {
                frame.set(offsetX, offsetY, offsetX + width, offsetY + height);
            } else {
                offsetX = getPaddingLeft();
                offsetY += (preheigh + divider);
                totalHeight += (preheigh + divider);
                frame.set(offsetX, offsetY, offsetX + width, offsetY + height);
            }
            offsetX += (width + divider);
            // 将当前的Item的Rect边界数据保存
            allItemFrames.put(i, frame);
            // 由于已经调用了detachAndScrapAttachedViews，因此需要将当前的Item设置为未出现过
            hasAttachedItems.put(i, false);
            //将竖直方向偏移量增大height
            preheigh = height;
        }
        //加入最後一行的高度
        totalHeight += (height+divider);


        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());
        recycleAndFillItems(recycler, state);

    }

    public void setAloneViewType(int... viewType) {
        this.aloneType = viewType;
    }

    /**
     * 回收不需要的Item，并且将需要显示的Item从缓存中取出
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) { // 跳过preLayout，preLayout主要用于支持动画
            return;
        }

        // 当前scroll offset状态下的显示区域
        Rect displayFrame = new Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.right = getDecoratedRight(child);
            childFrame.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayFrame, childFrame)) {
                //回收掉滑出屏幕的View
                removeAndRecycleView(child, recycler);

            }
        }

        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {

            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {

                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                //将这个item布局出来
                layoutDecorated(scrap,
                        frame.left,
                        frame.top - verticalScrollOffset,
                        frame.right,
                        frame.bottom - verticalScrollOffset);

            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //先detach掉所有的子View
        detachAndScrapAttachedViews(recycler);

        //实际要滑动的距离
        int travel = dy;

        //如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {//如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        //将竖直方向的偏移量+travel
        verticalScrollOffset += travel;

        // 平移容器内的item
        offsetChildrenVertical(-travel);
        recycleAndFillItems(recycler, state);
        Log.d("--->", " childView count:" + getChildCount());
        return travel;


    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
