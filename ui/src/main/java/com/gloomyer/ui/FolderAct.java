package com.gloomyer.ui;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gloomyer.lib.Manager;
import com.gloomyer.lib.bean.ResultInfo;
import com.gloomyer.lib.interfaces.OnScreenListener;

import java.util.List;

/**
 * 相册选择目录
 */

public class FolderAct extends AppCompatActivity {

    private static final String TAG = "FolderAct";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<ResultInfo> infos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gloomy_act);
        UIManager.getInstance().activities.add(this);

        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setNavigationIcon(R.drawable.gloomy_save);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mToolbar.setTitle("相册选择");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 14;
                outRect.right = 14;
                outRect.left = 14;
                outRect.top = 14;
            }
        });


        Manager.getInstance().startScreen(this, new OnScreenListener() {
            @Override
            public void start() {
                Log.i(TAG, "start");
            }

            @Override
            public void finish(List<ResultInfo> results, boolean isCache) {
                infos = results;
                Log.i(TAG, "size:" + infos.size() + "," + results.toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gloomy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.gloomy_save) {
            UIManager.getInstance().save(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Utils.refresh(mToolbar);
        super.onResume();
    }


    private class Holder extends RecyclerView.ViewHolder {

        private View root;
        private ImageView cover;
        private TextView title;
        private TextView count;

        public Holder(View itemView) {
            super(itemView);
            root = itemView;
            cover = (ImageView) root.findViewById(R.id.cover);
            title = (TextView) root.findViewById(R.id.title);
            count = (TextView) root.findViewById(R.id.count);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<Holder> {
        int size;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(FolderAct.this)
                            .inflate(R.layout.gloomy_item_folder, parent, false);
            if (size == 0) {
                size = Utils.getScreenWidth(FolderAct.this) / 2 - 28;
            }

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = size;
            layoutParams.width = size;
            view.setLayoutParams(layoutParams);

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {

            Glide.with(FolderAct.this)
                    .load(infos.get(position).getFirstImagePath())
                    .centerCrop()
                    .into(holder.cover);

            holder.title.setText(infos.get(position).getName());

            holder.count.setText(infos.get(position).getCount() + " 张");

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIManager.getInstance().startPre(FolderAct.this, infos.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return infos == null ? 0 : infos.size();
        }
    }

    @Override
    protected void onDestroy() {
        UIManager.getInstance().activities.remove(this);
        super.onDestroy();
    }
}
