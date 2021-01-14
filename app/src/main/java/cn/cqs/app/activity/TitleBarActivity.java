package cn.cqs.app.activity;

import android.os.Bundle;
import com.alibaba.android.arouter.facade.annotation.Route;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.app.R;

@Route(path = "/main/titlebar")
public class TitleBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlebar);
    }
}
