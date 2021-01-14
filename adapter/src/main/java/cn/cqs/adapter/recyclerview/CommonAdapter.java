package cn.cqs.adapter.recyclerview;

import android.content.Context;

import java.util.List;

import cn.cqs.adapter.recyclerview.base.ItemViewDelegate;
import cn.cqs.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);
}
