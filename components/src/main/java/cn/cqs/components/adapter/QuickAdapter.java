package cn.cqs.components.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.cqs.components.R;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    //多状态视图
    private View emptyView;
    private View errorView;
    private View loadingView;

    public QuickAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, T item) {

    }
    /**
     * 获取当前Holder位置的数据索引
     * @param helper
     * @return
     */
    public int getCurrentPosition(@NonNull BaseViewHolder helper){
        return helper.getCurrentPosition(getHeaderLayoutCount());
    }
    public void attachRecyclerView(RecyclerView recyclerView){
        if (recyclerView == null) throw new IllegalArgumentException("recyclerView == null");
        attachRecyclerView(recyclerView,new LinearLayoutManager(recyclerView.getContext()));
    }
    public void attachRecyclerView(RecyclerView recyclerView,RecyclerView.LayoutManager layoutManager){
        if (recyclerView == null) throw new IllegalArgumentException("recyclerView == null");
        initEmptyView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);
    }
    /**
     * 初始化空视图、错误视图、加载视图
     * @param recyclerView
     */
    public void initEmptyView(RecyclerView recyclerView){
        loadingView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.common_loading_view, (ViewGroup) recyclerView.getParent(), false);
        emptyView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEmptyView();
            }
        });
        errorView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.common_error_view, (ViewGroup) recyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickErrorView();
            }
        });
        View footView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.common_foot_view, (ViewGroup) recyclerView.getParent(), false);
        addFooterView(footView,0);
//        View headView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.common_head_view, (ViewGroup) recyclerView.getParent(), false);
//        addHeaderView(headView,0);

    }
    /**
     * 显示加载视图
     */
    public void showLoading(){
        if (loadingView != null){
            setEmptyView(loadingView);
        }
    }

    /**
     * 显示空视图
     */
    public void showEmptyView(){
        if (emptyView != null){
            setEmptyView(emptyView);
        }
    }

    /**
     * 显示错误视图
     */
    public void showErrorView(){
        if (errorView != null){
            setEmptyView(errorView);
        }
    }

    public void onClickEmptyView(){

    }
    public void onClickErrorView(){

    }
}
