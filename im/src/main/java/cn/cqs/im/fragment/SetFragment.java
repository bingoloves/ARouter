package cn.cqs.im.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.cqs.android.base.BaseFragment;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.activity.UserInfoActivity;
import cn.cqs.im.bean.User;
import cn.cqs.im.model.UserModel;

/**
 * 设置
 *
 * @author :smile
 * @project:SetFragment
 * @date :2016-01-25-18:23
 */
public class SetFragment extends BaseFragment {

    @BindView(R2.id.tv_set_name)
    TextView tv_set_name;

    @BindView(R2.id.layout_info)
    RelativeLayout layout_info;


    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(R.id.title_bar).init();
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        tv_set_name.setText(TextUtils.isEmpty(username) ? "" : username);
    }


    @OnClick(R2.id.layout_info)
    public void onInfoClick(View view) {
        toast("123");
        Intent intent = new Intent(getContext(),UserInfoActivity.class);
        intent.putExtra("u", BmobUser.getCurrentUser(User.class));
        startActivity(intent);
    }
    private void startActivity(Class<? extends Activity> target, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(getContext(), target);
        if (bundle != null) intent.putExtra(getContext().getPackageName(), bundle);
        startActivity(intent);
    }
    @OnClick(R2.id.btn_logout)
    public void onLogoutClick(View view) {
//        UserModel.getInstance().logout();
//        //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
//        BmobIM.getInstance().disConnect();
//        getActivity().finish();
    }
}
