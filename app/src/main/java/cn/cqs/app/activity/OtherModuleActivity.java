package cn.cqs.app.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.base.SuportToolbarActivity;
import cn.cqs.app.R;
import cn.cqs.service.ServiceFactory;

/**
 * Created by Administrator on 2021/1/9 0009.
 */
@Route(path = "/main/other", name = "Login模块中的开放出来的Fragment")
public class OtherModuleActivity extends SuportToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_module);
        setTitleText("LoginFragment");
        ServiceFactory.getInstance().getLoginService().newUserFragment(this, R.id.container, getSupportFragmentManager(), null, "");
    }
    @Override
    protected void initImmersionbar() {

    }
}
