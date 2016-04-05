package com.example.wytings.activity;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wytings.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rex on 2016/3/13.
 * https://github.com/wytings
 */
public class ActivityRecyclerView extends BaseActivity {

    @Override
    protected void initialize() {

        final RecyclerView recyclerView = new RecyclerView(this);
        setExtraContent(recyclerView);
        
        final MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(null);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                if (source.getItemViewType() != target.getItemViewType()) {
                    return false;
                }
                int fromPosition = source.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(myRecyclerAdapter.getDataList(), fromPosition, toPosition);
                myRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                myRecyclerAdapter.getDataList().remove(position);
                myRecyclerAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private List<String> list = new ArrayList<>();

        public List<String> getDataList() {
            return list;
        }

        public MyRecyclerAdapter(List<String> data) {
            if (data == null) {
                for (int i = 0; i < 50; i++) {
                    list.add("This is No." + i + " Item.");
                }
            } else {
                list = data;
            }

        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(View.inflate(getBaseActivity(), R.layout.recycler_view_item, null));

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
