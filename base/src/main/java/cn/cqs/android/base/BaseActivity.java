package cn.cqs.android.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

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
    /**
     * 当前的TransitionAnimation
     */
    private TransitionEnum mTransitionEnum = TransitionEnum.SLIDE_RIGHT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
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
        LogUtils.e("onBackPressed");
        handleExitAnim();
    }

    @Override
    public void finish() {
        super.finish();
        handleExitAnim();
        LogUtils.e("finish");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * 是否启用页面跳转动画，默认不启用
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
            LogUtils.e("123");
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
