package cn.bingoogolapple.acvp.recyclerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.acvp.recyclerview.mode.ItemMode;
import cn.bingoogolapple.acvp.recyclerview.widget.BGAEmptyView;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewAdapter;
import cn.bingoogolapple.acvp.recyclerview.widget.BGAViewHolder;
import cn.bingoogolapple.acvp.recyclerview.widget.ItemDivider;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemClickListener;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemLongClickListener;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_helloworld)
public class HelloworldActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = HelloworldActivity.class.getSimpleName();
    @BGAAView(R.id.ev_helloworld_root)
    private BGAEmptyView mRootEv;
    @BGAAView(R.id.srl_helloworld_container)
    private SwipeRefreshLayout mContainerSrl;
    @BGAAView(R.id.rv_helloworld_data)
    private RecyclerView mDataRv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<ItemMode> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        mContainerSrl.setOnRefreshListener(this);
        mContainerSrl.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataRv.setLayoutManager(mLinearLayoutManager);
        mDataRv.addItemDecoration(new ItemDivider(this, R.mipmap.list_divider));
        mItemModeAdapter = new ItemModeAdapter(this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDatas = ItemMode.getHelloworldDatas();
        mItemModeAdapter.setDatas(mDatas);
        mDataRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Log.i(TAG, "开始拖拽");
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.i(TAG, "停止滚动");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.i(TAG, "开始飞");
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "dx = " + dx + "   dy = " + dy);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_helloworld_add:
                add();
                break;
            case R.id.btn_helloworld_delete:
                delete();
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目" + mItemModeAdapter.getItemMode(position).attr1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目" + mItemModeAdapter.getItemMode(position).attr1, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add();
            }
        }, 2000);
    }

    private void add() {
        mDatas.add(new ItemMode("attr1 " + mDatas.size(), "attr2 " + mDatas.size()));
        mItemModeAdapter.notifyDataSetChanged();
        mContainerSrl.setRefreshing(false);
        mRootEv.showContentView();
    }

    private void delete() {
        if (mDatas.size() > 0) {
            mDatas.remove(mDatas.size() - 1);
            if (mDatas.size() == 0) {
                mRootEv.showEmptyView();
            }
            mItemModeAdapter.notifyDataSetChanged();
        }
    }

    private static class ItemModeViewHolder extends BGAViewHolder<ItemMode> {
        @BGAAView(R.id.tv_item_helloworld_attr1)
        private TextView mAttr1;
        @BGAAView(R.id.tv_item_helloworld_attr2)
        private TextView mAttr2;

        public ItemModeViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
            BGAA.injectViewField2Obj(this, itemView);
        }

        @Override
        public void fillData(ItemMode item, int position) {
            mAttr1.setText(item.attr1);
            mAttr2.setText(item.attr2);
        }

    }

    private static class ItemModeAdapter extends BGARecyclerViewAdapter<ItemMode, ItemModeViewHolder> {
        public ItemModeAdapter(HelloworldActivity helloworldActivity) {
            super(helloworldActivity, helloworldActivity);
        }

        @Override
        public ItemModeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helloworld, parent, false);
            return new ItemModeViewHolder(item, mOnItemClickListener, mOnItemLongClickListener);
        }
    }
}