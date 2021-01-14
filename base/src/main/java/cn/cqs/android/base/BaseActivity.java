package cn.cqs.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;

import java.io.Serializable;
import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cqs.android.R;
import cn.cqs.android.enums.TransitionEnum;
import cn.cqs.android.utils.ActivityStackManager;
import cn.cqs.android.utils.PendingTransitionUtils;
import cn.cqs.android.utils.log.LogUtils;

/**
 * Created by Administrator on 2021/1/9.
 */

public class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    protected Activity activity;
    /**
     * 当前的TransitionAnimation
     */
    private TransitionEnum mTransitionEnum;
    private static final String TRANSITION_NAME = "transitionAnimation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        ARouter.getInstance().inject(this);
        initTransitionEnum();
    }

    protected void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**隐藏软键盘-一般是EditText.getWindowToken()
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 初始化沉浸式状态
     */
    protected void initImmersionbar() {
        ImmersionBar.with(this).init();
    }

    /**
     * 初始化当前的动画类型
     */
    private void initTransitionEnum() {
        Intent intent = getIntent();
        if (intent != null){
            mTransitionEnum = (TransitionEnum) getIntent().getSerializableExtra(TRANSITION_NAME);
            if (mTransitionEnum == null){
                mTransitionEnum = TransitionEnum.SLIDE_RIGHT;
            }
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        initImmersionbar();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initImmersionbar();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        intent.putExtra(TRANSITION_NAME,mTransitionEnum);
        super.startActivityForResult(intent, requestCode, options);
        if (openNavAnimate()){
            /**
             *  {@link PendingTransitionUtils#animateEnter(android.app.Activity,TransitionEnum)}进入动画
             */
            PendingTransitionUtils.animateEnter(this,mTransitionEnum);
        }
    }

    /**
     * 设置当前跳转动画
     * @param mTransitionEnum
     */
    public void setTransitionAnimation(TransitionEnum mTransitionEnum) {
        this.mTransitionEnum = mTransitionEnum;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleExitAnim();
    }

    @Override
    public void finish() {
        super.finish();
        handleExitAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * 是否启用页面跳转动画，默认启用
     * @return
     */
    protected boolean openNavAnimate(){
        return true;
    }
    private boolean isAnimExited = false;
    /**
     * 处理退出动画
     * {@link ActivityStackManager#getActivityStack()}当前页面栈只有一个的时候也不在执行退出动画
     */
    private void handleExitAnim(){
        if (openNavAnimate() && !isAnimExited){
            Stack<Activity> activityStack = ActivityStackManager.getActivityStack();
            if (activityStack == null || activityStack.size()<=1){

            } else {
                /**
                 *  {@link PendingTransitionUtils#animateExit(android.app.Activity,TransitionEnum)}退出动画
                 */
                PendingTransitionUtils.animateExit(this,mTransitionEnum);
                isAnimExited = true;
            }
        }
    }
}
