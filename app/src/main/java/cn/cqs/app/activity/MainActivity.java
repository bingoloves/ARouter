package cn.cqs.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.app.R;
import cn.cqs.app.bean.SimpleItem;
import cn.cqs.app.bean.User;
import cn.cqs.components.adapter.QuickAdapter;
import cn.cqs.service.constants.IRoutePath;
import cn.cqs.service.login.LoginService;

@Route(path = "/main/main")
public class MainActivity extends BaseActivity {
    @Autowired
    User user;
    @Autowired
    LoginService loginService;

    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    private QuickAdapter<SimpleItem> adapter;
    private List<SimpleItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        adapter = new QuickAdapter<SimpleItem>(R.layout.layout_mine_item,list){
            @Override
            protected void convert(final com.chad.library.adapter.base.BaseViewHolder helper, final SimpleItem item) {
                super.convert(helper, item);
                    helper.setText(R.id.tv_name,item.name);
                    helper.addOnClickListener(R.id.item_root);
            }
        };
        adapter.openLoadAnimation();
        adapter.attachRecyclerView(recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SimpleItem simpleItem = list.get(position);
                if (simpleItem.clazz != null){
                    Intent intent = new Intent(activity,simpleItem.clazz);
                    startActivity(intent);
                } else if (!TextUtils.isEmpty(simpleItem.routePath)){
                    ARouter.getInstance().build(simpleItem.routePath).navigation(activity);
                } else if (simpleItem.onClickListener != null){
                    simpleItem.onClickListener.onClick(view);
                }
            }
        });
    }
    //ARouter跳转示例
//      ARouter.getInstance().build("/im/wechat")
//                .withString("name", "xuebing")
//                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                .greenChannel()//跳转时跳过所有的拦截器
//                .navigation(this, new DefaultNavCallback());
    public static final String ACTION_REBOOT =  "android.intent.action.REBOOT";
    public static final String ACTION_REQUEST_SHUTDOWN = "android.intent.action.ACTION_REQUEST_SHUTDOWN";
    private void initData() {
        //setTransitionAnimation(TransitionEnum.RIGHT);
        list.add(new SimpleItem("登录", "模块化登录", IRoutePath.LOGIN));
        list.add(new SimpleItem("IM", "模块化即时通许", IRoutePath.WECHAT));
        list.add(new SimpleItem("登录模块的Fragment", "模块化登录模块的Fragment碎片","/main/other"));
        list.add(new SimpleItem("TitleBar", "自定义TitleBar","/main/titlebar"));
        list.add(new SimpleItem("BRVAH", "基于BRVAH适配器的简单使用",QuickAdapterActivity.class));
        list.add(new SimpleItem("九宫格图片", "图片选择",GridPhotoActivity.class));
//        list.add(new SimpleItem("关机", "关机意图", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
////                intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
//                //其中false换成true,会弹出是否关机的确认窗口
//                sendBroadcast(intent);
//            }
//        }));
//        list.add(new SimpleItem("重启", "重启广播", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //需要系统App权限才行
//                Intent intent2 = new Intent(Intent.ACTION_REBOOT);
//                intent2.putExtra("nowait", 1);
//                intent2.putExtra("interval", 1);
//                intent2.putExtra("window", 0);
//                sendBroadcast(intent2);
//            }
//        }));
    }


    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this)
                .titleBar(R.id.title_bar)
                .statusBarDarkFont(true)
                .init();
    }
}
