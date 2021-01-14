package cn.cqs.im.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.adapter.BaseViewPagerAdapter;
import cn.cqs.im.bean.TabEntity;
import cn.cqs.im.bean.User;
import cn.cqs.im.db.NewFriendManager;
import cn.cqs.im.event.RefreshEvent;
import cn.cqs.im.fragment.ContactFragment;
import cn.cqs.im.fragment.ConversationFragment;
import cn.cqs.im.fragment.SetFragment;
import cn.cqs.service.ServiceFactory;
import cn.cqs.service.constants.IRoutePath;

/**
 * @author :smile
 * @project:MainActivity
 * @date :2016-01-15-18:23
 */
@Route(path = IRoutePath.WECHAT,name = "即时通讯的主页")
public class WeChatActivity extends BaseActivity {

    @BindView(R2.id.viewPager)
    ViewPager mViewPager;
    @BindView(R2.id.tabLayout)
    CommonTabLayout bottomTab;

    @Autowired
    String name;

    private String[] mTitles = {"会话", "联系人", "设置"};
    private int[] mIconUnselectIds = {R.drawable.ic_message, R.drawable.ic_contact, R.drawable.ic_setting};
    private int[] mIconSelectIds = {R.drawable.ic_message_select, R.drawable.ic_contact_select, R.drawable.ic_setting_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        initView();
        initLoginState();
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    toast(status.getMsg());
                    LogUtils.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        //IMMLeaks.fixFocusedViewLeak(getApplication());
        checkStoragePermissions(0);
    }

    private void initLoginState() {
        boolean isLogin = ServiceFactory.getInstance().getLoginService().isLogin();
        String loginTips = isLogin?"已登录":"未登录";
        String loginText = "登录状态：" + loginTips;
        Toast.makeText(this, loginTips, Toast.LENGTH_SHORT).show();
        LogUtils.e(loginText);
    }
    /**
     * TODO 1、兼容android6.0运行时权限
     * 检查权限
     *
     * @param requestCode
     */
    public void checkStoragePermissions(int requestCode) {
        List<String> permissions = new ArrayList<>();
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            String[] missions = new String[]{};
            ActivityCompat.requestPermissions(this, permissions.toArray(missions), requestCode);
        }
    }


    /**
     * TODO 1、兼容android6.0运行时权限
     * <p>
     * 权限授权结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void initView() {
        if (mTabEntities != null){
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            }
        }
        if (mFragments != null){
            mFragments.add(new ConversationFragment());
            mFragments.add(new ContactFragment());
            mFragments.add(new SetFragment());
        }
        mViewPager.setAdapter(new BaseViewPagerAdapter(getSupportFragmentManager(),mFragments,mTitles));
        bottomTab.setTabData(mTabEntities);
        bottomTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position,false);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        bottomTab.showMsg(0, count);//设置消息未读数，第一参数是索引,第二个参数是未读数数值
        //TODO 好友管理：是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            bottomTab.showDot(1);
        }  else {
            bottomTab.showMsg(1,0);
        }
    }
}
