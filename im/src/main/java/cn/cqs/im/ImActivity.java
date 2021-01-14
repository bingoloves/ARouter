package cn.cqs.im;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.im.widget.keyboard.BaseSoftInputLayout;
import cn.cqs.im.widget.keyboard.ChatEmotionLayout;
import cn.cqs.service.ServiceFactory;
public class ImActivity extends BaseActivity {

    @Autowired
    String name;

    TextView loginStateTv;
    ChatEmotionLayout chatEmotionLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);
        loginStateTv = findViewById(R.id.tv_im_login_state);
        initChatView();
        initLoginState();
        LogUtils.d("name = " + name);
    }
    private ArrayAdapter<CharSequence> listAdapter;
    private void initChatView() {
        chatEmotionLayout = findViewById(R.id.chatView);
        final ListView listView = chatEmotionLayout.getChatListView();
        Button sendBtn = chatEmotionLayout.getSendBtn();
        final EditText editText = chatEmotionLayout.getEditText();
        List<CharSequence> stringList = new ArrayList<>();
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,stringList);
        listView.setAdapter(listAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏 软键盘|表情
                chatEmotionLayout.hideKeyBoardView();
                return false;
            }
        });
//        chatEmotionLayout.setKeyboardOpenListener(new BaseSoftInputLayout.KeyboardOpenListener() {
//            @Override
//            public void onKeyboard(boolean isOpen) {
//                LogUtils.e("isOpen = "+isOpen);
//                listView.setSelection(ListView.FOCUS_DOWN); //刷新到底部
//            }
//        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText())) {
                    LogUtils.e(editText.getText().toString());
                    listAdapter.add(editText.getText());
//                    listView.setSelection(ListView.FOCUS_DOWN); //刷新到底部
//                    listView.setSelection(listView.getBottom());
//                    listView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            listView.setSelection(listView.getCount() - 1);
//                        }
//                    });
                    editText.setText("");
                }
            }
        });
    }

    private void initLoginState() {
        boolean isLogin = ServiceFactory.getInstance().getLoginService().isLogin();
        String loginTips = isLogin?"已登录":"未登录";
        String loginText = "登录状态：" + loginTips;
        Toast.makeText(this, loginTips, Toast.LENGTH_SHORT).show();
        loginStateTv.setText(loginText);
    }

    public void moveLogin(View view){
        ARouter.getInstance().build("/login/login").navigation();
    }
}
