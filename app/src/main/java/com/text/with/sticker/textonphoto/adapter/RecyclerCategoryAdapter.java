package com.text.with.sticker.textonphoto.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.activity.QuotesListActivity;
import com.text.with.sticker.textonphoto.utility.CategoryInfo;



import java.util.ArrayList;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.ViewHolder> {
    ArrayList<CategoryInfo> categoryInfoList;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.cardview);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    public RecyclerCategoryAdapter(Context context2, ArrayList<CategoryInfo> categoryInfoList2) {
        this.context = context2;
        this.categoryInfoList = categoryInfoList2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        return (long) position;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.categoryInfoList.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(this.context).load(Integer.valueOf(this.context.getResources().getIdentifier(this.categoryInfoList.get(position).getCATEGORY_DRAWABLE(), "drawable", this.context.getPackageName()))).placeholder((int) R.drawable.no_image).error((int) R.drawable.no_image).into(viewHolder.imageView);
        viewHolder.tvTitle.setText(this.categoryInfoList.get(position).getCATEGORY_NAME());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            /* class com.textonphoto.customqoutescreator.adapter.RecyclerCategoryAdapter.AnonymousClass1 */

            public void onClick(View v) {
                Intent quotesListActivity = new Intent(RecyclerCategoryAdapter.this.context, QuotesListActivity.class);
                quotesListActivity.putExtra("categoryId", RecyclerCategoryAdapter.this.categoryInfoList.get(position).getCATEGORY_ID());
                quotesListActivity.putExtra("categoryName", RecyclerCategoryAdapter.this.categoryInfoList.get(position).getCATEGORY_NAME());
                quotesListActivity.putExtra("searchString", "");
                RecyclerCategoryAdapter.this.context.startActivity(quotesListActivity);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        return new ViewHolder(LayoutInflater.from(arg0.getContext()).inflate(R.layout.row_layout_category, arg0, false));
    }
}
