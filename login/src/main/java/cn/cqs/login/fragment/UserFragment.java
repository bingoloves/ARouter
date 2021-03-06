package cn.cqs.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cqs.login.LoginApp;
import cn.cqs.login.R;
import cn.cqs.service.ServiceFactory;

public class UserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView tvName = view.findViewById(R.id.tv_user_name);
        boolean isLogin = ServiceFactory.getInstance().getLoginService().isLogin();
        tvName.setText(isLogin ? "用户未登录" : "登录用户：" + ServiceFactory.getInstance().getLoginService().getUser());
        return view;
    }
}
