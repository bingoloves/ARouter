package cn.cqs.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.ACache;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.login.R;
import cn.cqs.login.R2;
import cn.cqs.login.services.LoginServiceImpl;
import cn.cqs.service.ServiceFactory;
import cn.cqs.service.constants.Cache;
import cn.cqs.service.constants.IRoutePath;
import cn.cqs.service.im.BmobService;
import cn.cqs.service.im.LoginCallback;

/**
 * Created by Administrator on 2021/1/9 0009.
 */
@Route(path = IRoutePath.LOGIN)
public class LoginActivity extends BaseActivity{
    @BindView(R2.id.et_name)
    EditText nameEt;
    @BindView(R2.id.et_password)
    EditText passwordEt;
    @BindView(R2.id.iv_eyes)
    ImageView eyesIv;

    @OnClick({R2.id.btn_login,R2.id.btn_register,R2.id.iv_eyes})
    public void clickEvent(View view){
        int i = view.getId();
        if (i == R.id.btn_login) {
            login(view);
        } else if (i == R.id.btn_register) {
            register(view);
        } else if (i == R.id.iv_eyes) {
            setPasswordVisible(passwordEt);
            showPwd = !showPwd;
            eyesIv.setImageResource(showPwd ? R.mipmap.login_icon_show : R.mipmap.login_icon_hide);
        }
    }

    /**
     * 是否显示密码
     */
    private boolean showPwd = false;
    /**
     * 获取IM 提供的接口
     * 方式1: @Autowired 自动注入的形式
     * 方式2: 通过类名ARouter.getInstance().navigation(BmobService.class);
     * 方式3: 通过路径名称ARouter.getInstance().build("path").navigation();
     */
    @Autowired
    BmobService bmobService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }

    private void register(View view) {
        String userName = nameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            toast("请输入完整信息");
            return;
        }
        bmobService.register(userName, password, new LoginCallback() {
            @Override
            public void onSuccess(String userInfo) {
                toast("注册成功");
            }

            @Override
            public void onError(String error) {
                toast("注册失败："+ error);
            }
        });
//        ServiceFactory.getInstance().getBmobService().register(userName, password, new LoginCallback() {
//            @Override
//            public void onSuccess(String userInfo) {
//                toast("注册成功");
//            }
//
//            @Override
//            public void onError(String error) {
//                toast("注册失败："+ error);
//            }
//        });
    }

    private void login(View view) {
        String userName = nameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            toast("请输入完整信息");
            return;
        }
        bmobService.login(userName, password, new LoginCallback() {
            @Override
            public void onSuccess(String userInfo) {
                LoginServiceImpl.userInfo = userInfo;
                toast("登录成功");
                ACache.get(LoginActivity.this).put(Cache.IS_LOGIN,true);
                ARouter.getInstance().build(IRoutePath.WECHAT)
                        .withString("user", userInfo)
                        .navigation(LoginActivity.this);
                finish();
            }

            @Override
            public void onError(String error) {
                LogUtils.e("error = "+error);
                toast("登录失败");
            }
        });
        //普通接口方式，比较繁琐，需要手动对外暴露ServiceFactory.getInstance().setBmobService(xxx)
//        ServiceFactory.getInstance().getBmobService().login(userName, password, new LoginCallback() {
//            @Override
//            public void onSuccess(String userInfo) {
//                toast("登录成功");
//                ACache.get(LoginActivity.this).put(Cache.IS_LOGIN,true);
//                ARouter.getInstance().build(IRoutePath.WECHAT)
//                        .withString("name", "xuebing")
//                        .navigation(LoginActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onError(String error) {
//                LogUtils.e("error = "+error);
//                toast("登录失败："+ error);
//            }
//        });
    }

    /**
     * 切换密码的明文显示
     */
    private void setPasswordVisible(EditText editText) {
        if (EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == editText.getInputType()) {
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        editText.setSelection(editText.getText().toString().length());
    }
}
