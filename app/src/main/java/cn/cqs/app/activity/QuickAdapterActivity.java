package cn.cqs.app.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.route.DefaultNavCallback;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.app.R;
import cn.cqs.app.bean.CheckBoxItem;
import cn.cqs.components.adapter.QuickAdapter;
import cn.cqs.components.titlebar.OnClickTitleBarImpl;
import cn.cqs.components.titlebar.TitleBar;

public class QuickAdapterActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    private QuickAdapter<CheckBoxItem> adapter;
    private List<CheckBoxItem> list = new ArrayList<>();
    private boolean mError = true;
    private boolean mNoData = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_adapter);
        mTitleBar.setRightTitle("打印数据");
        mTitleBar.setOnTitleBarListener(new OnClickTitleBarImpl(){
            @Override
            public void onRightClick(View v) {
                String value = new Gson().toJson(list);
                LogUtils.e(value);
                toast(value);
            }
        });
        adapter = new QuickAdapter<CheckBoxItem>(R.layout.layout_checkbox_item,list){
            //当前EditText获取焦点时的索引
            private int etFocusPosition;
            @Override
            protected void convert(final com.chad.library.adapter.base.BaseViewHolder helper, final CheckBoxItem item) {
                super.convert(helper, item);
                //真实数据索引
                final int currentPosition = getCurrentPosition(helper);
                helper.setText(R.id.tv_name,item.name);
                helper.setText(R.id.edit_text,list.get(currentPosition).msg);
                CheckBox checkBox = helper.getView(R.id.checkbox);
                //checkBox一定要先清除监听器，避免View复用时数据混乱
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(list.get(currentPosition).checked);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        list.get(currentPosition).checked = isChecked;
                    }
                });
                final EditText editText = helper.getView(R.id.edit_text);
                final TextWatcher textWatcher = new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        list.get(currentPosition).msg = s.toString().trim();
                    }
                };
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            //etFocusPosition = helper.getLayoutPosition();
                            etFocusPosition = helper.getAdapterPosition();
                            //添加监听
                            editText.addTextChangedListener(textWatcher);
                            if (etFocusPosition == helper.getAdapterPosition()){
                                editText.setSelection(editText.getText().length());
                            }
                        } else {
                            editText.removeTextChangedListener(textWatcher);
                            if (etFocusPosition == helper.getAdapterPosition()){
                                hideSoftInputView();
                            }
                        }
                    }
                });
            }
            @Override
            public void onClickEmptyView() {
                refresh();
            }
        };
        //adapter.setHeaderAndEmpty(true);
        //设置头部和尾部都显示
        adapter.setHeaderFooterEmpty(true,true);
        adapter.openLoadAnimation();
        adapter.attachRecyclerView(recyclerView);
        adapter.enableLoadMoreEndClick(true);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                toast( "onItemChildClick" + position);
            }
        });
        refresh();
    }

    /**
     * 模拟请求数据
     */
    private void refresh(){
        adapter.showLoading();
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mError) {
                    adapter.showEmptyView();
                    mError = false;
                } else {
                    if (mNoData) {
                        adapter.showEmptyView();
                        mNoData = false;
                    } else {
                        list.clear();
                        for (int i = 0;i<20;i++){
                            list.add(new CheckBoxItem("列表："+i));
                        }
                        adapter.setNewData(list);
                    }
                }
            }
        },1000);
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this)
                .titleBar(R.id.title_bar)
                .statusBarDarkFont(true)
                .init();
    }
}
