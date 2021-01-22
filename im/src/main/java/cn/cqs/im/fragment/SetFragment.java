package cn.cqs.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.cqs.adapter.recyclerview.CommonAdapter;
import cn.cqs.adapter.recyclerview.base.ViewHolder;
import cn.cqs.android.base.BaseFragment;
import cn.cqs.android.utils.DensityUtils;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.activity.UserInfoActivity;
import cn.cqs.im.bean.MineItem;
import cn.cqs.im.bean.User;
import cn.cqs.im.model.UserModel;
import cn.cqs.service.constants.IRoutePath;

/**
 * 设置
 *
 * @author :smile
 * @project:SetFragment
 * @date :2016-01-25-18:23
 */
public class SetFragment extends BaseFragment {
    @BindView(R2.id.iv_head)
    ImageView headIv;
    @BindView(R2.id.tv_account)
    TextView accountName;
    @BindView(R2.id.tv_name)
    TextView userName;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private CommonAdapter<MineItem> commonAdapter;
    private List<MineItem> list = new ArrayList<>();

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(R.id.title_bar).init();
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        accountName.setText(TextUtils.isEmpty(username) ? "账号ID" :"账号ID:"+ username);
        initMineList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<MineItem>(getContext(),R.layout.layout_mine_item,list) {
            @Override
            protected void convert(ViewHolder holder, MineItem mineItem, int position) {
                holder.setImageResource(R.id.iv_mine_icon,mineItem.icon);
                holder.setText(R.id.tv_name,mineItem.name);
                holder.setVisible(R.id.line,mineItem.hasLine);
                holder.itemView.setOnClickListener(mineItem.clickListener);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.topMargin = position == 1? DensityUtils.dp2px(getContext(),12):0;
            }
        };
        recyclerView.setAdapter(commonAdapter);
    }
    private void initMineList(){
        list.add(new MineItem(R.mipmap.ic_mine_feedback,"意见反馈","", false,null));
        list.add(new MineItem(R.mipmap.ic_mine_about, "个人资料", "", true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),UserInfoActivity.class);
                intent.putExtra("u", BmobUser.getCurrentUser(User.class));
                startActivity(intent);
            }
        }));
        list.add(new MineItem(R.mipmap.ic_mine_logout, "退出登录", "", false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel.getInstance().logout();
                // TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                BmobIM.getInstance().disConnect();
                ARouter.getInstance().build(IRoutePath.LOGIN).navigation(getContext());
            }
        }));
    }
}
