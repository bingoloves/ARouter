package cn.cqs.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.route.DefaultNavCallback;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.app.R;

@Route(path = "/main/main")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void toLogin(View view){
        ARouter.getInstance().build("/login/login").navigation();
    }
    public void toIm(View view){
        ARouter.getInstance().build("/im/main")
                .withString("name", "xuebing")
//                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .greenChannel()//跳转时跳过所有的拦截器
                .navigation(this, new DefaultNavCallback());
    }
    public void toFragment(View view){
        ARouter.getInstance().build("/main/other").navigation();
    }
}
