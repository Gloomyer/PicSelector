package com.gloomyer.ui;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * 图片预览文件夹
 */

public class PreAct extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

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
        mToolbar.setTitle("图片预览");
        Utils.refresh(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 8;
                outRect.right = 8;
                outRect.left = 8;
                outRect.top = 8;
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

    private class Holder extends RecyclerView.ViewHolder {

        private View root;
        private ImageView image;
        private TextView selected;

        public Holder(View itemView) {
            super(itemView);
            root = itemView;
            image = (ImageView) root.findViewById(R.id.image);
            selected = (TextView) root.findViewById(R.id.selected);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<Holder> {
        int size;

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(PreAct.this)
                            .inflate(R.layout.gloomy_item_preview, parent, false);
            if (size == 0) {
                size = Utils.getScreenWidth(PreAct.this) / 4 - 16;
            }

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = size;
            layoutParams.width = size;
            view.setLayoutParams(layoutParams);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {

            final String path = UIManager.getInstance().preList.get(position);
            Glide.with(PreAct.this)
                    .load(path)
                    .centerCrop()
                    .into(holder.image);

            final int index = UIManager.getInstance().select.indexOf(path);
            holder.selected.setText(index == -1 ? "" : (index + 1 + ""));
            if (index == -1) {
                //未选中
                holder.selected.setBackgroundResource(R.drawable.gloomy_unselected);
            } else {
                holder.selected.setBackgroundResource(R.drawable.gloomy_selected);
            }

            if (UIManager.getInstance().autoSave) {
                //自动保存
                holder.selected.setVisibility(View.INVISIBLE);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIManager
                                .getInstance()
                                .select
                                .add(path);
                        UIManager.getInstance().save(PreAct.this);
                    }
                });
                return;
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index != -1) {
                        //已经选择过了
                        //取消选择
                        UIManager.getInstance().select.remove(index);
                        notifyDataSetChanged();
                        Utils.refresh(mToolbar);
                        return;
                    }

                    if (UIManager.getInstance().select.size()
                            >= UIManager.getInstance().totalCount) {
                        Toast.makeText(PreAct.this,
                                "已经满了不能再选择了!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UIManager
                            .getInstance()
                            .select
                            .add(path);
                    notifyDataSetChanged();
                    Utils.refresh(mToolbar);
                }
            };
            if (UIManager.getInstance().mImageClickListener != null) {
                holder.selected.setOnClickListener(listener);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIManager.getInstance().mImageClickListener.onClick(path);
                    }
                });
            } else {
                holder.root.setOnClickListener(listener);
            }
        }

        @Override
        public int getItemCount() {
            return UIManager.getInstance().getPreCount();
        }
    }

    @Override
    protected void onDestroy() {
        UIManager.getInstance().activities.remove(this);
        super.onDestroy();
    }
}
