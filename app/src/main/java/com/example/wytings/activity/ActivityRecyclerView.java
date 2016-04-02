package com.example.wytings.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wytings.R;
import com.example.wytings.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rex on 2016/3/13.
 * https://github.com/wytings
 */
public class ActivityRecyclerView extends BaseActivity {

    @Override
    protected void initialize() {

        RecyclerView recyclerView = new RecyclerView(this);
        setExtraContent(recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyRecyclerAdapter());
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private int GROUP = 0;
        private int CHILD = 1;

        private List<Pair<String, String>> pairs = new ArrayList<>();

        public MyRecyclerAdapter() {
            for (int i = 10; i < 50; i++) {
                pairs.add(Pair.create("Text " + i, "Button " + i));
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyLog.d("viewType=" + viewType);
            if (viewType == GROUP) {
                return new MyViewHolder(new Button(getActivity()), viewType);
            } else if (viewType == CHILD) {
                return new MyViewHolder(View.inflate(getActivity(), R.layout.recycler_view_item, null), viewType);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Pair<String, String> pair = pairs.get(position);
            if (holder.type == GROUP) {
                holder.button.setText(pair.second);
            } else if (holder.type == CHILD) {
                holder.textView.setText(pair.first);
                holder.button.setText(pair.second);
            }
        }

        @Override
        public int getItemCount() {
            return pairs.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 10 == 0) {
                return GROUP;
            } else {
                return CHILD;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            int type;
            TextView textView;
            Button button;

            public MyViewHolder(View itemView, int viewType) {
                super(itemView);
                type = viewType;
                if (viewType == GROUP) {
                    button = (Button) itemView;
                    itemView.setOnClickListener(this);
                } else if (viewType == CHILD) {
                    textView = ButterKnife.findById(itemView, R.id.text);
                    button = ButterKnife.findById(itemView, R.id.button);
                    button.setOnClickListener(this);
                }
            }

            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    showToast(((Button) v).getText().toString());
                } else {
                    showToast("not a button");
                }
            }
        }
    }
}
