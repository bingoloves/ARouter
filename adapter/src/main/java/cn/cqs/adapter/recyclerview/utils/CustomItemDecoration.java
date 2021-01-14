package cn.cqs.adapter.recyclerview.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义列表分割线，这里只处理非网格列表布局
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    /**
     * 水平分割线
     */
    private Drawable mHorizontalDivider;
    /**
     * 垂直分割线
     */
    private Drawable mVerticalDivider;
    /**
     * 上下左右分割线缩进值
     */
    private int leftInset,rightInset,topInset,bottomInset;
    /**
     * 当前的方向,是否是水平分割线
     */
    private boolean isHorizontal;
    /**
     * 当前画笔
     */
    private Paint mPaint;

    public CustomItemDecoration(Context context) {
        this(context,true);
    }

    public CustomItemDecoration(Context context, boolean isHorizontal) {
        init(context,isHorizontal);
    }

    public void setLeftInset(int leftInset) {
        this.leftInset = leftInset;
    }

    public void setRightInset(int rightInset) {
        this.rightInset = rightInset;
    }

    public void setTopInset(int topInset) {
        this.topInset = topInset;
    }

    public void setBottomInset(int bottomInset) {
        this.bottomInset = bottomInset;
    }

    /**
     * 默认是水平分割线，使用场景最多
     * @param context
     * @param isHorizontal
     */
    private void init(Context context,boolean isHorizontal){
        this.mContext = context;
        this.isHorizontal = isHorizontal;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }
    public void setHorizontalDividerDrawable(@DrawableRes int drawable){
        mHorizontalDivider = mContext.getResources().getDrawable(drawable);
    }
    public void setVerticalDividerDrawable(@DrawableRes int drawable){
        mVerticalDivider = mContext.getResources().getDrawable(drawable);
    }
    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (isHorizontal) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制垂直方向上的分割线
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        //最后一个item不画分割线
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mHorizontalDivider.getIntrinsicHeight();
            if (leftInset > 0) {
                c.drawRect(left, top, right, bottom, mPaint);
                if (right > 0) {
                    mHorizontalDivider.setBounds(left + leftInset, top, right - rightInset, bottom);
                } else {
                    mHorizontalDivider.setBounds(left + leftInset, top, right, bottom);
                }
            } else {
                if (right > 0) {
                    mHorizontalDivider.setBounds(left, top, right - rightInset, bottom);
                } else {
                    mHorizontalDivider.setBounds(left, top, right, bottom);
                }
            }
            mHorizontalDivider.draw(c);
        }
    }
    /**
     * 绘制水平方向上的分割线
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mVerticalDivider.getIntrinsicHeight();
            if (topInset > 0) {
                c.drawRect(left, top, right, bottom, mPaint);
                if (bottomInset > 0) {
                    mVerticalDivider.setBounds(left, top + topInset, right, bottom - bottomInset);
                } else {
                    mVerticalDivider.setBounds(left, top + topInset, right, bottom);
                }
            } else {
                if (bottomInset > 0) {
                    mVerticalDivider.setBounds(left, top, right, bottom - bottomInset);
                } else {
                    mVerticalDivider.setBounds(left, top, right, bottom);
                }
            }
            mVerticalDivider.draw(c);
        }
    }

    //由于Divider也有宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (isHorizontal) {
            outRect.set(0, 0, 0, mHorizontalDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mVerticalDivider.getIntrinsicWidth(), 0);
        }
    }
}