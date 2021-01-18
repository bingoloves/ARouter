package cn.cqs.android.base;

import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;

import cn.cqs.components.titlebar.OnClickTitleBarImpl;
import cn.cqs.components.titlebar.OnTitleBarListener;
import cn.cqs.components.titlebar.TitleBar;
import cn.cqs.components.titlebar.initializer.NightBarInitializer;

/**
 * Created by bingo on 2021/1/13.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/13
 */
public class SuportToolbarActivity extends BaseActivity {

    protected TitleBar mTitleBar;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initInnerTitleBar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initInnerTitleBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initInnerTitleBar();
    }

    @Override
    protected void initImmersionbar() {

    }

    /**
     * 初始化内部TitleBar
     */
    private void initInnerTitleBar(){
        if (mTitleBar == null){
            ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            if (rootView != null && rootView.getChildCount() != 0) {
                View container = rootView.getChildAt(0);
                rootView.removeView(container);
                mTitleBar = new TitleBar(this);
                initTitleBar(mTitleBar);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(mTitleBar);
                linearLayout.addView(container);
                rootView.addView(linearLayout);
            }
        }
        ImmersionBar.with(this).titleBar(mTitleBar).statusBarDarkFont(true).init();
    }
    private void initTitleBar(TitleBar titleBar){
        titleBar.setLeftTitle("返回");
        titleBar.setOnTitleBarListener(new OnClickTitleBarImpl(){
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }
    /**
     * 取消左侧图标
     */
    public void hideTitleBarLeftIcon(){
        mTitleBar.setLeftIcon(null);
    }
    /**
     * 设置titleBar文字
     *
     * @param title 标题文字
     */
    protected void setTitleText(String title) {
        mTitleBar.setTitle(title);
    }

    /**
     * 设置标题文字颜色
     *
     * @param titleBarColor 颜色
     */
    protected void setTitleTextColor(@ColorInt int titleBarColor) {
        mTitleBar.setTitleColor(titleBarColor);
    }
}
