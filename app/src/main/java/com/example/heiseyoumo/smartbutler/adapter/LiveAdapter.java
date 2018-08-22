package com.example.heiseyoumo.smartbutler.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.ui.LiveTvActivity;

import java.util.List;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

    private List<String> mNameList;
    private List<String> mUrlList;
//    private OnItemClickListener onClickListener;

    public LiveAdapter(List<String> mNameList, List<String> mUrlList) {
        this.mNameList = mNameList;
        this.mUrlList = mUrlList;
    }

    @NonNull
    @Override
    public LiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mLiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(v.getContext(), LiveTvActivity.class);
                intent.putExtra("url", mUrlList.get(position));
                intent.putExtra("title", mNameList.get(position));
                v.getContext().startActivity(intent);
            }
        });
//        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveAdapter.ViewHolder holder, int position) {
        holder.mLiveTv.setText(mNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mLiveTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mLiveTv = (TextView) itemView.findViewById(R.id.mLiveTv);
        }
    }


//    //将点击事件转移给外面的调用者
//    @Override
//    public void onClick(View view) {
//        if (onClickListener != null) {
//            onClickListener.onItemClick(view, (int) view.getTag());
//        }
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    //最后暴露给外面的调用者，定义一个设置Listener的方法():
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.onClickListener = listener;
//    }
}
