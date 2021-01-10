package cn.cqs.im;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.service.ServiceFactory;

@Route(path = "/im/main",name = "即时通讯的主页")
public class ImActivity extends BaseActivity {

    @Autowired
    String name;

    TextView loginStateTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);
        loginStateTv = findViewById(R.id.tv_login_state);
        initLoginState();
        LogUtils.d("name = " + name);
    }

    private void initLoginState() {
        boolean isLogin = ServiceFactory.getInstance().getLoginService().isLogin();
        String loginTips = isLogin?"已登录":"未登录";
        String loginText = "登录状态：" + loginTips;
        Toast.makeText(this, loginTips, Toast.LENGTH_SHORT).show();
        loginStateTv.setText(loginText);
    }

    public void moveLogin(View view){
        ARouter.getInstance().build("/login/login").navigation();
    }
}
