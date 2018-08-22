package com.example.heiseyoumo.smartbutler.adapter;

/*
    PictureFragment的适配器
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.entity.Fruit;
import com.example.heiseyoumo.smartbutler.ui.FruitActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private Context mContext;
    private List<Fruit> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView mFruitIv;
        private TextView mFruitNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            mFruitIv = (ImageView) itemView.findViewById(R.id.mFruitIv);
            mFruitNameTv = (TextView) itemView.findViewById(R.id.mFruitNameTv);
        }
    }

    public PictureAdapter(Context mContext, List<Fruit> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public PictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item,
                parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruitCard = mList.get(position);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruitCard.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruitCard.getImgId());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureAdapter.ViewHolder holder, int position) {
        Fruit fruit = mList.get(position);
        Picasso.with(mContext).load(fruit.getImgId()).into(holder.mFruitIv);
        holder.mFruitNameTv.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
