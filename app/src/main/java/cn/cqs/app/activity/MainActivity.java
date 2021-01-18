package cn.cqs.app.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.route.DefaultNavCallback;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.app.R;
import cn.cqs.components.photo.PreviewUtils;
import cn.cqs.components.photo.TakePhoto;

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
                .statusBarDarkFont(true)
                .init();
    }

    public void toLogin(View view){
//        setTransitionAnimation(TransitionEnum.RIGHT);
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
    public void openPhoto(View view){
        TakePhoto.openAlbum(activity, 9, new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                LogUtils.e(TextUtils.join("\n",paths));
                PreviewUtils.start(activity,paths,0);
            }
        });
    }
}
