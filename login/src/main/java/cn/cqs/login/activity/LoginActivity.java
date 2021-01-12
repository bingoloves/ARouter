package cn.cqs.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.android.arouter.facade.annotation.Route;
import butterknife.BindView;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.enums.TransitionEnum;
import cn.cqs.android.utils.PendingTransitionUtils;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.LoginApp;
import cn.cqs.login.R;
import cn.cqs.login.R2;
import cn.cqs.login.bean.UserInfo;

/**
 * Created by Administrator on 2021/1/9 0009.
 */
@Route(path = "/login/login")
public class LoginActivity extends BaseActivity{

    @BindView(R2.id.tv_login_state)
    TextView loginStateTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        loginStateTv = findViewById(R.id.tv_login_state1);
        updateLoginState();
    }
    public void login(View view){
        LoginApp.userInfo = new UserInfo("10086", "Admin");
        updateLoginState();
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    public void exit(View view){
        LoginApp.userInfo = null;
        updateLoginState();
        Toast.makeText(this, "已退出", Toast.LENGTH_SHORT).show();
    }
    public void back(View view){
        finish();
        Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show();
    }

    private void updateLoginState() {
        loginStateTv.setText("这里是登录界面：" + (LoginApp.userInfo == null ? "未登录" : LoginApp.userInfo.getUserName()));
    }
}
