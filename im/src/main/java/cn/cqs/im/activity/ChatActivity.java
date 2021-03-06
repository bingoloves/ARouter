package cn.cqs.im.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gyf.immersionbar.ImmersionBar;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.components.titlebar.OnClickTitleBarImpl;
import cn.cqs.components.titlebar.TitleBar;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.adapter.ChatAdapter;
import cn.cqs.im.adapter.OnRecyclerViewListener;
import cn.cqs.im.event.RefreshEvent;
import cn.cqs.im.utils.SoftKeyBoardListener;
import cn.cqs.im.widget.emotion.EmotionKeyboard;
import cn.cqs.im.widget.keyboard.viewpager.EmotionAdapter;
import cn.cqs.im.widget.keyboard.viewpager.GlobalOnItemClickManager;

/**
 * 聊天界面
 *
 * @author :smile
 * @project:ChatActivity
 * @date :2016-01-25-18:23
 */
public class ChatActivity extends BaseActivity implements MessageListHandler {
    //根视图
    @BindView(R2.id.chat_root_layout)
    LinearLayout chatRootView;
    //标题组件
    @BindView(R2.id.title_bar)
    TitleBar mTitleBar;
    //中间容器视图
    @BindView(R2.id.container)
    FrameLayout container;
    //刷新组件
    @BindView(R2.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R2.id.rc_view)
    RecyclerView rc_view;

    //底部输入组件
    //语音按钮
    @BindView(R2.id.btn_chat_voice)
    Button chatVoiceBtn;
    //输入框组件
    @BindView(R2.id.et_emotion)
    EditText emotionEditText;
    //按住说话按钮
    @BindView(R2.id.btn_chat_speak)
    Button chatSpeakBtn;
    //表情按钮
    @BindView(R2.id.btn_chat_emoji)
    Button chatEmojiBtn;
    //更多按钮
    @BindView(R2.id.btn_chat_more)
    Button chatMoreBtn;
    //发送按钮
    @BindView(R2.id.btn_chat_send)
    Button chatSendBtn;

    //底部更多视图
    @BindView(R2.id.emotion_layout)
    RelativeLayout layoutEmotion;
    @BindView(R2.id.more_layout)
    LinearLayout layoutMore;
    //更多功能

    // 语音有关
    @BindView(R2.id.layout_record)
    RelativeLayout layout_record;
    @BindView(R2.id.tv_voice_tips)
    TextView tv_voice_tips;
    @BindView(R2.id.iv_record)
    ImageView iv_record;
    private Drawable[] drawable_Anims;// 话筒动画
    BmobRecordManager recordManager;

    ChatAdapter adapter;
    protected LinearLayoutManager layoutManager;
    BmobIMConversation mConversationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }

    protected void initView() {
        BmobIMConversation conversationEntrance = (BmobIMConversation) getIntent().getSerializableExtra("c");
        //TODO 消息：5.1、根据会话入口获取消息管理，聊天页面
        mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        ImmersionBar.with(this).statusBarDarkFont(true).keyboardEnable(true).titleBar(mTitleBar).init();
        mTitleBar.setTitle(mConversationManager.getConversationTitle());
        mTitleBar.setOnTitleBarListener(new OnClickTitleBarImpl(){
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        initSwipeLayout();
        initVoiceView();
        initBottomView();
        verifyAudioPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==GET_RECODE_AUDIO){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "授予录音权限成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "授予录音权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * 申请录音权限*/
    public static void verifyAudioPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_AUDIO,
                    GET_RECODE_AUDIO);
        }
    }

    private void initSwipeLayout() {
        sw_refresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
//        container.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                hideSoftInputView();
//                return false;
//            }
//        });
        adapter = new ChatAdapter(this, mConversationManager);
        rc_view.setAdapter(adapter);
        chatRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                chatRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.i("" + position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                //TODO 消息：5.3、删除指定聊天消息
                mConversationManager.deleteMessage(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }

    private void initBottomView() {
        //绑定表情键盘
        bindToEmotionKeyboard();
        //监听软键盘弹出，并获取软键盘高度
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘弹起事件 并给View设置高度
                smoothToBottom(0);
                LogUtils.e("软键盘弹起");
            }

            @Override
            public void keyBoardHide(int height) {
                LogUtils.e("软键盘关闭");
            }
        });

    }
    private EmotionKeyboard emotionKeyboard;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private void bindToEmotionKeyboard() {
        emotionKeyboard = EmotionKeyboard.with(this)
                .setExtendView(layoutMore)
                .setEmotionView(layoutEmotion)
                .bindToContent(container)
                .bindToEditText(emotionEditText)
                .bindToExtendButton(chatMoreBtn)
                .bindToEmotionButton(chatEmojiBtn)
                .build();
        setUpEmotionViewPager();
        setUpExtendView();
        emotionEditText.addTextChangedListener(new ButtonBtnWatcher());//动态监听EditText
    }
    /* 设置表情布局下的视图 */
    private void setUpEmotionViewPager() {
        int fragmentNum;
		/*获取ems文件夹有多少个表情  减1 是因为有个删除键
           每页20个表情  总共有length个表情
           先判断能不能整除  判断是否有不满一页的表情
		 */
        int emsTotalNum = getSizeOfAssetsCertainFolder("ems") - 1;//表情的数量(除去删除按钮)
        if(emsTotalNum % emsNumOfEveryFragment == 0){
            fragmentNum = emsTotalNum / emsNumOfEveryFragment;
        } else {
            fragmentNum = (emsTotalNum / emsNumOfEveryFragment) + 1;
        }
        EmotionAdapter mViewPagerAdapter = new EmotionAdapter(getSupportFragmentManager(), fragmentNum);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_emotion);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);

        GlobalOnItemClickManager globalOnItemClickListener = GlobalOnItemClickManager.getInstance();
        globalOnItemClickListener.attachToEditText(emotionEditText);

		/* 设置表情下的提示点 */
        setUpTipPoints(fragmentNum, mViewPager);
    }
    /* 设置扩展布局下的视图 */
    private void setUpExtendView() {
        //语言点击按钮
        chatVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //发送消息的按钮
        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    toast("尚未连接IM服务器");
                    return;
                }
                sendMessage();
            }
        });
    }

    /**
     @param
     num   提示点的数量
     */
    private void setUpTipPoints(int num, ViewPager mViewPager) {
        rgTipPoints = (RadioGroup) findViewById(R.id.rg_reply_layout);
        for(int i=0;i<num;i++){
            rbPoint = new RadioButton(this);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(30, 30);
            lp.setMargins(10, 0, 10, 0);
            rbPoint.setLayoutParams(lp);
            rbPoint.setId(i);//为每个RadioButton设置标记
            rbPoint.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));//设置button为@null
            rbPoint.setBackgroundResource(R.drawable.emotion_tip_points_selector);
            rbPoint.setClickable(false);
            if(i == 0){ // 第一个点默认为选中，与其他点显示颜色不同
                rbPoint.setChecked(true);
            } else {
                rbPoint.setChecked(false);
            }
            rgTipPoints.addView(rbPoint);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rgTipPoints.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!emotionKeyboard.interceptBackPress()){
            super.onBackPressed();
        }
    }

    /* 获取assets下某个指定文件夹下的文件数量 */
    private int getSizeOfAssetsCertainFolder(String folderName){
        int size = 0;
        try {
            size = getAssets().list(folderName).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /* EditText输入框动态监听 */
    class ButtonBtnWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            scrollToBottom();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!TextUtils.isEmpty(emotionEditText.getText().toString())){ //有文本内容，按钮为可点击状态
                chatSendBtn.setVisibility(View.VISIBLE);
                chatMoreBtn.setVisibility(View.GONE);
                chatSendBtn.setBackgroundResource(R.drawable.shape_button_reply_button_clickable);
                chatSendBtn.setTextColor(Color.parseColor("#f7fff6"));
            } else { // 无文本内容，按钮为不可点击状态
                chatSendBtn.setVisibility(View.GONE);
                chatMoreBtn.setVisibility(View.VISIBLE);
                chatSendBtn.setBackgroundResource(R.drawable.shape_button_reply_button_unclickable);
                chatSendBtn.setTextColor(Color.parseColor("#f5f5f5"));
            }
            LogUtils.e("输入框："+s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    /*滚动到列表底部*/
    private void smoothToBottom(int delay){
        rc_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                rc_view.smoothScrollToPosition(adapter.getCount());
            }
        },delay);
    }

    /**
     * 初始化语音布局
     *
     * @param
     * @return void
     */
    private void initVoiceView() {
        chatSpeakBtn.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    /**
     * 初始化语音动画资源
     *
     * @param
     * @return void
     * @Title: initVoiceAnimRes
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6)};
    }

    private void initRecordManager() {
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {


            @Override
            public void onVolumeChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                LogUtils.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    chatSpeakBtn.setPressed(false);
                    chatSpeakBtn.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            chatSpeakBtn.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    //申请录音权限
    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!checkSdCard()) {
                        toast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(mConversationManager.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            LogUtils.i("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(mConversationManager.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }
    /**
     * 检查是否安装了SD 卡
     * @return
     */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)){
            return true;
        } else {
            return false;
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     *
     * @return void
     * @Title: showShortToast
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }


//    @OnClick(R2.id.tv_picture)
//    public void onPictureClick(View view) {
//        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
//            toast("尚未连接IM服务器");
//            return;
//        }
//        sendLocalImageMessage();
//    }

//    @OnClick(R2.id.tv_camera)
//    public void onCameraClick(View view) {
//        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
//            toast("尚未连接IM服务器");
//            return;
//        }
//        sendRemoteImageMessage();
//    }

//    @OnClick(R2.id.tv_location)
//    public void onLocationClick(View view) {
//        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
//            toast("尚未连接IM服务器");
//            return;
//        }
//        sendLocationMessage();
//    }

    /**
     * 显示软键盘
     */
//    public void showSoftInputView() {
//        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//            if (getCurrentFocus() != null)
//                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                        .showSoftInput(emotionEditText, 0);
//        }
//    }
    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = emotionEditText.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("请输入内容");
            return;
        }
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可随意设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        msg.setExtra("OK");
        mConversationManager.sendMessage(msg, listener);
    }

    /**
     * 发送本地图片文件
     */
    public void sendLocalImageMessage() {
        //TODO 发送消息：6.2、发送本地图片消息
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
//        BmobIMImageMessage image = new BmobIMImageMessage("/storage/emulated/0/crop_file.jpg");
//        mConversationManager.sendMessage(image, listener);
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage() {
        //TODO 发送消息：6.3、发送远程图片消息
//        BmobIMImageMessage image = new BmobIMImageMessage();
//        image.setRemoteUrl("https://avatars3.githubusercontent.com/u/11643472?v=4&u=df609c8370b3ef7a567457eafd113b3ba6ba3bb6&s=400");
//        mConversationManager.sendMessage(image, listener);
    }


    /**
     * 发送本地音频文件
     */
    private void sendLocalAudioMessage() {
        //TODO 发送消息：6.4、发送本地音频文件消息
//        BmobIMAudioMessage audio = new BmobIMAudioMessage("此处替换为你本地的音频文件地址");
//        mConversationManager.sendMessage(audio, listener);
    }


    /**
     * 发送远程音频文件
     */
    private void sendRemoteAudioMessage(){
        //TODO 发送消息：6.5、发送本地音频文件消息
//        BmobIMAudioMessage audio = new BmobIMAudioMessage();
//        audio.setRemoteUrl("此处替换为你远程的音频文件地址");
//        mConversationManager.sendMessage(audio, listener);
    }

    /**
     * 发送本地视频文件
     */
    private void sendLocalVideoMessage() {
//        BmobIMVideoMessage video = new BmobIMVideoMessage("此处替换为你本地的视频文件地址");
//        //TODO 发送消息：6.6、发送本地视频文件消息
//        mConversationManager.sendMessage(video, listener);
    }

    /**
     * 发送远程视频文件
     */
    private void sendRemoteVideoMessage(){
        //TODO 发送消息：6.7、发送本地音频文件消息
//        BmobIMAudioMessage audio = new BmobIMAudioMessage();
//        audio.setRemoteUrl("此处替换为你远程的音频文件地址");
//        mConversationManager.sendMessage(audio, listener);
    }

    /**
     * 发送本地文件
     */
    public void sendLocalFileMessage() {
        //TODO 发送消息：6.8、发送本地文件消息
//        BmobIMFileMessage file = new BmobIMFileMessage("此处替换为你本地的文件地址");
//        mConversationManager.sendMessage(file, listener);
    }
    /**
     * 发送远程文件
     */
    public void sendRemoteFileMessage() {
        //TODO 发送消息：6.9、发送远程文件消息
//        BmobIMFileMessage file = new BmobIMFileMessage();
//        file.setRemoteUrl("此处替换为你远程的文件地址");
//        mConversationManager.sendMessage(file, listener);
    }
    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        //TODO 发送消息：6.5、发送本地音频文件消息
//        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
//        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
//        Map<String, Object> map = new HashMap<>();
//        map.put("from", "优酷");
//        //TODO 自定义消息：7.1、给消息设置额外信息
//        audio.setExtraMap(map);
//        //设置语音文件时长：可选
////        audio.setDuration(length);
//        mConversationManager.sendMessage(audio, listener);
    }


    /**
     * 发送地理位置消息
     */
    public void sendLocationMessage() {
        //TODO 发送消息：6.10、发送位置消息
        //测试数据，真实数据需要从地图SDK中获取
//        BmobIMLocationMessage location = new BmobIMLocationMessage("广州番禺区", 23.5, 112.0);
//        Map<String, Object> map = new HashMap<>();
//        map.put("from", "百度地图");
//        location.setExtraMap(map);
//        mConversationManager.sendMessage(location, listener);
    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            LogUtils.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            emotionEditText.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            emotionEditText.setText("");
            //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
            scrollToBottom();
            if (e != null) {
                toast(e.getMessage());
            }
        }
    };

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversationManager.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                sw_refresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }



    //TODO 消息接收：8.2、单个页面的自定义接收器
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        LogUtils.i("聊天页面接收到消息：" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (mConversationManager != null && event != null && mConversationManager.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            LogUtils.i("不是与当前聊天对象的消息");
        }
    }


    @Override
    public void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    public void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null) {
            mConversationManager.updateLocalCache();
        }
        hideSoftInputView();
        EventBus.getDefault().post(new RefreshEvent());
        super.onDestroy();
    }
}
