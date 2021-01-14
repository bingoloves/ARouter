package cn.cqs.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.cqs.adapter.recyclerview.CommonAdapter;
import cn.cqs.adapter.recyclerview.base.ViewHolder;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.android.utils.log.LogUtils;
import cn.cqs.android.views.titlebar.OnClickTitleBarImpl;
import cn.cqs.android.views.titlebar.TitleBar;
import cn.cqs.im.R;
import cn.cqs.im.R2;
import cn.cqs.im.bean.User;
import cn.cqs.im.model.BaseModel;
import cn.cqs.im.model.UserModel;

/**
 * 搜索用户
 *
 * @author :smile
 * @project:SearchUserActivity
 * @date :2016-01-25-18:23
 */
public class SearchUserActivity extends BaseActivity {

    @BindView(R2.id.et_find_name)
    EditText et_find_name;
    @BindView(R2.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    CommonAdapter adapter;
    @BindView(R2.id.btn_search)
    Button btn_search;
    private List<User> newUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initView();
    }

    protected void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(mTitleBar).init();
        mTitleBar.setOnTitleBarListener(new OnClickTitleBarImpl(){
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        adapter = new CommonAdapter<User>(this,R.layout.item_search_user,newUsers) {
            @Override
            protected void convert(ViewHolder holder, final User user, int position) {
                ImageView imageView = holder.getView(R.id.avatar);
                Glide.with(SearchUserActivity.this).asBitmap().load(user.getAvatar()).apply(new RequestOptions().error(R.mipmap.head)).into(imageView);
                holder.setText(R.id.name, user.getUsername());
                holder.setOnClickListener(R.id.btn_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看个人详情
                        Intent intent = new Intent(SearchUserActivity.this,UserInfoActivity.class);
                        intent.putExtra("u", user);
                        startActivity(intent);
                    }
                });

//                holder.itemView.setOnClickListener(v -> {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("u", user);
//                    Intent intent = new Intent(mActivity,UserInfoActivity.class);
//                    startActivity(intent);
//                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
    }



    @OnClick(R2.id.btn_search)
    public void onSearchClick(View view) {
        refreshLayout.setRefreshing(false);
        query();
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请填写用户名");
            refreshLayout.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            refreshLayout.setRefreshing(false);
                            adapter.update(list);
                        } else {
                            refreshLayout.setRefreshing(false);
                            adapter.clear();
                            LogUtils.e(e.getMessage());
                        }
                    }
                }
        );
    }

}
