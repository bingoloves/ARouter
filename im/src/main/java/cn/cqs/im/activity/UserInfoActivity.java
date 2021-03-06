package cn.cqs.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;

import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.components.titlebar.OnClickTitleBarImpl;
import cn.cqs.components.titlebar.TitleBar;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.bean.AddFriendMessage;
import cn.cqs.im.bean.User;

/**
 * 用户资料
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R2.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R2.id.iv_avator)
    ImageView iv_avator;
    @BindView(R2.id.tv_name)
    TextView tv_name;
    @BindView(R2.id.btn_add_friend)
    Button btn_add_friend;
    @BindView(R2.id.btn_chat)
    Button btn_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }
    //用户
    User user;
    //用户信息
    BmobIMUserInfo info;

    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(mTitleBar).init();
        mTitleBar.setOnTitleBarListener(new OnClickTitleBarImpl(){
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        //String currentUid = UserModel.getInstance().getCurrentUser().getObjectId();
        //用户
        user = (User) getIntent().getSerializableExtra("u");
        if (user != null && user.getObjectId().equals(getCurrentUid())) {//用户为登录用户
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
        } else {//用户为非登录用户
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        //加载头像
        Glide.with(UserInfoActivity.this).asBitmap().load(user.getAvatar()).apply(new RequestOptions().error(R.mipmap.head)).into(iv_avator);
        //显示名称
        tv_name.setText(user.getUsername());
    }
    public String getCurrentUid(){
        return BmobUser.getCurrentUser(User.class).getObjectId();
    }
    @OnClick({R2.id.btn_add_friend, R2.id.btn_chat})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_add_friend){
            sendAddFriendMessage();
        } else if (view.getId() == R.id.btn_chat){
            chat();
        }
    }


    /**
     * 发送添加好友的请求
     */
    //TODO 好友管理：9.7、发送添加好友请求
    private void sendAddFriendMessage() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            toast("尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个暂态会话入口，发送好友请求
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，发送好友请求
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        AddFriendMessage msg = new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 与陌生人聊天
     */
    private void chat() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            toast("尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个常态会话入口，陌生人聊天
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("c",conversationEntrance);
        startActivity(intent);
    }
}