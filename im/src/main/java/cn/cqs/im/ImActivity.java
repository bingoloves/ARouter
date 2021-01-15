package cn.cqs.im;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.service.ServiceFactory;
public class ImActivity extends BaseActivity {

    @Autowired
    String name;

    TextView loginStateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);
        loginStateTv = findViewById(R.id.tv_im_login_state);
        initChatView();
        initLoginState();
        LogUtils.d("name = " + name);
    }
    private void initChatView() {

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
