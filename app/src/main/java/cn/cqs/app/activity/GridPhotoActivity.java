package cn.cqs.app.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.cqs.android.base.BaseActivity;
import cn.cqs.app.R;
import cn.cqs.components.titlebar.TitleBar;

/**
 * Created by bingo on 2021/1/22.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/22
 */
public class GridPhotoActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_photo);

    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this)
                .titleBar(R.id.title_bar)
                .statusBarDarkFont(true)
                .init();
    }
}
