package cn.cqs.app.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.enums.TransitionEnum;
import cn.cqs.android.route.DefaultNavCallback;
import cn.cqs.app.R;

@Route(path = "/main/main")
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA},1001);
        }
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this)
                .titleBar(R.id.title_bar)
                .init();
    }

    public void toLogin(View view){
        setTransitionAnimation(TransitionEnum.RIGHT);
        ARouter.getInstance().build("/login/login")
                .navigation(this);
//        finish();
    }
    public void toIm(View view){
        ARouter.getInstance().build("/im/wechat")
                .withString("name", "xuebing")
//                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .greenChannel()//跳转时跳过所有的拦截器
                .navigation(this, new DefaultNavCallback());
    }
    public void toFragment(View view){
        ARouter.getInstance().build("/main/other").navigation(this);
    }
    public void toTitleBar(View view){
        ARouter.getInstance().build("/main/titlebar").navigation(this);
    }

}
