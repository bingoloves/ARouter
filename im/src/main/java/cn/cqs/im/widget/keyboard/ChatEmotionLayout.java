package cn.cqs.im.widget.keyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import cn.cqs.im.R;
import cn.cqs.im.widget.emotion.EmotionPager;
import cn.cqs.im.widget.emotion.data.HahaEmotion;


public class ChatEmotionLayout extends BaseSoftInputLayout {
    //切换键盘View
    private View mKeyBoardView;

    // emotionView,otherView容器
    private View container;
    private EmotionPager emotionView;
    //添加图片按钮
    private View mAddImage;
    //显示其他布局的View
    private View mOtherView;
    private View frame;
    private EditText editText;
    private ListView chatListView;
    private Button sendBtn;

    public ChatEmotionLayout(Context context) {
        super(context);
    }

    public ChatEmotionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChatEmotionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatEmotionLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.im_activity_chat, this, true);
        container = view.findViewById(R.id.container);
        frame = view.findViewById(R.id.frame);
        chatListView = view.findViewById(R.id.listView);
        editText = view.findViewById(R.id.et_emotion);
        sendBtn = view.findViewById(R.id.btn_send);
        setupKeyboardView(view);
        setupOtherView(view);
    }

    private void setupKeyboardView(View view) {
        mKeyBoardView = view.findViewById(R.id.ic_keyboard);
        mKeyBoardView.setOnClickListener(this);
        emotionView = view.findViewById(R.id.emotionPager);
        emotionView.bindData(HahaEmotion.DATA);
        add2ShowViewList(emotionView);
        add2MappingMap(mKeyBoardView, SHOW_EMOTION, emotionView);
    }

    private void setupOtherView(View view) {
        mAddImage = view.findViewById(R.id.btn_add_image);
        mAddImage.setOnClickListener(this);
        mOtherView = view.findViewById(R.id.otherView);
        add2ShowViewList(mOtherView);
        add2MappingMap(mAddImage, SHOW_OTHER, mOtherView);
    }

    public ListView getChatListView() {
        return chatListView;
    }

    public Button getSendBtn() {
        return sendBtn;
    }

    @Override
    protected View getContainer() {
        return container;
    }

    @Override
    protected View getFrame() {
        return frame;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    protected View getBtnKeyBoard() {
        return mKeyBoardView;
    }

}
