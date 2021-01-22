package cn.cqs.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.cqs.logviewer.FloatingLogcatService;
import cn.cqs.logviewer.LogcatActivity;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.app.R;
import cn.cqs.app.bean.User;
import cn.cqs.service.constants.IRoutePath;

/**
 * 启动界面
 */
@Route(path = IRoutePath.SPLASH)
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(activity,MainActivity.class);
//                startActivity(intent);
                ARouter.getInstance().build("/main/main")
                        .withObject("user",new User("xuebing",20))
                        .navigation(activity);
//                boolean isLogin = ServiceFactory.getInstance().getLoginService().isLogin();
//                if (isLogin) {
//                    ARouter.getInstance().build(IRoutePath.WECHAT).navigation(activity);
//                }else{
//                    ARouter.getInstance().build(IRoutePath.LOGIN).navigation(activity);
//                }
                finish();
            }
        },1000);
//        FloatingLogcatService.launch(this);
    }
}
